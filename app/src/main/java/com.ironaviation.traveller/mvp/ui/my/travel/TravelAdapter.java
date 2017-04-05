package com.ironaviation.traveller.mvp.ui.my.travel;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.ironaviation.traveller.R;
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

public class TravelAdapter extends BaseQuickAdapter<TravelResponse,BaseViewHolder> {

    public TravelAdapter(int layoutResId) {
        super(layoutResId);
    }

    @Override
    protected void convert(BaseViewHolder helper, TravelResponse item) {
        helper.setText(R.id.tv_status,"进行中");
        helper.setText(R.id.tv_time,"time");
        helper.setText(R.id.tv_airport,"机场");
        helper.setText(R.id.tv_hotel,"旅店");
    }
}
