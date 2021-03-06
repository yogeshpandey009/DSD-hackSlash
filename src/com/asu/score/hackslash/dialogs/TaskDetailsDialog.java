package com.asu.score.hackslash.dialogs;

import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;


import org.eclipse.jface.dialogs.Dialog;

import org.eclipse.jface.dialogs.IDialogConstants;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.ModifyEvent;
import org.eclipse.swt.events.ModifyListener;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Text;

import org.eclipse.swt.widgets.Tree;
import org.eclipse.swt.widgets.TreeColumn;
import org.eclipse.swt.widgets.TreeItem;


import com.asu.score.hackslash.actions.im.UsersService;
import com.asu.score.hackslash.dao.ShowTasksDetailsDAO;
import com.asu.score.hackslash.engine.ConnectionManager;
import com.asu.score.hackslash.model.ShowTaskDetails;
import com.asu.score.hackslash.taskhelper.Task;

public class TaskDetailsDialog extends Dialog {
	
	private String taskName="";
	private int list_size = 0;
	private List<String> user_id_list = new ArrayList<String>();
	private List<Timestamp> start_date_list = new ArrayList<Timestamp>();
	private List<Timestamp> end_date_list = new ArrayList<Timestamp>();
	private List<String> start_date_list1 = new ArrayList<String>();
	private List<String> end_date_list1 = new ArrayList<String>();
	ShowTaskDetails std = new ShowTaskDetails();
	private List<ShowTaskDetails> details = new ArrayList<ShowTaskDetails>();

	public TaskDetailsDialog(Shell parentShell, String name,List task_details) {
		super(parentShell);
		System.out.println("cons");
	if (name != null) {
			taskName = name;
			}
		if(task_details != null){
			//user_id_list = (List<String>) task_details.get(0);
			//start_date_list = (List<Timestamp>) task_details.get(1);
			//end_date_list =   (List<Timestamp>) task_details.get(2);
			//list_size = user_id_list.size();
			list_size = task_details.size();
			details = task_details;
		}
		SimpleDateFormat smpldtFrmt = new SimpleDateFormat("MM/dd/yyyy HH:mm:ss");
		System.out.println(user_id_list);
		System.out.println(start_date_list);
		System.out.println(end_date_list);
		for(int i=0;i<start_date_list.size();i++){
			if(start_date_list.get(i)==null)
				start_date_list1.add("");				
			else
				start_date_list1.add(smpldtFrmt.format(start_date_list.get(i)));
			
		}
		for(int i=0;i<end_date_list.size();i++){
			if(end_date_list.get(i)==null)
				end_date_list1.add("");				
			else
				end_date_list1.add(smpldtFrmt.format(start_date_list.get(i)));
			
		}
		for(int i=0;i<user_id_list.size();i++){
			if(user_id_list.get(i)==null)
				user_id_list.add("");				
			
		}
		
		
	}

	@Override
	protected Control createDialogArea(Composite parent) {
		System.out.println("createDialogArea");
		
		
		SimpleDateFormat smpldtFrmt = new SimpleDateFormat("MM/dd/yyyy HH:mm:ss");
		getShell().setText(taskName);
		Composite container = (Composite) super.createDialogArea(parent);
		GridLayout layout = new GridLayout(2, false);
		layout.marginRight = 5;
		layout.marginLeft = 10;
		layout.marginBottom = 10;
		layout.marginTop = 5;
		container.setLayout(layout);
		getShell().setBounds(50,50,150,65);
		Tree tree = new Tree(getShell(),   SWT.BORDER | SWT.H_SCROLL | SWT.V_SCROLL);
		tree.setHeaderVisible(true);
		TreeColumn column1 = new TreeColumn(tree, SWT.LEFT);
		column1.setText("     User ID");
		column1.setWidth(200);
		TreeColumn column2 = new TreeColumn(tree, SWT.CENTER);
		column2.setText("Start Date");
		column2.setWidth(200);
		TreeColumn column3 = new TreeColumn(tree, SWT.RIGHT);
		column3.setText("End Date");
		column3.setWidth(200);
		//TreeItem item = new TreeItem(tree, SWT.NONE);
		//item.setText(new String[] { "taskName", "", "" });
		//item.setText(new String[] { taskName, "", "" });
		//item.setText(new String[] { "    User ID", "Start Date","End Date" });
		
		//item.setExpanded(true);
		/*for (int j = 0; j < list_size; j++) {
			TreeItem subItem = new TreeItem(item, SWT.NONE);
			//subItem.setText(new String[] { user_id_list.get(j),  smpldtFrmt.format(start_date_list.get(j)) ,  smpldtFrmt.format(end_date_list.get(j))});
			subItem.setText(new String[] { user_id_list.get(j),  start_date_list1.get(j),  end_date_list1.get(j)});
			TreeItem item1 = new TreeItem(tree, SWT.NONE);
			item1.setText(new String[] { "", "", "" });
			
		}*/
		
		for (int j = 0; j < list_size; j++) {
			std = details.get(j);
			
			TreeItem subItem = new TreeItem(tree, SWT.NONE);
			//subItem.setText(new String[] { user_id_list.get(j),  smpldtFrmt.format(start_date_list.get(j)) ,  smpldtFrmt.format(end_date_list.get(j))});
			//subItem.setText(new String[] { user_id_list.get(j),  start_date_list1.get(j),  end_date_list1.get(j)});
			subItem.setText(new String[] { std.getUser_id() , std.getStart_dt() , std.getEnd_dt()});
			//TreeItem item1 = new TreeItem(tree, SWT.NONE);
			//item1.setText(new String[] { "", "", "" });
			
		}
		TreeItem item1 = new TreeItem(tree, SWT.NONE);
		item1.setText(new String[] { "", "", "" });
		//item.setExpanded(true);
		

		getShell().pack();
		getShell().open();
		System.out.println("createDialogArea ends");

		return container;
}
	@Override
	protected boolean isResizable() {
	    return false;
	}

}



