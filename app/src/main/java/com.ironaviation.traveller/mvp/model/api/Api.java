package com.ironaviation.traveller.mvp.model.api;

/**
 * Created by jess on 8/5/16 11:25
 * contact with jess.yan.effort@gmail.com
 */
public interface Api {
//    String APP_DOMAIN = "http://th-api.bestwise.net";// 测试
//    String APP_DOMAIN = "http://192.168.0.75:8051";
//    String APP_DOMAIN = "http://192.168.0.20:8050";
    String APP_DOMAIN = "http://th-api.bestwise.net"; //测试服务器
    String LOGIN = "/api/Users/LoginWithSMS";// 登录
    String Travel = "";
    String MESSAGE = "";
    String FLIGHT = "/api/Trips/GetAirlineInfo"; //航班信息
    String APP_SIGN_OUT="/api/Users/AppSignOut";//POST 注销登录
    String GET_USER_ADDRESS_BOOK="/api/Users/GetUserAddressBook";//Get 获取用户常用地址
    String UPDATE_ADDRESS_BOOK="/api/Users/UpdateAddressBook";//POST 更新用户地址
    String DELETE_ADDRESS_BOOK="/api/Users/DeleteAddressBook";//POST 删除用户常用地址
    String PRECLEAR_PORT = "/api/Booking/PreClearPortBooking";//订单预览出港信息
    String ROUTE_DETAILS = "/api/Booking/GetDetail";
    String ROUTE_DETAILS_MORE = "/api/Booking/GetBookings"; //行程列表
    String VALIDREAL_IDCARD ="/api/Users/ValidRealIDCard"; //实名认证

    String VALID_REAL_ID_CARD="/api/Users/ValidRealIDCard";//POST 实名认证
    String ADD_ORDER="/api/Booking/AddClearPortBooking";   //添加订单
    String PAYMENT = "/api/Booking/PayBooking";

    String CANCEL_BOOKING="/api/Booking/CancelBooking/{bid}";//GET {bid} 获取取消预约详情
                                                       //POST {bid} 获取取消预约详情
    String COMMENTTAGS="/api/Booking/GetCommentTags"; //获取评价标签
    String COMMENTS ="/api/Booking/AddComments"; //添加标签
    String ENTER_PORT="/api/Booking/AddEnterPortBooking";  //接机订单
    String VALID_CODE = "/api/Users/SendValidSMS";
    String CONFIRM_ARRIVE = "/api/Booking/ConfirmArrive";
    String CONFIRM_PICKUP ="/api/Booking/ConfirmPickup"; //乘客确认上车
    String GET_OTHER_PASSENGER = "/api/Booking/GetOtherPassengersStatus"; //其他乘客
    int RequestSuccess = 1;


    String GET_MESSAGES="/api/Users/GetMessages"; //Get 获取用户消息

    String PHONE_PRICE_ROLE="http://th-api.bestwise.net/app/view/phonePriceRole.html";
    String PHONE_CANCEL_ROLE_ON ="http://th-api.bestwise.net/app/view/phoneCancelRole.html?type=jieji";
    String PHONE_CANCEL_ROLE_OFF ="http://th-api.bestwise.net/app/view/phoneCancelRole.html";
    String PHONE_ID_CARD ="http://th-api.bestwise.net/app/view/phoneIdCard.html";///*?userName=XXXXXX&number=XXXXXXX*/
    String PHONE_INTRODUCE ="http://th-api.bestwise.net/app/view/phoneIntroduce.html";



}
