package com.ironaviation.traveller.mvp.ui.widget;

import android.content.Context;
import android.graphics.drawable.BitmapDrawable;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.PopupWindow;

import com.ironaviation.traveller.R;
import com.ironaviation.traveller.event.TravelCancelEvent;
import com.ironaviation.traveller.mvp.constant.Constant;
import com.zhy.autolayout.AutoLinearLayout;

import org.simple.eventbus.EventBus;

import butterknife.ButterKnife;

/**
 * 项目名称：Transfer
 * 类描述：
 * 创建人：flq
 * 创建时间：2017/3/31 10:53
 * 修改人：
 * 修改时间：2017/3/31 10:53
 * 修改备注：
 */

public class MoreActionPopupWindow extends PopupWindow implements View.OnClickListener{
    AutoLinearLayout llCustomer;
    AutoLinearLayout llCancel;
    private Context mContext;

    public MoreActionPopupWindow(Context context) {
        this.mContext = context;
        View v = LayoutInflater.from(context).inflate(R.layout.pop_action_more,null,false);
        llCustomer = (AutoLinearLayout) v.findViewById(R.id.ll_customer);
        llCancel = (AutoLinearLayout) v.findViewById(R.id.ll_cancel);
        llCustomer.setOnClickListener(this);
        llCancel.setOnClickListener(this);

        setContentView(v);
        setHeight(222);
        setWidth(172);
        setFocusable(true);
        setOutsideTouchable(true);
        setBackgroundDrawable(new BitmapDrawable());
    }

    public void showPopupWindow(View view) {
        if (!isShowing()) {
            showAtLocation(view, Gravity.TOP|Gravity.RIGHT,40,80);
        } else {
            dismiss();
        }
    }

    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.ll_cancel:
                EventBus.getDefault().post(new TravelCancelEvent(Constant.TRAVEL_CANCEL));
                break;
            case R.id.ll_customer:
                EventBus.getDefault().post(new TravelCancelEvent(Constant.TRAVEL_CUSTOMER));
                break;
        }
        dismiss();
    }
}
