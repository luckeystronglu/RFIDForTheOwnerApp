package com.foxconn.rfid.theowner.socket;

import android.annotation.SuppressLint;

import org.apache.mina.core.session.IoSession;

@SuppressLint("DefaultLocale")
public class SocketAppPacket {
	// 最新版本检查
	public static final int COMMAND_ID_GET_APP_VERSION			= 0x000000010;
	// 用户登录
	public static final int COMMAND_ID_USER_LOGIIN				= 0x000030020;

	// 退出登录
	public static final int COMMAND_ID_LOGIINOUT = 0x30040;

	// 用户注册
	public static final int COMMAND_ID_UPDATE_PASSWORD		= 0x000030030;

	public static final int COMMAND_ID_get_bikes_by_user_id	= 0x000010020;
	public static final int COMMAND_ID_get_bikes_location	= 0x000010050;
	public static final int COMMAND_ID_get_bike_track	= 0x000010060;
	public static final int COMMAND_ID_get_bike_defence	= 0x000010070;
	public static final int COMMAND_ID_EDIT_BIKE_DEFENCE = 0x000010080;
    public static final int COMMAND_ID_get_bike_insurance = 0x000010090;
    public static final int COMMAND_ID_GET_BIKE_INSURANCE_LIST = 0x10100;
    public static final int COMMAND_ID_get_bike_lost = 0x0000100a0;
    public static final int COMMAND_ID_get_bike_lost_result = 0x0000100b0;
	public static final int COMMAND_ID_apply_payout = 0x0000100c0;
	public static final int COMMAND_ID_GETMONEY_RESULT = 0x100d0;

	public static final int COMMAND_ID_APPLY_INSURANCE_PAYOUT = 0x0000100B0;
	public static final int COMMANT_ID_GET_Message_system = 0x51010;//系统消息
	public static final int COMMANT_ID_GET_SECURITY_MESSAGE_APP = 0x52010;//车辆安防
	public static final int COMMAND_ID_GET_INSURANCE_MESSAGE_APP = 0x53000;//车辆保险
	public static final int COMMANT_ID_DEL_SECURITY_MESSAGE_BY_IDS_APP = 0x52020;  //删除车辆安防消息列表
	public static final int COMMANT_ID_DEL_INSURANCE_MESSAGE_BY_IDS_APP = 0x53040;  //删除车辆保险消息列表
	public static final int COMMANT_ID_GET_BIKE_INSURANCE_CASH_BY_ID = 0x53030;  //通过id获取提现申请消息
	public static final int COMMANT_ID_GET_BIKE_INSURANCE_PAYOUT_BY_ID = 0x53020;  //通过id获取索赔申请消息
	public static final int COMMANT_ID_MESSAGE_INSURANCE_APPLY_PAYOUT = 0x53020;  //索赔申请消息
	public static final int COMMANT_ID_MESSAGE_APPLY_LOSS = 0x53010;  //挂失申请消息
//	public static final int COMMAND_ID_BIKE_INSURE_LIST = 0x54010; //车辆保险列表
//  public static final int COMMAND_ID_BIKE_INSURE = 0x54020; //提交所购买保险
	public static final int COMMAND_ID_BIKE_INSURE_LIST = 0x100e0; //车辆保险列表
	public static final int COMMAND_ID_BIKE_INSURE = 0x100f0; //提交所购买保险
	public static final int COMMAND_ID_OPEN_CLOSE_SAFETY_MSG = 0x10200; //打开/关闭车辆安防
	public static final int COMMAND_ID_BIKE_GET_MONEY = 0x100c0; //打开/关闭车辆安防
	public static final int COMMAND_ID_GETBIKEMSG_BY_BIKEID = 0x10300; //通过车辆ID获取该车辆信息



	private IoSession fromClient = null;

	/**
	 * @return the fromClient
	 */
	public IoSession getFromClient() {
		return fromClient;
	}

	public SocketAppPacket(IoSession channel) {
		this.fromClient = channel;
	}

	String packetType;

	public String getPacketType() {
		return packetType;
	}

	/**
	 * @param packetType
	 *            the packetType to set
	 */
	private void setPacketType(String packetType) {
		this.packetType = packetType;
	}


	private int commandId = 0;

	/**
	 * @return the commandId
	 */
	public int getCommandId() {
		return commandId;
	}

	/**
	 * @param commandId
	 *            the commandId to set
	 */
	public void setCommandId(int commandId) {
		this.commandId = commandId;

		String typeString = "0x" + Integer.toHexString(commandId).toUpperCase() + "_";
		switch (commandId) {
		// 最新版本检查
		case SocketAppPacket.COMMAND_ID_GET_APP_VERSION:
			typeString += "GET_APP_VERSION";
			break;
		// 用户登录
		case SocketAppPacket.COMMAND_ID_USER_LOGIIN:
			typeString += "USER_LOGIN";
			break;
		default:
			typeString += "UNKNOWN";
			break;
		}

		setPacketType(typeString);
	}

	private byte[] commandData = null;
	/**
	 * @return the commandData
	 */
	public byte[] getCommandData() {
		return commandData;
	}

	/**
	 * @param commandData
	 *            the commandData to set
	 */
	public void setCommandData(byte[] commandData) {
		this.commandData = commandData;
	}

	long receiveTime = 0;

	/**
	 * @return the receiveTime
	 */
	public long getReceiveTime() {
		return receiveTime;
	}

	/**
	 * @param receiveTime
	 *            the receiveTime to set
	 */
	public void setReceiveTime(long receiveTime) {
		this.receiveTime = receiveTime;
	}

}
