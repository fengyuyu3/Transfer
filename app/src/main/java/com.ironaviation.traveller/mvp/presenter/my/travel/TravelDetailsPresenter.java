package com.ironaviation.traveller.mvp.presenter.my.travel;

import android.app.Application;
import android.text.format.DateUtils;

import com.baidu.mapapi.search.core.RouteLine;
import com.ironaviation.traveller.app.utils.TimerUtils;
import com.ironaviation.traveller.mvp.constant.Constant;
import com.ironaviation.traveller.mvp.contract.my.travel.TravelDetailsContract;
import com.ironaviation.traveller.mvp.model.entity.BaseData;
import com.ironaviation.traveller.mvp.model.entity.response.PassengersResponse;
import com.ironaviation.traveller.mvp.model.entity.response.RouteStateResponse;
import com.jess.arms.base.AppManager;
import com.jess.arms.di.scope.ActivityScope;
import com.jess.arms.mvp.BasePresenter;
import com.jess.arms.utils.RxUtils;
import com.jess.arms.widget.imageloader.ImageLoader;

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
 * 项目名称：Transfer
 * 类描述：
 * 创建人：flq
 * 创建时间：2017/3/29 15:40
 * 修改人：
 * 修改时间：2017/3/29 15:40
 * 修改备注：
 */

@ActivityScope
public class TravelDetailsPresenter extends BasePresenter<TravelDetailsContract.Model, TravelDetailsContract.View> {
    private RxErrorHandler mErrorHandler;
    private Application mApplication;
    private ImageLoader mImageLoader;
    private AppManager mAppManager;
    private RouteStateResponse mRouteStateResponse;

    private String fomart = "预计 HH:mm 到达机场";

    @Inject
    public TravelDetailsPresenter(TravelDetailsContract.Model model, TravelDetailsContract.View rootView
            , RxErrorHandler handler, Application application
            , ImageLoader imageLoader, AppManager appManager) {
        super(model, rootView);
        this.mErrorHandler = handler;
        this.mApplication = application;
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
                            if (routeStateResponseBaseData.getData() != null) {
                                if (routeStateResponseBaseData.getData().getExt() != null) {
                                    for (int i = 0; i < routeStateResponseBaseData.getData().getExt().size(); i++) {
                                        routeStateResponseBaseData.getData().getExt().get(i).setJsonData(routeStateResponseBaseData.getData().getExt().get(i).getData().toString());
                                        routeStateResponseBaseData.getData().getExt().get(i).setData(null);
                                    }
                                }
                                setRouteStateResponse(routeStateResponseBaseData.getData());
                                mRouteStateResponse = routeStateResponseBaseData.getData();
                                mRootView.setPassengersResponseInfo(routeStateResponseBaseData.getData());
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

    public void getPassengersInfo(String bid) {
        mModel.getPassengerInfo(bid)
                .compose(RxUtils.<BaseData<List<PassengersResponse>>>applySchedulers(mRootView))
                .subscribe(new ErrorHandleSubscriber<BaseData<List<PassengersResponse>>>(mErrorHandler) {
                    @Override
                    public void onNext(BaseData<List<PassengersResponse>> listBaseData) {
                        if (listBaseData.isSuccess()) {
                            if (listBaseData.getData() != null) {
                                mRootView.setPassengersInfo(listBaseData.getData());
                            }
                        } else {
                            mRootView.showMessage(listBaseData.getMessage());
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

    public void setScheduledTime(RouteLine route, PassengersResponse mPassengersResponse) {

        if (mPassengersResponse != null && mPassengersResponse.getChildStatus() != null && mPassengersResponse.getChildStatus().equals(Constant.ABORAD)) {

            mRootView.setScheduledTime((TimerUtils.getDateFormat(System.currentTimeMillis() + route.getDuration() * 1000, fomart)).toString());

        }


    }
}
