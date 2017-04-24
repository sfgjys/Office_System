package com.minlu.office_system;

/**
 * Created by user on 2017/3/29.
 */

public class IpFiled {

    private final static String ip = "http://192.168.1.31:8080/MJOA/";// 127.0.0.1

    // ***********************************************************************************************************************************

    // 登录接口
    public final static String LOGIN = ip + "phone/moblogin.html";

    // ***********************************************************************************************************************************

    // 公告列表
    public final static String NOTICE_LIST = ip + "phone/noticeList.html";
    // 点击公告列表进入详情
    public final static String NOTICE_DETAIL = ip + "phone/noticeDetail.html";

    // ***********************************************************************************************************************************

    /*  ***六个功能模块列表*** */
    // 请假审批（管理），车辆审批（管理），计划总结，收文管理，发文管理，工作月报六个模块获取列表的接口
    public final static String MANY_MANAGE_LIST = ip + "phone/getUserTask.html";            //--------------------6

    // ***********************************************************************************************************************************

    /* ***六个功能模块详情,注意工作月报的详情单独一个,请假申请和车辆申请分别是单独的接口*** */
    // 点击请假审批（管理），车辆审批（管理），计划总结，收文管理，发文管理五个功能模块的列表后进入对应的详情界面获取详情界面应该展示的内容
    public final static String FORM_LIST_ITEM_DETAIL = ip + "phone/recdocPhone.html";       //--------------------5

    // 点击工作月报列表后进入工作月报详情界面请求获取详情数据
    public final static String WORK_MONTHLY_REPORT_DETAIL = ip + "phone/bmgzView.html";     //--------------------1

    // 进入请假申请界面，请求这个接口，可以获取一些用于显示表单的信息和用于进行提交表单的隐藏数据
    public final static String LEAVE_APPLY_PREMISE = ip + "phone/leaveApply.html";          //--------------------1

    // 进入车辆申请界面，请求这个接口，可以获取一些用于显示表单的信息和用于进行提交表单的隐藏数据
    public final static String BUS_REQUEST_APPLY_PREMISE = ip + "phone/tocarApply.html";    //--------------------1

    // ***********************************************************************************************************************************

    // 在获取了详情以后,获取审批所有的各个步骤的建议,( 车辆申请和请假申请没有建议 )
    public final static String GET_ALL_SUGGEST = ip + "phone/viewLC.html";                  //--------------------6

    // ***********************************************************************************************************************************

    // 准备提交前先请求网络获取下一步操作人的接口,
    public final static String REQUEST_USER_LIST = ip + "phone/getUserList.html";           //--------------------8

    // ***********************************************************************************************************************************

    /* ***四个功能模块提交表单的接口*** */
    // 计划总结，收文管理，发文管理，工作月报四个功能模块最后选择了下一步操作人后提交表单的接口
    public final static String SUBMIT_IS_AGREE_POST = ip + "phone/fwProcess.html";                  //--------------------1
    public final static String SUBMIT_IS_AGREE_RECORD = ip + "phone/swProcess.html";                //--------------------1
    public final static String SUBMIT_IS_AGREE_PLAN_SUMMARY = ip + "phone/gzjhprocess.html";        //--------------------1
    public final static String SUBMIT_IS_AGREE_WORK_MONTHLY_REPORT = ip + "phone/gzybProcess.html"; //--------------------1

    // 填写完表单界面并选择了下一步操作人的时候提交是否同意表单的接口(第一次请假和审批请假单都是这个)
    public final static String LEAVE_APPLY_SUBMIT = ip + "phone/leaveProcess.html";                 //--------------------2

    // 填写完表单界面并选择了下一步操作人的时候提交是否同意表单的接口(第一次车辆申请和审批申请单都是这个)
    public final static String BUS_REQUEST_APPLY_SUBMIT = ip + "phone/carStartProcess.html";        //--------------------2

    // ***********************************************************************************************************************************

    // 请求网络下载附件
    public final static String DOWNLOAD_ACCESSORY = ip + "upload/download1.html";                   //--------------------2


    // 请网络获取流程其中一个步骤如果驳回的的话,可以驳回到哪几个步骤
    public final static String REJECT_WHICH_STEP=ip+"phone/taskBack.html";

}
