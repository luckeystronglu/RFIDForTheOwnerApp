/**
 * 
 */
package com.foxconn.rfid.theowner.adapter;

import android.annotation.SuppressLint;
import android.app.Notification;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.foxconn.rfid.theowner.R;


/**
 * @author WT00111
 * 
 */
@SuppressLint("InflateParams")
public class MessageAdapter extends BaseAdapter {
	private Context context;

	public MessageAdapter(Context context) {
		this.context = context;
	}

	/**
	 * Chsion (non-Javadoc)
	 *
	 * @see android.widget.Adapter#getCount()
	 */
	@Override
	public int getCount() {
		return 20;
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
	public View getView(final int position, View convertView, ViewGroup parent) {
		if (convertView == null) {
			convertView = LayoutInflater.from(context).inflate(R.layout.vlist_message, null);
			ImageView imageView = (ImageView)convertView.findViewById(R.id.img);
			imageView.setBackgroundResource(R.drawable.ico_news);
			TextView textView = (TextView)convertView.findViewById(R.id.title);
			textView.setText("安全区域");
			TextView textView1 = (TextView)convertView.findViewById(R.id.date);
			textView1.setText("2016-11-11 11:11:11");
			TextView textView2 = (TextView)convertView.findViewById(R.id.info);
			textView2.setText(position+"");
			ImageView iv_del = (ImageView)convertView.findViewById(R.id.iv_del);
			iv_del.setBackgroundResource(R.drawable.ico_delete);
			iv_del.setOnClickListener(new View.OnClickListener() {
				@Override
				public void onClick(View v) {
					Toast.makeText(context,"确定删除第"+position+"条消息吗？",Toast.LENGTH_SHORT).show();

				}
			});
			
		}
		return convertView;
	}

}