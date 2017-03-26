package com.ironaviation.traveller.app.utils;

import android.graphics.Color;
import android.os.CountDownTimer;
import android.support.v7.widget.AppCompatTextView;
import android.widget.TextView;

import com.ironaviation.traveller.R;

/**
 * Created by Administrator on 2016/12/8.
 */

public class CountTimerUtil extends CountDownTimer {

    private TextView mSend;
    public CountTimerUtil(long millisInFuture, long countDownInterval, TextView text)
    {
        super(millisInFuture, countDownInterval);
        this.mSend = text;
    }

    @Override
    public void onFinish()
    {
        mSend.setTextColor(mSend.getResources().getColor(R.color.code_grey));
        mSend.setText(mSend.getResources().getText(R.string.login_code));
        mSend.setClickable(true);
    }

    @Override
    public void onTick(long millisUntilFinished)
    {
        mSend.setTextColor(mSend.getResources().getColor(R.color.code_grey));
        mSend.setText(millisUntilFinished / 1000 + mSend.getResources().getString(R.string.login_code_reset));
        mSend.setClickable(false);
    }
}
