package com.minlu.office_system.fragment.form;

import android.content.DialogInterface;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.minlu.baselibrary.base.ContentPage;
import com.minlu.baselibrary.util.SharedPreferencesUtil;
import com.minlu.baselibrary.util.StringUtils;
import com.minlu.baselibrary.util.ViewsUitls;
import com.minlu.office_system.IpFiled;
import com.minlu.office_system.PassBackStringData;
import com.minlu.office_system.R;
import com.minlu.office_system.StringsFiled;
import com.minlu.office_system.activity.FormActivity;
import com.minlu.office_system.bean.CountersignSuggestBean;
import com.minlu.office_system.bean.SingleOption;
import com.minlu.office_system.customview.EditTextItem;
import com.minlu.office_system.customview.TableSuggest;
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
    private List<SingleOption> mNextUsers;
    private EditTextItem mApproveIdea;
    private String mSecretGradeText = "";
    private List<String> mDownloadFileName;
    private List<String> mDownloadFilePath;
    private String mNuclearDraftText = "";
    private String mOfficeNuclearDraftIdeaText = "";
    private String mEndSignAndIssueText = "";
    private String mAssignee = "";
    private String mTaskName = "";
    private String mStep = "";
    private String mAutoOrg = "";
    private String mOrd = "";
    private List<CountersignSuggestBean> countersignSuggestData = new ArrayList<>();
    private String mUserOneselfSuggest = "";
    private EditTextItem drafter;
    private EditTextItem mainOffice;
    private EditTextItem postTitle;
    private EditTextItem mainSendOffice;
    private EditTextItem postType;
    private EditTextItem secretGrade;
    private EditTextItem postNumber;
    private EditTextItem officeNuclearDraftIdea;
    private EditTextItem nuclearDraftIdea;
    private EditTextItem signAndIssue;
    private EditTextItem mCountersignIdea;
    private FormActivity formActivity;
    private EditTextItem fairCopyIdea;
    private EditTextItem printNumber;
    private String mFairCopySuggestText = "";
    private String mPrintNumberText;
    private String mPostNumberText;

    @Override
    protected void onSubClassOnCreateView() {
        loadDataAndRefresh();
    }

    @Override
    protected View onCreateSuccessView(Bundle savedInstanceState) {
        // 因为本fragment是通过R.id.sv_replace_form控件replace开启的，但是R.id.sv_replace_form控件是居中属性，所以再次我们要使得居中属性去除
        formActivity = (FormActivity) getContext();
        if (formActivity != null) {
            formActivity.setScrollViewNoGravity();
        }

        View inflate = ViewsUitls.inflate(R.layout.form_post_management);

        initView(inflate);

        return inflate;
    }

    private void initView(View inflate) {
        // 正常显示数据
        drafter = (EditTextItem) inflate.findViewById(R.id.form_post_management_drafter);
        drafter.setEditText(mDrafterText);// 拟稿人
        mainOffice = (EditTextItem) inflate.findViewById(R.id.form_post_management_main_office);
        mainOffice.setEditText(mMainOfficeText);// 主办处室
        ViewsUitls.setWidthFromTargetView(mainOffice.getCustomEditTextLeft(), drafter.getCustomEditTextLeft());
        postTitle = (EditTextItem) inflate.findViewById(R.id.form_post_management_post_title);
        postTitle.setEditText(mPostTitleText);// 发文标题
        mainSendOffice = (EditTextItem) inflate.findViewById(R.id.form_post_management_main_send_office);
        mainSendOffice.setEditText(mMainSendOfficeText);// 主送机关
        postType = (EditTextItem) inflate.findViewById(R.id.form_post_management_post_type);
        postType.setEditText(mPostTypeText);// 发文类型
        secretGrade = (EditTextItem) inflate.findViewById(R.id.form_post_management_secret_grade);
        ViewsUitls.setWidthFromTargetView(mainOffice.getCustomEditTextLeft(), secretGrade.getCustomEditTextLeft());
        secretGrade.setEditText(mSecretGradeText);// 密级

        // 各个步骤审批意见
        officeNuclearDraftIdea = (EditTextItem) inflate.findViewById(R.id.form_post_management_office_nuclear_draft_idea);
        officeNuclearDraftIdea.setEditTextGistIsEmpty(mOfficeNuclearDraftIdeaText);
        nuclearDraftIdea = (EditTextItem) inflate.findViewById(R.id.form_post_management_nuclear_draft_idea);
        nuclearDraftIdea.setEditTextGistIsEmpty(mNuclearDraftText);
        signAndIssue = (EditTextItem) inflate.findViewById(R.id.form_post_management_sign_and_issue);
        signAndIssue.setEditTextGistIsEmpty(mEndSignAndIssueText);
        ViewsUitls.setWidthFromTargetView(mainOffice.getCustomEditTextLeft(), nuclearDraftIdea.getCustomEditTextLeft());
        ViewsUitls.setWidthFromTargetView(mainOffice.getCustomEditTextLeft(), officeNuclearDraftIdea.getCustomEditTextLeft());
        ViewsUitls.setWidthFromTargetView(mainOffice.getCustomEditTextLeft(), signAndIssue.getCustomEditTextLeft());

        setDownloadView(inflate);

        // 用户自己的会签意见
        mCountersignIdea = (EditTextItem) inflate.findViewById(R.id.form_post_management_countersign_idea);
        mCountersignIdea.setEditTextGistIsEmpty(mUserOneselfSuggest);// mUserOneselfSuggest文本只有在用户编辑过会签，且重新编辑会签时才会有内容，其他时候都为空

        // 会签意见
        TableSuggest countersignSuggest = (TableSuggest) inflate.findViewById(R.id.form_post_management_countersign_suggest);
        countersignSuggest.addTableData(countersignSuggestData);

        fairCopyIdea = (EditTextItem) inflate.findViewById(R.id.form_post_management_fair_copy_idea);
        fairCopyIdea.setEditTextGistIsEmpty(mFairCopySuggestText);
        printNumber = (EditTextItem) inflate.findViewById(R.id.form_post_management_print_number);
        printNumber.setEditTextGistIsEmpty(mPrintNumberText);
        postNumber = (EditTextItem) inflate.findViewById(R.id.form_post_management_post_number);  // 发文文号 暂无数据
        postNumber.setEditTextGistIsEmpty(mPostNumberText);

        showEachStepView();
    }

    private void showEachStepView() {
        switch (Integer.parseInt(mStep)) {
            case 1:// 发文被驳回回到第一步
                setStep1ViewShow();
                formActivity.showAgreeSubmitButton(View.GONE, View.VISIBLE);
                break;
            case 2:// 核稿步骤
                showEditTextItemDifferentState(nuclearDraftIdea, "请填写核稿意见", "(原核稿意见)");
                mApproveIdea = nuclearDraftIdea;
                formActivity.showAgreeSubmitButton(View.VISIBLE, View.GONE);
                break;
            case 3:// 办公室核稿步骤
                showEditTextItemDifferentState(officeNuclearDraftIdea, "请填写办公室核稿意见", "(原办公室核稿意见)");
                mApproveIdea = officeNuclearDraftIdea;
                formActivity.showAgreeSubmitButton(View.VISIBLE, View.GONE);
                break;
            case 4:// 会签
                showEditTextItemDifferentState(mCountersignIdea, "请填写会签意见", "(原办会签意见)");
                mApproveIdea = mCountersignIdea;
                formActivity.showAgreeSubmitButton(View.VISIBLE, View.GONE);
                break;
            case 5:// 签发
                showEditTextItemDifferentState(signAndIssue, "请填写签发意见", "(原签发意见)");
                mApproveIdea = signAndIssue;
                formActivity.showAgreeSubmitButton(View.VISIBLE, View.GONE);
                break;
            case 7:
                showEditTextItemDifferentState(fairCopyIdea, "请填写清稿意见", "(原清稿意见)");
                mApproveIdea = fairCopyIdea;
                formActivity.showAgreeSubmitButton(View.VISIBLE, View.GONE);
                break;
            case 6:
                showEditTextItemDifferentState(postNumber, "请填写发文文号", "(原发文文号)");
                showEditTextItemDifferentState(printNumber, "请填写打印份数", "(原打印分数)");
                formActivity.showAgreeSubmitButton(View.GONE, View.VISIBLE);
                break;
        }
    }

    private void setStep1ViewShow() {
        final List<String> postTypeData = new ArrayList<>();
        postTypeData.add("常安帮");
        postTypeData.add("常人调办");
        postTypeData.add("常矫正");
        postTypeData.add("常基协");
        postTypeData.add("常司复");
        postTypeData.add("常特管办");
        postTypeData.add("常司群组发");
        postTypeData.add("常多解办");
        postTypeData.add("常律办");
        postTypeData.add("常公协");
        postTypeData.add("常法宣办");
        postTypeData.add("常法宣");
        postTypeData.add("常司机工");
        postTypeData.add("常州市司法局信笺");
        postTypeData.add("常司密电");
        postTypeData.add("常司罚决字");
        postTypeData.add("常司机党");
        postTypeData.add("常司职");
        postTypeData.add("常司函");
        postTypeData.add("常司办");
        postTypeData.add("常司党组");
        postTypeData.add("常司通");
        postTypeData.add("其他");
        final List<String> secretGradeData = new ArrayList<>();
        secretGradeData.add("公开");
        secretGradeData.add("机密");
        secretGradeData.add("秘密");
        secretGradeData.add("内部");
        secretGradeData.add("普通");
        showEditTextItemCanEdit(postTitle);
        showEditTextItemCanEdit(mainSendOffice);
        showEditTextItemCanClick(postType);
        showEditTextItemCanClick(secretGrade);
        setWhichViewShowListPopupWindow(false, postType.getCustomEditTextRight(), postTypeData, new ShowListPopupItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                postType.getCustomEditTextRight().setText(postTypeData.get(position));
            }

            @Override
            public void onAnchorViewClick(View v) {
                setBackGroundDarkColor(0.7f);
            }

            @Override
            public void onListPopupDismiss() {
                setBackGroundDarkColor(1.0f);
            }
        }, getActivity());
        setWhichViewShowListPopupWindow(false, secretGrade.getCustomEditTextRight(), secretGradeData, new ShowListPopupItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                secretGrade.getCustomEditTextRight().setText(secretGradeData.get(position));
            }

            @Override
            public void onAnchorViewClick(View v) {
                setBackGroundDarkColor(0.7f);
            }

            @Override
            public void onListPopupDismiss() {
                setBackGroundDarkColor(1.0f);
            }
        }, getActivity());
    }

    /* 设置下载控件操作 */
    private void setDownloadView(View inflate) {
        View details = inflate.findViewById(R.id.form_post_management_details);
        LinearLayout accessoryList = (LinearLayout) inflate.findViewById(R.id.form_post_management_details_right);
        if (mDownloadFileName.size() > 0) {
            details.setVisibility(View.VISIBLE);
            for (int i = 0; i < mDownloadFileName.size(); i++) {
                LinearLayout layout = (LinearLayout) ViewsUitls.inflate(R.layout.item_accessory_list);
                TextView textView = (TextView) layout.findViewById(R.id.tv_accessory_name);
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
                accessoryList.addView(layout);
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

    // ***********************************************************************************************************************************************
    @Override
    protected ContentPage.ResultState onLoad() {
        Response response = requestFormListItemDetail();
        if (response != null && response.isSuccessful()) {
            try {
                String resultList = response.body().string();
                if (StringUtils.interentIsNormal(resultList)) {
                    JSONObject jsonObject = new JSONObject(resultList);
                    if (jsonObject.has("DRAFTER")) {// 有标题字段，说明返回的数据正常
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
        mDrafterText = jsonObject.optString("DRAFTER");// 拟稿人
        mMainOfficeText = jsonObject.optString("ZBCS");// 主办事处
        mPostTypeText = jsonObject.optString("DOCUMENT_TYPE");// 发文类型
        mPostTitleText = jsonObject.optString("TITLE");// 发文标题
        mSecretGradeText = jsonObject.optString("MIJI");// 密级
        mMainSendOfficeText = jsonObject.optString("ZSJG");// 主送机关

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
        mStep = jsonObject.optString("STEP");

        JSONObject mapInfo = jsonObject.optJSONObject("MAPINFO");
        if (mapInfo.has("ord")) {
            mOrd = mapInfo.optInt("ord") + "";
        }
        mAutoOrg = mapInfo.optString("autoOrg");

        // 获取审批建议
        getAllSuggest(new AnalysisJSON() {

            @Override
            public void analysisJSON(JSONObject jsonObject) {
                if (jsonObject.has("rect3method")) {
                    mNuclearDraftText = getSuggestIdea(jsonObject, "rect3suggest", "rect3method");
                }
                if (jsonObject.has("rect4method")) {
                    mOfficeNuclearDraftIdeaText = getSuggestIdea(jsonObject, "rect4suggest", "rect4method");
                }
                if (jsonObject.has("rect9method")) {
                    mEndSignAndIssueText = getSuggestIdea(jsonObject, "rect9suggest", "rect9method");
                }
                if (jsonObject.has("rect10method")) {
                    mFairCopySuggestText = getSuggestIdea(jsonObject, "rect10suggest", "rect10method");
                }
                if (jsonObject.has("num")) {
                    mPrintNumberText = jsonObject.optString("num");
                }
                if (jsonObject.has("wh_year")) {
                    mPostNumberText = jsonObject.optString("wh_year");
                }

                if (jsonObject.has("rect8")) {// 发文的会签建议
                    try {
                        JSONArray jsonArray = new JSONArray(jsonObject.optString("rect8"));
                        for (int i = 0; i < jsonArray.length(); i++) {
                            JSONObject jsonObject1 = jsonArray.getJSONObject(i);
                            if (Integer.parseInt(mStep) == 4) {// 去除在会签步骤时的用户自身的意见
                                String string = SharedPreferencesUtil.getString(ViewsUitls.getContext(), StringsFiled.LOGIN_GET_USER_NAME, "");
                                if (!jsonObject1.optString("truename").contains(string)) {
                                    countersignSuggestData.add(new CountersignSuggestBean(jsonObject1.optString("truename"), jsonObject1.optString("method"), getSuggestIdea(jsonObject1, "suggest", "method"), jsonObject1.optString("time")));
                                } else {
                                    mUserOneselfSuggest = getSuggestIdea(jsonObject1, "suggest", "method");// 用户在会签步骤的自身的建议
                                }
                            } else {
                                countersignSuggestData.add(new CountersignSuggestBean(jsonObject1.optString("truename"), jsonObject1.optString("method"), getSuggestIdea(jsonObject1, "suggest", "method"), jsonObject1.optString("time")));
                            }
                        }
                        excessive = new ArrayList<>();
                        excessive.add("excessive");// 给excessive创建实例，并添加元素，让界面走onCreateSuccessView()方法
                    } catch (JSONException e) {
                        e.printStackTrace();
                        excessive = null;
                    }
                } else {
                    excessive = new ArrayList<>();
                    excessive.add("excessive");// 给excessive创建实例，并添加元素，让界面走onCreateSuccessView()方法
                }
            }
        });
    }

    // ********************************************************************************************************************************************
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
        getNextPersonData(mAssignee, mOrd, mAutoOrg, getUserNameHashMap(mStep, 5), "PostManagementAgree_Have_Next", "PostManagementAgree_No_Next", "是否同意该发文拟稿", new PassBackStringData() {
            @Override
            public void passBackStringData(String passBackData) {
                officialLeaveApply(passBackData, 0);
            }
        });
    }

    @Override
    public void submitOnClick(View v) {
        getNextPersonData(mAssignee, mOrd, mAutoOrg, null, "PostManagementAgree_Have_Next", "PostManagementAgree_No_Next", "是否同意该发文拟稿", new PassBackStringData() {
            @Override
            public void passBackStringData(String passBackData) {
                officialLeaveApply(passBackData, 0);
            }
        });
    }

    private void officialLeaveApply(String userList, int method) {
        startLoading();

        HashMap<String, String> hashMap = getUnifiedDataHashMap();

        hashMap.put("taskName", mTaskName);
        hashMap.put("assignee", mAssignee);
        hashMap.put("method", "" + method);
        hashMap.put("userList", userList);

        // 以下为表单上的填写数据
        if (Integer.parseInt(mStep) != 1) {
            hashMap.put("suggest", mApproveIdea.getCustomEditTextRight().getText().toString());
        } else {
            hashMap.put("document_type", postType.getCustomEditTextRight().getText().toString());
            hashMap.put("title", postTitle.getCustomEditTextRight().getText().toString());
            hashMap.put("miji", secretGrade.getCustomEditTextRight().getText().toString());
            hashMap.put("first_zsjg", mainSendOffice.getCustomEditTextRight().getText().toString());
        }
        startUltimatelySubmit(IpFiled.SUBMIT_IS_AGREE_POST, hashMap, "success", "服务器正忙,请稍后", "提交成功");
    }
}