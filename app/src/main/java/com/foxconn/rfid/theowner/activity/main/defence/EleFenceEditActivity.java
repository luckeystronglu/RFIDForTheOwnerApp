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
import com.yzh.rfidbike.app.response.GetBikeDefenceResponse;

import java.util.LinkedHashMap;

import de.greenrobot.event.EventBus;

import static com.foxconn.rfid.theowner.activity.main.defence.DefenceRadiusActivity.RESULT_CODE;

/**
 * @author WT00111
 */
public class EleFenceEditActivity extends BaseActivity implements View.OnClickListener,OnGetGeoCoderResultListener {
    public LocationClient mLocationClient = null;
    public BDLocationListener myListener = new MyLocationListener();
    //	public BDLocationListener myListener = new BDLocationListener() {
    //		@Override
    //		public void onReceiveLocation(BDLocation location) {
    //
    //			//接收定位返回结果
    //			//Receive Location
    //			StringBuffer sb = new StringBuffer(256);
    //			sb.append("time : ");
    //			sb.append(location.getTime());
    //			sb.append("\nerror code : ");
    //			sb.append(location.getLocType());
    //			sb.append("\nlatitude : ");
    //			sb.append(location.getLatitude());
    //			sb.append("\nlontitude : ");
    //			sb.append(location.getLongitude());
    //			sb.append("\nradius : ");
    //			sb.append(location.getRadius());
    //			if (location.getLocType() == BDLocation.TypeGpsLocation) {// GPS定位结果
    //				sb.append("\nspeed : ");
    //				sb.append(location.getSpeed());// 单位：公里每小时
    //				sb.append("\nsatellite : ");
    //				sb.append(location.getSatelliteNumber());
    //				sb.append("\nheight : ");
    //				sb.append(location.getAltitude());// 单位：米
    //				sb.append("\ndirection : ");
    //				sb.append(location.getDirection());// 单位度
    //				sb.append("\naddr : ");
    //				sb.append(location.getAddrStr());
    //				sb.append("\ndescribe : ");
    //				sb.append("gps定位成功");
    //
    //			} else if (location.getLocType() == BDLocation.TypeNetWorkLocation) {// 网络定位结果
    //				sb.append("\naddr : ");
    //				sb.append(location.getAddrStr());
    //				//运营商信息
    //				sb.append("\noperationers : ");
    //				sb.append(location.getOperators());
    //				sb.append("\ndescribe : ");
    //				sb.append("网络定位成功");
    //
    //
    //				//获得经纬度
    //				lat = location.getLatitude();
    //				lng = location.getLongitude();
    //				locationText.setText(location.getAddrStr());
    //				Log.d("print", "onReceiveLocation: lat" + lat + " lng:" + lng);
    //				//                Log.d("print", "定位坐标Latitude： "+ lat +" Longitude:"+ lng);
    //				Toast.makeText(EleFenceEditActivity.this, "您当前位置为北纬：" + lat + "，东经:" + lng, Toast.LENGTH_SHORT).show();
    //
    //				//构建Marker图标
    //				BitmapDescriptor bitmap = BitmapDescriptorFactory
    //						.fromResource(R.drawable.orp);
    //				//构建MarkerOption，用于在地图上添加Marker
    //				MarkerOptions option = new MarkerOptions()
    //						.position(new LatLng(lat, lng))
    //						.icon(bitmap)
    //						.draggable(true);//可拖拽
    //
    //				// 生长动画
    //				option.animateType(MarkerOptions.MarkerAnimateType.grow);
    //
    //				//在地图上添加Marker，并显示
    //				map.addOverlay(option);
    //
    //				//设置地图显示的位置
    //				MapStatus mMapStatus = new MapStatus.Builder()
    //						.target(new LatLng(lat, lng))//地图显示位置的中心点
    //						.build();
    //
    //
    //				MapStatusUpdate mMapStatusUpdate = MapStatusUpdateFactory.newMapStatus(mMapStatus);
    //				map.setMapStatus(mMapStatusUpdate);
    //
    //			} else if (location.getLocType() == BDLocation.TypeOffLineLocation) {// 离线定位结果
    //				sb.append("\ndescribe : ");
    //				sb.append("离线定位成功，离线定位结果也是有效的");
    //			} else if (location.getLocType() == BDLocation.TypeServerError) {
    //				sb.append("\ndescribe : ");
    //				sb.append("服务端网络定位失败，可以反馈IMEI号和大体定位时间到loc-bugs@baidu.com，会有人追查原因");
    //			} else if (location.getLocType() == BDLocation.TypeNetWorkException) {
    //				sb.append("\ndescribe : ");
    //				sb.append("网络不同导致定位失败，请检查网络是否通畅");
    //			} else if (location.getLocType() == BDLocation.TypeCriteriaException) {
    //				sb.append("\ndescribe : ");
    //				sb.append("无法获取有效定位依据导致定位失败，一般是由于手机的原因，处于飞行模式下一般会造成这种结果，可以试着重启手机");
    //			}
    //			sb.append("\nlocationdescribe : ");
    //			sb.append(location.getLocationDescribe());// 位置语义化信息
    //			List<Poi> list = location.getPoiList();// POI数据
    //			if (list != null) {
    //				sb.append("\npoilist size = : ");
    //				sb.append(list.size());
    //				for (Poi p : list) {
    //					sb.append("\npoi= : ");
    //					sb.append(p.getId() + " " + p.getName() + " " + p.getRank());
    //				}
    //
    //			}
    //			Log.d("print", sb.toString());
    //		}
    //	};

    private boolean enableChangeMapStatus = false;
    private Overlay mCircle = null, mCircleCenter = null;
    BitmapDescriptor bitmap_mark_center = BitmapDescriptorFactory
            .fromResource(R.drawable.bike);
    public static final int REQUEST_CODE = 0x001;
    private Button edit_save;
    private int radiusNum;
    private MapView mMapView;
    private BaiduMap map;
    private double lng, lat;
    private SwitchButton swithbutton1, swithbutton2;
    private TextView areaRadius, locationText;
    private EditText edit_name;
    private RelativeLayout areaRadius_rl;
    private GetBikeDefenceResponse.DefenceMessage defenceMessage;
    private Bike bike;
    protected LinkedHashMap<String, Overlay> overlays = new LinkedHashMap<String, Overlay>();
    private int radiu = 0;
    private int markerposition;
    GeoCoder mSearch = null;



    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fence_edit);
        initView();
        initSwithBtnEvent();
    }

    //提醒swithbutton的监听事件
    private void initSwithBtnEvent() {
        swithbutton1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //离开时提醒

            }
        });
        swithbutton2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //进入时提醒

            }
        });
    }




    protected void initView() {
        //获得点击item后传过来的值
        Intent in = getIntent();
        defenceMessage = (GetBikeDefenceResponse.DefenceMessage) in.getSerializableExtra("entity");
        markerposition = in.getIntExtra("position", 0);
        bike = (Bike) in.getSerializableExtra("BIKE");

        edit_save = (Button) findViewById(R.id.edit_save);
        edit_save.setOnClickListener(this);
        //区域半径所在RelativeLayout布局
        radiusNum = defenceMessage.getRadius();
        areaRadius_rl = (RelativeLayout) findViewById(R.id.areaRadius_rl);
        swithbutton1 = (SwitchButton) findViewById(R.id.swithbutton1); //提醒swithbutton 1
        swithbutton2 = (SwitchButton) findViewById(R.id.swithbutton2); //提醒swithbutton 2
        swithbutton1.setChecked(defenceMessage.getOutAlert());//获取
        swithbutton2.setChecked(defenceMessage.getInAlert());


        edit_name = (EditText) findViewById(R.id.edit_name); //电子围栏名称
        edit_name.setText(defenceMessage.getName());
        locationText = (TextView) findViewById(R.id.edit_location_tv); //当前定位地址

        areaRadius = (TextView) findViewById(R.id.areaRadius);//区域半径
        //		edit_name_tv.setText(defenceMessage.getName());//设置获取电子围栏名称
        areaRadius.setText(radiusNum + getResources().getString(R.string.meter).trim());//设置区域半径
        areaRadius_rl.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //区域半径变动时回调获取
                startActivityForResult(new Intent(EleFenceEditActivity.this, DefenceRadiusActivity.class), REQUEST_CODE);

            }
        });



        initMap();// 初始化地图
    }

    //listview跳转后回传结果（区域半径）
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_CODE && resultCode == RESULT_CODE) {
            Bundle bundle = data.getExtras();
            String radius_str = bundle.getString("radius");
            areaRadius.setText(radius_str);
            String substring = radius_str.substring(0, radius_str.length() - 1);
            radiu = Integer.parseInt(substring);
            Log.d("print", "onActivityResult: radiu " + radiu);
//            map.clear();

            LatLng rpoint = new LatLng(lat, lng);
//            BitmapDescriptor bitmap = BitmapDescriptorFactory.fromResource(R.drawable.bike);
//            OverlayOptions option = new MarkerOptions().position(rpoint).icon(bitmap);
//            map.addOverlay(option);
            initCircleOverlay(rpoint);

            //            map.setOnMapClickListener(new BaiduMap.OnMapClickListener() {
            //                @Override
            //                public void onMapClick(LatLng latLng) {
            //                    initCircleOverlay(latLng);
            //                }
            //
            //                @Override
            //                public boolean onMapPoiClick(MapPoi mapPoi) {
            //                    return false;
            //                }
            //            });


        }
    }

    public void mapListener() {
        map.setOnMapStatusChangeListener(new BaiduMap.OnMapStatusChangeListener() {
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
        mMapView = (MapView) findViewById(R.id.edit_mapview);
        map = mMapView.getMap();

        lat = defenceMessage.getLatitude();
        lng = defenceMessage.getLongitude();
        //			locationText.setText(location.getAddrStr());
        LatLng point = new LatLng(lat, lng);
//        BitmapDescriptor bitmap = BitmapDescriptorFactory.fromResource(R.drawable.bike);
//        OverlayOptions option = new MarkerOptions().position(point).icon(bitmap);
//        map.addOverlay(option);

        initCircleOverlay(point);
        MapStatusUpdate mapstatusUpdatePoint = MapStatusUpdateFactory.newLatLngZoom(point, 14);
        //         带动画的更新地图状态，还是300毫秒
        map.setMapStatus(mapstatusUpdatePoint);

        mMapView.showScaleControl(false);
        mMapView.showZoomControls(false);

        // 隐藏 百度地图logo
        View child = mMapView.getChildAt(1);
        if (child != null && (child instanceof ImageView)) {
            child.setVisibility(View.INVISIBLE);
        }

        map.setBuildingsEnabled(true);
        map.setCompassPosition(new Point(Utils.dpToPx(20, getResources()), Utils.dpToPx(20, getResources())));
        UiSettings settings = map.getUiSettings();
        settings.setOverlookingGesturesEnabled(false);// 屏蔽双指下拉时变成3D地图
        settings.setZoomGesturesEnabled(true);// 获取是否允许缩放手势返回:是否允许缩放手势
        settings.setCompassEnabled(true);

        mLocationClient = new LocationClient(getApplicationContext());     //声明LocationClient类
        mLocationClient.registerLocationListener( myListener );    //注册监听函数
        initLocation();
        mLocationClient.start();

        mapListener();



        //        MarkerOptions option = new MarkerOptions().position(point).icon(bitmap);
        //        option.draggable(true);
        //        // 在地图上添加Marker，并显示
        //        //        		addOverlay(option, "locationIcon");
        //        //         设置地图中心点，默认是天安门
        //
        ////        addOverlay(option, "edit");
        //        drawCircle(point, radiusNum);
        ////        drawCircle(point, "电子围栏", radiusNum);
        //
        //        //调整地图的中心位置及缩放比例
        //        LatLng ll = new LatLng(lat, lng);
        //        MapStatus.Builder builder = new MapStatus.Builder();
        //        builder.target(ll).zoom(14.0f);
        //        map.animateMapStatus(MapStatusUpdateFactory.newMapStatus(builder.build()));


    }


    public void initCircleOverlay(LatLng latlng_item) {
        if (mCircle != null) {
            mCircle.remove();
        }
        if (mCircleCenter != null) {
            mCircleCenter.remove();
        }
        OverlayOptions overlayOptions = new MarkerOptions()
                .position(latlng_item).icon(bitmap_mark_center)
                .perspective(false).zIndex(7);
        mCircleCenter = map.addOverlay(overlayOptions);
        // add marker overlay
        CircleOptions circleOption = new CircleOptions()
                .radius(radiu == 0 ? radiusNum : radiu)
                .center(latlng_item).zIndex(8)
                .stroke(new Stroke(2, 0x19ff0C00))
                .fillColor(0x18ff0C00);
//        .fillColor(0xAAF4CAC3);
        //.stroke(new Stroke(2, Color.RED))
        mCircle = map.addOverlay(circleOption);

        MapStatusUpdate u = MapStatusUpdateFactory.newLatLng(latlng_item);
        map.animateMapStatus(u);

        lat = latlng_item.latitude;
        lng = latlng_item.longitude;

        mSearch = GeoCoder.newInstance();
        mSearch.setOnGetGeoCodeResultListener(EleFenceEditActivity.this);
        LatLng ptCenter = new LatLng(lat, lng);
        // 反Geo搜索
        mSearch.reverseGeoCode(new ReverseGeoCodeOption()
                .location(ptCenter));
    }

    private void initLocation() {
        LocationClientOption option = new LocationClientOption();
        option.setLocationMode(LocationClientOption.LocationMode.Hight_Accuracy
        );//可选，默认高精度，设置定位模式，高精度，低功耗，仅设备
        option.setCoorType("bd09ll");//可选，默认gcj02，设置返回的定位结果坐标系
        int span = 0; //只定位一次
        //		int span= 1000; //每隔1秒定位一次
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

    public class MyLocationListener implements BDLocationListener {
        @Override
        public void onReceiveLocation(BDLocation location) {

            double localat = location.getLatitude();
            double localLng = location.getLongitude();
//            locationText.setText(location.getAddrStr());

            //            			mLocationClient.stop();
            LatLng locationPoint = new LatLng(localat, localLng);
            Log.d("print", "onReceive Location: lat: " + lat + " lng:" + lng);
//            // 构建Marker图标
            BitmapDescriptor bitmap = BitmapDescriptorFactory.fromResource(R.drawable.orp);
            MarkerOptions option = new MarkerOptions().position(locationPoint).icon(bitmap);
            option.draggable(true);
            map.addOverlay(option);

            //调整地图的中心位置及缩放比例
//            MapStatus.Builder builder = new MapStatus.Builder();
//            builder.target(locationPoint).zoom(14.0f);
//            map.animateMapStatus(MapStatusUpdateFactory.newMapStatus(builder.build()));
//            initCircleOverlay(locationPoint);
        }
    }



    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.edit_save:// 电子围栏
                boolean cancel = false;
                View focusView = null;
                if (TextUtils.isEmpty(edit_name.getText().toString().trim())) {
                    ToastUtils.showTextToast(EleFenceEditActivity.this,"电子围栏名称不能为空");
                    focusView = edit_name;
                    cancel = true;
                }
                if (cancel) {
                    focusView.requestFocus();
                } else {
                    doSocket();
                }

                break;

            default:
                break;
        }
    }

    @Override
    protected void doSocket() {
        super.doSocket();
        final EditBikeFenceRequest.EditBikeFenceRequestMessage.Builder requestMessage = EditBikeFenceRequest.EditBikeFenceRequestMessage.newBuilder();
        BikeUser user = BikeUser.getCurUser(this);
        requestMessage.setLat(lat);
        requestMessage.setLng(lng);
        requestMessage.setId(defenceMessage.getId());
        requestMessage.setName(edit_name.getText().toString().trim());
        requestMessage.setRadius(radiu == 0 ? radiusNum : radiu);
        requestMessage.setOutAlert(swithbutton1.isChecked());
        requestMessage.setInAlert(swithbutton2.isChecked());
        requestMessage.setBikeId(bike.getId());

        requestMessage.setCompanyId(user.getCompanyId());
        requestMessage.setSession(PreferenceData.loadSession(this));
        requestMessage.setType(Types.Type.EDIT);


        new Thread() {
            public void run() {
                SocketClient client;
                client = SocketClient.getInstance();
                client.send(SocketAppPacket.COMMAND_ID_EDIT_BIKE_DEFENCE, requestMessage.build().toByteArray());
            }
        }.start();
    }

    @Override
    public void onEventMainThread(SocketAppPacket eventPackage) {
        try {
            super.onEventMainThread(eventPackage);
            if (eventPackage.getCommandId() == SocketAppPacket.COMMAND_ID_EDIT_BIKE_DEFENCE) {
                CommonResponse.CommonResponseMessage changePwMessage = CommonResponse.CommonResponseMessage.parseFrom(eventPackage.getCommandData());

                if (dlgWaiting.isShowing()) {
                    dlgWaiting.dismiss();
                }
                mDlgWaitingHandler.removeMessages(MSG_cannt_get_data);
                if (changePwMessage.getErrorMsg().getErrorCode() != 0) {
                    Toast.makeText(this, changePwMessage.getErrorMsg().getErrorMsg(), Toast.LENGTH_LONG).show();
                    if (changePwMessage.getErrorMsg().getErrorCode() == 20003) {
                        Intent intent = new Intent();
                        intent.setClass(this, LoginActivity.class);
                        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK|Intent.FLAG_ACTIVITY_CLEAR_TASK);
                        startActivity(intent);
                    }

                } else {

                    EventBusMsg msg = new EventBusMsg();
                    msg.setMsgType(EventBusMsg.MSG_defence_edit_data);
                    EventBus.getDefault().post(msg);

                    finish();

                }
            }

        } catch (InvalidProtocolBufferException e) {
            e.printStackTrace();
        }


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

    @Override
    public void onGetGeoCodeResult(GeoCodeResult result) {

    }

    @Override
    public void onGetReverseGeoCodeResult(ReverseGeoCodeResult result) {

        locationText.setText(result.getAddress());
        //result保存翻地理编码的结果 坐标-->城市
    }

}
