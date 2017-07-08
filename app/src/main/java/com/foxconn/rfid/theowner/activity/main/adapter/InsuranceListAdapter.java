package com.foxconn.rfid.theowner.activity.main.adapter;

import android.content.Context;
import android.graphics.Color;

import com.foxconn.rfid.theowner.R;
import com.yzh.rfidbike.app.response.BikeInsurance;

import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by appadmin on 2017/5/4.
 */

public class InsuranceListAdapter extends AbsBaseAdapter<BikeInsurance.BikeInsuranceMessage>{
    public InsuranceListAdapter(Context context) {
        super(context, R.layout.item_insurance_info);
    }

    @Override
    public void bindDatas(ViewHodler viewHodler, BikeInsurance.BikeInsuranceMessage data) {
        SimpleDateFormat time = new SimpleDateFormat("yyyy-MM-dd");

        if (data.getExpirationDate() > System.currentTimeMillis()) {
            viewHodler
                    .setTextView(R.id.tv_insurance_num,data.getInsuranceNo(), Color.parseColor("#ff33b5e5"))
                    .setTextView(R.id.tv_insurance_name,data.getInsuranceType(), Color.parseColor("#ff33b5e5"))
                    .setTextView(R.id.tv_startTime,time.format(new Date(data.getEffectiveDate())), Color.parseColor("#ff33b5e5"))
                    .setTextView(R.id.tv_endTime,time.format(new Date(data.getExpirationDate())), Color.parseColor("#ff33b5e5"))
                    .setTextView(R.id.tv_money,"￥" + new DecimalFormat("#.00").format(data.getInsuranceAmount()) + "元", Color.parseColor("#ff33b5e5"));
        }else {
            viewHodler
                    .setTextView(R.id.tv_insurance_num,data.getInsuranceNo(), Color.parseColor("#909090"))
                    .setTextView(R.id.tv_insurance_name,data.getInsuranceType(), Color.parseColor("#909090"))
                    .setTextView(R.id.tv_startTime,time.format(new Date(data.getEffectiveDate())), Color.parseColor("#909090"))
                    .setTextView(R.id.tv_endTime,time.format(new Date(data.getExpirationDate())), Color.parseColor("#909090"))
                    .setTextView(R.id.tv_money,"￥" + new DecimalFormat("#.00").format(data.getInsuranceAmount()) + "元", Color.parseColor("#909090"));
        }


    }
}
