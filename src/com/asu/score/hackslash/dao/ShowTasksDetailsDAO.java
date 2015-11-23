package com.asu.score.hackslash.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

import com.asu.score.hackslash.engine.Database;
import com.asu.score.hackslash.model.ShowTaskDetails;
import com.asu.score.hackslash.properties.SQLQueries;

public class ShowTasksDetailsDAO {
	
	Connection conn = null;
	PreparedStatement pst = null;
	String sql = "";
	String taskName = "";
	String taskId = "";
	
	ResultSet rs = null;
	List<String> user_id_list = new ArrayList<String>();
	List<Timestamp> start_date_list = new ArrayList<Timestamp>();
	List<Timestamp> end_date_list = new ArrayList<Timestamp>();
	List task_details = new ArrayList<>();
	ShowTaskDetails std;
	List<ShowTaskDetails> details = new ArrayList<ShowTaskDetails>();
	SimpleDateFormat smpldtFrmt = new SimpleDateFormat("MM/dd/yyyy HH:mm:ss");
	
	//public List<String> getTaskDetails() {
	public List<ShowTaskDetails> getTaskDetails() {

		try {
			try {
				
				conn = Database.getConnection();
				sql = "SELECT A.USERID,A.StartDate,A.EndDate FROM ALLOCATION AS A WHERE A.TASKID = '"+getTaskId()+"' ";
				
				System.out.println("getTaskName()"+getTaskName());
				pst = conn.prepareStatement(sql);
				rs = pst.executeQuery();
				while (rs.next()) {
					std = new ShowTaskDetails();
					//user_id_list.add(rs.getString("userID"));
					//start_date_list.add(rs.getTimestamp("StartDate"));
					//end_date_list.add(rs.getTimestamp("EndDate"));
					if(rs.getString("userID")!= null)
						std.setUser_id(rs.getString("userID"));
					if((rs.getTimestamp("StartDate")) != null)
						std.setStart_dt(smpldtFrmt.format(rs.getTimestamp("StartDate")));
					if((rs.getTimestamp("EndDate")) != null)
						std.setEnd_dt(smpldtFrmt.format(rs.getTimestamp("EndDate")));
					details.add(std);
					
				}
			} finally {
				if (pst != null) {
					pst.close();
				}
				if (conn != null) {
					conn.close();
				}
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		task_details.add(user_id_list);
		task_details.add(start_date_list);
		task_details.add(end_date_list);
		//return task_details;
		return details;
	}

	public String getTaskName() {
		// TODO Auto-generated method stub
		return taskName;
	}
	
	public void setTaskName(String taskName){
		this.taskName = taskName;
		
	}

	public String getTaskId() {
		return taskId;
	}

	public void setTaskId(String taskId) {
		this.taskId = taskId;
	}
	
	

}
