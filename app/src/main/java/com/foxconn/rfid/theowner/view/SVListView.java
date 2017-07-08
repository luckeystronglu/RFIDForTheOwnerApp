/**
 * 
 */
package com.foxconn.rfid.theowner.view;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.ListView;

/**
 * 嵌套进ScrollView 的ListView
 * 
 * @author WT00111
 * 
 */
public class SVListView extends ListView {
	public SVListView(Context context) {
		super(context);
	}

	public SVListView(Context context, AttributeSet attrs) {
		super(context, attrs);
	}

	public SVListView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
	}

	@Override
	protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
		int expandSpec = MeasureSpec.makeMeasureSpec(Integer.MAX_VALUE >> 2, MeasureSpec.AT_MOST);
		super.onMeasure(widthMeasureSpec, expandSpec);
	}

}
