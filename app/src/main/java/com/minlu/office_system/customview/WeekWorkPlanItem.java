package com.minlu.office_system.customview;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.res.TypedArray;
import android.support.v4.app.FragmentActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.AttributeSet;
import android.view.View;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.TimePicker;

import com.minlu.baselibrary.util.StringUtils;
import com.minlu.baselibrary.util.TimeTool;
import com.minlu.baselibrary.util.ToastUtil;
import com.minlu.baselibrary.util.ViewsUitls;
import com.minlu.office_system.R;
import com.minlu.office_system.fragment.time.DatePickerFragment;
import com.minlu.office_system.fragment.time.TimePickerFragment;

import java.util.Date;
import java.util.List;

public class WeekWorkPlanItem extends LinearLayout {

    private boolean mItemOrganizationNameLl;
    private EditText mOrganizationNameView;

    private Date mTextChangedStartTime;
    private Date mTextChangedEndTime;
    private List<String> mCanSelectPersonData;
    private View llOrganizationName;

    public interface OnDeleteButtonClick {
        void onDeleteClick(View v);
    }

    private String mItemTopTitleText;
    private TextView mItemTopTitleView;
    private ImageView mDeleteItem;
    private EditText mParticipantsView;
    private EditText mAttendLeadView;
    private EditText mWorkContentView;
    private EditText mWorkSiteView;
    private EditText mWeekView;
    private EditText mEndTimeView;
    private EditText mStartTimeView;

    private OnDeleteButtonClick mOnDeleteButtonClick;

    public WeekWorkPlanItem(Context context) {
        super(context);
        // 直接代码创建本控件时调用此处
        initView(context);
    }

    public WeekWorkPlanItem(Context context, AttributeSet attrs) {
        super(context, attrs);

        TypedArray attributes = context.obtainStyledAttributes(attrs, R.styleable.WeekWorkPlanItem);
        // 通过R文件中的id来获取对应属性的值，要什么值，那方法就是get该值的类型,参数就是id值,给id是所要获取值的对应设置的名字，参数一是我们前面写的declare-styleable标签下的attr标签
        // 每个工作条目的顶部标题
        mItemTopTitleText = attributes.getString(R.styleable.WeekWorkPlanItem_each_work_title);
        mItemOrganizationNameLl = attributes.getBoolean(R.styleable.WeekWorkPlanItem_each_work_organization_name_ll, false);

        // 使用了本方法，最后必须调用下面的这个方法
        attributes.recycle();

        initView(context);
    }

    public WeekWorkPlanItem(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    private void initView(Context context) {
        View view = View.inflate(context, R.layout.item_week_work_plan, this);

        mItemTopTitleView = (TextView) view.findViewById(R.id.week_work_plan_item_title);
        if (!isEmpty(mItemTopTitleText)) {
            mItemTopTitleView.setText(mItemTopTitleText);
        } else {
            mItemTopTitleView.setText("标题");
        }

        llOrganizationName = view.findViewById(R.id.ll_week_work_plan_item_organization_name);
        if (mItemOrganizationNameLl) {
            llOrganizationName.setVisibility(VISIBLE);
        } else {
            llOrganizationName.setVisibility(GONE);
        }
        mOrganizationNameView = (EditText) view.findViewById(R.id.week_work_plan_item_organization_name);

        mWorkSiteView = (EditText) view.findViewById(R.id.week_work_plan_item_work_site);
        mWorkContentView = (EditText) view.findViewById(R.id.week_work_plan_item_work_content);
        mParticipantsView = (EditText) view.findViewById(R.id.week_work_plan_item_work_participants);

        mDeleteItem = (ImageView) view.findViewById(R.id.week_work_plan_item_delete);
        mDeleteItem.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mOnDeleteButtonClick != null) {
                    mOnDeleteButtonClick.onDeleteClick(v);
                }
            }
        });

        mStartTimeView = (EditText) view.findViewById(R.id.week_work_plan_item_start_time);
        mEndTimeView = (EditText) view.findViewById(R.id.week_work_plan_item_end_time);

        mWeekView = (EditText) view.findViewById(R.id.week_work_plan_item_week);
        setWeekViewText();

        mAttendLeadView = (EditText) view.findViewById(R.id.week_work_plan_work_item_attend_lead);
        mAttendLeadView.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                showSelectPersonDialog();
            }
        });
    }

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
                            } else {
                                if (!StringUtils.isEmpty(mStartTimeView.getText().toString())) {// 设置结束时间,且有开始时间，所已需要对比
                                    if (!(TimeTool.textTimeToLongTime(time, "yyyy-MM-dd HH:mm:ss") > TimeTool.textTimeToLongTime(mStartTimeView.getText().toString(), "yyyy-MM-dd HH:mm:ss"))) {
                                        ToastUtil.showToast(ViewsUitls.getContext(), "结束时间必须大于开始时间");
                                        return;
                                    }
                                }
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

    private void setWeekViewText() {
        mStartTimeView.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

            @Override
            public void afterTextChanged(Editable s) {
                if (!StringUtils.isEmpty(s.toString())) {
                    mTextChangedStartTime = new Date(TimeTool.textTimeToLongTime(s.toString(), "yyyy-MM-dd HH:mm:ss"));
                    compareStartAndEndTime();
                }
            }
        });
        mEndTimeView.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

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
    }

    private void showSelectPersonDialog() {
        if (mCanSelectPersonData != null && mCanSelectPersonData.size() > 0) {









        } else {
            ToastUtil.showToast(ViewsUitls.getContext(), "无数据");
        }
    }

    // 对外使用
    public void setOnDeleteButtonClick(OnDeleteButtonClick mOnDeleteButtonClick) {
        this.mOnDeleteButtonClick = mOnDeleteButtonClick;
    }

    // 对外使用
    public void giveStartAndEndTimeSetOnClick(final FragmentActivity activity) {
        mEndTimeView.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                showTimeSelectDialog(activity, mEndTimeView, 1);
            }
        });
        mStartTimeView.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                showTimeSelectDialog(activity, mStartTimeView, 0);
            }
        });
    }

    // 对外使用
    public void setCanSelectPersonData(List<String> mCanSelectPersonData) {
        this.mCanSelectPersonData = mCanSelectPersonData;
    }

    public List<String> getCanSelectPersonData() {
        return mCanSelectPersonData;
    }

    public TextView getItemTopTitleView() {
        return mItemTopTitleView;
    }

    public EditText getParticipantsView() {
        return mParticipantsView;
    }

    public EditText getAttendLeadView() {
        return mAttendLeadView;
    }

    public EditText getWorkContentView() {
        return mWorkContentView;
    }

    public EditText getWorkSiteView() {
        return mWorkSiteView;
    }

    public EditText getWeekView() {
        return mWeekView;
    }

    public EditText getEndTimeView() {
        return mEndTimeView;
    }

    public EditText getStartTimeView() {
        return mStartTimeView;
    }

    public EditText getOrganizationNameView() {
        return mOrganizationNameView;
    }

    public View getLlOrganizationName() {
        return llOrganizationName;
    }

    public boolean isEmpty(String value) {
        if (value != null && !"".equalsIgnoreCase(value.trim())
                && !"null".equalsIgnoreCase(value.trim())) {
            // 不为空
            return false;
        } else {
            // 为空
            return true;
        }
    }
}
