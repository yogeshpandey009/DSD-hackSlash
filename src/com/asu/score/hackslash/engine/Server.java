package com.asu.score.hackslash.engine;

import java.io.IOException;

import javax.net.SocketFactory;

import org.jivesoftware.smack.ConnectionConfiguration;
import org.jivesoftware.smack.SmackException;
import org.jivesoftware.smack.XMPPException;
import org.jivesoftware.smack.chat.ChatManager;
import org.jivesoftware.smack.roster.Roster;
import org.jivesoftware.smack.tcp.XMPPTCPConnection;
import org.jivesoftware.smack.tcp.XMPPTCPConnectionConfiguration;

import com.asu.score.hackslash.actions.im.ChatController;
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
		
		createConnection();
		login("yp", "qwe");
		
		ChatController chatCtrl = new ChatController(mConnection);
        
        chatCtrl.init();
        chatCtrl.setStatus(true, "Hello everyone");
        
        String buddyJID = "temp";
        String buddyName = "temp";
        chatCtrl.createEntry(buddyJID, buddyName);
        
        chatCtrl.sendMessage("Hello mate", "temp@yashu.local");
        Roster roster = Roster.getInstanceFor(mConnection);
        System.out.println("Yo" + roster.getEntries().size());
        
        boolean isRunning = true;
        
        while (isRunning) {
            Thread.sleep(50);
        }
        
        
        
        chatCtrl.destroy();
	}
}
