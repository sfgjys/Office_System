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

public class RecordManagementFragment extends FormFragment {

    private ArrayList<String> excessive;
    private EditTextItem mApproveIdea;
    private String mTitle = "";
    private String mTextNumber = "";
    private String mTextUnit = "";
    private String mTextTime = "";
    private List<CheckBoxChild> mNextUsers;
    private List<String> mDownloadFilePath;
    private List<String> mDownloadFileName;

    private String mProposeToIdeaText;
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

        View inflate = ViewsUitls.inflate(R.layout.form_record_management);

        initView(inflate);

        return inflate;
    }

    private void initView(View inflate) {
        // 正常的展示数据
        EditTextItem superiorTextTitle = (EditTextItem) inflate.findViewById(R.id.form_record_management_title);
        superiorTextTitle.setEditText(mTitle);
        EditTextItem superiorTextNumber = (EditTextItem) inflate.findViewById(R.id.form_record_management_number);
        superiorTextNumber.setEditText(mTextNumber);
        EditTextItem superiorTextUnit = (EditTextItem) inflate.findViewById(R.id.form_record_management_unit);
        superiorTextUnit.setEditText(mTextUnit);
        EditTextItem superiorTextDay = (EditTextItem) inflate.findViewById(R.id.form_record_management_day);
        superiorTextDay.setEditText(mTextTime);

        // 审批意见有可能有，这个意见只能展示不能编辑
        EditTextItem proposeToIdea = (EditTextItem) inflate.findViewById(R.id.form_record_management_propose_to_idea);
        proposeToIdea.setEditText(mProposeToIdeaText);

        // 此为用户进行审批意见编辑的控件
        mApproveIdea = (EditTextItem) inflate.findViewById(R.id.form_record_management_approve_idea);

        setDownloadView(inflate);
    }

    /* 设置下载控件操作 */
    private void setDownloadView(View inflate) {
        View details = inflate.findViewById(R.id.form_record_management_details);
        LinearLayout accessoryList = (LinearLayout) inflate.findViewById(R.id.form_record_management_details_right);
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
                        promptDialog.show(getActivity().getSupportFragmentManager(), "RecordManagementDisAgree");
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

    /* 解析网络获取的JSON数据 */
    private void analyticalData(JSONObject jsonObject) throws JSONException {
        // 审批表单的界面展示数据
        mTitle = jsonObject.optString("TITLE");
        mTextNumber = jsonObject.optString("CALL") + "( " + jsonObject.optString("NUM") + " )号";
        mTextUnit = jsonObject.optString("ORG_NAME");
        mTextTime = jsonObject.optString("REC_TIME");

        // 收文接面上展示的下载附件数据
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
                    mProposeToIdeaText = jsonObject.optString("rect3suggest");
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
        }, "是否不同意该收文签收 !");
        promptDialog.show(getActivity().getSupportFragmentManager(), "RecordManagementDisAgree");
    }

    @Override
    public void agreeOnClick(View v) {
        getNextPersonData(mAssignee, "RecordManagementAgree_Have_Next", "RecordManagementAgree_No_Next", new PassNextPersonString() {
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

        startUltimatelySubmit(IpFiled.SUBMIT_IS_AGREE_RECORD, hashMap, "success", "服务器正忙,请稍候", "提交成功");
    }
}