package com.wwly.vblog;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.widget.RelativeLayout;

import com.wwly.vblog.ui.widget.EmptyView;
import com.wwly.vblog.ui.widget.ErrorView;
import com.wwly.vblog.ui.widget.LoadingView;
import com.wwly.vblog.utils.StringUtil;

public abstract class BaseFragment extends Fragment {

	protected FragmentActivity mFragmentActivity;
	protected Context mContext;

	// views
	protected ViewGroup mViewGroup;
	private LoadingView mLoadingView;
	private ErrorView mErrorView;
	private EmptyView mEmptyView;
	private RelativeLayout.LayoutParams mViewLayoutParams = new RelativeLayout.LayoutParams(
			LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT);

	protected int mId = -1;
	private boolean mIsForeground = false;
	private String mFragmentTitle = "";

	public interface OnLoadFinishListener {
		public void onLoadFinish(BaseFragment fragment);
	}

	public String getFragmentTitle() {
		return mFragmentTitle;
	}

	public void setFragmentTitle(String title) {
		mFragmentTitle = title;
	}
	
	public void setId(int id) {
		mId = id;
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		mFragmentActivity = getActivity();
		mContext = getActivity().getBaseContext();
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		if (mViewGroup != null) {
			if (mViewGroup.getParent() != null) {
				((ViewGroup) mViewGroup.getParent()).removeView(mViewGroup);
			}
		}
		if(!StringUtil.isEmpty(mFragmentTitle)) {
			mFragmentActivity.setTitle(mFragmentTitle);
			mFragmentActivity.getActionBar().setTitle(mFragmentTitle);
		}
		return mViewGroup;
	}

	@Override
	public void onResume() {
		super.onResume();
		mIsForeground = true;
	}

	@Override
	public void onPause() {
		super.onPause();
		mIsForeground = false;
	}

	public Context getContext() {
		return mContext;
	}

	public Activity getFragmentActivity() {
		return mFragmentActivity;
	}

	public boolean onKeyDown(int keyCode, KeyEvent event) {
		return false;
	}

	protected void addLoadingView() {
		if (mLoadingView == null) {
			mLoadingView = new LoadingView(mContext);
		}
		ViewGroup parent = (ViewGroup) mLoadingView.getParent();
		if (parent != mViewGroup) {
			if (parent != null) {
				parent.removeView(mLoadingView);
			}
			mViewGroup.addView(mLoadingView);
		}

		mLoadingView.setLayoutParams(mViewLayoutParams);
	}

	public void removeLoadingView() {
		if (mLoadingView != null && mLoadingView.getParent() != null) {
			((ViewGroup) mLoadingView.getParent()).removeView(mLoadingView);
			mLoadingView = null;
		}
	}

	protected void showLoadingView() {
		addLoadingView();
		mLoadingView.show();
	}

	private void addErrorView() {
		if (mErrorView == null) {
			mErrorView = new ErrorView(mContext);
		}
		ViewGroup parent = (ViewGroup) mErrorView.getParent();
		if (parent != mViewGroup) {
			if (parent != null) {
				parent.removeView(mErrorView);
			}
			mViewGroup.addView(mErrorView);
		}

		mErrorView.setLayoutParams(mViewLayoutParams);
		mErrorView.setRetryClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				onClickOfErrorView(v);
			}
		});
	}

	protected void onClickOfErrorView(View v) {
	}

	public void removeErrorView() {
		if (mErrorView != null && mErrorView.getParent() != null) {
			mErrorView.setVisibility(View.GONE);
			((ViewGroup) mErrorView.getParent()).removeView(mErrorView);
			mErrorView = null;
		}
	}

	protected void showErrorView() {
		addErrorView();
		mErrorView.show();
	}

	protected void addEmptyView() {
		if (mEmptyView == null) {
			mEmptyView = new EmptyView(mContext);
		}
		ViewGroup parent = (ViewGroup) mEmptyView.getParent();
		if (parent != mViewGroup) {
			if (parent != null) {
				parent.removeView(mEmptyView);
			}
			mViewGroup.addView(mEmptyView);
		}
		
		RelativeLayout.LayoutParams mEmptyViewLayoutParams = new RelativeLayout.LayoutParams(
				LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
		mEmptyViewLayoutParams.addRule(RelativeLayout.CENTER_IN_PARENT); 
		
		mEmptyView.setLayoutParams(mEmptyViewLayoutParams);
	}

	public void removeEmptyView() {
		if (mEmptyView != null && mEmptyView.getParent() != null) {
			((ViewGroup) mEmptyView.getParent()).removeView(mEmptyView);
			mEmptyView = null;
		}
	}

	protected void showEmptyView() {
		addEmptyView();
		mEmptyView.show();
	}

	public void release() {
		if (mViewGroup != null) {
			if (mViewGroup.getParent() != null) {
				((ViewGroup) mViewGroup.getParent()).removeView(mViewGroup);
			}
			mViewGroup.removeAllViews();
			mLoadingView = null;
			mErrorView = null;
			mViewGroup = null;
		}
	}

	public boolean isForeground() {
		return mIsForeground;
	}
}
