package com.asu.score.hackslash.chathelper;

import java.util.ArrayList;

import com.asu.score.hackslash.dialogs.ChatDialog;

/**
 * Record of chats to a buddy.
 *
 */
public class Chat {
	private String buddy;
	private ArrayList<Message> conversation;
	private ChatDialog chatDialog;
	
	/**
	 * Getter for ChatDialog
	 * @return
	 */
	public ChatDialog getChatDialog() {
		return chatDialog;
	}

	/**
	 * Setter for Chat Dialog
	 * @param chatDialog
	 */
	public void setChatDialog(ChatDialog chatDialog) {
		this.chatDialog = chatDialog;
	}

	/**
	 *  Getter of buddy for the chat
	 * @return
	 */
	public String getBuddy() {
		return buddy;
	}
	
	/**
	 * Setter of buddy for the chat
	 * @param buddy
	 */
	public void setBuddy(String buddy) {
		this.buddy = buddy;
	}
	
	/**
	 * Getter for a list of conversation with the buddy.
	 * @return
	 */
	public ArrayList<Message> getConversation() {
		return conversation;
	}
	
	
	public void setConversation(ArrayList<Message> conversation) {
		this.conversation = conversation;
	}
}
