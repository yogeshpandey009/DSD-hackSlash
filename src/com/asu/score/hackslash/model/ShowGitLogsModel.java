package com.asu.score.hackslash.model;

import java.sql.Timestamp;

public class ShowGitLogsModel {

	
	private String authorName;
	private String logIdentifier;
	private String date;
	private String commitMessage;
	
	
	public String getAuthorName() {
		return authorName;
	}
	public void setAuthorName(String authorName) {
		this.authorName = authorName;
	}
	public String getLogIdentifier() {
		return logIdentifier;
	}
	public void setLogIdentifier(String logIdentifier) {
		this.logIdentifier = logIdentifier;
	}
	public String getDate() {
		return date;
	}
	public void setDate(String date) {
		this.date = date;
	}
	public String getCommitMessage() {
		return commitMessage;
	}
	public void setCommitMessage(String commitMessage) {
		this.commitMessage = commitMessage;
	}
	
}