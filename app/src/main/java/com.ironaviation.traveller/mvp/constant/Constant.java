package com.ironaviation.traveller.mvp.constant;

/**
 * 项目名称：Transfer
 * 类描述：
 * 创建人：flq
 * 创建时间：2017/3/29 9:04
 * 修改人：
 * 修改时间：2017/3/29 9:04
 * 修改备注：
 */

public interface Constant {
    int AIRPORT_NO = 0;
    int AIRPORT_SUCCESS = 1;
    int AIRPORT_FAILURE = 2;

    int AIRPORT_TYPE_TERMINAL = 11;
    int AIRPORT_TYPE_SEAT = 12;

    int AIRPORT_FLY_NUM = 21;
    int AIRPORT_FLY_TIME = 22;
    int TRAVEL_CANCEL = 31;
    int TRAVEL_CUSTOMER = 32;

    int REBACK = 21;

    String LOGIN = "login";//登录信息
    String IDENTIFICATION = "identification";//登录信息
    String ADDRESS_HISTORY = "AddressHistory";//历史

    int TRAVEL_DETAILS_GOING = 41;
    int TRAVEL_DETAILS_COMPLETE = 42;
    int TRAVEL_DETAILS_ORDER = 43;
    int TRAVEL_DETAILS_ARRIVE = 44;

    int SEAT_NUM = 6;

    String SC_AIRPORT = "3u";
    int DEFULT_SEAT = 1;
    /*addressType*/
    String ADDRESS_TYPE = "addressType";
    String UABID = "uabid";
    int ADDRESS_TYPE_HOME = 991;
    int ADDRESS_TYPE_COMPANY = 992;
    int AIRPORT_GO = 993;
    int AIRPORT_ON = 994;
    double AIRPORT_T1_LONGITUDE=103.965305;
    double AIRPORT_T1_LATITUDE=30.585158;
    double AIRPORT_T2_LONGITUDE=103.961509;
    double AIRPORT_T2_LATITUDE=30.575978;
    String AIRPORT_T1 = "成都双流国际机场T1航站楼";
    String AIRPORT_T2 = "成都双流国际机场T2航站楼";

    int INDEX = 10;

    String STATUS = "status";
    String CHILD_STATUS = "child_status";
    String REGISTERED = "Registered"; // 预约成功
    String INHAND = "InHand";// 进行中
    String ARRIVED = "Arrived";// 已到达
    String CANCEL = "Cancel";// 已取消
    String BOOKSUCCESS = "BookSuccess";// 派单成功
    String COMPLETED = "Completed";// 已完成
    String NOTPAID = "NotPaid"; //未支付
    String INVALIDATION = "Invalidation"; //已失效
    String WAIT_APPRAISE = "wait"; //等待评价
    String TOSEND = "ToSend"; //接其他人
    String PICKUP = "PickUp"; //接您
    String ABORAD = "Aboard"; //已上车
    String BID = "bid";
    String SEPARATOR = "&!&";
    String SEPARATOR_OTHER = "!&!";

    String WECHAT = "WeChat";
    String ALIPAY = "AliPay";
    String UPAY = "UPay";

    String PHONE = "phone";

    int TYPE_AIRPORT_OFF = 101;//送機
    int TYPE_AIRPORT_ON = 102; //接機

    int SERVICEID = 138814;
    String CITY = "成都";
    String ON = "on";  //接机
    String OFF = "off"; //送机
    String CLEAR_PORT = "ClearPort"; //送机
    String ENTER_PORT = "EnterPort"; //接机


    String CLASS_PAY_INFO = "PayInfo";
    String CANCELREASON = "CancelReason"; //取消原因
    String PAYMENT= "payment"; //付款方式
    String REAL_PRICE  = "real_price";  //真实价格
    String FIXED_PRICE = "fixed_price"; //一口价
    String FREE_PASSENGER = "free_passenger"; //免费乘客
    String FREE_PASSENGER_PRICE = "free_passenger_price"; //免费乘客
    String PEOPLE_NUM = "num"; //乘客数
    String PAYMENT_NOMAL = "payment_nomal";
    String PAYMETHOD = "PayMethod";
    String PAY_WECHAT = "微信支付";
    String PAY_ALIPAY = "支付宝支付";
    String URL = "url";
    String TITLE = "title";
    int SETTING = 201;
    int AUTO_SETTTING = 202;

}