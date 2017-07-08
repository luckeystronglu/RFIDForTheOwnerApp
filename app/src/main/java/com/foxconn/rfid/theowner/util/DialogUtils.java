package com.foxconn.rfid.theowner.util;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.foxconn.rfid.theowner.R;

import java.util.HashMap;



@SuppressLint("UseSparseArrays")
public class DialogUtils extends Dialog {

	public static final int EDIT = 0XCCAA0001;
	public static final int NOTIFY = 0XCCAA0002;
	public static final int ERROR = 0XCCAA0003;
	public static final int REFRESH = 0XCCAA0004;
	public static final int RESTART = 0XCCAA0006;
	public static final int NOTIFY_2BUTTON = 0XCCAA0005;
	public static final int LOGIN = 0XCCAA0007;
	public static final int VERSION = 0XCCAA0008;

	private Context mContext;
	private int dlgType;
	private HashMap<Integer, Integer> layoutMap = new HashMap<Integer, Integer>();
	private String titleStr;
	private String msgStr;
	private String confirmBtnStr;
	private String cancelBtnStr;
	private OnClickListener clickListener;

	/**
	 * @param activity
	 *            : the dialog depend on the activity which to start.
	 * @param titleId
	 *            :title String id , can be 0;
	 * @param msgId
	 *            : message String id, can be 0;
	 * @param dlgType
	 *            : this par. must be one of flow: CONFIRM_EDIT_DLG =
	 *            0XCCAA0001; NOTIFY_DLG = 0XCCAA0002; NOTIFY_ERROR_DLG =
	 *            0XCCAA0003;
	 */
	private DialogUtils(Context context, int dlgType) {
		super(context, R.style.dlg_refresh_style);
		this.dlgType = dlgType;
		this.mContext = context;
		initLayoutMap();
	}

	public static DialogUtils createDialog(Context context, int dlgType) {
		return new DialogUtils(context, dlgType);
	}

	public void setClickListener(OnClickListener clickListener) {
		this.clickListener = clickListener;
	}

	/**
	 * Description: .
	 */
	private void initViews() {
		setContentView(layoutMap.get(this.dlgType));
		addListenerToViews();
		switch (dlgType) {
		case NOTIFY:
			if (titleStr != null) {
				((TextView) findViewById(R.id.dlg_tv_title)).setText(titleStr);
			} else {
				((TextView) findViewById(R.id.dlg_tv_title))
						.setText(R.string.dlg_btn_confirm);
			}
			if (msgStr != null) {
				((TextView) findViewById(R.id.dlg_tv_msg)).setText(msgStr);
			}
			break;
		case REFRESH:
			if (msgStr != null) {
				((TextView) findViewById(R.id.dlg_refresh_tv_msg))
						.setText(msgStr);
			} else {
				((TextView) findViewById(R.id.dlg_refresh_tv_msg))
						.setText(R.string.dlg_msg_refresh);
			}
			break;

		case RESTART:
			if (msgStr != null) {
				((TextView) findViewById(R.id.dlg_refresh_tv_msg))
						.setText("正在重启,请稍后");
			} else {
				((TextView) findViewById(R.id.dlg_refresh_tv_msg))
						.setText("正在重启,请稍后");
			}
			break;
		case LOGIN:
			if (msgStr != null) {
				((TextView) findViewById(R.id.dlg_refresh_tv_msg))
						.setText("正在登录，请稍后");
			} else {
				((TextView) findViewById(R.id.dlg_refresh_tv_msg))
						.setText("正在登录，请稍后");
			}
			break;
		case VERSION:
			if (msgStr != null) {
				((TextView) findViewById(R.id.dlg_refresh_tv_msg))
						.setText("获取版本中,请稍后");
			} else {
				((TextView) findViewById(R.id.dlg_refresh_tv_msg))
						.setText("获取版本中,请稍后");
			}
			break;
		default:
			break;
		}
	}

	/**
	 * (non-Javadoc).
	 * 
	 * @see android.app.AlertDialog#onCreate(Bundle)
	 * @param savedInstanceState
	 */
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(layoutMap.get(this.dlgType));
		initViews();
	}

	/*
	 * public abstract void excuteConfirmBtnClick();
	 * 
	 * public abstract void excuteCancelBtnClick();
	 */

	/**
	 * Description: Return the edit text content.If the dialog does not contain
	 * the edit , it'll return the null value.
	 * 
	 * @return
	 */
	public String getEditText() {
		if (this.dlgType != EDIT) {
			return null;
		}
		EditText editText = new EditText(mContext);// (EditText)
													// findViewById(R.id.iinc_sign_dialog_et_message);
		return editText.getText().toString();
	}

	/**
	 * Description: set the given resource component listener.
	 */
	private void addListenerToViews() {
		Log.i("DialogUtils", "addListenerToView");
		Button confirmBtn = (Button) findViewById(R.id.dlg_btn_confirm);
		Button cancelBtn = (Button) findViewById(R.id.dlg_btn_cancel);
		if (confirmBtn != null) {
			if (cancelBtnStr != null) {
				cancelBtn.setText(cancelBtnStr);
			}
			confirmBtn.setOnClickListener(new View.OnClickListener() {
				@Override
				public void onClick(View v) {
					if (clickListener != null) {
						clickListener.onConfirm();
					}
					if (dlgType != DialogUtils.EDIT) {
						dismiss();
					}
				}
			});
		}
		if (cancelBtn != null) {
			if (confirmBtnStr != null) {
				confirmBtn.setText(confirmBtnStr);
			}
			cancelBtn.setOnClickListener(new View.OnClickListener() {
				@Override
				public void onClick(View v) {
					dismiss();
				}
			});
		}
	}

	public void setConfirmBtnText(String text) {
		this.confirmBtnStr = text;
	}

	public void setCancelBtnText(String text) {
		this.cancelBtnStr = text;
	}

	public void setTitle(CharSequence title) {
		this.titleStr = title.toString();
	}

	public void setTitle(int titleId) {
		this.titleStr = mContext.getResources().getString(titleId);
	}

	public void setMessage(int messageId) {
		this.msgStr = mContext.getResources().getString(messageId);
	}

	public void setMessage(String message) {
		this.msgStr = message;
	}

	/**
	 * Description: .
	 */
	private void initLayoutMap() {
		layoutMap.put(NOTIFY, R.layout.dlg_notify);// 系統提示，只有確認
		layoutMap.put(ERROR, R.layout.dlg_notify);// 錯誤提示，只有確認
		layoutMap.put(EDIT, R.layout.dlg_notify);// 系統提示，内容可编辑，確認、取消
		layoutMap.put(REFRESH, R.layout.dlg_refresh);// 刷新
		layoutMap.put(NOTIFY_2BUTTON, R.layout.dlg_notify);// 退出提示，確認、取消
		layoutMap.put(RESTART, R.layout.dlg_refresh);
	}

	public static abstract class OnClickListener {
		public abstract void onConfirm();

		public void onCancel() {
		};

		public void onThird() {
		};
	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		// TODO Auto-generated method stub
		// if(keyCode == KeyEvent.KEYCODE_BACK && dlgType ==
		// DialogUtils.REFRESH){
		// return true;
		// }
		return super.onKeyDown(keyCode, event);
		// return false;
	}
}
