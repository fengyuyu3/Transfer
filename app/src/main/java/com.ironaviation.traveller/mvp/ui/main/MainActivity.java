package com.ironaviation.traveller.mvp.ui.main;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.NavigationView;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.igexin.sdk.PushManager;
import com.ironaviation.traveller.R;
import com.ironaviation.traveller.app.service.WEGTIntentService;
import com.ironaviation.traveller.app.service.WEPushService;
import com.ironaviation.traveller.app.utils.CountTimerUtil;
import com.ironaviation.traveller.app.utils.ViewFindUtils;
import com.ironaviation.traveller.common.AppComponent;
import com.ironaviation.traveller.common.WEActivity;
import com.ironaviation.traveller.di.component.DaggerMainComponent;
import com.ironaviation.traveller.di.module.MainModule;
import com.ironaviation.traveller.mvp.constant.Constant;
import com.ironaviation.traveller.mvp.contract.MainContract;
import com.ironaviation.traveller.mvp.model.entity.LoginEntity;
import com.ironaviation.traveller.mvp.presenter.MainPresenter;
import com.ironaviation.traveller.mvp.ui.airportoff.AirPortOffFragment;
import com.ironaviation.traveller.mvp.ui.airporton.AirPortOnFragment;
import com.ironaviation.traveller.mvp.ui.login.IdentificationActivity;
import com.ironaviation.traveller.mvp.ui.my.MessageActivity;
import com.ironaviation.traveller.mvp.ui.my.SettingActivity;
import com.ironaviation.traveller.mvp.ui.my.travel.TravelActivity;
import com.ironaviation.traveller.mvp.ui.widget.AutoSlidingTabLayout;
import com.ironaviation.traveller.mvp.ui.widget.AutoToolbar;
import com.jess.arms.utils.DataHelper;
import com.jess.arms.utils.UiUtils;
import com.zhy.autolayout.AutoRelativeLayout;

import java.util.ArrayList;

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
 * 创建时间：2017-03-23 10:58
 * 修改人：starRing
 * 修改时间：2017-03-23 10:58
 * 修改备注：
 */
public class MainActivity extends WEActivity<MainPresenter> implements MainContract.View {

    @BindView(R.id.iv_function_left)
    ImageView mIvFunctionLeft;
    @BindView(R.id.tv_title)
    TextView mTvTitle;
    @BindView(R.id.tv_function_right)
    TextView mTvFunctionRight;
    @BindView(R.id.toolbar)
    AutoToolbar mToolbar;
    @BindView(R.id.tl_7)
    AutoSlidingTabLayout mTl7;
    @BindView(R.id.vp)
    ViewPager mVp;
    @BindView(R.id.main_content)
    CoordinatorLayout mMainContent;
    @BindView(R.id.navigation_view)
    NavigationView mNavigationView;
    @BindView(R.id.drawer_layout)
    DrawerLayout mDrawerLayout;
    @BindView(R.id.tv_left_slide_menu_title)
    TextView mTvLeftSlideMenuTitle;
    @BindView(R.id.iv_head_portrait)
    ImageView mIvHeadPortrait;
    @BindView(R.id.iv_phone)
    TextView mIvPhone;
    @BindView(R.id.rl_head_portrait)
    AutoRelativeLayout mRlHeadPortrait;
    @BindView(R.id.iv_trip)
    ImageView mIvTrip;
    @BindView(R.id.rl_trip)
    AutoRelativeLayout mRlTrip;
    @BindView(R.id.iv_message)
    ImageView mIvMessage;
    @BindView(R.id.rl_message)
    AutoRelativeLayout mRlMessage;
    @BindView(R.id.iv_setting)
    ImageView mIvSetting;
    @BindView(R.id.rl_setting)
    AutoRelativeLayout mRlSetting;
    private ArrayList<Fragment> mFragments = new ArrayList<>();
    private final String[] mTitles = {
            "接机", "送机"
    };
    private MyPagerAdapter mAdapter;

    @Override
    protected void setupActivityComponent(AppComponent appComponent) {
        DaggerMainComponent
                .builder()
                .appComponent(appComponent)
                .mainModule(new MainModule(this)) //请将MainModule()第一个首字母改为小写
                .build()
                .inject(this);
    }

    @Override
    protected void nodataRefresh() {

    }

    @Override
    protected View initView() {
        return LayoutInflater.from(this).inflate(R.layout.activity_main, null, false);
    }

    @Override
    protected void initData() {

        mFragments.add(new AirPortOnFragment());
        mFragments.add(new AirPortOffFragment());
        /*for (String title : mTitles) {
            mFragments.add(SimpleCardFragment.getInstance(title));
        }*/
        View decorView = getWindow().getDecorView();
        ViewPager vp = ViewFindUtils.find(decorView, R.id.vp);
        mAdapter = new MyPagerAdapter(getSupportFragmentManager());
        vp.setAdapter(mAdapter);
        final AutoSlidingTabLayout tabLayout_7 = ViewFindUtils.find(decorView, R.id.tl_7);
        tabLayout_7.setViewPager(vp, mTitles);
        vp.setCurrentItem(4);
        setTitle(getString(R.string.app_name));
        setRightFunctionText("成都", R.color.white, null);
        setPhone();
        setNavigationIcon(ContextCompat.getDrawable(this, R.mipmap.ic_head));
        mToolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mDrawerLayout.openDrawer(Gravity.LEFT);
            }
        });


    }

    @OnClick({R.id.rl_message, R.id.rl_setting, R.id.rl_trip})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.rl_message:
                startActivity(MessageActivity.class);
                break;
            case R.id.rl_setting:
                startActivity(SettingActivity.class);
                break;
            case R.id.rl_trip:
                startActivity(TravelActivity.class);
                break;
        }
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
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // TODO: add setContentView(...) invocation
        ButterKnife.bind(this);
    }

    @Override
    public void setPhone() {
        LoginEntity response = DataHelper.getDeviceData(mApplication, Constant.LOGIN);

        mIvPhone.setText(setPhoneSecret(response.getPhone()));
    }

    private String setPhoneSecret(String pNumber) {
        StringBuilder sb = new StringBuilder();

        if (!TextUtils.isEmpty(pNumber) && pNumber.length() > 6) {
            for (int i = 0; i < pNumber.length(); i++) {
                char c = pNumber.charAt(i);
                if (i >= 3 && i <= 6) {
                    sb.append('*');
                } else {
                    sb.append(c);
                }
            }

        }
        return sb.toString();
    }

    private class MyPagerAdapter extends FragmentPagerAdapter {
        public MyPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public int getCount() {
            return mFragments.size();
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return mTitles[position];
        }

        @Override
        public Fragment getItem(int position) {
            return mFragments.get(position);
        }
    }

    public boolean onKeyDown(int keyCode, KeyEvent event) {
        switch (keyCode) {
            case KeyEvent.KEYCODE_BACK:
            case KeyEvent.KEYCODE_HOME:
                if (mPresenter.exit()) {
                    return true;
                }
                break;
        }
        return super.onKeyUp(keyCode, event);
    }

}
