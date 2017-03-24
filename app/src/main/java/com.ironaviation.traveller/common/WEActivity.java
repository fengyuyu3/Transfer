package com.ironaviation.traveller.common;

import android.os.Handler;
import android.view.WindowManager;

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

    @Override
    protected void initId() {
        initNoDataId();
    }

    @Override
    protected int getNodataId() {
        return R.layout.include_nodata;
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

}
