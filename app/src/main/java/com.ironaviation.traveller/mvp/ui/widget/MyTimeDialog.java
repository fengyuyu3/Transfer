package com.ironaviation.traveller.mvp.ui.widget;

import android.app.Dialog;
import android.content.Context;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.ironaviation.traveller.R;
import com.ironaviation.traveller.app.EventBusTags;
import com.ironaviation.traveller.app.utils.TimerUtils;
import com.weigan.loopview.LoopView;
import com.weigan.loopview.OnItemSelectedListener;

import org.simple.eventbus.EventBus;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import static com.jess.arms.utils.UiUtils.getResources;

/**
 * Created by Administrator on 2017/4/5 0005.
 */

public class MyTimeDialog {

        private int indexDay,indexHour,indexMinite;
        private Dialog  mCameraDialog;
        private Context context;
        private long time;
        private long currentTime;
        private List<String> list;
        private int status = -1;
        private LoopView lwDay,lwHour,lwSec;
        private boolean isFirst = false;
        private List<String> minites;
        private List<String> hours;
        private ItemCallBack mCallBack;
        public MyTimeDialog(Context context,ItemCallBack mCallBack,long time){
            this.context = context;
            this.mCallBack = mCallBack;
            this.time = time-(60*60*1500);
            currentTime = time-60*60*24*1000;
        }
        public void showDialog(final String title) {
            mCameraDialog  = new Dialog(context, R.style.picker_dialog_background);
            LinearLayout root = (LinearLayout) LayoutInflater.from(context).inflate(
                    R.layout.dialog_time_view, null);
            mCameraDialog.setContentView(root);
            Window dialogWindow = mCameraDialog.getWindow();
            dialogWindow.setGravity(Gravity.BOTTOM);
            list = getStartDate(time);
            lwDay = (LoopView) mCameraDialog.findViewById(R.id.loopView_day);
            lwHour = (LoopView) mCameraDialog.findViewById(R.id.loopView_hour);
            lwSec = (LoopView) mCameraDialog.findViewById(R.id.loopView_m);
            TextView twTitle = (TextView) mCameraDialog.findViewById(R.id.tw_title);
            TextView twConfirm = (TextView) mCameraDialog.findViewById(R.id.tw_confirm);
            TextView twCancel = (TextView) mCameraDialog.findViewById(R.id.tw_cancel);
            twTitle.setText(title);

            // 滚动监听
            lwDay.setListener(new OnItemSelectedListener() {
                @Override
                public void onItemSelected(int index) {
                    indexDay = index;
                    if(index == 0 && list.size() != 1){// 在第一天并且天数有两天以上
                        lwHour.setItems(getStartHour());
                        hours = getStartHour();
                    }else if(index == list.size()-1){ //  在最后一天
                        lwHour.setItems(getEndHour(time));
                        hours = getEndHour(time);
                    }else if(index == 0 && list.size() == 1){ //总的天数只有一天
                        lwHour.setItems(getOneHour(time));
                        hours = getOneHour(time);
                    }else{
                        lwHour.setItems(getMidHour());
                        hours = getMidHour();
                    }
                    lwHour.setCurrentPosition(0);
                    indexHour = 0;
                    setRefreshMinite(indexHour);
                }
            });
            lwHour.setListener(new OnItemSelectedListener() {
                @Override
                public void onItemSelected(int index) {
                    indexHour = index;
                    setRefreshMinite(index);
                    /*if(indexDay == 0 && list.size() != 1 && index == 0){ //在第一天并且天数有两天以上并且是第一个小时
                        lwSec.setItems(getStartMinite());
                    }else if(indexDay == list.size()-1 && index == getEndHour(time).size()-1){ //在最后一天
                        lwSec.setItems(getEndMinite(time));
                    }else if(index == 0 && list.size() == 1 && index == getOneHour(time).size() -1){
                        lwSec.setItems(getOneMinite(time));
                    }else{
                        lwSec.setItems(getMidMinite());
                    }*/
                }
            });
            lwSec.setListener(new OnItemSelectedListener() {
                @Override
                public void onItemSelected(int index) {
                    indexMinite = index;
                    /*if(list.size() == 1){
                        lwSec.setItems(getOneMinite(time));
                    } else if(indexDay == 0 && indexDay != list.size()-1){
                        lwSec.setItems(getStartMinite());
                    }else if(indexDay == list.size()-1){
                        lwSec.setItems(getEndMinite(time));
                    }else{
                        lwSec.setItems(getMidMinite());
                    }*/
                }
            });
            // 设置原始数据
            hours = getStartHour();
            minites = getStartMinite();
            lwDay.setItems(getStartDate(time));
            lwHour.setItems(getStartHour());
            lwSec.setItems(getStartMinite());
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
//                    EventBus.getDefault().post(myIndex, EventBusTags.DIALOG_EVENT);
                    String date = TimerUtils.getYear(currentTime+indexDay*24*60*60)
                    +list.get(indexDay) + hours.get(indexHour)+minites.get(indexMinite);
                    Log.e("kkk",date+"   "+TimerUtils.getTimeMillis(date));
                    mCallBack.setTime(TimerUtils.getTimeMillis(date));
                    mCameraDialog.dismiss();
                }
            });

            twCancel.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mCameraDialog.dismiss();
                }
            });

            Log.e("kkk",new Date(time).toString());
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
    public List<String> getStartDate(long times){
        return TimerUtils.getDays(times,currentTime);
    }

    public List<String> getStartHour(){
        return TimerUtils.getStartHours(currentTime);
    }

    public List<String> getStartMinite(){
        return TimerUtils.getStartMins(currentTime);
    }

    public List<String> getMidHour(){
        return TimerUtils.getMidHours();
    }

    public List<String> getMidMinite(){
        return TimerUtils.getMidMinites();
    }

    public List<String> getEndHour(long endTimes){
        return TimerUtils.getEndHours(endTimes);
    }

    public List<String> getEndMinite(long endTimes){
        return TimerUtils.getEndMinites(endTimes);
    }

    public List<String> getOneHour(long endTimes){
        return TimerUtils.getOneHours(endTimes,currentTime);
    }

    public List<String> getOneMinite(long endTimes){
        return TimerUtils.getOneMinites(endTimes,currentTime);
    }

    public void setRefreshMinite(int index){
        if(indexDay == 0 && list.size() != 1 && index == 0){ //在第一天并且天数有两天以上并且是第一个小时
            lwSec.setItems(getStartMinite());
            minites = getStartMinite();
        }else if(indexDay == list.size()-1 && index == getEndHour(time).size()-1){ //在最后一天
            lwSec.setItems(getEndMinite(time));
            minites = getEndMinite(time);
        }else if(index == 0 && list.size() == 1 && index == getOneHour(time).size() -1){
            lwSec.setItems(getOneMinite(time));
            minites = getOneMinite(time);
        }else{
            lwSec.setItems(getMidMinite());
            minites = getMidMinite();
        }
        lwSec.setCurrentPosition(0);
        indexMinite = 0;
    }

    public interface ItemCallBack{
        void setTime(long time);
    }
}
