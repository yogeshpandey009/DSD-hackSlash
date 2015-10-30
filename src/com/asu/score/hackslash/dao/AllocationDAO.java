package com.asu.score.hackslash.dao;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Calendar;

import com.asu.score.hackslash.engine.Database;

public class AllocationDAO {
	static Connection con = null;
	
	public void setAllocation(Connection con, String TaskId, String UserId, java.sql.Timestamp StartDate) throws SQLException
	{
		Statement stmt = null;
		String query = "Insert into Allocation(TaskID, UserID, StartDate) values (\"" + TaskId + "\",\"" + UserId + "\",'" + StartDate + "');";
		System.out.println(query);
		try
		{
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
	
	public void updateAllocation(Connection con, String TaskId, String UserId, java.sql.Timestamp date) throws SQLException
	{
		Statement stmt = null;
		//String query = "Insert into Allocation(TaskID, UserID, StartDate) values (\"" + TaskId + "\",\"" + UserId + "\",'" + StartDate + "')";
		String query = "Update Allocation set UserID=\"" + UserId + "\", EndDate='" + date + "' where TaskID = \"" + TaskId + "\" and EndDate = timestamp(0)";
		System.out.println(query);
		try
		{
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
	
	public String getAllocation(Connection con, String TaskId) throws SQLException
	{
		Statement stmt = null;
		String query = "Select UserID from Allocation where enddate=timestamp(0) and TaskId = \"" + TaskId+ "\";";
		String taskid = null;
		try
		{
			stmt = con.createStatement();
			ResultSet rs = stmt.executeQuery(query);
			while (rs.next())
			{				
				taskid = rs.getString("UserID");
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
		return taskid;
	}
	public static void main(String[] args){
		AllocationDAO t = new AllocationDAO();
		try {
			Calendar calendar = Calendar.getInstance();
		    java.sql.Timestamp startDate = new java.sql.Timestamp(calendar.getTime().getTime());
			t.setAllocation(Database.getConnection(),"2", "USER 2", startDate);
			t.getAllocation(Database.getConnection(), null);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
