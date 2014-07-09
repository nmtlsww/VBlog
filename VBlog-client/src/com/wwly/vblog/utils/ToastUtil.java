package com.wwly.vblog.utils;

import android.content.Context;
import android.widget.Toast;

public class ToastUtil {
	public static void showText(Context ctx, String text) {
		Toast.makeText(ctx, text, Toast.LENGTH_SHORT).show();
	}

	public static void showText(Context ctx, int resId) {
		Toast.makeText(ctx, resId, Toast.LENGTH_SHORT).show();
	}
}
