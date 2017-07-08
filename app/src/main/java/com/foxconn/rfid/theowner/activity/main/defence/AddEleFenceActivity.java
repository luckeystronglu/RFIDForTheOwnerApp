/**
 *
 */
package com.foxconn.rfid.theowner.activity.main.defence;

import android.content.Intent;
import android.graphics.Point;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;
import com.baidu.mapapi.map.BaiduMap;
import com.baidu.mapapi.map.BitmapDescriptor;
import com.baidu.mapapi.map.BitmapDescriptorFactory;
import com.baidu.mapapi.map.CircleOptions;
import com.baidu.mapapi.map.MapStatus;
import com.baidu.mapapi.map.MapStatusUpdate;
import com.baidu.mapapi.map.MapStatusUpdateFactory;
import com.baidu.mapapi.map.MapView;
import com.baidu.mapapi.map.MarkerOptions;
import com.baidu.mapapi.map.Overlay;
import com.baidu.mapapi.map.OverlayOptions;
import com.baidu.mapapi.map.Stroke;
import com.baidu.mapapi.map.UiSettings;
import com.baidu.mapapi.model.LatLng;
import com.baidu.mapapi.search.geocode.GeoCodeResult;
import com.baidu.mapapi.search.geocode.GeoCoder;
import com.baidu.mapapi.search.geocode.OnGetGeoCoderResultListener;
import com.baidu.mapapi.search.geocode.ReverseGeoCodeOption;
import com.baidu.mapapi.search.geocode.ReverseGeoCodeResult;
import com.customview.switchbutton.SwitchButton;
import com.foxconn.rfid.theowner.R;
import com.foxconn.rfid.theowner.activity.LoginActivity;
import com.foxconn.rfid.theowner.base.BaseActivity;
import com.foxconn.rfid.theowner.base.PreferenceData;
import com.foxconn.rfid.theowner.model.Bike;
import com.foxconn.rfid.theowner.model.BikeUser;
import com.foxconn.rfid.theowner.model.EventBusMsg;
import com.foxconn.rfid.theowner.socket.SocketAppPacket;
import com.foxconn.rfid.theowner.socket.SocketClient;
import com.foxconn.rfid.theowner.util.Utils;
import com.foxconn.rfid.theowner.util.logort.ToastUtils;
import com.google.protobuf.InvalidProtocolBufferException;
import com.yzh.rfidbike.app.request.EditBikeFenceRequest;
import com.yzh.rfidbike.app.request.Types;
import com.yzh.rfidbike.app.response.CommonResponse;

import de.greenrobot.event.EventBus;

/**
 * @author WT00111
 *
 */
public class AddEleFenceActivity extends BaseActivity implements OnGetGeoCoderResultListener {
	public LocationClient mLocationClient = null;
	public BDLocationListener myListener = new MyLocationListener();
	private boolean enableChangeMapStatus=false;
	private Overlay mCircle =null, mCircleCenter = null,mManOveryLay=null;
	BitmapDescriptor bitmap_mark_center = BitmapDescriptorFactory
			.fromResource(R.drawable.bike);
	public static final int ADDREQUEST_CODE = 0x003;
	private Button add_save;
	private int radiusNum = 500;
	private MapView mMapView;
	private BaiduMap mMap;
	private double lng, lat;
	private ImageButton imageButton;
	private SwitchButton switch_leave,switch_enter;
	private TextView tvAreaRadius,add_location_tv;
	private EditText tv_edit_name;
	private RelativeLayout add_areaRadius_rl;
	private Bike bike;
	GeoCoder mSearch = null;
//	private double mDefenceLatitude,mDefenceLongitude;


	@Override
	public void onCreate(@Nullable Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_add_fence);
		initView();
		initMap();// 初始化地图
		initEvent();
	}



	protected void initView() {

		add_areaRadius_rl = (RelativeLayout) findViewById(R.id.add_areaRadius_rl);
		switch_leave = (SwitchButton) findViewById(R.id.switch_leave);
		switch_enter = (SwitchButton) findViewById(R.id.switch_enter);

		tv_edit_name = (EditText) findViewById(R.id.et_defence_name);

		add_location_tv = (TextView) findViewById(R.id.add_location_tv);


		bike = (Bike) this.getIntent().getSerializableExtra("BIKE");
		if (bike == null)
			return;
		tvAreaRadius = (TextView) findViewById(R.id.tv_radius);
		tvAreaRadius.setText(getResources().getString(R.string.ele_edit_radius_default).trim());
		add_areaRadius_rl.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {
				startActivityForResult(new Intent(AddEleFenceActivity.this,DefenceRadiusActivity.class),ADDREQUEST_CODE);
			}
		});

	}

	//listview跳转后回传结果
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		if (requestCode == ADDREQUEST_CODE && resultCode == DefenceRadiusActivity.RESULT_CODE) {
			Bundle bundle = data.getExtras();
			String radius_str = bundle.getString("radius");
			tvAreaRadius.setText(radius_str);
			String substring = radius_str.substring(0, radius_str.length()-1);
			int radiu = Integer.parseInt(substring);
			Log.d("print", "onActivityResult: radiu " +radiu);
            //获得新的半径数据
			radiusNum = radiu;

			LatLng rpoint = new LatLng(lat,lng);
			initCircleOverlay(rpoint);


		}
	}


	public void slideMapListener(){
		mMap.setOnMapStatusChangeListener(new BaiduMap.OnMapStatusChangeListener() {
			@Override
			public void onMapStatusChangeStart(MapStatus mapStatus) {
				// TODO Auto-generated method stub
			}

			@Override
			public void onMapStatusChangeFinish(MapStatus mapStatus) {
				// TODO Auto-generated method stub
				initCircleOverlay(mapStatus.target);
			}

			@Override
			public void onMapStatusChange(MapStatus mapStatus) {
				// TODO Auto-generated method stub

			}
		});
	}

	//初始化地图
	private void initMap() {

		mMapView = (MapView) findViewById(R.id.add_mapview);
		mMap = mMapView.getMap();
		mMapView.showScaleControl(false);
		mMapView.showZoomControls(false);
		lat=39.915291;
		lng=116.403857;
		LatLng cenpt = new LatLng( lat,lng);

		MapStatus mMapStatus = new MapStatus.Builder()
				.target(cenpt)
				.zoom(14)
				.build();
		MapStatusUpdate mMapStatusUpdate = MapStatusUpdateFactory.newMapStatus(mMapStatus);
		mMap.setMapStatus(mMapStatusUpdate);


		// 隐藏 百度地图logo
		View child = mMapView.getChildAt(1);
		if (child != null && (child instanceof ImageView )) {
			child.setVisibility(View.INVISIBLE);
		}
		mMap.setBuildingsEnabled(false);
		mMap.setCompassPosition(new Point(Utils.dpToPx(20, getResources()), Utils.dpToPx(20, getResources())));
		UiSettings settings = mMap.getUiSettings();
		settings.setOverlookingGesturesEnabled(false);// 屏蔽双指下拉时变成3D地图
		settings.setZoomGesturesEnabled(true);// 获取是否允许缩放手势返回:是否允许缩放手势
		settings.setCompassEnabled(true);



		mLocationClient = new LocationClient(getApplicationContext());     //声明LocationClient类
		mLocationClient.registerLocationListener(myListener); //注册监听函数
		initLocation();
		mLocationClient.start();//开始定位
		slideMapListener();

	}
	public void initCircleOverlay(LatLng latlng_item) {
		if (mCircle != null) {
			mCircle.remove();
		}
		if (mCircleCenter != null) {
			mCircleCenter.remove();
		}
		OverlayOptions overlayOptions = new MarkerOptions()
				.position(latlng_item)
				.icon(bitmap_mark_center)
				.perspective(false)
				.zIndex(7);
		mCircleCenter = mMap.addOverlay(overlayOptions);
		// add marker overlay
		String radius_str = tvAreaRadius.getText().toString();
		String substring = radius_str.substring(0, radius_str.length()-1);
		int radiu = Integer.parseInt(substring);

		CircleOptions circleOption = new CircleOptions()
				.radius(radiu)
				.center(latlng_item)
				.zIndex(8)
				.stroke(new Stroke(2, 0x19ff0C00))
				.fillColor(0x18ff0C00);
//		.stroke(new Stroke(2, Color.RED))
		mCircle = mMap.addOverlay(circleOption);

		MapStatusUpdate u = MapStatusUpdateFactory.newLatLng(latlng_item);
		mMap.animateMapStatus(u);

		lng = latlng_item.longitude;
		lat = latlng_item.latitude;
		mSearch = GeoCoder.newInstance();
		mSearch.setOnGetGeoCodeResultListener(AddEleFenceActivity.this);
		LatLng ptCenter = new LatLng(lat, lng);
		Log.e("---test-------",String.valueOf(lat));
		// 反Geo搜索
		mSearch.reverseGeoCode(new ReverseGeoCodeOption()
				.location(ptCenter));

	}

	private void initLocation() {
		LocationClientOption option = new LocationClientOption();
		option.setLocationMode(LocationClientOption.LocationMode.Hight_Accuracy
		);//可选，默认高精度，设置定位模式，高精度，低功耗，仅设备
		option.setCoorType("bd09ll");//可选，默认gcj02，设置返回的定位结果坐标系
		int span= 0; //每隔1秒定位一次
		//        int span= 5000; //每隔5秒定位一次
		option.setScanSpan(span);//可选，默认0，即仅定位一次，设置发起定位请求的间隔需要大于等于1000ms才是有效的
		option.setIsNeedAddress(true);//可选，设置是否需要地址信息，默认不需要
		option.setOpenGps(true);//可选，默认false,设置是否使用gps
		option.setLocationNotify(true);//可选，默认false，设置是否当gps有效时按照1S1次频率输出GPS结果
		option.setIsNeedLocationDescribe(true);//可选，默认false，设置是否需要位置语义化结果，可以在BDLocation.getLocationDescribe里得到，结果类似于“在北京天安门附近”
		option.setIsNeedLocationPoiList(true);//可选，默认false，设置是否需要POI结果，可以在BDLocation.getPoiList里得到
		option.setIgnoreKillProcess(false);//可选，默认true，定位SDK内部是一个SERVICE，并放到了独立进程，设置是否在stop的时候杀死这个进程，默认不杀死
		option.SetIgnoreCacheException(false);//可选，默认false，设置是否收集CRASH信息，默认收集
		option.setEnableSimulateGps(false);//可选，默认false，设置是否需要过滤gps仿真结果，默认需要
		mLocationClient.setLocOption(option);

	}

	//地图反解析回调方法
	@Override
	public void onGetGeoCodeResult(GeoCodeResult geoCodeResult) {
	}

	@Override
	public void onGetReverseGeoCodeResult(ReverseGeoCodeResult reverseGeoCodeResult) {
		add_location_tv.setText(reverseGeoCodeResult.getAddress());
		//result保存翻地理编码的结果 坐标-->城市
	}


	public class MyLocationListener implements BDLocationListener {
		@Override
		public void onReceiveLocation(BDLocation location) {

			lat = location.getLatitude();
			lng = location.getLongitude();
			LatLng point = new LatLng(lat,lng);
			// 构建Marker图标
			BitmapDescriptor bitmap = BitmapDescriptorFactory.fromResource(R.drawable.orp);
			MarkerOptions option = new MarkerOptions().position(point).icon(bitmap);
            //调整地图的中心位置及缩放比例
            MapStatus.Builder builder = new MapStatus.Builder();
            builder.target(point).zoom(14.0f);
            mMap.animateMapStatus(MapStatusUpdateFactory.newMapStatus(builder.build()));

            mMap.addOverlay(option);
            initCircleOverlay(point);

		}
	}


	protected void initEvent() {
		switch_leave.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {

			}
		});
		switch_enter.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {

			}
		});

	}

	//头部返回键
	public void onAddDefenceBack(View view){
		finish();
	}

	//添加后保存
	public void addDefenceSaveClick(View view){

		boolean cancel = false;
		View focusView = null;
		if (TextUtils.isEmpty(tv_edit_name.getText().toString().trim())) {
			ToastUtils.showTextToast(AddEleFenceActivity.this,"电子围栏名称不能为空");
			focusView = tv_edit_name;
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
		final EditBikeFenceRequest.EditBikeFenceRequestMessage.Builder requestMessage = EditBikeFenceRequest.EditBikeFenceRequestMessage.newBuilder();
		BikeUser user=BikeUser.getCurUser(this);
		requestMessage.setBikeId(bike.getId());
		requestMessage.setInAlert(switch_enter.isChecked());
		requestMessage.setOutAlert(switch_leave.isChecked());

		requestMessage.setLat(lat);
		requestMessage.setLng(lng);
		requestMessage.setCompanyId(user.getCompanyId());
		requestMessage.setName(tv_edit_name.getText().toString());
		String radius_str=tvAreaRadius.getText().toString();
		String substring = radius_str.substring(0, radius_str.length()-1);
		int radiu = Integer.parseInt(substring);
		requestMessage.setRadius(radiu);
		requestMessage.setSession(PreferenceData.loadSession(this));
		requestMessage.setType(Types.Type.NEW);
		new Thread() {
			public void run() {
				SocketClient client;
				client = SocketClient.getInstance();
				client.send(SocketAppPacket.COMMAND_ID_EDIT_BIKE_DEFENCE, requestMessage.build().toByteArray());
			}

		}.start();
	}



	@Override
	protected void onDestroy() {
		super.onDestroy();
		//在activity执行onDestroy时执行mMapView.onDestroy()，实现地图生命周期管理
		mMapView.onDestroy();
	}
	@Override
	protected void onResume() {
		super.onResume();
		//在activity执行onResume时执行mMapView. onResume ()，实现地图生命周期管理
		mMapView.onResume();
	}
	@Override
	protected void onPause() {
		super.onPause();
		//在activity执行onPause时执行mMapView. onPause ()，实现地图生命周期管理
		mMapView.onPause();
	}
	public void onEventMainThread(SocketAppPacket eventPackage) {
		try {
			super.onEventMainThread(eventPackage);
			if (eventPackage.getCommandId() == SocketAppPacket.COMMAND_ID_EDIT_BIKE_DEFENCE) {
				CommonResponse.CommonResponseMessage commonResponseMessage =CommonResponse.CommonResponseMessage.parseFrom(eventPackage.getCommandData());
				if (dlgWaiting.isShowing()) {
					dlgWaiting.dismiss();
				}
				mDlgWaitingHandler.removeMessages(MSG_cannt_get_data);
				if (commonResponseMessage.getErrorMsg().getErrorCode() != 0) {
					Toast.makeText(this, commonResponseMessage.getErrorMsg().getErrorMsg(), Toast.LENGTH_LONG).show();
					if (commonResponseMessage.getErrorMsg().getErrorCode() == 20003) {
						Intent intent = new Intent();
						intent.setClass(this, LoginActivity.class);
						intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK|Intent.FLAG_ACTIVITY_CLEAR_TASK);
						startActivity(intent);
					}
				} else {
					EventBusMsg msg=new EventBusMsg();
					msg.setMsgType(EventBusMsg.MSG_defence_edit_data);
					EventBus.getDefault().post(msg);
					this.finish();
				}
			}
		} catch (InvalidProtocolBufferException e) {
			e.printStackTrace();
		}
	}

}
