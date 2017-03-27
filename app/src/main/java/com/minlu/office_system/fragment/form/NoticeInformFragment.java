package com.minlu.office_system.fragment.form;

import android.view.View;

import com.minlu.baselibrary.base.ContentPage;
import com.minlu.office_system.fragment.form.formPremise.FormFragment;

/**
 * Created by user on 2017/3/27.
 */

public class NoticeInformFragment extends FormFragment {
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
        return null;
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
