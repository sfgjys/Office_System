package com.minlu.office_system;

/**
 * Created by user on 2017/3/1.
 */

public class StringsFiled {

    public static final String IS_AUTO_LOGIN = "is_auto_login";

    public static final String LOGIN_USER = "login_user";

    /*
    * 跳转至表单界面(FromActivity)时的Intent存储数据的Key
    * */
    public static final String TO_FORM_SHOW_WHICH_BUTTON = "to_form_show_which_button";
    public static final String TO_FORM_SHOW_FORM_FRAGMENT = "to_form_show_form_fragment";
    public static final String TO_FORM_SHOW_FORM_FRAGMENT_TAG = "to_form_show_form_fragment_tag";
    public static final String TO_FORM_SHOW_IS_USE_SCROLL = "to_form_show_is_use_scroll";

    /*
    * 打开表单Fragment的Tag
    * */
    public static final String RecordManagementFragment_TAG = "RecordManagement";
    public static final String PostManagementFragment_TAG = "PostManagement";
    public static final String LeaveManagementFragment_TAG = "LeaveManagement";
    public static final String BusManagementFragment_TAG = "BusManagement";
    public static final String PlanSummaryFragment_TAG = "PlanSummary";
    public static final String AssetsManagementFragment_TAG = "AssetsManagement";
    public static final String WorkMonthlyReportFragment_TAG = "WorkMonthlyReport";
    public static final String MeetingOrderFragment_TAG = "MeetingOrder";
    public static final String LeaveApplyFragment_TAG = "LeaveApply";
    public static final String BusRequestFragment_TAG = "BusRequest";
    public static final String OtherItemFragment_TAG = "OtherItem";
    public static final String NoticeInformFragment_TAG = "NoticeInformFragment";

    /*
    * 区别显示是否同意按钮还是提交按钮
    * */
    public final static int NO_SHOW_BUTTON = 0;
    public final static int SHOW_IS_AGREE_BUTTON = 1;
    public final static int SHOW_SUBMIT_BUTTON = 2;

}
