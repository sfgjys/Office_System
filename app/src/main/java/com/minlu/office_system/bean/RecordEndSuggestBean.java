package com.minlu.office_system.bean;

/**
 * Created by user on 2017/4/26.
 */

public class RecordEndSuggestBean {

    private String suggestName;
    private String suggestMethod;
    private String suggestContent;
    private String suggestTime;

    public RecordEndSuggestBean(String suggestName, String suggestMethod, String suggestContent, String suggestTime) {
        this.suggestName = suggestName;
        this.suggestMethod = suggestMethod;
        this.suggestContent = suggestContent;
        this.suggestTime = suggestTime;
    }

    public String getSuggestTime() {
        return suggestTime;
    }

    public void setSuggestTime(String suggestTime) {
        this.suggestTime = suggestTime;
    }

    public String getSuggestName() {
        return suggestName;
    }

    public void setSuggestName(String suggestName) {
        this.suggestName = suggestName;
    }

    public String getSuggestMethod() {
        return suggestMethod;
    }

    public void setSuggestMethod(String suggestMethod) {
        this.suggestMethod = suggestMethod;
    }

    public String getSuggestContent() {
        return suggestContent;
    }

    public void setSuggestContent(String suggestContent) {
        this.suggestContent = suggestContent;
    }
}
