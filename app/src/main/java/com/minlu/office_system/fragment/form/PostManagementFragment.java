package com.minlu.office_system.fragment.form;

import android.content.DialogInterface;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.minlu.baselibrary.base.ContentPage;
import com.minlu.baselibrary.util.StringUtils;
import com.minlu.baselibrary.util.ViewsUitls;
import com.minlu.office_system.IpFiled;
import com.minlu.office_system.R;
import com.minlu.office_system.activity.FormActivity;
import com.minlu.office_system.bean.CheckBoxChild;
import com.minlu.office_system.customview.EditTextItem;
import com.minlu.office_system.fragment.dialog.PromptDialog;
import com.minlu.office_system.fragment.form.formPremise.FormFragment;
import com.minlu.office_system.http.OkHttpMethod;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import okhttp3.Call;
import okhttp3.Callback;
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
    private String mSecretGradeText = "";
    private List<String> mDownloadFileName;
    private List<String> mDownloadFilePath;
    private String mNuclearDraftText;
    private String mOfficeNuclearDraftIdeaText;
    private String mAssignee;
    private String mTaskName;

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
        // 正常显示数据
        EditTextItem drafter = (EditTextItem) inflate.findViewById(R.id.form_post_management_drafter);
        drafter.setEditText(mDrafterText);
        EditTextItem mainOffice = (EditTextItem) inflate.findViewById(R.id.form_post_management_main_office);
        mainOffice.setEditText(mMainOfficeText);
        ViewsUitls.setWidthFromTargetView(mainOffice.getCustomEditTextLeft(), drafter.getCustomEditTextLeft());
        EditTextItem postTitle = (EditTextItem) inflate.findViewById(R.id.form_post_management_post_title);
        postTitle.setEditText(mPostTitleText);
        EditTextItem mainSendOffice = (EditTextItem) inflate.findViewById(R.id.form_post_management_main_send_office);
        mainSendOffice.setEditText(mMainSendOfficeText);
        EditTextItem postType = (EditTextItem) inflate.findViewById(R.id.form_post_management_post_type);
        postType.setEditText(mPostTypeText);
        EditTextItem secretGrade = (EditTextItem) inflate.findViewById(R.id.form_post_management_secret_grade);
        ViewsUitls.setWidthFromTargetView(mainOffice.getCustomEditTextLeft(), secretGrade.getCustomEditTextLeft());
        secretGrade.setEditText(mSecretGradeText);

        // 暂无数据
        EditTextItem postNumber = (EditTextItem) inflate.findViewById(R.id.form_post_management_post_number);

        // 各个步骤审批意见
        EditTextItem officeNuclearDraftIdea = (EditTextItem) inflate.findViewById(R.id.form_post_management_office_nuclear_draft_idea);
        officeNuclearDraftIdea.setEditText(mOfficeNuclearDraftIdeaText);
        EditTextItem nuclearDraftIdea = (EditTextItem) inflate.findViewById(R.id.form_post_management_nuclear_draft_idea);
        nuclearDraftIdea.setEditText(mNuclearDraftText);
        ViewsUitls.setWidthFromTargetView(officeNuclearDraftIdea.getCustomEditTextLeft(), nuclearDraftIdea.getCustomEditTextLeft());

        mApproveIdea = (EditTextItem) inflate.findViewById(R.id.form_post_management_approve_idea);

        setDownloadView(inflate);
    }

    /* 设置下载控件操作 */
    private void setDownloadView(View inflate) {
        View details = inflate.findViewById(R.id.form_post_management_details);
        LinearLayout accessoryList = (LinearLayout) inflate.findViewById(R.id.form_post_management_details_right);
        if (mDownloadFileName.size() > 0) {
            details.setVisibility(View.VISIBLE);
            for (int i = 0; i < mDownloadFileName.size(); i++) {
                TextView textView = (TextView) ViewsUitls.inflate(R.layout.item_accessory_list);
                textView.setText(mDownloadFileName.get(i));
                final int pressIndex = i;
                textView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        final String fileName = mDownloadFileName.get(pressIndex);
                        final String filePath = mDownloadFilePath.get(pressIndex);
                        PromptDialog promptDialog = new PromptDialog(new PromptDialog.OnSureButtonClick() {
                            @Override
                            public void onSureClick(DialogInterface dialog, int id) {
                                startDownLoad(fileName, filePath);
                            }
                        }, "是否下载 “ " + fileName + " ” 附件");
                        promptDialog.show(getActivity().getSupportFragmentManager(), "PostManagementDisAgree");
                    }
                });
                textView.setPadding(ViewsUitls.dpToPx(0), ViewsUitls.dpToPx(6), ViewsUitls.dpToPx(0), ViewsUitls.dpToPx(6));
                accessoryList.addView(textView);
            }
        }
    }

    // TODO 下载附件的方法
    private void startDownLoad(String fileName, String filePath) {
        HashMap<String, String> hashMap = new HashMap<>();
        hashMap.put("oldfilename", fileName);
        hashMap.put("path", filePath);
        OkHttpMethod.asynPostRequest(IpFiled.DOWNLOAD_ACCESSORY, hashMap, new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                System.out.println();
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                System.out.println();
            }
        });
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
                        analyticalData(jsonObject);
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return chat(excessive);
    }

    private void analyticalData(JSONObject jsonObject) throws JSONException {
        // 用于表单界面展示的数据
        mDrafterText = jsonObject.optString("DRAFTER");
        mMainOfficeText = jsonObject.optString("ZBCS");
        mPostTypeText = jsonObject.optString("DOCUMENT_TYPE");
        mPostTitleText = jsonObject.optString("TITLE");
        mMainSendOfficeText = jsonObject.optString("ZSJG");
        mSecretGradeText = jsonObject.optString("MIJI");

        // 下载附件数据
        JSONArray fileList = jsonObject.optJSONArray("FILELIST");
        mDownloadFileName = new ArrayList<>();
        mDownloadFilePath = new ArrayList<>();
        if (fileList != null) {
            for (int i = 0; i < fileList.length(); i++) {
                JSONObject file = fileList.getJSONObject(i);
                mDownloadFileName.add(file.optString("FILE_NAME"));
                mDownloadFilePath.add(file.optString("FILE_PATH"));
            }
        }

        // 后面的接口需要到的数据
        mAssignee = jsonObject.optString("ASSIGNEE");
        mTaskName = jsonObject.optString("TASKNAME");

        // 获取审批建议
        getAllSuggest(new AnalysisJSON() {
            @Override
            public void analysisJSON(JSONObject jsonObject) {
                if (jsonObject.has("rect3suggest")) {
                    mNuclearDraftText = jsonObject.optString("rect3suggest");
                }
                if (jsonObject.has("rect4suggest")) {
                    mOfficeNuclearDraftIdeaText = jsonObject.optString("rect4suggest");
                }
                excessive = new ArrayList<>();
                excessive.add("excessive");// 给excessive创建实例，并添加元素，让界面走onCreateSuccessView()方法
            }
        });
    }

    @Override
    public void disAgreeOnClick(View v) {
        PromptDialog promptDialog = new PromptDialog(new PromptDialog.OnSureButtonClick() {
            @Override
            public void onSureClick(DialogInterface dialog, int id) {
                officialLeaveApply("", -1);
            }
        }, "是否不同意该发文拟稿 !");
        promptDialog.show(getActivity().getSupportFragmentManager(), "PostManagementDisAgree");
    }

    @Override
    public void agreeOnClick(View v) {
        getNextPersonData(mAssignee, "PostManagementAgree_Have_Next", "PostManagementAgree_No_Next", "是否同意该发文拟稿", new PassNextPersonString() {
            @Override
            public void passNextPersonString(String userList) {
                officialLeaveApply(userList, 0);
            }
        });
    }

    @Override
    public void submitOnClick(View v) {
    }

    private void officialLeaveApply(String userList, int method) {
        startLoading();

        HashMap<String, String> hashMap = getUnifiedDataHashMap();

        hashMap.put("taskName", mTaskName);
        hashMap.put("assignee", mAssignee);
        hashMap.put("method", "" + method);
        hashMap.put("userList", userList);

        // 以下为表单上的填写数据
        hashMap.put("suggest", mApproveIdea.getCustomEditTextRight().getText().toString());

        startUltimatelySubmit(IpFiled.SUBMIT_IS_AGREE_POST, hashMap, "success", "服务器正忙,请稍后", "提交成功");
    }
}