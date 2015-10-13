package com.asu.score.hackslash.engine;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

import com.asu.score.hackslash.properties.Constants;

public class Database {

	Connection conn = null;
	Statement stmt = null;

	public void setConnection(final String USER, final String PASS) throws SQLException {

		try {
			Class.forName("com.mysql.jdbc.Driver");
			System.out.println("Connecting to database...");
			conn = DriverManager.getConnection(Constants.DB_URL, USER, PASS);
			System.out.println("Check!!");
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
			conn.close();
		}


	}

	// public static void main(String[] args) {
	// Database db = new Database();
	// db.setConnection("root", "1qaz2wsx");
	// }

	public Connection getConnection() throws SQLException {

		try {
			Class.forName("com.mysql.jdbc.Driver");
			conn = DriverManager.getConnection(Constants.DB_URL, Constants.DB_USER_NAME, Constants.DB_PASSWORD);
		} catch (ClassNotFoundException | SQLException ex) {
			ex.printStackTrace();
		}
		return conn;
	}


	
	public static void main(String[] args) {
		Database db = new Database();
		try {
			db.setConnection("root", "1qaz2wsx");
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}
