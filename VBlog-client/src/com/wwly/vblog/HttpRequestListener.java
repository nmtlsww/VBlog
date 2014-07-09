package com.wwly.vblog;

import com.android.volley.VolleyError;

public interface HttpRequestListener {
	public void onSuccess(String result);
	public void onNetworkError(VolleyError error);
	public void onFail(String result);
	public void onNoLogin();
}
