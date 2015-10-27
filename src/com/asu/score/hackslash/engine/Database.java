package com.asu.score.hackslash.engine;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import com.asu.score.hackslash.properties.Constants;

public class Database {

	static {
        try {
			Class.forName("com.mysql.jdbc.Driver");
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}
    }
	
	public static Connection getConnection() throws SQLException {
		return DriverManager.getConnection(Constants.DB_URL, Constants.DB_USER_NAME, Constants.DB_PASSWORD);
	}

	/**
    * @param query String The query to be executed
    * @return a ResultSet object containing the results or null if not available
    * @throws SQLException
    */
   public static ResultSet query(String query) throws SQLException{
	   Connection conn = getConnection();
       Statement statement = conn.createStatement();
       ResultSet res = statement.executeQuery(query);
       return res;
   }

   /**
    * @desc Method to insert data to a table
    * @param insertQuery String The Insert query
    * @return boolean
    * @throws SQLException
    */
   public static int insert(String insertQuery) throws SQLException {
	   Connection conn = getConnection();
       Statement statement = conn.createStatement();
       int result = statement.executeUpdate(insertQuery);
       return result;

   }

	
	public static void main(String[] args) {
		try {
			Database.getConnection();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

}
