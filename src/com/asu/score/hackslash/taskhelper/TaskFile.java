package com.asu.score.hackslash.taskhelper;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import com.asu.score.hackslash.dao.TaskDao;

public class TaskFile {

	//private File file;
	TaskDao taskDao = new TaskDao();
	private List<Task> list = new ArrayList<Task>();
	private Listener listener;
	
	public interface Listener {
		public void added(Task w);
		public void removed(Task w);
		public void refresh();
	}
	
	public TaskFile() {
		getTasks();
	}
		
	public void setListener(Listener l) {
		listener = l;
	}
	
	public void add(Task task) {
		System.out.println("inside add");
		addTask(task);		
		if (listener != null)
			listener.added(task);
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
		return list;
	}
	
	private void addTask(Task task) {
		System.out.println("adding in DB");
		try {
			taskDao.addTask(task);
			list.add(task);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
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

