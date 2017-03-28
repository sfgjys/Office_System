package com.minlu.office_system.activity;

import android.support.v4.app.Fragment;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ScrollView;

import com.minlu.baselibrary.base.BaseActivity;
import com.minlu.baselibrary.util.ToastUtil;
import com.minlu.office_system.R;
import com.minlu.office_system.StringsFiled;
import com.minlu.office_system.fragment.form.formPremise.FormFragment;

public class FormActivity extends BaseActivity implements View.OnClickListener {

    private LinearLayout twoIsAgreeButtons;
    private Button submitButton;
    private View mContent;
    private String mFragmentTag;
    private ScrollView scrollView;

    @Override
    public void onCreateContent() {
        mContent = setContent(R.layout.activity_form);

        initView();

        showWhichButton(getmIntent().getIntExtra(StringsFiled.TO_FORM_SHOW_WHICH_BUTTON, -1));

        // 根据Class<?>创建Fragment，并开启Fragment
        Class<?> aClass = (Class<?>) getmIntent().getSerializableExtra(StringsFiled.TO_FORM_SHOW_FORM_FRAGMENT);
        mFragmentTag = getmIntent().getStringExtra(StringsFiled.TO_FORM_SHOW_FORM_FRAGMENT_TAG);


        try {
            Fragment fragmentByTag = getSupportFragmentManager().findFragmentByTag(mFragmentTag);
            if (fragmentByTag != null) {
                getSupportFragmentManager().beginTransaction().show(fragmentByTag).commit();
            } else {
                Fragment fragment = (Fragment) aClass.newInstance();
                boolean isUseScroll = getmIntent().getBooleanExtra(StringsFiled.TO_FORM_SHOW_IS_USE_SCROLL, false);
                if (isUseScroll) {
                    getSupportFragmentManager().beginTransaction().add(R.id.sv_replace_form, fragment, mFragmentTag).commit();
                } else {
                    scrollView.setVisibility(View.GONE);
                    getSupportFragmentManager().beginTransaction().add(R.id.fl_replace_form, fragment, mFragmentTag).commit();
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            ToastUtil.showToast(getApplication(), "程序异常");
        }
    }

    private void initView() {
        scrollView = (ScrollView) mContent.findViewById(R.id.sv_replace_form);
        twoIsAgreeButtons = (LinearLayout) mContent.findViewById(R.id.ll_dis_agree_button);
        Button agreeButton = (Button) mContent.findViewById(R.id.bt_agree_button);
        agreeButton.setOnClickListener(this);
        Button disagreeButton = (Button) mContent.findViewById(R.id.bt_disagree_button);
        disagreeButton.setOnClickListener(this);
        submitButton = (Button) mContent.findViewById(R.id.bt_submit_button);
        submitButton.setOnClickListener(this);
    }

    /*
    * 三个按钮的点击事件,同过获取在activity上显示的Fragment对象,调用对应的方法,让对应的Fragment对象自己实现
    * */
    @Override
    public void onClick(View view) {
        FormFragment formFragment = (FormFragment) getSupportFragmentManager().findFragmentByTag(mFragmentTag);
        switch (view.getId()) {
            case R.id.bt_agree_button:
                formFragment.agreeOnClick(view);
                break;
            case R.id.bt_disagree_button:
                formFragment.disAgreeOnClick(view);
                break;
            case R.id.bt_submit_button:
                formFragment.submitOnClick(view);
                break;
        }
    }

    /*
    * 展示哪组按钮
    * */
    private void showWhichButton(int whichButton) {
        switch (whichButton) {
            case StringsFiled.SHOW_IS_AGREE_BUTTON:
                twoIsAgreeButtons.setVisibility(View.VISIBLE);
                submitButton.setVisibility(View.GONE);
                break;
            case StringsFiled.SHOW_SUBMIT_BUTTON:
                twoIsAgreeButtons.setVisibility(View.GONE);
                submitButton.setVisibility(View.VISIBLE);
                break;
            case StringsFiled.NO_SHOW_BUTTON:
                twoIsAgreeButtons.setVisibility(View.GONE);
                submitButton.setVisibility(View.GONE);
                break;
            case -1:
                ToastUtil.showToast(getApplication(), "home to form show which failure");
                break;
        }
    }


    /*
    * 设置R.id.sv_replace_form控件的展示形式
    * */
    public void setScrollViewNoGravity() {
        ScrollView.LayoutParams layoutParams = (ScrollView.LayoutParams) scrollView.getLayoutParams();
        layoutParams.gravity = Gravity.NO_GRAVITY;
        scrollView.setLayoutParams(layoutParams);
    }
}
