/**
 * 
 */
package com.foxconn.rfid.theowner.util.string;

import java.net.Inet4Address;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.util.Enumeration;
import java.util.regex.Pattern;

import android.annotation.SuppressLint;

/**
 * @author WT00111
 * 
 */
public class StringUtils {
	/**
	 * 验证身份证格式是否正确，
	 * 
	 * @param str
	 * @return 正确：true，错误:false
	 */
	public static boolean identity(String str) {
		Pattern pattern = Pattern
				.compile("^[1-9][0-9]{5}(19[0-9]{2}|200[0-9]|2010)(0[1-9]|1[0-2])(0[1-9]|[12][0-9]|3[01])[0-9]{3}[0-9xX]$");
		return pattern.matcher(str).matches();
	}
	
	/**
	 * 获取当前系统连接网络的网卡的mac地址
	 * 
	 * @return
	 */
	@SuppressLint("NewApi")
	public static final String getMac() {
		byte[] mac = null;
		StringBuffer sb = new StringBuffer();
		try {
			Enumeration<NetworkInterface> netInterfaces = NetworkInterface.getNetworkInterfaces();
			while (netInterfaces.hasMoreElements()) {
				NetworkInterface ni = netInterfaces.nextElement();
				Enumeration<InetAddress> address = ni.getInetAddresses();

				while (address.hasMoreElements()) {
					InetAddress ip = address.nextElement();
					if (ip.isAnyLocalAddress() || !(ip instanceof Inet4Address) || ip.isLoopbackAddress())
						continue;
					if (ip.isSiteLocalAddress())
						mac = ni.getHardwareAddress();
					else if (!ip.isLinkLocalAddress()) {
						mac = ni.getHardwareAddress();
						break;
					}
				}
			}
		} catch (SocketException e) {
			e.printStackTrace();
		}

		if (mac != null) {
			for (int i = 0; i < mac.length; i++) {
				String str="00" + Integer.toHexString(mac[i]) + ":";
				sb.append(str.substring(str.length() - 3));
			}
			return sb.substring(0, sb.length() - 1);
		} else {
			return "";
		}
	}
	// 获取当前连接网络的网卡的mac地址
	private static String parseByte(byte b) {
		String s = "00" + Integer.toHexString(b) + ":";
		return s.substring(s.length() - 3);
	}

}
