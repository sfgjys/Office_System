package com.minlu.office_system.holder;

import android.view.View;
import android.widget.TextView;

import com.minlu.baselibrary.base.BaseHolder;
import com.minlu.baselibrary.util.ViewsUitls;
import com.minlu.office_system.R;
import com.minlu.office_system.bean.TaskListItem;

/**
 * Created by user on 2017/3/30.
 */

public class FormListHolder extends BaseHolder<TaskListItem> {

    private TextView mCreator;
    private TextView mProcessName;
    private TextView mTaskCreateTime;
    private TextView mTaskName;
    private TextView mTaskVariable;

    @Override
    public View initView() {
        View inflate = ViewsUitls.inflate(R.layout.item_form_list);

        mCreator = (TextView) inflate.findViewById(R.id.tv_item_form_list_creator);
        mProcessName = (TextView) inflate.findViewById(R.id.tv_item_form_list_process_name);
        mTaskCreateTime = (TextView) inflate.findViewById(R.id.tv_item_form_list_task_create_time);
        mTaskName = (TextView) inflate.findViewById(R.id.tv_item_form_list_task_name);
        mTaskVariable = (TextView) inflate.findViewById(R.id.tv_item_form_list_task_variable);

        return inflate;
    }

    @Override
    public void setRelfshData(TaskListItem mData, int position) {

        String processId = mData.getProcessId();
        String creator = mData.getCreator();
        String processName = mData.getProcessName();
        String taskCreateTime = mData.getTaskCreateTime();
        String taskName = mData.getTaskName();
        String taskVariable = mData.getTaskVariable();


    }
}
