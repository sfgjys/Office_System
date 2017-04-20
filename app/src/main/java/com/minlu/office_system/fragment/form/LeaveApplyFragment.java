package com.minlu.office_system.fragment.form;

import android.content.DialogInterface;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;

import com.minlu.baselibrary.base.ContentPage;
import com.minlu.baselibrary.manager.ThreadManager;
import com.minlu.baselibrary.util.SharedPreferencesUtil;
import com.minlu.baselibrary.util.StringUtils;
import com.minlu.baselibrary.util.TimeTool;
import com.minlu.baselibrary.util.ToastUtil;
import com.minlu.baselibrary.util.ViewsUitls;
import com.minlu.office_system.IpFiled;
import com.minlu.office_system.R;
import com.minlu.office_system.StringsFiled;
import com.minlu.office_system.activity.FormActivity;
import com.minlu.office_system.bean.CheckBoxChild;
import com.minlu.office_system.customview.EditTextItem;
import com.minlu.office_system.customview.EditTextTimeSelector;
import com.minlu.office_system.fragment.dialog.OnSureButtonClick;
import com.minlu.office_system.fragment.dialog.PromptDialog;
import com.minlu.office_system.fragment.dialog.SelectNextUserDialog;
import com.minlu.office_system.fragment.form.formPremise.FormFragment;
import com.minlu.office_system.http.OkHttpMethod;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.TimerTask;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;


public class LeaveApplyFragment extends FormFragment {

    private List<String> mLeaveType;
    private EditTextItem mLeaveTypeEdit;
    private EditTextItem mLeaveDayNumber;
    private EditTextTimeSelector mStartTime;
    private EditTextTimeSelector mEndTime;
    private EditTextItem mLeaveTitle;
    private EditTextItem mLeaveRemark;
    private EditTextItem mAddUpLeaveDays;
    private EditTextItem mResidueLeaveYears;
    private String mResidueYearLeaveText;
    private String mAddUpLeaveDaysText;
    private EditTextItem mSelectHour;
    private List<String> mSelectHourData;
    private String mResult;
    private String mOrd;
    private String mAssignee;
    private String mAutoOrg;
    private String mUserName;
    private List<CheckBoxChild> mNextUsers;
    private String mTaskId;
    private String mTaskName;
    private String mOrderId;

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

        View inflate = ViewsUitls.inflate(R.layout.form_leave_apply);

        initView(inflate);

        return inflate;
    }

    private void initView(View inflate) {
        mAddUpLeaveDays = (EditTextItem) inflate.findViewById(R.id.form_leave_apply_add_up_leave_day_number);
        mAddUpLeaveDays.setEditText(mAddUpLeaveDaysText);
        mResidueLeaveYears = (EditTextItem) inflate.findViewById(R.id.form_leave_apply_residue_leave_year_number);
        mResidueLeaveYears.setEditText(mResidueYearLeaveText);

        mLeaveTitle = (EditTextItem) inflate.findViewById(R.id.form_leave_apply_title);
        mLeaveRemark = (EditTextItem) inflate.findViewById(R.id.form_leave_apply_remark);
        ViewsUitls.setWidthFromTargetView(mLeaveTitle.getCustomEditTextLeft(), mLeaveRemark.getCustomEditTextLeft());

        // 自动填写
        mLeaveDayNumber = (EditTextItem) inflate.findViewById(R.id.form_leave_apply_leave_day_number);

        // 类型列表展示
        mLeaveTypeEdit = (EditTextItem) inflate.findViewById(R.id.form_leave_apply_type);
        EditText typeCustomEditTextRight = mLeaveTypeEdit.getCustomEditTextRight();
        setWhichViewShowListPopupWindow(typeCustomEditTextRight, mLeaveType, new ShowListPopupItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                mLeaveTypeEdit.setEditText(mLeaveType.get(position));
            }

            @Override
            public void onAnchorViewClick(View v) {
                setBackGroundDarkColor(0.6f);
            }

            @Override
            public void onListPopupDismiss() {
                setBackGroundDarkColor(1.0f);
            }
        }, getActivity());

        // 小时选择
        mSelectHour = (EditTextItem) inflate.findViewById(R.id.form_leave_apply_leave_day_number_select_hour);
        mSelectHour.getCustomEditTextRight().setText("0 小时");
        setWhichViewShowListPopupWindow(mSelectHour.getCustomEditTextRight(), mSelectHourData, new ShowListPopupItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                mSelectHour.setEditText(mSelectHourData.get(position));
            }

            @Override
            public void onAnchorViewClick(View v) {
                setBackGroundDarkColor(0.6f);
            }

            @Override
            public void onListPopupDismiss() {
                setBackGroundDarkColor(1.0f);
            }
        }, getActivity());

        // 时间选择对话框展示
        mStartTime = (EditTextTimeSelector) inflate.findViewById(R.id.form_leave_apply_start_time);
        mStartTime.getmTimeOfDay().setVisibility(View.INVISIBLE);
        mStartTime.setNowDayOfYearAndTimeOfDay();
        startUseEditTextOnClickShowTimePicker(mStartTime);// 给mStartTime设置点击弹出时间选择对话框
        mStartTime.setOnSetTextListener(new EditTextTimeSelector.OnSetTextListener() {
            @Override
            public void onSetText() {
                setLeaveDayNumberText();
            }
        });// 设置 当mStartTime修改了文本输入框内的文本时的 监听事件

        mEndTime = (EditTextTimeSelector) inflate.findViewById(R.id.form_leave_apply_end_time);
        mEndTime.getmTimeOfDay().setVisibility(View.INVISIBLE);
        mEndTime.setNowDayOfYearAndTimeOfDay();
        startUseEditTextOnClickShowTimePicker(mEndTime);
        mEndTime.setOnSetTextListener(new EditTextTimeSelector.OnSetTextListener() {
            @Override
            public void onSetText() {
                setLeaveDayNumberText();
            }
        });

        setLeaveDayNumberText();// 第一此显示界面对的时候就需要设置文本了
    }

    /*给开始时间和结束时间之间的  请假天数  的输入文本框设置具体的文本*/
    private void setLeaveDayNumberText() {
        if (mStartTime != null && mEndTime != null) {
            // TODO 计算请假天数的时候,需要注意时间格式
            float[] timeDifferenceValue = TimeTool.getBaseStringOfTimeDifferenceValue(mStartTime.getDayOrTimeText(), mEndTime.getDayOrTimeText(), "yyyy-MM-dd HH:mm");
            mLeaveDayNumber.setEditText((int) timeDifferenceValue[0] + " 天 ");//  + timeDifferenceValue[1] + " 小时 , 共 " + (int) timeDifferenceValue[2] + " 天"
        }
    }

    @Override
    protected ContentPage.ResultState onLoad() {

        mSelectHourData = new ArrayList<>();
        mSelectHourData.add("0 小时");
        mSelectHourData.add("1 小时");
        mSelectHourData.add("2 小时");
        mSelectHourData.add("3 小时");
        mSelectHourData.add("4 小时");
        mSelectHourData.add("5 小时");
        mSelectHourData.add("6 小时");
        mSelectHourData.add("7 小时");
        mSelectHourData.add("8 小时");

        HashMap<String, String> leaveApplyPremise = new HashMap<>();
        leaveApplyPremise.put("processId", StringsFiled.Leave_ProcessId);
        leaveApplyPremise.put("taskName", "qjsh");
        Response response = OkHttpMethod.synPostRequest(IpFiled.LEAVE_APPLY_PREMISE, leaveApplyPremise);
        if (response != null && response.isSuccessful()) {
            try {
                String resultList = response.body().string();
                if (StringUtils.interentIsNormal(resultList)) {
                    JSONObject jsonObject = new JSONObject(resultList);
                    if (jsonObject.has("year")) {
                        // 用来显示的
                        mResidueYearLeaveText = jsonObject.optString("year");
                        mAddUpLeaveDaysText = jsonObject.optString("allday");

                        // 用来请求下一步操作人
                        mOrd = jsonObject.optString("ord");
                        mAssignee = jsonObject.optString("assignee");
                        mAutoOrg = jsonObject.optString("autoOrg");
                        mUserName = jsonObject.optString("userName");

                        // 用于正式提交
                        mResult = jsonObject.optString("result");
                        mTaskId = jsonObject.optString("taskId");
                        mTaskName = jsonObject.optString("taskName");
                        mOrderId = jsonObject.optString("orderId");

                        mLeaveType = new ArrayList<>();
                        mLeaveType.add("事假");
                        mLeaveType.add("婚假");
                        mLeaveType.add("产假");
                        mLeaveType.add("陪产假");
                        mLeaveType.add("丧假");
                        mLeaveType.add("年假");
                        mLeaveType.add("病假");
                        mLeaveType.add("其他");

                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return chat(mLeaveType);
    }

    @Override
    public void disAgreeOnClick(View v) {
    }

    @Override
    public void agreeOnClick(View v) {
    }

    @Override
    public void submitOnClick(View v) {
        PromptDialog promptDialog = new PromptDialog(new PromptDialog.OnSureButtonClick() {
            @Override
            public void onSureClick(DialogInterface dialog, int id) {
                requestSubmitUserList();
            }
        }, "是否将请假申请进行提交处理 !");
        promptDialog.show(getActivity().getSupportFragmentManager(), "LeaveApplySubmit");
    }

    private void requestSubmitUserList() {
        ThreadManager.getInstance().execute(new TimerTask() {
            @Override
            public void run() {
                HashMap<String, String> requestUserList = new HashMap<>();
                requestUserList.put("assignee", mAssignee);
                requestUserList.put("org_id", mOrd);
                requestUserList.put("autoOrg", mAutoOrg);
                Response response = OkHttpMethod.synPostRequest(IpFiled.REQUEST_USER_LIST, requestUserList);
                if (response != null && response.isSuccessful()) {
                    try {
                        String resultList = response.body().string();
                        if (StringUtils.interentIsNormal(resultList)) {
                            JSONArray jsonArray = new JSONArray(resultList);
                            mNextUsers = new ArrayList<>();
                            for (int i = 0; i < jsonArray.length(); i++) {
                                JSONObject nextUserData = jsonArray.getJSONObject(i);
                                mNextUsers.add(new CheckBoxChild(nextUserData.optString("TRUENAME"), nextUserData.optString("USERNAME"), nextUserData.optString("ORG_INFOR")));
                            }
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
                ViewsUitls.runInMainThread(new TimerTask() {
                    @Override
                    public void run() {
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
                                if (sureUsers.size() > 0) {
                                    officialLeaveApply(sureUsers);
                                } else {
                                    ToastUtil.showToast(ViewsUitls.getContext(), "请选择下一步操作人");
                                }
                            }
                        });
                        selectNextUserDialog.show(getActivity().getSupportFragmentManager(), "LeaveApplyFragmentSelectNext");
                    }
                });

            }
        });
    }

    private void officialLeaveApply(List<CheckBoxChild> sureUsers) {
        HashMap<String, String> hashMap = new HashMap<>();
        hashMap.put("processId", StringsFiled.Leave_ProcessId);
        hashMap.put("orderId", mOrderId);
        hashMap.put("taskId", mTaskId);
        hashMap.put("taskName", mTaskName);
        hashMap.put("Method", "0");
        hashMap.put("userName", SharedPreferencesUtil.getString(ViewsUitls.getContext(), StringsFiled.LOGIN_USER, ""));
        hashMap.put("assignee", mAssignee);
        String userList = "";
        for (int i = 0; i < sureUsers.size(); i++) {
            userList += (sureUsers.get(i).getUserName() + ",");
        }
        hashMap.put("userList", userList);

        // 以下为表单上的填写数据
        hashMap.put("title", mLeaveTitle.getCustomEditTextRight().getText().toString());
        hashMap.put("qtype", mLeaveTypeEdit.getCustomEditTextRight().getText().toString());
        hashMap.put("stime", mStartTime.getmDayOfYear().getText().toString());
        hashMap.put("etime", mEndTime.getmDayOfYear().getText().toString());
        String leaveDayNumber = mLeaveDayNumber.getCustomEditTextRight().getText().toString().split(" ")[0] + "." + mSelectHour.getCustomEditTextRight().getText().toString().split(" ")[0];
        hashMap.put("allleave", leaveDayNumber);
        hashMap.put("bz", mLeaveRemark.getCustomEditTextRight().getText().toString());
        hashMap.put("result", mResult);

        OkHttpMethod.asynPostRequest(IpFiled.LEAVE_APPLY_SUBMIT, hashMap, new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                showToastToMain("服务器异常，请联系管理员");
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                if (response != null && response.isSuccessful()) {
                    try {
                        String resultList = response.body().string();
                        if ("success".contains(resultList)) {
                            showToastToMain("申请成功");
                            getActivity().finish();
                        } else {
                            showToastToMain("服务器正忙请稍后");
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                } else {
                    showToastToMain("服务器异常，请联系管理员");
                }
            }
        });
    }
}
