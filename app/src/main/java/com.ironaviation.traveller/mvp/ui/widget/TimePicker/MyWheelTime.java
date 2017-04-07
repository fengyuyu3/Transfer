package com.ironaviation.traveller.mvp.ui.widget.TimePicker;

import android.content.Context;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;

import com.bigkoo.pickerview.lib.WheelView;
import com.bigkoo.pickerview.listener.OnItemSelectedListener;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;

/**
 * 项目名称：OranTimes
 * 类描述：==WheelTime
 * 创建人：starRing
 * 创建时间：2016-09-20 15:36
 * 修改人：starRing
 * 修改时间：2016-09-20 15:36
 * 修改备注：
 */
public class MyWheelTime {
    public static DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm");
    private View view;
    private WheelView wv_year;
    private WheelView wv_month;
    private WheelView wv_day;
    private WheelView wv_hours;
    private WheelView wv_mins;

    private MyTimePickerView.Type type;
    public static final int DEFULT_START_YEAR = 1990;
    public static final int DEFULT_END_YEAR = 2100;
    private int startYear = DEFULT_START_YEAR;
    private int endYear = DEFULT_END_YEAR;
    private int upYear = DEFULT_START_YEAR;//自动回滚上限
    private int downYear = DEFULT_END_YEAR;//自动回滚下限
    private int endMonth = 12;
    private int year_num;

    private boolean flag=false;
    private Calendar mCalendar = Calendar.getInstance();

    public MyWheelTime(View view) {
        super();
        this.view = view;
        type = MyTimePickerView.Type.ALL;
        setView(view);
    }

    public MyWheelTime(View view, MyTimePickerView.Type type) {
        super();
        this.view = view;
        this.type = type;
        setView(view);
    }

    public void setPicker(int year, int month, int day) {
        this.setPicker(year, month, day, 0, 0);
    }

    public void setPicker(int year, int month, int day, int h, int m) {
        // 添加大小月月份并将其转换为list,方便之后的判断
        String[] months_big = {"1", "3", "5", "7", "8", "10", "12"};
        String[] months_little = {"4", "6", "9", "11"};

        final List<String> list_big = Arrays.asList(months_big);
        final List<String> list_little = Arrays.asList(months_little);

        Context context = view.getContext();
        // 年
        wv_year = (WheelView) view.findViewById(com.bigkoo.pickerview.R.id.year);
        wv_year.setAdapter(new MyNumericWheelAdapter(startYear, endYear));// 设置"年"的显示数据
        wv_year.setLabel(context.getString(com.bigkoo.pickerview.R.string.pickerview_year));// 添加文字
        wv_year.setCurrentItem(year - startYear);// 初始化时显示的数据\

        // 月
        wv_month = (WheelView) view.findViewById(com.bigkoo.pickerview.R.id.month);
        wv_month.setAdapter(new MyNumericWheelAdapter(1, 12));


        wv_month.setLabel(context.getString(com.bigkoo.pickerview.R.string.pickerview_month));
        wv_month.setCurrentItem(month);

        // 日
        wv_day = (WheelView) view.findViewById(com.bigkoo.pickerview.R.id.day);
        // 判断大小月及是否闰年,用来确定"日"的数据
        if (list_big.contains(String.valueOf(month + 1))) {
            wv_day.setAdapter(new MyNumericWheelAdapter(1, 31));
        } else if (list_little.contains(String.valueOf(month + 1))) {
            wv_day.setAdapter(new MyNumericWheelAdapter(1, 30));
        } else {
            // 闰年
            if ((year % 4 == 0 && year % 100 != 0) || year % 400 == 0)
                wv_day.setAdapter(new MyNumericWheelAdapter(1, 29));
            else
                wv_day.setAdapter(new MyNumericWheelAdapter(1, 28));
        }
        wv_day.setLabel(context.getString(com.bigkoo.pickerview.R.string.pickerview_day));
        wv_day.setCurrentItem(day - 1);


        wv_hours = (WheelView) view.findViewById(com.bigkoo.pickerview.R.id.hour);
        wv_hours.setAdapter(new MyNumericWheelAdapter(0, 23));
        wv_hours.setLabel(context.getString(com.bigkoo.pickerview.R.string.pickerview_hours));// 添加文字
        wv_hours.setCurrentItem(h);

        wv_mins = (WheelView) view.findViewById(com.bigkoo.pickerview.R.id.min);
        // wv_mins.setAdapter(new CustomNumericWheelAdapter(0,5));
        // 根据屏幕密度来指定选择器字体的大小(不同屏幕可能不同)
        switch (type) {
            case MONTH_DAY_HOUR_TEN_MIN:
                wv_mins.setAdapter(new TenMinutesNumericWheelAdapter(0, 5));
                break;
            default:
                wv_mins.setAdapter(new MyNumericWheelAdapter(0, 59));

                break;
        }
        wv_mins.setLabel(context.getString(com.bigkoo.pickerview.R.string.pickerview_minutes));// 添加文字
        wv_mins.setCurrentItem(m);

        // 添加"年"监听
        OnItemSelectedListener wheelListener_year = new OnItemSelectedListener() {
            @Override
            public void onItemSelected(int index) {


                year_num = index + startYear;
                // 判断大小月及是否闰年,用来确定"日"的数据
                wv_month.setAdapter(new MyNumericWheelAdapter(1, 12));
                if (year_num > downYear) {
                    wv_year.setCurrentItem(downYear - startYear);
                    year_num = downYear;
                    setMonth();
                } else if (year_num < upYear) {
                    year_num = upYear;
                    wv_year.setCurrentItem(upYear - startYear);
                    setMonth();
                }
                if (year_num == downYear) {
                    setMonth();
                } else {

                }

                int maxItem = 30;

                if (list_big
                        .contains(String.valueOf(wv_month.getCurrentItem() + 1))) {
                    wv_day.setAdapter(new MyNumericWheelAdapter(1, 31));
                    maxItem = 31;
                } else if (list_little.contains(String.valueOf(wv_month
                        .getCurrentItem() + 1))) {
                    wv_day.setAdapter(new MyNumericWheelAdapter(1, 30));
                    maxItem = 30;
                } else {
                    if ((year_num % 4 == 0 && year_num % 100 != 0)
                            || year_num % 400 == 0) {
                        wv_day.setAdapter(new MyNumericWheelAdapter(1, 29));
                        maxItem = 29;
                    } else {
                        wv_day.setAdapter(new MyNumericWheelAdapter(1, 28));
                        maxItem = 28;
                    }
                }
                if (wv_day.getCurrentItem() > maxItem - 1) {
                    wv_day.setCurrentItem(maxItem - 1);
                }
            }
        };
        // 添加"月"监听
        OnItemSelectedListener wheelListener_month = new OnItemSelectedListener() {
            @Override
            public void onItemSelected(int index) {
               int m=  wv_month.getCurrentItem();


                if (flag){
                    if (mCalendar.before(getDate())) {
                        wv_month.setCurrentItem(getDate().get(Calendar.MONTH));
                    }
                }

               /* if (year_num == downYear) {
                    setMonth();

                } else {

                }
*/

                int month_num = index + 1;
                int maxItem = 30;
                // 判断大小月及是否闰年,用来确定"日"的数据
                if (list_big.contains(String.valueOf(month_num))) {
                    wv_day.setAdapter(new MyNumericWheelAdapter(1, 31));
                    maxItem = 31;
                } else if (list_little.contains(String.valueOf(month_num))) {
                    wv_day.setAdapter(new MyNumericWheelAdapter(1, 30));
                    maxItem = 30;
                } else {
                    if (((wv_year.getCurrentItem() + startYear) % 4 == 0 && (wv_year
                            .getCurrentItem() + startYear) % 100 != 0)
                            || (wv_year.getCurrentItem() + startYear) % 400 == 0) {
                        wv_day.setAdapter(new MyNumericWheelAdapter(1, 29));
                        maxItem = 29;
                    } else {
                        wv_day.setAdapter(new MyNumericWheelAdapter(1, 28));
                        maxItem = 28;
                    }
                }
                if (wv_day.getCurrentItem() > maxItem - 1) {
                    wv_day.setCurrentItem(maxItem - 1);
                }

            }
        };
        wv_year.setOnItemSelectedListener(wheelListener_year);
        wv_month.setOnItemSelectedListener(wheelListener_month);

        // 根据屏幕密度来指定选择器字体的大小(不同屏幕可能不同)
        int textSize = 6;
        switch (type) {
            case ALL:
                textSize = textSize * 3;
                break;
            case YEAR_MONTH_DAY:
                textSize = textSize * 4;
                wv_hours.setVisibility(View.GONE);
                wv_mins.setVisibility(View.GONE);
                break;
            case HOURS_MINS:
                textSize = textSize * 4;
                wv_year.setVisibility(View.GONE);
                wv_month.setVisibility(View.GONE);
                wv_day.setVisibility(View.GONE);

                wv_hours.setLabel(null);
                wv_mins.setLabel(null);


                ViewGroup.MarginLayoutParams wv_hoursMarginLayoutParams = (ViewGroup.MarginLayoutParams) wv_hours.getLayoutParams();
                wv_hoursMarginLayoutParams.width = 200;
                wv_hours.setLayoutParams(wv_hoursMarginLayoutParams);

                ViewGroup.MarginLayoutParams wv_minsMarginLayoutParams = (ViewGroup.MarginLayoutParams) wv_mins.getLayoutParams();
                wv_minsMarginLayoutParams.width = 100;
                wv_mins.setLayoutParams(wv_minsMarginLayoutParams);

                //                wv_hours. setPadding(500,0,0,0);
                //                ViewGroup.MarginLayoutParams marginLayoutParams = (ViewGroup.MarginLayoutParams) wv_hours.getLayoutParams();
                //                marginLayoutParams.setMargins(30,0,0,0);
                //                wv_hours.setLayoutParams(marginLayoutParams);
                break;
            case NINE:
                textSize = textSize * 4;
                wv_year.setVisibility(View.GONE);
                wv_month.setVisibility(View.GONE);
                wv_day.setVisibility(View.GONE);

                wv_hours.setLabel(null);
                wv_mins.setLabel(null);


                ViewGroup.MarginLayoutParams wv_hoursMarginLayoutParams_NINE = (ViewGroup.MarginLayoutParams) wv_hours.getLayoutParams();
                wv_hoursMarginLayoutParams_NINE.width = 200;
                wv_hours.setLayoutParams(wv_hoursMarginLayoutParams_NINE);

                ViewGroup.MarginLayoutParams wv_minsMarginLayoutParams_NINE = (ViewGroup.MarginLayoutParams) wv_mins.getLayoutParams();
                wv_minsMarginLayoutParams_NINE.width = 100;
                wv_mins.setLayoutParams(wv_minsMarginLayoutParams_NINE);

                //                wv_hours. setPadding(500,0,0,0);
                //                ViewGroup.MarginLayoutParams marginLayoutParams = (ViewGroup.MarginLayoutParams) wv_hours.getLayoutParams();
                //                marginLayoutParams.setMargins(30,0,0,0);
                //                wv_hours.setLayoutParams(marginLayoutParams);
                break;
            case MONTH_DAY_HOUR_MIN:
            case MONTH_DAY_HOUR_TEN_MIN:
                textSize = textSize * 3;
                wv_year.setVisibility(View.GONE);
                break;
            case YEAR_MONTH:
                textSize = textSize * 4;
                wv_day.setVisibility(View.GONE);
                wv_hours.setVisibility(View.GONE);
                wv_mins.setVisibility(View.GONE);
        }
        wv_day.setTextSize(textSize);
        wv_month.setTextSize(textSize);
        wv_year.setTextSize(textSize);
        wv_hours.setTextSize(textSize);
        wv_mins.setTextSize(textSize);

    }

    /**
     * 设置是否循环滚动
     *
     * @param cyclic
     */
    public void setCyclic(boolean cyclic) {
        wv_year.setCyclic(cyclic);
        wv_month.setCyclic(cyclic);
        wv_day.setCyclic(cyclic);
        wv_hours.setCyclic(cyclic);
        wv_mins.setCyclic(cyclic);
    }

    public String getTime() {
        StringBuffer sb = new StringBuffer();
        sb.append((wv_year.getCurrentItem() + startYear)).append("-")
                .append((wv_month.getCurrentItem() + 1)).append("-")
                .append((wv_day.getCurrentItem() + 1)).append(" ")
                .append(wv_hours.getCurrentItem()).append(":")
                .append(wv_mins.getCurrentItem());
        return sb.toString();
    }

    public Calendar getDate() {
        Calendar calendar = new GregorianCalendar();
        calendar.set(wv_year.getCurrentItem() + startYear
                , wv_month.getCurrentItem() + 1
                , wv_day.getCurrentItem() + 1
                , wv_hours.getCurrentItem()
                , wv_mins.getCurrentItem());
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH);
        int day = calendar.get(Calendar.DAY_OF_MONTH);
        int hours = calendar.get(Calendar.HOUR_OF_DAY);
        int minute = calendar.get(Calendar.MINUTE);

        Log.i("Time",year+"年"+month+"月"+day+"日"+hours+"点"+minute+"分");
        return calendar;
    }

    public View getView() {
        return view;
    }

    public void setView(View view) {
        this.view = view;
    }

    public int getStartYear() {
        return startYear;
    }

    public void setStartYear(int startYear) {
        this.startYear = startYear;
    }

    public void setEndMonth(int endMonth) {
        this.endMonth = endMonth;
    }

    public void setEndDate(Date endDate) {
        flag=true;
        mCalendar.setTime(endDate);
    }

    public int getEndYear() {
        return endYear;
    }

    public void setEndYear(int endYear) {
        this.endYear = endYear;

    }

    public void setUpYear(int upYear) {
        this.upYear = upYear;
    }

    public void setDownYear(int downYear) {
        this.downYear = downYear;
        year_num = downYear;
    }

    public void setMonth() {
        if (wv_month.getCurrentItem() > endMonth - 1) {
            wv_month.setCurrentItem(endMonth - 1);
        } else {

        }
    }
}
