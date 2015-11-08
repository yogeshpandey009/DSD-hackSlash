package com.asu.score.hackslash.git.test;

import java.io.IOException;
import java.util.Date;
import java.util.List;
import java.util.Map;

import org.eclipse.jgit.api.Git;
import org.eclipse.jgit.api.errors.GitAPIException;
import org.eclipse.jgit.api.errors.NoHeadException;
import org.eclipse.jgit.internal.storage.file.FileRepository;
import org.eclipse.jgit.lib.Constants;
import org.eclipse.jgit.lib.Ref;
import org.eclipse.jgit.lib.Repository;
import org.eclipse.jgit.revwalk.RevCommit;
import org.eclipse.jgit.revwalk.RevWalk;
import org.gitective.core.CommitFinder;
import org.gitective.core.filter.commit.DiffLineCountFilter;

public class GitTest {
	
	private static final String GIT_PATH = "/Users/yogeshpandey/Documents/dsd/DSD-hackSlash/.git";

	public static void main(String... args) throws IOException, NoHeadException, GitAPIException {
		Repository repo = new FileRepository(GIT_PATH);
	    Git git = new Git(repo);
	    RevWalk walk = new RevWalk(repo);

	    List<Ref> branches = git.branchList().call();

	    for (Ref branch : branches) {
	        String branchName = branch.getName();

	        System.out.println("Commits of branch: " + branch.getName());
	        System.out.println("-------------------------------------");

	        Iterable<RevCommit> commits = git.log().all().call();

	        for (RevCommit commit : commits) {
	            boolean foundInThisBranch = false;

	            RevCommit targetCommit = walk.parseCommit(repo.resolve(
	                    commit.getName()));
	            for (Map.Entry<String, Ref> e : repo.getAllRefs().entrySet()) {
	                if (e.getKey().startsWith(Constants.R_HEADS)) {
	                    if (walk.isMergedInto(targetCommit, walk.parseCommit(
	                            e.getValue().getObjectId()))) {
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

        //ObjectId start = repository.resolve(b);
        //ObjectId end = repository.resolve(a);

        //finder.findBetween(start, end);
        finder.find();

        System.out.println("Added:\t"+filter.getAdded());
        System.out.println("Changed:\t"+filter.getEdited());
        System.out.println("Deleted:\t"+filter.getDeleted());

        System.out.println("CSI:\t"+(filter.getAdded()+filter.getEdited()));

        //walk.dispose();
	    //git.close();

	    
	}

	
}
