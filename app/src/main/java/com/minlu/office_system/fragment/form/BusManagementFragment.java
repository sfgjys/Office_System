package com.minlu.office_system.fragment.form;

import android.content.DialogInterface;
import android.view.View;

import com.minlu.baselibrary.base.ContentPage;
import com.minlu.baselibrary.util.ViewsUitls;
import com.minlu.office_system.R;
import com.minlu.office_system.activity.FormActivity;
import com.minlu.office_system.customview.EditTextItem;
import com.minlu.office_system.fragment.dialog.PromptDialog;
import com.minlu.office_system.fragment.form.formPremise.FormFragment;

import java.util.ArrayList;

/**
 * Created by user on 2017/3/7.
 */
public class BusManagementFragment extends FormFragment {

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

        View inflate = ViewsUitls.inflate(R.layout.form_bus_management);

        initView(inflate);

        return inflate;
    }

    private void initView(View inflate) {
        EditTextItem title = (EditTextItem) inflate.findViewById(R.id.form_bus_management_title);
        title.setEditText("出差");
        EditTextItem office = (EditTextItem) inflate.findViewById(R.id.form_bus_management_office);
        office.setEditText("宣传部");
        EditTextItem busNumber = (EditTextItem) inflate.findViewById(R.id.form_bus_management_bus_number);
        busNumber.setEditText("苏D G23098F");

        EditTextItem startTime = (EditTextItem) inflate.findViewById(R.id.form_bus_management_start_time);
        startTime.setEditText("2017-6-26");

        EditTextItem destination = (EditTextItem) inflate.findViewById(R.id.form_bus_management_destination);
        destination.setEditText("湖北省武汉市洪山区珞喻路129号---武汉大学(信息学部)");

        EditTextItem cause = (EditTextItem) inflate.findViewById(R.id.form_bus_management_cause);
        cause.setEditText("本来就因为有事情才会出差的——带着目的性的出差，而不是出去了才想起应该做什么。出差都是为了公司的事务，有时候可能是谈什么项目、有时候是找某个客户处理什么事情，可能每一次的出差都带着不同的目的。本来就因为有事情才会出差的——带着目的性的出差，而不是出去了才想起应该做什么。出差都是为了公司的事务，有时候可能是谈什么项目、有时候是找某个客户处理什么事情，可能每一次的出差都带着不同的目的。本来就因为有事情才会出差的——带着目的性的出差，而不是出去了才想起应该做什么。出差都是为了公司的事务，有时候可能是谈什么项目、有时候是找某个客户处理什么事情，可能每一次的出差都带着不同的目的。");

        ViewsUitls.setWidthFromTargetView(title.getCustomEditTextLeft(), busNumber.getCustomEditTextLeft());
        ViewsUitls.setWidthFromTargetView(title.getCustomEditTextLeft(), destination.getCustomEditTextLeft());
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
                System.out.println("BusManagementFragment-disAgreeOnClick");
            }
        }, "是否不同意该用车请求 !");
        promptDialog.show(getActivity().getSupportFragmentManager(), "BusManagementFragment");
    }

    @Override
    public void agreeOnClick(View v) {
        PromptDialog promptDialog = new PromptDialog(new PromptDialog.OnSureButtonClick() {
            @Override
            public void onSureClick(DialogInterface dialog, int id) {
                System.out.println("BusManagementFragment-agreeOnClick");
            }
        }, "是否同意该用车请求 !");
        promptDialog.show(getActivity().getSupportFragmentManager(), "BusManagementFragment");
    }

    @Override
    public void submitOnClick(View v) {
        System.out.println("BusManagementFragment-submitOnClick");
    }
}