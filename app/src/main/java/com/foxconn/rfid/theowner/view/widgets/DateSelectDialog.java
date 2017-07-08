package com.foxconn.rfid.theowner.view.widgets;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.NumberPicker;

import com.foxconn.rfid.theowner.R;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 * Created by Administrator on 2016/12/24.
 */

public class DateSelectDialog extends Dialog implements View.OnClickListener {
    private DateSelectListener mListener;
    private NumberPicker mNpYear;
    private NumberPicker mNpMonth;
    private NumberPicker mNpDay;
    private Button mBtnYes;
    private Button mBtnNo;
    private String[] mDays;
    private int mCurrentYear;
    private int mCurrentMonth;
    private int mCurrentDay;
    private long mCurrentTime;
    private String[] days28;
    private String[] days29;
    private String[] days30;
    private String[] days31;

    public DateSelectDialog(Context context) {
        super(context, R.style.BaseDialog);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dialog_date_selecter);

        mBtnYes = (Button) findViewById(R.id.btn_yes);
        mBtnNo = (Button) findViewById(R.id.btn_no);
        mNpYear = (NumberPicker) findViewById(R.id.np_year);
        mNpMonth = (NumberPicker) findViewById(R.id.np_month);
        mNpDay = (NumberPicker) findViewById(R.id.np_day);

        mBtnYes.setOnClickListener(this);
        mBtnNo.setOnClickListener(this);
        Calendar calendar = Calendar.getInstance();
        mCurrentYear = calendar.get(Calendar.YEAR);
        mCurrentMonth = calendar.get(Calendar.MONTH);
        mCurrentDay = calendar.get(Calendar.DAY_OF_MONTH);
        String[] years = {mCurrentYear - 1 + "年", mCurrentYear + "年", mCurrentYear + 1 + "年"};
        mNpYear.setDisplayedValues(years);
        mNpYear.setMinValue(mCurrentYear - 1);
        mNpYear.setMaxValue(mCurrentYear + 1);
        mNpYear.setValue(mCurrentYear);
        String[] months = {"1月", "2月", "3月", "4月", "5月", "6月", "7月", "8月", "9月", "10月", "11月",
                "12月"};
        mNpMonth.setDisplayedValues(months);
        mNpMonth.setMaxValue(11);
        mNpMonth.setMinValue(0);
        mNpMonth.setValue(mCurrentMonth);

        days28 = new String[]{"1日", "2日", "3日", "4日", "5日", "6日", "7日", "8日", "9日", "10日", "11日",
                "12日", "13日", "14日", "15日", "16日", "17日", "18日", "19日", "20日", "21日", "22日",
                "23日", "24日", "25日", "26日", "27日", "28日"};
        days29 = new String[]{"1日", "2日", "3日", "4日", "5日", "6日", "7日", "8日", "9日", "10日", "11日",
                "12日", "13日", "14日", "15日", "16日", "17日", "18日", "19日", "20日", "21日", "22日",
                "23日", "24日", "25日", "26日", "27日", "28日", "29日"};
        days30 = new String[]{"1日", "2日", "3日", "4日", "5日", "6日", "7日", "8日", "9日", "10日", "11日",
                "12日", "13日", "14日", "15日", "16日", "17日", "18日", "19日", "20日", "21日", "22日",
                "23日", "24日", "25日", "26日", "27日", "28日", "29日", "30日"};
        days31 = new String[]{"1日", "2日", "3日", "4日", "5日", "6日", "7日", "8日", "9日", "10日", "11日",
                "12日", "13日", "14日", "15日", "16日", "17日", "18日", "19日", "20日", "21日", "22日",
                "23日", "24日", "25日", "26日", "27日", "28日", "29日", "30日", "31日"};

        setDays(mCurrentYear, mCurrentMonth + 1);
        mNpDay.setDisplayedValues(mDays);
        mNpDay.setMinValue(1);
        mNpDay.setMaxValue(mDays.length);
        mNpDay.setValue(mCurrentDay);
        mNpYear.setOnValueChangedListener(new NumberPicker.OnValueChangeListener() {
            @Override
            public void onValueChange(NumberPicker picker, int oldVal, int newVal) {
                mCurrentYear = newVal;
                setDays(mCurrentYear, mCurrentMonth + 1);
                try {
                    mNpDay.setDisplayedValues(mDays);
                    mNpDay.setMaxValue(mDays.length);
                } catch (Exception e) {
                    e.printStackTrace();
                    mNpDay.setDisplayedValues(mDays);
                    mNpDay.setMaxValue(mDays.length);
                }


            }
        });
        mNpMonth.setOnValueChangedListener(new NumberPicker.OnValueChangeListener() {
            @Override
            public void onValueChange(NumberPicker picker, int oldVal, int newVal) {
                mCurrentMonth = newVal;
                setDays(mCurrentYear, mCurrentMonth + 1);
                try {
                    mNpDay.setDisplayedValues(mDays);
                    mNpDay.setMaxValue(mDays.length);
                } catch (Exception e) {
                    e.printStackTrace();
                    mNpDay.setDisplayedValues(mDays);
                    mNpDay.setMaxValue(mDays.length);
                }


            }
        });
        mNpDay.setOnValueChangedListener(new NumberPicker.OnValueChangeListener() {
            @Override
            public void onValueChange(NumberPicker picker, int oldVal, int newVal) {
                mCurrentDay = newVal;
            }
        });
    }

    private void setDays(int year, int month) {
        int a = getDaysOfMonth(year, month);
        switch (a) {
            case 28:
                mDays = days28;
                break;
            case 29:
                mDays = days29;
                break;
            case 30:
                mDays = days30;
                break;
            case 31:
                mDays = days31;
                break;
        }
    }

    public int getDaysOfMonth(int year, int month) {
        if (month == 2) {
            if (isLeapYear(year)) {
                return 29;
            } else {
                return 28;
            }
        } else if (month == 1
                || month == 3
                || month == 5
                || month == 7
                || month == 8
                || month == 10
                || month == 12) {
            return 31;
        } else {
            return 30;
        }
    }

    private boolean isLeapYear(int year) {
        if (year % 100 == 0) {
            if (year % 400 == 0) {
                return true;
            }
        } else if (year % 4 == 0) {
            return true;
        }
        return false;
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btn_yes:

                Date date = new Date(mCurrentYear - 1900, mCurrentMonth, mCurrentDay);
                mCurrentTime = date.getTime();
                SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy年MM月dd日");
                String time = simpleDateFormat.format(new Date(mCurrentTime));
                mListener.selected(mCurrentTime, time);
                dismiss();
                break;
            case R.id.btn_no:
                mListener.cancel();
                dismiss();
                break;
            default:
                break;
        }
    }

    public void setListener(DateSelectListener listener) {
        mListener = listener;
    }

    public interface DateSelectListener {
        void selected(long time, String date);

        void cancel();

        void dismiss();

    }

    @Override
    public void show() {
        super.show();
        Window Window = getWindow();
        WindowManager.LayoutParams lp = Window.getAttributes();
        lp.width = WindowManager.LayoutParams.MATCH_PARENT;
        lp.height = WindowManager.LayoutParams.WRAP_CONTENT;
        Window.setGravity(Gravity.BOTTOM);
        Window.setAttributes(lp);
    }

    @Override
    public void dismiss() {
        super.dismiss();
        mListener.dismiss();
    }
}
