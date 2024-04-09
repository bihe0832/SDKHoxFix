package com.bihe0832.hotfix;

import android.app.Activity;
import android.content.Context;

import com.bihe0832.hotfix.impl.HotFixManager;

/**
 * Created by zixie on 2016/10/26.
 */
public class HotFixApi {
    public static void onCreate(Context context){
        HotFixManager.getInstance().onCreate(context);
    }
}
