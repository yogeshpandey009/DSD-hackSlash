package com.asu.score.hackslash.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.asu.score.hackslash.engine.Database;
import com.asu.score.hackslash.properties.SQLQueries;
import com.asu.score.hackslash.sessionloghelper.UserSessionLog;

public class UsersDAO {
	Connection conn = null;
	PreparedStatement pst = null;

	ResultSet rs = null;
	List<String> users_list = new ArrayList<String>();

	public List<String> getUsers() {

		try {
			conn = Database.getConnection();
			pst = conn.prepareStatement(SQLQueries.SQL_GET_USERS);
			rs = pst.executeQuery();
			while (rs.next()) {
				users_list.add(rs.getString("userID"));
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return users_list;
	}

	public void addUserSessionTime(String username, Date loginTime) {
		Connection conn = null;
		Statement stmt = null;
		Timestamp login = new Timestamp(loginTime.getTime());
		Timestamp logout = new Timestamp(System.currentTimeMillis());
		try {
			conn = Database.getConnection();
			stmt = conn.createStatement();
			String query = "Insert into UsersSessionLog(UserName, LoginTime, LogoutTime) values(\"" + username + "\",\""
					+ login + "\",\"" + logout + "\");";
			System.out.println(query);
			stmt.executeUpdate(query);
		} catch(SQLException se) {
			System.out.println("Unable to add user Session Time");
			se.printStackTrace();
		}
	}
	
	public List<UserSessionLog> getUserSessionLog(String username) throws Exception {
		System.out.println("getting from DB");
		Connection con = null;
		Statement stmt = null;
		String query = "SELECT LoginTime, LogoutTime FROM UsersSessionLog WHERE UserName = \"" + username + "\";";
		List<UserSessionLog> sessionLog = new ArrayList<UserSessionLog>();
		try {
			con = Database.getConnection();
			stmt = con.createStatement();
			ResultSet rs = stmt.executeQuery(query);
			while (rs.next()) {
				sessionLog.add(new UserSessionLog(rs.getTimestamp("LoginTime"), rs.getTimestamp("LogoutTime")));
			}
		} catch (Exception ex) {
			ex.printStackTrace();
			throw new Exception("Unable to fetch tasks");
		} finally {
			if (stmt != null) {
				stmt.close();
			}
		}
		return sessionLog;
	}

	public static void main(String... args) {
		System.out.println("Hello DAO");
		UsersDAO one = new UsersDAO();
		one.getUsers();

	}
}
