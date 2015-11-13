package com.asu.score.hackslash.statistics;

import java.io.IOException;
import java.util.*;

import org.eclipse.jgit.api.Git;
import org.eclipse.jgit.internal.storage.file.FileRepository;
import org.eclipse.jgit.lib.PersonIdent;
import org.eclipse.jgit.lib.Repository;
import org.gitective.core.CommitFinder;
import org.gitective.core.filter.commit.AuthorSetFilter;
import org.gitective.core.stat.AuthorHistogramFilter;
import org.gitective.core.stat.CommitCalendar;
import org.gitective.core.stat.Month;
import org.gitective.core.stat.UserCommitActivity;
import org.gitective.core.stat.YearCommitActivity;

public class GitData {
	private static final String GIT_PATH = "/Users/Mihir/Desktop/Fall 2015/SER 515 - Software Inception/DSDO/DSD-hackSlash/.git";
	private static final String REMOTE_URL = "https://github.com/ser515asu/DSD-hackSlash.git";

	public Set<String> getContributor() throws IOException{
		Repository repo = new FileRepository(GIT_PATH);
		Git git = new Git(repo);
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
	public Map<String,String> getCommitsPerContributor() throws IOException{
		Repository repo = new FileRepository(GIT_PATH);
		Git git = new Git(repo);
		Map<String,String> map = new HashMap<String,String>();
		AuthorHistogramFilter afilter = new AuthorHistogramFilter();
		CommitFinder cfinder = new CommitFinder(repo);
		cfinder.setFilter(afilter).find();
		UserCommitActivity[] activity = afilter.getHistogram()
				.getUserActivity();
		
		for (UserCommitActivity user : activity){
			map.put(user.getName(), String.valueOf(user.getCount()));
			System.out.println(user.getName() + " has done " + user.getCount()
			+ " commits.");
			
		}
		return map;
	}
	public Map<String,String> getMonthlyCommits() throws IOException{
		Repository repo = new FileRepository(GIT_PATH);
		Git git = new Git(repo);
		Map<String,String> map = new HashMap<String,String>();
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
				map.put(month,String.valueOf(c));
				System.out.println(c + " commits in " + Month.month(m) + "-"
						+ year.getYear());
				m++;
			}
		}
		return map;
	}
	public int getTotalCurrentMonthCommits() throws IOException{
		Repository repo = new FileRepository(GIT_PATH);
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
	
	
	public static void main(String[] args) throws IOException{
		GitData git = new GitData();
		Set<String> set = git.getContributor();
		Map<String,String> map = git.getCommitsPerContributor();
		Map<String,String> map2 = git.getMonthlyCommits();
		int c = git.getTotalCurrentMonthCommits();
		System.out.println(set);
		System.out.println(map);
		System.out.println(map2);
		System.out.println(c);
	}
}
