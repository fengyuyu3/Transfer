package com.ironaviation.traveller.mvp.ui.payment;

import android.content.Intent;
import android.os.Bundle;
import android.provider.SyncStateContract;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.ironaviation.traveller.R;
import com.ironaviation.traveller.common.AppComponent;
import com.ironaviation.traveller.common.WEActivity;
import com.ironaviation.traveller.di.component.payment.DaggerInvalidationComponent;
import com.ironaviation.traveller.di.module.payment.InvalidationModule;
import com.ironaviation.traveller.mvp.constant.Constant;
import com.ironaviation.traveller.mvp.contract.payment.InvalidationContract;
import com.ironaviation.traveller.mvp.model.entity.response.RouteStateResponse;
import com.ironaviation.traveller.mvp.presenter.payment.InvalidationPresenter;
import com.ironaviation.traveller.mvp.ui.widget.AutoToolbar;
import com.ironaviation.traveller.mvp.ui.widget.TextTextView;
import com.jess.arms.utils.UiUtils;

import butterknife.BindView;
import butterknife.ButterKnife;

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
 * Created by Administrator on 2017/4/13 0013.
 */

public class InvalidationActivity extends WEActivity<InvalidationPresenter> implements InvalidationContract.View {


    @BindView(R.id.iv_function_left)
    ImageView mIvFunctionLeft;
    @BindView(R.id.tv_title)
    TextView mTvTitle;
    @BindView(R.id.tv_function_right)
    TextView mTvFunctionRight;
    @BindView(R.id.iv_function_right)
    ImageView mIvFunctionRight;
    @BindView(R.id.toolbar)
    AutoToolbar mToolbar;
    @BindView(R.id.tv_payment_amount)
    TextView mTvPaymentAmount;
    @BindView(R.id.tv_unit)
    TextView mTvUnit;
    @BindView(R.id.tt_order_number)
    TextTextView mTtOrderNumber;
    @BindView(R.id.tt_get_on_the_bus_address)
    TextTextView mTtGetOnTheBusAddress;
    @BindView(R.id.tt_get_off_the_bus_address)
    TextTextView mTtGetOffTheBusAddress;
    @BindView(R.id.tt_need_seats)
    TextTextView mTtNeedSeats;

    private RouteStateResponse responses;
    private String bid;

    @Override
    protected void setupActivityComponent(AppComponent appComponent) {
        DaggerInvalidationComponent
                .builder()
                .appComponent(appComponent)
                .invalidationModule(new InvalidationModule(this)) //请将InvalidationModule()第一个首字母改为小写
                .build()
                .inject(this);
    }

    @Override
    protected View initView() {
        return LayoutInflater.from(this).inflate(R.layout.activity_invalidation, null, false);
    }

    @Override
    protected void initData() {
        Bundle bundle = getIntent().getExtras();
        responses = (RouteStateResponse) bundle.getSerializable(Constant.STATUS);
        if(responses != null){
            setData(responses);
        }else{
            String bid = bundle.getString(Constant.BID);
            if(bid != null) {
                mPresenter.getRouteStateInfo(bid);
            }
        }
    }

    public void setData(RouteStateResponse responses){
        mTtOrderNumber.setText(responses.getOrderNo());
        mTtGetOnTheBusAddress.setText(responses.getPickupAddress());
        mTtGetOffTheBusAddress.setText(responses.getDestAddress());
        mTtNeedSeats.setText(responses.getSeatNum()+"");
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
    public void setRouteStateData(RouteStateResponse responses) {
        setData(responses);
    }
}
