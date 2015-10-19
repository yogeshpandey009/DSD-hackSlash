package com.asu.score.hackslash.taskhelper;

import org.eclipse.core.runtime.IAdaptable;
import org.eclipse.ui.IActionFilter;

public class Task implements IAdaptable {

	private String name;
	private String desc;
	private String assignedTo;
	
	public Task(String name, String desc, String assignedTo) {
		super();
		this.name = name;
		this.desc = desc;
		this.assignedTo = assignedTo;
	}
	public String getName() {
        return name;
    }
	public String getDesc() {
        return desc;
    }
	public String getAssignedTo() {
		return assignedTo;
	}
	
	public String toString() {
		return name + ":" + desc + ":" + assignedTo;
	}

	public Object getAdapter(Class adapter) {
		if (adapter == IActionFilter.class) {
			return TaskActionFilter.getSingleton();
		}
		return null;
	}
	public void setName(String name) {
		this.name = name;
	}

	public void setDesc(String desc) {
		this.desc = desc;
	}

	public void setAssignedTo(String assignedTo) {
		this.assignedTo = assignedTo;
	}
	
}

