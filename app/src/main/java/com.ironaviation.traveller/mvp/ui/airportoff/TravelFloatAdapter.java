package com.ironaviation.traveller.mvp.ui.airportoff;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.ironaviation.traveller.R;
import com.ironaviation.traveller.app.EventBusTags;
import com.ironaviation.traveller.mvp.constant.Constant;
import com.ironaviation.traveller.mvp.model.entity.response.FlightDetails;

import org.simple.eventbus.EventBus;

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
    private Context mContext;
    private int type;
    public void setList(List<FlightDetails> flightDetailsList){
        this.flightDetailsList = flightDetailsList;
        notifyDataSetChanged();
    }

    public TravelFloatAdapter(Context mContext,int type){
        this.mContext = mContext;
        this.type = type;
    }
    @Override
    public TravelFloatHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_city,parent,false);
        return new TravelFloatHolder(view);
    }

    @Override
    public void onBindViewHolder(TravelFloatHolder holder, final int position) {
//        holder.ll_city.setOnClickListener();

        holder.twTime.setText(getDate(flightDetailsList.get(position).getTakeOffTime())+"-"
        +getDate(flightDetailsList.get(position).getArriveTime()));
        holder.twFromToCity.setText(getCity(flightDetailsList.get(position).getTakeOff())+" 至 "
        +getCity(flightDetailsList.get(position).getArrive()));
        holder.ll_city.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String city = null;
                boolean flag = false;

                if(type == Constant.TYPE_AIRPORT_OFF) {
                    city = getCity(flightDetailsList.get(position).getTakeOff());
                }else if(type == Constant.TYPE_AIRPORT_ON){
                    city = getCity(flightDetailsList.get(position).getArrive());
                    }
                if(city!= null) {
                    flag = city.contains("成都");
                    }
                if(type == Constant.TYPE_AIRPORT_OFF){
                    if((flightDetailsList.get(position).getTakeOffTime())-System.currentTimeMillis()-4*60*60*1000 < 0){
                        Toast.makeText(mContext, mContext.getResources().getString(R.string.fly_four_time), Toast.LENGTH_SHORT).show();
                    }else{
                        if(flag){
                            EventBus.getDefault().post(flightDetailsList.get(position), EventBusTags.FLIGHT_INFO);
                        }else{
                            Toast.makeText(mContext, mContext.getResources().getString(R.string.airport_drop_off_no_open), Toast.LENGTH_SHORT).show();
                        }
                    }
                }else if(type == Constant.TYPE_AIRPORT_ON){
                    if(flag){
                        EventBus.getDefault().post(flightDetailsList.get(position), EventBusTags.FLIGHT_INFO_ON);
                    }else{
                        Toast.makeText(mContext, mContext.getResources().getString(R.string.airport_pickup_no_open), Toast.LENGTH_SHORT).show();
                    }
                }
               /* if (flag) {
                    if (type == Constant.TYPE_AIRPORT_OFF) {
                        EventBus.getDefault().post(flightDetailsList.get(position), EventBusTags.FLIGHT_INFO);
                    } else if (type == Constant.TYPE_AIRPORT_ON) {
                        EventBus.getDefault().post(flightDetailsList.get(position), EventBusTags.FLIGHT_INFO_ON);
                    }
                } else {
                    if (type == Constant.TYPE_AIRPORT_OFF) {
                        Toast.makeText(mContext, mContext.getResources().getString(R.string.airport_drop_off_no_open), Toast.LENGTH_SHORT).show();
                    } else if (type == Constant.TYPE_AIRPORT_ON) {
                        Toast.makeText(mContext, mContext.getResources().getString(R.string.airport_pickup_no_open), Toast.LENGTH_SHORT).show();
                    }
                }*/
            }
        });
    }

    public String getDate(long time){
        Date date = new Date(time);
        SimpleDateFormat formatter = new SimpleDateFormat("HH:mm");
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
