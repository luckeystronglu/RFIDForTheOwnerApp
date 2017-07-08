package com.foxconn.rfid.theowner.activity.message;

import android.app.ActivityManager;
import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;

import com.foxconn.rfid.theowner.R;
import com.foxconn.rfid.theowner.base.BaseActivity;
import com.foxconn.rfid.theowner.base.BaseApplication;
import com.foxconn.rfid.theowner.model.EventBusMsg;
import com.foxconn.rfid.theowner.model.EventBusMsgMessage;
import com.foxconn.rfid.theowner.model.EventBusMsgPush;

import java.util.ArrayList;
import java.util.List;

import de.greenrobot.event.EventBus;

public class ActivityMessageCenter extends BaseActivity implements View.OnClickListener, TabLayout.OnTabSelectedListener {

    private ViewPager vp;
    private TabLayout tabLayout;
    private String [] tabs={"系统","安防","车况"};


//    private TabView tabview;
//    private String[] datas;
//     private TabsPagerAdapter tabsPagerAdapter;

    private TextView tv_delete_all;

    private int selectTabIndex = 0;
    private AlertDialog messageAlertDialog;
    private TextView tv_cancel,tv_sure;
    private ImageView iv_cancel;
    private int safefraglistsize = -1;
    private int insuranceListSize = -1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_msg_center);
        initDialog();
        initView();
        initEvent();

    }

    private void initDialog() {
        LayoutInflater inflater = this.getLayoutInflater();
        View layout = inflater.inflate(R.layout.dialog_layout,null);
        messageAlertDialog = new AlertDialog.Builder(this).setView(layout).create();
    }


    private void initView() {

        tv_delete_all = (TextView) findViewById(R.id.tv_delete_all);
        vp = (ViewPager) findViewById(R.id.msg_viewpager);
        tabLayout = (TabLayout) findViewById(R.id.msg_tablayout);
        initViewpager(vp);
        tabLayout.setupWithViewPager(vp);
        vp.setOffscreenPageLimit(2);
        tabLayout.setOnTabSelectedListener(this);
        //此处的方法是为了页面和标题联动


//        Intent intent=new Intent();
//        EventBusMsgPush msgPush= (EventBusMsgPush) intent.getSerializableExtra("EventBusMsgPush");
//        if(intent!=null&&msgPush!=null)
//        {
//            tabLayout.getTabAt(msgPush.getMsgType()-1).select();
//        }else
//        {
//            tabLayout.getTabAt(0).select();
//        }
        if(BaseApplication.getInstance().needGoToMessageCenter)
        {
            BaseApplication.getInstance().needGoToMessageCenter=false;
            tabLayout.getTabAt( BaseApplication.getInstance().MessageType-1).select();
        }
    }

    private void initViewpager(ViewPager vp) {
        MsgCenterPagerAdapter adapter = new MsgCenterPagerAdapter(getSupportFragmentManager());
        adapter.addFragment(new FragmentSystemMsg(), tabs[0]);
        adapter.addFragment(new FragmentSafetyGuard(), tabs[1]);
        adapter.addFragment(new FragmentBikeStatus(), tabs[2]);
        vp.setAdapter(adapter);

    }


    //接收EventBus的方法
    @Override
    public void onEventMainThread(EventBusMsg eventPackage) {
        super.onEventMainThread(eventPackage);
        if (eventPackage.getEmptyType().equals(EventBusMsg.MsgEmptyType.MSG_SAFE_LIST_SIZE_CHANGE)) {
            safefraglistsize = eventPackage.getValue();
//            tv_delete_all.setVisibility(View.GONE);
        }
        if (eventPackage.getEmptyType().equals(EventBusMsg.MsgEmptyType.MSG_INSURANCE_LIST_SIZE_CHANGE)) {
            insuranceListSize = eventPackage.getValue();
        }
//        if (eventPackage.getMsgType() == EventBusMsg.PUSH_CLICK) {
//            BaseApplication.getInstance().needGoToMessageCenter = true;
//            BaseApplication.getInstance().MessageType = eventPackage.getValue();
//            tabLayout.getTabAt( BaseApplication.getInstance().MessageType-1).select();
//        }




    }

    //返回键
    public void onMessageCenterBack(View view){
        finish();
    }

    private void initEvent() {


        tv_delete_all.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //点击显示AlertDialog
                messageAlertDialog.show();

                //设置dialog的样式属性
                Window dialogView = messageAlertDialog.getWindow();
                int width = ActivityMessageCenter.this.getResources().getDisplayMetrics().widthPixels;
                WindowManager.LayoutParams lp = dialogView.getAttributes();
                dialogView.setBackgroundDrawable(new BitmapDrawable());
                lp.width = width - 160;
                lp.height = ViewGroup.LayoutParams.WRAP_CONTENT;
                //        lp.height = height / 3;
                lp.gravity = Gravity.CENTER_HORIZONTAL|Gravity.CENTER;
                //lp.x = 100;
                lp.y = -300;
                dialogView.setAttributes(lp);

                iv_cancel = (ImageView) dialogView.findViewById(R.id.delete);
                tv_cancel = (TextView) dialogView.findViewById(R.id.tv_cancel);
                tv_sure = (TextView) dialogView.findViewById(R.id.tv_sure);
                iv_cancel.setOnClickListener(ActivityMessageCenter.this);
                tv_cancel.setOnClickListener(ActivityMessageCenter.this);
                tv_sure.setOnClickListener(ActivityMessageCenter.this);

            }
        });
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.delete:
                messageAlertDialog.dismiss();
                break;
            case R.id.tv_cancel:
                messageAlertDialog.dismiss();
                break;
            case R.id.tv_sure:

                EventBusMsgMessage msgMessage = new EventBusMsgMessage();
                if (selectTabIndex == 1) {
                    msgMessage.setMsgType(EventBusMsgMessage.MSG_Message_Security_Delete);

                }else if (selectTabIndex == 2){
                    msgMessage.setMsgType(EventBusMsgMessage.MSG_Message_Insurance_Delete);

                }
                EventBus.getDefault().post(msgMessage);
                messageAlertDialog.dismiss();

                break;
        }
    }



    @Override
    public void onTabSelected(TabLayout.Tab tab) {
        switch (tab.getPosition()) {
            case 0:
                tv_delete_all.setVisibility(View.GONE);

                break;
            case 1:
                selectTabIndex = 1;
                if (safefraglistsize != -1) {
                    tv_delete_all.setVisibility(View.GONE);
                }else {
                    tv_delete_all.setVisibility(View.VISIBLE);
                }

                break;
            case 2:
                if (insuranceListSize != -1) {
                    tv_delete_all.setVisibility(View.GONE);
                }else {
                    tv_delete_all.setVisibility(View.VISIBLE);
                }

                selectTabIndex = 2;

                break;
        }
    }

    @Override
    public void onTabUnselected(TabLayout.Tab tab) {

    }

    @Override
    public void onTabReselected(TabLayout.Tab tab) {

    }


    public class MsgCenterPagerAdapter extends FragmentPagerAdapter {
        private List<Fragment> mfragment;//一个fragment的集合
        private List<String> tabs;//一个头部标题的集合
        public MsgCenterPagerAdapter(FragmentManager fm ) {
            super(fm);
            mfragment=new ArrayList<>();
            tabs=new ArrayList<>();
        }

        public void addFragment(Fragment fragment,String tab){
            mfragment.add(fragment);
            tabs.add(tab);

        }

        @Override
        public CharSequence getPageTitle(int position) {
            return tabs.get(position);
        }

        @Override
        public Fragment getItem(int position) {
            return mfragment.get(position);
        }

        @Override
        public int getCount() {
            return tabs.size();
        }
    }
//对推送消息的特殊处理:
// 1.当前页面是消息中心，tabView跳转到响应的界面，添加一条记录
// 2. 当前页面不是消息中心，弹出对话框，让用户选择是否跳转，跳转后的处理逻辑与1同
//    edit by tom
@Override
    public void onEventMainThread(final EventBusMsgPush eventPackage)
    {
        if(isActivityTop()) {
            tabLayout.getTabAt(eventPackage.getMsgType()-1).select();
        }
    }

    protected boolean isActivityTop(){
        ActivityManager manager = (ActivityManager) getSystemService(Context.ACTIVITY_SERVICE);
        String name = manager.getRunningTasks(1).get(0).topActivity.getClassName();
        return name.equals(this.getClass().getName());
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        EventBusMsgPush msgPush= (EventBusMsgPush) intent.getSerializableExtra("EventBusMsgPush");
        if(intent!=null&&msgPush!=null)
        {
            tabLayout.getTabAt(msgPush.getMsgType()-1).select();
        }else
        {
            tabLayout.getTabAt(0).select();
        }
        if(BaseApplication.getInstance().needGoToMessageCenter)
        {
            BaseApplication.getInstance().needGoToMessageCenter=false;
            tabLayout.getTabAt( BaseApplication.getInstance().MessageType-1).select();
        }
    }
}
