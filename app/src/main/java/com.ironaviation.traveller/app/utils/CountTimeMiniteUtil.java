package com.ironaviation.traveller.app.utils;

import android.os.CountDownTimer;
import android.support.v4.content.ContextCompat;
import android.widget.TextView;

import com.ironaviation.traveller.R;
import com.ironaviation.traveller.common.WEApplication;

/**
 * Created by Administrator on 2017/4/25.
 */

public class CountTimeMiniteUtil  extends CountDownTimer {

    private String format = "HH:mm";
    private TextView mSend;
    public CountTimeMiniteUtil(long millisInFuture, long countDownInterval, TextView text)
    {
        super(millisInFuture, countDownInterval);
        this.mSend = text;
    }

    @Override
    public void onFinish()
    {
        mSend.setTextColor(ContextCompat.getColor(WEApplication.getContext(), R.color.code_grey));
        mSend.setText(mSend.getResources().getText(R.string.travel_payment_lose));
    }

    @Override
    public void onTick(long millisUntilFinished)
    {
        mSend.setTextColor(ContextCompat.getColor(WEApplication.getContext(), R.color.code_grey));
        mSend.setText(getTime(millisUntilFinished));
    }

    public String getTime(long time){
        String t = null;
        int hour = (int) (time/(60*60*1000));
        if(hour < 10){
            t = "0"+hour+"小时";
        }else{
            t = hour+"小时";
        }
        int minite = (int) ((time-hour*60*60*1000)/(60*1000));
        if(minite < 10){
            t = t+"0"+minite+"分";
        }else{
            t = t+""+minite+"分";
        }
        return t;
    }
}
