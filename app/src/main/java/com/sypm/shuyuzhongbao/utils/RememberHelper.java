package com.sypm.shuyuzhongbao.utils;


import com.tumblr.remember.Remember;


public class RememberHelper {

    public static void saveUserInfo(String number, String password) {
        Remember.putString("number", number);
        Remember.putString("password", password);
    }

    public static void saveRegistrationId(String registrationId) {
        Remember.putString("registrationId", registrationId);
    }

    public static void saveUserPhone(String phone) {
        Remember.putString("phone", phone);
    }

    public static String getPassword() {
        return Remember.getString("password", "");
    }


    public static String getNumber() {
        return Remember.getString("number", "");
    }

    public static String getPhone() {
        return Remember.getString("phone", "");
    }

    public static String getRegistrationId() {
        return Remember.getString("registrationId", "");
    }

}
