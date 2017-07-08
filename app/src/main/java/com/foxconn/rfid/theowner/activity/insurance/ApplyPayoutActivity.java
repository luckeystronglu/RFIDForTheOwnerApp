package com.foxconn.rfid.theowner.activity.insurance;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.foxconn.rfid.theowner.R;
import com.foxconn.rfid.theowner.activity.LoginActivity;
import com.foxconn.rfid.theowner.base.BaseActivity;
import com.foxconn.rfid.theowner.base.PreferenceData;
import com.foxconn.rfid.theowner.model.Bike;
import com.foxconn.rfid.theowner.model.EventBusMsg;
import com.foxconn.rfid.theowner.socket.SocketAppPacket;
import com.foxconn.rfid.theowner.socket.SocketClient;
import com.foxconn.rfid.theowner.util.logort.ToastUtils;
import com.google.protobuf.InvalidProtocolBufferException;
import com.yzh.rfidbike.app.request.ApplyBikeInsurancePayoutRequest;
import com.yzh.rfidbike.app.response.CommonResponse;
import com.yzh.rfidbike.app.response.GetBikeInsurancePayoutResponse;

import de.greenrobot.event.EventBus;

/**
 * Created by appadmin on 2016/12/20.
 * 申请索赔
 */

public class ApplyPayoutActivity extends BaseActivity implements View.OnClickListener {
    private EditText et_apply;
    private ImageButton btn_back_apply_payout;
    private TextView tv_apply_payout;
    private Button comfirmBtn;
    private String applyContent = "";
    private long insuranceId;
    private Bike bike;

    private LinearLayout ll_payout_warning;
    private TextView tv_reject_reason;

    private int applyPayout = 0;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_apply_insu_payout);
        initView();
    }

    private void initView() {
        Intent in = getIntent();
        bike = (Bike) in.getSerializableExtra("BIKE");//获取Bike对象
        insuranceId = in.getLongExtra("insuranceId", -1);
        applyPayout = in.getIntExtra("applyPayout", 0);

        et_apply = (EditText) findViewById(R.id.et_apply_insu_payout);
        btn_back_apply_payout = (ImageButton) findViewById(R.id.btn_back_apply_payout);
        btn_back_apply_payout.setOnClickListener(this);
        comfirmBtn = (Button) findViewById(R.id.confirm_apply_btn);
        comfirmBtn.setOnClickListener(this);
        ll_payout_warning = findViewByIds(R.id.ll_payout_warning);
        tv_reject_reason = findViewByIds(R.id.tv_reject_reason);
        tv_apply_payout = findViewByIds(R.id.tv_apply_payout);

        if (applyPayout == 0) {
            comfirmBtn.setVisibility(View.VISIBLE);
            comfirmBtn.setOnClickListener(this);
            tv_apply_payout.setText("申请索赔");
        } else if (applyPayout == 1) {
            comfirmBtn.setVisibility(View.GONE);
            tv_apply_payout.setText("索赔审核中");
            doCheckPayout();
        } else if (applyPayout == 2) {
            comfirmBtn.setVisibility(View.VISIBLE);
            tv_apply_payout.setText("索赔结果");
            doCheckPayout();
        }
    }


    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btn_back_apply_payout:
                finish(); //返回键监听
                break;
            case R.id.confirm_apply_btn://确认键监听
                if (applyPayout == 0) {
                    applyContent = et_apply.getText().toString().trim();//获取EditText文字,并去除空格
                    boolean cancel = false;
                    View focusView = null;
                    //判断备注信息填写是否为空
                    if (TextUtils.isEmpty(applyContent)) {
                        ToastUtils.showTextToast(context, "请认真填写备注信息");
                        focusView = et_apply;
                        cancel = true;
                    }
                    if (cancel) {
                        focusView.requestFocus();
                    } else {
                        doSocket();
                    }
                }else if (applyPayout == 2){

                    ll_payout_warning.setVisibility(View.GONE);
                    et_apply.setText("");
                    et_apply.setHint(getResources().getString(R.string.apply_insu_payout_remarks));
                    et_apply.setHintTextColor(ContextCompat.getColor(context, R.color.textDarkGray));

                    et_apply.setFocusable(true);
                    et_apply.setFocusableInTouchMode(true);
                    et_apply.requestFocus();
                    et_apply.findFocus();

                    et_apply.setEnabled(true);
                    et_apply.setClickable(true);
                    et_apply.setSelection(0);

                    comfirmBtn.setText("确认索赔");
                    tv_apply_payout.setText("申请索赔");
                    applyPayout = 0;


                    //刷新
                    EventBusMsg eventBusMsg = new EventBusMsg();
                    eventBusMsg.setMsgType(EventBusMsg.BIKE_STATUS_REFRESH);
                    eventBusMsg.setValue(103);
                    EventBus.getDefault().post(eventBusMsg);


                }

                break;
            default:
                break;
        }
    }

    @Override
    protected void doSocket() {
        super.doSocket();
        final ApplyBikeInsurancePayoutRequest.ApplyBikeInsurancePayoutRequestMessage.Builder applyMessage = ApplyBikeInsurancePayoutRequest.ApplyBikeInsurancePayoutRequestMessage.newBuilder();
//        BikeUser user = BikeUser.getCurUser(this);
        applyMessage.setBikeId(bike.getId());
        applyMessage.setInsuranceId(insuranceId);//车辆投保ID
        applyMessage.setSession(PreferenceData.loadSession(this));
        applyMessage.setApplyRemarks(et_apply.getText().toString());
        new Thread() {
            public void run() {
                SocketClient client;
                client = SocketClient.getInstance();
                client.send(SocketAppPacket.COMMAND_ID_APPLY_INSURANCE_PAYOUT, applyMessage.build().toByteArray());
            }
        }.start();
    }

    //查看申请索赔状态
    private void doCheckPayout() {
        super.doSocket();
        final ApplyBikeInsurancePayoutRequest.ApplyBikeInsurancePayoutRequestMessage.Builder applyMessage = ApplyBikeInsurancePayoutRequest.ApplyBikeInsurancePayoutRequestMessage.newBuilder();
//        applyMessage.setId(insuranceId);
        applyMessage.setInsuranceId(insuranceId);
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
            if (eventPackage.getCommandId() == SocketAppPacket.COMMAND_ID_APPLY_INSURANCE_PAYOUT) {
                CommonResponse.CommonResponseMessage applyMessage = CommonResponse.CommonResponseMessage.parseFrom(eventPackage.getCommandData());

                if (dlgWaiting.isShowing()) {
                    dlgWaiting.dismiss();
                }
                mDlgWaitingHandler.removeMessages(MSG_cannt_get_data);
                if (applyMessage.getErrorMsg().getErrorCode() != 0) {
                    Toast.makeText(this, applyMessage.getErrorMsg().getErrorMsg(), Toast.LENGTH_LONG).show();
                    if (applyMessage.getErrorMsg().getErrorCode() == 20003) {
                        Intent intent = new Intent();
                        intent.setClass(this, LoginActivity.class);
                        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                        startActivity(intent);
                    }
                } else {
                    Toast.makeText(this, getResources().getString(R.string.payout_apply_have_send), Toast.LENGTH_LONG).show();


                    //刷新
                    EventBusMsg eventBusMsg = new EventBusMsg();
                    eventBusMsg.setMsgType(EventBusMsg.BIKE_STATUS_REFRESH);
                    eventBusMsg.setValue(102);
                    EventBus.getDefault().post(eventBusMsg);


                    finish();
                }
            } else if (eventPackage.getCommandId() == SocketAppPacket.COMMANT_ID_GET_BIKE_INSURANCE_PAYOUT_BY_ID) {
                GetBikeInsurancePayoutResponse.GetBikeInsurancePayoutResponseMessage checkApplyMessage
                        = GetBikeInsurancePayoutResponse.GetBikeInsurancePayoutResponseMessage.parseFrom(eventPackage.getCommandData());
                if (dlgWaiting.isShowing()) {
                    dlgWaiting.dismiss();
                }
                mDlgWaitingHandler.removeMessages(MSG_cannt_get_data);
                if (checkApplyMessage.getErrorMsg().getErrorCode() != 0) {
                    Toast.makeText(this, checkApplyMessage.getErrorMsg().getErrorMsg(), Toast.LENGTH_LONG).show();
                    if (checkApplyMessage.getErrorMsg().getErrorCode() == 20003) {
                        Intent intent = new Intent();
                        intent.setClass(this, LoginActivity.class);
                        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                        startActivity(intent);
                    }
                } else {
                    et_apply.setText(checkApplyMessage.getBikeInsurancePayoutMessage(0).getApplyRemarks());
                    et_apply.setTextColor(ContextCompat.getColor(context, R.color.textDarkGray));
                    et_apply.setFocusable(false);
//                    et_apply.setEnabled(false);
                    String status = checkApplyMessage.getBikeInsurancePayoutMessage(0).getStatus();
                    if (status.equals("2")) {
                        applyPayout = 2;
                        tv_apply_payout.setText("索赔结果");
                        ll_payout_warning.setVisibility(View.VISIBLE);
                        tv_reject_reason.setText(checkApplyMessage.getBikeInsurancePayoutMessage(0).getRejectReason());

                        comfirmBtn.setVisibility(View.VISIBLE);
                        comfirmBtn.setText("重新申请索赔");

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
