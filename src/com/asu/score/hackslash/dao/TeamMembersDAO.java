package com.asu.score.hackslash.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import com.asu.score.hackslash.engine.Database;
import com.asu.score.hackslash.properties.SQLQueries;

public class TeamMembersDAO {
	Connection conn = null;
	PreparedStatement pst = null;
	Statement stmt = null;

	ResultSet rs = null;
	List<String> usersList = new ArrayList<String>();

	public List<String> getUsers() {
		try{
			try {
				conn = Database.getConnection();
				pst = conn.prepareStatement(SQLQueries.SQL_GET_TEAMMEMBERS);
				rs = pst.executeQuery();
				while (rs.next()) {
					usersList.add(rs.getString("Username"));
				}
			} finally{
				pst.close();
				conn.close();
			}
		} catch(SQLException e){
			System.out.println("Database Error!!");
			e.printStackTrace();
		}
		return usersList;
	}
	
	public void addUser(String username){
		try {
			
			try{
				conn = Database.getConnection();
				stmt = conn.createStatement();
				String query = "INSERT INTO TEAMMEMBERS(Username) VALUES(\"" + username + "\");";
				System.out.println(query);
				stmt.executeUpdate(query);
			} finally {
				stmt.close();
				conn.close();
			}
		} catch(SQLException se) {
			System.out.println("Unable to add user to Team Member");
		}
	}
	
}
