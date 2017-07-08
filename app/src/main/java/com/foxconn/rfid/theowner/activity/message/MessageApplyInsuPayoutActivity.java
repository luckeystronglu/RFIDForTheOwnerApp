package com.foxconn.rfid.theowner.activity.message;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.foxconn.rfid.theowner.R;
import com.foxconn.rfid.theowner.activity.LoginActivity;
import com.foxconn.rfid.theowner.base.BaseActivity;
import com.foxconn.rfid.theowner.socket.SocketAppPacket;
import com.foxconn.rfid.theowner.socket.SocketClient;
import com.google.protobuf.InvalidProtocolBufferException;
import com.yzh.rfidbike.app.request.ApplyBikeInsurancePayoutRequest;
import com.yzh.rfidbike.app.response.GetBikeInsurancePayoutResponse;

/**
 * Created by appadmin on 2016/12/20.
 */

public class MessageApplyInsuPayoutActivity extends BaseActivity {
    private TextView tv_apply_insu_payout;
    private long referenId;
    private LinearLayout ll_payout_warning;
    private TextView tv_reject_reason;
    private ImageButton btn_back_apply_payout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_message_apply_insu_payout);
        tv_apply_insu_payout = (TextView) findViewById(R.id.tv_apply_insu_payout);
        referenId = Long.valueOf(getIntent().getStringExtra("referenId")==null?"0":getIntent().getStringExtra("referenId"));
        ll_payout_warning =(LinearLayout)findViewById(R.id.ll_payout_warning);
        tv_reject_reason = (TextView)findViewById(R.id.tv_reject_reason);
        btn_back_apply_payout = (ImageButton) findViewById(R.id.btn_back_apply_payout);
        doSocket();
        btn_back_apply_payout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }



    @Override
    protected void doSocket() {
        super.doSocket();
        final ApplyBikeInsurancePayoutRequest.ApplyBikeInsurancePayoutRequestMessage.Builder applyMessage = ApplyBikeInsurancePayoutRequest.ApplyBikeInsurancePayoutRequestMessage.newBuilder();
        applyMessage.setId(referenId);
        new Thread() {
            public void run() {
                SocketClient client;
                client = SocketClient.getInstance();
                client.send(SocketAppPacket.COMMANT_ID_GET_BIKE_INSURANCE_PAYOUT_BY_ID, applyMessage.build().toByteArray());
            }
        }.start();
    }

    @Override
    public void onEventMainThread(SocketAppPacket eventPackage) {
        try {
            super.onEventMainThread(eventPackage);

            if (eventPackage.getCommandId() == SocketAppPacket.COMMANT_ID_GET_BIKE_INSURANCE_PAYOUT_BY_ID) {
                GetBikeInsurancePayoutResponse.GetBikeInsurancePayoutResponseMessage applyMessage = GetBikeInsurancePayoutResponse.GetBikeInsurancePayoutResponseMessage.parseFrom(eventPackage.getCommandData());
                if (dlgWaiting.isShowing()) {
                    dlgWaiting.dismiss();
                }
                mDlgWaitingHandler.removeMessages(MSG_cannt_get_data);
                if (applyMessage.getErrorMsg().getErrorCode() != 0) {
                    Toast.makeText(this, applyMessage.getErrorMsg().getErrorMsg(), Toast.LENGTH_LONG).show();
                    if (applyMessage.getErrorMsg().getErrorCode() == 20003) {
                        Intent intent = new Intent();
                        intent.setClass(this, LoginActivity.class);
                        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK|Intent.FLAG_ACTIVITY_CLEAR_TASK);
                        startActivity(intent);
                    }
                } else {
                    String status= applyMessage.getBikeInsurancePayoutMessage(0).getStatus();
                    tv_apply_insu_payout.setText(applyMessage.getBikeInsurancePayoutMessage(0).getApplyRemarks());
                    if(status.equals("2")) {
                        ll_payout_warning.setVisibility(View.VISIBLE);
                        tv_reject_reason.setText(applyMessage.getBikeInsurancePayoutMessage(0).getRejectReason());
                    }else {
                        ll_payout_warning.setVisibility(View.GONE);
                    }
                }
            }

        } catch (InvalidProtocolBufferException e) {
            e.printStackTrace();
        }

    }

}
