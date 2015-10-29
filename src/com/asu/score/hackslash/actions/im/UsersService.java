package com.asu.score.hackslash.actions.im;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import org.jivesoftware.smack.SmackException;
import org.jivesoftware.smack.SmackException.NoResponseException;
import org.jivesoftware.smack.SmackException.NotConnectedException;
import org.jivesoftware.smack.XMPPException;
import org.jivesoftware.smack.XMPPException.XMPPErrorException;
import org.jivesoftware.smack.packet.Presence;
import org.jivesoftware.smack.roster.Roster;
import org.jivesoftware.smack.roster.RosterEntry;
import org.jivesoftware.smackx.iqlast.LastActivityManager;
import org.jivesoftware.smackx.iqlast.packet.LastActivity;

import com.asu.score.hackslash.dao.TeamMembersDAO;
import com.asu.score.hackslash.engine.ConnectionManager;
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
	 * @throws XMPPException 
	 * @throws IOException 
	 * @throws SmackException 
	 */
	public static List<User> getAllUsers() throws SmackException, IOException, XMPPException {
		List<User> users = new ArrayList<User>();
		SessionManager session = SessionManager.getInstance();
		if (session.isAuthenticated()) {
			Roster ros = ConnectionManager.getRoster();
			LastActivityManager lastactivityManager = null;
			lastactivityManager = LastActivityManager.getInstanceFor(ConnectionManager.getConnection());
			if (ros != null) {
				Set<RosterEntry> entries = ros.getEntries();
				for (RosterEntry entry : entries) {
					String username = entry.getUser();
					Presence entryPresence = ros.getPresence(username);
					Presence.Type type = entryPresence.getType();
					String status = type.toString();					
					System.out
							.println(String.format("Buddy:%1$s - Status:%2$s",
									entry.getUser(), status));
					String lastSeenTime = getLastSeenTime(lastactivityManager, username);
					users.add(new User(username, status, lastSeenTime));
				}
			}
		}
		return users;
	}
	
	private static String getLastSeenTime(LastActivityManager lastActivityManager, String username) {
		String lastSeenTime = "";
		LastActivity lastSeen = null;
		if(lastActivityManager != null) {
			try {
				lastSeen = lastActivityManager.getLastActivity(username);
				System.out.println(lastSeen);
			} catch (NoResponseException | XMPPErrorException
					| NotConnectedException e) {
				//e.printStackTrace();
				System.out.println("Could not get last activity " + e.getMessage());
			}			
		}
		if(lastSeen != null) {
			long duration = lastSeen.lastActivity;
			if(duration != -1) {
				long hours = duration / 3600;
				long minutes = (duration % 3600) / 60;
				long seconds = duration % 60;
				lastSeenTime = String.format("%d hours %d mins %d secs ago", hours, minutes, seconds);
			}
		}
		return lastSeenTime;
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
			Roster ros = ConnectionManager.getRoster();
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
			Roster ros = ConnectionManager.getRoster();
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
				chatCtrl.createEntry(buddyJID);
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
