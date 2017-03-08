package com.minlu.office_system.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;

import com.minlu.baselibrary.util.SharedPreferencesUtil;
import com.minlu.office_system.R;

/**
 * Created by user on 2017/2/28.
 */

public class SplshActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_splsh);

        // 用延时模拟更新程序
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                // 第一次获取是否第一次登录，因为没有记录所以使用 参数三 来返回,而下次存储过后就可以用存储的来
                boolean mIsFirst = SharedPreferencesUtil.getBoolean(getApplicationContext(), "is_first_login", true);

                if (mIsFirst) {  // 引导界面
                    Intent intent = new Intent(getApplicationContext(), GuideActivity.class);
                    startActivity(intent);
                    finish();
                } else {         // 主界面
                    Intent intent = new Intent(getApplicationContext(), LoginActivity.class);
                    startActivity(intent);
                    finish();
                }
            }
        }, 500);

    }

}
