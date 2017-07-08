/**
 *
 */
package com.foxconn.rfid.theowner.activity.main;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.foxconn.rfid.theowner.R;
import com.foxconn.rfid.theowner.activity.LoginActivity;
import com.foxconn.rfid.theowner.activity.main.adapter.InsuranceListAdapter;
import com.foxconn.rfid.theowner.base.BaseActivity;
import com.foxconn.rfid.theowner.base.PreferenceData;
import com.foxconn.rfid.theowner.model.Bike;
import com.foxconn.rfid.theowner.model.EventBusMsg;
import com.foxconn.rfid.theowner.socket.SocketAppPacket;
import com.foxconn.rfid.theowner.socket.SocketClient;
import com.foxconn.rfid.theowner.util.logort.ToastUtils;
import com.foxconn.rfid.theowner.view.SVListView;
import com.google.protobuf.InvalidProtocolBufferException;
import com.yzh.rfidbike.app.request.GetInsuranceInfoByBikeIdRequest;
import com.yzh.rfidbike.app.response.BikeInsurance;
import com.yzh.rfidbike.app.response.GetBikeInsurancesByBikeIdResponse;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;



/**
 * @author WT00111
 */
public class MyInsuranceActivity extends BaseActivity {
    private TextView tv_buy_insurance;
    private Bike mBike;
    private SVListView svlv;
    private InsuranceListAdapter adapter;
    private ArrayList<BikeInsurance.BikeInsuranceMessage> bikeInsuranceList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_insurance);
        initView();

    }


    protected void initView() {
        mBike = (Bike) getIntent().getSerializableExtra("BIKE");
        tv_buy_insurance = findViewByIds(R.id.tv_buy_insurance);
        tv_buy_insurance.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //购买保险
//                Intent buyintent = new Intent(MyInsuranceActivity.this, BuyInsuranceActivity.class);
//                buyintent.putExtra("BIKE",mBike);
//                startActivity(buyintent);
                ToastUtils.showTextToast(context,"此功能暂未开放");
            }
        });

        svlv = findViewByIds(R.id.lv_insurance_info);
        adapter = new InsuranceListAdapter(this);
        svlv.setAdapter(adapter);

        if (mBike != null) {
            doSocket();
        }


    }

    @Override
    protected void doSocket(){
        super.doSocket();
        final GetInsuranceInfoByBikeIdRequest.GetInsuranceInfoByBikeIdRequestMessage.Builder requestMessage = GetInsuranceInfoByBikeIdRequest.GetInsuranceInfoByBikeIdRequestMessage.newBuilder();
        requestMessage.setSession(PreferenceData.loadSession(this));
        requestMessage.setBikeId(mBike.getId());
        new Thread() {
            public void run() {
                SocketClient client;
                client = SocketClient.getInstance();
                client.send(SocketAppPacket.COMMAND_ID_GET_BIKE_INSURANCE_LIST, requestMessage.build().toByteArray());
            }
        }.start();
    }

    @Override
    public void onEventMainThread(SocketAppPacket eventPackage) {
        try {
            super.onEventMainThread(eventPackage);
            if (eventPackage.getCommandId() == SocketAppPacket.COMMAND_ID_GET_BIKE_INSURANCE_LIST) {
                final GetBikeInsurancesByBikeIdResponse.GetBikeInsurancesByBikeIdResponseMessage responseMessage
                        = GetBikeInsurancesByBikeIdResponse.GetBikeInsurancesByBikeIdResponseMessage.parseFrom(eventPackage.getCommandData());

                if (dlgWaiting.isShowing()) {
                    dlgWaiting.dismiss();
                }
                mDlgWaitingHandler.removeMessages(MSG_cannt_get_data);
                if (responseMessage.getErrorMsg().getErrorCode() != 0) {
                    Toast.makeText(this, responseMessage.getErrorMsg().getErrorMsg(), Toast.LENGTH_LONG).show();
                    if (responseMessage.getErrorMsg().getErrorCode() == 20003) {
                        Intent intent = new Intent();
                        intent.setClass(this, LoginActivity.class);
                        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK|Intent.FLAG_ACTIVITY_CLEAR_TASK);
                        startActivity(intent);
                    }
                } else {
                    //                    List<BikeInsurance.BikeInsuranceMessage> bikeInsuranceList = responseMessage.getBikeInsuranceMessageList();

                    bikeInsuranceList = new ArrayList<>();
                    bikeInsuranceList.addAll(responseMessage.getBikeInsuranceMessageList());


                    if (bikeInsuranceList.size() > 0) {

                        Collections.sort(bikeInsuranceList, new Comparator<BikeInsurance.BikeInsuranceMessage>() {
                            @Override
                            public int compare(BikeInsurance.BikeInsuranceMessage o1, BikeInsurance.BikeInsuranceMessage o2) {
                                long long1 = o1.getExpirationDate() ;
                                long long2 = o2.getExpirationDate() ;
                                if (long1 - long2 > 0) {
                                    return -1;
                                }else if (long1 - long2 == 0){
                                    return 0;
                                }else {
                                    return 1;
                                }

                            }
                        });
                        //加载获取的列表数据
                        adapter.setDatas(bikeInsuranceList);
                    }

                }
            }
        } catch (InvalidProtocolBufferException e1) {
            e1.printStackTrace();
        }
    }


    @Override
    public void onEventMainThread(EventBusMsg eventPackage) {
        switch (eventPackage.getMsgType()) {
            case EventBusMsg.MSG_INSURANCE_STATUS:
               doSocket();
                break;
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (bikeInsuranceList != null) {
            bikeInsuranceList.clear();
        }
    }
}
