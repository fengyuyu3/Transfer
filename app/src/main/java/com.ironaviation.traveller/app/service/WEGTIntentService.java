package com.ironaviation.traveller.app.service;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.widget.Toast;

import com.google.gson.Gson;
import com.igexin.sdk.GTIntentService;
import com.igexin.sdk.PushManager;
import com.igexin.sdk.message.GTCmdMessage;
import com.igexin.sdk.message.GTTransmitMessage;
import com.ironaviation.traveller.R;
import com.ironaviation.traveller.app.EventBusTags;
import com.ironaviation.traveller.app.utils.Cache;
import com.ironaviation.traveller.app.utils.PushClientUtil;
import com.ironaviation.traveller.common.WEApplication;
import com.ironaviation.traveller.mvp.constant.Constant;
import com.ironaviation.traveller.mvp.model.entity.BaseData;
import com.ironaviation.traveller.mvp.model.entity.BasePushData;
import com.ironaviation.traveller.mvp.model.entity.response.PushResponse;
import com.ironaviation.traveller.mvp.ui.login.LoginActivity;
import com.ironaviation.traveller.mvp.ui.main.MainActivity;
import com.ironaviation.traveller.mvp.ui.my.travel.PaymentDetailsActivity;
import com.ironaviation.traveller.mvp.ui.my.travel.TravelActivity;
import com.ironaviation.traveller.mvp.ui.my.travel.TravelCancelActivity;
import com.ironaviation.traveller.mvp.ui.my.travel.TravelDetailsActivity;
import com.ironaviation.traveller.mvp.ui.my.travel.TravelDetailsOnActivity;
import com.ironaviation.traveller.mvp.ui.payment.InvalidationActivity;
import com.jess.arms.utils.DataHelper;
import com.jess.arms.utils.UiUtils;

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
                if(response.getType() == Constant.OTHER_LOGIN){
                    otherLogin(context);
                }else {
                    if (!TextUtils.isEmpty(response.getData().getTripType()) && !response.getData().getStatus().equals(Constant.ORDER_SUCCESS)) {
                        setStatus(response, context);
                        messageLogic(response, context);
                        EventBus.getDefault().post(response.getData().getBID(), EventBusTags.REFRESH);
                    }
                }
            }catch (Exception e){
                e.printStackTrace();
            }
            /*TransparentMessageEntity transparentMessageEntity =
                    new Gson().fromJson(data, TransparentMessageEntity.class);*/
        }
    }

    public void otherLogin(Context context){
        DataHelper.removeSF(context,Constant.LOGIN);
        Intent intent = new Intent(context, LoginActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        UiUtils.SnackbarText(getString(R.string.login_other));
        context.startActivity(intent);
    }

    public void setStatus(BasePushData response, Context context){
        switch (response.getData().getTripType()){
            case Constant.CLEAR_PORT: //送机
                setChildCode(response,context);
                break;
            case Constant.ENTER_PORT:
                setChildCodeOn(response,context);
                break;
        }
    }


    // int ORDER_SUCCESS = 211; //付款成功(预约成功) 不知道怎么处理
    public void setChildCode(BasePushData response, Context context){
        switch (response.getData().getCode()){
            case Constant.TIMEOUT_NO_PAY:
                EventBus.getDefault().post(response,EventBusTags.TIMEOUT_NO_PAY);
                break;
            case Constant.ROUTE_INVALID: //超时支付界面
                EventBus.getDefault().post(response,EventBusTags.ROUTE_INVALID);
                break;
            case Constant.ROUTE_CANCEL: //行程取消
                EventBus.getDefault().post(response,EventBusTags.ROUTE_CANCEL);
                break;
//            case Constant.ORDER_SUCCESS:
            case Constant.WAIT_DRIVER:
            case Constant.RECEIVE_ONE_PASSENGER:
            case Constant.RECEIVE_MINE:
            case Constant.RECEIVE_OTHER_PASSENGER:
            case Constant.ALREADY_ABOARD:
            case Constant.ALREADY_ARRIVE:
            case Constant.AUTO_AFFIRM:
                EventBus.getDefault().post(response,EventBusTags.TRAVEL_DETAIL);
                break;
        }
    }
    public void setChildCodeOn(BasePushData response, Context context){
        switch (response.getData().getCode()){
            case Constant.TIMEOUT_NO_PAY_ON:
                EventBus.getDefault().post(response,EventBusTags.TIMEOUT_NO_PAY);
                break;
            case Constant.ROUTE_INVALID_ON: //超时支付界面
                EventBus.getDefault().post(response,EventBusTags.ROUTE_INVALID);
                break;
            case Constant.ROUTE_CANCEL_ON:
                EventBus.getDefault().post(response,EventBusTags.ROUTE_CANCEL);
                break;
            case Constant.REMIND_BY_BUS:
            case Constant.WAIT_ABOARD:
            case Constant.AFFIRM_DEPART:
            case Constant.ALREADY_ARRIVE_ON:
            case Constant.AUTO_AFFIRM_ON:
                EventBus.getDefault().post(response,EventBusTags.TRAVEL_DETAIL_ON);
                break;
        }
    }

    public Intent setChildCodeNotification(BasePushData response, Context context,Intent intent){
        switch (response.getData().getCode()){
            case Constant.TIMEOUT_NO_PAY:
                intent.setClass(context, PaymentDetailsActivity.class);
                if(!TextUtils.isEmpty(response.getData().getBID())) {
                    intent.putExtra(Constant.BID, response.getData().getBID());
                }
                break;
            case Constant.ROUTE_INVALID: //超时支付界面
                intent.setClass(context, InvalidationActivity.class);
                Bundle bundle = new Bundle();
                if(!TextUtils.isEmpty(response.getData().getBID())) {
                    bundle.putString(Constant.BID, response.getData().getBID());
                }
                intent.putExtras(bundle);
                break;
            case Constant.ROUTE_CANCEL: //行程取消
                intent.setClass(context,TravelCancelActivity.class);
                Bundle pBundle=new Bundle();
                if(!TextUtils.isEmpty(response.getData().getBID())) {
                    pBundle.putString(Constant.BID, response.getData().getBID());
                }
                pBundle.putString(Constant.STATUS,Constant.CLEAR_PORT);
                intent.putExtras(pBundle);
                break;
//            case Constant.ORDER_SUCCESS:
            case Constant.WAIT_DRIVER:
            case Constant.RECEIVE_ONE_PASSENGER:
            case Constant.RECEIVE_MINE:
            case Constant.RECEIVE_OTHER_PASSENGER:
            case Constant.ALREADY_ABOARD:
            case Constant.ALREADY_ARRIVE:
            case Constant.AUTO_AFFIRM:
                intent.setClass(context, TravelDetailsActivity.class);
                Bundle bundle1 = new Bundle();
                if(!TextUtils.isEmpty(response.getData().getBID())) {
                    bundle1.putString(Constant.BID,response.getData().getBID());
                }
                intent.putExtras(bundle1);
                break;
        }
        return intent;
    }

    public Intent setChildCodeOnNotification(BasePushData response, Context context,Intent intent){
        switch (response.getData().getCode()){
            case Constant.TIMEOUT_NO_PAY_ON:
                intent.setClass(context,PaymentDetailsActivity.class);
                if(!TextUtils.isEmpty(response.getData().getBID())) {
                    intent.putExtra(Constant.BID, response.getData().getBID());
                }
                break;
            case Constant.ROUTE_INVALID_ON: //超时支付界面
                intent.setClass(context, InvalidationActivity.class);
                Bundle bundle = new Bundle();
                if(!TextUtils.isEmpty(response.getData().getBID())) {
                    bundle.putString(Constant.BID, response.getData().getBID());
                }
                intent.putExtras(bundle);
                break;
            case Constant.ROUTE_CANCEL_ON:
                intent.setClass(context,TravelCancelActivity.class);
                Bundle pBundle=new Bundle();
                if(!TextUtils.isEmpty(response.getData().getBID())) {
                    pBundle.putString(Constant.BID, response.getData().getBID());
                }
                pBundle.putString(Constant.STATUS,Constant.CLEAR_PORT);
                intent.putExtras(pBundle);
                break;
            case Constant.REMIND_BY_BUS:
            case Constant.WAIT_ABOARD:
            case Constant.AFFIRM_DEPART:
            case Constant.ALREADY_ARRIVE_ON:
            case Constant.AUTO_AFFIRM_ON:
                intent.setClass(context, TravelDetailsOnActivity.class);
                Bundle bundle1 = new Bundle();
                if(!TextUtils.isEmpty(response.getData().getBID())) {
                    bundle1.putString(Constant.BID,response.getData().getBID());
                }
                intent.putExtras(bundle1);
                break;
        }
       return intent;
    }

    @Override
    public void onReceiveClientId(Context context, String clientid) {
        Log.e(TAG, "onReceiveClientId -> " + "clientid = " + clientid);
    }

    @Override
    public void onReceiveOnlineState(Context context, boolean online) {
        if(!online){
            EventBus.getDefault().post(true,EventBusTags.PUSH_ONLINE);
        }

        Log.e(TAG, "onReceiveClientId -> " + "online = " + online);
    }

    @Override
    public void onReceiveCommandResult(Context context, GTCmdMessage cmdMessage) {
        Log.e(TAG, "onReceiveClientId -> " + "cmdMessage = " + cmdMessage.toString());

    }

    private void messageLogic(BasePushData response,Context context) {

        // EventBus.getDefault().post(transparentMessageEntity);
        Intent intent = new Intent();
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        if(response.getData().getTripType()!= null){
            if(response.getData().getTripType().equals(Constant.CLEAR_PORT)){
                intent = setChildCodeNotification(response,context,intent);
                setPush(context,intent,response);
            }else if(response.getData().getTripType().equals(Constant.ENTER_PORT)){
                intent = setChildCodeOnNotification(response,context,intent);
                setPush(context,intent,response);
            }
        }


    }

    public void setPush(Context context,Intent intent,BasePushData response){
        Cache.PUSH_ID++;
        PendingIntent pendingIntent = PendingIntent.getActivity(context, 0, intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK), PendingIntent.FLAG_CANCEL_CURRENT);
        Notification notify = new Notification.Builder(context)
                .setSmallIcon(R.drawable.ic_logo) // 设置状态栏中的小图片，尺寸一般建议在24×24， 这里也可以设置大图标
                .setTicker(response.getData().getStatusName() != null ? response.getData().getStatusName():"")// 设置显示的提示文字
                .setContentTitle(response.getData().getStatusName() != null ? response.getData().getStatusName():"")// 设置显示的标题
                .setContentText(response.getMessage())// 消息的详细内容
                .setContentIntent(pendingIntent) // 关联PendingIntent
                .setDefaults(Notification.DEFAULT_SOUND)
                //.setNumber(1) // 在TextView的右方显示的数字，可以在外部定义一个变量，点击累加setNumber(count),这时显示的和
                .getNotification(); // 需要注意build()是在API level16及之后增加的，在API11中可以使用getNotificatin()来代替
        notify.flags |= Notification.FLAG_AUTO_CANCEL;
        NotificationManager manager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        manager.notify(Cache.PUSH_ID, notify);
    }
}
