package com.asu.score.hackslash.engine;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.Statement;

import com.asu.score.hackslash.properties.Constants;

public class Database {
	
	public static void setConnection(final String USER, final String PASS){
		Connection conn = null;
		Statement stmt = null;
		
		try {
			Class.forName("com.mysql.jdbc.Driver");
			System.out.println("Connecting to database...");
		    conn = DriverManager.getConnection(Constants.DB_URL,USER,PASS);
		    System.out.println("Check!!");
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public static void main(String[] args) {
		setConnection("root", "1qaz2ws");
	}
}
