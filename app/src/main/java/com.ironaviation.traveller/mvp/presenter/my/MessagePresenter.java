package com.ironaviation.traveller.mvp.presenter.my;

import android.app.Application;

import com.ironaviation.traveller.mvp.constant.Constant;
import com.ironaviation.traveller.mvp.contract.my.MessageContract;
import com.ironaviation.traveller.mvp.model.entity.BaseData;
import com.ironaviation.traveller.mvp.model.entity.response.CommentTag;
import com.ironaviation.traveller.mvp.model.entity.response.MessageResponse;
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
 * 项目名称：Traveller
 * 类描述：
 * 创建人：starRing
 * 创建时间：2017-03-27 10:12
 * 修改人：starRing
 * 修改时间：2017-03-27 10:12
 * 修改备注：
 */
@ActivityScope
public class MessagePresenter extends BasePresenter<MessageContract.Model, MessageContract.View> {
    private RxErrorHandler mErrorHandler;
    private Application mApplication;
    private ImageLoader mImageLoader;
    private AppManager mAppManager;
    private int page;

    @Inject
    public MessagePresenter(MessageContract.Model model, MessageContract.View rootView
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

    public void getFirstMessageData(final int pageIndex, final boolean flag) {
        mModel.getMessageData(Constant.PAGE_SIZE, pageIndex)
                .subscribeOn(Schedulers.io())
                .doOnSubscribe(new Action0() {
                    @Override
                    public void call() {//显示进度条
                        if(flag)
                            mRootView.showDialog();
                    }
                })
                .subscribeOn(AndroidSchedulers.mainThread())
                .observeOn(AndroidSchedulers.mainThread())
                .doAfterTerminate(new Action0() {
                    @Override
                    public void call() {
                        mRootView.dismissDialog();
                        mRootView.stopRefreshing();
                    }
                })
                .subscribe(new ErrorHandleSubscriber<BaseData<MessageResponse>>(mErrorHandler) {
                    @Override
                    public void onNext(BaseData<MessageResponse> commentTagBaseData) {
                        if (commentTagBaseData.isSuccess()) {
                            if (commentTagBaseData.getData() != null
                                    && commentTagBaseData.getData().getItems().size() > 0) {
                                    mRootView.setDatas(commentTagBaseData.getData());
                                if(commentTagBaseData.getData().getItems().size() <10){
                                    mRootView.setNoMore();
                                }else{
                                    page = commentTagBaseData.getData().getCurrentPageIndex()+1;
                                }
                            }else{
                                mRootView.setNodata();
                            }
                        } else {
                            mRootView.showMessage(commentTagBaseData.getMessage());
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

    public void getMessageData(final int pageIndex) {
        mModel.getMessageData(Constant.PAGE_SIZE, pageIndex)
                .compose(RxUtils.<BaseData<MessageResponse>>applySchedulers(mRootView))
                .subscribe(new ErrorHandleSubscriber<BaseData<MessageResponse>>(mErrorHandler) {
                    @Override
                    public void onNext(BaseData<MessageResponse> commentTagBaseData) {
                        if (commentTagBaseData.isSuccess()) {
                            if (commentTagBaseData.getData() != null
                                    && commentTagBaseData.getData().getItems().size() > 0) {
                                    mRootView.addDatas(commentTagBaseData.getData());
                                    page = commentTagBaseData.getData().getCurrentPageIndex() + 1;
                                if (commentTagBaseData.getData().getItems().size() < 10) {
                                    mRootView.setNoMore();
                                }else{
                                    mRootView.setMoreComplete();
                                }
                            }else{
                                mRootView.setNoMore();
                                mRootView.showMessage(commentTagBaseData.getMessage());
                            }
                        } else {

                        }
                    }


                    @Override
                    public void onError(Throwable e) {
                        super.onError(e);
                        mRootView.setError();
                        mRootView.stopRefreshing();
                    }
                });
    }

    public int getPage(){
        return page;
    }
}