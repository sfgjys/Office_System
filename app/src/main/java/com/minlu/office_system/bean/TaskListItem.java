package com.minlu.office_system.bean;

/**
 * Created by user on 2017/3/30.
 */

public class TaskListItem {

    private String processId;
    private String processName;
    private String taskVariable;
    private String taskName;
    private String taskCreateTime;
    private String creator;

    private String orderId;
    private String taskId;

    public TaskListItem(String processId, String processName, String taskVariable, String taskName, String taskCreateTime, String creator, String orderId, String taskId) {
        this.processId = processId;
        this.processName = processName;
        this.taskVariable = taskVariable;
        this.taskName = taskName;
        this.taskCreateTime = taskCreateTime;
        this.creator = creator;
        this.orderId = orderId;
        this.taskId = taskId;
    }

    public String getProcessId() {
        return processId;
    }

    public void setProcessId(String processId) {
        this.processId = processId;
    }

    public String getTaskVariable() {
        return taskVariable;
    }

    public void setTaskVariable(String taskVariable) {
        this.taskVariable = taskVariable;
    }

    public String getProcessName() {
        return processName;
    }

    public void setProcessName(String processName) {
        this.processName = processName;
    }

    public String getTaskCreateTime() {
        return taskCreateTime;
    }

    public void setTaskCreateTime(String taskCreateTime) {
        this.taskCreateTime = taskCreateTime;
    }

    public String getCreator() {
        return creator;
    }

    public void setCreator(String creator) {
        this.creator = creator;
    }

    public String getOrderId() {
        return orderId;
    }

    public void setOrderId(String orderId) {
        this.orderId = orderId;
    }

    public String getTaskName() {
        return taskName;
    }

    public void setTaskName(String taskName) {
        this.taskName = taskName;
    }

    public String getTaskId() {
        return taskId;
    }

    public void setTaskId(String taskId) {
        this.taskId = taskId;
    }
}
