package com.asu.score.hackslash.actions.im;

import org.jivesoftware.smack.SmackException.NotConnectedException;
import org.jivesoftware.smack.chat.Chat;
import org.jivesoftware.smack.chat.ChatManager;
import org.jivesoftware.smack.chat.ChatMessageListener;
import org.jivesoftware.smack.packet.Message;
import org.jivesoftware.smack.packet.Presence;

import com.asu.score.hackslash.engine.Server;

public class ChatController {

	private static ChatManager chatManager = Server.getChatManager();
	
	public static void startChat(String toUser){
		ChatMessageListener messageListener = new ChatMessageListener() {
			
			@Override
			public void processMessage(Chat arg0, Message arg1) {
				System.out.println("You have received a new message from " + arg1.getFrom() + " = " + arg1.getBody());
				
			}
		};
		Chat chat = chatManager.createChat(toUser, messageListener);
		try {
			chat.sendMessage("I L ");
		} catch (NotConnectedException e) {
			System.out.println("The User is no more online.");
			e.printStackTrace();
		}
	}
	
	public static void getPresence(){
	}
	
}
