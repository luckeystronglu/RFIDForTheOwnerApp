/**
 *
 */
package com.foxconn.rfid.theowner.activity.main;

import android.annotation.SuppressLint;
import android.app.ActivityManager;
import android.app.Notification;
import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Point;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.provider.MediaStore;
import android.support.v4.content.ContextCompat;
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

import com.baidu.android.pushservice.CustomPushNotificationBuilder;
import com.baidu.android.pushservice.PushManager;
import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;
import com.baidu.mapapi.map.BaiduMap;
import com.baidu.mapapi.map.BitmapDescriptor;
import com.baidu.mapapi.map.BitmapDescriptorFactory;
import com.baidu.mapapi.map.MapPoi;
import com.baidu.mapapi.map.MapStatus;
import com.baidu.mapapi.map.MapStatusUpdate;
import com.baidu.mapapi.map.MapStatusUpdateFactory;
import com.baidu.mapapi.map.MapView;
import com.baidu.mapapi.map.MarkerOptions;
import com.baidu.mapapi.map.Overlay;
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
import com.foxconn.rfid.theowner.activity.insurance.ApplyPayoutActivity;
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
import com.foxconn.rfid.theowner.util.string.DateUtils;
import com.google.protobuf.InvalidProtocolBufferException;
import com.yzh.rfidbike.app.request.GetBikeCurLocationRequest;
import com.yzh.rfidbike.app.request.GetBikesRequest;
import com.yzh.rfidbike.app.response.GetBikeCurLocationResponse;
import com.yzh.rfidbike.app.response.GetBikesResponse;

import java.util.ArrayList;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

/**
 * 主activity
 *
 * @author WT00111
 */
public class MainActivity extends BaseActivity implements View.OnClickListener,
        OnGetGeoCoderResultListener {
    /**
     * 个人消息按钮
     */
    private ImageButton btnMsg, btnSetting;
    private ImageView btnDown;
    private LinearLayout ll_bike_no;
    private RelativeLayout outside_rl;

    private TextView tvCarNum, tvCarModel;
    /**
     * 百度地图控件
     */
    private MapView mapView;
//	private Button mapViewBtn;
    /**
     * 百度地图
     */
    private BaiduMap mBaiduMap;
    /**
     * 车辆信息
     */
    private LinearLayout carInfo;
    /**
     * 历史轨迹
     */
    private LinearLayout historicalTrack;
    /**
     * 我的保险
     */
    private ImageView img_circle, iv_bike_status, iv_insurance_status;
    private LinearLayout myInsurance, ll_bike_status;
    private TextView tv_bike_status, remain_time_tv, tv_insurance_status, tv_insurance_status_content;
    private View v_bike_status, v_my_insurance;

    private LinearLayout tv_no_bike;

    /** 挂失索赔 */

    /**
     * 标题
     */
    private TextView tvHeaderTitle, tvAddress;
    private List<Bike> listBike;
    private int selectedIndex = 0;
    private BDLocationListener myListener = new MyLocationListener();
    private LocationClient mLocationClient = null;
    protected LinkedHashMap<String, Overlay> overlays = new LinkedHashMap<String, Overlay>();

    private double curLongitude = 0;
    private double curLatitude = 0;
    GeoCoder mSearch = null;
    Timer mTimer = null;
    private Bike bikelossBike;
    private final int MSG_update_bike_status = 3000;
    protected Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            try {
                switch (msg.what) {
                    case MSG_update_bike_status:
                        Bundle bundle = msg.getData();
                        long interval = bundle.getLong("INTERVAL");
                        bikelossBike = listBike.get(selectedIndex);
                        //未投保 或已过期
                        if (bikelossBike.getInsurance().equals("0") || bikelossBike.getInsurance().equals("2")) {
                            ll_bike_status.setOnClickListener(null);
                            if (interval < 60 * 1000) {
                                mTimer.cancel();
                                tv_bike_status.setText(getResources().getString(R.string.bike_stolen));
                                tv_bike_status.setTextColor(ContextCompat.getColor(context, R.color.font_color_red));
                                v_bike_status.setBackgroundResource(R.drawable.ico_ds);
                                remain_time_tv.setVisibility(View.GONE);
                                iv_bike_status.setVisibility(View.GONE);

                            } else {
                                tv_bike_status.setText(getResources().getString(R.string.apply_lost));
                                tv_bike_status.setTextColor(ContextCompat.getColor(context, R.color.font_color_red));
                                remain_time_tv.setVisibility(View.VISIBLE);
                                remain_time_tv.setTextColor(ContextCompat.getColor(context, R.color.textGray));
                                remain_time_tv.setText(getResources().getString(R.string.remain_find_time) + " " + DateUtils.formatTime(interval));
                                iv_bike_status.setVisibility(View.GONE);
                            }

                            //已投保
                        } else if (bikelossBike.getInsurance().equals("1")) {
                            if (interval < 60 * 1000) {
                                mTimer.cancel();
                                tv_bike_status.setText(getResources().getString(R.string.apply_payout));
                                tv_bike_status.setTextColor(ContextCompat.getColor(context, R.color.font_color_blue));
                                v_bike_status.setBackgroundResource(R.drawable.ico_sp);
                                remain_time_tv.setVisibility(View.GONE);
                                iv_bike_status.setVisibility(View.VISIBLE);
                                ll_bike_status.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View view) {
                                        Intent intentlossBike = new Intent();
                                        intentlossBike.putExtra("BIKE", bikelossBike);
                                        intentlossBike.setClass(MainActivity.this, ApplyPayoutActivity.class);
                                        startActivity(intentlossBike);
                                    }
                                });
                            } else {
                                tv_bike_status.setText(getResources().getString(R.string.apply_lost));
                                tv_bike_status.setTextColor(ContextCompat.getColor(context, R.color.font_color_red));
                                remain_time_tv.setVisibility(View.VISIBLE);
                                remain_time_tv.setTextColor(ContextCompat.getColor(context, R.color.textGray));
                                remain_time_tv.setText(DateUtils.formatTime(interval) + " " + getResources().getString(R.string.enable_apply_payout));
                                iv_bike_status.setVisibility(View.GONE);
                            }
                        }

                        break;
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
//		super.handleMessage(msg);
        }
    };

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

        setContentView(R.layout.activity_main);
        btnMsg = (ImageButton) findViewById(R.id.btn_msg);
        btnSetting = (ImageButton) findViewById(R.id.btn_setting);
        btnDown = (ImageView) findViewById(R.id.btn_down);
        mapView = (MapView) findViewById(R.id.mapview);
        carInfo = (LinearLayout) findViewById(R.id.carInfo);
        historicalTrack = (LinearLayout) findViewById(R.id.historicalTrack);
        myInsurance = (LinearLayout) findViewById(R.id.my_insurance);

        ll_bike_status = (LinearLayout) findViewById(R.id.ll_bike_status);
//		theLossOfClaim = (LinearLayout) findViewById(R.id.theLossOfClaim);
        v_bike_status = (View) findViewById(R.id.v_bike_status);
        v_my_insurance = (View) findViewById(R.id.v_my_insurance);
        ll_bike_no = (LinearLayout) findViewById(R.id.ll_bike_no);
        outside_rl = (RelativeLayout) findViewById(R.id.outside_rl);

        tvHeaderTitle = (TextView) findViewById(R.id.title);
//		mapViewBtn = (Button) findViewById(R.id.btnMap);
        tvCarNum = (TextView) findViewById(R.id.tv_car_num);
        tvCarModel = (TextView) findViewById(R.id.tv_car_model);
        tvAddress = (TextView) findViewById(R.id.tv_address);
        tv_no_bike = (LinearLayout) findViewById(R.id.tv_no_bike);
        tv_bike_status = (TextView) findViewById(R.id.tv_bike_status);
        remain_time_tv = (TextView) findViewById(R.id.remain_time_tv);

        tv_insurance_status = (TextView) findViewById(R.id.tv_insurance_status);
        tv_insurance_status_content = (TextView) findViewById(R.id.tv_insurance_status_content);
        LinearLayout ll_home_center = (LinearLayout) findViewById(R.id.ll_home_center);
        ll_home_center.bringToFront();
        img_circle = (ImageView) findViewById(R.id.img_circle);
        iv_insurance_status = (ImageView) findViewById(R.id.iv_insurance_status);
        iv_bike_status = (ImageView) findViewById(R.id.iv_bike_status);

    }


    private void initBaiduNotify() {
        Resources resource = this.getResources();
        String pkgName = this.getPackageName();
        CustomPushNotificationBuilder cBuilder = new CustomPushNotificationBuilder(
                resource.getIdentifier(
                        "notification_custom_builder", "layout", pkgName),
                resource.getIdentifier("notification_icon", "id", pkgName),
                resource.getIdentifier("notification_title", "id", pkgName),
                resource.getIdentifier("notification_text", "id", pkgName));
        cBuilder.setNotificationFlags(Notification.FLAG_AUTO_CANCEL);
        cBuilder.setNotificationDefaults(Notification.DEFAULT_VIBRATE);
        cBuilder.setStatusbarIcon(this.getApplicationInfo().icon);
        cBuilder.setLayoutDrawable(resource.getIdentifier(
                "simple_notification_icon", "drawable", pkgName));
        cBuilder.setNotificationSound(Uri.withAppendedPath(
                MediaStore.Audio.Media.INTERNAL_CONTENT_URI, "6").toString());
        // 推送高级设置，通知栏样式设置为下面的ID
        PushManager.setNotificationBuilder(this, 1, cBuilder);
    }


    protected void initEvent() {
        btnSetting.setOnClickListener(this);
        btnMsg.setOnClickListener(this);
        carInfo.setOnClickListener(this);
        historicalTrack.setOnClickListener(this);
        v_bike_status.setOnClickListener(this);
        v_my_insurance.setOnClickListener(this);

//		tvHeaderTitle.setOnClickListener(this);
        ll_bike_no.setOnClickListener(this);
        mapView.setClickable(true);
//		mapViewBtn.setOnClickListener(this);
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
                        listBike.add(bike);
                    }
                    if (listBike.size() > 0) {
                        getBikeLocation(listBike.get(0).getId());
                        outside_rl.setVisibility(View.VISIBLE);
                        tv_no_bike.setVisibility(View.GONE);
                    } else {
                        if (dlgWaiting.isShowing()) {
                            dlgWaiting.dismiss();
                        }
                        mDlgWaitingHandler.removeMessages(MSG_cannt_get_data);
                        tv_no_bike.setVisibility(View.VISIBLE);
                        outside_rl.setVisibility(View.GONE);
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
                refreshView(0);
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
                    addMarkOnBaiduMap(getBikeCurLocationResponseMessage.getLocations(0).getLongitude(), getBikeCurLocationResponseMessage.getLocations(0).getLatitude());
                }
//				initBaiduLocation();
            }
        } catch (InvalidProtocolBufferException e) {
            e.printStackTrace();
        }

    }

    protected void initData() {

        listBike = new ArrayList<Bike>();
        doSocket();
        mBaiduMap = mapView.getMap();

    }

    protected void refreshView(int selectIndex) {
        if (listBike == null || listBike.size() < 2) {
            btnDown.setVisibility(View.GONE);

        } else {
            btnDown.setVisibility(View.VISIBLE);
        }
        selectedIndex = selectIndex;
        tvHeaderTitle.setText(listBike.get(selectIndex).getPlateNumber());
        tvCarNum.setText(listBike.get(selectIndex).getPlateNumber());
        tvCarModel.setText(listBike.get(selectIndex).getBrandModel());

        Glide.with(this)
                .load(listBike.get(selectIndex).getPhoto()).transform(new GlideCircleImage(this))
                .placeholder(R.drawable.bikeimage)
//				.transform(new GlideCircleTransform(this))
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .thumbnail(0.1f)
                .crossFade(500)
                .into(img_circle);
        refreshBikeStatus(listBike.get(selectIndex));
    }

    void refreshBikeStatus(final Bike bike) {

        switch (bike.getInsurance()) {
            case "0":
				tv_insurance_status.setText(getResources().getString(R.string.no_insurance));
                v_my_insurance.setBackgroundResource(R.drawable.ico_wtb);
                iv_insurance_status.setVisibility(View.GONE);
                tv_insurance_status_content.setText(getResources().getString(R.string.no_insurance));
                break;
            case "1":
				tv_insurance_status.setText(getResources().getString(R.string.insurance_normal));
                v_my_insurance.setBackgroundResource(R.drawable.bx);
                iv_insurance_status.setVisibility(View.VISIBLE);
                tv_insurance_status_content.setText(getResources().getString(R.string.insurance_normal));
                myInsurance.setOnClickListener(this);
                break;
            case "2":
                tv_insurance_status_content.setText(getResources().getString(R.string.insurance_expire));
                iv_insurance_status.setVisibility(View.VISIBLE);
                v_my_insurance.setBackgroundResource(R.drawable.ico_gq);
                break;
            default:
                break;
        }
        switch (bike.getLostStatus()) {
            case ""://正常使用
                tv_bike_status.setText(getResources().getString(R.string.use_normal));
                tv_bike_status.setTextColor(ContextCompat.getColor(context, R.color.font_color_green));
                v_bike_status.setBackgroundResource(R.drawable.zc);
                remain_time_tv.setVisibility(View.GONE);
                ll_bike_status.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Intent intentlossBike = new Intent();
                        Bike bikelossBike = listBike.get(selectedIndex);
                        intentlossBike.putExtra("BIKE", bikelossBike);
//                        intentlossBike.setClass(MainActivity.this, ApplyLossBikeActivity.class);
                        intentlossBike.setClass(MainActivity.this, BikeLostDisposeActivity.class);
                        startActivity(intentlossBike);
                    }
                });
                break;
            case "0"://已申请丢失
                ll_bike_status.setOnClickListener(null);
                remain_time_tv.setVisibility(View.GONE);
                tv_bike_status.setText(getResources().getString(R.string.apply_lost));
                tv_bike_status.setTextColor(ContextCompat.getColor(context, R.color.font_color_red));
                v_bike_status.setBackgroundResource(R.drawable.ico_ds);

//				ll_bike_status.setOnClickListener(new View.OnClickListener() {
//					@Override
//					public void onClick(View view) {
//						Intent intentlossBike = new Intent();
//						Bike bikelossBike = listBike.get(selectedIndex);
//						intentlossBike.putExtra("BIKE", bikelossBike);
//						intentlossBike.setClass(ActivityMessageCenter.this, LossBikeActivity.class);
//						startActivity(intentlossBike);
//					}
//				});


                break;
            case "1"://车辆丢失审核通过，倒计时中
//				tv_bike_status.setText(getResources().getString(R.string.bike_lost)+ DateUtils.getStringFromLong(Long.valueOf(bike.getLostEndDate()),"yyyy-MM-dd HH:mm")+getResources().getString(R.string.enable_apply_payout));
                remain_time_tv.setVisibility(View.VISIBLE);
                ll_bike_status.setOnClickListener(null);
                if (mTimer != null) {
                    mTimer = null;
                }
                if (bike.getPayoutStatus().trim().length() > 0) {
                    break;
                }

                mTimer = new Timer();
                TimerTask task = new TimerTask() {
                    @Override
                    public void run() {
                        long interval = Long.valueOf(bike.getLostEndDate() - DateUtils.getLongFromDate(new Date()));
                        //多加1分钟防止时间误差
                        interval += 60 * 1000 * 1;
                        Message message = new Message();
                        Bundle bundle = new Bundle();
                        bundle.putLong("INTERVAL", interval);
                        message.setData(bundle);
                        message.what = MSG_update_bike_status;
                        mHandler.sendMessage(message);
                    }
                };
                mTimer.schedule(task, 0, 60 * 1000);
                break;
            case "2"://
                tv_bike_status.setText(getResources().getString(R.string.use_normal));
                tv_bike_status.setTextColor(ContextCompat.getColor(context, R.color.font_color_green));
                v_bike_status.setBackgroundResource(R.drawable.zc);
                remain_time_tv.setVisibility(View.GONE);
                ll_bike_status.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Intent intentlossBike = new Intent();
                        Bike bikelossBike = listBike.get(selectedIndex);
                        intentlossBike.putExtra("BIKE", bikelossBike);
//                        intentlossBike.setClass(MainActivity.this, ApplyLossBikeActivity.class);
                        intentlossBike.setClass(MainActivity.this, BikeLostDisposeActivity.class);
                        startActivity(intentlossBike);
                    }
                });
                break;
            case "3"://
                remain_time_tv.setVisibility(View.GONE);
                tv_bike_status.setText(getResources().getString(R.string.use_normal));
                tv_bike_status.setTextColor(ContextCompat.getColor(context, R.color.font_color_green));
                v_bike_status.setBackgroundResource(R.drawable.zc);
                ll_bike_status.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Intent intentlossBike = new Intent();
                        Bike bikelossBike = listBike.get(selectedIndex);
                        intentlossBike.putExtra("BIKE", bikelossBike);
//                        intentlossBike.setClass(MainActivity.this, ApplyLossBikeActivity.class);
                        intentlossBike.setClass(MainActivity.this, BikeLostDisposeActivity.class);
                        startActivity(intentlossBike);
                    }
                });
                break;
            case "4"://已丢失
                remain_time_tv.setVisibility(View.GONE);
                if (bike.getInsurance().equals("0") || bike.getInsurance().equals("2")) {
                    tv_bike_status.setText(getResources().getString(R.string.bike_stolen));
                    tv_bike_status.setTextColor(ContextCompat.getColor(context, R.color.font_color_red));
                    v_bike_status.setBackgroundResource(R.drawable.ico_ds);
                    iv_bike_status.setVisibility(View.GONE);
                }else {
                    tv_bike_status.setText(getResources().getString(R.string.apply_payout));
                    tv_bike_status.setTextColor(ContextCompat.getColor(context, R.color.font_color_blue));
                    v_bike_status.setBackgroundResource(R.drawable.ico_sp);
                    ll_bike_status.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            Intent intentlossBike = new Intent();
                            Bike bikelossBike = listBike.get(selectedIndex);
                            intentlossBike.putExtra("BIKE", bikelossBike);
                            intentlossBike.setClass(MainActivity.this, ApplyPayoutActivity.class);
                            startActivity(intentlossBike);
                        }
                    });
                }
                break;
        }
        switch (bike.getPayoutStatus()) {
            case "0":
                tv_bike_status.setText(getResources().getString(R.string.applied_payout));
                ll_bike_status.setOnClickListener(null);
                break;
            case "1":
                tv_bike_status.setText(getResources().getString(R.string.apply_cash));
                tv_bike_status.setTextColor(ContextCompat.getColor(context, R.color.font_color_grape));
                v_bike_status.setBackgroundResource(R.drawable.ico_tx);
                ll_bike_status.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Intent intentlossBike = new Intent();
                        Bike bikelossBike = listBike.get(selectedIndex);
                        intentlossBike.putExtra("BIKE", bikelossBike);
//                        intentlossBike.setClass(MainActivity.this, ApplyCashActivity.class);
//                        intentlossBike.setClass(MainActivity.this, ApplyGetMoneyActivity.class);
                        startActivity(intentlossBike);
                    }
                });
                break;
            case "2":
                tv_bike_status.setText(getResources().getString(R.string.apply_payout));
                tv_bike_status.setTextColor(ContextCompat.getColor(context, R.color.font_color_blue));
                v_bike_status.setBackgroundResource(R.drawable.ico_sp);

                ll_bike_status.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Intent intentlossBike = new Intent();
                        Bike bikelossBike = listBike.get(selectedIndex);
                        intentlossBike.putExtra("BIKE", bikelossBike);
                        intentlossBike.setClass(MainActivity.this, ApplyPayoutActivity.class);
                        startActivity(intentlossBike);
                    }
                });
                break;
            case "3":
                tv_bike_status.setText(getResources().getString(R.string.applied_cash));
                tv_bike_status.setTextColor(ContextCompat.getColor(context, R.color.font_color_grape));
                v_bike_status.setBackgroundResource(R.drawable.ico_tx);
                ll_bike_status.setOnClickListener(null);
                break;
            case "4":
                tv_bike_status.setText(getResources().getString(R.string.applied_cash));
                v_bike_status.setBackgroundResource(R.drawable.ico_tx);
                ll_bike_status.setOnClickListener(null);
                break;

        }

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_setting:
                startActivity(new Intent(this, SettingActivity.class));
                break;
            case R.id.btn_msg:
//			startActivity(new Intent(this, MessageActivity.class));
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
            case R.id.historicalTrack:// 历史轨迹
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
            case R.id.my_insurance:// 我的保险
                Intent intentInsurance = new Intent();
                Bike bikeInsurance = listBike.get(selectedIndex);
                //bikeInsurance.getInsurance()中"0"状态为"未投保",不能进入我的保险界面
                if (bikeInsurance.getInsurance().equals("0")) {
                    return;
                } else {
                    intentInsurance.putExtra("BIKE", bikeInsurance);
                    intentInsurance.setClass(this, MyInsuranceActivity.class);
                    startActivity(intentInsurance);
                }

                break;
            case R.id.ll_bike_no:
                selectBike();
                btnDown.setImageDrawable(getResources().getDrawable(R.drawable.icon_up));
                break;
//		case R.id.btnMap:
//
//			break;
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


    private void addMarkOnBaiduMap(double longitude, double latitude) {
//		LatLng point = new LatLng(longitude, latitude);
//		// 构建Marker图标
//		BitmapDescriptor bitmap = BitmapDescriptorFactory.fromResource(R.drawable.bg_fence_add_btn);
//		// 构建MarkerOption，用于在地图上添加Marker
//		OverlayOptions option = new MarkerOptions().position(point).icon(bitmap);
//
//		// 在地图上添加Marker，并显示
//		mBaiduMap.addOverlay(option);
        mSearch = GeoCoder.newInstance();
        mSearch.setOnGetGeoCodeResultListener(MainActivity.this);
        LatLng ptCenter = new LatLng(latitude, longitude);
        // 反Geo搜索
        mSearch.reverseGeoCode(new ReverseGeoCodeOption()
                .location(ptCenter));


        BDLocation location = new BDLocation();

        LatLng point = new LatLng(latitude, longitude);
        // 构建Marker图标
        BitmapDescriptor bitmap = BitmapDescriptorFactory.fromResource(R.drawable.bike);
        // 构建MarkerOption，用于在地图上添加Marker
        OverlayOptions option = new MarkerOptions().position(point).icon(bitmap);
        // 在地图上添加Marker，并显示
        mBaiduMap.addOverlay(option);
        // 设置地图中心点，默认是天安门
        MapStatusUpdate mapstatusUpdatePoint = MapStatusUpdateFactory.newLatLngZoom(point, 14);
        mBaiduMap.setMapStatus(mapstatusUpdatePoint);
        mapView.showScaleControl(false);
        mapView.showZoomControls(false);
        // 隐藏 百度地图logo
        View child = mapView.getChildAt(1);
        if (child != null && (child instanceof ImageView || child instanceof ZoomControls)) {
            child.setVisibility(View.INVISIBLE);
        }
        UiSettings settings = mBaiduMap.getUiSettings();
        settings.setAllGesturesEnabled(true); // 关闭一切手势操作
        settings.setOverlookingGesturesEnabled(false);// 屏蔽双指下拉时变成3D地图
        settings.setZoomGesturesEnabled(true);// 获取是否允许缩放手势返回:是否允许缩放手势
        mBaiduMap.setBuildingsEnabled(true);
        mBaiduMap.setCompassPosition(new Point(20, 20));
    }

    /**
     * 初始化地图
     */
    public void initMap() {
        // 定义Maker坐标点
        LatLng point = new LatLng(29.607395, 106.378996);
        // 构建Marker图标
//		BitmapDescriptor bitmap = BitmapDescriptorFactory.fromResource(R.drawable.orp);
//		// 构建MarkerOption，用于在地图上添加Marker
//		OverlayOptions option = new MarkerOptions().position(point).icon(bitmap);
//		// 在地图上添加Marker，并显示
//		mBaiduMap.addOverlay(option);
        // 设置地图中心点，默认是天安门
        MapStatusUpdate mapstatusUpdatePoint = MapStatusUpdateFactory.newLatLngZoom(point, 14);
        mBaiduMap.setMapStatus(mapstatusUpdatePoint);

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
        mBaiduMap.setCompassPosition(new Point(20, 20));
        mBaiduMap.setOnMapClickListener(new BaiduMap.OnMapClickListener() {
            @Override
            public void onMapClick(LatLng latLng) {
                if (listBike.size() == 0) {
                    ToastUtils.showTextToast(MainActivity.this, getResources().getString(R.string.none_bike));
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
//        mLocationClient.stop();
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
//        switch (eventPackage.getMsgType()) {
//            case EventBusMsg.MSG_apply_lost:
//                listBike.get(selectedIndex).setLostStatus("0");
//                refreshBikeStatus(listBike.get(selectedIndex));
//                break;
//            case EventBusMsg.MSG_apply_cash:
//                tvCarNum.setText("");
//                tvCarModel.setText("");
//                tvHeaderTitle.setText("");
//                doSocket();
//				listBike.remove(selectedIndex);
//
//				if(listBike.size()>0)
//				{
//					selectedIndex=0;
//					refreshBikeStatus(listBike.get(selectedIndex));
//				}else
//				{
//					tv_bike_status.setText(getResources().getString(R.string.use_normal));
//					tv_insurance_status.setText(getResources().getString(R.string.no_insurance));
//					tvHeaderTitle.setText("");
//				}
//                break;
//            case EventBusMsg.MSG_apply_insurance_payout:
//                listBike.get(selectedIndex).setPayoutStatus("0");
//                refreshBikeStatus(listBike.get(selectedIndex));
//                break;
//        }
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
