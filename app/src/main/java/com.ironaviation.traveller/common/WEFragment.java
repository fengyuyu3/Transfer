package com.ironaviation.traveller.common;

import android.os.Handler;
import android.view.WindowManager;

import com.ironaviation.traveller.mvp.ui.widget.CustomProgress;
import com.jess.arms.base.BaseFragment;
import com.jess.arms.mvp.Presenter;
import com.squareup.leakcanary.RefWatcher;

/**
 * Created by jess on 8/5/16 14:11
 * contact with jess.yan.effort@gmail.com
 */
public abstract class WEFragment<P extends Presenter> extends BaseFragment<P> {
    protected WEApplication mWeApplication;
    private CustomProgress customProgress;

    @Override
    protected void ComponentInject() {
        mWeApplication = (WEApplication)mActivity.getApplication();
        setupFragmentComponent(mWeApplication.getAppComponent());
    }

    //提供AppComponent(提供所有的单例对象)给子类，进行Component依赖
    protected abstract void setupFragmentComponent(AppComponent appComponent);

    @Override
    public void onDestroy() {
        super.onDestroy();
        RefWatcher watcher = WEApplication.getRefWatcher(getActivity());//使用leakCanary检测fragment的内存泄漏
        if (watcher != null) {
            watcher.watch(this);
        }
        this.mWeApplication =null;
    }
    /**
     * 显示正在加载的进度条
     */
    public void showProgressDialog() {
        if (customProgress != null
                && customProgress.isShowing()) {
            customProgress.miss();
            customProgress = null;
        }
        try {
            customProgress = new CustomProgress(getActivity());
            //customProgress.show(this, "加载中...", true, null);
            customProgress.show(getActivity(), "", true, null);
        } catch (WindowManager.BadTokenException exception) {
            exception.printStackTrace();
        }
    }

    public void showProgressDialog(String msg) {
        if (customProgress != null
                && customProgress.isShowing()) {
            customProgress.miss();
            customProgress = null;
        }
        try {
            customProgress = new CustomProgress(getActivity());
            customProgress.show(getActivity(), msg, true, null);
        } catch (WindowManager.BadTokenException exception) {
            exception.printStackTrace();
        }
    }

    /**
     * 隐藏正在加载的进度条
     */
    public void dismissProgressDialog() {


        if (customProgress != null) {

            new Handler().postDelayed(new Runnable() {

                public void run() {
                    //execute the task
                    customProgress.miss();
                }
            }, 300);

        }

    }
}
