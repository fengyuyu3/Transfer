package com.ironaviation.traveller.mvp.ui.my.travel;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.NonNull;
import android.support.v4.content.ContextCompat;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.baidu.mapapi.map.BaiduMap;
import com.baidu.mapapi.map.BitmapDescriptor;
import com.baidu.mapapi.map.BitmapDescriptorFactory;
import com.baidu.mapapi.map.MapStatus;
import com.baidu.mapapi.map.MapStatusUpdate;
import com.baidu.mapapi.map.MapStatusUpdateFactory;
import com.baidu.mapapi.map.MapView;
import com.baidu.mapapi.map.Marker;
import com.baidu.mapapi.map.MarkerOptions;
import com.baidu.mapapi.map.Overlay;
import com.baidu.mapapi.map.OverlayOptions;
import com.baidu.mapapi.map.PolylineOptions;
import com.baidu.mapapi.model.LatLng;
import com.baidu.mapapi.search.core.RouteLine;
import com.baidu.mapapi.search.core.SearchResult;
import com.baidu.mapapi.search.route.BikingRouteResult;
import com.baidu.mapapi.search.route.DrivingRoutePlanOption;
import com.baidu.mapapi.search.route.DrivingRouteResult;
import com.baidu.mapapi.search.route.IndoorRouteResult;
import com.baidu.mapapi.search.route.MassTransitRouteResult;
import com.baidu.mapapi.search.route.OnGetRoutePlanResultListener;
import com.baidu.mapapi.search.route.PlanNode;
import com.baidu.mapapi.search.route.RoutePlanSearch;
import com.baidu.mapapi.search.route.TransitRouteResult;
import com.baidu.mapapi.search.route.WalkingRouteResult;
import com.baidu.trace.LBSTraceClient;
import com.baidu.trace.OnEntityListener;
import com.baidu.trace.OnStartTraceListener;
import com.baidu.trace.Trace;
import com.baidu.trace.TraceLocation;
import com.google.gson.Gson;
import com.ironaviation.traveller.R;
import com.ironaviation.traveller.app.EventBusTags;
import com.ironaviation.traveller.app.utils.CommonUtil;
import com.ironaviation.traveller.app.utils.MapUtil;
import com.ironaviation.traveller.common.AppComponent;
import com.ironaviation.traveller.common.WEActivity;
import com.ironaviation.traveller.di.component.my.travel.DaggerTravelDetailsComponent;
import com.ironaviation.traveller.di.module.my.travel.TravelDetailsModule;
import com.ironaviation.traveller.event.TravelCancelEvent;
import com.ironaviation.traveller.map.overlayutil.DrivingRouteOverlay;
import com.ironaviation.traveller.map.overlayutil.OverlayManager;
import com.ironaviation.traveller.mvp.constant.Constant;
import com.ironaviation.traveller.mvp.contract.my.travel.TravelDetailsContract;
import com.ironaviation.traveller.mvp.model.entity.request.PathPlanning;
import com.ironaviation.traveller.mvp.model.entity.response.PassengersResponse;
import com.ironaviation.traveller.mvp.model.entity.response.PushResponse;
import com.ironaviation.traveller.mvp.model.entity.response.RealtimeTrackData;
import com.ironaviation.traveller.mvp.model.entity.response.RouteStateResponse;
import com.ironaviation.traveller.mvp.presenter.my.travel.TravelDetailsPresenter;
import com.ironaviation.traveller.mvp.ui.my.EstimateActivity;
import com.ironaviation.traveller.mvp.ui.widget.MoreActionPopupWindow;
import com.jess.arms.utils.UiUtils;
import com.zhy.autolayout.AutoLinearLayout;


import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.simple.eventbus.Subscriber;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import butterknife.BindView;
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
 * 项目名称：Transfer
 * 类描述：
 * 创建人：flq
 * 创建时间：2017/3/29 15:41
 * 修改人：
 * 修改时间：2017/3/29 15:41
 * 修改备注：
 */

public class TravelDetailsActivity extends WEActivity<TravelDetailsPresenter> implements
        TravelDetailsContract.View, View.OnClickListener,OnGetRoutePlanResultListener
,BaiduMap.OnMarkerClickListener{

    @BindView(R.id.tw_name) //司机名字
    TextView mTwName;
    @BindView(R.id.tw_score) //分数
    TextView mTwScore;
    @BindView(R.id.tw_car_num) //车牌号码
    TextView mTwCarNum;
    @BindView(R.id.yw_car_color) //汽车颜色
    TextView mYwCarColor;
    @BindView(R.id.iw_mobile) //打电话
    ImageView mIwMobile;
    @BindView(R.id.ll_driver_info)
    AutoLinearLayout mLlDriverInfo; //司机信息
    @BindView(R.id.ll_complete)
    AutoLinearLayout mLlComplete; //派单成功
    @BindView(R.id.ll_ordering)
    AutoLinearLayout mLlOrdering; //派单中
    @BindView(R.id.rl_go_to_pay)
    TextView mRlGoToPay;  //确认到达按钮
    @BindView(R.id.ll_going)  //这个不要了
    AutoLinearLayout mLlGoing; //派单进行中
    @BindView(R.id.id_line)
    View idLine;
    @BindView(R.id.ll_arrive)
    AutoLinearLayout mLlArrive; // 确认到达
    @BindView(R.id.tw_order_info)
    TextView mTwOrderInfo; //派单中标题
    @BindView(R.id.iw_zoom)
    ImageView mIwZoom;
    @BindView(R.id.iw_zoom_nomal)
    ImageView mIwZoomNomal;
    @BindView(R.id.mapview)
    MapView mMapview;
    @BindView(R.id.tw_title)
    TextView mTwTitle;
    @BindView(R.id.tw_text)
    TextView mTwText;
    @BindView(R.id.tw_wait_one)
    TextView mTwWaitOne;
    @BindView(R.id.tW_wait_two)
    TextView mTwWaitTwo;
    @BindView(R.id.tw_title_wait)
    TextView mTwTitleWait;

    private MoreActionPopupWindow mPopupWindow;
    private String phone;
    private RouteStateResponse responses;
    private String status;
    private List<PlanNode> mPlanNodes;
    private PlanNode stNode;//启点
    private PlanNode etNode;//终点
    private RouteLine route = null;
    private BaiduMap mBaiduMap;
    private RoutePlanSearch mSearch = null;    // 搜索模块，也可去掉地图模块独立使用
    private BitmapDescriptor bd;
    private BitmapDescriptor car;
    private OnEntityListener entityListener = null;
    private MapUtil mapUtils;
    protected static OverlayOptions overlayOptions;
    private static Overlay overlay = null;
    private String bid;
    private PassengersResponse mPassengersResponse;
    private List<PassengersResponse> mPassengersResponseList;
    private OverlayManager mOverlayManager;
    private static MapStatusUpdate msUpdate = null;
    private static BitmapDescriptor realtimeBitmap;  //图标
    private static OverlayOptions overlay1;  //覆盖物
    private static PolylineOptions polyline = null;  //路线覆盖物
    private static List<LatLng> pointList = new ArrayList<LatLng>();  //定位点的集合
    private int packInterval = 3;
    private RefreshThread refreshThread = null;  //刷新地图线程以获取实时点
    private static OnStartTraceListener startTraceListener = null;  //开启轨迹服务监听器
    private Trace trace;  // 实例化轨迹服务
    private int traceType = 2;  //轨迹服务类型
    /*private double driverLongitude;
    private double driverLatitude;*/
    private String uid;
    private String driverCode;

    @Override
    protected void setupActivityComponent(AppComponent appComponent) {
        DaggerTravelDetailsComponent
                .builder()
                .appComponent(appComponent)
                .travelDetailsModule(new TravelDetailsModule(this)) //请将TravelDetailsModule()第一个首字母改为小写
                .build()
                .inject(this);
    }

    @Override
    protected View initView() {
        return LayoutInflater.from(this).inflate(R.layout.activity_travel_details, null, false);
    }

    @Override
    protected void initData() {
        initTitle();
        initMap();
        Bundle pBundle = getIntent().getExtras();
        if (pBundle != null) {
            responses = (RouteStateResponse) pBundle.getSerializable(Constant.STATUS);
            if (responses!=null&&!TextUtils.isEmpty(responses.getBID())){
//                mPopupWindow = new MoreActionPopupWindow(this, EventBusTags.WAITING_PAYMENT,responses.getBID());
                /*status = responses.getStatus();
                bid = responses.getBID();
                mPresenter.setRouteStateResponse(responses);
                showStatus(status,responses);
                mPopupWindow = new MoreActionPopupWindow(this, EventBusTags.TRAVEL_DETAILS, responses.getBID());*/
                setPassengersResponseInfo(responses);
            }else{
                bid = pBundle.getString(Constant.BID);
                if(bid != null) {
                    mPresenter.getRouteState(bid);
                }
            }
        }else{
//            mPresenter.getRouteState();
        }
        /*Bundle bundle = getIntent().getExtras();
        responses = bundle.getParcelable(Constant.STATUS);
        if(responses != null) {
            pStatus = responses.getStatus();
        }else{
//            mPresenter.getRouteState();
        }*/

//        pathPlanning(getList());//路径规划
//        initQuery();
    }

    @Override
    public void setPassengersResponseInfo(RouteStateResponse response){
        mPassengersResponse = new PassengersResponse();
        mPassengersResponse.setOrderNo(response.getOrderNo());
        mPassengersResponse.setPickupAddress(response.getPickupAddress());
        mPassengersResponse.setDestAddress(response.getDestAddress());
        mPassengersResponse.setStatus(response.getStatus());
        mPassengersResponse.setChildStatus(response.getChildStatus());
        mPassengersResponse.setPickupTime(response.getPickupTime());
        mPassengersResponse.setPickupLongitude(response.getPickupLongitude());
        mPassengersResponse.setPickupLatitude(response.getPickupLatitude());
        mPassengersResponse.setDestLongitude(response.getDestLongitude());
        mPassengersResponse.setDestLatitude(response.getDestLagitude());
        driverCode = response.getDriverCode();
        trace = new Trace(getApplicationContext(), Constant.SERVICEID, driverCode, traceType);
        mWeApplication.getClient().startTrace(trace, startTraceListener);
        status = response.getStatus();
        bid = response.getBID();
        mPresenter.setRouteStateResponse(response);
        mPopupWindow = new MoreActionPopupWindow(this, EventBusTags.TRAVEL_DETAILS, response.getBID());
        for(int i = 0; i < response.getExt().size(); i++){
            if(response.getExt().get(i).getName().equals(Constant.CURRENT_PICKUP)){
                uid = response.getExt().get(i).getJsonData();
            }
        }
        showStatus(status,response);
    }
    public void initTitle(){
        setTitle(getString(R.string.travel_detail));
        mToolbar.setNavigationIcon(ContextCompat.getDrawable(this, R.mipmap.ic_back));
        mToolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        setRightFunction(R.mipmap.ic_more, this);
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
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.iv_function_right:
                if (mPopupWindow != null) {
                    mPopupWindow.showPopupWindow(idLine);
                }
                break;
        }
    }

    @OnClick({R.id.iw_zoom,R.id.iw_zoom_nomal,R.id.iw_mobile,R.id.rl_go_to_pay})
    public void myOnClick(View view){
        switch(view.getId()){
            case R.id.iw_zoom:
                AllGone();
                break;
            case R.id.iw_zoom_nomal:
                if(status != null) {
                    showStatusAll(status,responses);
                }
                break;
            case R.id.iw_mobile:
                if(phone != null){
                    callMobile(phone);
                }
                break;
            case R.id.rl_go_to_pay:
                if(bid != null) {
                    mPresenter.isConfirmArrive(bid);
                }
                break;
        }
    }

    //有数据请求
    public void showStatus(String  status,RouteStateResponse responses){
        /*route = null;
        mBaiduMap.clear();*/
        switch (status){
            case Constant.INHAND: //派单进行中
                if(bid != null) {
                    mPresenter.getPassengersInfo(bid);
                }
                showChildStatus(responses.getChildStatus());
                break;
            case Constant.BOOKSUCCESS: //派单成功
                setAddress();
                waitPickup();
                break;
            case Constant.REGISTERED:
//                orderGoingDay();//白天 晚上 判断起飞时间是13点以前还是以后
                setAddress();
                if(responses.isMorning()){
                    orderGoingDay();
                }else{
                    orderGoing();
                }
                break;
            case Constant.ARRIVED:
                arrive();
                break;
        }
    }

    public void showChildStatus(String status){
        switch (status){
            case Constant.TOSEND:
                Peer();
                break;
            case Constant.PICKUP:
                pickup();
                break;
            case Constant.ABORAD:
                already();
                break;
        }
    }

    //无数据请求
    public void showStatusAll(String  status,RouteStateResponse responses){
        /*route = null;
        mBaiduMap.clear();*/
        switch (status){
            case Constant.INHAND: //派单进行中
                showChildStatus(responses.getChildStatus());
                break;
            case Constant.BOOKSUCCESS: //派单成功
                waitPickup();
                break;
            case Constant.REGISTERED:
//                orderGoingDay();//白天 晚上 判断起飞时间是13点以前还是以后
                if(responses.isMorning()){
                    orderGoingDay();
                }else{
                    orderGoing();
                }
                break;
            case Constant.ARRIVED:
                arrive();
                break;
        }
    }

    @Subscriber(tag = EventBusTags.TRAVEL_DETAILS)
    public void onEventMainThread(TravelCancelEvent event) {
        switch (event.getEvent()) {
            case Constant.TRAVEL_CANCEL:
                Bundle pBundle=new Bundle();
                pBundle.putString(Constant.BID,event.getBid());
                pBundle.putString(Constant.STATUS,Constant.CLEAR_PORT);
                startActivity(TravelCancelActivity.class,pBundle);
                break;
            case Constant.TRAVEL_CUSTOMER:
                showMessage("联系客户");
                break;
        }
    }

    public void setAddress(){
        if(responses != null &&responses.getDestAddress() != null) {
            pathTwo(responses.getPickupLatitude(), responses.getPickupLongitude(),
                    responses.getDestLagitude(), responses.getDestLongitude(),responses.getDestAddress());
        }
    }

    public void orderGoingDay(){ //预约成功 白班
        mIwZoomNomal.setVisibility(View.GONE);
        mIwZoom.setVisibility(View.VISIBLE);
        mLlOrdering.setVisibility(View.GONE);//派单中
        mLlComplete.setVisibility(View.VISIBLE);//派单成功
        mLlDriverInfo.setVisibility(View.GONE); //司机信息
        mLlArrive.setVisibility(View.GONE);  // 确认到达
        mTwTitle.setText(getResources().getString(R.string.travel_ordering_success));
        mTwText.setText(getResources().getString(R.string.travel_ordering_info_day));
        /*if(responses != null &&responses.getDestAddress() != null) {
            pathTwo(responses.getPickupLatitude(), responses.getPickupLongitude(),
                    responses.getDestLagitude(), responses.getDestLongitude(),responses.getDestAddress());
        }*/
    }

    public void orderGoing() {  //预约成功 晚班
        mIwZoomNomal.setVisibility(View.GONE);
        mIwZoom.setVisibility(View.VISIBLE);
        mLlOrdering.setVisibility(View.GONE);//派单中
        mLlComplete.setVisibility(View.VISIBLE);//派单成功
        mLlDriverInfo.setVisibility(View.GONE); //司机信息
        mLlArrive.setVisibility(View.GONE);  // 确认到达
        mTwTitle.setText(getResources().getString(R.string.travel_ordering_success));
        mTwText.setText(getResources().getString(R.string.travel_ordering_info));
        /*if(responses != null &&responses.getDestAddress() != null) {
            pathTwo(responses.getPickupLatitude(), responses.getPickupLongitude(),
                    responses.getDestLagitude(), responses.getDestLongitude(),responses.getDestAddress());
        }*/
    }

    public void pickup(){ //司机正在接您的途中
        mIwZoomNomal.setVisibility(View.GONE);
        mIwZoom.setVisibility(View.VISIBLE);
        mLlOrdering.setVisibility(View.GONE);//派单中
        mLlComplete.setVisibility(View.VISIBLE);//派单成功
        mLlDriverInfo.setVisibility(View.VISIBLE); //司机信息
        mLlArrive.setVisibility(View.GONE);  // 确认到达
        mTwTitle.setText(getResources().getString(R.string.travel_pickup));
        mTwText.setText(getResources().getString(R.string.travel_pickup_info));
    }

    public void Peer(){ //司机正在接您同行乘客的途中
        mIwZoomNomal.setVisibility(View.GONE);
        mIwZoom.setVisibility(View.VISIBLE);
        mLlOrdering.setVisibility(View.GONE);//派单中
        mLlComplete.setVisibility(View.VISIBLE);//派单成功
        mLlDriverInfo.setVisibility(View.VISIBLE); //司机信息
        mLlArrive.setVisibility(View.GONE);  // 确认到达
        mTwTitle.setText(getResources().getString(R.string.travel_peer));
        mTwText.setText(getResources().getString(R.string.travel_pickup_info));
    }

    public void already(){ //你已经上车
        mIwZoomNomal.setVisibility(View.GONE);
        mIwZoom.setVisibility(View.VISIBLE);
        mLlOrdering.setVisibility(View.GONE);//派单中
        mLlComplete.setVisibility(View.VISIBLE);//派单成功
        mLlDriverInfo.setVisibility(View.VISIBLE); //司机信息
        mLlArrive.setVisibility(View.GONE);  // 确认到达
        mTwTitle.setText(getResources().getString(R.string.travel_already));
        mTwText.setText(getResources().getString(R.string.travel_already_info));
        /*if(responses != null &&responses.getDestAddress() != null) {
            pathTwo(responses.getPickupLatitude(), responses.getPickupLongitude(),
                    responses.getDestLagitude(), responses.getDestLongitude(),responses.getDestAddress());
        }*/
    }

    public void waitPickup(){ //等待接驾
        mIwZoomNomal.setVisibility(View.GONE);
        mIwZoom.setVisibility(View.VISIBLE);
        mLlOrdering.setVisibility(View.VISIBLE);//派单中
        mLlComplete.setVisibility(View.GONE);//派单成功
        mLlDriverInfo.setVisibility(View.VISIBLE); //司机信息
        mLlArrive.setVisibility(View.GONE);  // 确认到达
        mTwTitle.setText(getResources().getString(R.string.travel_peer));
        mTwText.setText(getResources().getString(R.string.travel_pickup_info));
    }

    public void arrive() { //确认到达
        mIwZoomNomal.setVisibility(View.GONE);
        mIwZoom.setVisibility(View.VISIBLE);
        mLlArrive.setVisibility(View.VISIBLE);  // 确认到达
        mLlDriverInfo.setVisibility(View.VISIBLE); //司机信息
        mLlOrdering.setVisibility(View.GONE);//派单中
        mLlComplete.setVisibility(View.GONE);//派单成功
    }

    public void AllGone(){  //隐藏
        mIwZoomNomal.setVisibility(View.VISIBLE);
        mIwZoom.setVisibility(View.GONE);
        mLlArrive.setVisibility(View.GONE);  // 确认到达
        mLlOrdering.setVisibility(View.GONE);//派单中
        mLlComplete.setVisibility(View.GONE);//派单成功
//        mLlDriverInfo.setVisibility(View.GONE); //司机信息
//        mLlGoing.setVisibility(View.GONE); //正在进行中
    }

    @Override
    public void setDriverName(String name) {
        mTwName.setText(name);
    }

    @Override
    public void setDriverRate(String rate) {
        mTwScore.setText(rate);
    }

    @Override
    public void setDriverPhone(String phone) {
        this.phone = phone;
    }

    public void callMobile(String phone){
        Intent intent = new Intent(Intent.ACTION_DIAL);
        Uri data = Uri.parse("tel:" + phone);
        intent.setData(data);
        startActivity(intent);
    }

    @Override
    public void setCarLicense(String liscense) {
        mTwCarNum.setText(liscense);
    }

    @Override
    public void setCarColor(String carColor,String model) {
        mYwCarColor.setText(carColor+"."+model);
    }

    @Override
    public void setStatus(String status) {
        this.status = status;
        showStatus(status,responses);
    }

    @Override
    public void isSuccess() {
//        responses = mPresenter.getData();
        Bundle bundle = new Bundle();
        if(responses != null) {
            bundle.putSerializable(Constant.STATUS, responses);
        }
        startActivity(EstimateActivity.class,bundle);
        /*Intent intent = new Intent(this,EstimateActivity.class);
        launchActivity(intent);*/
        finish();
    }

    @Override
    public void setBid(String bid) {
        this.bid = bid;
        mPopupWindow = new MoreActionPopupWindow(this, EventBusTags.TRAVEL_DETAILS, responses.getBID());
    }

    @Override
    public void setPassengersInfo(List<PassengersResponse> info) {
        initOnEntityListener();
        setTraceServer();
        mPassengersResponseList = info;

        /*for(int i= 0 ; i < info.size() ;i++){
            if(mPassengersResponse.getChildStatus().equals(Constant.ABORAD)){
                info.remove(info.get(i));
            }
        }
        if(!mPassengersResponse.getChildStatus().equals(Constant.TOSEND) ||
                mPassengersResponse.getChildStatus().equals(Constant.PICKUP)) {
            info.add(mPassengersResponse);
        }
        Collections.sort(info);
        if(responses.getDestAddress() != null) {
            pathPlanning(info, driverLatitude, driverLongitude, responses.getDestAddress());
        }else{
            pathPlanning(info, driverLatitude, driverLongitude, Constant.AIRPORT_T1);
        }*/
        mapLocation(info,mPassengersResponse);
    }

    public void mapLocation(List<PassengersResponse> info,PassengersResponse mPassengersResponse){

        List<PassengersResponse> passengersResponseList = new ArrayList<>();
        if(info != null) {
            info.add(mPassengersResponse);
            Collections.sort(info);
        }
        for(int i= 0 ; i < info.size() ;i++){
            if(info.get(i).getChildStatus().equals(Constant.ABORAD)){
                passengersResponseList.add(info.get(i));
                info.remove(info.get(i));
            }
        }
        Collections.sort(passengersResponseList);
        if(info != null && info.size() > 0){
            if(uid != null){
                for(int i = 0; i < info.size(); i++){
                    if(info.get(i).getUID()!= null && info.get(i).getUID().equals(uid)){
                        passengersResponseList.add(info.get(i));
                        info.remove(info.get(i));
                    }
                }
            }
            passengersResponseList.addAll(info);
        }
//        info.remove(0);
        double latitude = passengersResponseList.get(0).getPickupLatitude();
        double longtude = passengersResponseList.get(0).getPickupLongitude();
        passengersResponseList.remove(0);
        if(responses.getDestAddress() != null) {
            pathPlanning(passengersResponseList,latitude,longtude, responses.getDestAddress());
        }else{
            pathPlanning(passengersResponseList, latitude, longtude, Constant.AIRPORT_T1);
        }
    }

    //开启轨迹
    public void setTraceServer(){
        initOnStartTraceListener();
        mWeApplication.getClient().startTrace(trace, startTraceListener);  // 开启轨迹服务
    }

    public void initMap(){
        mBaiduMap = mMapview.getMap();
        // 隐藏缩放控件
        mMapview.showZoomControls(false);
        //地图上比例尺
        mMapview.showScaleControl(false);
        mSearch = RoutePlanSearch.newInstance();
        mSearch.setOnGetRoutePlanResultListener(this);
        //覆盖物初始化
        bd = BitmapDescriptorFactory
                .fromResource(R.mipmap.ic_location_customer);
        car = BitmapDescriptorFactory.fromResource(R.mipmap.ic_location_car);
    }

    //路径规划 route  stNode设置一个假的 Latitude 30.542191  Longitude 104.066535
    private void pathPlanning (List<PassengersResponse> planningListt,double
                              latitude,double longitudu,String address){
        mPlanNodes = new ArrayList<>();
        stNode = PlanNode.withLocation(new LatLng(latitude,longitudu));//起点 104.083864,30.622657
        /*mSearch.drivingSearch((new DrivingRoutePlanOption())
                .from(stNode).to(enNode));*/
        for (int i = 0; i < planningListt.size(); i++) {
            mPlanNodes.add(PlanNode.withLocation(new LatLng
                    (planningListt.get(i).getPickupLatitude(),
                            planningListt.get(i).getPickupLongitude())));
        }
        DrivingRoutePlanOption drivingRoutePlanOption = new DrivingRoutePlanOption();
        drivingRoutePlanOption.from(stNode);
        drivingRoutePlanOption.to(PlanNode.withCityNameAndPlaceName("成都", address));
        if(mPlanNodes.size() > 0) {
            drivingRoutePlanOption.passBy(mPlanNodes);
        }
        mSearch.drivingSearch(drivingRoutePlanOption);
        initMarker(planningListt); //初始化覆盖物
    }

    private void pathTwo(double startlatitude, double startlongitude
        , double endlatitude, double endlongitude,String address){
        stNode = PlanNode.withLocation(new LatLng(startlatitude,startlongitude));//起点 104.083864,30.622657
        etNode = PlanNode.withLocation(new LatLng(endlatitude,endlongitude));
        /*mSearch.drivingSearch((new DrivingRoutePlanOption())
                .from(stNode).to(enNode));*/
        DrivingRoutePlanOption drivingRoutePlanOption = new DrivingRoutePlanOption();
        drivingRoutePlanOption.from(stNode);
//        drivingRoutePlanOption.to(etNode);
        drivingRoutePlanOption.to(PlanNode.withCityNameAndPlaceName("成都", address));
        mSearch.drivingSearch(drivingRoutePlanOption);
    }

    @Override
    public void onGetWalkingRouteResult(WalkingRouteResult walkingRouteResult) {

    }

    @Override
    public void onGetTransitRouteResult(TransitRouteResult transitRouteResult) {

    }

    @Override
    public void onGetMassTransitRouteResult(MassTransitRouteResult massTransitRouteResult) {

    }

    @Override
    public void onGetDrivingRouteResult(DrivingRouteResult result) {
        if (result == null || result.error != SearchResult.ERRORNO.NO_ERROR) {
            //Toast.makeText(getActivity(), "抱歉，未找到结果", Toast.LENGTH_SHORT).show();
        }
        if (result.error == SearchResult.ERRORNO.AMBIGUOUS_ROURE_ADDR) {
            // 起终点或途经点地址有岐义，通过以下接口获取建议查询信息
            //result.getSuggestAddrInfo();
            return;
        }
        if (result.error == SearchResult.ERRORNO.NO_ERROR) {
                route = result.getRouteLines().get(0);
                MyDrivingRouteOverlay overlay = new MyDrivingRouteOverlay(mBaiduMap, this);
                mOverlayManager = overlay;
                mBaiduMap.setOnMarkerClickListener(overlay);
                overlay.setData(result.getRouteLines().get(0));
                overlay.addToMap();
                overlay.zoomToSpan();
                return;
            }
    }

    @Override
    public void onGetIndoorRouteResult(IndoorRouteResult indoorRouteResult) {

    }

    @Override
    public void onGetBikingRouteResult(BikingRouteResult bikingRouteResult) {

    }

    //测试数据
    /*public List<PathPlanning> getList(){
        //104.064317,30.667025  Latitude 30.542191  Longitude 104.066535
       // 104.050519,30.644657  104.092488,30.655593  104.094788,30.68504
        List<PathPlanning> pathPlanningList = new ArrayList<>();
        double[] mLatitude = new double[]
                {30.667025,30.542191,30.644657,30.655593,30.68504};
        double[] mLongitude = new double[]
                {104.064317,104.066535,104.050519,104.092488,104.094788};
        for(int i= 0; i< mLatitude.length; i++){
            PathPlanning pathPlanning = new PathPlanning();
            pathPlanning.setLatitude(mLatitude[i]);
            pathPlanning.setLongitude(mLongitude[i]);
            pathPlanningList.add(pathPlanning);
        }
        return pathPlanningList;
    }*/

    @Override
    public boolean onMarkerClick(Marker marker) {
        return false;
    }

    public void initMarker(List<PassengersResponse> planningListt){

        for(int i = 0; i < planningListt.size(); i++){
            LatLng ll = new LatLng(planningListt.get(i).getPickupLatitude(),
                    planningListt.get(i).getPickupLongitude());
            MarkerOptions markerOptions = new MarkerOptions()
                    .position(ll).icon(bd).zIndex(9).draggable(true);
            mBaiduMap.addOverlay(markerOptions);
        }
    }

    @Override
    protected void onPause() {
        // MapView的生命周期与Activity同步，当activity挂起时需调用MapView.onPause()
        mMapview.onPause();
        super.onPause();
    }

    @Override
    protected void onResume() {
        // MapView的生命周期与Activity同步，当activity恢复时需调用MapView.onResume()
        mMapview.onResume();
        super.onResume();
    }

    @Override
    protected void onDestroy() {
        // MapView的生命周期与Activity同步，当activity销毁时需调用MapView.destroy()
        mMapview.onDestroy();
        super.onDestroy();
        // 回收 bitmap 资源
        bd.recycle();
    }

    /*Handler handler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what){
                case 1:
                queryEntityList();
                break;
            }
        }
    };

    public void initQuery(){
        initOnEntityListener();
        Timer timer = new Timer();
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                Message msg = new Message();
                msg.what = 1;
                handler.sendMessage(msg);
            }
        }, 1000, 5000);
    }*/
    /** 追踪开始 */
    private void initOnStartTraceListener() {

        // 实例化开启轨迹服务回调接口
        startTraceListener = new OnStartTraceListener() {
            // 开启轨迹服务回调接口（arg0 : 消息编码，arg1 : 消息内容，详情查看类参考）
            @Override
            public void onTraceCallback(int arg0, String arg1) {
                Log.i("TAG", "onTraceCallback=" + arg1);
                if(arg0 == 0 || arg0 == 10006){
                    startRefreshThread(true);
                }
            }

            // 轨迹服务推送接口（用于接收服务端推送消息，arg0 : 消息类型，arg1 : 消息内容，详情查看类参考）
            @Override
            public void onTracePushCallback(byte arg0, String arg1) {
                Log.i("TAG", "onTracePushCallback=" + arg1);
            }
        };



    }

    /**
     * 查询entityList
     */
    private void queryEntityList() {
        // entity标识列表（多个entityName，以英文逗号"," 分割）

        // 属性名称（格式为 : "key1=value1,key2=value2,....."）
        String columnKey = "";
        // 返回结果的类型（0 : 返回全部结果，1 : 只返回entityName的列表）
        int returnType = 0;
        // 活跃时间（指定该字段时，返回从该时间点之后仍有位置变动的entity的实时点集合）

        int activeTime = 0;

        // 分页大小
        int pageSize = 10;
        // 分页索引
        int pageIndex = 1;

        mWeApplication.getClient().queryEntityList(Constant.SERVICEID, driverCode, columnKey, returnType, activeTime,
                pageSize,
                pageIndex, entityListener);
    }

    /**
     * 初始化OnEntityListener
     */
    private void initOnEntityListener() {
        entityListener = new OnEntityListener() {

            // 请求失败回调接口
            @Override
            public void onRequestFailedCallback(String arg0) {
                // TODO Auto-generated method stub
            }

            // 添加entity回调接口
            public void onAddEntityCallback(String arg0) {
                // TODO Auto-generated method stub
            }

            // 查询entity列表回调接口
            @Override
            public void onQueryEntityListCallback(String message) {
                // TODO Auto-generated method stub
                /*TraceLocation entityLocation = new TraceLocation();
                try {
                    JSONObject dataJson = new JSONObject(message);
                    if (null != dataJson && dataJson.has("status") && dataJson.getInt("status") == 0
                            && dataJson.has("size") && dataJson.getInt("size") > 0) {
                        JSONArray entities = dataJson.getJSONArray("entities");
                        JSONObject entity = entities.getJSONObject(0);
                        JSONObject point = entity.getJSONObject("realtime_point");
                        JSONArray location = point.getJSONArray("location");
//                        setCar(location.getDouble(1),location.getDouble(0));
                        *//*entityLocation.setLongitude(location.getDouble(0));
                        entityLocation.setLatitude(location.getDouble(1));*//*
                    }
                } catch (JSONException e) {
                    // TODO Auto-generated catch block
                    return;
                }*/
                try {
                    //数据以JSON形式存取
                    RealtimeTrackData realtimeTrackData = new Gson().fromJson(message, RealtimeTrackData.class);

                    if (realtimeTrackData != null && realtimeTrackData.getStatus() == 0) {

                        LatLng latLng = realtimeTrackData.getRealtimePoint();
                      /*  driverLongitude = latLng.longitude;
                        driverLatitude = latLng.latitude;*/

                        if (latLng != null) {
                            pointList.add(latLng);
                            drawRealtimePoint(latLng);
                        } else {
//                        Toast.makeText(getApplicationContext(), "当前无轨迹点", Toast.LENGTH_LONG).show();
                        }

                    }
                }catch (Exception e){
                    return;
                }
            }

            @Override
            public void onReceiveLocation(TraceLocation location) {
                // TODO Auto-generated method stub
            }
        };
    }

    /*public void setCar(double mLatitude ,double mLongitude ){

        if (null != overlay && mLatitude != 0 && mLongitude != 0) {
            overlay.remove();
        }else {
            LatLng ll = new LatLng(mLatitude, mLongitude);
            overlayOptions = new MarkerOptions().position(ll)
                    .icon(car).zIndex(9).draggable(true);
            // 实时点覆盖物
            if (null != overlayOptions) {
                overlay = mBaiduMap.addOverlay(overlayOptions);
            }
        }
    }*/

    /**
     * 画出实时线路点
     * @param point
     */
    private void drawRealtimePoint(LatLng point){

        mBaiduMap.clear();
        MapStatus mapStatus = new MapStatus.Builder().target(point).zoom(18).build();
        msUpdate = MapStatusUpdateFactory.newMapStatus(mapStatus);
        realtimeBitmap = BitmapDescriptorFactory.fromResource(R.mipmap.ic_location_car);
        overlay1 = new MarkerOptions().position(point)
                .icon(realtimeBitmap).zIndex(9).draggable(true);

        if(pointList.size() >= 2  && pointList.size() <= 1000){
            polyline = new PolylineOptions().width(10).color(Color.TRANSPARENT).points(pointList);
        }

        addMarker();

    }


    private void addMarker(){

        if(msUpdate != null){
            mBaiduMap.setMapStatus(msUpdate);
        }

        if(polyline != null){
            mBaiduMap.addOverlay(polyline);
        }

        if(overlay1 != null){
            mBaiduMap.addOverlay(overlay1);
        }
    }

    /**
     * 轨迹刷新线程
     * @author BLYang
     */
    private class RefreshThread extends Thread{

        protected boolean refresh = true;

        public void run(){

            while(refresh){
                queryEntityList();
                try{
                    Thread.sleep(packInterval * 1000);
                }catch(InterruptedException e){
//                    System.out.println("线程休眠失败");
                }
            }

        }
    }


    /**
     * 启动刷新线程
     * @param isStart
     */
    private void startRefreshThread(boolean isStart){

        if(refreshThread == null){
            refreshThread = new RefreshThread();
        }

        refreshThread.refresh = isStart;

        if(isStart){
            if(!refreshThread.isAlive()){
                refreshThread.start();
            }
        }
        else{
            refreshThread = null;
        }
    }

    @Subscriber(tag=EventBusTags.AIRPORT_PUSH_OFF)
    public void getPushData(PushResponse response){
        mPresenter.getRouteState(response.getBID());
    }

    private class MyDrivingRouteOverlay extends DrivingRouteOverlay {

        public MyDrivingRouteOverlay(BaiduMap baiduMap, Context context) {
            super(baiduMap,context);
        }

        @Override
        public BitmapDescriptor getStartMarker() {
            return BitmapDescriptorFactory.fromResource(R.mipmap.ic_location_start);
        }

        @Override
        public BitmapDescriptor getTerminalMarker() {
            return BitmapDescriptorFactory.fromResource(R.mipmap.ic_location_end);
        }
    }

    @Subscriber(tag = EventBusTags.TRAVEL_DETAIL)
    public void travelDetail(PushResponse response){
        mPresenter.getRouteState(response.getBID());
    }

}
