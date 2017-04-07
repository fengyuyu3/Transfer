package com.ironaviation.traveller.mvp.ui.my;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.content.ContextCompat;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.ironaviation.traveller.R;
import com.ironaviation.traveller.common.AppComponent;
import com.ironaviation.traveller.common.WEActivity;
import com.ironaviation.traveller.di.component.my.DaggerSettingComponent;
import com.ironaviation.traveller.di.module.my.SettingModule;
import com.ironaviation.traveller.mvp.contract.my.SettingContract;
import com.ironaviation.traveller.mvp.presenter.my.SettingPresenter;
import com.ironaviation.traveller.mvp.ui.login.LoginActivity;
import com.ironaviation.traveller.mvp.ui.widget.TextTextImageView;
import com.jess.arms.utils.DeviceUtils;
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
 * 项目名称：Traveller
 * 类描述：
 * 创建人：starRing
 * 创建时间：2017-03-27 10:33
 * 修改人：starRing
 * 修改时间：2017-03-27 10:33
 * 修改备注：
 */
public class SettingActivity extends WEActivity<SettingPresenter> implements SettingContract.View {


    @BindView(R.id.iv_function_left)
    ImageView mIvFunctionLeft;
    @BindView(R.id.tti_identification)
    TextTextImageView mTtiIdentification;
    @BindView(R.id.tti_usual_address)
    TextTextImageView mTtiUsualAddress;
    @BindView(R.id.tti_connect_us)
    TextTextImageView mTtiConnectUs;
    @BindView(R.id.tti_cancellation_account)
    TextTextImageView mTtiCancellationAccount;
    @BindView(R.id.tv_version_name)
    TextView mTvVersionName;

    @Override
    protected void setupActivityComponent(AppComponent appComponent) {
        DaggerSettingComponent
                .builder()
                .appComponent(appComponent)
                .settingModule(new SettingModule(this)) //请将SettingModule()第一个首字母改为小写
                .build()
                .inject(this);
    }

    @Override
    protected void nodataRefresh() {

    }

    @Override
    protected View initView() {
        return LayoutInflater.from(this).inflate(R.layout.activity_setting, null, false);
    }

    @Override
    protected void initData() {
        setTitle(getString(R.string.setting));
        setNavigationIcon(ContextCompat.getDrawable(this, R.mipmap.ic_back));
        mToolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        mTtiIdentification.setText(getString(R.string.unauthorized));
        mTtiIdentification.setTextColor(ContextCompat.getColor(this, R.color.word_red));
        setTv_version_name();
    }


    @Override
    public void showLoading() {

    }

    @Override
    public void hideLoading() {

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
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // TODO: add setContentView(...) invocation
        ButterKnife.bind(this);
    }

    @OnClick({R.id.tti_identification, R.id.tti_usual_address, R.id.tti_connect_us, R.id.tti_cancellation_account})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.tti_identification:

                break;
            case R.id.tti_usual_address:
                startActivity(UsualAddressActivity.class);
                break;
            case R.id.tti_connect_us:

                break;
            case R.id.tti_cancellation_account:
                mPresenter.signOut();
                //UiUtils.killAll();
               // startActivity(LoginActivity.class);
                break;
        }
    }

    private void setTv_version_name() {

        String versionName = DeviceUtils.getVersionName(this);
        if (!TextUtils.isEmpty(versionName)) {
            mTvVersionName.setText(getResources().getString(R.string.app_name) + " " + versionName);
        }
    }

}
