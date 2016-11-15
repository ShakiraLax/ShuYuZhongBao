package com.sypm.shuyuzhongbao.utils;

import android.content.Context;
import android.widget.Toast;

/**
 * Created by Administrator on 2016/11/14.
 */

public class ToastUtils {
    private static Context sContext;

    private ToastUtils() {
    }

    public static void init(Context context) {
        sContext = context.getApplicationContext();
    }

    private static void check() {
        if (sContext == null) {
            throw new NullPointerException(
                    "Must initial call ToastUtils.init(Context context) in your " +
                            "<? " +
                            "extends Application class>");
        }
    }

    public static void show(String error) {
        check();
        Toast.makeText(sContext, error, Toast.LENGTH_SHORT).show();
    }
}
