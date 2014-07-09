package com.wwly.vblog.utils;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.content.SharedPreferences.OnSharedPreferenceChangeListener;

public class PrefAccessor implements OnSharedPreferenceChangeListener {
	private static final String APP_PREF_NAME = "VBlog";
	private static PrefAccessor mPrefAccessor = null;
	private final Context mContext;
	private final SharedPreferences mSharedPreferences;

	private final static String SP_USER_MODEL = "user_model";

	synchronized public static PrefAccessor getInstance(Context context) {
		if (mPrefAccessor == null) {
			mPrefAccessor = new PrefAccessor(context);
		}
		return mPrefAccessor;
	}

	private PrefAccessor(Context context) {
		mContext = context;
		mSharedPreferences = mContext.getSharedPreferences(APP_PREF_NAME,
				Context.MODE_PRIVATE);
	}

	public String getUserModel() {
		String sessionId = mSharedPreferences.getString(SP_USER_MODEL, "");
		return sessionId;
	}

	public void setUserModel(String user) {
		Editor editor = mSharedPreferences.edit();
		editor.putString(SP_USER_MODEL, user);
		editor.commit();
	}

	@Override
	public void onSharedPreferenceChanged(SharedPreferences sharedPreferences,
			String key) {
	}
}
