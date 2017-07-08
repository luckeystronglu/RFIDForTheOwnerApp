package com.foxconn.rfid.theowner.activity;

import android.app.Notification;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.baidu.android.pushservice.BasicPushNotificationBuilder;
import com.baidu.android.pushservice.PushConstants;
import com.baidu.android.pushservice.PushManager;
import com.foxconn.rfid.theowner.R;
import com.foxconn.rfid.theowner.activity.main.MainNewActivity;
import com.foxconn.rfid.theowner.base.App;
import com.foxconn.rfid.theowner.base.BaseActivity;
import com.foxconn.rfid.theowner.base.BaseApplication;
import com.foxconn.rfid.theowner.base.PreferenceData;
import com.foxconn.rfid.theowner.model.BikeUser;
import com.foxconn.rfid.theowner.socket.SocketAppPacket;
import com.foxconn.rfid.theowner.socket.SocketClient;
import com.foxconn.rfid.theowner.util.BaiduPushUtils;
import com.foxconn.rfid.theowner.util.logort.ToastUtils;
import com.google.protobuf.InvalidProtocolBufferException;
import com.yzh.rfidbike.app.request.LoginRequest;
import com.yzh.rfidbike.app.response.LoginResponse;

import net.tsz.afinal.FinalDb;

import java.util.List;
import java.util.regex.Pattern;

/**
 * Activity which displays a login screen to the user, offering registration as
 * well.
 */
public class LoginActivity extends BaseActivity {

    private String mUserName = "";
    private String mPassword = "";

    // UI references.
    private EditText mUserNameView;
    private EditText mPasswordView;
    private Button btnLogin;

    //
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_login);

        // Set up the login form.
        mUserNameView = (EditText) findViewById(R.id.userName);
        mPasswordView = (EditText) findViewById(R.id.password);
        mUserName = PreferenceData.loadLoginName(this);
        mUserNameView.setText(mUserName);
        mPasswordView.setText(mPassword);

        btnLogin = (Button) findViewById(R.id.btn_login);
        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                attemptLogin();
            }
        });
        //获取百度推送channelId
        initBaiduChannelId();
    }


    /**
     * Attempts to sign in or register the account specified by the login form.
     * If there are form errors (invalid email, missing fields, etc.), the
     * errors are presented and no actual login attempt is made.
     */
    public void attemptLogin() {
        // Reset errors.
//		mUserNameView.setError(null);
//		mPasswordView.setError(null);

        // Store values at the time of the login attempt.
        mUserName = mUserNameView.getText().toString();
        mPassword = mPasswordView.getText().toString();

        boolean cancel = false;
        View focusView = null;

        // Check for a valid email address.
        if (TextUtils.isEmpty(mUserName)) {
            ToastUtils.showTextToast(LoginActivity.this,"用户名不能为空");
//            mUserNameView.setError(getString(R.string.error_field_required));
            focusView = mUserNameView;
            cancel = true;


        } else if (!identity(mUserName)) {
            ToastUtils.showTextToast(LoginActivity.this,"请输入正确的身份证号码");
//			mUserNameView.setError(getString(R.string.error_invalid_username));
			focusView = mUserNameView;
			cancel = true;

		}

        // Check for a valid password.
        if (TextUtils.isEmpty(mPassword)) {
            ToastUtils.showTextToast(LoginActivity.this,"密码不能为空");
            focusView = mPasswordView;
            cancel = true;

        }
		 else if (mPassword.length() < 6) {
            ToastUtils.showTextToast(LoginActivity.this,"密码不能少于6位");
            focusView = mPasswordView;
            cancel = true;
        }



        if (cancel) {
            focusView.requestFocus();
        } else {
            doSocket();
        }
    }

    @Override
    protected void doSocket() {
        super.doSocket();
        final LoginRequest.LoginRequestMessage.Builder requestMessage = LoginRequest.LoginRequestMessage.newBuilder();
        requestMessage.setIdCard(mUserNameView.getText().toString());
        requestMessage.setPassword(mPasswordView.getText().toString());
        requestMessage.setPhoneType(LoginRequest.LoginRequestMessage.PhoneType.AndroidPhone);
        String channelId = BaiduPushUtils.getBaiduPushChannelId(context);
//        channelId="3777229275677183110";
        requestMessage.setPushChannelId(channelId);
        new Thread() {
            public void run() {
                SocketClient client;
                client = SocketClient.getInstance();
                client.send(SocketAppPacket.COMMAND_ID_USER_LOGIIN, requestMessage.build().toByteArray());
            };
        }.start();
    }


    public void onEventMainThread(SocketAppPacket eventPackage) {
        try {
            super.onEventMainThread(eventPackage);
            if (eventPackage.getCommandId() == SocketAppPacket.COMMAND_ID_USER_LOGIIN) {
                LoginResponse.LoginResponseMessage loginResponseMessage = LoginResponse.LoginResponseMessage.parseFrom(eventPackage.getCommandData());
                if (dlgWaiting.isShowing()) {
                    dlgWaiting.dismiss();
                }
                mDlgWaitingHandler.removeMessages(MSG_cannt_get_data);
                if (loginResponseMessage.getErrorMsg().getErrorCode() != 0) {
                    Toast.makeText(LoginActivity.this, loginResponseMessage.getErrorMsg().getErrorMsg(), Toast.LENGTH_LONG).show();
                } else {

                    BikeUser bikeuser = new BikeUser();
                    bikeuser.setUserId(loginResponseMessage.getId());
                    bikeuser.setName(loginResponseMessage.getName());
                    bikeuser.setAddress(loginResponseMessage.getAddress());
                    bikeuser.setAge(loginResponseMessage.getAge());
                    bikeuser.setCompanyId(loginResponseMessage.getCompanyId());
                    bikeuser.setEmail(loginResponseMessage.getEmail());
                    bikeuser.setIdBackPicPath(loginResponseMessage.getIdBackPicPath());
                    bikeuser.setIdCard(loginResponseMessage.getIdCard());
                    bikeuser.setPhone(loginResponseMessage.getPhone());
                    bikeuser.setPhoto(loginResponseMessage.getPhoto());

                    FinalDb mDb = FinalDb.create(getApplicationContext(), App.DB_NAME, true, App.DB_VERSION, BaseApplication.getInstance());




                    //判断是否存在，如存在就修改，不存在直接保存
                    List<BikeUser> list = mDb.findAllByWhere(BikeUser.class, "userId=" + String.valueOf(loginResponseMessage.getId()));
                    if (list.size() > 0) {
                        mDb.update(bikeuser);
                    } else {
                        mDb.save(bikeuser);
                    }
                    //删除先前登录过的用户
                    mDb.deleteByWhere(BikeUser.class, "userId <>" +
                            String
                                    .valueOf(loginResponseMessage.getId()));
                    PreferenceData.saveLoginAccount(this, loginResponseMessage.getId());
                    PreferenceData.saveSession(this, loginResponseMessage.getSession());
                    PreferenceData.saveLoginName(this,loginResponseMessage.getIdCard());
//                    startActivity(new Intent(this, MainActivity.class));
                    startActivity(new Intent(this, MainNewActivity.class));
                }
            }
//			startActivity(new Intent(this,MainActivity.class));
        } catch (InvalidProtocolBufferException e) {
            e.printStackTrace();
        }
    }

    /**
     * 判断是否是身份证，
     *
     * @param str
     * @return
     */
    public static boolean identity(String str) {
        Pattern pattern = Pattern
                .compile("^[1-9][0-9]{5}(19[0-9]{2}|200[0-9]|2010)(0[1-9]|1[0-2])(0[1-9]|[12][0-9]|3[01])[0-9]{3}[0-9xX]$");
        return pattern.matcher(str).matches();
    }

    private void initBaiduChannelId() {
        PushManager.startWork(getApplicationContext(),
                PushConstants.LOGIN_TYPE_API_KEY,
                App.BAIDU_PUSH_API_KEY);

        BasicPushNotificationBuilder basicbuilder = new BasicPushNotificationBuilder();
        basicbuilder.setStatusbarIcon(R.drawable.owner_tras);
        basicbuilder.setNotificationFlags(Notification.FLAG_AUTO_CANCEL);
        PushManager.setDefaultNotificationBuilder(LoginActivity.this,basicbuilder);


    }
}
