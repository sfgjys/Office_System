package com.minlu.office_system.holder;

import android.graphics.drawable.Drawable;
import android.view.View;
import android.widget.TextView;

import com.minlu.baselibrary.base.BaseHolder;
import com.minlu.baselibrary.util.StringUtils;
import com.minlu.baselibrary.util.TimeTool;
import com.minlu.baselibrary.util.ViewsUitls;
import com.minlu.office_system.R;
import com.minlu.office_system.StringsFiled;
import com.minlu.office_system.bean.TaskListItem;

/**
 * Created by user on 2017/3/30.
 */

public class FormListHolder extends BaseHolder<TaskListItem> {

    private TextView mCreator;
    private TextView mTaskCreateTime;
    private TextView mTaskVariable;

    @Override
    public View initView() {
        View inflate = ViewsUitls.inflate(R.layout.item_form_list);

        mCreator = (TextView) inflate.findViewById(R.id.tv_item_form_list_creator);
        mTaskVariable = (TextView) inflate.findViewById(R.id.tv_item_form_list_task_variable);
        mTaskCreateTime = (TextView) inflate.findViewById(R.id.tv_item_form_list_task_create_time);

        return inflate;
    }

    @Override
    public void setRelfshData(TaskListItem mData, int position) {
        String creator = mData.getCreator();
        String taskVariable = mData.getTaskVariable();

        String processId = mData.getProcessId();
        switch (processId) {
            case StringsFiled.RecordManagement_ProcessId:
                setCreatorAndTitle(View.VISIBLE, "签收人 : " + creator, View.VISIBLE, "来文标题 : " + taskVariable);
                break;
            case StringsFiled.PostManagement_ProcessId:
                setCreatorAndTitle(View.VISIBLE, "拟稿人 : " + creator, View.VISIBLE, "发文标题 : " + taskVariable);
                break;
            case StringsFiled.Leave_ProcessId:
                setCreatorAndTitle(View.VISIBLE, "请假人 : " + creator, View.VISIBLE, "请假标题 : " + taskVariable);
                break;
            case StringsFiled.Bus_ProcessId:
                setCreatorAndTitle(View.VISIBLE, "申请人 : " + creator, View.VISIBLE, "申请标题 : " + taskVariable);
                break;
            case StringsFiled.PlanSummary_ProcessId:
                setCreatorAndTitle(View.GONE, "", View.VISIBLE, creator + " - 工作计划");
                break;
            case StringsFiled.WorkMonthlyReport_ProcessId:
                setCreatorAndTitle(View.GONE, "", View.VISIBLE, creator + " - 工作月报");
                break;
        }

        String taskCreateTime = mData.getTaskCreateTime();
        if (!StringUtils.isEmpty(taskCreateTime)) {
            // 时间数据没有问题，显示文本
            mTaskCreateTime.setText(taskCreateTime);

            long taskLongTime = TimeTool.textTimeToLongTime(taskCreateTime, "yyyy-MM-dd");
            long nowLongTime = TimeTool.getDayLongTime();
            String s = TimeTool.longTimeToTextTime(nowLongTime, "yyyy-MM-dd");
            // 将时间数据与现在的日期进行比较
            if (taskLongTime == nowLongTime) {// 今天
                mTaskVariable.setCompoundDrawables(getLeftIcon(R.mipmap.form_list_left_small_icon_today), null, null, null);
            } else if ((taskLongTime + 86400000) == nowLongTime) {// 昨天
                mTaskVariable.setCompoundDrawables(getLeftIcon(R.mipmap.form_list_left_small_icon_yesterday), null, null, null);
            } else if ((taskLongTime + 86400000 * 2) == nowLongTime) {// 前天
                mTaskVariable.setCompoundDrawables(getLeftIcon(R.mipmap.form_list_left_small_icon_before_yesterday), null, null, null);
            } else {// 超过前天了，不显示
                mTaskVariable.setCompoundDrawables(null, null, null, null);
            }
        } else {
            // 时间数据有问题，不显示文本
            mTaskCreateTime.setText("");
            mTaskVariable.setCompoundDrawables(null, null, null, null);
        }
    }

    private void setCreatorAndTitle(int creatorVisibility, CharSequence creatorText, int titleVisibility, CharSequence titleText) {
        mCreator.setVisibility(creatorVisibility);
        mCreator.setText(creatorText);
        mTaskVariable.setVisibility(titleVisibility);
        mTaskVariable.setText(titleText);
    }

    private Drawable getLeftIcon(int resourceId) {
        Drawable drawable = ViewsUitls.getContext().getResources().getDrawable(resourceId);
        drawable.setBounds(0, 0, drawable.getMinimumWidth(), drawable.getMinimumHeight());
        return drawable;
    }
}
