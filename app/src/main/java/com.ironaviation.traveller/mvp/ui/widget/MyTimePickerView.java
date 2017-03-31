package com.ironaviation.traveller.mvp.ui.widget;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bigkoo.pickerview.view.BasePickerView;
import com.bigkoo.pickerview.view.WheelTime;
import com.ironaviation.traveller.R;

import java.text.ParseException;
import java.util.Calendar;
import java.util.Date;


/**
 * 项目名称：OranTimes
 * 类描述：PickerView 控制类
 * 创建人：starRing
 * 创建时间：2016-09-20 15:35
 * 修改人：starRing
 * 修改时间：2016-09-20 15:35
 * 修改备注：修改
 */
public class MyTimePickerView extends BasePickerView implements View.OnClickListener {
    public enum Type {
        ALL, YEAR_MONTH_DAY, HOURS_MINS, MONTH_DAY_HOUR_MIN, YEAR_MONTH, NINE
    }// 四种选择模式，年月日时分，年月日，时分，月日时分

    MyWheelTime wheelTime;
    private View btnSubmit, btnCancel;
    private TextView tvTitle;
    private static final String TAG_SUBMIT = "submit";
    private static final String TAG_CANCEL = "cancel";
    private OnTimeSelectListener timeSelectListener;
    private Context mContext;

    public MyTimePickerView(Context context, Type type) {
        super(context);
        mContext = context;
        LayoutInflater.from(context).inflate(R.layout.my_pickerview_time, contentContainer);
        // -----确定和取消按钮
        btnSubmit = findViewById(R.id.btnSubmit);
        btnSubmit.setTag(TAG_SUBMIT);
        btnCancel = findViewById(R.id.btnCancel);
        btnCancel.setTag(TAG_CANCEL);
        btnSubmit.setOnClickListener(this);
        btnCancel.setOnClickListener(this);
        //顶部标题
        tvTitle = (TextView) findViewById(R.id.tvTitle);
        // ----时间转轮
        final View timepickerview = findViewById(R.id.timepicker);
        wheelTime = new MyWheelTime(timepickerview, type);

    /*    //默认选中当前时间
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(System.currentTimeMillis());
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH);
        int day = calendar.get(Calendar.DAY_OF_MONTH);
        int hours = calendar.get(Calendar.HOUR_OF_DAY);
        int minute = calendar.get(Calendar.MINUTE);
        if (type.equals(NINE)){
            wheelTime.setPicker(year, month, day, 9, 0);

        }else {
            wheelTime.setPicker(year, month, day, hours, minute);

        }*/

    }

    /**
     * 设置可以选择的时间范围
     * 要在setTime之前调用才有效果
     *
     * @param startYear 开始年份
     * @param endYear   结束年份
     */
    public void setRange(int startYear, int endYear) {
        wheelTime.setStartYear(startYear);
        wheelTime.setEndYear(endYear);
    }

    /**
     * 设置可以选择的时间范围
     * 要在setTime之前调用才有效果
     *
     * @param upYear 开始年份
     * @param downYear   结束年份
     */
    public void setRangeChange(int upYear, int downYear) {
        wheelTime.setUpYear(upYear);
        wheelTime.setDownYear(downYear);
    }
    /**
     * 设置可以选择的时间范围
     * 要在setTime之前调用才有效果
     *
     * @param startYear 开始年份
     */
    public void setStartYear(int startYear) {
        wheelTime.setStartYear(startYear);
    }

    /**
     * 设置可以选择的时间范围
     * 要在setTime之前调用才有效果
     *
     * @param endYear 结束年份
     */
    public void setEndYear(int endYear) {
        wheelTime.setEndYear(endYear);
    }
    /**
     * 设置可以选择的时间范围
     * 要在setTime之前调用才有效果
     *
     * @param endMonth 结束雨纷纷
     */
    public void setEndMonth(int endMonth) {
        wheelTime.setEndMonth(endMonth);
    }

    /**
     * 设置选中时间
     *
     * @param date 时间
     */
    public void setTime(Date date) {
        Calendar calendar = Calendar.getInstance();
        if (date == null)
            calendar.setTimeInMillis(System.currentTimeMillis());
        else
            calendar.setTime(date);
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH);
        int day = calendar.get(Calendar.DAY_OF_MONTH);
        int hours = calendar.get(Calendar.HOUR_OF_DAY);
        int minute = calendar.get(Calendar.MINUTE);
        wheelTime.setPicker(year, month, day, hours, minute);
    }

    public void setTime(int year, int month) {


        wheelTime.setPicker(year, month, 0, 0, 0);
    }
//    /**
//     * 指定选中的时间，显示选择器
//     *
//     * @param date
//     */
//    public void show(Date date) {
//        Calendar calendar = Calendar.getInstance();
//        if (date == null)
//            calendar.setTimeInMillis(System.currentTimeMillis());
//        else
//            calendar.setTime(date);
//        int year = calendar.get(Calendar.YEAR);
//        int month = calendar.get(Calendar.MONTH);
//        int day = calendar.get(Calendar.DAY_OF_MONTH);
//        int hours = calendar.get(Calendar.HOUR_OF_DAY);
//        int minute = calendar.get(Calendar.MINUTE);
//        wheelTime.setPicker(year, month, day, hours, minute);
//        show();
//    }

    /**
     * 设置是否循环滚动
     *
     * @param cyclic 是否循环
     */
    public void setCyclic(boolean cyclic) {
        wheelTime.setCyclic(cyclic);
    }

    /**
     * 设置背景透明
     */
    public void setBGTransparent() {
        LinearLayout linearLayout = (LinearLayout) findViewById(R.id.timepicker);
        linearLayout.setBackgroundColor(mContext.getResources().getColor(R.color.play_range_bg));
    }

    @Override
    public void onClick(View v) {
        String tag = (String) v.getTag();
        if (tag.equals(TAG_CANCEL)) {
            dismiss();
            return;
        } else {
            if (timeSelectListener != null) {
                try {
                    Date date = WheelTime.dateFormat.parse(wheelTime.getTime());
                    timeSelectListener.onTimeSelect(date);
                } catch (ParseException e) {
                    e.printStackTrace();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
            dismiss();
            return;
        }
    }

    public interface OnTimeSelectListener {
        void onTimeSelect(Date date) throws Exception;
    }

    public void setOnTimeSelectListener(OnTimeSelectListener timeSelectListener) {
        this.timeSelectListener = timeSelectListener;
    }

    public void setTitle(String title) {
        tvTitle.setText(title);
    }
}
