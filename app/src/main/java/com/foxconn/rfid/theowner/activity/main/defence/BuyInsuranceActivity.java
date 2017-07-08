package com.foxconn.rfid.theowner.activity.main.defence;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;

import com.foxconn.rfid.theowner.R;
import com.foxconn.rfid.theowner.activity.LoginActivity;
import com.foxconn.rfid.theowner.base.BaseActivity;
import com.foxconn.rfid.theowner.base.PreferenceData;
import com.foxconn.rfid.theowner.model.Bike;
import com.foxconn.rfid.theowner.model.EventBusMsg;
import com.foxconn.rfid.theowner.socket.SocketAppPacket;
import com.foxconn.rfid.theowner.socket.SocketClient;
import com.foxconn.rfid.theowner.util.logort.ToastUtils;
import com.foxconn.rfid.theowner.view.widgets.DateSelectDialog;
import com.foxconn.rfid.theowner.view.widgets.InputView;
import com.foxconn.rfid.theowner.view.widgets.SelectView;
import com.google.protobuf.InvalidProtocolBufferException;
import com.yzh.rfidbike.app.request.AddBikeInsuranceRequest;
import com.yzh.rfidbike.app.request.GetInsuranceRequest;
import com.yzh.rfidbike.app.response.BikeInsurance;
import com.yzh.rfidbike.app.response.CommonResponse;
import com.yzh.rfidbike.app.response.GetInsuranceResponse;
import com.yzh.rfidbike.app.response.Insurance;

import java.util.ArrayList;
import java.util.List;

import cn.qqtheme.framework.picker.OptionPicker;
import de.greenrobot.event.EventBus;

/**
 * Created by appadmin on 2017/5/3.
 */

public class BuyInsuranceActivity extends BaseActivity implements View.OnClickListener {
    private SelectView mSelectInsureClass; //保险种类
    private SelectView effective_date;  //生效日期
    private InputView mInputInsureNumber, mInputInsureMoney, mInputInsureMoneyMax;
    private InputView mInputInsureCompany, mInputInsureMonthValid;
    private EditText mEtRemark;
    private Bike mBike;
    private ImageView buy_insurance_back;
    private long mCurrentTime = 0;

    private DateSelectDialog mDateSelectDialog;
    private List<Insurance.InsuranceMessage> mInsuranceMessageList;
    private List<String> mInsureStringList;

//    public static final int INSURANCE_RESULT_CODE = 0x334;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_buy_insure);
        initView();

    }

    private void initView() {
        mBike = (Bike) getIntent().getSerializableExtra("BIKE");

        mSelectInsureClass = findViewByIds(R.id.select_insure_class);
        effective_date = findViewByIds(R.id.effective_date);
        mInputInsureNumber = findViewByIds(R.id.input_insure_number);
        mInputInsureMoney = findViewByIds(R.id.input_insure_money);
        mInputInsureMoneyMax = findViewByIds(R.id.input_insure_money_max);
        mInputInsureCompany = findViewByIds(R.id.input_insure_company);
        mInputInsureMonthValid = findViewByIds(R.id.input_insure_month_valid);
        mEtRemark = findViewByIds(R.id.et_remark);
        buy_insurance_back = findViewByIds(R.id.buy_insurance_back);
        mDateSelectDialog = new DateSelectDialog(this);

//        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy年MM月dd日");
//        String time = simpleDateFormat.format(new Date(System.currentTimeMillis()));
//        mCurrentTime = System.currentTimeMillis();

        initEvent();

        mInsureStringList = new ArrayList<>();
    }

    private void initEvent() {
        mSelectInsureClass.setOnClickListener(this);
        effective_date.setOnClickListener(this);
        buy_insurance_back.setOnClickListener(this);

        mDateSelectDialog.setListener(new DateSelectDialog.DateSelectListener() {
            @Override
            public void selected(long time, String date) {
                mCurrentTime = time;
                effective_date.setContent(date);
            }

            @Override
            public void cancel() {

            }

            @Override
            public void dismiss() {
//                mDateView.setDisplay(false);
            }
        });
    }


    public void buyInsuranceSubmit(View view) {
        //提交保险信息
//        ToastUtils.showTextToast(this,"提交保险信息");
        if (mInputInsureNumber.getContent().equals("")) {
            ToastUtils.showTextToast(this, "请正确填写保单号");
            return;
        }

        if (message == null) {
            ToastUtils.showTextToast(this,"请选择保险类型");
            return;
        }

        if (effective_date.getContent().equals("")){
            ToastUtils.showTextToast(this,"请选择生效日期");
            return;
        }

        doSubmit();


//        else if (message == null){
//            ToastUtils.showTextToast(this,"请完善保险信息");
//            return;
//        }
//        if (message != null) {
//            doSubmit();
//        }else {
//            ToastUtils.showTextToast(this,"请完善保险信息");
//        }

    }

    private void doSubmit() {

        super.doSocket();
        final AddBikeInsuranceRequest.AddBikeInsuranceRequestMessage.Builder addInsurancebuilder
                = AddBikeInsuranceRequest.AddBikeInsuranceRequestMessage.newBuilder();
        addInsurancebuilder.setSession(PreferenceData.loadSession(this));
        BikeInsurance.BikeInsuranceMessage.Builder bInsurance = BikeInsurance.BikeInsuranceMessage.newBuilder();
        bInsurance.setBikeId(mBike.getId());
        bInsurance.setInsuranceType(message.getInsuranceType());
        bInsurance.setInsuranceNo(mInputInsureNumber.getContent());
        bInsurance.setInsuranceAmount(message.getInsuranceAmount());
        bInsurance.setCompensationAmount(message.getCompensationAmount());
        bInsurance.setInsuranceCompany(message.getInsuranceCompany());
        bInsurance.setValidMonth(message.getValidMonth());
        bInsurance.setEffectiveDate(mCurrentTime);
        bInsurance.setRemarks(message.getRemarks());

        addInsurancebuilder.setBikeInsuranceMessage(bInsurance);

        new Thread() {
            public void run() {
                SocketClient client;
                client = SocketClient.getInstance();
                client.send(SocketAppPacket.COMMAND_ID_BIKE_INSURE, addInsurancebuilder.build()
                        .toByteArray());
            }
        }.start();
    }


    @Override
    protected void doSocket() {
        super.doSocket();
        final GetInsuranceRequest.GetInsuranceRequestMessage.Builder requestMessage =
                GetInsuranceRequest.GetInsuranceRequestMessage.newBuilder();
        Insurance.InsuranceMessage insuranceMessage = Insurance.InsuranceMessage.newBuilder()
                .build();
        requestMessage.setSession(PreferenceData.loadSession(this));
        requestMessage.setInsuranceMessage(insuranceMessage);
        new Thread() {
            public void run() {
                SocketClient client;
                client = SocketClient.getInstance();
                client.send(SocketAppPacket.COMMAND_ID_BIKE_INSURE_LIST, requestMessage.build()
                        .toByteArray());
            }

        }.start();
    }

    Insurance.InsuranceMessage message = null;

    @Override
    public void onEventMainThread(SocketAppPacket eventPackage) {
        try {
            super.onEventMainThread(eventPackage);
            if (eventPackage.getCommandId() == SocketAppPacket.COMMAND_ID_BIKE_INSURE_LIST) {
                GetInsuranceResponse.GetInsuranceResponseMessage responseMessage =
                        GetInsuranceResponse.GetInsuranceResponseMessage.parseFrom(eventPackage.getCommandData());
                if (dlgWaiting.isShowing()) {
                    dlgWaiting.dismiss();
                }
                mDlgWaitingHandler.removeMessages(MSG_cannt_get_data);
                if (responseMessage.getErrorMsg().getErrorCode() != 0) {
                    ToastUtils.showTextToast(this, responseMessage.getErrorMsg().getErrorMsg());
                    if (responseMessage.getErrorMsg().getErrorCode() == 20003) {
                        Intent intent = new Intent();
                        intent.setClass(this, LoginActivity.class);
                        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                        startActivity(intent);
                    }
                } else {
                    mInsureStringList.clear();
                    mInsuranceMessageList = responseMessage.getInsuranceMessageList();
                    for (Insurance.InsuranceMessage message : mInsuranceMessageList) {
                        mInsureStringList.add(message.getInsuranceType());
                    }

                    OptionPicker optionPicker = new OptionPicker(this,
                            mInsureStringList);
                    optionPicker.setCycleDisable(true);
                    optionPicker.setOnOptionPickListener(new OptionPicker.OnOptionPickListener() {
                        @Override
                        public void onOptionPicked(int index, String item) {
                            mSelectInsureClass.setContent(item);
                            message = mInsuranceMessageList.get(index);
                            mInputInsureMoney.setContent(message.getInsuranceAmount() + "");
                            mInputInsureMoneyMax.setContent(message.getCompensationAmount() + "");
                            mInputInsureCompany.setContent(message.getInsuranceCompany());
                            mInputInsureMonthValid.setContent(message.getValidMonth() + "");
                            mEtRemark.setText(message.getRemarks());

                        }
                    });
                    optionPicker.show();
                }
            } else if (eventPackage.getCommandId() == SocketAppPacket.COMMAND_ID_BIKE_INSURE) {
                CommonResponse.CommonResponseMessage commonResponseMessage = CommonResponse.CommonResponseMessage.parseFrom(eventPackage.getCommandData());
                if (dlgWaiting.isShowing()) {
                    dlgWaiting.dismiss();
                }
                mDlgWaitingHandler.removeMessages(MSG_cannt_get_data);
                if (commonResponseMessage.getErrorMsg().getErrorCode() != 0) {
                    ToastUtils.showTextToast(this, commonResponseMessage.getErrorMsg().getErrorMsg());
                    if (commonResponseMessage.getErrorMsg().getErrorCode() == 20003) {
                        Intent intent = new Intent();
                        intent.setClass(this, LoginActivity.class);
                        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                        startActivity(intent);
                    }
                } else {
                    ToastUtils.showTextToast(this, "保险信息提交成功");

                    EventBusMsg msg = new EventBusMsg();
                    msg.setMsgType(EventBusMsg.MSG_INSURANCE_STATUS);
                    EventBus.getDefault().post(msg);

                    BuyInsuranceActivity.this.finish();

                }

            }
        } catch (InvalidProtocolBufferException e) {
            e.printStackTrace();
        }
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.select_insure_class:
                doSocket();
                break;

            case R.id.buy_insurance_back:
                BuyInsuranceActivity.this.finish();
                break;

            case R.id.effective_date:
                mDateSelectDialog.show();
                break;

            default:
                break;
        }
    }
}
