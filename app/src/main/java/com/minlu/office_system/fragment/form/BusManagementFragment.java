package com.minlu.office_system.fragment.form;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;

import com.minlu.baselibrary.base.ContentPage;
import com.minlu.baselibrary.util.StringUtils;
import com.minlu.baselibrary.util.ViewsUitls;
import com.minlu.office_system.IpFiled;
import com.minlu.office_system.PassBackStringData;
import com.minlu.office_system.R;
import com.minlu.office_system.activity.FormActivity;
import com.minlu.office_system.customview.EditTextItem;
import com.minlu.office_system.customview.EditTextTimeSelector;
import com.minlu.office_system.fragment.dialog.PromptDialog;
import com.minlu.office_system.fragment.form.formPremise.FormFragment;
import com.minlu.office_system.http.OkHttpMethod;

import org.json.JSONArray;
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
    private String[] mStartTime;
    private String[] mEndTime;
    private String mTitleText = "";
    private String mOfficeText = "";
    private String mDestinationText = "";
    private String mUseBusCauseText = "";
    private String mBusTypeText = "";
    private String mGoAlongPersonText = "";
    private String mBusTypeRemarkText = "";
    private String mOfficeIdeaText = "";
    private String mAssignee = "";
    private String mTaskName = "";
    private String mOrd = "";
    private String mAutoOrg = "";
    private String mLeadTeamPersonText = "";
    private String mStep = "";
    private String mLeadIdeaText = "";
    private String mCarNameText = "";
    private String mDriverNameText = "";
    private FormActivity formActivity;
    private EditTextItem mApproveIdea;
    private List<String> mBusTypeData;
    private List<String> mLicensePlateNumberData = new ArrayList<>();
    private EditTextItem mTitleView;
    private EditTextItem mOfficeView;
    private EditTextItem mBusTypeView;
    private EditTextItem mBusTypeRemarkView;
    private EditTextTimeSelector mStartTimeView;
    private EditTextTimeSelector mEndTimeView;
    private EditTextItem mDestinationView;
    private EditTextItem mCauseView;
    private EditTextItem mLeadTeamPersonView;
    private EditTextItem mGoAlongPersonView;
    private EditTextItem mBusArrangeView;
    private EditTextItem mDriverArrangeView;
    private EditTextItem mOfficeIdeaView;
    private EditTextItem mLeadIdeaView;
    private String mApplyPersonText;
    private EditTextItem mApplyPersonView;

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

        View inflate = ViewsUitls.inflate(R.layout.form_bus_management);

        initView(inflate);

        return inflate;
    }

    private void initView(View inflate) {
        // 下面几个数据展示是必定显示的，只有在驳回到第一步时才会有是否可以编辑的考虑
        mTitleView = (EditTextItem) inflate.findViewById(R.id.form_bus_management_title);
        mTitleView.setEditText(mTitleText);
        mOfficeView = (EditTextItem) inflate.findViewById(R.id.form_bus_management_office);
        mOfficeView.setEditText(mOfficeText);
        mApplyPersonView = (EditTextItem) inflate.findViewById(R.id.form_bus_management_person);
        mApplyPersonView.setEditText(mApplyPersonText);
        mBusTypeView = (EditTextItem) inflate.findViewById(R.id.form_bus_management_bus_type);
        mBusTypeView.setEditText(mBusTypeText);
        mBusTypeRemarkView = (EditTextItem) inflate.findViewById(R.id.form_bus_management_bus_type_remark);
        if (mBusTypeView.getCustomEditTextRight().getText().toString().contains("其他")) {
            mBusTypeRemarkView.setVisibility(View.VISIBLE);
            mBusTypeRemarkView.setEditText(mBusTypeRemarkText);
        } else {
            mBusTypeRemarkView.setVisibility(View.GONE);
        }
        mStartTimeView = (EditTextTimeSelector) inflate.findViewById(R.id.form_bus_management_start_time);
        mStartTimeView.setDayOfYearText(mStartTime[0]);
        mStartTimeView.setTimeOfDayText(mStartTime[1]);
        mEndTimeView = (EditTextTimeSelector) inflate.findViewById(R.id.form_bus_management_end_time);
        mEndTimeView.setDayOfYearText(mEndTime[0]);
        mEndTimeView.setTimeOfDayText(mEndTime[1]);
        mDestinationView = (EditTextItem) inflate.findViewById(R.id.form_bus_management_destination);
        mDestinationView.setEditText(mDestinationText);
        mCauseView = (EditTextItem) inflate.findViewById(R.id.form_bus_management_cause);
        mCauseView.setEditText(mUseBusCauseText);
        mLeadTeamPersonView = (EditTextItem) inflate.findViewById(R.id.form_bus_management_lead_team_person);
        mLeadTeamPersonView.setEditText(mLeadTeamPersonText);
        mGoAlongPersonView = (EditTextItem) inflate.findViewById(R.id.form_bus_management_go_along_person);
        mGoAlongPersonView.setEditText(mGoAlongPersonText);


        // 车辆安排和司机安排
        mBusArrangeView = (EditTextItem) inflate.findViewById(R.id.form_bus_management_bus_arrange);
        mBusArrangeView.setEditTextGistIsEmpty(mCarNameText);
        mDriverArrangeView = (EditTextItem) inflate.findViewById(R.id.form_bus_management_driver_arrange);
        mDriverArrangeView.setEditTextGistIsEmpty(mDriverNameText);

        // 办公室和领导意见
        mOfficeIdeaView = (EditTextItem) inflate.findViewById(R.id.form_bus_management_office_idea);
        mOfficeIdeaView.setEditTextGistIsEmpty(mOfficeIdeaText);
        mLeadIdeaView = (EditTextItem) inflate.findViewById(R.id.form_bus_management_lead_idea);
        mLeadIdeaView.setEditTextGistIsEmpty(mLeadIdeaText);

        ViewsUitls.setWidthFromTargetView(mTitleView.getCustomEditTextLeft(), mBusTypeView.getCustomEditTextLeft());
        ViewsUitls.setWidthFromTargetView(mTitleView.getCustomEditTextLeft(), mDestinationView.getCustomEditTextLeft());
        ViewsUitls.setWidthFromTargetView(mTitleView.getCustomEditTextLeft(), mOfficeIdeaView.getCustomEditTextLeft());
        ViewsUitls.setWidthFromTargetView(mTitleView.getCustomEditTextLeft(), mLeadIdeaView.getCustomEditTextLeft());
        ViewsUitls.setWidthFromTargetView(mTitleView.getCustomEditTextLeft(), mApplyPersonView.getCustomEditTextLeft());

        showEachStepView();
    }

    /* 每一步骤控件的不同的展示 */
    private void showEachStepView() {
        switch (Integer.parseInt(mStep)) {
            case 1:// 车辆申请被驳回到第一步申请界面
                showEditTextItemCanClick(mBusTypeView);
                setWhichViewShowListPopupWindow(false, mBusTypeView.getCustomEditTextRight(), mBusTypeData, new ShowListPopupItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                        mBusTypeView.setEditText(mBusTypeData.get(position));
                        if (mBusTypeData.get(position).contains("其他")) {
                            mBusTypeRemarkView.setVisibility(View.VISIBLE);
                            showEditTextItemCanEdit(mBusTypeRemarkView);
                        } else {
                            mBusTypeRemarkView.setVisibility(View.GONE);
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
                showEditTextItemCanEdit(mDestinationView);
                showEditTextItemCanEdit(mCauseView);
                showEditTextItemCanEdit(mGoAlongPersonView);
                showEditTextItemCanEdit(mLeadTeamPersonView);
                startUseEditTextOnClickShowTimePicker(mStartTimeView);
                startUseEditTextOnClickShowTimePicker(mEndTimeView);
                formActivity.showAgreeSubmitButton(View.GONE, View.VISIBLE);
                break;
            case 2:// 办公室意见审批
                showEditTextItemCanClick(mBusArrangeView);
                setWhichViewShowListPopupWindow(false, mBusArrangeView.getCustomEditTextRight(), mLicensePlateNumberData, new ShowListPopupItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                        mBusArrangeView.setEditText(mLicensePlateNumberData.get(position));
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
                showEditTextItemDifferentState(mDriverArrangeView, "请填写驾驶员安排", "(原驾驶员安排)");
                showEditTextItemDifferentState(mOfficeIdeaView, "请填写办公室意见", "(原办公室意见)");
                mApproveIdea = mOfficeIdeaView;
                formActivity.showAgreeSubmitButton(View.VISIBLE, View.GONE);
                formActivity.onActivityResult(2, 1, new Intent());
                break;
            case 3:// 领导意见审批
                showEditTextItemDifferentState(mLeadIdeaView, "请填领导意见", "(原领导意见)");
                mApproveIdea = mLeadIdeaView;
                formActivity.showAgreeSubmitButton(View.VISIBLE, View.GONE);
                break;
        }
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

        if (Integer.parseInt(mStep) == 2) {
            Response busInfoResult = OkHttpMethod.synPostRequest(IpFiled.GET_BUS_INFO, null);
            if (busInfoResult != null && busInfoResult.isSuccessful()) {
                try {
                    String resultList = busInfoResult.body().string();
                    if (StringUtils.interentIsNormal(resultList)) {
                        JSONArray jsonArray = new JSONArray(resultList);
                        for (int i = 0; i < jsonArray.length(); i++) {
                            JSONObject jsonObject = jsonArray.getJSONObject(i);
                            mLicensePlateNumberData.add(jsonObject.optString("CAR_NO"));
                        }
                    } else {
                        excessive = null;
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                    excessive = null;
                }
            }
        }
        return chat(excessive);
    }

    private void analyticalData(JSONObject jsonObject) {
        mBusTypeData = new ArrayList<>();
        mBusTypeData.add("五座");
        mBusTypeData.add("七座");
        mBusTypeData.add("其他");
        // 正常显示数据
        // 申请人
        mApplyPersonText = jsonObject.optString("SQR");
        mTitleText = jsonObject.optString("TITLE");// 申请标题
        mOfficeText = jsonObject.optString("SQCS");// 申请处室
        mDestinationText = jsonObject.optString("MDD");// 目的地
        mUseBusCauseText = jsonObject.optString("YCSY");// 用车是由
        mBusTypeText = jsonObject.optString("CAR_NO");// 用车类型
        mGoAlongPersonText = jsonObject.optString("SXRY");// 随行人员
        mStartTime = jsonObject.optString("BTIME").split(" ");// 开始时间
        mEndTime = jsonObject.optString("ETIME").split(" ");// 结束时间
        mBusTypeRemarkText = jsonObject.optString("BEIZHU");// 用车类型为 其他 时，有备注这个数据
        JSONObject mapInfo = jsonObject.optJSONObject("MAPINFO");
        mLeadTeamPersonText = mapInfo.optString("ddry");// 带队人员
        // TODO 此处需要进一步的解析
        mCarNameText = mapInfo.optString("cl_sel");// 车辆牌照号
        mDriverNameText = jsonObject.optString("JSY");// 车辆驾驶人员

        // 后面的接口需要到的数据
        mTaskName = jsonObject.optString("TASKNAME");
        mAssignee = jsonObject.optString("ASSIGNEE");
        mAutoOrg = mapInfo.optString("autoOrg");
        mOrd = mapInfo.optString("ord");

        mStep = jsonObject.optString("STEP");

        getAllSuggest(new AnalysisJSON() {
            @Override
            public void analysisJSON(JSONObject jsonObject) {
                if (jsonObject.has("rect3method")) {// 办公室意见
                    mOfficeIdeaText = getSuggestIdea(jsonObject, "rect3suggest", "rect3method");
                }
                if (jsonObject.has("rect4method")) {// 局领导意见
                    mLeadIdeaText = getSuggestIdea(jsonObject, "rect4suggest", "rect4method");
                }
                excessive = new ArrayList<>();
                excessive.add("excessive");// 给excessive创建实例，并添加元素，让界面走onCreateSuccessView()方法
            }
        });
    }

    // *****************************************************************************************************************************************************

    @Override
    public void disAgreeOnClick(View v) {
        PromptDialog promptDialog = new PromptDialog(new PromptDialog.OnSureButtonClick() {
            @Override
            public void onSureClick(DialogInterface dialog, int id) {
                requestRejectWhichStep(new PassBackStringData() {
                    @Override
                    public void passBackStringData(String passBackData) {
                        HashMap<String, String> unifyHashMap = getUnifyHashMap(passBackData, -1);
                        unifyHashMap.put("taskBack", passBackData);
                        officialBusUseApply(unifyHashMap);
                    }
                });
            }
        }, "是否驳回该车辆申请!");
        promptDialog.show(getActivity().getSupportFragmentManager(), "BusManagementFragment");
    }

    @Override
    public void agreeOnClick(View v) {
        getNextPersonData(mAssignee, mOrd, mAutoOrg, null, "BusManagementAgree_Have_Next", "BusManagementAgree_No_Next", "是否同意该车辆申请", new PassBackStringData() {
            @Override
            public void passBackStringData(String passBackData) {
                switch (Integer.parseInt(mStep)) {
                    case 2:// 办公室意见审批
                        HashMap<String, String> hashMap = getUnifyHashMap(passBackData, 0);
                        hashMap.put("jsy", mDriverArrangeView.getCustomEditTextRight().getText().toString().trim());
                        hashMap.put("cl_sel", mBusArrangeView.getCustomEditTextRight().getText().toString().trim());
                        officialBusUseApply(hashMap);
                        break;
                    case 3:
                        officialBusUseApply(getUnifyHashMap(passBackData, 0));
                        break;
                }
            }
        });
    }

    @Override
    public void submitOnClick(View v) {
        getNextPersonData(mAssignee, mOrd, mAutoOrg, null, "BusManagementAgree_Have_Next", "BusManagementAgree_No_Next", "是否同意该车辆申请", new PassBackStringData() {
            @Override
            public void passBackStringData(String passBackData) {
                HashMap<String, String> hashMap = getUnifyHashMap(passBackData, 0);
                hashMap.put("title", mTitleView.getCustomEditTextRight().getText().toString());
                hashMap.put("sqcs", mOfficeView.getCustomEditTextRight().getText().toString());
                hashMap.put("sqr", mApplyPersonView.getCustomEditTextRight().getText().toString());
                hashMap.put("car_no", mBusTypeView.getCustomEditTextRight().getText().toString());
                hashMap.put("beizhu", mBusTypeRemarkView.getCustomEditTextRight().getText().toString());
                hashMap.put("stime", mStartTimeView.getmDayOfYear().getText().toString() + " " + mStartTimeView.getmTimeOfDay().getText().toString() + ":00");
                hashMap.put("etime", mEndTimeView.getmDayOfYear().getText().toString() + " " + mEndTimeView.getmTimeOfDay().getText().toString() + ":00");
                hashMap.put("mdd", mDestinationView.getCustomEditTextRight().getText().toString());
                hashMap.put("ycsy", mCauseView.getCustomEditTextRight().getText().toString());
                hashMap.put("sxry", mGoAlongPersonView.getCustomEditTextRight().getText().toString());
                hashMap.put("ddry", mLeadTeamPersonView.getCustomEditTextRight().getText().toString());
                officialBusUseApply(hashMap);
            }
        });
    }


    public HashMap<String, String> getUnifyHashMap(String userList, int method) {
        HashMap<String, String> hashMap = getUnifiedDataHashMap();
        hashMap.put("taskName", mTaskName);
        hashMap.put("assignee", mAssignee);
        hashMap.put("method", "" + method);
        hashMap.put("userList", userList);
        return hashMap;
    }


    private void officialBusUseApply(HashMap<String, String> hashMap) {
        startLoading();

        // 以下为表单上的填写数据
        if (Integer.parseInt(mStep) != 1) {
            hashMap.put("suggest", mApproveIdea.getCustomEditTextRight().getText().toString());
        }

        startUltimatelySubmit(IpFiled.BUS_REQUEST_APPLY_SUBMIT, hashMap, "success", "服务器正忙,请稍后", "提交成功");
    }
}