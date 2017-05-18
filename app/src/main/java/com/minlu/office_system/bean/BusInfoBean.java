package com.minlu.office_system.bean;

/**
 * Created by user on 2017/5/5.
 */

public class BusInfoBean {

    private String busNumber;

    public BusInfoBean(String busNumber) {
        this.busNumber = busNumber;
    }

    public String getBusNumber() {
        return busNumber;
    }

    public void setBusNumber(String busNumber) {
        this.busNumber = busNumber;
    }
}
