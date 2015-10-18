DROP DATABASE IF EXISTS hackSlash

CREATE DATABASE hackslash;

USE hackslash;


CREATE TABLE Task(TaskID int NOT NULL AUTO_INCREMENT PRIMARY KEY, TaskName varchar(50), TaskDscription varchar(200));

CREATE TABLE Allocation(TaskAllocationID int NOT NULL AUTO_INCREMENT PRIMARY KEY, TaskID int , UserID int, StartDate timestamp, EndDate timestamp, FOREIGN KEY (TaskID) REFERENCES Task(TaskID));