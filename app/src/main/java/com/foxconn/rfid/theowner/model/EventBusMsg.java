package com.foxconn.rfid.theowner.model;


public class EventBusMsg {

    public static final int MSG_Receive_Device_data = 1001;
    public static final int MSG_Device_disconnect = 1002;
    public static final int MSG_Device_connected = 1003;
    public static final int MSG_device_location_data = 1004;
    public static final int MSG_defence_edit_data = 1005;
//    public static final int MSG_apply_lost = 1006;
    public static final int MSG_apply_insurance_payout = 1007;
    public static final int MSG_apply_cash = 1008;
    public static final int MSG_APPLY_CASH_FINISH = 10112;

    public static final int MSG_INSURANCE_STATUS = 0x3666;
    public static final int MSG_REFRESH_BIKE_STATUS = 0x3667;

    public static final int BIKE_STATUS_REFRESH = 0x3681; //刷新LOSTDISPOSE 流程图实时状态值
    public static final int BIKE_STATUS_REFRESH_MAIN = 0x3682; //关闭LOSTDISPOSE时 刷新该车辆状态值
    public static final int BIKE_SETTING_REFRESH_MAIN = 0x3683; //关闭Setting时 刷新该车辆状态值

    public static final int PUSH_CLICK = 0x3686; //推送消息的点击事件


    private MsgEmptyType emptyType;

    public MsgEmptyType getEmptyType() {
        return emptyType;
    }

    public void setEmptyType(MsgEmptyType emptyType) {
        this.emptyType = emptyType;
    }

    private int bundle;
    public int getValue() {
        return bundle;
    }

    public void setValue(int bundle) {
        this.bundle = bundle;
    }



    private String tvBundle;
    public String getTvBundle() {
        return tvBundle;
    }
    public void setTvBundle(String tvBundle) {
        this.tvBundle = tvBundle;
    }

    private String tvBundle2;
    public String getTvBundle2() {
        return tvBundle2;
    }
    public void setTvBundle2(String tvBundle2) {
        this.tvBundle2 = tvBundle2;
    }


    private int MsgType;
    public int getMsgType() {
        return MsgType;
    }

    public void setMsgType(int msgType) {
        MsgType = msgType;
    }

    public enum MsgEmptyType {
        MSG_SAFE_LIST_SIZE_CHANGE,
        MSG_INSURANCE_LIST_SIZE_CHANGE,
        MSG_GET_ADDRESS_TEXT
//        MSG_REFRESH_BIKE_STATUS
    }
}
