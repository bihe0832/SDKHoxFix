package com.bihe0832.hotfix.impl;

import android.content.Context;
import android.util.Log;
import android.widget.Toast;

import com.bihe0832.hotfix.FixInfo;
import com.bihe0832.hotfix.consts.HotFixConsts;

import java.io.File;

/**
 * Created by hardyshi on 16/3/21.
 * 热更内容加载
 */
public class HotFixManager {
    private Context mApplicationContext = null;
    private static volatile HotFixManager instance = null;
    public static HotFixManager getInstance() {
        if (instance == null) {
            synchronized (HotFixManager.class) {
                if (instance == null) {
                    instance = new HotFixManager();
                }
            }
        }
        return instance;
    }

    private HotFixManager() {
        Log.d(HotFixConsts.LOG_TAG,"version_name:" + HotFixConsts.VERSION_NAME);
        Log.d(HotFixConsts.LOG_TAG,"version_code:" + HotFixConsts.VERSION_CODE);
    }

    public void onCreate(Context applicationContext) {
        Log.d(HotFixConsts.LOG_TAG,"onCreate");
        mApplicationContext = applicationContext;
        File dexPath = new File(mApplicationContext.getDir("dex", Context.MODE_PRIVATE), HotFixConsts.DEFAULT_DEX_JAR);
        Utils.prepareDex(mApplicationContext, dexPath, HotFixConsts.DEFAULT_DEX_JAR);
        HotFix.patch(mApplicationContext, dexPath.getAbsolutePath(), HotFixConsts.DEFAULT_MULTI_DEX_CLASS);
        try {
            if(checkSoInFileExist()){
                loadFilesSo();
            }else{
                loadLibsSo();
            }

            if(checkJarInFileExist()){
                loadClassFromJar();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    private boolean checkJarInFileExist(){
        Log.d(HotFixConsts.LOG_TAG, "checkJarInFileExist");
        File jarFile = new File(mApplicationContext.getFilesDir().getAbsolutePath()+ "/" + HotFixConsts.PATCH_DEX_JAR);
        if(jarFile.exists()){
            Log.d(HotFixConsts.LOG_TAG, "File exists:"+ jarFile.getAbsolutePath().toString());
            Toast.makeText(mApplicationContext, "File exists", Toast.LENGTH_LONG).show();
            return true;
        }else{
            Log.d(HotFixConsts.LOG_TAG, "File not found");
            return false;
        }
    }

    private void loadClassFromJar(){
        Log.d(HotFixConsts.LOG_TAG, "loadClassFromJar");
        File dexPath =  new File(mApplicationContext.getFilesDir().getAbsolutePath()+ "/" + HotFixConsts.PATCH_DEX_JAR);
        HotFix.patch(mApplicationContext, dexPath.getAbsolutePath(), HotFixConsts.DEFAULT_HTOFIX_TEST_DEX_CLASS);
    }


    private boolean checkSoInFileExist(){
        Log.d(HotFixConsts.LOG_TAG, "checkFileExist");
        File soFile = new File(mApplicationContext.getFilesDir().getAbsolutePath()+"/lib" + HotFixConsts.PATCH_SO +".so");
        if(soFile.exists()){
            Log.d(HotFixConsts.LOG_TAG, "File exists:"+ soFile.getAbsolutePath().toString());
            Toast.makeText(mApplicationContext, "File exists", Toast.LENGTH_LONG).show();
            return true;
        }else{
            Log.d(HotFixConsts.LOG_TAG, "File not found");
            return false;
        }
    }

    private void loadLibsSo(){
        Log.d(HotFixConsts.LOG_TAG,"loadLibsSo" );
        System.loadLibrary(HotFixConsts.PATCH_SO);
    }

    private void loadFilesSo(){
        Log.d(HotFixConsts.LOG_TAG, "loadFilesSo");
        File soFile = new File(mApplicationContext.getFilesDir().getAbsolutePath()+"/lib" + HotFixConsts.PATCH_SO +".so");
        if(soFile.exists()){
            try {
                System.load(soFile.getAbsolutePath());
            }catch (UnsatisfiedLinkError error){
                //TODO 下发SO错误，上报日志
                //删除错误的so
                soFile.delete();
                //尝试加载默认so
                loadLibsSo();
                error.printStackTrace();
            }catch (Exception e){
                e.printStackTrace();
            }

        }else{
            Log.d(HotFixConsts.LOG_TAG, "File not found");
        }
    }

    public void startUpdate(){
        Log.d(HotFixConsts.LOG_TAG, "startUpdate start");
        downloadSo();
        downloadJar();
        Toast.makeText(mApplicationContext, "SDK更新已下载，应用重启后生效", Toast.LENGTH_LONG).show();
        Log.d(HotFixConsts.LOG_TAG, "startUpdate end");
    }

    private void downloadSo(){
        Log.d(HotFixConsts.LOG_TAG, "downloadSo");
        File soFile = new File(mApplicationContext.getFilesDir().getAbsolutePath()+"/lib" + HotFixConsts.PATCH_SO +".so");
        FileUtils.URLToFile(HotFixConsts.PATCH_DOWNLOAD_URL +"/lib" + HotFixConsts.PATCH_SO +".so"+ "?"+System.currentTimeMillis(), soFile);
        Log.d(HotFixConsts.LOG_TAG, "So 下载完成");
    }

    private void clearSoInFiles(){
        Log.d(HotFixConsts.LOG_TAG, "clearSoInFiles");
        File soFile = new File(mApplicationContext.getFilesDir().getAbsolutePath()+"/lib" + HotFixConsts.PATCH_SO +".so");
        soFile.delete();
        if(checkSoInFileExist()){
            Log.d(HotFixConsts.LOG_TAG, "so删除失败");
        }else{
            Log.d(HotFixConsts.LOG_TAG, "so已删除");
        }
    }



    private void downloadJar(){
        Log.d(HotFixConsts.LOG_TAG, "downloadJar");
        File jarFile = new File(mApplicationContext.getFilesDir().getAbsolutePath()+ "/" + HotFixConsts.PATCH_DEX_JAR);
        FileUtils.URLToFile( HotFixConsts.PATCH_DOWNLOAD_URL + "/" + HotFixConsts.PATCH_DEX_JAR + "?"+System.currentTimeMillis(), jarFile);
        Log.d(HotFixConsts.LOG_TAG, "jar下载完成");
    }

    private void clearJarInFiles(){
        Log.d(HotFixConsts.LOG_TAG, "clearJarInFiles");
        File jarFile = new File(mApplicationContext.getFilesDir().getAbsolutePath()+ "/" + HotFixConsts.PATCH_DEX_JAR);
        jarFile.delete();
        if(checkJarInFileExist()){
            Log.d(HotFixConsts.LOG_TAG, "Jar删除失败");
        }else{
            Log.d(HotFixConsts.LOG_TAG, "Jar已删除");
        }

    }

    public void testHotfix(){
        FixInfo a = new FixInfo();
        Log.d(HotFixConsts.LOG_TAG,a.bug());
    }

    public void clearUpdate(){
        if(checkSoInFileExist()){
            clearSoInFiles();
        }

        if(checkJarInFileExist()){
           clearJarInFiles();
        }
    }
}
