package com.ironaviation.traveller.mvp.model.api.service;

import com.google.gson.JsonObject;
import com.ironaviation.traveller.mvp.model.api.Api;
import com.ironaviation.traveller.mvp.model.entity.AppVersionEntity;
import com.ironaviation.traveller.mvp.model.entity.BaseData;
import com.ironaviation.traveller.mvp.model.entity.Login;
import com.ironaviation.traveller.mvp.model.entity.LoginEntity;
import com.ironaviation.traveller.mvp.model.entity.request.AddressLimitRequest;
import com.ironaviation.traveller.mvp.model.entity.request.AirportGoInfoRequest;
//import com.ironaviation.traveller.mvp.model.entity.request.CancelBookingRequest;
import com.ironaviation.traveller.mvp.model.entity.request.BIDRequest;
import com.ironaviation.traveller.mvp.model.entity.request.CancelBookingRequest;
import com.ironaviation.traveller.mvp.model.entity.request.CancelOrderRequest;
import com.ironaviation.traveller.mvp.model.entity.request.IdentificationRequest;
import com.ironaviation.traveller.mvp.model.entity.request.MessageRequest;
import com.ironaviation.traveller.mvp.model.entity.request.PhoneRequest;
import com.ironaviation.traveller.mvp.model.entity.request.RouteListMoreRequest;
import com.ironaviation.traveller.mvp.model.entity.request.TravelRequest;
import com.ironaviation.traveller.mvp.model.entity.request.UpdateAddressBookRequest;
import com.ironaviation.traveller.mvp.model.entity.response.AddressResponse;
import com.ironaviation.traveller.mvp.model.entity.response.CancelBookingInfo;
import com.ironaviation.traveller.mvp.model.entity.response.CommentTag;
import com.ironaviation.traveller.mvp.model.entity.response.CommentsInfo;
import com.ironaviation.traveller.mvp.model.entity.response.Flight;
import com.ironaviation.traveller.mvp.model.entity.response.IdentificationResponse;
import com.ironaviation.traveller.mvp.model.entity.response.MessageResponse;
import com.ironaviation.traveller.mvp.model.entity.response.PassengersResponse;
import com.ironaviation.traveller.mvp.model.entity.response.RouteListResponse;
import com.ironaviation.traveller.mvp.model.entity.response.RouteStateResponse;
import com.ironaviation.traveller.mvp.model.entity.response.TravelResponse;

import java.util.List;

import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;
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

    @GET(Api.GET_MESSAGES)
    Observable<BaseData<MessageResponse>> getMessageData(@Query("pageSize") int pageSize, @Query("pageIndex") int pageIndex);

    @GET(Api.FLIGHT)
    Observable<BaseData<Flight>> getFlightInfo(@Query("flightNo") String flightNo, @Query("date") String date);

    @POST(Api.APP_SIGN_OUT)
    Observable<BaseData<JsonObject>> signOut();

    @GET(Api.GET_USER_ADDRESS_BOOK)
    Observable<BaseData<List<UpdateAddressBookRequest>>> getUserAddressBook();

    @POST(Api.UPDATE_ADDRESS_BOOK)
    Observable<BaseData<List<JsonObject>>> updateAddressBook(@Body UpdateAddressBookRequest params);

    @POST(Api.DELETE_ADDRESS_BOOK)
    Observable<BaseData<List<JsonObject>>> deleteAddressBook(@Query("uabId") String uabId);


    @POST(Api.VALID_REAL_ID_CARD)
    Observable<BaseData<IdentificationResponse>> identification(@Body IdentificationRequest params);


    @POST(Api.PRECLEAR_PORT)
    Observable<BaseData<AirportGoInfoRequest>> getAirPortInfo(@Body AirportGoInfoRequest params);

   /* @GET(Api.ROUTE_DETAILS_MORE)
    Observable<BaseData<RouteListResponse>> getRouteInfo(@Query("bid") String bid);*/

    @POST(Api.ROUTE_DETAILS_MORE)
    Observable<BaseData<RouteListResponse>> getRouteInfoMore(@Body RouteListMoreRequest params);

    @GET(Api.ROUTE_DETAILS)
    Observable<BaseData<RouteStateResponse>> getRouteStateInfo(@Query("bid") String bid);

    @GET(Api.CANCEL_BOOKING)
    Observable<BaseData<CancelBookingInfo>> getCancelBookInfo(@Path("bid") String bid);

    @POST(Api.CANCEL_BOOKING)
    Observable<BaseData<Boolean>> cancelBooking(@Path("bid") String bid, @Body CancelOrderRequest params);


    @POST(Api.ADD_ORDER)
    Observable<BaseData<Boolean>> isOrderSuccess(@Body BIDRequest params);

    @GET(Api.PAYMENT)
    Observable<BaseData> getPayment(@Query("bid") String bid, @Query("payMethod") String payMethod);

    @GET(Api.COMMENTTAGS)
    Observable<BaseData<List<CommentTag>>> getCommentTagInfo();

    @POST(Api.COMMENTS)
    Observable<BaseData<Boolean>> isCommentSuccess(@Body CommentsInfo params);

    @POST(Api.ENTER_PORT)
    Observable<BaseData<Boolean>> isOrderSuccessOn(@Body BIDRequest params);

    @POST(Api.VALID_CODE)
    Observable<BaseData<Boolean>> isValidCode(@Body PhoneRequest params);

    @POST(Api.CONFIRM_ARRIVE)
    Observable<BaseData<Boolean>> isConfirmArrive(@Body BIDRequest params);

    @POST(Api.CONFIRM_PICKUP)
    Observable<BaseData<Boolean>> isConfirmPickup(@Body BIDRequest params);

    @GET(Api.GET_OTHER_PASSENGER)
    Observable<BaseData<List<PassengersResponse>>> getOtherPassengerInfo(@Query("bid") String bid);

    @POST(Api.ADDRESS_LIMIT)
    Observable<BaseData<AddressResponse>> isAddress(@Body AddressLimitRequest params);

    @GET(Api.GET_LATEST_VERSION)
    Observable<BaseData<AppVersionEntity>> getLatestVersion();

    @POST(Api.HAS_BOOK)
    Observable<BaseData<AirportGoInfoRequest>> getHasBookInfo(@Body AirportGoInfoRequest params);
}
