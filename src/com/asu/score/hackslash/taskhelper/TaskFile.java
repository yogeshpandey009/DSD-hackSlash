package com.asu.score.hackslash.taskhelper;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class TaskFile {

	private File file;
	private ArrayList<Task> list = new ArrayList<Task>();
	private Listener listener;
	
	public interface Listener {
		public void added(Task w);
		public void removed(Task w);
	}
	
	/**
	 * Constructor for FileList
	 */
	public TaskFile(File file) {
		this.file = file;
		if (file.exists()) {
			readFile();
		} else {
			writeFile();
		}
	}
	
	public void setListener(Listener l) {
		listener = l;
	}
	
	public void add(Task word) {
		list.add(word);
		writeFile();
		if (listener != null)
			listener.added(word);
	}
	
	public void remove(Task word) {
		list.remove(word);
		writeFile();
		if (listener != null)
			listener.removed(word);
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
	
	private void writeFile() {
		try {
			OutputStream os = new FileOutputStream(file);
			DataOutputStream data = new DataOutputStream(os);
			data.writeInt(list.size());
			Iterator iter = list.iterator();
			while (iter.hasNext()) {
				Task word = (Task)iter.next();
				data.writeUTF(word.toString());
			}
			data.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	private void readFile() {
		try {
			InputStream is = new FileInputStream(file);
			DataInputStream data = new DataInputStream(is);
			int size = data.readInt();
			for (int nX = 0; nX < size; nX ++) {
				String str = data.readUTF();
				String [] taskElems = str.split(":", 3);
				list.add(new Task(taskElems[0], taskElems[1], taskElems[2]));
			}
			System.out.println(list.size());
			data.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}

