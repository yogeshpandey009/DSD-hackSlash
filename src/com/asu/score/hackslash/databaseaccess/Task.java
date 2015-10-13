package com.asu.score.hackslash.databaseaccess;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import com.asu.score.hackslash.properties.Constants;
import com.asu.score.hackslash.engine.Database;;

public class Task {
	public void setTask(Connection con, String taskName, String taskDesc) 
		 {

		    Statement stmt = null;
		     //String query = "select COF_NAME, SUP_ID, PRICE, " +
		                  // "SALES, TOTAL " +
		                   //"from " + dbName + ".COFFEES";
		    try {
		    	//Database db = new Database();
				//db.setConnection("root", "1qaz2wsx");
				stmt = con.createStatement();
		        ResultSet rs = stmt.executeQuery("select * from profile");
		        while (rs.next()) {
		            String tname = rs.getString("FirstName");
		            System.out.println(tname);
		        }
		    } 
		    catch (Exception e ) {
		    	e.printStackTrace();
		    }
		    finally {
		        //if (stmt != null) { stmt.close(); }
		    }
		}
		
	public static void main(String[] args) {
		Database db = new Database();
		Task t = new Task();
		try {
			t.setTask(db.getConnection(), "", "");
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
