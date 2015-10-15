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

import com.asu.score.hackslash.engine.ConnectionManger;
import com.asu.score.hackslash.engine.SessionManager;
import com.asu.score.hackslash.properties.Constants;


public class ChatDialog extends Dialog {
	private Text txtMsg;
	private String msg = "";
	private SessionManager session = SessionManager.getInstance();

	public ChatDialog(Shell parentShell) {
		super(parentShell);
	}

	@Override
	protected Control createDialogArea(Composite parent) {
		Composite container = (Composite) super.createDialogArea(parent);
		GridLayout layout = new GridLayout(2, false);
		layout.marginRight = 10;
		layout.marginLeft = 10;
		layout.marginBottom = 10;
		layout.marginTop = 10;
		container.setLayout(layout);
		
		Label lblMsg = new Label(container, SWT.NONE);
		if (session.isAuthenticated()) {
			lblMsg.setText("Enter Message:");
			GridData gd_lblNewLabel = new GridData(SWT.LEFT, SWT.CENTER, false,
					false, 1, 1);
			gd_lblNewLabel.horizontalIndent = 1;
	
			txtMsg = new Text(container, SWT.MULTI | SWT.BORDER | SWT.WRAP
					| SWT.V_SCROLL);
			txtMsg.setLayoutData(new GridData(GridData.FILL_BOTH));
			txtMsg.setText(msg);
			txtMsg.addModifyListener(new ModifyListener() {
	
				@Override
				public void modifyText(ModifyEvent e) {
					Text textWidget = (Text) e.getSource();
					msg = textWidget.getText();
				}
			});
		} else {
			lblMsg.setText("Kindly Login to start the chat!");
		}
		return container;
	}

	// override method to use "Login" as label for the OK button
	@Override
	protected void createButtonsForButtonBar(Composite parent) {
		if (session.isAuthenticated()) {
			createButton(parent, IDialogConstants.OK_ID, Constants.SEND_LABEL,
					true);
			createButton(parent, IDialogConstants.CANCEL_ID,
					IDialogConstants.CANCEL_LABEL, false);
		} else {
			createButton(parent, IDialogConstants.BACK_ID, IDialogConstants.OK_LABEL,
					true);
		}
		
	}

	@Override
	protected Point getInitialSize() {
		return new Point(400, 250);
	}

	@Override
	protected void okPressed() {
		msg = txtMsg.getText();
		super.okPressed();
	}

	public String getMsg() {
		return msg;
	}

	public void setMsg(String msg) {
		this.msg = msg;
	}

}
