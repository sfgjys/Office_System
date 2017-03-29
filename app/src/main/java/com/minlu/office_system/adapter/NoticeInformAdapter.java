package com.minlu.office_system.adapter;

import com.minlu.baselibrary.base.BaseHolder;
import com.minlu.baselibrary.base.MyBaseAdapter;
import com.minlu.office_system.bean.NoticeList;
import com.minlu.office_system.holder.NoticeInformHolder;

import java.util.List;

/**
 * Created by user on 2017/3/28.
 */

public class NoticeInformAdapter extends MyBaseAdapter<NoticeList> {

    public NoticeInformAdapter(List<NoticeList> list) {
        super(list);
    }

    @Override
    public boolean hasMore() {
        return false;
    }

    @Override
    public BaseHolder getHolder() {
        return new NoticeInformHolder();
    }

    @Override
    public List<NoticeList> onLoadMore() {
        return null;
    }
}
