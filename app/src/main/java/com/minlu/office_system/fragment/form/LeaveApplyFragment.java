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
import com.minlu.office_system.customview.EditTextTimeSelector;
import com.minlu.office_system.fragment.form.formPremise.FormFragment;
import com.minlu.office_system.fragment.time.DatePickerFragment;
import com.minlu.office_system.fragment.time.TimePickerFragment;

import java.util.ArrayList;
import java.util.List;


public class LeaveApplyFragment extends FormFragment {

    private List<String> mLeaveType;
    private EditTextItem mLeaveTypeEdit;
    private EditTextTimeSelector mStartTime;
    private EditTextTimeSelector mEndTime;

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

        // 类型列表展示
        mLeaveTypeEdit = (EditTextItem) inflate.findViewById(R.id.form_leave_apply_type);
        EditText typeCustomEditTextRight = mLeaveTypeEdit.getCustomEditTextRight();
        showListPopupWindow(typeCustomEditTextRight, mLeaveType, new ShowListPopupItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                mLeaveTypeEdit.setEditText(mLeaveType.get(position));
            }

            @Override
            public void onAnchorViewClick(View v) {
                setBackGroundDarkColor(0.6f);
            }

            @Override
            public void onListPopupDismiss() {
                setBackGroundDarkColor(1.0f);
            }
        });

        mStartTime = (EditTextTimeSelector) inflate.findViewById(R.id.form_leave_apply_start_time);
        showAndSetTimeText(mStartTime);
        mEndTime = (EditTextTimeSelector) inflate.findViewById(R.id.form_leave_apply_end_time);
        showAndSetTimeText(mEndTime);

        EditTextItem remark = (EditTextItem) inflate.findViewById(R.id.form_leave_apply_remark);

        ViewsUitls.setWidthFromTargetView(title.getCustomEditTextLeft(), remark.getCustomEditTextLeft());
    }

    private void showAndSetTimeText(final EditTextTimeSelector editTextTimeSelector) {
        editTextTimeSelector.setDayOrTimeOnClickListener(new EditTextTimeSelector.DayOrTimeOnClickListener() {
            @Override
            public void onTimeClick(View v) {
                showTimePickerDialog(new TimePickerFragment.SetTimeListener() {
                    @Override
                    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                        editTextTimeSelector.setTimeOfDayText(hourOfDay + ":" + minute);
                    }
                });
            }

            @Override
            public void onDayClick(View v) {
                showDatePickerDialog(new DatePickerFragment.SetDateListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                        editTextTimeSelector.setDayOfYearText(year + "-" + (month + 1) + "-" + dayOfMonth);
                    }
                });
            }
        });
    }

    @Override
    protected ContentPage.ResultState onLoad() {
        mLeaveType = new ArrayList<>();
        mLeaveType.add("事假");
        mLeaveType.add("病假");
        mLeaveType.add("婚假");
        mLeaveType.add("产假");
        mLeaveType.add("年假");
        mLeaveType.add("休假");
        return chat(mLeaveType);
    }

    @Override
    public void disAgreeOnClick(View v) {
        System.out.println("LeaveApplyFragment-disAgreeOnClick");
    }

    @Override
    public void agreeOnClick(View v) {
        System.out.println("LeaveApplyFragment-agreeOnClick");
    }

    @Override
    public void submitOnClick(View v) {
        System.out.println("LeaveApplyFragment-submitOnClick");
    }
}
