package com.minlu.office_system.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.view.KeyEvent;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.Toast;

import com.ashokvarma.bottomnavigation.BottomNavigationBar;
import com.ashokvarma.bottomnavigation.BottomNavigationItem;
import com.minlu.baselibrary.customview.MyLinearLayout;
import com.minlu.baselibrary.manager.ThreadManager;
import com.minlu.baselibrary.util.ViewsUitls;
import com.minlu.office_system.FragmentShowHide;
import com.minlu.office_system.R;
import com.minlu.office_system.fragment.HomePageFragment;
import com.minlu.office_system.fragment.MeFragment;
import com.minlu.office_system.fragment.SettingFragment;
import com.minlu.office_system.fragment.dialog.NoticeInformListDialog;

import java.util.ArrayList;
import java.util.List;
import java.util.TimerTask;

public class MainActivity extends FragmentActivity {

    private FragmentShowHide fragmentShowHide;
    private int showWhichFragment = 0;
    private MyLinearLayout myLinearLayout;
    private FrameLayout mLoadingUI;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        myLinearLayout = (MyLinearLayout) findViewById(R.id.custom_ll);
        mLoadingUI = (FrameLayout) findViewById(R.id.main_fl_loading);

        BottomNavigationBar bottomNavigationBar = (BottomNavigationBar) findViewById(R.id.bottom_navigation_button);
        bottomNavigationBar.clearAll();
        bottomNavigationBar.setMode(BottomNavigationBar.MODE_FIXED);
        bottomNavigationBar.setBackgroundStyle(BottomNavigationBar.BACKGROUND_STYLE_STATIC);
        bottomNavigationBar
                .addItem(new BottomNavigationItem(R.mipmap.main_home_page_icon, "首页").setActiveColorResource(R.color.main_bottom_red).setInActiveColorResource(R.color.main_bottom_gray))
                .addItem(new BottomNavigationItem(R.mipmap.main_me_icon, "我的").setActiveColorResource(R.color.main_bottom_red).setInActiveColorResource(R.color.main_bottom_gray))
                .addItem(new BottomNavigationItem(R.mipmap.main_setting_icon, "设置").setActiveColorResource(R.color.main_bottom_red).setInActiveColorResource(R.color.main_bottom_gray))
                .setFirstSelectedPosition(0)// 首次选择哪个
                .initialise();
        bottomNavigationBar.setTabSelectedListener(new BottomNavigationBar.OnTabSelectedListener() {
            @Override
            public void onTabSelected(int position) {// 按下Tab时会调用
                showWhichFragment = position;
                System.out.println("onTabSelected: " + position);
                if (fragmentShowHide != null) {
                    fragmentShowHide.showFragment(position, R.id.fl_replace_fragment, getSupportFragmentManager().beginTransaction());
                }
            }

            @Override
            public void onTabUnselected(int position) {// 原先被按下过的Tab不再是被按下的会调用
                System.out.println("onTabUnselected: " + position);
            }

            @Override
            public void onTabReselected(int position) {
                System.out.println("onTabReselected: " + position);
            }
        });

        List<String> tagList = new ArrayList<>();
        tagList.add("fragmentTag_homePage");
        tagList.add("fragmentTag_me");
        tagList.add("fragmentTag_setting");

        List<Fragment> fragmentList = new ArrayList<>();
        fragmentList.add(new HomePageFragment());
        fragmentList.add(new MeFragment());
        fragmentList.add(new SettingFragment());

        if (savedInstanceState == null) {
            System.out.println("第一次进入该界面，需要通过add来进行展示Fragment");
            showFragment(tagList, fragmentList, 0);
        } else {
            for (int i = 0; i < tagList.size(); i++) {
                Fragment fragmentByTag = getSupportFragmentManager().findFragmentByTag(tagList.get(i));
                if (fragmentByTag != null) {
                    fragmentList.remove(i);
                    fragmentList.add(i, fragmentByTag);
                }
            }
            System.out.println("第二次+++++++++++++++++++++++++++++");
            int saveWhichFragment = savedInstanceState.getInt("showWhichFragment", 0);
            bottomNavigationBar.selectTab(saveWhichFragment);
            showFragment(tagList, fragmentList, saveWhichFragment);
        }

        myLinearLayout.setIsInterruptTouch(true);
        mLoadingUI.setVisibility(View.VISIBLE);
        ThreadManager.getInstance().execute(new TimerTask() {
            @Override
            public void run() {
                showNoticeInform();
            }
        });
    }

    private void showNoticeInform() {
        // TODO 查询是否有公告,有的话就展示对话框并取消限制，没有就取消限制
        try {
            Thread.sleep(2000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        cancelConfine();


        ArrayList<String> objects = new ArrayList<>();
        objects.add("");
        objects.add("");
        objects.add("");
        objects.add("");
        objects.add("");
        objects.add("");
        objects.add("");
        NoticeInformListDialog noticeInformListDialog = new NoticeInformListDialog(objects);
        noticeInformListDialog.show(getSupportFragmentManager(), "NoticeInformListDialog");
    }

    /* 取消点击事件阻止和加载页面 */
    private void cancelConfine() {
        ViewsUitls.runInMainThread(new TimerTask() {
            @Override
            public void run() {
                myLinearLayout.setIsInterruptTouch(false);
                mLoadingUI.setVisibility(View.GONE);
            }
        });
    }

    private void showFragment(List<String> tagList, List<Fragment> fragmentList, int which) {
        fragmentShowHide = new FragmentShowHide(fragmentList, tagList);
        fragmentShowHide.showFragment(which, R.id.fl_replace_fragment, getSupportFragmentManager().beginTransaction());
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        outState.putInt("showWhichFragment", showWhichFragment);// 将选择哪个Tab进行存储
        super.onSaveInstanceState(outState);
    }

    private long firstTime = 0;

    @Override
    public boolean onKeyUp(int keyCode, KeyEvent event) {
        switch (keyCode) {
            case KeyEvent.KEYCODE_BACK:
                long secondTime = System.currentTimeMillis();
                if (secondTime - firstTime > 2000) {                                         //如果两次按键时间间隔大于2秒，则不退出
                    Toast.makeText(this, "再按一次退出程序", Toast.LENGTH_SHORT).show();
                    firstTime = secondTime;//更新firstTime
                    return true;
                } else {                                                    //两次按键小于2秒时，退出应用
                    Intent home = new Intent(Intent.ACTION_MAIN);
                    home.addCategory(Intent.CATEGORY_HOME);
                    startActivity(home);
                }
                break;
        }
        return super.onKeyUp(keyCode, event);
    }
}
