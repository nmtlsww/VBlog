package com.wwly.vblog.utils;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import org.apache.http.Header;
import org.json.JSONObject;

import com.android.volley.AuthFailureError;
import com.android.volley.NetworkResponse;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.Response.ErrorListener;
import com.android.volley.Response.Listener;
import com.android.volley.VolleyError;
import com.google.gson.Gson;
import com.wwly.vblog.CommConst;
import com.wwly.vblog.HttpRequestListener;
import com.wwly.vblog.VBlogApplication;
import com.wwly.vblog.model.CategoryModel;
import com.wwly.vblog.model.UserModel;

public class HttpUtil {
	private static final String REQUEST_PARAM_USERNAME = "username";
	private static final String REQUEST_PARAM_PASSWORD = "password";
	private static final String BLOG_LIST_PARAM_BEGIN = "begin";
	private static final String BLOG_LIST_PARAM_NUM = "num";
	private static final String BLOG_LIST_PARAM_CATEGORY_ID = "category_id";
	private static final String ADD_COMMENT_PARAM_BLOG_ID = "blog_id";
	private static final String ADD_COMMENT_PARAM_EMAIL = "email";
	private static final String ADD_COMMENT_PARAM_USERNAME = "username";
	private static final String ADD_COMMENT_PARAM_CONTENT = "content";
	private static final String ADD_BLOG_PARAM_TITLE = "title";
	private static final String ADD_BLOG_PARAM_USER_ID = "user_id";
	private static final String ADD_BLOG_PARAM_CONTENT = "content";
	private static final String ADD_BLOG_PARAM_CATEGORY_NAME = "category_name";
	private static final String ADD_BLOG_PARAM_CATEGORY_ID = "category_id";
	private static final String ADD_BLOG_PARAM_BLOG_ID = "blog_id";

	private static final String SET_COOKIE_KEY = "Set-Cookie";
	private static final String COOKIE_KEY = "Cookie";
	private static final String SESSION_COOKIE = "sessionid";

	public static String sessionId = "";

	public static void login(String username, String password,
			final HttpRequestListener httpRequestListener) {
		Map<String, String> params = new HashMap<String, String>();
		params.put(REQUEST_PARAM_USERNAME, username);
		params.put(REQUEST_PARAM_PASSWORD, password);
		httpPost(httpRequestListener, CommConst.URL.LOGIN_URL, params);
	}

	public static void logout(final HttpRequestListener httpRequestListener) {
		httpGet(httpRequestListener, CommConst.URL.LOGOUT_URL);
	}

	public static void getBlogList(
			final HttpRequestListener httpRequestListener, int begin, int count) {
		Map<String, String> params = new HashMap<String, String>();
		params.put(BLOG_LIST_PARAM_BEGIN, begin + "");
		params.put(BLOG_LIST_PARAM_NUM, count + "");
		httpPost(httpRequestListener, CommConst.URL.BLOG_LIST_URL, params);
	}
	
	public static void getBlogListByCategoryId(
			final HttpRequestListener httpRequestListener, int begin, int count, int categoryId) {
		Map<String, String> params = new HashMap<String, String>();
		params.put(BLOG_LIST_PARAM_BEGIN, begin + "");
		params.put(BLOG_LIST_PARAM_NUM, count + "");
		params.put(BLOG_LIST_PARAM_CATEGORY_ID, categoryId + "");
		httpPost(httpRequestListener, CommConst.URL.BLOG_LIST_URL, params);
	}

	public static void getBlogDetail(
			final HttpRequestListener httpRequestListener, int id) {
		httpGet(httpRequestListener,
				String.format(CommConst.URL.BLOG_DETAIL_URL, id));
	}

	public static void deleteBlog(
			final HttpRequestListener httpRequestListener, int id) {
		httpGet(httpRequestListener,
				String.format(CommConst.URL.BLOG_DELETE_URL, id));
	}

	public static void deleteComment(
			final HttpRequestListener httpRequestListener, int id) {
		httpGet(httpRequestListener,
				String.format(CommConst.URL.COMMENT_DELETE_URL, id));
	}

	public static void getCommentList(
			final HttpRequestListener httpRequestListener, int id) {
		httpGet(httpRequestListener,
				String.format(CommConst.URL.COMMENT_LIST_URL, id));
	}

	public static void getCategotyList(
			final HttpRequestListener httpRequestListener) {
		httpGet(httpRequestListener, CommConst.URL.CATEGORY_LIST_URL);
	}

	public static void addComment(
			final HttpRequestListener httpRequestListener, int blogId,
			String username, String email, String content) {
		Map<String, String> params = new HashMap<String, String>();
		params.put(ADD_COMMENT_PARAM_BLOG_ID, blogId + "");
		params.put(ADD_COMMENT_PARAM_CONTENT, content);
		params.put(ADD_COMMENT_PARAM_EMAIL, email);
		params.put(ADD_COMMENT_PARAM_USERNAME, username);
		httpPost(httpRequestListener, CommConst.URL.ADD_COMMENT_URL, params);
	}

	public static void addBlog(final HttpRequestListener httpRequestListener,
			int userId, String title, String content, int categoryId,
			String categoryName) {
		Map<String, String> params = new HashMap<String, String>();
		params.put(ADD_BLOG_PARAM_TITLE, title);
		params.put(ADD_BLOG_PARAM_CONTENT, content);
		params.put(ADD_BLOG_PARAM_USER_ID, userId + "");
		if (categoryId == CategoryModel.ATTACH_NEW_CATEGORY_ID) {
			params.put(ADD_BLOG_PARAM_CATEGORY_NAME, categoryName);
		} else {
			params.put(ADD_BLOG_PARAM_CATEGORY_ID, categoryId + "");
		}

		httpPost(httpRequestListener, CommConst.URL.ADD_BLOG_URL, params);
	}

	public static void updateBlog(
			final HttpRequestListener httpRequestListener, int blogId,
			int userId, String title, String content, int categoryId,
			String categoryName) {
		Map<String, String> params = new HashMap<String, String>();
		params.put(ADD_BLOG_PARAM_BLOG_ID, blogId + "");
		params.put(ADD_BLOG_PARAM_TITLE, title);
		params.put(ADD_BLOG_PARAM_CONTENT, content);
		params.put(ADD_BLOG_PARAM_USER_ID, userId + "");
		if (categoryId == CategoryModel.ATTACH_NEW_CATEGORY_ID) {
			params.put(ADD_BLOG_PARAM_CATEGORY_NAME, categoryName);
		} else {
			params.put(ADD_BLOG_PARAM_CATEGORY_ID, categoryId + "");
		}

		httpPost(httpRequestListener, CommConst.URL.UPDATE_BLOG_URL, params);
	}

	public static class StringRequest extends
			com.android.volley.toolbox.StringRequest {

		private final Map<String, String> mParams;

		public StringRequest(int method, String url, Listener<String> listener,
				ErrorListener errorListener) {
			super(method, url, listener, errorListener);
			mParams = null;
		}

		public StringRequest(int method, String url,
				Map<String, String> params, Listener<String> listener,
				ErrorListener errorListener) {
			super(method, url, listener, errorListener);
			mParams = params;
		}

		@Override
		protected Map<String, String> getParams() {
			return mParams;
		}

		@Override
		protected Response<String> parseNetworkResponse(NetworkResponse response) {
			if (mParams != null) {
				if (mParams.containsKey(REQUEST_PARAM_USERNAME)
						&& mParams.containsKey(REQUEST_PARAM_PASSWORD)) {
					getSessionCookie(response.apacheHeaders);
				}
			}
			return super.parseNetworkResponse(response);
		}

		@Override
		public Map<String, String> getHeaders() throws AuthFailureError {
			Map<String, String> headers = super.getHeaders();
			if (headers == null || headers.equals(Collections.emptyMap())) {
				headers = new HashMap<String, String>();
			}
			addSessionCookie(headers);

			return headers;
		}

		@Override
		protected void deliverResponse(String response) {
			super.deliverResponse(response);
		}

	}

	private static void getSessionCookie(Header[] headers) {
		for (int i = 0; i < headers.length; i++) {
			String key = headers[i].getName();
			String value = headers[i].getValue();
			if (key.contains(SET_COOKIE_KEY)
					&& value.startsWith(SESSION_COOKIE)) {
				String[] splitCookie = value.split(";");
				String[] splitSessionId = splitCookie[0].split("=");
				value = splitSessionId[1];
				sessionId = value;
				return;
			}
		}
	}

	private static void addSessionCookie(Map<String, String> headers) {
		String userJson = PrefAccessor.getInstance(
				VBlogApplication.getInstance()).getUserModel();
		if (!StringUtil.isEmpty(userJson)) {
			String session = new Gson().fromJson(userJson, UserModel.class)
					.getSessionId();
			if (session.length() > 0) {
				StringBuilder builder = new StringBuilder();
				builder.append(SESSION_COOKIE);
				builder.append("=");
				builder.append(session);
				if (headers.containsKey(COOKIE_KEY)) {
					builder.append("; ");
					builder.append(headers.get(COOKIE_KEY));
				}
				headers.put(COOKIE_KEY, builder.toString());
			}
		}
	}

	private static void httpGet(final HttpRequestListener httpRequestListener,
			String url) {
		StringRequest request = new StringRequest(Request.Method.GET, url,
				new Response.Listener<String>() {
					@Override
					public void onResponse(String result) {
						handleRequestResult(httpRequestListener, result);
					}
				}, new Response.ErrorListener() {
					@Override
					public void onErrorResponse(VolleyError error) {
						httpRequestListener.onNetworkError(error);
					}
				});
		VBlogApplication.getInstance().getRequestQueue().add(request);
	}

	private static void httpPost(final HttpRequestListener httpRequestListener,
			String url, Map<String, String> params) {
		StringRequest request = new StringRequest(Request.Method.POST, url,
				params, new Response.Listener<String>() {
					@Override
					public void onResponse(String result) {
						handleRequestResult(httpRequestListener, result);
					}
				}, new Response.ErrorListener() {
					@Override
					public void onErrorResponse(VolleyError error) {
						httpRequestListener.onNetworkError(error);
					}
				});
		VBlogApplication.getInstance().getRequestQueue().add(request);
	}

	private static void handleRequestResult(
			HttpRequestListener httpRequestListener, String result) {
		try {
			JSONObject json = new JSONObject(result);
			String status = json.optString("status", "success");
			boolean isLogin = json.optBoolean("islogin", true);
			if ("fail".equals(status)) {
				if (!isLogin) {
					LogUtil.w("http request no login");
					PrefAccessor.getInstance(VBlogApplication.getInstance())
							.setUserModel("");
					VBlogApplication.getInstance().setUserModel(null);
					httpRequestListener.onNoLogin();
				} else {
					LogUtil.w("http request fail : " + result);
					httpRequestListener.onFail(result);
				}
			} else {
				httpRequestListener.onSuccess(result);
			}
		} catch (Exception e) {
			LogUtil.w("http request fail : " + result);
			httpRequestListener.onFail(result);
		}
	}
}
