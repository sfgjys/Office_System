package com.minlu.office_system.fragment.time;

import android.app.Dialog;
import android.app.TimePickerDialog;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;
import android.widget.TimePicker;

import java.util.Calendar;

public class TimePickerFragment extends DialogFragment implements TimePickerDialog.OnTimeSetListener {

    public interface SetTimeListener {
        void onTimeSet(TimePicker view, int hourOfDay, int minute);
    }

    private final SetTimeListener setTimeListener;

    public TimePickerFragment(SetTimeListener setTimeListener) {
        this.setTimeListener = setTimeListener;
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        final Calendar c = Calendar.getInstance();
        int hour = c.get(Calendar.HOUR_OF_DAY);
        int minute = c.get(Calendar.MINUTE);
        // 最后一个参数代表 是否是24小时制
        return new TimePickerDialog(getActivity(), this, hour, minute, true); // DateFormat.is24HourFormat(getActivity())
    }

    @Override
    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
        setTimeListener.onTimeSet(view, hourOfDay, minute);
    }
}
