/**
 * 
 */
package com.foxconn.rfid.theowner.activity.setting;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.androidquery.AQuery;
import com.androidquery.callback.AjaxCallback;
import com.androidquery.callback.AjaxStatus;
import com.foxconn.rfid.theowner.R;
import com.foxconn.rfid.theowner.base.App;
import com.foxconn.rfid.theowner.base.BaseActivity;
import com.foxconn.rfid.theowner.base.BaseApplication;
import com.foxconn.rfid.theowner.socket.SocketAppPacket;
import com.foxconn.rfid.theowner.socket.SocketClient;
import com.foxconn.rfid.theowner.util.ToolsUtils;
import com.foxconn.rfid.theowner.util.file.FileUtils;
import com.foxconn.rfid.theowner.util.logort.ToastUtils;
import com.yzh.rfidbike.app.request.CheckUpdateRequest;
import com.yzh.rfidbike.app.request.PlatformTypes;
import com.yzh.rfidbike.app.response.CheckUpdateResponse;

import java.io.File;


/**
 * @author WT00111
 *
 */
public class VersionUpdateActivity extends BaseActivity {
	private TextView app_version_tv1,app_version_tv,app_size_num,version_update_date;
	private static final int MSG_cannt_get_data = 1000;
@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_versionupdate);
		initView();
	}

	protected void initView() {
		app_version_tv1=(TextView) findViewById(R.id.app_version_tv1);
		app_version_tv = (TextView) findViewById(R.id.app_version_tv);
		app_size_num = (TextView) findViewById(R.id.app_size_num);
		version_update_date = (TextView) findViewById(R.id.version_update_date);
		app_version_tv.setText(ToolsUtils.getAppVersionName(this));
		app_size_num.setText(ToolsUtils.getAppSize(this));
		app_version_tv1.setText(ToolsUtils.getAppVersionName(this));
		version_update_date.setText(ToolsUtils.getApkLastUpdateTime(this));
	}

	public void onVersionUpdateBack(View view){
		finish();//返回键
	}

	//返回按键的点击事件
	public void onUpdateNow(View view){
		dlgWaiting.show();
		mHandler.sendEmptyMessageDelayed(MSG_cannt_get_data, 10*1000);
		new Thread(new Runnable() {

			@Override
			public void run() {
				// TODO Auto-generated method stub
				SocketClient client=SocketClient.getInstance();
				CheckUpdateRequest.CheckUpdateRequestMessage.Builder requestMessage = CheckUpdateRequest.CheckUpdateRequestMessage.newBuilder();
				requestMessage.setAppVersion(ToolsUtils
						.getAppVersionName(VersionUpdateActivity.this.getApplicationContext()));
				requestMessage.setPlatformType(PlatformTypes.PlatformType.AndroidPhone);
				client.send(SocketAppPacket.COMMAND_ID_GET_APP_VERSION,requestMessage.build().toByteArray());
			}
		}).start();
	}


	@Override
	public void onEventMainThread(SocketAppPacket eventPackage) {
		try {
			mHandler.removeMessages(MSG_cannt_get_data);
			if(dlgWaiting.isShowing())
			{
				dlgWaiting.dismiss();
			}
			if(eventPackage.getCommandId()==SocketAppPacket.COMMAND_ID_GET_APP_VERSION)
			{
				if(BaseApplication.getInstance().isChangeLanguage)
				{
					return;
				}
				CheckUpdateResponse.CheckUpdateResponseMessage responseMessage;

				responseMessage =CheckUpdateResponse.CheckUpdateResponseMessage.parseFrom(eventPackage.getCommandData());

				if(responseMessage.getErrorMsg().getErrorCode()==0)
				{

//			PreferenceData.saveSessionNo(context, responseMessage.getVersion());
					String version1 = ToolsUtils
							.getAppVersionName(this.getApplicationContext());
					if (responseMessage.getVersion().length()>0) {

						Dialog delDia = new AlertDialog.Builder(this)

								.setTitle(R.string.home_dlg_download_update)
								.setMessage(this.getResources().getString(R.string.last_version) + responseMessage.getVersion() + "\n" + responseMessage.getUpdateLog())
								.setPositiveButton(
										R.string.home_dlg_download_update,
										new DialogInterface.OnClickListener() {

											@Override
											public void onClick(
													DialogInterface dia, int which) {
												dia.dismiss();
												downloadFile(
														App.APK_DOWNLOAD_ADDRESS,
														getString(R.string.app_name),
														getString(R.string.home_dlg_download_title));
											}
										})
								.setNegativeButton(
										R.string.home_dlg_download_cansel,
										new DialogInterface.OnClickListener() {

											@Override
											public void onClick(
													DialogInterface dia, int which) {
												dia.dismiss();
											}
										}).create();
						delDia.show();

					}else
					{
						ToastUtils.showTextToast(context, VersionUpdateActivity.this.getResources().getString(R.string.lastest_version), 2000);
					}
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
					Toast.makeText(VersionUpdateActivity.this,
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
						if(dlgWaiting.isShowing())
						{
							dlgWaiting.dismiss();
							Toast.makeText(VersionUpdateActivity.this, R.string.network_error, Toast.LENGTH_SHORT).show();
						}
						break;
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
//		super.handleMessage(msg);
		}
	};
}
