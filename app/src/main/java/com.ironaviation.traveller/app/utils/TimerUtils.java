package com.ironaviation.traveller.app.utils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;

/**
 * Created by Administrator on 2017/4/7 0007.
 */

public class TimerUtils {

    public static final int WEEKS = 7;
    public static final int HOURS = 24;
    public static final int MINITES = 6;
    private int day;
    private int hour;
    private int sec;
    public static List<String> getSevenDate(){
        List<String> list = new ArrayList<>();
        for(int i = 0; i < WEEKS ; i++) {
            Date date=new Date();//取时间
            Calendar calendar = new GregorianCalendar();
            calendar.setTime(date);
            calendar.add(calendar.DATE, i-1);//把日期往后增加一天.整数往后推,负数往前移动
            date = calendar.getTime(); //这个时间就是日期往后推一天的结果
            SimpleDateFormat formatter = new SimpleDateFormat("MM月-dd日");
            String dateString = formatter.format(date);
            list.add(dateString);
        }
        return list;
    }

    public static List<String> getDays(long times){
        double minite = times - System.currentTimeMillis();
        int day = (int) Math.ceil(minite/(60*60*1000*24));
        List<String> list = new ArrayList<>();
        Date date1 = new Date(System.currentTimeMillis());
        if(date1.getMinutes() >= 50 && date1.getHours() == 23){
            for(int i = 1 ; i <= day; i++){
                Date date=new Date();//取时间
                Calendar calendar = new GregorianCalendar();
                calendar.setTime(date);
                calendar.add(calendar.DATE, i);//把日期往后增加一天.整数往后推,负数往前移动
                date = calendar.getTime(); //这个时间就是日期往后推一天的结果
                SimpleDateFormat formatter = new SimpleDateFormat("MM月-dd日");
                String dateString = formatter.format(date);
                list.add(dateString);
            }
        }else {
            for (int i = 0; i <= day; i++) {
                Date date = new Date();//取时间
                Calendar calendar = new GregorianCalendar();
                calendar.setTime(date);
                calendar.add(calendar.DATE, i);//把日期往后增加一天.整数往后推,负数往前移动
                date = calendar.getTime(); //这个时间就是日期往后推一天的结果
                SimpleDateFormat formatter = new SimpleDateFormat("MM月-dd日");
                String dateString = formatter.format(date);
                list.add(dateString);
            }
        }
        return list;
    }

    public static List<String> getStartHours(){
        List<String> list = new ArrayList<>();
        Date date = new Date();
        int currentHour = 0;
        if(date.getMinutes()/10 == 5){
            currentHour = date.getHours()+1;
        }else{
            currentHour = date.getHours();
        }
        for(int i = currentHour ; i < HOURS; i++){
            list.add(i+"点");
        }
        return list;
    }

    public static List<String> getStartMins(){
        List<String> list = new ArrayList<>();
        Date date = new Date();
        double currentSec = date.getMinutes();
        if(currentSec >= 50){
            for(int i = 0 ; i < MINITES; i++){
                if(i == 0){
                    list.add("00分");
                }else {
                    list.add(i * 10 + "分");
                }
            }
        }else {
            for (int i = (int) Math.ceil(currentSec / 10); i < MINITES; i++) {
                if (i == 0) {
                    list.add("00分");
                } else {
                    list.add(i * 10 + "分");
                }
            }
        }
        return list;
    }

    public static List<String> getMidHours(){
        List<String> list = new ArrayList<>();
        for(int i = 0 ; i< HOURS; i++){
            list.add((i)+"点");
        }
        return list;
    }

    public static List<String> getMidMinites(){
        List<String> list = new ArrayList<>();
        for(int i = 0; i < MINITES; i++){
            if(i == 0){
                list.add("00分");
            }else {
                list.add(i * 10 + "分");
            }
        }
        return list;
    }

    public static List<String> getEndHours(long times){
        List<String> list = new ArrayList<>();
        Date date = new Date(times);
        int hour = date.getHours();
        for(int i = 0 ; i< hour; i++){
            list.add(i+"点");
        }
        return list;
    }

    public static List<String> getEndMinites(long times){
        List<String> list = new ArrayList<>();
        Date date = new Date(times);
        int minite = date.getMinutes();
        for(int i = 0; i <= minite / 10; i++){
            if(i == 0){
                list.add("00分");
            }else {
                list.add(i * 10 + "分");
            }
        }
        return list;
    }

    public static List<String> getOneHours(long times){
        List<String> list = new ArrayList<>();
        Date currentDate = new Date(System.currentTimeMillis());
        Date date = new Date(times);
        int hour = date.getHours();
        int currentHour = currentDate.getHours();
        for(int i = currentHour ; i< hour; i++){
            list.add(i+"点");
        }
        return list;
    }

    public static List<String> getOneMinites(long times){
        List<String> list = new ArrayList<>();
        Date currentDate = new Date(System.currentTimeMillis());
        Date date = new Date(times);
        int currentMinite = currentDate.getMinutes();
        double minite = date.getMinutes();
        for(int i = currentMinite/10 ; i< (int)Math.ceil(minite/10); i++){
            if(i == 0){
                list.add("00分");
            }else {
                list.add(i * 10 + "分");
            }
        }
        return list;
    }

    public static String getYear(long time){
        Date date = new Date(time);
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy年");
        String dateString = formatter.format(date);
        return dateString;
    }

    public static long getTimeMillis(String date){
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy年MM月-dd日hh点mm分");

        try {
            long millionSeconds = sdf.parse(date).getTime();//毫秒
            return millionSeconds;
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return 0;
    }
}