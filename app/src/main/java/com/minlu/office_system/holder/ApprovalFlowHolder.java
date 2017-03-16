package com.minlu.office_system.holder;

import android.view.View;
import android.widget.TextView;

import com.minlu.baselibrary.base.BaseHolder;
import com.minlu.baselibrary.util.ViewsUitls;
import com.minlu.office_system.R;

/**
 * Created by user on 2017/3/16.
 */
public class ApprovalFlowHolder extends BaseHolder {

    private TextView mSingleApprovalNumber;
    private View mSingleApprovalLine;

    @Override
    public View initView() {
        View inflate = ViewsUitls.inflate(R.layout.single_approval_link);
        mSingleApprovalLine = inflate.findViewById(R.id.single_approval_link_left_line);
        mSingleApprovalNumber = (TextView) inflate.findViewById(R.id.single_approval_link_left_round);
        return inflate;
    }

    @Override
    public void setRelfshData(Object mData, int postion) {
        mSingleApprovalNumber.setText((postion + 1) + "");
        if (postion == 8) {
            mSingleApprovalLine.setVisibility(View.INVISIBLE);
        } else {
            mSingleApprovalLine.setVisibility(View.VISIBLE);
        }
    }
}
