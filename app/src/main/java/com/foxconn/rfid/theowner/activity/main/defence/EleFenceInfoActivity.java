/**
 *
 */
package com.foxconn.rfid.theowner.activity.main.defence;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import com.baidu.mapapi.map.MapStatusUpdate;
import com.baidu.mapapi.map.MapStatusUpdateFactory;
import com.baidu.mapapi.model.LatLng;
import com.foxconn.rfid.theowner.R;

/**
 * @author WT00111
 */
public class EleFenceInfoActivity extends EleFenceBaseActivity implements View.OnClickListener {

    private Button test;
    private ImageView locationIcon;

//    @Override
//    public void onCreate(@Nullable Bundle savedInstanceState, @Nullable PersistableBundle persistentState) {
//        super.onCreate(savedInstanceState, persistentState);
//        initView();
//        initEvent();
//    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initView();
        initEvent();
    }

    @Override
    protected void initView() {
        setContentView(R.layout.fragment_fence_information);
        super.initView();
        test = (Button) findViewById(R.id.fenceList);
        locationIcon = (ImageView) findViewById(R.id.locationIcon);
        initMap();// 初始化地图
    }



    protected void initEvent() {
        test.setOnClickListener(this);
        locationIcon.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.fenceList:// 电子围栏
                startActivity(new Intent(this, EleFenceListActivity.class));
                break;
            case R.id.locationIcon:// 当前位置信息
                LatLng point = new LatLng(29.607396, 106.378996);
                MapStatusUpdate mapstatusUpdatePoint = MapStatusUpdateFactory.newLatLngZoom(point, 15);
                // 带动画的更新地图状态，还是300毫秒
                mBaiduMap.animateMapStatus(mapstatusUpdatePoint, 1000);
                break;
            default:
                break;
        }
    }
}
