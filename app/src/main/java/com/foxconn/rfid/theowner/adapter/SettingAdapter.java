/**
 * 
 */
package com.foxconn.rfid.theowner.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.foxconn.rfid.theowner.R;
import com.foxconn.rfid.theowner.util.ToolsUtils;


/**
 * @author WT00111
 * 
 */
@SuppressLint("InflateParams")
public class SettingAdapter extends BaseAdapter {
	private Context context;

	public SettingAdapter(Context context) {
		this.context = context;
	}

	/**
	 * Chsion (non-Javadoc)
	 *
	 * @see android.widget.Adapter#getCount()
	 */
	@Override
	public int getCount() {
		return 6;
	}

	/**
	 * Chsion (non-Javadoc)
	 *
	 * @see android.widget.Adapter#getItem(int)
	 */
	@Override
	public Object getItem(int position) {
		return null;
	}

	/**
	 * Chsion (non-Javadoc)
	 *
	 * @see android.widget.Adapter#getItemId(int)
	 */
	@Override
	public long getItemId(int position) {
		return position;
	}

	/**
	 * Chsion (non-Javadoc)
	 *
	 * @see android.widget.Adapter#getView(int, View,
	 *      ViewGroup)
	 */
	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		if (convertView == null) {
			convertView = LayoutInflater.from(context).inflate(R.layout.vlist_setting, null);
			int settingimage[] = {R.drawable.ico_gr_setting,R.drawable.ico_mm,R.drawable.ico_afd,R.drawable.ico_xx,R.drawable.ico_gx_setting,R.drawable.ico_zx};
			String settingtitle[] = {"个人信息","修改密码","车辆安防","消息中心","版本更新","注销登陆"};
			String settinginfo[] = {"","","","", ToolsUtils.getAppVersionName(context),""};
			ImageView imageView = (ImageView)convertView.findViewById(R.id.img);
			imageView.setBackgroundResource(settingimage[position]);
			TextView textView = (TextView)convertView.findViewById(R.id.title);
			textView.setText(settingtitle[position]);
			TextView textView1 = (TextView)convertView.findViewById(R.id.info);
			textView1.setText(settinginfo[position]);
			ImageView imageView1 = (ImageView)convertView.findViewById(R.id.img_right);
			imageView1.setBackgroundResource(R.drawable.right);
			
		}
		return convertView;
	}

}
