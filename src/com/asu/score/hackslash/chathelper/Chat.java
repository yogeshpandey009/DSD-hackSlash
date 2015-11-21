package com.asu.score.hackslash.chathelper;

import org.eclipse.core.runtime.IAdaptable;
import org.eclipse.ui.IActionFilter;

public class Chat implements IAdaptable {
	
	private String buddy;
	private String msg;
	
	public Chat(String buddy, String msg) {
		super();
		
		this.buddy = buddy;
		this.msg = msg;
	}
	
	
	
	public String getBuddy() {
		return buddy;
	}



	public void setBuddy(String buddy) {
		this.buddy = buddy;
	}



	public String getMsg() {
		return msg;
	}



	public void setMsg(String msg) {
		this.msg = msg;
	}



	public String toString() {
		return buddy.toUpperCase() + ":  " + msg;
	}

	public Object getAdapter(Class adapter) {
		if (adapter == IActionFilter.class) {
			return ChatActionFilter.getSingleton();
		}
		return null;
	}
	
	
}

