package com.ironaviation.traveller.mvp.ui.airportoff;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.ironaviation.traveller.R;

/**
 * 项目名称：Transfer
 * 类描述：
 * 创建人：flq
 * 创建时间：2017/4/3 23:46
 * 修改人：
 * 修改时间：2017/4/3 23:46
 * 修改备注：
 */

public class TravelFloatAdapter extends RecyclerView.Adapter<TravelFloatHolder> {

    public void setList(){

    }
    @Override
    public TravelFloatHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_city,parent,false);
        return new TravelFloatHolder(view);
    }

    @Override
    public void onBindViewHolder(TravelFloatHolder holder, int position) {
//        holder.ll_city.setOnClickListener();
        holder.twTime.setText("");
        holder.twDate.setText("");
        holder.twFromToCity.setText("");
    }

    @Override
    public int getItemCount() {
        return 0;
    }
}
