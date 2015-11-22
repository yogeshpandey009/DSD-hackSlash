package com.asu.score.hackslash.dialogs;

import java.io.IOException;
import java.util.List;

import org.eclipse.jface.dialogs.Dialog;
import org.eclipse.jgit.api.errors.GitAPIException;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.KeyAdapter;
import org.eclipse.swt.events.KeyEvent;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableColumn;
import org.eclipse.swt.widgets.TableItem;
import org.eclipse.swt.widgets.Text;

import com.asu.score.hackslash.model.ShowGitLogsModel;
import com.asu.score.hackslash.statistics.GitData;

public class GitLogsDialog extends Dialog {

	public GitLogsDialog(Shell parentShell) {
		super(parentShell);
		// TODO Auto-generated constructor stub
	}



	private List<ShowGitLogsModel> commitLogs;

	
	@Override
	protected Control createDialogArea(Composite parent) {
	//public void showCommitLogs(List<ShowGitLogsModel> commitLogs) {
		
		//final Shell s = new Shell();
		//Composite container = new Composite(getShell(), SWT.BORDER | SWT.H_SCROLL | SWT.V_SCROLL);
		//s.setBounds(500, 500, 500, 500);
		
		//container.setBounds(0, 0, 500, 500);
		/*s.setLayout(new FillLayout());
		

		s.setSize(700, 700);

		s.setText("Commit Logs");
		
*/		//container.setLayout(new FillLayout());


		//container.setSize(300, 300);
	

		//s.setText("Commit Logs");
		//GridLayout gl = new GridLayout(2, false);
		//s.setLayout(gl);
		Composite container = (Composite) super.createDialogArea(parent);
		GridLayout layout = new GridLayout(2, false);
		layout.marginRight = 5;
		layout.marginLeft = 10;
		layout.marginBottom = 10;
		layout.marginTop = 5;
		container.setLayout(layout);
		//container.setLayout(gl);
		container.setBounds(500, 500, 500, 500);
		getShell().setBounds(500,500, 700, 1000);

		final Table t = new Table(container, SWT.BORDER | SWT.H_SCROLL | SWT.V_SCROLL | SWT.FULL_SELECTION);
		t.setBounds(300, 300, 300, 300);
		final GridData gd = new GridData(GridData.FILL_BOTH);
		gd.horizontalSpan = 2;
		t.setLayoutData(gd);
		t.setHeaderVisible(true);
		final TableColumn tc1 = new TableColumn(t, SWT.CENTER);
		final TableColumn tc2 = new TableColumn(t, SWT.CENTER);
		final TableColumn tc3 = new TableColumn(t, SWT.LEFT);
		tc1.setText("Author Name");
		tc2.setText("Date");
		tc3.setText("Commit Message");
		tc1.setWidth(100);
		tc2.setWidth(250);
		tc3.setWidth(300);
		tc3.setAlignment(1);
		for (int i = 0; i < commitLogs.size(); i++) {
			TableItem item1 = new TableItem(t, SWT.NONE);
			item1.setText(new String[] { commitLogs.get(i).getAuthorName(), commitLogs.get(i).getDate(),
					commitLogs.get(i).getCommitMessage().trim() });

		}

		//GridData gd_lblNewLabel = new GridData();
		
		//input.setLayoutData(gd_lblNewLabel);
		//input.setLayoutData(new GridData(GridData.FILL_BOTH));
		//input.setText("");

		Text input = new Text(container,  SWT.BORDER );
		input.setFocus();
		input.addKeyListener(new KeyAdapter() {
			public void keyPressed(KeyEvent e) {
				if (e.keyCode == SWT.CR) {
					searchOnLogs(t.getItems(), input.getText());
				}
			}
		});
		//input.redraw(100, 100, 50, 50, true);
		//input.setBounds(50,50,50,50);
		
		Button searchBtn = new Button(container, SWT.NONE);
		//searchBtn.setLayoutData(gd_lblNewLabel);
		searchBtn.setText("Search");
		searchBtn.addSelectionListener(new SelectionAdapter() {
			public void widgetSelected(SelectionEvent e) {
				searchOnLogs(t.getItems(), input.getText());
			}
		});
		
		
		return container;

	
	}
	
	private void searchOnLogs(TableItem[] tia, String search) {
		Display d = getShell().getDisplay();
		for (int i = 0; i < tia.length; i++) {
			if (tia[i].getText(0).toLowerCase().contains(search.toLowerCase())) {
				tia[i].setBackground(new Color(d,127, 178, 127));
			} else {
				tia[i].setBackground(new Color(d, 255, 255, 255));
			}

		}
	}

	@Override
	protected void createButtonsForButtonBar(Composite parent) {
	}
	
	
	
	

	public static void main(String[] argv) throws IOException, GitAPIException {
		GitLogsDialog one = new GitLogsDialog(new Shell());
		one.commitLogs = new GitData().getGitCommitLog();

		one.open();
		
		//one.showCommitLogs(one.commitLogs);

	}



	public List<ShowGitLogsModel> getCommitLogs() {
		return commitLogs;
	}

	@Override
	protected boolean isResizable() {
	    return true;
	}


	public void setCommitLogs(List<ShowGitLogsModel> commitLogs) {
		this.commitLogs = commitLogs;
	}

}
