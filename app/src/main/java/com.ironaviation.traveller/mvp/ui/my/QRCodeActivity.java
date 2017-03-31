package com.ironaviation.traveller.mvp.ui.my;

import android.view.LayoutInflater;
import android.view.View;

import com.ironaviation.traveller.R;
import com.ironaviation.traveller.common.AppComponent;
import com.ironaviation.traveller.common.WEActivity;

/**
 * 项目名称：Traveller
 * 类描述：
 * 创建人：starRing
 * 创建时间：2017-03-31 16:53
 * 修改人：starRing
 * 修改时间：2017-03-31 16:53
 * 修改备注：
 */
public class QRCodeActivity extends WEActivity {


    @Override
    protected void nodataRefresh() {

    }

    @Override
    protected void setupActivityComponent(AppComponent appComponent) {

    }

    @Override
    protected View initView() {
        return LayoutInflater.from(this).inflate(R.layout.activity_qr_code, null, false);

    }

    @Override
    protected void initData() {

    }
}
