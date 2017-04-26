package com.ironaviation.traveller.mvp.ui.my.adapter;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.ironaviation.traveller.R;
import com.ironaviation.traveller.mvp.model.entity.response.CancelBookingInfo;
import com.ironaviation.traveller.mvp.model.entity.response.EstimateResponse;
import com.ironaviation.traveller.mvp.model.entity.response.TravelCancelReason;

/**
 * 项目名称：Traveller
 * 类描述：
 * 创建人：starRing
 * 创建时间：2017-03-30 16:58
 * 修改人：starRing
 * 修改时间：2017-03-30 16:58
 * 修改备注：
 */
public class TravelCancelAdapter extends BaseQuickAdapter<CancelBookingInfo.Reasons, BaseViewHolder> {

    public TravelCancelAdapter(int layoutResId) {
        super(layoutResId);
    }

    @Override
    protected void convert(BaseViewHolder helper, CancelBookingInfo.Reasons item) {
        helper.setText(R.id.tv_travel_cancel, item.getReason());
        if (item.isType()) {
            helper.setBackgroundRes(R.id.iv_travel_cancel, R.mipmap.ic_selected_champagne);

        } else {
            helper.setBackgroundRes(R.id.iv_travel_cancel, R.mipmap.ic_un_selected_grey);

        }
    }
}
