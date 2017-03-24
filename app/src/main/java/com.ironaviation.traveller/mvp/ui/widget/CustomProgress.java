package com.ironaviation.traveller.mvp.ui.widget;

import android.app.Dialog;
import android.content.Context;
import android.graphics.drawable.AnimationDrawable;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;

import com.ironaviation.traveller.R;


/**
 * Created by starRing on 2016-07-06.
 */
public class CustomProgress extends Dialog {
    public CustomProgress dialog;

    public CustomProgress(Context context) {
        super(context);
    }

    public CustomProgress(Context context, int theme) {
        super(context, theme);
    }

    /**
     * 当窗口焦点改变时调用
     */
    public void onWindowFocusChanged(boolean hasFocus) {
        ImageView imageView = (ImageView) findViewById(R.id.spinnerImageView);
        // 获取ImageView上的动画背景
        AnimationDrawable spinner = (AnimationDrawable) imageView
                .getBackground();
        // 开始动画
        spinner.start();
    }

    /**
     * 给Dialog设置提示信息
     *
     * @param message
     */
    public void setMessage(CharSequence message) {
        if (message != null && message.length() > 0) {
            findViewById(R.id.tv_message).setVisibility(View.VISIBLE);
            TextView txt = (TextView) findViewById(R.id.tv_message);
            txt.setText(message);
            txt.invalidate();
        }
    }

	/*
     * public static void miss() { dialog.dismiss(); }
	 */

    /**
     * 弹出自定义ProgressDialog
     *
     * @param context        上下文
     * @param message        提示
     * @param cancelable     是否按返回键取消
     * @param cancelListener 按下返回键监听
     * @return
     */
    public CustomProgress show(Context context, CharSequence message,
                               boolean cancelable, OnCancelListener cancelListener) {
        dialog = new CustomProgress(context, R.style.Custom_Progress);
        dialog.setTitle("");
        dialog.setContentView(R.layout.progress_custom);

        if (message == null || message.length() == 0) {
            dialog.findViewById(R.id.tv_message).setVisibility(View.GONE);
        } else {
            TextView txt = (TextView) dialog.findViewById(R.id.tv_message);
            txt.setText(message);
        }
        // 设置点击进度对话框外的区域对话框不消失
        dialog.setCanceledOnTouchOutside(false);
        // 按返回键是否取消
        dialog.setCancelable(cancelable);
        // 监听返回键处理
        dialog.setOnCancelListener(cancelListener);
        // 设置居中
        dialog.getWindow().getAttributes().gravity = Gravity.CENTER;

        Window window = dialog.getWindow();

        WindowManager.LayoutParams lp = window.getAttributes();
        // 设置背景层透明度
        lp.dimAmount = 0.35f;
        dialog.getWindow().setAttributes(lp);
        // dialog.getWindow().addFlags(WindowManager.LayoutParams.FLAG_BLUR_BEHIND);
        dialog.show();

        return dialog;
    }

    public void miss() {
        dialog.dismiss();

    }
}