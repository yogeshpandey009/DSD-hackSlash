package com.asu.score.hackslash.userhelper;

public class User {
	private String name;
	private String status;
	private String lastSeen;
	
	public User(String name, String status, String lastSeen) {
		super();
		this.name = name;
		this.status = status;
		this.lastSeen = lastSeen;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
	public String getLastSeen() {
		return lastSeen;
	}
	public void setLastSeen(String lastSeen) {
		this.lastSeen = lastSeen;
	}
	
}
