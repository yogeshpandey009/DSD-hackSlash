package com.asu.score.hackslash.actions.im;

import java.io.IOException;

import javax.net.SocketFactory;

import org.jivesoftware.smack.AbstractXMPPConnection;
import org.jivesoftware.smack.ConnectionConfiguration;
import org.jivesoftware.smack.SmackConfiguration;
import org.jivesoftware.smack.SmackException;
import org.jivesoftware.smack.SmackException.NotConnectedException;
import org.jivesoftware.smack.XMPPException;
import org.jivesoftware.smack.chat.Chat;
import org.jivesoftware.smack.chat.ChatManager;
import org.jivesoftware.smack.chat.ChatManagerListener;
import org.jivesoftware.smack.chat.ChatMessageListener;
import org.jivesoftware.smack.packet.Message;
import org.jivesoftware.smack.packet.Presence;
import org.jivesoftware.smack.packet.Presence.Type;
import org.jivesoftware.smack.roster.Roster;
import org.jivesoftware.smack.tcp.XMPPTCPConnection;
import org.jivesoftware.smack.tcp.XMPPTCPConnectionConfiguration;

import com.asu.score.hackslash.properties.Constants;

public class ChatController {

	private XMPPTCPConnection connection;

	private ChatManager chatManager;
	private ChatMessageListener messageListener;
	private ChatManagerListener managerListener;

	public ChatController(XMPPTCPConnection connection) {
		this.connection = connection;
	}

	public void init() throws XMPPException, SmackException, IOException {
		chatManager = ChatManager.getInstanceFor(connection);
		managerListener = new MyManagerListener();
		chatManager.addChatListener(managerListener);
	}

	public void setStatus(boolean available, String status)
			throws NotConnectedException {

		Presence.Type type = available ? Type.available : Type.unavailable;
		Presence presence = new Presence(type);

		presence.setStatus(status);
		connection.sendPacket(presence);

	}

	public void destroy() {
		if (connection != null && connection.isConnected()) {
			connection.disconnect();
		}
	}

	public void sendMessage(String message, String buddyJID)
			throws NotConnectedException {
		System.out.println(String.format("Sending mesage '%1$s' to user %2$s",
				message, buddyJID));
		Chat chat = chatManager.createChat(buddyJID, messageListener);
		chat.sendMessage(message);
	}

	public void createEntry(String user, String name) throws Exception {
		System.out.println(String.format(
				"Creating entry for buddy '%1$s' with name %2$s", user, name));
		Roster roster = Roster.getInstanceFor(connection);
		roster.createEntry(user, name, null);
	}

	class MyMessageListener implements ChatMessageListener {

		@Override
		public void processMessage(Chat chat, Message message) {
			String from = message.getFrom();
			String body = message.getBody();
			System.out.println(String.format(
					"Received message '%1$s' from %2$s", body, from));
		}

	}
	
	class MyManagerListener implements ChatManagerListener {

		@Override
		public void chatCreated(final Chat chat, final boolean createdLocally)
	    {
			messageListener = new MyMessageListener();
			chat.addMessageListener(messageListener);
	    }

	}

}