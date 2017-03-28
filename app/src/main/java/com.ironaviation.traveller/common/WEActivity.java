package com.ironaviation.traveller.common;

import android.annotation.TargetApi;
import android.content.Intent;
import android.graphics.drawable.AnimationDrawable;
import android.graphics.drawable.Drawable;
import android.os.Build;
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
import com.zhy.autolayout.AutoLinearLayout;

/**
 * Created by jess on 8/5/16 13:13
 * contact with jess.yan.effort@gmail.com
 */
public abstract class WEActivity<P extends Presenter> extends BaseWEActivity<P> {


    protected Toolbar mToolbar, nodataToolbar;
    protected TextView mTitle, nodataTitle, mFunctionRight,nodataFunctionRight;
    protected ImageView mIvFunctionLeft,nodataIvFunctionLeft;
    protected SwipeRefreshLayout mNodataSwipeRefresh;
    protected AutoLinearLayout llError;

    @Override
    protected void initId() {
        initNoDataId();
        initStartId();
        initErrorId();
    }

    @Override
    protected int getNodataId() {
        return R.layout.include_nodata;
    }

    @Override
    protected int getStartId() {
        return R.layout.progress_custom;
    }

    @Override
    protected int getErrorId() {
        return R.layout.include_error;
    }

    @Override
    protected void initBaseData() {
        initToolBar();
        setNodataSwipeRefreshLayout();
        setErrorRefresh();
    }

    protected abstract void nodataRefresh();

    /**
     * 设置右功能键
     */
    protected void setRightFunctionText(String text) {
        if (!TextUtils.isEmpty(text)) {
            if (mFunctionRight != null) {
                mFunctionRight.setVisibility(View.VISIBLE);
                mFunctionRight.setText(text);
            }
            if(nodataFunctionRight != null){
                nodataFunctionRight.setVisibility(View.VISIBLE);
                nodataFunctionRight.setText(text);
            }
        }
    }

    /**
     * 设置自定义左功能键
     */
    protected void setLeftFunction(Drawable text) {
        if (text != null) {
            if (mIvFunctionLeft != null) {
                mIvFunctionLeft.setVisibility(View.VISIBLE);
                mIvFunctionLeft.setImageDrawable(text);
            }
            if (nodataIvFunctionLeft != null) {
                nodataIvFunctionLeft.setVisibility(View.VISIBLE);
                nodataIvFunctionLeft.setImageDrawable(text);
            }
        }
    }
    /**
     * 设置导航左功能键
     */
    protected void setNavigationIcon(Drawable text) {
        if (text != null) {
            if (mToolbar != null) {
                mToolbar.setNavigationIcon(text);
            }
            if(nodataToolbar != null){
                nodataToolbar.setNavigationIcon(text);
            }
        }
    }

    public void setErrorRefresh(){
        llError = (AutoLinearLayout) getDelegate().findViewById(R.id.ll_error);
        llError.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                nodataRefresh();
            }
        });
    }

    //设置
    public void setNodataSwipeRefreshLayout() {
        mNodataSwipeRefresh = (SwipeRefreshLayout) getDelegate().findViewById(R.id.nodata_swipeRefreshLayout);
        mNodataSwipeRefresh.setColorSchemeColors(getResources().getColor(R.color.colorPrimaryDark), getResources().getColor(R.color.colorPrimaryDark));
        mNodataSwipeRefresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                nodataRefresh();
            }
        });
    }

    protected void initToolBar() {
        mToolbar = (Toolbar) getDelegate().findViewById(R.id.toolbar);
        mTitle = (TextView) getDelegate().findViewById(R.id.tv_title);
        mFunctionRight = (TextView) getDelegate().findViewById(R.id.tv_function_right);
        mIvFunctionLeft = (ImageView) getDelegate().findViewById(R.id.iv_function_left);

        nodataToolbar = (Toolbar) getDelegate().findViewById(R.id.nodata_toolbar);
        nodataTitle = (TextView) getDelegate().findViewById(R.id.nodata_tv_title);
        nodataFunctionRight = (TextView) getDelegate().findViewById(R.id.nodata_tv_function_right);
        nodataIvFunctionLeft = (ImageView) getDelegate().findViewById(R.id.nodata_iv_function_left);

        if (mToolbar != null) {
            setSupportActionBar(mToolbar);
            getSupportActionBar().setDisplayShowTitleEnabled(false);
        }
        if(nodataToolbar != null){
            setSupportActionBar(nodataToolbar);
            getSupportActionBar().setDisplayShowTitleEnabled(false);
        }
    }

    @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
    public void setToolbarColor(int color){
        if(color != 0){
            mToolbar.setBackgroundColor(getResources().getColor(color));
            nodataToolbar.setBackgroundColor(getResources().getColor(color));
        }
    }

    /**
     * 设置标题
     */
    public void setTitle(String title) {
        if (!TextUtils.isEmpty(title)) {
            if (mTitle != null) {
                mTitle.setText(title);
            }
            if (nodataTitle != null) {
                nodataTitle.setText(title);
            }
        }
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
}
