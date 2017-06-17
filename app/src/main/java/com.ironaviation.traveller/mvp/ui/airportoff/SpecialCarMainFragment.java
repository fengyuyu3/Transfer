package com.ironaviation.traveller.mvp.ui.airportoff;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.ironaviation.traveller.R;
import com.ironaviation.traveller.common.AppComponent;
import com.ironaviation.traveller.common.WEFragment;
import com.ironaviation.traveller.di.component.airportoff.DaggerSpecialCarMainComponent;
import com.ironaviation.traveller.di.module.airportoff.SpecialCarMainModule;
import com.ironaviation.traveller.mvp.constant.Constant;
import com.ironaviation.traveller.mvp.contract.airportoff.SpecialCarMainContract;
import com.ironaviation.traveller.mvp.presenter.airportoff.SpecialCarMainPresenter;
import com.ironaviation.traveller.mvp.ui.my.adapter.TableLayoutAdapter;
import com.ironaviation.traveller.mvp.ui.widget.AutoSlidingTabLayout;
import com.jess.arms.utils.UiUtils;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

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
 * 接机
 * Created by Administrator on 2017/6/9.
 */

public class SpecialCarMainFragment extends WEFragment<SpecialCarMainPresenter> implements SpecialCarMainContract.View {


    @BindView(R.id.tablayout)
    AutoSlidingTabLayout tablayout;
    @BindView(R.id.vp)
    ViewPager vp;
    Unbinder unbinder;
    private String[] mTitles = new String[]{"拼车","专车"};
    private ArrayList<Fragment> mFragments = new ArrayList<>();
    private TableLayoutAdapter adapter;

    public static SpecialCarMainFragment newInstance() {
        SpecialCarMainFragment fragment = new SpecialCarMainFragment();
        return fragment;
    }

    @Override
    protected void setupFragmentComponent(AppComponent appComponent) {
        DaggerSpecialCarMainComponent
                .builder()
                .appComponent(appComponent)
                .specialCarMainModule(new SpecialCarMainModule(this))//请将SpecialCarMainModule()第一个首字母改为小写
                .build()
                .inject(this);
    }

    @Override
    protected View initView(LayoutInflater inflater, ViewGroup container) {
        return inflater.inflate(R.layout.fragment_specialcar, container, false);
    }

    @Override
    protected void initData() {
        /*String status = getArguments().getString(Constant.STATUS);
        if(status != null && status.equals(Constant.ON)) {*/
            Bundle bundle = new Bundle();
            bundle.putString(Constant.STATUS, Constant.ENTER_PORT);
            AirportFragment airportFragment = new AirportFragment();
            airportFragment.setArguments(bundle);
            Bundle bundle1 = new Bundle();
            bundle1.putString(Constant.STATUS, Constant.Z_ENTER_PORT);
            SpecialCarFragment mAirportFragment = new SpecialCarFragment();
            mAirportFragment.setArguments(bundle1);
            mFragments.add(airportFragment);
            mFragments.add(mAirportFragment);
        /*}else{
            Bundle bundle = new Bundle();
            bundle.putString(Constant.STATUS, Constant.CLEAR_PORT);
            AirportFragment airportFragment = new AirportFragment();
            airportFragment.setArguments(bundle);
            Bundle bundle1 = new Bundle();
            bundle1.putString(Constant.STATUS, Constant.Z_CLEAR_PORT);
            SpecialCarFragment mAirportFragment = new SpecialCarFragment();
            mAirportFragment.setArguments(bundle1);
            mFragments.add(airportFragment);
            mFragments.add(mAirportFragment);
        }*/
            adapter = new TableLayoutAdapter(getActivity().getSupportFragmentManager(),mTitles,mFragments);
            vp.setAdapter(adapter);
            tablayout.setViewPager(vp, mTitles);
            vp.setCurrentItem(0);
    }

    /**
     * 此方法是让外部调用使fragment做一些操作的,比如说外部的activity想让fragment对象执行一些方法,
     * 建议在有多个需要让外界调用的方法时,统一传bundle,里面存一个what字段,来区分不同的方法,在setData
     * 方法中就可以switch做不同的操作,这样就可以用统一的入口方法做不同的事,和message同理
     * <p>
     * 使用此方法时请注意调用时fragment的生命周期,如果调用此setData方法时onActivityCreated
     * 还没执行,setData里调用presenter的方法时,是会报空的,因为dagger注入是在onActivityCreated
     * 方法中执行的,如果要做一些初始化操作,可以不必让外部调setData,在内部onActivityCreated中
     * 初始化就可以了
     *
     * @param data
     */

    @Override
    public void setData(Object data) {

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

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // TODO: inflate a fragment view
        View rootView = super.onCreateView(inflater, container, savedInstanceState);
        unbinder = ButterKnife.bind(this, rootView);
        return rootView;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }
}