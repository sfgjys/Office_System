package com.minlu.office_system.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.minlu.baselibrary.BaseStringsFiled;
import com.minlu.baselibrary.base.BaseFragment;
import com.minlu.baselibrary.base.ContentPage;
import com.minlu.baselibrary.util.ViewsUitls;
import com.minlu.office_system.R;
import com.minlu.office_system.activity.ApprovalFlow;
import com.minlu.office_system.customview.SettingItem;

import java.util.ArrayList;

public class MeFragment extends BaseFragment<String> implements View.OnClickListener {
    @Override
    protected void onSubClassOnCreateView() {
        loadDataAndRefresh();
    }

    @Override
    protected View onCreateSuccessView(Bundle savedInstanceState) {
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
                skipApprovalFlow("我的请假");
                break;
            case R.id.me_my_apply_for_bus:
                skipApprovalFlow("我的车辆申请");
                break;
            case R.id.me_my_meeting:
                skipApprovalFlow("我的会议室预约");
                break;
        }
    }

    private void skipApprovalFlow(String title) {
        Intent intent = new Intent(ViewsUitls.getContext(), ApprovalFlow.class);
        intent.putExtra(BaseStringsFiled.ACTIVITY_TITLE, title);
        getActivity().startActivity(intent);
    }
}
