package com.asu.score.hackslash.chathelper;

import org.eclipse.ui.IActionFilter;

public class ChatActionFilter implements IActionFilter {

	public static final String NAME = "name";
	
	private static ChatActionFilter singleton;

	public static ChatActionFilter getSingleton() {
		if (singleton == null)
			singleton = new ChatActionFilter();
		return singleton;
	}
		
	/**
	 * @see IActionFilter#testAttribute(Object, String, String)
	 */
	public boolean testAttribute(Object target, String name, String value) {
		if (name.equals(NAME)) {
			Chat le = (Chat)target;
			return value.equals(le.toString());
		}	
		return false;
	}
}

