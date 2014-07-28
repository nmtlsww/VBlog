package com.wwly.vblog.ui.widget;

import android.content.Context;
import android.graphics.drawable.AnimationDrawable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AbsListView.OnScrollListener;
import android.widget.Adapter;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.RelativeLayout;

import com.wwly.vblog.R;

public class PlusListView extends RelativeLayout implements
		OnScrollListener, SwipeRefreshLayout.OnRefreshListener {
	
	private SwipeRefreshLayout mSwipeRefreshLayout;
	private ListView mListView;
	private Adapter mAdapter;
	private ViewGroup mFooterView;
	private View mFooterLoadingView;
	private View mFooterFailView;

	private int mLastVisibleIndex;
	private int mTotalCount;
	private int mRequestCount = 5;
	private boolean mIsLoading = false;

	private PlusListViewListener mListener;

	private enum FooterViewEnum {
		LOADING, FAIL, NONE
	};

	private FooterViewEnum mCurrentFooterType = FooterViewEnum.NONE;

	private OnClickListener mFailFooterListener = new OnClickListener() {
		@Override
		public void onClick(View v) {
			if (mListener != null) {
				mIsLoading = true;
				mListener.onLoadData(getCount(), mRequestCount);
				mCurrentFooterType = FooterViewEnum.LOADING;
				updateFooterView();
			}
		}
	};

	public PlusListView(Context context) {
		super(context);
		setupViews();
	}
	
	public PlusListView(Context context, AttributeSet attrs) {
		super(context, attrs);
		setupViews();
	}

	private void setupViews() {
		LayoutInflater inflater = (LayoutInflater) getContext()
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		ViewGroup viewGroup = (ViewGroup) inflater.inflate(
				R.layout.plus_list_view_layout, this, true);

		mFooterView = (ViewGroup) inflater.inflate(
				R.layout.blog_list_empty_footer, null);
		mFooterLoadingView = inflater.inflate(R.layout.widget_loading_view,
				null);
		mFooterFailView = inflater.inflate(R.layout.widget_error_view, null);
		mFooterFailView.findViewById(R.id.retry_text).setOnClickListener(
				mFailFooterListener);

		mListView = (ListView) viewGroup.findViewById(R.id.listview);
		mListView.addFooterView(mFooterView);
		mListView.setOnScrollListener(this);
		
		mSwipeRefreshLayout = (SwipeRefreshLayout)findViewById(R.id.swipe_refresh);
		mSwipeRefreshLayout.setOnRefreshListener(this);
		mSwipeRefreshLayout.setColorScheme(android.R.color.holo_red_light,
				android.R.color.holo_green_light,
				android.R.color.holo_blue_bright,
				android.R.color.holo_orange_light);
	}

	public void setAdapter(BaseAdapter adapter) {
		mAdapter = adapter;
		if (mListView != null && mAdapter != null)
			mListView.setAdapter(adapter);
	}

	public void setPlusListViewListener(
			PlusListViewListener plusListViewListener) {
		mListener = plusListViewListener;
	}

	public void setRequestCount(int count) {
		mRequestCount = count;
	}
	
	public void setFooterLoadingView(View view){
		mFooterLoadingView = view;
	}
	
	public void setFooterFailView(View view){
		mFooterFailView = view;
	}

	public void setTotalCount(int count) {
		mTotalCount = count;
	}
	
	public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
		mListView.setOnItemClickListener(onItemClickListener);
	}

	public void onLoadFinish(boolean success) {
		mIsLoading = false;
		mSwipeRefreshLayout.setRefreshing(false);
		if (success) {
			if (mTotalCount > getCount()) {
				mCurrentFooterType = FooterViewEnum.LOADING;
			} else {
				mCurrentFooterType = FooterViewEnum.NONE;
			}
			updateFooterView();
		} else {
			if (getCount() != 0) {
				mCurrentFooterType = FooterViewEnum.FAIL;
				updateFooterView();
			}
		}
	}

	public boolean isLoading() {
		return mIsLoading;
	}

	private int getCount() {
		if (mAdapter != null)
			return mAdapter.getCount();
		return 0;
	}

	private void updateFooterView() {
		switch (mCurrentFooterType) {
		case LOADING:
			mFooterView.removeAllViews();
			mFooterView.addView(mFooterLoadingView);
			AnimationDrawable mLoadingAnimation = (AnimationDrawable) mFooterLoadingView
					.findViewById(R.id.loading_icon).getBackground();
			mLoadingAnimation.start();
			break;
		case NONE:
			mFooterView.removeAllViews();
			break;
		case FAIL:
			mFooterView.removeAllViews();
			mFooterView.addView(mFooterFailView);
			break;
		default:
			break;
		}

	}

	@Override
	public void onRefresh() {
		if (mListener != null && !mIsLoading) {
			mIsLoading = true;
			mListener.onReloadData();
		}
	}

	@Override
	public void onScroll(AbsListView view, int firstVisibleItem,
			int visibleItemCount, int totalItemCount) {
		mLastVisibleIndex = firstVisibleItem + visibleItemCount - 1;
	}

	@Override
	public void onScrollStateChanged(AbsListView view, int scrollState) {
		if (scrollState == OnScrollListener.SCROLL_STATE_IDLE
				&& mLastVisibleIndex == getCount() ) {
			if (mCurrentFooterType == FooterViewEnum.LOADING && !mIsLoading) {
				if (mListener != null) {
					mIsLoading = true;
					mListener.onLoadData(mLastVisibleIndex, mRequestCount);
				}
			}
		}
	}

	public static interface PlusListViewListener {
		public void onLoadData(int begin, int count);
		public void onReloadData();
	}
}
