package com.wwly.vblog.ui.widget;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.wwly.vblog.R;

public class EmptyView extends RelativeLayout {
	
	private TextView mEmptyTextView;

	public EmptyView(Context context) {
		super(context);
		setupViews();
	}

	public EmptyView(Context context, AttributeSet attrs) {
		super(context, attrs);
		setupViews();
	}

	public EmptyView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		setupViews();
	}

	private void setupViews() {
		LayoutInflater inflater = (LayoutInflater) getContext()
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		ViewGroup viewGroup = (ViewGroup) inflater.inflate(R.layout.widget_empty_view,
				this, true);
		mEmptyTextView = (TextView) viewGroup.findViewById(R.id.empty_text);
		mEmptyTextView.setText(R.string.view_empty_text);
	}

	public void show() {
		setVisibility(View.VISIBLE);
	}

	public void hide() {
		setVisibility(View.GONE);
	}

}