package com.ironaviation.traveller.app.utils;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by Administrator on 2017/5/19.
 */

public class TimeNewUtil {

    private static String format = "MM月dd日";
    private static int HOUR_NUM = 23;
    private static int MINITE_NUM = 5;
    public static List<String> getDays(long currentTime, long endTime){
        List<String> list = new ArrayList<>();
        Date currentDate = new Date(currentTime);
        Date endDate = new Date(endTime);
        SimpleDateFormat formatter = new SimpleDateFormat(format);
        if(isOneDay(currentTime,endTime)){
            list.add(formatter.format(endDate));
        }else{
            for(int i = 0 ; i < 2; i++){
                Date current = new Date(currentTime);
                list.add(formatter.format(current));
                currentTime = currentTime+24*60*60*1000;
            }
        }
        return list;
    }

    public static List<String> getHours(long currentTime, long endTime,int day){
        Date currentDate = new Date(currentTime);
        Date endDate = new Date(endTime);
        int currentHour = currentDate.getHours();
        int endHour = endDate.getHours();
        List<String> days = getDays(currentTime,endTime);
        List<String> list = new ArrayList<>();
        if(days.size() >= day){ //数据是否正确
            if(isOneDay(currentTime,endTime)){
                if(isOneHours(currentTime,endTime)){ //只有一天的逻辑
                    list.add(endHour+"点");
                }else{
                    if(isMoreOneMinite(currentTime,endTime)){
                        currentHour = currentHour+1;
                    }
                    for (int i = currentHour; i <= endHour; i++) {
                        list.add(i + "点");
                    }
                }
                return list;
            }else{ //大于一天的逻辑
                if(day == 0){
                    if(isMoreOneMinite(currentTime,endTime)){
                        currentHour = currentHour+1;
                    }
                    for(int i = currentHour; i <= HOUR_NUM; i++){
                        list.add(i+"点");
                    }
                }else{
                    for(int i = 0; i <= endHour; i++){
                        list.add(i+"点");
                    }
                }
                return list;
            }
        }else{
            //有错
            return null;
        }
    }

    public static List<String> getMinite(long currentTime, long endTime, int day,int hour){
        Date currentDate = new Date(currentTime);
        Date endDate = new Date(endTime);
        int currentHour = currentDate.getHours();
        int endHour = endDate.getHours();
        double currentMinites = currentDate.getMinutes();
        int currentMinite = (int) Math.ceil(currentMinites/10);
        int endMinite = endDate.getMinutes()/10;
        List<String> list = new ArrayList<>();
        if(isOneDay(currentTime,endTime) && isOneHours(currentTime,endTime)
                && isOneMinite(currentTime,endTime)){ //只有一小时 并且只有一分
            list.add(getMinite(endMinite));
        }else if(isOneDay(currentTime,endTime) && isOneHours(currentTime,endTime)){
            for(int i = currentMinite ; i <= endMinite ; i++){//只有一小时
               list.add(getMinite(i));
            }
        }else if(isOneDay(currentTime,endTime)){//只有一天
            if(hour == 0){
                list = getStartMinite(currentMinite,endMinite,MINITE_NUM);
            }else if(getHours(currentTime,endTime,day) != null &&
                    hour == getHours(currentTime,endTime,day).size()-1){
                list = getEndMinite(currentMinite,endMinite,MINITE_NUM);
            }else{
                list = getMidMinite(currentMinite,endMinite,MINITE_NUM);
            }
        }else{ //两天的情况
            if(day == 0 && hour == 0){
                list = getStartMinite(currentMinite,endMinite,MINITE_NUM);
            }else if(day == 1 && hour == getHours(currentTime,endTime,day).size()-1){
                list = getEndMinite(currentMinite,endMinite,MINITE_NUM);
            }else{
                list = getMidMinite(currentMinite,endMinite,MINITE_NUM);
            }
        }

        return list;
    }

    public static List<String> getStartMinite(int currentMinite,long endMinite,int miniteNum){
        List<String> list = new ArrayList<>();
        if(isMoreOneMinite(currentMinite,endMinite)){
            list = getMidMinite(currentMinite,endMinite,miniteNum);
        }else {
            for (int i = currentMinite; i <= miniteNum; i++) {
                list.add(getMinite(i));
            }
        }
        return list;
    }

    public  static List<String> getEndMinite(int currentMinite,long endMinite,int miniteNum){
        List<String> list = new ArrayList<>();
        for(int i = 0; i <= endMinite ; i++){
            list.add(getMinite(i));
        }
        return list;
    }

    public static List<String> getMidMinite(int currentMinite,long endMinite,int miniteNum){
        List<String> list = new ArrayList<>();
        for(int i = 0; i <= miniteNum ; i++){
            list.add(getMinite(i));
        }
        return list;
    }

    public static String getMinite(int minite){
        if(minite == 0){
            return "00分";
        }else{
            return minite*10+"分";
        }
    }

    public static boolean isOneMinite(long currentTime, long endTime){
        Date currentDate = new Date(currentTime);
        Date endDate = new Date(endTime);
       /* int currentHour = currentDate.getHours();
        int endHour = endDate.getHours();*/
        double currentMinite = currentDate.getMinutes();
        int endMinite = endDate.getMinutes();
        if(isOneDay(currentTime,endTime) && isOneHours(currentTime,endTime)){
            if(Math.ceil(currentMinite/10) == endMinite /10){
                return true;
            }else{
                return false;
            }
        }else{
            return false;
        }
    }

    public static boolean isMoreOneDay(long currentTime,long endTime){
        Date currentDate = new Date(currentTime);
        int currentHour = currentDate.getHours();
        int currentMinite = currentDate.getMinutes();
        if(currentHour == 23 && currentMinite > 50 ){
            return true;
        }else{
            return false;
        }
    }

    public static boolean isMoreOneMinite(long currentTime,long endTime){
        Date currentDate = new Date(currentTime);
        int currentMinite = currentDate.getMinutes();
        if(currentMinite > 50){
            return true;
        }else{
            return false;
        }
    }

    public static boolean isOneHours(long currentTime, long endTime){
        Date currentDate = new Date(currentTime);
        Date endDate = new Date(endTime);
        int currentHour = currentDate.getHours();
        int endHour = endDate.getHours();
        int currentMinite = currentDate.getMinutes();
        if(isOneDay(currentTime,endTime)){
            if(currentHour == endHour || (endHour-currentHour == 1 && currentMinite > 50)){
                return true;
            }else{
                return false;
            }
        }else{
            return false;
        }
    }

    public static boolean isOneDay(long currentTime, long endTime){
        Date currentDate = new Date(currentTime);
        Date endDate = new Date(endTime);
        int currentDay = currentDate.getDay();
        int endDay = endDate.getDay();
        int currentHour = currentDate.getHours();
        int currentMinite = currentDate.getMinutes();
        if(currentDay == endDay || (currentHour == 23 && currentMinite > 50 )){
            return true;
        }else{
            return false;
        }
    }
}
