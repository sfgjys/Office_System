package com.minlu.office_system.fragment.form.formPremise;

import android.content.Context;
import android.content.DialogInterface;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;
import android.support.v7.widget.ListPopupWindow;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.DatePicker;
import android.widget.PopupWindow;
import android.widget.TimePicker;

import com.minlu.baselibrary.base.BaseFragment;
import com.minlu.baselibrary.util.SharedPreferencesUtil;
import com.minlu.baselibrary.util.StringUtils;
import com.minlu.baselibrary.util.TimeTool;
import com.minlu.baselibrary.util.ToastUtil;
import com.minlu.baselibrary.util.ViewsUitls;
import com.minlu.office_system.IpFiled;
import com.minlu.office_system.PassBackStringData;
import com.minlu.office_system.StringsFiled;
import com.minlu.office_system.activity.FormActivity;
import com.minlu.office_system.bean.SingleOption;
import com.minlu.office_system.customview.EditTextItem;
import com.minlu.office_system.customview.EditTextTimeSelector;
import com.minlu.office_system.fragment.dialog.MultipleChoiceDialog;
import com.minlu.office_system.fragment.dialog.OnSureButtonClick;
import com.minlu.office_system.fragment.dialog.PromptDialog;
import com.minlu.office_system.fragment.dialog.SelectNextUserDialog;
import com.minlu.office_system.fragment.time.DatePickerFragment;
import com.minlu.office_system.fragment.time.TimePickerFragment;
import com.minlu.office_system.http.OkHttpMethod;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.TimerTask;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

public abstract class FormFragment extends BaseFragment {

    private FormActivity mFormActivity;

    public interface ShowListPopupItemClickListener {
        void onItemClick(AdapterView<?> parent, View view, int position, long id);

        void onAnchorViewClick(View v);

        void onListPopupDismiss();
    }

    public abstract void disAgreeOnClick(View v);

    public abstract void agreeOnClick(View v);

    public abstract void submitOnClick(View v);

    /*
    * 展示时间选择对话框，参数为点击时间选择对话框的确认后的监听回调
    * */
    public void showTimePickerDialog(TimePickerFragment.SetTimeListener setTimeListener) {
        DialogFragment timePickerFragment = new TimePickerFragment(setTimeListener);
        timePickerFragment.show(getActivity().getSupportFragmentManager(), "timePicker");
    }

    /*
    * 展示日期选择对话框，参数为点击日期选择对话框的确认后的监听回调
    * */
    public void showDatePickerDialog(DatePickerFragment.SetDateListener setDateListener) {
        DialogFragment datePickerFragment = new DatePickerFragment(setDateListener);
        datePickerFragment.show(getActivity().getSupportFragmentManager(), "datePicker");
    }

    /*
    * 在参数一控件下，展示参数二集合中的文本数据，参数三是自定义点击文本条目的监听事件
    * */
    public void setWhichViewShowListPopupWindow(boolean isDirectShow, View anchorView, final List<String> date, final ShowListPopupItemClickListener clickListener, Context context) {
        final ListPopupWindow listPopupWindow = new ListPopupWindow(context);
        listPopupWindow.setWidth(ViewGroup.LayoutParams.WRAP_CONTENT);
        listPopupWindow.setHeight((date.size() > 3) ? ViewsUitls.dpToPx(200) : ViewGroup.LayoutParams.WRAP_CONTENT);
        listPopupWindow.setAnchorView(anchorView);//设置ListPopupWindow的锚点，即关联PopupWindow的显示位置和这个锚点
        listPopupWindow.setModal(true);//设置是否是模式
        listPopupWindow.setAdapter(new ArrayAdapter<>(context, android.R.layout.simple_list_item_1, date));

        listPopupWindow.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                clickListener.onItemClick(parent, view, position, id);
                listPopupWindow.dismiss();
            }
        });
        listPopupWindow.setOnDismissListener(new PopupWindow.OnDismissListener() {
            @Override
            public void onDismiss() {
                clickListener.onListPopupDismiss();
            }
        });
        if (isDirectShow) {
            listPopupWindow.show();
            clickListener.onAnchorViewClick(null);
        } else {
            anchorView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    listPopupWindow.show();
                    clickListener.onAnchorViewClick(v);
                }
            });
        }
    }

    /*
    * 设置背景颜色变暗 参数0.0f~1.0f
    * */
    public void setBackGroundDarkColor(float alpha) {
        WindowManager.LayoutParams layoutParams = getActivity().getWindow().getAttributes();
        layoutParams.alpha = alpha;
        getActivity().getWindow().setAttributes(layoutParams);
    }


    /*
    * 将需要展示时间选择对话框的EditTextTimeSelector控件传入进行具体操作代码
    * */
    public void startUseEditTextOnClickShowTimePicker(final EditTextTimeSelector editTextTimeSelector) {
        editTextTimeSelector.setDayOrTimeOnClickListener(new EditTextTimeSelector.DayOrTimeOnClickListener() {
            @Override
            public void onTimeClick(View v) { // 点击24h的事件
                // 展示24h时间选择对话框
                showTimePickerDialog(new TimePickerFragment.SetTimeListener() {
                    @Override
                    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {// 点击对话框确认的方法回调
                        // TODO 点击时间选择对话框的时候设置String时间,需要注意格式,还有下面的
                        editTextTimeSelector.setTimeOfDayText(StringUtils.lessThanNineConvertString(hourOfDay) + ":" + StringUtils.lessThanNineConvertString(minute));
                    }
                });
            }

            @Override
            public void onDayClick(View v) {// 点击年月日的事件
                // 展示年月日时间选择对话框
                showDatePickerDialog(new DatePickerFragment.SetDateListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {// 点击对话框确认的方法回调
                        long selectTime = TimeTool.textTimeToLongTime(year + "-" + (month + 1) + "-" + dayOfMonth, "yyyy-MM-dd");
                        long nowTime = TimeTool.textTimeToLongTime(Calendar.getInstance().get(Calendar.YEAR) + "-" + (Calendar.getInstance().get(Calendar.MONTH) + 1) + "-" + Calendar.getInstance().get(Calendar.DAY_OF_MONTH), "yyyy-MM-dd");
                        if (selectTime < nowTime) {// 选择的日期小于现在的是不可能的，要复原
                            year = Calendar.getInstance().get(Calendar.YEAR);
                            month = Calendar.getInstance().get(Calendar.MONTH);
                            dayOfMonth = Calendar.getInstance().get(Calendar.DAY_OF_MONTH);
                        }
                        editTextTimeSelector.setDayOfYearText(year + "-" + (StringUtils.lessThanNineConvertString(month + 1)) + "-" + StringUtils.lessThanNineConvertString(dayOfMonth));
                    }
                });
            }
        });
    }


    /* 同步网络请求获取表单详情 */
    public Response requestFormListItemDetail() {
        HashMap<String, String> hashMap = new HashMap<>();
        hashMap.put("processId", getProcessIdFromList());
        hashMap.put("orderId", getOrderIdFromList());
        hashMap.put("taskId", getTaskIdFromList());
        hashMap.put("userName", SharedPreferencesUtil.getString(ViewsUitls.getContext(), StringsFiled.LOGIN_USER, ""));
        return OkHttpMethod.synPostRequest(IpFiled.FORM_LIST_ITEM_DETAIL, hashMap);
    }

    public String getTaskIdFromList() {
        return SharedPreferencesUtil.getString(ViewsUitls.getContext(), StringsFiled.FORM_LIST_TO_FORM_TASK_ID, "");
    }

    public String getOrderIdFromList() {
        return SharedPreferencesUtil.getString(ViewsUitls.getContext(), StringsFiled.FORM_LIST_TO_FORM_ORDER_ID, "");
    }

    public String getProcessIdFromList() {
        return SharedPreferencesUtil.getString(ViewsUitls.getContext(), StringsFiled.FORM_LIST_TO_FORM_PROCESS_ID, "");
    }

    /* 六个审批模块请求网络获取审批建议 */
    public void getAllSuggest(AnalysisJSON analysisJSON) {
        HashMap<String, String> hashMap = new HashMap<>();
        hashMap.put("orderId", getOrderIdFromList());
        Response response = OkHttpMethod.synPostRequest(IpFiled.GET_ALL_SUGGEST, hashMap);
        if (response != null && response.isSuccessful()) {
            try {
                String resultList = response.body().string();
                if (StringUtils.interentIsNormal(resultList)) {
                    JSONObject jsonObject = new JSONObject(resultList);
                    analysisJSON.analysisJSON(jsonObject);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    /* 解析审批意见json数据的接口 */
    public interface AnalysisJSON {
        void analysisJSON(JSONObject jsonObject);
    }


    /* 请求网络获取下一步操作数据 */
    public void getNextPersonData(String assignee, String org_id, String autoOrg, final String tag1, final String tag2, final String dialogHint, final PassBackStringData passBackStringData) {
        startLoading();

        HashMap<String, String> hashMap = new HashMap<>();
        hashMap.put("assignee", assignee);
        hashMap.put("org_id", org_id);
        hashMap.put("autoOrg", autoOrg);
        OkHttpMethod.asynPostRequest(IpFiled.REQUEST_USER_LIST, hashMap, new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                showToastAndEndLoading("服务器正忙,请稍后");
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                if (response != null && response.isSuccessful()) {
                    try {
                        String resultList = response.body().string();
                        if (StringUtils.interentIsNormal(resultList)) {
                            JSONArray jsonArray = new JSONArray(resultList);
                            final List<SingleOption> nextUsers = new ArrayList<>();
                            for (int i = 0; i < jsonArray.length(); i++) {
                                JSONObject nextUserData = jsonArray.getJSONObject(i);
                                nextUsers.add(new SingleOption(nextUserData.optString("TRUENAME"), nextUserData.optString("USERNAME"), nextUserData.optString("ORG_INFOR"), ""));
                            }
                            // 走到这mNextUsers里已经有了下一步操作人的数据(或者为空)
                            ViewsUitls.runInMainThread(new TimerTask() {
                                @Override
                                public void run() {
                                    showNextPersonData(nextUsers, passBackStringData, tag1, tag2, dialogHint);
                                }
                            });
                        } else {
                            showToastAndEndLoading("服务器正忙,请稍后");
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                        showToastAndEndLoading("服务器正忙,请稍后");
                    }
                } else {
                    showToastAndEndLoading("服务器正忙,请稍后");
                }
            }
        });
    }

    /* 将下一步操作人通过对话框展示出来 */
    private void showNextPersonData(final List<SingleOption> nextUsers, final PassBackStringData passBackStringData,
                                    String tag1, String tag2, String dialogHint) {
        if (nextUsers.size() > 0) {
            endLoading();
            SelectNextUserDialog selectNextUserDialog = new SelectNextUserDialog();
            selectNextUserDialog.setDialogDataPacket(nextUsers);
            selectNextUserDialog.setOnSureButtonClick(new OnSureButtonClick() {
                @Override
                public void onSureClick(DialogInterface dialog, int id, List<Boolean> isChecks) {
                    List<SingleOption> sureUsers = new ArrayList<>();
                    // 通过isChecks集合中的选择数据去判断哪些数据选中，并将选中的数据填进sureUsers集合中
                    for (int i = 0; i < isChecks.size(); i++) {
                        if (isChecks.get(i)) {
                            sureUsers.add(nextUsers.get(i));
                        }
                    }
                    // 用户有可能不选择下一步操作人，就点击了确定
                    if (sureUsers.size() > 0) {
                        String userList = "";
                        for (int i = 0; i < sureUsers.size(); i++) {
                            userList += (sureUsers.get(i).getUserName() + ",");
                        }
                        passBackStringData.passBackStringData(userList);
                    } else {
                        showToastToMain("请先选择下一步操作人");
                    }
                }
            });
            selectNextUserDialog.show(getActivity().getSupportFragmentManager(), tag1);
        } else {
            endLoading();
            PromptDialog promptDialog = new PromptDialog(new PromptDialog.OnSureButtonClick() {
                @Override
                public void onSureClick(DialogInterface dialog, int id) {
                    passBackStringData.passBackStringData("");
                }
            }, dialogHint);
            promptDialog.show(getActivity().getSupportFragmentManager(), tag2);
        }
    }

    /* 请求网络获取该流程步骤可以进行驳回的步骤 */
    public void requestRejectWhichStep(final PassBackStringData passBackStringData) {
        startLoading();
        HashMap<String, String> hashMap = new HashMap<>();
        hashMap.put("orderId", getOrderIdFromList());
        hashMap.put("processId", getProcessIdFromList());
        OkHttpMethod.asynPostRequest(IpFiled.REJECT_WHICH_STEP, hashMap, new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                showToastAndEndLoading("服务器异常,请稍后");
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                if (response != null && response.isSuccessful()) {
                    try {
                        String resultList = response.body().string();
                        if (StringUtils.interentIsNormal(resultList)) {
                            JSONArray jsonArray = new JSONArray(resultList);
                            final List<SingleOption> rejectSteps = new ArrayList<>();
                            for (int i = 0; i < jsonArray.length(); i++) {
                                JSONObject rejectStep = jsonArray.getJSONObject(i);
                                rejectSteps.add(new SingleOption(rejectStep.optString("displayName"), "", "", rejectStep.optString("taskName")));
                            }
                            /* 根据结果来真是驳回步骤的单选对话框 */
                            ViewsUitls.runInMainThread(new TimerTask() {
                                @Override
                                public void run() {
                                    endLoading();
                                    final MultipleChoiceDialog multipleChoiceDialog = new MultipleChoiceDialog();
                                    multipleChoiceDialog.setSingleOptions("请选择驳回的流程步骤", rejectSteps);
                                    multipleChoiceDialog.setOnSureButtonClick(new OnSureButtonClick() {
                                        @Override
                                        public void onSureClick(DialogInterface dialog, int id, List<Boolean> isChecks) {
                                            String rejectStepTaskNam = "";
                                            for (int i = 0; i < isChecks.size(); i++) {
                                                if (isChecks.get(i)) {
                                                    rejectStepTaskNam = rejectSteps.get(i).getRejectStepTaskName();
                                                    break;
                                                }
                                            }
                                            // 将选择的驳回步骤的taskName通过接口进行回调
                                            if (StringUtils.isEmpty(rejectStepTaskNam)) {
                                                ToastUtil.showToast(ViewsUitls.getContext(), "请先选择需要驳回的流程步骤");
                                            } else {
                                                passBackStringData.passBackStringData(rejectStepTaskNam);
                                            }
                                        }
                                    });
                                    multipleChoiceDialog.show(getActivity().getSupportFragmentManager(), "Plan_Summary_Select_Quarter_Type");
                                }
                            });
                        } else {
                            showToastAndEndLoading("服务器正忙,请稍后");
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                        showToastAndEndLoading("服务器正忙,请稍后");
                    }
                } else {
                    showToastAndEndLoading("服务器正忙,请稍后");
                }
            }
        });
    }

    /* 根据参数创建表单提交所需要的统一参数 */
    @NonNull
    public HashMap<String, String> getUnifiedDataHashMap() {
        HashMap<String, String> hashMap = new HashMap<>();
        hashMap.put("processId", getProcessIdFromList());
        hashMap.put("orderId", getOrderIdFromList());
        hashMap.put("taskId", getTaskIdFromList());
        hashMap.put("userName", SharedPreferencesUtil.getString(ViewsUitls.getContext(), StringsFiled.LOGIN_USER, ""));
        return hashMap;
    }

    /* 最后提交表单的方法 */
    public void startUltimatelySubmit(String url, HashMap<String, String> passingParameters, final String successCondition, final String throwText, final String successText) {
        OkHttpMethod.asynPostRequest(url, passingParameters, new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                showToastAndEndLoading(throwText);
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                if (response != null && response.isSuccessful()) {
                    try {
                        String resultList = response.body().string();
                        if (successCondition.contains(resultList)) {
                            showToastAndEndLoading(successText);
                            getActivity().finish();
                        } else {
                            showToastAndEndLoading(throwText);
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                        showToastAndEndLoading(throwText);
                    }
                } else {
                    showToastAndEndLoading(throwText);
                }
            }
        });
    }

    public void showToastAndEndLoading(String text) {
        endLoading();
        showToastToMain(text);
    }

    /* 设置EditTextItem控件可见并可以编辑 */
    public void showEditTextItemCanEdit(EditTextItem editTextItem) {
        editTextItem.setVisibility(View.VISIBLE);
        editTextItem.getCustomEditTextRight().setFocusableInTouchMode(true);
        editTextItem.getCustomEditTextRight().setFocusable(true);
    }

    /* 设置EditTextItem控件可见并可以点击 */
    public void showEditTextItemCanClick(EditTextItem editTextItem) {
        editTextItem.setVisibility(View.VISIBLE);
        editTextItem.getCustomEditTextRight().setEnabled(true);
    }


    // 开启加载
    public void startLoading() {
        ViewsUitls.runInMainThread(new TimerTask() {
            @Override
            public void run() {
                getFormActivity().setLoadingVisibility(View.VISIBLE);
                getFormActivity().setIsInterruptTouch(true);
            }
        });
    }

    // 结束加载
    public void endLoading() {
        ViewsUitls.runInMainThread(new TimerTask() {
            @Override
            public void run() {
                getFormActivity().setLoadingVisibility(View.GONE);
                getFormActivity().setIsInterruptTouch(false);
            }
        });
    }

    public void showToastToMain(final String s) {
        ViewsUitls.runInMainThread(new TimerTask() {
            @Override
            public void run() {
                ToastUtil.showToast(ViewsUitls.getContext(), s);
            }
        });
    }

    public FormActivity getFormActivity() {
        if (mFormActivity == null) {
            mFormActivity = (FormActivity) getActivity();
        }
        return mFormActivity;
    }
}
