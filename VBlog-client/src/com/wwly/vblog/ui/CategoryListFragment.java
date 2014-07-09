package com.wwly.vblog.ui;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONObject;

import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;

import com.android.volley.VolleyError;
import com.wwly.vblog.BaseFragment;
import com.wwly.vblog.HttpRequestListener;
import com.wwly.vblog.MainActivity;
import com.wwly.vblog.R;
import com.wwly.vblog.adapter.CategoryListAdapter;
import com.wwly.vblog.model.CategoryModel;
import com.wwly.vblog.model.CategoryModelList;
import com.wwly.vblog.model.DrawerListModel;
import com.wwly.vblog.utils.HttpUtil;
import com.wwly.vblog.utils.ToastUtil;

public class CategoryListFragment extends BaseFragment {

	private ListView mListView;
	private CategoryListAdapter mAdapter;
	private List<CategoryModel> mItems = new ArrayList<CategoryModel>();
	private CategoryModelList mCategoryModelList = new CategoryModelList();

	private HttpRequestListener mCategoryListListener = new HttpRequestListener() {

		@Override
		public void onSuccess(String result) {
			try {
				mCategoryModelList.parse(new JSONObject(result));
				onLoadDetailCompleted(true);
			} catch (Exception e) {
				onLoadDetailCompleted(false);
			}
		}

		@Override
		public void onNoLogin() {
		}

		@Override
		public void onFail(String result) {
			ToastUtil.showText(mFragmentActivity,
					R.string.http_fail_category_list);
			onLoadDetailCompleted(false);
		}

		@Override
		public void onNetworkError(VolleyError error) {
			ToastUtil.showText(mFragmentActivity, R.string.http_error_network);
			onLoadDetailCompleted(false);
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
			setHasOptionsMenu(true);
			mContext = getActivity().getBaseContext();
			mViewGroup = (ViewGroup) inflater.inflate(
					R.layout.fragment_category_list, null);
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
		showLoadingView();
		onLoadData();
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
		mListView = (ListView) mViewGroup.findViewById(R.id.listview);
		mAdapter = new CategoryListAdapter(mContext, mItems);

		mListView.setAdapter(mAdapter);
		mListView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
					long arg3) {
				showBlogList(mItems.get(arg2));
			}
		});
	}

	@Override
	protected void onClickOfErrorView(View v) {
		showLoadingView();
		onLoadData();
		removeErrorView();
	}

	private void onLoadData() {
		HttpUtil.getCategotyList(mCategoryListListener);
	}

	private void onLoadDetailCompleted(boolean success) {
		removeLoadingView();
		if (success) {
			if (mItems.size() > 0) {
				mItems.clear();
			}
			CategoryModel allCategoryModel = new CategoryModel();
			allCategoryModel.setId(CategoryModel.ATTACH_ALL_CATEGORY_ID);
			allCategoryModel
					.setCategoryName(getString(R.string.fragment_category_list_all_category));
			mItems.add(allCategoryModel);
			mItems.addAll(mCategoryModelList.getList());
			mAdapter.notifyDataSetChanged();
		} else {
			showErrorView();
		}
	}

	private void showBlogList(CategoryModel categoryModel) {
		BlogListFragment fragment = new BlogListFragment();
		if (categoryModel.getId() != CategoryModel.ATTACH_ALL_CATEGORY_ID) {
			fragment.setFragmentTitle(getString(R.string.drawer_item_list)
					+ ":" + categoryModel.getCategoryName());
			fragment.setCategoryId(categoryModel.getId());
		} else {
			fragment.setFragmentTitle(getString(R.string.drawer_item_list));
		}
		FragmentManager fragmentManager = mFragmentActivity
				.getSupportFragmentManager();
		fragmentManager.beginTransaction()
				.replace(R.id.content_frame, fragment).commit();

		if (mFragmentActivity instanceof MainActivity) {
			((MainActivity) mFragmentActivity)
					.selectedDrawerListItem(DrawerListModel.TAG_LIST);
		}
	}

}
