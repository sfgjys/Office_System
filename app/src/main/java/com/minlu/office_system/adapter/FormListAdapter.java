package com.minlu.office_system.adapter;

import com.minlu.baselibrary.base.BaseHolder;
import com.minlu.baselibrary.base.MyBaseAdapter;
import com.minlu.office_system.bean.TaskListItem;
import com.minlu.office_system.holder.FormListHolder;

import java.util.List;

/**
 * Created by user on 2017/3/30.
 */

public class FormListAdapter extends MyBaseAdapter<TaskListItem> {

    public FormListAdapter(List<TaskListItem> list) {
        super(list);
    }

    @Override
    public BaseHolder getHolder() {
        return new FormListHolder();
    }

    @Override
    public List<TaskListItem> onLoadMore() {
        return null;
    }

    @Override
    public boolean hasMore() {
        return false;
    }
}
