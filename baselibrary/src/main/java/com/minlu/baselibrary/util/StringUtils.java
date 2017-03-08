package com.minlu.baselibrary.util;

public class StringUtils {
    /**
     * 判断字符串是否有值，如果为null或者是空字符串或者只有空格或者为"null"字符串，则返回true，否则则返回false
     */
    public static boolean isEmpty(String value) {
        if (value != null && !"".equalsIgnoreCase(value.trim())
                && !"null".equalsIgnoreCase(value.trim())) {
            // 不为空
            return false;
        } else {
            // 为空
            return true;
        }
    }

    /**
     * 判断网络请求后的内容是否正常 false不正常，true正常
     */
    public static boolean interentIsNormal(String value) {
        if (isEmpty(value)) {
            return false;
        } else {
            if (value.contains("relogin")) {
                return false;
            } else if (value.contains("Error")) {
                return false;
            } else if (value.contains("html")) {
                return false;
            }
        }
        return true;
    }
}
