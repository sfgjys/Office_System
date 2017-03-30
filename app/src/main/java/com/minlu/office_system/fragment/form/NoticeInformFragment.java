package com.minlu.office_system.fragment.form;

import android.content.Intent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import com.minlu.baselibrary.BaseStringsFiled;
import com.minlu.baselibrary.base.BaseFragment;
import com.minlu.baselibrary.base.ContentPage;
import com.minlu.baselibrary.util.StringUtils;
import com.minlu.baselibrary.util.ViewsUitls;
import com.minlu.office_system.IpFiled;
import com.minlu.office_system.R;
import com.minlu.office_system.StringsFiled;
import com.minlu.office_system.activity.FormActivity;
import com.minlu.office_system.activity.NoticeInformActivity;
import com.minlu.office_system.adapter.NoticeInformAdapter;
import com.minlu.office_system.bean.NoticeList;
import com.minlu.office_system.http.OkHttpMethod;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import okhttp3.Response;

/**
 * Created by user on 2017/3/27.
 */

public class NoticeInformFragment extends BaseFragment<NoticeList> {

    private List<NoticeList> mNoticeListData;

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


        View inflate = ViewsUitls.inflate(R.layout.layout_list);
        ListView listView = (ListView) inflate.findViewById(R.id.list_view);
        listView.setAdapter(new NoticeInformAdapter(mNoticeListData));
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(ViewsUitls.getContext(), NoticeInformActivity.class);
                intent.putExtra(BaseStringsFiled.ACTIVITY_TITLE, "公告详情");
                intent.putExtra(StringsFiled.HTML_DETAIL_CODE, mNoticeListData.get(position).getmNoticeContentId());
                getActivity().startActivity(intent);
            }
        });

        return inflate;
    }

    @Override
    protected ContentPage.ResultState onLoad() {
        String noticeListResult = null;

        Response response = OkHttpMethod.synPostRequest(IpFiled.NOTICE_LIST, null);

        if (response != null && response.isSuccessful()) {// 请求成功则获取返回结果字符串
            try {
                noticeListResult = response.body().string();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        if (StringUtils.interentIsNormal(noticeListResult)) {// 返回结果字符串正常就解析
            try {
                JSONObject jsonObject = new JSONObject(noticeListResult);
                if (jsonObject.has("rows")) {
                    mNoticeListData = new ArrayList<>();// 有total值说明返回json字符串格式正确,不管有没有公告 都先创建数据对象
                    JSONArray jsonArray = jsonObject.optJSONArray("rows");
                    for (int i = 0; i < jsonArray.length(); i++) {
                        JSONObject notice = jsonArray.optJSONObject(i);
                        JSONObject noticeDetail = notice.optJSONObject("cell");
                        mNoticeListData.add(new NoticeList(noticeDetail.optString("NOTICENAME"), noticeDetail.optString("NOTICEEMPNAME"), noticeDetail.optString("SDATE"), noticeDetail.optInt("ID"), true));
                    }
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

        return chat(mNoticeListData);
    }
}
