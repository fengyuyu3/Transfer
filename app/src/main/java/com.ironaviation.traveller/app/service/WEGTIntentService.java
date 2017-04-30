package com.ironaviation.traveller.app.service;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;

import com.google.gson.Gson;
import com.igexin.sdk.GTIntentService;
import com.igexin.sdk.PushManager;
import com.igexin.sdk.message.GTCmdMessage;
import com.igexin.sdk.message.GTTransmitMessage;
import com.ironaviation.traveller.app.EventBusTags;
import com.ironaviation.traveller.mvp.constant.Constant;
import com.ironaviation.traveller.mvp.model.entity.BaseData;
import com.ironaviation.traveller.mvp.model.entity.BasePushData;
import com.ironaviation.traveller.mvp.model.entity.response.PushResponse;
import com.ironaviation.traveller.mvp.ui.my.travel.PaymentDetailsActivity;
import com.ironaviation.traveller.mvp.ui.my.travel.TravelActivity;
import com.ironaviation.traveller.mvp.ui.my.travel.TravelCancelActivity;
import com.ironaviation.traveller.mvp.ui.my.travel.TravelDetailsActivity;
import com.ironaviation.traveller.mvp.ui.my.travel.TravelDetailsOnActivity;
import com.ironaviation.traveller.mvp.ui.payment.InvalidationActivity;

import org.simple.eventbus.EventBus;

import java.util.concurrent.CopyOnWriteArrayList;

/**
 * 项目名称：Traveller
 * 类描述：
 * 创建人：starRing
 * 创建时间：2017-03-27 14:59
 * 修改人：starRing
 * 修改时间：2017-03-27 14:59
 * 修改备注：
 */
/**
 * 继承 GTIntentService 接收来自个推的消息, 所有消息在线程中回调, 如果注册了该服务, 则务必要在 AndroidManifest中声明, 否则无法接受消息<br>
 * onReceiveMessageData 处理透传消息<br>
 * onReceiveClientId 接收 cid <br>
 * onReceiveOnlineState cid 离线上线通知 <br>
 * onReceiveCommandResult 各种事件处理回执 <br>
 */
public class WEGTIntentService extends GTIntentService {
    private Context mContext;

    public WEGTIntentService() {

    }

    @Override
    public void onReceiveServicePid(Context context, int pid) {
        this.mContext = context;
    }

    @Override
    public void onReceiveMessageData(Context context, GTTransmitMessage msg) {
        this.mContext = context;
        String taskid = msg.getTaskId();
        String messageid = msg.getMessageId();
        byte[] payload = msg.getPayload();
        boolean result = PushManager.getInstance()
                .sendFeedbackMessage(context, taskid, messageid, 90001);
        if (payload == null) {
        } else {
            String data = new String(payload);
            try{
                BasePushData response = new Gson().fromJson(data,BasePushData.class);
                /*if(response.getData().getStatus().equals(Constant.ENTER_PORT)){//接机
                    EventBus.getDefault().post(response,EventBusTags.AIRPORT_PUSH_ON);
                }else if(response.getData().getStatus().equals(Constant.CLEAR_PORT)){
                    EventBus.getDefault().post(response,EventBusTags.AIRPORT_PUSH_OFF);
                }*/
                if(!TextUtils.isEmpty(response.getData().getStatus())){
                    setStatus(response,context);
                }
            }catch (Exception e){
                e.printStackTrace();
            }
            /*TransparentMessageEntity transparentMessageEntity =
                    new Gson().fromJson(data, TransparentMessageEntity.class);*/
        }

    }

    public void setStatus(BasePushData response, Context context){
        switch (response.getData().getStatus()){
            case Constant.CLEAR_PORT: //送机
                setChildCode(response,context);
                break;
            case Constant.ENTER_PORT :
                setChildCodeOn(response,context);
                break;
        }
    }


    // int ORDER_SUCCESS = 211; //付款成功(预约成功) 不知道怎么处理
    public void setChildCode(BasePushData response, Context context){
        switch (response.getData().getCode()){
            case Constant.TIMEOUT_NO_PAY:
                Intent intent = new Intent(context, PaymentDetailsActivity.class);
                if(!TextUtils.isEmpty(response.getData().getBID())) {
                    intent.putExtra(Constant.BID, response.getData().getBID());
                }
                context.startActivity(intent);
                break;
            case Constant.ROUTE_INVALID: //超时支付界面
                Intent intent1 = new Intent(context, InvalidationActivity.class);
                Bundle bundle = new Bundle();
                if(!TextUtils.isEmpty(response.getData().getBID())) {
                    bundle.putString(Constant.BID, response.getData().getBID());
                }
                intent1.putExtras(bundle);
                context.startActivity(intent1);
                break;
            case Constant.ROUTE_CANCEL:
                Intent intent2 = new Intent(context,TravelCancelActivity.class);
                Bundle pBundle=new Bundle();
                if(!TextUtils.isEmpty(response.getData().getBID())) {
                    pBundle.putString(Constant.BID, response.getData().getBID());
                }
                pBundle.putString(Constant.STATUS,Constant.CLEAR_PORT);
                intent2.putExtras(pBundle);
                context.startActivity(intent2);
                break;
            case Constant.WAIT_DRIVER:
            case Constant.RECEIVE_ONE_PASSENGER:
            case Constant.RECEIVE_MINE:
            case Constant.RECEIVE_OTHER_PASSENGER:
            case Constant.ALREADY_ABOARD:
            case Constant.ALREADY_ARRIVE:
            case Constant.AUTO_AFFIRM:
                Intent intent3 = new Intent(context, TravelDetailsActivity.class);
                Bundle bundle1 = new Bundle();
                if(!TextUtils.isEmpty(response.getData().getBID())) {
                    bundle1.putString(Constant.BID,response.getData().getBID());
                }
                intent3.putExtras(bundle1);
                context.startActivity(intent3);
            break;
        }
    }

    /**
     * int TIMEOUT_NO_PAY_ON = 220; //超时未支付 接机
     int REMIND_BY_BUS = 214; // 提醒坐车(飞机到达后(飞机到达时间))
     int WAIT_ABOARD = 216;   //等待上车
     int AFFIRM_DEPART = 209; //确认发车
     int ALREADY_ARRIVE_ON = 206; // 确认已到达 接机
     int AUTO_AFFIRM_ON = 217 ;  //自动确认 接机
     int ROUTE_CANCEL_ON = 218 ; //行程取消 接机
     int ORDER_SUCCESS_ON = 221; //付款成功(预约成功)接机
     int ROUTE_INVALID_ON = 222; //行程已失效 接机
     * @param response
     * @param context
     */

    public void setChildCodeOn(BasePushData response, Context context){
        switch (response.getData().getCode()){
            case Constant.TIMEOUT_NO_PAY_ON:
                Intent intent = new Intent(context, PaymentDetailsActivity.class);
                if(!TextUtils.isEmpty(response.getData().getBID())) {
                    intent.putExtra(Constant.BID, response.getData().getBID());
                }
                context.startActivity(intent);
                break;
            case Constant.ROUTE_INVALID_ON: //超时支付界面
                Intent intent1 = new Intent(context, InvalidationActivity.class);
                Bundle bundle = new Bundle();
                if(!TextUtils.isEmpty(response.getData().getBID())) {
                    bundle.putString(Constant.BID, response.getData().getBID());
                }
                intent1.putExtras(bundle);
                context.startActivity(intent1);
                break;
            case Constant.ROUTE_CANCEL_ON:
                Intent intent2 = new Intent(context,TravelCancelActivity.class);
                Bundle pBundle=new Bundle();
                if(!TextUtils.isEmpty(response.getData().getBID())) {
                    pBundle.putString(Constant.BID, response.getData().getBID());
                }
                pBundle.putString(Constant.STATUS,Constant.CLEAR_PORT);
                intent2.putExtras(pBundle);
                context.startActivity(intent2);
                break;
            case Constant.REMIND_BY_BUS:
            case Constant.WAIT_ABOARD:
            case Constant.AFFIRM_DEPART:
            case Constant.ALREADY_ARRIVE_ON:
            case Constant.AUTO_AFFIRM_ON:
                Intent intent3 = new Intent(context, TravelDetailsOnActivity.class);
                Bundle bundle1 = new Bundle();
                if(!TextUtils.isEmpty(response.getData().getBID())) {
                    bundle1.putString(Constant.BID,response.getData().getBID());
                }
                intent3.putExtras(bundle1);
                context.startActivity(intent3);
                break;
        }
    }

    @Override
    public void onReceiveClientId(Context context, String clientid) {
        Log.e(TAG, "onReceiveClientId -> " + "clientid = " + clientid);
    }

    @Override
    public void onReceiveOnlineState(Context context, boolean online) {

        Log.e(TAG, "onReceiveClientId -> " + "online = " + online);
    }

    @Override
    public void onReceiveCommandResult(Context context, GTCmdMessage cmdMessage) {
    }
}
