package com.minlu.office_system.fragment.time;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;
import android.widget.DatePicker;

import com.minlu.baselibrary.util.StringUtils;
import com.minlu.baselibrary.util.TimeTool;

import java.util.Calendar;

public class DatePickerFragment extends DialogFragment implements DatePickerDialog.OnDateSetListener {

    private DatePickerDialog datePickerDialog;

    private SetDateListener setDateListener;
    private DatePicker.OnDateChangedListener onDateChangedListener;
    private String dataAreaUpperLimit;
    private String dataAreaLowerLimit;

    public interface SetDateListener {
        void onDateSet(DatePicker view, int year, int month, int dayOfMonth);
    }

    public DatePickerFragment(SetDateListener setDateListener) {
        this.setDateListener = setDateListener;
    }

    public DatePickerFragment() {
        this.setDateListener = null;
    }

    public void setOnDateChangedListener(DatePicker.OnDateChangedListener onDateChangedListener) {
        this.onDateChangedListener = onDateChangedListener;
    }

    /**
     * @param dataAreaUpperLimit 格式要是yyyy-MM-dd
     * @param dataAreaLowerLimit 格式要是yyyy-MM-dd
     */
    public void setDatAreaString(String dataAreaUpperLimit, String dataAreaLowerLimit) {
        this.dataAreaUpperLimit = dataAreaUpperLimit;
        this.dataAreaLowerLimit = dataAreaLowerLimit;
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        final Calendar calendar = Calendar.getInstance();
        final int nowYear = calendar.get(Calendar.YEAR);
        final int nowMonth = calendar.get(Calendar.MONTH);
        final int nowDayOfMonth = calendar.get(Calendar.DAY_OF_MONTH);

        datePickerDialog = new DatePickerDialog(getActivity(), this, nowYear, nowMonth, nowDayOfMonth);

        setTimeAreaLimit(nowYear, nowMonth, nowDayOfMonth);

        return datePickerDialog;
    }

    /**
     * 时间区域限制方法
     *
     * @param nowYear       当前 年
     * @param nowMonth      当前 月
     * @param nowDayOfMonth 当前 日
     */
    public void setTimeAreaLimit(final int nowYear, final int nowMonth, final int nowDayOfMonth) {
        if (onDateChangedListener != null) {// 有自定义的监听就优先使用
            DatePicker datePicker = datePickerDialog.getDatePicker();
            datePicker.init(nowYear, nowMonth, nowDayOfMonth, onDateChangedListener);
        } else {
            if (!StringUtils.isEmpty(dataAreaLowerLimit) && !StringUtils.isEmpty(dataAreaUpperLimit)) {// 没自定义监听就看有没有设置时间区间
                DatePicker datePicker = datePickerDialog.getDatePicker();
                datePicker.init(nowYear, nowMonth, nowDayOfMonth, new DatePicker.OnDateChangedListener() {
                    @Override
                    public void onDateChanged(DatePicker datePicker, int year,
                                              int monthOfYear, int dayOfMonth) {//滑动日期进行对日期的方位进行判断
                        if (isDateAfter(datePicker)) {
                            datePicker.init(nowYear, nowMonth, nowDayOfMonth, this);// 调用datePicker对象的init方法跳回到指定的年月日
                        }
                        if (isDateBefore(datePicker)) {
                            datePicker.init(nowYear, nowMonth, nowDayOfMonth, this);
                        }
                    }

                    private boolean isDateAfter(DatePicker datePicker) {
                        long selectTime = TimeTool.textTimeToLongTime(datePicker.getYear() + "-" + (datePicker.getMonth() + 1) + "-" + datePicker.getDayOfMonth(), "yyyy-MM-dd");
                        long lowerLimitTime = TimeTool.textTimeToLongTime(dataAreaLowerLimit, "yyyy-MM-dd");
                        return selectTime > lowerLimitTime;// datePicker中包含了用户选择的具体年月日，使用这个进行判断是否超出区间
                    }

                    private boolean isDateBefore(DatePicker datePicker1) {
                        long selectTime = TimeTool.textTimeToLongTime(datePicker1.getYear() + "-" + (datePicker1.getMonth() + 1) + "-" + datePicker1.getDayOfMonth(), "yyyy-MM-dd");
                        long upperLimitTime = TimeTool.textTimeToLongTime(dataAreaUpperLimit, "yyyy-MM-dd");
                        return selectTime < upperLimitTime;
                    }
                });
            }
        }
    }

    @Override
    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
        if (setDateListener != null) {
            setDateListener.onDateSet(view, year, month, dayOfMonth);
        }
    }

    @Override
    public void onStart() {
        if (setDateListener == null) {//当展示对话框的时候，如果没有setDateListener监听那就不让对话框显示
            datePickerDialog.cancel();
            datePickerDialog.dismiss();
        }
        super.onStart();
    }
}
