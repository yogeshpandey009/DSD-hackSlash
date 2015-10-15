package com.asu.score.hackslash.dao;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import com.asu.score.hackslash.engine.Database;

public class TaskLogDAO {
	static Connection con = null;
	
	public void setTaskLog(Connection con, String TaskAllocationID, String StartDate, String EndDate) throws SQLException
	{
		Statement stmt = null;
		String query = "Insert into TaskLog(TaskAllocationID, StartDate, EndDate) values (" +TaskAllocationID + "," + StartDate + "," + EndDate + ")";
		try
		{
			con = new Database().getConnection();
			stmt = con.createStatement();
			stmt.executeUpdate(query);
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
	
	public void getTaskLog(Connection con) throws SQLException
	{
		Statement stmt = null;
		String query = "Select TaskAllocationID, StartDate, EndDate from TaskLog";
		try
		{
			con = new Database().getConnection();
			stmt = con.createStatement();
			ResultSet rs = stmt.executeQuery(query);
			while (rs.next())
			{
				System.out.println(rs.getString("TaskAllocationID"));
				System.out.println(rs.getString("StartDate"));
				System.out.println(rs.getString("EndDate"));
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
}
