package com.asu.score.hackslash.model;

import java.sql.Timestamp;

public class ShowTaskDetails {

	private String user_id;
	private String start_dt;
	private String end_dt;
	
	
	public ShowTaskDetails(){
		this.setUser_id("");
		this.setStart_dt("");
		this.setEnd_dt("");
	}
	public String getUser_id() {
		return user_id;
	}
	public void setUser_id(String user_id) {
		this.user_id = user_id;
	}
	public String getStart_dt() {
		return start_dt;
	}
	public void setStart_dt(String start_dt) {
		this.start_dt = start_dt;
	}
	public String getEnd_dt() {
		return end_dt;
	}
	public void setEnd_dt(String end_dt) {
		this.end_dt = end_dt;
	}
	
	
}
