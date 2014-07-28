package com.wwly.vblog.ui;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONObject;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;

import com.android.volley.VolleyError;
import com.wwly.vblog.BaseFragment;
import com.wwly.vblog.HttpRequestListener;
import com.wwly.vblog.R;
import com.wwly.vblog.VBlogApplication;
import com.wwly.vblog.adapter.BlogListAdapter;
import com.wwly.vblog.model.BlogModel;
import com.wwly.vblog.model.BlogModelList;
import com.wwly.vblog.ui.widget.PlusListView;
import com.wwly.vblog.ui.widget.PlusListView.PlusListViewListener;
import com.wwly.vblog.utils.HttpUtil;
import com.wwly.vblog.utils.ToastUtil;

public class BlogListFragment extends BaseFragment {

	private final static int FIRST_BEGIN_INDEX = 0;
	private final static int REQUEST_COUNT = 5;

	private BlogListAdapter mAdapter;
	private List<BlogModel> mItems = new ArrayList<BlogModel>();
	private BlogModelList mBlogModelList = new BlogModelList();
	private PlusListView mPlusListView;
	private int mCategoryId = 0;
	private boolean mIsReloadData = false;

	private PlusListViewListener mPlusListViewListener = new PlusListViewListener() {
		@Override
		public void onLoadData(int begin, int count) {
			BlogListFragment.this.onLoadData(begin, count);
		}

		@Override
		public void onReloadData() {
			BlogListFragment.this.onReloadData();
		}
	};

	private HttpRequestListener mBlogListListener = new HttpRequestListener() {

		@Override
		public void onSuccess(String result) {
			try {
				mBlogModelList.parse(new JSONObject(result));
				mPlusListView.setTotalCount(new JSONObject(result)
						.optInt("total"));
				onLoadCompleted(true);
			} catch (Exception e) {
				onLoadCompleted(false);
			}
		}

		@Override
		public void onNoLogin() {
		}

		@Override
		public void onFail(String result) {
			ToastUtil.showText(mFragmentActivity, R.string.http_fail_blog_list);
			onLoadCompleted(false);
		}

		@Override
		public void onNetworkError(VolleyError error) {
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
			onReloadData();
		}
	}

	@Override
	public void onStart() {
		super.onStart();
		if (VBlogApplication.getInstance().isNeedRefreshBlogList()) {
			VBlogApplication.getInstance().setNeedRefreshBlogList(false);
			showLoadingView();
			onReloadData();
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
		mPlusListView = (PlusListView) mViewGroup
				.findViewById(R.id.plus_listview);
		mAdapter = new BlogListAdapter(mContext, mItems);
		mPlusListView.setOnItemClickListener(new OnItemClickListener() {
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
		mPlusListView.setAdapter(mAdapter);
		mPlusListView.setPlusListViewListener(mPlusListViewListener);
	}

	@Override
	protected void onClickOfErrorView(View v) {
		showLoadingView();
		onReloadData();
		removeErrorView();
	}

	private void onLoadData(int begin, int count) {
		if (mCategoryId == 0) {
			HttpUtil.getBlogList(mBlogListListener, begin, count);
		} else {
			HttpUtil.getBlogListByCategoryId(mBlogListListener, begin, count,
					mCategoryId);
		}
	}

	private void onLoadCompleted(boolean success) {
		removeLoadingView();
		if(mIsReloadData) {
			mItems.clear();
			mIsReloadData = false;
		}
		if (success) {
			if (mBlogModelList.getList().size() == 0) {
				showEmptyView();
			} else {
				removeEmptyView();
				if (!mItems.contains(mBlogModelList.getList())) {
					mItems.addAll(mBlogModelList.getList());
				}
			}
			mAdapter.notifyDataSetChanged();
		} else {
			if (mItems.size() == 0) {
				showErrorView();
			}
		}
		mPlusListView.onLoadFinish(success);
	}

	public void setCategoryId(int categoryId) {
		mCategoryId = categoryId;
	}

	public void onReloadData() {
		mIsReloadData = true;
		onLoadData(FIRST_BEGIN_INDEX, REQUEST_COUNT);
	}
}
