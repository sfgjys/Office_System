package com.minlu.office_system.fragment.form.formPremise;

import com.minlu.office_system.R;
import com.minlu.office_system.StringsFiled;
import com.minlu.office_system.fragment.form.AssetsManagementFragment;
import com.minlu.office_system.fragment.form.BusManagementFragment;
import com.minlu.office_system.fragment.form.BusRequestFragment;
import com.minlu.office_system.fragment.form.LeaveApplyFragment;
import com.minlu.office_system.fragment.form.LeaveManagementFragment;
import com.minlu.office_system.fragment.form.MeetingOrderFragment;
import com.minlu.office_system.fragment.form.OtherItemFragment;
import com.minlu.office_system.fragment.form.PlanSummaryFragment;
import com.minlu.office_system.fragment.form.PostManagementFragment;
import com.minlu.office_system.fragment.form.RecordManagementFragment;
import com.minlu.office_system.fragment.form.WorkMonthlyReportFragment;

public enum AllForms {

    /*
    * 主页中item的文本名字，主页中item的图片所在资源id，表单Fragment的class，在表单Fragment界面中显示的是什么按钮，打开表单Fragment时的Tag
    * */
    RECORD_MANAGEMENT("收文管理", R.mipmap.home_page_icon1, RecordManagementFragment.class, StringsFiled.SHOW_IS_AGREE_BUTTON, StringsFiled.RecordManagementFragment_TAG),
    POST_MANAGEMENT("发文管理", R.mipmap.home_page_icon2, PostManagementFragment.class, StringsFiled.SHOW_IS_AGREE_BUTTON, StringsFiled.PostManagementFragment_TAG),
    LEAVE_MANAGEMENT("请假管理", R.mipmap.home_page_icon3, LeaveManagementFragment.class, StringsFiled.SHOW_IS_AGREE_BUTTON, StringsFiled.LeaveManagementFragment_TAG),
    BUS_MANAGEMENT("车辆管理", R.mipmap.home_page_icon4, BusManagementFragment.class, StringsFiled.SHOW_IS_AGREE_BUTTON, StringsFiled.BusManagementFragment_TAG),
    PLAN_SUMMARY("计划总结", R.mipmap.home_page_icon5, PlanSummaryFragment.class, StringsFiled.SHOW_SUBMIT_BUTTON, StringsFiled.PlanSummaryFragment_TAG),
    ASSETS_MANAGEMENT("资产管理", R.mipmap.home_page_icon6, AssetsManagementFragment.class, StringsFiled.SHOW_IS_AGREE_BUTTON, StringsFiled.AssetsManagementFragment_TAG),
    WORK_MONTHLY_REPORT("工作月报", R.mipmap.home_page_icon7, WorkMonthlyReportFragment.class, StringsFiled.SHOW_SUBMIT_BUTTON, StringsFiled.WorkMonthlyReportFragment_TAG),
    MEETING_ORDER("会议预约", R.mipmap.home_page_icon8, MeetingOrderFragment.class, StringsFiled.SHOW_SUBMIT_BUTTON, StringsFiled.MeetingOrderFragment_TAG),
    LEAVE_REQUEST("请假申请", R.mipmap.home_page_icon9, LeaveApplyFragment.class, StringsFiled.SHOW_SUBMIT_BUTTON, StringsFiled.LeaveApplyFragment_TAG),
    BUS_REQUEST("车辆申请", R.mipmap.home_page_icon10, BusRequestFragment.class, StringsFiled.SHOW_SUBMIT_BUTTON, StringsFiled.BusRequestFragment_TAG),
    OTHER_ITEM("其他栏目", R.mipmap.home_page_icon11, OtherItemFragment.class, StringsFiled.SHOW_SUBMIT_BUTTON, StringsFiled.OtherItemFragment_TAG);

    private String formName;
    private int formIconId;
    private Class<?> formClassName;
    private int showWhichButton;
    private String fragmentTAG;

    AllForms(String formName, int formIconId, Class<?> formClassName, int showWhichButton, String fragmentTAG) {
        this.formName = formName;
        this.formIconId = formIconId;
        this.formClassName = formClassName;
        this.showWhichButton = showWhichButton;
        this.fragmentTAG = fragmentTAG;
    }

    public String getFormName() {
        return formName;
    }

    public void setFormName(String formName) {
        this.formName = formName;
    }

    public int getFormIconId() {
        return formIconId;
    }

    public void setFormIconId(int formIconId) {
        this.formIconId = formIconId;
    }

    public Class<?> getFormClassName() {
        return formClassName;
    }

    public void setFormClassName(Class<?> formClassName) {
        this.formClassName = formClassName;
    }

    public int getShowWhichButton() {
        return showWhichButton;
    }

    public void setShowWhichButton(int showWhichButton) {
        this.showWhichButton = showWhichButton;
    }

    public String getFragmentTAG() {
        return fragmentTAG;
    }

    public void setFragmentTAG(String fragmentTAG) {
        this.fragmentTAG = fragmentTAG;
    }
}
