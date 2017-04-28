package com.ironaviation.traveller.app.service;

import android.content.Context;
import android.util.Log;

import com.google.gson.Gson;
import com.igexin.sdk.GTIntentService;
import com.igexin.sdk.PushManager;
import com.igexin.sdk.message.GTCmdMessage;
import com.igexin.sdk.message.GTTransmitMessage;
import com.ironaviation.traveller.app.EventBusTags;
import com.ironaviation.traveller.mvp.constant.Constant;
import com.ironaviation.traveller.mvp.model.entity.response.PushResponse;

import org.simple.eventbus.EventBus;

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

    public WEGTIntentService() {

    }

    @Override
    public void onReceiveServicePid(Context context, int pid) {

    }

    @Override
    public void onReceiveMessageData(Context context, GTTransmitMessage msg) {
        String taskid = msg.getTaskId();
        String messageid = msg.getMessageId();
        byte[] payload = msg.getPayload();
        boolean result = PushManager.getInstance()
                .sendFeedbackMessage(context, taskid, messageid, 90001);
        if (payload == null) {
        } else {
            String data = new String(payload);
            try{
                PushResponse response = new Gson().fromJson(data,PushResponse.class);
                if(response.getStutas().equals(Constant.ON)){
                    EventBus.getDefault().post(response,EventBusTags.AIRPORT_PUSH_ON);
                }else if(response.getStutas().equals(Constant.OFF)){
                    EventBus.getDefault().post(response,EventBusTags.AIRPORT_PUSH_OFF);
                }
            }catch (Exception e){
                e.printStackTrace();
            }
            /*TransparentMessageEntity transparentMessageEntity =
                    new Gson().fromJson(data, TransparentMessageEntity.class);*/
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
