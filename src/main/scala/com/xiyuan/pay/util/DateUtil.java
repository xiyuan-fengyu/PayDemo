package com.xiyuan.pay.util;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by xiyuan_fengyu on 2016/8/25.
 */
public class DateUtil {

    private static final String yyyy_MM_dd_HH_mm_ss = "yyyy-MM-dd HH:mm:ss";

    private static final String yyyyMMddHHmmss = "yyyyMMddHHmmss";

    private static final SimpleDateFormat format0 = new SimpleDateFormat(yyyy_MM_dd_HH_mm_ss);

    private static final SimpleDateFormat format1 = new SimpleDateFormat(yyyyMMddHHmmss);

    public static String dateToStr(Date date, String formatStr) {
        Date temp = null;
        if (date != null) {
            temp = date;
        }
        else {
            temp = new Date();
        }

        SimpleDateFormat format = null;
        if (yyyyMMddHHmmss.equals(formatStr)) {
            format = format1;
        }
        else if (yyyy_MM_dd_HH_mm_ss.equals(formatStr)) {
            format = format0;
        }
        else {
            format = new SimpleDateFormat(formatStr);
        }

        return format.format(temp);
    }

    public static String dateToStr(Date date) {
        return dateToStr(date, yyyy_MM_dd_HH_mm_ss);
    }

    public static String nowToStr() {
        return dateToStr(null, yyyy_MM_dd_HH_mm_ss);
    }

    public static String dateToNoDividerStr(Date date) {
        return dateToStr(date, yyyyMMddHHmmss);
    }

    public static String nowToNoDividerStr() {
        return dateToStr(null, yyyyMMddHHmmss);
    }

    public static Date strToDate(String str) {
        try {
            return format0.parse(str);
        } catch (ParseException e) {
            e.printStackTrace();
            return new Date(0);
        }
    }

}
