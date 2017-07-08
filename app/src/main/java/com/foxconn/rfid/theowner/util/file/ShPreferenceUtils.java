/**
 * 
 */
package com.foxconn.rfid.theowner.util.file;

/**
 * @author WT00111
 * @category sharepreference 数据存储的帮助类
 */
public class ShPreferenceUtils {
	/**
	 * 
	 * @author WT00111
	 * @category sharedPreference 名称类
	 */
	public static final class SpNames {
		/** 专门存放用户信息 */
		public static final String USER_INFO = "user_info";
	}

	/**
	 * 
	 * @author WT00111
	 * @category sharedPreference 存放键的类
	 */
	public static final class SpKeys {
		/** 用户登陆时所用的名称 */
		public static final String KEY_USERNAME = "userName";
		/** 用户登陆时所用的密码 */
		public static final String KEY_PASSWORD = "password";
		/** 用户登陆时所用的是否登陆的标志 */
		public static final String KEY_ISLOGIN_FLAG = "isLogin_flag";
	}
}
