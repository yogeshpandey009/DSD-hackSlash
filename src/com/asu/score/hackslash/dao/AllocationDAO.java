package com.asu.score.hackslash.dao;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import com.asu.score.hackslash.engine.Database;

public class AllocationDAO {
	static Connection con = null;
	
	public static void setAllocation(String TaskId, String UserId) throws SQLException
	{
		Statement stmt = null;
		String query = "Insert into Allocation(TaskID, UserID) values (" + TaskId + "," + UserId + ")";
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
	
	public static void getAllocation() throws SQLException
	{
		Statement stmt = null;
		String query = "Select TaskAllocationID, TaskID, UserID from Allocation";
		try
		{
			con = new Database().getConnection();
			stmt = con.createStatement();
			ResultSet rs = stmt.executeQuery(query);
			while (rs.next())
			{
				System.out.println(rs.getString("TaskAllocationID"));
				System.out.println(rs.getString("TaskID"));
				System.out.println(rs.getString("UserID"));
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
