package com.wwly.vblog;

import java.lang.ref.WeakReference;

import android.app.Application;

import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;
import com.google.gson.Gson;
import com.wwly.vblog.model.UserModel;
import com.wwly.vblog.utils.PrefAccessor;

public class VBlogApplication extends Application {
	
	private static WeakReference<VBlogApplication> mInstance = null;
	private RequestQueue mRequestQueue;
	private UserModel mUserModel;

	private boolean mNeedRefreshBlogList = false;
	private boolean mNeedRefreshCommentList = false;
	private boolean mNeedRefreshBlogDetail = false;
	
	public static VBlogApplication getInstance() {
		return ((null != mInstance) ? mInstance.get() : null);
	}

	public VBlogApplication() {
		super();
		mInstance = new WeakReference<VBlogApplication>(this);
	}
	
	@Override
    public void onCreate() {
        super.onCreate();
        mRequestQueue = Volley.newRequestQueue(this);
        mUserModel = new Gson().fromJson(PrefAccessor.getInstance(this).getUserModel(), UserModel.class);
	}
	
	public RequestQueue getRequestQueue()
	{
		return mRequestQueue ;
	}
	
	public UserModel getUserModel() {
		return mUserModel;
	}
	
	public void setUserModel(UserModel userModel) {
		mUserModel= userModel;
	}

	public boolean isNeedRefreshBlogList() {
		return mNeedRefreshBlogList;
	}

	public void setNeedRefreshBlogList(boolean needRefreshBlogList) {
		this.mNeedRefreshBlogList = needRefreshBlogList;
	}

	public boolean isNeedRefreshCommentList() {
		return mNeedRefreshCommentList;
	}

	public void setNeedRefreshCommentList(boolean needRefreshCommentList) {
		this.mNeedRefreshCommentList = needRefreshCommentList;
	}
	
	public boolean isNeedRefreshBlogDetail() {
		return mNeedRefreshBlogDetail;
	}

	public void setNeedRefreshBlogDetail(boolean needRefreshBlogDetail) {
		this.mNeedRefreshBlogDetail = needRefreshBlogDetail;
	}
	
}
