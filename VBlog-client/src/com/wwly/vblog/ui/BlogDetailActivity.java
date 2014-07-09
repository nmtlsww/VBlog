package com.wwly.vblog.ui;

import java.util.Locale;

import android.app.ActionBar;
import android.app.FragmentTransaction;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.Menu;
import android.view.MenuItem;

import com.wwly.vblog.BaseFragment;
import com.wwly.vblog.R;

public class BlogDetailActivity extends FragmentActivity implements
		ActionBar.TabListener {
	public static final String INTENT_BUNDLE_ID = "id";

	private SectionsPagerAdapter mSectionsPagerAdapter;
	private ViewPager mViewPager;
	private int mId = -1;
	
	private BlogDetailFragment mBlogDetailFragment = new BlogDetailFragment();
	private BlogCommentListFragment mBlogCommentListFragment = new BlogCommentListFragment();

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_blog_detail);

		final ActionBar actionBar = getActionBar();
		actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_TABS);
		actionBar.setDisplayHomeAsUpEnabled(true);
		mSectionsPagerAdapter = new SectionsPagerAdapter(
				getSupportFragmentManager());

		mViewPager = (ViewPager) findViewById(R.id.pager);
		mViewPager.setAdapter(mSectionsPagerAdapter);

		mViewPager
				.setOnPageChangeListener(new ViewPager.SimpleOnPageChangeListener() {
					@Override
					public void onPageSelected(int position) {
						actionBar.setSelectedNavigationItem(position);
					}
				});

		for (int i = 0; i < mSectionsPagerAdapter.getCount(); i++) {
			actionBar.addTab(actionBar.newTab()
					.setText(mSectionsPagerAdapter.getPageTitle(i))
					.setTabListener(this));
		}

		Intent intent = getIntent();
		if (intent != null) {
			Bundle bundle = intent.getExtras();
			if (bundle != null) {
				mId = bundle.getInt(INTENT_BUNDLE_ID, -1);
			}
		}
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.menu_blog_detail, menu);
		return true;
	}

	@Override
	public void onTabSelected(ActionBar.Tab tab,
			FragmentTransaction fragmentTransaction) {
		mViewPager.setCurrentItem(tab.getPosition());
	}

	@Override
	public void onTabUnselected(ActionBar.Tab tab,
			FragmentTransaction fragmentTransaction) {
	}

	@Override
	public void onTabReselected(ActionBar.Tab tab,
			FragmentTransaction fragmentTransaction) {
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case android.R.id.home:
			onBackPressed();
			return true;
		}
		return false;
	}

	public class SectionsPagerAdapter extends FragmentPagerAdapter {

		public SectionsPagerAdapter(FragmentManager fm) {
			super(fm);
		}

		@Override
		public Fragment getItem(int position) {
			BaseFragment fragment = null;
			switch (position) {
			case 0:
				fragment = mBlogDetailFragment;
				break;
			case 1:
				fragment = mBlogCommentListFragment;
				break;
			default:
				break;
			}
			if (fragment != null) {
				fragment.setId(mId);
			}
			return fragment;
		}

		@Override
		public int getCount() {
			return 2;
		}

		@Override
		public CharSequence getPageTitle(int position) {
			Locale l = Locale.getDefault();
			switch (position) {
			case 0:
				return getString(R.string.fragment_blog_detail_subtitle_detail)
						.toUpperCase(l);
			case 1:
				return getString(R.string.fragment_blog_detail_subtitle_comment)
						.toUpperCase(l);
			}
			return null;
		}
	}
	
	@Override
    public void onBackPressed() {
        finish();
        overridePendingTransition(R.anim.in_from_left, R.anim.out_to_right);
    }
	
	public BlogDetailFragment getBlogDetailFragment() {
		return mBlogDetailFragment;
	}
	
	public BlogCommentListFragment getBlogCommentListFragment() {
		return mBlogCommentListFragment;
	}

}
