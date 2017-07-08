package com.foxconn.rfid.theowner.util.string;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class VerifyUtils {
	/**
	 * 验证邮箱地址是否正确
	 * 
	 * @param email
	 * @return
	 */
	public static boolean verifyEmail(String email) {
		boolean flag = false;
		try {
			Pattern regex = Pattern.compile("^([a-zA-Z0-9_\\-\\.]+)@((\\[[0-9]{1,3}\\.[0-9]{1,3}\\.[0-9]{1,3}\\.)|(([a-zA-Z0-9\\-]+\\.)+))([a-zA-Z]{2,4}|[0-9]{1,3})(\\]?)$");
			Matcher matcher = regex.matcher(email);
			flag = matcher.matches();
		} catch (Exception e) {
			flag = false;
		}

		return flag;
	}

	/**
	 * 验证手机号码
	 * 
	 * @param mobiles
	 * @return [0-9]{5,9}
	 */
	public static boolean verifyPhone(String phone) {
		boolean flag = false;
		try {
			Pattern p = Pattern.compile("^((13[0-9])|(15[^4,\\D])|(18[0,5-9]))\\d{8}$");
			Matcher m = p.matcher(phone);
			flag = m.matches();
		} catch (Exception e) {
			flag = false;
		}
		return !flag;
	}

	public static boolean verifyNumber(String number) {
		boolean flag = false;
		try {
			Pattern p = Pattern.compile("^(-?[0-9]*.?[0-9]*)$");
			Matcher m = p.matcher(number);
			flag = m.matches();
		} catch (Exception e) {
			flag = false;
		}
		return flag;
	}
	
	public static boolean verifyServerIp(String ip) {
		boolean flag = false;
		try {
			Pattern p = Pattern.compile("^(25[0-5]|2[0-4][0-9]|1[0-9][0-9]|[0-9]{1,2})(\\.(25[0-5]|2[0-4][0-9]|1[0-9][0-9]|[0-9]{1,2})){3}$");
			Matcher m = p.matcher(ip);
			flag = m.matches();
		} catch (Exception e) {
			flag = false;
		}
		return flag;
	}
}