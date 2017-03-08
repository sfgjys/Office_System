package com.minlu.baselibrary.util;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class SystemTime {


    /**
     * 获取系统时间
     */
    public static String getTimer() {
        long currentTimeMillis = System.currentTimeMillis();
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss", Locale.getDefault());
        String formatTime = simpleDateFormat.format(new Date(currentTimeMillis));
        return formatTime.substring(0, 10) + "/" + formatTime.substring(11, 16);
    }

    public static String getSureWarnTime() {
        long currentTimeMillis = System.currentTimeMillis();
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault());
        String formatTime = simpleDateFormat.format(new Date(currentTimeMillis));
        return formatTime;
    }

    public static String getCustomTimer(long customTime) {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault());
        String formatTime = simpleDateFormat.format(new Date(customTime));
        return formatTime;
    }

    public static String getTimerWeek() {
        Calendar calendar = Calendar.getInstance();
        int week = calendar.get(Calendar.DAY_OF_WEEK);
        String timeWeek = "星期";
        switch (week) {
            case 2:
                timeWeek = timeWeek + "一";
                break;
            case 3:
                timeWeek = timeWeek + "二";
                break;
            case 4:
                timeWeek = timeWeek + "三";
                break;
            case 5:
                timeWeek = timeWeek + "四";
                break;
            case 6:
                timeWeek = timeWeek + "五";
                break;
            case 7:
                timeWeek = timeWeek + "六";
                break;
            case 1:
                timeWeek = timeWeek + "日";
                break;
        }
        return timeWeek;
    }

}
