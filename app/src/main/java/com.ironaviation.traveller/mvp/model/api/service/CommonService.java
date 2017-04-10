package com.ironaviation.traveller.mvp.model.api.service;

import com.google.gson.JsonObject;
import com.ironaviation.traveller.mvp.model.api.Api;
import com.ironaviation.traveller.mvp.model.entity.BaseData;
import com.ironaviation.traveller.mvp.model.entity.Login;
import com.ironaviation.traveller.mvp.model.entity.LoginEntity;
import com.ironaviation.traveller.mvp.model.entity.request.ClearanceOrderRequest;
import com.ironaviation.traveller.mvp.model.entity.request.MessageRequest;
import com.ironaviation.traveller.mvp.model.entity.request.TravelRequest;
import com.ironaviation.traveller.mvp.model.entity.response.Flight;
import com.ironaviation.traveller.mvp.model.entity.response.MessageResponse;
import com.ironaviation.traveller.mvp.model.entity.response.TravelResponse;

import org.json.JSONObject;

import java.util.List;

import retrofit2.http.Body;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Query;
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

    @POST(Api.Travel)
    Observable<BaseData<List<TravelResponse>>> getTravelData(@Body TravelRequest params);

    @POST(Api.MESSAGE)
    Observable<BaseData<List<MessageResponse>>> getMessageData(@Body MessageRequest params);

    @GET(Api.FLIGHT)
    Observable<BaseData<Flight>> getFlightInfo(@Query("flightNo") String flightNo, @Query("date") String date);

    @POST(Api.APP_SIGN_OUT)
    Observable<BaseData<JsonObject>> signOut();

    @POST(Api.CLEARANCE_ORDER)
    Observable<BaseData<ClearanceOrderRequest>> getClearanceInfo(@Body ClearanceOrderRequest params);
}
