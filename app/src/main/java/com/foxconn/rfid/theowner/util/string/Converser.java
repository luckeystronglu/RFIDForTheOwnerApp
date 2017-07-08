package com.foxconn.rfid.theowner.util.string;

public class Converser {
	public static String bytes2HexString(byte[] b) {
		String ret = "";
		for (int i = 0; i < b.length; ++i) {
			String hex = Integer.toHexString(b[i] & 0xFF);
			if (hex.length() == 1) {
				hex = '0' + hex;
			}
			ret = ret + hex.toUpperCase();
		}
		return ret;
	}

	/**
	 * 字符串转换成十六进制字符串
	 */
	public static String str2HexStr(String str) {
		char[] chars = "0123456789ABCDEF".toCharArray();
		StringBuilder sb = new StringBuilder("");
		byte[] bs = str.getBytes();
		int bit;
		for (int i = 0; i < bs.length; i++) {
			bit = (bs[i] & 0x0f0) >> 4;
			sb.append(chars[bit]);
			bit = bs[i] & 0x0f;
			sb.append(chars[bit]);
		}
		return sb.toString();
	}

	/**
	 * 把16进制字符串转换成字节数组
	 * 
	 * @param hexString
	 * @return byte[]
	 */
	public static byte[] hexStringToByte(String hex) {
		int len = (hex.length() / 2);
		byte[] result = new byte[len];
		char[] achar = hex.toUpperCase().toCharArray();
		for (int i = 0; i < len; i++) {
			int pos = i * 2;
			result[i] = (byte) (toByte(achar[pos]) * 16 + toByte(achar[pos + 1]));
			if (i == 516) {
				System.out.print(result[i]);
			}
		}
		return result;
	}

	private static int toByte(char c) {
		byte b = (byte) "0123456789ABCDEF".indexOf(c);
		return b;
	}

	/**
	 * 数组转换成十六进制字符串
	 * 
	 * @param byte[]
	 * @return HexString
	 */
	public static final String bytesToHexString(byte[] bArray) {
		StringBuffer sb = new StringBuffer(bArray.length);
		String sTemp;
		for (int i = 0; i < bArray.length; i++) {
			sTemp = Integer.toHexString(0xFF & bArray[i]);
			if (sTemp.length() < 2)
				sb.append(0);
			sb.append(sTemp.toUpperCase());
		}
		return sb.toString();
	}

	/**
	 * 数组转成十六进制字符串
	 * 
	 * @param byte[]
	 * @return HexString
	 */
	public static String toHexString1(byte[] b) {
		StringBuffer buffer = new StringBuffer();
		for (int i = 0; i < b.length; ++i) {
			buffer.append(toHexString1(b[i]));
		}
		return buffer.toString();
	}

	public static String toHexString1(byte b) {
		String s = Integer.toHexString(b & 0xFF);
		if (s.length() == 1) {
			return "0" + s;
		} else {
			return s;
		}
	}

	/**
	 * 十六进制字符串转换成字符串
	 * 
	 * @param hexString
	 * @return String
	 */
	public static String hexStr2Str(String hexStr) {

		String str = "0123456789ABCDEF";
		char[] hexs = hexStr.toCharArray();
		byte[] bytes = new byte[hexStr.length() / 2];
		int n;
		for (int i = 0; i < bytes.length; i++) {
			n = str.indexOf(hexs[2 * i]) * 16;
			n += str.indexOf(hexs[2 * i + 1]);
			bytes[i] = (byte) (n & 0xff);
		}
		return new String(bytes);
	}

	/**
	 * 十六进制字符串转换字符串
	 * 
	 * @param HexString
	 * @return String
	 */
	public static String toStringHex(String s) {
		byte[] baKeyword = new byte[s.length() / 2];
		for (int i = 0; i < baKeyword.length; i++) {
			try {
				baKeyword[i] = (byte) (0xff & Integer.parseInt(s.substring(i * 2, i * 2 + 2), 16));
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		try {
			s = new String(baKeyword, "utf-8");// UTF-16le:Not
		} catch (Exception e1) {
			e1.printStackTrace();
		}
		return s;
	}

	/**
	 * 将一个单字节的byte转换成32位的int
	 * 
	 * @param b
	 *            byte
	 * @return convert result
	 */
	public static int unsignedByteToInt(byte b) {
		return (int) b & 0xFF;
	}

	/**
	 * 将一个单字节的Byte转换成十六进制的数
	 * 
	 * @param b
	 *            byte
	 * @return convert result
	 */
	public static String byteToHex(byte b) {
		int i = b & 0xFF;
		return Integer.toHexString(i);
	}

	/**
	 * 将一个4byte的数组转换成32位的int
	 * 
	 * @param buf
	 *            bytes buffer
	 * @param byte[]中开始转换的位置
	 * @return convert result
	 */
	public static long unsigned4BytesToInt(byte[] buf, int pos) {
		int firstByte = 0;
		int secondByte = 0;
		int thirdByte = 0;
		int fourthByte = 0;
		int index = pos;
		firstByte = (0x000000FF & ((int) buf[index]));
		secondByte = (0x000000FF & ((int) buf[index + 1]));
		thirdByte = (0x000000FF & ((int) buf[index + 2]));
		fourthByte = (0x000000FF & ((int) buf[index + 3]));
		index = index + 4;
		return ((long) (firstByte << 24 | secondByte << 16 | thirdByte << 8 | fourthByte)) & 0xFFFFFFFFL;
	}

	/**
	 * 将16位的short转换成byte数组
	 * 
	 * @param s
	 *            short
	 * @return byte[] 长度为2
	 * */
	public static byte[] shortToByteArray(short s) {
		byte[] targets = new byte[2];
		for (int i = 0; i < 2; i++) {
			int offset = (targets.length - 1 - i) * 8;
			targets[i] = (byte) ((s >>> offset) & 0xff);
		}
		return targets;
	}

	/**
	 * 将32位整数转换成长度为4的byte数组
	 * 
	 * @param s
	 *            int
	 * @return byte[]
	 * */
	public static byte[] intToByteArray(int s) {
		byte[] targets = new byte[2];
		for (int i = 0; i < 4; i++) {
			int offset = (targets.length - 1 - i) * 8;
			targets[i] = (byte) ((s >>> offset) & 0xff);
		}
		return targets;
	}

	/**
	 * long to byte[]
	 * 
	 * @param s
	 *            long
	 * @return byte[]
	 * */
	public static byte[] longToByteArray(long s) {
		byte[] targets = new byte[2];
		for (int i = 0; i < 8; i++) {
			int offset = (targets.length - 1 - i) * 8;
			targets[i] = (byte) ((s >>> offset) & 0xff);
		}
		return targets;
	}

	/** 32位int转byte[] */
	public static byte[] int2byte(int res) {
		byte[] targets = new byte[4];
		targets[0] = (byte) (res & 0xff);// 最低位
		targets[1] = (byte) ((res >> 8) & 0xff);// 次低位
		targets[2] = (byte) ((res >> 16) & 0xff);// 次高位
		targets[3] = (byte) (res >>> 24);// 最高位,无符号右移。
		return targets;
	}

	/**
	 * 将长度为2的byte数组转换为16位int
	 * 
	 * @param res
	 *            byte[]
	 * @return int
	 * */
	public static int byte2int(byte[] res) {
		// res = InversionByte(res);
		// 一个byte数据左移24位变成0x??000000，再右移8位变成0x00??0000
		int targets = (res[0] & 0xff) | ((res[1] << 8) & 0xff00); // | 表示安位或
		return targets;
	}

}
