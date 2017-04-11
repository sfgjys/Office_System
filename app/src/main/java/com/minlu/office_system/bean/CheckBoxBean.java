package com.minlu.office_system.bean;

import java.io.Serializable;
import java.util.List;

/**
 * Created by user on 2017/4/11.
 */

public class CheckBoxBean implements Serializable {

    private static final long serialVersionUID = 4L;

    private List<CheckBoxChild> checkBoxChild;

    public CheckBoxBean() {
    }

    public List<CheckBoxChild> getCheckBoxChild() {
        return checkBoxChild;
    }

    public void setCheckBoxChild(List<CheckBoxChild> checkBoxChild) {
        this.checkBoxChild = checkBoxChild;
    }
}
