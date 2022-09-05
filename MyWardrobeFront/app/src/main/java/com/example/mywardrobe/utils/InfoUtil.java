package com.example.mywardrobe.utils;

public class InfoUtil {
    /**
     * TODO: 完成用户信息的存储
     */

    static String username;
    public static void login(String mUsername, String mPassword){
         username = mUsername;
    }

    public static String getUsername() {
        return username;
    }
}
