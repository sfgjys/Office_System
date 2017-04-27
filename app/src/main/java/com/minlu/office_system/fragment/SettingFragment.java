package com.minlu.office_system.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.minlu.baselibrary.base.BaseFragment;
import com.minlu.baselibrary.base.ContentPage;
import com.minlu.baselibrary.util.SharedPreferencesUtil;
import com.minlu.baselibrary.util.ViewsUitls;
import com.minlu.office_system.R;
import com.minlu.office_system.StringsFiled;
import com.minlu.office_system.activity.LoginActivity;
import com.minlu.office_system.customview.SettingItem;

import java.util.ArrayList;

/**
 * Created by user on 2017/2/27.
 */

public class SettingFragment extends BaseFragment implements View.OnClickListener {
    @Override
    protected void onSubClassOnCreateView() {
        loadDataAndRefresh();
    }

    @Override
    protected View onCreateSuccessView(Bundle savedInstanceState) {
        View inflate = ViewsUitls.inflate(R.layout.layout_setting);

        SettingItem versionInformation = (SettingItem) inflate.findViewById(R.id.setting_version_information);
        versionInformation.setOnClickListener(this);
        SettingItem aboutUs = (SettingItem) inflate.findViewById(R.id.setting_about_us);
        aboutUs.setOnClickListener(this);
        SettingItem logout = (SettingItem) inflate.findViewById(R.id.setting_logout);
        logout.setOnClickListener(this);

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
            case R.id.setting_version_information:
                break;
            case R.id.setting_about_us:
                break;
            case R.id.setting_logout:
                SharedPreferencesUtil.saveBoolean(ViewsUitls.getContext(), StringsFiled.IS_AUTO_LOGIN, false);
                getActivity().startActivity(new Intent(ViewsUitls.getContext(), LoginActivity.class));
                getActivity().finish();// TODO 这里只需要关闭一个MainActivity界面就行，以后界面开启有改动这里也记得改下
                break;
        }
    }
}
