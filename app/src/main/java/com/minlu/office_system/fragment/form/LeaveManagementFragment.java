package com.minlu.office_system.fragment.form;

import android.view.View;

import com.minlu.baselibrary.base.ContentPage;
import com.minlu.baselibrary.util.ViewsUitls;
import com.minlu.office_system.R;
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
        View inflate = ViewsUitls.inflate(R.layout.form_leave_apply);

        return inflate;
    }

    @Override
    protected ContentPage.ResultState onLoad() {
        excessive = new ArrayList<>();
//        excessive.add("excessive");
        return chat(excessive);
    }

    @Override
    public void disAgreeOnClick(View v) {
        System.out.println("LeaveManagementFragment-disAgreeOnClick");
    }

    @Override
    public void agreeOnClick(View v) {
        System.out.println("LeaveManagementFragment-agreeOnClick");
    }

    @Override
    public void submitOnClick(View v) {
        System.out.println("LeaveManagementFragment-submitOnClick");
    }
}