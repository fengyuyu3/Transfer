package com.ironaviation.traveller.mvp.ui.my.adapter;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.ironaviation.traveller.R;
import com.ironaviation.traveller.app.utils.TimerUtils;
import com.ironaviation.traveller.common.WEApplication;
import com.ironaviation.traveller.mvp.constant.Constant;
import com.ironaviation.traveller.mvp.model.entity.response.RouteItemResponse;
import com.ironaviation.traveller.mvp.model.entity.response.TravelResponse;

/**
 * 项目名称：Transfer
 * 类描述：
 * 创建人：flq
 * 创建时间：2017/3/26 18:06
 * 修改人：
 * 修改时间：2017/3/26 18:06
 * 修改备注：
 */

public class TravelAdapter extends BaseQuickAdapter<RouteItemResponse,BaseViewHolder> {

    public TravelAdapter(int layoutResId) {
        super(layoutResId);
    }

    @Override
    protected void convert(BaseViewHolder helper, RouteItemResponse item) {
        String status = setStatus(item.getStatus(),item);
        if(status != null){
            helper.setText(R.id.tv_status,status);
        }else{
            helper.setText(R.id.tv_status,"default");
        }
        helper.setText(R.id.tv_time, TimerUtils.getDayHour(TimerUtils.getTime(item.getPickupTime())));
        helper.setText(R.id.tv_airport,item.getPickupAddress());
        helper.setText(R.id.tv_hotel,item.getDestAddress());
    }

    public String setStatus(String status,RouteItemResponse item){
        String st = null;
        if(Constant.REGISTERED.equalsIgnoreCase(status)){
            st = WEApplication.getContext().getResources().getString(R.string.travel_status_registered);
        }else if(Constant.INHAND.equalsIgnoreCase(status)){
            st = WEApplication.getContext().getResources().getString(R.string.travel_status_inhand);
        }else if(Constant.ARRIVED.equalsIgnoreCase(status)){
            st = WEApplication.getContext().getResources().getString(R.string.travel_status_arrived);
        }else if(Constant.CANCEL.equalsIgnoreCase(status)){
            st = WEApplication.getContext().getResources().getString(R.string.travel_status_cancel);
        }else if(Constant.BOOKSUCCESS.equalsIgnoreCase(status)){
            if(item.getTripType().equals(Constant.CLEAR_PORT)){ //送机
                    st = WEApplication.getContext().getResources().getString(R.string.travel_status_wait_train);
            }else if(item.getTripType().equals(Constant.ENTER_PORT)){ //接机
                if(item.getChildStatus().equals(Constant.PICKUP)){
                    st = WEApplication.getContext().getResources().getString(R.string.travel_status_wait_pickup);
                }else if(item.getChildStatus().equals(Constant.ABORAD)) {
                    st = WEApplication.getContext().getResources().getString(R.string.travel_status_wait_come);
                }else{
                    st = WEApplication.getContext().getResources().getString(R.string.travel_status_booksuccess);
                }
            }else{
               st = WEApplication.getContext().getResources().getString(R.string.travel_status_booksuccess);
            }

        }else if(Constant.COMPLETED.equalsIgnoreCase(status)){
            st = WEApplication.getContext().getResources().getString(R.string.travel_status_completed);
        }else if(Constant.NOTPAID.equalsIgnoreCase(status)){
            st = WEApplication.getContext().getResources().getString(R.string.travel_status_NotPaid);
        }else if(Constant.INVALIDATION .equalsIgnoreCase(status)){
            st = WEApplication.getContext().getResources().getString(R.string.travel_status_invalidation);
        }else if(Constant.WAIT_APPRAISE .equalsIgnoreCase(status)){
            st = WEApplication.getContext().getResources().getString(R.string.travel_status_wait);
        }
        return st;
    }
}
