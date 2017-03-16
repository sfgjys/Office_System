package com.minlu.office_system.fragment.form;

import android.content.DialogInterface;
import android.view.View;

import com.minlu.baselibrary.base.ContentPage;
import com.minlu.baselibrary.util.ViewsUitls;
import com.minlu.office_system.R;
import com.minlu.office_system.activity.FormActivity;
import com.minlu.office_system.customview.EditTextItem;
import com.minlu.office_system.customview.EditTextTimeSelector;
import com.minlu.office_system.fragment.dialog.PromptDialog;
import com.minlu.office_system.fragment.form.formPremise.FormFragment;

import java.util.ArrayList;

/**
 * Created by user on 2017/3/7.
 */
public class LeaveManagementFragment extends FormFragment {

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

        View inflate = ViewsUitls.inflate(R.layout.form_leave_management);

        EditTextItem addUpLeaveDays = (EditTextItem) inflate.findViewById(R.id.form_leave_management_add_up_leave_day_number);
        addUpLeaveDays.setEditText("5 天");
        EditTextItem leaveDayNumber = (EditTextItem) inflate.findViewById(R.id.form_leave_management_leave_day_number);
        leaveDayNumber.setEditText("1 天 2.5 小时，共 2 天");
        EditTextItem residueLeaveYears = (EditTextItem) inflate.findViewById(R.id.form_leave_management_residue_leave_year_number);
        residueLeaveYears.setEditText("0 天");
        EditTextItem title = (EditTextItem) inflate.findViewById(R.id.form_leave_management_title);
        title.setEditText("全家外出旅游");
        EditTextItem type = (EditTextItem) inflate.findViewById(R.id.form_leave_management_type);
        type.setEditText("事假");
        EditTextItem remark = (EditTextItem) inflate.findViewById(R.id.form_leave_management_remark);
        ViewsUitls.setWidthFromTargetView(title.getCustomEditTextLeft(), remark.getCustomEditTextLeft());
        remark.setEditText("世界那么大，我想去看看！");


        EditTextTimeSelector endTime = (EditTextTimeSelector) inflate.findViewById(R.id.form_leave_management_end_time);
//        endTime.setDayOfYearText();
//        endTime.setTimeOfDayText();
        endTime.setNowDayOfYearAndTimeOfDay();
        EditTextTimeSelector startTime = (EditTextTimeSelector) inflate.findViewById(R.id.form_leave_management_start_time);
//        startTime.setDayOfYearText();
//        startTime.setTimeOfDayText();
        startTime.setNowDayOfYearAndTimeOfDay();
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
        PromptDialog promptDialog = new PromptDialog(new PromptDialog.OnSureButtonClick() {
            @Override
            public void onSureClick(DialogInterface dialog, int id) {
                System.out.println("LeaveManagementFragment-disAgreeOnClick");
            }
        }, "是否不同意该请假 !");
        promptDialog.show(getActivity().getSupportFragmentManager(), "LeaveManagementDisAgree");
    }

    @Override
    public void agreeOnClick(View v) {
        PromptDialog promptDialog = new PromptDialog(new PromptDialog.OnSureButtonClick() {
            @Override
            public void onSureClick(DialogInterface dialog, int id) {
                System.out.println("LeaveManagementFragment-agreeOnClick");
            }
        }, "是否同意该请假 !");
        promptDialog.show(getActivity().getSupportFragmentManager(), "LeaveManagementAgree");
    }

    @Override
    public void submitOnClick(View v) {
    }
}