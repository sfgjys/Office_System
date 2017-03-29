package com.minlu.office_system.activity;

import android.view.View;
import android.webkit.WebView;

import com.minlu.baselibrary.base.BaseActivity;
import com.minlu.baselibrary.util.StringUtils;
import com.minlu.baselibrary.util.ToastUtil;
import com.minlu.baselibrary.util.ViewsUitls;
import com.minlu.office_system.IpFiled;
import com.minlu.office_system.R;
import com.minlu.office_system.StringsFiled;
import com.minlu.office_system.http.OkHttpMethod;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.HashMap;
import java.util.TimerTask;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

/**
 * Created by user on 2017/3/28.
 */

public class NoticeInformActivity extends BaseActivity {

    private WebView webView;// 要加载的html
    private String loadHTML;

    @Override
    public void onCreateContent() {
        View content = setContent(R.layout.activity_notice_inform);
        webView = (WebView) content.findViewById(R.id.web_view);

        setLoadingVisibility(View.VISIBLE);

        HashMap<String, String> hashMap = new HashMap<>();
        hashMap.put("id", "" + getmIntent().getIntExtra(StringsFiled.HTML_DETAIL_CODE, -1));
        OkHttpMethod.asynPostRequest(IpFiled.NOTICE_DETAIL, hashMap, new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                ViewsUitls.runInMainThread(new TimerTask() {
                    @Override
                    public void run() {
                        setLoadingVisibility(View.GONE);
                        ToastUtil.showToast(ViewsUitls.getContext(), "服务器异常");
                    }
                });
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                String noticeDetailResult = response.body().string();

                if (StringUtils.interentIsNormal(noticeDetailResult)) {// 返回结果字符串正常就解析
                    try {
                        JSONObject jsonObject = new JSONObject(noticeDetailResult);
                        if (jsonObject.has("CONTENT")) {
                            loadHTML = jsonObject.optString("CONTENT");
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }

                ViewsUitls.runInMainThread(new TimerTask() {
                    @Override
                    public void run() {
                        setLoadingVisibility(View.GONE);
                        if (StringUtils.interentIsNormal(loadHTML)) {
                            webView.loadDataWithBaseURL(null, loadHTML, "text/html", "utf-8", null);
                        } else {
                            ToastUtil.showToast(ViewsUitls.getContext(), "服务器正忙，请稍候");
                        }
                    }
                });
            }
        });
    }
}
