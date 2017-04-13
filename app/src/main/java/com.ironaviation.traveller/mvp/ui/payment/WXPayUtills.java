package com.ironaviation.traveller.mvp.ui.payment;

import android.content.Context;

import com.tencent.mm.opensdk.modelpay.PayReq;
import com.tencent.mm.opensdk.openapi.IWXAPI;
import com.tencent.mm.opensdk.openapi.WXAPIFactory;

/**
 * Created by Administrator on 2017/4/12 0012.
 */

public class WXPayUtills {

    private IWXAPI api;
    public WXPayUtills(Context context){
        api = WXAPIFactory.createWXAPI(context, "wxb4ba3c02aa476ea1");
    }

    public void wxPay(){
        PayReq req = new PayReq();
        //req.appId = "wxf8b4f85f3a794e77";  // 测试用appId
        req.appId			= "appid";
        req.partnerId		= "partnerid";
        req.prepayId		= "prepayid";
        req.nonceStr		= "noncestr";
        req.timeStamp		= "timestamp";
        req.packageValue	= "package";
        req.sign			= "sign";
        req.extData			= "app data"; // optional
//        Toast.makeText(PayActivity.this, "正常调起支付", Toast.LENGTH_SHORT).show();
        // 在支付之前，如果应用没有注册到微信，应该先调用IWXMsg.registerApp将应用注册到微信
        api.sendReq(req);
    }
}
