package com.ironaviation.traveller.mvp.presenter.airportoff;

import android.app.Application;

import com.ironaviation.traveller.mvp.contract.airportoff.SpecialCarContract;
import com.ironaviation.traveller.mvp.model.entity.BaseData;
import com.ironaviation.traveller.mvp.model.entity.request.AirportGoInfoRequest;
import com.ironaviation.traveller.mvp.model.entity.request.PreViewRequest;
import com.ironaviation.traveller.mvp.model.entity.response.CarTypeResponse;
import com.jess.arms.base.AppManager;
import com.jess.arms.di.scope.ActivityScope;
import com.jess.arms.mvp.BasePresenter;
import com.jess.arms.utils.RxUtils;
import com.jess.arms.widget.imageloader.ImageLoader;

import java.util.List;

import me.jessyan.rxerrorhandler.core.RxErrorHandler;
import me.jessyan.rxerrorhandler.handler.ErrorHandleSubscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action0;
import rx.schedulers.Schedulers;

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
 * Created by Dennis on 2017/6/12.
 */

@ActivityScope
public class SpecialCarPresenter extends BasePresenter<SpecialCarContract.Model, SpecialCarContract.View> {
    private RxErrorHandler mErrorHandler;
    private Application mApplication;
    private ImageLoader mImageLoader;
    private AppManager mAppManager;

    @Inject
    public SpecialCarPresenter(SpecialCarContract.Model model, SpecialCarContract.View rootView
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

    public void getPreViewInfo(PreViewRequest params){
        mModel.getPreViewInfo(params)
                .compose(RxUtils.<BaseData<AirportGoInfoRequest>>applySchedulers(mRootView))
                .subscribe(new ErrorHandleSubscriber<BaseData<AirportGoInfoRequest>>(mErrorHandler) {
                    @Override
                    public void onNext(BaseData<AirportGoInfoRequest> airportGoInfoRequestBaseData) {
                         if(airportGoInfoRequestBaseData.isSuccess()){

                         }else{
                             //error
                         }
                    }
                });
    }

    public void getCarType(){
        mModel.getCarTypes()
                .subscribeOn(Schedulers.io())
                .doOnSubscribe(new Action0() {
                    @Override
                    public void call() {//显示进度条
                    }
                })
                .subscribeOn(AndroidSchedulers.mainThread())
                .observeOn(AndroidSchedulers.mainThread())
                .doAfterTerminate(new Action0() {
                    @Override
                    public void call() {
                    }
                })
                .subscribe(new ErrorHandleSubscriber<BaseData<List<CarTypeResponse>>>(mErrorHandler) {
                    @Override
                    public void onNext(BaseData<List<CarTypeResponse>> listBaseData) {
                        if(listBaseData.isSuccess()){
                            if(listBaseData.getData() != null && listBaseData.getData().size() > 0 ){
                                mRootView.freshCarType(listBaseData.getData());
                            }else{
                                //  no data
                            }
                        }else{
                            //error
                            mRootView.showMessage(listBaseData.getMessage());
                        }
                    }
                });
    }

}