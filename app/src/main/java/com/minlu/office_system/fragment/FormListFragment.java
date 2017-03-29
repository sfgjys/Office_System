package com.minlu.office_system.fragment;

import android.os.Bundle;
import android.view.View;

import com.minlu.baselibrary.base.BaseFragment;
import com.minlu.baselibrary.base.ContentPage;
import com.minlu.baselibrary.util.SharedPreferencesUtil;
import com.minlu.baselibrary.util.ViewsUitls;
import com.minlu.office_system.IpFiled;
import com.minlu.office_system.R;
import com.minlu.office_system.StringsFiled;
import com.minlu.office_system.fragment.form.formPremise.AllForms;
import com.minlu.office_system.http.OkHttpMethod;

import java.util.HashMap;

import okhttp3.Response;

/**
 * Created by user on 2017/3/29.
 */

public class FormListFragment extends BaseFragment {

    private int mFormTypePosition;

    @Override
    protected void onSubClassOnCreateView() {
        loadDataAndRefresh();
    }

    @Override
    protected View onCreateSuccessView() {
        View inflate = ViewsUitls.inflate(R.layout.form_list_view);

        return inflate;
    }

    @Override
    protected ContentPage.ResultState onLoad() {
        Bundle bundle = getBundle();
        // 从传递过来的Bundle中获取position
        mFormTypePosition = bundle.getInt(StringsFiled.HOME_PAGE_TO_FORM_LIST_POSITION);

        // 并根据position获取processid作为网络请求参数，获取数据
        HashMap<String, String> hashMap = new HashMap<>();
        hashMap.put("userName", SharedPreferencesUtil.getString(ViewsUitls.getContext(), StringsFiled.LOGIN_USER, ""));
        hashMap.put("processid", AllForms.values()[mFormTypePosition].getGetListParam());

        Response response = OkHttpMethod.synPostRequest(IpFiled.MANY_MANAGE_LIST, hashMap);


        return chat(null);
    }
}
