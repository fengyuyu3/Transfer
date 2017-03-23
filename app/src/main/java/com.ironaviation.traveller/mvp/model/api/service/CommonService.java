package com.ironaviation.traveller.mvp.model.api.service;

import com.ironaviation.traveller.mvp.model.api.Api;
import com.ironaviation.traveller.mvp.model.entity.BaseData;
import com.ironaviation.traveller.mvp.model.entity.Login;
import com.ironaviation.traveller.mvp.model.entity.LoginEntity;

import org.json.JSONObject;

import retrofit2.http.Body;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;
import rx.Observable;

/**
 * 存放通用的一些API
 * Created by jess on 8/5/16 12:05
 * contact with jess.yan.effort@gmail.com
 */
public interface CommonService {
    // @FormUrlEncoded
    @POST(Api.LOGIN)
    Observable<BaseData<LoginEntity>> login(@Body Login params);
}
