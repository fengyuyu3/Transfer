package com.ironaviation.traveller.mvp.ui.login;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.igexin.sdk.PushManager;
import com.ironaviation.traveller.R;
import com.ironaviation.traveller.app.service.WEGTIntentService;
import com.ironaviation.traveller.app.service.WEPushService;
import com.ironaviation.traveller.app.utils.BarUtils;
import com.ironaviation.traveller.app.utils.CountTimerUtil;
import com.ironaviation.traveller.app.utils.PushClientUtil;
import com.ironaviation.traveller.common.AppComponent;
import com.ironaviation.traveller.common.WEActivity;
import com.ironaviation.traveller.di.component.login.DaggerLoginComponent;
import com.ironaviation.traveller.di.module.login.LoginModule;
import com.ironaviation.traveller.mvp.contract.login.LoginContract;
import com.ironaviation.traveller.mvp.presenter.Login.LoginPresenter;
import com.ironaviation.traveller.mvp.ui.main.MainActivity;
import com.jess.arms.utils.UiUtils;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

import static com.jess.arms.utils.Preconditions.checkNotNull;

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
 * 创建时间：2017/3/23 13:30
 * 修改人：
 * 修改时间：2017/3/23 13:30
 * 修改备注：
 */

public class LoginActivity extends WEActivity<LoginPresenter> implements LoginContract.View {

    @BindView(R.id.et_phone)
    EditText etphone;
    @BindView(R.id.et_code)
    EditText etCode;
    @BindView(R.id.btn_login)
    Button mBtnLogin;
    @BindView(R.id.tv_code)
    TextView mTvCode;

    // DemoPushService.class 自定义服务名称, 核心服务
    private Class userPushService = WEPushService.class;

    CountTimerUtil mCountTimerUtil;

    @Override
    protected void setupActivityComponent(AppComponent appComponent) {
        DaggerLoginComponent
                .builder()
                .appComponent(appComponent)
                .loginModule(new LoginModule(this)) //请将LoginModule()第一个首字母改为小写
                .build()
                .inject(this);
    }

    @Override
    protected void nodataRefresh() {
        setTitle("测试没有数据");
    }

    @Override
    protected View initView() {
        return LayoutInflater.from(this).inflate(R.layout.activity_login, null, false);
    }

    @Override
    protected void initData() {
//        initClientId();
        PushClientUtil.initClientId(this);
        mPresenter.loginRegulation();
        mCountTimerUtil = new CountTimerUtil(60000, 1000, mTvCode);
    }


    @Override
    public void showLoading() {
        showProgressDialog();
    }

    @Override
    public void hideLoading() {
        dismissProgressDialog();
    }

    @Override
    public void showMessage(@NonNull String message) {
        checkNotNull(message);
        UiUtils.SnackbarText(message);
    }

    @Override
    public void launchActivity(@NonNull Intent intent) {
        checkNotNull(intent);
        UiUtils.startActivity(intent);
    }

    @Override
    public void killMyself() {
        finish();
    }


    @Override
    public String getCode() {
        return etCode.getText().toString().trim();
    }

    @Override
    public String getUserInfo() {
        return etphone.getText().toString().trim();
    }

    @Override
    public String getClientId() {
        return PushManager.getInstance().getClientid(getApplicationContext());
    }

    @Override
    public void initClientId() {

        // com.getui.demo.DemoPushService 为第三方自定义推送服务
        PackageManager pkgManager = getPackageManager();

        // 读写 sd card 权限非常重要, android6.0默认禁止的, 建议初始化之前就弹窗让用户赋予该权限
        boolean sdCardWritePermission =
                pkgManager.checkPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE, getPackageName()) == PackageManager.PERMISSION_GRANTED;

        // read phone state用于获取 imei 设备信息
        boolean phoneSatePermission =
                pkgManager.checkPermission(Manifest.permission.READ_PHONE_STATE, getPackageName()) == PackageManager.PERMISSION_GRANTED;

        if (Build.VERSION.SDK_INT >= 23 && !sdCardWritePermission || !phoneSatePermission) {
            requestPermission();
        } else {
            PushManager.getInstance().initialize(this.getApplicationContext(), userPushService);
        }
        PushManager.getInstance().initialize(this.getApplicationContext(), userPushService);
        PushManager.getInstance().registerPushIntentService(this.getApplicationContext(), WEGTIntentService.class);
    }

    @Override
    public void isSuccess() {
        mCountTimerUtil.start();
    }


    @OnClick({R.id.btn_login, R.id.tv_code})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btn_login:
                mPresenter.getLoginInfo();
                break;
            case R.id.tv_code:
                etphone.clearFocus();
                etCode.setFocusable(true);
                etCode.setFocusableInTouchMode(true);
                etCode.requestFocus();
                mPresenter.getValidCode();
                break;
        }
    }

    private void requestPermission() {
        ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.READ_PHONE_STATE},
                REQUEST_PERMISSION);
    }

    private static final int REQUEST_PERMISSION = 0;


    public boolean onKeyDown(int keyCode, KeyEvent event) {
        switch (keyCode) {
            case KeyEvent.KEYCODE_BACK:
                if (mPresenter.exit()) {
                    return true;
                }
                break;
            case KeyEvent.KEYCODE_HOME:

                break;
        }
        return super.onKeyUp(keyCode, event);
    }

}
