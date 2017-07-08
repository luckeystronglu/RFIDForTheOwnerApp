package com.foxconn.rfid.theowner.model;



public class EventBusMsgMessage {


	public static final  int MSG_Message_Security_Delete= 1001,MSG_Message_Insurance_Delete=1002;
	private int MsgType;
	private int Message_Selected_Index;

public int getMsgType() {
	return MsgType;
}

public void setMsgType(int msgType) {
	MsgType = msgType;
}

	public int getMessage_Selected_Index() {
		return Message_Selected_Index;
	}

	public void setMessage_Selected_Index(int message_Selected_Index) {
		Message_Selected_Index = message_Selected_Index;
	}
}
