/**
 *
 */
package com.foxconn.rfid.theowner.activity;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.androidquery.AQuery;
import com.androidquery.callback.AjaxCallback;
import com.androidquery.callback.AjaxStatus;
import com.customview.switchbutton.SwitchButton;
import com.foxconn.rfid.theowner.R;
import com.foxconn.rfid.theowner.activity.message.ActivityMessageCenter;
import com.foxconn.rfid.theowner.activity.setting.PersonalInfoActivity;
import com.foxconn.rfid.theowner.activity.setting.UpdatePasswordActivity;
import com.foxconn.rfid.theowner.base.App;
import com.foxconn.rfid.theowner.base.BaseActivity;
import com.foxconn.rfid.theowner.base.BaseApplication;
import com.foxconn.rfid.theowner.base.PreferenceData;
import com.foxconn.rfid.theowner.model.Bike;
import com.foxconn.rfid.theowner.model.EventBusMsg;
import com.foxconn.rfid.theowner.socket.SocketAppPacket;
import com.foxconn.rfid.theowner.socket.SocketClient;
import com.foxconn.rfid.theowner.util.ToolsUtils;
import com.foxconn.rfid.theowner.util.file.FileUtils;
import com.foxconn.rfid.theowner.util.logort.ToastUtils;
import com.yzh.rfidbike.app.request.CheckUpdateRequest;
import com.yzh.rfidbike.app.request.ConfigBikeProtectRequest;
import com.yzh.rfidbike.app.request.LogoutRequest;
import com.yzh.rfidbike.app.request.PlatformTypes;
import com.yzh.rfidbike.app.response.CheckUpdateResponse;
import com.yzh.rfidbike.app.response.CommonResponse;

import java.io.File;

import de.greenrobot.event.EventBus;


/**
 * @author WT00111
 */
public class SettingActivity extends BaseActivity implements View.OnClickListener {
    //	private ListView listView;
    private ImageButton imageButton;
    private RelativeLayout rl_personal_info, rl_modify_password;
    private RelativeLayout rl_msg_center, rl_version_update, rl_cancel_login;
    private TextView tv_app_version;
    private SwitchButton safety_swb;
    private ImageView iv_safety_right;
    private Bike bike;
    private long bikeId; //车辆ID

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);
        initView();
    }

    protected void initView() {
        Intent it = getIntent();

        if (it.getSerializableExtra("BIKE") != null) {
            bike = (Bike)it.getSerializableExtra("BIKE");
        }

        bikeId = getIntent().getLongExtra("bikeId",-1);

        imageButton = (ImageButton) findViewById(R.id.setting_btn_back);
        imageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                EventBusMsg eventBusMsg = new EventBusMsg();
//                eventBusMsg.setMsgType(EventBusMsg.MSG_REFRESH_BIKE_STATUS);
//                EventBus.getDefault().post(eventBusMsg);
                finish();
            }
        });
        rl_personal_info = findViewByIds(R.id.rl_personal_info);
        rl_modify_password = findViewByIds(R.id.rl_modify_password);
        rl_msg_center = findViewByIds(R.id.rl_msg_center);
        rl_version_update = findViewByIds(R.id.rl_version_update);
        rl_cancel_login = findViewByIds(R.id.rl_cancel_login);

        rl_personal_info.setOnClickListener(this);
        rl_modify_password.setOnClickListener(this);
        rl_msg_center.setOnClickListener(this);
        rl_version_update.setOnClickListener(this);
        rl_cancel_login.setOnClickListener(this);

        tv_app_version = findViewByIds(R.id.tv_app_version);
        tv_app_version.setText(ToolsUtils.getAppVersionName(this));

        safety_swb = findViewByIds(R.id.switch_safety_swb);
        iv_safety_right = findViewByIds(R.id.iv_safety_right);


        if (bike != null) {
            if (bike.protestValue.equals("0")) {
                safety_swb.setChecked(false);
            }else if (bike.protestValue.equals("1")){
                safety_swb.setChecked(true);
            }

            safety_swb.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                    if (isChecked) {
                        bike.protestValue = "1";
                        getBikeConfigProtect();

                        ToastUtils.showTextToast(context,"车辆安防已打开");
                    }else {
                        bike.protestValue = "0";
                        getBikeConfigProtect();
                        ToastUtils.showTextToast(context,"车辆安防已关闭");
                    }
                }
            });

        }else {
            safety_swb.setVisibility(View.GONE);
            iv_safety_right.setVisibility(View.VISIBLE);
        }



    }

    private void getBikeConfigProtect() {
        if(bike == null){
            return; //对象为空,不再请求
        }

        super.doSocket();
        final ConfigBikeProtectRequest.ConfigBikeProtectRequestMessage.Builder getConfigRequest
                = ConfigBikeProtectRequest.ConfigBikeProtectRequestMessage.newBuilder();
        getConfigRequest.setBikeId(bike.getId());
        getConfigRequest.setSession(PreferenceData.loadSession(this));

        new Thread() {
            public void run() {
                SocketClient client;
                client = SocketClient.getInstance();
                client.send(SocketAppPacket.COMMAND_ID_OPEN_CLOSE_SAFETY_MSG, getConfigRequest.build().toByteArray());
            }
        }.start();
    }



    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.rl_personal_info:
                startActivity(new Intent(SettingActivity.this, PersonalInfoActivity.class));
                break;

            case R.id.rl_modify_password:
                startActivity(new Intent(SettingActivity.this, UpdatePasswordActivity.class));
                break;


            case R.id.rl_msg_center:
                startActivity(new Intent(SettingActivity.this, ActivityMessageCenter.class));
                break;

            case R.id.rl_version_update:
                dlgWaiting.show();
                mHandler.sendEmptyMessageDelayed(MSG_cannt_get_data, 10 * 1000);
                new Thread(new Runnable() {

                    @Override
                    public void run() {
                        // TODO Auto-generated method stub
                        SocketClient client = SocketClient.getInstance();
                        CheckUpdateRequest.CheckUpdateRequestMessage.Builder requestMessage = CheckUpdateRequest.CheckUpdateRequestMessage.newBuilder();
                        requestMessage.setAppVersion(ToolsUtils
                                .getAppVersionName(SettingActivity.this.getApplicationContext()));
                        requestMessage.setPlatformType(PlatformTypes.PlatformType.AndroidPhone);
                        client.send(SocketAppPacket.COMMAND_ID_GET_APP_VERSION, requestMessage.build().toByteArray());
                    }
                }).start();
                break;

            case R.id.rl_cancel_login:
                //退出登陆
                doLoginOutSocket();
                break;
        }
    }

    private void doLoginOutSocket() {
        doSocket();
        final LogoutRequest.LogoutRequestMessage.Builder logoutbuilder = LogoutRequest.LogoutRequestMessage.newBuilder();
        logoutbuilder.setUserId(PreferenceData.loadLoginAccount(this));

        new Thread() {
            public void run() {
                SocketClient client;
                client = SocketClient.getInstance();
                client.send(SocketAppPacket.COMMAND_ID_LOGIINOUT, logoutbuilder.build().toByteArray());
            }

        }.start();
    }


    @Override
    public void onEventMainThread(SocketAppPacket eventPackage) {
        try {

            if (eventPackage.getCommandId() == SocketAppPacket.COMMAND_ID_GET_APP_VERSION) {
                mHandler.removeMessages(MSG_cannt_get_data);
                if (dlgWaiting.isShowing()) {
                    dlgWaiting.dismiss();
                }
                if (BaseApplication.getInstance().isChangeLanguage) {
                    return;
                }
                final CheckUpdateResponse.CheckUpdateResponseMessage responseMessage;

                responseMessage = CheckUpdateResponse.CheckUpdateResponseMessage.parseFrom(eventPackage.getCommandData());

                if (responseMessage.getErrorMsg().getErrorCode() == 0) {
                    if (responseMessage.getVersion().length() > 0) {
                        tv_app_version.setText(responseMessage.getVersion()); //更新版本信息
                        LayoutInflater inflater = this.getLayoutInflater();
                        View layout = inflater.inflate(R.layout.dlg_version_update, null);
                        final Dialog dlgVersionUpdate = new AlertDialog.Builder(this).setView(layout).create();
                        dlgVersionUpdate.show();

                        TextView tv_cancel = (TextView) dlgVersionUpdate.findViewById(R.id.tv_cancel);
                        TextView tv_sure = (TextView) dlgVersionUpdate.findViewById(R.id.tv_sure);
                        TextView tv_content = (TextView) dlgVersionUpdate.findViewById(R.id.tv_content);
                        tv_content.setText(this.getResources().getString(R.string.last_version) + responseMessage.getVersion() + "\n" + responseMessage.getUpdateLog());
                        tv_cancel.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                dlgVersionUpdate.dismiss();
                            }
                        });
                        tv_sure.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                dlgVersionUpdate.dismiss();
                                downloadFile(
                                        responseMessage.getUpdateURL(),
                                        getString(R.string.app_name),
                                        getString(R.string.home_dlg_download_title));
                            }
                        });


                    } else {
                        ToastUtils.showTextToast(context, SettingActivity.this.getResources().getString(R.string.lastest_version), 2000);
                    }
                }

            }

            //退出登陆
            if (eventPackage.getCommandId() == SocketAppPacket.COMMAND_ID_LOGIINOUT) {
                CommonResponse.CommonResponseMessage commonResponseMessage = CommonResponse.CommonResponseMessage.parseFrom(eventPackage.getCommandData());
                if (commonResponseMessage.getErrorMsg().getErrorCode() != 0) {
                    Toast.makeText(SettingActivity.this, commonResponseMessage.getErrorMsg()
                            .getErrorMsg(), Toast.LENGTH_LONG).show();
                    if (commonResponseMessage.getErrorMsg().getErrorCode() == 20003) {
                        Intent intent = new Intent();
                        intent.setClass(this, LoginActivity.class);
                        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                        startActivity(intent);
                    }
                } else {
                    //退出登陆success
//					Toast.makeText(SettingActivity.this, "退出登陆success", Toast
//							.LENGTH_SHORT).show();
                    //解绑百度推送
//					PushManager.stopWork(SettingActivity.this);

                    PreferenceData.saveLoginAccount(SettingActivity.this, -1);

                    Intent in = new Intent(SettingActivity.this, LoginActivity.class);
                    in.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                    startActivity(in);

                }
            }
            if (eventPackage.getCommandId() == SocketAppPacket.COMMAND_ID_OPEN_CLOSE_SAFETY_MSG) {
                mHandler.removeMessages(MSG_cannt_get_data);
                if (dlgWaiting.isShowing()) {
                    dlgWaiting.dismiss();
                }
                CommonResponse.CommonResponseMessage commonResponseMessage
                        = CommonResponse.CommonResponseMessage.parseFrom(eventPackage.getCommandData());

                if (commonResponseMessage.getErrorMsg().getErrorCode() != 0) {
                    Toast.makeText(SettingActivity.this, commonResponseMessage.getErrorMsg()
                            .getErrorMsg(), Toast.LENGTH_LONG).show();
                    if (commonResponseMessage.getErrorMsg().getErrorCode() == 20003) {
                        Intent intent = new Intent();
                        intent.setClass(this, LoginActivity.class);
                        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                        startActivity(intent);
                    }
                } else {
                    if (bike.protestValue.equals("0")) {
                        safety_swb.setChecked(false);

                    }else if (bike.protestValue.equals("1")){
                        safety_swb.setChecked(true);
                    }


//                    boolean isPushOk = PushManager.isPushEnabled(getApplicationContext());
//                    if (bike.protestValue.equals("0")) {
//                        safety_swb.setChecked(false);
//
//                        if (isPushOk) {
//                            PushManager.stopWork(SettingActivity.this);
//                        }
//
//
//                    }else if (bike.protestValue.equals("1")){
//                        safety_swb.setChecked(true);
//                        if (!isPushOk) {
//                            PushManager.resumeWork(getApplicationContext());
//                        }
//                    }
                }
            }


        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    /**
     * APK文件下载
     *
     * @param url
     * @param fileName
     */
    private void downloadFile(String url, String fileName, String title) {
        // 创建ProgressDialog对象
        final ProgressDialog dlg = new ProgressDialog(this);
        // 设置进度条风格，风格为圆形，旋转的
        dlg.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
        // 设置ProgressDialog 标题
        // dlg.setTitle(R.string.home_dlg_download_title);
        dlg.setTitle(title);
        dlg.setIcon(R.drawable.icon_app);
        dlg.setMax(100);
        // 设置ProgressDialog 的进度条是否不明确 false 就是不设置为不明确
        dlg.setIndeterminate(false);
        // 设置ProgressDialog 是否可以按退回键取消
        dlg.setCancelable(false);
        dlg.setCanceledOnTouchOutside(false);

        File target = null;
        if (FileUtils.isSDCardMounted()) {
            target = Environment
                    .getExternalStoragePublicDirectory(App.IMAGE_CACHE_PATH
                            + fileName + ".apk");
            // target = new File (App.IMAGE_CACHE_PATH + fileName + ".apk");
        }
        AQuery homeAq = new AQuery(this);
        homeAq.progress(dlg).download(url, target, new AjaxCallback<File>() {
            public void callback(String url, File file, AjaxStatus status) {
                if (status.getCode() == 200 && file != null) {
                    logMessage("Http-->File:" + file.length() + ":" + file);
                    Uri uri = Uri.fromFile(file);
                    Intent intent = new Intent();
                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    intent.setAction(android.content.Intent.ACTION_VIEW);
                    intent.setDataAndType(uri,
                            "application/vnd.android.package-archive");
                    startActivity(intent);
                } else {
                    logMessage("Http-->Failed");
                    Toast.makeText(SettingActivity.this,
                            R.string.home_dlg_download_update_fail,
                            Toast.LENGTH_SHORT).show();
                }
                dlg.cancel();
            }
        });
        // 让ProgressDialog显示
        dlg.show();

    }

    private Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            try {
                switch (msg.what) {
                    case MSG_cannt_get_data:
                        if (dlgWaiting.isShowing()) {
                            dlgWaiting.dismiss();
                            Toast.makeText(SettingActivity.this, R.string.network_error, Toast.LENGTH_SHORT).show();
                        }
                        break;
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    };


    @Override
    protected void onDestroy() {
        super.onDestroy();

        EventBusMsg evBg = new EventBusMsg();
        evBg.setMsgType(EventBusMsg.BIKE_SETTING_REFRESH_MAIN);
        evBg.setValue((int) bikeId);
        evBg.setTvBundle(bike.protestValue);
        EventBus.getDefault().post(evBg);
    }
}
