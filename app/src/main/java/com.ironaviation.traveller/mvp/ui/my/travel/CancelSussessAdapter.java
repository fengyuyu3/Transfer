package com.ironaviation.traveller.mvp.ui.my.travel;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.ironaviation.traveller.R;
import com.ironaviation.traveller.mvp.model.entity.response.CancelSuccessResponse;
import com.ironaviation.traveller.mvp.model.entity.response.TravelCancelResponse;

/**
 * 项目名称：Traveller
 * 类描述：
 * 创建人：starRing
 * 创建时间：2017-03-30 16:58
 * 修改人：starRing
 * 修改时间：2017-03-30 16:58
 * 修改备注：
 */
public class CancelSussessAdapter extends BaseQuickAdapter<CancelSuccessResponse, BaseViewHolder> {

    public CancelSussessAdapter(int layoutResId) {
        super(layoutResId);
    }

    @Override
    protected void convert(BaseViewHolder helper, CancelSuccessResponse item) {
        helper.setText(R.id.tv_item_cancel_success, item.getName());

    }
}
