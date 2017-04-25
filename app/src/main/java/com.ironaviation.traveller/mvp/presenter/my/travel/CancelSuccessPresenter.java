package com.ironaviation.traveller.mvp.presenter.my.travel;

import android.app.Application;
import android.text.TextUtils;

import com.google.gson.Gson;
import com.ironaviation.traveller.R;
import com.ironaviation.traveller.common.WEApplication;
import com.ironaviation.traveller.mvp.constant.Constant;
import com.ironaviation.traveller.mvp.contract.my.travel.CancelSuccessContract;
import com.ironaviation.traveller.mvp.model.entity.BaseData;
import com.ironaviation.traveller.mvp.model.entity.Ext;
import com.ironaviation.traveller.mvp.model.entity.PayInfo;
import com.ironaviation.traveller.mvp.model.entity.response.CancelBookingInfo;
import com.ironaviation.traveller.mvp.model.entity.response.RouteStateResponse;
import com.ironaviation.traveller.mvp.model.entity.response.TravelCancelReason;
import com.jess.arms.base.AppManager;
import com.jess.arms.di.scope.ActivityScope;
import com.jess.arms.mvp.BasePresenter;
import com.jess.arms.utils.RxUtils;
import com.jess.arms.widget.imageloader.ImageLoader;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

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
 * 创建时间：2017-04-05 14:29
 * 修改人：starRing
 * 修改时间：2017-04-05 14:29
 * 修改备注：
 */
@ActivityScope
public class CancelSuccessPresenter extends BasePresenter<CancelSuccessContract.Model, CancelSuccessContract.View> {
    private RxErrorHandler mErrorHandler;
    private Application mApplication;
    private ImageLoader mImageLoader;
    private AppManager mAppManager;

    @Inject
    public CancelSuccessPresenter(CancelSuccessContract.Model model, CancelSuccessContract.View rootView
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

    public void getRouteStateInfo(String bid) {
        mModel.getRouteStateInfo(bid)
                .compose(RxUtils.<BaseData<RouteStateResponse>>applySchedulers(mRootView))
                .subscribe(new ErrorHandleSubscriber<BaseData<RouteStateResponse>>(mErrorHandler) {
                    @Override
                    public void onNext(BaseData<RouteStateResponse> data) {
                        if (data.getData() != null) {
                            //mRootView.setResponsibilityView(data.getData().getIsFreeCancel(), data.getData().getCancelPrice());
                            getRouteStateInfo(data.getData());


                        }

                    }
                });
    }

    public void getRouteStateInfo(RouteStateResponse data) {
        if (data != null) {
            //mRootView.setResponsibilityView(data.getData().getIsFreeCancel(), data.getData().getCancelPrice());

            if (!TextUtils.isEmpty(data.getNotes())) {
                setReasons(data.getNotes());
            }

            if (!TextUtils.isEmpty(data.getDriverName())) {
                mRootView.setDriverName(data.getDriverName());
            }

            if (!TextUtils.isEmpty(data.getDriverRate())) {
                mRootView.setDriverRate(data.getDriverRate());
            }

            if (!TextUtils.isEmpty(data.getCarLicense())) {
                mRootView.setCarLicense(data.getCarLicense());
            }
            List<Ext> Ext = data.getExt();

            setMoneyResponsibilityView(Ext);
        }
    }

    private void setMoneyResponsibilityView(List<Ext> Ext) {
        PayInfo payInfo = null;
        if (Ext != null && Ext.size() != 0) {
            for (int i = 0; i < Ext.size(); i++) {
                if (Ext.get(i).getName().equals(Constant.CLASS_PAY_INFO)) {
                    try {
                        payInfo = new Gson().fromJson(Ext.get(i).getJsonData(), PayInfo.class);

                    }catch (Exception e){
                        //友盟
                    }

                }

            }
        }
        if (payInfo != null) {
            if (payInfo.getIsFreeCancel()) {
                mRootView.setResponsibilityView(WEApplication.getContext().getString(R.string.penal_sum_hint));
                mRootView.setMoneyView(payInfo.getRebate() + "");
            } else {
                mRootView.setResponsibilityView(WEApplication.getContext().getString(R.string.free_hint));

            }

        } else {
            mRootView.setResponsibilityView(WEApplication.getContext().getString(R.string.penal_sum_hint));
        }
    }

    private List<TravelCancelReason> setTravelCancelReason(String reasons[]) {
        List<TravelCancelReason> mTravelCancelResponseList = new ArrayList<>();

        for (int i = 0; i < reasons.length; i++) {
            mTravelCancelResponseList.add(new TravelCancelReason(reasons[i]));
        }
        return mTravelCancelResponseList;
    }

    private void setReasons(String reason) {
        String reasonsList[] = null;
        String reasons[] = null;
        String otherReason = null;
        String otherReasons[] = null;
        if (reason.contains(Constant.SEPARATOR_OTHER)) {

            if (reason.startsWith(Constant.SEPARATOR_OTHER)) {
                otherReasons = reason.split(Constant.SEPARATOR_OTHER.toString());
                otherReason = otherReasons[0];
            } else {
                try {
                    reasonsList = reason.split(Constant.SEPARATOR_OTHER);

                    otherReason = reasonsList[1];
                    reasons = reasonsList[0].split(Constant.SEPARATOR);
                }catch (Exception e){

                }

            }

        } else {
            reasons = reason.split(Constant.SEPARATOR);

        }

        mRootView.setReasonView(setTravelCancelReason(reasons), otherReason);
    }
}