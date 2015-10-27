package com.asu.score.hackslash.engine;

import java.util.Date;

import org.jivesoftware.smack.SmackException.NotConnectedException;
import org.jivesoftware.smack.packet.Presence;
import org.jivesoftware.smack.tcp.XMPPTCPConnection;

/**
 * This manager is responsible for the handling of the XMPPTCPConnection. This is used
 * for the changing of the users presence, the handling of connection errors and the ability to add
 * presence listeners and retrieve the connection used in Spark.
 *
 */
public class SessionManager {

	private static SessionManager manager = null;
	
	private XMPPTCPConnection connection;
    
    private String serverAddress;
    private String username;
    private String password;
    private Date loginTime;
    private String JID;

    private SessionManager() {
    }
    
    public static synchronized SessionManager getInstance() {
		//Synchronized to ensure that we don't end up creating two singletons
		if(null == manager) {
			manager = new SessionManager();			
		}
		return manager;
	}

    /**
     * Initializes session.
     *
     * @param connection the XMPPTCPConnection used in this session.
     * @param username   the agents username.
     * @param password   the agents password.
     */
    public void initializeSession(XMPPTCPConnection connection, String username, String password) {
        this.connection = connection;
        this.username = username;
        this.password = password;
        this.loginTime = new Date();
		setServerAddress(connection.getServiceName());
		setJID(connection.getUser());
    }

	/**
     * Update the current availability of the user
     *
     * @param presence the current presence of the user.
     */
    public void changePresence(Presence presence) {
    	// Do NOT  send presence if disconnected.
        if (connection.isConnected()) {
            // Send Presence Packet
        	try {
				connection.sendStanza(presence);
			} catch (NotConnectedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
        }
    }

    /**
     * Returns the XMPPTCPConnection used for this session.
     *
     * @return the XMPPTCPConnection used for this session.
     */
    public XMPPTCPConnection getConnection() {
        return connection;
    }

    /**
     * Returns the host for this connection.
     *
     * @return the connection host.
     */
    public String getServerAddress() {
        return serverAddress;
    }

    /**
     * Set the server address
     *
     * @param address the address of the server.
     */
    public void setServerAddress(String address) {
        this.serverAddress = address;
    }

    /**
     * Notify agent that the connection has been closed.
     */
    public void connectionClosed() {
    }

    /**
     * Return the username associated with this session.
     *
     * @return the username associated with this session.
     */
    public String getUsername() {
        return username;
    }

    /**
     * Return the password associated with this session.
     *
     * @return the password assoicated with this session.
     */
    public String getPassword() {
        return password;
    }

    /**
     * Returns the jid of the user.
     *
     * @return the jid of the user.
     */
    public String getJID() {
        return JID;
    }

    /**
     * Sets the jid of the current user.
     *
     * @param jid the jid of the current user.
     */
    public void setJID(String jid) {
        this.JID = jid;
    }
    
    public Date getLoginTime() {
		return loginTime;
	}

	public void setLoginTime(Date loginTime) {
		this.loginTime = loginTime;
	}

	public void setConnection(XMPPTCPConnection conn) {
        this.connection = conn;
    }
    
	public boolean isAuthenticated() {
		if (connection != null && connection.isAuthenticated()) {
			return true;
		}
		return false;
	}
	
	public void logout() {
		if (connection != null) {
			connection.disconnect();
		}
		loginTime = null;
	}

}
