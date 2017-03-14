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
import com.minlu.baselibrary.util.ViewsUitls;
import com.minlu.office_system.R;
import com.minlu.office_system.StringsFiled;
import com.minlu.office_system.activity.FormActivity;
import com.minlu.office_system.adapter.HomePageAdapter;
import com.minlu.office_system.bean.HomePageItem;
import com.minlu.office_system.fragment.form.formPremise.AllForms;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
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
//                start();
                skipFormActivity(position);
            }
        });

        return inflate;
    }

    private void skipFormActivity(int position) {
        Intent intent = new Intent(getContext(), FormActivity.class);
        intent.putExtra(BaseStringsFiled.ACTIVITY_TITLE, AllForms.values()[position].getFormName());
        intent.putExtra(StringsFiled.TO_FORM_SHOW_WHICH_BUTTON, AllForms.values()[position].getShowWhichButton());
        intent.putExtra(StringsFiled.TO_FORM_SHOW_FORM_FRAGMENT, AllForms.values()[position].getFormClassName());
        intent.putExtra(StringsFiled.TO_FORM_SHOW_FORM_FRAGMENT_TAG, AllForms.values()[position].getFragmentTAG());
        getContext().startActivity(intent);
    }

    @Override
    protected ContentPage.ResultState onLoad() {
        date = new ArrayList<>();
        for (int i = 0; i < AllForms.values().length; i++) {
            date.add(new HomePageItem(AllForms.values()[i].getFormName(), AllForms.values()[i].getFormIconId()));
        }
        return chat(date);
    }


    private void start() {
        new Thread() {
            @Override
            public void run() {
                super.run();
                BufferedReader bufferedReader = null;
                try {
                    bufferedReader = new BufferedReader(new InputStreamReader(getContext().getAssets().open("text1.txt")));
                    String readLine;
                    while ((readLine = bufferedReader.readLine()) != null) {
                        System.out.println(readLine);
                        System.out.println("Instant Run Runtime started. Android package is baidumapsdk.demo, real application class is baidumapsdk.demo.");
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                } finally {
                    try {
                        assert bufferedReader != null;
                        bufferedReader.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
        }.start();
    }
}
