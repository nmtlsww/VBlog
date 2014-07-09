package com.wwly.vblog;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONObject;

import android.app.ActionBar;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.support.v4.app.ActionBarDrawerToggle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.android.volley.VolleyError;
import com.google.gson.Gson;
import com.wwly.vblog.adapter.DrawerListAdapter;
import com.wwly.vblog.model.DrawerListModel;
import com.wwly.vblog.model.UserModel;
import com.wwly.vblog.ui.BlogListFragment;
import com.wwly.vblog.ui.CategoryListFragment;
import com.wwly.vblog.ui.EditActivity;
import com.wwly.vblog.utils.HandlerUtil;
import com.wwly.vblog.utils.HttpUtil;
import com.wwly.vblog.utils.PrefAccessor;
import com.wwly.vblog.utils.StringUtil;
import com.wwly.vblog.utils.SystemUtil;
import com.wwly.vblog.utils.ToastUtil;

public class MainActivity extends FragmentActivity {

	private DrawerLayout mDrawerLayout;
	private ListView mDrawerList;
	private DrawerListAdapter mDrawerListAdapter;
	private ActionBar mActionBar;
	private ActionBarDrawerToggle mDrawerToggle;
	private List<DrawerListModel> mDrawerListModelList = new ArrayList<DrawerListModel>();
	private ProgressDialog mProgressDialog;

	private View mHeaderUserView;
	private View mHeaderLoginView;
	private boolean mIsLogin;
	private int mSelectedPosition;

	private HttpRequestListener mLoginListener = new HttpRequestListener() {

		@Override
		public void onSuccess(String result) {
			if (mProgressDialog != null) {
				mProgressDialog.dismiss();
			}
			ToastUtil.showText(MainActivity.this, R.string.toast_login_success);

			try {
				if (!StringUtil.isEmpty(HttpUtil.sessionId)) {
					JSONObject json = new JSONObject(result);
					UserModel user = new UserModel(json);
					user.setSessionId(HttpUtil.sessionId);
					PrefAccessor.getInstance(MainActivity.this).setUserModel(
							new Gson().toJson(user));
					VBlogApplication.getInstance().setUserModel(user);

					mIsLogin = true;
					updateHeaderView();
				}
			} catch (Exception e) {
				e.printStackTrace();
			}

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
			ToastUtil.showText(MainActivity.this, R.string.toast_login_fail);
		}

		@Override
		public void onNetworkError(VolleyError error) {
			if (mProgressDialog != null) {
				mProgressDialog.dismiss();
			}
			ToastUtil.showText(MainActivity.this, R.string.http_error_network);
		}

	};

	private HttpRequestListener mLogoutListener = new HttpRequestListener() {

		@Override
		public void onSuccess(String result) {
			if (mProgressDialog != null) {
				mProgressDialog.dismiss();
			}
			ToastUtil
					.showText(MainActivity.this, R.string.toast_logout_success);

			HttpUtil.sessionId = "";
			VBlogApplication.getInstance().setUserModel(null);
			PrefAccessor.getInstance(MainActivity.this).setUserModel("");
			mIsLogin = false;
			updateHeaderView();
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
			ToastUtil.showText(MainActivity.this, R.string.toast_logout_fail);
		}

		@Override
		public void onNetworkError(VolleyError error) {
			if (mProgressDialog != null) {
				mProgressDialog.dismiss();
			}
			ToastUtil.showText(MainActivity.this, R.string.http_error_network);
		}
	};

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		if (VBlogApplication.getInstance().getUserModel() != null) {
			mIsLogin = true;
		}

		mActionBar = getActionBar();
		mActionBar.setDisplayHomeAsUpEnabled(true);
		mActionBar.setHomeButtonEnabled(true);

		mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
		mDrawerList = (ListView) findViewById(R.id.left_drawer);

		mDrawerListAdapter = new DrawerListAdapter(this, mDrawerListModelList);
		updateHeaderView();
		initDefaultDrawerItems();
		mDrawerList.setAdapter(mDrawerListAdapter);
		mDrawerList.setOnItemClickListener(new DrawerItemClickListener());

		mDrawerLayout.setDrawerShadow(R.drawable.drawer_shadow,
				GravityCompat.START);
		mDrawerToggle = new ActionBarDrawerToggle(this, mDrawerLayout,
				R.drawable.ic_drawer, R.string.drawer_open,
				R.string.drawer_close) {
			public void onDrawerClosed(View view) {
				getActionBar().setTitle(getTitle());
				invalidateOptionsMenu();
			}

			public void onDrawerOpened(View drawerView) {
				getActionBar().setTitle(getString(R.string.app_name));
				invalidateOptionsMenu();
			}
		};
		mDrawerLayout.setDrawerListener(mDrawerToggle);

		if (savedInstanceState == null) {
			selectItem(1);
		}

	}

	private void updateHeaderView() {
		UserModel userModel = VBlogApplication.getInstance().getUserModel();
		if (mIsLogin && userModel != null) {
			mHeaderUserView = getLayoutInflater().inflate(
					R.layout.drawer_list_header_user, null);
			((TextView) mHeaderUserView.findViewById(R.id.header_user_username))
					.setText(userModel.getUsername());
			((ImageView) mHeaderUserView
					.findViewById(R.id.header_user_portrait))
					.setImageResource(R.drawable.user_portrait);
			mDrawerList.removeHeaderView(mHeaderLoginView);
			mDrawerList.addHeaderView(mHeaderUserView);
		} else {
			mHeaderLoginView = getLayoutInflater().inflate(
					R.layout.drawer_list_header_login, null);
			mDrawerList.removeHeaderView(mHeaderUserView);
			mDrawerList.addHeaderView(mHeaderLoginView);
		}
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.menu_main, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		if (mDrawerToggle.onOptionsItemSelected(item)) {
			return true;
		}
		switch (item.getItemId()) {
		case R.id.action_add:
			showAddBlogFragment();
			return true;
		case R.id.action_about:
			showAboutDialog();
			return true;
		default:
			return super.onOptionsItemSelected(item);
		}
	}

	@Override
	protected void onPostCreate(Bundle savedInstanceState) {
		super.onPostCreate(savedInstanceState);
		mDrawerToggle.syncState();
	}

	@Override
	public void onConfigurationChanged(Configuration newConfig) {
		super.onConfigurationChanged(newConfig);
		mDrawerToggle.onConfigurationChanged(newConfig);
	}

	private void initDefaultDrawerItems() {
		if (mDrawerListModelList.size() > 0)
			mDrawerListModelList.clear();
		mDrawerListModelList.add(new DrawerListModel(
				getString(R.string.drawer_item_list),
				R.drawable.drawer_list_item_list, DrawerListModel.TAG_LIST));
		mDrawerListModelList.add(new DrawerListModel(
				getString(R.string.drawer_item_category),
				R.drawable.drawer_list_item_category,
				DrawerListModel.TAG_CATEGORY));
		mDrawerListModelList.add(new DrawerListModel(
				getString(R.string.drawer_item_add),
				R.drawable.drawer_list_item_add, DrawerListModel.TAG_ADD));
		mDrawerListModelList
				.add(new DrawerListModel(getString(R.string.drawer_item_about),
						R.drawable.drawer_list_item_settings,
						DrawerListModel.TAG_ABOUT));
	}

	private class DrawerItemClickListener implements
			ListView.OnItemClickListener {
		@Override
		public void onItemClick(AdapterView<?> parent, View view, int position,
				long id) {
			selectItem(position);
		}
	}

	private void selectItem(int position) {
		if (position <= 0) {
			HandlerUtil.postOnUiThread(new Runnable() {
				@Override
				public void run() {
					if (mIsLogin) {
						showLogoutDialog();
					} else {
						showLoginDialog();
					}
				}
			}, 100);
		} else {
			BaseFragment fragment = null;
			int tag = mDrawerListModelList.get(position - 1).getTag();
			String title = mDrawerListModelList.get(position - 1).getName();

			switch (tag) {
			case DrawerListModel.TAG_LIST:
				fragment = new BlogListFragment();
				fragment.setFragmentTitle(title);
				mSelectedPosition = position;
				break;
			case DrawerListModel.TAG_CATEGORY:
				fragment = new CategoryListFragment();
				fragment.setFragmentTitle(title);
				mSelectedPosition = position;
				break;
			case DrawerListModel.TAG_ADD:
				showAddBlogFragment();
				break;
			case DrawerListModel.TAG_ABOUT:
				HandlerUtil.postOnUiThread(new Runnable() {
					@Override
					public void run() {
						showAboutDialog();
					}
				}, 100);
				break;
			default:
				break;
			}

			if (fragment != null) {
				FragmentManager fragmentManager = getSupportFragmentManager();
				fragmentManager.beginTransaction()
						.replace(R.id.content_frame, fragment).commit();
			}
		}
		mDrawerList.setItemChecked(mSelectedPosition, true);
		mDrawerLayout.closeDrawer(mDrawerList);
	}

	public void selectedDrawerListItem(int tag) {
		for (int i = 0; i < mDrawerListModelList.size(); i++) {
			if (mDrawerListModelList.get(i).getTag() == tag) {
				mSelectedPosition = i + 1;
				mDrawerList.setItemChecked(mSelectedPosition, true);
				return;
			}
		}
	}

	private long mLastBackTime;

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK) {
			if ((System.currentTimeMillis() - mLastBackTime) > 2000) {
				ToastUtil.showText(this, getString(R.string.toast_activity_main_exit));
				mLastBackTime = System.currentTimeMillis();
				return false;
			} else {
				return super.onKeyDown(keyCode, event);
			}
		}
		return super.onKeyDown(keyCode, event);
	}

	private void showAddBlogFragment() {
		Intent intent = new Intent(this, EditActivity.class);
		Bundle bundle = new Bundle();
		bundle.putInt(EditActivity.INTENT_BUNDLE_EDIT_TYPE,
				EditActivity.TYPE_ADD_BLOG);
		intent.putExtras(bundle);
		startActivity(intent);
		overridePendingTransition(R.anim.in_from_right, R.anim.out_to_left);
	}

	private void showLoginDialog() {
		AlertDialog.Builder builder = new AlertDialog.Builder(this);

		final View dialogView = getLayoutInflater().inflate(
				R.layout.dialog_login, null);
		final EditText usernameText = (EditText) dialogView
				.findViewById(R.id.username);
		final EditText passwordText = (EditText) dialogView
				.findViewById(R.id.password);

		final AlertDialog alertDialog = builder
				.setView(dialogView)
				.setTitle(R.string.dialog_login_title)
				.setPositiveButton(R.string.dialog_login_button_positive,
						new DialogInterface.OnClickListener() {
							@Override
							public void onClick(DialogInterface dialog, int id) {
								mProgressDialog = ProgressDialog.show(
										MainActivity.this, null,
										getString(R.string.progress_logining),
										true, false);
								HttpUtil.login(usernameText.getText()
										.toString(), passwordText.getText()
										.toString(), mLoginListener);
							}
						})
				.setNegativeButton(R.string.dialog_login_button_negative,
						new DialogInterface.OnClickListener() {
							public void onClick(DialogInterface dialog, int id) {
							}
						}).create();
		alertDialog.show();

		final Button positiveButton = alertDialog
				.getButton(AlertDialog.BUTTON_POSITIVE);
		positiveButton.setEnabled(false);

		usernameText.addTextChangedListener(new TextWatcher() {
			@Override
			public void onTextChanged(CharSequence s, int start, int before,
					int count) {
				if (s.toString().trim().length() > 0
						&& passwordText.getText().toString().length() > 0) {
					if (!positiveButton.isEnabled()) {
						positiveButton.setEnabled(true);
					}
				} else {
					if (positiveButton.isEnabled()) {
						positiveButton.setEnabled(false);
					}
				}
			}

			@Override
			public void beforeTextChanged(CharSequence s, int start, int count,
					int after) {
			}

			@Override
			public void afterTextChanged(Editable s) {
			}
		});

		passwordText.addTextChangedListener(new TextWatcher() {
			@Override
			public void onTextChanged(CharSequence s, int start, int before,
					int count) {
				if (s.toString().length() > 0
						&& usernameText.getText().toString().trim().length() > 0) {
					if (!positiveButton.isEnabled()) {
						positiveButton.setEnabled(true);
					}
				} else {
					if (positiveButton.isEnabled()) {
						positiveButton.setEnabled(false);
					}
				}
			}

			@Override
			public void beforeTextChanged(CharSequence s, int start, int count,
					int after) {
			}

			@Override
			public void afterTextChanged(Editable s) {
			}
		});
	}

	private void showLogoutDialog() {
		AlertDialog.Builder builder = new AlertDialog.Builder(this);

		final AlertDialog alertDialog = builder
				.setMessage(R.string.dialog_logout_message)
				.setTitle(R.string.dialog_title)
				.setPositiveButton(R.string.dialog_ok,
						new DialogInterface.OnClickListener() {
							@Override
							public void onClick(DialogInterface dialog, int id) {
								mProgressDialog = ProgressDialog.show(
										MainActivity.this, null,
										getString(R.string.progress_logouting),
										true, false);
								HttpUtil.logout(mLogoutListener);
							}
						})
				.setNegativeButton(R.string.dialog_cancel,
						new DialogInterface.OnClickListener() {
							public void onClick(DialogInterface dialog, int id) {
							}
						}).create();
		alertDialog.show();
	}

	private void showAboutDialog() {
		AlertDialog.Builder builder = new AlertDialog.Builder(this);

		final View dialogView = this.getLayoutInflater().inflate(
				R.layout.dialog_about, null);

		((TextView) dialogView.findViewById(R.id.dialog_about_version))
				.setText(SystemUtil.getAppVersion(this));

		final AlertDialog alertDialog = builder
				.setView(dialogView)
				.setTitle(R.string.action_main_about)
				.setPositiveButton(R.string.dialog_ok,
						new DialogInterface.OnClickListener() {
							public void onClick(DialogInterface dialog, int id) {
							}
						}).create();
		alertDialog.show();
	}

}
