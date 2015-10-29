package com.asu.score.hackslash.dialogs;

import java.util.List;

import org.eclipse.jface.dialogs.Dialog;

import org.eclipse.jface.dialogs.IDialogConstants;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.ModifyEvent;
import org.eclipse.swt.events.ModifyListener;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Text;

import com.asu.score.hackslash.actions.im.UsersService;
import com.asu.score.hackslash.dao.ShowTasksDetailsDAO;
import com.asu.score.hackslash.engine.ConnectionManger;
import com.asu.score.hackslash.taskhelper.Task;



public class TaskDialog extends Dialog {
	private Text txtName;
	private Text txtDesc;
	private Combo comboAssignedTo;
	private Combo comboStatus;
	private String name = "";
	private String desc = "";
	private String assignedTo;
	private String status;

	public TaskDialog(Shell parentShell, Task task) {
		super(parentShell);
		if (task != null){
			name = task.getName();
			desc = task.getDesc();
			assignedTo = task.getAssignedTo();
		}
	}

	@Override
	protected Control createDialogArea(Composite parent) {
		getShell().setText(" - Add / Edit Task - ");
		Composite container = (Composite) super.createDialogArea(parent);
		GridLayout layout = new GridLayout(2, false);
		layout.marginRight = 5;
		layout.marginLeft = 10;
		layout.marginBottom = 10;
		layout.marginTop = 5;
		container.setLayout(layout);

		Label lblName = new Label(container, SWT.NONE);
		lblName.setText("Enter task name:");
		txtName = new Text(container, SWT.BORDER);
		txtName.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false,
				1, 1));
		txtName.setText(name);
		txtName.addModifyListener(new ModifyListener() {

			@Override
			public void modifyText(ModifyEvent e) {
				Text textWidget = (Text) e.getSource();
				name = textWidget.getText();
			}
		});

		Label lblDesc = new Label(container, SWT.NONE);
		GridData gd_lblNewLabel = new GridData(SWT.LEFT, SWT.CENTER, false,
				false, 1, 1);
		gd_lblNewLabel.horizontalIndent = 1;
		lblDesc.setLayoutData(gd_lblNewLabel);
		lblDesc.setText("Description:");

		txtDesc = new Text(container, SWT.MULTI | SWT.BORDER | SWT.WRAP
				| SWT.V_SCROLL);
		txtDesc.setLayoutData(new GridData(GridData.FILL_BOTH));
		txtDesc.setText(desc);
		txtDesc.addModifyListener(new ModifyListener() {

			@Override
			public void modifyText(ModifyEvent e) {
				Text textWidget = (Text) e.getSource();
				desc = textWidget.getText();
			}
		});

		Label lblAssignedTo = new Label(container, SWT.NONE);
		lblAssignedTo.setText("Assigned To:");
		comboAssignedTo = new Combo(container, SWT.READ_ONLY);
		comboAssignedTo.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true,
				false, 1, 1));
		comboAssignedTo.setBounds(50, 50, 150, 65);
		String[] items = {};
		if (ConnectionManger.isUserLoggedIn()){
			List<String> users = UsersService.getAllUsernames(); 
			users.add("Unassigned");
			items = users.toArray(new String[users.size()]);
		}
		comboAssignedTo.setItems(items);
		if (assignedTo != null){
			int index = 200;
			for (int i=0; i< items.length ; i++){
				if (assignedTo.equals(items[i])){
					index = i;
				}
			}
			if (index != 200){
			comboAssignedTo.select(index);
			}
		}
		comboAssignedTo.addModifyListener(new ModifyListener() {

			@Override
			public void modifyText(ModifyEvent e) {
				Combo comboWidget = (Combo) e.getSource();
				assignedTo = comboWidget.getText();
			}
		});
		
		Label lblStatus = new Label(container, SWT.NONE);
		lblStatus.setText("Status:");
		comboStatus = new Combo(container, SWT.READ_ONLY);
		comboStatus.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true,
				false, 1, 1));
		comboStatus.setBounds(50, 50, 150, 65);
		String[] status_items = {"New", "In Progress", "Closed"};
		comboStatus.setItems(status_items);
		if (status != null){
			int index = 200;
			for (int i=0; i< status_items.length ; i++){
				if (status.equals(items[i])){
					index = i;
				}
			}
			if (index != 200){
				comboStatus.select(index);
			}
		}
		comboStatus.addModifyListener(new ModifyListener() {

			@Override
			public void modifyText(ModifyEvent e) {
				Combo comboWidget = (Combo) e.getSource();
				status = comboWidget.getText();
			}
		});

		return container;
	}

	// override method to use "Login" as label for the OK button
	@Override
	protected void createButtonsForButtonBar(Composite parent) {
		
		createButton(parent, IDialogConstants.DETAILS_ID,
				IDialogConstants.SHOW_DETAILS_LABEL, false);		
		createButton(parent, IDialogConstants.OK_ID, IDialogConstants.OK_LABEL,
				true);
		createButton(parent, IDialogConstants.CANCEL_ID,
				IDialogConstants.CANCEL_LABEL, false);
		
	}

	@Override
	protected Point getInitialSize() {
		return new Point(450, 300);
	}

	@Override
	protected void okPressed() {
		name = txtName.getText();
		desc = txtDesc.getText();
		super.okPressed();
	}
	
	@Override
	protected void buttonPressed(int DETAILS_ID) {
			
			name = txtName.getText();
			System.out.println("DETAILS_ID button pressed+name"+name);
			TaskDetailsDialog tdd = new TaskDetailsDialog();
			ShowTasksDetailsDAO std =  new ShowTasksDetailsDAO();
			std.setTaskName(name);
			//tdd.displayTable(std.getTaskDetails(),name);
			
			
	}
	

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getDesc() {
		return desc;
	}

	public void setDesc(String desc) {
		this.desc = desc;
	}

	public String getAssignedTo() {
		return assignedTo;
	}

	public void setAssignedTo(String assignedTo) {
		this.assignedTo = assignedTo;
	}

}
