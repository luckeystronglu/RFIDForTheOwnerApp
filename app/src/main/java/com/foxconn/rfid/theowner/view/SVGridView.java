/**
 * 
 */
package com.foxconn.rfid.theowner.view;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.GridView;

/**
 * 嵌套进ScrollView 的ListView
 * 
 * @author WT00111
 * 
 */
public class SVGridView extends GridView {
	public SVGridView(Context context) {
		super(context);
	}

	public SVGridView(Context context, AttributeSet attrs) {
		super(context, attrs);
	}

	public SVGridView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
	}

	@Override
	protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
		int expandSpec = MeasureSpec.makeMeasureSpec(Integer.MAX_VALUE >> 2, MeasureSpec.AT_MOST);
		super.onMeasure(widthMeasureSpec, expandSpec);
	}

}
