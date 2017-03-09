package com.minlu.office_system.fragment.form;

import android.view.View;
import android.widget.DatePicker;

import com.minlu.baselibrary.base.ContentPage;
import com.minlu.baselibrary.util.ViewsUitls;
import com.minlu.office_system.R;
import com.minlu.office_system.activity.FormActivity;
import com.minlu.office_system.customview.EditTextItem;
import com.minlu.office_system.fragment.form.formPremise.FormFragment;
import com.minlu.office_system.fragment.time.DatePickerFragment;

import java.util.ArrayList;

/**
 * Created by user on 2017/3/7.
 */
public class PlanSummaryFragment extends FormFragment {

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

        View inflate = ViewsUitls.inflate(R.layout.form_plan_summary);
        EditTextItem year = (EditTextItem) inflate.findViewById(R.id.form_plan_summary_year);
        EditTextItem quarter = (EditTextItem) inflate.findViewById(R.id.form_plan_summary_quarter);
        EditTextItem userName = (EditTextItem) inflate.findViewById(R.id.form_plan_summary_user_name);
        EditTextItem job = (EditTextItem) inflate.findViewById(R.id.form_plan_summary_job);
        EditTextItem department = (EditTextItem) inflate.findViewById(R.id.form_plan_summary_department);
        EditTextItem time = (EditTextItem) inflate.findViewById(R.id.form_plan_summary_time);
        time.getCustomEditTextRight().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDatePickerDialog(new DatePickerFragment.SetDateListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {

                    }
                });
            }
        });
        EditTextItem workPlan = (EditTextItem) inflate.findViewById(R.id.form_plan_summary_work_plan);


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
        System.out.println("PlanSummaryFragment-disAgreeOnClick");
    }

    @Override
    public void agreeOnClick(View v) {
        System.out.println("PlanSummaryFragment-agreeOnClick");
    }

    @Override
    public void submitOnClick(View v) {
        System.out.println("PlanSummaryFragment-submitOnClick");
    }
}

