/**
 * 
 */
package com.foxconn.rfid.theowner.activity.setting;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import com.foxconn.rfid.theowner.R;
import com.foxconn.rfid.theowner.activity.LoginActivity;
import com.foxconn.rfid.theowner.base.App;
import com.foxconn.rfid.theowner.base.BaseActivity;
import com.foxconn.rfid.theowner.base.PreferenceData;
import com.foxconn.rfid.theowner.model.BikeUser;
import com.foxconn.rfid.theowner.socket.SocketAppPacket;
import com.foxconn.rfid.theowner.socket.SocketClient;
import com.foxconn.rfid.theowner.util.logort.ToastUtils;
import com.google.protobuf.InvalidProtocolBufferException;
import com.yzh.rfidbike.app.request.UpdatePasswordRequest;
import com.yzh.rfidbike.app.response.CommonResponse;

import net.tsz.afinal.FinalDb;

import java.util.List;


/**
 * @author WT00111
 *
 */
public class UpdatePasswordActivity extends BaseActivity {

	/* (non-Javadoc)
	 * @see android.view.View.OnClickListener#onClick(android.view.View)
	 */
	private String mUserOldPassword = "";
	private String mUserNewPassword = "";
	private String mUserNewPassword2 = "";

	// UI references.
	private EditText mUserOldpwView;
	private EditText mUserNewpwView;
	private EditText mUserNewpwView2;
	private ImageButton imagebtn;


	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_updatepassword);
		initView();
	}

	/* (non-Javadoc)
	 * @see com.foxconn.rfid.theowner.activity.BaseActivity#initView()
	 */

	protected void initView() {
		mUserOldpwView = (EditText) findViewById(R.id.originalPassword);
		mUserNewpwView = (EditText) findViewById(R.id.newPassWord);
		mUserNewpwView2 = (EditText) findViewById(R.id.confirmPassword);

	}

	public void updatePasswordBack(View v) {
		finish();
	}

	public void onSaveClick(View v) {
		getPassword();
	}



	private void getPassword() {
		mUserOldPassword = mUserOldpwView.getText().toString();
		mUserNewPassword = mUserNewpwView.getText().toString();
		mUserNewPassword2 = mUserNewpwView2.getText().toString();
		boolean cancel = false;
		View focusView = null;
		//判断输入的旧密码是否为空
		if (TextUtils.isEmpty(mUserOldPassword)) {
			ToastUtils.showTextToast(UpdatePasswordActivity.this,"旧密码不能为空");
			focusView = mUserOldpwView;
			cancel = true;
			return;
		}
		//判断输入的新密码是否为空
		if (TextUtils.isEmpty(mUserNewPassword)) {
		//	mUserNewpwView.setError(getString(R.string.error_field_required));
			ToastUtils.showTextToast(UpdatePasswordActivity.this,"新密码不能为空");
			focusView = mUserNewpwView;
			cancel = true;
			return;
		}

		//判断输入的新密码是否为6-12位
		if (mUserNewPassword.length() < 6) {
			ToastUtils.showTextToast(UpdatePasswordActivity.this,"密码位数不能少于6位");
			focusView = mUserNewpwView;
			cancel = true;
			return;
		}

//		if (mUserNewPassword.length() > 12){
//			ToastUtils.showTextToast(UpdatePasswordActivity.this,"密码位数不能超过12位");
//			focusView = mUserNewpwView;
//			cancel = true;
//			return;
//		}

		//判断再次输入的新密码是否为空
		if (TextUtils.isEmpty(mUserNewPassword2)) {
			ToastUtils.showTextToast(UpdatePasswordActivity.this,"确认密码不能为空");
			focusView = mUserNewpwView2;
			cancel = true;
			return;
		}

		if (cancel) {
			focusView.requestFocus();
		} else {
			//判断新密码与确认密码是否一致
			if (mUserNewPassword.equals(mUserNewPassword2)) {
				doSocket();
			}else {
				Toast.makeText(this,"新密码与确认密码不一致",Toast.LENGTH_SHORT).show();
				return;
			}
		}
	}

	@Override
	protected void doSocket() {
		super.doSocket();
		final UpdatePasswordRequest.UpdatePasswordRequestMessage.Builder requestMessage = UpdatePasswordRequest.UpdatePasswordRequestMessage.newBuilder();
		requestMessage.setSession(PreferenceData.loadSession(this));
		requestMessage.setPassword(mUserOldPassword);
		requestMessage.setNewPassword(mUserNewPassword2);
		new Thread() {
			public void run() {
				SocketClient client;
				client = SocketClient.getInstance();
				client.send(SocketAppPacket.COMMAND_ID_UPDATE_PASSWORD, requestMessage.build().toByteArray());
			};
		}.start();
	}

	@Override
	public void onEventMainThread(SocketAppPacket eventPackage) {
		try{
			super.onEventMainThread(eventPackage);
			CommonResponse.CommonResponseMessage changePwMessage = CommonResponse.CommonResponseMessage.parseFrom(eventPackage.getCommandData());

			if(dlgWaiting.isShowing())
			{
				dlgWaiting.dismiss();
			}
			mDlgWaitingHandler.removeMessages(MSG_cannt_get_data);
			if(changePwMessage.getErrorMsg().getErrorCode()!=0){
				Toast.makeText(this, changePwMessage.getErrorMsg().getErrorMsg(),Toast.LENGTH_LONG).show();
				if (changePwMessage.getErrorMsg().getErrorCode() == 20003) {
					Intent intent = new Intent();
					intent.setClass(this, LoginActivity.class);
					intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK|Intent.FLAG_ACTIVITY_CLEAR_TASK);
					startActivity(intent);
				}

			}else {


				final FinalDb mDb= FinalDb.create(this, App.DB_NAME, true,App.DB_VERSION,this );
				//判断是否存在，如存在就修改，不存在直接保存
				List<BikeUser> list= mDb.findAllByWhere(BikeUser.class,"userId="+String.valueOf(PreferenceData.loadLoginAccount(this)));

				BikeUser bikeuser=list.get(0);

				mDb.update(bikeuser);

				Toast.makeText(this,"密码修改成功",Toast.LENGTH_SHORT).show();
				finish();


			}

		} catch (InvalidProtocolBufferException e) {
			e.printStackTrace();
		}


	}





}
