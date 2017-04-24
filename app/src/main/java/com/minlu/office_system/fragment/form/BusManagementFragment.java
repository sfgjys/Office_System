package com.minlu.office_system.fragment.form;

import android.content.DialogInterface;
import android.view.View;

import com.minlu.baselibrary.base.ContentPage;
import com.minlu.baselibrary.util.StringUtils;
import com.minlu.baselibrary.util.ViewsUitls;
import com.minlu.office_system.IpFiled;
import com.minlu.office_system.PassBackStringData;
import com.minlu.office_system.R;
import com.minlu.office_system.activity.FormActivity;
import com.minlu.office_system.bean.SingleOption;
import com.minlu.office_system.customview.EditTextItem;
import com.minlu.office_system.customview.EditTextTimeSelector;
import com.minlu.office_system.fragment.dialog.PromptDialog;
import com.minlu.office_system.fragment.form.formPremise.FormFragment;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import okhttp3.Response;

/**
 * Created by user on 2017/3/7.
 */
public class BusManagementFragment extends FormFragment {

    private ArrayList<String> excessive;
    private List<SingleOption> mNextUsers;
    private String mTitle;
    private String mOffice;
    private String mDestination;
    private String mUseBusCause;
    private String mBusType;
    private EditTextItem mApproveIdea;
    private String mGoAlongPerson;
    private String[] mStartTime;
    private String[] mEndTime;
    private String mBusTypeRemarkText;
    private String mOfficeIdeaText;
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

        View inflate = ViewsUitls.inflate(R.layout.form_bus_management);

        initView(inflate);

        return inflate;
    }

    private void initView(View inflate) {
        // 正常数据显示
        mApproveIdea = (EditTextItem) inflate.findViewById(R.id.form_bus_management_approve_idea);
        EditTextItem title = (EditTextItem) inflate.findViewById(R.id.form_bus_management_title);
        title.setEditText(mTitle);
        EditTextItem office = (EditTextItem) inflate.findViewById(R.id.form_bus_management_office);
        office.setEditText(mOffice);
        EditTextItem busType = (EditTextItem) inflate.findViewById(R.id.form_bus_management_bus_type);
        busType.setEditText(mBusType);
        EditTextItem destination = (EditTextItem) inflate.findViewById(R.id.form_bus_management_destination);
        destination.setEditText(mDestination);
        EditTextItem cause = (EditTextItem) inflate.findViewById(R.id.form_bus_management_cause);
        cause.setEditText(mUseBusCause);
        EditTextItem goAlongPerson = (EditTextItem) inflate.findViewById(R.id.form_bus_management_go_along_person);
        goAlongPerson.setEditText(mGoAlongPerson);
        EditTextTimeSelector startTime = (EditTextTimeSelector) inflate.findViewById(R.id.form_bus_management_start_time);
        startTime.setDayOfYearText(mStartTime[0]);
        startTime.setTimeOfDayText(mStartTime[1]);
        EditTextTimeSelector endTime = (EditTextTimeSelector) inflate.findViewById(R.id.form_bus_management_end_time);
        endTime.setDayOfYearText(mEndTime[0]);
        endTime.setTimeOfDayText(mEndTime[1]);
        EditTextItem busTypeRemark = (EditTextItem) inflate.findViewById(R.id.form_bus_management_bus_type_remark);
        if (StringUtils.isEmpty(mBusTypeRemarkText)) {
            busTypeRemark.setVisibility(View.GONE);
        } else {
            busTypeRemark.setVisibility(View.VISIBLE);
            busTypeRemark.setEditText(mBusTypeRemarkText);
        }
        ViewsUitls.setWidthFromTargetView(title.getCustomEditTextLeft(), busType.getCustomEditTextLeft());
        ViewsUitls.setWidthFromTargetView(title.getCustomEditTextLeft(), destination.getCustomEditTextLeft());

        // 审批各步骤的审批意见
        EditTextItem officeIdea = (EditTextItem) inflate.findViewById(R.id.form_bus_management_office_idea);
        officeIdea.setEditText(mOfficeIdeaText);

        // 此时用户填写的意见
        mApproveIdea = (EditTextItem) inflate.findViewById(R.id.form_bus_management_approve_idea);
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

    private void analyticalData(JSONObject jsonObject) {
        // 正常显示数据
        mTitle = jsonObject.optString("TITLE");
        mOffice = jsonObject.optString("SQCS");
        mDestination = jsonObject.optString("MDD");
        mUseBusCause = jsonObject.optString("YCSY");
        mBusType = jsonObject.optString("CAR_NO");
        mGoAlongPerson = jsonObject.optString("SXRY");
        mStartTime = jsonObject.optString("BTIME").split(" ");
        mEndTime = jsonObject.optString("ETIME").split(" ");
        mBusTypeRemarkText = jsonObject.optString("JSY");

        // 后面的接口需要到的数据
        mAssignee = jsonObject.optString("ASSIGNEE");
        mTaskName = jsonObject.optString("TASKNAME");

        getAllSuggest(new AnalysisJSON() {
            @Override
            public void analysisJSON(JSONObject jsonObject) {
                mOfficeIdeaText = jsonObject.optString("rect3suggest");
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
                officialBusUseApply("", -1);
            }
        }, "是否不同意该用车请求 !");
        promptDialog.show(getActivity().getSupportFragmentManager(), "BusManagementFragment");
    }

    @Override
    public void agreeOnClick(View v) {
        getNextPersonData(mAssignee, "", "", "BusManagementAgree_Have_Next", "BusManagementAgree_No_Next", "是否同意该车辆申请", new PassBackStringData() {
            @Override
            public void passBackStringData(String passBackData) {
                officialBusUseApply(passBackData, 0);
            }
        });
    }

    @Override
    public void submitOnClick(View v) {
    }

    private void officialBusUseApply(String userList, int method) {
        startLoading();

        HashMap<String, String> hashMap = getUnifiedDataHashMap();

        hashMap.put("taskName", mTaskName);
        hashMap.put("assignee", mAssignee);
        hashMap.put("method", "" + method);
        hashMap.put("userList", userList);

        // 以下为表单上的填写数据
        hashMap.put("suggest", mApproveIdea.getCustomEditTextRight().getText().toString());

        startUltimatelySubmit(IpFiled.BUS_REQUEST_APPLY_SUBMIT, hashMap, "success", "服务器正忙,请稍后", "提交成功");
    }
}