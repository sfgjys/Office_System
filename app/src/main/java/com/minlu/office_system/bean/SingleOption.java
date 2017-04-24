package com.minlu.office_system.bean;

import java.io.Serializable;

/**
 * Created by user on 2017/4/1.
 */

public class SingleOption implements Serializable {

    private static final long serialVersionUID = 3L;

    private String userName;
    private String orgInform;
    private String singleOptionRightText;
    private String rejectStepTaskName;

    public SingleOption(String singleOptionRightText, String userName, String orgInform, String rejectStepTaskName) {
        this.singleOptionRightText = singleOptionRightText;
        this.userName = userName;
        this.orgInform = orgInform;
        this.rejectStepTaskName = rejectStepTaskName;
    }

    public String getRejectStepTaskName() {
        return rejectStepTaskName;
    }

    public void setRejectStepTaskName(String rejectStepTaskName) {
        this.rejectStepTaskName = rejectStepTaskName;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getSingleOptionRightText() {
        return singleOptionRightText;
    }

    public void setSingleOptionRightText(String singleOptionRightText) {
        this.singleOptionRightText = singleOptionRightText;
    }

    public String getOrgInform() {
        return orgInform;
    }

    public void setOrgInform(String orgInform) {
        this.orgInform = orgInform;
    }
}
