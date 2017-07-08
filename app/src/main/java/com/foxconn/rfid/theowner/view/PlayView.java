package com.foxconn.rfid.theowner.view;

import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.foxconn.rfid.theowner.R;

/**
 * Created by Administrator on 2016/12/17.
 */

public class PlayView extends LinearLayout {
    private OnPlaySpeedChangeListener speedChangeListener;
    private OnPlayToggleChangeListener toggleChangeListener;
    private LinearLayout mllS1;
    private LinearLayout mllS2;
    private LinearLayout mllS3;
    private LinearLayout mllS4;
    private ImageView mIvS1;
    private ImageView mIvS2;
    private ImageView mIvS3;
    private ImageView mIvS4;
    private TextView mTvS1;
    private TextView mTvS2;
    private TextView mTvS3;
    private TextView mTvS4;
    private ProgressBar mPbPlay;
    private Button mBtnPlayToggle;
    private boolean mIsPlay = false;
    private static final int PLAY = 10000;
    private static final int PAUSE = 10001;
    private long mIntervalTime = 5000;

    private int mIndex = 0;
    private int mSendIndex = 0;
    private int mMax;
    private boolean mFirst = true;

    Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case PLAY:

                    toggleChangeListener.toggleOn(mIndex, mFirst);
                    mIndex++;
                    mPbPlay.setProgress(mIndex);
                    if (mIndex == mMax) {
                        mFirst = false;
                        mIndex = 0;
                        mBtnPlayToggle.setBackgroundResource(R.drawable.ico_play_start);
//                        mPbPlay.setProgress(mIndex);

                        mIsPlay = false;

                    }
                    break;
                case PAUSE:
                    mBtnPlayToggle.setBackgroundResource(R.drawable.ico_play_start);
                    mIsPlay = false;

                    toggleChangeListener.toggleOff();
                    break;
            }
        }
    };

    public PlayView(Context context, AttributeSet attrs) {
        super(context, attrs);
        View view = LayoutInflater.from(context).inflate(R.layout.view_play_speed, this);
        mllS1 = (LinearLayout) view.findViewById(R.id.ll_s1);
        mllS2 = (LinearLayout) view.findViewById(R.id.ll_s2);
        mllS3 = (LinearLayout) view.findViewById(R.id.ll_s3);
        mllS4 = (LinearLayout) view.findViewById(R.id.ll_s4);
        mIvS1 = (ImageView) view.findViewById(R.id.iv_s1);
        mIvS2 = (ImageView) view.findViewById(R.id.iv_s2);
        mIvS3 = (ImageView) view.findViewById(R.id.iv_s3);
        mIvS4 = (ImageView) view.findViewById(R.id.iv_s4);
        mTvS1 = (TextView) view.findViewById(R.id.tv_s1);
        mTvS2 = (TextView) view.findViewById(R.id.tv_s2);
        mTvS3 = (TextView) view.findViewById(R.id.tv_s3);
        mTvS4 = (TextView) view.findViewById(R.id.tv_s4);

        mPbPlay = (ProgressBar) view.findViewById(R.id.pb_play);
        mBtnPlayToggle = (Button) view.findViewById(R.id.btn_playToggle);


    }

    Runnable runTask = new Runnable() {
        @Override
        public void run() {
            while (true) {

                if (!mIsPlay) {
                    break;
                }
                if (mSendIndex == mMax) {
                    mHandler.sendEmptyMessage(PAUSE);
                    break;
                }
                if (mSendIndex == mMax - 1) {
                    mSendIndex = 0;
                }


                Message message = Message.obtain();
                message.what = PLAY;
                mHandler.sendMessage(message);
                mSendIndex++;

                try {
                    Thread.sleep(mIntervalTime);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }

        }
    };

    public void setOnPlaySpeedChangeListener(final boolean change, OnPlaySpeedChangeListener
            listener) {
        speedChangeListener = listener;

        mllS1.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                if (!change) {
                    speedChangeListener.onNotChange();
                    return;
                }
                mTvS1.setTextColor(getResources().getColor(R.color.actionsheet_blue));
                mTvS2.setTextColor(getResources().getColor(R.color.textGray));
                mTvS3.setTextColor(getResources().getColor(R.color.textGray));
                mTvS4.setTextColor(getResources().getColor(R.color.textGray));
                mIvS1.setImageResource(R.drawable.ico_speed_checked);
                mIvS2.setImageResource(R.drawable.ico_speed_uncheced);
                mIvS3.setImageResource(R.drawable.ico_speed_uncheced);
                mIvS4.setImageResource(R.drawable.ico_speed_uncheced);
                mIntervalTime = 1000;
            }
        });
        mllS2.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!change) {
                    speedChangeListener.onNotChange();
                    return;
                }
                mTvS1.setTextColor(getResources().getColor(R.color.textGray));
                mTvS2.setTextColor(getResources().getColor(R.color.actionsheet_blue));
                mTvS3.setTextColor(getResources().getColor(R.color.textGray));
                mTvS4.setTextColor(getResources().getColor(R.color.textGray));
                mIvS1.setImageResource(R.drawable.ico_speed_uncheced);
                mIvS2.setImageResource(R.drawable.ico_speed_checked);
                mIvS3.setImageResource(R.drawable.ico_speed_uncheced);
                mIvS4.setImageResource(R.drawable.ico_speed_uncheced);
                mIntervalTime = 1000 / 2;
            }
        });
        mllS3.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!change) {
                    speedChangeListener.onNotChange();
                    return;
                }
                mTvS1.setTextColor(getResources().getColor(R.color.textGray));
                mTvS2.setTextColor(getResources().getColor(R.color.textGray));
                mTvS3.setTextColor(getResources().getColor(R.color.actionsheet_blue));
                mTvS4.setTextColor(getResources().getColor(R.color.textGray));
                mIvS1.setImageResource(R.drawable.ico_speed_uncheced);
                mIvS2.setImageResource(R.drawable.ico_speed_uncheced);
                mIvS3.setImageResource(R.drawable.ico_speed_checked);
                mIvS4.setImageResource(R.drawable.ico_speed_uncheced);
                mIntervalTime = 1000 / 3;
            }
        });
        mllS4.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!change) {
                    speedChangeListener.onNotChange();
                    return;
                }
                mTvS1.setTextColor(getResources().getColor(R.color.textGray));
                mTvS2.setTextColor(getResources().getColor(R.color.textGray));
                mTvS3.setTextColor(getResources().getColor(R.color.textGray));
                mTvS4.setTextColor(getResources().getColor(R.color.actionsheet_blue));
                mIvS1.setImageResource(R.drawable.ico_speed_uncheced);
                mIvS2.setImageResource(R.drawable.ico_speed_uncheced);
                mIvS3.setImageResource(R.drawable.ico_speed_uncheced);
                mIvS4.setImageResource(R.drawable.ico_speed_checked);
                mIntervalTime = 1000 / 4;
            }
        });
    }

    public void setToggleChangeListener(final int max, final boolean play,
                                        OnPlayToggleChangeListener
                                                listener) {
        toggleChangeListener = listener;
        mBtnPlayToggle.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!play) {
                    toggleChangeListener.notToggle();
                    return;
                }
                mMax = max;
                mPbPlay.setMax(mMax);
                mIsPlay = !mIsPlay;
                if (mIsPlay) {
                    mBtnPlayToggle.setBackgroundResource(R.drawable.ioc_pause);

                } else {
                    mBtnPlayToggle.setBackgroundResource(R.drawable.ico_play_start);

                }
                if (mIsPlay) {
                    new Thread(runTask).start();

                }

            }
        });

    }


    public interface OnPlaySpeedChangeListener {
        void onNotChange();


    }

    public interface OnPlayToggleChangeListener {

        void notToggle();

        void toggleOn(int position, boolean first);

        void toggleOff();

    }


    public void restore() {
        mIndex = 0;
        mPbPlay.setProgress(0);
        mIntervalTime = 1000;
        mBtnPlayToggle.setBackgroundResource(R.drawable.ico_play_start);
        mIsPlay = false;
        mTvS1.setTextColor(getResources().getColor(R.color.actionsheet_blue));
        mTvS2.setTextColor(getResources().getColor(R.color.textGray));
        mTvS3.setTextColor(getResources().getColor(R.color.textGray));
        mTvS4.setTextColor(getResources().getColor(R.color.textGray));
        mIvS1.setImageResource(R.drawable.ico_speed_checked);
        mIvS2.setImageResource(R.drawable.ico_speed_uncheced);
        mIvS3.setImageResource(R.drawable.ico_speed_uncheced);
        mIvS4.setImageResource(R.drawable.ico_speed_uncheced);
    }
}
