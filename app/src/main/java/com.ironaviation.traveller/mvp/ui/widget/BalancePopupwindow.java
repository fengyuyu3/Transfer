package com.ironaviation.traveller.mvp.ui.widget;

import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.BitmapDrawable;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.ironaviation.traveller.R;
import com.ironaviation.traveller.mvp.constant.Constant;
import com.ironaviation.traveller.mvp.ui.my.travel.BalancePayActivity;
import com.ironaviation.traveller.mvp.ui.payment.ChargeMoneyActivity;

/**
 * Created by flq on 2017/6/17.
 */

public class BalancePopupwindow extends PopupWindow implements View.OnClickListener{

    private Context context;
    private TextView mTwAtOneceMoney,mTwPriceInfo;

    public BalancePopupwindow(Context context) {
        this.context = context;
        init();
    }

    private void init() {
        View view = LayoutInflater.from(context).inflate(R.layout.popupwindow_balance, null);
        setContentView(view);
        setWidth(ViewGroup.LayoutParams.MATCH_PARENT);
        setHeight(ViewGroup.LayoutParams.MATCH_PARENT);
        setOutsideTouchable(false);
        setTouchable(true);
        setFocusable(true);
        mTwAtOneceMoney = (TextView) view.findViewById(R.id.tw_at_onece_money);
        mTwPriceInfo = (TextView) view.findViewById(R.id.tw_price_info);
        mTwAtOneceMoney.setOnClickListener(this);
        setBackgroundDrawable(new BitmapDrawable());
        setAnimationStyle(R.style.popwin_anim_style);
        mTwAtOneceMoney.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.tw_at_onece_money:
                Intent intent = new Intent(context, ChargeMoneyActivity.class);
                intent.putExtra(Constant.PAY_MONEY,12);
                context.startActivity(intent);
                break;
        }
    }

    public void show(View parentView) {
        showAtLocation(parentView, Gravity.CENTER, 0, 0);
    }
}
