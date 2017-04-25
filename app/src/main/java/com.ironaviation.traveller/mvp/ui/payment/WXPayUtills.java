package com.ironaviation.traveller.mvp.ui.payment;

import android.content.Context;
import android.content.DialogInterface;
import android.support.v7.app.AlertDialog;

import com.tencent.mm.sdk.modelpay.PayReq;
import com.tencent.mm.sdk.openapi.IWXAPI;
import com.tencent.mm.sdk.openapi.WXAPIFactory;


/**
 * Created by Administrator on 2017/4/12 0012.
 */

public class WXPayUtills {

    private IWXAPI api;
    private Context mContext;
    public WXPayUtills(Context context){
        this.mContext = context;
        initWX();
    }

    public void initWX(){
        api = WXAPIFactory.createWXAPI(mContext, "wxb277d9d5c3cf0829");
        api.registerApp("wxb277d9d5c3cf0829");
    }
    /**
     *
     *
     * {"AppId":"wxb277d9d5c3cf0829","PartnerId":"1461817702",
     * "PrepayId":"wx201704201626123f9c58cf7f0811665248",
     * "NonceStr":"lWRRbkVCTNmvXGE","TimeStamp":"1492676769",
     * "Package":"Sign=WXPay","Sign":"6CB7B29252F446444E6FCF130545D96F"}
     *
     *
     */
    public void pay(){
        if(api == null){
            initWX();
        }
        if(!api.isWXAppInstalled()){
            createDialog();
        }else{
            wxPay();
        }

    }

    /**
     * {"AppId":"wxb277d9d5c3cf0829","PartnerId":"1461817702",
     * "PrepayId":"wx2017042108542077deb2b7740176869356",
     * "NonceStr":"exgRUDizxpzDVTt","TimeStamp":"1492736060",
     * "Package":"Sign=WXPay","Sign":"76C0E01C853E51AC65273E062164BB6C"}
     *
     * {"AppId":"wxb277d9d5c3cf0829","PartnerId":"1461817702"
     * ,"PrepayId":"wx20170420154915c24ac2acaa0178722295"
     * ,"NonceStr":"KKKQNLLQoQCJwoI","TimeStamp":"1492674552"
     * ,"Package":"Sign=WXPay","Sign":"BA0A8F857902D813BCD057207E9BC5BC"}
     */

    public void wxPay(){
        PayReq req = new PayReq();
        //req.appId = "wxf8b4f85f3a794e77";  // 测试用appId
        req.appId			= "wxb277d9d5c3cf0829";
        req.partnerId		= "1461817702";
        req.prepayId		= "wx20170420154915c24ac2acaa0178722295";
        req.nonceStr		= "KKKQNLLQoQCJwoI";
        req.timeStamp		= "1492674552";
        req.packageValue	= "Sign=WXPay";
        req.sign			= "BA0A8F857902D813BCD057207E9BC5BC";

        // Toast.makeText(PayActivity.this, "正常调起支付", Toast.LENGTH_SHORT).show();
        // 在支付之前，如果应用没有注册到微信，应该先调用IWXMsg.registerApp将应用注册到微信
        api.sendReq(req);
    }

    public void createDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(mContext);
        builder.setMessage("请安装微信");
        builder.setPositiveButton("确定",
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });
        builder.create().show();
    }
}
