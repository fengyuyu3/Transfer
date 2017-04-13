package com.ironaviation.traveller.mvp.ui.my.travel;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.ironaviation.traveller.R;
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
        helper.setText(R.id.tv_status,item.getStatus());
        helper.setText(R.id.tv_time,item.getPickupTime());
        helper.setText(R.id.tv_airport,item.getPickupAddress());
        helper.setText(R.id.tv_hotel,item.getDestAddress());
    }
}
