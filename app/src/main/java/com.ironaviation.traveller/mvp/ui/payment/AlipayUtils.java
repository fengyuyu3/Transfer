package com.ironaviation.traveller.mvp.ui.payment;

import android.content.Intent;
import android.text.TextUtils;
import android.widget.Toast;

import com.alipay.sdk.app.PayTask;
import com.ironaviation.traveller.app.EventBusTags;
import com.ironaviation.traveller.common.WEActivity;
import com.ironaviation.traveller.mvp.ui.my.travel.TravelActivity;

import org.simple.eventbus.EventBus;

import java.util.Map;

/**
 * Created by Administrator on 2017/4/12 0012.
 */

public class AlipayUtils {

    private static String resultStatus,result1,memo;
    public static void aliPay(final WEActivity activity, final String info) {
        Runnable payRunnable = new Runnable() {
            @Override
            public void run() {
                PayTask alipay = new PayTask(activity);
                Map<String, String> result = alipay.payV2(info, true);
                for (String key : result.keySet()) {
                    if (TextUtils.equals(key, "resultStatus")) {
                        resultStatus = result.get(key);
                    } else if (TextUtils.equals(key, "result")) {
                        result1 = result.get(key);
                    } else if (TextUtils.equals(key, "memo")) {
                        memo = result.get(key);
                    }
                }
                if(TextUtils.equals(resultStatus,"9000")) {
                    Intent intent = new Intent(activity, TravelActivity.class);
                    activity.startActivity(intent);
                    EventBus.getDefault().post(true, EventBusTags.REFRESH);
                    activity.finish();
                }else{
                    Toast.makeText(activity,"支付失败",Toast.LENGTH_SHORT).show();
                    activity.finish();
                }
            }
        };
        Thread payThread = new Thread(payRunnable);
        payThread.start();
    }

    /**
     *
     9000	订单支付成功
     8000	正在处理中，支付结果未知（有可能已经支付成功），请查询商户订单列表中订单的支付状态
     4000	订单支付失败
     5000	重复请求
     6001	用户中途取消
     6002	网络连接出错
     6004	支付结果未知（有可能已经支付成功），请查询商户订单列表中订单的支付状态
     其它	其它支付错误
     *
     *
     */
}
