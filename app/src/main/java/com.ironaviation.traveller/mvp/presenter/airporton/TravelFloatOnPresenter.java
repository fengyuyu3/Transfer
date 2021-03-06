package com.ironaviation.traveller.mvp.presenter.airporton;

import android.app.Application;

import com.ironaviation.traveller.mvp.contract.airporton.TravelFloatOnContract;
import com.ironaviation.traveller.mvp.model.entity.BaseData;
import com.ironaviation.traveller.mvp.model.entity.request.AirportGoInfoRequest;
import com.ironaviation.traveller.mvp.model.entity.response.Flight;
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
 * Created by Administrator on 2017/4/17.
 */

@ActivityScope
public class TravelFloatOnPresenter extends BasePresenter<TravelFloatOnContract.Model, TravelFloatOnContract.View> {
    private RxErrorHandler mErrorHandler;
    private Application mApplication;
    private ImageLoader mImageLoader;
    private AppManager mAppManager;

    @Inject
    public TravelFloatOnPresenter(TravelFloatOnContract.Model model, TravelFloatOnContract.View rootView
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

    public void getFlightInfo(final String flightNo, final String date){
        mModel.getFlightInfo(flightNo,date)
                .compose(RxUtils.<BaseData<Flight>>applySchedulers(mRootView))
                .subscribe(new ErrorHandleSubscriber<BaseData<Flight>>(mErrorHandler) {
                    @Override
                    public void onNext(BaseData<Flight> flightBaseData) {
                        if(flightBaseData.isSuccess()){
                            if(flightBaseData.getData() != null){
//                                mRootView.showMessage(flightBaseData.getData().getInfo().toString());
                                mRootView.setData(flightBaseData.getData(),date);
                            }else{
                                mRootView.showMessage(flightBaseData.getMessage());
                            }
                        }else{
                            mRootView.showMessage(flightBaseData.getMessage());
                        }
                    }
                });

    }

}
