package com.ironaviation.traveller.mvp.presenter.payment;

import android.app.Application;

import com.google.gson.Gson;
import com.ironaviation.traveller.common.WEActivity;
import com.ironaviation.traveller.mvp.constant.Constant;
import com.ironaviation.traveller.mvp.contract.payment.WaitingPaymentContract;
import com.ironaviation.traveller.mvp.model.entity.BaseData;
import com.ironaviation.traveller.mvp.model.entity.response.RouteStateResponse;
import com.ironaviation.traveller.mvp.model.entity.response.WeChaTInfo;
import com.jess.arms.base.AppManager;
import com.jess.arms.di.scope.ActivityScope;
import com.jess.arms.mvp.BasePresenter;
import com.jess.arms.utils.RxUtils;
import com.jess.arms.widget.imageloader.ImageLoader;

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
 *
 * 项目名称：Traveller      
 * 类描述：   
 * 创建人：starRing  
 * 创建时间：2017-04-10 14:53   
 * 修改人：starRing  
 * 修改时间：2017-04-10 14:53   
 * 修改备注：   
 * @version
 *
 */
@ActivityScope
public class WaitingPaymentPresenter extends BasePresenter<WaitingPaymentContract.Model, WaitingPaymentContract.View> {
    private RxErrorHandler mErrorHandler;
    private Application mApplication;
    private ImageLoader mImageLoader;
    private AppManager mAppManager;
    private WEActivity weActivity;

    @Inject
    public WaitingPaymentPresenter(WaitingPaymentContract.Model model, WaitingPaymentContract.View rootView
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
    }

    public void getRouteStateInfo(String bid){
        mModel.getRouteStateInfo(bid)
                .compose(RxUtils.<BaseData<RouteStateResponse>>applySchedulers(mRootView))
                .subscribe(new ErrorHandleSubscriber<BaseData<RouteStateResponse>>(mErrorHandler) {
                    @Override
                    public void onNext(BaseData<RouteStateResponse> routeStateResponseBaseData) {
                        if(routeStateResponseBaseData.isSuccess()){
                            if(routeStateResponseBaseData.getData()!= null){
                                setValue(routeStateResponseBaseData.getData());
                            }
                        }else{

                        }
                    }
                });
    }

    public void setPayment(String bid, final String payment){
        mModel.getPayment(bid,payment)
                .compose(RxUtils.<BaseData>applySchedulers(mRootView))
                .subscribe(new ErrorHandleSubscriber<BaseData>(mErrorHandler) {
                    @Override
                    public void onNext(BaseData jsonObjectBaseData) {
                        if(jsonObjectBaseData.isSuccess()){
                            if(Constant.WECHAT.equals(payment)){
                                String in = jsonObjectBaseData.getData().toString();
                                WeChaTInfo info = new Gson().fromJson(in,WeChaTInfo.class);
                                mRootView.setWeChat(info);
                            }else if(Constant.ALIPAY.equals(payment)){
                                String info = jsonObjectBaseData.getData().toString();
                                mRootView.setAliPay(info);
                            }
                        }
                    }
                });
    }


    public void setValue(RouteStateResponse response){
        if(response.getOrderNo() != null){
            mRootView.setOrderNum(response.getOrderNo());
        }
        if(response.getPhone() != null){
            mRootView.setMobile(response.getPhone());
        }
        if(response.getFlightNo() != null) {//航班号
            mRootView.setTravelNO(response.getFlightNo());
        }
        if(response.getPickupTime() != 0){
            mRootView.setTime(response.getPickupTime());
        }
        if(response.getPickupAddress() != null){
            mRootView.setPickUpAddress(response.getPickupAddress());
        }
        if(response.getDestAddress() != null){
            mRootView.setDestAddress(response.getDestAddress());
        }
        if(response.getSeatNum() != 0){
            mRootView.setSeatNum(response.getSeatNum());
        }
        if(response.getActualPrice() != -1.00){
            mRootView.setPrice(response.getActualPrice());
        }
//        mRootView.setCountdown(); //倒计时
        if(response.getCurrentTime() != 0 && response.getCdt() != 0){
            mRootView.setCountdown(response.getCurrentTime(),response.getCdt());
        }
    }
}