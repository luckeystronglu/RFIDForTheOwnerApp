/**
 * 
 */
package com.foxconn.rfid.theowner.activity.main.defence;

import android.graphics.Color;
import android.graphics.Point;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.ImageView;
import android.widget.ZoomControls;

import com.baidu.mapapi.map.BaiduMap;
import com.baidu.mapapi.map.BitmapDescriptor;
import com.baidu.mapapi.map.BitmapDescriptorFactory;
import com.baidu.mapapi.map.CircleOptions;
import com.baidu.mapapi.map.MapStatusUpdate;
import com.baidu.mapapi.map.MapStatusUpdateFactory;
import com.baidu.mapapi.map.MapView;
import com.baidu.mapapi.map.MarkerOptions;
import com.baidu.mapapi.map.Overlay;
import com.baidu.mapapi.map.OverlayOptions;
import com.baidu.mapapi.map.Stroke;
import com.baidu.mapapi.map.TextOptions;
import com.baidu.mapapi.map.UiSettings;
import com.baidu.mapapi.model.LatLng;
import com.foxconn.rfid.theowner.R;
import com.foxconn.rfid.theowner.base.BaseActivity;
import com.foxconn.rfid.theowner.util.Utils;

import java.util.LinkedHashMap;

/**
 * @author WT00111
 * 
 */
public class EleFenceBaseActivity extends BaseActivity {
	protected MapView mapView;
	protected BaiduMap mBaiduMap;
	protected LinkedHashMap<String, Overlay> overlays = new LinkedHashMap<String, Overlay>();




	protected void initView() {
		mapView = (MapView) findViewById(R.id.mapview);
		mBaiduMap = mapView.getMap();
	}



	@Override
	public void onCreate(@Nullable Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		initView();
	}


	public void onClick(View v) {

	}

	/**
	 * 初始化地图
	 */
	public void initMap() {
//		mBaiduMap = mapView.getMap();
		LatLng point = new LatLng(22.608875, 114.065234);

		// 构建Marker图标
		BitmapDescriptor bitmap = BitmapDescriptorFactory.fromResource(R.drawable.orp);
		// 构建MarkerOption，用于在地图上添加Marker
		MarkerOptions option = new MarkerOptions().position(point).icon(bitmap);
		option.draggable(true);
		// 在地图上添加Marker，并显示
		addOverlay(option, "locationIcon");
		// 设置地图中心点
		MapStatusUpdate mapstatusUpdatePoint = MapStatusUpdateFactory.newLatLngZoom(point, 15);
		// 带动画的更新地图状态　　
		mBaiduMap.setMapStatus(mapstatusUpdatePoint);
		// mapView.showScaleControl(false);
		// mapView.showZoomControls(false);
		// 隐藏 百度地图logo
		View child = mapView.getChildAt(1);
		if (child != null && (child instanceof ImageView || child instanceof ZoomControls)) {
			child.setVisibility(View.INVISIBLE);
		}
		mBaiduMap.setBuildingsEnabled(true);
		mBaiduMap.setCompassPosition(new Point(Utils.dpToPx(20, getResources()), Utils.dpToPx(20, getResources())));
		UiSettings settings = mBaiduMap.getUiSettings();
		settings.setCompassEnabled(true);
//		drawCircle(new LatLng(29.617396, 106.368996), "1");
//		drawCircle(new LatLng(29.597396, 106.388996), "2");
//		drawCircle(new LatLng(29.617396, 106.388996), "3");
//		drawCircle(new LatLng(29.597396, 106.368996), "4");

	}

	/**
	 * 画圆图层
	 * 
	 * @param latLng
	 */
	private void drawCircle(LatLng latLng, String tag) {
		// 定义一个圆 ： 圆心+半径

		// 1.创建自己
		CircleOptions circleOptions = new CircleOptions();
		// 2.给自己设置数据
		circleOptions.center(latLng) // 圆心
				.radius(1000)// 半径 单位米
				.fillColor(0x60ff0000)// 填充色
				.stroke(new Stroke(2, Color.TRANSPARENT));// 边框宽度和颜色
		// 3.把覆盖物添加到地图中
		addOverlay(circleOptions, "range" + tag);
		TextOptions textOptions = new TextOptions();
		textOptions.fontColor(Color.parseColor("#ffffff"))// 设置字体颜色
				.text(tag)// 文字内容
				.position(latLng)// 位置
				.fontSize(Utils.spToPx(20, getResources()))// 字体大小
				.typeface(Typeface.SERIF)// 字体
				.bgColor(Color.TRANSPARENT);
		addOverlay(textOptions, "lable" + tag);
	}

	/**
	 * 添加覆盖物
	 * 
	 * @param overlayOptions
	 * @param tag
	 */
	public void addOverlay(OverlayOptions overlayOptions, String tag) {
		Bundle bundle = new Bundle();
		bundle.putString("title", tag);
		addOverLay(overlayOptions, bundle);
	}

	public void addOverLay(OverlayOptions overlayOptions, Bundle bundle) {
		Overlay overlay = (Overlay) mBaiduMap.addOverlay(overlayOptions);
		overlay.setExtraInfo(bundle);
		overlays.put(bundle.getString("title"), overlay);
	}

	@Override
	public void onPause() {
		super.onPause();
		mapView.onPause();
	}

	@Override
	public void onDestroy() {
		super.onDestroy();
		mapView.onDestroy();
	}

	@Override
	public void onResume() {
		super.onResume();
		mapView.onResume();
	}
}
