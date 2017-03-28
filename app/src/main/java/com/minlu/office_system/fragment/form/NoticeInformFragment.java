package com.minlu.office_system.fragment.form;

import android.content.Intent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import com.minlu.baselibrary.BaseStringsFiled;
import com.minlu.baselibrary.base.BaseFragment;
import com.minlu.baselibrary.base.ContentPage;
import com.minlu.baselibrary.util.ViewsUitls;
import com.minlu.office_system.R;
import com.minlu.office_system.StringsFiled;
import com.minlu.office_system.activity.FormActivity;
import com.minlu.office_system.activity.NoticeInformActivity;
import com.minlu.office_system.adapter.NoticeInformAdapter;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by user on 2017/3/27.
 */

public class NoticeInformFragment extends BaseFragment {

    private String html="<!DOCTYPE html><html lang=\"en\"><head><meta http-equiv=\"Content-Type\" content=\"text/html; charset=UTF-8\"><!-- Meta, title, CSS, favicons, etc. --><meta charset=\"utf-8\"><meta http-equiv=\"X-UA-Compatible\" content=\"IE=edge\"><meta name=\"viewport\" content=\"width=device-width, initial-scale=1\"><title>Seera OA! | </title></head><body style=\"background:#F7F7F7;\"><div class=\"\"><a class=\"hiddenanchor\" id=\"toregister\"></a><a class=\"hiddenanchor\" id=\"tologin\"></a><div id=\"wrapper\"><div id=\"login\" class=\"  form\"><section class=\"login_content\"><form action=\"/loginForm\" id=\"loginForm\" #if($rurl) data-rurl=\"$rurl\" #end method=\"post\"><h1>12345 </h1><div><input type=\"text\" class=\"form-control\" name=\"username\" required=\"\" placeholder=\"请输入用户\" /></div><div><input type=\"password\" class=\"form-control\" name=\"password\" required=\"\" placeholder=\"请输入密码\" /></div><label class=\"error\" style=\"color: darkorange\">&nbsp;</label><div><button type=\"button\" id=\"loginBtn\" class=\"btn btn-default submit\"  >123</button></div><div class=\"clearfix\"></div><div class=\"separator\"><div class=\"clearfix\"></div><br /><div></div></div></form><!-- form --></section><!-- content --></div></div></div></body></html>\n";

    private List<String> excessive;

    @Override
    protected void onSubClassOnCreateView() {
        loadDataAndRefresh();
    }

    @Override
    protected View onCreateSuccessView() {
        // 因为本fragment是通过R.id.sv_replace_form控件replace开启的，但是R.id.sv_replace_form控件是居中属性，所以再次我们要使得居中属性去除
        FormActivity formActivity = (FormActivity) getContext();
        if (formActivity != null) {
            formActivity.setScrollViewNoGravity();
        }


        View inflate = ViewsUitls.inflate(R.layout.layout_list);
        ListView listView = (ListView) inflate.findViewById(R.id.list_view);
        listView.setAdapter(new NoticeInformAdapter(excessive));
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(ViewsUitls.getContext(), NoticeInformActivity.class);
                intent.putExtra(BaseStringsFiled.ACTIVITY_TITLE, "公告详情");
                intent.putExtra(StringsFiled.HTML_DETAIL_CODE, html);
                getActivity().startActivity(intent);
            }
        });

        return inflate;
    }

    @Override
    protected ContentPage.ResultState onLoad() {
        excessive = new ArrayList<>();
        excessive.add("");
        excessive.add("");
        excessive.add("");
        excessive.add("");
        excessive.add("");
        excessive.add("");
        excessive.add("");
        excessive.add("");
        excessive.add("");
        excessive.add("");
        return chat(excessive);
    }
}
