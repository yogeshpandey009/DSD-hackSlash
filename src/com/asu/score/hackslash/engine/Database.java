package com.asu.score.hackslash.engine;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.Statement;

import com.asu.score.hackslash.properties.Constants;

public class Database {
	
	public Connection setConnection(final String USER, final String PASS) {
		Connection conn = null;
		Statement stmt = null;
		
		try {
			Class.forName("com.mysql.jdbc.Driver");
			System.out.println("Connecting to database...");
		    conn = DriverManager.getConnection(Constants.DB_URL,USER,PASS);		    
		    System.out.println("Connected...");
		    
		    
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return conn;
	}
	
	public static void main(String[] args) {
		Database db = new Database();
		System.out.println(db.setConnection("root", "1qaz2wsx"));
	}
}
