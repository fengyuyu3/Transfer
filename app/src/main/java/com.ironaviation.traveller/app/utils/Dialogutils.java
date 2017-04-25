package com.ironaviation.traveller.app.utils;

import android.content.Context;

import com.ironaviation.traveller.R;
import com.ironaviation.traveller.mvp.ui.widget.CustomDialog;

/**
 * Created by Administrator on 2017/4/22.
 */

public class DialogUtils {

    public static void createDialogViewWithFinish(final Context context, int title, int leftResId, int rightResId, int msgResId,
                                                  CustomDialog.OnPositiveListener leftListener, CustomDialog.OnNegativeListener rightListener) {
        CustomDialog dialog=new CustomDialog(context,R.style.Dialog);
        dialog.setmTitleText(context.getString(title));
        dialog.setContentText(context.getString(msgResId));
        dialog.setPositiveListener(R.color.play_range_bg,context.getString(leftResId),leftListener);
        dialog.setNegativeListener(R.color.play_range_bg,context.getString(rightResId),rightListener).show();
    }
}
