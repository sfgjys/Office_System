package com.minlu.office_system.fragment.dialog;

import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AlertDialog;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.minlu.baselibrary.customview.SmoothCheckBox;
import com.minlu.baselibrary.util.ViewsUitls;
import com.minlu.office_system.R;
import com.minlu.office_system.StringsFiled;
import com.minlu.office_system.bean.CheckBoxBean;
import com.minlu.office_system.bean.CheckBoxChild;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by user on 2017/3/31.
 */

public class SelectNextUserDialog extends DialogFragment {

    private OnSureButtonClick onSureButtonClick;
    private AlertDialog alertDialog;
    private LinearLayout mAddCheckBox;
    private CheckBoxBean checkBoxBean;

    public SelectNextUserDialog() {
    }

    public void setOnSureButtonClick(OnSureButtonClick onSureButtonClick) {
        this.onSureButtonClick = onSureButtonClick;
    }

    public void setCheckBoxTexts(List<CheckBoxChild> checkBoxTexts) {
        checkBoxBean = new CheckBoxBean();
        checkBoxBean.setCheckBoxChild(checkBoxTexts);
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        if (savedInstanceState != null) {
            checkBoxBean = (CheckBoxBean) savedInstanceState.getSerializable(StringsFiled.SELECT_NEXT_DIALOG_SAVE_DATA);
        }

        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

        LayoutInflater inflater = getActivity().getLayoutInflater();
        View view = inflater.inflate(R.layout.dialog_select_next_user, null);

        mAddCheckBox = (LinearLayout) view.findViewById(R.id.ll_add_check_box);

        for (int i = 0; i < checkBoxBean.getCheckBoxChild().size(); i++) {
            // 创建要添加进addCheckBox控件的checkbos子控件
            View inflate = ViewsUitls.inflate(R.layout.custom_check_box_style);
            final SmoothCheckBox smoothCheckBox = (SmoothCheckBox) inflate.findViewById(R.id.smooth_check_box);
            TextView textView = (TextView) inflate.findViewById(R.id.tv_custom_check_box_text);
            textView.setText(checkBoxBean.getCheckBoxChild().get(i).getCheckBoxRightText());
            textView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    smoothCheckBox.setChecked(!smoothCheckBox.isChecked(), true);
                }
            });

            // 添加
            mAddCheckBox.addView(inflate);
        }

        builder.setView(view)
                .setPositiveButton("确认",
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int id) {
                                List<Boolean> isChecks = new ArrayList<>();
                                // 从父布局中获取子控件，获取其是否被选中
                                for (int i = 0; i < checkBoxBean.getCheckBoxChild().size(); i++) {
                                    if (mAddCheckBox.getChildAt(i) instanceof LinearLayout) {

                                        LinearLayout linearLayout = (LinearLayout) mAddCheckBox.getChildAt(i);
                                        SmoothCheckBox smoothCheckBox = (SmoothCheckBox) linearLayout.getChildAt(0);

                                        isChecks.add(smoothCheckBox.isChecked());
                                    } else {
                                        isChecks.add(false);
                                    }
                                }
                                if (onSureButtonClick != null) {
                                    onSureButtonClick.onSureClick(dialog, id, isChecks);
                                }
                            }
                        })
                .setNegativeButton("取消", null);
        alertDialog = builder.create();
        return alertDialog;
    }

    @Override
    public void onStart() {
        super.onStart();
        if (onSureButtonClick == null) {//当展示对话框的时候，如果没有setDateListener监听那就不让对话框显示
            alertDialog.cancel();
            alertDialog.dismiss();
        } else {
            // 注意 该修改对话框的宽度必须在super.onStart();后面
            WindowManager windowManager = getActivity().getWindowManager();
            Display display = windowManager.getDefaultDisplay();  //为获取屏幕宽、高

            Window window = alertDialog.getWindow();

            WindowManager.LayoutParams layoutParams = window.getAttributes();
            // 设置透明度为0.3
            layoutParams.width = (int) (display.getWidth() * 0.7);
            window.setAttributes(layoutParams);
        }
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        outState.putSerializable(StringsFiled.SELECT_NEXT_DIALOG_SAVE_DATA, checkBoxBean);
        super.onSaveInstanceState(outState);
    }
}
