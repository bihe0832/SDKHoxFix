package com.bihe0832.hotfix.consts;

/**
 * Created by hardyshi on 16/4/27.
 */
public class HotFixConsts {

    public static final String LOG_TAG = "bihe0832 Hotfix";


    //SDK原始的版本号,不能通过热更更新
    public static final String VERSION_NAME = "1.0.0";
    //SDK原始版本code,不能通过热更更新
    public static final int VERSION_CODE = 1;

    //SDK热更加载的初始dex的文件名
    public static final String DEFAULT_DEX_JAR = "bihe0832_hackdex.jar";
    //SDK插桩使用的类的类名
    public static final String DEFAULT_MULTI_DEX_CLASS = "com.bihe0832.hotfix.Fix";
    //SDK补丁包必定会包含的类名
    public static final String DEFAULT_HTOFIX_TEST_DEX_CLASS = "com.bihe0832.hotfix.FixInfo";

    public static final String PATCH_DEX_JAR = "bihe0832_patch_dex.jar";
    public static final String PATCH_SO = "bihe0832MD5";
    public static final String PATCH_DOWNLOAD_URL = "http://microdemo.sinaapp.com/hotfix";
}
