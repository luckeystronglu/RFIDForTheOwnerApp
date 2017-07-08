package com.foxconn.rfid.theowner.activity.main.defence;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.content.ContextCompat;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.foxconn.rfid.theowner.R;
import com.foxconn.rfid.theowner.activity.LoginActivity;
import com.foxconn.rfid.theowner.activity.insurance.ApplyLossBikeActivity;
import com.foxconn.rfid.theowner.activity.insurance.ApplyPayoutActivity;
import com.foxconn.rfid.theowner.activity.message.ApplyGetMoneyActivity;
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
import com.yzh.rfidbike.app.request.GetBikeByBikeIdRequest;
import com.yzh.rfidbike.app.request.GetInsuranceInfoByBikeIdRequest;
import com.yzh.rfidbike.app.response.Bike.BikeMessage;
import com.yzh.rfidbike.app.response.BikeInsurance;
import com.yzh.rfidbike.app.response.GetBikeInsurancesByBikeIdResponse;
import com.yzh.rfidbike.app.response.GetBikesResponse;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import de.greenrobot.event.EventBus;


/**
 * Created by appadmin on 2017/4/28.
 */

public class BikeLostDisposeActivity extends BaseActivity implements View.OnClickListener {
    private LinearLayout ll_apply_lost,ll_check_lost,ll_finding_lost,ll_apply_payout;
    private LinearLayout ll_check_payout,ll_getmoney_payout,ll_diapose_over;  //,ll_getmoney_dispose
    private LinearLayout ll_bike_found,ll_bike_return,ll_getmoney_notify;  //ll_survey_evidence
    private Bike mBi;
    private List<BikeInsurance.BikeInsuranceMessage> bikeInsuranceMessageList = new ArrayList<>();

    private int applyPayout = 0; //0为可申请索赔  1为已申请索赔 2为申请被驳回
    private int getMoneyDispose = 0;
    private long bikeId; //车辆ID

    private ImageView lost_dispose_back,iv_dispose_refresh;
    Timer mTimer = null;

    private final int MSG_update_bike_status = 3010;
    protected Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            try {
                switch (msg.what) {
                    case MSG_update_bike_status:
                        Bundle bundle = msg.getData();
                        long interval = bundle.getLong("INTERVAL");

                        //保险在有效期内
                            if (interval > 0 && interval < 60 * 1000) {
                                mTimer.cancel();

                                mBi.setLostStatus("4");
                                mBi.setPayoutStatus("");
                                refreshBikeStatus(mBi);


                            } else {
                                ll_finding_lost.setBackground(ContextCompat.getDrawable(context,R.drawable.shape_corner_blue));
                                ll_finding_lost.setClickable(false);
                                ll_apply_payout.setBackground(ContextCompat.getDrawable(context,R.drawable.shape_corner_gray));
                                ll_apply_payout.setClickable(false);
                            }


                        break;
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    };



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bike_dispose_chart);
        initView();

    }


    private void initView() {
        mBi = (Bike) getIntent().getSerializableExtra("BIKE");
        bikeId = getIntent().getLongExtra("bikeId",-1);
        if (mBi.getPayoutStatus().equals("4")) {
            ToastUtils.showTextToast(context,"此车辆已索赔提现完成");

            EventBusMsg evBg = new EventBusMsg();
            evBg.setMsgType(EventBusMsg.MSG_APPLY_CASH_FINISH);
            EventBus.getDefault().post(evBg);

            BikeLostDisposeActivity.this.finish();
        }

        initEvent();


    }

    private void getBikeInsurance() {
        super.doSocket();
        final GetInsuranceInfoByBikeIdRequest.GetInsuranceInfoByBikeIdRequestMessage.Builder requestMessage = GetInsuranceInfoByBikeIdRequest.GetInsuranceInfoByBikeIdRequestMessage.newBuilder();
        requestMessage.setSession(PreferenceData.loadSession(this));
        requestMessage.setBikeId(mBi.getId());
        new Thread() {
            public void run() {
                SocketClient client;
                client = SocketClient.getInstance();
                client.send(SocketAppPacket.COMMAND_ID_GET_BIKE_INSURANCE_LIST, requestMessage.build().toByteArray());
            }
        }.start();
    }


    private void initEvent() {
        if (mBi != null) {
            getBikeInsurance();
        }

        lost_dispose_back = findViewByIds(R.id.lost_dispose_back);
        lost_dispose_back.setOnClickListener(this);
        ll_apply_lost = findViewByIds(R.id.ll_apply_lost);
        ll_apply_lost.setOnClickListener(this);
        ll_check_lost = findViewByIds(R.id.ll_check_lost);

        ll_finding_lost = findViewByIds(R.id.ll_finding_lost);

        ll_apply_payout = findViewByIds(R.id.ll_apply_payout);
        ll_apply_payout.setOnClickListener(this);
        ll_check_payout = findViewByIds(R.id.ll_check_payout);
        ll_check_payout.setOnClickListener(this);

        iv_dispose_refresh = findViewByIds(R.id.iv_dispose_refresh);
        iv_dispose_refresh.setOnClickListener(this);

        ll_getmoney_payout = findViewByIds(R.id.ll_getmoney_payout);
        ll_getmoney_payout.setOnClickListener(this);

        ll_diapose_over = findViewByIds(R.id.ll_diapose_over);
        ll_diapose_over.setOnClickListener(this);

        ll_bike_found = findViewByIds(R.id.ll_bike_found);

        ll_bike_return = findViewByIds(R.id.ll_bike_return);

        ll_getmoney_notify = findViewByIds(R.id.ll_getmoney_notify);
        ll_getmoney_notify.setOnClickListener(this);


    }


    @Override
    public void onClick(View v) {
        Intent intent = new Intent();
        switch (v.getId()) {
                //刷新(车辆状态)
            case R.id.iv_dispose_refresh:
                getBikeStatusById();
                break;

            case R.id.lost_dispose_back:
                finish(); //返回
                break;

            //报失申请
            case R.id.ll_apply_lost:

                intent.setClass(BikeLostDisposeActivity.this, ApplyLossBikeActivity.class);
                intent.putExtra("BIKE", mBi);
                startActivity(intent);
                break;

//
//            //车辆寻找
//            case R.id.ll_finding_lost:
//
//                break;

            //索赔申请
            case R.id.ll_apply_payout:
                if (mBi.getLostStatus().equals("4")) {
                    intent.setClass(BikeLostDisposeActivity.this, ApplyPayoutActivity.class);
                    intent.putExtra("insuranceId",bikeInsuranceMessageList.get(0).getId());
                    intent.putExtra("BIKE", mBi);
                    intent.putExtra("applyPayout",applyPayout); //0为可申请索赔  1为已申请索赔 2为审核未通过
                    startActivity(intent);
                }

                break;

            //索赔审核
            case R.id.ll_check_payout:
                if (mBi.getLostStatus().equals("4")) {
                    intent.setClass(BikeLostDisposeActivity.this, ApplyPayoutActivity.class);
                    intent.putExtra("insuranceId",bikeInsuranceMessageList.get(0).getId());
                    intent.putExtra("BIKE", mBi);
                    intent.putExtra("applyPayout",applyPayout); //0为可申请索赔  1为已申请索赔  2索赔申请驳回
                    startActivity(intent);
                }

                break;

            //理赔通知
            case R.id.ll_getmoney_payout:
                if (mBi.getLostStatus().equals("4")) {
                    intent.setClass(BikeLostDisposeActivity.this, ApplyGetMoneyActivity.class);
                    intent.putExtra("insuranceId",bikeInsuranceMessageList.get(0).getId());
                    intent.putExtra("plateNumber",mBi.getPlateNumber());
                    intent.putExtra("getMoneyDispose",getMoneyDispose);
                    intent.putExtra("bikeid",mBi.getId());
                    startActivity(intent);
                }

                break;

            //理赔消息
            case R.id.ll_getmoney_notify:
                if (mBi.getLostStatus().equals("4")) {
                    intent.setClass(BikeLostDisposeActivity.this, ApplyGetMoneyActivity.class);
                    intent.putExtra("insuranceId",bikeInsuranceMessageList.get(0).getId());
                    intent.putExtra("getMoneyDispose",getMoneyDispose);
                    intent.putExtra("bikeid",mBi.getId());
                    intent.putExtra("plateNumber",mBi.getPlateNumber());
                    startActivity(intent);
                }

                break;


            //结束
            case R.id.ll_diapose_over:

                break;

//            //是否找到
//            case R.id.ll_bike_found:
//                break;
//            //车辆归还
//            case R.id.ll_bike_return:
//                break;


            default:
                break;


        }

    }

    private void getBikeStatusById() {
        super.doSocket();
        BaseApplication.getInstance().getbyidflag = "BikeLostDisposeActivity";
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
    public void onEventMainThread(EventBusMsg eventPackage) {
        switch (eventPackage.getMsgType()) {

            //状态值 101(已申请丢失) 102(已申请索赔) 103(可<重新>申请索赔) 104(已申请提现)
            case EventBusMsg.BIKE_STATUS_REFRESH: // 刷新状态
                if (eventPackage.getValue() == 101) {
                    mBi.setLostStatus("0");
                    mBi.setPayoutStatus("");
                    refreshBikeStatus(mBi);
                }else if (eventPackage.getValue() == 102) {
                    mBi.setLostStatus("4");
                    mBi.setPayoutStatus("0");
                    refreshBikeStatus(mBi);
                }else if (eventPackage.getValue() == 103) {
                    mBi.setLostStatus("4");
                    mBi.setPayoutStatus("");
                    refreshBikeStatus(mBi);
                }else if (eventPackage.getValue() == 104) {
                    mBi.setLostStatus("4");
                    mBi.setPayoutStatus("3");
                    refreshBikeStatus(mBi);
                }
                 break;




        }
    }


    @Override
    public void onEventMainThread(SocketAppPacket eventPackage) {
        super.onEventMainThread(eventPackage);
        try {
            if (eventPackage.getCommandId() == SocketAppPacket.COMMAND_ID_GET_BIKE_INSURANCE_LIST) {
                final GetBikeInsurancesByBikeIdResponse.GetBikeInsurancesByBikeIdResponseMessage insuListResponse
                        = GetBikeInsurancesByBikeIdResponse.GetBikeInsurancesByBikeIdResponseMessage.parseFrom(eventPackage.getCommandData());

                if (dlgWaiting.isShowing()) {
                    dlgWaiting.dismiss();
                }
                mDlgWaitingHandler.removeMessages(MSG_cannt_get_data);
                if (insuListResponse.getErrorMsg().getErrorCode() != 0) {
                    Toast.makeText(this, insuListResponse.getErrorMsg().getErrorMsg(), Toast.LENGTH_LONG).show();
                    if (insuListResponse.getErrorMsg().getErrorCode() == 20003) {
                        Intent intent = new Intent();
                        intent.setClass(this, LoginActivity.class);
                        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                        startActivity(intent);
                    }
                } else {
                    for (int i = 0; i < insuListResponse.getBikeInsuranceMessageList().size(); i++) {
                        if (insuListResponse.getBikeInsuranceMessageList().get(i).getEffectiveDate() < System.currentTimeMillis()
                                && insuListResponse.getBikeInsuranceMessageList().get(i).getExpirationDate() > System.currentTimeMillis()) {
                            bikeInsuranceMessageList.add(insuListResponse.getBikeInsuranceMessageList().get(i));
                        }
                    }

                    refreshBikeStatus(mBi);


                }
            }else if (eventPackage.getCommandId() == SocketAppPacket.COMMAND_ID_GETBIKEMSG_BY_BIKEID && BaseApplication.getInstance().getbyidflag.equals("BikeLostDisposeActivity")){
                final GetBikesResponse.GetBikesResponseMessage getBikesResponseMessage = GetBikesResponse.GetBikesResponseMessage.parseFrom(eventPackage.getCommandData());

                if (dlgWaiting.isShowing()) {
                    dlgWaiting.dismiss();
                }
                mDlgWaitingHandler.removeMessages(MSG_cannt_get_data);
                if (getBikesResponseMessage.getErrorMsg().getErrorCode() != 0) {
                    Toast.makeText(this, getBikesResponseMessage.getErrorMsg().getErrorMsg(), Toast.LENGTH_LONG).show();
                    if (getBikesResponseMessage.getErrorMsg().getErrorCode() == 20003) {
                        Intent intent = new Intent();
                        intent.setClass(this, LoginActivity.class);
                        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                        startActivity(intent);
                    }
                } else {
                    BikeMessage bikeMessage = getBikesResponseMessage.getBikeList().get(0);
                    String payoutStatus = bikeMessage.getPayoutStatus();
                    String lostStatus = bikeMessage.getLostStatus();
                    mBi.setLostStatus(lostStatus);
                    mBi.setPayoutStatus(payoutStatus);

                    if (mBi.getLostStatus().equals("1")) {
                        mBi.setLostEndDate(bikeMessage.getLostEndDate());
                    }

                    refreshBikeStatus(mBi);

                }
            }
        } catch (InvalidProtocolBufferException e1) {
            e1.printStackTrace();
        }
    }

    void refreshBikeStatus(final Bike bike) {

        switch (bike.getLostStatus()) {
            case ""://正常使用
                ll_apply_lost.setBackground(ContextCompat.getDrawable(this,R.drawable.shape_corner_blue));
                ll_apply_lost.setClickable(true);
                ll_check_lost.setBackground(ContextCompat.getDrawable(this,R.drawable.shape_corner_gray));
                ll_check_lost.setClickable(false);
                ll_bike_found.setBackground(ContextCompat.getDrawable(this,R.drawable.shape_corner_gray));
                ll_bike_found.setClickable(false);

                break;
            case "0"://已申请丢失,等待后台审核
                ll_apply_lost.setBackground(ContextCompat.getDrawable(this,R.drawable.shape_corner_gray));
                ll_apply_lost.setClickable(false);
                ll_check_lost.setBackground(ContextCompat.getDrawable(this,R.drawable.shape_corner_blue));
                ll_check_lost.setClickable(false);

                ll_bike_found.setBackground(ContextCompat.getDrawable(this,R.drawable.shape_corner_gray));
                ll_bike_found.setClickable(false);

                break;
            case "1"://车辆丢失审核通过，倒计时中

                ll_apply_lost.setBackground(ContextCompat.getDrawable(this,R.drawable.shape_corner_gray));
                ll_apply_lost.setClickable(false);

                ll_check_lost.setBackground(ContextCompat.getDrawable(this,R.drawable.shape_corner_gray));
                ll_check_lost.setClickable(false);
                ll_finding_lost.setBackground(ContextCompat.getDrawable(this,R.drawable.shape_corner_blue));
                ll_finding_lost.setClickable(false);


                    if (mTimer != null) {
                        mTimer = null;
                    }

                    mTimer = new Timer();
                    TimerTask task = new TimerTask() {
                        @Override
                        public void run() {
                            long interval = mBi.getLostEndDate() - DateUtils.getLongFromDate(new Date());

                            //多加1分钟防止时间误差
                            interval += 60 * 1000;
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

            case "2"://申请丢失驳回

                ll_apply_lost.setBackground(ContextCompat.getDrawable(this,R.drawable.shape_corner_blue));
                ll_apply_lost.setClickable(true);

                ll_check_lost.setBackground(ContextCompat.getDrawable(this,R.drawable.shape_corner_gray));
                ll_check_lost.setClickable(false);
                ll_finding_lost.setBackground(ContextCompat.getDrawable(this,R.drawable.shape_corner_gray));
                ll_finding_lost.setClickable(false);


                break;
            case "3"://已找回
                if (mTimer != null) {
                    mTimer.cancel();
                }
                ll_apply_lost.setBackground(ContextCompat.getDrawable(this,R.drawable.shape_corner_blue));
                ll_apply_lost.setClickable(true);

                ll_check_lost.setBackground(ContextCompat.getDrawable(this,R.drawable.shape_corner_gray));
                ll_check_lost.setClickable(false);
                ll_finding_lost.setBackground(ContextCompat.getDrawable(this,R.drawable.shape_corner_gray));
                ll_finding_lost.setClickable(false);
                ll_bike_found.setBackground(ContextCompat.getDrawable(this,R.drawable.shape_corner_gray));
                ll_bike_found.setClickable(false);

                break;
            case "4":
                //已丢失
                if (mTimer != null) {
                    mTimer.cancel();
                }
                //有效保险中+
                if (bikeInsuranceMessageList.size() > 0) {
                    ll_apply_lost.setBackground(ContextCompat.getDrawable(this,R.drawable.shape_corner_gray));
                    ll_apply_lost.setClickable(false);

                    ll_check_lost.setBackground(ContextCompat.getDrawable(this,R.drawable.shape_corner_gray));
                    ll_check_lost.setClickable(false);
                    ll_finding_lost.setBackground(ContextCompat.getDrawable(this,R.drawable.shape_corner_gray));
                    ll_finding_lost.setClickable(false);

                    switch (bike.getPayoutStatus()) {
                        case "": //申请索赔
                            applyPayout = 0;
                            ll_apply_payout.setBackground(ContextCompat.getDrawable(this,R.drawable.shape_corner_blue));
                            ll_apply_payout.setClickable(true);
                            ll_check_payout.setBackground(ContextCompat.getDrawable(this,R.drawable.shape_corner_gray));
                            ll_check_payout.setClickable(false);
                            ll_getmoney_payout.setBackground(ContextCompat.getDrawable(this,R.drawable.shape_corner_gray));
                            ll_getmoney_payout.setClickable(false);
                            ll_getmoney_notify.setBackground(ContextCompat.getDrawable(this,R.drawable.shape_corner_gray));
                            ll_getmoney_notify.setClickable(false);

                            break;
                        case "0": //已申请索赔
                            applyPayout = 1;
                            ll_apply_payout.setBackground(ContextCompat.getDrawable(this,R.drawable.shape_corner_gray));
                            ll_apply_payout.setClickable(false);
                            ll_check_payout.setBackground(ContextCompat.getDrawable(this,R.drawable.shape_corner_blue));
                            ll_check_payout.setClickable(true);
                            ll_getmoney_notify.setBackground(ContextCompat.getDrawable(this,R.drawable.shape_corner_gray));
                            ll_getmoney_notify.setClickable(false);
                            ll_getmoney_payout.setBackground(ContextCompat.getDrawable(this,R.drawable.shape_corner_gray));
                            ll_getmoney_payout.setClickable(false);

                            break;
                        case "1": //审核通过
                            getMoneyDispose = 0;
                            ll_apply_payout.setBackground(ContextCompat.getDrawable(this,R.drawable.shape_corner_gray));
                            ll_apply_payout.setClickable(false);
                            ll_check_payout.setBackground(ContextCompat.getDrawable(this,R.drawable.shape_corner_gray));
                            ll_check_payout.setClickable(false);
                            ll_getmoney_payout.setBackground(ContextCompat.getDrawable(this,R.drawable.shape_corner_blue));
                            ll_getmoney_payout.setClickable(true);
                            ll_getmoney_notify.setBackground(ContextCompat.getDrawable(this,R.drawable.shape_corner_gray));
                            ll_getmoney_notify.setClickable(false);

                            break;
                        case "2": //未通过审核
                            applyPayout = 2;
                            ll_apply_payout.setBackground(ContextCompat.getDrawable(this,R.drawable.shape_corner_gray));
                            ll_apply_payout.setClickable(false);
                            ll_check_payout.setBackground(ContextCompat.getDrawable(this,R.drawable.shape_corner_blue));
                            ll_check_payout.setClickable(true);
                            ll_getmoney_payout.setBackground(ContextCompat.getDrawable(this,R.drawable.shape_corner_gray));
                            ll_getmoney_payout.setClickable(false);
                            ll_getmoney_notify.setBackground(ContextCompat.getDrawable(this,R.drawable.shape_corner_gray));
                            ll_getmoney_notify.setClickable(false);

                            break;
                        case "3": //已申请提现
                            getMoneyDispose = 1;
                            ll_apply_payout.setBackground(ContextCompat.getDrawable(this,R.drawable.shape_corner_gray));
                            ll_apply_payout.setClickable(false);
                            ll_check_payout.setBackground(ContextCompat.getDrawable(this,R.drawable.shape_corner_gray));
                            ll_check_payout.setClickable(false);
                            ll_getmoney_payout.setBackground(ContextCompat.getDrawable(this,R.drawable.shape_corner_gray));
                            ll_getmoney_payout.setClickable(false);
                            ll_getmoney_notify.setBackground(ContextCompat.getDrawable(this,R.drawable.shape_corner_blue));
                            ll_getmoney_notify.setClickable(true);


                            break;
                        case "4": //已提现
                            ll_apply_payout.setBackground(ContextCompat.getDrawable(this,R.drawable.shape_corner_gray));
                            ll_apply_payout.setClickable(false);
                            ll_check_payout.setBackground(ContextCompat.getDrawable(this,R.drawable.shape_corner_gray));
                            ll_getmoney_payout.setBackground(ContextCompat.getDrawable(this,R.drawable.shape_corner_gray));
                            ll_getmoney_payout.setClickable(false);

                            ll_getmoney_notify.setBackground(ContextCompat.getDrawable(this,R.drawable.shape_corner_gray));
                            ll_getmoney_notify.setClickable(false);
                            ll_diapose_over.setBackground(ContextCompat.getDrawable(this,R.drawable.shape_corner_blue));
                            ll_diapose_over.setClickable(false);

                            break;
                    }

                }else {
                    ToastUtils.showTextToast(context,"您的车辆没有购买保险\n无法申请索赔");
                }

                break;
        }


    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        EventBusMsg evBg = new EventBusMsg();
        evBg.setMsgType(EventBusMsg.BIKE_STATUS_REFRESH_MAIN);
        evBg.setValue((int) bikeId);
        EventBus.getDefault().post(evBg);

        if (bikeInsuranceMessageList.size() > 0) {
            bikeInsuranceMessageList.clear();
        }
    }
}
