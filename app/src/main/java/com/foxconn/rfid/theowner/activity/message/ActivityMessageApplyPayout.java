package com.foxconn.rfid.theowner.activity.message;

import android.os.Bundle;

import com.foxconn.rfid.theowner.R;
import com.foxconn.rfid.theowner.base.BaseActivity;

/**
 * Created by appadmin on 2016/12/27.
 */

public class ActivityMessageApplyPayout extends BaseActivity {

//    private Button fix_apply_btn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_msg_apply_payout);
//        initView();
    }

//    private void initView() {
//        fix_apply_btn = (Button) findViewById(fix_apply_btn);
//        fix_apply_btn.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
////                doSocket();
//            }
//        });
//    }


//    @Override
//    protected void doSocket() {
//        super.doSocket();
//        final ApplyBikeInsurancePayoutRequest.ApplyBikeInsurancePayoutRequestMessage.Builder applyPayoutMessage = ApplyBikeInsurancePayoutRequest.ApplyBikeInsurancePayoutRequestMessage.newBuilder();
//        BikeUser user = BikeUser.getCurUser(this);
//        //        applyPayoutMessage.setInsuranceId(Long.parseLong(bike.getInsurance()));
//        applyPayoutMessage.setSession(PreferenceData.loadSession(this));
//
//        new Thread() {
//            public void run() {
//                SocketClient client;
//                client = SocketClient.getInstance();
//                client.send(SocketAppPacket.COMMANT_ID_MESSAGE_INSURANCE_APPLY_PAYOUT, applyPayoutMessage.build().toByteArray());
//            }
//        }.start();
//    }
//
//
//    @Override
//    public void onEventMainThread(SocketAppPacket eventPackage) {
//        try {
//            super.onEventMainThread(eventPackage);
//            if (eventPackage.getCommandId() == SocketAppPacket.COMMANT_ID_MESSAGE_INSURANCE_APPLY_PAYOUT) {
//                GetBikeInsurancePayoutResponse.GetBikeInsurancePayoutResponseMessage getPayoutMessage = GetBikeInsurancePayoutResponse.GetBikeInsurancePayoutResponseMessage.parseFrom(eventPackage.getCommandData());
//
//                if (dlgWaiting.isShowing()) {
//                    dlgWaiting.dismiss();
//                }
//                mDlgWaitingHandler.removeMessages(MSG_cannt_get_data);
//                if (getPayoutMessage.getErrorMsg().getErrorCode() != 0) {
//                    Toast.makeText(this, getPayoutMessage.getErrorMsg().getErrorMsg(), Toast.LENGTH_LONG).show();
//                } else {
//                    //点击申请索赔消息- 修改信息 成功
//
//
//                    finish();
//                }
//            }
//
//        } catch (InvalidProtocolBufferException e) {
//            e.printStackTrace();
//        }
//
//    }




}
