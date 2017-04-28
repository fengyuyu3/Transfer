package com.ironaviation.traveller.common;

import android.annotation.TargetApi;
import android.content.Intent;
import android.graphics.drawable.AnimationDrawable;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.content.ContextCompat;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.util.Log;
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

import org.simple.eventbus.EventBus;

import static com.jess.arms.base.AppManager.APPMANAGER_MESSAGE;
import static com.jess.arms.base.AppManager.START_ACTIVITY;

/**
 * Created by jess on 8/5/16 13:13
 * contact with jess.yan.effort@gmail.com
 */
public abstract class WEActivity<P extends Presenter> extends BaseWEActivity<P> {


    protected Toolbar mToolbar, nodataToolbar;
    protected TextView mTitle, nodataTitle, mFunctionRight, nodataFunctionRight;
    protected ImageView mIvFunctionLeft, nodataIvFunctionLeft,mIvFunctionRight,noDataIvFunctionRight;
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
    protected void setRightFunctionText(String text,View.OnClickListener listener) {
        if (!TextUtils.isEmpty(text)) {
            if (mFunctionRight != null) {
                mFunctionRight.setVisibility(View.VISIBLE);
                mFunctionRight.setTextColor(ContextCompat.getColor(this, R.color.white));
                mFunctionRight.setText(text);
                mFunctionRight.setOnClickListener(listener);
            }

        }
    }

    /**
     * 设置右功能键+颜色
     */
    protected void setRightFunctionText(String text, int color,View.OnClickListener listener) {
        if (!TextUtils.isEmpty(text)) {
            if (mFunctionRight != null) {
                mFunctionRight.setVisibility(View.VISIBLE);
                mFunctionRight.setTextColor(ContextCompat.getColor(this, color));
                mFunctionRight.setText(text);
                mFunctionRight.setOnClickListener(listener);

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

        }
    }
    /**
     * 设置自定义左功能键
     */
    protected void setLeftFunction(Drawable text,View.OnClickListener listener) {
        if (text != null) {
            if (mIvFunctionLeft != null) {
                mIvFunctionLeft.setVisibility(View.VISIBLE);
                mIvFunctionLeft.setImageDrawable(text);
                mIvFunctionLeft.setOnClickListener(listener);
            }
        }
    }

    /**
     * 设置自定义右功能键
     */
    protected void setRightFunction(Drawable text, View.OnClickListener listener) {
        if (text != null) {
            if (mIvFunctionRight != null) {
                mIvFunctionRight.setVisibility(View.VISIBLE);
                mIvFunctionRight.setImageDrawable(text);
                mIvFunctionRight.setOnClickListener(listener);
            }
           /* if (nodataIvFunctionLeft != null) {
                nodataIvFunctionLeft.setVisibility(View.VISIBLE);
                nodataIvFunctionLeft.setImageDrawable(text);
            }*/
        }
    }

    /**
     * 设置自定义右功能键
     */
    protected void setRightFunction(int resId, View.OnClickListener listener) {
        if (resId != 0) {
            if (mIvFunctionRight != null) {
                mIvFunctionRight.setVisibility(View.VISIBLE);
                mIvFunctionRight.setImageResource(resId);
                mIvFunctionRight.setOnClickListener(listener);
            }
           /* if (nodataIvFunctionLeft != null) {
                nodataIvFunctionLeft.setVisibility(View.VISIBLE);
                nodataIvFunctionLeft.setImageDrawable(text);
            }*/
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
            if (nodataToolbar != null) {
                nodataToolbar.setNavigationIcon(text);
            }
        }
    }

    public void setErrorRefresh() {
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
        mNodataSwipeRefresh.setColorSchemeColors(ContextCompat.getColor(this, R.color.colorPrimaryDark), ContextCompat.getColor(this, R.color.colorPrimaryDark));
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
        mIvFunctionRight= (ImageView) getDelegate().findViewById(R.id.iv_function_right);
        nodataToolbar = (Toolbar) getDelegate().findViewById(R.id.nodata_toolbar);
        nodataTitle = (TextView) getDelegate().findViewById(R.id.nodata_tv_title);
        nodataFunctionRight = (TextView) getDelegate().findViewById(R.id.nodata_tv_function_right);
        nodataIvFunctionLeft = (ImageView) getDelegate().findViewById(R.id.nodata_iv_function_left);
        noDataIvFunctionRight= (ImageView) getDelegate().findViewById(R.id.nodata_iv_function_right);
        if (mToolbar != null) {
            setSupportActionBar(mToolbar);
            getSupportActionBar().setDisplayShowTitleEnabled(false);
        }
        if (nodataToolbar != null) {
            setSupportActionBar(nodataToolbar);
            getSupportActionBar().setDisplayShowTitleEnabled(false);
        }
    }

    @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
    public void setToolbarColor(int color) {
        if (color != 0) {
            mToolbar.setBackgroundColor(ContextCompat.getColor(WEApplication.getContext(), color));
            nodataToolbar.setBackgroundColor(ContextCompat.getColor(WEApplication.getContext(), color));
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

    /*public void startActivity(Intent intent) {
        super.startActivity(intent);
        Message message = new Message();
        message.what = START_ACTIVITY;
        message.obj = intent;
        EventBus.getDefault().post(message, APPMANAGER_MESSAGE);
    }*/

    public void startActivity(Class clazz) {
        Intent intent = new Intent(this, clazz);
        Message message = new Message();
        message.what = START_ACTIVITY;
        message.obj = intent;
        EventBus.getDefault().post(message, APPMANAGER_MESSAGE);
    }

    public void startActivity(Class clazz, Bundle c) {
        Intent intent = new Intent(this, clazz);
        intent.putExtras(c);
        Message message = new Message();
        message.what = START_ACTIVITY;
        message.obj = intent;
        EventBus.getDefault().post(message, APPMANAGER_MESSAGE);
    }
}
