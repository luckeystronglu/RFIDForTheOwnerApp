package com.foxconn.rfid.theowner.base;

import android.app.ActivityManager;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.DatePickerDialog.OnDateSetListener;
import android.app.TimePickerDialog;
import android.app.TimePickerDialog.OnTimeSetListener;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.database.sqlite.SQLiteDatabase;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.FragmentActivity;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.AnimationSet;
import android.view.animation.LayoutAnimationController;
import android.view.animation.ScaleAnimation;
import android.view.animation.TranslateAnimation;
import android.view.inputmethod.InputMethodManager;
import android.widget.ArrayAdapter;
import android.widget.Toast;

import com.androidquery.callback.AjaxStatus;
import com.foxconn.rfid.theowner.R;
import com.foxconn.rfid.theowner.activity.LoadingActivity;
import com.foxconn.rfid.theowner.activity.message.ActivityMessageCenter;
import com.foxconn.rfid.theowner.model.EventBusMsg;
import com.foxconn.rfid.theowner.model.EventBusMsgPush;
import com.foxconn.rfid.theowner.socket.SocketAppPacket;
import com.foxconn.rfid.theowner.socket.SocketMsg;
import com.foxconn.rfid.theowner.util.DialogUtils;
import com.foxconn.rfid.theowner.util.WebServiceUtils;
import com.foxconn.rfid.theowner.util.logort.LogUtils;
import com.foxconn.rfid.theowner.util.logort.ToastUtils;
import com.foxconn.rfid.theowner.util.pinfo.ScreenUtils;

import net.tsz.afinal.FinalDb.DbUpdateListener;

import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

import de.greenrobot.event.EventBus;


/**
 * BaseActivity
 *
 * @author Tom
 * @date 2014/11/24 11:40
 */
public class BaseActivity extends FragmentActivity implements
        DbUpdateListener {

    private static final String TAG = BaseActivity.class.getName();

    protected Context context = this;
    protected DialogUtils dlgWaiting;
    private ConnectivityManager mConnectivityManager;
    private NetworkInfo netInfo;
    boolean isExit;
    private static final int MSG_Exit_APP = 9999;
    protected BaseApplication baseApplication;
    protected static final int MSG_cannt_get_data = 2000;
    protected Handler mDlgWaitingHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            try {
                switch (msg.what) {

                    case MSG_cannt_get_data:
                        if (dlgWaiting.isShowing()) {
                            dlgWaiting.dismiss();
                            ToastUtils.showTextToast(context, getResources().getString(R.string.network_error));
                        }
                        break;
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
//		super.handleMessage(msg);
        }
    };


    protected String getLogTAG() {
        return TAG;
    }

    protected void logMessage(String log) {
        LogUtils.logMessage(getLogTAG(), log);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        super.onCreate(savedInstanceState);
        ScreenUtils.hideTitle(this);
        dlgWaiting = DialogUtils.createDialog(context, DialogUtils.REFRESH);
        dlgWaiting.setCanceledOnTouchOutside(false);
        BaseApplication.getInstance().isPushDialogShow = false;
//		registerNetworkChange();
        EventBus.getDefault().register(this);
//		changeAppLanguage();
        App.addActivity(this);
//		baseApplication=(BaseApplication)this.getApplication();
//		getWindow().setFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON,
//				WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);// 淇濇寔灞忓箷涓嶅彉榛�

    }


    protected void dataLoaded() {

    }

    /**
     * 查找页面布局ID所对应的控件对象，不用强制转换
     *
     * @param resId
     * @param <T>
     * @return
     */
    protected <T> T findViewByIds(int resId) {
        return (T) findViewById(resId);
    }

    public void onBack(View v) {
        switch (v.getId()) {
            case R.id.btn_back:
                onBackPressed();
                finish();
                break;

            default:
                break;
        }
    }

    public void changeAppLanguage() {
        String sta = PreferenceData.loadAppLanguage(context) == 0 ? "zh" : "en";//这是SharedPreferences工具类，用于保存设置，代码很简单，自己实现吧
        // 本地语言设置
        Locale myLocale = new Locale(sta);
        Resources res = getResources();
        DisplayMetrics dm = res.getDisplayMetrics();
        Configuration conf = res.getConfiguration();
        conf.locale = myLocale;
        res.updateConfiguration(conf, dm);
    }

    public void onEventMainThread(SocketAppPacket eventPackage) {

        String msg = "onEventMainThread收到了消息：" + eventPackage.getCommandId() + eventPackage.getCommandData();
        LogUtils.logMessage("harvic", msg);

    }

    public void onEventMainThread(SocketMsg eventPackage) {

//		String msg = "onEventMainThread收到了消息：" + eventPackage.getCommandId()+eventPackage.getCommandData();
//		LogUtils.logMessage("harvic", msg);
        ToastUtils.showTextToast(context, "网络连接失败");
        if (dlgWaiting.isShowing()) {
            dlgWaiting.dismiss();
        }

    }

    public void onEventMainThread(EventBusMsg eventPackage) {

//		String msg = "onEventMainThread收到了消息：" + eventPackage.getCommandId()+eventPackage.getCommandData();
//		LogUtils.logMessage("harvic", msg);


    }

    @Override
    protected void onDestroy() {
        logMessage("--->onDestroy()...");
        super.onDestroy();
        mDlgWaitingHandler.removeMessages(MSG_cannt_get_data);
        App.activityList.remove(this);
        EventBus.getDefault().unregister(this);
//		unregisterReceiver(myNetReceiver);
    }

    /**
     * Layout鍔ㄧ敾
     *
     * @return
     */
    protected LayoutAnimationController getLayoutAnimationController() {
        int duration = 500;
        AnimationSet set = new AnimationSet(true);

        Animation animation = new AlphaAnimation(0.0f, 1.0f);
        animation.setDuration(duration);
        set.addAnimation(animation);

        animation = new TranslateAnimation(Animation.RELATIVE_TO_SELF, 0.0f,
                Animation.RELATIVE_TO_SELF, 0.0f, Animation.RELATIVE_TO_SELF,
                -1.0f, Animation.RELATIVE_TO_SELF, 0.0f);
        animation.setDuration(duration);
        set.addAnimation(animation);

        LayoutAnimationController controller = new LayoutAnimationController(
                set, 0.5f);
        controller.setOrder(LayoutAnimationController.ORDER_NORMAL);
        return controller;
    }

    protected void startScaleAnimation(View view) {
        AnimationSet set = new AnimationSet(false);
        ScaleAnimation scaleAnim = new ScaleAnimation(1.0f, 0.9f, 1.0f, 0.9f,
                Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF,
                0.5f);
        set.addAnimation(scaleAnim);
        set.setDuration(200);
        view.startAnimation(set);
    }

    protected void showToast(int msg_resId) {
        showToast(msg_resId, Toast.LENGTH_SHORT);
    }

    protected void showToast(int msg_resId, int duration) {
        Toast.makeText(this, msg_resId, duration).show();
    }

    protected void showToast(String msg) {
        showToast(msg, Toast.LENGTH_SHORT);
    }

    protected void showToast(String msg, int duration) {
        Toast.makeText(this, msg, duration).show();
    }

    protected void showMsgDlgBtn(String msg) {
        showMsgDialog(msg, 1, null);
    }

    protected void showMsgDlgBtn(int msg) {
        showMsgDialog(getString(msg), 1, null);
    }

    protected void showMsgDlg2Btn(String msg, OnClickListener listener_confirm) {
        showMsgDialog(msg, 2, listener_confirm);
    }

    protected void showMsgDlg2Btn(int msg, OnClickListener listener_confirm) {
        showMsgDialog(getString(msg), 2, listener_confirm);
    }

    protected void showMsgDialog(String msg, int btnCount,
                                 OnClickListener listener_confirm) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle(R.string.dlg_title_notify);
        builder.setMessage(msg);
        if (btnCount == 1) {
            builder.setPositiveButton(R.string.btn_txt_confirm,
                    new OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                        }
                    });
        } else if (btnCount == 2) {
            builder.setPositiveButton(R.string.btn_txt_confirm,
                    listener_confirm);
            builder.setNegativeButton(R.string.btn_txt_cancel,
                    new OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                        }
                    });
        }
        AlertDialog dialog = builder.create();
        dialog.setCanceledOnTouchOutside(false);
        dialog.show();
    }

    /**
     * 鏄剧ずListDialog
     **/
    protected void showArrayListDialog(int titleResId, String[] data,
                                       OnClickListener listener) {
        ArrayAdapter<CharSequence> adapter_dlg = new ArrayAdapter<CharSequence>(
                context, android.R.layout.simple_list_item_1,
                android.R.id.text1, data);
        AlertDialog dialog = new AlertDialog.Builder(context)
                .setTitle(titleResId)
                .setAdapter(adapter_dlg, listener)
                .setNegativeButton(R.string.btn_txt_cancel,
                        new OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog,
                                                int which) {
                                dialog.dismiss();
                            }
                        }).create();
        dialog.setCanceledOnTouchOutside(false);// 浣块櫎浜哾ialog浠ュ鐨勫湴鏂逛笉鑳借鐐瑰嚮
        dialog.show();
    }

    /**
     * 鏄剧ず鏃ユ湡Dialog
     **/
    protected void showDatePickerDialog(Date date, OnDateSetListener listener) {
        if (date == null) {
            date = new Date();
        }
        Calendar calendar = Calendar.getInstance(Locale.CHINA);
        calendar.setTime(date);
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH);
        int day = calendar.get(Calendar.DAY_OF_MONTH);
        DatePickerDialog dlg = new DatePickerDialog(context, listener, year,
                month, day);
        dlg.show();
    }

    /**
     * 鏄剧ず鏃堕棿Dialog
     **/
    protected void showTimePickerDialog(Date date, OnTimeSetListener listener) {
        if (date == null) {
            date = new Date();
        }
        Calendar calendar = Calendar.getInstance(Locale.CHINA);
        calendar.setTime(date);
        int hourOfDay = calendar.get(Calendar.HOUR_OF_DAY);
        int minute = calendar.get(Calendar.MINUTE);
        TimePickerDialog dlg = new TimePickerDialog(context, listener,
                hourOfDay, minute, true);
        dlg.show();
    }


    public void Message(String count) {
        // TODO Auto-generated method stub

    }


    /*------------------------------------鐩戝惉缃戠粶鍙樺寲 Start---------------------- */
    private void registerNetworkChange() {
        // 鎺ュ彈缃戠粶鍙樺寲鐨勫箍鎾�
        IntentFilter mFilter = new IntentFilter();
        mFilter.addAction(ConnectivityManager.CONNECTIVITY_ACTION);
        registerReceiver(myNetReceiver, mFilter);
    }

    private BroadcastReceiver myNetReceiver = new BroadcastReceiver() {

        @Override
        public void onReceive(Context context, Intent intent) {

            String action = intent.getAction();
            if (action.equals(ConnectivityManager.CONNECTIVITY_ACTION)) {
                mConnectivityManager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
                netInfo = mConnectivityManager.getActiveNetworkInfo();
                if (netInfo != null && netInfo.isAvailable()) {
                    OnNetworkConnected();
                } else {
                    OnNetworkDisconnected();
                }
            }
        }
    };

    protected void OnNetworkConnected() {

    }

    protected void OnNetworkDisconnected() {

    }

    // --------------------------------------鐩戝惉缃戠粶鍙樺寲
    // End-------------------------------------------
    // -------------------------------------call web service
    // start-------------------------------------/
    protected void callWebService(final Context context, boolean refreshFlag,
                                  String url) {
        WebServiceUtils.callWebService(context, refreshFlag, url,
                new WebServiceUtils.WebServiceCallBack() {

                    @Override
                    public void success(String url, String callbackStr,
                                        AjaxStatus status) {
                        // TODO Auto-generated method stub
                        onWebServiceReturn(url, callbackStr);
                    }

                });
    }

    protected void onWebServiceReturn(String url, String returnMsg) {

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // TODO Auto-generated method stub
        // TODO Auto-generated method stub
        try {

        } catch (Exception e) {
            System.out.print(e.getMessage());
        }
    }

    protected void doSocket() {
        dlgWaiting.show();
        mDlgWaitingHandler.sendEmptyMessageDelayed(MSG_cannt_get_data, App.WAITTING_SECOND);
    }

    public void onEventMainThread(final EventBusMsgPush eventPackage) {
        if (isActivityTop()) {
            if (isMessageCenterTop()) {
                return;
            }

            if (BaseApplication.getInstance().isPushDialogShow) {
                return;
            }
            BaseApplication.getInstance().isPushDialogShow = true;
            AlertDialog.Builder builder = new AlertDialog.Builder(context);
            builder.setTitle(eventPackage.getMsgTitle());
            builder.setMessage(eventPackage.getMsgContent());

            builder.setPositiveButton(R.string.look_up,
                    new OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            Intent intent = new Intent();
                            intent.setClass(BaseActivity.this, ActivityMessageCenter.class);
//								Bundle bundle = new Bundle();
//								bundle.putSerializable("EventBusMsgPush", eventPackage);
//								intent.toUri(0);
                            BaseApplication.getInstance().needGoToMessageCenter = true;
                            BaseApplication.getInstance().MessageType = eventPackage.getMsgType();
                            startActivity(intent);

                            BaseApplication.getInstance().isPushDialogShow = false;
                        }
                    });
            builder.setNegativeButton(R.string.ignore,
                    new OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                            BaseApplication.getInstance().isPushDialogShow = false;
                        }
                    });
            AlertDialog dialog = builder.create();
            dialog.setCanceledOnTouchOutside(false);
            dialog.show();

        }
    }

    protected boolean isActivityTop() {
        ActivityManager manager = (ActivityManager) getSystemService(Context.ACTIVITY_SERVICE);
        String name = manager.getRunningTasks(1).get(0).topActivity.getClassName();
        return name.equals(this.getClass().getName());
    }

    protected boolean isMessageCenterTop() {
        ActivityManager manager = (ActivityManager) getSystemService(Context.ACTIVITY_SERVICE);
        String name = manager.getRunningTasks(1).get(0).topActivity.getClassName();
        return name.equals(ActivityMessageCenter.class.getName());
    }

    protected boolean isLoadingTop() {
        ActivityManager manager = (ActivityManager) getSystemService(Context.ACTIVITY_SERVICE);
        String name = manager.getRunningTasks(1).get(0).topActivity.getClassName();
        return name.equals(LoadingActivity.class.getName());
    }

    protected void showKeyBoard() {
        InputMethodManager imm = (InputMethodManager) context.getSystemService(Context
                .INPUT_METHOD_SERVICE);
        imm.toggleSoftInput(InputMethodManager.SHOW_FORCED, 0);
    }

    protected void hideKeyBoard() {
        InputMethodManager imm = (InputMethodManager) context.getSystemService(Context
                .INPUT_METHOD_SERVICE);
        imm.toggleSoftInput(0, 0);
    }
}
