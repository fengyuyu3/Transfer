package com.ironaviation.traveller.app;

/**
 * Created by jess on 8/30/16 16:39
 * Contact with jess.yan.effort@gmail.com
 */
public interface EventBusTags {

    String SHUT_DOWN = "shut_down";
    String TRAVEL_DETAILS = "TravelDetailsActivity";
    String WAITING_PAYMENT = "WaitingPaymentActivity";
    String TRAVEL_DETAILS_ON = "TravelDetailsOnActivity";

    String DIALOG_EVENT = "dialog";
    String FLIGHT_INFO = "flight_info";
    String FLIGHT = "flight";
    String AIRPORT_TIME = "airport_time";
    String ALIPAY = "alipay";
    String AIRPORT_GO = "airport_go";
    String AIRPORT_ON = "airport_on";
    String AIRPORT_Z_GO = "airport_z_go";
    String AIRPORT_Z_ON = "airport_z_on";
    String PAYMENT = "payment";

    String FLIGHT_INFO_ON = "flight_info_on";
    String FLIGHT_ON = "flight_on";
    String AIRPORT_PUSH_ON = "airport_push_on";
    String AIRPORT_PUSH_OFF = "airport_push_off";
    String REFRESH = "refresh";
    String PUSH_ON = "push_on";  //接机推送
    String PUSH_OFF= "push_off"; //送机推送
    String TIMEOUT_NO_PAY = "timeout_no_pay"; //超时未支付
    String ROUTE_INVALID = "route_invalid";   //超时支付界面
    String ROUTE_CANCEL = "route_cancel";     //行程取消 送机
    String TRAVEL_DETAIL = "travel_detail";
    String TRAVEL_DETAIL_ON = "travel_detail_on";

    String USUAL_ADDRESS = "UsualAddress";
    String ADDRESS = "Address";
    String ALIPAY_RESULT = "alipay_result";
    String WX_PAY = "wx_pay";
    String PAYMENT_FINISH = "payment_finish";
    String WX_FILED = "wx_filed";
    String TIME_OUT = "time_out"; //订单失效
    String PUSH_ONLINE = "push_online";
    String PUSH_REFRESH = "push_refresh";
    String LOGIN_OTHER = "login_other";
    String ID_CARD_ON = "id_card_on"; //接机
    String ID_CARD = "id_card";  //送机
    String NO_NETWORK = "no_network";
    String TRAVEL_DETAILS_ON_CANCEL = "cancel";
}
