package com.asu.score.hackslash.properties;

public class SQLQueries {
	
	public static final String SQL_GET_USERS = "SELECT userID FROM PROFILE";
	public static final String SQL_TASK_DETAILS = "SELECT A.USERID,A.StartDate,A.EndDate FROM ALLOCATION AS A, TASK AS T WHERE T.TASKID = A.TASKID  AND T.TASKNAME =''";
		

}
