package com.minlu.office_system.customview;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.DialogInterface;
import android.support.v4.app.FragmentActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.AttributeSet;
import android.view.View;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TimePicker;

import com.minlu.baselibrary.util.StringUtils;
import com.minlu.baselibrary.util.TimeTool;
import com.minlu.baselibrary.util.ToastUtil;
import com.minlu.baselibrary.util.ViewsUitls;
import com.minlu.office_system.R;
import com.minlu.office_system.bean.CanSelectAttendLead;
import com.minlu.office_system.bean.WeekWorkEachData;
import com.minlu.office_system.customview.treelist.Node;
import com.minlu.office_system.fragment.dialog.OnSureButtonClick;
import com.minlu.office_system.fragment.dialog.SelectAttendLeadDialog;
import com.minlu.office_system.fragment.time.DatePickerFragment;
import com.minlu.office_system.fragment.time.TimePickerFragment;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class WeekWorkPlanItem extends LinearLayout {

    // 点击删除图片时要调用的回调接口
    public interface OnDeleteButtonClick {
        void onDeleteClick(View v);
    }

    // 文本变化回调接口
    private abstract class MyTextWatcher implements TextWatcher {
        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {
        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {
        }
    }

    // 存储控件数据的对象
    private WeekWorkEachData mWeekWorkEachData;


    private Date mTextChangedStartTime;
    private Date mTextChangedEndTime;
    private List<CanSelectAttendLead> mCanSelectPersonData;


    private EditText mAttendLeadView;
    private EditText mWeekView;
    private EditText mEndTimeView;
    private EditText mStartTimeView;

    private OnDeleteButtonClick mOnDeleteButtonClick;

    public WeekWorkPlanItem(Context context, WeekWorkEachData weekWorkEachData) {
        super(context);

        this.mWeekWorkEachData = weekWorkEachData;

        // 直接代码创建本控件时调用此处
        initView(context);
    }

    public WeekWorkPlanItem(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public WeekWorkPlanItem(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    private void initView(final Context context) {
        View view = View.inflate(context, R.layout.item_week_work_plan, this);

        // ******************************************************************************************************************************************

        // 组织显示
        View llOrganizationName = view.findViewById(R.id.ll_week_work_plan_item_organization_name);
        if (StringUtils.isEmpty(mWeekWorkEachData.getOrganization())) {
            llOrganizationName.setVisibility(GONE);// 隐藏
        } else {
            llOrganizationName.setVisibility(VISIBLE);// 显示
            EditText mOrganizationNameView = (EditText) view.findViewById(R.id.week_work_plan_item_organization_name);
            mOrganizationNameView.setText(mWeekWorkEachData.getOrganization());
        }

        // ******************************************************************************************************************************************

        // 设置工作地点内容和参加人员文本,并根据文本内容的变动实时修改数据源中对应的数据
        EditText mWorkSiteView = (EditText) view.findViewById(R.id.week_work_plan_item_work_site);
        mWorkSiteView.setText(mWeekWorkEachData.getWorkSite());
        mWorkSiteView.addTextChangedListener(new MyTextWatcher() {
            @Override
            public void afterTextChanged(Editable s) {
                mWeekWorkEachData.setWorkSite(s.toString());
            }
        });
        EditText mWorkContentView = (EditText) view.findViewById(R.id.week_work_plan_item_work_content);
        mWorkContentView.setText(mWeekWorkEachData.getWorkContent());
        mWorkContentView.addTextChangedListener(new MyTextWatcher() {
            @Override
            public void afterTextChanged(Editable s) {
                mWeekWorkEachData.setWorkContent(s.toString());
            }
        });
        EditText mParticipantsView = (EditText) view.findViewById(R.id.week_work_plan_item_work_participants);
        mParticipantsView.setText(mWeekWorkEachData.getParticipants());
        mParticipantsView.addTextChangedListener(new MyTextWatcher() {
            @Override
            public void afterTextChanged(Editable s) {
                mWeekWorkEachData.setParticipants(s.toString());
            }
        });

        // ******************************************************************************************************************************************

        // 设置删除回调接口
        ImageView mDeleteItem = (ImageView) view.findViewById(R.id.week_work_plan_item_delete);
        mDeleteItem.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mOnDeleteButtonClick != null) {
                    mOnDeleteButtonClick.onDeleteClick(v);
                } else {
                    ToastUtil.showToast(context, "删除回调接口没有设置");
                }
            }
        });

        // ******************************************************************************************************************************************

        // 开始与结束时间控件设置
        mStartTimeView = (EditText) view.findViewById(R.id.week_work_plan_item_start_time);
        mStartTimeView.setText(mWeekWorkEachData.getStartTime());
        mEndTimeView = (EditText) view.findViewById(R.id.week_work_plan_item_end_time);
        mEndTimeView.setText(mWeekWorkEachData.getEndTime());
        if (context instanceof FragmentActivity) {// 给开始与结束时间控件设置点击事件
            giveStartAndEndTimeSetOnClick((FragmentActivity) context);
        }

        // 星期控件设置，不可以被直接编辑
        mWeekView = (EditText) view.findViewById(R.id.week_work_plan_item_week);
        mWeekView.setText(mWeekWorkEachData.getWeek());
        setWeekViewText();// 根据开始和结束时间的文本变化来设置星期的文本

        // 设置出席领导人控件
        mAttendLeadView = (EditText) view.findViewById(R.id.week_work_plan_work_item_attend_lead);
        mAttendLeadView.setText(mWeekWorkEachData.getAttendLead());
        if (context instanceof FragmentActivity) {
            mAttendLeadView.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
                    showSelectPersonDialog((FragmentActivity) context);
                }
            });
        }
    }

    // 给开始与结束时间控件设置点击事件
    private void giveStartAndEndTimeSetOnClick(final FragmentActivity activity) {
        mStartTimeView.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                showTimeSelectDialog(activity, mStartTimeView, 0);
            }
        });
        mEndTimeView.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                showTimeSelectDialog(activity, mEndTimeView, 1);
            }
        });
    }

    // 具体的弹出时间选择对话框的方法，在方法中最后确定前，进行了时间限制判断
    private void showTimeSelectDialog(final FragmentActivity activity, final EditText editText, final int distinguish) {
        DatePickerFragment datePickerFragment = new DatePickerFragment(new DatePickerFragment.SetDateListener() {
            int ciShu = 0;

            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                final String dateTime = year + "-"
                        + StringUtils.lessThanNineConvertString(month + 1)
                        + "-"
                        + StringUtils.lessThanNineConvertString(dayOfMonth);
                if (ciShu == 0) {
                    TimePickerFragment timePickerFragment = new TimePickerFragment(new TimePickerFragment.SetTimeListener() {
                        @Override
                        public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                            String time = dateTime
                                    + " "
                                    + StringUtils
                                    .lessThanNineConvertString(hourOfDay)
                                    + ":"
                                    + StringUtils
                                    .lessThanNineConvertString(minute)
                                    + ":" + "00";

                            if (distinguish == 0) {
                                if (!StringUtils.isEmpty(mEndTimeView.getText().toString())) {// 设置开始时间,且有结束时间，所已需要对比
                                    if (!(TimeTool.textTimeToLongTime(mEndTimeView.getText().toString(), "yyyy-MM-dd HH:mm:ss") > TimeTool.textTimeToLongTime(time, "yyyy-MM-dd HH:mm:ss"))) {
                                        ToastUtil.showToast(ViewsUitls.getContext(), "结束时间必须大于开始时间");
                                        return;
                                    }
                                }
                                mWeekWorkEachData.setStartTime(time);
                            } else {
                                if (!StringUtils.isEmpty(mStartTimeView.getText().toString())) {// 设置结束时间,且有开始时间，所已需要对比
                                    if (!(TimeTool.textTimeToLongTime(time, "yyyy-MM-dd HH:mm:ss") > TimeTool.textTimeToLongTime(mStartTimeView.getText().toString(), "yyyy-MM-dd HH:mm:ss"))) {
                                        ToastUtil.showToast(ViewsUitls.getContext(), "结束时间必须大于开始时间");
                                        return;
                                    }
                                }
                                mWeekWorkEachData.setEndTime(time);
                            }
                            editText.setText(time);
                            ciShu = 0;
                        }
                    });
                    timePickerFragment.show(activity.getSupportFragmentManager(), "TimePickerFragment");
                    ciShu++;
                }
            }
        });
        datePickerFragment.show(activity.getSupportFragmentManager(), "DatePickerFragment");
    }

    // 根据开始和结束时间的文本变化来设置星期的文本
    private void setWeekViewText() {
        mStartTimeView.addTextChangedListener(new MyTextWatcher() {
            @Override
            public void afterTextChanged(Editable s) { // 这里的开始时间都是在确认时，就已经经过判断了
                if (!StringUtils.isEmpty(s.toString())) {
                    mTextChangedStartTime = new Date(TimeTool.textTimeToLongTime(s.toString(), "yyyy-MM-dd HH:mm:ss"));
                    compareStartAndEndTime();
                }
            }
        });
        mEndTimeView.addTextChangedListener(new MyTextWatcher() {// 这里的结束时间都是在确认时，就已经经过判断了
            @Override
            public void afterTextChanged(Editable s) {
                if (!StringUtils.isEmpty(s.toString())) {
                    mTextChangedEndTime = new Date(TimeTool.textTimeToLongTime(s.toString(), "yyyy-MM-dd HH:mm:ss"));
                    compareStartAndEndTime();
                }
            }
        });
    }

    // mTextChangedStartTime和mTextChangedEndTime不可能都为空
    @SuppressLint("SetTextI18n")
    public void compareStartAndEndTime() {
        if (mTextChangedStartTime != null && mTextChangedEndTime != null) {
            if (TimeTool.getTimerWeek(mTextChangedStartTime, "周").contains(TimeTool.getTimerWeek(mTextChangedEndTime, "周"))) {
                mWeekView.setText(TimeTool.getTimerWeek(mTextChangedStartTime, "周"));
            } else {
                mWeekView.setText(TimeTool.getTimerWeek(mTextChangedStartTime, "周") + "至" + TimeTool.getTimerWeek(mTextChangedEndTime, "周"));
            }
        } else if (mTextChangedStartTime != null) {
            mWeekView.setText(TimeTool.getTimerWeek(mTextChangedStartTime, "周") + "开始");
        } else if (mTextChangedEndTime != null) {
            mWeekView.setText(TimeTool.getTimerWeek(mTextChangedEndTime, "周") + "结束");
        }
        mWeekWorkEachData.setWeek(mWeekView.getText().toString());
    }

    // 弹出选择出席领导人的对话框
    private void showSelectPersonDialog(FragmentActivity activity) {
        if (mCanSelectPersonData != null && mCanSelectPersonData.size() > 0) {
            final List<Node> nodeList = new ArrayList<>();

            nodeList.add(new Node<Integer, Integer>(1, -1, "常州市司法局"));
            for (int i = 0; i < mCanSelectPersonData.size(); i++) {
                CanSelectAttendLead canSelectAttendLead = mCanSelectPersonData.get(i);
                int ID = i + 2;
                nodeList.add(new Node<Integer, Integer>(ID, 1, canSelectAttendLead.getDepartmentName()));

                List<String> leadName = canSelectAttendLead.getLeadName();

                for (int j = 0; j < leadName.size(); j++) {
                    nodeList.add(new Node<Integer, Integer>(ID * 10 + j, ID, leadName.get(j)));
                }
            }

            // 如此就不一样了
//            int i = 1;
//            int ID = i + 2;
//            nodeList.add(new Node(ID + "", "1", "局领导"));
//            nodeList.add(new Node("", ID + "", "陈向群"));
//            nodeList.add(new Node("",ID + "", "张家林"));

            SelectAttendLeadDialog selectAttendLeadDialog = new SelectAttendLeadDialog();
            selectAttendLeadDialog.setDialogData(nodeList);
            selectAttendLeadDialog.setOnSureButtonClick(new OnSureButtonClick() {
                @SuppressLint("SetTextI18n")
                @Override
                public void onSureClick(DialogInterface dialog, int id, List<Boolean> isChecks) {
                    mAttendLeadView.setText("");// 先清空原先的文本
                    for (int i = 0; i < nodeList.size(); i++) {
                        if (nodeList.get(i).isChecked() && nodeList.get(i).isLeaf()) {
                            mAttendLeadView.setText((StringUtils.isEmpty(mAttendLeadView.getText().toString()) ? "" : mAttendLeadView.getText().toString() + ";") + nodeList.get(i).getName());
                            mWeekWorkEachData.setAttendLead(mAttendLeadView.getText().toString());
                        }
                    }
                }
            });
            selectAttendLeadDialog.show(activity.getSupportFragmentManager(), "SelectAttendLeadDialog");
        } else {
            ToastUtil.showToast(ViewsUitls.getContext(), "无数据");
        }
    }

    // 对外使用设置删除按钮点击回调接口
    public void setOnDeleteButtonClick(OnDeleteButtonClick mOnDeleteButtonClick) {
        this.mOnDeleteButtonClick = mOnDeleteButtonClick;
    }

    // 对外使用设置选择出席领导所需要的可选领导数据
    public void setCanSelectPersonData(List<CanSelectAttendLead> mCanSelectPersonData) {
        this.mCanSelectPersonData = mCanSelectPersonData;
    }
}
