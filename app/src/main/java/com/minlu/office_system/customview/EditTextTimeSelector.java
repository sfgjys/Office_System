package com.minlu.office_system.customview;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.minlu.office_system.R;


public class EditTextTimeSelector extends LinearLayout {

    private String mEditTextLeftText;

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

    private void initView(Context context) {
        View view = View.inflate(context, R.layout.custom_selector_time_layout, this);
        TextView textView = (TextView) view.findViewById(R.id.tv_custom_selector_time_left);
        textView.setText(mEditTextLeftText);
    }
}
