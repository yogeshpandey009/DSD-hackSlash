package com.asu.score.hackslash.engine;

import java.io.IOException;

import javax.net.SocketFactory;

import org.jivesoftware.smack.ConnectionConfiguration;
import org.jivesoftware.smack.SmackException;
import org.jivesoftware.smack.XMPPException;
import org.jivesoftware.smack.tcp.XMPPTCPConnection;
import org.jivesoftware.smack.tcp.XMPPTCPConnectionConfiguration;

public class Server {
	
	public static void createConnection(){
		 System.out.println("Starting IM client");

		 XMPPTCPConnectionConfiguration.Builder config = XMPPTCPConnectionConfiguration.builder();
		    config.setSecurityMode(ConnectionConfiguration.SecurityMode.disabled);
		    //config.setUsernameAndPassword("qqq", "qqq");
		    config.setServiceName("Smack");
		    config.setHost("127.0.0.1");
		    config.setPort(5222);
		    config.setDebuggerEnabled(true);
		    config.setSocketFactory(SocketFactory.getDefault());
		    
		    XMPPTCPConnection mConnection = new XMPPTCPConnection(config.build());
		    try {
		        mConnection.connect();
		        System.out.println(" Status : " + mConnection.isConnected());
		    } catch (SmackException | IOException | XMPPException e) {
		        e.printStackTrace();
		    }
		    try {
		    	CharSequence c = "bharat";
				mConnection.login(c, "hello");
				System.out.println("Logged In??" + mConnection.isAuthenticated());
			} catch (XMPPException | SmackException | IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
    }
	
	public static void main(String[] args) {
		createConnection();
	}
}
