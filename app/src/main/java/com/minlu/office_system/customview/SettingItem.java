package com.minlu.office_system.customview;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.minlu.office_system.R;


public class SettingItem extends RelativeLayout {

    private View view;
    private int mTubiao;
    private String mMiaoshu;
    private int mIsswitch;
    private int mIsright;
    private int mIscache;
    private TextView mCache;
    private ImageView mSwitch;
    private boolean mIsOnOff;
    private int mTextStyleId;

    public SettingItem(Context context) {
        super(context);
    }

    public SettingItem(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public SettingItem(Context context, AttributeSet attrs) {
        super(context, attrs);
        view = View.inflate(context, R.layout.settings_item, this);

        TypedArray attributes = context.obtainStyledAttributes(attrs,
                R.styleable.SettingItem);
        // 通过R文件中的id来获取对应属性的值，要什么值，那方法就是get该值的类型,参数就是id值,给id是所要获取值的对应设置的名字，参数一是我们前面写的declare-styleable标签下的attr标签

        mIscache = attributes.getInt(R.styleable.SettingItem_iscache, 0);
        mIsright = attributes.getInt(R.styleable.SettingItem_isright, 0);
        mIsswitch = attributes.getInt(R.styleable.SettingItem_isswitch, 0);

        mMiaoshu = attributes.getString(R.styleable.SettingItem_setting_itm_miaoshu);

        mTubiao = attributes.getResourceId(R.styleable.SettingItem_setting_itm_tubiao, 0);

        mTextStyleId = attributes.getResourceId(R.styleable.SettingItem_describeTextStyle, 0);

        // 使用了本方法，最后必须调用下面的这个方法
        attributes.recycle();

        initView();

    }

    private void initView() {

        ImageView mIcon = (ImageView) view.findViewById(R.id.iv_setting_icon);
        mIcon.setImageResource(mTubiao);

        TextView mDescribe = (TextView) view.findViewById(R.id.tv_setting_miaoshu);
        mDescribe.setText(mMiaoshu);
        mDescribe.setTextAppearance(getContext(), mTextStyleId);

        ImageView mToRight = (ImageView) view.findViewById(R.id.iv_setting_ico_right);
        setIsShow(mToRight, mIsright);

        mCache = (TextView) view.findViewById(R.id.tv_setting_cache_size);
        setIsShow(mCache, mIscache);

        mSwitch = (ImageView) view.findViewById(R.id.iv_setting_switch);
        setIsShow(mSwitch, mIsswitch);

    }

    public ImageView getSwitchImage() {
        return mSwitch;
    }

    public boolean getSwitchState() {
        return mIsOnOff;
    }

    public void setSwitchImage(boolean isonoff) {
        this.mIsOnOff = isonoff;
        if (isonoff) {
            // 开
            mSwitch.setImageResource(R.mipmap.setting_switch_open);
        } else {
            // 闭
            mSwitch.setImageResource(R.mipmap.setting_switch_close);
        }
    }

    private void setIsShow(View view, int num) {
        if (num == 0) {
            view.setVisibility(View.VISIBLE);
        } else if (num == 1) {
            view.setVisibility(View.INVISIBLE);
        } else if (num == 1) {
            view.setVisibility(View.GONE);
        }
    }

    public void setCacheText(String cache) {
        mCache.setText(cache);
    }

}
