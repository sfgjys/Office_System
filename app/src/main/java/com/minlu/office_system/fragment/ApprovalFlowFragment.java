package com.minlu.office_system.fragment;

import android.os.Bundle;
import android.view.View;
import android.widget.ListView;

import com.minlu.baselibrary.base.BaseFragment;
import com.minlu.baselibrary.base.ContentPage;
import com.minlu.baselibrary.util.ViewsUitls;
import com.minlu.office_system.R;
import com.minlu.office_system.adapter.ApprovalFlowAdapter;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by user on 2017/3/16.
 */
public class ApprovalFlowFragment extends BaseFragment {

    private List<String> excessive;

    @Override
    protected void onSubClassOnCreateView() {
        loadDataAndRefresh();
    }

    @Override
    protected View onCreateSuccessView(Bundle savedInstanceState) {

        View inflate = ViewsUitls.inflate(R.layout.layout_list);
        ListView listView = (ListView) inflate.findViewById(R.id.list_view);
        listView.setAdapter(new ApprovalFlowAdapter(excessive));
        listView.setPadding(ViewsUitls.dpToPx(18), ViewsUitls.dpToPx(18), ViewsUitls.dpToPx(18), ViewsUitls.dpToPx(18));

        return inflate;
    }

    @Override
    protected ContentPage.ResultState onLoad() {
        excessive = new ArrayList<>();
        excessive.add("excessive");
        excessive.add("excessive");
        excessive.add("excessive");
        excessive.add("excessive");
        excessive.add("excessive");
        excessive.add("excessive");
        excessive.add("excessive");
        excessive.add("excessive");
        excessive.add("excessive");
        return chat(excessive);
    }
}
