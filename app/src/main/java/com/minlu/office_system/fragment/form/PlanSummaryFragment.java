package com.minlu.office_system.fragment.form;

import android.content.DialogInterface;
import android.view.View;

import com.minlu.baselibrary.base.ContentPage;
import com.minlu.baselibrary.util.StringUtils;
import com.minlu.baselibrary.util.ViewsUitls;
import com.minlu.office_system.R;
import com.minlu.office_system.activity.FormActivity;
import com.minlu.office_system.bean.CheckBoxChild;
import com.minlu.office_system.customview.EditTextItem;
import com.minlu.office_system.fragment.dialog.OnSureButtonClick;
import com.minlu.office_system.fragment.dialog.PromptDialog;
import com.minlu.office_system.fragment.dialog.SelectNextUserDialog;
import com.minlu.office_system.fragment.form.formPremise.FormFragment;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import okhttp3.Response;

/**
 * Created by user on 2017/3/7.
 */
public class PlanSummaryFragment extends FormFragment {

    private ArrayList<String> excessive;
    private List<CheckBoxChild> mNextUsers;
    private String mYearText;
    private String mQuarterText;
    private String mUserNameText;
    private String mJobText;
    private String mDepartmentText;
    private String mTimeText;
    private String mWorkPlanText;
    private EditTextItem mApproveIdea;

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

        initView(inflate);

        return inflate;
    }

    private void initView(View inflate) {
        EditTextItem year = (EditTextItem) inflate.findViewById(R.id.form_plan_summary_year);
        year.setEditText(mYearText);
        EditTextItem quarter = (EditTextItem) inflate.findViewById(R.id.form_plan_summary_quarter);
        quarter.setEditText(mQuarterText);
        EditTextItem userName = (EditTextItem) inflate.findViewById(R.id.form_plan_summary_user_name);
        userName.setEditText(mUserNameText);
        EditTextItem job = (EditTextItem) inflate.findViewById(R.id.form_plan_summary_job);
        job.setEditText(mJobText);
        EditTextItem department = (EditTextItem) inflate.findViewById(R.id.form_plan_summary_department);
        department.setEditText(mDepartmentText);
        EditTextItem time = (EditTextItem) inflate.findViewById(R.id.form_plan_summary_time);
        time.setEditText(mTimeText);
        EditTextItem workPlan = (EditTextItem) inflate.findViewById(R.id.form_plan_summary_work_plan);
        workPlan.setEditText(mWorkPlanText);

        mApproveIdea = (EditTextItem) inflate.findViewById(R.id.form_plan_summary_approve_idea);
    }

    @Override
    protected ContentPage.ResultState onLoad() {
        Response response = requestFormListItemDetail();
        if (response != null && response.isSuccessful()) {
            try {
                String resultList = response.body().string();
                if (StringUtils.interentIsNormal(resultList)) {
                    JSONObject jsonObject = new JSONObject(resultList);
                    if (jsonObject.has("NAME")) {// 有标题字段，说明返回的数据正常
                        excessive = new ArrayList<>();
                        excessive.add("excessive");// 给excessive创建实例，并添加元素，让界面走onCreateSuccessView()方法

                        mYearText = jsonObject.optString("YEAR");
                        mQuarterText = jsonObject.optString("QUARTER");
                        mUserNameText = jsonObject.optString("NAME");
                        mJobText = jsonObject.optString("WORK");
                        mDepartmentText = jsonObject.optString("SECTION");
                        mTimeText = jsonObject.optString("TIME");
                        mWorkPlanText = jsonObject.optString("PLAN");

                        JSONArray jsonArray = jsonObject.optJSONArray("USERLIST");
                        mNextUsers = new ArrayList<>();
                        for (int i = 0; i < jsonArray.length(); i++) {
                            JSONObject nextUserData = jsonArray.getJSONObject(i);
                            mNextUsers.add(new CheckBoxChild(nextUserData.optString("TRUENAME"), nextUserData.optString("USERNAME"), nextUserData.optString("ORG_INFOR")));
                        }
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
        }, "是否不同意该工作计划 !");
        promptDialog.show(getActivity().getSupportFragmentManager(), "RecordManagementDisAgree");
    }

    @Override
    public void agreeOnClick(View v) {
        SelectNextUserDialog selectNextUserDialog = new SelectNextUserDialog();
        selectNextUserDialog.setCheckBoxTexts(mNextUsers);
        selectNextUserDialog.setOnSureButtonClick(new OnSureButtonClick() {
            @Override
            public void onSureClick(DialogInterface dialog, int id, List<Boolean> isChecks) {
                List<CheckBoxChild> sureUsers = new ArrayList<>();
                // 通过isChecks集合中的选择数据去判断哪些数据选中，并将选中的数据填进sureUsers集合中
                for (int i = 0; i < isChecks.size(); i++) {
                    if (isChecks.get(i)) {
                        sureUsers.add(mNextUsers.get(i));
                    }
                }
                String approveIdea = mApproveIdea.getCustomEditTextRight().getText().toString();
                System.out.println(approveIdea + sureUsers.size());
                // TODO 使用sureUsers集合和审批意见去进行网络请求
            }
        });
        selectNextUserDialog.show(getActivity().getSupportFragmentManager(), "PlanSummaryAgree");
    }

    @Override
    public void submitOnClick(View v) {
        System.out.println("PlanSummaryFragment-submitOnClick");
    }
}

