package com.asu.score.hackslash.chathelper;

/**
 * Message class that stores the sender and the message sent.
 *
 */
public class Message {
	
	private String sender;
	private String msg;
	
	/**
	 * Constructor for Message
	 * @param sender
	 * @param msg
	 */
	public Message(String sender, String msg){
		this.sender = sender;
		this.msg = msg;
	}
	
	
	/**
	 * Getter for Sender
	 * @return
	 */
	public String getSender() {
		return sender;
	}
	
	/**
	 * Setter for Sender
	 * @param sender
	 */
	public void setSender(String sender) {
		this.sender = sender;
	}
	
	/**
	 * Getter for Message
	 * @return
	 */
	public String getMsg() {
		return msg;
	}
	
	/**
	 * Setter for message
	 * @param msg
	 */
	public void setMsg(String msg) {
		this.msg = msg;
	}
}
