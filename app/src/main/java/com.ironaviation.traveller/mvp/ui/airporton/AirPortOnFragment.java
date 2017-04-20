package com.ironaviation.traveller.mvp.ui.airporton;

import android.content.Intent;
import android.graphics.Paint;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.ironaviation.traveller.R;
import com.ironaviation.traveller.app.EventBusTags;
import com.ironaviation.traveller.app.utils.CheckIdCardUtils;
import com.ironaviation.traveller.app.utils.TimerUtils;
import com.ironaviation.traveller.app.utils.UserInfoUtils;
import com.ironaviation.traveller.common.AppComponent;
import com.ironaviation.traveller.common.WEFragment;
import com.ironaviation.traveller.di.component.airporton.DaggerAirPortOnComponent;
import com.ironaviation.traveller.di.module.airporton.AirPortOnModule;
import com.ironaviation.traveller.mvp.constant.Constant;
import com.ironaviation.traveller.mvp.contract.airporton.AirPortOnContract;
import com.ironaviation.traveller.mvp.model.entity.HistoryPoiInfo;
import com.ironaviation.traveller.mvp.model.entity.request.AirPortRequest;
import com.ironaviation.traveller.mvp.model.entity.request.AirportGoInfoRequest;
import com.ironaviation.traveller.mvp.model.entity.request.PassengersRequest;
import com.ironaviation.traveller.mvp.model.entity.response.Flight;
import com.ironaviation.traveller.mvp.model.entity.response.IdentificationResponse;
import com.ironaviation.traveller.mvp.presenter.airporton.AirPortOnPresenter;
import com.ironaviation.traveller.mvp.ui.airportoff.TravelFloatActivity;
import com.ironaviation.traveller.mvp.ui.my.AddressActivity;
import com.ironaviation.traveller.mvp.ui.widget.FontTextView;
import com.ironaviation.traveller.mvp.ui.widget.MyTimeDialog;
import com.ironaviation.traveller.mvp.ui.widget.NumDialog;
import com.ironaviation.traveller.mvp.ui.widget.PublicTextView;
import com.ironaviation.traveller.mvp.ui.widget.TerminalPopupWindow;
import com.jess.arms.utils.DataHelper;
import com.jess.arms.utils.UiUtils;
import com.zhy.autolayout.AutoLinearLayout;
import com.zhy.autolayout.AutoRelativeLayout;

import org.simple.eventbus.Subscriber;

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
 * Created by Administrator on 2017/4/10 0010.
 */

public class AirPortOnFragment extends WEFragment<AirPortOnPresenter> implements AirPortOnContract.View
        ,NumDialog.CallBackItem,TerminalPopupWindow.CallBackItem{


    @BindView(R.id.pw_person_on)
    PublicTextView mPwPerson;
    @BindView(R.id.pw_flt_no_on)
    PublicTextView mPwFltNo;
    @BindView(R.id.pw_airport_on)
    PublicTextView mPwAirport;
    @BindView(R.id.pw_address_on)
    PublicTextView mPwAddress;
    @BindView(R.id.pw_seat_on)
    PublicTextView mPwSeat;
    @BindView(R.id.rw_airport_on)
    RecyclerView mRwAirport;
    @BindView(R.id.ll_certification_on)
    AutoLinearLayout llCertification;
    @BindView(R.id.ll_state_on)
    AutoRelativeLayout mLlState;
    @BindView(R.id.tw_best_price_on)
    FontTextView mTwBestPrice;
    @BindView(R.id.tw_original_price_on)
    FontTextView mTwOriginalPrice;
    @BindView(R.id.tw_go_to_order_on)
    TextView mTwGoToOrder;
    @BindView(R.id.ll_price_on)
    AutoLinearLayout mLlPrice;
    @BindView(R.id.ll_book_on)
    AutoLinearLayout mLlBook;
    @BindView(R.id.tv_code_all_on)
    TextView mTvCodeAll;
    private NumDialog mNumDialog;
    private List<AirPortRequest> mAirportRequests;
    private TerminalPopupWindow mTerminalPopupWindow;
    private int terminalNum = -1;
    private MyTimeDialog mMyTimeDialog;
    private String phone;
    private Flight flight;
    private int seatNum = 2;
    private String fomart = "预计MM/dd EEEE HH:mm到达";


    public static AirPortOnFragment newInstance() {
        AirPortOnFragment fragment = new AirPortOnFragment();
        return fragment;
    }

    @Override
    protected void setupFragmentComponent(AppComponent appComponent) {
        DaggerAirPortOnComponent
                .builder()
                .appComponent(appComponent)
                .airPortOnModule(new AirPortOnModule(this))//请将AirPortOnModule()第一个首字母改为小写
                .build()
                .inject(this);
    }

    @Override
    protected View initView(LayoutInflater inflater, ViewGroup container) {
        return inflater.inflate(R.layout.fragment_airporton,container, false);
    }

    @Override
    protected void initData() {
        if(UserInfoUtils.getInstance().getInfo(getActivity()) != null
                && UserInfoUtils.getInstance().getInfo(getActivity()).getPhone() != null){
            phone = UserInfoUtils.getInstance().getInfo(getActivity()).getPhone();
            mPwPerson.setTextInfo(phone);
        }
        initEmptyData();
        setHideAll();
       /* initEmptyData();
        mNumDialog = new NumDialog(getActivity(), getNumsData(), this, Constant.AIRPORT_TYPE_SEAT);
        mMyTimeDialog = new MyTimeDialog(getActivity());*/
    }

    public void initEmptyData() {
        mAirportRequests = new ArrayList<>();
        for (int i = 0; i < Constant.SEAT_NUM; i++) {
            AirPortRequest request = new AirPortRequest();
            if(isValid() != null && i == 0){
//                request.setStatus(Constant.AIRPORT_SUCCESS);
                request.setIdCard(isValid());//设置了身份证
                request.setStatus(Constant.AIRPORT_NO);
            }else{
                request.setStatus(Constant.AIRPORT_NO);
            }
            mAirportRequests.add(request);
        }
    }

    public void clearMoreData(int position) {
        for (int i = position + 1; i < Constant.SEAT_NUM; i++) {
            mAirportRequests.get(i).setIdCard("");
            mAirportRequests.get(i).setStatus(Constant.AIRPORT_NO);
        }
    }

    public void setHideAll(){
        mPwAirport.setVisibility(View.GONE); //航站楼
        mPwAddress.setVisibility(View.GONE); //下车地址
        mPwSeat.setVisibility(View.GONE);    //座位数
        mLlBook.setVisibility(View.GONE); //接机说明和价格
        /*llCertification.setVisibility(View.GONE); //接机说明
        mLlPrice.setVisibility(View.GONE);   //设置价格*/
    }

    public void setshowSeat(){
        mPwAirport.setVisibility(View.VISIBLE); //航站楼
        mPwAddress.setVisibility(View.VISIBLE); //下车地址
        mPwSeat.setVisibility(View.GONE);    //座位数
        mLlBook.setVisibility(View.GONE);
        /*llCertification.setVisibility(View.GONE); //接机说明
        mLlPrice.setVisibility(View.GONE);   //设置价格*/
    }

    public void setShowPrice(){
        mPwAirport.setVisibility(View.VISIBLE); //航站楼
        mPwAddress.setVisibility(View.VISIBLE); //下车地址
        mPwSeat.setVisibility(View.VISIBLE);    //座位数
        mLlBook.setVisibility(View.VISIBLE);
        /*llCertification.setVisibility(View.VISIBLE); //接机说明
        mLlPrice.setVisibility(View.VISIBLE); */  //设置价格
    }

    /**
     * 此方法是让外部调用使fragment做一些操作的,比如说外部的activity想让fragment对象执行一些方法,
     * 建议在有多个需要让外界调用的方法时,统一传bundle,里面存一个what字段,来区分不同的方法,在setData
     * 方法中就可以switch做不同的操作,这样就可以用统一的入口方法做不同的事,和message同理
     * <p>
     * 使用此方法时请注意调用时fragment的生命周期,如果调用此setData方法时onActivityCreated
     * 还没执行,setData里调用presenter的方法时,是会报空的,因为dagger注入是在onActivityCreated
     * 方法中执行的,如果要做一些初始化操作,可以不必让外部调setData,在内部onActivityCreated中
     * 初始化就可以了
     *
     * @param data
     */

    @Override
    public void setData(Object data) {

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

    }

    public void addLinearLayout(int position) {
//        llCertification
        llCertification.removeAllViews();
        for (int i = 0; i < position + 1; i++) {
            MyAirportHolder holder = new MyAirportHolder();
            View view = LayoutInflater.from(getActivity()).inflate(R.layout.include_public_view, null, false);
            holder.mIvLogo = (ImageView) view.findViewById(R.id.iv_logo); //右边的图标
            holder.mLineEdt = view.findViewById(R.id.line_edt); //底下的线
            holder.mIvStatus = (ImageView) view.findViewById(R.id.iv_status); //右边的图标  占时不用;
            holder.mEdtContent = (EditText) view.findViewById(R.id.edt_content); // 文本框
            holder.mTvCode = (TextView) view.findViewById(R.id.tv_code);  //验证按钮
            holder.mPwLl = (AutoLinearLayout) view.findViewById(R.id.pw_ll); //整个布局
            if (mAirportRequests.get(i).getStatus() == Constant.AIRPORT_SUCCESS) {
                setSuccess(holder);
            } else if (mAirportRequests.get(i).getStatus() == Constant.AIRPORT_FAILURE) {
                setFailure(holder);
            } else {
                setNomal(holder);
            }
            if (i == position) {
                holder.mLineEdt.setVisibility(View.GONE);
            }
            setAirportData(holder, mAirportRequests.get(i));
            setlistener(holder, i);
            llCertification.addView(view);
        }

    }

    public void setlistener(final MyAirportHolder holder, final int position) {
        holder.mEdtContent.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                mAirportRequests.get(position).setIdCard(editable.toString());
            }
        });
        holder.mTvCode.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (mAirportRequests.get(position).getStatus() != Constant.AIRPORT_SUCCESS) {
                    if (holder.mEdtContent.getText().toString().trim() != null && CheckIdCardUtils.validateCard(holder.mEdtContent.getText().toString().trim())) {
                        mAirportRequests.get(position).setStatus(Constant.AIRPORT_SUCCESS);
                        mAirportRequests.get(position).setIdCard(holder.mEdtContent.getText().toString().trim());
                        UiUtils.makeText(holder.mEdtContent.getText().toString().trim() + "  " + position);
                    } else {
                        UiUtils.makeText("请输入正确身份证号码:" + holder.mEdtContent.getText().toString().trim() + "  " + position);
                    }
                } else {
                    holder.mEdtContent.setText("");
                    setNomal(holder);
                    mAirportRequests.get(position).setStatus(Constant.AIRPORT_NO);
                }
            }
        });
    }

    public List<String> getNumsData() {
        List<String> list = new ArrayList<>();
        for (int i = 0; i < Constant.SEAT_NUM; i++) {
            list.add(i + 1 + "个座位");
        }
        return list;
    }

    //正常状况
    public void setNomal(MyAirportHolder holder) {
        holder.mIvLogo.setImageResource(R.mipmap.ic_validate);
        holder.mTvCode.setText("验证");
        holder.mTvCode.setTextColor(getResources().getColor(R.color.white));
        holder.mTvCode.setBackgroundResource(R.drawable.btn_code_brown);
        holder.mEdtContent.setEnabled(true);
    }

    //成功状态
    public void setSuccess(MyAirportHolder holder) {
        holder.mIvLogo.setImageResource(R.mipmap.ic_success);
        holder.mTvCode.setText("重置");
        holder.mTvCode.setTextColor(getResources().getColor(R.color.white));
        holder.mTvCode.setBackgroundResource(R.drawable.btn_code_brown);
        holder.mEdtContent.setEnabled(false);
    }

    //失败状态
    public void setFailure(MyAirportHolder holder) {
        holder.mIvLogo.setImageResource(R.mipmap.ic_failure);
        holder.mTvCode.setText("验证");
        holder.mTvCode.setTextColor(getResources().getColor(R.color.white));
        holder.mTvCode.setBackgroundResource(R.drawable.btn_code_brown);
        holder.mEdtContent.setEnabled(true);
    }

    //设置数据
    public void setAirportData(MyAirportHolder holder, AirPortRequest request) {
        holder.mEdtContent.setText(request.getIdCard());
        if (request.getStatus() == Constant.AIRPORT_SUCCESS) {
            setSuccess(holder);
        } else if (request.getStatus() == Constant.AIRPORT_FAILURE) {
            setFailure(holder);
        } else {
            setNomal(holder);
        }
    }
    //获取航站楼位置
    @Override
    public void getItem(int position) {
        terminalNum = position;
        mPwAirport.setTextInfo("成都双流国际机场T" + (position + 1) + "航站楼");
        mTerminalPopupWindow.dismiss();
    }


    @Override
    public void setAirPortPrice(double price, double myPrice) {
        setPrice(price,myPrice);
    }

    @Override
    public void setSeatNum(List<PassengersRequest> list) {
        showPrice();
        //seatNum 座位数
        for(int i = 0 ; i < list.size(); i++){
            for(int j = 0; j < mAirportRequests.size();j++){
                if(list.get(i).getIDCardNo() != null && list.get(i).getIDCardNo().equals(mAirportRequests.get(j).getIdCard())){
                    if(list.get(i).isIsValid()){
                        mAirportRequests.get(j).setStatus(Constant.AIRPORT_SUCCESS);
                    }else{
                        mAirportRequests.get(j).setStatus(Constant.AIRPORT_FAILURE);
                    }
                }
            }
        }
        setSeat(seatNum);
    }

    @Override
    public void getItem(int position, int type) {
        if (type == Constant.AIRPORT_TYPE_SEAT) {
            mPwSeat.setTextInfo("需要" + (position + 1) + "个座位");
            clearMoreData(position);
            List<AirPortRequest> list = new ArrayList<>();
            for (int i = 0; i < position; i++) {
                list.add(mAirportRequests.get(i));
            }
            addLinearLayout(position);
//            mAirPortAdapter.setData(list);
            mNumDialog.dismiss();
        }
    }

    @Override
    public void setBID(String bid) {

    }

    @Override
    public void isOrderSuccess(boolean flag) {

    }

    public class MyAirportHolder {
        public ImageView mIvLogo;
        public View mLineEdt;
        public ImageView mIvStatus;
        public EditText mEdtContent;
        public TextView mTvCode;
        public AutoLinearLayout mPwLl;
    }

    @OnClick({R.id.pw_seat_on, R.id.pw_airport_on,R.id.pw_address_on,R.id.pw_flt_no_on})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.pw_seat_on:
                mNumDialog.show();
                break;
            case R.id.pw_airport_on:
                if(mTerminalPopupWindow != null){
                    mTerminalPopupWindow.setNum(terminalNum);
                    mTerminalPopupWindow.show(mPwSeat);
                }
//                mTerminal.show();
                break;
            case R.id.pw_address_on:
                Intent intent1 = new Intent(getActivity(),AddressActivity.class);
                Bundle bundle = new Bundle();
                bundle.putInt(Constant.ADDRESS_TYPE,Constant.AIRPORT_ON);
                intent1.putExtras(bundle);
                launchActivity(intent1);
                break;
            case R.id.pw_flt_no_on:
                Intent intent = new Intent(getActivity(), TravelFloatOnActivity.class);
                startActivity(intent);
                getActivity().overridePendingTransition(R.anim.left_in_alpha, R.anim.right_out_alpha);
            break;
           /* case R.id.pw_time:
                mMyTimeDialog.showDialog("test");
                break;
            case R.id.pw_flt_no:
                Intent intent = new Intent(getActivity(), TravelFloatActivity.class);
                startActivity(intent);
                getActivity().overridePendingTransition(R.anim.left_in_alpha, R.anim.right_out_alpha);
//                mTravelPopupwindow.showPopupWindow(mPwPerson);
                break;*/
        }
    }

    public String isValid(){
        if(DataHelper.getDeviceData(getActivity(),Constant.IDENTIFICATION) != null) {
            IdentificationResponse response = DataHelper.getDeviceData(getActivity(), Constant.IDENTIFICATION);
            return response.getIDCard();
        }else{
            return null;
        }
    }

    @Subscriber(tag = EventBusTags.FLIGHT_ON)
    public void getFlightInfo(Flight flight){
        this.flight = flight;
        clearData();
        setshowSeat();
        mPwFltNo.setTextInfo(flight.getInfo().getFlightNo());
        if(getTerminalNum(flight.getList().get(0).getTakeOff()) != -1){
            terminalNum = getTerminalNum(flight.getList().get(0).getTakeOff());
            mPwAirport.setTextInfo(getTerminal().get(getTerminalNum(flight.getList().get(0).getTakeOff())));
        }
        if(flight.getList().get(0).getArriveTime() != 0){
            mPwFltNo.setArriveTime(getDateInfo(flight.getList().get(0).getArriveTime()));
        }
    }

    public String getDateInfo(long time){
       return TimerUtils.getDateFormat(time,fomart);
    }

    //清理数据
    public void clearData(){
        mPwAddress.setInitInfo(getResources().getString(R.string.travel_get_address)); //下车地址
        mPwAirport.setInitInfo(getResources().getString(R.string.airport_airport));
        mPwSeat.setTextInfo(getResources().getString(R.string.airport_seat));
        mLlPrice.setVisibility(View.GONE);
        mPwSeat.setVisibility(View.GONE);
//        showPrice(false);
        setSeat(Constant.DEFULT_SEAT);
//        addressFlag = false;
//        timeFlag = false;

    }

    public void setSeat(int position){
        mPwSeat.setTextInfo("需要" + (position) + "个座位");
        clearMoreData(position);
        if(mPwFltNo.getTextInfo().substring(0,2).equalsIgnoreCase(Constant.SC_AIRPORT)) {
            List<AirPortRequest> list = new ArrayList<>();
            for (int i = 0; i < position; i++) {
                list.add(mAirportRequests.get(i));
            }
            addLinearLayout(position);
            mPwSeat.setLineVisiable(true);
        }else{
            mPwSeat.setLineVisiable(false);
        }
    }

    public int getTerminalNum(String text){
        if(text.contains("T1")){
            return 0;
        }else if(text.contains("T2")){
            return 1;
        }else{
            return -1;
        }
    }

    public List<String> getTerminal() {
        List<String> list = new ArrayList<>();
        for (int i = 0; i < 2; i++) {
            list.add("成都双流国际机场T"  + (i + 1) + "航站楼");
        }
        return list;
    }

    @Subscriber(tag = EventBusTags.AIRPORT_ON)
    public void getAddress(HistoryPoiInfo info){
//        this.info = info;
        mPwAddress.setTextInfo(info.address);
        //getAirPortInfo
//        mPresenter.getAirportInfo(getAirPortInfo());
    }

    public void setPrice(double price,double acturlPrice) {
//        TypefaceUtils.getInstance().setTypeface(getActivity(),mTwBestPrice);
//        TypefaceUtils.getInstance().setTypeface(getActivity(),mTwOriginalPrice);
//        String bestPrice = "<font color='#e83328'>" + "22.04" + "</font>" + "<font color='#b2b2b2' size=40> 元</font>";
        mTwBestPrice.setTextType(price+"");
//        String originalPrice = "<font color='#3a3a3a' >" + "22.04" + "</font>" + "<font color='#3a3a3a' size=40> 元</font>";
        mTwOriginalPrice.setTextType(acturlPrice+"");
        mTwOriginalPrice.getPaint().setFlags(Paint.STRIKE_THRU_TEXT_FLAG);
    }

    public void showPrice(){
        mPwSeat.setVisibility(View.VISIBLE);
        llCertification.setVisibility(View.VISIBLE);
        mLlPrice.setVisibility(View.VISIBLE);
    }

    /*public AirportGoInfoRequest getAirPortInfo(){
        AirportGoInfoRequest request = new AirportGoInfoRequest();
        request.setFlightNo(flight.getInfo().getFlightNo());
        request.setFlightDate(TimerUtils.getDateFormat(flight.getList().get(0).getTakeOffTime(),formatDate));
        request.setTakeOffDateTime(flight.getList().get(0).getTakeOffTime());
        request.setArriveDateTime(flight.getList().get(0).getArriveTime());
        request.setTakeOffAddress(flight.getList().get(0).getTakeOff());
        request.setArriveAddress(flight.getList().get(0).getArrive());
       *//* request.setPickupAddress(info.address);
        request.setPickupLatitude(info.location.latitude);
        request.setPickupLongitude(info.location.longitude);
        request.setPickupTime(TimerUtils.getDateFormat(time,formatDate));*//*
        if(phone != null){
            request.setCallNumber(phone);
        }
        if(terminalNum == 1) {
            request.setDestAddress(Constant.AIRPORT_T2);
            request.setDestLatitude(Constant.AIRPORT_T2_LATITUDE);
            request.setDestLongitude(Constant.AIRPORT_T2_LONGITUDE);
        }else{
            request.setDestAddress(Constant.AIRPORT_T1);
            request.setDestLatitude(Constant.AIRPORT_T1_LATITUDE);
            request.setDestLongitude(Constant.AIRPORT_T1_LONGITUDE);
        }
        request.setSeatNum(seatNum);
        List<PassengersRequest> list = new ArrayList<>();
        for(int i = 0; i< seatNum;i++){
            PassengersRequest request1 = new PassengersRequest();
            if(mAirportRequests.get(i) != null  &&
                    !TextUtils.isEmpty(mAirportRequests.get(i).getIdCard())) {
                request1.setIDCardNo(mAirportRequests.get(i).getIdCard());
                list.add(request1);
            }else{
                mAirportRequests.get(i).setStatus(Constant.AIRPORT_NO);
            }
        }
        if(list.size() > 0){
            request.setPassengers(list);
        }
        *//*if(bid != null){
            request.setBID(bid);
        }*//*
        return request;
    }*/
}