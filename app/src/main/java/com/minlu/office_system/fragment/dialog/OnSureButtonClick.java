package com.minlu.office_system.fragment.dialog;

import android.content.DialogInterface;

import java.io.Serializable;
import java.util.List;

/**
 * Created by user on 2017/4/11.
 */

public interface OnSureButtonClick extends Serializable {

    long serialVersionUID = 5L;

    void onSureClick(DialogInterface dialog, int id, List<Boolean> isChecks);
}
