package com.sypm.shuyuzhongbao.utils;


import com.tumblr.remember.Remember;


public class RememberHelper {

    public static void saveUserInfo(String number, String password) {
        Remember.putString("number", number);
        Remember.putString("password", password);
    }

    public static String getPassword() {
        return Remember.getString("password", "");
    }


    public static String getNumber() {
        return Remember.getString("number", "");
    }

}
