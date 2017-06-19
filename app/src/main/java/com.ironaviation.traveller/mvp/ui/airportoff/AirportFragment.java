package com.ironaviation.traveller.mvp.ui.airportoff;

import android.Manifest;
import android.content.Intent;
import android.graphics.Paint;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.ironaviation.traveller.R;
import com.ironaviation.traveller.app.EventBusTags;
import com.ironaviation.traveller.app.utils.PriceUtil;
import com.ironaviation.traveller.app.utils.TimerUtils;
import com.ironaviation.traveller.app.utils.UserInfoUtils;
import com.ironaviation.traveller.common.AppComponent;
import com.ironaviation.traveller.common.WEFragment;
import com.ironaviation.traveller.di.component.airportoff.DaggerAirportComponent;
import com.ironaviation.traveller.di.module.airportoff.AirportModule;
import com.ironaviation.traveller.mvp.constant.Constant;
import com.ironaviation.traveller.mvp.contract.airportoff.AirportContract;
import com.ironaviation.traveller.mvp.model.api.Api;
import com.ironaviation.traveller.mvp.model.entity.HistoryPoiInfo;
import com.ironaviation.traveller.mvp.model.entity.LoginEntity;
import com.ironaviation.traveller.mvp.model.entity.request.AirPortRequest;
import com.ironaviation.traveller.mvp.model.entity.request.AirportGoInfoRequest;
import com.ironaviation.traveller.mvp.model.entity.request.PassengersRequest;
import com.ironaviation.traveller.mvp.model.entity.response.Flight;
import com.ironaviation.traveller.mvp.presenter.airportoff.AirportPresenter;
import com.ironaviation.traveller.mvp.ui.airporton.TravelFloatOnActivity;
import com.ironaviation.traveller.mvp.ui.my.AddressActivity;
import com.ironaviation.traveller.mvp.ui.my.travel.PaymentDetailsActivity;
import com.ironaviation.traveller.mvp.ui.payment.WaitingPaymentActivity;
import com.ironaviation.traveller.mvp.ui.webview.WebViewActivity;
import com.ironaviation.traveller.mvp.ui.widget.AlertDialog;
import com.ironaviation.traveller.mvp.ui.widget.FontTextView;
import com.ironaviation.traveller.mvp.ui.widget.MyTimeDialog;
import com.ironaviation.traveller.mvp.ui.widget.NumDialog;
import com.ironaviation.traveller.mvp.ui.widget.PublicTextView;
import com.ironaviation.traveller.mvp.ui.widget.TerminalPopupWindow;
import com.ironaviation.traveller.mvp.ui.widget.TimeNewDialog;
import com.jess.arms.utils.DataHelper;
import com.jess.arms.utils.UiUtils;
import com.squareup.haha.guava.base.Predicates;
import com.yanzhenjie.permission.AndPermission;
import com.zhy.autolayout.AutoLinearLayout;

import org.simple.eventbus.Subscriber;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;

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
 * Created by Administrator on 2017/5/15.
 */

public class AirportFragment extends WEFragment<AirportPresenter> implements AirportContract.View ,
        MyTimeDialog.ItemCallBack,NumDialog.CallBackItem,TerminalPopupWindow.CallBackItem{

    @BindView(R.id.pw_person_airport)
    PublicTextView mPwPersonAirport;
    @BindView(R.id.pw_flt)
    PublicTextView mPwFlt;
    @BindView(R.id.pw_time_off)
    PublicTextView mPwTimeOff;
    @BindView(R.id.pw_address_off)
    PublicTextView mPwAddressOff;
    @BindView(R.id.pw_airport_off)
    PublicTextView mPwAirportOff;
    @BindView(R.id.ll_off_address)
    AutoLinearLayout mLlOffAddress;
    @BindView(R.id.pw_airport_on)
    PublicTextView mPwAirportOn;
    @BindView(R.id.pw_address_on)
    PublicTextView mPwAddressOn;
    @BindView(R.id.ll_on_address)
    AutoLinearLayout mLlOnAddress;
    @BindView(R.id.pw_seat)
    PublicTextView mPwSeat;
    @BindView(R.id.pw_expain_free)
    PublicTextView mPwExpainFree;
    @BindView(R.id.tw_reset_price)
    ImageView mTwResetPrice;
    @BindView(R.id.tw_best_price)
    FontTextView mTwBestPrice;
    @BindView(R.id.tw_original_price)
    FontTextView mTwOriginalPrice;
    @BindView(R.id.ll_set_price)
    AutoLinearLayout mLlSetPrice;
    @BindView(R.id.ll_price)
    AutoLinearLayout mLlPrice;
    @BindView(R.id.ll_price_seat)
    AutoLinearLayout mLlPriceSeat;
    @BindView(R.id.tw_go_to_order)
    TextView mTwGoToOrder;
    Unbinder unbinder;
    private String status;
    private String flightNo="";
    private Flight flight;
    private MyTimeDialog mMyTimeDialog;
    private int terminalNum = 0;
    /*private String fomartOFF = "预计MM/dd EEEE HH:mm起飞";
    private String fomartOn = "预计MM/dd EEEE HH:mm到达";*/
    private boolean addressFlagOFF,addressFlagON;
    private boolean timeFlag,flightFlag;
    private HistoryPoiInfo info;
    private double price, acturlPrice;
    private String bid;
    private int seatNum = 1;
    private NumDialog mNumDialog;
   /* private String formatDate = "yyyy-MM-dd";
    private String format = "MM月dd日 HH点mm分";*/
    private long time;
    private String phone;
    private List<PassengersRequest> mPassengersRequests;
    private TerminalPopupWindow mTerminalPopupWindow;
    private List<AirPortRequest> mAirportRequests;
    private String idCard;
    private TimeNewDialog mTimeNewDialog;


    @Override
    protected void setupFragmentComponent(AppComponent appComponent) {
        DaggerAirportComponent
                .builder()
                .appComponent(appComponent)
                .airportModule(new AirportModule(this))//请将AirportModule()第一个首字母改为小写
                .build()
                .inject(this);
    }

    @Override
    protected View initView(LayoutInflater inflater, ViewGroup container) {
        return inflater.inflate(R.layout.fragment_airport, container, false);
    }

    @Override
    protected void initData() {
        Bundle bundle = getArguments();
        status = bundle.getString(Constant.STATUS);
        setOrderButtonStatus(false);
        initLayout(status);
        mPwSeat.setTextInfo("需要" + seatNum + "个座位");
        mNumDialog = new NumDialog(getActivity(), getNumsData(), this, Constant.AIRPORT_TYPE_SEAT);
        if(UserInfoUtils.getInstance().getInfo(getActivity()) != null
                && UserInfoUtils.getInstance().getInfo(getActivity()).getPhone() != null){
            phone = UserInfoUtils.getInstance().getInfo(getActivity()).getPhone();
            mPwPersonAirport.setTextInfo(phone);
        }
        mPwExpainFree.setFirstTextColor(getResources().getColor(R.color.color_brown));
        mTerminalPopupWindow = new TerminalPopupWindow(getActivity(),getTerminal(),this);
        mTerminalPopupWindow.setNum(terminalNum);
        initEmptyData();
    }

    public void initLayout(String myStatus){
        if(myStatus != null){
            if(myStatus.equals(Constant.ENTER_PORT)){
                showOnLayout();
            }else if(myStatus.equals(Constant.CLEAR_PORT)){
                showOffLayout();
            }
        }
    }

    public void showOnLayout(){
//        AIRPORT_T1
        mPwAirportOn.setTextInfo(Constant.AIRPORT_T1);
        mLlOnAddress.setVisibility(View.VISIBLE);
        mLlOffAddress.setVisibility(View.GONE);
        mPwFlt.setInitInfo(getResources().getString(R.string.airport_no));
    }

    public void showOffLayout(){
        mPwAirportOff.setTextInfo(Constant.AIRPORT_T1);
        mLlOffAddress.setVisibility(View.VISIBLE);
        mLlOnAddress.setVisibility(View.GONE);
        mPwFlt.setInitInfo(getResources().getString(R.string.airport_off));
    }

    public void showPrice(boolean flag){
        mLlPriceSeat.setVisibility(flag?View.VISIBLE:View.GONE);
    }

    @Override
    public void setData(Object data) {

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

    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // TODO: inflate a fragment view
        View rootView = super.onCreateView(inflater, container, savedInstanceState);
        unbinder = ButterKnife.bind(this, rootView);
        return rootView;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }

    @OnClick({R.id.pw_flt,R.id.pw_time_off,R.id.pw_address_off,R.id.pw_address_on,
            R.id.pw_expain_free,R.id.pw_seat,R.id.ll_set_price,R.id.tw_reset_price,
            R.id.tw_go_to_order,R.id.pw_airport_on,R.id.pw_airport_off})
    public void onClick(View view){
        switch (view.getId()){
            case R.id.pw_flt:
                setPwFltNo(status);
                break;
            case R.id.pw_time_off:
                judgeFlyNo();
                break;
            case R.id.pw_address_off:
                setAddress(status);

                break;
            case R.id.pw_address_on:
                setAddress(status);
                break;
            case R.id.pw_seat:
                mNumDialog.show();
                break;
            case R.id.ll_set_price:
                if(status != null && status.equals(Constant.CLEAR_PORT)) {
                    setPaymentDetail();
                }else if(status != null && status.equals(Constant.ENTER_PORT)){
                    setPaymentDetail();
                }
                break;
            case R.id.pw_expain_free:
                expainFree();
                break;
            case R.id.tw_reset_price:
                if(status != null && status.equals(Constant.CLEAR_PORT)) {
                    mPresenter.getAirportInfo(getAirPortInfo());
                }else if(status != null && status.equals(Constant.ENTER_PORT)){
                    mPresenter.getAirportInfo(getAirPortInfo());
                }
                break;
            case R.id.tw_go_to_order:
                if(status != null && status.equals(Constant.CLEAR_PORT)) {
                    mPresenter.isOrderSuccess(bid);
                }else if(status != null && status.equals(Constant.ENTER_PORT)){
                    mPresenter.isOrderOnSuccess(bid);
                }
                break;
            case R.id.pw_airport_on:
                if (mTerminalPopupWindow != null) {
                    mTerminalPopupWindow.setNum(terminalNum);
                    mTerminalPopupWindow.show(mPwSeat);
                }
                break;
            case R.id.pw_airport_off:
                if (mTerminalPopupWindow != null) {
                    mTerminalPopupWindow.setNum(terminalNum);
                    mTerminalPopupWindow.show(mPwSeat);
                }
                break;

        }
    }

    public void  expainFree(){
        Intent intent3 = new Intent(getActivity(),VerifyIdCardActivity.class);
        Bundle bundle1 = new Bundle();
        if(status != null && status.equals(Constant.CLEAR_PORT)) {
            bundle1.putString(Constant.STATUS, Constant.CLEAR_PORT);
        }else if(status != null && status.equals(Constant.ENTER_PORT)){
            bundle1.putString(Constant.STATUS, Constant.ENTER_PORT);
        }
        bundle1.putInt(Constant.PEOPLE_NUM,seatNum != 0 ? seatNum :1);
        AirportGoInfoRequest mRequest = getAirPortInfo();
        /*if(mPassengersRequests != null && mPassengersRequests.size() > 0){
            mRequest.setPassengers(mPassengersRequests);
        }*/
        bundle1.putSerializable(Constant.AIRPORT_GO_INFO,mRequest);
        intent3.putExtras(bundle1);
        startActivity(intent3);
    }

    //付款详情
    public void setPaymentDetail(){
        Intent intent = new Intent(getActivity(), PaymentDetailsActivity.class);
        intent.putExtra(Constant.REAL_PRICE,price);
        intent.putExtra(Constant.FIXED_PRICE,acturlPrice);
        int num = 0;
        double myPrice = 0;
        if(mPassengersRequests != null && mPassengersRequests.size() > 0) {
            for (int i = 0; i < mPassengersRequests.size(); i++) {
                if (mPassengersRequests.get(i).isIsValid() && !mPassengersRequests.get(i).isHasBooked()) {
                    num++;
                    myPrice = myPrice + mPassengersRequests.get(i).getPrice();
                }
            }
        }
        intent.putExtra(Constant.PEOPLE_NUM,seatNum);
        intent.putExtra(Constant.FREE_PASSENGER,num);
        intent.putExtra(Constant.FREE_PASSENGER_PRICE,myPrice);
        intent.putExtra(Constant.PAYMENT,Constant.PAYMENT_NOMAL);
        startActivity(intent);
    }

    public void setAddress(String status){
        Bundle bundle = new Bundle();
        if(status != null && status.equals(Constant.ENTER_PORT)){
            bundle.putInt(Constant.ADDRESS_TYPE,Constant.AIRPORT_ON);
        }else if(status != null && status.equals(Constant.CLEAR_PORT)){
            bundle.putInt(Constant.ADDRESS_TYPE,Constant.AIRPORT_GO);
        }
        Intent intent1 = new Intent(getActivity(),AddressActivity.class);
        intent1.putExtras(bundle);
        launchActivity(intent1);
    }

    //判断时间
    public void judgeFlyNo() {
        String fno = mPwFlt.getTextInfo();
        if (fno.equals(getActivity().getResources().getString(R.string.airport_off))) {
            showMessage(getString(R.string.airport_off));
        } else {
            long num = (flight.getList().get(0).getTakeOffTime() - System.currentTimeMillis())
                    / (60 * 60 * 1000);
            if (num < 5) {
                showMessage(getString(R.string.fly_four_time));
            } else {
                mTimeNewDialog.showDialog(getResources().getString(R.string.airport_input_time));
//                mMyTimeDialog.showDialog(getResources().getString(R.string.airport_input_time));
            }
        }
    }

    //航班判断是跳转到接机或者送机
    public void setPwFltNo(String myStatus){
        if(myStatus != null){
            Intent intent = new Intent();
            if(myStatus.equals(Constant.ENTER_PORT)){
                intent.setClass(getActivity(), TravelFloatOnActivity.class);
                intent.putExtra(Constant.FLIGHT_NO,flightNo);
                intent.putExtra(Constant.STATUS,Constant.ENTER_PORT);
            }else if(myStatus.equals(Constant.CLEAR_PORT)){
                intent.setClass(getActivity(), TravelFloatActivity.class);
                intent.putExtra(Constant.FLIGHT_NO,flightNo);
                intent.putExtra(Constant.STATUS,Constant.CLEAR_PORT);
            }
            startActivity(intent);
            getActivity().overridePendingTransition(R.anim.left_in_alpha, R.anim.right_out_alpha);
        }
    }

    //價格按鈕
    public void setOrderButtonStatus(boolean flag) {
        if (flag) {
            mTwGoToOrder.setBackgroundResource(R.drawable.select_btn_red);
        } else {
            mTwGoToOrder.setBackgroundResource(R.drawable.btn_grey_shap);
        }
        mTwGoToOrder.setEnabled(flag);
    }

    //航班返回的值做的处理
    @Subscriber(tag = EventBusTags.FLIGHT)
    public void getFlightInfo(Flight flight){
        this.flight = flight;
        flightFlag = true;
        if(status != null && flight.getStatus() != null && flight.getStatus().equals(Constant.ENTER_PORT)&&flight.getStatus().equals(status)){
//            clearData();
            if(getTerminalNum(flight.getList().get(0).getTakeOff()) != -1) {
                terminalNum = getTerminalNum(flight.getList().get(0).getTakeOff());
                mPwAirportOn.setTextInfo(getTerminal(getTerminalNum(flight.getList().get(0).getTakeOff())));
            }
            if(flight.getList().get(0).getTakeOffTime() != 0){
                mPwFlt.setArriveTime(TimerUtils.getDateFormat(flight.getList().get(0).getArriveTime(),Constant.fomartOn));
            }
            if(flight.getInfo().getFlightNo() != null){
                flightNo = flight.getInfo().getFlightNo();
                mPwFlt.setTextInfo(flight.getInfo().getFlightNo());
            }
            if(flightFlag && addressFlagON){
                mPresenter.getAirportInfo(getAirPortInfo());
            }
        }else if(status != null && flight.getStatus() != null && flight.getStatus().equals(Constant.CLEAR_PORT) && flight.getStatus().equals(status)){
            clearData();
            mTimeNewDialog = new TimeNewDialog(getActivity(),this);
            mTimeNewDialog.setTime(flight.getList().get(0).getTakeOffTime(),Constant.AIRPORT_GO);
//            mMyTimeDialog = new MyTimeDialog(getActivity(),this,flight.getList().get(0).getTakeOffTime());
            if(getTerminalNum(flight.getList().get(0).getTakeOff()) != -1) {
                terminalNum = getTerminalNum(flight.getList().get(0).getTakeOff());
                mPwAirportOff.setTextInfo(getTerminal(getTerminalNum(flight.getList().get(0).getTakeOff())));
            }
            if(flight.getList().get(0).getTakeOffTime() != 0){
                mPwFlt.setArriveTime(TimerUtils.getDateFormat(flight.getList().get(0).getTakeOffTime(),Constant.fomartOFF));
            }
            if(flight.getInfo().getFlightNo() != null){
                flightNo = flight.getInfo().getFlightNo();
                mPwFlt.setTextInfo(flight.getInfo().getFlightNo());
            }
        }
    }

    public List<String> getTerminal() {
        List<String> list = new ArrayList<>();
        for (int i = 0; i < 2; i++) {
            list.add("成都双流国际机场T"  + (i + 1) + "航站楼");
        }
        return list;
    }

    public String getTerminal(int terminalNum){
        if(terminalNum == 0){
            return Constant.AIRPORT_T1;
        }else if(terminalNum == 1){
            return Constant.AIRPORT_T2;
        }else{
            return  Constant.AIRPORT_T1;
        }
    }

    //判断是哪个航站楼
    public int getTerminalNum(String text){
        if(text.contains("T1")){
            return 0;
        }else if(text.contains("T2")){
            return 1;
        }else{
            return -1;
        }
    }

    //设置座位数
    public List<String> getNumsData() {
        List<String> list = new ArrayList<>();
        for (int i = 0; i < Constant.SEAT_NUM; i++) {
            list.add(i + 1 + "个座位");
        }
        return list;
    }

    //清空数据
    public void clearData(){
        if(status != null && status.equals(Constant.CLEAR_PORT)){
            mPwTimeOff.setInitInfo(getResources().getString(R.string.airport_time));
            /*mPwAddressOff.setInitInfo(getResources().getString(R.string.airport_address));
            mPwAirportOff.setTextInfo(Constant.AIRPORT_T1);
            mPwSeat.setTextInfo(getResources().getString(R.string.airport_seat));*/
//            addressFlagOFF = false;
            timeFlag = false;
            setOrderButtonStatus(false);
            showPrice(false);
//            mPwSeat.setFreeInfo("");
        }else if(status != null && status.equals(Constant.ENTER_PORT)){
            /*mPwSeat.setTextInfo(getResources().getString(R.string.airport_seat));
            mPwAirportOn.setTextInfo(Constant.AIRPORT_T1);
            mPwAddressOn.setInitInfo(getResources().getString(R.string.airport_address));
            addressFlagON = false;*/
            setOrderButtonStatus(false);
            showPrice(false);
//            mPwSeat.setFreeInfo("");
        }
    }

    //航班起飞时间 返回的
    @Override
    public void setTime(long time) {
        this.time = time;
        mPwTimeOff.setTextInfo(TimerUtils.getDateFormat(time,Constant.format));
        timeFlag = true;
        if(timeFlag && addressFlagOFF ){
            mPresenter.getAirportInfo(getAirPortInfo());
        }
    }

    @Subscriber(tag = EventBusTags.AIRPORT_GO)
    public void getAddress(HistoryPoiInfo info){
        if(status != null && status.equals(Constant.CLEAR_PORT)) {
            addressFlagOFF = true;
            this.info = info;
            mPwAddressOff.setTextInfo(info.name);
            if (timeFlag && addressFlagOFF) {
                mPresenter.getAirportInfo(getAirPortInfo());
            }
        }
    }

    @Subscriber(tag = EventBusTags.AIRPORT_ON)
    public void getAddressOn(HistoryPoiInfo info){
        if(status != null && status.equals(Constant.ENTER_PORT)) {
            addressFlagON = true;
            this.info = info;
            mPwAddressOn.setTextInfo(info.name);
            if (addressFlagON && flightFlag) {
                mPresenter.getAirportInfo(getAirPortInfo());
            }
        }
    }

    public AirportGoInfoRequest getAirPortInfo(){
        AirportGoInfoRequest request = new AirportGoInfoRequest();
        request.setFlightNo(flight.getInfo().getFlightNo());
        request.setFlightDate(TimerUtils.getDateFormat(flight.getList().get(0).getTakeOffTime(),Constant.formatDate));
        request.setTakeOffDateTime(flight.getList().get(0).getTakeOffTime());
        if(flight.getList().get(0).getRealityArriveTime() != null) {
            request.setArriveDateTime(flight.getList().get(0).getRealityArriveTime());
        }else{
            request.setArriveDateTime(flight.getList().get(0).getArriveTime());
        }
        request.setTakeOffAddress(flight.getList().get(0).getTakeOff());
        request.setArriveAddress(flight.getList().get(0).getArrive());

        request.setCity(Constant.CITY);
        if(status.equals(Constant.CLEAR_PORT)) {
            request.setEnterPort(false);
            request.setDestDetailAddress("");
            request.setPickupAddress(info.name);
            request.setPickupDetailAddress(info.address);
            request.setPickupLatitude(info.location.latitude);
            request.setPickupLongitude(info.location.longitude);
            request.setPickupTime(time);
            if(terminalNum == 1) {
                request.setDestAddress(Constant.AIRPORT_T2);
                request.setDestLatitude(Constant.AIRPORT_T2_LATITUDE);
                request.setDestLongitude(Constant.AIRPORT_T2_LONGITUDE);
            }else{
                request.setDestAddress(Constant.AIRPORT_T1);
                request.setDestLatitude(Constant.AIRPORT_T1_LATITUDE);
                request.setDestLongitude(Constant.AIRPORT_T1_LONGITUDE);
            }

        }else if(status.equals(Constant.ENTER_PORT)){
            request.setEnterPort(true);
            request.setDestLatitude(info.location.latitude);
            request.setDestLongitude(info.location.longitude);
            request.setDestAddress(info.name);
            request.setDestDetailAddress(info.address);
            request.setPickupDetailAddress("");
            request.setPickupTime(flight.getList().get(0).getArriveTime());
            if(terminalNum == 1) {
                request.setPickupAddress(Constant.AIRPORT_T2);
                request.setPickupLatitude(Constant.AIRPORT_T2_LATITUDE);
                request.setPickupLongitude(Constant.AIRPORT_T2_LONGITUDE);
            }else{
                request.setPickupAddress(Constant.AIRPORT_T1);
                request.setPickupLatitude(Constant.AIRPORT_T1_LATITUDE);
                request.setPickupLongitude(Constant.AIRPORT_T1_LONGITUDE);
            }
        }

        List<PassengersRequest> list = getPassengers(mAirportRequests);
        /*List<PassengersRequest> list = new ArrayList<>();
        for(int i = 0; i< seatNum;i++){
            PassengersRequest request1 = new PassengersRequest();
            if(mAirportRequests.get(i) != null  &&
                    !TextUtils.isEmpty(mAirportRequests.get(i).getIdCard())) {
                request1.setIDCardNo(mAirportRequests.get(i).getIdCard().toUpperCase());
                list.add(request1);
            }else{
                mAirportRequests.get(i).setStatus(Constant.AIRPORT_NO);
            }
        }*/
        if(list !=null && list.size() > 0){
            request.setPassengers(list);
        }
        if(phone != null){
            request.setCallNumber(phone);
        }

        request.setSeatNum(seatNum);
        if(bid != null){
            request.setBID(bid);
        }
        return request;
    }

    @Override
    public void setAirPortPrice(double price, double myPrice) {
        if(status.equals(Constant.ENTER_PORT)) {
            setPrice(price, myPrice);
            showPrice(true);
            showPrice();
        }else if(status.equals(Constant.CLEAR_PORT)){
            setPrice(price, myPrice);
            showPrice(true);
            showPrice();
        }
    }

    @Override
    public void setBID(String bid) {
        if(status.equals(Constant.ENTER_PORT)) {
            this.bid = bid;
        }else if(status.equals(Constant.CLEAR_PORT)){
            this.bid = bid;
        }
    }

    @Override
    public void isOrderSuccess(boolean flag) {
        if(flag){
            Intent intent = new Intent(getActivity(), WaitingPaymentActivity.class);
            intent.putExtra(Constant.BID,bid);
            if(status != null && status.equals(Constant.CLEAR_PORT)) {
                intent.putExtra(Constant.CHILD_STATUS, Constant.OFF);

            }else if(status != null && status.equals(Constant.ENTER_PORT)){
                intent.putExtra(Constant.CHILD_STATUS, Constant.ON);
            }
            startActivity(intent);
            clearAll();
        }
    }

    public void clearAll(){
        if(status != null && status.equals(Constant.CLEAR_PORT)){
            mPwTimeOff.setInitInfo(getResources().getString(R.string.airport_time));
            mPwAddressOff.setInitInfo(getResources().getString(R.string.airport_address));
            mPwAirportOff.setTextInfo(Constant.AIRPORT_T1);
            mPwSeat.setTextInfo(getResources().getString(R.string.airport_seat));
            addressFlagOFF = false;
            timeFlag = false;
            setOrderButtonStatus(false);
            showPrice(false);
            mPwSeat.setFreeInfo("");
            mPassengersRequests = null;
            seatNum = 1;
            mPwFlt.setInitInfo(getResources().getString(R.string.airport_off));
            mPwFlt.setArriveTime("");
            flightNo = "";
            bid = null;
        }else if(status != null && status.equals(Constant.ENTER_PORT)){
            mPwSeat.setTextInfo(getResources().getString(R.string.airport_seat));
            mPwAirportOn.setTextInfo(Constant.AIRPORT_T1);
            mPwAddressOn.setInitInfo(getResources().getString(R.string.airport_address));
            addressFlagON = false;
            setOrderButtonStatus(false);
            showPrice(false);
            mPwSeat.setFreeInfo("");
            mPassengersRequests = null;
            seatNum = 1;
            mPwFlt.setInitInfo(getResources().getString(R.string.airport_no));
            mPwFlt.setArriveTime("");
            flightNo = "";
            bid = null;
        }
    }

    @Override
    public void setError() {
        if(status.equals(Constant.ENTER_PORT)) {
            showPrice(true);
            resetPrice();
        }else if(status.equals(Constant.CLEAR_PORT)){
            showPrice(true);
            resetPrice();
        }
    }

    @Override
    public void setFreeNum(int num) {
        if(num != 0) {
            mPwSeat.setFreeInfo(num + "人免费");
        }else{
            mPwSeat.setFreeInfo("");
        }
    }

    public void showPrice(){ //显示价格和重置价格
        mTwResetPrice.setVisibility(View.GONE);
        mLlSetPrice.setVisibility(View.VISIBLE);
        setOrderButtonStatus(true);
    }

    public void resetPrice(){
        mTwResetPrice.setVisibility(View.VISIBLE);
        mLlSetPrice.setVisibility(View.GONE);
        setOrderButtonStatus(false);
    }

    public void setPrice(double price,double acturlPrice) {
        mTwBestPrice.setTextType(PriceUtil.getPrecent(acturlPrice));
        mTwOriginalPrice.setTextType(PriceUtil.getPrecent(price));
        mTwOriginalPrice.getPaint().setFlags(Paint.STRIKE_THRU_TEXT_FLAG);
        this.price = price;
        this.acturlPrice = acturlPrice;
    }

    public void initEmptyData() {
        mAirportRequests = new ArrayList<>();
        for (int i = 0; i < Constant.SEAT_NUM; i++) {
            AirPortRequest request = new AirPortRequest();
            if(isValid() && i == 0 && idCard != null){
//                request.setStatus(Constant.AIRPORT_SUCCESS);
                request.setIdCard(idCard);
                request.setStatus(Constant.AIRPORT_NO);
            }else{
                request.setStatus(Constant.AIRPORT_NO);
            }
            mAirportRequests.add(request);
        }
    }

    public List<PassengersRequest> getPassengers(List<AirPortRequest> airportRequests){
        List<PassengersRequest> list = new ArrayList<>();
        for(int i = 0; i< seatNum;i++){
            PassengersRequest request1 = new PassengersRequest();
            if(airportRequests.get(i) != null  &&
                    !TextUtils.isEmpty(airportRequests.get(i).getIdCard())) {
                request1.setIDCardNo(airportRequests.get(i).getIdCard().toUpperCase());
                list.add(request1);
            }else{
                airportRequests.get(i).setStatus(Constant.AIRPORT_NO);
            }
        }
        return list;
    }

    public void getAirport(List<PassengersRequest> requests){
        for (int i = 0; i < requests.size(); i++) {
                if (requests.get(i).getIDCardNo() != null ) {
                    if (requests.get(i).isIsValid() && !requests.get(i).isHasBooked()) {
                        mAirportRequests.get(i).setStatus(Constant.AIRPORT_SUCCESS);
                        mAirportRequests.get(i).setIdCard(requests.get(i).getIDCardNo());
                    } else if (requests.get(i).isIsValid() && requests.get(i).isHasBooked()) {
                        mAirportRequests.get(i).setStatus(Constant.AIRPORT_FAILURE);
                    } else {
                        mAirportRequests.get(i).setStatus(Constant.AIRPORT_FAILURE);
                    }
                }

        }


       /* for (int i = 0; i < requests.size(); i++) {
            for (int j = 0; j < mAirportRequests.size(); j++) {
                if (requests.get(i).getIDCardNo() != null ) {
                    if (requests.get(i).isIsValid() && !requests.get(i).isHasBooked()) {
                        mAirportRequests.get(j).setStatus(Constant.AIRPORT_SUCCESS);
                        mAirportRequests.get(j).setIdCard(requests.get(i).getIDCardNo());
                    } else if (requests.get(i).isIsValid() && requests.get(i).isHasBooked()) {
                        mAirportRequests.get(j).setStatus(Constant.AIRPORT_FAILURE);
                    } else {
                        mAirportRequests.get(j).setStatus(Constant.AIRPORT_FAILURE);
                    }
                }
            }
        }*/

    }

    public boolean isValid(){
        if(DataHelper.getDeviceData(getActivity(),Constant.LOGIN) != null) {
            LoginEntity response = DataHelper.getDeviceData(getActivity(), Constant.LOGIN);
            if(response.getIDCard() != null){
                idCard = response.getIDCard();
            }
            return response.isRealValid();
        }else{
            return false;
        }
    }

    @Override
    public void getItem(int position, int type) {
        mNumDialog.dismiss();
        seatNum = position+1;
        mPwSeat.setFreeInfo("");
        mPresenter.getAirportInfo(getAirPortInfo());
        mPwSeat.setTextInfo("需要" + seatNum + "个座位");
    }

    @Subscriber(tag = EventBusTags.ID_CARD)
    public void setPriceInfo(AirportGoInfoRequest info){
        if(status != null && status.equals(Constant.CLEAR_PORT)) {
            setPrice(info.getTotalPrice(), info.getActualPrice());
            showPrice();//设置显示价格
            mPwSeat.setTextInfo("需要" + info.getSeatNum() + "个座位");
            seatNum = info.getSeatNum();
            if (info.getPassengers() != null && info.getPassengers().size() > 0) {
                mPassengersRequests = info.getPassengers();
                int num = 0;
                for(int i = 0; i < info.getPassengers().size(); i++){
                    if(info.getPassengers().get(i).isIsValid() && !info.getPassengers().get(i).isHasBooked()){
                        num++;
                    }
                }
                if(num != 0){
                    mPwSeat.setFreeInfo(num+"人免费");
                }
                getAirport(mPassengersRequests);
            }

            if(info.getBID() != null){
                bid = info.getBID();
            }
        }
    }

    @Subscriber(tag = EventBusTags.ID_CARD_ON)
    public void setPriceInfoON(AirportGoInfoRequest info){
        if(status != null && status.equals(Constant.ENTER_PORT)) {
            setPrice(info.getTotalPrice(), info.getActualPrice());
            showPrice();//设置显示价格
            mPwSeat.setTextInfo("需要" + info.getSeatNum() + "个座位");
            seatNum = info.getSeatNum();
            if (info.getPassengers() != null && info.getPassengers().size() > 0) {
                mPassengersRequests = info.getPassengers();
                int num = 0;
                for(int i = 0; i < info.getPassengers().size(); i++){
                    if(info.getPassengers().get(i).isIsValid() && !info.getPassengers().get(i).isHasBooked()){
                        num++;
                    }
                }
                if(num != 0){
                    mPwSeat.setFreeInfo(num+"人免费");
                }
                getAirport(mPassengersRequests);
            }
            if(info.getBID() != null){
                bid = info.getBID();
            }
        }
    }

    @Override
    public void getItem(int position) {
        if(status != null && status.equals(Constant.CLEAR_PORT)) {
            terminalNum = position;
            mPwAirportOff.setTextInfo("成都双流国际机场T" + (position + 1) + "航站楼");
            mTerminalPopupWindow.dismiss();
        }else if(status != null && status.equals(Constant.ENTER_PORT)){
            terminalNum = position;
            mPwAirportOn.setTextInfo("成都双流国际机场T" + (position + 1) + "航站楼");
            mTerminalPopupWindow.dismiss();
        }
    }

    @Override
    public void setForbid(String info) {
        mLlPriceSeat.setVisibility(View.VISIBLE);
        mLlPrice.setVisibility(View.GONE);
        showIDCardDialog(info);
        setOrderButtonStatus(false);
    }

    public void showIDCardDialog(String info){
        AlertDialog dialog = new AlertDialog(getActivity());
        dialog.builder().setTitle("温馨提示").setMsg(info)
                .setOneButton("确定", new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                    }
                }).show();
    }
}