package com.asu.score.hackslash.engine;

import java.io.IOException;

import javax.net.SocketFactory;

import org.jivesoftware.smack.ConnectionConfiguration;
import org.jivesoftware.smack.SmackException;
import org.jivesoftware.smack.XMPPException;
import org.jivesoftware.smack.tcp.XMPPTCPConnection;
import org.jivesoftware.smack.tcp.XMPPTCPConnectionConfiguration;

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
		    
		    XMPPTCPConnection mConnection = new XMPPTCPConnection(config.build());
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
	
	public static void main(String[] args) throws Exception{
		createConnection();
	}
}
