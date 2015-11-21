package com.asu.score.hackslash.chathelper;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import com.asu.score.hackslash.engine.SessionManager;
import com.asu.score.hackslash.taskhelper.Task;

public class ChatInput {

	private List<Chat> list = new ArrayList<Chat>();
	private Listener listener;
	
	public interface Listener {
		public void added(Chat w);
		public void refresh();
	}
	
	public void setListener(Listener l) {
		listener = l;
	}
	
	public Chat add(Chat chat) {
		System.out.println("inside add");
		addChat(chat);
		if (listener != null)
			listener.added(chat);
		return chat;
	}
	
	public void refresh() {
		System.out.println("inside refresh");
		if (listener != null)
			listener.refresh();
	}

	public Task find(String str) {
		Iterator iter = list.iterator();
		while (iter.hasNext()) {
			Task word = (Task)iter.next();
			if (str.equals(word.toString()))
				return word;
		}
		return null;
	}
	
	public List elements() {
		if (SessionManager.getInstance().isAuthenticated()){
			return list;
		}
		return new ArrayList<String>() {{
			add("Login to Start");
		}};
	}
	
	private void addChat(Chat chat) {
		//System.out.println("adding in DB");
			list.add(chat);
	}
	
}

