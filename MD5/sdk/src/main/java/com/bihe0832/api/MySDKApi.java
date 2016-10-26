package com.bihe0832.api;

import android.app.Activity;

import java.lang.reflect.Method;


/**
 * SDK 对外接口
 */
public class MySDKApi {

    public static void onCreate(Activity activity) {
        try {
            Class<?> hotfixClass = Class.forName("com.bihe0832.hotfix.HotFixApi");
            Method method1 = hotfixClass.getMethod("onCreate", Activity.class);
            method1.invoke(null, activity);
            Class<?> threadClazz = Class.forName("com.bihe0832.api.MySDKInnerApi");
            Method method = threadClazz.getMethod("onCreate", Activity.class);
            method.invoke(null, activity);
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    public static String getUpperMD5(String encryptKey){
        try {
            Class<?> threadClazz = Class.forName("com.bihe0832.api.MySDKInnerApi");
            Method method = threadClazz.getMethod("getUpperMD5",String.class);
            return (String) method.invoke(null,encryptKey);
        }catch (Exception e){
            e.printStackTrace();
            return "";
        }
    }

    public static String getLowerMD5(String encryptKey){
        try {
            Class<?> threadClazz = Class.forName("com.bihe0832.api.MySDKInnerApi");
            Method method = threadClazz.getMethod("getLowerMD5",String.class);
            return (String) method.invoke(null,encryptKey);
        }catch (Exception e){
            e.printStackTrace();
            return "";
        }
    }

    public static void startUpdate(){
        try {
            Class<?> threadClazz = Class.forName("com.bihe0832.api.MySDKInnerApi");
            Method method = threadClazz.getMethod("startUpdate");
            method.invoke(null);
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    public static void testHotfix(){
        try {
            Class<?> threadClazz = Class.forName("com.bihe0832.api.MySDKInnerApi");
            Method method = threadClazz.getMethod("testHotfix");
            method.invoke(null);
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    public static void clearUpdate(){
        try {
            Class<?> threadClazz = Class.forName("com.bihe0832.api.MySDKInnerApi");
            Method method = threadClazz.getMethod("clearUpdate");
            method.invoke(null);
        }catch (Exception e){
            e.printStackTrace();
        }
    }
}
