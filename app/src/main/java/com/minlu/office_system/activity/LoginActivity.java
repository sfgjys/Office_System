package com.minlu.office_system.activity;

import android.app.Activity;
import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;

import com.minlu.baselibrary.BaseStringsFiled;
import com.minlu.baselibrary.http.OkHttpManger;
import com.minlu.baselibrary.manager.ThreadManager;
import com.minlu.baselibrary.sqlite.MySQLiteOpenHelper;
import com.minlu.baselibrary.util.SharedPreferencesUtil;
import com.minlu.baselibrary.util.StringUtils;
import com.minlu.baselibrary.util.ToastUtil;
import com.minlu.baselibrary.util.ViewsUitls;
import com.minlu.office_system.IpFiled;
import com.minlu.office_system.R;
import com.minlu.office_system.StringsFiled;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.TimerTask;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class LoginActivity extends Activity {

    private EditText mLoginUser;
    private EditText mLoginPassWord;
    private Button mLoginButton;
    private CheckBox mRememberPassWord;
    private MySQLiteOpenHelper mySQLiteOpenHelper;
    private SQLiteDatabase writableDatabase;
    private boolean mIsAuto;
    private String mHistoryPassWord;
    private String mHistoryUser;
    private String mUser;
    private String mPassWord;
    private String loginResult;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_login);

        //创建数据库操作对象
        mySQLiteOpenHelper = new MySQLiteOpenHelper(ViewsUitls.getContext());
        writableDatabase = mySQLiteOpenHelper.getWritableDatabase();

        getData();// 获取上次登录成功后的历史数据

        initView();

        if (mIsAuto) {
            login();
        }
    }

    private void getData() {

        mIsAuto = SharedPreferencesUtil.getBoolean(ViewsUitls.getContext(), StringsFiled.IS_AUTO_LOGIN, false);
        // mHistoryPassward = SharedPreferencesUtil.getString(
        // ViewsUitls.getContext(), "mPassWord", "");
        /*
         * 参数1:表名 参数2:要查询的字段 参数3:where表达式 参数4:替换?号的真实值 参数5:分组 null
		 * 参数6:having表达式null 参数7:排序规则 c_age desc
		 */
        Cursor cursor = writableDatabase.query("t_user",
                new String[]{"c_password"}, "c_pw>?", new String[]{"0"},
                null, null, null);
        while (cursor.moveToNext()) {
            mHistoryPassWord = cursor.getString(0);
        }
        cursor.close();

        mHistoryUser = SharedPreferencesUtil.getString(ViewsUitls.getContext(), StringsFiled.LOGIN_USER, "");

    }

    private void initView() {
        mLoginUser = (EditText) findViewById(R.id.login_user);
        mLoginPassWord = (EditText) findViewById(R.id.login_password);
        mLoginButton = (Button) findViewById(R.id.bt_login_button);
        mRememberPassWord = (CheckBox) findViewById(R.id.cb_login_remember_password);

        // 根据历史记录来设置显示
        if (!mHistoryUser.isEmpty() && !mHistoryPassWord.isEmpty()) {
            mLoginUser.setText(mHistoryUser);
            mLoginPassWord.setText(mHistoryPassWord);
        }

        if (mLoginUser != null && mLoginButton != null) {
            ViewTreeObserver viewTreeObserver = mLoginUser.getViewTreeObserver();
            viewTreeObserver.addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
                @Override
                public void onGlobalLayout() {
                    mLoginUser.getViewTreeObserver().removeGlobalOnLayoutListener(this);
                    ViewGroup.LayoutParams layoutParams = mLoginButton.getLayoutParams();
                    layoutParams.height = mLoginUser.getHeight();
                    mLoginButton.setLayoutParams(layoutParams);
                }
            });
        }


        mLoginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                login();
            }
        });

    }

    private void login() {
        mUser = mLoginUser.getText().toString().trim();
        mPassWord = mLoginPassWord.getText().toString().trim();
        if (!StringUtils.isEmpty(mUser) && !StringUtils.isEmpty(mPassWord)) {
            System.out.println("username:" + mUser + "password:" + mPassWord);
            requestIsLoginSuccess(mUser, mPassWord);
        } else {
            ToastUtil.showToast(this, "帐户密码不可为空");
        }
    }


    /*请求网络是否登录成功*/
    private void requestIsLoginSuccess(String userName, String passWord) {
        OkHttpClient okHttpClient = OkHttpManger.getInstance().getOkHttpClient();
        RequestBody formBody = new FormBody.Builder().add("name", userName)
                .add("password", passWord).build();
        Request request = new Request.Builder()
                .url(IpFiled.LOGIN)
                .post(formBody)
                .build();
        System.out.println(IpFiled.LOGIN);
        okHttpClient.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                System.out.println("=========================onFailure=============================");
                ViewsUitls.runInMainThread(new Runnable() {
                    @Override
                    public void run() {
                        ToastUtil.showToast(LoginActivity.this, "网络异常,请稍候");
                    }
                });
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                try {
                    loginResult = response.body().string();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                System.out.println(loginResult + "      +++++++++++++++++++++++++++++++++");
                ViewsUitls.runInMainThread(new Runnable() {
                    @Override
                    public void run() {
                        if (StringUtils.interentIsNormal(loginResult)) {
                            try {
                                JSONObject jsonObject = new JSONObject(loginResult);
                                if (jsonObject.optInt("status") == 1) {// 登录成功
                                    loginSuccee();
                                } else {
                                    ToastUtil.showToast(LoginActivity.this, "用户帐号或密码错误");
                                }
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        } else {
                            ToastUtil.showToast(LoginActivity.this, "服务器异常,请稍候");
                        }
                    }
                });
            }
        });


    }

    /* 登录网络请求成功后的处理方案 */
    private void loginSuccee() {
        ThreadManager.getInstance().execute(new TimerTask() {
            @Override
            public void run() {
                saveSuccessPassWardUserName();// 跳转前先把帐号密码进行异步存储
            }
        });

        Intent mainActivity = new Intent(ViewsUitls.getContext(), MainActivity.class);
        mainActivity.putExtra(BaseStringsFiled.ACTIVITY_TITLE, "主页面");
        startActivity(mainActivity);
        ToastUtil.showToast(LoginActivity.this, "登录成功");
        finish();
    }


    /*当登录成功后需要将帐号密码进行保存*/
    private void saveSuccessPassWardUserName() {
        if (StringUtils.isEmpty(mHistoryPassWord)) {// 当数据库中没有保存过密码时需要第一次插入密码数据
            ContentValues values = new ContentValues();
            values.put("c_password", mPassWord);
            values.put("c_pw", 1);
            writableDatabase.insert("t_user", null, values);
        } else {// 修改数据
            if (!mHistoryPassWord.equals(mPassWord)) {// EditText中的密码与历史密码不一样
                ContentValues values = new ContentValues();
                values.put("c_password", mPassWord);
                writableDatabase.update("t_user", values, "c_pw>?",
                        new String[]{"0"});
            }
        }
        writableDatabase.close();
        mySQLiteOpenHelper.close();

        if (!mHistoryUser.equals(mUser)) {// EditText中的帐号与历史帐号不一样
            SharedPreferencesUtil.saveString(ViewsUitls.getContext(),
                    StringsFiled.LOGIN_USER, mUser);
        }
        SharedPreferencesUtil.saveBoolean(ViewsUitls.getContext(), StringsFiled.IS_AUTO_LOGIN, mRememberPassWord.isChecked());
    }

}
