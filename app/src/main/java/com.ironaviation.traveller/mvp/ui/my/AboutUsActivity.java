package com.ironaviation.traveller.mvp.ui.my;

import android.support.v4.content.ContextCompat;
import android.view.LayoutInflater;
import android.view.View;

import com.ironaviation.traveller.R;
import com.ironaviation.traveller.common.AppComponent;
import com.ironaviation.traveller.common.WEActivity;

/**
 * 项目名称：Traveller
 * 类描述：
 * 创建人：starRing
 * 创建时间：2017-04-21 14:44
 * 修改人：starRing
 * 修改时间：2017-04-21 14:44
 * 修改备注：
 */
public class AboutUsActivity extends WEActivity {
    @Override
    protected void nodataRefresh() {

    }

    @Override
    protected void setupActivityComponent(AppComponent appComponent) {

    }

    @Override
    protected View initView() {
        return LayoutInflater.from(this).inflate(R.layout.activity_about_us, null, false);
    }

    @Override
    protected void initData() {
        setTitle(getString(R.string.about_us));
        setNavigationIcon(ContextCompat.getDrawable(this, R.mipmap.ic_back));
        mToolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }
}
