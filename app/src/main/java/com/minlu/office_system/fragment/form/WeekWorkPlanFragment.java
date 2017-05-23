package com.minlu.office_system.fragment.form;

import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;

import com.minlu.baselibrary.base.ContentPage;
import com.minlu.baselibrary.util.StringUtils;
import com.minlu.baselibrary.util.ViewsUitls;
import com.minlu.office_system.R;
import com.minlu.office_system.bean.WeekWorkEachData;
import com.minlu.office_system.customview.WeekWorkPlanItem;
import com.minlu.office_system.fragment.form.formPremise.FormFragment;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by user on 2017/5/18.
 */

public class WeekWorkPlanFragment extends FormFragment {

    private List<WeekWorkEachData> mWeekWorkPlanData;

    @Override
    protected void onSubClassOnCreateView() {
        loadDataAndRefresh();
    }

    @Override
    protected View onCreateSuccessView(Bundle savedInstanceState) {
        View inflate = ViewsUitls.inflate(R.layout.form_week_work_plan);

        LinearLayout linearLayout = (LinearLayout) inflate.findViewById(R.id.form_week_work_plan_parent_add_item);

        for (int i = 0; i < mWeekWorkPlanData.size(); i++) {
            WeekWorkPlanItem weekWorkPlanItem = new WeekWorkPlanItem(getActivity());
            // 删除点击事件
            weekWorkPlanItem.setOnDeleteButtonClick(new WeekWorkPlanItem.OnDeleteButtonClick() {
                @Override
                public void onDeleteClick(View v) {

                }
            });
            // 开启选择时间对话框
            weekWorkPlanItem.giveStartAndEndTimeSetOnClick(getActivity());


            if (StringUtils.isEmpty(mWeekWorkPlanData.get(i).getOrganization())) {
                weekWorkPlanItem.getLlOrganizationName().setVisibility(View.GONE);
            } else {
                weekWorkPlanItem.getLlOrganizationName().setVisibility(View.VISIBLE);
            }
            weekWorkPlanItem.getItemTopTitleView().setText("工作" + i);
            weekWorkPlanItem.getStartTimeView().setText(mWeekWorkPlanData.get(i).getStartTime());
            weekWorkPlanItem.getEndTimeView().setText(mWeekWorkPlanData.get(i).getEndTime());
            weekWorkPlanItem.getWeekView().setText(mWeekWorkPlanData.get(i).getWeek());
            weekWorkPlanItem.getWorkSiteView().setText(mWeekWorkPlanData.get(i).getWorkSite());
            weekWorkPlanItem.getWorkContentView().setText(mWeekWorkPlanData.get(i).getWorkContent());
            weekWorkPlanItem.getAttendLeadView().setText(mWeekWorkPlanData.get(i).getAttendLead());
            weekWorkPlanItem.getParticipantsView().setText(mWeekWorkPlanData.get(i).getParticipants());
            weekWorkPlanItem.getOrganizationNameView().setText(mWeekWorkPlanData.get(i).getOrganization());

            linearLayout.addView(weekWorkPlanItem);
        }


        return inflate;
    }

    @Override
    protected ContentPage.ResultState onLoad() {
        mWeekWorkPlanData = new ArrayList<>();
        for (int i = 0; i < 9; i++) {
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

    }
}
