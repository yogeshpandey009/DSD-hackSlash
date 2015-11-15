package com.asu.score.hackslash.statistics;

import java.io.IOException;
import java.util.*;

import org.eclipse.jgit.api.Git;
import org.eclipse.jgit.api.errors.GitAPIException;
import org.eclipse.jgit.internal.storage.file.FileRepository;
import org.eclipse.jgit.lib.Constants;
import org.eclipse.jgit.lib.PersonIdent;
import org.eclipse.jgit.lib.Ref;
import org.eclipse.jgit.lib.Repository;
import org.eclipse.jgit.revwalk.RevCommit;
import org.eclipse.jgit.revwalk.RevWalk;
import org.gitective.core.CommitFinder;
import org.gitective.core.filter.commit.AuthorSetFilter;
import org.gitective.core.filter.commit.DiffLineCountFilter;
import org.gitective.core.stat.AuthorHistogramFilter;
import org.gitective.core.stat.CommitCalendar;
import org.gitective.core.stat.Month;
import org.gitective.core.stat.UserCommitActivity;
import org.gitective.core.stat.YearCommitActivity;

public class GitData {
	private static final String GIT_PATH = "C:/Users/Mihir/Desktop/Fall 2015/SER 515 - Software Inception/DSDO/DSD-hackSlash/.git";
	private static final String REMOTE_URL = "https://github.com/ser515asu/DSD-hackSlash.git";

	public Set<String> getContributor() throws IOException{
		//Repository repo = new FileRepository(GIT_PATH);
		Repository repo = GitController.getInstance().getRepo();
		//Git git = new Git(repo);
		AuthorSetFilter authors = new AuthorSetFilter();
		CommitFinder afinder = new CommitFinder(repo);
		afinder.setFilter(authors).find();
		Set<String> set = new HashSet<String>();
		for (PersonIdent author : authors.getPersons()){
				set.add(author.getName());
			/*System.out.println(author);*/
		}
			
		return set;
	}
	public Map<String,Integer> getCommitsPerContributor() throws IOException {
		//Repository repo = GitController.getInstance().getRepo();
		Repository repo = new FileRepository(GIT_PATH);
		//Git git = new Git(repo);
		Map<String,Integer> map = new HashMap<String,Integer>();
		AuthorHistogramFilter afilter = new AuthorHistogramFilter();
		CommitFinder cfinder = new CommitFinder(repo);
		cfinder.setFilter(afilter).find();
		UserCommitActivity[] activity = afilter.getHistogram()
				.getUserActivity();
		
		for (UserCommitActivity user : activity){
			map.put(user.getName(), user.getCount());
			System.out.println(user.getName() + " has done " + user.getCount()
			+ " commits.");
			
		}
		return map;
	}

	
	public List<List<String>> getMonthlyCommits() throws IOException{
		//Repository repo = GitController.getInstance().getRepo();
		Repository repo = new FileRepository(GIT_PATH);
		//Git git = new Git(repo);
		List<List<String>> all = new ArrayList<List<String>>();
		
		AuthorHistogramFilter afilter = new AuthorHistogramFilter();
		CommitFinder cfinder = new CommitFinder(repo);
		cfinder.setFilter(afilter).find();
		UserCommitActivity[] activity = afilter.getHistogram()
				.getUserActivity();
		CommitCalendar commits = new CommitCalendar(activity);

		for (YearCommitActivity year : commits.getYears()) {
			int m = 0;
			for (int c : year.getMonths()) {
				String month =  Month.month(m) + "-" + year.getYear();
				List<String> mo = new ArrayList<String>();
				mo.add(month);
				mo.add(String.valueOf(c));
				all.add(mo);
				//map.put(month,c);
				System.out.println(c + " commits in " + Month.month(m) + "-"
						+ year.getYear());
				m++;
			}
		}
		return all;
	}
	public List<List<String>> getGitCommitLog() throws IOException, GitAPIException{
		//Repository repo = GitController.getInstance().getRepo();
		Repository repo = new FileRepository(GIT_PATH);
		Git git = new Git(repo);
		RevWalk walk = new RevWalk(repo);
		List<List<String>> log = new ArrayList<List<String>>();
		List<String> logRow = new ArrayList();
		List<Ref> branches = git.branchList().call();
		for (Ref branch : branches) {
			String branchName = branch.getName();

			System.out.println("Commits of branch: " + branch.getName());
			System.out.println("-------------------------------------");

			Iterable<RevCommit> commits = git.log().all().call();

			for (RevCommit commit : commits) {
				boolean foundInThisBranch = false;

				RevCommit targetCommit = walk.parseCommit(repo.resolve(commit
						.getName()));
				for (Map.Entry<String, Ref> e : repo.getAllRefs().entrySet()) {
					if (e.getKey().startsWith(Constants.R_HEADS)) {
						if (walk.isMergedInto(targetCommit,
								walk.parseCommit(e.getValue().getObjectId()))) {
							String foundInBranch = e.getValue().getName();
							if (branchName.equals(foundInBranch)) {
								foundInThisBranch = true;
								break;
							}
						}
					}
				}

				if (foundInThisBranch) {
					logRow.add(commit.getName());
					logRow.add(commit.getAuthorIdent().getName());
					long time = commit.getCommitTime();
					time = time*1000;
					logRow.add(String.valueOf(new Date(time)));
					logRow.add(commit.getFullMessage());
					System.out.println(commit.getName());
					System.out.println(commit.getAuthorIdent().getName());
					System.out.println(new Date(commit.getCommitTime()));
					System.out.println(commit.getFullMessage());
				}
			log.add(logRow);
			}
		}		
		return log;
	}
	
	public int getTotalCurrentMonthCommits() throws IOException{
		Repository repo = GitController.getInstance().getRepo();
		//Repository repo = new FileRepository(GIT_PATH);
		Git git = new Git(repo);
		AuthorHistogramFilter afilter = new AuthorHistogramFilter();
		CommitFinder cfinder = new CommitFinder(repo);
		cfinder.setFilter(afilter).find();
		UserCommitActivity[] activity = afilter.getHistogram()
				.getUserActivity();
		CommitCalendar commits = new CommitCalendar(activity);
		
		int c = commits.getMonthCount(Calendar.getInstance().get(Calendar.MONTH)); 
			System.out.println(c);
		
		return c;
	}
	
	public int getTotalCommits() throws IOException{
		//Repository repo = GitController.getInstance().getRepo();
		Repository repo = new FileRepository(GIT_PATH);
		Git git = new Git(repo);
		AuthorHistogramFilter afilter = new AuthorHistogramFilter();
		CommitFinder cfinder = new CommitFinder(repo);
		cfinder.setFilter(afilter).find();
		UserCommitActivity[] activity = afilter.getHistogram()
				.getUserActivity();
		CommitCalendar commits = new CommitCalendar(activity);
		int count = 0;
		for (YearCommitActivity year : commits.getYears()) {
			int m = 0;
			for (int c : year.getMonths()) {
				count = count + c;
			}
		}
		return count;
	}
	
	public List<Integer> getLocChange() throws IOException {
		//Repository repo = GitController.getInstance().getRepo();
		Repository repo = new FileRepository(GIT_PATH);
		Git git = new Git(repo);
		CommitFinder finder = new CommitFinder(repo);
		DiffLineCountFilter filter = new DiffLineCountFilter();
		finder.setFilter(filter);
		finder.find();
		List<Integer> loc = new ArrayList<Integer>();
		loc.add((int) filter.getAdded());
		loc.add((int) filter.getEdited());
		loc.add((int) filter.getDeleted());
		loc.add((int) filter.getAdded() + (int)filter.getEdited());
		System.out.println("Added:\t" + filter.getAdded());
		System.out.println("Changed:\t" + filter.getEdited());
		System.out.println("Deleted:\t" + filter.getDeleted());
		System.out.println("CSI:\t" + (filter.getAdded() + filter.getEdited()));
		return loc;
		
	}
	
	public static void main(String[] args) throws IOException, GitAPIException{
		GitData git = new GitData();
		//Set<String> set = git.getContributor();
		//Map<String,Integer> map = git.getCommitsPerContributor();
		//List<List<String>> mo = git.getMonthlyCommits();
		List<List<String>> log = git.getGitCommitLog();
		//int c = git.getTotalCurrentMonthCommits();
		//System.out.println(set);
		System.out.println(log);
		//System.out.println(map2);
		//System.out.println(c);
	}
}
