package com.bihe0832.hotfix;

import android.app.Activity;

import com.bihe0832.hotfix.impl.HotFixManager;

/**
 * Created by hardyshi on 2016/10/26.
 */
public class HotFixApi {
    public static void onCreate(Activity activity){
        HotFixManager.getInstance().onCreate(activity.getApplicationContext());
    }
}
