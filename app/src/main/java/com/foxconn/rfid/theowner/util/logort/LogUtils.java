package com.foxconn.rfid.theowner.util.logort;

import android.util.Log;

/**
 * LogUtils
 * 
 * @author F1041566
 * @date 2014/11/24 11:40
 * 
 */
public class LogUtils {
	public static final boolean logFlag = true;
	public static final String TAG = "Mine";

	public static void logMessage(String tag, String log) {
		if (logFlag) {
			Log.i(tag, log);
		}
	}

	public static void logError(String tag, String log) {
		if (logFlag) {
			Log.e(tag, log);
		}
	}

	public static void logWarn(String tag, String log) {
		if (logFlag) {
			Log.w(tag, log);
		}
	}
}
