package com.asu.score.hackslash.statistics;

import java.io.IOException;

import org.eclipse.jgit.api.Git;
import org.eclipse.jgit.internal.storage.file.FileRepository;
import org.eclipse.jgit.lib.Repository;

/**
 * Singleton Class to access Git APIs
 *
 */
public class GitControl {

	private static GitControl gitCtrl;

	/**
	 * Repository path.
	 */
	private String gitPath = "C:/Bharat/Masters Material/GIT Repo/DSD-hackSlash/.git";
	private Repository repo;
	private Git git;

	/**
	 * Returns the instance of the singleton class
	 * 
	 * @return
	 */
	public static synchronized GitControl getInstance() {
		// Synchronize to ensure that we don't end up creating two singletons
		if (null == gitCtrl) {
			gitCtrl = new GitControl();
		}
		return gitCtrl;
	}

	public String getGitPath() {
		return gitPath;
	}

	public void setGitPath(String gitPath) {
		this.gitPath = gitPath;
	}

	public Repository getRepo() {
		if (repo == null) {
			try {
				repo = new FileRepository(gitPath);
			} catch (IOException e) {
				System.out.println("Unable to get repository!");
				e.printStackTrace();
			}
		}
		return repo;
	}

	public void setRepo(Repository repo) {
		this.repo = repo;
	}

	public Git getGit() {
		if (git == null) {
			git = new Git(getRepo());
		}
		return git;
	}

	public void setGit(Git git) {
		this.git = git;
	}

}
