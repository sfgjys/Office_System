package com.minlu.office_system.fragment.form;

import android.content.DialogInterface;
import android.view.View;

import com.minlu.baselibrary.base.ContentPage;
import com.minlu.baselibrary.util.SharedPreferencesUtil;
import com.minlu.baselibrary.util.StringUtils;
import com.minlu.baselibrary.util.ViewsUitls;
import com.minlu.office_system.IpFiled;
import com.minlu.office_system.R;
import com.minlu.office_system.StringsFiled;
import com.minlu.office_system.activity.FormActivity;
import com.minlu.office_system.bean.CheckBoxChild;
import com.minlu.office_system.customview.EditTextItem;
import com.minlu.office_system.customview.EditTextTimeSelector;
import com.minlu.office_system.fragment.dialog.PromptDialog;
import com.minlu.office_system.fragment.form.formPremise.FormFragment;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import okhttp3.Response;

/**
 * Created by user on 2017/3/7.
 */
public class LeaveManagementFragment extends FormFragment {

    private ArrayList<String> excessive;
    private List<CheckBoxChild> mNextUsers;
    private String mTitle;
    private String mType;
    private String mRemark;
    private String mLeaveDay;
    private String mEndTime;
    private String mStartTime;
    private EditTextItem mApproveIdea;
    private String mOfficeIdeaText = "";
    private String mSectionIdeaText = "";
    private String mMainPrincipalIdeaText = "";
    private String mMinutePrincipalIdeaText = "";
    private String mAddUpLeaveDayText;
    private String mResidueLeaveYearText;
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

        View inflate = ViewsUitls.inflate(R.layout.form_leave_management);

        initView(inflate);

        return inflate;
    }

    private void initView(View inflate) {
        // 正常数据显示
        EditTextItem addUpLeaveDays = (EditTextItem) inflate.findViewById(R.id.form_leave_management_add_up_leave_day_number);
        addUpLeaveDays.setEditText(mAddUpLeaveDayText);
        EditTextItem leaveDayNumber = (EditTextItem) inflate.findViewById(R.id.form_leave_management_leave_day_number);
        leaveDayNumber.setEditText(mLeaveDay + " 天");
        EditTextItem residueLeaveYears = (EditTextItem) inflate.findViewById(R.id.form_leave_management_residue_leave_year_number);
        residueLeaveYears.setEditText(mResidueLeaveYearText);
        EditTextItem title = (EditTextItem) inflate.findViewById(R.id.form_leave_management_title);
        title.setEditText(mTitle);
        EditTextItem type = (EditTextItem) inflate.findViewById(R.id.form_leave_management_type);
        type.setEditText(mType);
        EditTextItem remark = (EditTextItem) inflate.findViewById(R.id.form_leave_management_remark);
        remark.setEditText(mRemark);
        ViewsUitls.setWidthFromTargetView(title.getCustomEditTextLeft(), remark.getCustomEditTextLeft());
        EditTextTimeSelector endTime = (EditTextTimeSelector) inflate.findViewById(R.id.form_leave_management_end_time);
        endTime.setDayOfYearText(mEndTime);
        endTime.setTimeOfDayText("");
        endTime.getmTimeOfDay().setVisibility(View.INVISIBLE);
        EditTextTimeSelector startTime = (EditTextTimeSelector) inflate.findViewById(R.id.form_leave_management_start_time);
        startTime.setDayOfYearText(mStartTime);
        startTime.setTimeOfDayText("");
        startTime.getmTimeOfDay().setVisibility(View.INVISIBLE);

        // 审批各步骤的审批意见
        EditTextItem mainPrincipalIdea = (EditTextItem) inflate.findViewById(R.id.form_leave_management_main_principal_idea);
        mainPrincipalIdea.setEditText(mMainPrincipalIdeaText);
        EditTextItem sectionIdea = (EditTextItem) inflate.findViewById(R.id.form_leave_management_section_idea);
        sectionIdea.setEditText(mSectionIdeaText);
        EditTextItem officeIdea = (EditTextItem) inflate.findViewById(R.id.form_leave_management_office_idea);
        officeIdea.setEditText(mOfficeIdeaText);
        EditTextItem minutePrincipalIdea = (EditTextItem) inflate.findViewById(R.id.form_leave_management_minute_principal_idea);
        minutePrincipalIdea.setEditText(mMinutePrincipalIdeaText);

        ViewsUitls.setWidthFromTargetView(mainPrincipalIdea.getCustomEditTextLeft(), sectionIdea.getCustomEditTextLeft());
        ViewsUitls.setWidthFromTargetView(mainPrincipalIdea.getCustomEditTextLeft(), officeIdea.getCustomEditTextLeft());

        // 此时用户填写的意见
        mApproveIdea = (EditTextItem) inflate.findViewById(R.id.form_leave_management_approve_idea);
    }

    @Override
    protected ContentPage.ResultState onLoad() {
        // 登录成功后获取的数据在此显示
        mResidueLeaveYearText = SharedPreferencesUtil.getString(ViewsUitls.getContext(), StringsFiled.LOGIN_GET_USER_RESIDUE_YEAR_LEAVE, "");
        mAddUpLeaveDayText = SharedPreferencesUtil.getString(ViewsUitls.getContext(), StringsFiled.LOGIN_GET_USER_ADD_UP_LEAVE_DAYS, "");

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
        // 正常界面显示数据
        mTitle = jsonObject.optString("TITLE");
        mType = jsonObject.optString("QTYPE");
        mRemark = jsonObject.optString("REMARK");
        mLeaveDay = jsonObject.optString("ALLLEAVE");
        mStartTime = jsonObject.optString("BTIME");
        mEndTime = jsonObject.optString("ETIME");
        String mLeavePersonName = jsonObject.optString("NAME");

        // 后面的接口需要到的数据
        mAssignee = jsonObject.optString("ASSIGNEE");
        mTaskName = jsonObject.optString("TASKNAME");

        // 获取审批建议
        getAllSuggest(new AnalysisJSON() {
            @Override
            public void analysisJSON(JSONObject jsonObject) {
                mSectionIdeaText = jsonObject.optString("bmyjsuggest");
                mOfficeIdeaText = jsonObject.optString("bgsyjsuggest");
                mMinutePrincipalIdeaText = jsonObject.optString("fgfzryjsuggest");
                mMainPrincipalIdeaText = jsonObject.optString("zyfzryjsuggest");

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
                officialLeaveApply("", 1);
            }
        }, "是否不同意该请假 !");
        promptDialog.show(getActivity().getSupportFragmentManager(), "LeaveManagementDisAgree");
    }

    @Override
    public void agreeOnClick(View v) {
        getNextPersonData(mAssignee, "LeaveManagementAgree_Have_Next", "LeaveManagementAgree_No_Next", new PassNextPersonString() {
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
        hashMap.put("Method", "" + method);
        hashMap.put("userList", userList);

        // 以下为表单上的填写数据
        hashMap.put("suggest", mApproveIdea.getCustomEditTextRight().getText().toString());

        startUltimatelySubmit(IpFiled.LEAVE_APPLY_SUBMIT, hashMap, "success", "服务器正忙,请稍后", "请假申请提交成功");
    }
}