package com.minlu.office_system.bean;

import java.io.Serializable;
import java.util.List;

/**
 * Created by user on 2017/4/10.
 */

public class DialogBean implements Serializable {

    private static final long serialVersionUID = 1L;

    private List<NoticeList> data;

    public DialogBean() {
    }

    public List<NoticeList> getData() {
        return data;
    }

    public void setData(List<NoticeList> data) {
        this.data = data;
    }
}
