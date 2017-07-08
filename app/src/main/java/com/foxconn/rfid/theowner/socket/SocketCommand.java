package com.foxconn.rfid.theowner.socket;

import com.foxconn.rfid.theowner.util.logort.LogUtils;
import com.foxconn.rfid.theowner.util.string.FormatTransfer;





/**
 * SocketCommand
 * 
 * @author F1041566
 * @date 2014/11/24 11:40
 * 
 */
public class SocketCommand {
	private static final String TAG = SocketCommand.class.getName();

	public static final int PACKET_LEN_START_FLAG = 2,
			PACKET_LEN_DATA_LENGTH = 4, PACKET_LEN_COMMAND_ID = 2,
			PACKET_LEN_CRC = 2;

	public static final int PACKET_LEN_WITHOUTDATA = PACKET_LEN_DATA_LENGTH
			+ PACKET_LEN_COMMAND_ID;
	public static final int DATA_START_INDEX = PACKET_LEN_DATA_LENGTH
			+ PACKET_LEN_COMMAND_ID;

	public static final String API_VERSION = "1.0";

	public static final short app_getAppVersion = 0x00010;// 获取APP版本信息
	public static final short app_getAllDeviceInfo = 0x0002;// 获取终端所有信息

	public static final short app_getDeviceDetailInfo = 0x0003;// 设置终端防御
	public static final short app_sendDeviceSetting = 0x1002;// 下发终端设置信息
	public static final short app_getDeviceSettings = 0x1001;// 获取终端设置信息
	public static final short app_rebootDevice = 0x1003;// 下发终端重启
	public static final short app_setProtect = 0x1004;// 设置终端防御
	// trace
	public static final short app_getDeviceStatistics = 0x2001;// 设置终端防御
	public static final short app_startRealtimeTrace = 0x2002;// 开始轨迹追踪
	public static final short app_stopRealtimeTrace = 0x2003;// 停止轨迹追踪
	public static final short app_GetRealtimeTrace = 0x2004;// 獲取即時軌跡
	public static final short app_getRealtimeTraceList = 0x2005;// 获取轨迹列表
	public static final short app_getRealtimeTraceDetail = 0x2006;// 获取轨迹详情
	public static final short app_getRealtimeTraceStatus = 0x2007;// 获取正在进行中的轨迹

	public static byte[] generateData(String jsonStr, short cmdId,
			boolean hasSign) {
		String jsonStr_sign = null;
		jsonStr_sign = signJsonParam(jsonStr, false);
		LogUtils.logMessage(TAG, "jsonStr_sign=" + jsonStr_sign);
		byte[] cmdData = FormatTransfer.stringToBytes(jsonStr_sign);
		return generateData(cmdData, cmdId);
	}

	public static byte[] generateData(byte[] cmdData, short cmdId) {
		// data
		int cmdDataLen = 0;
		if (cmdData != null) {
			cmdDataLen = cmdData.length;
		}

		// the len of whole packet
		int len = PACKET_LEN_START_FLAG + PACKET_LEN_WITHOUTDATA + cmdDataLen
				+ PACKET_LEN_CRC;
		int lenWithoutStartFlag = PACKET_LEN_WITHOUTDATA + cmdDataLen
				+ PACKET_LEN_CRC;

		byte[] bytes = new byte[len];

		// start flag
		bytes[0] = (byte) 0xAA;
		bytes[1] = (byte) 0xAA;

		// the len of packet without header.

		byte[] lenWithoutStartFlagbytes = FormatTransfer
				.toLH(lenWithoutStartFlag);
		int commandIDStartIndex = PACKET_LEN_START_FLAG
				+ PACKET_LEN_DATA_LENGTH;

		bytes[2] = lenWithoutStartFlagbytes[0];
		bytes[3] = lenWithoutStartFlagbytes[1];
		bytes[4] = lenWithoutStartFlagbytes[2];
		bytes[5] = lenWithoutStartFlagbytes[3];

		// command Id
		byte[] commandIdBytes = FormatTransfer.toLH(cmdId);

		bytes[commandIDStartIndex + 0] = commandIdBytes[0];
		bytes[commandIDStartIndex + 1] = commandIdBytes[1];

		for (int i = 0; i < cmdDataLen; i++) {
			bytes[commandIDStartIndex + 2 + i] = cmdData[i];
		}

//		byte[] crc = CRC16.calcCRC(bytes, 2, bytes.length - 2 - 1);
		byte[] crc = {0};
		int crcStartIndex = commandIDStartIndex + PACKET_LEN_COMMAND_ID
				+ cmdDataLen;
		bytes[crcStartIndex] = crc[0];
		bytes[crcStartIndex + 1] = crc[1];
		return bytes;
	}

	public static String signJsonParam(String jsonStr, boolean hasSign) {
		if (hasSign) {
			// return "{\"body\":" + jsonStr + ",\"sign\":\"" +
			// DigestUtils.encryptJniStr(jsonStr) + "\"}";
			return "{" + jsonStr + ",\"sign\":\""
					+ (jsonStr) + "\"}";
		} else {
			// return "{\"body\":" + jsonStr + "}";
			return jsonStr;
		}
	}

	public static boolean verifyJson(String jsonStr, String signStr) {
		String signJson =(jsonStr);
		return signJson.equals(signStr);
	}

	

}
