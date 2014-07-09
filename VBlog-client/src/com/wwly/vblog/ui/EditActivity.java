package com.wwly.vblog.ui;

import android.app.ActionBar;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;

import com.wwly.vblog.BaseFragment;
import com.wwly.vblog.R;
import com.wwly.vblog.model.BlogModel;

public class EditActivity extends FragmentActivity {

	public static final String INTENT_BUNDLE_EDIT_TYPE = "edit_type";
	public static final String INTENT_BUNDLE_ADD_COMMENT_BLOG_ID = "blog_id";
	public static final String INTENT_BUNDLE_EDIT_BLOG_MODEL = "edit_blog_model";
	public static final int TYPE_ADD_BLOG = 0;
	public static final int TYPE_ADD_COMMENT = 1;

	private EditBlogFragment mEditBlogFragment = new EditBlogFragment();
	private EditCommentFragment mEditCommentFragment = new EditCommentFragment();
	private BaseFragment mCurrentFragment;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_public_layout);

		final ActionBar actionBar = getActionBar();
		actionBar.setDisplayHomeAsUpEnabled(true);

		Intent intent = getIntent();
		if (intent != null) {
			Bundle bundle = intent.getExtras();
			if (bundle != null) {
				int type = bundle
						.getInt(INTENT_BUNDLE_EDIT_TYPE, TYPE_ADD_BLOG);
				switch (type) {
				case TYPE_ADD_BLOG:
					if (bundle.getSerializable(INTENT_BUNDLE_EDIT_BLOG_MODEL) != null) {
						BlogModel blog = (BlogModel) bundle
								.getSerializable(INTENT_BUNDLE_EDIT_BLOG_MODEL);
						mEditBlogFragment.setEditBlog(blog);
						mEditBlogFragment.setFragmentTitle(getString(R.string.fragment_title_edit_blog));
					} else {
						mEditBlogFragment.setFragmentTitle(getString(R.string.fragment_title_add_blog));
					}
					mCurrentFragment = mEditBlogFragment;
					break;
				case TYPE_ADD_COMMENT:
					int id = bundle.getInt(INTENT_BUNDLE_ADD_COMMENT_BLOG_ID,
							-1);
					if (id != -1) {
						mEditCommentFragment.setId(id);
					}
					mEditCommentFragment.setFragmentTitle(getString(R.string.fragment_title_add_comment));
					mCurrentFragment = mEditCommentFragment;
					break;
				}
				FragmentManager fragmentManager = getSupportFragmentManager();
				fragmentManager.beginTransaction()
						.replace(R.id.content_frame, mCurrentFragment).commit();
				return;
			}
		}

		onBackPressed();
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.menu_add_comment, menu);
		return true;
	}

	@Override
	public void onBackPressed() {
		finish();
		overridePendingTransition(R.anim.in_from_left, R.anim.out_to_right);
	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK) {
			showQuitDialog();
			return true;
		}
		return super.onKeyDown(keyCode, event);
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case android.R.id.home:
			showQuitDialog();
			return true;
		}
		return false;
	}

	private void showQuitDialog() {
		AlertDialog.Builder builder = new AlertDialog.Builder(this);

		final AlertDialog alertDialog = builder
				.setMessage(R.string.dialog_quit_edit_message)
				.setTitle(R.string.dialog_title)
				.setPositiveButton(R.string.dialog_ok,
						new DialogInterface.OnClickListener() {
							@Override
							public void onClick(DialogInterface dialog, int id) {
								onBackPressed();
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
