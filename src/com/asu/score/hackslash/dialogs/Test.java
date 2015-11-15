package com.asu.score.hackslash.dialogs;

import java.io.File;

public class Test {
	public static void main(String[] args) throws Exception{
		
		DisplayGit d = new DisplayGit();
		d.showCommitMeter();
		
		System.out.println(System.getProperty("user.dir"));
		
		
		File currentDirFile = new File(".");
		String helper = currentDirFile.getAbsolutePath();
		String currentDir = helper.substring(0, helper.length() - currentDirFile.getCanonicalPath().length());
		

		System.out.println(helper);
	}
}
