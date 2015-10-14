package com.asu.score.hackslash.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import com.asu.score.hackslash.engine.Database;
import com.asu.score.hackslash.properties.SQLQueries;

public class UsersDAO {
	Connection conn = null;
	PreparedStatement pst = null;

	ResultSet rs = null;
	List<String> users_list = new ArrayList<String>();

	public List<String> getUsers() {

		try {

			conn = new Database().getConnection();
			pst = conn.prepareStatement(SQLQueries.SQL_GET_USERS);
			rs = pst.executeQuery();
			while (rs.next()) {
				users_list.add(rs.getString("userID"));
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		// finally {
		// conn.close();
		// }
		return users_list;
	}

	public static void main(String... args) {
		System.out.println("Hello DAO");
		UsersDAO one = new UsersDAO();
		one.getUsers();

	}
}
