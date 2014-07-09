package com.wwly.vblog.model;

public class DrawerListModel {

	public static final int TAG_LOGIN = 0;
	public static final int TAG_LIST = 1;
	public static final int TAG_CATEGORY = 2;
	public static final int TAG_ADD = 3;
	public static final int TAG_SETTINGS = 4;
	public static final int TAG_LOGOUT = 5;
	public static final int TAG_ABOUT = 6;
	
	private String name = "";
	private int iconRes = 0;
	private int tag = 1;
	
	public DrawerListModel(String name, int iconRes, int tag) {
		this.name = name;
		this.iconRes = iconRes;
		this.tag = tag;
	}
	
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public int getIconRes() {
		return iconRes;
	}

	public void setIconRes(int iconRes) {
		this.iconRes = iconRes;
	}

	public int getTag() {
		return tag;
	}

	public void setTag(int tag) {
		this.tag = tag;
	}
}
