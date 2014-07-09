package com.wwly.vblog.model;

import org.json.JSONObject;

import com.wwly.vblog.utils.StringUtil;

public class CommentModel {

	private int id = 0;
	private int blogId = 0;
	private String email = "";
	private String username = "";
	private String content = "";
	private String createTime = "";
	
	public CommentModel() {
		
	}
	
	public CommentModel(JSONObject object) {
		id = object.optInt("id");
		blogId = object.optInt("blog_id");
		email = object.optString("email");
		username = object.optString("username");
		content = object.optString("content");
		createTime = StringUtil.formatDjangoTime(object.optString("create_time"));
	}

	public int getId() {
		return id;
	}

	public int getBlogId() {
		return blogId;
	}

	public void setBlogId(int blogId) {
		this.blogId = blogId;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public void setId(int id) {
		this.id = id;
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

	public String getCreateTime() {
		return createTime;
	}

	public void setCreateTime(String createTime) {
		this.createTime = createTime;
	}

}
