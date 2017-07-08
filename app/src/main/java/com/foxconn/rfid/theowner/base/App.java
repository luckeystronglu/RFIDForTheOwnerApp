package com.foxconn.rfid.theowner.base;

import android.app.Activity;
import android.content.Context;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

/**
 * App
 * 
 * @author Tom
 * @date 2014/11/24 11:40
 * 
 */
public class App {

	public final static String APK_DOWNLOAD_ADDRESS=""; // apk版本更新url地址
	//"http://112.74.23.119:8080/app/esmoking/eSmoking.apk",




	public static final String SERVER_IP = "112.74.23.119"; //正式环境
//	public static final String SERVER_IP = "172.16.5.237"; //许
	public static final int SERVER_PORT = 7891;
//	public static final int SERVER_PORT = 7892;

	public static final String SMS_APPKEY = "18c18fa43c278";
	public static final String SMS_APPSECRET = "111cbfc55936459af945a9cdeefb3446";
	public static final String DB_NAME = "rfidbike.db";
	public static final int DB_VERSION = 19;
	public static final int WAITTING_SECOND=20*1000;
	public static final String SELECTED_TAB="SELECTED_TAB";
	public static final String SHOPPING_WEB_SITE="http://www.baidu.com";
	public static final String EVENT_REFRESH_LANGUAGE="EVENT_REFRESH_LANGUAGE";
	
	

	
	/** 甯搁噺 **/
	public static final String AQUERY_CACHE_PATH = File.separator
			+ "com.yzh.rfidbike" + File.separator;
	public static final String IMAGE_CACHE_PATH = AQUERY_CACHE_PATH
			+ "imagecache" + File.separator;
	public static final String CRASH_CACHE_PATH = AQUERY_CACHE_PATH
			+ "crashcache" + File.separator;
	public static final String SOCKET_NOTE_PATH = AQUERY_CACHE_PATH
			+ "socketnote" + File.separator;

	public static final String SEPARATOR_CHAR = "#@#";
	public static final String DECIMAL_FORMAT_STRING = "#####0.00";


	public static final String SYSTEM_ID = "rfidbike";

	public final static String ALLOWED_URI_CHARS = "@#&=*+-_.,:!?()/~'%";

	public static final String SYSTEM_TYPE = "aPhone";
	public static final String SYSTEM_UPDATEDATE_DEFAULT = "2014-01-01 00:00:00";

	public static final String PHONE_NUMBER_START = "+86";

	public static final int PAGE_SIZE = 8;

	public static class IntentKey {
		public static final String FOR_RESULT_FLAG = "for_result_flag";
		public static final String ACCOUNT = "account";
		public static final String PWD = "pwd";
		public static final String REGIEST_FLAG = "regiest_flag";

		public static final String SURVEY_ITEM_ID = "survey_item_id";
		public static final String ADDRESS_ITEM = "address_item";
		public static final String SELECTED_IMAGE_URI = "selected_image_uri";
		public static final String CLIP_IMAGE_DATA = "clip_image_data";

	}

	public static class BDOpenPlatform {
		public static final String MAP_KEY = "31C1A78A0EC8D634729CB90546913764414D7ABF";
	}
	//	百度推送 api KEY
	public static final String BAIDU_PUSH_API_KEY = "WICmfwpSL1OyDcgmRjAq5REgz933DOFf";
//	public static final String BAIDU_PUSH_API_KEY = "pQxHFh4cisdL0CBnLjzPAdhK"; //luckey
//	APP ID 9128823
//	API KEY bCpdGCNeBezxE9nZAq6nIfGs
//	SECRET KEY 68zPFXCO4GkOyvKk9jTrO1HSGD3mR50t
	public static class HandlerKey {
		public static final int HANDLER_MSG_TOTAL_CHANGE = 101;
		public static final int HANDLER_MSG_TIME_CHANGE = 102;
		public static final int HANDLER_MSG_CAN_CLICK = 103;
		public static final int HANDLER_MSG_CAN_NOT_CLICK = 104;
	}

	public static final String BIND_TYPE_PHONE = "phone",
			BIND_TYPE_MAIL = "mail";

	public static final String WEB_URL = "http://%1$s:%2$d";

	// WebService method
	public static class WebService {
		public static final String MSGHEAD = "msgHead";
		public static final String CODE = "code";
		public static final String MSG = "msg";
		public static final String BODY = "body";

		public static final String ERROR = "error";
		public static final String SIGN = "sign";
		// socket杩炴帴瓒呮椂鏃堕棿
		public static final int SocketConnectTimeout = 6000;
		// socket鎺ユ敹瓒呮椂鏃堕棿
		public static final int SocketReceiveTimeout = 6000;

		/**webservice **/
		public static final int WEBSERVICE_PORT = 8080;// 8080
		public static final String WEBSERVICE_IP ="127.0.0.1";// 8080
		public static final int CONNECTION_WEB_PORT = 8080;// 8080



	}





//	public static String getDetailUrl(Context context) {
//		return getWebServerUrl(context) + "/byq/admin";
//	}

	public static class Contants {
		public static final String MSGKEY = "message";// 娑堟伅鐨刱ey
		public static final String BACKKEY_ACTION = "com.foxconn.positionplatform.backKey";// 杩斿洖閿彂閫佸箍鎾殑action
		public static final int NOTIFY_ID = 0x911;// 閫氱煡ID
		public static final String ACTION = "com.foxconn.positionplatform.message";// 娑堟伅骞挎挱action

		public static final String ACTION_GET_ALL_DEVICE_INFO = "com.foxconn.positionplatform.getalldeviceinfo";// 娑堟伅骞挎挱action
		public static final String BROADCAST_GET_ALL_DEVICE_INFO = "getalldeviceinfo";// 娑堟伅鐨刱ey

		public static final String ACTION_GET_REAL_TIME_TRACK = "com.foxconn.positionplatform.getrealtimetrack";// 娑堟伅骞挎挱action
		public static final String BROADCAST_GET_REAL_TIME_TRACK = "getrealtimetrack";// 娑堟伅鐨刱ey

	}

	public static class DownloadService {
		public static final String ACTION_DOWNLOAD_PROGRESS = "my_download_progress";
		public static final String ACTION_DOWNLOAD_SUCCESS = "my_download_success";
		public static final String ACTION_DOWNLOAD_FAIL = "my_download_fail";
	}

	// 娣诲姞 activity
	public static List<Activity> activityList = new ArrayList<Activity>();

	public static void addActivity(Activity activity) {
		App.activityList.add(activity);
	}

	public static void removeActivity(Activity activity)
	{
		App.activityList.remove(activity);
	}
	// 閫�鍑� activity
	public static void exitSystem(Context context) {
		// finish all activity
		for (int i = 0; i < App.activityList.size(); i++) {
			if (null != App.activityList.get(i)) {
				App.activityList.get(i).finish();
			}
		}

	}
}
