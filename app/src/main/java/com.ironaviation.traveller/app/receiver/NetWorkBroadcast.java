package com.ironaviation.traveller.app.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

import com.ironaviation.traveller.app.EventBusTags;

import org.simple.eventbus.EventBus;

/**
 * Created by Administrator on 2017/5/18.
 */

public class NetWorkBroadcast extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {
            // TODO Auto-generated method stub
            //Toast.makeText(context, intent.getAction(), 1).show();
            ConnectivityManager manager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
            NetworkInfo mobileInfo = manager.getNetworkInfo(ConnectivityManager.TYPE_MOBILE);
            NetworkInfo wifiInfo = manager.getNetworkInfo(ConnectivityManager.TYPE_WIFI);
            NetworkInfo activeInfo = manager.getActiveNetworkInfo();
            if(activeInfo ==null){
                EventBus.getDefault().post(true, EventBusTags.NO_NETWORK);
            }else{
                EventBus.getDefault().post(false, EventBusTags.NO_NETWORK);
            }
        }  //如果无网络连接activeInfo为null


}
