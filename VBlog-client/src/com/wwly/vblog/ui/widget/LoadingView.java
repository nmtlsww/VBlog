package com.wwly.vblog.ui.widget;

import android.content.Context;
import android.graphics.drawable.AnimationDrawable;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.wwly.vblog.R;

public class LoadingView extends RelativeLayout
{
	private AnimationDrawable mLoadingAnimation;
	private ImageView mProgressView;
	
    public LoadingView(Context context)
    {
        super(context);
        setupViews();
    }

    public LoadingView(Context context, AttributeSet attrs)
    {
        super(context, attrs);
        setupViews();
    }

    public LoadingView(Context context, AttributeSet attrs, int defStyle)
    {
        super(context, attrs, defStyle);
        setupViews();
    }

    private void setupViews()
    {
        LayoutInflater inflater = (LayoutInflater)getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        ViewGroup viewGroup = (ViewGroup)inflater.inflate(R.layout.widget_loading_view, this, true);
        mProgressView = (ImageView)viewGroup.findViewById(R.id.loading_icon);
    }

    public void show()
    {
        setVisibility(View.VISIBLE);
        mLoadingAnimation = (AnimationDrawable)(AnimationDrawable)mProgressView.getBackground();
        mLoadingAnimation.start();
    }

    public void hide()
    {
        setVisibility(View.GONE);
        mLoadingAnimation.stop();
        mLoadingAnimation = null;
    }
}
