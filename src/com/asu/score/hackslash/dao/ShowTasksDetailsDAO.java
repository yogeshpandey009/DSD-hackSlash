package com.asu.score.hackslash.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

import com.asu.score.hackslash.engine.Database;
import com.asu.score.hackslash.properties.SQLQueries;

public class ShowTasksDetailsDAO {
	
	Connection conn = null;
	PreparedStatement pst = null;
	String sql = "";
	String taskName = "";

	ResultSet rs = null;
	List<String> user_id_list = new ArrayList<String>();
	List<Timestamp> start_date_list = new ArrayList<Timestamp>();
	List<Timestamp> end_date_list = new ArrayList<Timestamp>();
	List task_details = new ArrayList<>();
	
	public List<String> getTaskDetails() {

		try {
			conn = Database.getConnection();
			sql = "SELECT A.USERID,A.StartDate,A.EndDate FROM ALLOCATION AS A, TASK AS T WHERE T.TASKID = A.TASKID "
					+ " AND T.TASKNAME = "+getTaskName();
			System.out.println("getTaskName()"+getTaskName());
			pst = conn.prepareStatement(sql);
			rs = pst.executeQuery();
			while (rs.next()) {
				user_id_list.add(rs.getString("userID"));
				start_date_list.add(rs.getTimestamp("StartDate"));
				end_date_list.add(rs.getTimestamp("EndDate"));
				
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		task_details.add(user_id_list);
		task_details.add(start_date_list);
		task_details.add(end_date_list);
		return task_details;
	}

	public String getTaskName() {
		// TODO Auto-generated method stub
		return taskName;
	}
	
	public void setTaskName(String taskName){
		this.taskName = taskName;
		
	}

}
