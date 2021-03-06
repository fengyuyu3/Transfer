package com.ironaviation.traveller.mvp.ui.widget;

import android.content.Context;
import android.graphics.drawable.BitmapDrawable;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.PopupWindow;

import com.ironaviation.traveller.R;
import com.ironaviation.traveller.app.EventBusTags;
import com.ironaviation.traveller.app.utils.CommonUtil;
import com.ironaviation.traveller.app.utils.Utils;
import com.ironaviation.traveller.event.TravelCancelEvent;
import com.ironaviation.traveller.mvp.constant.Constant;
import com.zhy.autolayout.AutoLinearLayout;

import org.simple.eventbus.EventBus;

import butterknife.ButterKnife;

import static com.jess.arms.base.AppManager.APPMANAGER_MESSAGE;

/**
 * 项目名称：Transfer
 * 类描述：
 * 创建人：flq
 * 创建时间：2017/3/31 10:53
 * 修改人：
 * 修改时间：2017/3/31 10:53
 * 修改备注：
 */

public class MoreActionPopupWindow extends PopupWindow implements View.OnClickListener {
    AutoLinearLayout llCustomer;
    AutoLinearLayout llCancel;
    AutoLinearLayout llPop;
    private Context mContext;
    private String tags;
    private String bid;


    public MoreActionPopupWindow(Context context, String tags, String bid) {
        this.mContext = context;
        View v = LayoutInflater.from(context).inflate(R.layout.pop_action_more, null, false);
        llCustomer = (AutoLinearLayout) v.findViewById(R.id.ll_customer);
        llCancel = (AutoLinearLayout) v.findViewById(R.id.ll_cancel);
        llPop = (AutoLinearLayout) v.findViewById(R.id.ll_pop);
        llCustomer.setOnClickListener(this);
        llCancel.setOnClickListener(this);
        llPop.setOnClickListener(this);
        this.tags = tags;
        this.bid = bid;
        setContentView(v);
        setHeight(CommonUtil.getScreenHeight(context));
        setWidth(CommonUtil.getScreenWidth(context));
        setFocusable(true);
        setOutsideTouchable(true);
        setBackgroundDrawable(new BitmapDrawable());
        setAnimationStyle(R.style.popwin_anim_select_style);
    }

    public void showPopupWindow(View view) {
        if (!isShowing()) {
            showAtLocation(view, Gravity.CENTER,0,0);
//            showAsDropDown(view);
        } else {
            dismiss();
        }
    }

    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.ll_cancel:
                EventBus.getDefault().post(new TravelCancelEvent(Constant.TRAVEL_CANCEL, bid), tags);
                break;
            case R.id.ll_customer:
                EventBus.getDefault().post(new TravelCancelEvent(Constant.TRAVEL_CUSTOMER, bid), tags);
                break;
            case R.id.ll_pop:
                break;
        }
        dismiss();
    }
}
