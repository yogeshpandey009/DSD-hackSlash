package com.asu.score.hackslash.chathelper;

import java.io.IOException;
import java.util.ArrayList;

import org.eclipse.jface.window.Window;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;
import org.jivesoftware.smack.SmackException;
import org.jivesoftware.smack.XMPPException;

import com.asu.score.hackslash.actions.im.ChatController;
import com.asu.score.hackslash.dialogs.ChatDialog;
import com.asu.score.hackslash.engine.ConnectionManger;

/**
 * Record of chats to a buddy.
 *
 */
public class LocalChat {
	private String buddy;
	private ArrayList<Message> conversation;
	private ChatDialog chatDialog;
	private Display display;
	
	public LocalChat(String buddy, Display display){
		this.buddy = buddy;
		this.display = display;
		ActiveChats.activeChats.add(this);
		conversation = new ArrayList<Message>();
	}
	
	
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
	
	/**
	 * Called when a chat message is received.
	 * @param msg
	 */
	public void receivedChat(String msg){
		Message message = new Message(buddy, msg);
		conversation.add(message);
		displayChat();
	}

	/**
	 * Opens chat Dialog for display.
	 */
	private void displayChat() {
		if (chatDialog != null){
			chatDialog.close();
		}
		
		final Shell shell = new Shell(display);
		chatDialog = new ChatDialog(shell, buddy, createMessageBody());
		if (chatDialog.open() == Window.OK){
			try {
				ChatController chatController = ChatController.getInstance();
				chatController.sendMessage(chatDialog.getMsg().trim(),
						buddy);
				Message message = new Message(ConnectionManger.getCurrentUser(), chatDialog.getMsg().trim());
				conversation.add(message);
				displayChat();
			} catch (XMPPException | SmackException | IOException e) {
				e.printStackTrace();
			}
		}
		
	}
	
	/**
	 * Creates message to be displayed.
	 * @return
	 */
	private String createMessageBody(){
		StringBuilder body = new StringBuilder("");
		for (Message msg : conversation){
			String sender = msg.getSender();
			if (sender.contains("@")){
				sender = sender.substring(0, sender.indexOf('@'));
				sender = sender.toUpperCase();
			}
			body.append(sender + " :- ");
			body.append(msg.getMsg());
			body.append("\n");
		}
		return body.toString();
	}
	
	/**
	 * Called to send message to a Buddy.
	 */
	public void sendMessege(){
		displayChat();
	}
}
