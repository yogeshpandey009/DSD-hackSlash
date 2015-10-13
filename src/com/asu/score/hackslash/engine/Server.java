package com.asu.score.hackslash.engine;

import java.io.IOException;
import java.util.Collection;

import javax.net.SocketFactory;

import org.jivesoftware.smack.ConnectionConfiguration;
import org.jivesoftware.smack.SmackException;
import org.jivesoftware.smack.XMPPException;
import org.jivesoftware.smack.chat.ChatManager;
import org.jivesoftware.smack.packet.Presence;
import org.jivesoftware.smack.packet.Presence.Type;
import org.jivesoftware.smack.roster.Roster;
import org.jivesoftware.smack.roster.RosterEntry;
import org.jivesoftware.smack.tcp.XMPPTCPConnection;
import org.jivesoftware.smack.tcp.XMPPTCPConnectionConfiguration;

import com.asu.score.hackslash.actions.im.ChatController;
import com.asu.score.hackslash.actions.im.XmppManager;
import com.asu.score.hackslash.properties.Constants;

public class Server {
	
	private static XMPPTCPConnection mConnection = null;
	
	public static void createConnection() throws SmackException, IOException, XMPPException {
		 System.out.println("Starting Client");

		 XMPPTCPConnectionConfiguration.Builder config = XMPPTCPConnectionConfiguration.builder();
		    config.setSecurityMode(ConnectionConfiguration.SecurityMode.disabled);
		    config.setServiceName(Constants.SERVICE_NAME);
		    config.setHost(Constants.SERVER_IP_ADDRESS);
		    config.setPort(Constants.SERVER_PORT);
		    config.setDebuggerEnabled(true);
		    config.setSocketFactory(SocketFactory.getDefault());
		    
		    mConnection = new XMPPTCPConnection(config.build());
		    try {
		        mConnection.connect();
		        if (mConnection.isConnected()) {
		        	System.out.println("Successfully connected to Server!");
		        }
		    } catch (SmackException | IOException | XMPPException e) {
		        System.out.println("Error while connecting to Server. ->" + e.getMessage());
		        throw e;
		    }
    }
	
	public static void login(final String user, final String pwrd) throws XMPPException, SmackException, IOException{
		try {
			mConnection.login(user, pwrd);
			if (mConnection.isAuthenticated()) {
				System.out.println("---" + user + "--- Successfully Logged In!");
			}
		} catch (XMPPException | SmackException | IOException e) {
			System.out.println("Error while logging into Server. ->" + e.getMessage());
	        throw e;
		}
	}
	
	public static boolean getServerStatus(){
		if (mConnection != null && mConnection.isConnected()){
			return true;
		}
		return false;
	}
	
	public static boolean getUserStatus(){
		if (mConnection != null && mConnection.isAuthenticated()){
			return true;
		}
		return false;
	}
	
	public static String getCurrentUser(){
		return mConnection.getUser();
	}
	
	public static void disconnect(){
		mConnection.disconnect();
	}
	
	public static ChatManager getChatManager(){
		return ChatManager.getInstanceFor(mConnection);
	}
	
	public static void main(String[] args) throws Exception{
		/*
		createConnection();
		login("yp", "qwe");
		ChatManager cm = ChatManager.getInstanceFor(mConnection);
	    Presence presence = new Presence(Type.available);
	    presence.setStatus("HIII");
	    mConnection.sendPacket(presence);

		Roster ros = Roster.getInstanceFor(mConnection);
		Collection<RosterEntry> entries = ros.getEntries();
		ros.createEntry("bharat", "bharat", null);
		 
	    System.out.println("\n\n" + entries.size() + " buddy(ies):");
	    for(RosterEntry r:entries)
	    {
	    System.out.println(r.getUser());
	    }
	    ChatController.startChat("bharat@bks-pc");
	    */
		String username = "yashu";
        String password = "yashu";
        
        XmppManager xmppManager = new XmppManager(Constants.SERVER_IP_ADDRESS, Constants.SERVER_PORT, Constants.SERVICE_NAME);
        
        xmppManager.init();
        xmppManager.performLogin(username, password);
        xmppManager.setStatus(true, "Hello everyone");
        
        String buddyJID = "temp";
        String buddyName = "temp";
        xmppManager.createEntry(buddyJID, buddyName);
        
        xmppManager.sendMessage("Hello mate", "temp@yashu.local");
        
        boolean isRunning = true;
        
        while (isRunning) {
            Thread.sleep(50);
        }
        
        xmppManager.destroy();
	}
}
