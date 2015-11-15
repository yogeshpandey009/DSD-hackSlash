package com.asu.score.hackslash.git.test;

import java.io.File;
import java.io.IOException;
import java.util.Collection;
import java.util.Date;
import java.util.List;
import java.util.Map;

import org.eclipse.jgit.api.CloneCommand;
import org.eclipse.jgit.api.Git;
import org.eclipse.jgit.api.errors.GitAPIException;
import org.eclipse.jgit.api.errors.NoHeadException;
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

public class GitTest {

	//private static final String GIT_PATH = "/Users/yogeshpandey/Documents/dsd/DSD-hackSlash/.git";

	private static final String REMOTE_URL = "https://github.com/ser515asu/DSD-hackSlash.git";

	public static void main(String... args) throws IOException,
			NoHeadException, GitAPIException {
		
		File localPath = File.createTempFile("TestGitRepository", "");
        localPath.delete();

        System.out.println("Cloning from " + REMOTE_URL + " to " + localPath);
       
        	
        Git git = Git.cloneRepository()
        .setURI( REMOTE_URL )
        .setDirectory(localPath)
        .call();
        
        
        Repository repo = git.getRepository();
        
        
        System.out.println("Having repository: " + repo.getDirectory());
	    
		
		RevWalk walk = new RevWalk(repo);
	
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
					System.out.println(commit.getName());
					System.out.println(commit.getAuthorIdent().getName());
					System.out.println(new Date(commit.getCommitTime()));
					System.out.println(commit.getFullMessage());
				}
			}
		}

		CommitFinder finder = new CommitFinder(repo);
		DiffLineCountFilter filter = new DiffLineCountFilter();
		finder.setFilter(filter);
		
		// ObjectId start = repository.resolve(b);
		// ObjectId end = repository.resolve(a);

		// finder.findBetween(start, end);
		finder.find();
		System.out.println("Added:\t" + filter.getAdded());
		System.out.println("Changed:\t" + filter.getEdited());
		System.out.println("Deleted:\t" + filter.getDeleted());

		System.out.println("CSI:\t" + (filter.getAdded() + filter.getEdited()));

		// System.out.println("Listing remote repository " + REMOTE_URL);
		// Collection<Ref> refs = Git.lsRemoteRepository()
		// .setHeads(true)
		// .setTags(true)
		// .setRemote(REMOTE_URL)
		// .call();
		//
		// for (Ref ref : refs) {
		// System.out.println("Ref: " + ref);
		// }

		AuthorHistogramFilter afilter = new AuthorHistogramFilter();
		CommitFinder cfinder = new CommitFinder(repo);
		cfinder.setFilter(afilter).find();
		UserCommitActivity[] activity = afilter.getHistogram()
				.getUserActivity();
		CommitCalendar commits = new CommitCalendar(activity);

		for (YearCommitActivity year : commits.getYears()) {
			int m = 0;
			for (int c : year.getMonths()) {
				System.out.println(c + " commits in " + Month.month(m) + "/"
						+ year.getYear());
				m++;
			}
		}

		for (UserCommitActivity user : activity)
			System.out.println(user.getName() + " has done " + user.getCount()
					+ " commits." );

		AuthorSetFilter authors = new AuthorSetFilter();
		CommitFinder afinder = new CommitFinder(repo);
		afinder.setFilter(authors).find();

		for (PersonIdent author : authors.getPersons())
			System.out.println(author);

		walk.dispose();
		repo.close();
		git.close();

	}

}
