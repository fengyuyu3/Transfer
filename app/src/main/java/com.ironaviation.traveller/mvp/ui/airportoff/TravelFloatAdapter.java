package com.ironaviation.traveller.mvp.ui.airportoff;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.ironaviation.traveller.R;
import com.ironaviation.traveller.mvp.model.entity.response.FlightDetails;
import com.ironaviation.traveller.mvp.ui.my.travel.TravelAdapter;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

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

    private List<FlightDetails> flightDetailsList;
    public void setList(List<FlightDetails> flightDetailsList){
        this.flightDetailsList = flightDetailsList;
        notifyDataSetChanged();
    }
    @Override
    public TravelFloatHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_city,parent,false);
        return new TravelFloatHolder(view);
    }

    @Override
    public void onBindViewHolder(TravelFloatHolder holder, int position) {
//        holder.ll_city.setOnClickListener();

        holder.twTime.setText(getDate(flightDetailsList.get(position).getTakeOffTime())+":"
        +getDate(flightDetailsList.get(position).getArriveTime()));
        holder.twFromToCity.setText(getCity(flightDetailsList.get(position).getTakeOff())+":"
        +getCity(flightDetailsList.get(position).getTakeOff()));
    }

    public String getDate(long time){
        Date date = new Date(time);
        SimpleDateFormat formatter = new SimpleDateFormat("mm:ss");
        String dateString = formatter.format(date);
        return dateString;
    }

    public String getCity(String city){
        String[] cityArray = city.split(" ");
        return cityArray[0];
    }
    @Override
    public int getItemCount() {
        return flightDetailsList != null ? flightDetailsList.size() : 0;
    }
}
