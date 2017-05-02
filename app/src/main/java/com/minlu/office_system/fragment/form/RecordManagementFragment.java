package com.minlu.office_system.fragment.form;

import android.content.DialogInterface;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.DatePicker;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.minlu.baselibrary.base.ContentPage;
import com.minlu.baselibrary.util.SharedPreferencesUtil;
import com.minlu.baselibrary.util.StringUtils;
import com.minlu.baselibrary.util.ViewsUitls;
import com.minlu.office_system.IpFiled;
import com.minlu.office_system.PassBackStringData;
import com.minlu.office_system.R;
import com.minlu.office_system.StringsFiled;
import com.minlu.office_system.activity.FormActivity;
import com.minlu.office_system.bean.CountersignSuggestBean;
import com.minlu.office_system.customview.EditTextItem;
import com.minlu.office_system.customview.TableSuggest;
import com.minlu.office_system.fragment.dialog.PromptDialog;
import com.minlu.office_system.fragment.form.formPremise.FormFragment;
import com.minlu.office_system.fragment.time.DatePickerFragment;
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

public class RecordManagementFragment extends FormFragment {

    private ArrayList<String> excessive;
    private EditTextItem mApproveIdea;
    private String mTitle = "";
    private String mTextNumber = "";
    private String mTextUnit = "";
    private String mTextTime = "";
    private ArrayList<String> mDownloadFileName;
    private ArrayList<String> mDownloadFilePath;

    private String mProposeToIdeaText = "";
    private String mTranspondOfficeText = "";
    private String mAssignee = "";
    private String mTaskName = "";
    private String mOrd = "";
    private String mAutoOrg = "";
    private String mStep = "";
    private EditTextItem superiorTextTitle;
    private EditTextItem superiorTextNumber;
    private EditTextItem superiorTextUnit;
    private EditTextItem superiorTextDay;
    private EditTextItem superiorTextType;
    private FormActivity formActivity;
    private View mAccessoryDownload;
    private LinearLayout mAccessoryList;
    private String mFromSdFilePath = "";
    private String mTextType = "普通";
    private List<CountersignSuggestBean> instructionsSuggestData = new ArrayList<>();
    private List<CountersignSuggestBean> readTransactSuggestData = new ArrayList<>();
    private String mEndStepOneselfIdea = "";
    private String mReadStepOneselfIdea = "";

    @Override
    protected void onSubClassOnCreateView() {
        loadDataAndRefresh();
    }

    @Override
    protected View onCreateSuccessView(Bundle savedInstanceState) {
        // 因为本fragment是通过R.id.sv_replace_form控件replace开启的,但是R.id.sv_replace_form控件是居中属性,所以再次我们要使得居中属性去除
        formActivity = (FormActivity) getContext();
        if (formActivity != null) {
            formActivity.setScrollViewNoGravity();
        }

        View inflate = ViewsUitls.inflate(R.layout.form_record_management);

        if (savedInstanceState != null) {
            mTitle = savedInstanceState.getString("record_management_title");
            mTextNumber = savedInstanceState.getString("record_management_number");
            mTextType = savedInstanceState.getString("record_management_type");
            mTextUnit = savedInstanceState.getString("record_management_unit");
            mTextTime = savedInstanceState.getString("record_management_day");
            mDownloadFileName = savedInstanceState.getStringArrayList("record_management_file_name");
            mDownloadFilePath = savedInstanceState.getStringArrayList("record_management_file_path");
            addFromFile();
        }

        initView(inflate);

        return inflate;
    }

    private void addFromFile() {
        if (!StringUtils.isEmpty(mFromSdFilePath)) {// 判断从文件管理器选择的文件的路径是否为空
            String[] split = mFromSdFilePath.split("/");
            String fromSdFileName = split[split.length - 1];
            mDownloadFileName.add(fromSdFileName);
            mDownloadFilePath.add(mFromSdFilePath);
            mFromSdFilePath = "";
        }
    }

    private void initView(View inflate) {
        // 正常的展示数据
        superiorTextTitle = (EditTextItem) inflate.findViewById(R.id.form_record_management_title);
        superiorTextTitle.setEditText(mTitle);// 来文标题
        superiorTextNumber = (EditTextItem) inflate.findViewById(R.id.form_record_management_number);
        superiorTextNumber.setEditText(mTextNumber);// 来文文号
        superiorTextUnit = (EditTextItem) inflate.findViewById(R.id.form_record_management_unit);
        superiorTextUnit.setEditText(mTextUnit);// 来文单位
        superiorTextDay = (EditTextItem) inflate.findViewById(R.id.form_record_management_day);
        superiorTextDay.setEditText(mTextTime);// 来文日期
        superiorTextType = (EditTextItem) inflate.findViewById(R.id.form_record_management_type);
        superiorTextType.setEditText(mTextType);

        // 有拟办意见就先展示(具体这个能不能编辑就看下面的代码)，没有拟办意见就不展示
        EditTextItem proposeToIdea = (EditTextItem) inflate.findViewById(R.id.form_record_management_propose_to_idea);
        proposeToIdea.setEditTextGistIsEmpty(mProposeToIdeaText);// 拟办意见

        // 有用户原先的审批意见(mEndStepOneselfIdea)就先展示(具体这个能不能编辑就看下面的代码)，没有就不展示
        EditTextItem leadApprove = (EditTextItem) inflate.findViewById(R.id.form_record_management_approve_idea);
        leadApprove.setEditTextGistIsEmpty(mEndStepOneselfIdea);// mEndStepOneselfIdea只有在第3步才可能有值

        // 转发处室
        EditTextItem transpondOffice = (EditTextItem) inflate.findViewById(R.id.form_record_management_transpond_office);
        transpondOffice.setEditTextGistIsEmpty(mTranspondOfficeText);

        // 处室阅办
        EditTextItem readTransactSuggest = (EditTextItem) inflate.findViewById(R.id.form_record_management_read_transact_suggest);
        readTransactSuggest.setEditTextGistIsEmpty(mReadStepOneselfIdea);// mReadStepOneselfIdea只有在第4步才可能有值

        mAccessoryDownload = inflate.findViewById(R.id.form_record_management_details);// 附件下载整体控件
        mAccessoryList = (LinearLayout) inflate.findViewById(R.id.form_record_management_details_right);// 附件下载右边的附件列表
        refreshDownloadView();// 下载控件

        // 领导批示表格式建议
        TableSuggest leadApproveTable = (TableSuggest) inflate.findViewById(R.id.form_record_management_lead_approve_table);
        leadApproveTable.addTableData(instructionsSuggestData);

        // 处室阅办表示式建议
        TableSuggest officeReadTable = (TableSuggest) inflate.findViewById(R.id.form_record_management_office_read_table);
        officeReadTable.addTableData(readTransactSuggestData);

        switch (Integer.parseInt(mStep)) {
            case 1:// 来文被打回
                showStep1View();
                formActivity.showAgreeSubmitButton(View.GONE, View.VISIBLE);
                break;
            case 2:// 来文拟办意见
                // proposeToIdea在此处可以进行编辑
                showEditTextItemDifferentState(proposeToIdea, "请填写拟办意见", "(原拟办意见)");
                mApproveIdea = proposeToIdea;// mApproveIdea在此处没有编辑，所以用来重新赋值
                formActivity.showAgreeSubmitButton(View.VISIBLE, View.GONE);
                break;
            case 3:// 领导批示
                showEditTextItemDifferentState(leadApprove, "请填写批示意见", "(原批示意见)");
                mApproveIdea = leadApprove;// mApproveIdea在此处没有编辑，所以用来重新赋值
                formActivity.showAgreeSubmitButton(View.VISIBLE, View.GONE);
                break;
            case 5:// 转发处室_没有会签
                // transpondOffice在此处可以进行编辑
                showEditTextItemDifferentState(transpondOffice, "请填写转发意见", "(原转发意见)");
                mApproveIdea = transpondOffice;// mApproveIdea在此处没有编辑，所以用来重新赋值
                formActivity.showAgreeSubmitButton(View.VISIBLE, View.GONE);
                break;
            case 4:// 处室阅办_有会签
                showEditTextItemDifferentState(readTransactSuggest, "请填写阅办意见", "(原阅办意见)");
                mApproveIdea = readTransactSuggest;// mApproveIdea在此处没有编辑，所以用来重新赋值
                formActivity.showAgreeSubmitButton(View.VISIBLE, View.GONE);
                break;
        }
    }

    /* 有数据就展示数据表格，没有就不展示 */
    private void showTableSuggest(List<CountersignSuggestBean> data, LinearLayout suggestView, View suggestLabel) {
        if (data.size() > 0) {
            suggestView.setVisibility(View.VISIBLE);
            suggestLabel.setVisibility(View.VISIBLE);
            for (CountersignSuggestBean recordEndSuggestBean : data) {
                View inflate = ViewsUitls.inflate(R.layout.item_record_end_step_suggest);
                TextView name = (TextView) inflate.findViewById(R.id.item_record_end_step_suggest_name);
                name.setText(recordEndSuggestBean.getSuggestName());
                TextView time = (TextView) inflate.findViewById(R.id.item_record_end_step_suggest_time);
                time.setText(recordEndSuggestBean.getSuggestTime());
                TextView idea = (TextView) inflate.findViewById(R.id.item_record_end_step_suggest_idea);
                idea.setText(recordEndSuggestBean.getSuggestContent());
                suggestView.addView(inflate);
            }
        } else {
            suggestView.setVisibility(View.GONE);
            suggestLabel.setVisibility(View.GONE);
        }
    }

    private void showStep1View() {
        final ArrayList<String> textNumberData = new ArrayList<>();
        textNumberData.add("其他_2017-0039_号");
        textNumberData.add("市部委办局来文_2017-0042_号");
        textNumberData.add("政法委、综治委来文_2017-0017_号");
        textNumberData.add("市委、市政府来文_2017-0031_号");
        textNumberData.add("省司法厅来文_2017-0057_号");
        textNumberData.add("中央、升级来文_2017-0225_号");
        final ArrayList<String> textUnitData = new ArrayList<>();
        textUnitData.add("其他");
        textUnitData.add("常州市综治委");
        textUnitData.add("常州市政法委");
        textUnitData.add("常州市政府");
        textUnitData.add("常州市委");
        textUnitData.add("江苏省司法厅");
        textUnitData.add("江苏省政府");
        textUnitData.add("司法部");
        textUnitData.add("江苏省委");
        textUnitData.add("中央");
        final ArrayList<String> textTypeData = new ArrayList<>();
        textTypeData.add("普通");
        textTypeData.add("10年");
        textTypeData.add("20年");
        textTypeData.add("永久");

        superiorTextType.setVisibility(View.VISIBLE);// 来文类型进行选择
        showEditTextItemCanEdit(superiorTextTitle);
        showEditTextItemCanClick(superiorTextNumber);
        showEditTextItemCanClick(superiorTextUnit);
        showEditTextItemCanClick(superiorTextDay);
        setWhichViewShowListPopupWindow(false, superiorTextNumber.getCustomEditTextRight(), textNumberData, new ShowListPopupItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                superiorTextNumber.getCustomEditTextRight().setText(textNumberData.get(position));
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
        setWhichViewShowListPopupWindow(false, superiorTextUnit.getCustomEditTextRight(), textUnitData, new ShowListPopupItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                superiorTextUnit.getCustomEditTextRight().setText(textUnitData.get(position));
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
        setWhichViewShowListPopupWindow(false, superiorTextType.getCustomEditTextRight(), textTypeData, new ShowListPopupItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                superiorTextType.getCustomEditTextRight().setText(textTypeData.get(position));
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
        superiorTextDay.getCustomEditTextRight().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDatePickerDialog(new DatePickerFragment.SetDateListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                        superiorTextDay.setEditText(year + "-" + (month + 1) + "-" + dayOfMonth);
                    }
                });
            }
        });
    }

    /* 设置新增附件的按钮的显示与点击事件 */
    private void setAddAccessory(View inflate) {
        if (Integer.parseInt(mStep) == 1) {
            View view = inflate.findViewById(R.id.ll_add_accessory_parent);
            view.setVisibility(View.VISIBLE);
            TextView addAccessory = (TextView) inflate.findViewById(R.id.tv_add_accessory);
            addAccessory.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    formActivity.chooseFile();// 调用fragment依附的Activity的方法结果会显示在returnFilePath方法中
                }
            });
        }
    }

    /* 该放通过fragment依附的Activity中选择完文件对结果处理时会调用 */
    public void returnFilePath(String path) {
        mFromSdFilePath = path;// 由于从选择文件界面返回本界面时，大部分成员变量都为空，会重新走生命周期，所以先赋值在考虑其他
        if (mAccessoryList != null) {// 有的手机重回本界面并不会重启周期
            addFromFile();
            refreshDownloadView();
        }
    }

    /* 设置下载控件操作 */
    private void refreshDownloadView() {
        // 清除附件列表下的所有控件
        mAccessoryList.removeAllViews();

        if (mDownloadFileName.size() > 0) {
            mAccessoryDownload.setVisibility(View.VISIBLE);// 显示附件下载整体控件
            for (int i = 0; i < mDownloadFileName.size(); i++) {// 根据附件名字集合添加附件条目

                final int pressIndex = i;// 附件条目控件的index

                View view = ViewsUitls.inflate(R.layout.item_accessory_list);// 附件条目样式

                // 获取附件的名字的控件并设置点击事件
                TextView textView = (TextView) view.findViewById(R.id.tv_accessory_name);
                textView.setText(mDownloadFileName.get(i));
                textView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        PromptDialog promptDialog = new PromptDialog(new PromptDialog.OnSureButtonClick() {
                            @Override
                            public void onSureClick(DialogInterface dialog, int id) {
                                startDownLoad(mDownloadFileName.get(pressIndex), mDownloadFilePath.get(pressIndex));
                            }
                        }, "是否下载 “ " + mDownloadFileName.get(pressIndex) + " ” 附件");
                        promptDialog.show(getActivity().getSupportFragmentManager(), "RecordManagementDisAgree");
                    }
                });
                if (i == 0) {
                    textView.setPadding(ViewsUitls.dpToPx(0), ViewsUitls.dpToPx(0), ViewsUitls.dpToPx(0), ViewsUitls.dpToPx(6));
                } else if (i == (mDownloadFileName.size() - 1)) {
                    textView.setPadding(ViewsUitls.dpToPx(0), ViewsUitls.dpToPx(6), ViewsUitls.dpToPx(0), ViewsUitls.dpToPx(0));
                } else {
                    textView.setPadding(ViewsUitls.dpToPx(0), ViewsUitls.dpToPx(6), ViewsUitls.dpToPx(0), ViewsUitls.dpToPx(6));
                }

                // 获取附件的删除控件并设置点击事件
                ImageView accessoryDelete = (ImageView) view.findViewById(R.id.iv_accessory_delete);
                if (Integer.parseInt(mStep) == 1) {// 如果是步骤1才显示可以移除
                    // TODO 取消删除功能
                    /*accessoryDelete.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            PromptDialog promptDialog = new PromptDialog(new PromptDialog.OnSureButtonClick() {
                                @Override
                                public void onSureClick(DialogInterface dialog, int id) {
                                    mDownloadFileName.remove(pressIndex);// 修改数据源
                                    mDownloadFilePath.remove(pressIndex);
                                    refreshDownloadView();// 并重新添加附件下载条目
                                }
                            }, "是否删除 “ " + mDownloadFileName.get(pressIndex) + " ” 附件");
                            promptDialog.show(getActivity().getSupportFragmentManager(), "RecordManagementDisAgree");
                        }
                    });
                       accessoryDelete.setVisibility(View.VISIBLE);*/
                }
                // 添加附件条目控件
                mAccessoryList.addView(view);
            }
        } else {
            mAccessoryDownload.setVisibility(View.GONE);// 隐藏附件下载整体控件
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

    // **************************************************************************************************************************************************
    @Override
    protected ContentPage.ResultState onLoad() {
        Response response = requestFormListItemDetail();
        if (response != null && response.isSuccessful()) {
            try {
                String resultList = response.body().string();
                if (StringUtils.interentIsNormal(resultList)) {
                    JSONObject jsonObject = new JSONObject(resultList);
                    if (jsonObject.has("MAPINFO")) {// 有标题字段，说明返回的数据正常
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
        mStep = jsonObject.optString("STEP");

        // 审批表单的界面展示数据
        mTitle = jsonObject.optString("TITLE");
        mTextNumber = jsonObject.optString("CALL") + "_" + jsonObject.optString("NUM") + "_号";
        mTextUnit = jsonObject.optString("ORG_NAME");
        mTextTime = jsonObject.optString("REC_TIME");

        // 收文接面上展示的下载附件数据
        JSONArray fileList = jsonObject.optJSONArray("FILELIST");
        mDownloadFileName = new ArrayList<>();// 用于显示的集合
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
        JSONObject mapInfo = jsonObject.optJSONObject("MAPINFO");
        if (mapInfo.has("ord")) {
            mOrd = mapInfo.optInt("ord") + "";
        }
        mAutoOrg = mapInfo.optString("autoOrg");

        // 获取审批建议
        getAllSuggest(new AnalysisJSON() {

            @Override
            public void analysisJSON(JSONObject jsonObject) {

                if (jsonObject.has("rect3method")) {
                    mProposeToIdeaText = getSuggestIdea(jsonObject, "rect3suggest", "rect3method");
                }
                if (jsonObject.has("rect5method")) {
                    mTranspondOfficeText = getSuggestIdea(jsonObject, "rect5suggest", "rect5method");
                }

                excessive = new ArrayList<>();
                excessive.add("excessive");// 给excessive创建实例，并添加元素，让界面走onCreateSuccessView()方法

                if (jsonObject.has("rect4")) {// 最后一步需要多个用户进行审批汇总
                    try {
                        JSONArray jsonArray = new JSONArray(jsonObject.optString("rect4"));
                        for (int i = 0; i < jsonArray.length(); i++) {
                            JSONObject jsonObject1 = jsonArray.getJSONObject(i);
                            if (Integer.parseInt(mStep) == 3) {
                                String string = SharedPreferencesUtil.getString(ViewsUitls.getContext(), StringsFiled.LOGIN_GET_USER_NAME, "");
                                if (!jsonObject1.optString("truename").contains(string)) {
                                    instructionsSuggestData.add(new CountersignSuggestBean(jsonObject1.optString("truename"), jsonObject1.optString("method"), getSuggestIdea(jsonObject1, "suggest", "method"), jsonObject1.optString("time")));
                                } else {
                                    mEndStepOneselfIdea = getSuggestIdea(jsonObject1, "suggest", "method");
                                }
                            } else {
                                instructionsSuggestData.add(new CountersignSuggestBean(jsonObject1.optString("truename"), jsonObject1.optString("method"), getSuggestIdea(jsonObject1, "suggest", "method"), jsonObject1.optString("time")));
                            }
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                        excessive = null;
                    }
                }
                if (jsonObject.has("rect6")) {// 最后一步需要多个用户进行审批汇总
                    try {
                        JSONArray jsonArray = new JSONArray(jsonObject.optString("rect6"));
                        for (int i = 0; i < jsonArray.length(); i++) {
                            JSONObject jsonObject1 = jsonArray.getJSONObject(i);
                            if (Integer.parseInt(mStep) == 4) {// 修改步骤
                                String string = SharedPreferencesUtil.getString(ViewsUitls.getContext(), StringsFiled.LOGIN_GET_USER_NAME, "");
                                if (!jsonObject1.optString("truename").contains(string)) {
                                    readTransactSuggestData.add(new CountersignSuggestBean(jsonObject1.optString("truename"), jsonObject1.optString("method"), getSuggestIdea(jsonObject1, "suggest", "method"), jsonObject1.optString("time")));
                                } else {
                                    mReadStepOneselfIdea = getSuggestIdea(jsonObject1, "suggest", "method");
                                }
                            } else {
                                readTransactSuggestData.add(new CountersignSuggestBean(jsonObject1.optString("truename"), jsonObject1.optString("method"), getSuggestIdea(jsonObject1, "suggest", "method"), jsonObject1.optString("time")));
                            }
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                        excessive = null;
                    }
                }
            }
        });
    }

    /* 如果本界面重启周期需要先存储数据 */
    @Override
    public void onSaveInstanceState(Bundle outState) {
        outState.putString("record_management_title", superiorTextTitle.getCustomEditTextRight().getText().toString());
        outState.putString("record_management_number", superiorTextNumber.getCustomEditTextRight().getText().toString());
        outState.putString("record_management_type", superiorTextType.getCustomEditTextRight().getText().toString());
        outState.putString("record_management_unit", superiorTextUnit.getCustomEditTextRight().getText().toString());
        outState.putString("record_management_day", superiorTextDay.getCustomEditTextRight().getText().toString());
        outState.putStringArrayList("record_management_file_name", mDownloadFileName);
        outState.putStringArrayList("record_management_file_path", mDownloadFilePath);
        super.onSaveInstanceState(outState);
    }

    // **************************************************************************************************************************************************

    @Override
    public void disAgreeOnClick(View v) {
        PromptDialog promptDialog = new PromptDialog(new PromptDialog.OnSureButtonClick() {
            @Override
            public void onSureClick(DialogInterface dialog, int id) {
                officialRecordManagement("", -1);
            }
        }, "是否不同意该收文签收 !");
        promptDialog.show(getActivity().getSupportFragmentManager(), "RecordManagementDisAgree");
    }

    @Override
    public void agreeOnClick(View v) {
        getNextPersonData(mAssignee, mOrd, mAutoOrg, getUserNameHashMap(mStep, 3), "RecordManagementAgree_Have_Next", "RecordManagementAgree_No_Next", "是否同意该收文签收", new PassBackStringData() {
            @Override
            public void passBackStringData(String passBackData) {
                officialRecordManagement(passBackData, 0);
            }
        });
    }

    @Override
    public void submitOnClick(View v) {
        getNextPersonData(mAssignee, mOrd, mAutoOrg, null, "RecordManagementAgree_Have_Next", "RecordManagementAgree_No_Next", "是否重新提交该收文签收", new PassBackStringData() {
            @Override
            public void passBackStringData(String passBackData) {
                startLoading();
                HashMap<String, String> hashMap = getUnifiedDataHashMap();

                hashMap.put("taskName", mTaskName);
                hashMap.put("assignee", mAssignee);
                hashMap.put("userList", passBackData);

                // 以下为表单上的填写数据
                hashMap.put("title", superiorTextTitle.getCustomEditTextRight().getText().toString());
                hashMap.put("step", "1");
                hashMap.put("rec_time", superiorTextDay.getCustomEditTextRight().getText().toString());
                hashMap.put("bcqx", superiorTextType.getCustomEditTextRight().getText().toString());
                hashMap.put("org_name", superiorTextUnit.getCustomEditTextRight().getText().toString());
                String numberType = superiorTextNumber.getCustomEditTextRight().getText().toString();
                String[] split = numberType.split("_");
                hashMap.put("call", split[0]);
                hashMap.put("num", split[1]);
                hashMap.put("sw_type", "");

                startUltimatelySubmit(IpFiled.SUBMIT_IS_AGREE_RECORD, hashMap, "success", "服务器正忙,请稍候", "提交成功");
            }
        });
    }

    private void officialRecordManagement(String userList, int method) {
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