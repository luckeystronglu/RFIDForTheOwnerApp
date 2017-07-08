package com.foxconn.rfid.theowner.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

import com.foxconn.rfid.theowner.R;
import com.foxconn.rfid.theowner.activity.main.MainNewActivity;
import com.foxconn.rfid.theowner.base.PreferenceData;

public class LoadingActivity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_loading);
		final long loadLoginAccount = PreferenceData.loadLoginAccount(this);
		new Thread() {
			public void run() {
				try {
					Thread.sleep(1 * 1000);
					if (loadLoginAccount <= 0){
						Intent intent = new Intent(LoadingActivity.this, LoginActivity.class);
						startActivity(intent);
						finish();
					}else{
//						Intent intent = new Intent(LoadingActivity.this, MainActivity.class);
						Intent intent = new Intent(LoadingActivity.this, MainNewActivity.class);
						startActivity(intent);
						finish();
					}
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
		}.start();
	}

	@Override
	protected void onNewIntent(Intent intent) {
		super.onNewIntent(intent);
		final long loadLoginAccount = PreferenceData.loadLoginAccount(this);
		if (loadLoginAccount <= 0){
			intent = new Intent(LoadingActivity.this, LoginActivity.class);
			startActivity(intent);
			finish();
		}else{

//			Intent intentNew = new Intent(LoadingActivity.this, MainActivity.class);
			Intent intentNew = new Intent(LoadingActivity.this, MainNewActivity.class);
			startActivity(intentNew);
			finish();
		}
	}
}
