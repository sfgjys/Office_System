package com.minlu.office_system.bean;

/**
 * Created by user on 2017/5/19.
 */

public class WeekWorkEachData {

    private String startTime;
    private String endTime;
    private String week;
    private String workSite;
    private String workContent;
    private String attendLead;
    private String organization;
    private String participants;

    public WeekWorkEachData() {
    }

    public WeekWorkEachData(String startTime, String endTime, String week, String workSite, String attendLead, String workContent, String participants, String organization) {
        this.startTime = startTime;
        this.endTime = endTime;
        this.week = week;
        this.workSite = workSite;
        this.attendLead = attendLead;
        this.workContent = workContent;
        this.participants = participants;
        this.organization = organization;
    }

    public String getStartTime() {
        return startTime;
    }

    public void setStartTime(String startTime) {
        this.startTime = startTime;
    }

    public String getEndTime() {
        return endTime;
    }

    public void setEndTime(String endTime) {
        this.endTime = endTime;
    }

    public String getWeek() {
        return week;
    }

    public void setWeek(String week) {
        this.week = week;
    }

    public String getWorkSite() {
        return workSite;
    }

    public void setWorkSite(String workSite) {
        this.workSite = workSite;
    }

    public String getWorkContent() {
        return workContent;
    }

    public void setWorkContent(String workContent) {
        this.workContent = workContent;
    }

    public String getAttendLead() {
        return attendLead;
    }

    public void setAttendLead(String attendLead) {
        this.attendLead = attendLead;
    }

    public String getOrganization() {
        return organization;
    }

    public void setOrganization(String organization) {
        this.organization = organization;
    }

    public String getParticipants() {
        return participants;
    }

    public void setParticipants(String participants) {
        this.participants = participants;
    }

    @Override
    public String toString() {
        return "WeekWorkEachData{" +
                "startTime='" + startTime + '\'' +
                ", endTime='" + endTime + '\'' +
                ", week='" + week + '\'' +
                ", workSite='" + workSite + '\'' +
                ", workContent='" + workContent + '\'' +
                ", attendLead='" + attendLead + '\'' +
                ", organization='" + organization + '\'' +
                ", participants='" + participants + '\'' +
                '}';
    }
}
