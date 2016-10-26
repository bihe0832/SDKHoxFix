package com.bihe0832.api;

import android.app.Activity;
import android.util.Log;

import com.bihe0832.hotfix.impl.HotFixManager;
import com.bihe0832.md5.MD5;


/**
 * SDK 对外接口
 */
public class MySDKInnerApi {

    public static void onCreate(Activity activity) {
        Log.d("bihe0832 MySDKInnerApi","onCreate");
        MD5.getInstance().onCreate(activity);
    }

    public static String getUpperMD5(String encryptKey){
        Log.d("bihe0832 MySDKInnerApi","getUpperMD5");
        return MD5.getInstance().getUpperMD5(encryptKey);
    }

    public static String getLowerMD5(String encryptKey){
        Log.d("bihe0832 MySDKInnerApi","getLowerMD5");
        return MD5.getInstance().getLowerMD5(encryptKey);
    }

    public static void startUpdate(){
        Log.d("bihe0832 MySDKInnerApi","startUpdate");
        HotFixManager.getInstance().startUpdate();
    }

    public static void testHotfix(){
        Log.d("bihe0832 MySDKInnerApi","testHotfix");
        HotFixManager.getInstance().testHotfix();
    }

    public static void clearUpdate(){
        Log.d("bihe0832 MySDKInnerApi","clearUpdate");
        HotFixManager.getInstance().clearUpdate();
    }
}
