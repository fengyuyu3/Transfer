package com.ironaviation.traveller.mvp.ui.my.travel;

import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
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
    @BindView(R.id.ll_going)
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

    private MoreActionPopupWindow mPopupWindow;
    private String phone;
    private RouteStateResponse responses;
    private String status;
    private List<PlanNode> mPlanNodes;
    private PlanNode stNode;//启点
    private PlanNode etNode;//终点
    private RouteLine route = null;
    private BaiduMap mBaiduMap;
    RoutePlanSearch mSearch = null;    // 搜索模块，也可去掉地图模块独立使用
    private BitmapDescriptor bd;
    private BitmapDescriptor car;
    private LBSTraceClient mLBSTraceClient;
    private static OnEntityListener entityListener = null;

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
        initMap();
        setRightFunction(R.mipmap.ic_more, this);
        Bundle pBundle = getIntent().getExtras();
        if (pBundle != null) {
            responses = pBundle.getParcelable(Constant.STATUS);
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
                status = Constant.ARRIVED;
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

        pathPlanning(getList());
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
                going();
                break;
            case Constant.BOOKSUCCESS:
                complete();
                break;
            case Constant.REGISTERED:
                order();
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
                startActivity(TravelCancelActivity.class,pBundle);
                break;
            case Constant.TRAVEL_CUSTOMER:
                showMessage("联系客户");
                break;
        }
    }


    public void going() {  //派单进行中
        mIwZoomNomal.setVisibility(View.GONE);
        mIwZoom.setVisibility(View.VISIBLE);
        mLlGoing.setVisibility(View.VISIBLE); //正在进行中
        mLlDriverInfo.setVisibility(View.VISIBLE); //司机信息
        mLlComplete.setVisibility(View.GONE);//派单成功
        mLlOrdering.setVisibility(View.GONE);//派单中
        mLlArrive.setVisibility(View.GONE);  // 确认到达a
    }

    public void complete() { //派单成功
        mIwZoomNomal.setVisibility(View.GONE);
        mIwZoom.setVisibility(View.VISIBLE);
        mLlComplete.setVisibility(View.VISIBLE);//派单成功
        mLlDriverInfo.setVisibility(View.VISIBLE); //司机信息
        mLlOrdering.setVisibility(View.GONE);//派单中
        mLlArrive.setVisibility(View.GONE);  // 确认到达
        mLlGoing.setVisibility(View.GONE); //正在进行中
    }

    public void order() { //预约成功
        mIwZoomNomal.setVisibility(View.GONE);
        mIwZoom.setVisibility(View.VISIBLE);
        mLlOrdering.setVisibility(View.VISIBLE);//派单中
        mLlComplete.setVisibility(View.GONE);//派单成功
        mLlDriverInfo.setVisibility(View.GONE); //司机信息
        mLlArrive.setVisibility(View.GONE);  // 确认到达
        mLlGoing.setVisibility(View.GONE); //正在进行中
    }

    public void arrive() { //确认到达
        mIwZoomNomal.setVisibility(View.GONE);
        mIwZoom.setVisibility(View.VISIBLE);
        mLlArrive.setVisibility(View.VISIBLE);  // 确认到达
        mLlDriverInfo.setVisibility(View.VISIBLE); //司机信息
        mLlOrdering.setVisibility(View.GONE);//派单中
        mLlComplete.setVisibility(View.GONE);//派单成功
        mLlGoing.setVisibility(View.GONE); //正在进行中
    }

    public void AllGone(){
        mIwZoomNomal.setVisibility(View.VISIBLE);
        mIwZoom.setVisibility(View.GONE);
        mLlArrive.setVisibility(View.GONE);  // 确认到达
        mLlOrdering.setVisibility(View.GONE);//派单中
        mLlComplete.setVisibility(View.GONE);//派单成功
        mLlGoing.setVisibility(View.GONE); //正在进行中
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

    /**
     * 查询实时轨迹
     */
    private void queryRealtimeLoc() {
        initOnEntityListener();
        mLBSTraceClient = new LBSTraceClient(this);
        mLBSTraceClient.queryRealtimeLoc(Constant.SERVICEID, entityListener);
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
                        /*entityLocation.setLongitude(location.getDouble(0));
                        entityLocation.setLatitude(location.getDouble(1));*/
                        setCar(location.getDouble(1),location.getDouble(0));
                    }
                } catch (JSONException e) {
                    // TODO Auto-generated catch block
                    return;
                }
//                showRealtimeTrack(entityLocation);
            }

            @Override
            public void onReceiveLocation(TraceLocation location) {
                // TODO Auto-generated method stub
//                showRealtimeTrack(location);
            }

        };
    }
    public void setCar(double mLatitude ,double mLongitude ){
        mBaiduMap.clear();
        initMarker(getList());
        LatLng ll = new LatLng(mLatitude,mLongitude);
        MarkerOptions markerOptions = new MarkerOptions()
                .position(ll).icon(car).zIndex(9).draggable(true);
        mBaiduMap.addOverlay(markerOptions);
    }
}
