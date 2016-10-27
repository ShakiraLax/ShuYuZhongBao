package com.sypm.shuyuzhongbao.utils;


import com.tumblr.remember.Remember;


public class RememberHelper {

    public static void saveUserInfo(String uid, String password) {
        Remember.putString("uid", uid);
        Remember.putString("password", password);
    }

    public static String getPassword() {
        return Remember.getString("password", "");
    }


    public static String getUid() {
        return Remember.getString("uid", "");
    }


}
