package com.asu.score.hackslash.engine;

import java.io.IOException;
import java.util.Scanner;

import javax.net.SocketFactory;

import org.jivesoftware.smack.ConnectionConfiguration;
import org.jivesoftware.smack.SmackException;
import org.jivesoftware.smack.XMPPException;
import org.jivesoftware.smack.chat.ChatManager;
import org.jivesoftware.smack.roster.Roster;
import org.jivesoftware.smack.tcp.XMPPTCPConnection;
import org.jivesoftware.smack.tcp.XMPPTCPConnectionConfiguration;

import com.asu.score.hackslash.actions.im.ChatController;
import com.asu.score.hackslash.actions.im.UsersService;
import com.asu.score.hackslash.properties.Constants;
import com.asu.score.hackslash.statistics.GitController;

/**
 * Singleton Class manages connection to the server.
 */
public class ConnectionManager {

	private static XMPPTCPConnection mConnection = null;
	
	/**
	 * Creates a connection to the Openfire server using Smack APIs
	 * @return Connection Object
	 * @throws SmackException
	 * @throws IOException
	 * @throws XMPPException
	 */
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
	
	/**
	 * Returns true or false based on the logged in status of the user.
	 * @return flag
	 */
	public static boolean isUserLoggedIn(){
		if (mConnection != null && mConnection.isAuthenticated()){
			return true;
		} 
		return false;
	}
	
	/**
	 * A thread safe method to get the object of connection
	 * @return Connection object
	 * @throws SmackException
	 * @throws IOException
	 * @throws XMPPException
	 */
	public static synchronized XMPPTCPConnection getConnection()
			throws SmackException, IOException, XMPPException {
		if (mConnection == null || mConnection.isConnected() == false) {
			mConnection = createConnection();
		}
		return mConnection;
	}

	/**
	 * Logs into the user account using user ID and password
	 * Throws exceptions in case of login errors
	 * @param user username
	 * @param pwrd Password
	 * @throws XMPPException
	 * @throws SmackException
	 * @throws IOException
	 */
	public static void login(final String user, final String pwrd)
			throws XMPPException, SmackException, IOException {
		try {
			mConnection = getConnection();
			mConnection.login(user, pwrd);
			System.out.println("---" + user + "--- Successfully Logged In!");
			SessionManager.getInstance().initializeSession(mConnection, user,
					pwrd);
			ChatController.getInstance().init();
			ChatController.getInstance().updateRoster();
			
			Thread t = new Thread(new Runnable() {
				@Override
				public void run() {
					GitController.getInstance();
				}
			});
			t.start();
		} catch (XMPPException | SmackException | IOException e) {
			mConnection.disconnect();//otherwise login always fail after wrong attempt
			System.out.println("Error while logging into Server. ->"
					+ e.getMessage());
			throw e;
		}
	}

	/**
	 * Returns the currently logged in User
	 * @return Current User
	 * @throws SmackException
	 * @throws IOException
	 * @throws XMPPException
	 */
	public static String getCurrentUser() throws SmackException, IOException,
			XMPPException {
		mConnection = getConnection();
		return mConnection.getUser();
	}
	
	/**
	 * Returns the instanse of Roster
	 * @return Roster
	 */
	public static Roster getRoster(){
		return Roster.getInstanceFor(mConnection);
	}

	/**
	 * Disconnects the connection to server.
	 * @throws SmackException
	 * @throws IOException
	 * @throws XMPPException
	 */
	public static void disconnect() throws SmackException, IOException,
			XMPPException {
		mConnection = getConnection();
		mConnection.disconnect();
		System.out.println("User Logged Out. Server Connection Closed!");
	}
	
	/**
	 * Gets an instance of ChatManager from the server.
	 * @return ChatManager Object
	 * @throws SmackException
	 * @throws IOException
	 * @throws XMPPException
	 */
	public static ChatManager getChatManager() throws SmackException,
			IOException, XMPPException {
		mConnection = getConnection();
		return ChatManager.getInstanceFor(mConnection);
	}

	public static void main(String[] args) throws Exception {

		login("bharat", "bharat");

		ChatController chatCtrl = ChatController.getInstance();

		chatCtrl.setStatus(true, "Hello everyone");

		String buddyJID = "yp";
		chatCtrl.createEntry(buddyJID);

		chatCtrl.sendMessage("Hello mate", "yp@yashu.local");
		
		UsersService.getAllUsers();

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
