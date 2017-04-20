package com.minlu.office_system.fragment.form;

import android.content.DialogInterface;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;

import com.minlu.baselibrary.base.ContentPage;
import com.minlu.baselibrary.util.SharedPreferencesUtil;
import com.minlu.baselibrary.util.StringUtils;
import com.minlu.baselibrary.util.ViewsUitls;
import com.minlu.office_system.IpFiled;
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

/**
 * Created by user on 2017/3/7.
 */
public class BusRequestFragment extends FormFragment {

    private List<String> mBusNumberData;
    private EditTextItem mTitle;
    private EditTextItem mOffice;
    private EditTextItem mRequestPerson;
    private EditTextItem mBusType;
    private EditTextItem mDestination;
    private EditTextItem mCause;
    private EditTextItem mGoAlongPerson;
    private EditTextTimeSelector mEndTime;
    private EditTextTimeSelector mStartTime;
    private String mUserName;
    private String mOrgName;
    private EditTextItem mBusTypeRemark;
    private String mOrd;
    private String mAssignee;
    private String mAutoOrg;
    private String mTaskId;
    private String mTaskName;
    private String mOrderId;
    private String mStep;

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

        View inflate = ViewsUitls.inflate(R.layout.form_bus_request);

        initView(inflate);

        return inflate;
    }

    private void initView(View inflate) {
        // 展示的已知数据
        mOffice = (EditTextItem) inflate.findViewById(R.id.form_bus_request_office);
        mOffice.setEditText(mOrgName);
        mRequestPerson = (EditTextItem) inflate.findViewById(R.id.form_bus_request_person);
        mRequestPerson.setEditText(mUserName);

        // 展示时间选择
        mStartTime = (EditTextTimeSelector) inflate.findViewById(R.id.form_bus_request_start_time);
        mStartTime.setNowDayOfYearAndTimeOfDay();
        startUseEditTextOnClickShowTimePicker(mStartTime);
        mEndTime = (EditTextTimeSelector) inflate.findViewById(R.id.form_bus_request_end_time);
        mEndTime.setNowDayOfYearAndTimeOfDay();
        startUseEditTextOnClickShowTimePicker(mEndTime);

        // 展示车子座位类型
        mBusTypeRemark = (EditTextItem) inflate.findViewById(R.id.form_bus_request_bus_type_remark);
        mBusType = (EditTextItem) inflate.findViewById(R.id.form_bus_request_bus_type);
        final EditText busNumberEditText = mBusType.getCustomEditTextRight();
        setWhichViewShowListPopupWindow(busNumberEditText, mBusNumberData, new ShowListPopupItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                busNumberEditText.setText(mBusNumberData.get(position));
                if (mBusNumberData.get(position).contains("其他")) {
                    mBusTypeRemark.setVisibility(View.VISIBLE);
                } else {
                    mBusTypeRemark.setVisibility(View.GONE);
                }
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

        // 下面四个是用户编写，提交时获取数据
        mTitle = (EditTextItem) inflate.findViewById(R.id.form_bus_request_title);
        mDestination = (EditTextItem) inflate.findViewById(R.id.form_bus_request_destination);
        mCause = (EditTextItem) inflate.findViewById(R.id.form_bus_request_cause);
        mGoAlongPerson = (EditTextItem) inflate.findViewById(R.id.form_bus_request_go_along_person);

        ViewsUitls.setWidthFromTargetView(mTitle.getCustomEditTextLeft(), mBusType.getCustomEditTextLeft());
        ViewsUitls.setWidthFromTargetView(mTitle.getCustomEditTextLeft(), mDestination.getCustomEditTextLeft());
        ViewsUitls.setWidthFromTargetView(mTitle.getCustomEditTextLeft(), mRequestPerson.getCustomEditTextLeft());
    }

    @Override
    protected ContentPage.ResultState onLoad() {
        mUserName = SharedPreferencesUtil.getString(ViewsUitls.getContext(), StringsFiled.LOGIN_GET_USER_NAME, "");
        mOrgName = SharedPreferencesUtil.getString(ViewsUitls.getContext(), StringsFiled.LOGIN_GET_USER_ORG_NAME, "");

        HashMap<String, String> busRequestPremise = new HashMap<>();
        busRequestPremise.put("processId", StringsFiled.Bus_ProcessId);
        busRequestPremise.put("taskName", "rec");
        busRequestPremise.put("username", mUserName);
        busRequestPremise.put("orderId", "");
        busRequestPremise.put("taskId", "");

        Response response = OkHttpMethod.synPostRequest(IpFiled.BUS_REQUEST_APPLY_PREMISE, busRequestPremise);
        if (response != null && response.isSuccessful()) {
            try {
                String resultList = response.body().string();
                if (StringUtils.interentIsNormal(resultList)) {
                    JSONObject jsonObject = new JSONObject(resultList);
                    if (jsonObject.has("processId")) {
                        analyticalData(jsonObject);
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return chat(mBusNumberData);
    }

    private void analyticalData(JSONObject jsonObject) {
        // 用来请求下一步操作人
        mOrd = jsonObject.optString("ord");
        mAssignee = jsonObject.optString("assignee");
        mAutoOrg = jsonObject.optString("autoOrg");

        // 用于正式提交
        mTaskId = jsonObject.optString("taskId");
        mTaskName = jsonObject.optString("taskName");
        mOrderId = jsonObject.optString("orderId");
        mStep = jsonObject.optString("step");

        mBusNumberData = new ArrayList<>();
        mBusNumberData.add("五座");
        mBusNumberData.add("七座");
        mBusNumberData.add("其他");
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
                getNextPersonData(mAssignee, "BusRequestSubmit_Have_Next", "BusRequestSubmit_No_Next", new PassNextPersonString() {
                    @Override
                    public void passNextPersonString(String userList) {
                        officialBusUseApply(userList, 0);
                    }
                });
            }
        }, "是否将车辆申请进行提交处理 !");
        promptDialog.show(getActivity().getSupportFragmentManager(), "BusRequestSubmit");
    }

    private void officialBusUseApply(String userList, int method) {
        HashMap<String, String> hashMap = new HashMap<>();
        hashMap.put("processId", StringsFiled.Bus_ProcessId);
        hashMap.put("orderId", mOrderId);
        hashMap.put("taskId", mTaskId);
        hashMap.put("taskName", mTaskName);
        hashMap.put("assignee", mAssignee);
        hashMap.put("userName", SharedPreferencesUtil.getString(ViewsUitls.getContext(), StringsFiled.LOGIN_USER, ""));
        hashMap.put("step", mStep);
        hashMap.put("method", "" + method);
        hashMap.put("userList", userList);

        // 以下为表单上的填写数据
        hashMap.put("title", mTitle.getCustomEditTextRight().getText().toString());
        hashMap.put("sqcs", mOffice.getCustomEditTextRight().getText().toString());
        hashMap.put("sqr", mRequestPerson.getCustomEditTextRight().getText().toString());
        hashMap.put("car_no", mBusType.getCustomEditTextRight().getText().toString());
        hashMap.put("jsy", mBusTypeRemark.getCustomEditTextRight().getText().toString());
        hashMap.put("stime", mStartTime.getmDayOfYear().getText().toString() + " " + mStartTime.getmTimeOfDay().getText().toString() + ":00");
        hashMap.put("etime", mEndTime.getmDayOfYear().getText().toString() + " " + mEndTime.getmTimeOfDay().getText().toString() + ":00");
        hashMap.put("mdd", mDestination.getCustomEditTextRight().getText().toString());
        hashMap.put("ycsy", mCause.getCustomEditTextRight().getText().toString());
        hashMap.put("sxry", mGoAlongPerson.getCustomEditTextRight().getText().toString());

        startUltimatelySubmit(IpFiled.BUS_REQUEST_APPLY_SUBMIT, hashMap, "success", "服务器正忙,请稍后", "请假申请提交成功");
    }
}