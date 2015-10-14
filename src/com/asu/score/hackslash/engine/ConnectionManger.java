package com.asu.score.hackslash.engine;

import java.io.IOException;
import java.util.Scanner;

import javax.net.SocketFactory;

import org.jivesoftware.smack.ConnectionConfiguration;
import org.jivesoftware.smack.SASLAuthentication;
import org.jivesoftware.smack.SmackException;
import org.jivesoftware.smack.XMPPException;
import org.jivesoftware.smack.chat.ChatManager;
import org.jivesoftware.smack.roster.Roster;
import org.jivesoftware.smack.tcp.XMPPTCPConnection;
import org.jivesoftware.smack.tcp.XMPPTCPConnectionConfiguration;

import com.asu.score.hackslash.actions.im.ChatController;
import com.asu.score.hackslash.actions.im.Users;
import com.asu.score.hackslash.properties.Constants;

public class ConnectionManger {

	private static XMPPTCPConnection mConnection = null;

	private static XMPPTCPConnection createConnection() throws SmackException,
			IOException, XMPPException {
		System.out.println("Starting Client");

		XMPPTCPConnectionConfiguration.Builder config = XMPPTCPConnectionConfiguration
				.builder();
		config.setSecurityMode(ConnectionConfiguration.SecurityMode.disabled);
		config.setServiceName(Constants.SERVICE_NAME);
		config.setHost(Constants.SERVER_IP_ADDRESS);
		config.setPort(Constants.SERVER_PORT);
		config.setDebuggerEnabled(true);
		config.setSocketFactory(SocketFactory.getDefault());

		XMPPTCPConnection mConnection = new XMPPTCPConnection(config.build());
		//SASLAuthentication.unBlacklistSASLMechanism("PLAIN");
		//SASLAuthentication.blacklistSASLMechanism("DIGEST-MD5");
		try {
			mConnection.connect();
			System.out.println("Successfully connected to Server!");
		} catch (SmackException | IOException | XMPPException e) {
			System.out.println("Error while connecting to Server. ->"
					+ e.getMessage());
			throw e;
		}
		return mConnection;
	}

	public static synchronized XMPPTCPConnection getConnection()
			throws SmackException, IOException, XMPPException {
		if (mConnection == null || mConnection.isConnected() == false) {
			mConnection = createConnection();
		}
		return mConnection;
	}

	public static void login(final String user, final String pwrd)
			throws XMPPException, SmackException, IOException {
		try {
			mConnection = getConnection();
			mConnection.login(user, pwrd);
			System.out.println("---" + user + "--- Successfully Logged In!");
			SessionManager.getInstance().initializeSession(mConnection, user,
					pwrd);
			ChatController.getInstance().init();
		} catch (XMPPException | SmackException | IOException e) {
			mConnection.disconnect();//otherwise login always fail after wrong attempt
			System.out.println("Error while logging into Server. ->"
					+ e.getMessage());
			throw e;
		}
	}

	public static String getCurrentUser() throws SmackException, IOException,
			XMPPException {
		mConnection = getConnection();
		return mConnection.getUser();
	}

	public static void disconnect() throws SmackException, IOException,
			XMPPException {
		mConnection = getConnection();
		mConnection.disconnect();
	}

	public static ChatManager getChatManager() throws SmackException,
			IOException, XMPPException {
		mConnection = getConnection();
		return ChatManager.getInstanceFor(mConnection);
	}

	public static void main(String[] args) throws Exception {

		login("yashu", "yashu");

		ChatController chatCtrl = ChatController.getInstance();

		chatCtrl.setStatus(true, "Hello everyone");

		String buddyJID = "yp";
		String buddyName = "yp";
		chatCtrl.createEntry(buddyJID, buddyName);

		chatCtrl.sendMessage("Hello mate", "yp@yashu.local");
		Roster roster = Roster.getInstanceFor(mConnection);
		Users.getAllUser(roster);

		Scanner sc = new Scanner(System.in);
		for (int x = 0; x < 10; x++) {
			String s = sc.nextLine();
			chatCtrl.sendMessage(s, "temp@yashu.local");
		}
		sc.close();

		boolean isRunning = true;

		while (isRunning) {
			Thread.sleep(50);
		}

		disconnect();
	}
}
