package com.ironaviation.traveller.mvp.ui.payment;

import com.alipay.sdk.app.PayTask;
import com.ironaviation.traveller.app.EventBusTags;
import com.ironaviation.traveller.common.WEActivity;

import org.simple.eventbus.EventBus;

import java.util.Map;

/**
 * Created by Administrator on 2017/4/12 0012.
 */

public class AlipayUtils {

    public void aliPay(final WEActivity activity, final String info) {
        Runnable payRunnable = new Runnable() {

            @Override
            public void run() {
                PayTask alipay = new PayTask(activity);
                Map<String, String> result = alipay.payV2(info, true);
                EventBus.getDefault().post(result, EventBusTags.ALIPAY);
            }
        };
        Thread payThread = new Thread(payRunnable);
        payThread.start();
    }

    /**
     *
     * private Handler mHandler = new Handler() {
     public void handleMessage(Message msg) {
     Result result = new Result((String) msg.obj);
     Toast.makeText(DemoActivity.this, result.getResult(),
     Toast.LENGTH_LONG).show();
     };
     //选择一个  结果处理
     PayResult payResult = new PayResult((Map<String, String>) msg.obj);
    String resultInfo = payResult.getResult();// 同步返回需要验证的信息
    String resultStatus = payResult.getResultStatus();
    // 判断resultStatus 为9000则代表支付成功
    if (TextUtils.equals(resultStatus, "9000")) {
        // 该笔订单是否真实支付成功，需要依赖服务端的异步通知。
        Toast.makeText(PayDemoActivity.this, "支付成功", Toast.LENGTH_SHORT).show();
    } else {
        // 该笔订单真实的支付结果，需要依赖服务端的异步通知。
        Toast.makeText(PayDemoActivity.this, "支付失败", Toast.LENGTH_SHORT).show();
    }
    break;
};
     *
     *
     */
}
