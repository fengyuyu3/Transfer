package com.ironaviation.traveller.mvp.ui.widget;

import android.app.Dialog;
import android.content.Context;
import android.os.Handler;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.ironaviation.traveller.R;
import com.ironaviation.traveller.app.utils.TimeNewUtil;
import com.ironaviation.traveller.app.utils.TimerUtils;
import com.weigan.loopview.LoopView;
import com.weigan.loopview.OnItemSelectedListener;

import java.util.List;

import static com.jess.arms.utils.UiUtils.getResources;

/**
 * Created by Administrator on 2017/5/19.
 */

public class TimeNewDialog {

    private int indexDay,indexHour,indexMinite;
    private Dialog mCameraDialog;
    private Context context;
    private long time;
    private long currentTime;
    private List<String> days;
    private int status = -1;
    private LoopView lwDay,lwHour,lwSec;
    private boolean isFirst = false;
    private List<String> minites;
    private List<String> hours;
    private MyTimeDialog.ItemCallBack mCallBack;
    private boolean isOneDay;
    private boolean isOneHour;
    private boolean isMinite;
    public TimeNewDialog(Context context, MyTimeDialog.ItemCallBack mCallBack, long time){
        this.context = context;
        this.mCallBack = mCallBack;
        this.time = time-(60*60*2000+60*30*1000);
        currentTime = time-60*60*24*1000;
        if(System.currentTimeMillis()+60*60*2000+10*60*1000 > currentTime){
            currentTime = System.currentTimeMillis()+60*60*2000+10*60*1000;
        }else{
//                currentTime = currentTime+(60*60*2000+10*60*1000);
        }
    }
    public void showDialog(final String title) {
        if (currentTime > time){
            Toast.makeText(context,context.getString(R.string.time_out_four_hours),Toast.LENGTH_SHORT).show();
            return;
        }
        mCameraDialog  = new Dialog(context, R.style.picker_dialog_background);
        LinearLayout root = (LinearLayout) LayoutInflater.from(context).inflate(
                R.layout.dialog_time_view, null);
        mCameraDialog.setContentView(root);
        Window dialogWindow = mCameraDialog.getWindow();
        dialogWindow.setGravity(Gravity.BOTTOM);
        lwDay = (LoopView) mCameraDialog.findViewById(R.id.loopView_day);
        lwHour = (LoopView) mCameraDialog.findViewById(R.id.loopView_hour);
        lwSec = (LoopView) mCameraDialog.findViewById(R.id.loopView_m);
        TextView twTitle = (TextView) mCameraDialog.findViewById(R.id.tw_title);
        TextView twConfirm = (TextView) mCameraDialog.findViewById(R.id.tw_confirm);
        TextView twCancel = (TextView) mCameraDialog.findViewById(R.id.tw_cancel);
        twTitle.setText(title);
        isOneDay = TimeNewUtil.isOneDay(currentTime,time);
        try {
            days = TimeNewUtil.getDays(currentTime,time);
            hours = TimeNewUtil.getHours(currentTime,time,0);
            minites = TimeNewUtil.getMinite(currentTime,time,0,0);
            lwDay.setItems(days);
            lwHour.setItems(hours);
            lwSec.setItems(minites);

            // 滚动监听
            lwDay.setListener(new OnItemSelectedListener() {
                @Override
                public void onItemSelected(int index) {
                    indexDay = index;
                    if(isOneDay){

                    }else{
//                        days = TimeNewUtil.getDays(currentTime,time);
                        hours = TimeNewUtil.getHours(currentTime,time,indexDay);
                        lwHour.setItems(hours);
                        indexHour = 0;
                        lwHour.setCurrentPosition(indexHour);
                        minites = TimeNewUtil.getMinite(currentTime,time,indexDay,indexHour);
                        lwSec.setItems(minites);
                        indexMinite = 0;
                        lwSec.setCurrentPosition(indexMinite);
                    }
                }
            });
            lwHour.setListener(new OnItemSelectedListener() {
                @Override
                public void onItemSelected(int index) {
                    indexHour = index;
                    if(isOneDay && isOneHour){
                    }else if(isOneDay){
                        hours = TimeNewUtil.getHours(currentTime,time,indexDay);
                        minites = TimeNewUtil.getMinite(currentTime,time,indexDay,indexHour);
                        lwSec.setItems(minites);
                        indexMinite = 0;
                        lwSec.setCurrentPosition(indexMinite);
                    }else{
                        hours = TimeNewUtil.getHours(currentTime,time,indexDay);
                        minites = TimeNewUtil.getMinite(currentTime,time,indexDay,indexHour);
                        lwSec.setItems(minites);
                        indexMinite = 0;
                        lwSec.setCurrentPosition(indexMinite);
                    }
                }
            });
            lwSec.setListener(new OnItemSelectedListener() {
                @Override
                public void onItemSelected(int index) {
                    indexMinite = index;
                }
            });
        }catch (Exception e){
            Toast.makeText(context,context.getString(R.string.time_out_four_hours),Toast.LENGTH_SHORT).show();
            return;
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
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
//                      EventBus.getDefault().post(myIndex, EventBusTags.DIALOG_EVENT);
                        String date = TimerUtils.getYear(currentTime+indexDay*24*60*60)
                                +days.get(indexDay) + hours.get(indexHour)+minites.get(indexMinite);
//                      Log.e("kkk",date+"   "+TimerUtils.getTimeMillis(date));
                        mCallBack.setTime(TimerUtils.getTimeMillis(date));
                        mCameraDialog.dismiss();
                    }
                },200);
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

    public interface ItemCallBack{
        void setTime(long time);
    }
}
