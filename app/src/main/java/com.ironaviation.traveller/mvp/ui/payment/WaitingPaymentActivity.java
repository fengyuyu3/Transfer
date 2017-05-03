package com.ironaviation.traveller.mvp.ui.payment;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.content.ContextCompat;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.ironaviation.traveller.R;
import com.ironaviation.traveller.app.utils.CountTimeMiniteUtil;
import com.ironaviation.traveller.app.utils.TimerUtils;
import com.ironaviation.traveller.app.EventBusTags;
import com.ironaviation.traveller.common.AppComponent;
import com.ironaviation.traveller.common.WEActivity;
import com.ironaviation.traveller.di.component.payment.DaggerWaitingPaymentComponent;
import com.ironaviation.traveller.di.module.payment.WaitingPaymentModule;
import com.ironaviation.traveller.mvp.constant.Constant;
import com.ironaviation.traveller.event.TravelCancelEvent;
import com.ironaviation.traveller.mvp.contract.payment.WaitingPaymentContract;
import com.ironaviation.traveller.mvp.model.entity.BasePushData;
import com.ironaviation.traveller.mvp.model.entity.response.RouteStateResponse;
import com.ironaviation.traveller.mvp.model.entity.response.WeChaTInfo;
import com.ironaviation.traveller.mvp.presenter.payment.WaitingPaymentPresenter;
import com.ironaviation.traveller.mvp.ui.my.travel.TravelActivity;
import com.ironaviation.traveller.mvp.ui.widget.AutoToolbar;
import com.ironaviation.traveller.mvp.ui.widget.FontTextView;
import com.ironaviation.traveller.mvp.ui.my.travel.TravelCancelActivity;
import com.ironaviation.traveller.mvp.ui.widget.ImageTextImageView;
import com.ironaviation.traveller.mvp.ui.widget.TextTextView;
import com.ironaviation.traveller.mvp.ui.widget.MoreActionPopupWindow;
import com.jess.arms.utils.UiUtils;

import org.simple.eventbus.EventBus;
import org.simple.eventbus.Subscriber;

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
 * 创建时间：2017-04-10 14:53
 * 修改人：starRing
 * 修改时间：2017-04-10 14:53
 * 修改备注：
 */
public class WaitingPaymentActivity extends WEActivity<WaitingPaymentPresenter> implements WaitingPaymentContract.View, View.OnClickListener {

    @BindView(R.id.ivi_we_chat)
    ImageTextImageView mIviWeChat;
    @BindView(R.id.ivi_ali_pay)
    ImageTextImageView mIviAliPay;
    @BindView(R.id.ivi_union_pay)
    ImageTextImageView mIviUnionPay;
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
    @BindView(R.id.tv_money)
    FontTextView mTvMoney;
    @BindView(R.id.tv_unit)
    TextView mTvUnit;
    @BindView(R.id.tv_money_unit)
    TextView mTvMoneyUnit;
    @BindView(R.id.tv_tt_title)
    TextView mTvTtTitle;
    @BindView(R.id.tt_order_number)
    TextTextView mTtOrderNumber;
    @BindView(R.id.tt_passengers_telephone)
    TextTextView mTtPassengersTelephone;
    @BindView(R.id.v_1)
    View mV1;
    @BindView(R.id.tt_flight_number)
    TextTextView mTtFlightNumber;
    @BindView(R.id.tt_riding_time)
    TextTextView mTtRidingTime;
    @BindView(R.id.tt_get_on_the_bus_address)
    TextTextView mTtGetOnTheBusAddress;
    @BindView(R.id.tt_get_off_the_bus_address)
    TextTextView mTtGetOffTheBusAddress;
    @BindView(R.id.tt_need_seats)
    TextTextView mTtNeedSeats;
    @BindView(R.id.tv_determine_cancel)
    TextView mTvDetermineCancel;
    private String format = "yyyy/MM/dd HH:mm";
    private String status;
    private String bid;
    private RouteStateResponse responses;
    private String myStatus; //订单过来的状态
    private String orderStatus; //订单列表过来的状态
    private CountTimeMiniteUtil mCountTimeMiniteUtil;

    @BindView(R.id.id_line)
    View idLine;
    private MoreActionPopupWindow mPopupWindow;

    @Override
    protected void setupActivityComponent(AppComponent appComponent) {
        DaggerWaitingPaymentComponent
                .builder()
                .appComponent(appComponent)
                .waitingPaymentModule(new WaitingPaymentModule(this)) //请将WaitingPaymentModule()第一个首字母改为小写
                .build()
                .inject(this);
    }

    @Override
    protected View initView() {
        return LayoutInflater.from(this).inflate(R.layout.activity_waiting_payment, null, false);
    }

    @Override
    protected void initData() {
        initToolbar();
        bid =  getIntent().getStringExtra(Constant.BID);
        myStatus = getIntent().getStringExtra(Constant.STATUS);
        if(myStatus != null) {
            if (myStatus.equals(Constant.ON)) {
                mTtRidingTime.setVisibility(View.GONE);
            } else if (myStatus.equals(Constant.OFF)) {
                mTtRidingTime.setVisibility(View.VISIBLE);
            }
        }
        if(bid != null) {
            mPresenter.getRouteStateInfo(bid);
            mPopupWindow = new MoreActionPopupWindow(this, EventBusTags.WAITING_PAYMENT,bid);
        }else{
            Bundle pBundle = getIntent().getExtras();
            if (pBundle != null) {
                responses = (RouteStateResponse) pBundle.getSerializable(Constant.STATUS);
                if (responses!=null&&!TextUtils.isEmpty(responses.getBID())){
                    mPopupWindow = new MoreActionPopupWindow(this, EventBusTags.WAITING_PAYMENT,responses.getBID());
                    bid = responses.getBID();
                    mPresenter.setValue(responses);
//                    setResponsesData(responses);
                }
                orderStatus = pBundle.getString(Constant.CHILD_STATUS);
                if(orderStatus != null){
                    if (orderStatus.equals(Constant.ON)) {
                        mTtRidingTime.setVisibility(View.GONE);
                    } else if (orderStatus.equals(Constant.OFF)) {
                        mTtRidingTime.setVisibility(View.VISIBLE);
                    }
                }
                if(responses.getCurrentTime() != 0 && responses.getExpireAt() != 0){
                    /*if(responses.getPickupTime()-responses.getCdt()-4*60*60*1000 > 24*60*60*1000){
                        setCountTime(24*60*60*1000-(responses.getCurrentTime()-responses.getCdt()));
                    }else{
                        setCountTime(responses.getPickupTime()-responses.getCurrentTime()-2*60*60*1000);
                    }*/
                    if((responses.getExpireAt() - responses.getCurrentTime()) > 0){
                        setCountTime(responses.getExpireAt()-responses.getCurrentTime());
                    }else{
                        setCountTime(0);
                    }
                }
            }
        }
        setRightFunction(R.mipmap.ic_more, this);
    }

    public void initToolbar(){
        setTitle(getString(R.string.travel_wait_payment));
        mToolbar.setNavigationIcon(ContextCompat.getDrawable(this, R.mipmap.ic_back));
        mToolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        setToolbarColor(R.color.base_color);
    }
    public void setCountTime(long time){
        mCountTimeMiniteUtil = new CountTimeMiniteUtil(time,1000*60,mTvMoneyUnit);
        mCountTimeMiniteUtil.start();
    }

    /*public void setResponsesData(RouteStateResponse responses){
        setOrderNum(responses.getOrderNo());
        setMobile(responses.getPhone());
        setTravelNO(responses.getFlightNo());
        long time ;
        try{
            time = Long.parseLong(responses.getFlightDate());
        }catch (Exception e){
            time = 0;
        }
        setTime(time);
        setPickUpAddress(responses.getPickupAddress());
        setDestAddress(responses.getDestAddress());
        setSeatNum(responses.getSeatNum());
        setPrice(responses.getActualPrice());
//        setCountdown();
    }*/
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

    @OnClick({R.id.ivi_we_chat, R.id.ivi_ali_pay, R.id.ivi_union_pay,R.id.tv_determine_cancel})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.iv_function_right:
                if (mPopupWindow != null) {
                    mPopupWindow.showPopupWindow(idLine);
                }
                break;

            case R.id.ivi_we_chat:
               /* mIviWeChat.setGoOn(R.mipmap.ic_radio_button_checked_black);
                mIviAliPay.setGoOn(R.mipmap.ic_radio_button_unchecked_black);
                mIviUnionPay.setGoOn(R.mipmap.ic_radio_button_unchecked_black);*/
                mIviWeChat.show(true, R.mipmap.ic_pay_select);
                mIviAliPay.show(false, R.mipmap.ic_pay_select);
                mIviUnionPay.show(false, R.mipmap.ic_pay_select);
                mIviAliPay.setBackGround(ContextCompat.getColor(this, R.color.white));
                mIviWeChat.setBackGround(ContextCompat.getColor(this, R.color.id_cord_background));
                mIviUnionPay.setBackGround(ContextCompat.getColor(this, R.color.white));
                status = Constant.WECHAT;
                break;
            case R.id.ivi_ali_pay:
                /*mIviAliPay.setGoOn(R.mipmap.ic_radio_button_checked_black);
                mIviWeChat.setGoOn(R.mipmap.ic_radio_button_unchecked_black);
                mIviUnionPay.setGoOn(R.mipmap.ic_radio_button_unchecked_black);*/
                mIviAliPay.show(true, R.mipmap.ic_pay_select);
                mIviWeChat.show(false, R.mipmap.ic_pay_select);
                mIviUnionPay.show(false, R.mipmap.ic_pay_select);
                mIviAliPay.setBackGround(ContextCompat.getColor(this, R.color.id_cord_background));
                mIviWeChat.setBackGround(ContextCompat.getColor(this, R.color.white));
                mIviUnionPay.setBackGround(ContextCompat.getColor(this, R.color.white));
                status = Constant.ALIPAY;
                break;
            case R.id.ivi_union_pay:
                /*mIviUnionPay.setGoOn(R.mipmap.ic_radio_button_checked_black);
                mIviWeChat.setGoOn(R.mipmap.ic_radio_button_unchecked_black);
                mIviAliPay.setGoOn(R.mipmap.ic_radio_button_unchecked_black);*/
                mIviUnionPay.show(true, R.mipmap.ic_pay_select);
                mIviWeChat.show(false, R.mipmap.ic_pay_select);
                mIviAliPay.show(false, R.mipmap.ic_pay_select);
                mIviAliPay.setBackGround(ContextCompat.getColor(this, R.color.white));
                mIviWeChat.setBackGround(ContextCompat.getColor(this, R.color.white));
                mIviUnionPay.setBackGround(ContextCompat.getColor(this, R.color.id_cord_background));
                status = Constant.UPAY;
                break;
            case R.id.tv_determine_cancel:
                if(status !=null) {
//                    setStatus(status);
                    mPresenter.setPayment(bid, status);
                }else{
                    showMessage("请选择支付方式");
                }
                break;
        }
    }

    public void setStatus(String status){
        /*switch (status){
            case Constant.ALIPAY:
                String info = "";
                AlipayUtils.aliPay(this,info);
                break;*/
            switch (status){
                case Constant.ALIPAY :
                    String info = "app_id=2017041706768720&biz_content=%7b%22body%22%3a%22%e7%9c%bc%e9%95%9c%e8%b4%ad%e4%b9%b0%22%2c%22out_trade_no%22%3a%2221234567%22%2c%22product_code%22%3a%22QUICK_MSECURITY_PAY%22%2c%22notify_url%22%3a%22http%3a%2f%2fwww.tralinker.com%2fPay%2fNotifyUrl%22%2c%22subject%22%3a%22%e7%9c%bc%e9%95%9c%22%2c%22timeout_express%22%3a%2230m%22%2c%22total_amount%22%3a%220.01%22%7d&charset=utf-8&format=json&method=alipay.trade.app.pay&sign_type=RSA2&timestamp=2017-04-19+20%3a09%3a11&version=1.0&sign=eR1aP0lie3okd4ZFPnbKEZfGAvct3IrAA1BKUMZzPQSisGk2yvZAsz3T7qUaUYXKKUJWVl6HMvT1HVhBopI9eWqZqi2OENGQebw%2fnNOzyGu1V%2fl%2fUsN%2fz6a983Vvo2Hnwv6zQiNe%2bc2nltZzB3zls0cZBhUhETRYWNMs0Z1lFGNB%2b2SrlL0cUz4hnucFCp4VSI3MsqltjURdbVcqAie%2bVZvZtu2rxMu75PQTULgEiUGbYCvRz%2fKic7cWCNOID3lNCOvaesBnW8w6HCup18hJ944Ajy7bg5Kvt84PdmVa4kMPufnImHxojqNDT82OH%2fj5GkTQjOrhPNwZdO%2foWOV7tQ%3d%3d";

                    break;
                case Constant.WECHAT:

                    break;
            }
    }

    @Override
    public void setOrderNum(String num) {
        mTtOrderNumber.setText(num);
    }

    @Override
    public void setMobile(String mobile) {
        mTtPassengersTelephone.setText(mobile);
    }

    @Override
    public void setTravelNO(String travelNO) {
        mTtFlightNumber.setText(travelNO);
    }

    @Override
    public void setTime(long time) {
        mTtRidingTime.setText(TimerUtils.getDateFormat(time,format));
    }

    @Override
    public void setPickUpAddress(String address) {
        mTtGetOnTheBusAddress.setText(address);
    }

    @Override
    public void setDestAddress(String address) {
        mTtGetOffTheBusAddress.setText(address);
    }

    @Override
    public void setSeatNum(int num) {
        mTtNeedSeats.setTitle("需要"+num+"个座位数");
    }
    @Override
    public void setPrice(float num) {
        mTvMoney.setTextType(num+"");
    }

    @Override
    public void setCountdown(long time,long cdt) {
        setCountTime(24*60*60*1000-(time-cdt));
    }

    @Override
    public void setSuccess() {
        EventBus.getDefault().post(bid,EventBusTags.PAYMENT);
        startActivity(new Intent(this, TravelActivity.class));
        finish();
    }

    @Override
    public void setWeChat(WeChaTInfo info) {
        WXPayUtills wxPayUtills = new WXPayUtills(this);
        wxPayUtills.pay(info);
    }

    @Override
    public void setAliPay(String aliPay) {
        AlipayUtils.aliPay(this,aliPay);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // TODO: add setContentView(...) invocation
        ButterKnife.bind(this);
    }

    @Subscriber(tag = EventBusTags.WAITING_PAYMENT)
    public void onEventMainThread(TravelCancelEvent event) {
        switch (event.getEvent()) {
            case Constant.TRAVEL_CANCEL:
                Bundle pBundle=new Bundle();
                pBundle.putString(Constant.BID,event.getBid());
                startActivity(TravelCancelActivity.class,pBundle);
                break;
            case Constant.TRAVEL_CUSTOMER:
                showMessage(getString(R.string.contact_customer_service));
                break;
        }
    }

    @Subscriber(tag = EventBusTags.ALIPAY)
    public void paySuccess(String result){
        //成功 跳转界面
    }

    @Subscriber(tag = EventBusTags.TIMEOUT_NO_PAY)
    public void timeNoPay(BasePushData response){
        if(response.getData().getBID() != null){
            mPresenter.getRouteStateInfo(response.getData().getBID());
        }

    }

}
