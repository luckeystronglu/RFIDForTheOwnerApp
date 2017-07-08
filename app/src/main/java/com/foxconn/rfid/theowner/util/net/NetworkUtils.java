package com.foxconn.rfid.theowner.util.net;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.telephony.TelephonyManager;
import android.util.Log;

public class NetworkUtils {

	private static final String TAG = "NetworkUtils";

	// 获取当前的网络连接类型
	// -1：没有网络
	// 1：WIFI网络
	// 2：wap网络
	// 3：net网络
	public static int getNetworkType(Context context) {

		int netType = -1;

		ConnectivityManager connMgr = (ConnectivityManager) context
				.getSystemService(Context.CONNECTIVITY_SERVICE);

		if (connMgr == null) {

			Log.i(TAG, "Network is Unavailabel");
			return netType;
		}

		NetworkInfo networkInfo = connMgr.getActiveNetworkInfo();

		if (networkInfo == null) {

			Log.i(TAG, "Network is Unavailabel");
			return netType;
		}

		int nType = networkInfo.getType();
		switch (nType) {

		case ConnectivityManager.TYPE_MOBILE:
			String extraInfo = networkInfo.getExtraInfo();
			if (extraInfo != null && "cmnet".equals(extraInfo.toLowerCase())) {
				netType = 3;
			} else {
				netType = 2;
			}
			break;

		case ConnectivityManager.TYPE_WIFI:
			netType = 1;
			break;

		default:

			break;
		}

		Log.e(TAG, "ConnectedType is " + netType);

		return netType;

	}

	// 获取wifi连接的质量
	// 0到-50表示信号最好
	// -50到-70表示信号偏差
	// 小于-70表示最差
	public static int getWIFIQuality(Context context) {

		WifiManager wifi_service = (WifiManager) context
				.getSystemService(Context.WIFI_SERVICE);

		WifiInfo wifiInfo = wifi_service.getConnectionInfo();

		int wifiQuality = wifiInfo.getRssi();

		Log.i(TAG, "wifi quality is " + wifiQuality);

		return wifiQuality;
	}	

	@SuppressWarnings("unused")
	private static boolean isFastMobileNetwork(Context context) {
		TelephonyManager telephonyManager = (TelephonyManager) context
				.getSystemService(Context.TELEPHONY_SERVICE);
		switch (telephonyManager.getNetworkType()) {
		case TelephonyManager.NETWORK_TYPE_1xRTT:
			return false; // ~ 50-100 kbps
		case TelephonyManager.NETWORK_TYPE_CDMA:
			return false; // ~ 14-64 kbps
		case TelephonyManager.NETWORK_TYPE_EDGE:
			return false; // ~ 50-100 kbps
		case TelephonyManager.NETWORK_TYPE_EVDO_0:
			return true; // ~ 400-1000 kbps
		case TelephonyManager.NETWORK_TYPE_EVDO_A:
			return true; // ~ 600-1400 kbps
		case TelephonyManager.NETWORK_TYPE_GPRS:
			return false; // ~ 100 kbps
		case TelephonyManager.NETWORK_TYPE_HSDPA:
			return true; // ~ 2-14 Mbps
		case TelephonyManager.NETWORK_TYPE_HSPA:
			return true; // ~ 700-1700 kbps
		case TelephonyManager.NETWORK_TYPE_HSUPA:
			return true; // ~ 1-23 Mbps
		case TelephonyManager.NETWORK_TYPE_UMTS:
			return true; // ~ 400-7000 kbps
			// case TelephonyManager.NETWORK_TYPE_EHRPD:
			// return true; // ~ 1-2 Mbps
		case TelephonyManager.NETWORK_TYPE_EVDO_B:
			return true; // ~ 5 Mbps
			// case TelephonyManager.NETWORK_TYPE_HSPAP:
			// return true; // ~ 10-20 Mbps
		case TelephonyManager.NETWORK_TYPE_IDEN:
			return false; // ~25 kbps
			// case TelephonyManager.NETWORK_TYPE_LTE:
			// return true; // ~ 10+ Mbps
		case TelephonyManager.NETWORK_TYPE_UNKNOWN:
			return false;
		default:
			return false;
		}
	}
}
