package com.bihe0832.md5;

import android.app.Activity;
import android.util.Log;

/**
 *
 * @author bihe0832
 *
 */
public class MD5 {

	private static final int VERSION = 1;

	private static volatile MD5 instance = null;
	public static MD5 getInstance() {
		if (instance == null) {
			synchronized (MD5.class) {
				if (instance == null) {
					instance = new MD5();
				}
			}
		}
		return instance;
	}

	private MD5() {
		Log.d(Consts.LOG_TAG,com.bihe0832.hotfix.Fix.class.getName());
	}

	public void onCreate(Activity activity) {
		Log.d(Consts.LOG_TAG,"MD5 version name:"+Consts.VERSION_NAME);
		Log.d(Consts.LOG_TAG,"MD5 version code:"+Consts.VERSION_CODE);
	}

	private native String getMD5Result(String encryptKey);

	public String getUpperMD5(String encryptKey){
		String result = getMD5Result(encryptKey);
		if(null != result && !result.equals("")){
			return result.toUpperCase();
		}else {
			return "";
		}
	}

	public String getLowerMD5(String encryptKey){
		String result = getMD5Result(encryptKey);
		if(null != result && !result.equals("")){
			return result.toUpperCase();
		}else {
			return "";
		}
	}
}
