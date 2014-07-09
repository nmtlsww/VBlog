package com.wwly.vblog.ui;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONObject;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;

import com.android.volley.VolleyError;
import com.wwly.vblog.BaseFragment;
import com.wwly.vblog.HttpRequestListener;
import com.wwly.vblog.R;
import com.wwly.vblog.VBlogApplication;
import com.wwly.vblog.model.BlogModel;
import com.wwly.vblog.model.CategoryModel;
import com.wwly.vblog.model.CategoryModelList;
import com.wwly.vblog.model.UserModel;
import com.wwly.vblog.utils.HttpUtil;
import com.wwly.vblog.utils.StringUtil;
import com.wwly.vblog.utils.ToastUtil;

public class EditBlogFragment extends BaseFragment {

	private EditText mTitleEditText;
	private EditText mContentEditText;
	private Spinner mCategorySpinner;
	private EditText mNewCategoryEditText;
	private ProgressDialog mProgressDialog;

	private List<CategoryModel> mItems = new ArrayList<CategoryModel>();
	private CategoryModelList mCategoryModelList = new CategoryModelList();
	private ArrayAdapter<CategoryModel> mAdapter;
	private CategoryModel mAttachCategoryModel = new CategoryModel();

	private boolean mIsUpdateBlog = false;
	private BlogModel mUpdateBlog;

	private HttpRequestListener mAddBlogListener = new HttpRequestListener() {

		@Override
		public void onSuccess(String result) {
			if (mProgressDialog != null) {
				mProgressDialog.dismiss();
			}
			ToastUtil.showText(mFragmentActivity, getString(R.string.toast_add_blog_success));
			if(mIsUpdateBlog) {
				VBlogApplication.getInstance().setNeedRefreshBlogDetail(true);
			} else {
				VBlogApplication.getInstance().setNeedRefreshBlogList(true);
			}
			
			mFragmentActivity.onBackPressed();
		}

		@Override
		public void onNoLogin() {
			if (mProgressDialog != null) {
				mProgressDialog.dismiss();
			}
			ToastUtil.showText(mFragmentActivity, R.string.http_error_no_login);
		}

		@Override
		public void onFail(String result) {
			if (mProgressDialog != null) {
				mProgressDialog.dismiss();
			}
			ToastUtil.showText(mFragmentActivity, R.string.http_fail_add_blog);
		}

		@Override
		public void onNetworkError(VolleyError error) {
			if (mProgressDialog != null) {
				mProgressDialog.dismiss();
			}
			ToastUtil.showText(mFragmentActivity, R.string.http_error_network);
		}

	};

	private HttpRequestListener mCategoryListListener = new HttpRequestListener() {

		@Override
		public void onSuccess(String result) {
			try {
				mCategoryModelList.parse(new JSONObject(result));
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
			ToastUtil.showText(mFragmentActivity, R.string.http_fail_category_list);
		}

		@Override
		public void onNetworkError(VolleyError error) {
			ToastUtil.showText(mFragmentActivity, R.string.http_error_network);
		}

	};

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		if (container == null)
			return null;

		if (mViewGroup == null) {
			setHasOptionsMenu(true);
			mContext = getActivity().getBaseContext();
			mViewGroup = (ViewGroup) inflater.inflate(
					R.layout.fragment_add_blog, null);
			setupView();
		}
		return super.onCreateView(inflater, container, savedInstanceState);
	}

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
		onLoadData();
	}

	public void setupView() {
		mTitleEditText = (EditText) mViewGroup
				.findViewById(R.id.add_blog_username);
		mContentEditText = (EditText) mViewGroup
				.findViewById(R.id.add_blog_content);
		mCategorySpinner = (Spinner) mViewGroup
				.findViewById(R.id.add_blog_category_spinner);

		mNewCategoryEditText = (EditText) mViewGroup
				.findViewById(R.id.add_blog_new_category);

		mAdapter = new ArrayAdapter<CategoryModel>(mFragmentActivity,
				android.R.layout.simple_spinner_item, mItems);

		mAttachCategoryModel.setId(CategoryModel.ATTACH_NEW_CATEGORY_ID);
		mAttachCategoryModel.setCategoryName(getString(R.string.toast_add_blog_new_category));
		mItems.add(mAttachCategoryModel);
		mCategorySpinner.setAdapter(mAdapter);

		mCategorySpinner
				.setOnItemSelectedListener(new OnItemSelectedListener() {

					@Override
					public void onItemSelected(AdapterView<?> arg0, View arg1,
							int arg2, long arg3) {
						if (arg2 == 0) {
							mNewCategoryEditText.setText("");
							mNewCategoryEditText.setEnabled(true);
						} else {
							if (mItems.size() > arg2) {
								mNewCategoryEditText.setText(mItems.get(arg2)
										.getCategoryName());
							} else {
								mNewCategoryEditText.setText("");
							}
							mNewCategoryEditText.setEnabled(false);
						}
					}

					@Override
					public void onNothingSelected(AdapterView<?> arg0) {

					}
				});

		if (mUpdateBlog != null && mIsUpdateBlog) {
			mTitleEditText.setText(mUpdateBlog.getTitle());
			mContentEditText.setText(mUpdateBlog.getContent());
		}
	}

	@Override
	public void onDestroy() {
		super.onDestroy();
		release();
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case R.id.action_send_comment:
			submitBlog();
			return true;
		}
		return false;
	}

	private void onLoadData() {
		HttpUtil.getCategotyList(mCategoryListListener);
	}

	private void onLoadCompleted(boolean success) {
		if (success) {
			mItems.addAll(mCategoryModelList.getList());
			mAdapter.notifyDataSetChanged();

			if (mIsUpdateBlog && mUpdateBlog != null) {
				for (int i = 0; i < mItems.size(); i++) {
					if (mItems.get(i).getId() == mUpdateBlog.getCategoryId()) {
						mCategorySpinner.setSelection(i);
					}
				}
			}

		} else {
			if (mItems.size() == 0) {
				mFragmentActivity.onBackPressed();
			}
		}
	}

	private void submitBlog() {
		String title = mTitleEditText.getText().toString();
		String content = mContentEditText.getText().toString();
		String categoryName = mNewCategoryEditText.getText().toString();
		int categoryId = ((CategoryModel) mCategorySpinner.getSelectedItem())
				.getId();

		if (StringUtil.isEmpty(title)
				|| StringUtil.isEmpty(content)
				|| (categoryId == CategoryModel.ATTACH_NEW_CATEGORY_ID && StringUtil
						.isEmpty(categoryName))) {
			ToastUtil.showText(mFragmentActivity,
					R.string.toast_add_comment_error_incomplete);
			return;
		}

		if (mIsUpdateBlog && mUpdateBlog != null) {
			HttpUtil.updateBlog(mAddBlogListener, mUpdateBlog.getId(), mUpdateBlog.getUserId(), title,
					content, categoryId, categoryName);
		} else {
			UserModel user = VBlogApplication.getInstance().getUserModel();
			if (user == null) {
				ToastUtil.showText(mFragmentActivity, R.string.http_error_no_login);
				return;
			}
			HttpUtil.addBlog(mAddBlogListener, user.getId(), title, content,
					categoryId, categoryName);
		}

		mProgressDialog = ProgressDialog.show(mFragmentActivity, null,
				getString(R.string.progress_sending_date), true, false);
	}

	public void setEditBlog(BlogModel blog) {
		mIsUpdateBlog = true;
		mUpdateBlog = blog;
	}

}
