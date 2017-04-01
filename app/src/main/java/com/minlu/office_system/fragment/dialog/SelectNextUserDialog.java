package com.minlu.office_system.fragment.dialog;

import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;

import com.minlu.office_system.R;

/**
 * Created by user on 2017/3/31.
 */

public class SelectNextUserDialog extends DialogFragment {

    private OnSureButtonClick onSureButtonClick;
    private AlertDialog alertDialog;

    public interface OnSureButtonClick {
        void onSureClick(DialogInterface dialog, int id);
    }

    public SelectNextUserDialog(OnSureButtonClick onSureButtonClick) {
        this.onSureButtonClick = onSureButtonClick;
    }

    public SelectNextUserDialog() {
        this.onSureButtonClick = null;
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

        LayoutInflater inflater = getActivity().getLayoutInflater();
        View view = inflater.inflate(R.layout.dialog_select_next_user, null);



        builder.setView(view)
                .setPositiveButton("确认",
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int id) {
                                if (onSureButtonClick != null) {
                                    onSureButtonClick.onSureClick(dialog, id);
                                }
                            }
                        })
                .setNegativeButton("取消", null);
        alertDialog = builder.create();
        return alertDialog;
    }

    @Override
    public void onStart() {
        if (onSureButtonClick == null) {//当展示对话框的时候，如果没有setDateListener监听那就不让对话框显示
            alertDialog.cancel();
            alertDialog.dismiss();
        }
        super.onStart();
    }

}
