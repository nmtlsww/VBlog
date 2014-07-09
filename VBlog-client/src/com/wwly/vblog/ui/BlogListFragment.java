package com.wwly.vblog.ui;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONObject;

import android.content.Intent;
import android.graphics.drawable.AnimationDrawable;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AbsListView.OnScrollListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;

import com.android.volley.VolleyError;
import com.wwly.vblog.BaseFragment;
import com.wwly.vblog.HttpRequestListener;
import com.wwly.vblog.R;
import com.wwly.vblog.VBlogApplication;
import com.wwly.vblog.adapter.BlogListAdapter;
import com.wwly.vblog.model.BlogModel;
import com.wwly.vblog.model.BlogModelList;
import com.wwly.vblog.utils.HttpUtil;
import com.wwly.vblog.utils.ToastUtil;

public class BlogListFragment extends BaseFragment implements
		OnScrollListener, SwipeRefreshLayout.OnRefreshListener {

	private final static int FIRST_BEGIN_INDEX = 0;
	private final static int REQUEST_COUNT = 5;

	private SwipeRefreshLayout mSwipeLayout;
	private ListView mListView;
	private ViewGroup mFooterView;
	private View mLoadingFooterView;
	private View mFailFooterView;
	private BlogListAdapter mAdapter;
	private List<BlogModel> mItems = new ArrayList<BlogModel>();
	private BlogModelList mBlogModelList = new BlogModelList();

	private int mLastVisibleIndex;
	private int mTotalCount;
	private boolean mIsAllLoadFinish = true;
	private boolean mIsLoading = false;
	private boolean mNeedAfreshDate = true;
	
	private int mCategoryId = 0;

	private enum FooterViewEnum {
		LOADING, FAIL, NONE
	};

	private FooterViewEnum mCurrentFooterView = FooterViewEnum.NONE;

	private HttpRequestListener mBlogListListener = new HttpRequestListener() {

		@Override
		public void onSuccess(String result) {
			try {
				mIsLoading = false;
				mBlogModelList.parse(new JSONObject(result));
				mTotalCount = new JSONObject(result).optInt("total");
				onLoadCompleted(true);
			} catch (Exception e) {
				onLoadCompleted(false);
			}
		}

		@Override
		public void onNoLogin() {
			mIsLoading = false;
		}

		@Override
		public void onFail(String result) {
			mIsLoading = false;
			ToastUtil.showText(mFragmentActivity, R.string.http_fail_blog_list);
			onLoadCompleted(false);
		}

		@Override
		public void onNetworkError(VolleyError error) {
			mIsLoading = false;
			ToastUtil.showText(mFragmentActivity, R.string.http_error_network);
			onLoadCompleted(false);
		}

	};

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		if (container == null)
			return null;
		if (mViewGroup == null) {
			mContext = getActivity().getBaseContext();
			mViewGroup = (ViewGroup) inflater.inflate(
					R.layout.fragment_blog_list, null);
			setupView();
		}

		if (mItems != null) {
			mItems.clear();
		}

		return super.onCreateView(inflater, container, savedInstanceState);
	}

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
		if ((mItems.size() == 0)) {
			showLoadingView();
			onLoadData(FIRST_BEGIN_INDEX, REQUEST_COUNT);
		}
	}
	
	@Override
	public void onStart() {
		super.onStart();
		if(VBlogApplication.getInstance().isNeedRefreshBlogList()) {
			mNeedAfreshDate = true;
			VBlogApplication.getInstance().setNeedRefreshBlogList(false);
			showLoadingView();
			onLoadData(FIRST_BEGIN_INDEX, REQUEST_COUNT);
		}
	}

	@Override
	public void onResume() {
		super.onResume();
		if (mAdapter != null) {
			mAdapter.notifyDataSetChanged();
		}
	}

	@Override
	public void onDestroy() {
		super.onDestroy();
		release();
	}

	private void setupView() {
		mFooterView = (ViewGroup) mFragmentActivity.getLayoutInflater()
				.inflate(R.layout.blog_list_empty_footer, null);
		mLoadingFooterView = mFragmentActivity.getLayoutInflater().inflate(
				R.layout.widget_loading_view, null);
		mFailFooterView = mFragmentActivity.getLayoutInflater().inflate(
				R.layout.widget_error_view, null);
		mFailFooterView.findViewById(R.id.retry_text).setOnClickListener(
				new OnClickListener() {
					@Override
					public void onClick(View v) {
						onLoadData(mItems.size(), REQUEST_COUNT);
						mCurrentFooterView = FooterViewEnum.LOADING;
						updateFooterView();
					}
				});

		mListView = (ListView) mViewGroup.findViewById(R.id.listview);
		mAdapter = new BlogListAdapter(mContext, mItems);
		mListView.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
					long arg3) {
				if (mItems != null && mItems.size() > arg2) {
					Intent intent = new Intent(mFragmentActivity,
							BlogDetailActivity.class);
					Bundle bundle = new Bundle();
					bundle.putInt(BlogDetailActivity.INTENT_BUNDLE_ID, mItems
							.get(arg2).getId());
					intent.putExtras(bundle);
					mFragmentActivity.startActivity(intent);
					mFragmentActivity.overridePendingTransition(
							R.anim.in_from_right, R.anim.out_to_left);
				}
			}
		});
		mListView.addFooterView(mFooterView);
		mListView.setAdapter(mAdapter);
		mListView.setOnScrollListener(this);

		mSwipeLayout = (SwipeRefreshLayout) mViewGroup
				.findViewById(R.id.swipe_refresh);
		mSwipeLayout.setOnRefreshListener(this);

		mSwipeLayout.setColorScheme(android.R.color.holo_red_light,
				android.R.color.holo_green_light,
				android.R.color.holo_blue_bright,
				android.R.color.holo_orange_light);
	}

	@Override
	protected void onClickOfErrorView(View v) {
		showLoadingView();
		onLoadData(FIRST_BEGIN_INDEX, REQUEST_COUNT);
		removeErrorView();
	}

	@Override
	public void onRefresh() {
		mNeedAfreshDate = true;
		onLoadData(FIRST_BEGIN_INDEX, REQUEST_COUNT);
	}

	private void updateFooterView() {
		switch (mCurrentFooterView) {
		case LOADING:
			mFooterView.removeAllViews();
			mFooterView.addView(mLoadingFooterView);
			AnimationDrawable mLoadingAnimation = (AnimationDrawable) mLoadingFooterView
					.findViewById(R.id.loading_icon).getBackground();
			mLoadingAnimation.start();
			break;
		case NONE:
			mFooterView.removeAllViews();
			break;
		case FAIL:
			mFooterView.removeAllViews();
			mFooterView.addView(mFailFooterView);
			break;
		default:
			break;
		}

	}

	private void onLoadData(int begin, int count) {
		mIsLoading = true;
		if(mCategoryId == 0) {
			HttpUtil.getBlogList(mBlogListListener, begin, count);
		} else {
			HttpUtil.getBlogListByCategoryId(mBlogListListener, begin, count, mCategoryId);
		}
	}

	private void onLoadCompleted(boolean success) {
		removeLoadingView();
		if (success) {
			if (mNeedAfreshDate) {
				mNeedAfreshDate = false;
				mItems.clear();
			}
			
			if(mBlogModelList.getList().size() == 0) {
				showEmptyView();
			} else {
				removeEmptyView();
				if (!mItems.contains(mBlogModelList.getList())) {
					mItems.addAll(mBlogModelList.getList());
				}
			}

			if (mTotalCount > mItems.size() && mIsAllLoadFinish) {
				mIsAllLoadFinish = false;
				mCurrentFooterView = FooterViewEnum.LOADING;
				updateFooterView();
			} else if (mTotalCount == mItems.size() && !mIsAllLoadFinish) {
				mIsAllLoadFinish = true;
				mCurrentFooterView = FooterViewEnum.NONE;
				updateFooterView();
			}

			mAdapter.notifyDataSetChanged();
		} else {
			if (mItems.size() == 0) {
				showErrorView();
			} else {
				mCurrentFooterView = FooterViewEnum.FAIL;
				updateFooterView();
			}
		}
		mSwipeLayout.setRefreshing(false);
	}
	
	public void setCategoryId(int categoryId) {
		mCategoryId = categoryId;
	}

	@Override
	public void onScrollStateChanged(AbsListView view, int scrollState) {
		if (scrollState == OnScrollListener.SCROLL_STATE_IDLE
				&& mLastVisibleIndex == mAdapter.getCount()
				&& !mIsAllLoadFinish) {
			if (!mIsLoading)
				onLoadData(mLastVisibleIndex, REQUEST_COUNT);
		}
	}

	@Override
	public void onScroll(AbsListView view, int firstVisibleItem,
			int visibleItemCount, int totalItemCount) {
		mLastVisibleIndex = firstVisibleItem + visibleItemCount - 1;

		if (totalItemCount == (mListView.getFooterViewsCount() + mListView
				.getHeaderViewsCount())) {
			return;
		}
	}
}
