package com.ironaviation.traveller.mvp.ui.widget;

/**
 * 项目名称：Transfer
 * 类描述：
 * 创建人：starRing
 * 创建时间：2017-03-12 17:15
 * 修改人：starRing
 * 修改时间：2017-03-12 17:15
 * 修改备注：
 */

import android.app.ProgressDialog;
import android.os.Bundle;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.LinearLayout;

import com.ironaviation.traveller.R;
import com.ironaviation.traveller.common.WEApplication;
import com.jess.arms.base.BaseActivity;
import com.umeng.analytics.MobclickAgent;

/** 自定义 遮罩 控件 */
public class MaskingDialog extends ProgressDialog {

    private BaseActivity baseActivity;
    private View view;
    private FrameLayout cd_fl;

    /** 自定义 遮罩 控件 构造方法 【view = 视图】 */
    public MaskingDialog(BaseActivity baseActivity, View view, boolean cancelable) {
        super(baseActivity, R.style.customViewDialog);
        this.baseActivity = baseActivity;
        this.view = view;
        setCancelable(cancelable);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        try {
            getWindow().setLayout(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT);
            init();
        } catch (Exception e) {
            e.printStackTrace();
            MobclickAgent.reportError(WEApplication.getContext(), e);

        }
    }

    /** 初始化 */
    private void init() throws Exception {
        setContentView(R.layout.custom_dialog);
        cd_fl = (FrameLayout) findViewById(R.id.cd_fl);
        cd_fl.addView(view);
    }

}
