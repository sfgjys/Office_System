package com.minlu.office_system;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;

import java.io.Serializable;
import java.util.List;

public class FragmentShowHide implements Serializable {

    private final List<String> tagList;
    private final List<Fragment> fragmentList;

    public FragmentShowHide(List<Fragment> fragmentList, List<String> tagList) {
        this.fragmentList = fragmentList;
        this.tagList = tagList;
    }

    public void showFragment(int position, int containerViewId, FragmentTransaction fragmentTransaction) {
        for (int i = 0; i < fragmentList.size(); i++) {

            Fragment fragment = fragmentList.get(i);

            // 从集合中获取的Fragment取一个赋值给其中一个Fragment局部变量,另一个为null
            if (position == i) {
                if (fragment.isAdded()) {
                    fragmentTransaction.show(fragment);
                } else {
                    fragmentTransaction.add(containerViewId, fragment, tagList.get(position));
                }
            } else {
                // 如果fragment已经被add过,才能被隐藏,否则还未空
                if (fragment.isAdded()) {
                    fragmentTransaction.hide(fragment);
                }
            }
        }
        fragmentTransaction.commit();
    }
}
