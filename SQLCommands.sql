DROP DATABASE IF EXISTS hackSlash

CREATE DATABASE hackslash;

USE hackslash;


CREATE TABLE Task(TaskID int NOT NULL AUTO_INCREMENT PRIMARY KEY, TaskName varchar(50), TaskDscription varchar(200), StartDate TIMESTAMP, EndDate TIMESTAMP, Status Char(1));

CREATE TABLE Allocation(TaskAllocationID int NOT NULL AUTO_INCREMENT PRIMARY KEY, TaskID int , UserID varchar(50), StartDate timestamp, EndDate timestamp, FOREIGN KEY (TaskID) REFERENCES Task(TaskID));