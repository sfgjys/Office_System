package com.minlu.office_system.fragment;

import android.view.View;

import com.minlu.baselibrary.base.BaseFragment;
import com.minlu.baselibrary.base.ContentPage;
import com.minlu.baselibrary.util.ViewsUitls;
import com.minlu.office_system.R;
import com.minlu.office_system.customview.SettingItem;

import java.util.ArrayList;

public class MeFragment extends BaseFragment<String> implements View.OnClickListener {
    @Override
    protected void onSubClassOnCreateView() {
        loadDataAndRefresh();
    }

    @Override
    protected View onCreateSuccessView() {
        View inflate = ViewsUitls.inflate(R.layout.layout_me);

        SettingItem myLeave = (SettingItem) inflate.findViewById(R.id.me_my_leave);
        myLeave.setOnClickListener(this);
        SettingItem myApplyForBus = (SettingItem) inflate.findViewById(R.id.me_my_apply_for_bus);
        myApplyForBus.setOnClickListener(this);
        SettingItem myMeeting = (SettingItem) inflate.findViewById(R.id.me_my_meeting);
        myMeeting.setOnClickListener(this);

        return inflate;
    }

    @Override
    protected ContentPage.ResultState onLoad() {
        ArrayList<String> excessive = new ArrayList<>();
        excessive.add("excessive");
        return chat(excessive);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.me_my_leave:
                break;
            case R.id.me_my_apply_for_bus:
                break;
            case R.id.me_my_meeting:
                break;
        }
    }
}
