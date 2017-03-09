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

public class PostManagementFragment extends FormFragment {

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

        View inflate = ViewsUitls.inflate(R.layout.form_post_management);
        EditTextItem drafter = (EditTextItem) inflate.findViewById(R.id.form_post_management_drafter);
        EditTextItem mainOffice = (EditTextItem) inflate.findViewById(R.id.form_post_management_main_office);
        EditTextItem postType = (EditTextItem) inflate.findViewById(R.id.form_post_management_post_type);
        EditTextItem postNumber = (EditTextItem) inflate.findViewById(R.id.form_post_management_post_number);
        EditTextItem isOpen = (EditTextItem) inflate.findViewById(R.id.form_post_management_is_open);
        EditTextItem postTitle = (EditTextItem) inflate.findViewById(R.id.form_post_management_post_title);
        EditTextItem mainSendOffice = (EditTextItem) inflate.findViewById(R.id.form_post_management_main_send_office);

        ViewsUitls.setWidthFromTargetView(mainOffice.getCustomEditTextLeft(), drafter.getCustomEditTextLeft());

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
        System.out.println("PostManagementFragment-disAgreeOnClick");
    }

    @Override
    public void agreeOnClick(View v) {
        System.out.println("PostManagementFragment-agreeOnClick");
    }

    @Override
    public void submitOnClick(View v) {
        System.out.println("PostManagementFragment-submitOnClick");
    }
}
