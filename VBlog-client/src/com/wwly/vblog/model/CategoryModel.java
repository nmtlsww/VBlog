package com.wwly.vblog.model;

import org.json.JSONObject;

import com.wwly.vblog.utils.StringUtil;

public class CategoryModel {

	public static final int ATTACH_NEW_CATEGORY_ID = -1;
	public static final int ATTACH_ALL_CATEGORY_ID = -2;
	
	private int id;
	private String categoryName;
	private String createTime;
	
	public CategoryModel() {
	}
	
	public CategoryModel(JSONObject object) {
		id = object.optInt("id");
		categoryName = object.optString("category_name");
		createTime = StringUtil.formatDjangoTime(object.optString("create_time"));
	}
	
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public String getCategoryName() {
		return categoryName;
	}
	public void setCategoryName(String categoryName) {
		this.categoryName = categoryName;
	}
	public String getCreateTime() {
		return createTime;
	}
	public void setCreateTime(String categoryCreateTime) {
		this.createTime = categoryCreateTime;
	}
	
	@Override
	public String toString() {
		return categoryName;
	}
}
