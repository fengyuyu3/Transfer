package com.ironaviation.traveller.mvp.ui.widget;

import android.app.Dialog;
import android.content.Context;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.ironaviation.traveller.R;
import com.ironaviation.traveller.app.EventBusTags;
import com.ironaviation.traveller.mvp.constant.Constant;
import com.weigan.loopview.LoopView;
import com.weigan.loopview.OnItemSelectedListener;

import org.simple.eventbus.EventBus;

import java.util.ArrayList;
import java.util.List;

import static com.jess.arms.utils.UiUtils.getResources;

/**
 * Created by Administrator on 2017/4/5 0005.
 */

public class MyDialog{

//        private static Dialog mCameraDialog;
        private int myIndex;
        private Dialog  mCameraDialog;
        private Context context;
        private String status;
        public MyDialog(Context context,String status){
            this.context = context;
            this.status = status;
        }
        public void showDialog(List<String> list,String title) {
            mCameraDialog  = new Dialog(context, R.style.picker_dialog);
            LinearLayout root = (LinearLayout) LayoutInflater.from(context).inflate(
                    R.layout.dialog_view, null);
            mCameraDialog.setContentView(root);
            Window dialogWindow = mCameraDialog.getWindow();
            dialogWindow.setGravity(Gravity.BOTTOM);

            LoopView loopView = (LoopView) mCameraDialog.findViewById(R.id.loopView);
            TextView twTitle = (TextView) mCameraDialog.findViewById(R.id.tw_title);
            TextView twConfirm = (TextView) mCameraDialog.findViewById(R.id.tw_confirm);
            TextView twCancel = (TextView) mCameraDialog.findViewById(R.id.tw_cancel);
            twTitle.setText(title);
            // 滚动监听
            loopView.setListener(new OnItemSelectedListener() {
                @Override
                public void onItemSelected(int index) {
                    myIndex = index;
                }
            });
            // 设置原始数据
            loopView.setItems(list);
            if(Constant.ENTER_PORT.equals(status)){
                myIndex = 1;
                loopView.setCurrentPosition(myIndex);
            }
            WindowManager.LayoutParams lp = dialogWindow.getAttributes(); // 获取对话框当前的参数值
            lp.x = 0; // 新位置X坐标
            lp.y = -20; // 新位置Y坐标
            lp.width = (int) getResources().getDisplayMetrics().widthPixels; // 宽度
            //      lp.height = WindowManager.LayoutParams.WRAP_CONTENT; // 高度
            //      lp.alpha = 9f; // 透明度
            root.measure(0, 0);
            lp.height = root.getMeasuredHeight();
            lp.alpha = 9f; // 透明度
            dialogWindow.setAttributes(lp);
            mCameraDialog.setCanceledOnTouchOutside(true);
            mCameraDialog.show();

            twConfirm.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    EventBus.getDefault().post(myIndex, EventBusTags.DIALOG_EVENT);
                }
            });

            twCancel.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mCameraDialog.dismiss();
                }
            });
        }

    public void dismiss(){
        if( mCameraDialog != null && mCameraDialog.isShowing()) {
            mCameraDialog.dismiss();
        }
    }

    public boolean isShowing(){
        if(mCameraDialog != null) {
            return mCameraDialog.isShowing();
        }else{
            return false;
        }
    }
}
