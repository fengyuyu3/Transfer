package com.ironaviation.traveller.mvp.ui.my;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.ironaviation.traveller.R;
import com.ironaviation.traveller.mvp.model.entity.response.MessageResponse;

/**
 * 项目名称：Traveller
 * 类描述：
 * 创建人：starRing
 * 创建时间：2017-03-30 16:58
 * 修改人：starRing
 * 修改时间：2017-03-30 16:58
 * 修改备注：
 */
public class EstimateAdapter extends BaseQuickAdapter<String, BaseViewHolder> {

    public EstimateAdapter(int layoutResId) {
        super(layoutResId);
    }

    @Override
    protected void convert(BaseViewHolder helper, String item) {
        helper.setText(R.id.tv_estimate_reason, item);
    }
}
