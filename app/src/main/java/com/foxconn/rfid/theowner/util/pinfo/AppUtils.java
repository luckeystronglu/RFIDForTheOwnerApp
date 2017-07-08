package com.foxconn.rfid.theowner.util.pinfo;

import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;

public class AppUtils {

	public static int getVersionCode(Context context){
		// 获取packagemanager的实例
		PackageManager packageManager = context.getPackageManager();
		// getPackageName()是你当前类的包名，0代表是获取版本信息
		try {
			PackageInfo packInfo = packageManager.getPackageInfo(context.getPackageName(), 0);
			int version = packInfo.versionCode;
			return version;
		} catch (NameNotFoundException e) {
			e.printStackTrace();
			return 1;
		}
	}
	public static String getVersionName(Context context){
		// 获取packagemanager的实例
		PackageManager packageManager = context.getPackageManager();
		// getPackageName()是你当前类的包名，0代表是获取版本信息
		try {
			PackageInfo packInfo = packageManager.getPackageInfo(context.getPackageName(), 0);
			String version = packInfo.versionName;
			return version;
		} catch (NameNotFoundException e) {
			e.printStackTrace();
			return "1.0";
		}
	}

}
