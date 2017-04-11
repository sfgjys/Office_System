package com.minlu.office_system.fragment.form;

import android.content.DialogInterface;
import android.view.View;

import com.minlu.baselibrary.base.ContentPage;
import com.minlu.baselibrary.util.StringUtils;
import com.minlu.baselibrary.util.ViewsUitls;
import com.minlu.office_system.R;
import com.minlu.office_system.activity.FormActivity;
import com.minlu.office_system.bean.CheckBoxChild;
import com.minlu.office_system.customview.EditTextItem;
import com.minlu.office_system.fragment.dialog.OnSureButtonClick;
import com.minlu.office_system.fragment.dialog.PromptDialog;
import com.minlu.office_system.fragment.dialog.SelectNextUserDialog;
import com.minlu.office_system.fragment.form.formPremise.FormFragment;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import okhttp3.Response;

/**
 * Created by user on 2017/3/7.
 */

public class PostManagementFragment extends FormFragment {

    private List<String> excessive;
    private String mDrafterText = "";
    private String mMainSendOfficeText = "";
    private String mPostTitleText = "";
    private String mPostTypeText = "";
    private String mMainOfficeText = "";
    private List<CheckBoxChild> mNextUsers;
    private EditTextItem mApproveIdea;

    @Override
    protected void onSubClassOnCreateView() {
        loadDataAndRefresh();
    }

    @Override
    protected View onCreateSuccessView() {
        // 因为本fragment是通过R.id.sv_replace_form控件replace开启的，但是R.id.sv_replace_form控件是居中属性，所以再次我们要使得居中属性去除
        FormActivity formActivity = (FormActivity) getContext();
        if (formActivity != null) {
            formActivity.setScrollViewNoGravity();
        }

        View inflate = ViewsUitls.inflate(R.layout.form_post_management);

        initView(inflate);

        return inflate;
    }

    private void initView(View inflate) {
        EditTextItem drafter = (EditTextItem) inflate.findViewById(R.id.form_post_management_drafter);
        drafter.setEditText(mDrafterText);
        EditTextItem mainOffice = (EditTextItem) inflate.findViewById(R.id.form_post_management_main_office);
        mainOffice.setEditText(mMainOfficeText);
        ViewsUitls.setWidthFromTargetView(mainOffice.getCustomEditTextLeft(), drafter.getCustomEditTextLeft());

        EditTextItem postNumber = (EditTextItem) inflate.findViewById(R.id.form_post_management_post_number);
        EditTextItem postTitle = (EditTextItem) inflate.findViewById(R.id.form_post_management_post_title);
        postTitle.setEditText(mPostTitleText);
        EditTextItem mainSendOffice = (EditTextItem) inflate.findViewById(R.id.form_post_management_main_send_office);
        mainSendOffice.setEditText(mMainSendOfficeText);

        mApproveIdea = (EditTextItem) inflate.findViewById(R.id.form_post_management_approve_idea);

        EditTextItem postType = (EditTextItem) inflate.findViewById(R.id.form_post_management_post_type);
        postType.setEditText(mPostTypeText);
//        final EditText postTypeEditText = postType.getCustomEditTextRight();
//        // 设置展示类型选择列表
//        setWhichViewShowListPopupWindow(postTypeEditText, mPostTypeData, new ShowListPopupItemClickListener() {
//            @Override
//            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
//                postTypeEditText.setText(mPostTypeData.get(position));
//            }
//
//            @Override
//            public void onAnchorViewClick(View v) {
//                setBackGroundDarkColor(0.6f);
//            }
//
//            @Override
//            public void onListPopupDismiss() {
//                setBackGroundDarkColor(1.0f);
//            }
//        }, getActivity());

        EditTextItem isOpen = (EditTextItem) inflate.findViewById(R.id.form_post_management_is_open);
//        // 设置展示是否公开列表
//        final EditText isOpenEditText = isOpen.getCustomEditTextRight();
//        setWhichViewShowListPopupWindow(isOpenEditText, mYesOrNo, new ShowListPopupItemClickListener() {
//            @Override
//            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
//                isOpenEditText.setText(mYesOrNo.get(position));
//            }
//
//            @Override
//            public void onAnchorViewClick(View v) {
//                setBackGroundDarkColor(0.6f);
//            }
//
//            @Override
//            public void onListPopupDismiss() {
//                setBackGroundDarkColor(1.0f);
//            }
//        }, getActivity());
    }

    @Override
    protected ContentPage.ResultState onLoad() {

        Response response = requestFormListItemDetail();

        if (response != null && response.isSuccessful()) {
            try {
                String resultList = response.body().string();
                if (StringUtils.interentIsNormal(resultList)) {
                    JSONObject jsonObject = new JSONObject(resultList);
                    if (jsonObject.has("TITLE")) {// 有标题字段，说明返回的数据正常
                        excessive = new ArrayList<>();
                        excessive.add("excessive");// 给excessive创建实例，并添加元素，让界面走onCreateSuccessView()方法

                        mDrafterText = jsonObject.optString("DRAFTER");
                        mMainOfficeText = jsonObject.optString("ZBCS");
                        mPostTypeText = jsonObject.optString("DOCUMENT_TYPE");
                        mPostTitleText = jsonObject.optString("TITLE");
                        mMainSendOfficeText = jsonObject.optString("ZSJG");

                        JSONArray jsonArray = jsonObject.optJSONArray("USERLIST");
                        mNextUsers = new ArrayList<>();
                        for (int i = 0; i < jsonArray.length(); i++) {
                            JSONObject nextUserData = jsonArray.getJSONObject(i);
                            mNextUsers.add(new CheckBoxChild(nextUserData.optString("TRUENAME"), nextUserData.optString("USERNAME"), nextUserData.optString("ORG_INFOR")));
                        }
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return chat(excessive);
    }

    @Override
    public void disAgreeOnClick(View v) {
        PromptDialog promptDialog = new PromptDialog(new PromptDialog.OnSureButtonClick() {
            @Override
            public void onSureClick(DialogInterface dialog, int id) {
                System.out.println("PostManagementFragment-disAgreeOnClick");
            }
        }, "是否不同意该发文拟稿 !");
        promptDialog.show(getActivity().getSupportFragmentManager(), "PostManagementDisAgree");
    }

    @Override
    public void agreeOnClick(View v) {
        SelectNextUserDialog selectNextUserDialog = new SelectNextUserDialog();
        selectNextUserDialog.setCheckBoxTexts(mNextUsers);
        selectNextUserDialog.setOnSureButtonClick(new OnSureButtonClick() {
            @Override
            public void onSureClick(DialogInterface dialog, int id, List<Boolean> isChecks) {
                List<CheckBoxChild> sureUsers = new ArrayList<>();
                // 通过isChecks集合中的选择数据去判断哪些数据选中，并将选中的数据填进sureUsers集合中
                for (int i = 0; i < isChecks.size(); i++) {
                    if (isChecks.get(i)) {
                        sureUsers.add(mNextUsers.get(i));
                    }
                }
                String approveIdea = mApproveIdea.getCustomEditTextRight().getText().toString();
                System.out.println(approveIdea + sureUsers.size());
                // TODO 使用sureUsers集合和审批意见去进行网络请求
            }
        });
        selectNextUserDialog.show(getActivity().getSupportFragmentManager(), "PostManagementAgree");
    }

    @Override
    public void submitOnClick(View v) {
        System.out.println("PostManagementFragment-submitOnClick");
    }
}
