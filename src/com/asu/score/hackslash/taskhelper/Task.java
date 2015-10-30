package com.asu.score.hackslash.taskhelper;

import org.eclipse.core.runtime.IAdaptable;
import org.eclipse.ui.IActionFilter;

public class Task implements IAdaptable {
	
	private String taskID;
	private String name;
	private String desc;
	private String assignedTo;
	private String status;
	
	public Task(String name, String desc, String assignedTo, String taskID, String status) {
		super();
		
		this.name = name;
		this.desc = desc;
		this.assignedTo = assignedTo;
		this.taskID = taskID;
		this.status = status;
	}
	public String getName() {
        return name;
    }
	public String getTaskID() {
        return taskID;
    }
	public String getDesc() {
        return desc;
    }
	public String getAssignedTo() {
		return assignedTo;
	}
	public String getStatus() {
		return status;
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
	
	public void setTaskID(String taskID) {
		this.taskID = taskID;
	}
	public void setDesc(String desc) {
		this.desc = desc;
	}

	public void setAssignedTo(String assignedTo) {
		this.assignedTo = assignedTo;
	}

	public void setStatus(String status) {
		this.status = status;
	}
	
}

