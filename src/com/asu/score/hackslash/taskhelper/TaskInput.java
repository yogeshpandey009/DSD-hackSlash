package com.asu.score.hackslash.taskhelper;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import com.asu.score.hackslash.dao.TaskDao;
import com.asu.score.hackslash.engine.SessionManager;

public class TaskInput {

	private TaskDao taskDao = new TaskDao();
	private List<Task> list = new ArrayList<Task>();
	private Listener listener;
	
	public interface Listener {
		public void added(Task w);
		public void removed(Task w);
		public void refresh();
	}
	
	public TaskInput() {
		getTasks();
	}
		
	public void setListener(Listener l) {
		listener = l;
	}
	
	public Task add(Task task) {
		System.out.println("inside add");
		String taskID = addTask(task);
		task.setTaskID(taskID);
		if (listener != null)
			listener.added(task);
		return task;
	}
	
	public void remove(Task word) {
		list.remove(word);
		//addTasks(word);
		if (listener != null)
			listener.removed(word);
	}
	
	public void refresh() {
		System.out.println("inside refresh");
		getTasks();
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
	
	private String addTask(Task task) {
		//System.out.println("adding in DB");
		String taskID = null;
		try {
			taskID = taskDao.addTask(task);
			list.add(task);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return taskID;
	}
	
	private void getTasks() {
		try {
			list = taskDao.getTasks();
			System.out.println(list);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}

