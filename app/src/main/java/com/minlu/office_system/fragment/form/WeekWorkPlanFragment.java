package com.minlu.office_system.fragment.form;

import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.ScrollView;

import com.minlu.baselibrary.base.ContentPage;
import com.minlu.baselibrary.util.ToastUtil;
import com.minlu.baselibrary.util.ViewsUitls;
import com.minlu.office_system.R;
import com.minlu.office_system.bean.CanSelectAttendLead;
import com.minlu.office_system.bean.WeekWorkEachData;
import com.minlu.office_system.customview.WeekWorkPlanItem;
import com.minlu.office_system.fragment.form.formPremise.FormFragment;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;
import java.util.TimerTask;

public class WeekWorkPlanFragment extends FormFragment {

    private List<WeekWorkEachData> mWeekWorkPlanData;
    private List<CanSelectAttendLead> mCanSelectAttendLeads;
    private LinearLayout mAddWeekWorkPlanItem;
    private Handler handler;

    @Override
    protected void onSubClassOnCreateView() {
        loadDataAndRefresh();
    }

    @Override
    protected View onCreateSuccessView(Bundle savedInstanceState) {
        View inflate = ViewsUitls.inflate(R.layout.form_week_work_plan);

        mAddWeekWorkPlanItem = (LinearLayout) inflate.findViewById(R.id.form_week_work_plan_parent_add_item);

        final ScrollView scrollView = (ScrollView) inflate.findViewById(R.id.form_week_work_plan_scroll_view);
        View workPlanAddButton = inflate.findViewById(R.id.tv_form_week_work_plan_add_button);

        workPlanAddButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // 添加新工作计划
                mWeekWorkPlanData.add(new WeekWorkEachData("", "", "", "", "", "", "", ""));
                notifyDataChangeRefreshView();

                if (handler == null) {
                    handler = new Handler();
                }
                handler.postDelayed(new TimerTask() {
                    @Override
                    public void run() {
                        scrollView.fullScroll(ScrollView.FOCUS_DOWN);//滚动到底部
//                        scrollView.fullScroll(ScrollView.FOCUS_UP);//滚动到顶部
                    }
                }, 200);
            }
        });

        // 显示进入本界面时的工作计划
        notifyDataChangeRefreshView();

        return inflate;
    }

    private void notifyDataChangeRefreshView() {
        mAddWeekWorkPlanItem.removeAllViews();
        for (int i = 0; i < mWeekWorkPlanData.size(); i++) {

            // 创建表格控件并将数据传递进去
            WeekWorkPlanItem weekWorkPlanItem = new WeekWorkPlanItem(getActivity(), mWeekWorkPlanData.get(i));
            // 删除点击事件
            final int position = i;
            weekWorkPlanItem.setOnDeleteButtonClick(new WeekWorkPlanItem.OnDeleteButtonClick() {
                int willDeletePosition = position;

                @Override
                public void onDeleteClick(View v) {
                    if (mWeekWorkPlanData.size() > 1) {
                        // 删除指定工作计划
                        mWeekWorkPlanData.remove(willDeletePosition);
                        notifyDataChangeRefreshView();
                    } else {
                        ToastUtil.showToast(getContext(), "最后一个工作计划不可以删除");
                    }
                }
            });

            // 设置可选出席领导的数据集合
            weekWorkPlanItem.setCanSelectPersonData(mCanSelectAttendLeads);

            mAddWeekWorkPlanItem.addView(weekWorkPlanItem);
        }
    }

    @Override
    protected ContentPage.ResultState onLoad() {

        try {
            JSONArray jsonArray = new JSONArray(testData);

            mCanSelectAttendLeads = new ArrayList<>();
            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject jsonObject = jsonArray.optJSONObject(i);

                CanSelectAttendLead canSelectAttendLead = new CanSelectAttendLead();
                canSelectAttendLead.setDepartmentName(jsonObject.optString("departmentName"));

                List<String> leadName = new ArrayList<>();
                JSONArray leadNames = jsonObject.optJSONArray("leadName");
                for (int j = 0; j < leadNames.length(); j++) {
                    leadName.add(leadNames.optString(j));
                }
                canSelectAttendLead.setLeadName(leadName);

                mCanSelectAttendLeads.add(canSelectAttendLead);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }

        mWeekWorkPlanData = new ArrayList<>();
        for (int i = 0; i < 1; i++) {
            WeekWorkEachData weekWorkEachData = new WeekWorkEachData("", "", "", "", "", "", "", "");
            mWeekWorkPlanData.add(weekWorkEachData);
        }
        return chat(mWeekWorkPlanData);
    }

    @Override
    public void disAgreeOnClick(View v) {

    }

    @Override
    public void agreeOnClick(View v) {

    }

    @Override
    public void submitOnClick(View v) {
        System.out.println();
    }


    private String testData = "[\n" +
            "  {\n" +
            "    \"departmentName\": \"局领导\",\n" +
            "    \"leadName\": [\n" +
            "      \"陈向群\",\n" +
            "      \"张家林\",\n" +
            "      \"高于花\"\n" +
            "    ]\n" +
            "  },\n" +
            "  {\n" +
            "    \"departmentName\": \"法援处\",\n" +
            "    \"leadName\": [\n" +
            "      \"张颖\",\n" +
            "      \"马健\",\n" +
            "      \"曹荣\"\n" +
            "    ]\n" +
            "  },\n" +
            "  {\n" +
            "    \"departmentName\": \"法规处\",\n" +
            "    \"leadName\": [\n" +
            "      \"李所归\",\n" +
            "      \"陈问问\",\n" +
            "      \"张丽\"\n" +
            "    ]\n" +
            "  },\n" +
            "  {\n" +
            "    \"departmentName\": \"机关党委\",\n" +
            "    \"leadName\": [\n" +
            "      \"王进\",\n" +
            "      \"张浩\",\n" +
            "      \"及第\"\n" +
            "    ]\n" +
            "  },\n" +
            "  {\n" +
            "    \"departmentName\": \"宣教处\",\n" +
            "    \"leadName\": [\n" +
            "      \"汪小菲\",\n" +
            "      \"王小芳\",\n" +
            "      \"尹晋华\"\n" +
            "    ]\n" +
            "  },\n" +
            "  {\n" +
            "    \"departmentName\": \"组织处\",\n" +
            "    \"leadName\": [\n" +
            "      \"周丽萍\",\n" +
            "      \"王永深\",\n" +
            "      \"朱新发\"\n" +
            "    ]\n" +
            "  }\n" +
            "]";
}
