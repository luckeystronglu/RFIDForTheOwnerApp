/**
 *
 */
package com.foxconn.rfid.theowner.activity.main;

import android.annotation.SuppressLint;
import android.app.ActivityManager;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.ViewGroup.LayoutParams;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListPopupWindow;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ZoomControls;

import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;
import com.baidu.mapapi.map.BaiduMap;
import com.baidu.mapapi.map.BaiduMap.OnMapRenderCallback;
import com.baidu.mapapi.map.BitmapDescriptor;
import com.baidu.mapapi.map.BitmapDescriptorFactory;
import com.baidu.mapapi.map.MapPoi;
import com.baidu.mapapi.map.MapStatus;
import com.baidu.mapapi.map.MapStatusUpdate;
import com.baidu.mapapi.map.MapStatusUpdateFactory;
import com.baidu.mapapi.map.MapView;
import com.baidu.mapapi.map.Marker;
import com.baidu.mapapi.map.MarkerOptions;
import com.baidu.mapapi.map.OverlayOptions;
import com.baidu.mapapi.map.UiSettings;
import com.baidu.mapapi.model.LatLng;
import com.baidu.mapapi.search.geocode.GeoCodeResult;
import com.baidu.mapapi.search.geocode.GeoCoder;
import com.baidu.mapapi.search.geocode.OnGetGeoCoderResultListener;
import com.baidu.mapapi.search.geocode.ReverseGeoCodeOption;
import com.baidu.mapapi.search.geocode.ReverseGeoCodeResult;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.customview.GlideCircleImage;
import com.foxconn.rfid.theowner.R;
import com.foxconn.rfid.theowner.activity.LoginActivity;
import com.foxconn.rfid.theowner.activity.SettingActivity;
import com.foxconn.rfid.theowner.activity.main.adapter.BikeAdapter;
import com.foxconn.rfid.theowner.activity.main.defence.BikeLostDisposeActivity;
import com.foxconn.rfid.theowner.activity.message.ActivityMessageCenter;
import com.foxconn.rfid.theowner.base.App;
import com.foxconn.rfid.theowner.base.BaseActivity;
import com.foxconn.rfid.theowner.base.BaseApplication;
import com.foxconn.rfid.theowner.base.PreferenceData;
import com.foxconn.rfid.theowner.model.Bike;
import com.foxconn.rfid.theowner.model.EventBusMsg;
import com.foxconn.rfid.theowner.socket.SocketAppPacket;
import com.foxconn.rfid.theowner.socket.SocketClient;
import com.foxconn.rfid.theowner.util.logort.ToastUtils;
import com.google.protobuf.InvalidProtocolBufferException;
import com.yzh.rfidbike.app.request.GetBikeByBikeIdRequest;
import com.yzh.rfidbike.app.request.GetBikeCurLocationRequest;
import com.yzh.rfidbike.app.request.GetBikesRequest;
import com.yzh.rfidbike.app.response.GetBikeCurLocationResponse;
import com.yzh.rfidbike.app.response.GetBikesResponse;

import java.util.ArrayList;
import java.util.List;

/**
 * 主activity
 *
 * @author WT00111
 */
public class MainNewActivity extends BaseActivity implements View.OnClickListener,
        OnGetGeoCoderResultListener {
    /**
     * 个人消息按钮
     */
    private ImageButton btnBikeList, btnSetting;
    private ImageView btnDown;

    private TextView tvCarNum, tvCarModel;

    private LinearLayout ll_bike_home, ll_no_bike; //ll_no_bike 没有车辆的布局
    private LinearLayout ll_bike_num; //车牌选择的布局
    /**
     * 百度地图
     */
    private MapView mapView;
    private BaiduMap mBaiduMap;

    //车辆信息
    private LinearLayout carInfo;
    private ImageView img_circle;

    //历史轨迹、报失处理、我的保险
    private RelativeLayout rl_historical_track, rl_loss_disponse, rl_my_insurance;

    private boolean firstGetLocation = false;
    private boolean isFresh = false;

    /** 挂失索赔 */

    /**
     * 标题
     */
    private TextView tvHeaderTitle, tvAddress;
    private List<Bike> listBike;
    private int selectedIndex = 0;
    private BDLocationListener myListener = new MyLocationListener();
    private LocationClient mLocationClient = null;

    private double curLongitude = 0;
    private double curLatitude = 0;
    GeoCoder mSearch = null;

    private int bikeId = -1;
    private String isProtect;
    private Marker marker = null;
    private Bike disposeBike;


    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        initView();
        initEvent();
        initData();
        initMap();

        if (BaseApplication.getInstance().needGoToMessageCenter) {
            Intent intentNew = new Intent();
            intentNew.setClass(context.getApplicationContext(), ActivityMessageCenter.class);
            intentNew.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            context.getApplicationContext().startActivity(intentNew);
        }
    }


    protected void initView() {
        // 在使用SDK各组件之前初始化context信息，传入ApplicationContext
        // 注意该方法要再setContentView方法之前实现

        setContentView(R.layout.activity_main_new);
        btnBikeList = findViewByIds(R.id.btn_refresh_bikelist);
        btnSetting = findViewByIds(R.id.btn_setting);
        btnDown = findViewByIds(R.id.btn_down);
        mapView = findViewByIds(R.id.mapview);
        carInfo = findViewByIds(R.id.carInfo);

        ll_no_bike = findViewByIds(R.id.ll_no_bike);
        rl_historical_track = findViewByIds(R.id.rl_historical_track);
        rl_loss_disponse = findViewByIds(R.id.rl_loss_disponse);
        rl_my_insurance = findViewByIds(R.id.rl_my_insurance);

        ll_bike_num = findViewByIds(R.id.ll_bike_num);
        tvHeaderTitle = findViewByIds(R.id.new_main_title);
        tvCarNum = findViewByIds(R.id.tv_car_num);
        tvCarModel = findViewByIds(R.id.tv_car_model);
        tvAddress = findViewByIds(R.id.tv_address);
        ll_bike_home = findViewByIds(R.id.ll_bike_home);

        LinearLayout ll_home_center = findViewByIds(R.id.ll_home_center);
        ll_home_center.bringToFront();
        img_circle = findViewByIds(R.id.img_circle);

    }


    protected void initEvent() {
        btnSetting.setOnClickListener(this);
        btnBikeList.setOnClickListener(this);
        carInfo.setOnClickListener(this);

        ll_bike_num.setOnClickListener(this);

        rl_historical_track.setOnClickListener(this);
        rl_loss_disponse.setOnClickListener(this);
        rl_my_insurance.setOnClickListener(this);

    }

    @Override
    public void onBackPressed() {
        this.moveTaskToBack(false);
    }

    @Override
    protected void doSocket() {
        super.doSocket();
        final GetBikesRequest.GetBikesRequestMessage.Builder requestMessage = GetBikesRequest.GetBikesRequestMessage.newBuilder();
        requestMessage.setSession(PreferenceData.loadSession(this));
        requestMessage.setUserId(PreferenceData.loadLoginAccount(this));
        new Thread() {
            public void run() {
                SocketClient client;
                client = SocketClient.getInstance();
                client.send(SocketAppPacket.COMMAND_ID_get_bikes_by_user_id, requestMessage.build().toByteArray());
            }

        }.start();
    }

    private void getBikeLocation(long bikeId) {
        final GetBikeCurLocationRequest.GetBikeCurLocationRequestMessage.Builder requestMessage = GetBikeCurLocationRequest.GetBikeCurLocationRequestMessage.newBuilder();
        requestMessage.setSession(PreferenceData.loadSession(this));
        requestMessage.addBikeId(bikeId);
        new Thread() {
            public void run() {
                SocketClient client;
                client = SocketClient.getInstance();
                client.send(SocketAppPacket.COMMAND_ID_get_bikes_location, requestMessage.build().toByteArray());
            }
        }.start();
    }

    @Override
    public void onEventMainThread(SocketAppPacket eventPackage) {
        try {
            super.onEventMainThread(eventPackage);
            if (eventPackage.getCommandId() == SocketAppPacket.COMMAND_ID_get_bikes_by_user_id) {
                GetBikesResponse.GetBikesResponseMessage getBikesResponseMessage = GetBikesResponse.GetBikesResponseMessage.parseFrom(eventPackage.getCommandData());

                if (getBikesResponseMessage.getErrorMsg().getErrorCode() != 0) {
                    if (dlgWaiting.isShowing()) {
                        dlgWaiting.dismiss();
                    }
                    mDlgWaitingHandler.removeMessages(MSG_cannt_get_data);
                    Toast.makeText(this, getBikesResponseMessage.getErrorMsg().getErrorMsg(), Toast.LENGTH_LONG).show();
                    if (getBikesResponseMessage.getErrorMsg().getErrorCode() == 20003) {
                        Intent intent = new Intent();
                        intent.setClass(this, LoginActivity.class);
                        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                        startActivity(intent);
                    }
                } else {
                    listBike.clear();
                    for (com.yzh.rfidbike.app.response.Bike.BikeMessage bikeMessage : getBikesResponseMessage.getBikeList()) {
                        Bike bike = new Bike();
                        bike.id = bikeMessage.getId();
                        bike.plateNumber = bikeMessage.getPlateNumber();
                        bike.brandModel = bikeMessage.getBrandModel();
                        bike.color = bikeMessage.getColor();
                        bike.photo = bikeMessage.getPhoto();
                        bike.purchaseDate = bikeMessage.getPurchaseDate();
                        bike.purchasePrice = bikeMessage.getPurchasePrice();
                        bike.usageNature = bikeMessage.getUsageNature();
                        bike.loadNumber = bikeMessage.getLoadNumber();
                        bike.userId = bikeMessage.getUserId();
                        bike.cardId = bikeMessage.getCardId();
                        bike.cardNo = bikeMessage.getCardNo();
                        bike.status = bikeMessage.getStatus();
                        bike.lostStatus = bikeMessage.getLostStatus();
                        bike.insurance = bikeMessage.getInsurance();
                        bike.lostEndDate = bikeMessage.getLostEndDate();
                        bike.payoutStatus = bikeMessage.getPayoutStatus();
                        bike.cardBatteryStatus = bikeMessage.getCardBatteryStatus();
                        bike.protestValue = bikeMessage.getIsProtect(); //是否开启安防 0 不开启, 1 开启
                        bike.lostEndDate = bikeMessage.getLostEndDate(); //添加最后丢失日期20170522
                        bike.creatDate = bikeMessage.getCreateDate(); //创建日期(上牌日期)
//                        Log.d("TAG", "bikeMessage.getIsProtect(): " + bikeMessage.getIsProtect());
                        listBike.add(bike);
                    }
                    if (listBike.size() > 0) {
                        getBikeLocation(listBike.get(0).getId());
                        ll_no_bike.setVisibility(View.GONE);

                        if (isFresh) {
                            //刷新后获取车辆列表第一辆车的简单信息
                            refreshView(0);
                        }


                    } else {
                        if (dlgWaiting.isShowing()) {
                            dlgWaiting.dismiss();
                        }
                        mDlgWaitingHandler.removeMessages(MSG_cannt_get_data);
                        ll_no_bike.setVisibility(View.VISIBLE);
                        ll_bike_home.setVisibility(View.GONE);
                        tvHeaderTitle.setText("我的车辆");
//						Toast.makeText(this, "没有找到车辆", Toast.LENGTH_LONG).show();
                    }

                }
            } else if (eventPackage.getCommandId() == SocketAppPacket.COMMAND_ID_get_bikes_location) {
                if (dlgWaiting.isShowing()) {
                    dlgWaiting.dismiss();
                }
                mDlgWaitingHandler.removeMessages(MSG_cannt_get_data);
                GetBikeCurLocationResponse.GetBikeCurLocationResponseMessage getBikeCurLocationResponseMessage = GetBikeCurLocationResponse.GetBikeCurLocationResponseMessage.parseFrom(eventPackage.getCommandData());

                if (getBikeCurLocationResponseMessage.getErrorMsg().getErrorCode() != 0) {
                    Toast.makeText(this, getBikeCurLocationResponseMessage.getErrorMsg().getErrorMsg(), Toast.LENGTH_LONG).show();
                    if (getBikeCurLocationResponseMessage.getErrorMsg().getErrorCode() == 20003) {
                        Intent intent = new Intent();
                        intent.setClass(this, LoginActivity.class);
                        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                        startActivity(intent);
                    }
                    //没有获取到车辆位置，获取当前位置
                    initBaiduLocation();
                } else {
                    if (!firstGetLocation) {
                        refreshView(0);
                    }

                    addMarkOnBaiduMap(getBikeCurLocationResponseMessage.getLocations(0).getLatitude() < 0.1 ? 39.963175 : getBikeCurLocationResponseMessage.getLocations(0).getLatitude(),
                            getBikeCurLocationResponseMessage.getLocations(0).getLongitude() < 0.1 ? 116.400244 : getBikeCurLocationResponseMessage.getLocations(0).getLongitude());
                }
            }
            else if (eventPackage.getCommandId() == SocketAppPacket.COMMAND_ID_GETBIKEMSG_BY_BIKEID && BaseApplication.getInstance().getbyidflag.equals("MainNewActivity")){
                final GetBikesResponse.GetBikesResponseMessage getBikesResponseMessage = GetBikesResponse.GetBikesResponseMessage.parseFrom(eventPackage.getCommandData());

                if (getBikesResponseMessage.getErrorMsg().getErrorCode() != 0) {
                    Toast.makeText(this, getBikesResponseMessage.getErrorMsg().getErrorMsg(), Toast.LENGTH_LONG).show();
                    if (getBikesResponseMessage.getErrorMsg().getErrorCode() == 20003) {
                        Intent intent = new Intent();
                        intent.setClass(this, LoginActivity.class);
                        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                        startActivity(intent);
                    }
                } else {
                    com.yzh.rfidbike.app.response.Bike.BikeMessage bikeMessage = getBikesResponseMessage.getBikeList().get(0);
                    String payoutStatus = bikeMessage.getPayoutStatus();
                    String lostStatus = bikeMessage.getLostStatus();
                    long bkid = listBike.get(selectedIndex).getId();
                    if (bkid == bikeId) {
                        listBike.get(selectedIndex).setLostStatus(lostStatus);
                        listBike.get(selectedIndex).setPayoutStatus(payoutStatus);
                    }else {
                        doSocket();
                    }

                }
            }else if (eventPackage.getCommandId() == SocketAppPacket.COMMAND_ID_GETBIKEMSG_BY_BIKEID && BaseApplication.getInstance().getbyidflag.equals("SettingActivity")){
                final GetBikesResponse.GetBikesResponseMessage getBikesResponseMessage = GetBikesResponse.GetBikesResponseMessage.parseFrom(eventPackage.getCommandData());

                if (getBikesResponseMessage.getErrorMsg().getErrorCode() != 0) {
                    Toast.makeText(this, getBikesResponseMessage.getErrorMsg().getErrorMsg(), Toast.LENGTH_LONG).show();
                    if (getBikesResponseMessage.getErrorMsg().getErrorCode() == 20003) {
                        Intent intent = new Intent();
                        intent.setClass(this, LoginActivity.class);
                        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                        startActivity(intent);
                    }
                } else {

                    long bkid = listBike.get(selectedIndex).getId();
                    if (bkid == bikeId) {
                        listBike.get(selectedIndex).setProtestValue(isProtect);
                    }else {
                        for (int i = 0; i < listBike.size(); i++) {
                            if (listBike.get(i).getId() == bikeId) {
                                listBike.get(i).setProtestValue(isProtect);
                            }
                        }
                    }

                }
            }else if (eventPackage.getCommandId() == SocketAppPacket.COMMAND_ID_GETBIKEMSG_BY_BIKEID && BaseApplication.getInstance().getbyidflag.equals("JumpToDisposeActivity")){
                final GetBikesResponse.GetBikesResponseMessage getBikesResponseMessage = GetBikesResponse.GetBikesResponseMessage.parseFrom(eventPackage.getCommandData());

                if (getBikesResponseMessage.getErrorMsg().getErrorCode() != 0) {
                    Toast.makeText(this, getBikesResponseMessage.getErrorMsg().getErrorMsg(), Toast.LENGTH_LONG).show();
                    if (getBikesResponseMessage.getErrorMsg().getErrorCode() == 20003) {
                        Intent intent = new Intent();
                        intent.setClass(this, LoginActivity.class);
                        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                        startActivity(intent);
                    }
                } else {

                    com.yzh.rfidbike.app.response.Bike.BikeMessage bikeMessage = getBikesResponseMessage.getBikeList().get(0);
                    String payoutStatus = bikeMessage.getPayoutStatus();
                    String lostStatus = bikeMessage.getLostStatus();
                    long bkid = listBike.get(selectedIndex).getId();
                    Intent ine = new Intent(MainNewActivity.this, BikeLostDisposeActivity.class);
                    if (bkid == bikeId) {
                        disposeBike.setLostStatus(lostStatus);
                        disposeBike.setPayoutStatus(payoutStatus);

                        ine.putExtra("BIKE", disposeBike);
                        ine.putExtra("bikeId",listBike.get(selectedIndex).getId());
                        startActivity(ine);

                    }else {
                        doSocket();
                    }


                }
            }


        } catch (InvalidProtocolBufferException e) {
            e.printStackTrace();
        }

    }

    protected void initData() {

        listBike = new ArrayList<>();
        doSocket();
        mBaiduMap = mapView.getMap();
        mBaiduMap.setOnMapRenderCallbadk(new OnMapRenderCallback() {
            @Override
            public void onMapRenderFinished() {

            }
        });
    }

    protected void refreshView(int selectIndex) {
        firstGetLocation = true;
        isFresh = false;
        if (listBike == null || listBike.size() < 2) {
            btnDown.setVisibility(View.GONE);

        } else {
            btnDown.setVisibility(View.VISIBLE);
        }

        selectedIndex = selectIndex;
        tvHeaderTitle.setText(listBike.get(selectIndex).getPlateNumber());
        tvCarNum.setText(listBike.get(selectIndex).getPlateNumber());
        tvCarModel.setText(listBike.get(selectIndex).getBrandModel());

        getBikeLocation(listBike.get(selectIndex).getId());

        Glide.with(this)
                .load(listBike.get(selectIndex).getPhoto())
                .transform(new GlideCircleImage(this))
                .placeholder(R.drawable.bikeimage)
//				.transform(new GlideCircleTransform(this))
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .thumbnail(0.1f)
                .crossFade(500)
                .into(img_circle);

    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_setting:
                Intent ie = new Intent(this, SettingActivity.class);
                if (listBike.size() > 0) {
                    Bike bike = listBike.get(selectedIndex);
                    ie.putExtra("bikeId",listBike.get(selectedIndex).getId());
                    ie.putExtra("BIKE", bike);
                }
                startActivity(ie);
                break;


            case R.id.btn_refresh_bikelist:
//			startActivity(new Intent(this, MessageActivity.class));
                isFresh = true;
                doSocket();
                break;


            case R.id.carInfo:// 车辆信息
                if (listBike.size() > 0) {
                    Bike bike = listBike.get(selectedIndex);
                    Intent intent = new Intent(this, VehicleInfoActivity.class);
                    intent.putExtra("BIKE", bike);
                    startActivity(intent);
                } else {
                    Toast.makeText(this, "没有选中的车辆", Toast.LENGTH_LONG).show();
                }
                break;


            case R.id.ll_bike_num:
                selectBike();
                btnDown.setImageDrawable(getResources().getDrawable(R.drawable.icon_up));
                break;


            case R.id.rl_historical_track:
                if (listBike.size() > 0) {
                    Intent i = new Intent();
                    Bike b = listBike.get(selectedIndex);
                    i.putExtra("BIKE", b);
                    i.setClass(this, HistoricalTrackActivity.class);
                    startActivity(i);
                } else {
                    Toast.makeText(this, "没有选中的车辆", Toast.LENGTH_LONG).show();
                }
                break;

            case R.id.rl_loss_disponse:

                if (listBike.size() > 0) {
                    disposeBike = listBike.get(selectedIndex);
                    bikeId = (int) disposeBike.getId();

                    BaseApplication.getInstance().getbyidflag = "JumpToDisposeActivity";
                    getBikeStatusById();

//                    Intent ine = new Intent(MainNewActivity.this, BikeLostDisposeActivity.class);
//                    ine.putExtra("BIKE", disposeBike);
//                    ine.putExtra("bikeId",listBike.get(selectedIndex).getId());
//                    startActivity(ine);
                }

                break;


            case R.id.rl_my_insurance:

                Intent intentInsurance = new Intent();
                if (listBike.size() > 0) {
                    Bike bikeInsurance = listBike.get(selectedIndex);
                    intentInsurance.putExtra("BIKE", bikeInsurance);
                }
                intentInsurance.setClass(this, MyInsuranceActivity.class);
                startActivity(intentInsurance);

                break;


            default:
                break;
        }
    }

    private ListPopupWindow mListPop;

    @SuppressLint("NewApi")
    private void selectBike() {

        if (listBike == null || listBike.size() < 2) {
            return;
        }
        mListPop = new ListPopupWindow(this, null, android.R.attr.listPopupWindowStyle,
                android.R.style.Widget_Holo_Light_ListPopupWindow);
        BikeAdapter adapter = new BikeAdapter(this, listBike);
        mListPop.setAdapter(adapter);
//		mListPop.setWidth(LayoutParams.WRAP_CONTENT);
        mListPop.setWidth(400);
        mListPop.setHeight(LayoutParams.WRAP_CONTENT);
        mListPop.setHorizontalOffset(-80);
        mListPop.setAnchorView(tvHeaderTitle);// 设置ListPopupWindow的锚点，即关联PopupWindow的显示位置和这个锚点
        mListPop.setModal(true);// 设置是否是模式
        mListPop.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                refreshView(position);
                btnDown.setImageDrawable(getResources().getDrawable(R.drawable.icon_dn));
                mListPop.dismiss();
            }
        });
        mListPop.show();
        mListPop.setOnDismissListener(new PopupWindow.OnDismissListener() {
            @Override
            public void onDismiss() {
                btnDown.setImageDrawable(getResources().getDrawable(R.drawable.icon_dn));
            }
        });
    }


    private void addMarkOnBaiduMap(double latitude, double longitude) {

        mSearch = GeoCoder.newInstance();
        mSearch.setOnGetGeoCodeResultListener(MainNewActivity.this);
        LatLng ptCenter = new LatLng(latitude, longitude);
        // 反Geo搜索
        mSearch.reverseGeoCode(new ReverseGeoCodeOption()
                .location(ptCenter));

        LatLng point = new LatLng(latitude, longitude);

        // 设置地图中心点，默认是天安门
        MapStatusUpdate mapstatusUpdatePoint = MapStatusUpdateFactory.newLatLngZoom(point, 14);
        mBaiduMap.setMapStatus(mapstatusUpdatePoint);

        // 构建Marker图标
        BitmapDescriptor bitmap = BitmapDescriptorFactory.fromResource(R.drawable.bike);

        // 构建MarkerOption，用于在地图上添加Marker
        OverlayOptions option = new MarkerOptions().position(point).icon(bitmap);
        // 在地图上添加Marker，并显示
        if (marker != null) {
            marker.remove();
        }
        marker = (Marker) mBaiduMap.addOverlay(option);

    }

    /**
     * 初始化地图
     */
    public void initMap() {
        // 定义Maker坐标点
//        LatLng point = new LatLng(29.607395, 106.378996);
//
//        MapStatusUpdate mapstatusUpdatePoint = MapStatusUpdateFactory.newLatLngZoom(point, 14);
//        mBaiduMap.setMapStatus(mapstatusUpdatePoint);

        mapView.showScaleControl(false);
        mapView.showZoomControls(false);
        // 隐藏 百度地图logo
        View child = mapView.getChildAt(1);
        if (child != null && (child instanceof ImageView || child instanceof ZoomControls)) {
            child.setVisibility(View.INVISIBLE);
        }
        UiSettings settings = mBaiduMap.getUiSettings();
//		settings.setAllGesturesEnabled(true); // 关闭一切手势操作
        settings.setOverlookingGesturesEnabled(false);// 屏蔽双指下拉时变成3D地图
        settings.setZoomGesturesEnabled(true);// 获取是否允许缩放手势返回:是否允许缩放手势
        mBaiduMap.setBuildingsEnabled(true);
//        mBaiduMap.setCompassPosition(new Point(20, 20));
        mBaiduMap.setOnMapClickListener(new BaiduMap.OnMapClickListener() {
            @Override
            public void onMapClick(LatLng latLng) {
                if (listBike.size() == 0) {
                    ToastUtils.showTextToast(MainNewActivity.this, getResources().getString(R.string.none_bike));
                    return;
                }

                Bike bike = listBike.get(selectedIndex);
                //确认已丢失车辆(包括被盗),不能点击进入车辆地图位置
                if (bike.getLostStatus().equals("4")) {
                    return;
                }
                //跳转至围栏列表Activity
//                else {
//                    Intent intent = new Intent(MainActivity.this, EleFenceListActivity.class);
//                    intent.putExtra("BIKE", bike);
//                    startActivity(intent);
//                }

            }

            @Override
            public boolean onMapPoiClick(MapPoi mapPoi) {
                return false;
            }
        });
//		initBaiduLocation();
    }


    //获取定位
    private void initBaiduLocation() {
        mLocationClient = new LocationClient(getApplicationContext());     //声明LocationClient类
        mLocationClient.registerLocationListener(myListener);    //注册监听函数
        initLocation();
        mLocationClient.start();
    }

    private void initLocation() {
        LocationClientOption option = new LocationClientOption();
        option.setLocationMode(LocationClientOption.LocationMode.Hight_Accuracy
        );//可选，默认高精度，设置定位模式，高精度，低功耗，仅设备
        option.setCoorType("bd09ll");//可选，默认gcj02，设置返回的定位结果坐标系
        int span = 0;
        option.setScanSpan(span);//可选，默认0，即仅定位一次，设置发起定位请求的间隔需要大于等于1000ms才是有效的
        option.setIsNeedAddress(true);//可选，设置是否需要地址信息，默认不需要
        option.setOpenGps(true);//可选，默认false,设置是否使用gps
        option.setLocationNotify(true);//可选，默认false，设置是否当GPS有效时按照1S/1次频率输出GPS结果
        option.setIsNeedLocationDescribe(true);//可选，默认false，设置是否需要位置语义化结果，可以在BDLocation.getLocationDescribe里得到，结果类似于“在北京天安门附近”
        option.setIsNeedLocationPoiList(true);//可选，默认false，设置是否需要POI结果，可以在BDLocation.getPoiList里得到
        option.setIgnoreKillProcess(false);//可选，默认true，定位SDK内部是一个SERVICE，并放到了独立进程，设置是否在stop的时候杀死这个进程，默认不杀死
        option.SetIgnoreCacheException(false);//可选，默认false，设置是否收集CRASH信息，默认收集
        option.setEnableSimulateGps(false);//可选，默认false，设置是否需要过滤GPS仿真结果，默认需要
        mLocationClient.setLocOption(option);
    }

    public class MyLocationListener implements BDLocationListener {
        @Override
        public void onReceiveLocation(BDLocation location) {

            curLatitude = location.getLatitude();
            curLongitude = location.getLongitude();
            tvAddress.setText(location.getAddrStr());

            mLocationClient.stop();
            LatLng point = new LatLng(curLatitude, curLongitude);
            // 构建Marker图标
//			BitmapDescriptor bitmap = BitmapDescriptorFactory.fromResource(R.drawable.orp);
//			MarkerOptions option = new MarkerOptions().position(point).icon(bitmap);
//			option.draggable(true);
//			addOverlay(option, "locationIcon");
            //调整地图的中心位置及缩放比例
            LatLng ll = new LatLng(location.getLatitude(),
                    location.getLongitude());
            MapStatus.Builder builder = new MapStatus.Builder();
            builder.target(ll).zoom(14.0f);
            mBaiduMap.animateMapStatus(MapStatusUpdateFactory.newMapStatus(builder.build()));
        }
    }

    @Override
    protected void onDestroy() {
        // 卸载super的前后是没有却别的
        mapView.onDestroy();
        mBaiduMap.setMyLocationEnabled(false);
        super.onDestroy();
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
    public void onEventMainThread(EventBusMsg eventPackage) {
        switch (eventPackage.getMsgType()) {
//            case EventBusMsg.MSG_REFRESH_BIKE_STATUS:
//                doSocket();
//                break;

            case EventBusMsg.MSG_APPLY_CASH_FINISH: //提现之后刷新(索赔状态为4)
                doSocket();
                break;

            case EventBusMsg.BIKE_STATUS_REFRESH: // 刷新状态
                doSocket();
                break;

            case EventBusMsg.BIKE_STATUS_REFRESH_MAIN:
                BaseApplication.getInstance().getbyidflag = "MainNewActivity";
                bikeId = eventPackage.getValue();
                if (bikeId != -1) {
                    getBikeStatusById();
                }

                break;

            case EventBusMsg.BIKE_SETTING_REFRESH_MAIN:
                BaseApplication.getInstance().getbyidflag = "SettingActivity";
                bikeId = eventPackage.getValue();
                isProtect = eventPackage.getTvBundle();
                if (bikeId != -1) {
                    getBikeStatusById();
                }

                break;

        }
    }

    private void getBikeStatusById() {
//        super.doSocket();
        final GetBikeByBikeIdRequest.GetBikeByBikeIdRequestMessage.Builder bikestatusMsg = GetBikeByBikeIdRequest.GetBikeByBikeIdRequestMessage.newBuilder();
//        bikestatusMsg.setSession(PreferenceData.loadSession(this));

        bikestatusMsg.setBikeId(bikeId);
        new Thread() {
            public void run() {
                SocketClient client;
                client = SocketClient.getInstance();
                client.send(SocketAppPacket.COMMAND_ID_GETBIKEMSG_BY_BIKEID, bikestatusMsg.build().toByteArray());
            }
        }.start();
    }

    @Override
    public void onGetGeoCodeResult(GeoCodeResult result) {

    }

    @Override
    public void onGetReverseGeoCodeResult(ReverseGeoCodeResult result) {

        tvAddress.setText(result.getAddress());
        //result保存翻地理编码的结果 坐标-->城市
    }


    @Override
    protected void onNewIntent(Intent intent) {
        if (BaseApplication.getInstance().needGoToMessageCenter) {
            Intent intentNew = new Intent();
//			intentNew.putExtras(intent.getExtras());
            intentNew.setClass(context.getApplicationContext(), ActivityMessageCenter.class);
            intentNew.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            context.getApplicationContext().startActivity(intentNew);
        }
    }

    //连续按键两次退出app
    private long presstime;

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
/**
 * web.canGoBack()判断webview是否有可以返回的页面
 */
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            if (System.currentTimeMillis() - presstime < 2000) {
                App.exitSystem(this);
            } else {
                Toast.makeText(getApplicationContext(), "再按一次退出应用", Toast.LENGTH_SHORT).show();
                presstime = System.currentTimeMillis();
            }

        }
        return true;
    }

    protected boolean isMessageCenterTop() {
        ActivityManager manager = (ActivityManager) getSystemService(Context.ACTIVITY_SERVICE);
        String name = manager.getRunningTasks(1).get(0).topActivity.getClassName();
        return name.equals(ActivityMessageCenter.class);
    }
}
