package com.ironaviation.traveller.common;

import android.content.Intent;
import android.graphics.drawable.Drawable;
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
import com.umeng.analytics.MobclickAgent;

/**
 * 项目名称：Transfer
 * 类描述：
 * 创建人：flq
 * 创建时间：2017/3/27 10:52
 * 修改人：
 * 修改时间：2017/3/27 10:52
 * 修改备注：
 */

public abstract class  BaseWEActivity<P extends Presenter> extends BaseActivity<P> {

    private CustomProgress customProgress;
    protected WEApplication mWeApplication;
    @Override
    protected void ComponentInject() {
        mWeApplication = (WEApplication) getApplication();
        setupActivityComponent(mWeApplication.getAppComponent());
    }

    //提供AppComponent(提供所有的单例对象)给子类，进行Component依赖
    protected abstract void setupActivityComponent(AppComponent appComponent);


    @Override
    protected void onDestroy() {
        super.onDestroy();
        this.mWeApplication = null;
        if (customProgress != null) {
            customProgress.miss();
        }
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
            customProgress = new CustomProgress(BaseWEActivity.this);
            //customProgress.show(this, "加载中...", true, null);
            customProgress.show(this, "", true, null);
        } catch (WindowManager.BadTokenException exception) {
            exception.printStackTrace();
            MobclickAgent.reportError(WEApplication.getContext(), exception);

        }
    }

    public void showProgressDialog(String msg) {
        if (!isFinishing() && customProgress != null
                && customProgress.isShowing()) {
            customProgress.miss();
            customProgress = null;
        }
        try {
            customProgress = new CustomProgress(BaseWEActivity.this);
            customProgress.show(this, msg, true, null);
        } catch (WindowManager.BadTokenException exception) {
            exception.printStackTrace();
            MobclickAgent.reportError(WEApplication.getContext(), exception);

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



}
