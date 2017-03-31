package com.ironaviation.traveller.mvp.ui.widget;

import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.ironaviation.traveller.R;
import com.ironaviation.traveller.mvp.constant.Constant;
import com.jess.arms.utils.UiUtils;
import com.zhy.autolayout.AutoLinearLayout;
import com.zhy.autolayout.AutoRelativeLayout;

import java.util.ArrayList;
import java.util.List;

/**
 * 项目名称：Transfer
 * 类描述：
 * 创建人：flq
 * 创建时间：2017/3/29 19:28
 * 修改人：
 * 修改时间：2017/3/29 19:28
 * 修改备注：
 */

public class TravelPopupwindow extends PopupWindow {

    private EditText travelNum;
    private AutoRelativeLayout rlTravelNum,rlAirportFlyTime,rlCity;
    private TextView twFlyTime,twCity,twCancel,twAffirm,twFlyInfo;
    private RecyclerView rwCity;
    private AutoLinearLayout llDate,llPort;
    private ImageView iwTime;
    private int status = Constant.AIRPORT_FLY_NUM;
    public TravelPopupwindow(final Context context){
        View v = LayoutInflater.from(context).inflate(R.layout.popupwindow_travel, null, false);
        /*travelNum = (EditText) v.findViewById(R.id.edt_travel_num);
        rlTravelNum = (AutoRelativeLayout) v.findViewById(R.id.rl_travel_num);
        rlAirportFlyTime = (AutoRelativeLayout) v.findViewById(R.id.rl_airport_fly_time);
        twFlyTime = (TextView) v.findViewById(R.id.tw_fly_time);
        rlCity = (AutoRelativeLayout) v.findViewById(R.id.rl_city);
        twCity = (TextView) v.findViewById(R.id.tw_city);
        rwCity = (RecyclerView) v.findViewById(R.id.rw_city);
        *//*llDate = (AutoLinearLayout) v.findViewById(R.id.ll_date);
        twCancel = (TextView) v.findViewById(R.id.tw_cancel);
        twAffirm = (TextView) v.findViewById(R.id.tw_affirm);
        twFlyInfo = (TextView) v.findViewById(R.id.tw_fly_info);*//*
        iwTime = (ImageView) v.findViewById(R.id.iw_time);
        llPort = (AutoLinearLayout) v.findViewById(R.id.ll_port);


        rlAirportFlyTime.setVisibility(View.GONE);
        rlCity.setVisibility(View.GONE);
        rwCity.setVisibility(View.GONE);
        llDate.setVisibility(View.GONE);

        travelNum.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                if(travelNum.getText().toString().trim().length() > 2){
                    rlAirportFlyTime.setVisibility(View.VISIBLE);
                    twFlyTime.setText(editable.toString());
                }else{
                    rlAirportFlyTime.setVisibility(View.GONE);
                }
            }
        });
        travelNum.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView textView, int actionId, KeyEvent keyEvent) {
                if (actionId == EditorInfo.IME_ACTION_DONE){
                    if(travelNum.getText().toString().trim().length() <= 2 ){
                        return false;
                    }else{
                        UiUtils.makeText("done");
                        llDate.setVisibility(View.VISIBLE);
                        return true;
                    }
                }
                return false;
            }
        });

        rlAirportFlyTime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(status ==  Constant.AIRPORT_FLY_NUM) {
                    iwTime.setVisibility(View.VISIBLE);
                    twFlyTime.setText(context.getResources().getString(R.string.airport_fly));
                    status = Constant.AIRPORT_FLY_TIME;
                    llDate.setVisibility(View.VISIBLE);
                }else if(status == Constant.AIRPORT_FLY_TIME){
                    if(llDate.getVisibility() == View.VISIBLE){
                        llDate.setVisibility(View.GONE);
                    }else{
                        llDate.setVisibility(View.VISIBLE);
                    }
                }
            }
        });

        *//*llPort.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dismiss();
            }
        });*/

        setContentView(v);
        setHeight(ViewGroup.LayoutParams.MATCH_PARENT);
        setWidth(ViewGroup.LayoutParams.MATCH_PARENT);
        setFocusable(true);
        setOutsideTouchable(true);
        setBackgroundDrawable(new ColorDrawable(000000));

    }
    public void showPopupWindow(View view) {
        if (!isShowing()) {
//            showAsDropDown(view);
            showAtLocation(view, Gravity.CENTER, 0, 0);
        } else {
            dismiss();
        }
    }

    public List<String> getData(){
        List<String> list = new ArrayList<>();
        for(int i= 0; i< 10; i++){
            list.add(i+1+"");
        }
        return list;
    }
}
