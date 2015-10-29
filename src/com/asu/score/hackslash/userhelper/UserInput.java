package com.asu.score.hackslash.userhelper;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.jivesoftware.smack.SmackException;
import org.jivesoftware.smack.XMPPException;

import com.asu.score.hackslash.actions.im.UsersService;
import com.asu.score.hackslash.engine.SessionManager;

public class UserInput {

	private List<User> list = new ArrayList<User>();
	private Listener listener;

	public interface Listener {
		public void refresh();
	}

	public UserInput() {
		populateUsers();
	}

	public void setListener(Listener l) {
		listener = l;
	}

	public String add(String username) {
		System.out.println("inside add user");
		String msg = addUser(username);
		if (listener != null)
			listener.refresh();
		return msg;
	}

	public void remove(User user) {
		// TODO:
		if (listener != null)
			listener.refresh();
	}

	public void refresh() {
		System.out.println("inside refresh user");
		populateUsers();
		if (listener != null)
			listener.refresh();
	}

	public User find(String str) {
		Iterator iter = list.iterator();
		while (iter.hasNext()) {
			User u = (User) iter.next();
			if (str.equals(u.toString()))
				return u;
		}
		return null;
	}

	public List elements() {
		if (SessionManager.getInstance().isAuthenticated()) {
			return list;
		}
		return new ArrayList<String>() {
			{
				add("Login to Start");
			}
		};
	}

	private String addUser(String username) {
		System.out.println("adding in Roster");
		String msg = "";
		try {
			msg = UsersService.addUserInRoster(username, username);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			msg = e.getMessage();
		}
		return msg;
	}

	private void populateUsers() {
		System.out.println("fetching all users");
		try {
			list = UsersService.getAllUsers();
		} catch (SmackException | IOException | XMPPException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		System.out.println(list.size() + " contacts founds");
	}
}
