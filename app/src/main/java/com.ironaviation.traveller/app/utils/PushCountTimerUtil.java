package com.ironaviation.traveller.app.utils;

import android.app.Activity;
import android.os.CountDownTimer;
import android.support.v4.content.ContextCompat;
import android.widget.TextView;

import com.igexin.sdk.PushManager;
import com.ironaviation.traveller.R;
import com.ironaviation.traveller.common.WEApplication;

/**
 * Created by Administrator on 2017/5/10.
 */

public class PushCountTimerUtil extends CountDownTimer {

    private Activity mActivity;
    public PushCountTimerUtil(Activity activity,long millisInFuture, long countDownInterval) {
        super(millisInFuture, countDownInterval);
        this.mActivity = activity;
    }

    @Override
    public void onFinish() {
    }

    @Override
    public void onTick(long millisUntilFinished) {
        PushManager.getInstance().initialize(mActivity);
    }
}
