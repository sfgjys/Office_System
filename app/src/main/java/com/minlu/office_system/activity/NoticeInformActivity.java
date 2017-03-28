package com.minlu.office_system.activity;

import android.view.View;
import android.webkit.WebView;

import com.minlu.baselibrary.base.BaseActivity;
import com.minlu.office_system.R;
import com.minlu.office_system.StringsFiled;

/**
 * Created by user on 2017/3/28.
 */

public class NoticeInformActivity extends BaseActivity {
    @Override
    public void onCreateContent() {
        View content = setContent(R.layout.activity_notice_inform);
        WebView webView = (WebView) content.findViewById(R.id.web_view);

        webView.getSettings().setJavaScriptEnabled(true);


        webView.loadDataWithBaseURL(null, getmIntent().getStringExtra(StringsFiled.HTML_DETAIL_CODE), "text/html", "utf-8", null);
    }
}
