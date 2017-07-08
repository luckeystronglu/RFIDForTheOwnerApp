package com.foxconn.rfid.theowner.util.pinfo;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

import android.app.ActivityManager;
import android.app.ActivityManager.MemoryInfo;
import android.content.Context;
import android.telephony.TelephonyManager;
import android.text.format.Formatter;

public class PhoneUtils {

	/** 获取android当前可用内存大小 **/
	public static String getAvailMemory(Context context) {// 获取android当前可用内存大小
		ActivityManager am = (ActivityManager) context .getSystemService(Context.ACTIVITY_SERVICE);
		MemoryInfo mi = new MemoryInfo();
		am.getMemoryInfo(mi);

		return Formatter.formatFileSize(context.getApplicationContext(), mi.availMem);// 将获取的内存大小规格化
	}

	/** 获得系统总内存 **/
	public static String getTotalMemory(Context context) {
		String str1 = "/proc/meminfo";// 系统内存信息文件
		String str2;
		String[] arrayOfString;
		long initial_memory = 0;
		try {
			FileReader localFileReader = new FileReader(str1);
			BufferedReader localBufferedReader = new BufferedReader(localFileReader, 8192);
			str2 = localBufferedReader.readLine();// 读取meminfo第一行，系统总内存大小

			arrayOfString = str2.split("\\s+");

			initial_memory = Integer.valueOf(arrayOfString[1]).intValue() * 1024;// 获得系统总内存，单位是KB，乘以1024转换为Byte
			localBufferedReader.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return Formatter.formatFileSize(context.getApplicationContext(), initial_memory);// Byte转换为KB或者MB，内存大小规格化
	}

	/** 手机CPU信息 **/
	public static String[] getCpuInfo(Context context) {
		String str1 = "/proc/cpuinfo";
		String str2 = "";
		String[] cpuInfo = { "", "" }; // 1-cpu型号 //2-cpu频率
		String[] arrayOfString;
		try {
			FileReader fr = new FileReader(str1);
			BufferedReader localBufferedReader = new BufferedReader(fr, 8192);
			str2 = localBufferedReader.readLine();
			arrayOfString = str2.split("\\s+");
			for (int i = 2; i < arrayOfString.length; i++) {
				cpuInfo[0] = cpuInfo[0] + arrayOfString[i] + " ";
			}
			str2 = localBufferedReader.readLine();
			arrayOfString = str2.split("\\s+");
			cpuInfo[1] += arrayOfString[2];
			localBufferedReader.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return cpuInfo;
	}
	
	/** 获取IMEI号 **/
	public static String getPhoneIMEI(Context context) {
		TelephonyManager mTm = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
		String imei = mTm.getDeviceId();
		if (imei != null) return imei;
		return "";
	}

	/** 获取IMSI号 **/
	public static String getPhoneIMSI(Context context) {
		TelephonyManager mTm = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
		String imsi = mTm.getSubscriberId();
		if (imsi != null) return imsi;
		return "";
	}

	/** 手机型号 **/
	public static String getPhoneModel(Context context) {
		String mtype = android.os.Build.MODEL; // 手机型号
		if (mtype != null) return mtype;
		return "";
	}

	/** 手机品牌 **/
	public static String getPhoneBrand(Context context) {
		String mtyb = android.os.Build.BRAND;// 手机品牌
		if (mtyb != null) return mtyb;
		return "";
	}

	/** 获取手机号码 **/
	public static String getPhoneNumber(Context context) {
		TelephonyManager mTm = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
		String numer = mTm.getLine1Number(); // 手机号码，有的可得，有的不可得
		if (numer != null) return numer;
		return "";
	}
	
	/** 获取运营商 **/
	public String getProvidersName(Context context) {
		String ProvidersName = "";
		// 返回唯一的用户ID;就是这张卡的编号神马的
		String imsi = getPhoneIMSI(context);
		// IMSI号前面3位460是国家，紧接着后面2位00 02是中国移动，01是中国联通，03是中国电信。
		System.out.println(imsi);
		if (imsi.startsWith("46000") || imsi.startsWith("46002")) {
			ProvidersName = "中国移动";
		} else if (imsi.startsWith("46001")) {
			ProvidersName = "中国联通";
		} else if (imsi.startsWith("46003")) {
			ProvidersName = "中国电信";
		}
		return ProvidersName;
	}

}
