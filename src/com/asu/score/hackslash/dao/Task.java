package com.asu.score.hackslash.dao;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import com.asu.score.hackslash.engine.Database;

public class Task {
	
	public void setTask(Connection con, String taskName, String taskDesc) throws SQLException
		 {

		    Statement stmt = null;
		    String query = "Insert into Task(TaskName, TaskDscription) values(\"" + taskName + "\",\"" + taskDesc + "\");";
		    System.out.println(query);
		    try {
		    	stmt = con.createStatement();
				stmt.executeUpdate(query);
		    } 
		    catch (Exception e ) {
		    	e.printStackTrace();
		    }
		    finally {
		        if (stmt != null) { stmt.close(); }
		    }
		}
	public static void getTask(Connection con) throws SQLException
	{
		Statement stmt = null;
		String query = "Select * from Task";
		try
		{
			stmt = con.createStatement();
			ResultSet rs = stmt.executeQuery(query);
			while (rs.next())
			{
				System.out.println(rs.getString("TaskID"));
				System.out.println(rs.getString("TaskName"));
				System.out.println(rs.getString("TaskDscription"));
			}
		}
		catch (Exception ex)
		{
			ex.printStackTrace();
		}
		finally 
		{
	        if (stmt != null) 
	        { 
	        	stmt.close(); 
	        }
	    }
	}

		
	public static void main(String[] args) {
		Database db = new Database();
		Task t = new Task();
		try {
			t.setTask(db.getConnection(),"Task 1", "Task Desc 1");
			t.getTask(db.getConnection());
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
