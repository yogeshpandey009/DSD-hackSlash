package com.asu.score.hackslash.databaseaccess;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.Statement;

import com.asu.score.hackslash.properties.Constants;
import com.asu.score.hackslash.engine.Database;;

public class Task {
	public static void main(String[] args) {
		Database db = new Database();
		db.setConnection("root", "1qaz2wsx");
	}
}
