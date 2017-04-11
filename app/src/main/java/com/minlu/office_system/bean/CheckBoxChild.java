package com.minlu.office_system.bean;

import java.io.Serializable;

/**
 * Created by user on 2017/4/1.
 */

public class CheckBoxChild implements Serializable {

    private static final long serialVersionUID = 3L;

    private String userName;
    private String orgInform;
    private String checkBoxRightText;

    public CheckBoxChild(String checkBoxRightText, String userName, String orgInform) {
        this.checkBoxRightText = checkBoxRightText;
        this.userName = userName;
        this.orgInform = orgInform;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getCheckBoxRightText() {
        return checkBoxRightText;
    }

    public void setCheckBoxRightText(String checkBoxRightText) {
        this.checkBoxRightText = checkBoxRightText;
    }

    public String getOrgInform() {
        return orgInform;
    }

    public void setOrgInform(String orgInform) {
        this.orgInform = orgInform;
    }
}
