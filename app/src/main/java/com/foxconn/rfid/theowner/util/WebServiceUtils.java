package com.foxconn.rfid.theowner.util;

import android.content.Context;
import android.util.Log;
import android.widget.Toast;

import com.androidquery.AQuery;
import com.androidquery.callback.AjaxCallback;
import com.androidquery.callback.AjaxStatus;
import com.foxconn.rfid.theowner.R;
import com.foxconn.rfid.theowner.base.App;
import com.foxconn.rfid.theowner.util.logort.LogUtils;

import java.util.HashMap;
import java.util.Map;



public class WebServiceUtils {

	private static final String TAG = WebServiceUtils.class.getName();

	public static abstract class WebServiceCallBack {

		public abstract void success(String url, String callbackStr,
				AjaxStatus status);

		public void failure(Context context, String callbackStr,
				AjaxStatus status) {
			Log.e(TAG, "status : " + status.getCode());
			Log.e(TAG, "callbackStr : " + callbackStr);
			Toast.makeText(context, R.string.toast_msg_call_webservice_fail,
					Toast.LENGTH_SHORT).show();
		}
	}

	public static interface CallbackListener {

		public void complete(Object callbackData);

		public void failure(int code, String failMsg);
	}

	public static void callWebService(final Context context, String APIName,
			String paramJson, final WebServiceCallBack webServiceCallBack) {
		callWebService(context, true, APIName, paramJson, webServiceCallBack);
	}

	public static void callWebService(final Context context, String url,
			String APIName, String paramJson,
			final WebServiceCallBack webServiceCallBack) {
		callWebService(context, true, url, APIName, paramJson,
				webServiceCallBack);
	}

	// public static void callWebService(final Context context, boolean
	// refreshFlag, String APIName, String paramJson, final WebServiceCallBack
	// webServiceCallBack) {
	// callWebService(context, refreshFlag, App.WebService.WS_URL, APIName,
	// paramJson, webServiceCallBack);
	// }

	public static void callWebService(final Context context,
			boolean refreshFlag, String url, String APIName, String paramJson,
			final WebServiceCallBack webServiceCallBack) {
		LogUtils.logMessage(TAG, "APIName:" + APIName + "paramJson ："
				+ paramJson);
		AQuery aq = new AQuery(context);
		if (paramJson != null && !paramJson.isEmpty()) {
			paramJson = buildjsonParam(paramJson);
		}

		LogUtils.logMessage(TAG, "encryptJson ：" + paramJson);
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("func", APIName);
		params.put("json", paramJson);
		if (refreshFlag) {
			aq = aq.progress(DialogUtils.createDialog(context,
					DialogUtils.REFRESH));
		}
		aq.ajax(url, params, String.class, new AjaxCallback<String>() {
			@Override
			public void callback(String url, String callbackStr,
					AjaxStatus status) {

				Log.i("esop", status.getCode() + "");
				if (status.getCode() == 200 && callbackStr != null
						&& !"".equals(callbackStr)) {
					LogUtils.logMessage(TAG, "callbackStr : " + callbackStr);
					webServiceCallBack.success(url, callbackStr, status);
				} else {
					webServiceCallBack.failure(context, callbackStr, status);
				}
			}
		});
	}

	public static void callWebService(final Context context, String url,
			final WebServiceCallBack webServiceCallBack) {
		callWebService(context, true, url, webServiceCallBack);
	}

	public static void callWebService(final Context context,
			boolean refreshFlag, String url,
			final WebServiceCallBack webServiceCallBack) {
		AQuery aq = new AQuery(context);

		LogUtils.logMessage(TAG, "url ：" + url);
		if (refreshFlag) {
			aq = aq.progress(DialogUtils.createDialog(context,
					DialogUtils.REFRESH));
		}
		aq.ajax(url, String.class, new AjaxCallback<String>() {
			@Override
			public void callback(String url, String callbackStr,
					AjaxStatus status) {
				if (status.getCode() == 200 && callbackStr != null
						&& !"".equals(callbackStr)) {
					LogUtils.logMessage(TAG, "callbackStr : " + callbackStr);
					webServiceCallBack.success(url, callbackStr, status);
				} else {
					webServiceCallBack.failure(context, callbackStr, status);
				}
			}
		});
	}

	public static void callWebService(final Context context,
			boolean refreshFlag, String url, String APIName,
			final WebServiceCallBack webServiceCallBack) {
		AQuery aq = new AQuery(context);

		LogUtils.logMessage(TAG, "url ：" + url);
		if (refreshFlag) {
			aq = aq.progress(DialogUtils.createDialog(context,
					DialogUtils.REFRESH));
		}
		aq.ajax(url, String.class, new AjaxCallback<String>() {
			@Override
			public void callback(String url, String callbackStr,
					AjaxStatus status) {
				if (status.getCode() == 200 && callbackStr != null
						&& !"".equals(callbackStr)) {
					LogUtils.logMessage(TAG, "callbackStr : " + callbackStr);
					webServiceCallBack.success(url, callbackStr, status);
				} else {
					webServiceCallBack.failure(context, callbackStr, status);
				}
			}
		});
	}

	public static void callWebService(final Context context,
			Map<String, ?> params, final WebServiceCallBack webServiceCallBack) {
		callWebService(context, true, App.WebService.WEBSERVICE_IP, params,
				webServiceCallBack);
	}

	public static void callWebService(final Context context, String url,
			Map<String, ?> params, final WebServiceCallBack webServiceCallBack) {
		callWebService(context, true, url, params, webServiceCallBack);
	}

	public static void callWebService(final Context context,
			boolean refreshFlag, Map<String, ?> params,
			final WebServiceCallBack webServiceCallBack) {
		callWebService(context, refreshFlag, App.WebService.WEBSERVICE_IP, params,
				webServiceCallBack);
	}

	public static void callWebService(final Context context,
			boolean refreshFlag, String url, Map<String, ?> params,
			final WebServiceCallBack webServiceCallBack) {
		AQuery aq = new AQuery(context);
		LogUtils.logMessage(TAG, "params ：" + params.toString());
		LogUtils.logMessage(TAG, "url ：" + url);
		if (refreshFlag) {
			aq = aq.progress(DialogUtils.createDialog(context,
					DialogUtils.REFRESH));
		}
		aq.ajax(url, params, String.class, new AjaxCallback<String>() {
			@Override
			public void callback(String url, String callbackStr,
					AjaxStatus status) {
				if (status.getCode() == 200 && callbackStr != null
						&& !"".equals(callbackStr)) {
					LogUtils.logMessage(TAG, "callbackStr : " + callbackStr);
					webServiceCallBack.success(url, callbackStr, status);
				} else {
					webServiceCallBack.failure(context, callbackStr, status);
				}
			}
		});
	}

	private static String buildjsonParam(String json) {
		return json;
	}

}