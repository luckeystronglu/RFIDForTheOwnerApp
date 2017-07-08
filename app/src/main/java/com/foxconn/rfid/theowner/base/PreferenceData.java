package com.foxconn.rfid.theowner.base;

import android.content.Context;
import android.content.SharedPreferences;

import java.io.Serializable;

/**
 * PreferenceData
 * 
 * @author Tom
 * @date 2014/11/24 11:40
 * 
 */
public class PreferenceData implements Serializable {

	private static final long serialVersionUID = 1019621703138147697L;

	/** Preference Config **/
	public static final String SHARED_PREFERENCE = "SmokingConfig";
	public static final String SHARED_PREFERENCE_PUSH = "PushConfig";

	/** Preference Key **/
	// EncryptKey
	public static final String KEY_ENCRYPT_KEY = "encrypt_key";
	// 登陆相关
	public static final String KEY_SESSION_NO = "session_no";
	public static final String KEY_LOGIN_ACCOUNT = "login_account";
	public static final String KEY_OFFICE_ID = "office_id";
	public static final String KEY_OFFICE_NAME = "office_name";
	public static final String KEY_ACCOUNT_HEAD_URL = "key_account_head_url";
	public static final String KEY_LOGIN_INFO = "login_info";
	public static final String KEY_LOGIN_MEMBER_ID = "login_member_id";
	public static final String KEY_AUTO_LOGIN_FLAG = "auto_login_flag";
	public static final String KEY_LOGIN_PLANT_ID = "plant_id";
	public static final String KEY_ID = "id";

	public static final String KEY_MEMBER_INFO = "member_info";
	public static final String KEY_MENU_INFO = "menu_info";
	public static final String KEY_DEPARTMENT_INFO = "departments_info";
	// Push相关
	public static final String KEY_PUSH_STATUS_FLAG = "push_status_flag";
	public static final String KEY_PUSH_SOUND_FLAG = "push_sound_flag";
	public static final String KEY_PUSH_SHOCK_FLAG = "push_shock_flag";
	public static final String KEY_PUSH_FLASH_FLAG = "push_flash_flag";
	public static final String KEY_PUSH_BIND_USER = "push_bind_user";
	public static final String KEY_PUSH_BIND_ACCOUNT = "push_bind_account";
	// 百度Push账号
	public static final String KEY_PUSH_BD_REQ_ID = "bd_req_id";
	public static final String KEY_PUSH_BD_APP_ID = "bd_app_id";
	public static final String KEY_PUSH_BD_CHANNEL_ID = "bd_channel_id";
	public static final String KEY_PUSH_BD_USER_ID = "bd_user_id";

	public static final String KEY_APP_RUN_FIRST_FLAG = "app_run_first_flag";
	// Server setting
	public static final String KEY_SETTING_SERVER_INFO = "setting_server_info";

	public static final String KEY_REQUEST_DT_HOME = "request_last_date";
	public static final String KEY_RECYCLE_INTERVAL="recycle_interval";
	public static final String KEY_SEPARATE_NUM="separate_num";
	
	public static final String KEY_DEVICE_ADDRESS="device_address";
	
	public static final String KEY_WORK_MODEL="device_work_model";
	public static final String KEY_POWER_TEMP="device_power_temp";
	public static final String KEY_SMOKING_TIME="device_smoking_time";
	public static final String KEY_BATTERY="device_battery";
	public static final String KEY_RESISTANCE="device_resistance";
	public static final String KEY_User_Photo="KEY_User_Photo";
	
	public static final String KEY_Device_Name="KEY_Device_Name";
	
	public static final String KEY_APP_Language="KEY_APP_Language";
	public static final String KEY_Login_Name="KEY_Login_Name";

	public static void clearAllSharePreferences(Context context) {
		clearSharePreferences(context, SHARED_PREFERENCE);
		clearSharePreferences(context, SHARED_PREFERENCE_PUSH);
	}

	public static void clearSharePreferences(Context context, String spName) {
		SharedPreferences sharedPreferences = context.getApplicationContext()
				.getSharedPreferences(spName, Context.MODE_PRIVATE);
		SharedPreferences.Editor editor = sharedPreferences.edit().clear();
		editor.commit();
	}

	public static void removeSharePreference(Context context, String key) {
		removeSharePreference(context, SHARED_PREFERENCE, key);
	}

	public static void removeSharePreference(Context context, String spName,
			String key) {
		SharedPreferences sharedPreferences = context.getApplicationContext()
				.getSharedPreferences(spName, Context.MODE_PRIVATE);
		SharedPreferences.Editor editor = sharedPreferences.edit().remove(key);
		editor.commit();
	}
	/** recycle interval **/
	public static void saveRecycleInterval(Context context, int key) {
		SharedPreferences sharedPreferences = context.getApplicationContext()
				.getSharedPreferences(SHARED_PREFERENCE, Context.MODE_PRIVATE);
		SharedPreferences.Editor editor = sharedPreferences.edit();
		editor.putInt(KEY_RECYCLE_INTERVAL, key);
		editor.commit();
	}

	public static int loadRecycleInterval(Context context) {
		SharedPreferences sharedPreferences = context.getApplicationContext()
				.getSharedPreferences(SHARED_PREFERENCE, Context.MODE_PRIVATE);
		return sharedPreferences.getInt(KEY_RECYCLE_INTERVAL, 10);
	}
	/** recycle interval **/
	public static void saveAppLanguage(Context context, int key) {
		SharedPreferences sharedPreferences = context.getApplicationContext()
				.getSharedPreferences(SHARED_PREFERENCE, Context.MODE_PRIVATE);
		SharedPreferences.Editor editor = sharedPreferences.edit();
		editor.putInt(KEY_APP_Language, key);
		editor.commit();
	}

	public static int loadAppLanguage(Context context) {
		SharedPreferences sharedPreferences = context.getApplicationContext()
				.getSharedPreferences(SHARED_PREFERENCE, Context.MODE_PRIVATE);
		return sharedPreferences.getInt(KEY_APP_Language, 1);
	}
	/** separate number  **/
	public static void saveSeparateNum(Context context, int key) {
		SharedPreferences sharedPreferences = context.getApplicationContext()
				.getSharedPreferences(SHARED_PREFERENCE, Context.MODE_PRIVATE);
		SharedPreferences.Editor editor = sharedPreferences.edit();
		editor.putInt(KEY_SEPARATE_NUM, key);
		editor.commit();
	}

	public static int loadSeparateNum(Context context) {
		SharedPreferences sharedPreferences = context.getApplicationContext()
				.getSharedPreferences(SHARED_PREFERENCE, Context.MODE_PRIVATE);
		return sharedPreferences.getInt(KEY_SEPARATE_NUM, 1);
	}
	/** EncryptKey **/
	public static void saveEncryptKey(Context context, String key) {
		SharedPreferences sharedPreferences = context.getApplicationContext()
				.getSharedPreferences(SHARED_PREFERENCE, Context.MODE_PRIVATE);
		SharedPreferences.Editor editor = sharedPreferences.edit();
		editor.putString(KEY_ENCRYPT_KEY, key);
		editor.commit();
	}

	public static String loadEncryptKey(Context context) {
		SharedPreferences sharedPreferences = context.getApplicationContext()
				.getSharedPreferences(SHARED_PREFERENCE, Context.MODE_PRIVATE);
		return sharedPreferences.getString(KEY_ENCRYPT_KEY, "");
	}

	/** 自动登录信息 **/
	public static void saveLoginInfo(Context context, String loginInfo,
			boolean autoLoginFlag) {
		SharedPreferences sharedPreferences = context.getApplicationContext()
				.getSharedPreferences(SHARED_PREFERENCE, Context.MODE_PRIVATE);
		SharedPreferences.Editor editor = sharedPreferences.edit();
		editor.putString(KEY_LOGIN_INFO, loginInfo);
		editor.putBoolean(KEY_AUTO_LOGIN_FLAG, autoLoginFlag);
		editor.commit();
	}

	public static String loadAutoLoginInfo(Context context) {
		SharedPreferences sharedPreferences = context.getApplicationContext()
				.getSharedPreferences(SHARED_PREFERENCE, Context.MODE_PRIVATE);
		return sharedPreferences.getString(KEY_LOGIN_INFO, "");
	}

	public static void saveAutoLoginFlag(Context context, boolean autoLoginFlag) {
		SharedPreferences sharedPreferences = context.getApplicationContext()
				.getSharedPreferences(SHARED_PREFERENCE, Context.MODE_PRIVATE);
		SharedPreferences.Editor editor = sharedPreferences.edit();
		editor.putBoolean(KEY_AUTO_LOGIN_FLAG, autoLoginFlag);
		editor.commit();
	}

	public static boolean loadAutoLoginFlag(Context context) {
		SharedPreferences sharedPreferences = context.getApplicationContext()
				.getSharedPreferences(SHARED_PREFERENCE, Context.MODE_PRIVATE);
		return sharedPreferences.getBoolean(KEY_AUTO_LOGIN_FLAG, false);
	}

	/** 账号信息 **/
	public static void saveLoginAccount(Context context, long account) {
		SharedPreferences sharedPreferences = context.getApplicationContext()
				.getSharedPreferences(SHARED_PREFERENCE, Context.MODE_PRIVATE);
		SharedPreferences.Editor editor = sharedPreferences.edit();
		editor.putLong(KEY_LOGIN_ACCOUNT, account);
		editor.commit();
	}

	public static long loadLoginAccount(Context context) {
		SharedPreferences sharedPreferences = context.getApplicationContext()
				.getSharedPreferences(SHARED_PREFERENCE, Context.MODE_PRIVATE);
		return sharedPreferences.getLong(KEY_LOGIN_ACCOUNT, 0);
	}

	/** 账户头像Url **/
	public static void saveAccountHeadUrl(Context context, String head_url) {
		SharedPreferences sharedPreferences = context.getApplicationContext()
				.getSharedPreferences(SHARED_PREFERENCE, Context.MODE_PRIVATE);
		SharedPreferences.Editor editor = sharedPreferences.edit();
		editor.putString(KEY_ACCOUNT_HEAD_URL, head_url);
		editor.commit();
	}

	public static String loadAccountHeadUrl(Context context) {
		SharedPreferences sharedPreferences = context.getApplicationContext()
				.getSharedPreferences(SHARED_PREFERENCE, Context.MODE_PRIVATE);
		return sharedPreferences.getString(KEY_ACCOUNT_HEAD_URL, "");
	}

	/** 账户头像Url **/
	public static void saveLoginName(Context context, String head_url) {
		SharedPreferences sharedPreferences = context.getApplicationContext()
				.getSharedPreferences(SHARED_PREFERENCE, Context.MODE_PRIVATE);
		SharedPreferences.Editor editor = sharedPreferences.edit();
		editor.putString(KEY_Login_Name, head_url);
		editor.commit();
	}

	public static String loadLoginName(Context context) {
		SharedPreferences sharedPreferences = context.getApplicationContext()
				.getSharedPreferences(SHARED_PREFERENCE, Context.MODE_PRIVATE);
		return sharedPreferences.getString(KEY_Login_Name, "");
	}
	/** 账号信息(Json) **/
	public static void saveMemberInfo(Context context, String member_json) {
		SharedPreferences sharedPreferences = context.getApplicationContext()
				.getSharedPreferences(SHARED_PREFERENCE, Context.MODE_PRIVATE);
		SharedPreferences.Editor editor = sharedPreferences.edit();
		editor.putString(KEY_MEMBER_INFO, member_json);
		editor.commit();
	}

	public static String loadMemberInfo(Context context) {
		SharedPreferences sharedPreferences = context.getApplicationContext()
				.getSharedPreferences(SHARED_PREFERENCE, Context.MODE_PRIVATE);
		return sharedPreferences.getString(KEY_MEMBER_INFO, "");
	}

	/** 菜单信息(Json) **/
	public static void saveMenusInfo(Context context, String menu_json) {
		SharedPreferences sharedPreferences = context.getApplicationContext()
				.getSharedPreferences(SHARED_PREFERENCE, Context.MODE_PRIVATE);
		SharedPreferences.Editor editor = sharedPreferences.edit();
		editor.putString(KEY_MENU_INFO, menu_json);
		editor.commit();
	}

	public static String loadMenusInfo(Context context) {
		SharedPreferences sharedPreferences = context.getApplicationContext()
				.getSharedPreferences(SHARED_PREFERENCE, Context.MODE_PRIVATE);
		return sharedPreferences.getString(KEY_MENU_INFO, "");
	}

	/** 部门信息(Json) **/
	public static void saveDepartemntsInfo(Context context,
			String obj_departments) {
		SharedPreferences sharedPreferences = context.getApplicationContext()
				.getSharedPreferences(SHARED_PREFERENCE, Context.MODE_PRIVATE);
		SharedPreferences.Editor editor = sharedPreferences.edit();
		editor.putString(KEY_DEPARTMENT_INFO, obj_departments);
		editor.commit();
	}

	public static String loadDepartemntsInfo(Context context) {
		SharedPreferences sharedPreferences = context.getApplicationContext()
				.getSharedPreferences(SHARED_PREFERENCE, Context.MODE_PRIVATE);
		return sharedPreferences.getString(KEY_DEPARTMENT_INFO, "");
	}

	public static void savePlantId(Context context, String plantId) {
		SharedPreferences sharedPreferences = context.getApplicationContext()
				.getSharedPreferences(SHARED_PREFERENCE, Context.MODE_PRIVATE);
		SharedPreferences.Editor editor = sharedPreferences.edit();
		editor.putString(KEY_LOGIN_PLANT_ID, plantId);
		editor.commit();
	}

	public static String loadPlantId(Context context) {
		SharedPreferences sharedPreferences = context.getApplicationContext()
				.getSharedPreferences(SHARED_PREFERENCE, Context.MODE_PRIVATE);
		return sharedPreferences.getString(KEY_LOGIN_PLANT_ID, "");
	}

	public static void saveMemberId(Context context, String memberId) {
		SharedPreferences sharedPreferences = context.getApplicationContext()
				.getSharedPreferences(SHARED_PREFERENCE, Context.MODE_PRIVATE);
		SharedPreferences.Editor editor = sharedPreferences.edit();
		editor.putString(KEY_LOGIN_MEMBER_ID, memberId);
		editor.commit();
	}

	public static String loadMemberId(Context context) {
		SharedPreferences sharedPreferences = context.getApplicationContext()
				.getSharedPreferences(SHARED_PREFERENCE, Context.MODE_PRIVATE);
		return sharedPreferences.getString(KEY_LOGIN_MEMBER_ID, "");
	}

	public static void saveSession(Context context, String sessionNo) {
		SharedPreferences sharedPreferences = context.getApplicationContext()
				.getSharedPreferences(SHARED_PREFERENCE, Context.MODE_PRIVATE);
		SharedPreferences.Editor editor = sharedPreferences.edit();
		editor.putString(KEY_SESSION_NO, sessionNo);
		editor.commit();
	}

	public static String loadSession(Context context) {
		SharedPreferences sharedPreferences = context.getApplicationContext()
				.getSharedPreferences(SHARED_PREFERENCE, Context.MODE_PRIVATE);
		return sharedPreferences.getString(KEY_SESSION_NO, "");
	}



	/** 是否首次运行 **/
	public static void saveAppRunFirstFlag(Context context, boolean falg) {
		SharedPreferences sharedPreferences = context.getApplicationContext()
				.getSharedPreferences(SHARED_PREFERENCE, Context.MODE_PRIVATE);
		SharedPreferences.Editor editor = sharedPreferences.edit();
		editor.putBoolean(KEY_APP_RUN_FIRST_FLAG, falg);
		editor.commit();
	}

	public static boolean loadAppRunFirstFlag(Context context) {
		SharedPreferences sharedPreferences = context.getApplicationContext()
				.getSharedPreferences(SHARED_PREFERENCE, Context.MODE_PRIVATE);
		return sharedPreferences.getBoolean(KEY_APP_RUN_FIRST_FLAG, true);
	}

	/** 设备蓝牙地址**/
	public static void saveDeviceAddress(Context context, String address) {
		SharedPreferences sharedPreferences = context.getApplicationContext()
				.getSharedPreferences(SHARED_PREFERENCE, Context.MODE_PRIVATE);
		SharedPreferences.Editor editor = sharedPreferences.edit();
		editor.putString(KEY_DEVICE_ADDRESS, address);
		editor.commit();
	}

	public static  String loadDeviceAddress(Context context) {
		SharedPreferences sharedPreferences = context.getApplicationContext()
				.getSharedPreferences(SHARED_PREFERENCE, Context.MODE_PRIVATE);
		return sharedPreferences.getString(KEY_DEVICE_ADDRESS, "");
	}



	/** 设备Work model**/
	public static void saveWorkModel(Context context, int param) {
		SharedPreferences sharedPreferences = context.getApplicationContext()
				.getSharedPreferences(SHARED_PREFERENCE, Context.MODE_PRIVATE);
		SharedPreferences.Editor editor = sharedPreferences.edit();
		editor.putInt(KEY_WORK_MODEL, param);
		editor.commit();
	}

	public  static int loadWorkModel(Context context) {
		SharedPreferences sharedPreferences = context.getApplicationContext()
				.getSharedPreferences(SHARED_PREFERENCE, Context.MODE_PRIVATE);
		return sharedPreferences.getInt(KEY_WORK_MODEL,0);
	}
	
	/** 设备KEY_POWER_TEMP**/
	public static void savePowerTemp(Context context, int param) {
		SharedPreferences sharedPreferences = context.getApplicationContext()
				.getSharedPreferences(SHARED_PREFERENCE, Context.MODE_PRIVATE);
		SharedPreferences.Editor editor = sharedPreferences.edit();
		editor.putInt(KEY_POWER_TEMP, param);
		editor.commit();
	}

	public  static int loadPowerTemp(Context context) {
		SharedPreferences sharedPreferences = context.getApplicationContext()
				.getSharedPreferences(SHARED_PREFERENCE, Context.MODE_PRIVATE);
		return sharedPreferences.getInt(KEY_POWER_TEMP,0);
	}
	
//	public static final String KEY_SMOKING_TIME="device_smoking_time";
//	public static final String KEY_BATTERY="device_battery";
//	public static final String KEY_RESISTANCE="device_resistance";
	
	
	/** 设备KEY_POWER_TEMP**/
	public static void saveSmokingTime(Context context, int param) {
		SharedPreferences sharedPreferences = context.getApplicationContext()
				.getSharedPreferences(SHARED_PREFERENCE, Context.MODE_PRIVATE);
		SharedPreferences.Editor editor = sharedPreferences.edit();
		editor.putInt(KEY_SMOKING_TIME, param);
		editor.commit();
	}

	public static  int loadSmokingTime(Context context) {
		SharedPreferences sharedPreferences = context.getApplicationContext()
				.getSharedPreferences(SHARED_PREFERENCE, Context.MODE_PRIVATE);
		return sharedPreferences.getInt(KEY_SMOKING_TIME,0);
	}
	
	/** 设备KEY_POWER_TEMP**/
	public static void saveBattery(Context context, int param) {
		SharedPreferences sharedPreferences = context.getApplicationContext()
				.getSharedPreferences(SHARED_PREFERENCE, Context.MODE_PRIVATE);
		SharedPreferences.Editor editor = sharedPreferences.edit();
		editor.putInt(KEY_BATTERY, param);
		editor.commit();
	}

	public static int loadBattery(Context context) {
		SharedPreferences sharedPreferences = context.getApplicationContext()
				.getSharedPreferences(SHARED_PREFERENCE, Context.MODE_PRIVATE);
		return sharedPreferences.getInt(KEY_BATTERY,0);
	}
	/** 设备KEY_POWER_TEMP**/
	public static void saveResistance(Context context, int param) {
		SharedPreferences sharedPreferences = context.getApplicationContext()
				.getSharedPreferences(SHARED_PREFERENCE, Context.MODE_PRIVATE);
		SharedPreferences.Editor editor = sharedPreferences.edit();
		editor.putInt(KEY_RESISTANCE, param);
		editor.commit();
	}

	public static  int loadResistance(Context context) {
		SharedPreferences sharedPreferences = context.getApplicationContext()
				.getSharedPreferences(SHARED_PREFERENCE, Context.MODE_PRIVATE);
		return sharedPreferences.getInt(KEY_RESISTANCE,0);
	}
	
	/** 设备KEY_POWER_TEMP**/
	public static void saveUserPhoto(Context context, String param) {
		SharedPreferences sharedPreferences = context.getApplicationContext()
				.getSharedPreferences(SHARED_PREFERENCE, Context.MODE_PRIVATE);
		SharedPreferences.Editor editor = sharedPreferences.edit();
		editor.putString(KEY_User_Photo, param);
		editor.commit();
	}

	public static  String loadUserPhoto(Context context) {
		SharedPreferences sharedPreferences = context.getApplicationContext()
				.getSharedPreferences(SHARED_PREFERENCE, Context.MODE_PRIVATE);
		return sharedPreferences.getString(KEY_User_Photo,"");
	}
	
	/** 设备KEY_POWER_TEMP**/
	public static void saveDeviceName(Context context, String param) {
		SharedPreferences sharedPreferences = context.getApplicationContext()
				.getSharedPreferences(SHARED_PREFERENCE, Context.MODE_PRIVATE);
		SharedPreferences.Editor editor = sharedPreferences.edit();
		editor.putString(KEY_Device_Name, param);
		editor.commit();
	}

	public static  String loadDeviceName(Context context) {
		SharedPreferences sharedPreferences = context.getApplicationContext()
				.getSharedPreferences(SHARED_PREFERENCE, Context.MODE_PRIVATE);
		return sharedPreferences.getString(KEY_Device_Name,"");
	}
	
	
	/**
	 *
	 */
	public static class Push {
		/** Push状态设置 **/
		public static void savePushStatusFlag(Context context, boolean flag) {
			SharedPreferences sharedPreferences = context
					.getApplicationContext().getSharedPreferences(
							SHARED_PREFERENCE_PUSH, Context.MODE_PRIVATE);
			SharedPreferences.Editor editor = sharedPreferences.edit();
			editor.putBoolean(KEY_PUSH_STATUS_FLAG, flag);
			editor.commit();
		}

		public static boolean loadPushStatusFlag(Context context) {
			SharedPreferences sharedPreferences = context
					.getApplicationContext().getSharedPreferences(
							SHARED_PREFERENCE_PUSH, Context.MODE_PRIVATE);
			return sharedPreferences.getBoolean(KEY_PUSH_STATUS_FLAG, true);
		}

		/** Push推送提示音 **/
		public static void savePushSoundFlag(Context context, boolean flag) {
			SharedPreferences sharedPreferences = context
					.getApplicationContext().getSharedPreferences(
							SHARED_PREFERENCE_PUSH, Context.MODE_PRIVATE);
			SharedPreferences.Editor editor = sharedPreferences.edit();
			editor.putBoolean(KEY_PUSH_SOUND_FLAG, flag);
			editor.commit();
		}

		public static boolean loadPushSoundFlag(Context context) {
			SharedPreferences sharedPreferences = context
					.getApplicationContext().getSharedPreferences(
							SHARED_PREFERENCE_PUSH, Context.MODE_PRIVATE);
			return sharedPreferences.getBoolean(KEY_PUSH_SOUND_FLAG, true);
		}

		/** Push推送震动提示 **/
		public static void savePushShockFlag(Context context, boolean flag) {
			SharedPreferences sharedPreferences = context
					.getApplicationContext().getSharedPreferences(
							SHARED_PREFERENCE_PUSH, Context.MODE_PRIVATE);
			SharedPreferences.Editor editor = sharedPreferences.edit();
			editor.putBoolean(KEY_PUSH_SHOCK_FLAG, flag);
			editor.commit();
		}

		public static boolean loadPushShockFlag(Context context) {
			SharedPreferences sharedPreferences = context
					.getApplicationContext().getSharedPreferences(
							SHARED_PREFERENCE_PUSH, Context.MODE_PRIVATE);
			return sharedPreferences.getBoolean(KEY_PUSH_SHOCK_FLAG, true);
		}

		/** Push灯光提示 **/
		public static void savePushFlashFlag(Context context, boolean flag) {
			SharedPreferences sharedPreferences = context
					.getApplicationContext().getSharedPreferences(
							SHARED_PREFERENCE_PUSH, Context.MODE_PRIVATE);
			SharedPreferences.Editor editor = sharedPreferences.edit();
			editor.putBoolean(KEY_PUSH_FLASH_FLAG, flag);
			editor.commit();
		}

		public static boolean loadPushFlashFlag(Context context) {
			SharedPreferences sharedPreferences = context
					.getApplicationContext().getSharedPreferences(
							SHARED_PREFERENCE_PUSH, Context.MODE_PRIVATE);
			return sharedPreferences.getBoolean(KEY_PUSH_FLASH_FLAG, true);
		}

		/** Push绑定工号 **/
		public static void savePushBindAccounts(Context context, String account) {
			SharedPreferences sharedPreferences = context
					.getApplicationContext().getSharedPreferences(
							SHARED_PREFERENCE_PUSH, Context.MODE_PRIVATE);
			SharedPreferences.Editor editor = sharedPreferences.edit();

			String accounts = loadPushBindAccounts(context);
			StringBuffer buffer = new StringBuffer(accounts);
			if (!"".equals(accounts)) {
				buffer.append(App.SEPARATOR_CHAR).append(account);
			}

			editor.putString(KEY_PUSH_BIND_USER, buffer.toString());
			editor.commit();
		}

		public static String loadPushBindAccounts(Context context) {
			SharedPreferences sharedPreferences = context
					.getApplicationContext().getSharedPreferences(
							SHARED_PREFERENCE_PUSH, Context.MODE_PRIVATE);
			return sharedPreferences.getString(KEY_PUSH_BIND_USER, "");
		}

		/** Push绑定工号 **/
		public static void savePushBindAccount(Context context, String accont) {
			SharedPreferences sharedPreferences = context
					.getApplicationContext().getSharedPreferences(
							SHARED_PREFERENCE_PUSH, Context.MODE_PRIVATE);
			SharedPreferences.Editor editor = sharedPreferences.edit();
			editor.putString(KEY_PUSH_BIND_ACCOUNT, accont);
			editor.commit();
		}

		public static String loadPushBindAccount(Context context) {
			SharedPreferences sharedPreferences = context
					.getApplicationContext().getSharedPreferences(
							SHARED_PREFERENCE_PUSH, Context.MODE_PRIVATE);
			return sharedPreferences.getString(KEY_PUSH_BIND_ACCOUNT, "");
		}

		/** 百度Push相关 **/
		public static void saveBDPushConfig(Context context, String appId,
				String userId, String channelId, String reqId) {
			SharedPreferences sharedPreferences = context
					.getApplicationContext().getSharedPreferences(
							SHARED_PREFERENCE_PUSH, Context.MODE_PRIVATE);
			SharedPreferences.Editor editor = sharedPreferences.edit();
			editor.putString(KEY_PUSH_BD_APP_ID, appId);
			editor.putString(KEY_PUSH_BD_USER_ID, userId);
			editor.putString(KEY_PUSH_BD_CHANNEL_ID, channelId);
			editor.putString(KEY_PUSH_BD_REQ_ID, reqId);
			editor.commit();
		}

		// 0-appID 1-userID 2-channelID 3-reqID
		public static String[] loadBDPushConfig(Context context) {
			SharedPreferences sharedPreferences = context
					.getApplicationContext().getSharedPreferences(
							KEY_PUSH_BD_REQ_ID, Context.MODE_PRIVATE);
			String appId = sharedPreferences.getString(KEY_PUSH_BD_APP_ID, "");
			String userId = sharedPreferences
					.getString(KEY_PUSH_BD_USER_ID, "");
			String channelId = sharedPreferences.getString(
					KEY_PUSH_BD_CHANNEL_ID, "");
			String reqId = sharedPreferences.getString(KEY_PUSH_BD_REQ_ID, "");
			return new String[] { appId, userId, channelId, reqId };
		}
	}

}
