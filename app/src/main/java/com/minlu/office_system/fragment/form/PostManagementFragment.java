package com.minlu.office_system.fragment.form;

import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;

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


        final ArrayList<String> busNumber = new ArrayList<>();
        busNumber.add("苏D 23RF124");
        busNumber.add("苏D SE380AS");
        busNumber.add("苏D SD20342");
        busNumber.add("苏D MRE45OI");

        View inflate = ViewsUitls.inflate(R.layout.form_post_management);
        EditTextItem drafter = (EditTextItem) inflate.findViewById(R.id.form_post_management_drafter);
        EditTextItem mainOffice = (EditTextItem) inflate.findViewById(R.id.form_post_management_main_office);
        EditTextItem postType = (EditTextItem) inflate.findViewById(R.id.form_post_management_post_type);
        final EditText postTypeEditText = postType.getCustomEditTextRight();
        showListPopupWindow(postTypeEditText, busNumber, new ShowListPopupItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                postTypeEditText.setText(busNumber.get(position));
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

        EditTextItem postNumber = (EditTextItem) inflate.findViewById(R.id.form_post_management_post_number);
        EditTextItem isOpen = (EditTextItem) inflate.findViewById(R.id.form_post_management_is_open);
        final EditText isOpenEditText = isOpen.getCustomEditTextRight();
        showListPopupWindow(isOpenEditText, busNumber, new ShowListPopupItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                isOpenEditText.setText(busNumber.get(position));
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
