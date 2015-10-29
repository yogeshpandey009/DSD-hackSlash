package com.asu.score.hackslash.actions.im;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.jivesoftware.smack.SmackException;
import org.jivesoftware.smack.XMPPException;
import org.jivesoftware.smack.packet.Presence;
import org.jivesoftware.smack.roster.Roster;
import org.jivesoftware.smack.roster.RosterEntry;

import com.asu.score.hackslash.dao.TeamMembersDAO;
import com.asu.score.hackslash.engine.ConnectionManger;
import com.asu.score.hackslash.engine.SessionManager;
import com.asu.score.hackslash.userhelper.User;

/**
 * Class to retrieve information of Users in Openfire.
 */
public class UsersService {

	/**
	 * Returns all users in the current user's roster.
	 * 
	 * @param ros
	 * @return
	 */
	public static List<User> getAllUsers() {
		List<User> users = new ArrayList<User>();
		SessionManager session = SessionManager.getInstance();
		if (session.isAuthenticated()) {
			Roster ros = ConnectionManger.getRoster();
			if (ros != null) {
				Set<RosterEntry> entries = ros.getEntries();
				for (RosterEntry entry : entries) {
					String username = entry.getUser();
					Presence entryPresence = ros.getPresence(username);
					Presence.Type type = entryPresence.getType();
					String status = type.toString();					
					System.out
							.println(String.format("Buddy:%1$s - Status:%2$s",
									entry.getName(), status));
					users.add(new User(username, status));
					// users.add(entry.getName());
				}
			}
		}
		return users;
	}

	/**
	 * Returns the names of all users in the current user's roster.
	 * 
	 * @param ros
	 * @return
	 */
	public static List<String> getAllUsernames() {
		List<String> usernames = new ArrayList<String>();
		SessionManager session = SessionManager.getInstance();
		if (session.isAuthenticated()) {
			Roster ros = ConnectionManger.getRoster();
			if (ros != null) {
				Set<RosterEntry> entries = ros.getEntries();
				for (RosterEntry entry : entries) {
					String username = entry.getUser();
					usernames.add(username);
				}
			}
		}
		return usernames;
	}

	/**
	 * Gets presence (available/ unavailable) of a user.
	 * 
	 * @param user
	 *            - username
	 * @return String
	 */
	public static String getUserPresenceType(String user) {
		SessionManager session = SessionManager.getInstance();
		String retVal = "";
		if (session.isAuthenticated()) {
			Roster ros = ConnectionManger.getRoster();
			if (ros != null) {
				Set<RosterEntry> entries = ros.getEntries();
				for (RosterEntry entry : entries) {
					Presence entryPresence = ros.getPresence(entry.getUser());

					Presence.Type type = entryPresence.getType();
					if (user.equals(entry.getName())) {
						retVal = type.toString();
					}
				}
			}
		}

		return retVal;
	}

	public static String addUserInRoster(String buddyJID, String buddyName) {
		String message = "Login is required";
		SessionManager session = SessionManager.getInstance();
		if (session.isAuthenticated()) {
			try {
				ChatController chatCtrl = ChatController.getInstance();
				chatCtrl.createEntry(buddyJID, buddyName);
				message = "Buddy - " + buddyJID + " - added successfully";
				TeamMembersDAO tmDao = new TeamMembersDAO();
				tmDao.addUser(buddyJID);
			} catch (XMPPException | SmackException | IOException e) {
				message = "Unable to add Buddy. Chat Controller not Available.";
				e.printStackTrace();
			}
		}
		return message;
	}

}
