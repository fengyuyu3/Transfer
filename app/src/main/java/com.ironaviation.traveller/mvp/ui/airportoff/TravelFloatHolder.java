package com.ironaviation.traveller.mvp.ui.airportoff;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.ironaviation.traveller.R;
import com.zhy.autolayout.AutoLinearLayout;

/**
 * 项目名称：Transfer
 * 类描述：
 * 创建人：flq
 * 创建时间：2017/4/3 23:47
 * 修改人：
 * 修改时间：2017/4/3 23:47
 * 修改备注：
 */

public class TravelFloatHolder extends RecyclerView.ViewHolder {
    public AutoLinearLayout ll_city;
    public TextView twFromToCity,twDate,twTime;
    public TravelFloatHolder(View itemView) {
        super(itemView);
        ll_city = (AutoLinearLayout) itemView.findViewById(R.id.ll_city);
        twFromToCity = (TextView) itemView.findViewById(R.id.tw_from_to_city);
        twDate = (TextView) itemView.findViewById(R.id.tw_date);
        twTime = (TextView) itemView.findViewById(R.id.tw_time);
    }
}
