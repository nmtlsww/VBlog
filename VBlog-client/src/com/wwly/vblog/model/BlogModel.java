package com.wwly.vblog.model;

import java.io.Serializable;

import org.json.JSONObject;

import com.wwly.vblog.utils.StringUtil;

public class BlogModel implements Serializable{

	private static final long serialVersionUID = 8081616497689685089L;

	private int id = 0;
	private int userId = 0;
	private int categoryId = 0;
	private String title = "";
	private String username = "";
	private String content = "";
	private String categoryName = "";
	private String updateTime = "";
	private String createTime = "";
	
	public BlogModel() {
		
	}
	
	public BlogModel(JSONObject object) {
		id = object.optInt("id");
		userId = object.optInt("user_id");
		categoryId = object.optInt("category_id");
		title = object.optString("title");
		username = object.optString("username");
		categoryName = object.optString("category_name");
		content = object.optString("content");
		updateTime = StringUtil.formatDjangoTime(object.optString("update_time"));
		createTime = StringUtil.formatDjangoTime(object.optString("create_time"));
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public int getUserId() {
		return userId;
	}

	public void setUserId(int userId) {
		this.userId = userId;
	}

	public int getCategoryId() {
		return categoryId;
	}

	public void setCategoryId(int categoryId) {
		this.categoryId = categoryId;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public String getCategoryName() {
		return categoryName;
	}

	public void setCategoryName(String categoryName) {
		this.categoryName = categoryName;
	}

	public String getUpdateTime() {
		return updateTime;
	}

	public void setUpdateTime(String updateTime) {
		this.updateTime = updateTime;
	}

	public String getCreateTime() {
		return createTime;
	}

	public void setCreateTime(String createTime) {
		this.createTime = createTime;
	}

}
