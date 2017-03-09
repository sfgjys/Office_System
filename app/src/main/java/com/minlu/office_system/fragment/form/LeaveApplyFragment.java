package com.minlu.office_system.fragment.form;

import android.view.View;
import android.widget.AdapterView;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TimePicker;

import com.minlu.baselibrary.base.ContentPage;
import com.minlu.baselibrary.util.ViewsUitls;
import com.minlu.office_system.R;
import com.minlu.office_system.activity.FormActivity;
import com.minlu.office_system.customview.EditTextItem;
import com.minlu.office_system.fragment.form.formPremise.FormFragment;
import com.minlu.office_system.fragment.time.DatePickerFragment;
import com.minlu.office_system.fragment.time.TimePickerFragment;

import java.util.ArrayList;


public class LeaveApplyFragment extends FormFragment implements FormFragment.ShowListPopupItemClickListener {

    private ArrayList<String> excessive;

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

        View inflate = ViewsUitls.inflate(R.layout.form_leave_apply);

        initView(inflate);

        return inflate;
    }

    private void initView(View inflate) {
        EditTextItem title = (EditTextItem) inflate.findViewById(R.id.form_leave_apply_title);

        EditTextItem type = (EditTextItem) inflate.findViewById(R.id.form_leave_apply_type);
        EditText typeCustomEditTextRight = type.getCustomEditTextRight();
        ArrayList<String> strings = new ArrayList<>();
        strings.add("事假");
        strings.add("病假");
        strings.add("婚假");
        strings.add("产假");
        strings.add("年假");
        strings.add("休假");
        showListPopupWindow(typeCustomEditTextRight, strings, this);


        EditTextItem startTime = (EditTextItem) inflate.findViewById(R.id.form_leave_apply_start_time);
        startTime.getCustomEditTextRight().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDatePickerDialog(new DatePickerFragment.SetDateListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {

                    }
                });
            }
        });
        EditTextItem endTime = (EditTextItem) inflate.findViewById(R.id.form_leave_apply_end_time);
        endTime.getCustomEditTextRight().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDatePickerDialog(new DatePickerFragment.SetDateListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {

                    }
                });
            }
        });

        EditTextItem remark = (EditTextItem) inflate.findViewById(R.id.form_leave_apply_remark);

        ViewsUitls.setWidthFromTargetView(title.getCustomEditTextLeft(), remark.getCustomEditTextLeft());
    }

    @Override
    protected ContentPage.ResultState onLoad() {
        excessive = new ArrayList<>();
        excessive.add("excessive");
        return chat(excessive);
    }

    @Override
    public void disAgreeOnClick(View v) {
        System.out.println("LeaveApplyFragment-disAgreeOnClick");
        showDatePickerDialog(new DatePickerFragment.SetDateListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                System.out.println(year + "-" + month + "-" + dayOfMonth);
            }
        });
    }

    @Override
    public void agreeOnClick(View v) {
        System.out.println("LeaveApplyFragment-agreeOnClick");
        showTimePickerDialog(new TimePickerFragment.SetTimeListener() {
            @Override
            public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                System.out.println(hourOfDay + "-" + minute);
            }
        });
    }

    @Override
    public void submitOnClick(View v) {
        System.out.println("LeaveApplyFragment-submitOnClick");
    }


    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
    }

    @Override
    public void onAnchorViewClick(View v) {
        setBackGroundDarkColor(0.7f);
    }

    @Override
    public void onListPopupDismiss() {
        setBackGroundDarkColor(1.0f);
    }
}
