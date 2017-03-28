package com.minlu.office_system.adapter;

import com.minlu.baselibrary.base.BaseHolder;
import com.minlu.baselibrary.base.MyBaseAdapter;
import com.minlu.office_system.holder.NoticeInformHolder;

import java.util.List;

/**
 * Created by user on 2017/3/28.
 */

public class NoticeInformAdapter extends MyBaseAdapter<String> {

    public NoticeInformAdapter(List<String> list) {
        super(list);
    }

    @Override
    public BaseHolder getHolder() {
        return new NoticeInformHolder();
    }

    @Override
    public boolean hasMore() {
        return false;
    }

    @Override
    public List<String> onLoadMore() {
        return null;
    }
}
