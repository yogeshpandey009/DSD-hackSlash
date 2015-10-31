package com.asu.score.hackslash.dialogs;


import org.eclipse.jface.dialogs.Dialog;
import org.eclipse.jface.dialogs.IDialogConstants;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.ModifyEvent;
import org.eclipse.swt.events.ModifyListener;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.graphics.RGB;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Text;

import com.asu.score.hackslash.engine.SessionManager;
import com.asu.score.hackslash.helper.ImageProviderHelper;

public class LoginDialog extends Dialog {
	private Text txtUser;
	private Text txtPassword;
	private String user = "";
	private String password = "";
	private SessionManager session = SessionManager.getInstance();
	

	public LoginDialog(Shell parentShell) {
		super(parentShell);
	}

	@Override
	protected Control createDialogArea(Composite parent) {
		getShell().setText(" - Login to hackSlash DSD - ");
		Image img = ImageProviderHelper.getImage("hs.png");
		getShell().setBackgroundImage(img);
		getShell().setBackgroundMode(SWT.INHERIT_FORCE);  
		getShell().setBounds(300, 200, img.getBounds().width, img.getBounds().height+40);
		Composite container = new Composite(getShell(), SWT.NONE);
		//container.setBounds(300, 200, img.getBounds().width, img.getBounds().height);
		GridLayout layout = new GridLayout(2, false);
		layout.marginRight = 5;
		layout.marginLeft = 10;
		layout.marginTop = 4;
		container.setLayout(layout);
		
		Color color = new Color(getShell().getDisplay(), new RGB(255, 255, 255));
		if (session.isAuthenticated()) {
			Label lblUser = new Label(container, SWT.NONE);
			lblUser.setBackground(color);
			lblUser.setText("You are Logged In as :- "
					+ session.getUsername());
		} else {
			Label lblUser = new Label(container, SWT.NONE);
			lblUser.setText("User:");
			//lblUser.setBackground(color);
			txtUser = new Text(container, SWT.BORDER);
			txtUser.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true,
					false, 1, 1));
			txtUser.setBackground(color);
			txtUser.setText(user);
			txtUser.addModifyListener(new ModifyListener() {

				@Override
				public void modifyText(ModifyEvent e) {
					Text textWidget = (Text) e.getSource();
					String userText = textWidget.getText();
					user = userText;
				}
			});

			Label lblPassword = new Label(container, SWT.NONE);
			GridData gd_lblNewLabel = new GridData(SWT.LEFT, SWT.CENTER, false,
					false, 1, 1);
			gd_lblNewLabel.horizontalIndent = 1;
			lblPassword.setLayoutData(gd_lblNewLabel);
			//lblPassword.setBackground(color);
			lblPassword.setText("Password:");

			txtPassword = new Text(container, SWT.BORDER | SWT.PASSWORD);
			txtPassword.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true,
					false, 1, 1));
			txtPassword.setBackground(color);
			txtPassword.setText(password);
			txtPassword.addModifyListener(new ModifyListener() {

				@Override
				public void modifyText(ModifyEvent e) {
					Text textWidget = (Text) e.getSource();
					String passwordText = textWidget.getText();
					password = passwordText;
				}
			});
		}

		return container;
	}

	// override method to use "Login" as label for the OK button
	@Override
	protected void createButtonsForButtonBar(Composite parent) {
		if (session.isAuthenticated()) {
			createButton(parent, IDialogConstants.CLOSE_ID, "Logout", true);
		} else {
			createButton(parent, IDialogConstants.OK_ID, "Login", true);
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
			user = txtUser.getText();
			password = txtPassword.getText();
		}
		setReturnCode(buttonId);
		close();
	}
	
	@Override
	protected boolean isResizable() {
	    return true;
	}

	public String getUser() {
		return user;
	}

	public void setUser(String user) {
		this.user = user;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}
}
