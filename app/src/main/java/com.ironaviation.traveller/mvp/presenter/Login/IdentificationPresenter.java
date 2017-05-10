package com.ironaviation.traveller.mvp.presenter.Login;

import android.app.Application;
import android.content.Intent;
import android.text.TextUtils;
import android.util.Log;

import com.google.gson.JsonObject;
import com.ironaviation.traveller.R;
import com.ironaviation.traveller.app.utils.CheckIdCardUtils;
import com.ironaviation.traveller.common.WEActivity;
import com.ironaviation.traveller.mvp.constant.Constant;
import com.ironaviation.traveller.mvp.contract.login.IdentificationContract;
import com.ironaviation.traveller.mvp.model.api.Api;
import com.ironaviation.traveller.mvp.model.entity.BaseData;
import com.ironaviation.traveller.mvp.model.entity.LoginEntity;
import com.ironaviation.traveller.mvp.model.entity.response.IdentificationResponse;
import com.ironaviation.traveller.mvp.ui.main.MainActivity;
import com.ironaviation.traveller.mvp.ui.webview.WebViewActivity;
import com.jess.arms.base.AppManager;
import com.jess.arms.di.scope.ActivityScope;
import com.jess.arms.mvp.BasePresenter;
import com.jess.arms.utils.DataHelper;
import com.jess.arms.utils.RxUtils;
import com.jess.arms.utils.UiUtils;
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
 * 项目名称：Traveller
 * 类描述：
 * 创建人：starRing
 * 创建时间：2017-03-26 10:03
 * 修改人：starRing
 * 修改时间：2017-03-26 10:03
 * 修改备注：
 */
@ActivityScope
public class IdentificationPresenter extends BasePresenter<IdentificationContract.Model, IdentificationContract.View> {
    private RxErrorHandler mErrorHandler;
    private Application mApplication;
    private ImageLoader mImageLoader;
    private AppManager mAppManager;
    private WEActivity weActivity;

    @Inject
    public IdentificationPresenter(IdentificationContract.Model model, IdentificationContract.View rootView
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

    public void identification() {
        if (TextUtils.isEmpty(mRootView.getName())) {
            UiUtils.makeText(mApplication.getString(R.string.hint_name));
            return;
        }
        if (TextUtils.isEmpty(mRootView.getNumeral())) {
            UiUtils.makeText(mApplication.getString(R.string.hint_id_numeral));
            return;
        }

        if (!CheckIdCardUtils.validateCard(mRootView.getNumeral())) {
            UiUtils.makeText(mApplication.getString(R.string.hint_id_numeral_rule));
            return;
        }
        mModel.identification(mRootView.getName(), mRootView.getNumeral())
                .compose(RxUtils.<BaseData<IdentificationResponse>>applySchedulers(mRootView))
                .subscribe(new ErrorHandleSubscriber<BaseData<IdentificationResponse>>(mErrorHandler) {
                    @Override
                    public void onNext(BaseData<IdentificationResponse> data) {
                        if (data.isSuccess()) {
                            if(data.getData().isResult()) {
                                saveIdentificationInfo(data.getData());
                                if(data.getData().getName() != null && data.getData().getIDCard() != null) {
                                   /* Intent intent = new Intent(weActivity, WebViewActivity.class);
                                    intent.putExtra(Constant.URL, Api.PHONE_ID_CARD + "?userName=" + data.getData().getName() + "&number=" + setTextIDCard(data.getData().getIDCard()));
                                    intent.putExtra(Constant.TITLE, weActivity.getResources().getString(R.string.authenticated_success));
                                    intent.putExtra(Constant.STATUS, Constant.AUTO_SETTTING);
                                    weActivity.startActivity(intent);*/
//                                    mRootView.killMyself();
                                    mRootView.isSuccess();
                                }
                            }else{
                                mRootView.showMessage(weActivity.getResources().getString(R.string.authenticated_filed));
                            }
                        } else {
                            UiUtils.makeText(data.getMessage());
                        }
                    }
                });

    }

    /*
     * <p>认证信息本地化
     * <p>@param Identification
     */
    public void saveIdentificationInfo(IdentificationResponse identificationResponse) {
        if(DataHelper.getDeviceData(mApplication, Constant.LOGIN) != null) {
            LoginEntity loginEntity = DataHelper.getDeviceData(mApplication,Constant.LOGIN);
            loginEntity.setIDCard(identificationResponse.getIDCard());
            loginEntity.setName(identificationResponse.getName());
            loginEntity.setRealValid(true);
            DataHelper.saveDeviceData(mApplication, Constant.LOGIN, loginEntity);
        }else{ //登录信息没有

        }
    }

    public String setTextIDCard(String idCard) {
        StringBuilder telBuilder = new StringBuilder(idCard);
        telBuilder.replace(3, 15, "************");
        return telBuilder.toString();
    }
}