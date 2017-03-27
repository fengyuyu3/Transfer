package com.ironaviation.traveller.mvp.model.api;

import retrofit2.http.POST;

/**
 * Created by jess on 8/5/16 11:25
 * contact with jess.yan.effort@gmail.com
 */
public interface Api {
    String APP_DOMAIN = "http://th-api.bestwise.net";// 测试
    String BASE_URL = "/api";
    String LOGIN = "/api/Drivers/SignIn";// 登录
    String Travel = "";
    int RequestSuccess = 1;
}
