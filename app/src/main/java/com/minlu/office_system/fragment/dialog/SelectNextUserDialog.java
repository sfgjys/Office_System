package com.minlu.office_system.fragment.dialog;

import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.CheckBox;
import android.widget.LinearLayout;

import com.minlu.baselibrary.util.ViewsUitls;
import com.minlu.office_system.R;
import com.minlu.office_system.bean.CheckBoxText;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by user on 2017/3/31.
 */

public class SelectNextUserDialog<T> extends DialogFragment {

    private OnSureButtonClick onSureButtonClick;
    private AlertDialog alertDialog;
    private List<T> checkBoxTexts;
    private LinearLayout mAddCheckBox;

    public interface OnSureButtonClick {
        void onSureClick(DialogInterface dialog, int id, List<Boolean> isChecks);
    }

    /**
     * 参数三集合中的类必须是CheckBoxText的子类
     */
    public SelectNextUserDialog(OnSureButtonClick onSureButtonClick, List<T> checkBoxTexts) {
        this.onSureButtonClick = onSureButtonClick;
        this.checkBoxTexts = checkBoxTexts;
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

        mAddCheckBox = (LinearLayout) view.findViewById(R.id.ll_add_check_box);

        for (int i = 0; i < checkBoxTexts.size(); i++) {
            // 创建要添加进addCheckBox控件的checkbos子控件
            CheckBox checkBox = (CheckBox) ViewsUitls.inflate(R.layout.custom_check_box_style);
            checkBox.setText(((CheckBoxText) checkBoxTexts.get(i)).getCheckBoxRightText());

            // 添加
            mAddCheckBox.addView(checkBox);
        }

        builder.setView(view)
                .setPositiveButton("确认",
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int id) {
                                List<Boolean> isChecks = new ArrayList<>();
                                // 从父布局中获取子控件，获取其是否被选中
                                for (int i = 0; i < checkBoxTexts.size(); i++) {
                                    if (mAddCheckBox.getChildAt(i) instanceof CheckBox) {
                                        isChecks.add(((CheckBox) mAddCheckBox.getChildAt(i)).isChecked());
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
        if (onSureButtonClick == null) {//当展示对话框的时候，如果没有setDateListener监听那就不让对话框显示
            alertDialog.cancel();
            alertDialog.dismiss();
        }
        super.onStart();
    }

}
