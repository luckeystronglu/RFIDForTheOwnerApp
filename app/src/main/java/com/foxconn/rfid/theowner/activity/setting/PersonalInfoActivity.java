/**
 * 
 */
package com.foxconn.rfid.theowner.activity.setting;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.foxconn.rfid.theowner.R;
import com.foxconn.rfid.theowner.base.App;
import com.foxconn.rfid.theowner.base.BaseActivity;
import com.foxconn.rfid.theowner.base.BaseApplication;
import com.foxconn.rfid.theowner.base.PreferenceData;
import com.foxconn.rfid.theowner.model.BikeUser;
import com.foxconn.rfid.theowner.view.widgets.GlideCircleTransform;

import net.tsz.afinal.FinalDb;

import java.util.List;


/**个人信息
 * @author WT00111
 *
 */
public class PersonalInfoActivity extends BaseActivity {
	private ImageView personinfo_img;
	private TextView personal_names,personal_id,personal_phone,personal_addr,personal_email;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_personalinfo);
		initView();
	}
	protected void initView() {
		personinfo_img = (ImageView) findViewById(R.id.personinfo_img);
		personal_names = (TextView) findViewById(R.id.personal_names);
		personal_id = (TextView) findViewById(R.id.personal_id);
		personal_phone = (TextView) findViewById(R.id.personal_phone);
		personal_addr = (TextView) findViewById(R.id.personal_addr);
		personal_email = (TextView) findViewById(R.id.personal_email);

		final FinalDb mDb= FinalDb.create(this.getApplicationContext(), App.DB_NAME, true,App.DB_VERSION, BaseApplication.getInstance());
		//获取当前用户信息
		List<BikeUser> list= mDb.findAllByWhere(BikeUser.class,"userId="+String.valueOf(PreferenceData.loadLoginAccount(this)));
		BikeUser bikeuser=list.get(0);
		personal_names.setText(bikeuser.getName());
		personal_id.setText(bikeuser.getIdCard());
		personal_phone.setText(bikeuser.getPhone());
		personal_addr.setText(bikeuser.getAddress());
		personal_email.setText(bikeuser.getEmail());
//		Log.d("print", "personal_names: "+bikeuser.getName() + ", personal_id:" + bikeuser.getIdCard() + "  ,personal_phone:"+ bikeuser.getPhone() + " ,personal_addr:" + bikeuser.getAddress() + " ,personal_email:"+bikeuser.getEmail() +",personinfo_img"+bikeuser.getPhoto());
		Glide.with(this)
				.load(bikeuser.getPhoto())
				.placeholder(R.drawable.headimage)
				.transform(new GlideCircleTransform(this))
				.diskCacheStrategy(DiskCacheStrategy.ALL)
				.thumbnail(0.1f)
				.crossFade(500)
				.into(personinfo_img);
	}

	//返回按键的点击事件
	public void onBack(View v) {
		switch (v.getId()) {
			case R.id.personinfo_btn_back:
				finish();
				break;
		default:
			break;
		}
	}





}
