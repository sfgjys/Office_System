package com.minlu.office_system.fragment.dialog;

import android.app.Dialog;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AlertDialog;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;

import com.minlu.office_system.R;

import java.util.List;

/**
 * Created by user on 2017/3/27.
 */

public class NoticeInformListDialog extends DialogFragment {

    private final List data;
    private AlertDialog alertDialog;

    public NoticeInformListDialog(List data) {
        this.data = data;
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        LayoutInflater inflater = getActivity().getLayoutInflater();

        View view = inflater.inflate(R.layout.dialog_notice_inform, null);


        alertDialog = builder.setView(view).create();
        alertDialog.setCanceledOnTouchOutside(false);



        return alertDialog;
    }

    @Override
    public void onStart() {
        super.onStart();

        WindowManager windowManager = getActivity().getWindowManager();
        Display display = windowManager.getDefaultDisplay();  //为获取屏幕宽、高

        Window window = alertDialog.getWindow();

        WindowManager.LayoutParams layoutParams = window.getAttributes();
        // 设置透明度为0.3
        layoutParams.width =  (int) (display.getWidth() * 0.6); ;
        window.setAttributes(layoutParams);

    }
}
