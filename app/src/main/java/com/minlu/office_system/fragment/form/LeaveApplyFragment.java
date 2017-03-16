package com.minlu.office_system.fragment.form;

import android.content.DialogInterface;
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
import com.minlu.office_system.fragment.dialog.PromptDialog;
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
        EditTextItem addUpLeaveDays = (EditTextItem) inflate.findViewById(R.id.form_leave_apply_add_up_leave_day_number);
        addUpLeaveDays.setEditText("0 天");
        EditTextItem residueLeaveYears = (EditTextItem) inflate.findViewById(R.id.form_leave_apply_residue_leave_year_number);
        residueLeaveYears.setEditText("0 天");


        mLeaveDayNumber = (EditTextItem) inflate.findViewById(R.id.form_leave_apply_leave_day_number);

        ViewsUitls.setWidthFromTargetView(title.getCustomEditTextLeft(), remark.getCustomEditTextLeft());

        // 类型列表展示
        mLeaveTypeEdit = (EditTextItem) inflate.findViewById(R.id.form_leave_apply_type);
        EditText typeCustomEditTextRight = mLeaveTypeEdit.getCustomEditTextRight();

        setWhichViewShowListPopupWindow(typeCustomEditTextRight, mLeaveType, new ShowListPopupItemClickListener() {
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
        mStartTime.setNowDayOfYearAndTimeOfDay();
        setEditTextOnClickShowTimePicker(mStartTime);// 给mStartTime设置点击弹出时间选择对话框
        mStartTime.setOnSetTextListener(new EditTextTimeSelector.OnSetTextListener() {
            @Override
            public void onSetText() {
                setLeaveDayNumberText();
            }
        });// 设置 当mStartTime修改了文本输入框内的文本时的 监听事件

        mEndTime = (EditTextTimeSelector) inflate.findViewById(R.id.form_leave_apply_end_time);
        mEndTime.setNowDayOfYearAndTimeOfDay();
        setEditTextOnClickShowTimePicker(mEndTime);
        mEndTime.setOnSetTextListener(new EditTextTimeSelector.OnSetTextListener() {
            @Override
            public void onSetText() {
                setLeaveDayNumberText();
            }
        });


        setLeaveDayNumberText();// 第一此显示界面对的时候就需要设置文本了
    }

    /*给开始时间和结束时间之间的  请假天数  的输入文本框设置具体的文本*/
    private void setLeaveDayNumberText() {
        if (mStartTime != null && mEndTime != null) {
            // TODO 计算请假天数的时候,需要注意时间格式
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
    }

    @Override
    public void agreeOnClick(View v) {
    }

    @Override
    public void submitOnClick(View v) {
        PromptDialog promptDialog = new PromptDialog(new PromptDialog.OnSureButtonClick() {
            @Override
            public void onSureClick(DialogInterface dialog, int id) {
                System.out.println("LeaveApplyFragment-submitOnClick");
            }
        }, "是否将请假申请进行提交处理 !");
        promptDialog.show(getActivity().getSupportFragmentManager(), "LeaveApplySubmit");
    }
}
