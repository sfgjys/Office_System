package com.minlu.baselibrary.util;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class TimeTool {


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

    public static String getTimerWeek(Date date, String oneOrTwo) {
        Calendar calendar = Calendar.getInstance();
        if (date != null) {
            calendar.setTime(date);
        }
        int week = calendar.get(Calendar.DAY_OF_WEEK);
        String timeWeek = oneOrTwo;
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

    public static long getDayLongTime() {
        Calendar calendar = Calendar.getInstance();
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH) + 1;
        int day_of_month = calendar.get(Calendar.DAY_OF_MONTH);
        return textTimeToLongTime(year + "-" + month + "-" + day_of_month, "yyyy-MM-dd");
    }

    /**
     * @param textTime       想要转换为Long时间的文本时间
     * @param textTimeFormat 文本时间的格式 例如:"yyyy-MM-dd HH:mm:ss" ss可省略
     * @return 返回-1代表方法中转换获得的Date对象为null(转换失败)
     */
    public static long textTimeToLongTime(String textTime, String textTimeFormat) {
        // 时间格式转换对象
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat(textTimeFormat, Locale.getDefault());
        Date date = null;
        try {
            date = simpleDateFormat.parse(textTime);// 调用parse方法将文本时间转换为Long时间
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return (date != null) ? date.getTime() : -1;
    }

    /**
     * @param longTime       想要转换为文本时间的Long时间
     * @param textTimeFormat 文本时间的格式 例如:"yyyy-MM-dd HH:mm:ss" ss可省略
     */
    public static String longTimeToTextTime(long longTime, String textTimeFormat) {
        // 时间格式转换对象
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat(textTimeFormat, Locale.getDefault());
        return simpleDateFormat.format(new Date(longTime));
    }

    /**
     * @param startTime      开始时间
     * @param endTime        结束时间
     * @param textTimeFormat 文本时间转换为long时间
     * @return 返回值是float数组, 角标0是时间差值的天数, 角标1是时间差值的小时
     */
    public static float[] getBaseStringOfTimeDifferenceValue(String startTime, String endTime, String textTimeFormat) {

        long longEndTime = TimeTool.textTimeToLongTime(endTime, textTimeFormat);// 结束时间的long时间
        long longStartTime = TimeTool.textTimeToLongTime(startTime, textTimeFormat);// 开始时间的long时间

        return getBaseLongOfTimeDifferenceValue(longEndTime, longStartTime);
    }

    /**
     * @param longEndTime   long类型的开始时间
     * @param longStartTime long类型的结束时间
     * @return 返回值是float数组, 角标0是时间差值的天数, 角标1是时间差值的小时
     */
    public static float[] getBaseLongOfTimeDifferenceValue(long longEndTime, long longStartTime) {
        int dayOfTimeDifferenceValue = 0;// 开始时间与结束时间差了多少整数天数
        float hourOfTimeDifferenceValue = 0;// 开始时间与结束时间差了多少小时(去除了上面的整数天数了)
        int allDay = 0;// 将dayOfTimeDifferenceValue天数加一(除了hourOfTimeDifferenceValue为0时，dayOfTimeDifferenceValue就不用加一了)
        if (longEndTime > longStartTime) {// 结束时间的long时间  大于  开始时间的long时间

            float timeDifferenceValueHour = ((float) (longEndTime - longStartTime) / 60000) / 60;// 用longEndTime和longStartTime计算差值,获得的值单位为小时
            timeDifferenceValueHour = ((float) Math.round(timeDifferenceValueHour * 100) / 100);// 将上面算出的值,精确到小数点后两位

            if (timeDifferenceValueHour >= 24) { // 如果时间差值大于24小时
                int timeDifferenceValueDay = (int) (timeDifferenceValueHour / 24);// 将单位为小时的时间差值除以24获取天数
                timeDifferenceValueHour = timeDifferenceValueHour - timeDifferenceValueDay * 24;// 通过上行的天数获取一天内的小时时间差值
                timeDifferenceValueHour = ((float) Math.round(timeDifferenceValueHour * 100) / 100);// 将上面算出的值,精确到小数点后两位

                dayOfTimeDifferenceValue = timeDifferenceValueDay;
                hourOfTimeDifferenceValue = timeDifferenceValueHour;
                if (hourOfTimeDifferenceValue != 0) {
                    allDay = dayOfTimeDifferenceValue + 1;
                } else {
                    allDay = dayOfTimeDifferenceValue;
                }
            } else {
                hourOfTimeDifferenceValue = timeDifferenceValueHour;
                if (hourOfTimeDifferenceValue != 0) {
                    allDay = dayOfTimeDifferenceValue + 1;
                }
            }
        }
        return new float[]{dayOfTimeDifferenceValue, hourOfTimeDifferenceValue, allDay};
    }
}
