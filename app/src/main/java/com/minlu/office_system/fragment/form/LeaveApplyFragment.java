package com.minlu.office_system.fragment.form;

import android.content.DialogInterface;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;

import com.minlu.baselibrary.base.ContentPage;
import com.minlu.baselibrary.util.SharedPreferencesUtil;
import com.minlu.baselibrary.util.StringUtils;
import com.minlu.baselibrary.util.TimeTool;
import com.minlu.baselibrary.util.ViewsUitls;
import com.minlu.office_system.IpFiled;
import com.minlu.office_system.PassBackStringData;
import com.minlu.office_system.R;
import com.minlu.office_system.StringsFiled;
import com.minlu.office_system.activity.FormActivity;
import com.minlu.office_system.customview.EditTextItem;
import com.minlu.office_system.customview.EditTextTimeSelector;
import com.minlu.office_system.fragment.dialog.PromptDialog;
import com.minlu.office_system.fragment.form.formPremise.FormFragment;
import com.minlu.office_system.http.OkHttpMethod;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import okhttp3.Response;


public class LeaveApplyFragment extends FormFragment {

    private List<String> mLeaveType;
    private EditTextItem mLeaveTypeEdit;
    private EditTextItem mLeaveDayNumber;
    private EditTextTimeSelector mStartTime;
    private EditTextTimeSelector mEndTime;
    private EditTextItem mLeaveTitle;
    private EditTextItem mLeaveRemark;
    private String mResidueYearLeaveText;
    private String mAddUpLeaveDaysText;
    private EditTextItem mSelectHour;
    private List<String> mSelectHourData;
    private String mResult;
    private String mOrd;
    private String mAssignee;
    private String mAutoOrg;
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
        EditTextItem mAddUpLeaveDays = (EditTextItem) inflate.findViewById(R.id.form_leave_apply_add_up_leave_day_number);
        mAddUpLeaveDays.setEditText(mAddUpLeaveDaysText);
        EditTextItem mResidueLeaveYears = (EditTextItem) inflate.findViewById(R.id.form_leave_apply_residue_leave_year_number);
        mResidueLeaveYears.setEditText(mResidueYearLeaveText);

        mLeaveTitle = (EditTextItem) inflate.findViewById(R.id.form_leave_apply_title);
        mLeaveRemark = (EditTextItem) inflate.findViewById(R.id.form_leave_apply_remark);
        ViewsUitls.setWidthFromTargetView(mLeaveTitle.getCustomEditTextLeft(), mLeaveRemark.getCustomEditTextLeft());

        // 自动填写
        mLeaveDayNumber = (EditTextItem) inflate.findViewById(R.id.form_leave_apply_leave_day_number);

        // 类型列表展示
        mLeaveTypeEdit = (EditTextItem) inflate.findViewById(R.id.form_leave_apply_type);
        EditText typeCustomEditTextRight = mLeaveTypeEdit.getCustomEditTextRight();
        setWhichViewShowListPopupWindow(false, typeCustomEditTextRight, mLeaveType, new ShowListPopupItemClickListener() {
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
        setWhichViewShowListPopupWindow(false, mSelectHour.getCustomEditTextRight(), mSelectHourData, new ShowListPopupItemClickListener() {
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
                        analyticalData(jsonObject);
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return chat(mLeaveType);
    }

    private void analyticalData(JSONObject jsonObject) {
        // 用来显示的
        mResidueYearLeaveText = jsonObject.optString("year");
        mAddUpLeaveDaysText = jsonObject.optString("allday");

        // 用来请求下一步操作人
        mAssignee = jsonObject.optString("assignee");
        mOrd = jsonObject.optString("ord");
        mAutoOrg = jsonObject.optString("autoOrg");

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
                getNextPersonData(mAssignee, "", "", "LeaveApplySubmit_Have_Next", "LeaveApplySubmit_No_Next", "本界面是请假申请的第一步,则必定有下一步操作人",
                        new PassBackStringData() {
                            @Override
                            public void passBackStringData(String passBackData) {
                                officialLeaveApply(passBackData, 0);
                            }
                        });
            }
        }, "是否将请假申请进行提交处理 !");
        promptDialog.show(getActivity().getSupportFragmentManager(), "LeaveApplySubmit");
    }

    private void officialLeaveApply(String userList, int method) {

        startLoading();

        HashMap<String, String> hashMap = new HashMap<>();
        hashMap.put("processId", StringsFiled.Leave_ProcessId);
        hashMap.put("orderId", mOrderId);
        hashMap.put("taskId", mTaskId);
        hashMap.put("taskName", mTaskName);
        hashMap.put("method", "" + method);
        hashMap.put("userName", SharedPreferencesUtil.getString(ViewsUitls.getContext(), StringsFiled.LOGIN_USER, ""));
        hashMap.put("assignee", mAssignee);
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

        startUltimatelySubmit(IpFiled.LEAVE_APPLY_SUBMIT, hashMap, "success", "服务器正忙,请稍后", "请假申请提交成功");
    }
}
