package com.asu.score.hackslash.actions;

import java.io.IOException;

import org.eclipse.jface.action.IAction;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.IWorkbenchWindowActionDelegate;
import org.jivesoftware.smack.SmackException;
import org.jivesoftware.smack.XMPPException;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.window.Window;

import com.asu.score.hackslash.dialogs.LoginDialog;
import com.asu.score.hackslash.engine.Server;

/**
 * Our sample action implements workbench action delegate. The action proxy will
 * be created by the workbench and shown in the UI. When the user tries to use
 * the action, this delegate will be created and execution will be delegated to
 * it.
 * 
 * @see IWorkbenchWindowActionDelegate
 */
public class DSDAction implements IWorkbenchWindowActionDelegate {
	private IWorkbenchWindow window;

	/**
	 * The constructor.
	 */
	public DSDAction() {
	}

	/**
	 * The action has been activated. The argument of the method represents the
	 * 'real' action sitting in the workbench UI.
	 * 
	 * @see IWorkbenchWindowActionDelegate#run
	 */
	public void run(IAction action) {
		LoginDialog dialog = new LoginDialog(window.getShell());

		// get the new values from the dialog
		if (dialog.open() == Window.OK) {
			try {
				Server.createConnection();
			} catch (SmackException | IOException | XMPPException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			String user = dialog.getUser();
			String pw = dialog.getPassword();
			System.out.println(user);
			System.out.println(pw);
			MessageDialog.openInformation(window.getShell(), "Hackslash",
					"Hello User, Welcome to DSD work enviroment");
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