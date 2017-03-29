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
    public final static String MANY_MANAGE_LIST= ip + "/phone/getUserTask.html";
}
