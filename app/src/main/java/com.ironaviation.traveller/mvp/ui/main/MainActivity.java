package com.ironaviation.traveller.mvp.ui.main;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;

import com.ironaviation.traveller.R;
import com.ironaviation.traveller.app.utils.ViewFindUtils;
import com.ironaviation.traveller.common.AppComponent;
import com.ironaviation.traveller.common.WEActivity;
import com.ironaviation.traveller.di.component.DaggerMainComponent;
import com.ironaviation.traveller.di.module.MainModule;
import com.ironaviation.traveller.mvp.contract.MainContract;
import com.ironaviation.traveller.mvp.presenter.MainPresenter;
import com.ironaviation.traveller.mvp.ui.widget.AutoSlidingTabLayout;
import com.jess.arms.utils.UiUtils;

import java.util.ArrayList;

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

    private ArrayList<Fragment> mFragments = new ArrayList<>();
    private final String[] mTitles = {
            "热门", "iOS", "Android"
            , "前端"
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

        for (String title : mTitles) {
            mFragments.add(SimpleCardFragment.getInstance(title));
        }
        View decorView = getWindow().getDecorView();
        ViewPager vp = ViewFindUtils.find(decorView, R.id.vp);
        mAdapter = new MyPagerAdapter(getSupportFragmentManager());
        vp.setAdapter(mAdapter);
        mPresenter.login("18980096915","147085");
        final AutoSlidingTabLayout tabLayout_7 = ViewFindUtils.find(decorView, R.id.tl_7);
        tabLayout_7.setViewPager(vp, mTitles);
        vp.setCurrentItem(4);

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

}
