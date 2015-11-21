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
	private String filePath = "./temp/.git";

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
		File gitDir = new File(filePath);
		openOrCreateGitRepo(gitDir);
		repo = git.getRepository();
	}

	private void openOrCreateGitRepo(File gitDirectory) {
		FileRepositoryBuilder repositoryBuilder = new FileRepositoryBuilder();
		repositoryBuilder.addCeilingDirectory(gitDirectory);
		repositoryBuilder.findGitDir(gitDirectory);
		try {
			if (repositoryBuilder.getGitDir() == null) {
				System.out.println("cloning new repo");
				git = Git.cloneRepository().setURI(gitPath).setDirectory(gitDirectory.getParentFile()).call();
				//git = Git.init().setDirectory(gitDirectory.getParentFile()).call();
			} else {
				System.out.println("using existing repo");
				git = new Git(repositoryBuilder.build());
			}
		} catch (IllegalStateException | GitAPIException | IOException e) {
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
