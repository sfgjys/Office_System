package com.minlu.office_system.fragment.form.formPremise;

import com.minlu.office_system.R;
import com.minlu.office_system.StringsFiled;
import com.minlu.office_system.fragment.form.AssetsManagementFragment;
import com.minlu.office_system.fragment.form.BusManagementFragment;
import com.minlu.office_system.fragment.form.BusRequestFragment;
import com.minlu.office_system.fragment.form.LeaveApplyFragment;
import com.minlu.office_system.fragment.form.LeaveManagementFragment;
import com.minlu.office_system.fragment.form.MeetingOrderFragment;
import com.minlu.office_system.fragment.form.NoticeInformFragment;
import com.minlu.office_system.fragment.form.OtherItemFragment;
import com.minlu.office_system.fragment.form.PlanSummaryFragment;
import com.minlu.office_system.fragment.form.PostManagementFragment;
import com.minlu.office_system.fragment.form.RecordManagementFragment;
import com.minlu.office_system.fragment.form.WorkMonthlyReportFragment;

public enum AllForms {

    /*
    * 主页中item的文本名字，主页中item的图片所在资源id，表单Fragment的class，在表单Fragment界面中显示的是什么按钮，打开表单Fragment时的Tag
    *   打开表单Fragment时使用的替换id布局是不是ScrollView, 请求列表时的参数ProcessId，
    * */
    RECORD_MANAGEMENT("收文管理", R.mipmap.home_page_icon1, RecordManagementFragment.class, StringsFiled.NO_SHOW_BUTTON,
            StringsFiled.RecordManagementFragment_TAG, true, StringsFiled.RecordManagement_ProcessId, StringsFiled.HOME_TO_LIST_SHOW_LIST),
    POST_MANAGEMENT("发文管理", R.mipmap.home_page_icon2, PostManagementFragment.class, StringsFiled.SHOW_IS_AGREE_BUTTON,
            StringsFiled.PostManagementFragment_TAG, true, StringsFiled.PostManagement_ProcessId, StringsFiled.HOME_TO_LIST_SHOW_LIST),
    LEAVE_MANAGEMENT("请假管理", R.mipmap.home_page_icon3, LeaveManagementFragment.class, StringsFiled.SHOW_IS_AGREE_BUTTON,
            StringsFiled.LeaveManagementFragment_TAG, true, StringsFiled.Leave_ProcessId, StringsFiled.HOME_TO_LIST_SHOW_LIST),
    BUS_MANAGEMENT("车辆管理", R.mipmap.home_page_icon4, BusManagementFragment.class, StringsFiled.SHOW_IS_AGREE_BUTTON,
            StringsFiled.BusManagementFragment_TAG, true, StringsFiled.Bus_ProcessId, StringsFiled.HOME_TO_LIST_SHOW_LIST),
    PLAN_SUMMARY("计划总结", R.mipmap.home_page_icon5, PlanSummaryFragment.class, StringsFiled.NO_SHOW_BUTTON,
            StringsFiled.PlanSummaryFragment_TAG, true, StringsFiled.PlanSummary_ProcessId, StringsFiled.HOME_TO_LIST_SHOW_LIST),
    ASSETS_MANAGEMENT("资产管理", R.mipmap.home_page_icon6, AssetsManagementFragment.class, StringsFiled.SHOW_IS_AGREE_BUTTON,
            StringsFiled.AssetsManagementFragment_TAG, true, StringsFiled.No_Need_ProcessId, StringsFiled.HOME_TO_LIST_SHOW_NULL),
    WORK_MONTHLY_REPORT("工作月报", R.mipmap.home_page_icon7, WorkMonthlyReportFragment.class, StringsFiled.SHOW_IS_AGREE_BUTTON,
            StringsFiled.WorkMonthlyReportFragment_TAG, true, StringsFiled.WorkMonthlyReport_ProcessId, StringsFiled.HOME_TO_LIST_SHOW_LIST),
    MEETING_ORDER("会议预约", R.mipmap.home_page_icon8, MeetingOrderFragment.class, StringsFiled.SHOW_SUBMIT_BUTTON,
            StringsFiled.MeetingOrderFragment_TAG, true, StringsFiled.No_Need_ProcessId, StringsFiled.HOME_TO_LIST_SHOW_NULL),
    LEAVE_REQUEST("请假申请", R.mipmap.home_page_icon9, LeaveApplyFragment.class, StringsFiled.SHOW_SUBMIT_BUTTON,
            StringsFiled.LeaveApplyFragment_TAG, true, StringsFiled.No_Need_ProcessId, StringsFiled.HOME_TO_LIST_SHOW_FORM),
    BUS_REQUEST("车辆申请", R.mipmap.home_page_icon10, BusRequestFragment.class, StringsFiled.SHOW_SUBMIT_BUTTON,
            StringsFiled.BusRequestFragment_TAG, true, StringsFiled.No_Need_ProcessId, StringsFiled.HOME_TO_LIST_SHOW_FORM),
    OTHER_ITEM("其他栏目", R.mipmap.home_page_icon11, OtherItemFragment.class, StringsFiled.SHOW_SUBMIT_BUTTON,
            StringsFiled.OtherItemFragment_TAG, true, StringsFiled.No_Need_ProcessId, StringsFiled.HOME_TO_LIST_SHOW_NULL),
    NOTICE_INFORM("公告通知", R.mipmap.home_page_icon11, NoticeInformFragment.class, StringsFiled.NO_SHOW_BUTTON,
            StringsFiled.NoticeInformFragment_TAG, false, StringsFiled.No_Need_ProcessId, StringsFiled.HOME_TO_LIST_SHOW_FORM);

    private String formName;
    private int formIconId;
    private Class<?> formClassName;
    private int showWhichButton;
    private String fragmentTAG;
    private boolean addFragmentIsUseScroll;
    private String getListParam;
    private int homeToListType;

    AllForms(String formName, int formIconId, Class<?> formClassName, int showWhichButton, String fragmentTAG, boolean addFragmentIsUseScroll, String getListParam, int homeToListType) {
        this.formName = formName;
        this.formIconId = formIconId;
        this.formClassName = formClassName;
        this.showWhichButton = showWhichButton;
        this.fragmentTAG = fragmentTAG;
        this.addFragmentIsUseScroll = addFragmentIsUseScroll;
        this.getListParam = getListParam;
        this.homeToListType = homeToListType;
    }

    public String getFormName() {
        return formName;
    }

    public int getFormIconId() {
        return formIconId;
    }

    public Class<?> getFormClassName() {
        return formClassName;
    }

    public int getShowWhichButton() {
        return showWhichButton;
    }

    public String getFragmentTAG() {
        return fragmentTAG;
    }

    public boolean isAddFragmentIsUseScroll() {
        return addFragmentIsUseScroll;
    }

    public String getGetListParam() {
        return getListParam;
    }

    public int getHomeToListType() {
        return homeToListType;
    }
}
