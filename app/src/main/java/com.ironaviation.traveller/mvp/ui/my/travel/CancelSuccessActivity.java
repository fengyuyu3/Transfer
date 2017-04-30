package com.ironaviation.traveller.mvp.ui.my.travel;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import com.alipay.sdk.auth.APAuthInfo;
import com.ironaviation.traveller.R;
import com.ironaviation.traveller.common.AppComponent;
import com.ironaviation.traveller.common.WEActivity;
import com.ironaviation.traveller.di.component.my.travel.DaggerCancelSuccessComponent;
import com.ironaviation.traveller.di.module.my.travel.CancelSuccessModule;
import com.ironaviation.traveller.mvp.constant.Constant;
import com.ironaviation.traveller.mvp.contract.my.travel.CancelSuccessContract;
import com.ironaviation.traveller.mvp.model.api.Api;
import com.ironaviation.traveller.mvp.model.entity.response.CancelSuccessResponse;
import com.ironaviation.traveller.mvp.model.entity.response.RouteStateResponse;
import com.ironaviation.traveller.mvp.model.entity.response.TravelCancelReason;
import com.ironaviation.traveller.mvp.presenter.my.travel.CancelSuccessPresenter;
import com.ironaviation.traveller.mvp.ui.my.adapter.CancelSuccessAdapter;
import com.ironaviation.traveller.mvp.ui.webview.WebViewActivity;
import com.ironaviation.traveller.mvp.ui.widget.FontTextView;
import com.ironaviation.traveller.mvp.ui.widget.SpaceItemDecoration;
import com.jess.arms.utils.UiUtils;
import com.zhy.autolayout.AutoLinearLayout;
import com.zhy.autolayout.AutoRelativeLayout;

import java.util.ArrayList;
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
 * 项目名称：Traveller
 * 类描述：
 * 创建人：starRing
 * 创建时间：2017-04-05 14:29
 * 修改人：starRing
 * 修改时间：2017-04-05 14:29
 * 修改备注：
 */
public class CancelSuccessActivity extends WEActivity<CancelSuccessPresenter> implements CancelSuccessContract.View {


    @BindView(R.id.rv_cancel_success)
    RecyclerView mRvCancelSuccess;

    @BindView(R.id.tv_responsibility)
    TextView mTvResponsibility;

    @BindView(R.id.it_driver_name)
    TextView mItDriverName;
    @BindView(R.id.it_driver_grade)
    TextView mItDriverGrade;
    @BindView(R.id.tv_money)
    FontTextView mTvMoney;
    @BindView(R.id.ic_car_license)
    TextView mIcCarLicense;
    @BindView(R.id.rl_head)
    AutoRelativeLayout mRlHead;
    @BindView(R.id.rl_driver)
    AutoLinearLayout mRlDriver;
    @BindView(R.id.rl_money)
    AutoRelativeLayout mRlMoney;
    @BindView(R.id.tv_money_title)
    TextView mTvMoneyTitle;
    @BindView(R.id.tv_other_reason)
    TextView mTvOtherReason;
    @BindView(R.id.rv_other_reason)
    AutoRelativeLayout mRvOtherReason;
    @BindView(R.id.rl_penal_sum)
    AutoRelativeLayout mRlPenalSum;
    @BindView(R.id.tw_cancel_rules)
    TextView mTwCancelRules;

    private String[] cancel_reasons;
    private List<CancelSuccessResponse> mTravelCancelResponseList = new ArrayList<>();
    private RecyclerView.LayoutManager mLayoutManager;

    private CancelSuccessAdapter mCancelSuccessAdapter;
    private RouteStateResponse data;
    private String status;


    @Override
    protected void setupActivityComponent(AppComponent appComponent) {
        DaggerCancelSuccessComponent
                .builder()
                .appComponent(appComponent)
                .cancelSuccessModule(new CancelSuccessModule(this)) //请将CancelSuccessModule()第一个首字母改为小写
                .build()
                .inject(this);
    }

    @Override
    protected View initView() {
        return LayoutInflater.from(this).inflate(R.layout.activity_cancel_success, null, false);
    }

    @Override
    protected void initData() {

        setTitle(getString(R.string.travel_cancel));
        mToolbar.setNavigationIcon(ContextCompat.getDrawable(this, R.mipmap.ic_back));
        mToolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        //GridLayout 3列
        GridLayoutManager mgr = new GridLayoutManager(this, 2);
        mRvCancelSuccess.addItemDecoration(new SpaceItemDecoration(20));
        mCancelSuccessAdapter = new CancelSuccessAdapter(R.layout.item_cancel_success);
        mRvCancelSuccess.setLayoutManager(mgr);
        mRvCancelSuccess.setAdapter(mCancelSuccessAdapter);
        getData();

    }
    public void getData(){
        Bundle pBundle = getIntent().getExtras();
        if (pBundle.getSerializable(Constant.STATUS) != null) {
            data = (RouteStateResponse) pBundle.getSerializable(Constant.STATUS);
            status = data.getTripType();
            mPresenter.getRouteStateInfo(data);
        } else if (!TextUtils.isEmpty(pBundle.getString(Constant.BID))) {
            mPresenter.getRouteStateInfo(pBundle.getString(Constant.BID));
            if(!TextUtils.isEmpty(pBundle.getString(Constant.CANCEL))){
                status = pBundle.getString(Constant.CANCEL);
            }
        }
    }

    @Override
    public void showLoading() {
        showProgressDialog();
    }

    @Override
    public void hideLoading() {
        dismissProgressDialog();
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

    @Override
    public void setReasonView(List<TravelCancelReason> strings, String otherReason) {
        if (strings != null) {
            mCancelSuccessAdapter.setNewData(strings);
        }
        if (!TextUtils.isEmpty(otherReason)) {
            mRvOtherReason.setVisibility(View.VISIBLE);
            mTvOtherReason.setText(otherReason);
        } else {
            mRvOtherReason.setVisibility(View.GONE);
        }
    }

    @Override
    public void setDriverName(String name) {
        mRlDriver.setVisibility(View.VISIBLE);
        mItDriverName.setText(name);

    }

    @Override
    public void setDriverRate(String rate) {
        mItDriverGrade.setText(rate);

    }

    @Override
    public void setCarLicense(String license) {

        mRlHead.setVisibility(View.VISIBLE);
        mIcCarLicense.setText(license);
    }

    @Override
    public void setMoneyView(String money) {
        mRlMoney.setVisibility(View.VISIBLE);
        mTvMoneyTitle.setVisibility(View.VISIBLE);
        mTvMoney.setTextType(money);
    }

    @Override
    public void setResponsibilityView(String responsibility) {
        mTvResponsibility.setText(responsibility);
    }

    @Override
    public void setRespons(RouteStateResponse data) {
        this.data = data;
    }

    @OnClick({R.id.tw_cancel_rules,R.id.tv_money})
    public void onClick(View view){
        switch (view.getId()){
            case R.id.tw_cancel_rules: //取消规则
                Intent intent = new Intent(this, WebViewActivity.class);
                if(status.equals(Constant.CLEAR_PORT)) { //送机
                    intent.putExtra(Constant.TITLE,getResources().getString(R.string.travel_enter_port));
                    intent.putExtra(Constant.URL, Api.PHONE_CANCEL_ROLE_OFF);
                }else if(status.equals(Constant.ENTER_PORT)){ //接机
                    intent.putExtra(Constant.TITLE,getResources().getString(R.string.travel_clear_port));
                    intent.putExtra(Constant.URL, Api.PHONE_CANCEL_ROLE_ON);
                }
                startActivity(intent);
                break;
            case R.id.tv_money:
                Intent intent1 = new Intent(this, RefundActivity.class);
                if(data != null) {
                    intent1.putExtra(Constant.STATUS,data);
                    startActivity(intent1);
                }
                break;
        }
    }

}
