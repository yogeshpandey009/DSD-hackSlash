package com.asu.score.hackslash.actions.im;

import java.util.HashSet;
import java.util.Set;

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
		Set<RosterEntry> entries = ros.getEntries();
		Set<String> users = new HashSet<String>();
		for (RosterEntry entry : entries) {
			System.out.println(String.format("Buddy:%1$s - Status:%2$s", entry.getName(), entry.getStatus()));
			users.add(entry.getName());
		}
		return users;
	}
}
