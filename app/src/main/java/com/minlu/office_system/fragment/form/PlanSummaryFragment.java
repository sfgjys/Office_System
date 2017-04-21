package com.minlu.office_system.fragment.form;

import android.content.DialogInterface;
import android.view.View;
import android.widget.AdapterView;
import android.widget.DatePicker;

import com.minlu.baselibrary.base.ContentPage;
import com.minlu.baselibrary.util.StringUtils;
import com.minlu.baselibrary.util.ViewsUitls;
import com.minlu.office_system.IpFiled;
import com.minlu.office_system.R;
import com.minlu.office_system.activity.FormActivity;
import com.minlu.office_system.customview.EditTextItem;
import com.minlu.office_system.fragment.dialog.PromptDialog;
import com.minlu.office_system.fragment.form.formPremise.FormFragment;
import com.minlu.office_system.fragment.time.DatePickerFragment;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import okhttp3.Response;

/**
 * Created by user on 2017/3/7.
 */
public class PlanSummaryFragment extends FormFragment {

    private ArrayList<String> excessive;
    private String mYearText = "";
    private String mQuarterText = "";
    private String mUserNameText = "";
    private String mJobText = "";
    private String mDepartmentText = "";
    private String mWorkPlanTimeText = "";
    private String mWorkPlanText = "";
    private String mAssignee = "";
    private String mTaskName = "";
    private EditTextItem mWorkSummaryView;
    private EditTextItem mWorkSummaryTimeView;
    private String mStep = "";
    private String mWorkSummaryText = "";
    private String mWorkPlanIdeaText = "";
    private String mWorkSummaryIdeaText = "";
    private EditTextItem mWorkPlanIdeaView;
    private EditTextItem mWorkSummaryIdeaView;
    private String mTaskBack = "";
    private String mUltimatelyLeadRejectIdea = "";
    private String mWorkSummaryTimeText = "";
    private EditTextItem mUltimatelyLeadIdeaView;

    private EditTextItem mApproveIdea;
    private EditTextItem mWorkPlanTimeView;
    private EditTextItem mWorkPlanView;
    private EditTextItem mYearView;
    private EditTextItem mQuarterView;
    private List<String> mYearData;

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
        // 正常显示数据
        EditTextItem departmentView = (EditTextItem) inflate.findViewById(R.id.form_plan_summary_department);
        departmentView.setEditText(mDepartmentText);// 所在部门

        mWorkPlanView = (EditTextItem) inflate.findViewById(R.id.form_plan_summary_work_plan);
        mWorkPlanView.setEditText(mWorkPlanText);// 工作计划
        mWorkPlanTimeView = (EditTextItem) inflate.findViewById(R.id.form_plan_summary_plan_time);
        mWorkPlanTimeView.setEditText(mWorkPlanTimeText);// 计划时间
        mWorkPlanTimeView.getCustomEditTextRight().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) { // 设置工作计划时间控件可以弹出时间选择对话框
                showDatePickerDialog(new DatePickerFragment.SetDateListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                        mWorkPlanTimeView.setEditText(year + "-" + (month + 1) + "-" + dayOfMonth);
                    }
                });
            }
        });

        EditTextItem userNameView = (EditTextItem) inflate.findViewById(R.id.form_plan_summary_user_name);
        EditTextItem jobView = (EditTextItem) inflate.findViewById(R.id.form_plan_summary_job);
        userNameView.setEditText(mUserNameText);// 姓名
        jobView.setEditText(mJobText);// 职位

        mQuarterView = (EditTextItem) inflate.findViewById(R.id.form_plan_summary_quarter);
        // 设置季度点击后弹出类型选择对话框
        mQuarterView.getCustomEditTextRight().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                System.out.println("=============================================");
            }
        });
        mYearView = (EditTextItem) inflate.findViewById(R.id.form_plan_summary_year);
        // 设置年度控件可以弹出选择年的下拉框
        setWhichViewShowListPopupWindow(mYearView.getCustomEditTextRight(), mYearData, new ShowListPopupItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                mYearView.getCustomEditTextRight().setText(mYearData.get(position));
            }

            @Override
            public void onAnchorViewClick(View v) {
                setBackGroundDarkColor(0.7f);
            }

            @Override
            public void onListPopupDismiss() {
                setBackGroundDarkColor(1.0f);
            }
        }, getActivity());
        mYearView.setEditText(mYearText);// 年度
        mQuarterView.setEditText(mQuarterText);// 季月份

        ViewsUitls.setWidthFromTargetView(departmentView.getCustomEditTextLeft(), mYearView.getCustomEditTextLeft());
        ViewsUitls.setWidthFromTargetView(departmentView.getCustomEditTextLeft(), mQuarterView.getCustomEditTextLeft());
        ViewsUitls.setWidthFromTargetView(departmentView.getCustomEditTextLeft(), userNameView.getCustomEditTextLeft());
        ViewsUitls.setWidthFromTargetView(departmentView.getCustomEditTextLeft(), jobView.getCustomEditTextLeft());


        // 获取不同步骤所要找的控件
        // 对工作计划的处室意见
        mWorkPlanIdeaView = (EditTextItem) inflate.findViewById(R.id.form_plan_summary_lead_idea);
        // 对工作总结的处室评价
        mWorkSummaryIdeaView = (EditTextItem) inflate.findViewById(R.id.form_plan_summary_lead_evaluate);
        // 最终领导审批意见
        mUltimatelyLeadIdeaView = (EditTextItem) inflate.findViewById(R.id.form_plan_summary_lead_criticism);
        // 工作总结
        mWorkSummaryView = (EditTextItem) inflate.findViewById(R.id.form_plan_summary_work_summary);
        // 总结时间
        mWorkSummaryTimeView = (EditTextItem) inflate.findViewById(R.id.form_plan_summary_summary_time);
        mWorkSummaryTimeView.getCustomEditTextRight().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDatePickerDialog(new DatePickerFragment.SetDateListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                        mWorkSummaryTimeView.setEditText(year + "-" + (month + 1) + "-" + dayOfMonth);
                    }
                });
            }
        });

        accordingStepShowView();
    }

    private void accordingStepShowView() {
        if (!StringUtils.isEmpty(mWorkPlanIdeaText)) {// 有工作计划的意见
            mWorkPlanIdeaView.setVisibility(View.VISIBLE);
            mWorkPlanIdeaView.setEditText(mWorkPlanIdeaText);
        }
        if (!StringUtils.isEmpty(mWorkSummaryText)) {// 有工作总结
            mWorkSummaryView.setVisibility(View.VISIBLE);
            mWorkSummaryView.setEditText(mWorkSummaryText);
            mWorkSummaryTimeView.setVisibility(View.VISIBLE);
            mWorkSummaryTimeView.setEditText(mWorkSummaryTimeText);
        }
        if (!StringUtils.isEmpty(mWorkSummaryIdeaText)) {// 有工作总结的意见
            mWorkSummaryIdeaView.setVisibility(View.VISIBLE);
            mWorkSummaryIdeaView.setEditText(mWorkSummaryIdeaText);
        }
        if (!StringUtils.isEmpty(mUltimatelyLeadRejectIdea)) {// 有最终领导的意见
            mUltimatelyLeadIdeaView.setVisibility(View.VISIBLE);
            mUltimatelyLeadIdeaView.setEditText(mUltimatelyLeadRejectIdea);
        }
        switch (Integer.parseInt(mStep)) {
            case 1:
                showEditTextItemCanClick(mYearView);
                showEditTextItemCanClick(mQuarterView);
                showEditTextItemCanClick(mWorkPlanTimeView);
                showEditTextItemDifferentState(mWorkPlanView, "请编写工作计划", "(流程步骤被反驳前的工作计划是) : ");
                mApproveIdea = null;
                break;
            case 2:
                showEditTextItemDifferentState(mWorkPlanIdeaView, "请编写对本工作计划的意见", "(流程步骤被反驳前的工作计划意见是) : ");
                mApproveIdea = mWorkPlanIdeaView;// 最后记得将工作计划意见控件赋值给审批意见便于提交意见
                break;
            case 3:
                showEditTextItemCanClick(mWorkSummaryTimeView);
                showEditTextItemDifferentState(mWorkSummaryView, "请编写对工作计划的总结", "(流程步骤被反驳前的工作总结是) : ");
                mApproveIdea = null;
                break;
            case 4:
                showEditTextItemDifferentState(mWorkSummaryIdeaView, "请编写对本工作总结的评价", "(流程步骤被反驳前的工作总结评价是) : ");
                mApproveIdea = mWorkSummaryIdeaView;
                break;
            case 5:
                showEditTextItemDifferentState(mUltimatelyLeadIdeaView, "请编写对本计划总结的评鉴", "(流程步骤被反驳前的计划总结评鉴是) : ");
                mApproveIdea = mUltimatelyLeadIdeaView;
                break;
        }
    }

    /* 根据流程步骤去设置对应控件的表现 */
    public void showEditTextItemDifferentState(EditTextItem editTextItem, String textEmptyHint, String haveTextHint) {
        // 先获取EditTextItem控件的文本(这个文本是从获取流程所有建议的接口那里解析出来的，有可能有,有可能没有)
        String editTextItemRightText = editTextItem.getCustomEditTextRight().getText().toString();
        // 在设置EditTextItem显示出来，并且可以编辑
        showEditTextItemCanEdit(editTextItem);
        // 设置文本初始值
        editTextItem.setEditText("");
        // 判断EditTextItem控件的文本是否为空，来区分EditTextItem控件是否初次显示
        if (StringUtils.isEmpty(editTextItemRightText)) {
            editTextItem.getCustomEditTextRight().setHint(textEmptyHint);
        } else {
            editTextItem.getCustomEditTextRight().setHint(haveTextHint + editTextItemRightText);
        }
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
                        analyticalData(jsonObject);
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return chat(excessive);
    }

    private void analyticalData(JSONObject jsonObject) {
        // 正常显示数据
        mYearText = jsonObject.optString("YEAR");
        mQuarterText = jsonObject.optString("QUARTER");
        mUserNameText = jsonObject.optString("NAME");
        mJobText = jsonObject.optString("WORK");
        mDepartmentText = jsonObject.optString("SECTION");
        mWorkPlanTimeText = jsonObject.optString("TIME");
        mWorkPlanText = jsonObject.optString("PLAN");

        // 步骤字段
        mStep = jsonObject.optString("STEP");

        // 后面的接口需要到的数据
        mAssignee = jsonObject.optString("ASSIGNEE");
        mTaskName = jsonObject.optString("TASKNAME");

        getAllSuggest(new AnalysisJSON() {

            @Override
            public void analysisJSON(JSONObject jsonObject) {
                if (jsonObject.has("fgldpjmethod")) {// 出现该字段其值一定是-1,代表最终领导驳回
                    mUltimatelyLeadRejectIdea = StringUtils.isEmpty(jsonObject.optString("fgldpjsuggest")) ? ((Integer.parseInt(jsonObject.optString("fgldpjmethod")) == 0) ? "同意 : " : "不同意 : ") : jsonObject.optString("fgldpjsuggest");
                }
                if (jsonObject.has("csldpjmethod")) {   // 第一次step:5 对工作总结的审批,此步骤是最终领导审批
                    mWorkSummaryIdeaText = StringUtils.isEmpty(jsonObject.optString("csldpjsuggest")) ? ((Integer.parseInt(jsonObject.optString("csldpjmethod")) == 0) ? "同意 : " : "不同意 : ") : jsonObject.optString("csldpjsuggest");
                }
                if (jsonObject.has("csldyjmethod")) {     // 第一次step:3 对工作计划的审批,此步骤是编写工作总结内容
                    mWorkPlanIdeaText = StringUtils.isEmpty(jsonObject.optString("csldyjsuggest")) ? ((Integer.parseInt(jsonObject.optString("csldyjmethod")) == 0) ? "同意 : " : "不同意 : ") : jsonObject.optString("csldyjsuggest");
                }
                if (jsonObject.has("gzzjmethod")) {       // 第一次step:4 工作总结的内容,在写工作总结时只有提交其menthod必定是0,此步骤是对工作总结内容进行审批
                    mWorkSummaryText = jsonObject.optString("gzzj");
                    mWorkSummaryTimeText = jsonObject.optString("gzzjtime");
                }

                if (jsonObject.has("taskBack")) {// 最近一次被被驳回的步骤名称
                    mTaskBack = jsonObject.optString("taskBack");
                }
                excessive = new ArrayList<>();
                excessive.add("excessive");// 给excessive创建实例，并添加元素，让界面走onCreateSuccessView()方法
            }
        });

        mYearData = new ArrayList<>();
        for (int i = 0; i < 50; i++) {
            mYearData.add((2000 + i) + "");
        }
    }

    @Override
    public void disAgreeOnClick(View v) {
        PromptDialog promptDialog = new PromptDialog(new PromptDialog.OnSureButtonClick() {
            @Override
            public void onSureClick(DialogInterface dialog, int id) {
                officialPlanSummary("", -1);
            }
        }, "是否不同意该工作计划 !");
        promptDialog.show(getActivity().getSupportFragmentManager(), "RecordManagementDisAgree");
    }

    @Override
    public void agreeOnClick(View v) {
        getNextPersonData(mAssignee, "PlanSummaryManagementAgree_Have_Next", "PlanSummaryManagementAgree_No_Next", "是否同意该计划总结", new PassNextPersonString() {
            @Override
            public void passNextPersonString(String userList) {
                officialPlanSummary(userList, 0);
            }
        });
    }

    @Override
    public void submitOnClick(View v) {
    }


    private void officialPlanSummary(String userList, int method) {
        startLoading();

        HashMap<String, String> hashMap = getUnifiedDataHashMap();

        hashMap.put("taskName", mTaskName);
        hashMap.put("assignee", mAssignee);
        hashMap.put("method", "" + method);
        hashMap.put("userList", userList);

        // 以下为表单上的填写数据
        hashMap.put("suggest", mApproveIdea.getCustomEditTextRight().getText().toString());

        hashMap.put("gzzj", mWorkSummaryView.getCustomEditTextRight().getText().toString());
        hashMap.put("gzzjtime", mWorkSummaryTimeView.getCustomEditTextRight().getText().toString());

        startUltimatelySubmit(IpFiled.SUBMIT_IS_AGREE_PLAN_SUMMARY, hashMap, "success", "服务器异常，请联系管理员", "提交成功");
    }
}

