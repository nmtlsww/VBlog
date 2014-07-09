package com.wwly.vblog.ui;

import org.json.JSONObject;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.android.volley.VolleyError;
import com.wwly.vblog.BaseFragment;
import com.wwly.vblog.HttpRequestListener;
import com.wwly.vblog.R;
import com.wwly.vblog.VBlogApplication;
import com.wwly.vblog.model.BlogModel;
import com.wwly.vblog.utils.HttpUtil;
import com.wwly.vblog.utils.ToastUtil;

public class BlogDetailFragment extends BaseFragment {
	
	private BlogModel mBlogModel;
	private TextView mTitleView;
	private TextView mContentView;
	private TextView mCategoryName;
	
	private ProgressDialog mDeleteProgressDialog;
	
	private HttpRequestListener mBlogDetailListener = new HttpRequestListener() {

		@Override
		public void onSuccess(String result) {
			try {
				mBlogModel = new BlogModel(new JSONObject(result));
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
			ToastUtil.showText(mFragmentActivity, R.string.http_fail_blog_detail);
			onLoadDetailCompleted(false);
		}

		@Override
		public void onNetworkError(VolleyError error) {
			ToastUtil.showText(mFragmentActivity, R.string.http_error_network);
			onLoadDetailCompleted(false);
		}

	};
	
	private HttpRequestListener mBlogDeleteListener = new HttpRequestListener() {

		@Override
		public void onSuccess(String result) {
			if (mDeleteProgressDialog != null) {
				mDeleteProgressDialog.dismiss();
			}
			ToastUtil.showText(mFragmentActivity, R.string.toast_delete_success);
			VBlogApplication.getInstance().setNeedRefreshBlogList(true);
			mFragmentActivity.onBackPressed();
		}

		@Override
		public void onNoLogin() {
			if (mDeleteProgressDialog != null) {
				mDeleteProgressDialog.dismiss();
			}
			ToastUtil.showText(mFragmentActivity, R.string.http_error_no_login);
		}

		@Override
		public void onFail(String result) {
			if (mDeleteProgressDialog != null) {
				mDeleteProgressDialog.dismiss();
			}
			ToastUtil.showText(mFragmentActivity, R.string.toast_delete_fail);
		}

		@Override
		public void onNetworkError(VolleyError error) {
			if (mDeleteProgressDialog != null) {
				mDeleteProgressDialog.dismiss();
			}
			ToastUtil.showText(mFragmentActivity, R.string.http_error_network);
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
					R.layout.fragment_blog_detail, null);
			setupView();
		}
		return super.onCreateView(inflater, container, savedInstanceState);
	}

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
		if (mId == -1) {
			mFragmentActivity.onBackPressed();
		} else {
			showLoadingView();
			onLoadData();
		}
	}
	
	@Override
	public void onStart() {
		super.onStart();
		if(VBlogApplication.getInstance().isNeedRefreshBlogDetail()) {
			VBlogApplication.getInstance().setNeedRefreshBlogDetail(false);
			VBlogApplication.getInstance().setNeedRefreshBlogList(true);
			if (mId == -1) {
				mFragmentActivity.onBackPressed();
			} else {
				showLoadingView();
				onLoadData();
			}
		}
	}
	
	public void setupView() {
		mTitleView = (TextView)mViewGroup.findViewById(R.id.blog_title);
		mContentView = (TextView)mViewGroup.findViewById(R.id.blog_content);
		mCategoryName = (TextView)mViewGroup.findViewById(R.id.blog_category_name);
	}
	
	@Override
	public void onDestroy() {
		super.onDestroy();
		release();
	}
	
	private void onLoadData() {
		HttpUtil.getBlogDetail(mBlogDetailListener, mId);
	}

	private void onLoadDetailCompleted(boolean success) {
		removeLoadingView();
		if (success) {
			if(mBlogModel != null) {
				mTitleView.setText(mBlogModel.getTitle());
				mContentView.setText(mBlogModel.getContent());
				mCategoryName.setText(mBlogModel.getCategoryName());
			}
		} else {
			showErrorView();
		}
	}
	
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		if(mBlogModel == null) {
			ToastUtil.showText(mFragmentActivity, R.string.toast_blog_detail_getting_data);
		}
		switch (item.getItemId()) {
		case R.id.action_detail_edit:
			Intent intent = new Intent(mFragmentActivity, EditActivity.class);
			Bundle bundle = new Bundle();
			bundle.putInt(EditActivity.INTENT_BUNDLE_EDIT_TYPE,
					EditActivity.TYPE_ADD_BLOG);
			bundle.putSerializable(EditActivity.INTENT_BUNDLE_EDIT_BLOG_MODEL, mBlogModel);
			intent.putExtras(bundle);
			startActivity(intent);
			mFragmentActivity.overridePendingTransition(R.anim.in_from_right, R.anim.out_to_left);
			return true;
		case R.id.action_detail_info:
			showInfoDialog();
			return true;
		case R.id.action_detail_remove:
			showDeleteDialog();
			return true;
		}
		return false;
	}
	
	private void showInfoDialog() {
		AlertDialog.Builder builder = new AlertDialog.Builder(mFragmentActivity);

		final View dialogView = mFragmentActivity.getLayoutInflater().inflate(
				R.layout.dialog_blog_detail_info, null);
		String title = String.format(getString(R.string.dialog_info_title), mBlogModel.getTitle());
		String username = String.format(getString(R.string.dialog_info_username), mBlogModel.getUsername());
		String categoryName = String.format(getString(R.string.dialog_info_category), mBlogModel.getCategoryName());
		String createTime = String.format(getString(R.string.dialog_info_create_time), mBlogModel.getCreateTime());
		String updateTime = String.format(getString(R.string.dialog_info_update_time), mBlogModel.getUpdateTime());

		((TextView)dialogView.findViewById(R.id.dialog_info_title)).setText(title);
		((TextView)dialogView.findViewById(R.id.dialog_info_username)).setText(username);
		((TextView)dialogView.findViewById(R.id.dialog_info_category)).setText(categoryName);
		((TextView)dialogView.findViewById(R.id.dialog_info_create_time)).setText(createTime);
		((TextView)dialogView.findViewById(R.id.dialog_info_update_time)).setText(updateTime);
		
		final AlertDialog alertDialog = builder
				.setView(dialogView)
				.setTitle(R.string.action_detail_info_blog)
				.setPositiveButton(R.string.dialog_ok,
						new DialogInterface.OnClickListener() {
							public void onClick(DialogInterface dialog, int id) {
							}
						}).create();
		alertDialog.show();
	}
	
	private void showDeleteDialog() {
		AlertDialog.Builder builder = new AlertDialog.Builder(mFragmentActivity);

		final AlertDialog alertDialog = builder
				.setMessage(R.string.dialog_delete_blog_message)
				.setTitle(R.string.dialog_title)
				.setPositiveButton(R.string.dialog_ok,
						new DialogInterface.OnClickListener() {
							@Override
							public void onClick(DialogInterface dialog, int id) {
								mDeleteProgressDialog = ProgressDialog.show(
										mFragmentActivity, null, getString(R.string.progress_deleting), true,
										false);
								HttpUtil.deleteBlog(mBlogDeleteListener, mBlogModel.getId());
							}
						})
				.setNegativeButton(R.string.dialog_cancel,
						new DialogInterface.OnClickListener() {
							public void onClick(DialogInterface dialog, int id) {
							}
						}).create();
		alertDialog.show();
	}

}
