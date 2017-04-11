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
import com.minlu.office_system.customview.EditTextTimeSelector;
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

        EditTextItem addUpLeaveDays = (EditTextItem) inflate.findViewById(R.id.form_leave_management_add_up_leave_day_number);
        addUpLeaveDays.setEditText("");
        EditTextItem leaveDayNumber = (EditTextItem) inflate.findViewById(R.id.form_leave_management_leave_day_number);
        leaveDayNumber.setEditText(mLeaveDay + " 天");
        EditTextItem residueLeaveYears = (EditTextItem) inflate.findViewById(R.id.form_leave_management_residue_leave_year_number);
        residueLeaveYears.setEditText("");
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

        mApproveIdea = (EditTextItem) inflate.findViewById(R.id.form_leave_management_approve_idea);

        return inflate;
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

                        mTitle = jsonObject.optString("TITLE");
                        mType = jsonObject.optString("QTYPE");
                        mRemark = jsonObject.optString("REMARK");
                        mLeaveDay = jsonObject.optString("ALLLEAVE");
                        mStartTime = jsonObject.optString("BTIME");
                        mEndTime = jsonObject.optString("ETIME");
                        String mLeavePersonName = jsonObject.optString("NAME");

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
                System.out.println("LeaveManagementFragment-disAgreeOnClick");
            }
        }, "是否不同意该请假 !");
        promptDialog.show(getActivity().getSupportFragmentManager(), "LeaveManagementDisAgree");
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
        selectNextUserDialog.show(getActivity().getSupportFragmentManager(), "LeaveManagementAgree");
    }

    @Override
    public void submitOnClick(View v) {
    }
}