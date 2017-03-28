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

import java.util.List;

/**
 * Created by user on 2017/3/27.
 */

public class NoticeInformListDialog extends DialogFragment {

    private String html="<!DOCTYPE html><html lang=\"en\"><head><meta http-equiv=\"Content-Type\" content=\"text/html; charset=UTF-8\"><!-- Meta, title, CSS, favicons, etc. --><meta charset=\"utf-8\"><meta http-equiv=\"X-UA-Compatible\" content=\"IE=edge\"><meta name=\"viewport\" content=\"width=device-width, initial-scale=1\"><title>Seera OA! | </title></head><body style=\"background:#F7F7F7;\"><div class=\"\"><a class=\"hiddenanchor\" id=\"toregister\"></a><a class=\"hiddenanchor\" id=\"tologin\"></a><div id=\"wrapper\"><div id=\"login\" class=\"  form\"><section class=\"login_content\"><form action=\"/loginForm\" id=\"loginForm\" #if($rurl) data-rurl=\"$rurl\" #end method=\"post\"><h1>12345 </h1><div><input type=\"text\" class=\"form-control\" name=\"username\" required=\"\" placeholder=\"请输入用户\" /></div><div><input type=\"password\" class=\"form-control\" name=\"password\" required=\"\" placeholder=\"请输入密码\" /></div><label class=\"error\" style=\"color: darkorange\">&nbsp;</label><div><button type=\"button\" id=\"loginBtn\" class=\"btn btn-default submit\"  >123</button></div><div class=\"clearfix\"></div><div class=\"separator\"><div class=\"clearfix\"></div><br /><div></div></div></form><!-- form --></section><!-- content --></div></div></div></body></html>\n";

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

        ListView listView = (ListView) view.findViewById(R.id.notice_inform_list_view);
        View cancel = view.findViewById(R.id.notice_inform_dialog_cancel);
        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                alertDialog.cancel();
                alertDialog.dismiss();
            }
        });

        listView.setAdapter(new NoticeInformAdapter(data));
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(ViewsUitls.getContext(), NoticeInformActivity.class);
                intent.putExtra(BaseStringsFiled.ACTIVITY_TITLE, "公告详情");
                intent.putExtra(StringsFiled.HTML_DETAIL_CODE, html);
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
}
