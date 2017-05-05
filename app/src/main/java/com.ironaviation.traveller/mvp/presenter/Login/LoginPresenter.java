package com.ironaviation.traveller.mvp.presenter.Login;

import android.app.Activity;
import android.app.Application;
import android.app.MediaRouteActionProvider;
import android.content.Intent;
import android.text.TextUtils;
import android.widget.TextView;

import com.igexin.sdk.PushManager;
import com.ironaviation.traveller.R;
import com.ironaviation.traveller.app.utils.CommonUtil;
import com.ironaviation.traveller.common.WEActivity;
import com.ironaviation.traveller.mvp.constant.Constant;
import com.ironaviation.traveller.mvp.contract.login.LoginContract;
import com.ironaviation.traveller.mvp.model.entity.BaseData;
import com.ironaviation.traveller.mvp.model.entity.LoginEntity;
import com.ironaviation.traveller.mvp.ui.login.IdentificationActivity;
import com.ironaviation.traveller.mvp.ui.main.MainActivity;
import com.jess.arms.base.AppManager;
import com.jess.arms.di.scope.ActivityScope;
import com.jess.arms.mvp.BasePresenter;
import com.jess.arms.utils.DataHelper;
import com.jess.arms.utils.RxUtils;
import com.jess.arms.utils.UiUtils;
import com.jess.arms.widget.imageloader.BaseImageLoaderStrategy;
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
 * 项目名称：Transfer
 * 类描述：
 * 创建人：flq
 * 创建时间：2017/3/23 13:29
 * 修改人：
 * 修改时间：2017/3/23 13:29
 * 修改备注：
 */

@ActivityScope
public class LoginPresenter extends BasePresenter<LoginContract.Model, LoginContract.View> {
    private RxErrorHandler mErrorHandler;
    private Application mApplication;
    private ImageLoader mImageLoader;
    private AppManager mAppManager;
    private WEActivity weActivity;

    @Inject
    public LoginPresenter(LoginContract.Model model, LoginContract.View rootView
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

    public void getLoginInfo() {
        if (TextUtils.isEmpty(mRootView.getUserInfo())) {
            UiUtils.makeText(mApplication.getString(R.string.login_no_userInfo));
            return;
        }
        if(!CommonUtil.isChinaPhoneLegal(mRootView.getUserInfo())){
            UiUtils.makeText(mApplication.getString(R.string.login_right_mobile));
            return;
        }
        if (TextUtils.isEmpty(mRootView.getCode())) {
            UiUtils.makeText(mApplication.getString(R.string.login_no_code));
            return;
        }
        if (TextUtils.isEmpty(mRootView.getClientId())){
            UiUtils.makeText(mApplication.getString(R.string.login_no_client_id));
            mRootView.initClientId();
            return;
        }

        mModel.getLoginInfo(mRootView.getUserInfo(), mRootView.getCode(),mRootView.getClientId())
                .compose(RxUtils.<BaseData<LoginEntity>>applySchedulers(mRootView))
                .subscribe(new ErrorHandleSubscriber<BaseData<LoginEntity>>(mErrorHandler) {
                    @Override
                    public void onNext(BaseData<LoginEntity> loginEntityBaseData) {
                        if (loginEntityBaseData.isSuccess()) {
                            if(loginEntityBaseData.getData().isRealValid()) {
                                weActivity.startActivity(MainActivity.class);
                            }else{
                                weActivity.startActivity(IdentificationActivity.class);
                            }
                            mRootView.killMyself();
                            saveLoginInfo(loginEntityBaseData.getData());
                        } else {
                            mRootView.showMessage(loginEntityBaseData.getMessage());
                        }
                    }
                });

    }

    public void getValidCode(){
        if (TextUtils.isEmpty(mRootView.getUserInfo())) {
            UiUtils.makeText(mApplication.getString(R.string.login_no_userInfo));
            return;
        }
        if(!CommonUtil.isChinaPhoneLegal(mRootView.getUserInfo())){
            UiUtils.makeText(mApplication.getString(R.string.login_right_mobile));
            return;
        }
        mModel.getValidCode(mRootView.getUserInfo())
                .compose(RxUtils.<BaseData<Boolean>>applySchedulers(mRootView))
                .subscribe(new ErrorHandleSubscriber<BaseData<Boolean>>(mErrorHandler) {
                    @Override
                    public void onNext(BaseData<Boolean> booleanBaseData) {
                        if(booleanBaseData.isSuccess()){
                            mRootView.isSuccess();
                            mRootView.showMessage(booleanBaseData.getMessage());
                        }else{
                            mRootView.showMessage(booleanBaseData.getMessage());
                        }
                    }
                });
    }

    /*
     * <p>登录信息本地化
     * <p>@param loginEntity 登录实体
     */
    public void saveLoginInfo(LoginEntity loginEntity) {
        DataHelper.saveDeviceData(mApplication, Constant.LOGIN, loginEntity);
    }

    /*
     * <p>判断是否登录成功
     * <p>@param loginEntity 登录实体
     */
    public void loginRegulation() {
      //  weActivity.startActivity(MainActivity.class);

        if (DataHelper.getDeviceData(mApplication, Constant.LOGIN) != null) {
            weActivity.startActivity(MainActivity.class);
        } else {

        }
    }
    private long firstTime = 0;

    public boolean exit() {
        boolean reason = false;
        long secondTime = System.currentTimeMillis();
        if (secondTime - firstTime > 2000) { // 如果两次按键时间间隔大于2秒，则不退出

            UiUtils.makeText("再按一次退出程序");
            firstTime = secondTime;// 更新firstTime
            reason = true;
        } else { // 两次按键小于2秒时，退出应用

            UiUtils.killAll();
            reason = false;
        }
        return reason;
    }
}
