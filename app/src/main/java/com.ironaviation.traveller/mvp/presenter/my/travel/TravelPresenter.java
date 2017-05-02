package com.ironaviation.traveller.mvp.presenter.my.travel;

import android.app.Application;

import com.ironaviation.traveller.mvp.contract.my.travel.TravelContract;
import com.ironaviation.traveller.mvp.model.entity.BaseData;
import com.ironaviation.traveller.mvp.model.entity.request.RouteListMoreRequest;
import com.ironaviation.traveller.mvp.model.entity.response.RouteListResponse;
import com.ironaviation.traveller.mvp.model.entity.response.RouteStateResponse;
import com.ironaviation.traveller.mvp.model.entity.response.TravelResponse;
import com.jess.arms.base.AppManager;
import com.jess.arms.di.scope.ActivityScope;
import com.jess.arms.mvp.BasePresenter;
import com.jess.arms.utils.RxUtils;
import com.jess.arms.widget.imageloader.ImageLoader;

import java.util.List;

import javax.inject.Inject;

import me.jessyan.rxerrorhandler.core.RxErrorHandler;
import me.jessyan.rxerrorhandler.handler.ErrorHandleSubscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action0;
import rx.schedulers.Schedulers;


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
 * 创建时间：2017/3/26 17:28
 * 修改人：
 * 修改时间：2017/3/26 17:28
 * 修改备注：
 */

@ActivityScope
public class TravelPresenter extends BasePresenter<TravelContract.Model, TravelContract.View> {
    private RxErrorHandler mErrorHandler;
    private Application mApplication;
    private ImageLoader mImageLoader;
    private AppManager mAppManager;
    private int num;
    private boolean flag;

    @Inject
    public TravelPresenter(TravelContract.Model model, TravelContract.View rootView
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

   /* public void getTravelData(String bid){
        mModel.getRouteList(bid)
                .compose(RxUtils.<BaseData<RouteListResponse>>applySchedulers(mRootView))
                .subscribe(new ErrorHandleSubscriber<BaseData<RouteListResponse>>(mErrorHandler) {
                    @Override
                    public void onNext(BaseData<RouteListResponse> responseBaseData) {
                        if(responseBaseData.isSuccess()){
                            if(responseBaseData.getData() != null) {
                                mRootView.setDatas(responseBaseData.getData());
                            }else{
                                mRootView.setNodata();
                            }
                        }else{
                            mRootView.showMessage(responseBaseData.getMessage());
                            mRootView.setError();
                        }
                    }

                    @Override
                    public void onError(Throwable e) {
                        super.onError(e);
                        mRootView.setError();
                    }
                });
    }*/

    public void getTravelData(int index) {
        mModel.getRouteListMore(index)
                .compose(RxUtils.<BaseData<RouteListResponse>>applySchedulers(mRootView))
                .subscribe(new ErrorHandleSubscriber<BaseData<RouteListResponse>>(mErrorHandler) {
                    @Override
                    public void onNext(BaseData<RouteListResponse> responseBaseData) {
                        if (responseBaseData.isSuccess()) {
                            if (responseBaseData.getData() != null && responseBaseData.getData().getItems() != null
                                    && responseBaseData.getData().getItems().size() > 0) {
                                mRootView.setDatas(responseBaseData.getData());
                                if(responseBaseData.getData().getItems().size() <10){
                                    mRootView.setNoMore();
                                }else{
                                    num = responseBaseData.getData().getCurrentPageIndex()+1;
                                }
                            } else {
                                mRootView.setNodata();
                            }
                        } else {
                            mRootView.setError();
                        }
                    }

                    @Override
                    public void onError(Throwable e) {
                        super.onError(e);
                        mRootView.setError();
                    }
                });
    }

    public void getTravelDataMore(int index) {
        mModel.getRouteListMore(index)
                .compose(RxUtils.<BaseData<RouteListResponse>>applySchedulers(mRootView))
                .subscribe(new ErrorHandleSubscriber<BaseData<RouteListResponse>>(mErrorHandler) {
                    @Override
                    public void onNext(BaseData<RouteListResponse> responseBaseData) {
                        if (responseBaseData.isSuccess()) {
                            if (responseBaseData.getData() != null && responseBaseData.getData().getItems() != null
                                    && responseBaseData.getData().getItems().size() > 0) {
                                mRootView.setMoreDatas(responseBaseData.getData());
                                num = responseBaseData.getData().getCurrentPageIndex() + 1;
                                if (responseBaseData.getData().getItems().size() < 10) {
                                    mRootView.setNoMore();
                                }else{
                                    mRootView.setMoreComplete();
                                }
                            } else {
                                mRootView.setNoMore();
                                mRootView.showMessage(responseBaseData.getMessage());
                            }
                        } else {
//                            mRootView.showMessage(responseBaseData.getMessage());
                        }
                    }

                    @Override
                    public void onError(Throwable e) {
                        super.onError(e);
                        mRootView.showMessage("网络连接失败");
                    }
                });
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
                                    for (int i = 0; i < routeStateResponseBaseData.getData().getExt().size(); i++){
                                        routeStateResponseBaseData.getData().getExt().get(i).setJsonData(routeStateResponseBaseData.getData().getExt().get(i).getData().toString());
                                        routeStateResponseBaseData.getData().getExt().get(i).setData(null);
                                    }
                                }
                                mRootView.getState(routeStateResponseBaseData.getData());
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

    public int getPage() {
        return num;
    }


    public void getData(RouteStateResponse r) {

    }
}
