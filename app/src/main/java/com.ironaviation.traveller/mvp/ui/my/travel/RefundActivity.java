package com.ironaviation.traveller.mvp.ui.my.travel;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.content.ContextCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.gson.Gson;
import com.ironaviation.traveller.R;
import com.ironaviation.traveller.app.utils.PriceUtil;
import com.ironaviation.traveller.common.AppComponent;
import com.ironaviation.traveller.common.WEActivity;
import com.ironaviation.traveller.di.component.my.travel.DaggerRefundComponent;
import com.ironaviation.traveller.di.module.my.travel.RefundModule;
import com.ironaviation.traveller.mvp.constant.Constant;
import com.ironaviation.traveller.mvp.contract.my.travel.RefundContract;
import com.ironaviation.traveller.mvp.model.api.Api;
import com.ironaviation.traveller.mvp.model.entity.Ext;
import com.ironaviation.traveller.mvp.model.entity.PayInfo;
import com.ironaviation.traveller.mvp.model.entity.response.RouteStateResponse;
import com.ironaviation.traveller.mvp.presenter.my.travel.RefundPresenter;
import com.ironaviation.traveller.mvp.ui.webview.WebViewActivity;
import com.ironaviation.traveller.mvp.ui.widget.AutoToolbar;
import com.ironaviation.traveller.mvp.ui.widget.FontTextView;
import com.jess.arms.utils.UiUtils;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

import static com.jess.arms.utils.Preconditions.checkNotNull;

/**
 * 通过Template生成对应页面的MVP和Dagger代码,请注意输入框中输入的名字必须相同
 * 由于每个项目包结构都不一定相同,所以每生成一个文件需要自己导入import包名,可以在设置中设置自动导入包名
 * 请在对应包下按以下顺序生成对应代码,Contract->Model->Presenter->Activity->Module->Component
 * 因为生成Activity时,Module和Component还没生成,但是Activity中有它们的引用,所以会报错,但是不用理会
 * 继续将Module和Component生成完后,编译一下项目再回到Activity,按提示修改一个方法名即可
 * 如果想生成Fragment的相关文件,则将上面构建顺序中的Activity换为Fragment,并将Component中inject方法的参数改为此Fragment
 */

/**
 * 退款明细
 * Created by Administrator on 2017/4/12 0012.
 */

public class RefundActivity extends WEActivity<RefundPresenter> implements RefundContract.View {


    @BindView(R.id.tw_go_back_money)
    FontTextView mTwGoBackMoney;
    @BindView(R.id.tw_order_no)
    TextView mTwOrderNo;
    @BindView(R.id.tw_payment_money)
    TextView mTwPaymentMoney;
    @BindView(R.id.tw_appointment_money)
    TextView mTwAppointmentMoney;
    @BindView(R.id.tw_back_money)
    TextView mTwBackMoney;
    @BindView(R.id.tw_payment_date)
    TextView mTwPaymentDate;
    @BindView(R.id.tw_check_rule)
    TextView mTwCheckRule;
    private RouteStateResponse data;

    @Override
    protected void setupActivityComponent(AppComponent appComponent) {
        DaggerRefundComponent
                .builder()
                .appComponent(appComponent)
                .refundModule(new RefundModule(this)) //请将RefundModule()第一个首字母改为小写
                .build()
                .inject(this);
    }

    @Override
    protected View initView() {
        return LayoutInflater.from(this).inflate(R.layout.activity_check_details, null, false);
    }

    @Override
    protected void initData() {
        initTitle();
        Intent intent = getIntent();
        data = (RouteStateResponse) intent.getSerializableExtra(Constant.STATUS);
        if(data != null){
            setInfo(data);
        }
    }

    public void setData(RouteStateResponse data){
        /*mTwGoBackMoney.setTextType(data);
        mTwOrderNo*/
    }

    public void initTitle(){
        setTitle(getString(R.string.refund_Amount));
        mToolbar.setNavigationIcon(ContextCompat.getDrawable(this, R.mipmap.ic_back));
        mToolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    public void setInfo(RouteStateResponse info){
        List<Ext> Ext= info.getExt();
        PayInfo payInfo = null;
        if (Ext != null && Ext.size() != 0) {
            for (int i = 0; i < Ext.size(); i++) {
                if (Ext.get(i).getName().equals(Constant.CLASS_PAY_INFO)) {
                    try {
                        payInfo = new Gson().fromJson(Ext.get(i).getJsonData(),PayInfo.class);
                    }catch (Exception e){
                        //友盟
                    }
                }
            }
        }
        mTwGoBackMoney.setTextType(PriceUtil.getPrecent(payInfo.getRebate()));
        mTwOrderNo.setText(info.getOrderNo() != null ? info.getOrderNo():"");
        mTwPaymentMoney.setText(PriceUtil.getPrecent(payInfo.getAmount()));
        mTwAppointmentMoney.setText(PriceUtil.getPrecent(payInfo.getAmount()-payInfo.getRebate()));
        mTwBackMoney.setText(PriceUtil.getPrecent(payInfo.getRebate()));
    }

    @Override
    public void showLoading() {

    }

    @Override
    public void hideLoading() {

    }

    @Override
    public void showMessage(@NonNull String message) {
        checkNotNull(message);
        UiUtils.SnackbarText(message);
    }

    @Override
    public void launchActivity(@NonNull Intent intent) {
        checkNotNull(intent);
        UiUtils.startActivity(intent);
    }

    @Override
    public void killMyself() {
        finish();
    }


    @Override
    protected void nodataRefresh() {

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // TODO: add setContentView(...) invocation
        ButterKnife.bind(this);
    }

    @OnClick(R.id.tw_check_rule)
    public void onClick(View view){
        switch (view.getId()){
            case R.id.tw_check_rule:
                if(data != null) {
                    String status = data.getTripType();
                    Intent intent = new Intent(this, WebViewActivity.class);
                    if (status.equals(Constant.CLEAR_PORT)) { //送机
                        intent.putExtra(Constant.TITLE, getResources().getString(R.string.travel_enter_port));
                        intent.putExtra(Constant.URL, Api.PHONE_CANCEL_ROLE_OFF);
                    } else if (status.equals(Constant.ENTER_PORT)) { //接机
                        intent.putExtra(Constant.TITLE, getResources().getString(R.string.travel_clear_port));
                        intent.putExtra(Constant.URL, Api.PHONE_CANCEL_ROLE_ON);
                    }
                    startActivity(intent);
                }
            break;
        }
    }
}
