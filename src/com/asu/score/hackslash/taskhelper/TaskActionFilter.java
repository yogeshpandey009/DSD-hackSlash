package com.asu.score.hackslash.taskhelper;

import org.eclipse.ui.IActionFilter;

public class TaskActionFilter implements IActionFilter {

	public static final String NAME = "name";
	
	private static TaskActionFilter singleton;

	public static TaskActionFilter getSingleton() {
		if (singleton == null)
			singleton = new TaskActionFilter();
		return singleton;
	}
		
	/**
	 * @see IActionFilter#testAttribute(Object, String, String)
	 */
	public boolean testAttribute(Object target, String name, String value) {
		if (name.equals(NAME)) {
			Task le = (Task)target;
			return value.equals(le.toString());
		}	
		return false;
	}
}

