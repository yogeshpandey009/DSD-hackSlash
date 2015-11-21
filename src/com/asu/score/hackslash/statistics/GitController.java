package com.asu.score.hackslash.statistics;

import java.io.File;
import java.io.IOException;

import org.eclipse.jgit.api.Git;
import org.eclipse.jgit.api.errors.GitAPIException;
import org.eclipse.jgit.lib.Repository;
import org.eclipse.jgit.storage.file.FileRepositoryBuilder;

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
	private String filePath = "./hackslash/.git";

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
		try {
			gitCtrl.getGit().pull().call();
			System.out.println("Pulling remote repository");
		} catch (GitAPIException e) {
			e.printStackTrace();
			System.out
					.println("Exception occured while pulling remote repository");
		}
		return gitCtrl;
	}

	private GitController() {
		try {
			File gitDir = new File(filePath);
			git = openOrCreate(gitDir);
			repo = git.getRepository();

		} catch (GitAPIException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	static Git openOrCreate(File gitDirectory) throws IOException,
			GitAPIException {
		Git git;
		FileRepositoryBuilder repositoryBuilder = new FileRepositoryBuilder();
		repositoryBuilder.addCeilingDirectory(gitDirectory);
		repositoryBuilder.findGitDir(gitDirectory);
		if (repositoryBuilder.getGitDir() == null) {
			git = Git.init().setDirectory(gitDirectory.getParentFile()).call();
		} else {
			git = new Git(repositoryBuilder.build());
		}
		return git;
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
