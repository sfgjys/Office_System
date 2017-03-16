package com.minlu.office_system.fragment.time;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;
import android.widget.DatePicker;

import java.util.Calendar;

public class DatePickerFragment extends DialogFragment implements DatePickerDialog.OnDateSetListener {

    private DatePickerDialog datePickerDialog;

    private SetDateListener setDateListener;

    public interface SetDateListener {
        void onDateSet(DatePicker view, int year, int month, int dayOfMonth);
    }

    public DatePickerFragment(SetDateListener setDateListener) {
        this.setDateListener = setDateListener;
    }

    public DatePickerFragment() {
        this.setDateListener = null;
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        final Calendar calendar = Calendar.getInstance();
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH);
        int day = calendar.get(Calendar.DAY_OF_MONTH);

        datePickerDialog = new DatePickerDialog(getActivity(), this, year, month, day);
        return datePickerDialog;
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
