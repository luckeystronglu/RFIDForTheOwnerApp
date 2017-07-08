package com.foxconn.rfid.theowner.activity.message;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.foxconn.rfid.theowner.R;
import com.foxconn.rfid.theowner.activity.LoginActivity;
import com.foxconn.rfid.theowner.base.BaseActivity;
import com.foxconn.rfid.theowner.socket.SocketAppPacket;
import com.foxconn.rfid.theowner.socket.SocketClient;
import com.foxconn.rfid.theowner.util.logort.ToastUtils;
import com.foxconn.rfid.theowner.util.string.DateUtils;
import com.foxconn.rfid.theowner.view.LossBikeDateView;
import com.google.protobuf.InvalidProtocolBufferException;
import com.yzh.rfidbike.app.request.GetBikeLostRequest;
import com.yzh.rfidbike.app.response.GetBikeLostResponse;

import java.util.Date;

/**
 * Created by Administrator on 2016/12/20.
 */

public class MessageApplyLossBikeActivity extends BaseActivity implements View.OnClickListener {

    private long mReferenId;
    private LossBikeDateView mDateView;
    private EditText mEtAddress;
    private RelativeLayout mRlError;
    private TextView tv_reason;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_apply_loss_bike_result);
        mDateView = (LossBikeDateView) findViewById(R.id.lbd_date);
        tv_reason = findViewByIds(R.id.tv_reason);
        mEtAddress = (EditText) findViewById(R.id.et_address);
        mRlError = (RelativeLayout) findViewById(R.id.rl_error);

        mReferenId = Long.valueOf(getIntent().getStringExtra("referenId")==null?"0":getIntent().getStringExtra("referenId"));
        doSocket();

    }

    @Override
    public void onClick(View view) {

    }

    @Override
    protected void doSocket() {
        super.doSocket();
        final GetBikeLostRequest.GetBikeLostRequestMessage.Builder requestMessage =
                GetBikeLostRequest.GetBikeLostRequestMessage
                        .newBuilder();
        requestMessage.setId(mReferenId);
        //baidu push id
        new Thread() {
            public void run() {
                SocketClient client;
                client = SocketClient.getInstance();
                client.send(SocketAppPacket.COMMANT_ID_MESSAGE_APPLY_LOSS, requestMessage.build()
                        .toByteArray());
            }


        }.start();
    }

    @Override
    public void onEventMainThread(SocketAppPacket eventPackage) {
        try {
            super.onEventMainThread(eventPackage);
            if (eventPackage.getCommandId() == SocketAppPacket.COMMANT_ID_MESSAGE_APPLY_LOSS) {
                GetBikeLostResponse.GetBikeLostResponseMessage
                        responseMessage = GetBikeLostResponse.GetBikeLostResponseMessage.
                        parseFrom(eventPackage.getCommandData());
                if (dlgWaiting.isShowing()) {
                    dlgWaiting.dismiss();
                }
                mDlgWaitingHandler.removeMessages(MSG_cannt_get_data);
                if (responseMessage.getErrorMsg().getErrorCode() == 0) {
                    long time = responseMessage.getBikeLost(0).getCreateDate();
                    String address = responseMessage.getBikeLost(0).getLostAddress();
                    String status = responseMessage.getBikeLost(0).getStatus();
                    String date = DateUtils.formatDate(new Date(time), "yyyy-MM-dd");
                    mDateView.setDate(date);
                    mEtAddress.setText(address);
                    if (status.equals("2")) {
                        mRlError.setVisibility(View.VISIBLE);
                        tv_reason.setText(responseMessage.getBikeLost(0).getRejectReason());

                    } else{
                        mRlError.setVisibility(View.GONE);
                    }
                } else {
                    ToastUtils.showTextToast(context, responseMessage.getErrorMsg().getErrorMsg());
                    if (responseMessage.getErrorMsg().getErrorCode() == 20003) {
                        Intent intent = new Intent();
                        intent.setClass(this, LoginActivity.class);
                        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK|Intent.FLAG_ACTIVITY_CLEAR_TASK);
                        startActivity(intent);
                    }
                }
            }
        } catch (InvalidProtocolBufferException e1) {
            e1.printStackTrace();
        }
    }
}
