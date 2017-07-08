package com.foxconn.rfid.theowner.activity.message;

import android.app.ActivityManager;
import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.foxconn.rfid.theowner.R;
import com.foxconn.rfid.theowner.activity.LoginActivity;
import com.foxconn.rfid.theowner.base.App;
import com.foxconn.rfid.theowner.base.BaseFragment;
import com.foxconn.rfid.theowner.base.PreferenceData;
import com.foxconn.rfid.theowner.model.EventBusMsg;
import com.foxconn.rfid.theowner.model.EventBusMsgMessage;
import com.foxconn.rfid.theowner.model.EventBusMsgPush;
import com.foxconn.rfid.theowner.socket.SocketAppPacket;
import com.foxconn.rfid.theowner.socket.SocketClient;
import com.foxconn.rfid.theowner.util.logort.ToastUtils;
import com.google.protobuf.InvalidProtocolBufferException;
import com.handmark.pulltorefresh.library.ILoadingLayout;
import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshListView;
import com.yzh.rfidbike.app.request.GetInsuranceMessageRequest;
import com.yzh.rfidbike.app.request.GetSecurityMessageRequest;
import com.yzh.rfidbike.app.response.CommonResponse;
import com.yzh.rfidbike.app.response.GetSecurityMessageResponse;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import de.greenrobot.event.EventBus;

import static com.foxconn.rfid.theowner.socket.SocketAppPacket.COMMANT_ID_GET_SECURITY_MESSAGE_APP;

/**
 * Created by lenovo on 2016/12/22.
 */
public class FragmentSafetyGuard extends BaseFragment {
    private PullToRefreshListView bikeSecurity_lv;
    private AdapterBikeSecurity adapter;
    private List<EventBusMsgPush> securityMessageList = new ArrayList<>();
    private LinearLayout ll_bike_safety;

    private int mPosition;
    private AlertDialog deleteAlertDialog;
    private TextView tv_cancel, tv_sure;
    private ImageView iv_cancel;

    private int deleteType = 0;//0是删除某条数据   1001 表示删除整个fragment中的listView列表数据

    private int pageNum = 1;
    private int pageSize = 10;
    private static final int MSG_get_data_failed = 5005;
    private int dataFleshType = 0;//0初始加载 1表示下拉刷新 2表示上拉加载
    private boolean isLastPage = false;
    private Handler handler = new Handler() {
        @SuppressWarnings({"unused", "unchecked"})
        @Override
        public void handleMessage(Message msg) {
            try {
                switch (msg.what) {
                    case MSG_get_data_failed:

                        ToastUtils.showTextToast(getContext(), getResources().getString(R.string.get_data_error));
                        bikeSecurity_lv.postDelayed(new Runnable() {

                            @Override
                            public void run() {
                                bikeSecurity_lv.onRefreshComplete();
                            }
                        }, 1000);

                }
            } catch (Exception e) {
                e.printStackTrace();
            }
            //		super.handleMessage(msg);
        }
    };

    @Override
    protected int getContentView() {
        return R.layout.fragment_message_bikesecurity;
    }


    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initDialog();
        initView();
        initData();
    }

    private void initDialog() {
        LayoutInflater inflater = getActivity().getLayoutInflater();
        View layout = inflater.inflate(R.layout.dialog_layout_delete_one, null);
        deleteAlertDialog = new AlertDialog.Builder(getContext()).setView(layout).create();
    }


    public void initView() {
        ll_bike_safety = (LinearLayout) getActivity().findViewById(R.id.ll_bike_safety);
        ll_bike_safety.setVisibility(View.GONE);
        bikeSecurity_lv = (PullToRefreshListView) getActivity().findViewById(R.id.bike_security_lv);
        bikeSecurity_lv.setMode(PullToRefreshBase.Mode.BOTH);// 同时支持上拉下拉
        adapter = new AdapterBikeSecurity(getContext());
        adapter.setDatas(securityMessageList);
        bikeSecurity_lv.setAdapter(adapter);

    }

    public void initData() {
        //预留listView item中删除标志的点击事件
        adapter.setOnDeleteClickListener(new AdapterBikeSecurity.OnDeleteClickListener() {
            @Override
            public void onClickListen(View view, final int position) {
                mPosition = position;
                //点击显示AlertDialog
                deleteAlertDialog.show();

                //设置dialog的样式属性
                Window dialogView = deleteAlertDialog.getWindow();
                int width = getActivity().getResources().getDisplayMetrics().widthPixels;
                WindowManager.LayoutParams lp = dialogView.getAttributes();
                dialogView.setBackgroundDrawable(new BitmapDrawable());
                lp.width = width - 160;
                lp.height = ViewGroup.LayoutParams.WRAP_CONTENT;
                //        lp.height = height / 3;
                lp.gravity = Gravity.CENTER_HORIZONTAL | Gravity.CENTER;
                //lp.x = 100;
                lp.y = -300;
                dialogView.setAttributes(lp);

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
                        deleteType = 0;
                        deleteSecurityMessage();
                    }
                });


            }
        });

        bikeSecurity_lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {

            }
        });

        doSocket();

        bikeSecurity_lv.setOnRefreshListener(new PullToRefreshBase.OnRefreshListener2<ListView>() {
            @Override
            public void onPullDownToRefresh(PullToRefreshBase<ListView> refreshView) {
                setUpdateTime(refreshView);
                isLastPage = false;
                dataFleshType = 1;
                pageNum = 1;

                doRefreshSocket();
            }

            @Override
            public void onPullUpToRefresh(PullToRefreshBase<ListView> refreshView) {
                if (isLastPage) {
                    ToastUtils.showTextToast(getContext(), getResources().getString(R.string.this_is_last_page));
                    bikeSecurity_lv.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            bikeSecurity_lv.onRefreshComplete();
                        }
                    }, 1000);
                    return;
                }
                setUpdateTime(refreshView);
                dataFleshType = 2;
                pageNum++;

                bikeSecurity_lv.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        bikeSecurity_lv.onRefreshComplete();
                    }
                }, 1000);

                //获取listView列表数据
                doRefreshSocket();

            }
        });
        initListViewTipText();
    }

    /**
     * 初始化列表刷新时的提示文本
     */
    private void initListViewTipText() {
        // 设置上拉刷新文本
        ILoadingLayout startLabels = bikeSecurity_lv.getLoadingLayoutProxy(true,
                false);
        startLabels.setPullLabel(getResources().getString(R.string.pull_down_refresh));
        startLabels.setReleaseLabel(getResources().getString(R.string.release_refresh));
        startLabels.setRefreshingLabel(getResources().getString(R.string.refreshing));

        // 设置下拉刷新文本
        ILoadingLayout endLabels = bikeSecurity_lv.getLoadingLayoutProxy(false, true);
        if (isLastPage) {
            endLabels.setPullLabel(getResources().getString(R.string.pull_up_load_more));
            endLabels.setReleaseLabel(getResources().getString(R.string.release_load_more));
            endLabels.setRefreshingLabel(getResources().getString(R.string.last_page));
        } else {
            endLabels.setPullLabel(getResources().getString(R.string.pull_up_load_more));
            endLabels.setReleaseLabel(getResources().getString(R.string.release_load_more));
            endLabels.setRefreshingLabel(getResources().getString(R.string.loading_more));
        }

    }


    /**
     * 设置更新时间
     *
     * @param refreshView
     */
    private void setUpdateTime(PullToRefreshBase refreshView) {
        String label = getStringDateNow();
        refreshView.getLoadingLayoutProxy().setLastUpdatedLabel(label);
    }


    /**
     * 获取现在时间
     *
     * @return返回字符串格式 yyyy-MM-dd HH:mm:ss
     */
    public static String getStringDateNow() {
        Date currentTime = new Date();
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String dateString = formatter.format(currentTime);
        return dateString;
    }

    @Override
    public void onEventMainThread(EventBusMsgMessage eventBusMsg) {
        super.onEventMainThread(eventBusMsg);
        if (eventBusMsg.getMsgType() == EventBusMsgMessage.MSG_Message_Security_Delete) {

            deleteType = EventBusMsgMessage.MSG_Message_Security_Delete;
            deleteSecurityMessage();
        }

    }


    private void deleteSecurityMessage() {

        dlgWaiting.show();
        mDlgWaitingHandler.sendEmptyMessageDelayed(MSG_cannt_get_data, App.WAITTING_SECOND);
        final GetInsuranceMessageRequest.GetInsuranceMessageRequestMessage.Builder insuranceMessage = GetInsuranceMessageRequest.GetInsuranceMessageRequestMessage.newBuilder();


        //删除某条选中的数据
        if (deleteType == 0) {
            insuranceMessage.setIds(String.valueOf(securityMessageList.get(mPosition).getMsgId()));
        }
        //删除整个fragment中的listView列表数据
        else if (deleteType == EventBusMsgMessage.MSG_Message_Security_Delete) {
            StringBuilder stringBuilder = new StringBuilder();
            for (int i = 0; i < securityMessageList.size(); i++) {
                if (i != 0) {
                    stringBuilder.append(",");
                }
                stringBuilder.append(securityMessageList.get(i).getMsgId());

            }
            insuranceMessage.setIds(stringBuilder.toString());
            Log.d("print", "deleteSafetyMessage: stringBuilder.toString(): " + stringBuilder.toString());
        }

        insuranceMessage.setSession(PreferenceData.loadSession(getContext()));

        new Thread() {
            public void run() {
                SocketClient client;
                client = SocketClient.getInstance();
                client.send(SocketAppPacket.COMMANT_ID_DEL_SECURITY_MESSAGE_BY_IDS_APP, insuranceMessage.build().toByteArray());
            }

        }.start();

    }


    @Override
    protected void doSocket() {
//        super.doSocket();
        handler.sendEmptyMessageDelayed(MSG_get_data_failed, App.WAITTING_SECOND);
        final GetSecurityMessageRequest.GetSecurityMessageRequestMessage.Builder securityMessage = GetSecurityMessageRequest.GetSecurityMessageRequestMessage.newBuilder();
        securityMessage.setPageNo(pageNum);
        securityMessage.setPageSize(pageSize);
        securityMessage.setSession(PreferenceData.loadSession(getContext()));

        new Thread() {
            public void run() {
                SocketClient client;
                client = SocketClient.getInstance();
                client.send(COMMANT_ID_GET_SECURITY_MESSAGE_APP, securityMessage.build().toByteArray());
            }
        }.start();
    }


    protected void doRefreshSocket() {
        handler.sendEmptyMessageDelayed(MSG_get_data_failed, App.WAITTING_SECOND);
        final GetSecurityMessageRequest.GetSecurityMessageRequestMessage.Builder securityMessage = GetSecurityMessageRequest.GetSecurityMessageRequestMessage.newBuilder();
        securityMessage.setPageNo(pageNum);
        securityMessage.setPageSize(pageSize);
        securityMessage.setSession(PreferenceData.loadSession(getContext()));

        new Thread() {
            public void run() {
                SocketClient client;
                client = SocketClient.getInstance();
                client.send(COMMANT_ID_GET_SECURITY_MESSAGE_APP, securityMessage.build().toByteArray());
            }
        }.start();
    }


    @Override
    public void onEventMainThread(SocketAppPacket eventPackage) {
        try {
            super.onEventMainThread(eventPackage);
            switch (eventPackage.getCommandId()) {
                case SocketAppPacket.COMMANT_ID_GET_SECURITY_MESSAGE_APP:
                    GetSecurityMessageResponse.GetSecurityMessageResponseMessage getSecurityMessage = GetSecurityMessageResponse.GetSecurityMessageResponseMessage.parseFrom(eventPackage.getCommandData());


                    if (getSecurityMessage.getErrorMsg().getErrorCode() != 0) {
                        Toast.makeText(getActivity(), getSecurityMessage.getErrorMsg().getErrorMsg(), Toast.LENGTH_LONG).show();
                        if (getSecurityMessage.getErrorMsg().getErrorCode() == 20003) {
                            Intent intent = new Intent();
                            intent.setClass(getActivity(), LoginActivity.class);
                            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK|Intent.FLAG_ACTIVITY_CLEAR_TASK);
                            startActivity(intent);
                        }
                    } else {
                        if (dataFleshType == 0) {
//                            if (dlgWaiting.isShowing()) {
//                                dlgWaiting.dismiss();
//                            }
//                            mDlgWaitingHandler.removeMessages(MSG_cannt_get_data);
                            handler.removeMessages(MSG_get_data_failed);
                        } else {
                            bikeSecurity_lv.postDelayed(new Runnable() {

                                @Override
                                public void run() {
                                    bikeSecurity_lv.onRefreshComplete();
                                }
                            }, 1000);
                            handler.removeMessages(MSG_get_data_failed);
                        }

                        if (dataFleshType == 0 || dataFleshType == 1) { //初始加载或下拉刷新
                            if (securityMessageList.size() > 0) {
                                securityMessageList.clear();
                            }
                            for(int i=0;i<getSecurityMessage.getSecurityMessageList().size();i++)
                            {
                                EventBusMsgPush msg=new EventBusMsgPush();
                                msg.setMsgTitle(getSecurityMessage.getSecurityMessageList().get(i).getMessageSubject());
                                msg.setMsgContent(getSecurityMessage.getSecurityMessageList().get(i).getMessageDescription());
                                msg.setMsgId(getSecurityMessage.getSecurityMessageList().get(i).getId());
                                msg.setMsgType(getSecurityMessage.getSecurityMessageList().get(i).getMessageType());
                                msg.setSubMsgType(getSecurityMessage.getSecurityMessageList().get(i).getMessageSubType());
                                msg.setCreateDate(getSecurityMessage.getSecurityMessageList().get(i).getCreateDate());
                                msg.setReferenceId(String.valueOf(getSecurityMessage.getSecurityMessageList().get(i).getReferenceId()));
                                securityMessageList.add(msg);
                            }


                            if (getSecurityMessage.getIsLastPage().equals("1")) {
                                isLastPage = true;
                            }

                            adapter.notifyDataSetChanged();
                        } else if (dataFleshType == 2) { //上拉加载
                            if (getSecurityMessage.getIsLastPage().equals("1")) {
                                isLastPage = true;
                            }
                            for(int i=0;i<getSecurityMessage.getSecurityMessageList().size();i++)
                            {
                                EventBusMsgPush msg=new EventBusMsgPush();
                                msg.setMsgTitle(getSecurityMessage.getSecurityMessageList().get(i).getMessageSubject());
                                msg.setMsgContent(getSecurityMessage.getSecurityMessageList().get(i).getMessageDescription());
                                msg.setMsgId(getSecurityMessage.getSecurityMessageList().get(i).getId());
                                msg.setMsgType(getSecurityMessage.getSecurityMessageList().get(i).getMessageType());
                                msg.setSubMsgType(getSecurityMessage.getSecurityMessageList().get(i).getMessageSubType());
                                msg.setCreateDate(getSecurityMessage.getSecurityMessageList().get(i).getCreateDate());
                                msg.setReferenceId(String.valueOf(getSecurityMessage.getSecurityMessageList().get(i).getReferenceId()));
                                securityMessageList.add(msg);
                            }
//                            securityMessageList.addAll(getSecurityMessage.getSecurityMessageList());

                            adapter.notifyDataSetChanged();
                        }

                        if (securityMessageList.size() == 0) {
                            ll_bike_safety.setVisibility(View.VISIBLE);
                            bikeSecurity_lv.setVisibility(View.GONE);

                            EventBusMsg msg = new EventBusMsg();
                            msg.setValue(securityMessageList.size());
                            msg.setEmptyType(EventBusMsg.MsgEmptyType.MSG_SAFE_LIST_SIZE_CHANGE);
                            EventBus.getDefault().post(msg);
//                            ToastUtils.showTextToast(getContext(), getResources().getString(R.string.no_need_delete));
                            return;

                        }

                    }
                    break;


                case SocketAppPacket.COMMANT_ID_DEL_SECURITY_MESSAGE_BY_IDS_APP:
                    CommonResponse.CommonResponseMessage commonResponseMessage = CommonResponse.CommonResponseMessage.parseFrom(eventPackage.getCommandData());
                    if (dlgWaiting.isShowing()) {
                        dlgWaiting.dismiss();
                    }
                    mDlgWaitingHandler.removeMessages(MSG_cannt_get_data);
                    if (commonResponseMessage.getErrorMsg().getErrorCode() != 0) {
                        Toast.makeText(getActivity(), commonResponseMessage.getErrorMsg().getErrorMsg(), Toast.LENGTH_LONG).show();
                        if (commonResponseMessage.getErrorMsg().getErrorCode() == 20003) {
                            Intent intent = new Intent();
                            intent.setClass(getActivity(), LoginActivity.class);
                            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK|Intent.FLAG_ACTIVITY_CLEAR_TASK);
                            startActivity(intent);
                        }
                    } else {
                        doSocket();
                        ToastUtils.showTextToast(getContext(), getResources().getString(R.string.delete_success));
                    }
                    break;

            }

        } catch (InvalidProtocolBufferException e) {
            e.printStackTrace();
        }


    }


    @Override
    public void onClick(View v) {

    }
    public void onEventMainThread(final EventBusMsgPush eventPackage)
    {
        if(isActivityTop()) {

            if(eventPackage.getMsgType()==2)
            {
                securityMessageList.add(0,eventPackage);
                adapter.notifyDataSetChanged();

                    ll_bike_safety.setVisibility(View.GONE);
                    bikeSecurity_lv.setVisibility(View.VISIBLE);

                    EventBusMsg msg = new EventBusMsg();
                    msg.setValue(securityMessageList.size());
                    msg.setEmptyType(EventBusMsg.MsgEmptyType.MSG_SAFE_LIST_SIZE_CHANGE);
                    EventBus.getDefault().post(msg);
                    return;
            }
        }
    }
    protected boolean isActivityTop(){
        ActivityManager manager = (ActivityManager) this.getActivity().getSystemService(Context.ACTIVITY_SERVICE);
        String name = manager.getRunningTasks(1).get(0).topActivity.getClassName();
        return name.equals(this.getActivity().getClass().getName());
    }
}
