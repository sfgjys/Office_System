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
public class BusRequestFragment extends FormFragment {

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

        View inflate = ViewsUitls.inflate(R.layout.form_bus_request);
        EditTextItem title = (EditTextItem) inflate.findViewById(R.id.form_bus_request_title);
        EditTextItem office = (EditTextItem) inflate.findViewById(R.id.form_bus_request_office);
        EditTextItem busNumber = (EditTextItem) inflate.findViewById(R.id.form_bus_request_bus_number);
        EditTextItem startTime = (EditTextItem) inflate.findViewById(R.id.form_bus_request_start_time);
        EditTextItem destination = (EditTextItem) inflate.findViewById(R.id.form_bus_request_destination);
        EditTextItem cause = (EditTextItem) inflate.findViewById(R.id.form_bus_request_cause);

        ViewsUitls.setWidthFromTargetView(title.getCustomEditTextLeft(),busNumber.getCustomEditTextLeft());
        ViewsUitls.setWidthFromTargetView(title.getCustomEditTextLeft(),destination.getCustomEditTextLeft());

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
        System.out.println("BusRequestFragment-disAgreeOnClick");
    }

    @Override
    public void agreeOnClick(View v) {
        System.out.println("BusRequestFragment-agreeOnClick");
    }

    @Override
    public void submitOnClick(View v) {
        System.out.println("BusRequestFragment-submitOnClick");
    }
}