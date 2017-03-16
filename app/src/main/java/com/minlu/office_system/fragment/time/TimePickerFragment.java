package com.minlu.office_system.fragment.time;

import android.app.Dialog;
import android.app.TimePickerDialog;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;
import android.widget.TimePicker;

import java.util.Calendar;

public class TimePickerFragment extends DialogFragment implements TimePickerDialog.OnTimeSetListener {

    private TimePickerDialog timePickerDialog;

    private SetTimeListener setTimeListener;

    public interface SetTimeListener {
        void onTimeSet(TimePicker view, int hourOfDay, int minute);
    }

    public TimePickerFragment(SetTimeListener setTimeListener) {
        this.setTimeListener = setTimeListener;
    }

    public TimePickerFragment() {
        this.setTimeListener = null;
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        final Calendar c = Calendar.getInstance();
        int hour = c.get(Calendar.HOUR_OF_DAY);
        int minute = c.get(Calendar.MINUTE);
        // 最后一个参数代表 是否是24小时制
        // DateFormat.is24HourFormat(getActivity())
        timePickerDialog = new TimePickerDialog(getActivity(), this, hour, minute, true);
        return timePickerDialog;
    }

    @Override
    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
        if (setTimeListener != null) {
            setTimeListener.onTimeSet(view, hourOfDay, minute);
        }
    }

    @Override
    public void onStart() {
        if (setTimeListener == null) {//当展示对话框的时候，如果没有setDateListener监听那就不让对话框显示
            timePickerDialog.cancel();
            timePickerDialog.dismiss();
        }
        super.onStart();
    }
}
