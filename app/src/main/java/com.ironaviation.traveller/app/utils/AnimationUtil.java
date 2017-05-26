package com.ironaviation.traveller.app.utils;

import android.content.Context;
import android.view.View;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.TranslateAnimation;

/**
 * Created by Administrator on 2017/5/16.
 */

public class AnimationUtil {

    public static AnimationUtil animationUtil = null;
    private AlphaAnimation mShowAction,mHiddenAction;
    private TranslateAnimation mTHiddenAction,mTShowAction;

    public static AnimationUtil getInstance(Context context){
        if(animationUtil == null){
            animationUtil = new AnimationUtil();
        }
        return animationUtil;
    }

    public void showAnimation(View view){
        mShowAction = new AlphaAnimation(0.1f,1.0f);
        mShowAction.setDuration(1000);
        view.startAnimation(mShowAction);
    }

    public void hiddenAnimation(View view){
        mHiddenAction = new AlphaAnimation(1.0f,0.1f);
        mHiddenAction.setDuration(1000);
        view.startAnimation(mHiddenAction);
    }

    public void moveToViewBottom(View view) {
        mTHiddenAction = new TranslateAnimation(Animation.RELATIVE_TO_SELF, 0.0f,
                Animation.RELATIVE_TO_SELF, 0.0f, Animation.RELATIVE_TO_SELF,
                0.0f, Animation.RELATIVE_TO_SELF, 1.0f);
        mTHiddenAction.setDuration(500);
        view.startAnimation(mTHiddenAction);
    }

    /**
     * 从控件的底部移动到控件所在位置
     *
     * @return
     */
    public void moveToViewLocation(View view) {
        mTShowAction = new TranslateAnimation(Animation.RELATIVE_TO_SELF, 0.0f,
                Animation.RELATIVE_TO_SELF, 0.0f, Animation.RELATIVE_TO_SELF,
                1.0f, Animation.RELATIVE_TO_SELF, 0.0f);
        mTShowAction.setDuration(500);
        view.startAnimation(mTShowAction);
    }

    public void moveToViewCenter(View view){
        mTShowAction = new TranslateAnimation(Animation.RELATIVE_TO_SELF, 0.0f,
                Animation.RELATIVE_TO_SELF, 0.0f, Animation.RELATIVE_TO_SELF,
                -1.0f, Animation.RELATIVE_TO_SELF, 0.0f);
        mTShowAction.setDuration(300);
        view.startAnimation(mTShowAction);
    }

    public void moveToViewTop(View view){
        mTShowAction = new TranslateAnimation(Animation.RELATIVE_TO_SELF, 0.0f,
                Animation.RELATIVE_TO_SELF, 0.0f, Animation.RELATIVE_TO_SELF,
                0.0f, Animation.RELATIVE_TO_SELF, -1.0f);
        mTShowAction.setDuration(300);
        view.startAnimation(mTShowAction);
    }
}
