package com.minlu.office_system.fragment.form;

import android.view.View;

import com.minlu.baselibrary.base.ContentPage;
import com.minlu.office_system.fragment.form.formPremise.FormFragment;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by user on 2017/3/27.
 */

public class NoticeInformFragment extends FormFragment {

    private List<Object> excessive;

    @Override
    protected void onSubClassOnCreateView() {
        loadDataAndRefresh();
    }

    @Override
    protected View onCreateSuccessView() {
        return null;
    }

    @Override
    protected ContentPage.ResultState onLoad() {
        excessive = new ArrayList<>();
//        excessive.add("excessive");
        return chat(excessive);
    }

    @Override
    public void disAgreeOnClick(View v) {

    }

    @Override
    public void agreeOnClick(View v) {

    }

    @Override
    public void submitOnClick(View v) {

    }
}
