package com.ironaviation.traveller.mvp.ui.my.travel;

import android.content.Context;
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
import android.widget.TextView;

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
import com.baidu.mapapi.model.LatLngBounds;
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
import com.baidu.trace.Trace;
import com.baidu.trace.api.entity.EntityListResponse;
import com.baidu.trace.api.entity.LocRequest;
import com.baidu.trace.api.entity.OnEntityListener;
import com.baidu.trace.api.track.DistanceResponse;
import com.baidu.trace.api.track.HistoryTrackRequest;
import com.baidu.trace.api.track.HistoryTrackResponse;
import com.baidu.trace.api.track.LatestPointResponse;
import com.baidu.trace.api.track.OnTrackListener;
import com.baidu.trace.api.track.SortType;
import com.baidu.trace.api.track.TrackPoint;
import com.baidu.trace.model.OnTraceListener;
import com.baidu.trace.model.PushMessage;
import com.baidu.trace.model.StatusCodes;
import com.baidu.trace.model.TraceLocation;
import com.ironaviation.traveller.R;
import com.ironaviation.traveller.app.EventBusTags;
import com.ironaviation.traveller.app.utils.AnimationUtil;
import com.ironaviation.traveller.app.utils.CommonUtil;
import com.ironaviation.traveller.app.utils.TimerUtils;
import com.ironaviation.traveller.common.AppComponent;
import com.ironaviation.traveller.common.WEActivity;
import com.ironaviation.traveller.common.WEApplication;
import com.ironaviation.traveller.di.component.my.travel.DaggerTravelDetailsComponent;
import com.ironaviation.traveller.di.module.my.travel.TravelDetailsModule;
import com.ironaviation.traveller.event.TravelCancelEvent;
import com.ironaviation.traveller.map.overlayutil.DrivingRouteOverlay;
import com.ironaviation.traveller.map.overlayutil.OverlayManager;
import com.ironaviation.traveller.mvp.constant.Constant;
import com.ironaviation.traveller.mvp.contract.my.travel.TravelDetailsContract;
import com.ironaviation.traveller.mvp.model.entity.BasePushData;
import com.ironaviation.traveller.mvp.model.entity.response.PassengersResponse;
import com.ironaviation.traveller.mvp.model.entity.response.PushResponse;
import com.ironaviation.traveller.mvp.model.entity.response.RouteStateResponse;
import com.ironaviation.traveller.mvp.presenter.my.travel.TravelDetailsPresenter;
import com.ironaviation.traveller.mvp.ui.my.CurrentLocation;
import com.ironaviation.traveller.mvp.ui.my.EstimateActivity;
import com.ironaviation.traveller.mvp.ui.widget.AlertDialog;
import com.ironaviation.traveller.mvp.ui.widget.MoreActionPopupWindow;
import com.jess.arms.utils.LogUtils;
import com.jess.arms.utils.UiUtils;
import com.umeng.analytics.MobclickAgent;
import com.zhy.autolayout.AutoLinearLayout;


import org.simple.eventbus.EventBus;
import org.simple.eventbus.Subscriber;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;

import static com.ironaviation.traveller.R.id.one;
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
        TravelDetailsContract.View, View.OnClickListener, OnGetRoutePlanResultListener
        , BaiduMap.OnMarkerClickListener {

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
    @BindView(R.id.ll_layout)
    AutoLinearLayout mLlLayout;

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
    private BitmapDescriptor start;
    private BitmapDescriptor end;
    private BitmapDescriptor my;


    protected static OverlayOptions overlayOptions;
    private static Overlay overlay = null;
    private String bid;
    private PassengersResponse mPassengersResponse;
    private List<PassengersResponse> mPassengersResponseList;
    private OverlayManager mOverlayManager;
    private static MapStatusUpdate msUpdate = null;
    private static BitmapDescriptor realtimeBitmap;  //图标
    private static OverlayOptions overlay1;  //覆盖物
    private static OverlayOptions startMarker;
    private static OverlayOptions endMarker;
    private static PolylineOptions polyline = null;  //路线覆盖物
    private static List<LatLng> pointList = new ArrayList<LatLng>();  //定位点的集合

    private RefreshThread refreshThread = null;  //刷新地图线程以获取实时点
    /*private double driverLongitude;
    private double driverLatitude;*/
    private String uid;
    private String driverCode;
    private String format = "HH:mm分";

    private RouteStateResponse historyResponse;
    /**
     * 地图工具
     */
    private WEApplication trackApp = null;

    private MapUtil mapUtil = null;

    /*Trace*/
    /**
     * 轨迹客户端
     */
    public LBSTraceClient mClient = null;

    /**
     * 轨迹服务
     */
    public Trace mTrace = null;

    /**
     * 轨迹服务ID
     */
    public long serviceId = Constant.SERVICEID;

    /**
     * Entity标识
     */
    /**
     * Entity监听器(用于接收实时定位回调)
     */
    private OnEntityListener entityListener = null;

    private LocRequest locRequest = null;
    /**
     * 监听器
     */
    private OnTraceListener mTraceListener;

    // 分页索引
    int pageIndex = 1;
    /**
     * 实时定位任务
     */
    private RealTimeHandler realTimeHandler = new RealTimeHandler();

    private RealTimeLocRunnable realTimeLocRunnable = null;

    /**
     * 轨迹点集合
     */
    private List<LatLng> trackPoints = new ArrayList<>();
    /**
     * 轨迹排序规则
     */
    private SortType sortType = SortType.asc;
    private AnimationUtil mAnimationUtil;
    private boolean realTimeLocFlag=false;

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
            if (responses != null && !TextUtils.isEmpty(responses.getBID())) {
//                mPopupWindow = new MoreActionPopupWindow(this, EventBusTags.WAITING_PAYMENT,responses.getBID());
                /*status = responses.getStatus();
                bid = responses.getBID();
                mPresenter.setRouteStateResponse(responses);
                showStatus(status,responses);
                mPopupWindow = new MoreActionPopupWindow(this, EventBusTags.TRAVEL_DETAILS, responses.getBID());*/
                setPassengersResponseInfo(responses);
            } else {
                bid = pBundle.getString(Constant.BID);
                if (bid != null) {
                    mPresenter.getRouteState(bid);
                }
            }
        } else {
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
        mAnimationUtil = AnimationUtil.getInstance(this);
    }

    @Override
    public void setPassengersResponseInfo(RouteStateResponse response) {
        this.responses = response;
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
        if (!TextUtils.isEmpty(driverCode)) {
            mClient = new LBSTraceClient(this);
            mTrace = new Trace(serviceId, driverCode);
            locRequest = new LocRequest(serviceId);
            initOnStartTraceListener();//事实更新司机位置
        }


//        mWeApplication.getClient().startTrace(trace, startTraceListener);
        status = response.getStatus();
        bid = response.getBID();
        mPresenter.setRouteStateResponse(response);
        mPopupWindow = new MoreActionPopupWindow(this, EventBusTags.TRAVEL_DETAILS, response.getBID());
        for (int i = 0; i < response.getExt().size(); i++) {
            if (response.getExt().get(i).getName().equals(Constant.CURRENT_PICKUP)) {
                uid = response.getExt().get(i).getJsonData();
            }
        }
        showStatus(status, response);
    }

    @Override
    public void setScheduledTime(String scheduledTime) {
        mTwText.setText(scheduledTime);
    }

    public void initTitle() {
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

    @OnClick({R.id.iw_zoom, R.id.iw_zoom_nomal, R.id.iw_mobile, R.id.rl_go_to_pay})
    public void myOnClick(View view) {
        switch (view.getId()) {
            case R.id.iw_zoom:
//                mAnimationUtil.moveToViewBottom(mLlLayout);
                AllGone();
                break;
            case R.id.iw_zoom_nomal:
                if (status != null) {
                    showStatusAll(status, responses);
                }
                break;
            case R.id.iw_mobile:
                if (phone != null) {
                    callMobile(phone);
                }
                break;
            case R.id.rl_go_to_pay:
                if (bid != null) {
                    mPresenter.isConfirmArrive(bid);
                }
                break;
        }
    }

    //有数据请求
    public void showStatus(String status, RouteStateResponse responses) {
        /*route = null;
        mBaiduMap.clear();*/
        mBaiduMap.clear();
        switch (status) {
            case Constant.INHAND: //派单进行中
                if (bid != null) {
                    mPresenter.getPassengersInfo(bid);
                }
                showChildStatus(responses);
                break;
            case Constant.BOOKSUCCESS: //派单成功
                mTwWaitTwo.setText("司机预计"+ TimerUtils.getDateFormat(responses.getPickupTime(),format)+"到达");
                setAddress();
                waitPickup();
                break;
            case Constant.REGISTERED:
//                orderGoingDay();//白天 晚上 判断起飞时间是13点以前还是以后
                setAddress();
                if (responses.isMorning()) {
                    orderGoingDay();
                } else {
                    orderGoing();
                }
                break;
            case Constant.ARRIVED://绘制历史轨迹
                historyResponse = responses;
                JudgmentStopRealTimeLoc();
                queryHistoryTrack(historyResponse);
                arrive();
                break;
        }
    }

    public void showChildStatus(RouteStateResponse responses) {
        switch (responses.getChildStatus()) {
            case Constant.TOSEND:
                mTwText.setText("司机预计"+ TimerUtils.getDateFormat(responses.getPickupTime(),format)+"到达");
                Peer();
                break;
            case Constant.PICKUP:
                mTwText.setText("司机预计"+ TimerUtils.getDateFormat(responses.getPickupTime(),format)+"到达");
                pickup();
                break;
            case Constant.ABORAD:
                already();
                break;
        }
    }

    //无数据请求
    public void showStatusAll(String status, RouteStateResponse responses) {
        /*route = null;
        mBaiduMap.clear();*/
//        mAnimationUtil.moveToViewLocation(mLlLayout);
        switch (status) {
            case Constant.INHAND: //派单进行中
                showChildStatus(responses);
                break;
            case Constant.BOOKSUCCESS: //派单成功
                waitPickup();
                break;
            case Constant.REGISTERED:
//                orderGoingDay();//白天 晚上 判断起飞时间是13点以前还是以后
                if (responses.isMorning()) {
                    orderGoingDay();
                } else {
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
                if (responses != null) {
                    setCancelTravel(responses, event);
                }
                break;
            case Constant.TRAVEL_CUSTOMER:
                CommonUtil.call(this, Constant.CONNECTION_US);
                break;
        }
    }

    public void setCancelTravel(RouteStateResponse responses, TravelCancelEvent event) {
        if (responses.getStatus().equals(Constant.ARRIVED)) {
            showDialog();
        } else if (responses.getStatus().equals(Constant.INHAND) &&
                responses.getChildStatus().equals(Constant.ABORAD)) {
            showDialog();
        } else {
            Bundle pBundle = new Bundle();
            pBundle.putString(Constant.BID, event.getBid());
            pBundle.putString(Constant.STATUS, Constant.CLEAR_PORT);
            startActivity(TravelCancelActivity.class, pBundle);
        }
    }

    public void showDialog() {
        AlertDialog dialog = new AlertDialog(this);
        dialog.builder().setTitle("温馨提示").setMsg("现在不能取消行程")
                .setOneButton("确定", new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                    }
                }).show();
    }

    public void setAddress() {
        if (responses != null && responses.getDestAddress() != null) {
            pathTwo(responses.getPickupLatitude(), responses.getPickupLongitude(),
                    responses.getDestLagitude(), responses.getDestLongitude(), responses.getDestAddress());
        }
    }

    public void orderGoingDay() { //预约成功 白班
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

    public void orderGoing() {  //预约成功 晚班
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

    public void pickup() { //司机正在接您的途中
        mIwZoomNomal.setVisibility(View.GONE);
        mIwZoom.setVisibility(View.VISIBLE);
        mLlOrdering.setVisibility(View.GONE);//派单中
        mLlComplete.setVisibility(View.VISIBLE);//派单成功
        mLlDriverInfo.setVisibility(View.VISIBLE); //司机信息
        mLlArrive.setVisibility(View.GONE);  // 确认到达
        mTwTitle.setText(getResources().getString(R.string.travel_pickup));
//        mTwText.setText(getResources().getString(R.string.travel_pickup_info));
    }

    public void Peer() { //司机正在接您同行乘客的途中
        mIwZoomNomal.setVisibility(View.GONE);
        mIwZoom.setVisibility(View.VISIBLE);
        mLlOrdering.setVisibility(View.GONE);//派单中
        mLlComplete.setVisibility(View.VISIBLE);//派单成功
        mLlDriverInfo.setVisibility(View.VISIBLE); //司机信息
        mLlArrive.setVisibility(View.GONE);  // 确认到达
        mTwTitle.setText(getResources().getString(R.string.travel_peer));
//        mTwText.setText(getResources().getString(R.string.travel_pickup_info));
    }

    public void already() { //你已经上车
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

    public void waitPickup() { //等待接驾
        mIwZoomNomal.setVisibility(View.GONE);
        mIwZoom.setVisibility(View.VISIBLE);
        mLlOrdering.setVisibility(View.VISIBLE);//派单中
        mLlComplete.setVisibility(View.GONE);//派单成功
        mLlDriverInfo.setVisibility(View.VISIBLE); //司机信息
        mLlArrive.setVisibility(View.GONE);  // 确认到达
        mTwWaitOne.setText(getResources().getString(R.string.travel_peer));
    }

    public void arrive() { //确认到达
        mIwZoomNomal.setVisibility(View.GONE);
        mIwZoom.setVisibility(View.VISIBLE);
        mLlArrive.setVisibility(View.VISIBLE);  // 确认到达
        mLlDriverInfo.setVisibility(View.VISIBLE); //司机信息
        mLlOrdering.setVisibility(View.GONE);//派单中
        mLlComplete.setVisibility(View.GONE);//派单成功
    }

    public void AllGone() {  //隐藏
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

    public void callMobile(String phone) {
        /*Intent intent = new Intent(Intent.ACTION_DIAL);
        Uri data = Uri.parse("tel:" + phone);
        intent.setData(data);
        startActivity(intent);*/
        CommonUtil.call(this,phone);
    }

    @Override
    public void setCarLicense(String liscense) {
        mTwCarNum.setText(liscense);
    }

    @Override
    public void setCarColor(String carColor, String model) {
        mYwCarColor.setText(carColor + "." + model);
    }

    @Override
    public void setStatus(String status) {
        this.status = status;
//        showStatus(status,responses);
    }

    @Override
    public void isSuccess() {
//        responses = mPresenter.getData();
        Bundle bundle = new Bundle();
        if (responses != null) {
            bundle.putSerializable(Constant.STATUS, responses);
        }
        startActivity(EstimateActivity.class, bundle);
        EventBus.getDefault().post(true,EventBusTags.REFRESH);
        /*Intent intent = new Intent(this,EstimateActivity.class);
        launchActivity(intent);*/
        finish();
    }

    @Override
    public void setBid(String bid) {
        this.bid = bid;
        mPopupWindow = new MoreActionPopupWindow(this, EventBusTags.TRAVEL_DETAILS, bid);
    }

    @Override
    public void setPassengersInfo(final List<PassengersResponse> info) {
        initOnEntityListener();
        setTraceServer();
        mPassengersResponseList = info;
        mClient.queryRealTimeLoc(locRequest, new OnEntityListener() {
            @Override
            public void onReceiveLocation(TraceLocation location) {

                System.out.println("TraceLocation : " + location);


                if (StatusCodes.SUCCESS != location.getStatus() || CommonUtil.isZeroPoint(location.getLatitude(),
                        location.getLongitude())) {
                    return;
                }
                LatLng currentLatLng = mapUtil.convertTraceLocation2Map(location);
                if (null == currentLatLng) {
                    return;
                }
                CurrentLocation.locTime = CommonBaiDuUtil.toTimeStamp(location.getTime());
                CurrentLocation.latitude = currentLatLng.latitude;
                CurrentLocation.longitude = currentLatLng.longitude;

                if (null != mapUtil) {
                    mapUtil.updateStatus(currentLatLng, true);
                }
                mapNewLocation(info, mPassengersResponse, currentLatLng);
            }
        });


    }

    public void mapLocation(List<PassengersResponse> info, PassengersResponse mPassengersResponse) {

        List<PassengersResponse> passengersResponseList = new ArrayList<>();
        if (info != null) {
            info.add(mPassengersResponse);
            Collections.sort(info);
        }
        for (int i = 0; i < info.size(); i++) {
            if (info.get(i).getChildStatus().equals(Constant.ABORAD)) {
                passengersResponseList.add(info.get(i));
                info.remove(info.get(i));
            }
        }
        Collections.sort(passengersResponseList);
        if (info != null && info.size() > 0) {
            if (uid != null) {
                for (int i = 0; i < info.size(); i++) {
                    if (info.get(i).getUID() != null && info.get(i).getUID().equals(uid)) {
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
        /*if (responses.getDestAddress() != null) {
            pathPlanning(passengersResponseList, latitude, longtude, responses.getDestAddress());
        } else {
            pathPlanning(passengersResponseList, latitude, longtude, Constant.AIRPORT_T1);
        }*/
    }

    //正在进行中
    public void mapNewLocation(List<PassengersResponse> info, PassengersResponse mPassengersResponse,
                               LatLng driverLatLng) {
        List<PassengersResponse> passengersResponseList = new ArrayList<>();
        if (info != null) {
            info.add(mPassengersResponse);
            Collections.sort(info);
        }
        for (int i = 0; i < info.size(); i++) {
            if (info.get(i).getChildStatus().equals(Constant.ABORAD)) {
                info.remove(info.get(i));
            }
        }
        if (info != null && info.size() > 0) {
            if (uid != null) {
                for (int i = 0; i < info.size(); i++) {
                    if (info.get(i).getUID() != null && info.get(i).getUID().equals(uid)) {
                        passengersResponseList.add(info.get(i));
                        info.remove(info.get(i));
                    }
                }
            }
        }
        passengersResponseList.addAll(info);
        if (responses != null && responses.getDestAddress() != null) {
            pathPlanning(passengersResponseList, driverLatLng, responses.getDestAddress(),responses);
        } else {
            pathPlanning(passengersResponseList, driverLatLng, Constant.AIRPORT_T1,responses);
        }
    }

    //
    public void setEndPoint() {


    }

    //开启轨迹
    public void setTraceServer() {
        // 开启轨迹服务
    }

    public void initMap() {
        trackApp = (WEApplication) getApplicationContext();
        mapUtil = MapUtil.getInstance();
        mapUtil.init((MapView) findViewById(R.id.mapview));
        BitmapUtil.init();
        //  mapUtil.setCenter(trackApp);

        mBaiduMap = mMapview.getMap();
        // 隐藏缩放控件
        mMapview.showZoomControls(false);
        //地图上比例尺
        mMapview.showScaleControl(false);
        mSearch = RoutePlanSearch.newInstance();
        mSearch.setOnGetRoutePlanResultListener(this);
        //覆盖物初始化
        my = BitmapDescriptorFactory.fromResource(R.mipmap.ic_location_mine);
        bd = BitmapDescriptorFactory.fromResource(R.mipmap.ic_location_customer);
        car = BitmapDescriptorFactory.fromResource(R.mipmap.ic_location_car);
        start = BitmapDescriptorFactory.fromResource(R.mipmap.ic_location_start);
        end = BitmapDescriptorFactory.fromResource(R.mipmap.ic_location_end);


    }


    //路径规划 route  stNode设置一个假的 Latitude 30.542191  Longitude 104.066535
    private void pathPlanning(List<PassengersResponse> planningListt,LatLng driver, String address,RouteStateResponse response) {
        mPlanNodes = new ArrayList<>();
        stNode = PlanNode.withLocation(driver);//起点 104.083864,30.622657
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
        if (mPlanNodes.size() > 0) {
            drivingRoutePlanOption.passBy(mPlanNodes);
        }
        mSearch.drivingSearch(drivingRoutePlanOption);
        initMarker(planningListt); //初始化覆盖物
        setEndMarker(response.getDestLagitude(),response.getDestLongitude());
    }

    private void pathTwo(double startlatitude, double startlongitude
            , double endlatitude, double endlongitude, String address) {
        stNode = PlanNode.withLocation(new LatLng(startlatitude, startlongitude));//起点 104.083864,30.622657
        etNode = PlanNode.withLocation(new LatLng(endlatitude, endlongitude));
        /*mSearch.drivingSearch((new DrivingRoutePlanOption())
                .from(stNode).to(enNode));*/
        DrivingRoutePlanOption drivingRoutePlanOption = new DrivingRoutePlanOption();
        drivingRoutePlanOption.from(stNode);
//        drivingRoutePlanOption.to(etNode);
        drivingRoutePlanOption.to(PlanNode.withCityNameAndPlaceName("成都", address));
        mSearch.drivingSearch(drivingRoutePlanOption);
        setStartEndMarker(startlatitude,startlongitude,endlatitude,endlongitude);
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
            if (result.getRouteLines().size() == 1) {
                route = result.getRouteLines().get(0);

            } else {
                int index = 0;
                int distance = 0;
                for (int i = 0; i < result.getRouteLines().size(); i++) {
                    if (distance <= result.getRouteLines().get(i).getDistance()) {
                        index = i;
                        distance = result.getRouteLines().get(i).getDistance();
                    }
                }
                route = result.getRouteLines().get(index);
            }
            MyDrivingRouteOverlay overlay = new MyDrivingRouteOverlay(mBaiduMap, this);
            mOverlayManager = overlay;
            route.getDuration();
            mBaiduMap.setOnMarkerClickListener(overlay);
            overlay.setData(result.getRouteLines().get(0));
            overlay.addToMap();
            overlay.zoomToSpan();
            mPresenter.setScheduledTime(route, mPassengersResponse);
            return;
        }
    }

    @Override
    public void onGetIndoorRouteResult(IndoorRouteResult indoorRouteResult) {

    }

    @Override
    public void onGetBikingRouteResult(BikingRouteResult bikingRouteResult) {

    }


    @Override
    public boolean onMarkerClick(Marker marker) {
        return false;
    }

    public void initMarker(List<PassengersResponse> planningListt) {

        for (int i = 0; i < planningListt.size(); i++) {
            LatLng ll = new LatLng(planningListt.get(i).getPickupLatitude(),
                    planningListt.get(i).getPickupLongitude());
            MarkerOptions markerOptions = null;
            if(mPassengersResponse != null) {
                if (ll.latitude == mPassengersResponse.getPickupLatitude() &&
                        ll.longitude == mPassengersResponse.getPickupLongitude()) {
                    markerOptions = new MarkerOptions()
                            .position(ll).icon(my).zIndex(9).draggable(true);
                } else {
                    markerOptions = new MarkerOptions()
                            .position(ll).icon(bd).zIndex(9).draggable(true);
                }
            }
            mBaiduMap.addOverlay(markerOptions);
        }
    }

    public void setStartEndMarker(double startlatitude, double startlongitude
            , double endlatitude, double endlongitude){
        LatLng llStart = new LatLng(startlatitude, startlongitude);
        LatLng llEnd = new LatLng(endlatitude,endlongitude);
        MarkerOptions markerOptions = new MarkerOptions()
                .position(llStart).icon(start).zIndex(9).draggable(true);
        MarkerOptions markerOptionEnd = new MarkerOptions()
                .position(llEnd).icon(end).zIndex(9).draggable(true);
        mBaiduMap.addOverlay(markerOptions);
        mBaiduMap.addOverlay(markerOptionEnd);
    }

    public void setEndMarker(double endlatitude, double endlongitude){
        LatLng llEnd = new LatLng(endlatitude,endlongitude);
        MarkerOptions markerOptions = new MarkerOptions()
                .position(llEnd).icon(end).zIndex(9).draggable(true);
        mBaiduMap.addOverlay(markerOptions);
    }


    @Override
    public void onPause() {
        // MapView的生命周期与Activity同步，当activity挂起时需调用MapView.onPause()
        mapUtil.onPause();
        super.onPause();
    }

    @Override
    public void onResume() {
        // MapView的生命周期与Activity同步，当activity恢复时需调用MapView.onResume()
        mapUtil.onResume();
        super.onResume();
    }

    @Override
    protected void onDestroy() {
        // MapView的生命周期与Activity同步，当activity销毁时需调用MapView.destroy()
        // 回收 bitmap 资源
        mapUtil.clear();
        mSearch.destroy();
        bd.recycle();
        BitmapUtil.clear();
        stopRealTimeLoc();
        super.onDestroy();

    }   /*Handler handler = new Handler(){
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

    /**
     * 追踪开始
     */
    private void initOnStartTraceListener() {

        // 初始化轨迹服务监听器
        mTraceListener = new OnTraceListener() {
            // 开启服务回调
            @Override
            public void onStartTraceCallback(int status, String message) {
                LogUtils.debugLongInfo("鹰眼轨迹服务", "开启服务回调:" + "消息类型=" + status + "消息内容=" + message);

            }

            // 停止服务回调
            @Override
            public void onStopTraceCallback(int status, String message) {
                LogUtils.debugLongInfo("鹰眼轨迹服务", "停止服务回调:" + "消息类型=" + status + "消息内容=" + message);
            }

            // 开启采集回调
            @Override
            public void onStartGatherCallback(int status, String message) {
                LogUtils.debugLongInfo("鹰眼轨迹服务", "开启采集回调:" + "消息类型=" + status + "消息内容=" + message);
            }

            // 停止采集回调
            @Override
            public void onStopGatherCallback(int status, String message) {
                LogUtils.debugLongInfo("鹰眼轨迹服务", "停止采集回调:" + "消息类型=" + status + "消息内容=" + message);
            }

            // 推送回调
            @Override
            public void onPushCallback(byte messageNo, PushMessage message) {

            }
        };

        entityListener = new OnEntityListener() {

            @Override
            public void onReceiveLocation(TraceLocation location) {

                System.out.println("TraceLocation : " + location);


                if (StatusCodes.SUCCESS != location.getStatus() || CommonUtil.isZeroPoint(location.getLatitude(),
                        location.getLongitude())) {
                    return;
                }
                LatLng currentLatLng = mapUtil.convertTraceLocation2Map(location);
                if (null == currentLatLng) {
                    return;
                }
                CurrentLocation.locTime = CommonBaiDuUtil.toTimeStamp(location.getTime());
                CurrentLocation.latitude = currentLatLng.latitude;
                CurrentLocation.longitude = currentLatLng.longitude;

                if (null != mapUtil) {
                    mapUtil.updateStatus(currentLatLng, true);
                }

            }

        };

        mClient.startTrace(mTrace, mTraceListener);
        //startRefreshThread(true);
        startRealTimeLoc(Constant.EAGLE_EYE_NOW_TIME_PACK_INTERVAL);

    }


    /**
     * 初始化OnEntityListener
     */
    private void initOnEntityListener() {

    }

    /**
     * 历史轨迹请求
     */
    private HistoryTrackRequest historyTrackRequest = new HistoryTrackRequest();
    String arrivedTime = null;

    /**
     * 查询历史轨迹
     */
    private void queryHistoryTrack(RouteStateResponse responses) {
        initOnEntityListenerBlue();
        String driverCode = responses.getDriverCode();
        // 是否返回精简的结果（0 : 否，1 : 是）
        int simpleReturn = 0;
        long startTime = 0;
        long endTime = 0;
        // 开始时间
        if (startTime == 0) {
            startTime = (int) (responses.getActualPickupTime() / 1000);
        }


        for (int i = 0; i < responses.getExt().size(); i++) {

            if (responses.getExt().get(i).getName().equals(Constant.EXT_ARRIVED_AT)) {
                arrivedTime = (String) responses.getExt().get(i).getJsonData();
            }
        }

        if (endTime == 0) {
            if (TextUtils.isEmpty(arrivedTime)) {
                endTime = (int) (System.currentTimeMillis() / 1000);

            } else {
                try {
                    endTime = Long.parseLong(arrivedTime) / 1000;
                } catch (Exception e) {

                }
            }
        }
        trackApp.initRequest(historyTrackRequest);
        historyTrackRequest.setEntityName(driverCode);
        historyTrackRequest.setStartTime(startTime);
        historyTrackRequest.setEndTime(endTime);
        historyTrackRequest.setPageIndex(pageIndex);
        historyTrackRequest.setPageSize(Constant.EAGLE_EYE_HISTORY_PAGE_SIZE);
        historyTrackRequest.setProcessed(true);
        mClient.queryHistoryTrack(historyTrackRequest, mTrackListener);
    }

    /**
     * 轨迹监听器（用于接收历史轨迹回调）
     */
    private OnTrackListener mTrackListener = null;

    /**
     * 初始化OnEntityListenerBlue
     */
    private void initOnEntityListenerBlue() {
        trackPoints.clear();
        mTrackListener = new OnTrackListener() {
            @Override
            public void onHistoryTrackCallback(HistoryTrackResponse response) {
                int total = response.getTotal();
                if (StatusCodes.SUCCESS != response.getStatus()) {
                    UiUtils.makeText(response.getMessage());
                } else if (0 == total) {
                    UiUtils.makeText(getString(R.string.no_track_data));
                } else {
                    List<TrackPoint> points = response.getTrackPoints();
                    if (null != points) {
                        for (TrackPoint trackPoint : points) {
                            if (!CommonUtil.isZeroPoint(trackPoint.getLocation().getLatitude(),
                                    trackPoint.getLocation().getLongitude())) {
                                trackPoints.add(MapUtil.convertTrace2Map(trackPoint.getLocation()));
                            }
                        }
                    }
                }

                if (total > Constant.EAGLE_EYE_HISTORY_PAGE_SIZE * pageIndex) {
                    historyTrackRequest.setPageIndex(++pageIndex);
                    queryHistoryTrack(historyResponse);
                } else {
                    mapUtil.drawHistoryTrack(trackPoints, sortType, arrivedTime);
                }

            }

            @Override
            public void onDistanceCallback(DistanceResponse response) {
                super.onDistanceCallback(response);
            }

            @Override
            public void onLatestPointCallback(LatestPointResponse response) {
                super.onLatestPointCallback(response);
            }
        };

    }


    private void addMarker() {

        if (msUpdate != null) {
            mBaiduMap.setMapStatus(msUpdate);
        }

        if (polyline != null) {
            mBaiduMap.addOverlay(polyline);
        }

        if (overlay1 != null) {
            mBaiduMap.addOverlay(overlay1);
        }
        if (null != startMarker) {
            mBaiduMap.addOverlay(startMarker);
        }

        if (null != endMarker) {
            mBaiduMap.addOverlay(endMarker);
        }
    }

    /**
     * 轨迹刷新线程
     *
     * @author BLYang
     */
    private class RefreshThread extends Thread {

        protected boolean refresh = true;

        public void run() {

            while (refresh) {
                //queryEntityList();
                mClient.queryRealTimeLoc(locRequest, entityListener);
                try {
                    Thread.sleep(Constant.EAGLE_EYE_NOW_TIME_PACK_INTERVAL * Constant.EAGLE_EYE_LOCATION_SIZE);
                } catch (InterruptedException e) {
//                    System.out.println("线程休眠失败");
                    MobclickAgent.reportError(WEApplication.getContext(), e);

                }
            }

        }
    }


    /**
     * 启动刷新线程
     *
     * @param isStart
     */
    private void startRefreshThread(boolean isStart) {

        if (refreshThread == null) {
            refreshThread = new RefreshThread();
        }

        refreshThread.refresh = isStart;

        if (isStart) {
            if (!refreshThread.isAlive()) {
                refreshThread.start();
            }
        } else {
            refreshThread = null;
        }
    }

    @Subscriber(tag = EventBusTags.AIRPORT_PUSH_OFF)
    public void getPushData(PushResponse response) {
        mPresenter.getRouteState(response.getBID());
    }

    private class MyDrivingRouteOverlay extends DrivingRouteOverlay {

        public MyDrivingRouteOverlay(BaiduMap baiduMap, Context context) {
            super(baiduMap, context);
        }

        @Override
        public BitmapDescriptor getStartMarker() {
            return BitmapDescriptorFactory.fromResource(R.mipmap.ic_transfer);
        }

        @Override
        public BitmapDescriptor getTerminalMarker() {
            return BitmapDescriptorFactory.fromResource(R.mipmap.ic_transfer);
        }
    }


    @Subscriber(tag = EventBusTags.TRAVEL_DETAIL)
    public void travelDetail(BasePushData response) {
        if(bid != null && response.getData() != null && response.getData().getBID()!=null){
            if(bid.equalsIgnoreCase(response.getData().getBID())){
                mPresenter.getRouteState(response.getData().getBID());
            }
//            EventBus.getDefault().post(response.getData().getBID(),EventBusTags.REFRESH);
        }
    }

    @Subscriber(tag = EventBusTags.PAYMENT_FINISH)
    public void travelFinish(boolean flag) {
        finish();
    }

    /**
     * 实时定位任务
     *
     * @author baidu
     */
    class RealTimeLocRunnable implements Runnable {

        private int interval = 0;

        public RealTimeLocRunnable(int interval) {
            this.interval = interval;
        }

        @Override
        public void run() {
            mClient.queryRealTimeLoc(locRequest, entityListener);
            realTimeHandler.postDelayed(this, interval * 1000);
        }
    }

    public void startRealTimeLoc(int interval) {
        realTimeLocRunnable = new RealTimeLocRunnable(interval);
        realTimeHandler.post(realTimeLocRunnable);
        realTimeLocFlag=true;
    }

    public void stopRealTimeLoc() {
        if (null != realTimeHandler && null != realTimeLocRunnable) {
            realTimeHandler.removeCallbacks(realTimeLocRunnable);
            realTimeLocRunnable = null;
            realTimeLocFlag=false;
        }
    }
    public void JudgmentStopRealTimeLoc(){

        if (realTimeLocFlag){
            if (null != realTimeHandler && null != realTimeLocRunnable) {
                realTimeHandler.removeCallbacks(realTimeLocRunnable);
                realTimeLocRunnable = null;
            }
            realTimeLocFlag=false;
            mapUtil.cleanMarker();
        }

    }
    static class RealTimeHandler extends Handler {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
        }
    }
}
