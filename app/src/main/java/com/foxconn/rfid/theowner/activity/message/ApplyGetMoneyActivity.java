/**
 * 
 */
package com.foxconn.rfid.theowner.activity.message;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.foxconn.rfid.theowner.R;
import com.foxconn.rfid.theowner.activity.LoginActivity;
import com.foxconn.rfid.theowner.base.BaseActivity;
import com.foxconn.rfid.theowner.base.PreferenceData;
import com.foxconn.rfid.theowner.model.EventBusMsg;
import com.foxconn.rfid.theowner.socket.SocketAppPacket;
import com.foxconn.rfid.theowner.socket.SocketClient;
import com.foxconn.rfid.theowner.util.logort.ToastUtils;
import com.google.protobuf.InvalidProtocolBufferException;
import com.yzh.rfidbike.app.request.ApplyBikeInsuranceCashRequest;
import com.yzh.rfidbike.app.request.GetBikeInsuranceCashRequest;
import com.yzh.rfidbike.app.response.GetBikeInsuranceCashResponse;

import de.greenrobot.event.EventBus;

/**
 * @author WT00111
 *
 */
public class ApplyGetMoneyActivity extends BaseActivity implements View.OnClickListener {
	private LinearLayout ll_getmoney_place,ll_getmoney_transport;
	private LinearLayout ll_getmoney_transport_content,ll_getmoney_place_content;
	private ImageView iv_getmoney_place,iv_getmoney_transport,btn_getmoney_back;
	private Button btn_confirm_getMoney;
	private TextView tv_get_cash;

	private LinearLayout ll_payout_amount;//提现金额布局(是否显示)
	private LinearLayout ll_big_place,ll_big_tranport;
	private TextView tv_payout_num,tv_getmoney_addr_tint; //提现金额和地址
	private LinearLayout ll_payout_amount_num;
	private TextView tv_payout_amount;

	private long insuranceId = 0;
	private long bikeid = 0;
	private String drawType = "";
	private String plateNumber = "";
	private EditText et_cash_name,et_cash_account,et_cash_bank; //个人汇款时的3个EditText 名字|账号|开户行
	private int getMoneyDispose = 0;


	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_apply_getmoney);
		initView();
	}

	private void initView() {
		Intent in = getIntent();
		bikeid = in.getLongExtra("bikeid",-1);
		insuranceId = in.getLongExtra("insuranceId",-1);
		getMoneyDispose = in.getIntExtra("getMoneyDispose",-1);
		plateNumber = in.getStringExtra("plateNumber");

		tv_get_cash = findViewByIds(R.id.tv_get_cash);

		ll_getmoney_place = findViewByIds(R.id.ll_getmoney_place);
		ll_getmoney_place.setOnClickListener(this);
		ll_getmoney_transport = findViewByIds(R.id.ll_getmoney_transport);
		ll_getmoney_transport.setOnClickListener(this);
		ll_getmoney_transport_content = findViewByIds(R.id.ll_getmoney_transport_content);
		ll_getmoney_place_content = findViewByIds(R.id.ll_getmoney_place_content);
		iv_getmoney_place = findViewByIds(R.id.iv_getmoney_place);
		iv_getmoney_transport = findViewByIds(R.id.iv_getmoney_transport);
		btn_getmoney_back = findViewByIds(R.id.btn_getmoney_back);
		btn_getmoney_back.setOnClickListener(this);

		ll_payout_amount_num = findViewByIds(R.id.ll_payout_amount_num);
		tv_payout_amount = findViewByIds(R.id.tv_payout_amount);

		ll_big_place = findViewByIds(R.id.ll_big_place);
		ll_big_tranport = findViewByIds(R.id.ll_big_tranport);
		btn_confirm_getMoney = findViewByIds(R.id.btn_confirm_getMoney);
		ll_payout_amount = findViewByIds(R.id.ll_payout_amount);
		tv_payout_num = findViewByIds(R.id.tv_payout_num);
		tv_getmoney_addr_tint = findViewByIds(R.id.tv_getmoney_addr_tint);

		et_cash_name = findViewByIds(R.id.et_cash_name);
		et_cash_account = findViewByIds(R.id.et_cash_account);
		et_cash_bank = findViewByIds(R.id.et_cash_bank);


		if (getMoneyDispose == 0) {
			ll_getmoney_transport.performClick();
			btn_confirm_getMoney.setVisibility(View.VISIBLE);
			ll_payout_amount.setVisibility(View.GONE);
			tv_getmoney_addr_tint.setText("点击确认后,可在\"理赔消息\"中查看 \'提现金额\' 和 \'提现地址\'.");
			tv_getmoney_addr_tint.setTextColor(ContextCompat.getColor(context,R.color.textDarkGray));
			ll_payout_amount_num.setVisibility(View.GONE);
		}
		else if (getMoneyDispose == 1) {
			btn_confirm_getMoney.setVisibility(View.GONE);
			getMoneyDisposeResult();

		}
	}


	@Override
	public void onClick(View v) {
		switch (v.getId()) {
			case R.id.ll_getmoney_place:
				ll_getmoney_place_content.setVisibility(View.VISIBLE);
				ll_getmoney_transport_content.setVisibility(View.GONE);
				iv_getmoney_place.setBackgroundResource(R.drawable.ico_xc_click);
				iv_getmoney_transport.setBackgroundResource(R.drawable.ico_xc);
				drawType = "0";
				break;

			case R.id.ll_getmoney_transport:
				ll_getmoney_place_content.setVisibility(View.GONE);
				ll_getmoney_transport_content.setVisibility(View.VISIBLE);
				iv_getmoney_place.setBackgroundResource(R.drawable.ico_xc);
				iv_getmoney_transport.setBackgroundResource(R.drawable.ico_xc_click);
				drawType = "1";
				break;

			case R.id.btn_getmoney_back:
				finish();
				break;
		}
	}

	public void confirmGetCash(View view){
		if (getMoneyDispose == 0) {  //申请提现
			doSocket();
		}

	}

	@Override
	protected void doSocket() {
		super.doSocket();
		final ApplyBikeInsuranceCashRequest.ApplyBikeInsuranceCashRequestMessage.Builder requestMessage
				= ApplyBikeInsuranceCashRequest.ApplyBikeInsuranceCashRequestMessage.newBuilder();
		requestMessage.setBikeId(bikeid);
		requestMessage.setInsuranceId(insuranceId);
		requestMessage.setSession(PreferenceData.loadSession(this));
		requestMessage.setWithdrawType(drawType);
		if (drawType.equals("1")) {
			requestMessage.setName(et_cash_name.getText().toString());
			requestMessage.setWithdrawAccount(et_cash_account.getText().toString());
			requestMessage.setWithdrawBank(et_cash_bank.getText().toString());
		}

		new Thread() {
			public void run() {
				SocketClient client;
				client = SocketClient.getInstance();
				client.send(SocketAppPacket.COMMAND_ID_BIKE_GET_MONEY, requestMessage.build().toByteArray());
			}
		}.start();
	}


	private void getMoneyDisposeResult() {
		super.doSocket();
		final GetBikeInsuranceCashRequest.GetBikeInsuranceCashRequestMessage.Builder requestMe
				= GetBikeInsuranceCashRequest.GetBikeInsuranceCashRequestMessage.newBuilder();

		requestMe.setPlateNumber(plateNumber);
		requestMe.setSession(PreferenceData.loadSession(this));

		new Thread() {
			public void run() {
				SocketClient client;
				client = SocketClient.getInstance();
				client.send(SocketAppPacket.COMMAND_ID_GETMONEY_RESULT, requestMe.build().toByteArray());
			}
		}.start();
	}


	public void onEventMainThread(SocketAppPacket eventPackage) {
		try {
			super.onEventMainThread(eventPackage);
			if (eventPackage.getCommandId() == SocketAppPacket.COMMAND_ID_BIKE_GET_MONEY) {
				GetBikeInsuranceCashResponse.GetBikeInsuranceCashResponseMessage responseMessage = GetBikeInsuranceCashResponse.GetBikeInsuranceCashResponseMessage.parseFrom(eventPackage.getCommandData());
				if (dlgWaiting.isShowing()) {
					dlgWaiting.dismiss();
				}
				mDlgWaitingHandler.removeMessages(MSG_cannt_get_data);
				if (responseMessage.getErrorMsg().getErrorCode() != 0) {
					Toast.makeText(ApplyGetMoneyActivity.this, responseMessage.getErrorMsg().getErrorMsg(), Toast.LENGTH_LONG).show();
					if (responseMessage.getErrorMsg().getErrorCode() == 20003) {
						Intent intent = new Intent();
						intent.setClass(this, LoginActivity.class);
						intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK|Intent.FLAG_ACTIVITY_CLEAR_TASK);
						startActivity(intent);
					}
				} else {
					ToastUtils.showTextToast(context,"已提交成功");

					//提交成功后,发消息通知流程图改状态

					EventBusMsg eventBusMsg = new EventBusMsg();
					eventBusMsg.setMsgType(EventBusMsg.BIKE_STATUS_REFRESH);
					eventBusMsg.setValue(104);
					EventBus.getDefault().post(eventBusMsg);

					finish();

				}
			}
			else if (eventPackage.getCommandId() == SocketAppPacket.COMMAND_ID_GETMONEY_RESULT){
				GetBikeInsuranceCashResponse.GetBikeInsuranceCashResponseMessage responseMessage = GetBikeInsuranceCashResponse.GetBikeInsuranceCashResponseMessage.parseFrom(eventPackage.getCommandData());
				if (dlgWaiting.isShowing()) {
					dlgWaiting.dismiss();
				}
				mDlgWaitingHandler.removeMessages(MSG_cannt_get_data);
				if (responseMessage.getErrorMsg().getErrorCode() != 0) {
					Toast.makeText(ApplyGetMoneyActivity.this, responseMessage.getErrorMsg().getErrorMsg(), Toast.LENGTH_LONG).show();
					if (responseMessage.getErrorMsg().getErrorCode() == 20003) {
						Intent intent = new Intent();
						intent.setClass(this, LoginActivity.class);
						intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK|Intent.FLAG_ACTIVITY_CLEAR_TASK);
						startActivity(intent);
					}
				} else {

					tv_get_cash.setText("理赔结果");

					if (responseMessage.getBikeInsurancePayoutMessageList().get(0).getWithdrawType().equals("0")) {
						ll_payout_amount.setVisibility(View.VISIBLE);

						ll_getmoney_place.setVisibility(View.VISIBLE);
						ll_big_tranport.setVisibility(View.GONE);
						ll_getmoney_place.performClick();

						tv_payout_num.setText("￥ " + responseMessage.getBikeInsurancePayoutMessageList().get(0).getCashPrice()); //获取索赔金额
						tv_getmoney_addr_tint.setTextColor(ContextCompat.getColor(context,R.color.font_light_black));
						tv_getmoney_addr_tint.setText("提现地址: " + responseMessage.getBikeInsurancePayoutMessageList().get(0).getWithdrawAddress());
					}

					else if (responseMessage.getBikeInsurancePayoutMessageList().get(0).getWithdrawType().equals("1")){

						ll_getmoney_transport.setVisibility(View.VISIBLE);
						ll_big_place.setVisibility(View.GONE);
						ll_getmoney_transport.performClick();
						ll_getmoney_transport.setClickable(false);

						ll_payout_amount_num.setVisibility(View.VISIBLE);
						tv_payout_amount.setText("￥ " + responseMessage.getBikeInsurancePayoutMessageList().get(0).getCashPrice());


						et_cash_name.setText(responseMessage.getBikeInsurancePayoutMessageList().get(0).getName());
						et_cash_name.setFocusable(false);
						et_cash_name.setEnabled(false);

						et_cash_account.setText(responseMessage.getBikeInsurancePayoutMessageList().get(0).getWithdrawAccount());
						et_cash_account.setFocusable(false);
						et_cash_account.setEnabled(false);

						et_cash_bank.setText(responseMessage.getBikeInsurancePayoutMessageList().get(0).getWithdrawBank());
						et_cash_bank.setFocusable(false);
						et_cash_bank.setEnabled(false);
					}


				}
			}
		} catch (InvalidProtocolBufferException e) {
			e.printStackTrace();
		}
	}


}
