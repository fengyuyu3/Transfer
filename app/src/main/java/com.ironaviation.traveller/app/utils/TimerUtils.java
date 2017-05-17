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
    private static String format ="MM月dd日 HH点mm分";
    private static String formatDate = "dd";
    private static final int day = 1;
    private static String formatHourMinite = "HH点mm分";

    public static List<String> getEightDate(){
        List<String> list = new ArrayList<>();
        for(int i = 0; i < WEEKS+1 ; i++) {
            Date date=new Date();//取时间
            Calendar calendar = new GregorianCalendar();
            calendar.setTime(date);
            calendar.add(calendar.DATE, i-1);//把日期往后增加一天.整数往后推,负数往前移动
            date = calendar.getTime(); //这个时间就是日期往后推一天的结果
            SimpleDateFormat formatter = new SimpleDateFormat("MM月dd日");
            String dateString = formatter.format(date);
            list.add(dateString);
        }
        return list;
    }
    public static List<String> getSevenDate(){
        List<String> list = new ArrayList<>();
        for(int i = 0; i < WEEKS ; i++) {
            Date date=new Date();//取时间
            Calendar calendar = new GregorianCalendar();
            calendar.setTime(date);
            calendar.add(calendar.DATE, i);//把日期往后增加一天.整数往后推,负数往前移动
            date = calendar.getTime(); //这个时间就是日期往后推一天的结果
            SimpleDateFormat formatter = new SimpleDateFormat("MM月dd日");
            String dateString = formatter.format(date);
            list.add(dateString);
        }
        return list;
    }

    public static List<String> getDays(long times,long currentTime){
        List<String> list = new ArrayList<>();
        Date date1 = new Date(times);
        if(date1.getMinutes() >= 50 && date1.getHours() == 23){
            for(int i = 1 ; i <= day; i++){
                Date date=new Date(currentTime);//取时间
                Calendar calendar = new GregorianCalendar();
                calendar.setTime(date);
                calendar.add(calendar.DATE, i);//把日期往后增加一天.整数往后推,负数往前移动
                date = calendar.getTime(); //这个时间就是日期往后推一天的结果
                SimpleDateFormat formatter = new SimpleDateFormat("MM月dd日");
                String dateString = formatter.format(date);
                list.add(dateString);
            }
        }else {
            for (int i = 0; i <= day; i++) {
                Date date = new Date(currentTime);//取时间
                Calendar calendar = new GregorianCalendar();
                calendar.setTime(date);
                calendar.add(calendar.DATE, i);//把日期往后增加一天.整数往后推,负数往前移动
                date = calendar.getTime(); //这个时间就是日期往后推一天的结果
                SimpleDateFormat formatter = new SimpleDateFormat("MM月dd日");
                String dateString = formatter.format(date);
                list.add(dateString);
            }
        }
        return list;
    }

    public static List<String> getOneday(long times){
        List<String> list = new ArrayList<>();
        SimpleDateFormat formatter = new SimpleDateFormat("MM月dd日");
        String dateString = formatter.format(new Date(times));
        list.add(dateString);
        return list;
    }

    public static List<String> getStartHours(long time){
        Date date = new Date(time);
        List<String> list = new ArrayList<>();
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

    public static List<String> getStartMins(long time){
        Date date = null;
        date = new Date(time);
        List<String> list = new ArrayList<>();
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
        for(int i = 0 ; i < HOURS; i++){
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
        for(int i = 0 ; i <= hour; i++){
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

    public static List<String> getOneHours(long times,long currentTime){
        List<String> list = new ArrayList<>();
        Date currentDate = null;
        currentDate = new Date(currentTime);
        Date date = new Date(times);
        int hour = date.getHours();
        int currentHour;
        if(currentDate.getMinutes()/10 == 5){
            currentHour = currentDate.getHours()+1;
        }else{
            currentHour = currentDate.getHours();
        }
        for(int i = currentHour ; i<= hour; i++){
            list.add(i+"点");
        }
        return list;
    }

    public static List<String> getOneMinites(long times,long currentTime){
        List<String> list = new ArrayList<>();
        Date currentDate = null;
        currentDate = new Date(currentTime);
        Date date = new Date(times);
        int currentMinite = currentDate.getMinutes();
        int minite = date.getMinutes();
        for(int i = currentMinite/10 ; i< Math.ceil(minite/10); i++){
            if(i == 0){
                list.add("00分");
            }else {
                list.add(i * 10 + "分");
            }
        }
        return list;
    }

    //只有一天的情况下
    public static List<String> setCurrentMinite(long times,long current){
        Date currentDate = null;
        currentDate = new Date(current);
        Date date = new Date(times);
        int currentHours = currentDate.getHours();
        int dayHours = date.getHours();
        int currentMinite = currentDate.getMinutes();
        int minite = date.getMinutes();
        if(currentHours == dayHours){
            return getOneMinites(times,current);
        }else{
            return getStartMins(current);
        }
    }

    public static String getYear(long time){
        Date date = new Date(time);
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy年");
        String dateString = formatter.format(date);
        return dateString;
    }

    public static long getTimeMillis(String date){
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy年MM月dd日HH点mm分");
        try {
            long millionSeconds = sdf.parse(date).getTime();//毫秒
            return millionSeconds;
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return 0;
    }

    public static String getDateFormat(long time,String format){
        try {
            Date date = new Date(time);
            SimpleDateFormat formatter = new SimpleDateFormat(format);
            String dateString = formatter.format(date);
            return dateString;
        }catch (Exception e){
            e.printStackTrace();
            return null;
        }
    }

    public static String getDayHour(long time){
        return getDateFormat(time,format);
    }

    public static long getTime(String time){
        long t;
        try{
            t = Long.parseLong(time);
        }catch (Exception e){
            t = 0;
        }
        return t;
    }

    public static boolean isOneHour(long time ,long currentTime){
        Date currentDate = null;
        currentDate = new Date(currentTime);
        Date date = new Date(time);
        int currentHours = currentDate.getHours();
        int dayHours = date.getHours();
        int currentDay = currentDate.getDate();
        int day = date.getDate();
        if(currentDay == day && dayHours == currentHours){
            return true;
        }else{
            return false;
        }
    }

    public static int getDay(long time, long currentTime){
        Date date = new Date(time);
        int currentHour = 0;
        if(date.getMinutes()/10 == 5){
            currentHour = date.getHours()+1;
        }
        if(time != 0 && currentTime != 0){
            if(currentHour == 24){
                return 1;
            }else if(getDateFormat(time,formatDate).equals(getDateFormat(currentTime,formatDate))){
                return 1;
            }else{
                return 2;
            }
        }
        return 0;
    }
}
