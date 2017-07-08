package com.foxconn.rfid.theowner.model;


import java.io.Serializable;

public class EventBusMsgPush implements Serializable {

	private NotifyType notifyType;//1.arrive 2.click
	private long msgId;
	private String msgTitle;
	private String msgContent;
	private int msgType;
	private int subMsgType;
	private String referenceId;
	private long createDate;
	public  enum NotifyType{MSG_Arrived,MSG_Clicked};

	public long getMsgId() {
		return msgId;
	}

	public void setMsgId(long msgId) {
		this.msgId = msgId;
	}

	public String getMsgTitle() {
		return msgTitle;
	}

	public void setMsgTitle(String msgTitle) {
		this.msgTitle = msgTitle;
	}

	public String getMsgContent() {
		return msgContent;
	}

	public void setMsgContent(String msgContent) {
		this.msgContent = msgContent;
	}

	public void setMsgType(String msgType) {
		msgType = msgType;
	}

	public String getReferenceId() {
		return referenceId;
	}

	public void setReferenceId(String referenceId) {
		this.referenceId = referenceId;
	}

	public int getMsgType() {
		return msgType;
	}

	public void setMsgType(int msgType) {
		this.msgType = msgType;
	}

	public int getSubMsgType() {
		return subMsgType;
	}

	public void setSubMsgType(int subMsgType) {
		this.subMsgType = subMsgType;
	}

	public long getCreateDate() {
		return createDate;
	}

	public void setCreateDate(long createDate) {
		this.createDate = createDate;
	}

	public NotifyType getNotifyType() {
		return notifyType;
	}

	public void setNotifyType(NotifyType notifyType) {
		this.notifyType = notifyType;
	}
}
