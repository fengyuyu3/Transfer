package com.ironaviation.traveller.mvp.ui.my.adapter;

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

public class MessageAdapter extends BaseQuickAdapter<MessageResponse, BaseViewHolder> {

    public MessageAdapter(int layoutResId) {
        super(layoutResId);
    }

    @Override
    protected void convert(BaseViewHolder helper, MessageResponse item) {
        helper.setText(R.id.tv_message_text, "您的预约已经接受");
        helper.setText(R.id.tv_message_title, "系统消息");
    }
}