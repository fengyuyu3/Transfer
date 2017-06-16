package com.ironaviation.traveller.mvp.ui.main;

import android.Manifest;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewPager;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.flyco.tablayout.listener.CustomTabEntity;
import com.flyco.tablayout.listener.OnTabSelectListener;
import com.igexin.sdk.PushManager;
import com.ironaviation.traveller.BuildConfig;
import com.ironaviation.traveller.R;
import com.ironaviation.traveller.app.utils.PushClientUtil;
import com.ironaviation.traveller.app.utils.PushCountTimerUtil;
import com.ironaviation.traveller.app.utils.ViewFindUtils;
import com.ironaviation.traveller.common.AppComponent;
import com.ironaviation.traveller.common.WEActivity;
import com.ironaviation.traveller.di.component.DaggerMainNewComponent;
import com.ironaviation.traveller.di.module.MainNewModule;
import com.ironaviation.traveller.mvp.constant.Constant;
import com.ironaviation.traveller.mvp.contract.MainNewContract;
import com.ironaviation.traveller.mvp.model.entity.TabEntity;
import com.ironaviation.traveller.mvp.presenter.MainNewPresenter;
import com.ironaviation.traveller.mvp.ui.airportoff.AirportFragment;
import com.ironaviation.traveller.mvp.ui.airportoff.SpecialCarMainClearPortFragment;
import com.ironaviation.traveller.mvp.ui.airportoff.SpecialCarMainFragment;
import com.ironaviation.traveller.mvp.ui.my.MessageActivity;
import com.ironaviation.traveller.mvp.ui.my.SettingActivity;
import com.ironaviation.traveller.mvp.ui.my.travel.TravelActivity;
import com.ironaviation.traveller.mvp.ui.widget.AutoCommonTabLayout;
import com.ironaviation.traveller.mvp.ui.widget.AutoDrawerLayout;
import com.ironaviation.traveller.mvp.ui.widget.AutoToolbar;
import com.jess.arms.utils.UiUtils;
import com.yanzhenjie.permission.AndPermission;
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
 * Created by Administrator on 2017/6/9.
 */

public class MainNewActivity extends WEActivity<MainNewPresenter> implements MainNewContract.View {

    @BindView(R.id.toolbar_main)
    AutoToolbar mToolbar;
    @BindView(R.id.main_content)
    CoordinatorLayout mMainContent;
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
    @BindView(R.id.tv_version)
    TextView mTvVersion;
    @BindView(R.id.navigation_view)
    NavigationView mNavigationView;
    @BindView(R.id.drawer_layout)
    AutoDrawerLayout mDrawerLayout;
    private ArrayList<Fragment> mFragments = new ArrayList<>();
    private final String[] mTitles = {
            "接机", "送机"
    };
    private MyPagerAdapter mAdapter;
    private ArrayList<CustomTabEntity> mTabEntities = new ArrayList<>();

    @Override
    protected void setupActivityComponent(AppComponent appComponent) {
        DaggerMainNewComponent
                .builder()
                .appComponent(appComponent)
                .mainNewModule(new MainNewModule(this)) //请将MainNewModule()第一个首字母改为小写
                .build()
                .inject(this);
    }

    @Override
    protected View initView() {
        return LayoutInflater.from(this).inflate(R.layout.activity_main_new, null, false);
    }

    @Override
    protected void initData() {
        AndPermission.with(this)
                .requestCode(100)
                .permission(Manifest.permission.ACCESS_FINE_LOCATION
                        , Manifest.permission.ACCESS_COARSE_LOCATION
                        , Manifest.permission.WRITE_EXTERNAL_STORAGE
                        , Manifest.permission.READ_PHONE_STATE
                        , Manifest.permission.READ_EXTERNAL_STORAGE)
                .send();
        PushClientUtil.initClientId(this);

        Bundle bundle = new Bundle();
        bundle.putString(Constant.STATUS, Constant.ENTER_PORT);
        SpecialCarMainFragment fragment = new SpecialCarMainFragment();
        mFragments.add(fragment);

        SpecialCarMainClearPortFragment fragment1 = new SpecialCarMainClearPortFragment();
        mFragments.add(fragment1);
        PushManager.getInstance().initialize(this);
        mToolbar.setNavigationIcon(ContextCompat.getDrawable(this, R.mipmap.ic_new_head));
        mToolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mDrawerLayout.openDrawer(Gravity.LEFT);
            }
        });
        View decorView = getWindow().getDecorView();
        /*final ViewPager vp = ViewFindUtils.find(decorView, R.id.vp);
        mAdapter = new MyPagerAdapter(getSupportFragmentManager());
        vp.setAdapter(mAdapter);*/
        final AutoCommonTabLayout tl_2 = ViewFindUtils.find(decorView, R.id.tl_2);
        tl_2.setTabData(getArrayList(),this,R.id.frament,mFragments);
        new Handler().post(new Runnable() {
            @Override
            public void run() {
                tl_2.setCurrentTab(1);
                tl_2.setCurrentTab(0);
            }
        });
        /*tl_2.setOnTabSelectListener(new OnTabSelectListener() {
            @Override
            public void onTabSelect(int position) {
                tl_2.setCurrentTab(position);
            }

            @Override
            public void onTabReselect(int position) {

            }
        });*/
        PushCountTimerUtil pushCountTimerUtil = new PushCountTimerUtil(this, 5 * 60 * 1000, 3 * 60 * 60 * 1000);
        pushCountTimerUtil.start();
        mTvVersion.setVisibility(View.GONE);
        if (BuildConfig.IS_TEST) {
            mTvVersion.setVisibility(View.VISIBLE);
            mTvVersion.setText("V:" + BuildConfig.debugTime + ".1");
        }
    }

    public ArrayList<CustomTabEntity> getArrayList() {
        ArrayList<CustomTabEntity> list = new ArrayList<>();
        list.add(new TabEntity(mTitles[0], R.mipmap.ic_zoom, R.mipmap.ic_zoom_nomal));
        list.add(new TabEntity(mTitles[1], R.mipmap.ic_zoom, R.mipmap.ic_zoom_nomal));
        return list;
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
    protected void nodataRefresh() {

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // TODO: add setContentView(...) invocation
        ButterKnife.bind(this);
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
                Bundle bundle = new Bundle();
                bundle.putString(Constant.STATUS,Constant.NO_PUSH);
                startActivity(TravelActivity.class,bundle);
                break;
        }
    }
}
