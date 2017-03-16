package com.minlu.office_system.adapter;

import com.minlu.baselibrary.base.BaseHolder;
import com.minlu.baselibrary.base.MyBaseAdapter;
import com.minlu.office_system.holder.ApprovalFlowHolder;

import java.util.List;

/**
 * Created by user on 2017/3/16.
 */

public class ApprovalFlowAdapter extends MyBaseAdapter {

    public ApprovalFlowAdapter(List list) {
        super(list);
    }

    @Override
    public BaseHolder getHolder() {
        return new ApprovalFlowHolder();
    }

    @Override
    public List onLoadMore() {
        return null;
    }

    @Override
    public boolean hasMore() {
        return false;
    }
}
