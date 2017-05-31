package com.minlu.office_system.fragment.form;

import android.os.Bundle;
import android.view.View;

import com.minlu.baselibrary.base.ContentPage;
import com.minlu.baselibrary.util.ViewsUitls;
import com.minlu.office_system.R;
import com.minlu.office_system.customview.EditTextTimeSelector;
import com.minlu.office_system.fragment.form.formPremise.FormFragment;

import java.util.ArrayList;

/**
 * Created by user on 2017/5/26.
 */

public class UseMoneyPlanFragment extends FormFragment {

    @Override
    protected void onSubClassOnCreateView() {
        loadDataAndRefresh();
    }

    @Override
    protected View onCreateSuccessView(Bundle savedInstanceState) {
        View inflate = ViewsUitls.inflate(R.layout.form_use_money_plan);

        // 设置显示当前时间
        EditTextTimeSelector nowTime = (EditTextTimeSelector) inflate.findViewById(R.id.form_use_money_plan_time);
        nowTime.getmTimeOfDay().setEnabled(false);
        nowTime.getmTimeOfDay().setVisibility(View.INVISIBLE);
        nowTime.getmDayOfYear().setEnabled(false);
        nowTime.setNowDayOfYearAndTimeOfDay();

        return inflate;
    }

    @Override
    protected ContentPage.ResultState onLoad() {
        ArrayList<String> strings = new ArrayList<>();
        strings.add("");
        return chat(strings);
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
