package com.asu.score.hackslash.dialogs;

import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Tree;
import org.eclipse.swt.widgets.TreeColumn;
import org.eclipse.swt.widgets.TreeItem;


public class TaskDetailsDialog {

	List<String> user_id_list = new ArrayList<String>();
	List<Timestamp> start_date_list = new ArrayList<Timestamp>();
	List<Timestamp> end_date_list = new ArrayList<Timestamp>();
	
	
	public void displayTable(List task_details,String taskName) {
		
		
		if(task_details == null || task_details == null){
			return;
		}
		
		/*user_id_list.add("HM123");
		user_id_list.add("MK232");
		Calendar calendar = Calendar.getInstance();
	    java.sql.Timestamp startDate = new java.sql.Timestamp(calendar.getTime().getTime());
		
		start_date_list.add(new SimpleDateFormat("MM/dd/yyyy HH:mm:ss").format(startDate));
		start_date_list.add(new SimpleDateFormat("MM/dd/yyyy HH:mm:ss").format(startDate));
		end_date_list.add(new SimpleDateFormat("MM/dd/yyyy HH:mm:ss").format(startDate));
		end_date_list.add(new SimpleDateFormat("MM/dd/yyyy HH:mm:ss").format(startDate));
		*/
		user_id_list = (List<String>) task_details.get(0);
		start_date_list = (List<Timestamp>) task_details.get(1);
		end_date_list =   (List<Timestamp>) task_details.get(2);
		SimpleDateFormat smpldtFrmt = new SimpleDateFormat("MM/dd/yyyy HH:mm:ss");
		
		
		Display display = new Display();
		final Shell shell = new Shell(display);
		shell.setBounds(200, 200, 200, 200);
		shell.setLayout(new FillLayout());
		Tree tree = new Tree(shell, SWT.BORDER | SWT.H_SCROLL | SWT.V_SCROLL);
		tree.setHeaderVisible(true);
		TreeColumn column1 = new TreeColumn(tree, SWT.LEFT);
		column1.setText(" User ID");
		column1.setWidth(200);
		TreeColumn column2 = new TreeColumn(tree, SWT.CENTER);
		column2.setText("Start Date");
		column2.setWidth(200);
		TreeColumn column3 = new TreeColumn(tree, SWT.RIGHT);
		column3.setText("End Date");
		column3.setWidth(200);
		TreeItem item = new TreeItem(tree, SWT.NONE);
		//item.setText(new String[] { "taskName", "", "" });
		item.setText(new String[] { taskName, "", "" });
		for (int j = 0; j < 2; j++) {
			TreeItem subItem = new TreeItem(item, SWT.NONE);
			subItem.setText(new String[] { user_id_list.get(j),  smpldtFrmt.format(start_date_list.get(j)) ,  smpldtFrmt.format(end_date_list.get(j))});
			TreeItem item1 = new TreeItem(tree, SWT.NONE);
			item1.setText(new String[] { "", "", "" });
			
		}
		shell.pack();
		shell.open();
		while (!shell.isDisposed()) {
			if (!display.readAndDispatch()) {
				display.sleep();
			}
		}

		display.dispose();

	}

	public static void main(String[] args) {

		TaskDetailsDialog one = new TaskDetailsDialog();
		one.displayTable(null,"");
	}
}
