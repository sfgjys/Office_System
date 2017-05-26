package com.minlu.office_system.fragment.dialog;

import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;

import com.minlu.office_system.R;
import com.minlu.office_system.adapter.SelectLeadTreeRecyclerAdapter;
import com.minlu.office_system.customview.treelist.Node;

import java.util.List;

public class SelectAttendLeadDialog extends DialogFragment {

    private OnSureButtonClick onSureButtonClick;
    private AlertDialog alertDialog;
    private List<Node> mData;

    public SelectAttendLeadDialog() {
    }

    public void setOnSureButtonClick(OnSureButtonClick onSureButtonClick) {
        this.onSureButtonClick = onSureButtonClick;
    }

    public void setDialogData(List<Node> mData) {
        this.mData = mData;
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        if (savedInstanceState != null) {
            // TODO 获取存储数据
        }

        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

        LayoutInflater inflater = getActivity().getLayoutInflater();
        View view = inflater.inflate(R.layout.dialog_select_attend_lead, null);

        RecyclerView recyclerView = (RecyclerView) view.findViewById(R.id.recyclerview);

        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        //第一个参数  RecyclerView
        //第二个参数  上下文
        //第三个参数  数据集
        //第四个参数  默认展开层级数 0为不展开
        //第五个参数  展开的图标
        //第六个参数  闭合的图标
        SelectLeadTreeRecyclerAdapter selectLeadTreeRecyclerAdapter = new SelectLeadTreeRecyclerAdapter(recyclerView, getContext(),
                mData, 1, R.mipmap.tree_ex, R.mipmap.tree_ec);
        recyclerView.setAdapter(selectLeadTreeRecyclerAdapter);

        builder.setView(view)
                .setPositiveButton("确认",
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int id) {
                                if (onSureButtonClick != null) {
                                    onSureButtonClick.onSureClick(dialog, id, null);
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
        // TODO 存储数据
        super.onSaveInstanceState(outState);
    }
}
