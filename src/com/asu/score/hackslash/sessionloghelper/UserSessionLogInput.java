package com.asu.score.hackslash.sessionloghelper;

import java.util.ArrayList;
import java.util.List;

import com.asu.score.hackslash.dao.UsersDAO;

public class UserSessionLogInput {


	private List<UserSessionLog> list = new ArrayList<UserSessionLog>();

	public interface Listener {
		public void refresh();
	}

	public UserSessionLogInput(String username) {
			try {
				list =  new UsersDAO().getUserSessionLog(username);
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
	}

	public List elements() {
		return list;
	}

}
