package com.asu.score.hackslash.dialogs;

import org.eclipse.jface.dialogs.Dialog;
import org.eclipse.jface.dialogs.IDialogConstants;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.ModifyEvent;
import org.eclipse.swt.events.ModifyListener;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Text;

import com.asu.score.hackslash.engine.SessionManager;
import com.asu.score.hackslash.properties.Constants;

public class AddContactDialog extends Dialog {
	private Text txtBuddyJID;
	private String buddyJID = "";
	private SessionManager session = SessionManager.getInstance();
	

	public AddContactDialog(Shell parentShell) {
		super(parentShell);
	}

	@Override
	protected Control createDialogArea(Composite parent) {
		getShell().setText(" - Add Contact - ");
		Composite container = (Composite) super.createDialogArea(parent);
		GridLayout layout = new GridLayout(2, false);
		layout.marginRight = 5;
		layout.marginLeft = 10;
		container.setLayout(layout);
		
		if (!session.isAuthenticated()) {
			Label lblUser = new Label(container, SWT.NONE);
			lblUser.setText("Kindly login before adding a contact.");
		} else {
			Label lblUser = new Label(container, SWT.NONE);
			lblUser.setText("Buddy JID:");
			txtBuddyJID = new Text(container, SWT.BORDER);
			txtBuddyJID.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true,
					false, 1, 1));
			txtBuddyJID.setText(buddyJID);
			txtBuddyJID.addModifyListener(new ModifyListener() {

				@Override
				public void modifyText(ModifyEvent e) {
					Text textWidget = (Text) e.getSource();
					String userText = textWidget.getText();
					buddyJID = userText;
				}
			});

		}

		return container;
	}

	// override method to use "Login" as label for the OK button
	@Override
	protected void createButtonsForButtonBar(Composite parent) {
		if (!session.isAuthenticated()) {
			createButton(parent, IDialogConstants.CLOSE_ID, "OK", true);
		} else {
			createButton(parent, IDialogConstants.OK_ID, "Add", true);
			createButton(parent, IDialogConstants.CANCEL_ID,
					IDialogConstants.CANCEL_LABEL, false);
		}
	}

	@Override
	protected Point getInitialSize() {
		return new Point(450, 300);
	}

	@Override
	protected void buttonPressed(int buttonId) {
		if (buttonId == IDialogConstants.OK_ID) {
			buddyJID = txtBuddyJID.getText() + Constants.SERVER_NAME;
		}
		setReturnCode(buttonId);
		close();
	}

	public String getUser() {
		return buddyJID;
	}

	public void setUser(String user) {
		this.buddyJID = user;
	}
	
	@Override
	protected boolean isResizable() {
	    return true;
	}

}
