package com.sypm.shuyuzhongbao.utils;

import android.content.Context;

import com.franmontiel.persistentcookiejar.PersistentCookieJar;
import com.franmontiel.persistentcookiejar.cache.SetCookieCache;
import com.franmontiel.persistentcookiejar.persistence.SharedPrefsCookiePersistor;

import okhttp3.CookieJar;

/**
 * Created by Administrator on 2016/11/14.
 */

public class Injection {
    private static Context appContext;

    public static CookieJar provideCookieJar() {
        return new PersistentCookieJar(new SetCookieCache(), new SharedPrefsCookiePersistor(appContext));
    }

    public static void setApplicationContext(Context applicationContext) {
        appContext = applicationContext;
    }
}
