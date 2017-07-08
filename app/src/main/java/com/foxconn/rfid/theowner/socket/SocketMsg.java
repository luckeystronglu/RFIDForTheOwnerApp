package com.foxconn.rfid.theowner.socket;






/**
 * SocketCommand
 * 
 * @author F1041566
 * @date 2014/11/24 11:40
 * 
 */
public class SocketMsg {
	@SuppressWarnings("unused")
	private static final String TAG = SocketMsg.class.getName();
	public static final String API_VERSION = "1.0";

	private String socketMsg;
	private int commandId;
	public SocketMsg( String socketMsg) {
		// TODO Auto-generated constructor stub
//		this.commandId=commandId;
		this.socketMsg=socketMsg;
	}

	public String getSocketMsg() {
		return socketMsg;
	}

	public void setSocketMsg(String socketMsg) {
		this.socketMsg = socketMsg;
	}

	public int getCommandId() {
		return commandId;
	}
	public void setCommandId(int commandId) {
		this.commandId = commandId;
	}
	
}
