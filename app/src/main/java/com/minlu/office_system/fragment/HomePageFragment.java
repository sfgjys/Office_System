package com.minlu.office_system.fragment;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;

import com.minlu.baselibrary.BaseStringsFiled;
import com.minlu.baselibrary.base.BaseFragment;
import com.minlu.baselibrary.base.ContentPage;
import com.minlu.baselibrary.util.ToastUtil;
import com.minlu.baselibrary.util.ViewsUitls;
import com.minlu.office_system.R;
import com.minlu.office_system.StringsFiled;
import com.minlu.office_system.activity.FormActivity;
import com.minlu.office_system.activity.FormListActivity;
import com.minlu.office_system.adapter.HomePageAdapter;
import com.minlu.office_system.bean.HomePageItem;
import com.minlu.office_system.fragment.form.formPremise.AllForms;

import java.util.ArrayList;
import java.util.List;

public class HomePageFragment extends BaseFragment<HomePageItem> {

    private List<HomePageItem> date;

    @Override
    protected void onSubClassOnCreateView() {
        loadDataAndRefresh();
    }

    @Override
    protected View onCreateSuccessView() {
        View inflate = ViewsUitls.inflate(R.layout.layout_home_page);
        GridView gridView = (GridView) inflate.findViewById(R.id.gv_home_page);
        HomePageAdapter homePageAdapter = new HomePageAdapter(date);
        gridView.setAdapter(homePageAdapter);
        gridView.setSelector(new ColorDrawable(Color.TRANSPARENT));

        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                skipFormListActivity(position);
            }
        });

        return inflate;
    }

    private void skipFormListActivity(int position) {

        Intent intent = new Intent();
        // 标题
        intent.putExtra(BaseStringsFiled.ACTIVITY_TITLE, AllForms.values()[position].getFormName());
        // 功能类型对应的position
        intent.putExtra(StringsFiled.HOME_PAGE_TO_FORM_LIST_POSITION, position);

        switch (AllForms.values()[position].getHomeToListType()) {
            case StringsFiled.HOME_TO_LIST_SHOW_LIST:
                intent.setClass(getContext(), FormListActivity.class);
                break;
            case StringsFiled.HOME_TO_LIST_SHOW_FORM:
                intent.setClass(getContext(), FormActivity.class);
                break;
            case StringsFiled.HOME_TO_LIST_SHOW_NULL:
                intent = null;
                ToastUtil.showToast(ViewsUitls.getContext(), "敬请期待");
                break;
        }
        if (intent != null) {
            getActivity().startActivity(intent);
        }
    }

    @Override
    protected ContentPage.ResultState onLoad() {
        date = new ArrayList<>();
        for (int i = 0; i < AllForms.values().length; i++) {
            date.add(new HomePageItem(AllForms.values()[i].getFormName(), AllForms.values()[i].getFormIconId()));
        }
        return chat(date);
    }
}
