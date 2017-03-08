package com.minlu.office_system.fragment.form;

import android.support.v7.widget.ListPopupWindow;
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
import java.util.List;

/**
 * Created by user on 2017/3/7.
 */

public class LeaveApplyFragment extends FormFragment {

    private ArrayList<String> excessive;
    private ListPopupWindow mListPop;
    private EditText customEditTextRight;
    private List<String> lists;

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


        EditTextItem editTextItem = (EditTextItem) inflate.findViewById(R.id.et_item_leave_type);
        customEditTextRight = editTextItem.getCustomEditTextRight();

        ArrayList<String> strings = new ArrayList<>();
        strings.add("KHBGS");
        strings.add("AKFBA");
        strings.add("FTRUH");


        showListPopupWindow(customEditTextRight, strings, new ShowListPopupItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                setBackGroundDarkColor(1f);
            }

            @Override
            public void onAnchorViewClick(View v) {
                setBackGroundDarkColor(0.7f);
            }
        });

        return inflate;
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
}
