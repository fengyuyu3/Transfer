package com.ironaviation.traveller.mvp.ui.my.adapter;

import android.text.TextUtils;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.ironaviation.traveller.R;
import com.ironaviation.traveller.mvp.model.entity.request.MessageRequest;
import com.ironaviation.traveller.mvp.model.entity.response.MessageResponse;
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

public class MessageAdapter extends BaseQuickAdapter<MessageResponse.Items, BaseViewHolder> {

    public MessageAdapter(int layoutResId) {
        super(layoutResId);
    }

    @Override
    protected void convert(BaseViewHolder helper, MessageResponse.Items item) {
        helper.setText(R.id.tv_message_title, item.getTitle());

        if (!TextUtils.isEmpty(item.getTitle())) {
            helper.setText(R.id.tv_message_text, item.getContents().replace("，点击查看行程详情", ""));
        } else {
            helper.setVisible(R.id.tv_message_text, false);
        }
    }
}
