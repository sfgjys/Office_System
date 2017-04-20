package com.minlu.office_system.fragment.form;

import android.content.DialogInterface;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.minlu.baselibrary.base.ContentPage;
import com.minlu.baselibrary.util.SharedPreferencesUtil;
import com.minlu.baselibrary.util.StringUtils;
import com.minlu.baselibrary.util.ViewsUitls;
import com.minlu.office_system.IpFiled;
import com.minlu.office_system.R;
import com.minlu.office_system.StringsFiled;
import com.minlu.office_system.activity.FormActivity;
import com.minlu.office_system.bean.CheckBoxChild;
import com.minlu.office_system.customview.EditTextItem;
import com.minlu.office_system.fragment.dialog.PromptDialog;
import com.minlu.office_system.fragment.form.formPremise.FormFragment;
import com.minlu.office_system.http.OkHttpMethod;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

/**
 * Created by user on 2017/3/7.
 */
public class WorkMonthlyReportFragment extends FormFragment {

    private ArrayList<String> excessive;
    private String mJobSummaryTitleText = "";
    private String mJobPerformanceTitleText = "";
    private String mJobPlanTitleText = "";

    private String mOfficeText = "";
    private String mTimeText = "";

    private String mBranchedPassageSuggest = "";
    private String mOfficeSuggest = "";

    private List<String> mJobSummaryData;
    private List<String> mJobPerformanceData;
    private List<String> mJobPlanData;

    private List<CheckBoxChild> mNextUsers;

    private int mWhichStep;

    private EditTextItem mRect3Idea;
    private EditTextItem mRect4Idea;
    private String mAssignee = "";
    private EditTextItem mApproveIdea;
    private String mAutoOrg = "";
    private String mTaskName;

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

        View inflate = ViewsUitls.inflate(R.layout.form_work_monthly_report);

        initView(inflate);

        return inflate;
    }

    private void initView(View inflate) {
        // 正常显示数据
        TextView office = (TextView) inflate.findViewById(R.id.form_work_monthly_report_office);
        office.setText("填报单位 : " + mOfficeText);
        TextView time = (TextView) inflate.findViewById(R.id.form_work_monthly_report_time);
        time.setText("日期 : " + mTimeText);
        TextView jobSummaryTitle = (TextView) inflate.findViewById(R.id.tv_title_job_summary);
        jobSummaryTitle.setText(mJobSummaryTitleText + "月份工作总结");
        TextView jobPerformanceTitle = (TextView) inflate.findViewById(R.id.tv_title_job_performance);
        jobPerformanceTitle.setText(mJobPerformanceTitleText + "月份工作完成情况");
        TextView jobPlanTitle = (TextView) inflate.findViewById(R.id.tv_title_job_plan);
        jobPlanTitle.setText(mJobPlanTitleText + "月份工作安排");
        LinearLayout jobSummary = (LinearLayout) inflate.findViewById(R.id.ll_work_monthly_report_job_summary);
        addTextToJobLinear(jobSummary, mJobSummaryData);
        LinearLayout jobPerformance = (LinearLayout) inflate.findViewById(R.id.ll_work_monthly_report_job_performance);
        addTextToJobLinear(jobPerformance, mJobPerformanceData);
        LinearLayout jobPlan = (LinearLayout) inflate.findViewById(R.id.ll_work_monthly_report_job_plan);
        addTextToJobLinear(jobPlan, mJobPlanData);


        // 此为用户进行审批意见编辑的控件
        mApproveIdea = (EditTextItem) inflate.findViewById(R.id.form_work_monthly_report_approve_idea);


        // 审批意见有可能有，这个意见只能展示不能编辑
        mRect3Idea = (EditTextItem) inflate.findViewById(R.id.form_work_monthly_report_approve_idea_rect3);
        mRect3Idea.setEditText(mBranchedPassageSuggest);
        mRect4Idea = (EditTextItem) inflate.findViewById(R.id.form_work_monthly_report_approve_idea_rect4);
        mRect4Idea.setEditText(mOfficeSuggest);
        if (StringUtils.isEmpty(mBranchedPassageSuggest) && StringUtils.isEmpty(mOfficeSuggest)) {
            mRect3Idea.setVisibility(View.GONE);
            mRect4Idea.setVisibility(View.GONE);
            mApproveIdea.setPadding(0, ViewsUitls.dpToPx(25), 0, 0);
        } else if (StringUtils.isEmpty(mBranchedPassageSuggest) && !StringUtils.isEmpty(mOfficeSuggest)) {
            mRect3Idea.setVisibility(View.GONE);
            ViewsUitls.setWidthFromTargetView(mRect4Idea.getCustomEditTextLeft(), mApproveIdea.getCustomEditTextLeft());
        } else if (!StringUtils.isEmpty(mBranchedPassageSuggest) && StringUtils.isEmpty(mOfficeSuggest)) {
            mRect4Idea.setVisibility(View.GONE);
            ViewsUitls.setWidthFromTargetView(mRect3Idea.getCustomEditTextLeft(), mApproveIdea.getCustomEditTextLeft());
        } else if (!StringUtils.isEmpty(mBranchedPassageSuggest) && !StringUtils.isEmpty(mOfficeSuggest)) {
            ViewsUitls.setWidthFromTargetView(mRect3Idea.getCustomEditTextLeft(), mRect4Idea.getCustomEditTextLeft());
            ViewsUitls.setWidthFromTargetView(mRect3Idea.getCustomEditTextLeft(), mApproveIdea.getCustomEditTextLeft());
        }
    }

    /* 根据条目数据去添加并显示条目 */
    private void addTextToJobLinear(LinearLayout linearLayout, List<String> data) {
        if (data != null && data.size() > 0) {
            for (int i = 0; i < data.size(); i++) {
                View inflate = ViewsUitls.inflate(R.layout.item_work_monthly_report);
                TextView textView = (TextView) inflate.findViewById(R.id.tv_work_monthly_report_item_detail);
                textView.setText(data.get(i));
                if ((i + 1) == data.size()) {
                    View view = inflate.findViewById(R.id.work_monthly_report_bottom_line);
                    view.setVisibility(View.GONE);
                }
                linearLayout.addView(inflate);
            }
        } else {
            linearLayout.addView(ViewsUitls.inflate(R.layout.item_work_monthly_report_empty));
        }
    }

    @Override
    protected ContentPage.ResultState onLoad() {

        HashMap<String, String> hashMap = new HashMap<>();
        hashMap.put("processId", getProcessIdFromList());
        hashMap.put("orderId", getOrderIdFromList());
        hashMap.put("taskId", getTaskIdFromList());
        hashMap.put("userName", SharedPreferencesUtil.getString(ViewsUitls.getContext(), StringsFiled.LOGIN_USER, ""));
        Response response = OkHttpMethod.synPostRequest(IpFiled.WORK_MONTHLY_REPORT_DETAIL, hashMap);

        if (response != null && response.isSuccessful()) {
            try {
                String resultList = response.body().string();
                if (StringUtils.interentIsNormal(resultList)) {

                    JSONArray jsonArray = new JSONArray(resultList);
                    analyticalData(jsonArray);
                }
                System.out.println();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return chat(excessive);
    }

    /* 解析网络获取的JSON数据 */
    private void analyticalData(JSONArray jsonArray) throws JSONException {
        mJobSummaryData = new ArrayList<>();
        mJobPerformanceData = new ArrayList<>();
        mJobPlanData = new ArrayList<>();

        for (int i = 0; i < jsonArray.length(); i++) {
            JSONObject jsonObject = jsonArray.getJSONObject(i);
            if (jsonObject.has("one")) {// 有one说明是月报详细条目内容
                mJobSummaryData.add(jsonObject.optString("one"));
                mJobPerformanceData.add(jsonObject.optString("two"));
                mJobPlanData.add(jsonObject.optString("three"));
            }
            // 获取标题
            if (jsonObject.has("first")) {
                mJobSummaryTitleText = jsonObject.optString("first");
            }
            if (jsonObject.has("second")) {
                mJobPerformanceTitleText = jsonObject.optString("second");
            }
            if (jsonObject.has("third")) {
                mJobPlanTitleText = jsonObject.optString("third");
            }
            // 获取单位和日期
            if (jsonObject.has("dw")) {
                mOfficeText = jsonObject.optString("dw");
            }
            if (jsonObject.has("year")) {
                mTimeText = jsonObject.optString("year") + "-" + jsonObject.optString("month") + "-" + jsonObject.optString("day");
            }

            // 后面的接口需要到的数据
            if (jsonObject.has("assignee")) {
                mAssignee = jsonObject.optString("assignee");
            }
            if (jsonObject.has("taskName")) {
                mTaskName = jsonObject.optString("taskName");
            }

            // 获取审批建议
            getAllSuggest(new AnalysisJSON() {
                @Override
                public void analysisJSON(JSONObject jsonObject) {
                    if (jsonObject.has("rect3suggest")) {
                        mBranchedPassageSuggest = jsonObject.optString("rect3suggest");
                    }
                    if (jsonObject.has("rect4suggest")) {
                        mOfficeSuggest = jsonObject.optString("rect4suggest");
                    }
                    excessive = new ArrayList<>();
                    excessive.add("excessive");// 给excessive创建实例，并添加元素，让界面走onCreateSuccessView()方法
                }
            });
        }
    }

    @Override
    public void disAgreeOnClick(View v) {
        PromptDialog promptDialog = new PromptDialog(new PromptDialog.OnSureButtonClick() {
            @Override
            public void onSureClick(DialogInterface dialog, int id) {
                officialLeaveApply("", 1);
            }
        }, "是否不同意该工作月报 !");
        promptDialog.show(getActivity().getSupportFragmentManager(), "WorkMonthlyReportDisAgree");
    }

    @Override
    public void agreeOnClick(View v) {
        getNextPersonData(mAssignee, "WorkMonthlyReportManagementAgree_Have_Next", "WorkMonthlyReportManagementAgree_No_Next", new PassNextPersonString() {
            @Override
            public void passNextPersonString(String userList) {
                officialLeaveApply(userList, 0);
            }
        });
    }

    @Override
    public void submitOnClick(View v) {
    }

    private void officialLeaveApply(String userList, int method) {
        HashMap<String, String> hashMap = new HashMap<>();
        hashMap.put("processId", getProcessIdFromList());
        hashMap.put("orderId", getOrderIdFromList());
        hashMap.put("taskId", getTaskIdFromList());
        hashMap.put("taskName", mTaskName);
        hashMap.put("assignee", mAssignee);
        hashMap.put("Method", "" + method);
        hashMap.put("userName", SharedPreferencesUtil.getString(ViewsUitls.getContext(), StringsFiled.LOGIN_USER, ""));
        hashMap.put("userList", userList);

        // 以下为表单上的填写数据
        hashMap.put("suggest", mApproveIdea.getCustomEditTextRight().getText().toString());

        OkHttpMethod.asynPostRequest(IpFiled.SUBMIT_IS_AGREE_WORK_MONTHLY_REPORT, hashMap, new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                showToastToMain("服务器异常，请联系管理员");
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                if (response != null && response.isSuccessful()) {
                    try {
                        String resultList = response.body().string();
                        if ("success".contains(resultList)) {
                            getActivity().finish();
                        } else {
                            showToastToMain("服务器正忙请稍后");
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                } else {
                    showToastToMain("服务器异常，请联系管理员");
                }
            }
        });
    }
}
