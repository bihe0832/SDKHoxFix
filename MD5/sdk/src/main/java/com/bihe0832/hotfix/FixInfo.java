package com.bihe0832.hotfix;

import android.util.Log;

import com.bihe0832.md5.Consts;

/**
 * Created by hardyshi on 16/3/21.
 * 测试热更是否OK
 */
public class FixInfo {
    //SDK 热更以后的版本号
    public static final String VERSION_NAME = "1.0.0";
    public static final int VERSION_CODE = 1;

    public FixInfo() {
        Log.d(Consts.LOG_TAG,com.bihe0832.hotfix.Fix.class.getName());
    }

    public String bug() {
        return "bug class";
    }
}
