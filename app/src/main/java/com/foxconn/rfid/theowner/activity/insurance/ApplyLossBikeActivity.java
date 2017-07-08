package com.foxconn.rfid.theowner.activity.insurance;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

import com.foxconn.rfid.theowner.R;
import com.foxconn.rfid.theowner.activity.LoginActivity;
import com.foxconn.rfid.theowner.activity.main.defence.LostAddrMapActivity;
import com.foxconn.rfid.theowner.base.BaseActivity;
import com.foxconn.rfid.theowner.base.PreferenceData;
import com.foxconn.rfid.theowner.model.Bike;
import com.foxconn.rfid.theowner.model.BikeUser;
import com.foxconn.rfid.theowner.model.EventBusMsg;
import com.foxconn.rfid.theowner.socket.SocketAppPacket;
import com.foxconn.rfid.theowner.socket.SocketClient;
import com.foxconn.rfid.theowner.util.logort.ToastUtils;
import com.foxconn.rfid.theowner.view.LossBikeDateView;
import com.foxconn.rfid.theowner.view.widgets.DateSelectDialog;
import com.google.protobuf.InvalidProtocolBufferException;
import com.yzh.rfidbike.app.request.ApplyBikeLostRequest;
import com.yzh.rfidbike.app.response.CommonResponse;

import java.text.SimpleDateFormat;
import java.util.Date;

import de.greenrobot.event.EventBus;

/**
 * Created by Administrator on 2016/12/20.
 */

public class ApplyLossBikeActivity extends BaseActivity implements View.OnClickListener {
    private LossBikeDateView mDateView;
    private DateSelectDialog mDateSelectDialog;
    private Button mBtnSave;
    private EditText mEtAddress;
    private ImageView imv_address;
    private long mLostTime;
    private Bike mBike;
    private String hintTime;
    private boolean isBuildLostSameDay;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_loss_bike);
        mBike = (Bike) getIntent().getSerializableExtra("BIKE");

        mBtnSave = (Button) findViewById(R.id.btn_save);
        mEtAddress = (EditText) findViewById(R.id.et_address);
        imv_address = (ImageView) findViewById(R.id.location);
        mDateSelectDialog = new DateSelectDialog(this);
        mDateSelectDialog.setListener(new DateSelectDialog.DateSelectListener() {
            @Override
            public void selected(long time, String date) {

                //如果丢失日期为当天,则丢失时间为当前时间戳,不是当天则为丢失日期当天0点0分
                if (date.equals(hintTime)) {
                    mLostTime = System.currentTimeMillis();
                } else {
                    mLostTime = time;
                }
                isBuildLostSameDay = isTwoTimeStampDayEqual(mLostTime, mBike.getCreatDate());

                mDateView.setDate(date);
            }

            @Override
            public void cancel() {

            }

            @Override
            public void dismiss() {
                mDateView.setDisplay(false);
            }
        });
        mBtnSave.setOnClickListener(this);
        mDateView = findViewByIds(R.id.lbd_date);
        mDateView.setListener(new LossBikeDateView.DateListener() {
            @Override
            public void display() {
                mDateSelectDialog.show();
            }

            @Override
            public void miss() {

            }
        });

        imv_address.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(ApplyLossBikeActivity.this, LostAddrMapActivity.class));
            }
        });

        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy年MM月dd日");
        hintTime = simpleDateFormat.format(new Date(System.currentTimeMillis()));
        mDateView.setHint(hintTime);
//        mLostTime = System.currentTimeMillis();

    }


    @Override
    protected void doSocket() {
        super.doSocket();
        final ApplyBikeLostRequest.ApplyBikeLostRequestMessage.Builder requestMessage =
                ApplyBikeLostRequest.ApplyBikeLostRequestMessage.newBuilder();
        requestMessage.setPlateNumber(mBike.getPlateNumber());
        requestMessage.setAreaId(1);
        requestMessage.setLostTime(mLostTime);
        requestMessage.setLostAddress(mEtAddress.getText().toString());
        requestMessage.setBikeId(mBike.getId());
        BikeUser user = BikeUser.getCurUser(this);
        requestMessage.setUserName(user.getName());
        requestMessage.setSession(PreferenceData.loadSession(this));


        new Thread() {
            public void run() {
                SocketClient client;
                client = SocketClient.getInstance();
                client.send(SocketAppPacket.COMMAND_ID_get_bike_lost, requestMessage.build()
                        .toByteArray());
            }
        }.start();
    }


    @Override
    public void onEventMainThread(EventBusMsg eventPackage) {
        super.onEventMainThread(eventPackage);
        if (eventPackage.getEmptyType().equals(EventBusMsg.MsgEmptyType.MSG_GET_ADDRESS_TEXT)) {
            String tvBundle = eventPackage.getTvBundle();
            mEtAddress.setText(tvBundle);
        }
    }

    @Override
    public void onEventMainThread(SocketAppPacket eventPackage) {
        try {
            super.onEventMainThread(eventPackage);
            if (eventPackage.getCommandId() == SocketAppPacket.COMMAND_ID_get_bike_lost) {
                CommonResponse.CommonResponseMessage responseMessage =
                        CommonResponse.CommonResponseMessage.parseFrom(eventPackage
                                .getCommandData());
                if (dlgWaiting.isShowing()) {
                    dlgWaiting.dismiss();
                }
                mDlgWaitingHandler.removeMessages(MSG_cannt_get_data);
                if (responseMessage.getErrorMsg().getErrorCode() != 0) {
                    ToastUtils.showTextToast(this, responseMessage.getErrorMsg().getErrorMsg());
                    if (responseMessage.getErrorMsg().getErrorCode() == 20003) {
                        Intent intent = new Intent();
                        intent.setClass(this, LoginActivity.class);
                        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                        startActivity(intent);
                    }
                } else {
                    ToastUtils.showTextToast(this, "挂失成功");

                    //刷新
                    EventBusMsg eventBusMsg = new EventBusMsg();
                    eventBusMsg.setMsgType(EventBusMsg.BIKE_STATUS_REFRESH);
                    eventBusMsg.setValue(101);
                    EventBus.getDefault().post(eventBusMsg);

                    this.finish();

                }
            }
        } catch (InvalidProtocolBufferException e1) {
            e1.printStackTrace();
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_save:

                if (mDateView.getDate().equals("")) {
                    ToastUtils.showTextToast(this, "请选择遗失日期");
                    return;
                }


                if (isBuildLostSameDay) {
                    if (isTwoTimeStampDayEqual(mLostTime,System.currentTimeMillis())) {
                        if ((mLostTime < mBike.getCreatDate())) {
                            ToastUtils.showTextToast(this, "遗失日期不能早于上牌日期");
                            return;
                        }
                    }else {
                        if (((mLostTime + 24*3600*1000) < mBike.getCreatDate())) {
                            ToastUtils.showTextToast(this, "遗失日期不能早于上牌日期");
                            return;
                        }
                    }

                } else {
                    if ((mLostTime < mBike.getCreatDate())) {
                        ToastUtils.showTextToast(this, "遗失日期不能早于上牌日期");
                        return;
                    }

                }


                if (mLostTime > System.currentTimeMillis() + 1000) {
                    ToastUtils.showTextToast(this, "请正确填写遗失日期");
                    return;
                }

                if (mEtAddress.getText().toString().isEmpty()) {
                    ToastUtils.showTextToast(this, "请填写地址");
                    return;
                }


                doSocket();
                break;
        }
    }


    //判断两个时间戳是否为同一天
    public static boolean isTwoTimeStampDayEqual(long firstTimeStamp, long secondTimeStamp) {
        if (getYearByTimeStamp(firstTimeStamp) == getYearByTimeStamp(secondTimeStamp) &&
                getMonthByTimeStamp(firstTimeStamp) == getMonthByTimeStamp(secondTimeStamp)
                && getDayByTimeStamp(firstTimeStamp) == getDayByTimeStamp(secondTimeStamp)) {
            return true;
        }
        return false;
    }

    public static String timeStampToDate(long timeStamp) {
        Date date = new Date(timeStamp);
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String dateStr = simpleDateFormat.format(date);
        return dateStr;
    }

    public static int getYearByTimeStamp(long timeStamp) {
        String date = timeStampToDate(timeStamp);
        String year = date.substring(0, 4);
        return Integer.parseInt(year);
    }

    public static int getMonthByTimeStamp(long timeStamp) {
        String date = timeStampToDate(timeStamp);
        String month = date.substring(5, 7);
        return Integer.parseInt(month);
    }

    public static int getDayByTimeStamp(long timeStamp) {
        String date = timeStampToDate(timeStamp);
        String day = date.substring(8, 10);
        return Integer.parseInt(day);
    }

}
