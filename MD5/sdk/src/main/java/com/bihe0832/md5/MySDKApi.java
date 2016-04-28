package com.bihe0832.md5;

import android.app.Activity;

import com.bihe0832.hotfix.impl.HotFixManager;


/**
 * SDK 对外接口
 */
public class MySDKApi {

    public static void onCreate(Activity activity) {
        HotFixManager.getInstance().onCreate(activity.getApplicationContext());
        MD5.getInstance().onCreate(activity);
    }

    public static String getUpperMD5(String encryptKey){
        return MD5.getInstance().getUpperMD5(encryptKey);
    }

    public static String getLowerMD5(String encryptKey){
        return MD5.getInstance().getLowerMD5(encryptKey);
    }

    public static void startUpdate(){
        HotFixManager.getInstance().startUpdate();
    }

    public static void testHotfix(){
        HotFixManager.getInstance().testHotfix();
    }

    public static void clearUpdate(){
        HotFixManager.getInstance().clearUpdate();
    }
}
