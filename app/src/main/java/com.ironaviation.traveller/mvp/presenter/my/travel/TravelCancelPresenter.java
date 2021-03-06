package com.ironaviation.traveller.mvp.presenter.my.travel;

import android.app.Application;
import android.os.Bundle;
import android.text.TextUtils;

import com.google.gson.JsonObject;
import com.ironaviation.traveller.common.WEActivity;
import com.ironaviation.traveller.mvp.constant.Constant;
import com.ironaviation.traveller.mvp.contract.my.travel.TravelCancelContract;
import com.ironaviation.traveller.mvp.model.entity.BaseData;
import com.ironaviation.traveller.mvp.model.entity.request.CancelBookingRequest;
import com.ironaviation.traveller.mvp.model.entity.request.CancelOrderRequest;
import com.ironaviation.traveller.mvp.model.entity.response.CancelBookingInfo;
import com.ironaviation.traveller.mvp.model.entity.response.RouteStateResponse;
import com.ironaviation.traveller.mvp.model.entity.response.TravelCancelReason;
import com.ironaviation.traveller.mvp.ui.my.travel.CancelSuccessActivity;
import com.jess.arms.base.AppManager;
import com.jess.arms.di.scope.ActivityScope;
import com.jess.arms.mvp.BasePresenter;
import com.jess.arms.utils.RxUtils;
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
 * 项目名称：Traveller
 * 类描述：
 * 创建人：starRing
 * 创建时间：2017-04-05 14:25
 * 修改人：starRing
 * 修改时间：2017-04-05 14:25
 * 修改备注：
 */
@ActivityScope
public class TravelCancelPresenter extends BasePresenter<TravelCancelContract.Model, TravelCancelContract.View> {
    private RxErrorHandler mErrorHandler;
    private Application mApplication;
    private ImageLoader mImageLoader;
    private AppManager mAppManager;
    private WEActivity weActivity;

    @Inject
    public TravelCancelPresenter(TravelCancelContract.Model model, TravelCancelContract.View rootView
            , RxErrorHandler handler, Application application
            , ImageLoader imageLoader, AppManager appManager) {
        super(model, rootView);
        this.mErrorHandler = handler;
        this.mApplication = application;
        this.mImageLoader = imageLoader;
        this.mAppManager = appManager;
        this.weActivity = (WEActivity) mAppManager.getCurrentActivity();

    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        this.mErrorHandler = null;
        this.mAppManager = null;
        this.mImageLoader = null;
        this.mApplication = null;
        this.weActivity = null;
    }

    public void getCancelBookInfo(String bid) {
        mModel.getCancelBookInfo(bid)
                .compose(RxUtils.<BaseData<CancelBookingInfo>>applySchedulers(mRootView))
                .subscribe(new ErrorHandleSubscriber<BaseData<CancelBookingInfo>>(mErrorHandler) {
                    @Override
                    public void onNext(BaseData<CancelBookingInfo> data) {
                        if(data.isSuccess()) {
                            if (data.getData() != null) {
                                mRootView.setFreeView(data.getData().getIsFreeCancel(), data.getData().getCancelPrice());
                                mRootView.setReasonView(data.getData().getReasons());
                            }else{
                                mRootView.showMessage(data.getMessage());
                            }
                        }else{
                            mRootView.showMessage(data.getMessage());
                        }

                    }
                });
    }

    public void cancelBook(final String bid, List<CancelBookingInfo.Reasons> travelCancelReasons, String otherReason) {

        mModel.cancelBooking(bid, setCancelBookingRequest(travelCancelReasons, otherReason))
                .compose(RxUtils.<BaseData<Boolean>>applySchedulers(mRootView))
                .subscribe(new ErrorHandleSubscriber<BaseData<Boolean>>(mErrorHandler) {
                    @Override
                    public void onNext(BaseData<Boolean> data) {
                        if(data.isSuccess()) {
                            if (data.getData()) {
                                Bundle pBundle = new Bundle();
                                pBundle.putString(Constant.BID, bid);
                                if(mRootView.getStatus() != null) {
                                    pBundle.putString(Constant.CANCEL, mRootView.getStatus());
                                }
                                weActivity.startActivity(CancelSuccessActivity.class, pBundle);
                                weActivity.finish();
                            } else {
                                mRootView.showMessage(data.getMessage());
                            }
                        }else{
                            mRootView.showMessage(data.getMessage());
                        }
                    }
                });
    }

    private List<TravelCancelReason> setTravelCancelReason(List<String> reasons) {
        List<TravelCancelReason> mTravelCancelResponseList = new ArrayList<>();

        for (int i = 0; i < reasons.size(); i++) {
            mTravelCancelResponseList.add(new TravelCancelReason(reasons.get(i)));
        }
        return mTravelCancelResponseList;
    }

    private CancelOrderRequest setCancelBookingRequest(List<CancelBookingInfo.Reasons> travelCancelReasons, String otherReason) {
        CancelOrderRequest cancelOrderRequest = new CancelOrderRequest();

        List<String> stringList = new ArrayList<>();

        for (int i = 0; i < travelCancelReasons.size(); i++) {
            if (travelCancelReasons.get(i).isType()) {
                stringList.add(travelCancelReasons.get(i).getCode());
            }
        }
        if (stringList.size() > 0) {
            cancelOrderRequest.setReasonCodes(stringList);
        }
        if (!TextUtils.isEmpty(otherReason)) {
            cancelOrderRequest.setReason(otherReason);
        } else {

        }
        return cancelOrderRequest;

    }
}