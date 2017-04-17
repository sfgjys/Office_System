package com.minlu.office_system;

/**
 * Created by user on 2017/3/29.
 */

public class IpFiled {

    private final static String ip = "http://192.168.1.31:8080/MJOA/";

    public final static String LOGIN = ip + "phone/moblogin.html";

    public final static String NOTICE_LIST = ip + "phone/noticeList.html";

    public final static String NOTICE_DETAIL = ip + "phone/noticeDetail.html";

    // 请假审批（管理），车辆审批（管理），计划总结，收文管理，发文管理，工作月报
    public final static String MANY_MANAGE_LIST = ip + "phone/getUserTask.html";

    // 请假申请前提
    public final static String LEAVE_APPLY_PREMISE = ip + "phone/leaveApply.html";
    // 请假申请前提后的真正请求请假
    public final static String LEAVE_APPLY_SUBMIT = ip + "phone/leaveProcess.html";
    // 车辆申请的前提
    public final static String BUS_REQUEST_APPLY_PREMISE = ip + "phone/tocarApply.html";
    // 真正的车辆申请
    public final static String BUS_REQUEST_APPLY_SUBMIT = ip + "phone/carStartProcess.html";

    public final static String FORM_LIST_ITEM_DETAIL = ip + "phone/recdocPhone.html";

    // 工作月报详情接口
    public final static String WORK_MONTHLY_REPORT_DETAIL = ip + "phone/bmgzView.html";

    public final static String REQUEST_USER_LIST = ip + "phone/getUserList.html";

    public final static String DOWNLOAD_ACCESSORY = ip + "upload/download1.html";
}
