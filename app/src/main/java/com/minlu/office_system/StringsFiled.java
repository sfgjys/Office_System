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
    public static final String HTML_DETAIL_CODE = "html_detail_code";


    public static final String HOME_PAGE_TO_FORM_LIST_POSITION = "HOME_PAGE_TO_FORM_LIST_POSITION";

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


    // processid为固定值
    public final static String Leave_ProcessId = "5ba62e2b649b493d8462b96c5a3e76ce";// 请假审批
    public final static String Bus_ProcessId = "a24f18f112a2414aae76accbe62a4cc4";// 车辆审批
    public final static String PlanSummary_ProcessId = "0cfc092d8e44454382fd68d5538656bc";// 计划总结
    public final static String RecordManagement_ProcessId = "739ba060ba08406abab4aa1fa0ae6346";// 收文管理
    public final static String PostManagement_ProcessId = "8289579875e346da8ddc563bacf23081";// 发文管理
    public final static String WorkMonthlyReport_ProcessId = "bf79e27c4f4047f0b67ad6e6402c093f";// 工作月报
    public final static String No_Need_ProcessId = "No_Need_ProcessId";// 没有

    public static final String FORM_LIST_TO_FORM_ORDER_ID = "form_list_to_form_order_id";
    public static final String FORM_LIST_TO_FORM_TASK_ID = "form_list_to_form_task_id";
    public static final String FORM_LIST_TO_FORM_PROCESS_ID = "form_list_to_form_process_id";

    /*
    * 区别显示是否同意按钮还是提交按钮
    * */
    public final static int NO_SHOW_BUTTON = 0;
    public final static int SHOW_IS_AGREE_BUTTON = 1;
    public final static int SHOW_SUBMIT_BUTTON = 2;

    public final static int HOME_TO_LIST_SHOW_LIST = 0;
    public final static int HOME_TO_LIST_SHOW_FORM = 1;
    public final static int HOME_TO_LIST_SHOW_NULL = 2;
}
