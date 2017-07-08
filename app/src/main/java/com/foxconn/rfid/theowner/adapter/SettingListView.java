/**
 * 
 */
package com.foxconn.rfid.theowner.adapter;

import android.annotation.SuppressLint;
import android.app.ListActivity;
import android.os.Bundle;
import android.widget.SimpleAdapter;

import com.foxconn.rfid.theowner.R;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


/**
 * @author WT00153
 * 
 */
@SuppressLint("InflateParams")
public class SettingListView extends ListActivity {


	// private List<String> data = new ArrayList<String>();
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		SimpleAdapter adapter = new SimpleAdapter(this,getData(),R.layout.vlist_setting,
				new String[]{"img","title","info","img_right"},
				new int[]{R.id.img,R.id.title,R.id.info,R.id.img_right});
		setListAdapter(adapter);
	}

	private List<Map<String, Object>> getData() {
		List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();

		Map<String, Object> map = new HashMap<String, Object>();
		map.put("img",R.drawable.man);
		map.put("title", "个人信息");
		map.put("info", "1");
		map.put("img", R.drawable.right);
		list.add(map);

		map = new HashMap<String, Object>();
		map.put("img",R.drawable.sop);
		map.put("title", "修改密码");
		map.put("info", "2");
		map.put("img", R.drawable.right);
		list.add(map);

		map = new HashMap<String, Object>();
		map.put("img",R.drawable.version);
		map.put("title", "版本更新");
		map.put("info", "1.0.0.3");
		map.put("img", R.drawable.right);
		list.add(map);

		map = new HashMap<String, Object>();
		map.put("img",R.drawable.logout);
		map.put("title", "注销登陆");
		map.put("info", "4");
		map.put("img", R.drawable.right);
		list.add(map);

		return list;
	}
}
