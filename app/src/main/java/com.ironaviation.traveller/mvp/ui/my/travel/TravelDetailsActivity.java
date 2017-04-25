package com.ironaviation.traveller.mvp.ui.my.travel;

import android.content.DialogInterface;
import android.content.Intent;
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

import com.baidu.mapapi.map.BaiduMap;
import com.baidu.mapapi.map.BitmapDescriptor;
import com.baidu.mapapi.map.BitmapDescriptorFactory;
import com.baidu.mapapi.map.MapView;
import com.baidu.mapapi.map.Marker;
import com.baidu.mapapi.map.MarkerOptions;
import com.baidu.mapapi.map.Overlay;
import com.baidu.mapapi.map.OverlayOptions;
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
import com.baidu.trace.TraceLocation;
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
import com.ironaviation.traveller.mvp.constant.Constant;
import com.ironaviation.traveller.mvp.contract.my.travel.TravelDetailsContract;
import com.ironaviation.traveller.mvp.model.entity.request.PathPlanning;
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
    private LBSTraceClient mLBSTraceClient;
    private OnEntityListener entityListener = null;
    private MapUtil mapUtils;
    private LBSTraceClient client;
    protected static OverlayOptions overlayOptions;
    private static Overlay overlay = null;

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
        setRightFunction(R.mipmap.ic_more, this);
        Bundle pBundle = getIntent().getExtras();
        if (pBundle != null) {
            responses = (RouteStateResponse) pBundle.getSerializable(Constant.STATUS);
            if (responses!=null&&!TextUtils.isEmpty(responses.getBID())){
                mPopupWindow = new MoreActionPopupWindow(this, EventBusTags.WAITING_PAYMENT,responses.getBID());
//                status = responses.getStatus();
                mPresenter.setRouteStateResponse(responses);
                showStatus(status);
                mPopupWindow = new MoreActionPopupWindow(this, EventBusTags.TRAVEL_DETAILS,responses.getBID());
                showStatus(responses.getStatus());
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
    public void initTitle(){
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
                showStatus(status);
                break;
            case R.id.iw_mobile:
                if(phone != null){
                    callMobile(phone);
                }
                break;
            case R.id.rl_go_to_pay:
                Bundle bundle = new Bundle();
                responses = mPresenter.getData();
                bundle.putSerializable(Constant.STATUS,responses);
                startActivity(EstimateActivity.class,bundle);
                break;
        }
    }

    public void showStatus(String  status){
        switch (status){
            case Constant.INHAND: //派单进行中
                showChildStatus(responses.getChildStatus());
                break;
            case Constant.BOOKSUCCESS:
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

    @Subscriber(tag = EventBusTags.TRAVEL_DETAILS)
    public void onEventMainThread(TravelCancelEvent event) {
        switch (event.getEvent()) {
            case Constant.TRAVEL_CANCEL:
                Bundle pBundle=new Bundle();
                pBundle.putString(Constant.BID,event.getBid());
                startActivity(TravelCancelActivity.class,pBundle);
                break;
            case Constant.TRAVEL_CUSTOMER:
                showMessage("联系客户");
                break;
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
        pathTwo(responses.getPickupLatitude(),responses.getPickupLongitude(),
                responses.getDestLagitude(),responses.getDestLongitude());
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
        pathTwo(responses.getPickupLatitude(),responses.getPickupLongitude(),
                responses.getDestLagitude(),responses.getDestLongitude());
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
    }

    @Override
    public void isSuccess() {
        Intent intent = new Intent(this,EstimateActivity.class);
        launchActivity(intent);
        finish();
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
                .fromResource(R.drawable.ic_gcoding);
        car = BitmapDescriptorFactory.fromResource(R.drawable.arrow);
    }

    //路径规划 route  stNode设置一个假的 Latitude 30.542191  Longitude 104.066535
    private void pathPlanning (List<PathPlanning> planningListt){
        mPlanNodes = new ArrayList<>();
        stNode = PlanNode.withLocation(new LatLng(30.622657,104.083864));//起点 104.083864,30.622657
        /*mSearch.drivingSearch((new DrivingRoutePlanOption())
                .from(stNode).to(enNode));*/
        for (int i = 0; i < planningListt.size(); i++) {
            mPlanNodes.add(PlanNode.withLocation(new LatLng
                    (planningListt.get(i).getLatitude(),
                            planningListt.get(i).getLongitude())));
        }
        DrivingRoutePlanOption drivingRoutePlanOption = new DrivingRoutePlanOption();
        drivingRoutePlanOption.from(stNode);
        drivingRoutePlanOption.to(PlanNode.withCityNameAndPlaceName("成都", "双流机场T1航站楼"));
        drivingRoutePlanOption.passBy(mPlanNodes);
        mSearch.drivingSearch(drivingRoutePlanOption);
        initMarker(planningListt); //初始化覆盖物
    }

    private void pathTwo(double startlatitude, double startlongitude
        , double endlatitude, double endlongitude){
        stNode = PlanNode.withLocation(new LatLng(startlatitude,startlongitude));//起点 104.083864,30.622657
        etNode = PlanNode.withLocation(new LatLng(endlatitude,endlongitude));
        /*mSearch.drivingSearch((new DrivingRoutePlanOption())
                .from(stNode).to(enNode));*/
        DrivingRoutePlanOption drivingRoutePlanOption = new DrivingRoutePlanOption();
        drivingRoutePlanOption.from(stNode);
//        drivingRoutePlanOption.to(etNode);
        drivingRoutePlanOption.to(PlanNode.withCityNameAndPlaceName("成都", "双流机场T1航站楼"));
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
                DrivingRouteOverlay overlay = new DrivingRouteOverlay(mBaiduMap, this);
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

    public List<PathPlanning> getList(){
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
    }

    @Override
    public boolean onMarkerClick(Marker marker) {
        return false;
    }

    public void initMarker(List<PathPlanning> planningListt){
        List<LatLng> latLngs = new ArrayList<>();
        for(int i = 0; i < planningListt.size(); i++){
            LatLng ll = new LatLng(planningListt.get(i).getLatitude(),
                    planningListt.get(i).getLongitude());
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

    Handler handler = new Handler(){
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
        client = new LBSTraceClient(this);
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

        int activeTime = (int) (System.currentTimeMillis() / 1000 - 5);

        // 分页大小
        int pageSize = 10;
        // 分页索引
        int pageIndex = 1;

        client.queryEntityList(Constant.SERVICEID, "958", columnKey, returnType, activeTime,
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
                TraceLocation entityLocation = new TraceLocation();
                try {
                    JSONObject dataJson = new JSONObject(message);
                    if (null != dataJson && dataJson.has("status") && dataJson.getInt("status") == 0
                            && dataJson.has("size") && dataJson.getInt("size") > 0) {
                        JSONArray entities = dataJson.getJSONArray("entities");
                        JSONObject entity = entities.getJSONObject(0);
                        JSONObject point = entity.getJSONObject("realtime_point");
                        JSONArray location = point.getJSONArray("location");
                        setCar(location.getDouble(1),location.getDouble(0));
                        /*entityLocation.setLongitude(location.getDouble(0));
                        entityLocation.setLatitude(location.getDouble(1));*/
                    }
                } catch (JSONException e) {
                    // TODO Auto-generated catch block
                    return;
                }
            }

            @Override
            public void onReceiveLocation(TraceLocation location) {
                // TODO Auto-generated method stub
            }
        };
    }

    public void setCar(double mLatitude ,double mLongitude ){

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
    }
}
