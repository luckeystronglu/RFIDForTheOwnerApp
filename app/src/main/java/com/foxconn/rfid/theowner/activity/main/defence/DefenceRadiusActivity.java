package com.foxconn.rfid.theowner.activity.main.defence;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.ListView;

import com.foxconn.rfid.theowner.R;
import com.foxconn.rfid.theowner.base.BaseActivity;

/**
 * Created by appadmin on 2016/12/16.
 */

public class DefenceRadiusActivity extends BaseActivity {
    private String[] radius = new String[]{"500米", "1000米", "2000米", "3000米", "5000米"};
    private ListView radius_lv;
    private ImageButton imageButton;
    public static final int RESULT_CODE = 0x002;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_defenceradius);
        initView();
    }


    private void initView() {
        radius_lv = (ListView) findViewById(R.id.defence_radius_lv);
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,
               R.layout.item_radius_tv,R.id.tv_item_radius,radius);
        radius_lv.setAdapter(adapter);
        radius_lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {
                Intent intent = getIntent();
                Bundle bundle = new Bundle();
                bundle.putString("radius", radius[position]);
                intent.putExtras(bundle);
                setResult(RESULT_CODE, intent);
                finish();

            }
        });

        imageButton = (ImageButton) findViewById(R.id.radius_imgbtn_back);
        imageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }


}
