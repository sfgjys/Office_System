package com.minlu.baselibrary.util;

import android.app.ActivityManager;
import android.content.Context;
import android.net.wifi.WifiManager;
import android.os.Build;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;

import com.minlu.baselibrary.base.BaseApplication;

import java.util.List;

public class ViewsUitls {

    public static Context getContext() {
        return BaseApplication.getContext();
    }


    /**
     * dip转px
     */
    public static int dpToPx(int dip) {
        float density = getContext().getResources().getDisplayMetrics().density;
        // px = dip * density
        // 3.3 3.8 3
        // 3.6 4.1 4
        return (int) (dip * density + 0.5);
    }

    /**
     * xml 转成View对象
     *
     * @param id
     * @return
     */
    public static View inflate(int id) {
        return View.inflate(getContext(), id, null);
    }

    /**
     * 在主线程中执行任务 模仿runOut。。。。
     *
     * @param task
     */
    public static void runInMainThread(Runnable task) {
        if (BaseApplication.getMainThreadId() == android.os.Process.myTid()) {
            // 当前就是主线程，直接执行task
            task.run();
        } else {
            // 在子线程，post给主线程
            BaseApplication.getHanlder().post(task);
        }
    }

    /*
    * 当前线程是否在子线程
    * */
    public static boolean isAtMainThread() {
        return BaseApplication.getMainThreadId() == android.os.Process.myTid();
    }

    /**
     * 判断某个服务是否正在运行的方法
     *
     * @param serviceName 是包名+服务的类名（例如：net.loonggg.testbackstage.TestService）
     * @return true代表正在运行，false代表服务没有正在运行
     */
    public static boolean isServiceWork(String serviceName) {
        boolean isWork = false;
        ActivityManager systemService = (ActivityManager) getContext().getSystemService(Context.ACTIVITY_SERVICE);
        List<ActivityManager.RunningServiceInfo> runningServices = systemService.getRunningServices(40);
        if (runningServices.size() <= 0) {
            return false;
        }
        for (int i = 0; i < runningServices.size(); i++) {
            String setviceName = runningServices.get(i).service.getClassName();
            if (setviceName.equals(serviceName)) {
                isWork = true;
                break;
            }
        }
        return isWork;
    }


    /*
    * 返回true:  应用所在系统的Api Level大于等于参数传递过来的Api Level
    * 返回false: 应用所在系统的Api Level小于参数传递过来的Api Level
    * */
    public static boolean systemSdkVersionIsBigThanParameter(int possibleSystemSdkVersion) {
        // Build.VERSION_CODES.BASE; 这个是SDK中存储的各个版本对应的Api Level
        // Build.VERSION.SDK_INT 获取的是应用所在系统的Api Level
        return Build.VERSION.SDK_INT >= possibleSystemSdkVersion;
    }

    /**
     * 获取登录设备mac地址
     */
    public static String getMacAddress(Context context) {
        WifiManager wifiManager = (WifiManager) context.getSystemService(Context.WIFI_SERVICE);
        String mac = wifiManager.getConnectionInfo().getMacAddress();
        return mac == null ? "" : mac;
    }

    public static void setHeightFromTargetView(final View targetView, final View needChangeView) {
        if (targetView != null && needChangeView != null) {// 两个参数必须不为空
            ViewTreeObserver viewTreeObserver = targetView.getViewTreeObserver();// 获取目标View的观察者
            viewTreeObserver.addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {// 添加目标View的布局完成的监听
                @Override
                public void onGlobalLayout() {
                    targetView.getViewTreeObserver().removeGlobalOnLayoutListener(this); // 移除目标View的布局完成的监听
                    ViewGroup.LayoutParams layoutParams = needChangeView.getLayoutParams();// 获取需要改变的View的布局属性集
                    layoutParams.height = targetView.getHeight();// 将已经完成布局的目标View的高或者宽赋值给需要改变的View
                    needChangeView.setLayoutParams(layoutParams);// 最后设置需要改变的View的布局属性集
                }
            });
        }
    }

    public static void setWidthFromTargetView(final View targetView, final View needChangeView) {
        if (targetView != null && needChangeView != null) {// 两个参数必须不为空
            ViewTreeObserver viewTreeObserver = targetView.getViewTreeObserver();// 获取目标View的观察者
            viewTreeObserver.addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {// 添加目标View的布局完成的监听
                @Override
                public void onGlobalLayout() {
                    targetView.getViewTreeObserver().removeGlobalOnLayoutListener(this); // 移除目标View的布局完成的监听
                    ViewGroup.LayoutParams layoutParams = needChangeView.getLayoutParams();// 获取需要改变的View的布局属性集
                    layoutParams.width = targetView.getWidth();// 将已经完成布局的目标View的高或者宽赋值给需要改变的View
                    needChangeView.setLayoutParams(layoutParams);// 最后设置需要改变的View的布局属性集
                }
            });
        }
    }
}
