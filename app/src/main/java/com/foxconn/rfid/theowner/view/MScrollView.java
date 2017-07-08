package com.foxconn.rfid.theowner.view;

import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.widget.ScrollView;

public class MScrollView extends ScrollView{
	
	public MScrollView(Context context, AttributeSet attrs, int defStyleAttr) {
		super(context, attrs, defStyleAttr);
	}
	public MScrollView(Context context, AttributeSet attrs) {
		super(context, attrs);
	}
	public MScrollView(Context context) {
		super(context);
	}
	
	@Override
	public boolean onInterceptTouchEvent(MotionEvent ev) {
		return super.onInterceptTouchEvent(ev);
	}
	
	

}
