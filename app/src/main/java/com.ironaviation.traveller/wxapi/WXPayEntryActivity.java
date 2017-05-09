package com.ironaviation.traveller.wxapi;


import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import com.ironaviation.traveller.R;
import com.ironaviation.traveller.app.EventBusTags;
import com.ironaviation.traveller.mvp.ui.my.travel.TravelActivity;
import com.tencent.mm.sdk.modelbase.BaseReq;
import com.tencent.mm.sdk.modelbase.BaseResp;
import com.tencent.mm.sdk.openapi.IWXAPI;
import com.tencent.mm.sdk.openapi.IWXAPIEventHandler;
import com.tencent.mm.sdk.openapi.WXAPIFactory;

import org.simple.eventbus.EventBus;

public class WXPayEntryActivity extends Activity implements IWXAPIEventHandler {

	private IWXAPI api;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
//		setContentView(R.layout.entry);

		// 通过WXAPIFactory工厂，获取IWXAPI的实例wxb277d9d5c3cf0829
		api = WXAPIFactory.createWXAPI(this, "wxb277d9d5c3cf0829");
		api.handleIntent(getIntent(), this);
	}

	@Override
	protected void onNewIntent(Intent intent) {
		super.onNewIntent(intent);

		setIntent(intent);
		api.handleIntent(intent, this);
	}

	// 微信发送请求到第三方应用时，会回调到该方法
	@Override
	public void onReq(BaseReq req) {
	}

	// 第三方应用发送到微信的请求处理后的响应结果，会回调到该方法
	@Override
	public void onResp(BaseResp resp) {
		int result = 0;

//		Toast.makeText(this, "baseresp.getType = " + resp.getType(), Toast.LENGTH_SHORT).show();

		switch (resp.errCode) {
			case BaseResp.ErrCode.ERR_OK:
				EventBus.getDefault().post(true,EventBusTags.WX_PAY);
				finish();
				break;
			case BaseResp.ErrCode.ERR_USER_CANCEL:
				EventBus.getDefault().post(true,EventBusTags.WX_FILED);
				finish();
				break;
			case BaseResp.ErrCode.ERR_AUTH_DENIED:
			case BaseResp.ErrCode.ERR_UNSUPPORT:
				EventBus.getDefault().post(false,EventBusTags.WX_FILED);
				finish();
				break;
		}
	}

}