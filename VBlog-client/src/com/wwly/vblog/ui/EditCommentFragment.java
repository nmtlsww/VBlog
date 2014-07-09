package com.wwly.vblog.ui;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import com.android.volley.VolleyError;
import com.wwly.vblog.BaseFragment;
import com.wwly.vblog.HttpRequestListener;
import com.wwly.vblog.R;
import com.wwly.vblog.VBlogApplication;
import com.wwly.vblog.utils.HttpUtil;
import com.wwly.vblog.utils.StringUtil;
import com.wwly.vblog.utils.ToastUtil;

public class EditCommentFragment extends BaseFragment {

	private EditText mUserNameEditText;
	private EditText mEmailEditText;
	private EditText mContentEditText;
	private ProgressDialog mProgressDialog;

	private HttpRequestListener mAddCommentListener = new HttpRequestListener() {

		@Override
		public void onSuccess(String result) {
			if (mProgressDialog != null) {
				mProgressDialog.dismiss();
			}
			ToastUtil.showText(mFragmentActivity, R.string.toast_add_comment_success);
			VBlogApplication.getInstance().setNeedRefreshCommentList(true);
			mFragmentActivity.onBackPressed();
		}

		@Override
		public void onNoLogin() {
			if (mProgressDialog != null) {
				mProgressDialog.dismiss();
			}
		}

		@Override
		public void onFail(String result) {
			if (mProgressDialog != null) {
				mProgressDialog.dismiss();
			}
			ToastUtil.showText(mFragmentActivity, R.string.http_fail_add_comment);
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
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		if (container == null)
			return null;

		if (mViewGroup == null) {
			setHasOptionsMenu(true);
			mContext = getActivity().getBaseContext();
			mViewGroup = (ViewGroup) inflater.inflate(
					R.layout.fragment_add_comment, null);
			setupView();
		}
		return super.onCreateView(inflater, container, savedInstanceState);
	}

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
		if (mId == -1) {
			mFragmentActivity.onBackPressed();
		}
	}

	public void setupView() {
		mUserNameEditText = (EditText) mViewGroup
				.findViewById(R.id.add_comment_username);
		mEmailEditText = (EditText) mViewGroup
				.findViewById(R.id.add_comment_email);
		mContentEditText = (EditText) mViewGroup
				.findViewById(R.id.add_comment_content);

		if (VBlogApplication.getInstance().getUserModel() != null) {
			mUserNameEditText.setText(VBlogApplication.getInstance()
					.getUserModel().getUsername());
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
			submitComment();
			return true;
		}
		return false;
	}

	private void submitComment() {
		String username = mUserNameEditText.getText().toString();
		String email = mEmailEditText.getText().toString();
		String content = mContentEditText.getText().toString();

		if (StringUtil.isEmpty(username) || StringUtil.isEmpty(content)) {
			ToastUtil.showText(mFragmentActivity,
					R.string.toast_add_comment_error_incomplete);
			return;
		}

		if (!StringUtil.isEmail(email) && !StringUtil.isEmpty(email)) {
			ToastUtil.showText(mFragmentActivity,
					R.string.toast_add_comment_error_email);
			return;
		}

		HttpUtil.addComment(mAddCommentListener, mId, username, email, content);
		mProgressDialog = ProgressDialog.show(mFragmentActivity, null,
				getString(R.string.progress_sending_date), true, false);
	}

}
