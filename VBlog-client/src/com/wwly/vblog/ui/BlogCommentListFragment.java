package com.wwly.vblog.ui;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONObject;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemLongClickListener;
import android.widget.EditText;
import android.widget.ListView;

import com.android.volley.VolleyError;
import com.wwly.vblog.BaseFragment;
import com.wwly.vblog.HttpRequestListener;
import com.wwly.vblog.R;
import com.wwly.vblog.VBlogApplication;
import com.wwly.vblog.adapter.CommentListAdapter;
import com.wwly.vblog.model.CommentModel;
import com.wwly.vblog.model.CommentModelList;
import com.wwly.vblog.utils.HttpUtil;
import com.wwly.vblog.utils.SystemUtil;
import com.wwly.vblog.utils.ToastUtil;

public class BlogCommentListFragment extends BaseFragment {

	private ListView mListView;
	private CommentListAdapter mAdapter;
	private List<CommentModel> mItems = new ArrayList<CommentModel>();
	private CommentModelList mCommentModelList = new CommentModelList();
	private ProgressDialog mProgressDialog;
	private EditText mReplyButton;

	private HttpRequestListener mCommentListListener = new HttpRequestListener() {

		@Override
		public void onSuccess(String result) {
			try {
				mCommentModelList.parse(new JSONObject(result));
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
			ToastUtil.showText(mFragmentActivity, R.string.http_fail_comment_list);
			onLoadDetailCompleted(false);
		}

		@Override
		public void onNetworkError(VolleyError error) {
			ToastUtil.showText(mFragmentActivity, R.string.http_error_network);
			onLoadDetailCompleted(false);
		}

	};

	private HttpRequestListener mDeleteCommentListener = new HttpRequestListener() {

		@Override
		public void onSuccess(String result) {
			if (mProgressDialog != null) {
				mProgressDialog.dismiss();
			}
			ToastUtil.showText(mFragmentActivity, R.string.toast_delete_success);
			showLoadingView();
			onLoadData();
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
			ToastUtil.showText(mFragmentActivity, R.string.toast_delete_fail);
		}

		@Override
		public void onNetworkError(VolleyError error) {
			if (mProgressDialog != null) {
				mProgressDialog.dismiss();
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
					R.layout.fragment_comment_list, null);
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
		if(VBlogApplication.getInstance().isNeedRefreshCommentList()) {
			VBlogApplication.getInstance().setNeedRefreshCommentList(false);
			if (mId == -1) {
				mFragmentActivity.onBackPressed();
			} else {
				showLoadingView();
				onLoadData();
			}
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
		mListView = (ListView) mViewGroup.findViewById(R.id.listview);
		mAdapter = new CommentListAdapter(mContext, mItems);
		mListView.setOnItemLongClickListener(new OnItemLongClickListener() {

			@Override
			public boolean onItemLongClick(AdapterView<?> arg0, View arg1,
					int arg2, long arg3) {
				SystemUtil.LongClickVibrator(mFragmentActivity);
				showDeleteCommentDialog(mItems.get(arg2).getId());
				return false;
			}
		});

		mListView.setAdapter(mAdapter);
		
		mReplyButton = (EditText)mViewGroup.findViewById(R.id.comment_list_reply_button);
	
		mReplyButton.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				Intent intent = new Intent(mFragmentActivity, EditActivity.class);
				Bundle bundle = new Bundle();
				bundle.putInt(EditActivity.INTENT_BUNDLE_ADD_COMMENT_BLOG_ID, mId);
				bundle.putInt(EditActivity.INTENT_BUNDLE_EDIT_TYPE, EditActivity.TYPE_ADD_COMMENT);
				intent.putExtras(bundle);
				mFragmentActivity.startActivity(intent);
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
		HttpUtil.getCommentList(mCommentListListener, mId);
	}

	private void onLoadDetailCompleted(boolean success) {
		removeLoadingView();
		if (success) {
			if (mItems.size() > 0) {
				mItems.clear();
			}
			if(mCommentModelList.getList().size() == 0) {
				showEmptyView();
			} else {
				removeEmptyView();
				mItems.addAll(mCommentModelList.getList());
				mAdapter.notifyDataSetChanged();
			}
		} else {
			showErrorView();
		}
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		if (mFragmentActivity instanceof BlogDetailActivity) {
			return ((BlogDetailActivity) mFragmentActivity)
					.getBlogDetailFragment().onOptionsItemSelected(item);
		}
		return false;
	}
	
	private void showDeleteCommentDialog(final int commentId) {
		AlertDialog.Builder builder = new AlertDialog.Builder(mFragmentActivity);

		final AlertDialog alertDialog = builder
				.setMessage(R.string.dialog_delete_comment_message)
				.setTitle(R.string.dialog_title)
				.setPositiveButton(R.string.dialog_ok,
						new DialogInterface.OnClickListener() {
							@Override
							public void onClick(DialogInterface dialog, int id) {
								mProgressDialog = ProgressDialog.show(
										mFragmentActivity, null,
										getString(R.string.progress_deleting), true,
										false);
								HttpUtil.deleteComment(mDeleteCommentListener, commentId);
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
