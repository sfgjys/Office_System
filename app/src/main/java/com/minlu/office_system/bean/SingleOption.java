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

    public SingleOption(String singleOptionRightText, String userName, String orgInform) {
        this.singleOptionRightText = singleOptionRightText;
        this.userName = userName;
        this.orgInform = orgInform;
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
