package com.minlu.office_system.customview;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.minlu.baselibrary.util.StringUtils;
import com.minlu.office_system.R;

import java.util.Calendar;


public class EditTextTimeSelector extends LinearLayout {

    private String mEditTextLeftText;
    private DayOrTimeOnClickListener dayOrTimeOnClickListener;
    private OnSetTextListener onSetTextListener;
    private EditText mDayOfYear;
    private EditText mTimeOfDay;

    public EditTextTimeSelector(Context context) {
        super(context);
    }

    public EditTextTimeSelector(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public EditTextTimeSelector(Context context, AttributeSet attrs) {
        super(context, attrs);

        TypedArray attributes = context.obtainStyledAttributes(attrs, R.styleable.EditTextTimeSelector);
        // 通过R文件中的id来获取对应属性的值，要什么值，那方法就是get该值的类型,参数就是id值,给id是所要获取值的对应设置的名字，参数一是我们前面写的declare-styleable标签下的attr标签

        mEditTextLeftText = attributes.getString(R.styleable.EditTextTimeSelector_time_selector_left_text);// 左边文本

        // 使用了本方法，最后必须调用下面的这个方法
        attributes.recycle();

        initView(context);
    }

    /*初始化控件*/
    private void initView(Context context) {
        View view = View.inflate(context, R.layout.custom_selector_time_layout, this);
        TextView textView = (TextView) view.findViewById(R.id.tv_custom_selector_time_left);
        textView.setText(mEditTextLeftText);

        mDayOfYear = (EditText) view.findViewById(R.id.et_custom_selector_time_day);
        mDayOfYear.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if (dayOrTimeOnClickListener != null) {
                    dayOrTimeOnClickListener.onDayClick(v);
                }
            }
        });
        mTimeOfDay = (EditText) view.findViewById(R.id.et_custom_selector_time_24h);
        mTimeOfDay.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if (dayOrTimeOnClickListener != null) {
                    dayOrTimeOnClickListener.onTimeClick(v);
                }
            }
        });
    }

    public String getDayOrTimeText() {
        String timeText = null;
        String dayText = null;
        if (mDayOfYear != null) {
            dayText = mDayOfYear.getText().toString().trim();
        }
        if (mTimeOfDay != null) {
            timeText = mTimeOfDay.getText().toString().trim();
        }
        return dayText + " " + timeText;
    }


    /*给日期edittext设置文本，并有一个监听回调方法*/
    public void setDayOfYearText(String dayOfYearText) {
        mDayOfYear.setText(dayOfYearText);
        if (onSetTextListener != null) {
            onSetTextListener.onSetText();// 当设置了文本时被调用监听
        }
    }

    /*给时间edittext设置文本，并有一个监听回调方法*/
    public void setTimeOfDayText(String timeOfDayText) {
        mTimeOfDay.setText(timeOfDayText);
        if (onSetTextListener != null) {
            onSetTextListener.onSetText();// 当设置了文本时被调用监听
        }
    }

    /*给日期和时间的edittext设置现在时间的文本*/
    public void setNowDayOfYearAndTimeOfDay() {
        // TODO 第一次显示现在的时间文本,注意格式
        Calendar calendar = Calendar.getInstance();
        setDayOfYearText(calendar.get(Calendar.YEAR) + "-" +
                StringUtils.lessThanNineConvertString(calendar.get(Calendar.MONTH) + 1) + "-" +
                StringUtils.lessThanNineConvertString(calendar.get(Calendar.DAY_OF_MONTH)));
        setTimeOfDayText(StringUtils.lessThanNineConvertString(calendar.get(Calendar.HOUR_OF_DAY)) + ":" +
                StringUtils.lessThanNineConvertString(calendar.get(Calendar.MINUTE)));
    }

    /*设置点击edittext的监听回调*/
    public void setDayOrTimeOnClickListener(DayOrTimeOnClickListener dayOrTimeOnClickListener) {
        this.dayOrTimeOnClickListener = dayOrTimeOnClickListener;
    }

    /*设置edittext文本进行设置时的监听回调*/
    public void setOnSetTextListener(OnSetTextListener onSetTextListener) {
        this.onSetTextListener = onSetTextListener;
    }

    public EditText getmTimeOfDay() {
        return mTimeOfDay;
    }

    public EditText getmDayOfYear() {
        return mDayOfYear;
    }

    public interface DayOrTimeOnClickListener {
        void onTimeClick(View v);

        void onDayClick(View v);
    }

    public interface OnSetTextListener {
        void onSetText();
    }
}
