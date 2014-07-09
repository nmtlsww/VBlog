package com.wwly.vblog.utils;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.util.Locale;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.http.protocol.HTTP;
import org.apache.http.util.EncodingUtils;

public class StringUtil {

	public static String toGbk(String string) {
		String gbk = null;
		try {
			gbk = URLEncoder.encode(string, "GBK");
		} catch (UnsupportedEncodingException e) {
		}
		return gbk;
	}

	public static String utf8Togb2312(String str) {
		StringBuffer sb = new StringBuffer();
		for (int i = 0; i < str.length(); i++) {
			char c = str.charAt(i);
			switch (c) {
			case '+':
				sb.append(' ');
				break;
			case '%':
				try {
					sb.append((char) Integer.parseInt(
							str.substring(i + 1, i + 3), 16));
				} catch (NumberFormatException e) {
					throw new IllegalArgumentException();
				}
				i += 2;
				break;
			default:
				sb.append(c);
				break;
			}
		}
		// Undo conversion to external encoding
		String result = sb.toString();
		String res = null;
		try {
			byte[] inputBytes = result.getBytes("8859_1");
			res = new String(inputBytes, "UTF-8");
		} catch (Exception e) {
		}
		return res;
	}

	public static boolean isEmpty(String paramString) {
		if ((paramString == null) || ("".equals(paramString))) {
			return true;
		}
		return false;
	}

	public static boolean isEmptyArray(Object[] obj) {
		return isEmptyArray(obj, 1);
	}

	public static boolean isEmptyArray(Object[] array, int paramInt) {
		if ((array == null) || (array.length < paramInt)) {
			return true;
		}
		return false;
	}

	public static boolean isNumeric(String str) {
		final String number = "0123456789";
		for (int i = 0; i < str.length(); i++) {
			if (number.indexOf(str.charAt(i)) == -1) {
				return false;
			}
		}
		return true;
	}
	
	public static boolean isEmail(String str) {
		Pattern pattern = Pattern.compile("[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+",
				Pattern.CASE_INSENSITIVE);
		Matcher matcher = pattern.matcher(str);
		if (matcher.matches()) {
			return true;
		} else {
			return false;
		}
	}

	public static String formatSpeed(int value) {
		return formatSize(value) + "/s";
	}

	public static String formatSize(long value) {

		double k = (double) value / 1024;
		if (k == 0) {
			return String.format(Locale.CHINA, "%d B", value);
		}

		double m = k / 1024;
		if (m < 1) {
			return String.format(Locale.CHINA, "%.2f K", k);
		}

		double g = m / 1024;
		if (g < 1) {
			return String.format(Locale.CHINA, "%.2f M", m);
		}

		return String.format(Locale.CHINA, "%.2f G", g);
	}

	public static String formatTime(int second) {

		int hh = second / 3600;
		int mm = second % 3600 / 60;
		int ss = second % 60;

		if (0 != hh) {
			return String.format(Locale.CHINA, "%02d:%02d:%02d", hh, mm, ss);
		} else {
			return String.format(Locale.CHINA, "%02d:%02d", mm, ss);
		}
	}
	
	public static String formatDjangoTime(String time) {
		String formatTime = time.replace("T", " ").replace("Z", "");
		return formatTime;
	}

	public static String formatPercent(long divisor, long dividend) {
		if (dividend == 0) {
			return null;
		}

		double ret = (double) (divisor * 100) / dividend;
		return String.format(Locale.CHINA, "%.2f%%", ret);
	}

	public static String encode(String url) {
		if (StringUtil.isEmpty(url)) {
			return "";
		}
		String str = new String();
		try {
			str = URLEncoder.encode(url, HTTP.UTF_8);
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		return str;
	}

	public static String decode(String url) {
		String str = new String();
		try {
			str = URLDecoder.decode(url, HTTP.UTF_8);
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		return str;
	}

	public static String readFileSdcard(File file) throws Exception {
		String res = "";
		FileInputStream fin = new FileInputStream(file);
		int length = fin.available();
		byte[] buffer = new byte[length];
		fin.read(buffer);
		res = EncodingUtils.getString(buffer, "UTF-8");
		fin.close();
		return res;
	}

	public static void writeFileSdcard(File file, String message)
			throws Exception {
		FileOutputStream fout = new FileOutputStream(file);
		byte[] bytes = message.getBytes("UTF-8");
		fout.write(bytes);
		fout.flush();
		fout.close();
	}

}
