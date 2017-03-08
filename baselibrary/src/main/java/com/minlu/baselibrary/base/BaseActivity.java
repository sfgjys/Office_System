package com.minlu.baselibrary.base;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import com.minlu.baselibrary.R;
import com.minlu.baselibrary.BaseStringsFiled;
import com.minlu.baselibrary.customview.MyLinearLayout;
import com.minlu.baselibrary.util.StringUtils;


/**
 * Created by user on 2016/11/21.
 */
public abstract class BaseActivity extends FragmentActivity {

    private ImageView mBaseBack;
    private MyLinearLayout mMyLinearLayout;

    private TextView mBaseTitle;

    private FrameLayout mBaseContetn;
    public Bundle savedInstanceState;
    public String stringTitle;
    private FrameLayout mBaseLoading;
    private Intent mIntent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.base_activity);

        this.savedInstanceState = savedInstanceState;

        initView();

        setTitleText();

        onCreateContent();
    }

    private void setTitleText() {
        mIntent = getIntent();
        if (mIntent != null) {
            stringTitle = mIntent.getStringExtra(BaseStringsFiled.ACTIVITY_TITLE);
            if (!StringUtils.isEmpty(stringTitle)) {
                mBaseTitle.setText(stringTitle);
            }
        }
    }

    public Intent getmIntent() {
        return mIntent;
    }

    public abstract void onCreateContent();

    private void initView() {
        mBaseBack = (ImageView) findViewById(R.id.iv_title_back);

        mBaseTitle = (TextView) findViewById(R.id.tv_title_text);

        mBaseContetn = (FrameLayout) findViewById(R.id.fl_base_content);

        mMyLinearLayout = (MyLinearLayout) findViewById(R.id.my_linear_layout);

        mBaseLoading = (FrameLayout) findViewById(R.id.fl_loading);

        mBaseBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

    }

    public void setLoadingVisibility(int visibility) {
        mBaseLoading.setVisibility(visibility);
    }

    public void setIsInterruptTouch(boolean is) {
        mMyLinearLayout.setIsInterruptTouch(is);
    }

    public void setTitleVisibility(int visibility) {
        mBaseTitle.setVisibility(visibility);
    }

    public void setBackVisibility(int visibility) {
        mBaseBack.setVisibility(visibility);
    }

    /**
     * 首先将一个xml布局打气压缩成一个View，在将该View添加到Framelayout中
     */
    public View setContent(int id) {
        View inflate = View.inflate(BaseApplication.getContext(), id, null);
        mBaseContetn.addView(inflate);
        return inflate;
    }

}
