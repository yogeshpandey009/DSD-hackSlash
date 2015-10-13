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
import org.jivesoftware.smack.chat.ChatMessageListener;
import org.jivesoftware.smack.packet.Message;
import org.jivesoftware.smack.packet.Presence;
import org.jivesoftware.smack.packet.Presence.Type;
import org.jivesoftware.smack.roster.Roster;
import org.jivesoftware.smack.tcp.XMPPTCPConnection;
import org.jivesoftware.smack.tcp.XMPPTCPConnectionConfiguration;

import com.asu.score.hackslash.properties.Constants;

public class XmppManager {

	private static final int packetReplyTimeout = 500; // millis

	private String server;
	private String service;
	private int port;

	private XMPPTCPConnectionConfiguration config;
	private XMPPTCPConnection connection;

	private ChatManager chatManager;
	private ChatMessageListener messageListener;

	public XmppManager(String server, int port, String service) {
		this.server = server;
		this.port = port;
		this.service = service;
	}

	public void init() throws XMPPException, SmackException, IOException {

		System.out.println(String.format(
				"Initializing connection to server %1$s port %2$d", server,
				port));

		//SmackConfiguration.setDefaultPacketReplyTimeout(packetReplyTimeout);
		 config = XMPPTCPConnectionConfiguration.builder()
		    .setSecurityMode(ConnectionConfiguration.SecurityMode.disabled)
		    .setServiceName(Constants.SERVICE_NAME)
		    .setHost(Constants.SERVER_IP_ADDRESS)
		    .setPort(Constants.SERVER_PORT)
		    .setDebuggerEnabled(true)
		    .setSocketFactory(SocketFactory.getDefault()).build();

//		config = XMPPTCPConnectionConfiguration.builder()
//				.setServiceName(service)
//				.setHost(server)
//				.setPort(port).build();

		connection = new XMPPTCPConnection(config);
		connection.connect();

		System.out.println("Connected: " + connection.isConnected());

		chatManager = ChatManager.getInstanceFor(connection);
		messageListener = new MyMessageListener();

	}

	public void performLogin(String username, String password)
			throws XMPPException, SmackException, IOException {
		if (connection != null && connection.isConnected()) {
			connection.login(username, password);
		}
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

}