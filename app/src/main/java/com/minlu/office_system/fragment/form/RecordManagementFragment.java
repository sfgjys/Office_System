package com.minlu.office_system.fragment.form;

import android.view.View;
import android.widget.DatePicker;
import android.widget.TimePicker;

import com.minlu.baselibrary.base.ContentPage;
import com.minlu.baselibrary.util.ViewsUitls;
import com.minlu.office_system.R;
import com.minlu.office_system.activity.FormActivity;
import com.minlu.office_system.customview.EditTextItem;
import com.minlu.office_system.fragment.form.formPremise.FormFragment;
import com.minlu.office_system.fragment.time.DatePickerFragment;
import com.minlu.office_system.fragment.time.TimePickerFragment;

import java.io.IOException;
import java.util.ArrayList;

import okhttp3.Response;

/**
 * Created by user on 2017/3/7.
 */

public class RecordManagementFragment extends FormFragment {

    private ArrayList<String> excessive;
    private int mYear;
    private int mMonth;
    private int mDayOfMonth;
    private int mHourOfDay;
    private int mMinute;
    private EditTextItem mSuperiorTextDay;

    @Override
    protected void onSubClassOnCreateView() {
        loadDataAndRefresh();
    }

    @Override
    protected View onCreateSuccessView() {
        // 因为本fragment是通过R.id.sv_replace_form控件replace开启的，但是R.id.sv_replace_form控件是居中属性，所以再次我们要使得居中属性去除
        FormActivity formActivity = (FormActivity) getContext();
        if (formActivity != null) {
            formActivity.setScrollViewNoGravity();
        }

        View inflate = ViewsUitls.inflate(R.layout.form_record_management);

        initView(inflate);

        return inflate;
    }

    private void initView(View inflate) {
        EditTextItem superiorTextTitle = (EditTextItem) inflate.findViewById(R.id.form_record_management_title);
        EditTextItem superiorTextNumber = (EditTextItem) inflate.findViewById(R.id.form_record_management_number);
        EditTextItem superiorTextUnit = (EditTextItem) inflate.findViewById(R.id.form_record_management_unit);
        mSuperiorTextDay = (EditTextItem) inflate.findViewById(R.id.form_record_management_day);
        mSuperiorTextDay.getCustomEditTextRight().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showTimeSelectorDialog();
            }
        });

        EditTextItem superiorTextDetails = (EditTextItem) inflate.findViewById(R.id.form_record_management_details);
        EditTextItem superiorTextRemark = (EditTextItem) inflate.findViewById(R.id.form_record_management_remark);

        ViewsUitls.setWidthFromTargetView(superiorTextTitle.getCustomEditTextLeft(), superiorTextRemark.getCustomEditTextLeft());
    }

    /* 显示时间选择器 */
    private void showTimeSelectorDialog() {
        showDatePickerDialog(new DatePickerFragment.SetDateListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                mYear = year;
                mMonth = month;
                mDayOfMonth = dayOfMonth;
                showTimePickerDialog(new TimePickerFragment.SetTimeListener() {
                    @Override
                    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                        mHourOfDay = hourOfDay;
                        mMinute = minute;
                        mSuperiorTextDay.setEditText(mYear + "-" + mMonth + "-" + mDayOfMonth + " " + mHourOfDay + ":" + mMinute);
                    }
                });
            }
        });
    }

    @Override
    protected ContentPage.ResultState onLoad() {

        Response response = requestFormListItemDetail();

        if (response != null && response.isSuccessful()) {
            try {
                String mResultList = response.body().string();
                System.out.println();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }


        excessive = new ArrayList<>();
        excessive.add("excessive");
        return chat(excessive);
    }

    @Override
    public void disAgreeOnClick(View v) {
        System.out.println("RecordManagementFragment-disAgreeOnClick");
    }

    @Override
    public void agreeOnClick(View v) {
        System.out.println("RecordManagementFragment-agreeOnClick");
    }

    @Override
    public void submitOnClick(View v) {
        System.out.println("RecordManagementFragment-submitOnClick");
    }
}
