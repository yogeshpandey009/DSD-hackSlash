package com.asu.score.hackslash.dao;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import com.asu.score.hackslash.engine.Database;
import com.asu.score.hackslash.taskhelper.Task;

public class TaskDao {

	public void addTask(Task task) throws Exception {

		System.out.println("adding in DB");
		TaskDao tsk = new TaskDao();
		Connection con = null;
		AllocationDAO allo = new AllocationDAO();
		Calendar calendar = Calendar.getInstance();
		Statement stmt = null;
		java.sql.Timestamp startDate = new java.sql.Timestamp(calendar.getTime().getTime());
		try {
			con = Database.getConnection();
			
			String query = "Insert into Task(TaskName, TaskDscription) values(\"" + task.getName() + "\",\""
					+ task.getDesc() + "\");";
			System.out.println(query);
			stmt = con.createStatement();
			stmt.executeUpdate(query);
			allo.setAllocation(con, tsk.getTaskID(con), task.getAssignTo(), startDate);
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			throw new Exception("Unable to Add Task");
		} finally {
			con.close();
		}
	}

	private String getTaskID(Connection con) throws SQLException {
		Statement stmt = null;
		String query = "Select max(TaskID) tskid from Task;";
		String taskid = null;
		try {
			con = Database.getConnection();
			stmt = con.createStatement();
			ResultSet rs = stmt.executeQuery(query);
			while (rs.next()) {
				taskid = rs.getString("tskid");
			}
		} catch (Exception ex) {
			ex.printStackTrace();
		} finally {
			if (stmt != null) {
				stmt.close();
			}
			if (con != null) {
				con.close();
			}
		}
		return taskid;
	}

	public List<Task> getTasks() throws Exception {
		System.out.println("getting from DB");
		Connection con = null;
		Statement stmt = null;
		//String query = "Select * from Task";
		String query = "Select t.taskname, t.TaskDscription, a.userid from Task t, allocation a where t.taskid = a.taskid and enddate = timestamp(0);";
		// where taskid in (Select max(TaskID) tskid from Task)
		List<Task> tasks = new ArrayList<Task>();
		try {
			con = Database.getConnection();
			stmt = con.createStatement();
			ResultSet rs = stmt.executeQuery(query);
			while (rs.next()) {
				tasks.add(new Task(rs.getString("TaskName"), rs.getString("TaskDscription"), rs.getString("userid")));
			}
		} catch (Exception ex) {
			ex.printStackTrace();
			throw new Exception("Unable to fetch tasks");
		} finally {
			if (stmt != null) {
				stmt.close();
			}
		}
		return tasks;
	}

	public static void main(String[] args) throws Exception {
		Database db = new Database();
		TaskDao t = new TaskDao();
		try {
			// t.setTask(db.getConnection(),"Task 1", "Task Desc 1");
			// System.out.println(t.getTaskID(db.getConnection()));
			System.out.println(t.getTasks());
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
