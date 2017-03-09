package com.minlu.office_system.fragment.form;

import android.view.View;

import com.minlu.baselibrary.base.ContentPage;
import com.minlu.baselibrary.util.ViewsUitls;
import com.minlu.office_system.R;
import com.minlu.office_system.activity.FormActivity;
import com.minlu.office_system.customview.EditTextItem;
import com.minlu.office_system.fragment.form.formPremise.FormFragment;

import java.util.ArrayList;

/**
 * Created by user on 2017/3/7.
 */

public class RecordManagementFragment extends FormFragment {

    private ArrayList<String> excessive;

    @Override
    protected void onSubClassOnCreateView() {
        loadDataAndRefresh();
    }

    @Override
    protected View onCreateSuccessView() {

        FormActivity formActivity = (FormActivity) getActivity();
        formActivity.setScrollViewNoGravity();

        View inflate = ViewsUitls.inflate(R.layout.form_record_management);

        EditTextItem superiorTextTitle = (EditTextItem) inflate.findViewById(R.id.form_record_management_title);
        EditTextItem superiorTextNumber = (EditTextItem) inflate.findViewById(R.id.form_record_management_number);
        EditTextItem superiorTextUnit = (EditTextItem) inflate.findViewById(R.id.form_record_management_unit);
        EditTextItem superiorTextDate = (EditTextItem) inflate.findViewById(R.id.form_record_management_date);
        EditTextItem superiorTextDetails = (EditTextItem) inflate.findViewById(R.id.form_record_management_details);
        EditTextItem superiorTextRemark = (EditTextItem) inflate.findViewById(R.id.form_record_management_remark);

        ViewsUitls.setWidthFromTargetView(superiorTextTitle.getCustomEditTextLeft(), superiorTextRemark.getCustomEditTextLeft());

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
