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

	public String addTask(Task task) throws Exception {

		System.out.println("adding in DB");
		Connection con = null;
		String taskId = null;
		AllocationDAO allo = new AllocationDAO();
		Calendar calendar = Calendar.getInstance();
		Statement stmt = null;
		java.sql.Timestamp startDate = new java.sql.Timestamp(calendar.getTime().getTime());
		try {
			con = Database.getConnection();
			// Locking tables to prevent simultaneous inserts in task
			// and allocation tables

			String lockQuery = "Lock tables task write, allocation write;";
			stmt = con.createStatement();
			stmt.executeUpdate(lockQuery);
			String status = null;
			if (task.getStatus().equals("New")){
				status = "N";
			}
			else if(task.getStatus().equals("In Progress")){
				status = "I";
			}
			else {
				status = "C";
			}
			String query = "Insert into Task(TaskName, TaskDscription, StartDate, Status) values(\"" + task.getName() + "\",\""
					+ task.getDesc() + "\",'" + startDate + "',\"" + status + "\");";
			System.out.println(query);
			stmt = con.createStatement();
			stmt.executeUpdate(query);
			// Getting taskID of the recently created task.
			// This function would return the same taskID for task identification.
			taskId = getTaskID(con);
			allo.setAllocation(con, taskId, task.getAssignedTo(), startDate);
			// Releasing table locks...
			String unlockQuery = "Unlock tables;";
			stmt = con.createStatement();
			stmt.executeUpdate(unlockQuery);
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			throw new Exception("Unable to Add Task");
		} finally {
			con.close();
		}
		return taskId;
	}
	
	public String updateTask(Task task) throws Exception {

		System.out.println("updating a task in DB");
		Connection con = null;
		String taskId = null;
		String userId = null;
		AllocationDAO allo = new AllocationDAO();
		Calendar calendar = Calendar.getInstance();
		Statement stmt = null;
		java.sql.Timestamp date = new java.sql.Timestamp(calendar.getTime().getTime());
		
		try {
			con = Database.getConnection();
			// Locking tables to prevent simultaneous inserts in task
			// and allocation tables
			String lockQuery = "Lock tables task write, allocation write;";
			stmt = con.createStatement();
			stmt.executeUpdate(lockQuery);
			String status = null;
			if (task.getStatus().equals("New")){
				status = "N";
			}
			else if(task.getStatus().equals("In Progress")){
				status = "I";
			}
			else {
				status = "C";
			}
			//New/In Progress Task Update
			if (status.equals("N") || status.equals("I")){
			System.out.println("New/In Progress Task Update");
			String updateQuery = "Update task set TaskName=\"" + task.getName() + "\", TaskDscription=\"" + task.getDesc() + "\", Status=\"" + status + "\" where TaskID = \"" + task.getTaskID() + "\";";
			System.out.println(updateQuery);
			stmt = con.createStatement();
			stmt.executeUpdate(updateQuery);
			taskId = task.getTaskID();
			userId = allo.getAllocation(con, taskId);
			// To check if there is any change in task allocation
			// Incase of any change, update and set allocation table
			if (!(userId.equals(task.getAssignedTo()))){
				
				allo.updateAllocation(con, taskId, userId, date);
				allo.setAllocation(con, taskId, task.getAssignedTo(), date);
			}
			}
			//Closing a task
			else {
				System.out.println("Closing a task");
				String updateQuery = "Update task set enddate='" + date + "', Status=\"C\" where TaskID = \"" + task.getTaskID() + "\";";
				System.out.println(updateQuery);
				stmt = con.createStatement();
				stmt.executeUpdate(updateQuery);
				taskId = task.getTaskID();
				userId = allo.getAllocation(con, taskId);
				allo.updateAllocation(con, taskId, userId, date);
			}
			// Release table locks after insert and update actions.
			String unlockQuery = "Unlock tables;";
			stmt = con.createStatement();
			stmt.executeUpdate(unlockQuery);
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			throw new Exception("Unable to update Task");
		} finally {
			con.close();
		}
		return taskId;
	}

	
	private String getTaskID(Connection con) throws SQLException {
		Statement stmt = null;
		String query = "Select max(TaskID) tskid from Task;";
		String taskid = null;
		try {
			//con = Database.getConnection();
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
				//con.close();
			}
		}
		return taskid;
	}

	public List<Task> getTasks() throws Exception {
		System.out.println("getting from DB");
		Connection con = null;
		Statement stmt = null;
		//String query = "Select * from Task";
		String query = "Select t.taskid, t.taskname, t.TaskDscription, t.Status, a.userid from Task t, allocation a where t.taskid = a.taskid and a.enddate = timestamp(0);";
		// where taskid in (Select max(TaskID) tskid from Task)
		List<Task> tasks = new ArrayList<Task>();
		try {
			con = Database.getConnection();
			stmt = con.createStatement();
			ResultSet rs = stmt.executeQuery(query);
			while (rs.next()) {
				String status = null;
				if ( rs.getString("Status").equals("N"))
					status = "New";
				else if ( rs.getString("Status").equals("I"))
					status = "In Progress";
				else
					status = "Closed";
				
				tasks.add(new Task(rs.getString("TaskName"), rs.getString("TaskDscription"), rs.getString("userid"), rs.getString("TaskID"), status));
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
