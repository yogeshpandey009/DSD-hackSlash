package com.asu.score.hackslash.actions.im;

import java.util.Set;

import org.jivesoftware.smack.roster.Roster;
import org.jivesoftware.smack.roster.RosterEntry;

public class Users {

	public static void getAllUser(Roster ros){
		Set<RosterEntry> entries = ros.getEntries();
		for (RosterEntry entry : entries) {
			System.out.println(String.format("Buddy:%1$s - Status:%2$s", entry.getName(), entry.getStatus()));
		}
	}
}
