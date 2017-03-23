package com.ironaviation.traveller.common;

import com.ironaviation.traveller.R;
import com.jess.arms.base.BaseActivity;
import com.jess.arms.mvp.Presenter;

/**
 * Created by jess on 8/5/16 13:13
 * contact with jess.yan.effort@gmail.com
 */
public abstract class WEActivity<P extends Presenter> extends BaseActivity<P> {
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
    }

    @Override
    protected void initId() {
        initNoDataId();
    }

    @Override
    protected int getNodataId() {
        return R.layout.include_nodata;
    }

}
