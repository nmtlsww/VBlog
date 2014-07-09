package com.wwly.vblog.ui.widget;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.wwly.vblog.R;

public class ErrorView extends RelativeLayout {
	
	private TextView mRetryTextView;

	public ErrorView(Context context) {
		super(context);
		setupViews();
	}

	public ErrorView(Context context, AttributeSet attrs) {
		super(context, attrs);
		setupViews();
	}

	public ErrorView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		setupViews();
	}

	private void setupViews() {
		LayoutInflater inflater = (LayoutInflater) getContext()
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		ViewGroup viewGroup = (ViewGroup) inflater.inflate(R.layout.widget_error_view,
				this, true);
		mRetryTextView = (TextView) viewGroup.findViewById(R.id.retry_text);
		mRetryTextView.setClickable(true);
		mRetryTextView.setText(R.string.view_error_text);
	}

	public void show() {
		setVisibility(View.VISIBLE);
	}

	public void hide() {
		setVisibility(View.GONE);
	}

	public void setRetryClickListener(OnClickListener onClickListener) {
		if (mRetryTextView != null) {
			mRetryTextView.setOnClickListener(onClickListener);
		}
	}
}
