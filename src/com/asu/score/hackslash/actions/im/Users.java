package com.asu.score.hackslash.actions.im;

import java.util.HashSet;
import java.util.Set;

import org.jivesoftware.smack.packet.Presence;
import org.jivesoftware.smack.roster.Roster;
import org.jivesoftware.smack.roster.RosterEntry;

import com.asu.score.hackslash.engine.ConnectionManger;

/**
 * Class to retrieve information of Users in Openfire.
 */
public class Users {

	private static Roster ros = ConnectionManger.getRoster();
	
	/**
	 * Returns the names of all user in the current user's roster.
	 * @param ros
	 * @return
	 */
	public static Set<String> getAllUser(){
		Set<String> users = new HashSet<String>();
		if (ros != null){
			Set<RosterEntry> entries = ros.getEntries();
			for (RosterEntry entry : entries) {
				Presence entryPresence = ros.getPresence(entry.getUser());
				
				Presence.Type type = entryPresence.getType();       
				
				System.out.println(String.format("Buddy:%1$s - Status:%2$s", entry.getName(), type.toString()));
				users.add(entry.getUser());
			}
		}
		return users;
	}
	
	/**
	 * Gets presence (available/ unavailable) of a user.
	 * @param user - username
	 * @return String
	 */
	public static String getUserPresenceType(String user){
		String retVal = "";
		if (ros != null){
			Set<RosterEntry> entries = ros.getEntries();
			for (RosterEntry entry : entries) {
				Presence entryPresence = ros.getPresence(entry.getUser());

	            Presence.Type type = entryPresence.getType();       
	            if (user.equals(entry.getName())){
	            	retVal = type.toString();
	            }
			}
		}
		
		return retVal;
	}

}
