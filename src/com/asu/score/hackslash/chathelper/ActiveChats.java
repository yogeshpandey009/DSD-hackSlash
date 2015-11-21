package com.asu.score.hackslash.chathelper;

import java.util.ArrayList;

/**
 * Contains all active chats for a user.
 *
 */
public class ActiveChats {

	public static ArrayList<LocalChat> activeChats = new ArrayList<LocalChat>();
	
	/**
	 * Returns object of local chat if it already exists for that user else
	 * creates returns null.
	 * 
	 * @param user
	 * @return
	 */
	public static LocalChat getChatIfAlreadyExist(String user) {
		for (LocalChat c : ActiveChats.activeChats) {
			String buddy =c.getBuddy();
			if (buddy.contains("@")){
				buddy = buddy.substring(0, buddy.indexOf('@'));
			}
			if (user.contains("@")){
				user = user.substring(0, user.indexOf('@'));
			}
			if (buddy.equals(user)) {
				return c;
			}
		}
		return null;
	}
}
