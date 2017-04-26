package com.ironaviation.traveller.mvp.ui.my.travel;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.content.ContextCompat;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.ironaviation.traveller.R;
import com.ironaviation.traveller.app.EventBusTags;
import com.ironaviation.traveller.common.AppComponent;
import com.ironaviation.traveller.common.WEActivity;
import com.ironaviation.traveller.di.component.my.travel.DaggerTravelDetailsOnComponent;
import com.ironaviation.traveller.di.module.my.travel.TravelDetailsOnModule;
import com.ironaviation.traveller.mvp.constant.Constant;
import com.ironaviation.traveller.mvp.contract.my.travel.TravelDetailsOnContract;
import com.ironaviation.traveller.mvp.model.entity.response.RouteStateResponse;
import com.ironaviation.traveller.mvp.presenter.my.travel.TravelDetailsOnPresenter;
import com.ironaviation.traveller.mvp.ui.my.QRCodeActivity;
import com.ironaviation.traveller.mvp.ui.widget.AutoToolbar;
import com.ironaviation.traveller.mvp.ui.widget.MoreActionPopupWindow;
import com.jess.arms.utils.UiUtils;
import com.zhy.autolayout.AutoLinearLayout;
import com.zhy.autolayout.AutoRelativeLayout;

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
 * Created by Administrator on 2017/4/21.
 */

public class TravelDetailsOnActivity extends WEActivity<TravelDetailsOnPresenter> implements TravelDetailsOnContract.View
,View.OnClickListener{


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
    @BindView(R.id.id_line)
    View mIdLine;
    @BindView(R.id.tw_name_airport)
    TextView mTwNameAirport;
    @BindView(R.id.tw_score_airport)
    TextView mTwScoreAirport;
    @BindView(R.id.tw_car_num_airport)
    TextView mTwCarNumAirport;
    @BindView(R.id.tw_car_color_airport)
    TextView mTwCarColorAirport;
    @BindView(R.id.iw_mobile_airport)
    ImageView mIwMobileAirport;
    @BindView(R.id.ll_driver_info_airport)
    AutoLinearLayout mLlDriverInfoAirport;
    @BindView(R.id.iw_zoom_airport)
    ImageView mIwZoomAirport;
    @BindView(R.id.ll_complete_airport)
    AutoLinearLayout mLlCompleteAirport;
    @BindView(R.id.ll_ordering_airport)
    AutoLinearLayout mLlOrderingAirport;
    @BindView(R.id.tw_go_to_pay_airport) //确认上车 按钮
    TextView mTwGoToPayAirport;
    @BindView(R.id.ll_arrive_airport)                    //等待上车
    AutoLinearLayout mLlWaitAirport;
    @BindView(R.id.tw_go_on_pay_airport) //确认到达按钮
    TextView mTwGoOnPayAirport;
    @BindView(R.id.ll_getOn_airport)                      //确认到达
    AutoLinearLayout mLlArriveAirport;
    @BindView(R.id.ll_arrive_time_airport)                //进行中 司机已发车
    AutoLinearLayout mLlGoOnAirport;
    @BindView(R.id.textView)
    TextView mTextView;
    @BindView(R.id.tw_address) //查看地址
    TextView mTwAddress;
    @BindView(R.id.rl_successful)                         //预约成功
    AutoLinearLayout mRlSuccessful;
    @BindView(R.id.iw_zoom_nomal_airport)
    ImageView mIwZoomNomalAirport;
    @BindView(R.id.al_two_dimension) //二维码
    AutoRelativeLayout mDimension;

    @BindView(R.id.tw_title_on)     //进行中的 title
    TextView mTwTitleOn;
    @BindView(R.id.tw_text_on)  // 进行中的内容
    TextView mTwTextOn;
    private String status;
    private RouteStateResponse responses;
    private MoreActionPopupWindow mPopupWindow;

    @Override
    protected void setupActivityComponent(AppComponent appComponent) {
        DaggerTravelDetailsOnComponent
                .builder()
                .appComponent(appComponent)
                .travelDetailsOnModule(new TravelDetailsOnModule(this)) //请将TravelDetailsOnModule()第一个首字母改为小写
                .build()
                .inject(this);
    }

    @Override
    protected View initView() {
        return LayoutInflater.from(this).inflate(R.layout.activity_travel_details_airport, null, false);
    }

    @Override
    protected void initData() {
        initTitle();
        Bundle pBundle = getIntent().getExtras();
        if (pBundle != null) {
            responses = (RouteStateResponse) pBundle.getSerializable(Constant.STATUS);
            if (responses!=null&&!TextUtils.isEmpty(responses.getBID())){
                mPopupWindow = new MoreActionPopupWindow(this, EventBusTags.WAITING_PAYMENT,responses.getBID());
//                status = responses.getStatus();
                mPresenter.setRouteStateResponse(responses);
                /**
                 String REGISTERED = "Registered"; // 预约成功
                 String INHAND = "InHand";// 进行中
                 String ARRIVED = "Arrived";// 已到达
                 String CANCEL = "Cancel";// 已取消
                 String BOOKSUCCESS = "BookSuccess";// 派单成功
                 String COMPLETED = "Completed";// 已完成
                 String NOTPAID = "NotPaid"; //未支付
                 String INVALIDATION = "Invalidation"; //已失效
                 String WAIT_APPRAISE = "wait"; //等待评价
                 */
//                status = Constant.ARRIVED;
                showStatus(status);
            }
        }else{
//            mPresenter.getRouteState();
        }
    }

    public void initTitle(){
        setRightFunction(R.mipmap.ic_more, this);
        setTitle(getString(R.string.travel_detail));
        mToolbar.setNavigationIcon(ContextCompat.getDrawable(this, R.mipmap.ic_back));
        mToolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
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
    public void setDriverName(String name) {
        mTwNameAirport.setText(name);
    }

    @Override
    public void setDriverRate(String rate) {
        mTwScoreAirport.setText(rate);
    }

    @Override
    public void setDriverPhone(String phone) {
        Intent intent = new Intent(Intent.ACTION_DIAL);
        Uri data = Uri.parse("tel:" + phone);
        intent.setData(data);
        startActivity(intent);
    }

    @Override
    public void setCarLicense(String liscense) {
        mTwCarNumAirport.setText(liscense);
    }

    @Override
    public void setCarColor(String carColor, String model) {
        mTwCarColorAirport.setText(carColor+"."+model);
    }

    @Override
    public void setStatus(String status) {
        this.status = status;
    }

    public void waitPickUp(){  //等待上车
        mLlDriverInfoAirport.setVisibility(View.VISIBLE);
        mIwZoomAirport.setVisibility(View.VISIBLE);
        mIwZoomNomalAirport.setVisibility(View.GONE);
        mLlWaitAirport.setVisibility(View.VISIBLE);   //等待上车
        mLlArriveAirport.setVisibility(View.GONE); //确认到达
        mLlGoOnAirport.setVisibility(View.GONE);   //进行中
        mRlSuccessful.setVisibility(View.GONE);    //预约成功
    }

    public void arrive(){   //确认到达
        mLlDriverInfoAirport.setVisibility(View.VISIBLE);
        mIwZoomAirport.setVisibility(View.VISIBLE);
        mIwZoomNomalAirport.setVisibility(View.GONE);
        mLlWaitAirport.setVisibility(View.GONE);   //等待上车
        mLlArriveAirport.setVisibility(View.VISIBLE); //确认到达
        mLlGoOnAirport.setVisibility(View.GONE);   //进行中
        mRlSuccessful.setVisibility(View.GONE);    //预约成功
    }

    public void GoOn(){     //进行中 司机已发车
        mLlDriverInfoAirport.setVisibility(View.VISIBLE);
        mIwZoomAirport.setVisibility(View.VISIBLE);
        mIwZoomNomalAirport.setVisibility(View.GONE);
        mLlWaitAirport.setVisibility(View.GONE);   //等待上车
        mLlArriveAirport.setVisibility(View.GONE); //确认到达
        mLlGoOnAirport.setVisibility(View.VISIBLE);   //进行中
        mRlSuccessful.setVisibility(View.GONE);    //预约成功
        mTwTitleOn.setText(getResources().getString(R.string.travel_go_on_train));
        mTwTextOn.setText(getResources().getString(R.string.travel_go_on_arrive_time));
    }

    public void aready(){     //进行中  已上车
        mLlDriverInfoAirport.setVisibility(View.VISIBLE);
        mIwZoomAirport.setVisibility(View.VISIBLE);
        mIwZoomNomalAirport.setVisibility(View.GONE);
        mLlWaitAirport.setVisibility(View.GONE);   //等待上车
        mLlArriveAirport.setVisibility(View.GONE); //确认到达
        mLlGoOnAirport.setVisibility(View.VISIBLE);   //进行中
        mRlSuccessful.setVisibility(View.GONE);    //预约成功
        mTwTitleOn.setText(getResources().getString(R.string.travel_go_on_car));
        mTwTextOn.setText(getResources().getString(R.string.travel_go_on_wait_driver));
    }

    public void success(){  //预约成功
        mLlDriverInfoAirport.setVisibility(View.GONE);
        mIwZoomAirport.setVisibility(View.VISIBLE);
        mIwZoomNomalAirport.setVisibility(View.GONE);
        mLlWaitAirport.setVisibility(View.GONE);   //等待上车
        mLlArriveAirport.setVisibility(View.GONE); //确认到达
        mLlGoOnAirport.setVisibility(View.GONE);   //进行中
        mRlSuccessful.setVisibility(View.VISIBLE);    //预约成功
    }

    public void AllGone(){
        mIwZoomAirport.setVisibility(View.GONE);
        mIwZoomNomalAirport.setVisibility(View.VISIBLE);
        mLlWaitAirport.setVisibility(View.GONE);   //等待上车
        mLlArriveAirport.setVisibility(View.GONE); //确认到达
        mLlGoOnAirport.setVisibility(View.GONE);   //进行中
        mRlSuccessful.setVisibility(View.GONE);    //预约成功
    }

    public void showStatus(String status){
        switch (status){
            case Constant.REGISTERED:
                success();
            break;
            case Constant.BOOKSUCCESS:
                waitPickUp();
            break;
            case Constant.ARRIVED:
                arrive();
            break;
            default:
                showChildStatus(responses.getChildStatus());
            break;
        }
    }

    public void showChildStatus(String status){
        switch (status){
            case Constant.ABORAD:
                aready();
                break;
            default:
                showOrderStatus(responses.getOrderStatus());
                break;
        }
    }
    public void showOrderStatus(String status){
        switch (status){
            case Constant.INHAND:
                GoOn();
                break;
        }
    }
    @OnClick({R.id.tw_go_on_pay_airport,R.id.tw_go_to_pay_airport,R.id.al_two_dimension
    ,R.id.tw_address})
    public void myOnClick(View view){
        switch (view.getId()){
            case R.id.tw_go_on_pay_airport: //确认到达按钮
                break;
            case R.id.tw_go_to_pay_airport: //确认上车按钮
                break;
            case R.id.al_two_dimension:     //二维码

                break;
            case R.id.tw_address:           //查看地址
                break;
            case R.id.iw_zoom_nomal_airport: //隐藏

                break;
        }
    }
    public void twoDimenSion(){
        String orderOn = responses.getOrderNo();
        Intent intent = new Intent(this, QRCodeActivity.class);

    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.iv_function_right:
                if (mPopupWindow != null) {
                    mPopupWindow.showPopupWindow(mIdLine);
                }
                break;
        }
    }
}
