package com.ironaviation.traveller.app.utils;

import java.math.BigDecimal;
import java.text.DecimalFormat;

public class PriceUtil {
    public static long toPrice(String rmb) {
        try {
            BigDecimal d=new BigDecimal(rmb);
            d=d.multiply(new BigDecimal(100));
            return Math.abs(d.longValue());
        } catch (Exception e) {
            return 0;
        }
    }
    public static BigDecimal toPriceDouble(long f)
    {
        BigDecimal d=new BigDecimal(f);
        d=d.divide(new BigDecimal(100),2, BigDecimal.ROUND_HALF_UP);
        return d;
    }
    public static BigDecimal toScoreFloat(int f)
    {
        BigDecimal d=new BigDecimal(f);
        d=d.divide(new BigDecimal(10),1, BigDecimal.ROUND_HALF_UP);
        return d;
    }
    private static BigDecimal toPriceDouble(double f)
    {
        BigDecimal d=new BigDecimal(f);
        d=d.divide(new BigDecimal(100),2, BigDecimal.ROUND_HALF_UP);
        return d;
    }
    private static String getPrice(BigDecimal price) {
        return "￥" + getPrecent(price);
    }
    public static String getPrecent(BigDecimal price) {
        String ds = "";
        DecimalFormat fmt = new DecimalFormat("0.00");
        ds = fmt.format(price);
        return ds;
    }
    public static String getPrecent(double price) {
        String ds = "";
        DecimalFormat fmt = new DecimalFormat("0.00");
        ds = fmt.format(price);
        return ds;
    }
    private static String getPrecentInteger(BigDecimal price) {
        String ds = "";
        DecimalFormat fmt = new DecimalFormat("0.00");
        ds = fmt.format(price);
        return ds;
    }
    private static String getTencentInteger(BigDecimal price) {
        String ds = "";
        DecimalFormat fmt = new DecimalFormat("0.0");
        ds = fmt.format(price);
        return ds;
    }
    public static String formatPriceInteger(long price)
    {
        return getPrecentInteger(toPriceDouble(price));
    }
    public static String formatScoreInteger(int score)
    {
        return getTencentInteger(toScoreFloat(score));
    }
    public static String formatRMBInteger(long price)
    {
        return "￥"+getPrecentInteger(toPriceDouble(price));
    }
    public static String formatRMB(long price)
    {
        return "¥"+getPrecent(toPriceDouble(price));
    }
    public static String formatRMB(double price)
    {
        return "¥ "+getPrecent(toPriceDouble(price));
    }
    public static String formatRMBNoSymbol(double price)
    {
        return getPrecent(toPriceDouble(price));
    }
    public static String format(long price)
    {
        return getPrecent(toPriceDouble(price));
    }

    public static String formatRedHtml(long f)
    {
        return "<font color='#FF0000'> ￥" + getPrecent(toPriceDouble(f)) + "</font>";
    }
    public static String formatBlackHtml(long f)
    {
        return "<font color='#000000'> ￥" + getPrecent(toPriceDouble(f)) + "</font>";
    }
    public static String formatHtml(long f)
    {
        return "<font> ￥" + getPrecent(toPriceDouble(f)) + "</font>";
    }

    /**
     *
     * @param f  金额
     * @param color 颜色
     * @return
     */
    public static String formatMarkRMBHtml(long f, String color)
    {
        if(f==0)
        {
            return "<font color='"+color+"'> ￥" + getPrecent(toPriceDouble(Math.abs(f))) + "</font>";
        }
        if(f>0){
            return "<font color='"+color+"'>+ ￥" + getPrecent(toPriceDouble(Math.abs(f))) + "</font>";
        }else {
            return "<font color='"+color+"'>- ￥" + getPrecent(toPriceDouble(Math.abs(f))) + "</font>";
        }
    }
}
