package com.minlu.office_system.activity;

import android.os.Bundle;

import com.minlu.baselibrary.base.BaseActivity;
import com.minlu.office_system.StringsFiled;
import com.minlu.office_system.fragment.FormListFragment;

/**
 * Created by user on 2017/3/29.
 */

public class FormListActivity extends BaseActivity {

    @Override
    public void onCreateContent() {

        // 从homepage跳转过来时获取对应功能的position
        int formTypePosition = getmIntent().getIntExtra(StringsFiled.HOME_PAGE_TO_FORM_LIST_POSITION, -1);

        // 将上面的position存储到Bundle中，并使用这个Bundle开启FormListFragment，这样FormListFragment中就可以通过position从AllForms中获取processid参数进行网络请求
        Bundle bundle = new Bundle();
        bundle.putInt(StringsFiled.HOME_PAGE_TO_FORM_LIST_POSITION, formTypePosition);
        FormListFragment formListFragment = new FormListFragment();
        formListFragment.setArguments(bundle);

        getSupportFragmentManager().beginTransaction().replace(com.minlu.baselibrary.R.id.fl_base_content, formListFragment).commit();

    }
}
