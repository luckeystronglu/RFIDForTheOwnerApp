package com.foxconn.rfid.theowner.activity.main;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;
import com.baidu.location.LocationClient;
import com.baidu.mapapi.map.BaiduMap;
import com.baidu.mapapi.map.BitmapDescriptor;
import com.baidu.mapapi.map.BitmapDescriptorFactory;
import com.baidu.mapapi.map.InfoWindow;
import com.baidu.mapapi.map.MapStatus;
import com.baidu.mapapi.map.MapStatusUpdate;
import com.baidu.mapapi.map.MapStatusUpdateFactory;
import com.baidu.mapapi.map.MapView;
import com.baidu.mapapi.map.Marker;
import com.baidu.mapapi.map.MarkerOptions;
import com.baidu.mapapi.model.LatLng;
import com.baidu.mapapi.search.geocode.GeoCodeResult;
import com.baidu.mapapi.search.geocode.GeoCoder;
import com.baidu.mapapi.search.geocode.OnGetGeoCoderResultListener;
import com.baidu.mapapi.search.geocode.ReverseGeoCodeOption;
import com.baidu.mapapi.search.geocode.ReverseGeoCodeResult;
import com.customview.roomorama.caldroid.CaldroidFragment;
import com.customview.roomorama.caldroid.CaldroidListener;
import com.foxconn.rfid.theowner.R;
import com.foxconn.rfid.theowner.activity.LoginActivity;
import com.foxconn.rfid.theowner.base.BaseActivity;
import com.foxconn.rfid.theowner.base.PreferenceData;
import com.foxconn.rfid.theowner.model.Bike;
import com.foxconn.rfid.theowner.socket.SocketAppPacket;
import com.foxconn.rfid.theowner.socket.SocketClient;
import com.foxconn.rfid.theowner.util.logort.ToastUtils;
import com.foxconn.rfid.theowner.util.string.DateUtils;
import com.foxconn.rfid.theowner.view.PlayView;
import com.google.protobuf.InvalidProtocolBufferException;
import com.yzh.rfidbike.app.request.GetBikeTrackRequest;
import com.yzh.rfidbike.app.response.GetBikeTrackResponse;
import com.yzh.rfidbike.app.response.Location;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class HistoricalTrackActivity extends BaseActivity {
    private MapView mapView;
    private BaiduMap mBaiduMap;
    private List<Location.LocationMessage> mLocations;
    private Bike mBike;
    private Button mBtnDate;
    private ImageView mIvBack;
    private ImageView mIvForward;
    private CaldroidFragment caldroidFragment;
    private PlayView mSpeedView;
    private long mTrackTime;
    private int mCount;
    private LinearLayout mLldate;
    private long mDayTime = 1000 * 60 * 60 * 24;
    BitmapDescriptor bdA = BitmapDescriptorFactory
            .fromResource(R.drawable.track_point);
    BitmapDescriptor bdb = BitmapDescriptorFactory
            .fromResource(R.drawable.bike);
    private InfoWindow mInfoWindow;
    private boolean isDestory = false;
    private MarkerOptions markerOption;
    private MarkerOptions lastMarkerOption;
    private Marker marker;
    private Marker lastMarker;
    LocationClient mLocClient;
    private TextView mTvToday;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_historical_track);
        initView();
        initData();
        initEvent(savedInstanceState);

    }


    protected void initView() {
        mTvToday = (TextView) findViewById(R.id.tv_today);
        mLldate = (LinearLayout) findViewById(R.id.ll_date);
        mIvBack = (ImageView) findViewById(R.id.iv_back);
        mIvForward = (ImageView) findViewById(R.id.iv_forward);
        mBtnDate = (Button) findViewById(R.id.btn_date);
        mapView = (MapView) findViewById(R.id.mapview);
        mSpeedView = (PlayView) findViewById(R.id.view_playSpeed);
        mBaiduMap = mapView.getMap();
        MapStatusUpdate msu = MapStatusUpdateFactory.zoomTo(14.0f);
        mBaiduMap.setMapStatus(msu);


    }

    private void locate(final List<LatLng> latLngList) {
        if (latLngList.size() == 0) {
            mLocClient = new LocationClient(this);
            mLocClient.registerLocationListener(new MyLocationListenner());
            mLocClient.start();
            dlgWaiting.show();
            return;
        }
        LatLng latLng = latLngList.get(0);
        MapStatus mMapStatus = new MapStatus.Builder()
                .target(latLng)
                .zoom(14)
                .build();
        MapStatusUpdate mMapStatusUpdate = MapStatusUpdateFactory.newMapStatus(mMapStatus);
        mBaiduMap.setMapStatus(mMapStatusUpdate);
        markerOption = new MarkerOptions().position(latLng).icon(bdb).zIndex(12).draggable(true);
        marker = (Marker) mBaiduMap.addOverlay(markerOption);

        mBaiduMap.setOnMarkerClickListener(new BaiduMap.OnMarkerClickListener() {
            @Override
            public boolean onMarkerClick(final Marker marker) {
                final Button button = new Button(getApplicationContext());
                button.setTextSize(10f);
                button.setWidth(600);
                button.setHeight(100);
                button.setBackgroundColor(getResources().getColor(R.color.white));
                button.setTextColor(getResources().getColor(R.color.blue));
                LatLng ll = marker.getPosition();
                GeoCoder geoCoder = GeoCoder.newInstance();
                geoCoder.reverseGeoCode(new ReverseGeoCodeOption().location(ll));
                geoCoder.setOnGetGeoCodeResultListener(new OnGetGeoCoderResultListener() {
                    @Override
                    public void onGetGeoCodeResult(GeoCodeResult geoCodeResult) {
                    }

                    @Override
                    public void onGetReverseGeoCodeResult(ReverseGeoCodeResult
                                                                  reverseGeoCodeResult) {
                        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd " +
                                "HH-mm-ss");
                        final String time = simpleDateFormat.format(mLocations.get(mCount)
                                .getCreateDate());
                        button.setText(time + "\n" + reverseGeoCodeResult.getAddress
                                ());

                    }
                });

                mInfoWindow = new InfoWindow(button, ll, -47);
                mBaiduMap.showInfoWindow(mInfoWindow);
                return true;
            }
        });
    }

    protected void initData() {
        mBaiduMap.setMyLocationEnabled(true);// 打开定位图层

        mBike = (Bike) getIntent().getSerializableExtra("BIKE");
        mTrackTime = System.currentTimeMillis();
        doSocket();

    }

    private void initEvent(Bundle savedInstanceState) {
        caldroidFragment = new CaldroidFragment();


        if (savedInstanceState != null) {
            caldroidFragment.restoreStatesFromKey(savedInstanceState, "CALDROID_SAVED_STATE");
        } else {
            Bundle args = new Bundle();
            Calendar cal = Calendar.getInstance();
            args.putInt(CaldroidFragment.MONTH, cal.get(Calendar.MONTH) + 1);
            args.putInt(CaldroidFragment.YEAR, cal.get(Calendar.YEAR));
            args.putBoolean(CaldroidFragment.ENABLE_SWIPE, true);
            args.putBoolean(CaldroidFragment.SIX_WEEKS_IN_CALENDAR, true);

            caldroidFragment.setArguments(args);
        }
        Calendar c = Calendar.getInstance();// 可以对每个时间域单独修改
        int year = c.get(Calendar.YEAR);
        int month = c.get(Calendar.MONTH) + 1;
        int date = c.get(Calendar.DATE);
        mBtnDate.setText(year + "年" + month + "月" + date + "日");
        mBtnDate.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View arg0) {
                showCaldroid();
                mLldate.setVisibility(View.GONE);

            }
        });

        mSpeedView = (PlayView) findViewById(R.id.view_playSpeed);

        mIvBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mTrackTime -= mDayTime;
                Date d = new Date(mTrackTime);
                caldroidFragment.setSelectedDate(d);
                mBtnDate.setText(DateUtils.formatDate(d, "yyyy年MM月dd日"));
                doSocket();
                mBaiduMap.clear();
            }
        });
        mIvForward.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mTrackTime += mDayTime;
                Date d = new Date(mTrackTime);
                caldroidFragment.setSelectedDate(d);
                mBtnDate.setText(DateUtils.formatDate(d, "yyyy年MM月dd日"));
                doSocket();
                mBaiduMap.clear();

            }
        });

        mTvToday.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mTrackTime = System.currentTimeMillis();
                Date d = new Date(mTrackTime);
                caldroidFragment.setSelectedDate(d);
                mBtnDate.setText(DateUtils.formatDate(d, "yyyy年MM月dd日"));
                doSocket();
                mBaiduMap.clear();
                FragmentTransaction t = getSupportFragmentManager().beginTransaction();
                t.hide(caldroidFragment);
                t.commit();
                mLldate.setVisibility(View.VISIBLE);
            }
        });
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        isDestory = true;
        mBaiduMap.setMyLocationEnabled(false);
        mapView.onDestroy();
        if (mLocClient != null) {
            mLocClient.stop();
        }
        bdA.recycle();
        bdb.recycle();


    }

    @Override
    protected void onPause() {
        super.onPause();
        mapView.onPause();
    }

    @Override
    protected void onResume() {
        mapView.onResume();
        super.onResume();
    }

    @Override
    protected void doSocket() {
        super.doSocket();
        mSpeedView.restore();
        final GetBikeTrackRequest.GetBikeTrackRequestMessage.Builder requestMessage =
                GetBikeTrackRequest.GetBikeTrackRequestMessage.newBuilder();
        requestMessage.setSession(PreferenceData.loadSession(this));
        requestMessage.setBikeId(mBike.getId());
        requestMessage.setSelectedDate(mTrackTime);
        new Thread() {
            public void run() {
                SocketClient client;
                client = SocketClient.getInstance();
                client.send(SocketAppPacket.COMMAND_ID_get_bike_track, requestMessage.build()
                        .toByteArray());
            }
        }.start();
    }

    @Override
    public void onEventMainThread(SocketAppPacket eventPackage) {
        super.onEventMainThread(eventPackage);
        if (eventPackage.getCommandId() == SocketAppPacket.COMMAND_ID_get_bike_track) {
            try {
                GetBikeTrackResponse.GetBikeTrackResponseMessage bikeTrackResponseMessage =
                        GetBikeTrackResponse.GetBikeTrackResponseMessage.
                                parseFrom(eventPackage.getCommandData());
                if (dlgWaiting.isShowing()) {
                    dlgWaiting.dismiss();
                }
                mDlgWaitingHandler.removeMessages(MSG_cannt_get_data);
                if (bikeTrackResponseMessage.getErrorMsg().getErrorCode() != 0) {
                    Toast.makeText(HistoricalTrackActivity.this, bikeTrackResponseMessage
                            .getErrorMsg().getErrorMsg(), Toast.LENGTH_LONG).show();
                    if (bikeTrackResponseMessage.getErrorMsg().getErrorCode() == 20003) {
                        Intent intent = new Intent();
                        intent.setClass(this, LoginActivity.class);
                        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK|Intent.FLAG_ACTIVITY_CLEAR_TASK);
                        startActivity(intent);
                    }
                } else {
                    mLocations = bikeTrackResponseMessage.getLocationsList();
                    final List<LatLng> latLngList = new ArrayList<>();
                    for (int i = 0; i < mLocations.size(); i++) {
                        LatLng latLng = new LatLng(mLocations.get(i).getLatitude(), mLocations
                                .get(i).getLongitude());
                        latLngList.add(latLng);
                    }
                    boolean play = mLocations.size() != 0;
                    if (!play) {
                        ToastUtils.showTextToast(HistoricalTrackActivity.this, "没有轨迹");
                        mSpeedView.setVisibility(View.GONE);
                    } else {
                        mSpeedView.setVisibility(View.VISIBLE);

                    }

                    locate(latLngList);


                    mSpeedView.setToggleChangeListener(mLocations.size(), play, new PlayView
                            .OnPlayToggleChangeListener() {


                        @Override
                        public void notToggle() {
                            ToastUtils.showTextToast(HistoricalTrackActivity.this, "没有轨迹");
                        }

                        @Override
                        public void toggleOn(int position, boolean first) {
                            if (isDestory) return;
                            if (position == 0) {
                                mBaiduMap.clear();
                            }
                            if (first) {
                                position++;
                            }

                            mCount = position;
                            lastMarker = marker;
                            if (lastMarker != null) {
                                lastMarker.remove();
                            }
                            if (position > 0) {
                                lastMarkerOption = new MarkerOptions().position
                                        (latLngList
                                                .get(position - 1)).icon(bdA).zIndex(12)
                                        .draggable(true);
                                mBaiduMap.addOverlay(lastMarkerOption);

                            }

                            if (position == latLngList.size()) {
                                return;
                            }
                            markerOption = new MarkerOptions().position(latLngList
                                    .get(position)).icon(bdb).zIndex(12).draggable(true);
                            marker = (Marker) mBaiduMap.addOverlay(markerOption);

                            MapStatus mMapStatus = new MapStatus.Builder()
                                    .target(latLngList.get(position))
                                    .build();
                            MapStatusUpdate mMapStatusUpdate = MapStatusUpdateFactory
                                    .newMapStatus(mMapStatus);

                            mBaiduMap.animateMapStatus(mMapStatusUpdate);
                        }

                        @Override
                        public void toggleOff() {

                        }
                    });
                    mSpeedView.setOnPlaySpeedChangeListener(play, new PlayView
                            .OnPlaySpeedChangeListener() {


                        @Override
                        public void onNotChange() {
                            ToastUtils.showTextToast(HistoricalTrackActivity.this, "没有轨迹");
                        }

                    });

                    if (mLocations.size() == 0) {
                        mSpeedView.setBackgroundResource(R.drawable.pause);
                    } else {
                        mSpeedView.setBackgroundResource(R.drawable.background_play_control);
                    }

                }
            } catch (InvalidProtocolBufferException e) {
                e.printStackTrace();
            }

        }
    }


    private void showCaldroid() {
        final SimpleDateFormat formatter = new SimpleDateFormat("yyyy年MM月dd日");

        FragmentTransaction t = getSupportFragmentManager().beginTransaction();
        if (caldroidFragment.isAdded()) {
            t.show(caldroidFragment);

        } else
            t.add(R.id.calendar, caldroidFragment);
        t.commit();
        // Setup listener
        CaldroidListener listener = new CaldroidListener() {

            @Override
            public void onSelectDate(Date date, View view) {
                mBtnDate.setText(formatter.format(date));
                FragmentTransaction t = getSupportFragmentManager().beginTransaction();
                t.hide(caldroidFragment);
                t.commit();
                mTrackTime = date.getTime();
                doSocket();
                mBaiduMap.clear();
                mLldate.setVisibility(View.VISIBLE);
            }


            @Override
            public void onChangeMonth(int month, int year) {
            }

            @Override
            public void onLongClickDate(Date date, View view) {


            }

            @Override
            public void onCaldroidViewCreated() {
            }

        };

        caldroidFragment.setCaldroidListener(listener);
    }


    public class MyLocationListenner implements BDLocationListener {

        @Override
        public void onReceiveLocation(BDLocation location) {
            if (location == null || mBaiduMap == null) {
                return;
            }

            LatLng latLng = new LatLng(location.getLatitude(), location.getLongitude());
            MapStatus mMapStatus = new MapStatus.Builder()
                    .target(latLng)
                    .zoom(14)
                    .build();
            MapStatusUpdate mMapStatusUpdate = MapStatusUpdateFactory.newMapStatus(mMapStatus);
            mBaiduMap.setMapStatus(mMapStatusUpdate);
            if (dlgWaiting.isShowing()) {
                dlgWaiting.dismiss();
            }
        }

    }

}
