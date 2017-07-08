/**
 * 
 */
package com.foxconn.rfid.theowner.activity.message;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.foxconn.rfid.theowner.R;
import com.foxconn.rfid.theowner.activity.LoginActivity;
import com.foxconn.rfid.theowner.base.BaseActivity;
import com.foxconn.rfid.theowner.socket.SocketAppPacket;
import com.foxconn.rfid.theowner.socket.SocketClient;
import com.google.protobuf.InvalidProtocolBufferException;
import com.yzh.rfidbike.app.request.ApplyBikeInsuranceCashRequest;
import com.yzh.rfidbike.app.response.GetBikeInsuranceCashResponse;

/**
 * @author WT00111
 *
 */
public class MessageApplyCashActivity extends BaseActivity {
	private ImageButton cashBtnBack,ivSpotCash,ivPersonalCash;
	private RelativeLayout rl_personal_cash,rl_spot_cash;
    private LinearLayout ll_cash_warning;
	private RelativeLayout ll_sport_cash,ll_personal_cash;
	private Button bt_spot_cash;
	private long referenId;
	private TextView tv_name,tv_bank_account,tv_bank;
    private TextView tv_reject_reason;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_message_apply_cash);
		cashBtnBack = (ImageButton) findViewById(R.id.cash_btn_back);
		ivSpotCash = (ImageButton) findViewById(R.id.iv_spot_cash);
		ivPersonalCash = (ImageButton) findViewById(R.id.iv_personal_cash);
		rl_personal_cash = (RelativeLayout) findViewById(R.id.rl_personal_cash) ;
		rl_spot_cash = (RelativeLayout)findViewById(R.id.rl_spot_cash);
		ll_sport_cash = (RelativeLayout)findViewById(R.id.ll_spot_cash);
		ll_personal_cash = (RelativeLayout)findViewById(R.id.ll_personal_cash);
		bt_spot_cash = (Button) findViewById(R.id.bt_spot_cash);
		tv_name = findViewByIds(R.id.tv_name);
		tv_bank_account = findViewByIds(R.id.tv_bank_account);
		tv_bank = findViewByIds(R.id.tv_bank);
		rl_spot_cash.setVisibility(View.GONE);
		referenId = Long.valueOf(getIntent().getStringExtra("referenId")==null?"0":getIntent().getStringExtra("referenId"));
        ll_cash_warning =(LinearLayout)findViewById(R.id.ll_cash_warning);
        tv_reject_reason = (TextView)findViewById(R.id.tv_reject_reason);
        doSocket();
		cashBtnBack.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {
				finish();
			}
		});

	}

	@Override
	protected void doSocket() {
		super.doSocket();
		final ApplyBikeInsuranceCashRequest.ApplyBikeInsuranceCashRequestMessage.Builder requestMessage = ApplyBikeInsuranceCashRequest.ApplyBikeInsuranceCashRequestMessage.newBuilder();
//        requestMessage.setBikeId(mBike.getId());
//        requestMessage.setInsuranceId(Long.valueOf(mBike.getInsurance()));
//        requestMessage.setSession(PreferenceData.loadSession(this));
//        requestMessage.setWithdrawType(mWithDrawType);
//        requestMessage.setWithdrawAccount(et_bank_account.getText().toString());
//        requestMessage.setWithdrawBank(et_bank.getText().toString());
//        requestMessage.setName(et_name.getText().toString());
        requestMessage.setId(referenId);
		new Thread() {
			public void run() {
				SocketClient client;
				client = SocketClient.getInstance();
				client.send(SocketAppPacket.COMMANT_ID_GET_BIKE_INSURANCE_CASH_BY_ID, requestMessage.build().toByteArray());
			}
		}.start();

	}


	public void onEventMainThread(SocketAppPacket eventPackage) {
		try {
			super.onEventMainThread(eventPackage);
			if (eventPackage.getCommandId() == SocketAppPacket.COMMANT_ID_GET_BIKE_INSURANCE_CASH_BY_ID) {
				GetBikeInsuranceCashResponse.GetBikeInsuranceCashResponseMessage responseMessage = GetBikeInsuranceCashResponse.GetBikeInsuranceCashResponseMessage.parseFrom(eventPackage.getCommandData());
				if (dlgWaiting.isShowing()) {
					dlgWaiting.dismiss();
				}
				mDlgWaitingHandler.removeMessages(MSG_cannt_get_data);
				if (responseMessage.getErrorMsg().getErrorCode() != 0) {
					Toast.makeText(MessageApplyCashActivity.this, responseMessage.getErrorMsg().getErrorMsg(), Toast.LENGTH_LONG).show();
					if (responseMessage.getErrorMsg().getErrorCode() == 20003) {
						Intent intent = new Intent();
						intent.setClass(this, LoginActivity.class);
						intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK|Intent.FLAG_ACTIVITY_CLEAR_TASK);
						startActivity(intent);
					}
				} else {
                  String  mWithDrawType=responseMessage.getBikeInsurancePayoutMessageList().get(0).getWithdrawType();
                    if(mWithDrawType.equals("1")){
                        ivSpotCash.setBackgroundResource(R.drawable.ico_xc_click);
                        ivPersonalCash.setBackgroundResource(R.drawable.ico_xc);
                        rl_personal_cash.setVisibility(View.GONE);
                        rl_spot_cash.setVisibility(View.VISIBLE);
                        tv_name.setText(responseMessage.getBikeInsurancePayoutMessageList().get(0).getName());
                        tv_bank.setText(responseMessage.getBikeInsurancePayoutMessageList().get(0).getWithdrawBank());
                        tv_bank_account.setText(responseMessage.getBikeInsurancePayoutMessageList().get(0).getWithdrawAccount());
                    }else{
                        ivPersonalCash.setBackgroundResource(R.drawable.ico_xc_click);
                        ivSpotCash.setBackgroundResource(R.drawable.ico_xc);
                        rl_spot_cash.setVisibility(View.GONE);
                        rl_personal_cash.setVisibility(View.VISIBLE);
                    }
                    String status= responseMessage.getBikeInsurancePayoutMessage(0).getStatus();
                    if(status.equals("1")){
                        ll_cash_warning.setVisibility(View.VISIBLE);
                        tv_reject_reason.setText(responseMessage.getBikeInsurancePayoutMessage(0).getRejectReason());
                    }
				}
			}
//			startActivity(new Intent(this,MainActivity.class));
		} catch (InvalidProtocolBufferException e) {
			e.printStackTrace();
		}
	}

}
