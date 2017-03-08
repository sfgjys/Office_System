package com.minlu.office_system.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.view.KeyEvent;
import android.widget.Toast;

import com.ashokvarma.bottomnavigation.BottomNavigationBar;
import com.ashokvarma.bottomnavigation.BottomNavigationItem;
import com.minlu.office_system.FragmentShowHide;
import com.minlu.office_system.R;
import com.minlu.office_system.fragment.HomePageFragment;
import com.minlu.office_system.fragment.MeFragment;
import com.minlu.office_system.fragment.SettingFragment;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends FragmentActivity {

    private FragmentShowHide fragmentShowHide;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        BottomNavigationBar bottomNavigationBar = (BottomNavigationBar) findViewById(R.id.bottom_navigation_button);
        bottomNavigationBar.clearAll();
        bottomNavigationBar.setMode(BottomNavigationBar.MODE_FIXED);
        bottomNavigationBar.setBackgroundStyle(BottomNavigationBar.BACKGROUND_STYLE_STATIC);
        bottomNavigationBar
                .addItem(new BottomNavigationItem(R.mipmap.main_home_page_icon, "首页").setActiveColorResource(R.color.main_bottom_red).setInActiveColorResource(R.color.main_bottom_gray))
                .addItem(new BottomNavigationItem(R.mipmap.main_me_icon, "我的").setActiveColorResource(R.color.main_bottom_red).setInActiveColorResource(R.color.main_bottom_gray))
                .addItem(new BottomNavigationItem(R.mipmap.main_setting_icon, "设置").setActiveColorResource(R.color.main_bottom_red).setInActiveColorResource(R.color.main_bottom_gray))
                .setFirstSelectedPosition(0)
                .initialise();
        bottomNavigationBar.setTabSelectedListener(new BottomNavigationBar.OnTabSelectedListener() {
            @Override
            public void onTabSelected(int position) {// 按下Tab时会调用
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
            showFragment(tagList, fragmentList);
        } else {
            for (int i = 0; i < tagList.size(); i++) {
                Fragment fragmentByTag = getSupportFragmentManager().findFragmentByTag(tagList.get(i));
                if (fragmentByTag != null) {
                    fragmentList.remove(i);
                    fragmentList.add(i, fragmentByTag);
                }
            }
            System.out.println("第二次+++++++++++++++++++++++++++++");
            showFragment(tagList, fragmentList);
        }


    }

    private void showFragment(List<String> tagList, List<Fragment> fragmentList) {
        fragmentShowHide = new FragmentShowHide(fragmentList, tagList);
        fragmentShowHide.showFragment(0, R.id.fl_replace_fragment, getSupportFragmentManager().beginTransaction());
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
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
