package com.minlu.office_system.customview;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.minlu.baselibrary.util.StringUtils;
import com.minlu.office_system.R;


public class EditTextItem extends LinearLayout {

    private String mEditTextHint;
    private int mEditTextRightIcon;
    private String mEditTextLeftText;
    private boolean mEditTextIsEdit;
    private EditText customEditTextRight;
    private int mEditTextInputType;
    private int mEditTextMaxLines;
    private boolean mEditTextIsClickable;
    private TextView customEditTextLeft;

    public EditTextItem(Context context) {
        super(context);
    }

    public EditTextItem(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public EditTextItem(Context context, AttributeSet attrs) {
        super(context, attrs);

        TypedArray attributes = context.obtainStyledAttributes(attrs, R.styleable.EditTextItem);
        // 通过R文件中的id来获取对应属性的值，要什么值，那方法就是get该值的类型,参数就是id值,给id是所要获取值的对应设置的名字，参数一是我们前面写的declare-styleable标签下的attr标签

        mEditTextHint = attributes.getString(R.styleable.EditTextItem_editText_hint);// 提示文本
        mEditTextLeftText = attributes.getString(R.styleable.EditTextItem_editText_left_text);// 左边文本

        mEditTextRightIcon = attributes.getResourceId(R.styleable.EditTextItem_editText_right_icon, 0);// 右边图标

        mEditTextIsEdit = attributes.getBoolean(R.styleable.EditTextItem_editText_is_edit, true);// 没使用该属性就返回默认值true，默认是可以编辑的
        mEditTextIsClickable = attributes.getBoolean(R.styleable.EditTextItem_editText_is_clickable, true);// 没使用该属性就返回默认值true，默认是可以点击的

        mEditTextInputType = attributes.getInt(R.styleable.EditTextItem_editText_input_type, -1);// 文本输入类型
        mEditTextMaxLines = attributes.getInteger(R.styleable.EditTextItem_editText_max_lines, -1);// 最大显示行数

        // 使用了本方法，最后必须调用下面的这个方法
        attributes.recycle();

        initView(context);
    }

    private void initView(Context context) {
        View view = View.inflate(context, R.layout.custom_edit_text_layout, this);

        customEditTextLeft = (TextView) view.findViewById(R.id.tv_custom_edit_text_left);
        customEditTextLeft.setText(mEditTextLeftText);

        customEditTextRight = (EditText) view.findViewById(R.id.et_custom_edit_text_right);
        customEditTextRight.setHint(mEditTextHint);
        if (mEditTextRightIcon != 0) {
            Drawable drawable = getResources().getDrawable(mEditTextRightIcon);
            drawable.setBounds(0, 0, drawable.getMinimumWidth(), drawable.getMinimumHeight());
            customEditTextRight.setCompoundDrawables(null, null, drawable, null);
        }
        if (mEditTextInputType != -1) {
            customEditTextRight.setInputType(mEditTextInputType);
        }
        if (mEditTextMaxLines != -1) {
            System.out.println("=================================================");
            customEditTextRight.setMaxLines(mEditTextMaxLines);
        }

        // EditText不一直显示边框颜色。但无法编辑
        customEditTextRight.setFocusableInTouchMode(mEditTextIsEdit);
        customEditTextRight.setFocusable(mEditTextIsEdit);
        // EditText可以一直显示边框颜色。但无法编辑
//        if (!mEditTextIsEdit) {
//            customEditTextRight.setKeyListener(null);
//        }

        customEditTextRight.setEnabled(mEditTextIsClickable);
    }

    public TextView getCustomEditTextLeft() {
        return customEditTextLeft;
    }

    public EditText getCustomEditTextRight() {
        return customEditTextRight;
    }

    public void setEditText(String editText) {
        customEditTextRight.setText(editText);
    }

    public void setEditTextGistIsEmpty(String editText) {
        if (StringUtils.isEmpty(editText)) {
            this.setVisibility(GONE);
        } else {
            customEditTextRight.setText(editText);
        }
    }

    public interface OnCustomTouchToClick {
        void onCustomTouch(View v, MotionEvent event);
    }

    public void setCustomOnTouch(final OnCustomTouchToClick customOnTouch) {
        customEditTextRight.setOnTouchListener(new OnTouchListener() {
            //按住和松开的标识
            int touch_flag = 0;

            @Override
            public boolean onTouch(View v, MotionEvent event) {
                touch_flag++;
                if (touch_flag == 2) {
                    touch_flag = 0;
                    //自己业务
                    customOnTouch.onCustomTouch(v, event);
                }
                return false;
            }
        });
    }
}
