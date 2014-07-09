package com.wwly.vblog.utils;

import android.content.Context;
import android.content.pm.PackageInfo;
import android.os.Vibrator;

public class SystemUtil {
	
	public static String getAppVersion(Context context) {
		try {
			PackageInfo packageInfo = context.getPackageManager()
					.getPackageInfo(context.getPackageName(), 0);
			return packageInfo.versionName;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return "1.0.0";
	}
	
	public static void LongClickVibrator(Context context) {
		Vibrator vibrator = (Vibrator) context.getSystemService(Context.VIBRATOR_SERVICE);
		vibrator.vibrate(50);
	}
}
