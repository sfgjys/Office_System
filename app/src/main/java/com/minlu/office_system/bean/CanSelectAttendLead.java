package com.minlu.office_system.bean;

import java.util.List;

/**
 * Created by user on 2017/5/25.
 */

public class CanSelectAttendLead {

    private String departmentName;
    private List<String> leadName;

    public CanSelectAttendLead() {
    }

    public CanSelectAttendLead(String departmentName, List<String> leadName) {
        this.departmentName = departmentName;
        this.leadName = leadName;
    }

    public String getDepartmentName() {
        return departmentName;
    }

    public void setDepartmentName(String departmentName) {
        this.departmentName = departmentName;
    }

    public List<String> getLeadName() {
        return leadName;
    }

    public void setLeadName(List<String> leadName) {
        this.leadName = leadName;
    }
}
