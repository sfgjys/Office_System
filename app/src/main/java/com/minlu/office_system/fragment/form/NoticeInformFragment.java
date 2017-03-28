package com.minlu.office_system.fragment.form;

import android.view.View;
import android.widget.ListView;

import com.minlu.baselibrary.base.BaseFragment;
import com.minlu.baselibrary.base.ContentPage;
import com.minlu.baselibrary.util.ViewsUitls;
import com.minlu.office_system.R;
import com.minlu.office_system.activity.FormActivity;
import com.minlu.office_system.adapter.NoticeInformAdapter;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by user on 2017/3/27.
 */

public class NoticeInformFragment extends BaseFragment {

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
