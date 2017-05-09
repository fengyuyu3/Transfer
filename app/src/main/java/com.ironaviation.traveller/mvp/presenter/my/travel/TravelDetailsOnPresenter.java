package com.ironaviation.traveller.mvp.presenter.my.travel;

import android.app.Application;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;

import com.baidu.mapapi.model.LatLng;
import com.baidu.mapapi.search.core.RouteLine;
import com.baidu.trace.LBSTraceClient;
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
import com.ironaviation.traveller.app.utils.CommonUtil;
import com.ironaviation.traveller.app.utils.TimerUtils;
import com.ironaviation.traveller.common.WEApplication;
import com.ironaviation.traveller.mvp.constant.Constant;
import com.ironaviation.traveller.mvp.contract.my.travel.TravelDetailsOnContract;
import com.ironaviation.traveller.mvp.model.entity.BaseData;
import com.ironaviation.traveller.mvp.model.entity.response.PassengersResponse;
import com.ironaviation.traveller.mvp.model.entity.response.RouteStateResponse;
import com.ironaviation.traveller.mvp.ui.my.CurrentLocation;
import com.ironaviation.traveller.mvp.ui.my.travel.CommonBaiDuUtil;
import com.ironaviation.traveller.mvp.ui.my.travel.MapUtil;
import com.ironaviation.traveller.mvp.ui.my.travel.TravelDetailsActivity;
import com.jess.arms.base.AppManager;
import com.jess.arms.di.scope.ActivityScope;
import com.jess.arms.mvp.BasePresenter;
import com.jess.arms.utils.LogUtils;
import com.jess.arms.utils.RxUtils;
import com.jess.arms.utils.UiUtils;
import com.jess.arms.widget.imageloader.ImageLoader;

import java.util.ArrayList;
import java.util.List;

import me.jessyan.rxerrorhandler.core.RxErrorHandler;
import me.jessyan.rxerrorhandler.handler.ErrorHandleSubscriber;

import javax.inject.Inject;


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

@ActivityScope
public class TravelDetailsOnPresenter extends BasePresenter<TravelDetailsOnContract.Model, TravelDetailsOnContract.View> {
    private RxErrorHandler mErrorHandler;
    private WEApplication mApplication;
    private ImageLoader mImageLoader;
    private AppManager mAppManager;
    private RouteStateResponse mRouteStateResponse;
    private RouteStateResponse mRouteStateResponseFormActivity;
    // 分页索引
    int pageIndex = 1;
    String arrivedTime = null;
    private boolean scheduledTime = false;
    /**
     * 实时定位任务
     */
    private String fomart = "预计 HH:mm 到达";

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
    /**
     * Entity标识
     */
    /**
     * Entity监听器(用于接收实时定位回调)
     */
    private OnEntityListener entityListener = null;

    /**
     * 监听器
     */
    private OnTraceListener mTraceListener;

    @Inject
    public TravelDetailsOnPresenter(TravelDetailsOnContract.Model model, TravelDetailsOnContract.View rootView
            , RxErrorHandler handler, Application application
            , ImageLoader imageLoader, AppManager appManager) {
        super(model, rootView);
        this.mErrorHandler = handler;
        this.mApplication = (WEApplication) application;
        this.mImageLoader = imageLoader;
        this.mAppManager = appManager;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        this.mErrorHandler = null;
        this.mAppManager = null;
        this.mImageLoader = null;
        this.mApplication = null;
    }

    public void getRouteState(String bid) {
        mModel.getRouteStateInfo(bid)
                .compose(RxUtils.<BaseData<RouteStateResponse>>applySchedulers(mRootView))
                .subscribe(new ErrorHandleSubscriber<BaseData<RouteStateResponse>>(mErrorHandler) {
                    @Override
                    public void onNext(BaseData<RouteStateResponse> routeStateResponseBaseData) {
                        if (routeStateResponseBaseData.isSuccess()) {
                            /*if (routeStateResponseBaseData.getData() != null) {
                                setRouteStateResponse(routeStateResponseBaseData.getData());
                                mRouteStateResponse = routeStateResponseBaseData.getData();
                            } else {

                            }*/
                            if (routeStateResponseBaseData.getData() != null) {
                                if (routeStateResponseBaseData.getData().getExt() != null) {
                                    for (int i = 0; i < routeStateResponseBaseData.getData().getExt().size(); i++) {
                                        routeStateResponseBaseData.getData().getExt().get(i).setJsonData(routeStateResponseBaseData.getData().getExt().get(i).getData().toString());
                                        routeStateResponseBaseData.getData().getExt().get(i).setData(null);
                                    }
                                }
                                setRouteStateResponse(routeStateResponseBaseData.getData());
                                mRouteStateResponse = routeStateResponseBaseData.getData();
                                mRootView.setDataResponse(routeStateResponseBaseData.getData());
                            } else {

                            }
                        } else {
//                            mRootView.showMessage("");
                        }
                    }

                    @Override
                    public void onError(Throwable e) {
                        super.onError(e);
                    }
                });
    }

    public void isConfirmArrive(final String bid) {
        mModel.isConfirmArrive(bid)
                .compose(RxUtils.<BaseData<Boolean>>applySchedulers(mRootView))
                .subscribe(new ErrorHandleSubscriber<BaseData<Boolean>>(mErrorHandler) {
                    @Override
                    public void onNext(BaseData<Boolean> booleanBaseData) {
                        if (booleanBaseData.isSuccess()) {
                            mRootView.isSuccess();
                        } else {
                            mRootView.showMessage(booleanBaseData.getMessage());
                        }
                    }
                });
    }

    public void isConfirmPickup(String bid) {
        mModel.isConfirmPickup(bid)
                .compose(RxUtils.<BaseData<Boolean>>applySchedulers(mRootView))
                .subscribe(new ErrorHandleSubscriber<BaseData<Boolean>>(mErrorHandler) {
                    @Override
                    public void onNext(BaseData<Boolean> booleanBaseData) {
                        if (booleanBaseData.isSuccess()) {
                            mRootView.isPickUpSuccess();
                        } else {
                            mRootView.showMessage(booleanBaseData.getMessage());
                        }
                    }
                });
    }


    public RouteStateResponse getData() {
        return mRouteStateResponse;
    }


    public void setRouteStateResponse(RouteStateResponse response) {
        if (response.getDriverName() != null) {
            mRootView.setDriverName(response.getDriverName());
        }
        if (response.getDriverRate() != null) {
            mRootView.setDriverRate(response.getDriverRate());
        }
        if (response.getDriverPhone() != null) {
            mRootView.setDriverPhone(response.getDriverPhone());
        }
        if (response.getCarLicense() != null) {
            mRootView.setCarLicense(response.getCarLicense());
        }
        if (response.getCarColor() != null && response.getCarModel() != null) {
            mRootView.setCarColor(response.getCarColor(), response.getCarModel());
        }
        if (response.getStatus() != null) {
            mRootView.setStatus(response.getStatus());
        }
        if (response.getBID() != null) {
            mRootView.setBid(response.getBID());
        }

    }

    /**
     * 追踪开始
     */
    public void initOnStartTraceListener(final RouteStateResponse responses) {

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


                if (!scheduledTime) {

                    if (null != mRootView.getMapUtil()) {
                        scheduledTime = true;
                        mRootView.pathTwo(location.getLatitude(),
                                location.getLongitude(),
                                responses.getDestLagitude(),
                                responses.getDestLongitude());
                    }

                } else {

                }

                if (StatusCodes.SUCCESS != location.getStatus() || CommonUtil.isZeroPoint(location.getLatitude(),
                        location.getLongitude())) {
                    return;
                }
                LatLng currentLatLng = mRootView.getMapUtil().convertTraceLocation2Map(location);
                if (null == currentLatLng) {
                    return;
                }
                CurrentLocation.locTime = CommonBaiDuUtil.toTimeStamp(location.getTime());
                CurrentLocation.latitude = currentLatLng.latitude;
                CurrentLocation.longitude = currentLatLng.longitude;

                if (null != mRootView.getMapUtil()) {
                    mRootView.getMapUtil().updateStatus(currentLatLng, true);
                }

            }

        };

        mRootView.getTraceClient().startTrace(mRootView.getTrace(), mTraceListener);
        //startRefreshThread(true);
        startRealTimeLoc(Constant.EAGLE_EYE_NOW_TIME_PACK_INTERVAL);
    }


    /**
     * 历史轨迹请求
     */
    private HistoryTrackRequest historyTrackRequest = new HistoryTrackRequest();

    /**
     * 查询历史轨迹
     */
    public void queryHistoryTrack(RouteStateResponse responses, LBSTraceClient mClient) {
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
                arrivedTime = responses.getExt().get(i).getJsonData().toString();
                break;
            }
        }

        if (endTime == 0) {
            if (TextUtils.isEmpty(arrivedTime)) {
                endTime = (int) (System.currentTimeMillis() / 1000);

            } else {
                endTime = Long.parseLong(arrivedTime) / 1000;
            }
        }

        mApplication.initRequest(historyTrackRequest);
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
    public void initOnEntityListenerBlue() {
        trackPoints.clear();
        mTrackListener = new OnTrackListener() {
            @Override
            public void onHistoryTrackCallback(HistoryTrackResponse response) {
                int total = response.getTotal();
                if (StatusCodes.SUCCESS != response.getStatus()) {
                    UiUtils.makeText(response.getMessage());
                } else if (0 == total) {
                    UiUtils.makeText(mApplication.getResources().getString(R.string.no_track_data));
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
                    queryHistoryTrack(mRootView.getRoutStateResponse(), mRootView.getTraceClient());
                } else {
                    if (mRootView.getMapUtil() != null) {
                        mRootView.getMapUtil().drawHistoryTrack(trackPoints, sortType, arrivedTime);
                    }
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
            mRootView.getTraceClient().queryRealTimeLoc(mRootView.getLocRequest(), entityListener);
            realTimeHandler.postDelayed(this, interval * 1000);
        }
    }

    public void startRealTimeLoc(int interval) {
        realTimeLocRunnable = new RealTimeLocRunnable(interval);
        realTimeHandler.post(realTimeLocRunnable);
    }

    public void stopRealTimeLoc() {
        if (null != realTimeHandler && null != realTimeLocRunnable) {
            realTimeHandler.removeCallbacks(realTimeLocRunnable);
            realTimeLocRunnable = null;
        }
    }

    static class RealTimeHandler extends Handler {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
        }
    }

    public void setScheduledTime(RouteLine route, RouteStateResponse mRouteStateResponse) {

        if (mRouteStateResponse.getChildStatus().equals(Constant.ABORAD)) {


            mRootView.setScheduledTime((TimerUtils.getDateFormat(System.currentTimeMillis() + route.getDuration() * 1000, fomart)).toString());

        }


    }
}
