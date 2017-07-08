package com.foxconn.rfid.theowner.view.widgets;

import android.content.Context;
import android.content.res.TypedArray;
import android.text.Editable;
import android.text.InputFilter;
import android.text.InputType;
import android.text.TextWatcher;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.foxconn.rfid.theowner.R;

import butterknife.ButterKnife;


/**
 * Created by Administrator on 2017/1/11.
 */

public class InputView extends LinearLayout {
//    @BindView(R.id.tv_title)
//    TextView mTvTitle;
//    @BindView(R.id.iv_mark)
//    ImageView mIvMark;
//    @BindView(R.id.et_content)
//    EditText mEtContent;
    private TextView mTvTitle;
    private ImageView mIvMark;
    private EditText mEtContent;


    private onInputFocusChangeListener mFocusChangeListener;
    private onInputContentChangeListener mContentChangeListener;

    public InputView(Context context, AttributeSet attrs) {
        super(context, attrs);
        View view = LayoutInflater.from(context).inflate(R.layout.view_input, this);
        ButterKnife.bind(view);
        mTvTitle = (TextView) findViewById(R.id.tv_title);
        mIvMark = (ImageView) findViewById(R.id.iv_mark);
        mEtContent = (EditText) findViewById(R.id.et_content);

        TypedArray array = context.obtainStyledAttributes(attrs, R.styleable.InputView);
        String title = array.getString(R.styleable.InputView_InputViewTitle);
        boolean editable = array.getBoolean(R.styleable.InputView_InputViewEditable, true);
        String content = array.getString(R.styleable.InputView_InputViewContent);
        String hint = array.getString(R.styleable.InputView_InputViewHint);
        int maxLength = array.getInt(R.styleable.InputView_InputViewMaxLength, Integer.MAX_VALUE);
        int markVisible = array.getInt(R.styleable.InputView_InputViewMarkVisible, 0);
        int inPutType = array.getInt(R.styleable.InputView_InputViewInputType, -1);
        array.recycle();
        mEtContent.setEnabled(editable);
        mTvTitle.setText(title);
        mEtContent.setText(content);
        mEtContent.setHint(hint);
        InputFilter[] filters = {new InputFilter.LengthFilter(maxLength)};
        mEtContent.setFilters(filters);
        switch (markVisible) {
            case 0:
                mIvMark.setVisibility(VISIBLE);
                break;

            case 1:
                mIvMark.setVisibility(INVISIBLE);

                break;
            case 2:
                mIvMark.setVisibility(GONE);

                break;
        }
        switch (inPutType) {
            case 0:
                mEtContent.setInputType(InputType.TYPE_CLASS_TEXT | InputType
                        .TYPE_TEXT_VARIATION_PASSWORD);
                break;

            case 1:
                mEtContent.setInputType(InputType.TYPE_CLASS_NUMBER | InputType
                        .TYPE_NUMBER_FLAG_DECIMAL);

                break;
            case 2:
                mEtContent.setInputType(InputType.TYPE_CLASS_NUMBER);

                break;
        }

        mEtContent.setOnFocusChangeListener(new OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean
                    hasFocus) {
                if (mFocusChangeListener != null) {
                    mFocusChangeListener.FocusChange(InputView.this, hasFocus);
                }
            }
        });
        mEtContent.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if (mContentChangeListener != null) {
                    mContentChangeListener.ContentChange(InputView.this, s.toString());
                }
            }
        });
    }

    public String getContent() {
        return mEtContent.getText().toString();
    }

    public void setContent(String content) {
        mEtContent.setText(content);
    }

    public void setInputEnable(boolean enable) {
        mEtContent.setEnabled(enable);
    }

    public void setOnInputFocusChangeListener(onInputFocusChangeListener listener) {
        mFocusChangeListener = listener;
    }

    public void setonInputContentChangeListener(onInputContentChangeListener listener) {
        mContentChangeListener = listener;
    }


    public interface onInputFocusChangeListener {
        void FocusChange(InputView InputView, boolean hasFocus);
    }

    public interface onInputContentChangeListener {
        void ContentChange(InputView inputView, String content);
    }

    /**
     * 光标尾部
     */
    public void setSelectionEnd() {
        mEtContent.setSelection(mEtContent.getText().length());
    }

    public void setContentErr(String err) {
        mEtContent.setError(err);
    }
}
