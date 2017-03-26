package com.jess.arms.base;

import android.content.Context;
import android.graphics.drawable.AnimationDrawable;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.jess.arms.R;
import com.jess.arms.mvp.Presenter;
import com.trello.rxlifecycle.components.support.RxAppCompatActivity;
import com.zhy.autolayout.AutoFrameLayout;
import com.zhy.autolayout.AutoLinearLayout;
import com.zhy.autolayout.AutoRelativeLayout;

import org.simple.eventbus.EventBus;

import javax.inject.Inject;

import butterknife.ButterKnife;
import butterknife.Unbinder;

public abstract class BaseActivity<P extends Presenter> extends RxAppCompatActivity {
    protected final String TAG = this.getClass().getSimpleName();
    protected BaseApplication mApplication;
    private Unbinder mUnbinder;
    @Inject
    protected P mPresenter;

    private static final String LAYOUT_LINEARLAYOUT = "LinearLayout";
    private static final String LAYOUT_FRAMELAYOUT = "FrameLayout";
    private static final String LAYOUT_RELATIVELAYOUT = "RelativeLayout";
    public static final String IS_NOT_ADD_ACTIVITY_LIST = "is_add_activity_list";//是否加入到activity的list，管理
    private ViewGroup rootView;
    private View noDataView;
    private View startView;


    @Override
    public View onCreateView(String name, Context context, AttributeSet attrs) {
        View view = null;
        if (name.equals(LAYOUT_FRAMELAYOUT)) {
            view = new AutoFrameLayout(context, attrs);
        }

        if (name.equals(LAYOUT_LINEARLAYOUT)) {
            view = new AutoLinearLayout(context, attrs);
        }

        if (name.equals(LAYOUT_RELATIVELAYOUT)) {
            view = new AutoRelativeLayout(context, attrs);
        }

        if (view != null) return view;

        return super.onCreateView(name, context, attrs);
    }


    @Nullable
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mApplication = (BaseApplication) getApplication();
        if (useEventBus())//如果要使用eventbus请将此方法返回true
            EventBus.getDefault().register(this);//注册到事件主线
        setContentView(initView());
        rootView = (ViewGroup) getWindow().getDecorView();
        initId();
        initBaseData();
        //绑定到butterknife
        mUnbinder = ButterKnife.bind(this);
        ComponentInject();//依赖注入
        initData();
    }

    /**
     * 依赖注入的入口
     */
    protected abstract void ComponentInject();
    protected abstract int getNodataId();
    protected abstract void initId();
    protected abstract void initBaseData();
    protected abstract int getStartId();
    protected abstract void showStartAnimation(View view);


    public void FullScreencall() {
        if (Build.VERSION.SDK_INT > 11 && Build.VERSION.SDK_INT < 19) { // lower api
            View v = this.getWindow().getDecorView();
            v.setSystemUiVisibility(View.GONE);
        } else if (Build.VERSION.SDK_INT >= 19) {
            //for new api versions.
            View decorView = getWindow().getDecorView();
            int uiOptions = View.SYSTEM_UI_FLAG_HIDE_NAVIGATION | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY;
            decorView.setSystemUiVisibility(uiOptions);
        }
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (mPresenter != null) mPresenter.onDestroy();//释放资源
        if (mUnbinder != Unbinder.EMPTY) mUnbinder.unbind();
        if (useEventBus())//如果要使用eventbus请将此方法返回true
            EventBus.getDefault().unregister(this);
        this.mPresenter = null;
        this.mUnbinder = null;
        this.mApplication = null;
    }

    /**
     * 是否使用eventBus,默认为使用(true)，
     *
     * @return
     */
    protected boolean useEventBus() {
        return true;
    }


    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }


    protected abstract View initView();

    protected abstract void initData();

    public void initNoDataId(){
        if(rootView != null ){
            if(noDataView == null) {
                noDataView = getLayoutInflater().inflate(getNodataId(), rootView, false);
                if (noDataView != null) {
                    showNodata(false);
                    rootView.addView(noDataView);
                }
            }else{
                Log.e(TAG,"noDataView is Empty");
            }
        }
    }

    public void showNodata(boolean show){
        if(noDataView != null){
            noDataView.setVisibility(show ? View.VISIBLE : View.GONE);
        }
    }

    public void initStartId(){
        if(rootView != null ){
            if(startView == null) {
                startView = getLayoutInflater().inflate(getStartId(), rootView, false);
                showStartAnimation(startView);
                if (startView != null) {
                    showStart(false);
                    rootView.addView(startView);
                }
            }else{
                Log.e(TAG,"startView is Empty");
            }
        }
    }

    public void showStart(boolean show){
        if(startView != null){
            startView.setVisibility(show ? View.VISIBLE : View.GONE);
        }
    }

}
