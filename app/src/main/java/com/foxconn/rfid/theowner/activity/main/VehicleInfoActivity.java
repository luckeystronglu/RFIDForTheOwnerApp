package com.foxconn.rfid.theowner.activity.main;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;

import com.foxconn.rfid.theowner.R;
import com.foxconn.rfid.theowner.base.BaseActivity;
import com.foxconn.rfid.theowner.model.Bike;
import com.foxconn.rfid.theowner.util.string.DateUtils;

import java.text.DecimalFormat;


/**
 * 车辆信息
 *
 * @author WT00111
 */
public class VehicleInfoActivity extends BaseActivity {
    private TextView tv_car_label, tv_car_num, tv_car_model, tv_car_color, tv_car_purchase_date, tv_car_purchase_price, tv_car_purchase_location, tv_car_license_location, tv_car_use_property, tv_car_load_num,tv_car_battery_status;
private ImageButton btn_back;

    @Override
    public void onCreate( Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_vehicleinfo);
        initView();
        initData();
    }

    protected void initView() {
        tv_car_label =(TextView) findViewById(R.id.tv_car_label);
        tv_car_num = (TextView) findViewById(R.id.tv_car_num);
        tv_car_model = (TextView) findViewById(R.id.tv_car_model);
        tv_car_color = (TextView) findViewById(R.id.tv_car_color);
        tv_car_purchase_date = (TextView) findViewById(R.id.tv_car_purchase_date);
        tv_car_purchase_price =(TextView)  findViewById(R.id.tv_car_purchase_price);
        tv_car_purchase_location =(TextView)  findViewById(R.id.tv_car_purchase_location);
        tv_car_license_location = (TextView) findViewById(R.id.tv_car_license_location);
        tv_car_use_property =(TextView)  findViewById(R.id.tv_car_use_property);
        tv_car_load_num =(TextView)  findViewById(R.id.tv_car_load_num);
        tv_car_battery_status=(TextView)  findViewById(R.id.tv_car_battery_status);
        btn_back=(ImageButton) findViewById(R.id.btn_back);
    }

    protected void initData() {
        Intent intent=this.getIntent();
        Bike bike= (Bike)intent.getSerializableExtra("BIKE");
        if(bike==null)
            return;
        tv_car_label.setText(String.valueOf(bike.getCardNo()));
        tv_car_num.setText(bike.getPlateNumber());
        tv_car_model.setText(bike.getBrandModel());
        tv_car_color.setText(bike.getColor());
        tv_car_purchase_date .setText(DateUtils.getStringFromLong(bike.getPurchaseDate(),"yyyy-MM-dd"));
//        tv_car_purchase_price.setText(String.valueOf(bike.getPurchasePrice()));
        tv_car_purchase_price.setText(new DecimalFormat("#.00").format(bike.getPurchasePrice()));
        tv_car_purchase_location .setText(bike.getAreaName());
        tv_car_license_location .setText(bike.getAreaName());
        tv_car_use_property.setText(bike.getUsageNature());
        tv_car_load_num.setText(String.valueOf(bike.getLoadNumber()));
        if (bike.cardBatteryStatus == 0) {
            tv_car_battery_status.setText("正常");
        }else {
            tv_car_battery_status.setText("低电");
        }

        btn_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                VehicleInfoActivity.this.finish();
            }
        });
    }


}
