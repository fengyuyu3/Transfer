package com.ironaviation.traveller.mvp.presenter.my;

import android.app.Application;

import com.ironaviation.traveller.common.WEActivity;
import com.ironaviation.traveller.mvp.contract.my.EstimateContract;
import com.ironaviation.traveller.mvp.model.entity.BaseData;
import com.ironaviation.traveller.mvp.model.entity.response.CommentTag;
import com.ironaviation.traveller.mvp.model.entity.response.CommentsInfo;
import com.ironaviation.traveller.mvp.model.entity.response.RouteStateResponse;
import com.ironaviation.traveller.mvp.ui.login.LoginActivity;
import com.jess.arms.base.AppManager;
import com.jess.arms.di.scope.ActivityScope;
import com.jess.arms.mvp.BasePresenter;
import com.jess.arms.utils.RxUtils;
import com.jess.arms.widget.imageloader.ImageLoader;

import java.util.List;

import me.jessyan.rxerrorhandler.core.RxErrorHandler;
import me.jessyan.rxerrorhandler.handler.ErrorHandleSubscriber;
import rx.functions.Action0;

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
 * 创建时间：2017-03-29 19:49
 * 修改人：starRing
 * 修改时间：2017-03-29 19:49
 * 修改备注：
 */
@ActivityScope
public class EstimatePresenter extends BasePresenter<EstimateContract.Model, EstimateContract.View> {
    private RxErrorHandler mErrorHandler;
    private Application mApplication;
    private ImageLoader mImageLoader;
    private AppManager mAppManager;
    private WEActivity weActivity;

    @Inject
    public EstimatePresenter(EstimateContract.Model model, EstimateContract.View rootView
            , RxErrorHandler handler, Application application
            , ImageLoader imageLoader, AppManager appManager) {
        super(model, rootView);
        this.mErrorHandler = handler;
        this.mApplication = application;
        this.mImageLoader = imageLoader;
        this.mAppManager = appManager;
        weActivity = (WEActivity) mAppManager.getCurrentActivity();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        this.mErrorHandler = null;
        this.mAppManager = null;
        this.mImageLoader = null;
        this.mApplication = null;
    }

    public void getCommentTagInfo() {
        mModel.getCommentTagInfo()
                .compose(RxUtils.<BaseData<List<CommentTag>>>applySchedulers(mRootView))
                .subscribe(new ErrorHandleSubscriber<BaseData<List<CommentTag>>>(mErrorHandler) {
                    @Override
                    public void onNext(BaseData<List<CommentTag>> commentTagBaseData) {
                        if (commentTagBaseData.isSuccess()) {
                            if (commentTagBaseData.getData() != null
                                    && commentTagBaseData.getData().size() > 0) {
                                mRootView.setList(commentTagBaseData.getData());
                            }
                        } else {
                            mRootView.showMessage(commentTagBaseData.getMessage());
                        }
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
                                    for (int i = 0; i < routeStateResponseBaseData.getData().getExt().size(); i++) {
                                        routeStateResponseBaseData.getData().getExt().get(i).setJsonData(routeStateResponseBaseData.getData().getExt().get(i).getData().toString());
                                        routeStateResponseBaseData.getData().getExt().get(i).setData(null);
                                    }
                                }
                            } else {

                            }

                            mRootView.setAlreadyComment(routeStateResponseBaseData.getData());
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

    public void getRouteStateNoDialog(String bid) {
        mModel.getRouteStateInfo(bid)
                .doOnSubscribe(new Action0() {
                    @Override
                    public void call() {//显示进度条
                    }
                }).doAfterTerminate(new Action0() {
            @Override
            public void call() {
                mRootView.hideLoading();
            }
        })

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
                            } else {

                            }

                            mRootView.setAlreadyComment(routeStateResponseBaseData.getData());
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

    public void isCommentSuccess(CommentsInfo info) {
        mModel.getCommentInfo(info)
                .compose(RxUtils.<BaseData<Boolean>>applySchedulers(mRootView))
                .subscribe(new ErrorHandleSubscriber<BaseData<Boolean>>(mErrorHandler) {
                    @Override
                    public void onNext(BaseData<Boolean> booleanBaseData) {
                        if (booleanBaseData.isSuccess()) {
                            getRouteStateNoDialog(mRootView.getRouteStateResponse().getBID());
                        } else {
                            mRootView.showMessage(booleanBaseData.getMessage());
                        }
                    }
                });
    }

    public CommentsInfo getCommentsInfo() {
        CommentsInfo commentsInfo = new CommentsInfo();
        commentsInfo.setBID(mRootView.getRouteStateResponse().getBID());
        commentsInfo.setDID(mRootView.getRouteStateResponse().getDID());
        commentsInfo.setNotes(mRootView.getOtherReason());
        commentsInfo.setRate(mRootView.getRate());
        commentsInfo.setTagIds(mRootView.getTagIds());
        return commentsInfo;

    }

}