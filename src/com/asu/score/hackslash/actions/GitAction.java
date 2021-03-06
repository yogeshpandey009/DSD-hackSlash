package com.asu.score.hackslash.actions;

import java.io.IOException;

import org.eclipse.jface.action.IAction;
import org.eclipse.jface.dialogs.IDialogConstants;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jgit.api.errors.GitAPIException;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.IWorkbenchWindowActionDelegate;

import com.asu.score.hackslash.dialogs.DisplayGit;
import com.asu.score.hackslash.dialogs.GitLogsDialog;
import com.asu.score.hackslash.dialogs.StatsDialog;
import com.asu.score.hackslash.properties.Constants;
import com.asu.score.hackslash.statistics.GitData;

/**
 * Our sample action implements workbench action delegate. The action proxy will
 * be created by the workbench and shown in the UI. When the user tries to use
 * the action, this delegate will be created and execution will be delegated to
 * it.
 * 
 * @see IWorkbenchWindowActionDelegate
 */
public class GitAction implements IWorkbenchWindowActionDelegate {
	private IWorkbenchWindow window;

	/**
	 * The constructor.
	 */
	public GitAction() {
	}

	/**
	 * The action has been activated. The argument of the method represents the
	 * 'real' action sitting in the workbench UI.
	 * 
	 * @see IWorkbenchWindowActionDelegate#run
	 */
	public void run(IAction action) {
		DisplayGit disGit = new DisplayGit();
		
		StatsDialog dialog = new StatsDialog(window.getShell());

		// get the new values from the dialog
		int result = dialog.open();
		if (result == 100) {
			try {
				disGit.showCommitMeter();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		} 
		if (result == 101) {
			try {
				disGit.showUserCommitsGraph();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		if (result == 102) {
			try {
				disGit.showMonthlyCommits();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		if (result == 103) {
			try {
				disGit.showLocChange();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		if(result == Constants.GIT_COMMIT_LOGS_BUTTON_ID){
			System.out.println("else if");
			/*try {
				Git gitLogDialog1 = new Git(window.getShell(),new GitData().getGitCommitLog());
				gitLogDialog1.open();
					//gitLogDialog1.setCommitLogs(new GitData().getGitCommitLog());
				} catch (IOException | GitAPIException e) {
					System.out.println("Exception in run():GitAction class");
					e.printStackTrace();
				}
				*/
			GitLogsDialog gitLogDialog =  new GitLogsDialog(window.getShell());
		
			try {
				gitLogDialog.setCommitLogs(new GitData().getGitCommitLog());
				gitLogDialog.open();
				//gitLogDialog.showCommitLogs(gitLogDialog.getCommitLogs());
			} catch (IOException | GitAPIException e) {
				System.out.println("Exception in run():GitAction class");
				e.printStackTrace();
			}
			
		 }
			
	}

	/**
	 * Selection in the workbench has been changed. We can change the state of
	 * the 'real' action here if we want, but this can only happen after the
	 * delegate has been created.
	 * 
	 * @see IWorkbenchWindowActionDelegate#selectionChanged
	 */
	public void selectionChanged(IAction action, ISelection selection) {
	}

	/**
	 * We can use this method to dispose of any system resources we previously
	 * allocated.
	 * 
	 * @see IWorkbenchWindowActionDelegate#dispose
	 */
	public void dispose() {
	}

	/**
	 * We will cache window object in order to be able to provide parent shell
	 * for the message dialog.
	 * 
	 * @see IWorkbenchWindowActionDelegate#init
	 */
	public void init(IWorkbenchWindow window) {
		this.window = window;
	}
}