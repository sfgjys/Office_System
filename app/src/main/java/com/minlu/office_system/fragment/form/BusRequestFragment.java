package com.minlu.office_system.fragment.form;

import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;

import com.minlu.baselibrary.base.ContentPage;
import com.minlu.baselibrary.util.SharedPreferencesUtil;
import com.minlu.baselibrary.util.ToastUtil;
import com.minlu.baselibrary.util.ViewsUitls;
import com.minlu.office_system.IpFiled;
import com.minlu.office_system.R;
import com.minlu.office_system.StringsFiled;
import com.minlu.office_system.activity.FormActivity;
import com.minlu.office_system.bean.CheckBoxChild;
import com.minlu.office_system.customview.EditTextItem;
import com.minlu.office_system.customview.EditTextTimeSelector;
import com.minlu.office_system.fragment.form.formPremise.FormFragment;
import com.minlu.office_system.http.OkHttpMethod;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.TimerTask;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

/**
 * Created by user on 2017/3/7.
 */
public class BusRequestFragment extends FormFragment {

    private List<String> mBusNumberData;
    private EditTextItem mTitle;
    private EditTextItem mOffice;
    private EditTextItem mRequestPerson;
    private EditTextItem mBusNumber;
    private EditTextItem mDestination;
    private EditTextItem mCause;
    private EditTextItem mGoAlongPerson;
    private EditTextTimeSelector mEndTime;
    private EditTextTimeSelector mStartTime;
    private String mUserName;
    private String mOrgName;
    private EditTextItem mBusTypeRemark;

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
        mOffice = (EditTextItem) inflate.findViewById(R.id.form_bus_request_office);
        mOffice.setEditText(mOrgName);
        mRequestPerson = (EditTextItem) inflate.findViewById(R.id.form_bus_request_person);
        mRequestPerson.setEditText(mUserName);

        // 下面四个是用户编写，提交时获取数据
        mTitle = (EditTextItem) inflate.findViewById(R.id.form_bus_request_title);
        mDestination = (EditTextItem) inflate.findViewById(R.id.form_bus_request_destination);
        mCause = (EditTextItem) inflate.findViewById(R.id.form_bus_request_cause);
        mGoAlongPerson = (EditTextItem) inflate.findViewById(R.id.form_bus_request_go_along_person);

        // 展示时间选择
        mStartTime = (EditTextTimeSelector) inflate.findViewById(R.id.form_bus_request_start_time);
        mStartTime.setNowDayOfYearAndTimeOfDay();
        startUseEditTextOnClickShowTimePicker(mStartTime);
        mEndTime = (EditTextTimeSelector) inflate.findViewById(R.id.form_bus_request_end_time);
        mEndTime.setNowDayOfYearAndTimeOfDay();
        startUseEditTextOnClickShowTimePicker(mEndTime);

        // 展示车子座位类型
        mBusTypeRemark = (EditTextItem) inflate.findViewById(R.id.form_bus_request_bus_type_remark);
        mBusNumber = (EditTextItem) inflate.findViewById(R.id.form_bus_request_bus_type);
        final EditText busNumberEditText = mBusNumber.getCustomEditTextRight();
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

        ViewsUitls.setWidthFromTargetView(mTitle.getCustomEditTextLeft(), mBusNumber.getCustomEditTextLeft());
        ViewsUitls.setWidthFromTargetView(mTitle.getCustomEditTextLeft(), mDestination.getCustomEditTextLeft());
        ViewsUitls.setWidthFromTargetView(mTitle.getCustomEditTextLeft(), mRequestPerson.getCustomEditTextLeft());
    }

    @Override
    protected ContentPage.ResultState onLoad() {
        mUserName = SharedPreferencesUtil.getString(ViewsUitls.getContext(), StringsFiled.LOGIN_GET_USER_NAME, "");
        mOrgName = SharedPreferencesUtil.getString(ViewsUitls.getContext(), StringsFiled.LOGIN_GET_USER_ORG_NAME, "");

        mBusNumberData = new ArrayList<>();
        mBusNumberData.add("五座");
        mBusNumberData.add("七座");
        mBusNumberData.add("其他");

        return chat(mBusNumberData);
    }

    @Override
    public void disAgreeOnClick(View v) {
        System.out.println("BusRequestFragment-disAgreeOnClick");
    }

    @Override
    public void agreeOnClick(View v) {
        System.out.println("BusRequestFragment-agreeOnClick");
    }

    @Override
    public void submitOnClick(View v) {
        System.out.println("BusRequestFragment-submitOnClick");
    }

    private void officialBusUseApply(List<CheckBoxChild> sureUsers) {
        HashMap<String, String> hashMap = new HashMap<>();
        hashMap.put("processId", StringsFiled.Leave_ProcessId);
        hashMap.put("orderId", "");
        hashMap.put("taskId", "");
        hashMap.put("taskName", "");
        hashMap.put("Method", "0");
        hashMap.put("userName", SharedPreferencesUtil.getString(ViewsUitls.getContext(), StringsFiled.LOGIN_USER, ""));
        hashMap.put("title", "");
        hashMap.put("qtype", "");
        hashMap.put("stime", "");
        hashMap.put("etime", "");
        hashMap.put("allleave", "");
        hashMap.put("bz", "");
        hashMap.put("result", "");

        String userList = "";
        for (int i = 0; i < sureUsers.size(); i++) {
            userList += (sureUsers.get(i).getUserName() + ",");
        }

        hashMap.put("userList", userList);
        hashMap.put("assignee", "");

        OkHttpMethod.asynPostRequest(IpFiled.LEAVE_APPLY_SUBMIT, hashMap, new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                showToast("服务器异常，请联系管理员");
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                if (response != null && response.isSuccessful()) {
                    try {
                        String resultList = response.body().string();
                        if ("success".contains(resultList)) {
                            showToast("申请成功");
                            getActivity().finish();
                        } else {
                            showToast("服务器正忙请稍后");
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                } else {
                    showToast("服务器异常，请联系管理员");
                }
            }
        });
    }

    private void showToast(final String s) {
        ViewsUitls.runInMainThread(new TimerTask() {
            @Override
            public void run() {
                ToastUtil.showToast(ViewsUitls.getContext(), s);
            }
        });
    }
}