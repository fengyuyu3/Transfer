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

import com.baidu.mapapi.map.BaiduMap;
import com.baidu.mapapi.map.BitmapDescriptor;
import com.baidu.mapapi.map.BitmapDescriptorFactory;
import com.baidu.mapapi.map.MapView;
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
import com.ironaviation.traveller.R;
import com.ironaviation.traveller.app.EventBusTags;
import com.ironaviation.traveller.common.AppComponent;
import com.ironaviation.traveller.common.WEActivity;
import com.ironaviation.traveller.di.component.my.travel.DaggerTravelDetailsOnComponent;
import com.ironaviation.traveller.di.module.my.travel.TravelDetailsOnModule;
import com.ironaviation.traveller.map.overlayutil.DrivingRouteOverlay;
import com.ironaviation.traveller.mvp.constant.Constant;
import com.ironaviation.traveller.mvp.contract.my.travel.TravelDetailsOnContract;
import com.ironaviation.traveller.mvp.model.entity.request.PathPlanning;
import com.ironaviation.traveller.mvp.model.entity.response.PushResponse;
import com.ironaviation.traveller.mvp.model.entity.response.RouteStateResponse;
import com.ironaviation.traveller.mvp.presenter.my.travel.TravelDetailsOnPresenter;
import com.ironaviation.traveller.mvp.ui.my.EstimateActivity;
import com.ironaviation.traveller.mvp.ui.my.QRCodeActivity;
import com.ironaviation.traveller.mvp.ui.widget.AutoToolbar;
import com.ironaviation.traveller.mvp.ui.widget.MoreActionPopupWindow;
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
 * Created by Administrator on 2017/4/21.
 */

public class TravelDetailsOnActivity extends WEActivity<TravelDetailsOnPresenter> implements TravelDetailsOnContract.View
,View.OnClickListener,OnGetRoutePlanResultListener {


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
    @BindView(R.id.mapview_on)
    MapView mMapview;
    private String status;
    private RouteStateResponse responses;
    private MoreActionPopupWindow mPopupWindow;
    private PlanNode stNode;//启点
    private PlanNode etNode;//终点
    private RoutePlanSearch mSearch = null;
    private BaiduMap mBaiduMap;
    private BitmapDescriptor bd;
    private BitmapDescriptor car;
    private RouteLine route = null;
    private String bid;
    private List<PlanNode> mPlanNodes;

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
        initMap();
        Bundle pBundle = getIntent().getExtras();
        if (pBundle != null) {
            responses = (RouteStateResponse) pBundle.getSerializable(Constant.STATUS);
            if (responses != null && !TextUtils.isEmpty(responses.getBID())) {
                status = responses.getStatus();
                bid = responses.getBID();
                mPresenter.setRouteStateResponse(responses);
                mPopupWindow = new MoreActionPopupWindow(this, EventBusTags.TRAVEL_DETAILS, responses.getBID());
//                status = Constant.ARRIVED;
                showStatus(status);
            } else {
                bid = pBundle.getString(Constant.BID);
                if (bid != null) {
                    mPresenter.getRouteState(bid);
                }
            }
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

    @Override
    public void isSuccess() {
        responses = mPresenter.getData();
        Bundle bundle = new Bundle();
        bundle.putSerializable(Constant.STATUS,responses);
        startActivity(EstimateActivity.class,bundle);
        finish();
    }

    @Override
    public void setBid(String bid) {
        this.bid = bid;
    }

    @Override
    public void isPickUpSuccess() {

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
        /*if(responses.getDestAddress() != null) {
            pathTwo(responses.getPickupLatitude(), responses.getPickupLongitude()
                    , responses.getDestLagitude(), responses.getDestLongitude()
                    ,responses.getDestAddress() );
        }*/
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
                showChildStatus(status);
            break;
            case Constant.ARRIVED:
                arrive();
            break;
            case Constant.INHAND:
                 GoOn();
            break;
        }
    }

    public void showChildStatus(String status){
        switch (status){
            case Constant.ABORAD:
                aready();
                break;
            case Constant.PICKUP:
                waitPickUp();
                break;
        }
    }
    /*public void showOrderStatus(String status){
        switch (status){
            case Constant.INHAND:
                GoOn();
                break;
        }
    }*/
    @OnClick({R.id.tw_go_on_pay_airport,R.id.tw_go_to_pay_airport,R.id.al_two_dimension
    ,R.id.tw_address,R.id.iw_zoom_airport,R.id.iw_zoom_nomal_airport})
    public void myOnClick(View view){
        switch (view.getId()){
            case R.id.tw_go_on_pay_airport: //确认到达按钮
                if(bid != null) {
                   mPresenter.isConfirmArrive(bid);
                }
                break;
            case R.id.tw_go_to_pay_airport: //确认上车按钮
                mPresenter.isConfirmPickup(bid);
                break;
            case R.id.al_two_dimension:     //二维码
                twoDimenSion();
                break;
            case R.id.tw_address:           //查看地址
                break;
            case R.id.iw_zoom_nomal_airport: //隐藏
                if(status != null) {
                    showStatus(status);
                }
                break;
            case R.id.iw_zoom_airport:
                AllGone();
                break;
        }
    }
    public void twoDimenSion(){
        String orderOn = responses.getOrderNo();
        if(orderOn != null) {
            Intent intent = new Intent(this, QRCodeActivity.class);
            startActivity(intent);
        }else{
            showMessage(getResources().getString(R.string.travel_order_failed));
        }
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
    public void initMarker(List<PathPlanning> planningListt){
        for(int i = 0; i < planningListt.size(); i++){
            LatLng ll = new LatLng(planningListt.get(i).getLatitude(),
                    planningListt.get(i).getLongitude());
            MarkerOptions markerOptions = new MarkerOptions()
                    .position(ll).icon(bd).zIndex(9).draggable(true);
            mBaiduMap.addOverlay(markerOptions);
        }
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

    @Subscriber(tag = EventBusTags.TRAVEL_DETAIL_ON)
    public void travelDetailOn(PushResponse response){
        mPresenter.getRouteState(response.getBID());
    }
}
