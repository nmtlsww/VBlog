package com.wwly.vblog;

import com.wwly.vblog.utils.LogUtil;

public class CommConst {

	public static boolean isDebug = false;

	static {
		LogUtil.setLoggable(isDebug);
		LogUtil.setIsPrintAssist(isDebug);
	}

	public class URL {

		public static final String BASE_URL = "http://vwebapi.duapp.com/"; // your domain
		public static final String BLOG_LIST_URL = BASE_URL + "blog/list/";
		public static final String LOGIN_URL = BASE_URL + "login/";
		public static final String LOGOUT_URL = BASE_URL + "logout/";
		public static final String BLOG_DETAIL_URL = BASE_URL + "blog/%1$d/";
		public static final String BLOG_DELETE_URL = BASE_URL
				+ "del/blog/%1$d/";
		public static final String COMMENT_DELETE_URL = BASE_URL
				+ "del/comment/%1$d/";
		public static final String COMMENT_LIST_URL = BASE_URL
				+ "comment/list/%1$d/";
		public static final String ADD_COMMENT_URL = BASE_URL + "add/comment/";
		public static final String CATEGORY_LIST_URL = BASE_URL
				+ "category/list/";
		public static final String ADD_BLOG_URL = BASE_URL + "add/blog/";
		public static final String UPDATE_BLOG_URL = BASE_URL + "update/blog/";
	}

	protected CommConst() {
	}

}
