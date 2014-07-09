package com.wwly.vblog.model;

import org.json.JSONObject;

public class UserModel {
	private int id;
	private String username = "";
	private String email = "";
	private String firstName = "";
	private String lastName = "";
	private String sessionId = "";

	public UserModel(int id, String username, String email, String firstName,
			String lastName, String sessionId) {
		this.id = id;
		this.username = username;
		this.email = email;
		this.firstName = firstName;
		this.lastName = lastName;
		this.sessionId = sessionId;
	}
	
	public UserModel(JSONObject object) {
		id = object.optInt("id");
		username = object.optString("username");
		email = object.optString("email");
		firstName = object.optString("first_name");
		lastName = object.optString("last_name");
	}

	public int getId() {
		return id;
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

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getFirstName() {
		return firstName;
	}

	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}

	public String getLastName() {
		return lastName;
	}

	public void setLastName(String lastName) {
		this.lastName = lastName;
	}

	public String getSessionId() {
		return sessionId;
	}

	public void setSessionId(String sessionId) {
		this.sessionId = sessionId;
	}

}
