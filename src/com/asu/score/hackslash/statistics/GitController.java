package com.asu.score.hackslash.statistics;

import java.io.File;
import java.io.IOException;

import org.eclipse.jgit.api.Git;
import org.eclipse.jgit.api.errors.GitAPIException;
import org.eclipse.jgit.lib.Repository;

/**
 * Singleton Class to access Git APIs
 *
 */
public class GitController {

	private static GitController gitCtrl;

	/**
	 * Repository path.
	 */
	private String gitPath = "https://github.com/ser515asu/DSD-hackSlash.git";
	private Repository repo;
	private Git git;

	/**
	 * Returns the instance of the singleton class
	 * 
	 * @return
	 */
	public static synchronized GitController getInstance() {
		// Synchronize to ensure that we don't end up creating two singletons
		if (null == gitCtrl) {
			gitCtrl = new GitController();
		}
		return gitCtrl;
	}

	private GitController() {
		try {
			File localPath = File.createTempFile("tempRepo", "");
	        localPath.delete();
	        System.out.println("Cloning from " + gitPath + " to " + localPath);
			git = Git.cloneRepository()
			        .setURI(gitPath)
			        .setDirectory(localPath)
			        .call();
			repo = git.getRepository();
		} catch (GitAPIException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public String getGitPath() {
		return gitPath;
	}


	public Repository getRepo() {
		return repo;
	}

	public void setRepo(Repository repo) {
		this.repo = repo;
	}

	public Git getGit() {
		return git;
	}

	public void setGit(Git git) {
		this.git = git;
	}

	public static void main(String... args) {
		GitController gt = getInstance();
		System.out.println(gt.getRepo());
	}
}
