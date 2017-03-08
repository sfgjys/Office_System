package com.minlu.office_system;

import android.support.v4.app.Fragment;

import com.minlu.office_system.fragment.HomePageFragment;
import com.minlu.office_system.fragment.MeFragment;
import com.minlu.office_system.fragment.SettingFragment;


public class FragmentFactory {

    /*存储工长要造的对象的仓库*/
    public static Fragment[] fragments = new Fragment[3];

    public static Fragment create(int position) {
        Fragment fragment = null;

        /*仓库里对应位置的对象为空才需要重新创造*/
        if (fragments[position] == null) {

            switch (position) {
                case 0:
                    fragment = new HomePageFragment();
                    break;
                case 1:
                    fragment = new MeFragment();
                    break;
                case 2:
                    fragment = new SettingFragment();
                    break;
            }

            fragments[position] = fragment;

            return fragment;
        } else {
            System.out.println("已经存在");
            return fragments[position];
        }
    }

}

