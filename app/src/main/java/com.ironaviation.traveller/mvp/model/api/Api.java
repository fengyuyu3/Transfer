package com.ironaviation.traveller.mvp.model.api;

import retrofit2.http.POST;

/**
 * Created by jess on 8/5/16 11:25
 * contact with jess.yan.effort@gmail.com
 */
public interface Api {
//    String APP_DOMAIN = "http://th-api.bestwise.net";// 测试
    String APP_DOMAIN = "http://192.168.1.144:8051";
    String LOGIN = "/api/Drivers/SignIn";// 登录
    String Travel = "";
    String MESSAGE = "";
    String FLIGHT = "/api/Trips/GetAirlineInfo"; //航班信息

    int RequestSuccess = 1;


}
