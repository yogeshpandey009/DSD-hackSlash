package com.asu.score.hackslash.dialogs;

import org.eclipse.jface.dialogs.IMessageProvider;
import org.eclipse.jface.dialogs.TitleAreaDialog;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Shell;

import com.asu.score.hackslash.engine.SessionManager;
import com.asu.score.hackslash.statistics.GitController;

public class StatsDialog extends TitleAreaDialog {

	private GitController gitCtrl = GitController.getInstance();
	private SessionManager session = SessionManager.getInstance();

	public StatsDialog(Shell parentShell) {
		super(parentShell);
	}

	@Override
	public void create() {
		super.create();
		setTitle("Git Commit Statistics");
		setMessage("Repo: - " + gitCtrl.getGitPath(), IMessageProvider.INFORMATION);
	}

	@Override
	protected Control createDialogArea(Composite parent) {
		Composite area = (Composite) super.createDialogArea(parent);
		Composite container = new Composite(area, SWT.NONE);
		container.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true));
		GridLayout layout = new GridLayout(2, false);
		container.setLayout(layout);

		if (session.isAuthenticated()) {
			createButton(container, 100, "Monthly Commit Meter", true);
			createButton(container, 101, "Commit Statistics Graph", true);

		} else {
			Label lblUser = new Label(container, SWT.NONE);
			lblUser.setText("Kindly login to view Git Statistics");
		}

		return area;
	}

	@Override
	protected boolean isResizable() {
		return true;
	}

	@Override
	protected void buttonPressed(int buttonId) {
		setReturnCode(buttonId);
		close();
	}

}