package com.minlu.office_system.bean;

/**
 * Created by user on 2017/2/27.
 */

public class HomePageItem {

    private String functionName;
    private int functionIconId;

    public HomePageItem(String functionName, int functionIconId) {
        this.functionName = functionName;
        this.functionIconId = functionIconId;
    }

    public String getFunctionName() {
        return functionName;
    }

    public void setFunctionName(String functionName) {
        this.functionName = functionName;
    }

    public int getFunctionIconId() {
        return functionIconId;
    }

    public void setFunctionIconId(int functionIconId) {
        this.functionIconId = functionIconId;
    }
}
