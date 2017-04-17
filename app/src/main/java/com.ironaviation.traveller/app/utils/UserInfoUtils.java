package com.ironaviation.traveller.app.utils;

import android.content.Context;

import com.ironaviation.traveller.mvp.constant.Constant;
import com.ironaviation.traveller.mvp.model.entity.LoginEntity;
import com.ironaviation.traveller.mvp.ui.main.MainActivity;
import com.jess.arms.utils.DataHelper;

/**
 * Created by Administrator on 2017/4/17.
 */

public class UserInfoUtils {

    public static UserInfoUtils userInfoUtils;
    public static UserInfoUtils getInstance(){
        LoginEntity loginEntity = null;
        if(userInfoUtils == null){
            userInfoUtils = new UserInfoUtils();
        }
        return userInfoUtils;
    }

    public  LoginEntity getInfo(Context context){
        LoginEntity loginEntity = null;
        if (DataHelper.getDeviceData(context,Constant.LOGIN) != null) {
            loginEntity = DataHelper.getDeviceData(context, Constant.LOGIN);
        }
        return loginEntity;
    }
}
