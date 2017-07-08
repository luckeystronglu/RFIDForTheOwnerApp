package com.foxconn.rfid.theowner.activity.main.defence;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.ZoomControls;

import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;
import com.baidu.mapapi.map.BaiduMap;
import com.baidu.mapapi.map.MapStatus;
import com.baidu.mapapi.map.MapStatusUpdateFactory;
import com.baidu.mapapi.map.MapView;
import com.baidu.mapapi.map.MyLocationData;
import com.baidu.mapapi.model.LatLng;
import com.baidu.mapapi.search.geocode.GeoCodeResult;
import com.baidu.mapapi.search.geocode.GeoCoder;
import com.baidu.mapapi.search.geocode.OnGetGeoCoderResultListener;
import com.baidu.mapapi.search.geocode.ReverseGeoCodeOption;
import com.baidu.mapapi.search.geocode.ReverseGeoCodeResult;
import com.foxconn.rfid.theowner.R;
import com.foxconn.rfid.theowner.base.BaseActivity;
import com.foxconn.rfid.theowner.model.EventBusMsg;

import de.greenrobot.event.EventBus;


public class LostAddrMapActivity extends BaseActivity implements View.OnClickListener {
    private MapView mMapView;
    private BaiduMap mBaiduMap;
    private ImageView img_addr_back,img_addr_save;

    private TextView current_location_tv;
//    GeoCoder mSearch = null;  //BaiduLBS_Android.jar  下载的此架包暂不包含GeoCoder
    private GeoCoder mSearch = null;
    public static int RESULT_NUM = 0x9966;

    LocationClient mLocClient;
    public MyLocationListenner myListener = new MyLocationListenner();

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lost_address);
        initView();
        initMap();
    }


    private void initView() {

        mSearch = GeoCoder.newInstance();
        img_addr_back = findViewByIds(R.id.img_addr_back);
        img_addr_save = findViewByIds(R.id.img_addr_save);
        current_location_tv = findViewByIds(R.id.current_location_tv);
        initEvent();

    }



    private void initEvent() {

        img_addr_back.setOnClickListener(this);
        img_addr_save.setOnClickListener(this);
        //反解析地址
        mSearch.setOnGetGeoCodeResultListener(new OnGetGeoCoderResultListener() {
            @Override
            public void onGetGeoCodeResult(GeoCodeResult geoCodeResult) {

            }

            @Override
            public void onGetReverseGeoCodeResult(ReverseGeoCodeResult reverseGeoCodeResult) {
                current_location_tv.setText(reverseGeoCodeResult.getAddress());
            }
        });
    }

    private void initMap() {
        mMapView = findViewByIds(R.id.lost_address_map);
        // 隐藏 百度地图logo
        View child = mMapView.getChildAt(1);
        if (child != null && (child instanceof ImageView || child instanceof ZoomControls)) {
            child.setVisibility(View.INVISIBLE);
        }
        mMapView.showZoomControls(true); //false可取消地图层级显示(+-)
        mMapView.showScaleControl(true); //false不显示默认比例尺控件

        mBaiduMap = mMapView.getMap();
//        MapStatusUpdate msu = MapStatusUpdateFactory.zoomTo(18.0f);
//        mBaiduMap.setMapStatus(msu);
        mBaiduMap.setMyLocationEnabled(true); // 开启图层定位


        mLocClient = new LocationClient(this);
        mLocClient.registerLocationListener(myListener);
        initLocation(); //定位配置信息
        mLocClient.start();//开始定位

        slideMapListener();

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.img_addr_back:
                finish();
                break;
            case R.id.img_addr_save:
                EventBusMsg eventBusMsg = new EventBusMsg();
                eventBusMsg.setEmptyType(EventBusMsg.MsgEmptyType.MSG_GET_ADDRESS_TEXT);
                eventBusMsg.setTvBundle(current_location_tv.getText().toString());
                EventBus.getDefault().post(eventBusMsg);
                new Thread(){
                    @Override
                    public void run() {
                        try {
                            Thread.sleep(50);
                            LostAddrMapActivity.this.finish();
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }
                }.start();
                break;
        }
    }


    /**
     * 定位SDK监听函数
     */
    public class MyLocationListenner implements BDLocationListener {

        @Override
        public void onReceiveLocation(BDLocation location) {
            // map view 销毁后不在处理新接收的位置
            if (location == null || mMapView == null) {
                return;
            }
            MyLocationData locData = new MyLocationData.Builder()
                    .accuracy(location.getRadius())  //location.getRadius() 在wifi 与流量下,得到的结果值不同(40 与1000)
                    // 此处设置开发者获取到的方向信息，顺时针0-360
                    .direction(0).latitude(location.getLatitude())
                    .longitude(location.getLongitude()).build();
            mBaiduMap.setMyLocationData(locData);
            //地址解析
            mSearch.reverseGeoCode(new ReverseGeoCodeOption()
                    .location(new LatLng(location.getLatitude(), location.getLongitude())));

            LatLng ll = new LatLng(location.getLatitude(),
                    location.getLongitude());
            MapStatus.Builder builder = new MapStatus.Builder();
            builder.target(ll).zoom(18.0f);
            mBaiduMap.animateMapStatus(MapStatusUpdateFactory.newMapStatus(builder.build()));
        }

    }

    private void initLocation() {

//        LocationClientOption option = new LocationClientOption();
//        option.setOpenGps(true); // 打开gps
//        option.setCoorType("bd09ll"); // 设置坐标类型
//        option.setScanSpan(1000);
//        mLocClient.setLocOption(option);

        LocationClientOption option = new LocationClientOption();
        option.setLocationMode(LocationClientOption.LocationMode.Hight_Accuracy
        );//可选，默认高精度，设置定位模式，高精度，低功耗，仅设备
        option.setCoorType("bd09ll");//可选，默认gcj02，设置返回的定位结果坐标系

        int span= 0; //仅定位一次
//        int span= 20000; //每隔20秒定位一次
        option.setScanSpan(span);//可选，默认0，即仅定位一次，设置发起定位请求的间隔需要大于等于1000ms才是有效的
        option.setIsNeedAddress(true);//可选，设置是否需要地址信息，默认不需要
        option.setOpenGps(true);//可选，默认false,设置是否使用gps
        option.setLocationNotify(true);//可选，默认false，设置是否当gps有效时按照1S1次频率输出GPS结果
        option.setIsNeedLocationDescribe(true);//可选，默认false，设置是否需要位置语义化结果，可以在BDLocation.getLocationDescribe里得到，结果类似于“在北京天安门附近”
        option.setIsNeedLocationPoiList(true);//可选，默认false，设置是否需要POI结果，可以在BDLocation.getPoiList里得到
        option.setIgnoreKillProcess(false);//可选，默认true，定位SDK内部是一个SERVICE，并放到了独立进程，设置是否在stop的时候杀死这个进程，默认不杀死
        option.SetIgnoreCacheException(false);//可选，默认false，设置是否收集CRASH信息，默认收集
        option.setEnableSimulateGps(false);//可选，默认false，设置是否需要过滤gps仿真结果，默认需要
        mLocClient.setLocOption(option);
    }

    //地图移动监听
    public void slideMapListener(){
        mBaiduMap.setOnMapStatusChangeListener(new BaiduMap.OnMapStatusChangeListener() {
            @Override
            public void onMapStatusChangeStart(MapStatus mapStatus) {
            }

            @Override
            public void onMapStatusChangeFinish(MapStatus mapStatus) {

                mSearch.reverseGeoCode(new ReverseGeoCodeOption()
                        .location(mapStatus.target));
                MyLocationData locData = new MyLocationData.Builder()
                        .accuracy(40)  //location.getRadius() 在wifi 与流量下,得到的结果值不同(40 与1000)
                        // 此处设置开发者获取到的方向信息，顺时针0-360
                        .direction(0).latitude(mapStatus.target.latitude)
                        .longitude(mapStatus.target.longitude).build();
                mBaiduMap.setMyLocationData(locData);
            }

            @Override
            public void onMapStatusChange(MapStatus mapStatus) {

            }
        });
    }



    @Override
    protected void onPause() {
        // MapView的生命周期与Activity同步，当activity挂起时需调用MapView.onPause()
        mMapView.onPause();
        super.onPause();
    }

    @Override
    protected void onResume() {
        // MapView的生命周期与Activity同步，当activity恢复时需调用MapView.onResume()
        mMapView.onResume();
        super.onResume();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        mLocClient.stop();
        // 关闭定位图层
        mBaiduMap.setMyLocationEnabled(false);
        mMapView.onDestroy();

    }


}
