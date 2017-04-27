package com.minlu.office_system.activity;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.Toast;

import com.minlu.baselibrary.base.BaseActivity;
import com.minlu.baselibrary.util.ToastUtil;
import com.minlu.office_system.R;
import com.minlu.office_system.StringsFiled;
import com.minlu.office_system.fragment.form.RecordManagementFragment;
import com.minlu.office_system.fragment.form.formPremise.AllForms;
import com.minlu.office_system.fragment.form.formPremise.FormFragment;

public class FormActivity extends BaseActivity implements View.OnClickListener {

    private LinearLayout twoIsAgreeButtons;
    private Button submitButton;
    private View mContent;
    private String mFragmentTag;
    private ScrollView scrollView;
    private int formTypePosition;

    @Override
    public void onCreateContent() {
        mContent = setContent(R.layout.activity_form);

        initView();

        formTypePosition = getmIntent().getIntExtra(StringsFiled.HOME_PAGE_TO_FORM_LIST_POSITION, -1);

        showWhichButton(AllForms.values()[formTypePosition].getShowWhichButton());

        showFormFragment();
    }

    private void showFormFragment() {
        // 根据Class<?>创建Fragment，并开启Fragment
        Class<?> aClass = AllForms.values()[formTypePosition].getFormClassName();
        mFragmentTag = AllForms.values()[formTypePosition].getFragmentTAG();

        try {
            Fragment fragmentByTag = getSupportFragmentManager().findFragmentByTag(mFragmentTag);
            if (fragmentByTag != null) {
                getSupportFragmentManager().beginTransaction().show(fragmentByTag).commit();
            } else {
                Fragment fragment = (Fragment) aClass.newInstance();
                boolean isUseScroll = AllForms.values()[formTypePosition].isAddFragmentIsUseScroll();
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
                showAgreeSubmitButton(View.VISIBLE, View.GONE);
                break;
            case StringsFiled.SHOW_SUBMIT_BUTTON:
                showAgreeSubmitButton(View.GONE, View.VISIBLE);
                break;
            case StringsFiled.NO_SHOW_BUTTON:
                showAgreeSubmitButton(View.GONE, View.GONE);
                break;
            case -1:
                ToastUtil.showToast(getApplication(), "home to form show which failure");
                break;
        }
    }

    public void showAgreeSubmitButton(int agreeShow, int submitShow) {
        twoIsAgreeButtons.setVisibility(agreeShow);
        submitButton.setVisibility(submitShow);
    }

    /*
    * 设置R.id.sv_replace_form控件的展示形式
    * */
    public void setScrollViewNoGravity() {
        ScrollView.LayoutParams layoutParams = (ScrollView.LayoutParams) scrollView.getLayoutParams();
        layoutParams.gravity = Gravity.NO_GRAVITY;
        scrollView.setLayoutParams(layoutParams);
    }

    private static final int FILE_SELECT_CODE = 0;

    /* 调用系统文件管理器后对结果的处理 */
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode != Activity.RESULT_OK) {// 调用文件管理器失败
            Log.e("FormActivity", "onActivityResult() error, resultCode: " + resultCode);
            super.onActivityResult(requestCode, resultCode, data);
            return;
        }
        if (requestCode == FILE_SELECT_CODE) {// 调用文件管理器成功
            Uri uri = data.getData();
            RecordManagementFragment recordManagementFragment = (RecordManagementFragment) getSupportFragmentManager().findFragmentByTag(StringsFiled.RecordManagementFragment_TAG);
            if (recordManagementFragment != null) {
                recordManagementFragment.returnFilePath(uri.getPath());
            }
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    /* 调用系统文件管理器 */
    public void chooseFile() {
        Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
        intent.setType("*/*");
        intent.addCategory(Intent.CATEGORY_OPENABLE);
        try {
            startActivityForResult(Intent.createChooser(intent, "选择文件"), FILE_SELECT_CODE);
        } catch (android.content.ActivityNotFoundException ex) {
            Toast.makeText(this, "亲，木有文件管理器啊-_-!!", Toast.LENGTH_SHORT).show();
        }
    }
}
