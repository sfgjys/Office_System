package com.minlu.office_system.fragment.form;

import android.content.DialogInterface;
import android.view.View;

import com.minlu.baselibrary.base.ContentPage;
import com.minlu.baselibrary.util.StringUtils;
import com.minlu.baselibrary.util.ViewsUitls;
import com.minlu.office_system.R;
import com.minlu.office_system.activity.FormActivity;
import com.minlu.office_system.customview.EditTextItem;
import com.minlu.office_system.fragment.dialog.PromptDialog;
import com.minlu.office_system.fragment.dialog.SelectNextUserDialog;
import com.minlu.office_system.fragment.form.formPremise.FormFragment;

import org.json.JSONObject;

import java.util.ArrayList;

import okhttp3.Response;

/**
 * Created by user on 2017/3/7.
 */

public class RecordManagementFragment extends FormFragment {

    private ArrayList<String> excessive;
    private int mYear;
    private int mMonth;
    private int mDayOfMonth;
    private int mHourOfDay;
    private int mMinute;
    private EditTextItem mApproveIdea;
    private String mTitle = "空";
    private String mTextNumber = "空";
    private String mTextUnit = "空";
    private String mTextTime = "空";

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

        View inflate = ViewsUitls.inflate(R.layout.form_record_management);

        initView(inflate);

        return inflate;
    }

    private void initView(View inflate) {
        EditTextItem superiorTextTitle = (EditTextItem) inflate.findViewById(R.id.form_record_management_title);
        superiorTextTitle.setEditText(mTitle);
        EditTextItem superiorTextNumber = (EditTextItem) inflate.findViewById(R.id.form_record_management_number);
        superiorTextNumber.setEditText(mTextNumber);
        EditTextItem superiorTextUnit = (EditTextItem) inflate.findViewById(R.id.form_record_management_unit);
        superiorTextUnit.setEditText(mTextUnit);
        EditTextItem superiorTextDay = (EditTextItem) inflate.findViewById(R.id.form_record_management_day);
        superiorTextDay.setEditText(mTextTime);

        EditTextItem superiorTextDetails = (EditTextItem) inflate.findViewById(R.id.form_record_management_details);

        mApproveIdea = (EditTextItem) inflate.findViewById(R.id.form_record_management_approve_idea);
    }

    @Override
    protected ContentPage.ResultState onLoad() {

        Response response = requestFormListItemDetail();
        if (response != null && response.isSuccessful()) {
            try {
                String resultList = response.body().string();
                if (StringUtils.interentIsNormal(resultList)) {
                    JSONObject jsonObject = new JSONObject(resultList);
                    if (jsonObject.has("TITLE")) {// 有标题字段，说明返回的数据正常
                        excessive = new ArrayList<>();
                        excessive.add("excessive");// 给excessive创建实例，并添加元素，让界面走onCreateSuccessView()方法

                        mTitle = jsonObject.optString("TITLE");
                        mTextNumber = jsonObject.optString("CALL") + "( " + jsonObject.optString("NUM") + " )号";
                        mTextUnit = jsonObject.optString("ORG_NAME");
                        mTextTime = jsonObject.optString("REC_TIME");

                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return chat(excessive);
    }

    @Override
    public void disAgreeOnClick(View v) {
        PromptDialog promptDialog = new PromptDialog(new PromptDialog.OnSureButtonClick() {
            @Override
            public void onSureClick(DialogInterface dialog, int id) {
                System.out.println("RecordManagementFragment-disAgreeOnClick");
            }
        }, "是否不同意该收文签收 !");
        promptDialog.show(getActivity().getSupportFragmentManager(), "RecordManagementDisAgree");
    }

    @Override
    public void agreeOnClick(View v) {
        SelectNextUserDialog selectNextUserDialog = new SelectNextUserDialog(new SelectNextUserDialog.OnSureButtonClick() {
            @Override
            public void onSureClick(DialogInterface dialog, int id) {
                System.out.println("RecordManagementFragment-agreeOnClick");
            }
        });
        selectNextUserDialog.show(getActivity().getSupportFragmentManager(), "RecordManagementAgree");
    }

    @Override
    public void submitOnClick(View v) {
        System.out.println("RecordManagementFragment-submitOnClick");
    }
}
