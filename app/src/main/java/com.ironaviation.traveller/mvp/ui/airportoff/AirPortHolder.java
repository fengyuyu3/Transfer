package com.ironaviation.traveller.mvp.ui.airportoff;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.ironaviation.traveller.R;
import com.jess.arms.base.BaseHolder;

/**
 * 项目名称：Transfer
 * 类描述：
 * 创建人：flq
 * 创建时间：2017/3/29 13:48
 * 修改人：
 * 修改时间：2017/3/29 13:48
 * 修改备注：
 */

public class AirPortHolder extends RecyclerView.ViewHolder {

    ImageView ivLogo;
    ImageView ivStatus;
    EditText etText;
    TextView twCode;
    View line;

    public AirPortHolder(View itemView) {
        super(itemView);
        ivLogo = (ImageView) itemView.findViewById(R.id.iv_logo);
        ivStatus = (ImageView) itemView.findViewById(R.id.iv_status);
        etText = (EditText) itemView.findViewById(R.id.edt_content);
        twCode = (TextView) itemView.findViewById(R.id.tv_code);
        line = itemView.findViewById(R.id.line_edt);
    }
}
