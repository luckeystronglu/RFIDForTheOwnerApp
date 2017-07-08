/**
 *
 */
package com.foxconn.rfid.theowner.activity.main.defence;

import android.app.AlertDialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Point;
import android.graphics.Typeface;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
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
import com.baidu.mapapi.map.TextOptions;
import com.baidu.mapapi.map.UiSettings;
import com.baidu.mapapi.model.LatLng;
import com.baidu.mapapi.model.LatLngBounds;
import com.foxconn.rfid.theowner.R;
import com.foxconn.rfid.theowner.activity.LoginActivity;
import com.foxconn.rfid.theowner.activity.main.defence.adapter.EleFenceAdapter;
import com.foxconn.rfid.theowner.base.App;
import com.foxconn.rfid.theowner.base.BaseActivity;
import com.foxconn.rfid.theowner.base.PreferenceData;
import com.foxconn.rfid.theowner.model.Bike;
import com.foxconn.rfid.theowner.model.EventBusMsg;
import com.foxconn.rfid.theowner.socket.SocketAppPacket;
import com.foxconn.rfid.theowner.socket.SocketClient;
import com.foxconn.rfid.theowner.util.Utils;
import com.foxconn.rfid.theowner.util.logort.ToastUtils;
import com.google.protobuf.InvalidProtocolBufferException;
import com.yzh.rfidbike.app.request.EditBikeFenceRequest;
import com.yzh.rfidbike.app.request.GetBikeDefenceRequest;
import com.yzh.rfidbike.app.request.Types;
import com.yzh.rfidbike.app.response.CommonResponse;
import com.yzh.rfidbike.app.response.GetBikeDefenceResponse;

import java.util.ArrayList;
import java.util.List;

/**
 * @author WT00111
 */
public class EleFenceListActivity extends BaseActivity implements View.OnClickListener {

    private Button addFence;
    //    private SVListView listView;
    private ListView listView;

    protected MapView mapView;
    protected BaiduMap mBaiduMap;
    private int mPosition = -1;

    private LocationClient mLocationClient = null;
    private BDLocationListener myListener = new MyLocationListener();
    private double curLongitude = 0;
    private double curLatitude = 0;
    private List<GetBikeDefenceResponse.DefenceMessage> listDefence;
    private GetBikeDefenceResponse.DefenceMessage defenceMessage;
    private EleFenceAdapter listAdapter;
    private Bike bike;
    private List<Overlay> listCircleOverlays;
    private List<Overlay> listBikeOverlays;
    private List<Overlay> listTextOverLays;
    private boolean isActivity = false;
    private LatLng centerPoint;

    //删除电子围栏dialog相关
    private AlertDialog deleteAlertDialog;
    private TextView tv_cancel, tv_sure;
    private ImageView iv_cancel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_fence_list);
        initView();
        initDialog();
    }

    private void initDialog() {
        LayoutInflater inflater = this.getLayoutInflater();
        View layout = inflater.inflate(R.layout.dialog_layout_delete_defence, null);
        deleteAlertDialog = new AlertDialog.Builder(this).setView(layout).create();
    }

    protected void initView() {

        Intent intent = getIntent();
        bike = (Bike) intent.getSerializableExtra("BIKE");
        if (bike == null)
            return;

        mapView = (MapView) findViewById(R.id.mapview);
        mBaiduMap = mapView.getMap();
        addFence = (Button) findViewById(R.id.add_safety_defence);
        addFence.setOnClickListener(this);
        //        listView = (SVListView) findViewById(R.id.listView);
        listView = (ListView) findViewById(R.id.listView);
        //初始化电子围档的list集合
        listDefence = new ArrayList<GetBikeDefenceResponse.DefenceMessage>();
        listCircleOverlays = new ArrayList<>();
        listBikeOverlays = new ArrayList<>();
        listTextOverLays = new ArrayList<>();

        listAdapter = new EleFenceAdapter(this, listDefence, bike);
        listView.setAdapter(listAdapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                listAdapter.setSelectItem(position);
                listAdapter.notifyDataSetChanged();

                for (int i = 0; i < listDefence.size(); i++) {
                    listCircleOverlays.get(i).remove();
                    listBikeOverlays.get(i).remove();
                    listTextOverLays.get(i).remove();
                }
                listCircleOverlays.clear();
                listBikeOverlays.clear();
                listTextOverLays.clear();

                for (int i = 0; i < listDefence.size(); i++) {

                    LatLng point = new LatLng(listDefence.get(i).getLatitude(), listDefence.get(i).getLongitude());
                    BitmapDescriptor bitmap;
                    if (i != position) {
                        // 构建Marker图标
                        bitmap = BitmapDescriptorFactory.fromResource(R.drawable.bike_red);
                    } else {
                        bitmap = BitmapDescriptorFactory.fromResource(R.drawable.bike);
                    }
                    MarkerOptions option = new MarkerOptions().position(point).icon(bitmap);
                    option.draggable(true);
                    listCircleOverlays.add(drawCircle(point, i + 1 + "", listDefence.get(i).getRadius()));
                    listBikeOverlays.add(addOverLay(option, i + 1 + ""));
                    listTextOverLays.add(addTextOverLay(point, i + 1 + ""));
                }

            }

        });



        //listView的长按点击事件,弹出AlertDialog
        listView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> adapterView, View view, int position, final long id) {
                defenceMessage = listDefence.get(position);
                mPosition = position;
                //点击显示AlertDialog
                deleteAlertDialog.show();

                //设置dialog的样式属性
                Window dialogView = deleteAlertDialog.getWindow();
                iv_cancel = (ImageView) dialogView.findViewById(R.id.delete);
                tv_cancel = (TextView) dialogView.findViewById(R.id.tv_cancel);
                tv_sure = (TextView) dialogView.findViewById(R.id.tv_sure);
                iv_cancel.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        deleteAlertDialog.cancel();
                    }
                });
                tv_cancel.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        deleteAlertDialog.cancel();
                    }
                });
                tv_sure.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        deleteAlertDialog.cancel();
                        deleteDefence();
                    }
                });


                return true;
            }
        });

        initMap();// 初始化地图
        //内网无法获取当前位置，先注释

        doSocket();
    }

    private void deleteDefence() {
        dlgWaiting.show();
        mDlgWaitingHandler.sendEmptyMessageDelayed(MSG_cannt_get_data, App.WAITTING_SECOND);
        final EditBikeFenceRequest.EditBikeFenceRequestMessage.Builder requestMessage = EditBikeFenceRequest.EditBikeFenceRequestMessage.newBuilder();
        requestMessage.setBikeId(bike.getId());
        requestMessage.setId(defenceMessage.getId());
        requestMessage.setSession(PreferenceData.loadSession(this));
        requestMessage.setType(Types.Type.DELETE);
        new Thread() {
            public void run() {
                SocketClient client;
                client = SocketClient.getInstance();
                client.send(SocketAppPacket.COMMAND_ID_EDIT_BIKE_DEFENCE, requestMessage.build().toByteArray());
            }

        }.start();
    }

    @Override
    protected void onResume() {
        super.onResume();
        isActivity = true;
    }

    @Override
    protected void onPause() {
        super.onPause();
        isActivity = false;
    }

    @Override
    protected void doSocket() {
        super.doSocket();
        final GetBikeDefenceRequest.GetBikeDefenceRequestMessage.Builder requestMessage = GetBikeDefenceRequest.GetBikeDefenceRequestMessage.newBuilder();
        requestMessage.setSession(PreferenceData.loadSession(this));
        requestMessage.setBikeId(bike.getId());
        new Thread() {
            public void run() {
                SocketClient client;
                client = SocketClient.getInstance();
                client.send(SocketAppPacket.COMMAND_ID_get_bike_defence, requestMessage.build().toByteArray());
            }

        }.start();
    }

    @Override
    public void onEventMainThread(EventBusMsg eventBusMsg) {
        super.onEventMainThread(eventBusMsg);
        if (eventBusMsg.getMsgType() == EventBusMsg.MSG_defence_edit_data) {

            doSocket();
        }


    }

    @Override
    public void onEventMainThread(SocketAppPacket eventPackage) {
        super.onEventMainThread(eventPackage);

        switch (eventPackage.getCommandId()) {
            case SocketAppPacket.COMMAND_ID_get_bike_defence:
                if (dlgWaiting.isShowing()) {
                    dlgWaiting.dismiss();
                }
                mDlgWaitingHandler.removeMessages(MSG_cannt_get_data);
                try {
                    GetBikeDefenceResponse.GetBikeDefenceResponseMessage responseMessage = GetBikeDefenceResponse.GetBikeDefenceResponseMessage.parseFrom(eventPackage.getCommandData());
                    if (responseMessage.getErrorMsg().getErrorCode() != 0) {
                        Toast.makeText(this, responseMessage.getErrorMsg().getErrorMsg(), Toast.LENGTH_LONG).show();
                        if (responseMessage.getErrorMsg().getErrorCode() == 20003) {
                            Intent intent = new Intent();
                            intent.setClass(this, LoginActivity.class);
                            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK|Intent.FLAG_ACTIVITY_CLEAR_TASK);
                            startActivity(intent);
                        }
                    } else {

                        listAdapter.setSelectItem(-1);
                        for (int i = 0; i < listDefence.size(); i++) {
                            listCircleOverlays.get(i).remove();
                            listBikeOverlays.get(i).remove();
                            listTextOverLays.get(i).remove();
                        }
                        listDefence.clear();
                        listCircleOverlays.clear();
                        listBikeOverlays.clear();
                        listTextOverLays.clear();


                        for (int i = 0; i < responseMessage.getDefenceList().size(); i++) {
                            listDefence.add(responseMessage.getDefenceList().get(i));
                            LatLng point = new LatLng(responseMessage.getDefenceList().get(i).getLatitude(), responseMessage.getDefenceList().get(i).getLongitude());
                            // 构建Marker图标
                            //                                Log.d("print", "onEventMainThread: point.latitude :" + point.latitude + " point.longitude :" + point.longitude);
                            BitmapDescriptor bitmap = BitmapDescriptorFactory.fromResource(R.drawable.bike_red);
                            MarkerOptions option = new MarkerOptions().position(point).icon(bitmap);
                            option.draggable(true);

                            listCircleOverlays.add(drawCircle(point, i + 1 + "", responseMessage.getDefenceList().get(i).getRadius()));
                            listBikeOverlays.add(addOverLay(option, i + 1 + ""));
                            listTextOverLays.add(addTextOverLay(point, i + 1 + ""));
                        }
                        if (listDefence.size() == 0) {
                            initBaiduLocation();
                            return;
                        } else if (listDefence.size() == 1) {
                            centerPoint = new LatLng(listDefence.get(0).getLatitude(), listDefence.get(0).getLongitude());
                            MapStatusUpdate u = MapStatusUpdateFactory.newLatLng(centerPoint);
                            mBaiduMap.animateMapStatus(u);
                        } else {
                            List<MapStatusUpdate> listStatus = getMapStatusList(listDefence, listDefence.get(0).getLatitude(), listDefence.get(0).getLatitude());
                            mBaiduMap.animateMapStatus(listStatus.get(0));
                        }

                        listAdapter.notifyDataSetChanged();
                    }
                } catch (InvalidProtocolBufferException e) {
                    e.printStackTrace();
                }
                break;


            case SocketAppPacket.COMMAND_ID_EDIT_BIKE_DEFENCE:
                if (!isActivity) {
                    return;
                }
                if (dlgWaiting.isShowing()) {
                    dlgWaiting.dismiss();
                }
                try {
                    CommonResponse.CommonResponseMessage commonPwMessage = CommonResponse.CommonResponseMessage.parseFrom(eventPackage.getCommandData());
                    mDlgWaitingHandler.removeMessages(MSG_cannt_get_data);
                    if (commonPwMessage.getErrorMsg().getErrorCode() != 0) {
                        Toast.makeText(this, commonPwMessage.getErrorMsg().getErrorMsg(), Toast.LENGTH_LONG).show();
                        if (commonPwMessage.getErrorMsg().getErrorCode() == 20003) {
                            Intent intent = new Intent();
                            intent.setClass(this, LoginActivity.class);
                            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK|Intent.FLAG_ACTIVITY_CLEAR_TASK);
                            startActivity(intent);
                        }
                    } else {
                        listAdapter.setSelectItem(-1);
                        for (int i = 0; i < listDefence.size(); i++) {
                            listCircleOverlays.get(i).remove();
                            listBikeOverlays.get(i).remove();
                            listTextOverLays.get(i).remove();
                        }
                        listCircleOverlays.clear();
                        listBikeOverlays.clear();
                        listTextOverLays.clear();
                        listDefence.remove(mPosition);
                        listAdapter.notifyDataSetChanged();
                        for (int i = 0; i < listDefence.size(); i++) {
                            LatLng point = new LatLng(listDefence.get(i).getLatitude(), listDefence.get(i).getLongitude());
                            // 构建Marker图标

                            BitmapDescriptor bitmap = BitmapDescriptorFactory.fromResource(R.drawable.bike_red);
                            MarkerOptions option = new MarkerOptions().position(point).icon(bitmap);
                            option.draggable(true);

                            listCircleOverlays.add(drawCircle(point, i + 1 + "", listDefence.get(i).getRadius()));
                            listBikeOverlays.add(addOverLay(option, i + 1 + ""));
                            listTextOverLays.add(addTextOverLay(point, i + 1 + ""));
                        }
                        if (listDefence.size() == 0) {
                            initBaiduLocation();
                            //                            LatLng mPoint;
                            //                            if (curLatitude > 0.1 && curLatitude < 90.0) {
                            //                                mPoint = new LatLng(curLatitude, curLongitude);//获取定位的经纬度
                            //                                Log.d("print ", "initMap: 这是定位得到的纬度" + curLatitude);
                            //                            } else {
                            //                                mPoint = new LatLng(39.963175, 116.400244);//没有则取默认值(北京)
                            //                            }
                            //                            MapStatusUpdate u = MapStatusUpdateFactory.newLatLng(mPoint);
                            //
                            //                            mBaiduMap.animateMapStatus(u);

                            return;
                        } else if (listDefence.size() == 1) {
                            centerPoint = new LatLng(listDefence.get(0).getLatitude(), listDefence.get(0).getLongitude());
                            MapStatusUpdate u = MapStatusUpdateFactory.newLatLng(centerPoint);
                            mBaiduMap.animateMapStatus(u);
                        } else {

                            List<MapStatusUpdate> listStatus = getMapStatusList(listDefence, listDefence.get(0).getLatitude(), listDefence.get(0).getLatitude());
                            mBaiduMap.animateMapStatus(listStatus.get(0));

                        }

                        listAdapter.notifyDataSetChanged();
                    }
                } catch (InvalidProtocolBufferException e) {
                    e.printStackTrace();
                }

                break;
        }

    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.add_safety_defence:
                if (listDefence.size() > 2) {
                    ToastUtils.showTextToast(this, getResources().getString(R.string.most_have_defence_num));
                } else {
                    Intent intent = new Intent(this, AddEleFenceActivity.class);
                    intent.putExtra("BIKE", bike);
                    startActivity(intent);
                }

                break;

            default:
                break;
        }
    }

    /**
     * 初始化地图
     */
    public void initMap() {
        mBaiduMap = mapView.getMap();
        LatLng point;
        if (listDefence.size() > 0) {
            point = new LatLng(listDefence.get(0).getLatitude(), listDefence.get(0).getLongitude());
        } else {
            if (curLatitude > 0.1 && curLatitude < 90.0) {
                point = new LatLng(curLatitude, curLongitude);//没有则取默认值
                Log.d("print ", "initMap: 这是定位得到的纬度" + curLatitude);
            } else {
                point = new LatLng(39.963175, 116.400244);//没有则取默认值
            }

        }

        // 构建Marker图标
        BitmapDescriptor bitmap = BitmapDescriptorFactory.fromResource(R.drawable.orp);
        // 构建MarkerOption，用于在地图上添加Marker
        MarkerOptions option = new MarkerOptions().position(point).icon(bitmap);
        Log.d("print", "initMap: point" + point.latitude + point.longitude);
        option.draggable(true);
        // 在地图上添加Marker，并显示
        //        		addOverlay(option, "locationIcon");
        //         设置地图中心点，默认是天安门
        MapStatusUpdate mapstatusUpdatePoint = MapStatusUpdateFactory.newLatLngZoom(point, 14);
        //         带动画的更新地图状态，还是300毫秒
        mBaiduMap.setMapStatus(mapstatusUpdatePoint);
        mapView.showScaleControl(false);
        mapView.showZoomControls(false);
        // 隐藏 百度地图logo
        View child = mapView.getChildAt(1);
        if (child != null && (child instanceof ImageView)) {
            child.setVisibility(View.INVISIBLE);
        }

        mBaiduMap.setBuildingsEnabled(true);
        mBaiduMap.setCompassPosition(new Point(Utils.dpToPx(20, getResources()), Utils.dpToPx(20, getResources())));
        UiSettings settings = mBaiduMap.getUiSettings();
        settings.setCompassEnabled(true);

    }


    /**
     * 画圆图层
     *
     * @param latLng
     */
    private Overlay drawCircle(LatLng latLng, String tag, int radius) {
        // 定义一个圆 ： 圆心+半径

        // 1.创建自己
        CircleOptions circleOptions = new CircleOptions();
        // 2.给自己设置数据
        circleOptions.center(latLng) // 圆心
                .radius(radius)// 半径 单位米
                .fillColor(0x18ff0C00)// 填充色
                .stroke(new Stroke(2, 0x19ff0C00));// 边框宽度和颜色

        return addOverLay(circleOptions, "lable" + tag);
    }

    /**
     * 添加覆盖物
     *
     * @param overlayOptions
     * @param tag
     */

    public Overlay addOverLay(OverlayOptions overlayOptions, String tag) {
        Bundle bundle = new Bundle();
        bundle.putString("title", tag);
        Overlay overlay = mBaiduMap.addOverlay(overlayOptions);
        overlay.setExtraInfo(bundle);
        return overlay;
    }

    public Overlay addTextOverLay(LatLng latLng, String tag) {
        TextOptions textOptions = new TextOptions();
        textOptions.fontColor(Color.parseColor("#ffffff"))// 设置字体颜色
                .text(tag)// 文字内容
                .position(latLng)// 位置
                .fontSize(Utils.spToPx(12, getResources()))// 字体大小
                .typeface(Typeface.SERIF)// 字体
                .bgColor(Color.TRANSPARENT);
        return addOverLay(textOptions, tag);
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
            mLocationClient.stop();
            LatLng ll = new LatLng(location.getLatitude(),
                    location.getLongitude());
            Log.d("print", "onReceiveLocation: " + location.getLatitude() + " " + location.getLongitude());
            MapStatus.Builder builder = new MapStatus.Builder();
            builder.target(ll).zoom(14.0f);
            mBaiduMap.animateMapStatus(MapStatusUpdateFactory.newMapStatus(builder.build()));


        }
    }

    public List<MapStatusUpdate> getMapStatusList(List<GetBikeDefenceResponse.DefenceMessage> listDevice, double location_latitude, double location_longitude) {
        double min_latitude = 0.00;
        double max_latitude = 0.00;
        double min_longitude = 0.00;
        double max_longitude = 0.00;

        if (listDevice == null) {
        } else {
            if (listDevice.size() == 1) {
                if (listDevice.get(0).getLatitude()
                        <= location_latitude) {
                    min_latitude = listDevice.get(0).getLatitude();
                    max_latitude = location_latitude;
                } else {
                    max_latitude = listDevice.get(0).getLatitude();
                    min_latitude = location_latitude;
                }
                if (listDevice.get(0).getLongitude() <= location_longitude) {
                    min_longitude = listDevice.get(0).getLongitude();
                    max_longitude = location_longitude;
                } else {
                    max_longitude = listDevice.get(0).getLatitude();
                    min_longitude = location_longitude;
                }
            } else {
                for (GetBikeDefenceResponse.DefenceMessage deviceInfo : listDevice) {
                    //					LatLng pt4 = new LatLng(deviceInfo.getLatitude(), deviceInfo.getLongitude());
                    //					LatLng latLngTmp = GpsToBaidu.tranfor(pt4);
                    LatLng latLngTmp = new LatLng(deviceInfo.getLatitude(), deviceInfo.getLongitude());
                    if (max_latitude == 0) {
                        max_latitude = latLngTmp.latitude;
                    } else if (max_latitude < latLngTmp.latitude) {
                        max_latitude = latLngTmp.latitude;
                    }

                    if (min_latitude == 0) {
                        min_latitude = latLngTmp.latitude;
                    } else if (min_latitude > latLngTmp.latitude) {
                        min_latitude = latLngTmp.latitude;
                    }

                    if (max_longitude == 0) {
                        max_longitude = latLngTmp.longitude;
                    } else if (max_longitude < latLngTmp.longitude) {
                        max_longitude = latLngTmp.longitude;
                    }

                    if (min_longitude == 0) {
                        min_longitude = latLngTmp.longitude;
                    } else if (min_longitude > latLngTmp.longitude) {
                        min_longitude = latLngTmp.longitude;
                    }
                }
            }
        }

        //设置中心位置及级别
        LatLng southwest = new LatLng(min_latitude - 0.04, min_longitude - 0.04);
        LatLng northeast = new LatLng(max_latitude + 0.04, max_longitude + 0.04);
        LatLng northwest = new LatLng(max_latitude + 0.04, min_longitude - 0.04);
        LatLng southeast = new LatLng(min_latitude - 0.04, max_longitude + 0.04);

        LatLngBounds bounds = new LatLngBounds.Builder().include(northeast).include(southwest).include(southeast).include(northwest).build();

        MapStatusUpdate u1 = MapStatusUpdateFactory.newLatLngBounds(bounds);

        mBaiduMap.setMapStatus(u1);
        ArrayList<MapStatusUpdate> listStatus = new ArrayList<MapStatusUpdate>();
        listStatus.add(u1);
        return listStatus;

    }
}
