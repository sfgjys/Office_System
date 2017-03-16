package com.minlu.office_system.fragment.form;

import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;

import com.minlu.baselibrary.base.ContentPage;
import com.minlu.baselibrary.util.TimeTool;
import com.minlu.baselibrary.util.ViewsUitls;
import com.minlu.office_system.R;
import com.minlu.office_system.activity.FormActivity;
import com.minlu.office_system.customview.EditTextItem;
import com.minlu.office_system.customview.EditTextTimeSelector;
import com.minlu.office_system.fragment.form.formPremise.FormFragment;

import java.util.ArrayList;
import java.util.List;


public class LeaveApplyFragment extends FormFragment {

    private List<String> mLeaveType;
    private EditTextItem mLeaveTypeEdit;
    private EditTextItem mLeaveDayNumber;
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
        EditTextItem remark = (EditTextItem) inflate.findViewById(R.id.form_leave_apply_remark);
        mLeaveDayNumber = (EditTextItem) inflate.findViewById(R.id.form_leave_apply_leave_day_number);

        ViewsUitls.setWidthFromTargetView(title.getCustomEditTextLeft(), remark.getCustomEditTextLeft());

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
        }, getActivity());

        // 时间选择对话框展示
        mStartTime = (EditTextTimeSelector) inflate.findViewById(R.id.form_leave_apply_start_time);
        mStartTime.setDayOfYearAndTimeOfDay();
        setEditTextOnClickShowTimePicker(mStartTime);
        mStartTime.setOnSetTextListener(new EditTextTimeSelector.OnSetTextListener() {
            @Override
            public void onSetText() {
                setLeaveDayNumberText();
            }
        });
        mEndTime = (EditTextTimeSelector) inflate.findViewById(R.id.form_leave_apply_end_time);
        mEndTime.setDayOfYearAndTimeOfDay();
        setEditTextOnClickShowTimePicker(mEndTime);
        mEndTime.setOnSetTextListener(new EditTextTimeSelector.OnSetTextListener() {
            @Override
            public void onSetText() {
                setLeaveDayNumberText();
            }
        });

        setLeaveDayNumberText();
    }

    private void setLeaveDayNumberText() {
        if (mStartTime != null && mEndTime != null) {
            float[] timeDifferenceValue = TimeTool.getBaseStringOfTimeDifferenceValue(mStartTime.getDayOrTimeText(), mEndTime.getDayOrTimeText(), "yyyy-MM-dd HH:mm");
            mLeaveDayNumber.setEditText((int) timeDifferenceValue[0] + " 天 " + timeDifferenceValue[1] + " 小时 , 共 " + (int) timeDifferenceValue[2] + " 天");
        }
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
