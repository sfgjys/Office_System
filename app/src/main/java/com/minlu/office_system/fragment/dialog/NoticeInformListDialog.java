package com.minlu.office_system.fragment.dialog;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AlertDialog;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ListView;

import com.minlu.baselibrary.BaseStringsFiled;
import com.minlu.baselibrary.util.ViewsUitls;
import com.minlu.office_system.R;
import com.minlu.office_system.StringsFiled;
import com.minlu.office_system.activity.NoticeInformActivity;
import com.minlu.office_system.adapter.NoticeInformAdapter;
import com.minlu.office_system.bean.DialogBean;
import com.minlu.office_system.bean.NoticeList;

import java.util.List;

/**
 * Created by user on 2017/3/27.
 */

public class NoticeInformListDialog extends DialogFragment {

    private AlertDialog alertDialog;
    private DialogBean dialogBean;

    public NoticeInformListDialog() {
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        if (savedInstanceState != null) {
            dialogBean = (DialogBean) savedInstanceState.getSerializable(StringsFiled.NOTICE_DIALOG_SAVE_DATA);
        }

        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        LayoutInflater inflater = getActivity().getLayoutInflater();

        View view = inflater.inflate(R.layout.dialog_notice_inform, null);

        ListView listView = (ListView) view.findViewById(R.id.notice_inform_list_view);
        View cancel = view.findViewById(R.id.notice_inform_dialog_cancel);
        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                alertDialog.cancel();
                alertDialog.dismiss();
            }
        });

        listView.setAdapter(new NoticeInformAdapter(dialogBean.getData()));
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(ViewsUitls.getContext(), NoticeInformActivity.class);
                intent.putExtra(BaseStringsFiled.ACTIVITY_TITLE, "公告详情");
                intent.putExtra(StringsFiled.HTML_DETAIL_CODE, dialogBean.getData().get(position).getmNoticeContentId());
                getActivity().startActivity(intent);
            }
        });


        alertDialog = builder.setView(view).create();
        alertDialog.setCanceledOnTouchOutside(false);

        alertDialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
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
        layoutParams.width = (int) (display.getWidth() * 0.8);
        layoutParams.height = (int) (display.getHeight() * 0.4);
        window.setAttributes(layoutParams);
    }

    public void setData(List<NoticeList> mNoticeListData) {
        dialogBean = new DialogBean();
        dialogBean.setData(mNoticeListData);
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        outState.putSerializable(StringsFiled.NOTICE_DIALOG_SAVE_DATA, dialogBean);
        super.onSaveInstanceState(outState);
    }
}
