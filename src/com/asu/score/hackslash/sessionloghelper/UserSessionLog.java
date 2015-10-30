package com.asu.score.hackslash.sessionloghelper;

import java.util.Date;

public class UserSessionLog {
	private Date LoginTime;
	private Date LogoutTime;

	public Date getLoginTime() {
		return LoginTime;
	}

	public void setLoginTime(Date loginTime) {
		LoginTime = loginTime;
	}

	public Date getLogoutTime() {
		return LogoutTime;
	}

	public void setLogoutTime(Date logoutTime) {
		LogoutTime = logoutTime;
	}

	public UserSessionLog(Date loginTime, Date logoutTime) {
		super();
		LoginTime = loginTime;
		LogoutTime = logoutTime;
	}

}
