package com.minlu.office_system.adapter;

import com.minlu.baselibrary.base.BaseHolder;
import com.minlu.baselibrary.base.MyBaseAdapter;
import com.minlu.office_system.bean.WeekWorkEachData;
import com.minlu.office_system.holder.WeekWorkPlanHolder;

import java.util.List;

/**
 * Created by user on 2017/5/23.
 */

public class WeekWorkPlanAdapter extends MyBaseAdapter<WeekWorkEachData> {

    public WeekWorkPlanAdapter(List<WeekWorkEachData> list) {
        super(list);
    }

    @Override
    public BaseHolder getHolder() {
        return new WeekWorkPlanHolder();
    }

    @Override
    public List<WeekWorkEachData> onLoadMore() {
        return null;
    }

    @Override
    public boolean hasMore() {
        return false;
    }
}
