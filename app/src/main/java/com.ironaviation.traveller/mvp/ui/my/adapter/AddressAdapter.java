package com.ironaviation.traveller.mvp.ui.my.adapter;

import com.baidu.mapapi.search.core.PoiInfo;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.ironaviation.traveller.R;
import com.ironaviation.traveller.mvp.constant.Constant;
import com.ironaviation.traveller.mvp.model.entity.HistoryPoiInfo;
import com.ironaviation.traveller.mvp.model.entity.response.EstimateResponse;

/**
 * 项目名称：Traveller
 * 类描述：
 * 创建人：starRing
 * 创建时间：2017-03-30 16:58
 * 修改人：starRing
 * 修改时间：2017-03-30 16:58
 * 修改备注：
 */
public class AddressAdapter extends BaseQuickAdapter<HistoryPoiInfo, BaseViewHolder> {

    public AddressAdapter(int layoutResId) {
        super(layoutResId);
    }

    @Override
    protected void convert(BaseViewHolder helper, HistoryPoiInfo item) {

        helper.setText(R.id.tv_item_address, item.name);
        helper.setText(R.id.tv_item_detail_address, item.address);
        if (item.isFlagHistory()) {
            helper.setBackgroundRes(R.id.iv_address, R.mipmap.ic_clock);

        } else {
            helper.setBackgroundRes(R.id.iv_address, R.mipmap.ic_address);
        }
    }


}
