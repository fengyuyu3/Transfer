package com.ironaviation.traveller.common;

import android.content.Intent;
import android.graphics.drawable.AnimationDrawable;
import android.os.Handler;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.ironaviation.traveller.R;
import com.ironaviation.traveller.mvp.ui.widget.CustomProgress;
import com.jess.arms.base.BaseActivity;
import com.jess.arms.mvp.Presenter;

/**
 * Created by jess on 8/5/16 13:13
 * contact with jess.yan.effort@gmail.com
 */
public abstract class WEActivity<P extends Presenter> extends BaseActivity<P> {
    protected WEApplication mWeApplication;
    private CustomProgress customProgress;
    protected Toolbar mToolbar, myToolbar;
    protected Button btnRight, myBtnRight;
    protected TextView mTitle, myTitle,mFunctionRight;
    protected SwipeRefreshLayout mSwipeRefreshLayout;

    @Override
    protected void ComponentInject() {
        mWeApplication = (WEApplication) getApplication();
        setupActivityComponent(mWeApplication.getAppComponent());
    }

    //提供AppComponent(提供所有的单例对象)给子类，进行Component依赖
    protected abstract void setupActivityComponent(AppComponent appComponent);

    protected abstract void nodataRefresh();

    @Override
    protected void onDestroy() {
        super.onDestroy();
        this.mWeApplication = null;
        if (customProgress != null) {
            customProgress.miss();
        }
    }

    @Override
    protected void initId() {
        initNoDataId();
        initStartId();
    }

    @Override
    protected int getNodataId() {
        return R.layout.include_nodata;
    }

    @Override
    protected int getStartId() {
        return R.layout.progress_custom;
    }

    /**
     * 显示正在加载的进度条
     */
    public void showProgressDialog() {
        if (!isFinishing() && customProgress != null
                && customProgress.isShowing()) {
            customProgress.miss();
            customProgress = null;
        }
        try {
            customProgress = new CustomProgress(WEActivity.this);
            //customProgress.show(this, "加载中...", true, null);
            customProgress.show(this, "", true, null);
        } catch (WindowManager.BadTokenException exception) {
            exception.printStackTrace();
        }
    }

    public void showProgressDialog(String msg) {
        if (!isFinishing() && customProgress != null
                && customProgress.isShowing()) {
            customProgress.miss();
            customProgress = null;
        }
        try {
            customProgress = new CustomProgress(WEActivity.this);
            customProgress.show(this, msg, true, null);
        } catch (WindowManager.BadTokenException exception) {
            exception.printStackTrace();
        }
    }

    /**
     * 隐藏正在加载的进度条
     */
    public void dismissProgressDialog() {


        if (!isFinishing() && customProgress != null) {

            new Handler().postDelayed(new Runnable() {

                public void run() {
                    //execute the task
                    customProgress.miss();
                }
            }, 300);

        }

    }

    @Override
    protected void initBaseData() {
        initToolBar();
        setNodataSwipeRefreshLayout();
    }

    protected void initToolBar() {
        mToolbar = (Toolbar) getDelegate().findViewById(R.id.toolbar);
        btnRight = (Button) getDelegate().findViewById(R.id.btn_right);
        mTitle = (TextView) getDelegate().findViewById(R.id.tv_title);
        mFunctionRight= (TextView) getDelegate().findViewById(R.id.tv_function_right);

        myToolbar = (Toolbar) getDelegate().findViewById(R.id.nodata_toolbar);
        myBtnRight = (Button) getDelegate().findViewById(R.id.nodata_btn_right);
        myTitle = (TextView) getDelegate().findViewById(R.id.nodata_tv_title);
        if (mToolbar != null) {
            setSupportActionBar(mToolbar);
            getSupportActionBar().setDisplayShowTitleEnabled(false);
        }
    }

    protected void setRightFunctionText(String text) {
        if (!TextUtils.isEmpty(text)) {
            if (mFunctionRight != null) {
                mFunctionRight.setVisibility(View.VISIBLE);
                mFunctionRight.setText(text);
            }
        }
    }


    //设置标题
    public void setTitle(String title) {
        if (!TextUtils.isEmpty(title)) {
            if (mTitle != null) {
                mTitle.setText(title);
            }
            if (myTitle != null) {
                myTitle.setText(title);
            }
        }
    }

    //设置
    public void setNodataSwipeRefreshLayout() {
        mSwipeRefreshLayout = (SwipeRefreshLayout) getDelegate().findViewById(R.id.nodata_swipeRefreshLayout);
        mSwipeRefreshLayout.setColorSchemeColors(getResources().getColor(R.color.colorPrimaryDark), getResources().getColor(R.color.colorPrimaryDark));
        mSwipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                nodataRefresh();
            }
        });
    }

    @Override
    protected void showStartAnimation(View startView) {
        /*ImageView imageView = (ImageView) startView.findViewById(R.id.spinnerImageView);
        // 获取ImageView上的动画背景
        AnimationDrawable spinner = (AnimationDrawable) imageView
                .getBackground();
        // 开始动画
        spinner.start();*/
    }

    public void startActivity(Intent intent){
        super.startActivity(intent);
        startAnimation();
    }

    public void startActivity(Class clazz){
        super.startActivity(new Intent(this,clazz));
        startAnimation();
    }

    public void startAnimation(){
        overridePendingTransition(R.anim.right_in,R.anim.left_out);
    }
}
