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
import com.minlu.office_system.fragment.dialog.OnSureButtonClick;
import com.minlu.office_system.fragment.dialog.PromptDialog;
import com.minlu.office_system.fragment.dialog.SelectNextUserDialog;
import com.minlu.office_system.fragment.form.formPremise.FormFragment;
import com.minlu.office_system.http.OkHttpMethod;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import okhttp3.Response;

/**
 * Created by user on 2017/3/7.
 */
public class WorkMonthlyReportFragment extends FormFragment {

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
    private EditTextItem mRect5Idea;

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

        mRect3Idea = (EditTextItem) inflate.findViewById(R.id.form_work_monthly_report_approve_idea_rect3);
        mRect3Idea.setEditText(mBranchedPassageSuggest);
        mRect4Idea = (EditTextItem) inflate.findViewById(R.id.form_work_monthly_report_approve_idea_rect4);
        mRect4Idea.setEditText(mOfficeSuggest);
        mRect5Idea = (EditTextItem) inflate.findViewById(R.id.form_work_monthly_report_approve_idea_rect5);
        ViewsUitls.setWidthFromTargetView(mRect3Idea.getCustomEditTextLeft(), mRect4Idea.getCustomEditTextLeft());
        ViewsUitls.setWidthFromTargetView(mRect3Idea.getCustomEditTextLeft(), mRect5Idea.getCustomEditTextLeft());

        showWhichSuggest();
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

    /* 根据步骤来选择显示哪几个审批意见 */
    private void showWhichSuggest() {
        switch (mWhichStep) {
            case 0:
                mRect4Idea.setVisibility(View.GONE);
                mRect5Idea.setVisibility(View.GONE);
                break;
            case 1:
                mRect3Idea.getCustomEditTextRight().setFocusableInTouchMode(false);
                mRect3Idea.getCustomEditTextRight().setFocusable(false);
                mRect5Idea.setVisibility(View.GONE);
                break;
            case 2:
                mRect3Idea.getCustomEditTextRight().setFocusableInTouchMode(false);
                mRect3Idea.getCustomEditTextRight().setFocusable(false);
                mRect4Idea.getCustomEditTextRight().setFocusableInTouchMode(false);
                mRect4Idea.getCustomEditTextRight().setFocusable(false);
                break;
        }
    }

    @Override
    protected ContentPage.ResultState onLoad() {

        HashMap<String, String> hashMap = new HashMap<>();
        hashMap.put("orderId", SharedPreferencesUtil.getString(ViewsUitls.getContext(), StringsFiled.FORM_LIST_TO_FORM_ORDER_ID, ""));
        Response response = OkHttpMethod.synPostRequest(IpFiled.WORK_MONTHLY_REPORT_DETAIL, hashMap);

        if (response != null && response.isSuccessful()) {
            try {
                String resultList = response.body().string();
                if (StringUtils.interentIsNormal(resultList)) {

                    JSONArray jsonArray = new JSONArray(resultList);

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
                        // 获取意见
                        if (jsonObject.has("rect3suggest")) {
                            mBranchedPassageSuggest = jsonObject.optString("rect3suggest");
                        }
                        if (jsonObject.has("rect4suggest")) {
                            mOfficeSuggest = jsonObject.optString("rect4suggest");
                        }
                        // 用来判断第几步骤了
                        if (!jsonObject.has("rect3method") && !jsonObject.has("rect4method")) {
                            mWhichStep = 0;
                        } else if (jsonObject.has("rect3method") && !jsonObject.has("rect4method")) {
                            mWhichStep = 1;
                        } else if (jsonObject.has("rect3method") && jsonObject.has("rect4method")) {
                            mWhichStep = 2;
                        }
                    }
                }
                System.out.println();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return chat(mJobSummaryData);
    }

    @Override
    public void disAgreeOnClick(View v) {
        PromptDialog promptDialog = new PromptDialog(new PromptDialog.OnSureButtonClick() {
            @Override
            public void onSureClick(DialogInterface dialog, int id) {
                System.out.println("WorkMonthlyReportFragment-disAgreeOnClick");
            }
        }, "是否不同意该工作月报 !");
        promptDialog.show(getActivity().getSupportFragmentManager(), "WorkMonthlyReportDisAgree");
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

                String approveIdea="";
                switch (mWhichStep){
                    case 0:
                        approveIdea=mRect3Idea.getCustomEditTextRight().getText().toString();
                        break;
                    case 1:
                        approveIdea=mRect4Idea.getCustomEditTextRight().getText().toString();
                        break;
                    case 2:
                        approveIdea=mRect5Idea.getCustomEditTextRight().getText().toString();
                        break;
                }
                System.out.println(approveIdea + sureUsers.size());
                // TODO 使用sureUsers集合和审批意见去进行网络请求
            }
        });
        selectNextUserDialog.show(getActivity().getSupportFragmentManager(), "WorkMonthlyReportAgree");
    }

    @Override
    public void submitOnClick(View v) {
        System.out.println("WorkMonthlyReportFragment-submitOnClick");
    }
}
