package com.minlu.office_system.bean;

import java.io.Serializable;
import java.util.List;

/**
 * Created by user on 2017/4/11.
 */

public class DialogDataPacket implements Serializable {

    private static final long serialVersionUID = 4L;

    private List<SingleOption> singleOptions;

    public DialogDataPacket() {
    }

    public List<SingleOption> getSingleOptions() {
        return singleOptions;
    }

    public void setSingleOptions(List<SingleOption> singleOptions) {
        this.singleOptions = singleOptions;
    }
}
