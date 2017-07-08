package com.foxconn.rfid.theowner.view;

import android.content.Context;
import android.content.res.TypedArray;
import android.support.v4.content.ContextCompat;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.foxconn.rfid.theowner.R;

import static com.foxconn.rfid.theowner.R.id.date;

/**
 * Created by Administrator on 2016/12/20.
 */

public class LossBikeDateView extends RelativeLayout {
    private boolean display;


    private DateListener listener;
    private final TextView tvDate;
    private ImageView mToggle;
    private boolean mClickable;

    public LossBikeDateView(Context context, AttributeSet attrs) {
        super(context, attrs);
        TypedArray array = context.obtainStyledAttributes(attrs, R.styleable.LossBikeDateView);
        mClickable = array.getBoolean(R.styleable.LossBikeDateView_viewClickable, true);
        View view = LayoutInflater.from(context).inflate(R.layout.view_loss_bike_date, this);
        tvDate = (TextView) view.findViewById(date);
        mToggle = (ImageView) view.findViewById(R.id.toggle);
        if (mClickable) {
            view.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
                    display = !display;
                    if (display) {
                        mToggle.setImageResource(R.drawable.ico_up);
                        listener.display();
                    } else {
                        mToggle.setImageResource(R.drawable.ico_down);
                        listener.miss();
                    }
                }
            });
        }


    }

    public void setDisplay(boolean isDisplay) {
        display = isDisplay;
        if (display) {
            mToggle.setImageResource(R.drawable.ico_up);
            listener.display();
        } else {
            mToggle.setImageResource(R.drawable.ico_down);
            listener.miss();
        }
    }

    public void setListener(DateListener listener) {
        this.listener = listener;
    }

    public void setDate(String date) {
        tvDate.setText(date);
        tvDate.setTextColor(ContextCompat.getColor(getContext(),R.color.actionsheet_gray));
    }

    public String getDate() {
        return tvDate.getText().toString();
    }

    public interface DateListener {
        void display();

        void miss();
    }

    public void setHint(String hint) {
        tvDate.setHint(hint);
    }
}
