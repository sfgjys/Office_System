package com.minlu.office_system.fragment.dialog;

import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import com.minlu.office_system.R;

/**
 * Created by user on 2017/3/16.
 */

public class PromptDialog extends DialogFragment {


    private String promptText;
    private OnSureButtonClick onSureButtonClick;
    private AlertDialog alertDialog;

    public interface OnSureButtonClick {
        void onSureClick(DialogInterface dialog, int id);
    }

    public PromptDialog(OnSureButtonClick onSureButtonClick, String promptText) {
        this.onSureButtonClick = onSureButtonClick;
        this.promptText = promptText;
    }

    public PromptDialog() {
        this.onSureButtonClick = null;
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        LayoutInflater inflater = getActivity().getLayoutInflater();
        View view = inflater.inflate(R.layout.dialog_prompt_text, null);
        TextView dialogPromptText = (TextView) view.findViewById(R.id.tv_dialog_prompt_text);
        dialogPromptText.setText(promptText);
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
