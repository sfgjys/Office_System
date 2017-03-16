package com.minlu.office_system.activity;

import com.minlu.baselibrary.base.BaseActivity;
import com.minlu.office_system.fragment.ApprovalFlowFragment;

/**
 * Created by user on 2017/3/16.
 */

public class ApprovalFlow extends BaseActivity {
    @Override
    public void onCreateContent() {

        getSupportFragmentManager().beginTransaction().replace(com.minlu.baselibrary.R.id.fl_base_content, new ApprovalFlowFragment()).commit();

    }
}
