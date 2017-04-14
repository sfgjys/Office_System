package com.minlu.office_system.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import com.minlu.baselibrary.BaseStringsFiled;
import com.minlu.baselibrary.base.BaseFragment;
import com.minlu.baselibrary.base.ContentPage;
import com.minlu.baselibrary.util.SharedPreferencesUtil;
import com.minlu.baselibrary.util.StringUtils;
import com.minlu.baselibrary.util.ViewsUitls;
import com.minlu.office_system.IpFiled;
import com.minlu.office_system.R;
import com.minlu.office_system.StringsFiled;
import com.minlu.office_system.activity.FormActivity;
import com.minlu.office_system.adapter.FormListAdapter;
import com.minlu.office_system.bean.TaskListItem;
import com.minlu.office_system.fragment.form.formPremise.AllForms;
import com.minlu.office_system.http.OkHttpMethod;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import okhttp3.Response;

/**
 * Created by user on 2017/3/29.
 */

public class FormListFragment extends BaseFragment<TaskListItem> {

    private List<TaskListItem> data;
    private int formTypePosition;

    @Override
    protected void onSubClassOnCreateView() {
        loadDataAndRefresh();
    }

    @Override
    protected View onCreateSuccessView() {

        View inflate = ViewsUitls.inflate(R.layout.form_list_view);
        ListView listView = (ListView) inflate.findViewById(R.id.list_view);
        listView.setAdapter(new FormListAdapter(data));

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String orderId = data.get(position).getOrderId();
                String taskId = data.get(position).getTaskId();
                String processId = data.get(position).getProcessId();

                boolean isSuccess1 = SharedPreferencesUtil.saveString(ViewsUitls.getContext(), StringsFiled.FORM_LIST_TO_FORM_ORDER_ID, orderId);
                boolean isSuccess2 = SharedPreferencesUtil.saveString(ViewsUitls.getContext(), StringsFiled.FORM_LIST_TO_FORM_TASK_ID, taskId);
                boolean isSuccess3 = SharedPreferencesUtil.saveString(ViewsUitls.getContext(), StringsFiled.FORM_LIST_TO_FORM_PROCESS_ID, processId);

                if (isSuccess1 && isSuccess2 && isSuccess3) {
                    Intent intent = new Intent();
                    // 标题
                    intent.putExtra(BaseStringsFiled.ACTIVITY_TITLE, AllForms.values()[formTypePosition].getFormName());
                    // 功能类型对应的position
                    intent.putExtra(StringsFiled.HOME_PAGE_TO_FORM_LIST_POSITION, formTypePosition);
                    intent.setClass(getContext(), FormActivity.class);
                    getActivity().startActivity(intent);
                }
            }
        });

        return inflate;
    }

    @Override
    protected ContentPage.ResultState onLoad() {
        // 以这个集合来确定网络请求的结果
        data = null;

        Bundle bundle = getBundle();
        // 从传递过来的Bundle中获取position
        formTypePosition = bundle.getInt(StringsFiled.HOME_PAGE_TO_FORM_LIST_POSITION);

        // 请求网络获取数据
        String mResultList = getResultListString(AllForms.values()[formTypePosition].getGetListParam());

        // 根据mResultList的json数据来决定 data集合的不同状态，以决定显示界面
        if (StringUtils.interentIsNormal(mResultList)) {// 网络获取的结果正常，进行解析
            try {
                JSONObject jsonObject = new JSONObject(mResultList);
                if (jsonObject.has("rows")) {
                    data = new ArrayList<>();// 有rows字段说明json正常,则data集合可以创建，最低情况没有数据
                    JSONArray mListDataJSON = jsonObject.optJSONArray("rows");
                    if (mListDataJSON.length() > 0) {// mListDataJSON数据至少有一条，才有继续下去的意义
                        // 既然mListDataJSON有数据，则必定角标0有数据
                        JSONObject itemListData = mListDataJSON.optJSONObject(0);
                        // 验证单条数据中的需要的字段是否存在
                        if (itemListData.has("processId") && itemListData.has("processName") && itemListData.has("taskVariable") &&
                                itemListData.has("taskName") && itemListData.has("taskCreateTime") && itemListData.has("creater")) {
                            for (int i = 0; i < mListDataJSON.length(); i++) {
                                JSONObject optJSONObject = mListDataJSON.optJSONObject(i);
                                String processId = optJSONObject.optString("processId");
                                String processName = optJSONObject.optString("processName");// 流程名称
                                String taskVariable = optJSONObject.optString("taskVariable");// 标题
                                String taskName = optJSONObject.optString("taskName");// 任务名称
                                String taskCreateTime = optJSONObject.optString("taskCreateTime");// 任务创建时间
                                String creator = optJSONObject.optString("creater");// 创建人

                                // 点击单条条目时需要传递到下个界面的数据
                                String orderId = optJSONObject.optString("orderId");
                                String taskId = optJSONObject.optString("taskId");

                                data.add(new TaskListItem(processId, processName, taskVariable, taskName, taskCreateTime, creator, orderId, taskId));
                            }
                        } else {
                            data = null;// 如果有数据但是获取不到processId等需要的字段，说明有问题，那界面显示为网络异常
                            System.out.println("============================字段异常==========================");
                        }
                    }
                }// data不创建则为null
            } catch (JSONException e) {
                e.printStackTrace();// data不创建则为null
            }
        }// 否则网络获取的结果有异常data不创建则为null
        return chat(data);
    }

    /**
     * 根据 AllForms参数获取对应的processid作为网络请求的参数，去获取网络数据
     */
    @NonNull
    private String getResultListString(String processId) {
        String mResultList = "";
        // 并根据position获取processid作为网络请求参数，获取数据
        HashMap<String, String> hashMap = new HashMap<>();
        hashMap.put("userName", SharedPreferencesUtil.getString(ViewsUitls.getContext(), StringsFiled.LOGIN_USER, ""));
        hashMap.put("processid", processId);
        Response response = OkHttpMethod.synPostRequest(IpFiled.MANY_MANAGE_LIST, hashMap);
        if (response != null && response.isSuccessful()) {
            try {
                mResultList = response.body().string();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return mResultList;
    }
}
