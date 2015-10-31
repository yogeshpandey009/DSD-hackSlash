package com.asu.score.hackslash.dialogs;

import java.text.SimpleDateFormat;

import org.eclipse.jface.dialogs.Dialog;
import org.eclipse.jface.viewers.ILabelProviderListener;
import org.eclipse.jface.viewers.IStructuredContentProvider;
import org.eclipse.jface.viewers.ITableLabelProvider;
import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.jface.viewers.Viewer;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.TableColumn;

import com.asu.score.hackslash.sessionloghelper.UserSessionLog;
import com.asu.score.hackslash.sessionloghelper.UserSessionLogInput;

public class UserSessionLogDialog extends Dialog {

	private String username;

	public UserSessionLogDialog(Shell parentShell, String username) {
		super(parentShell);
		this.username = username;
	}
	
	@Override
	protected Point getInitialSize() {
		return new Point(250, 400);
	}
	
	@Override
	protected boolean isResizable() {
	    return true;
	}

	@Override
	protected Control createDialogArea(Composite parent) {
		final Composite body = (Composite) super.createDialogArea(parent);

		final TableViewer viewer = new TableViewer(body, SWT.BORDER
				| SWT.V_SCROLL | SWT.H_SCROLL);

		viewer.getTable().setLayoutData(
				new GridData(SWT.FILL, SWT.FILL, true, true));

		TableColumn loginTimeCol = new TableColumn(viewer.getTable(), SWT.LEFT);
		loginTimeCol.setText("Login Time");

		TableColumn logoutTimeCol = new TableColumn(viewer.getTable(), SWT.LEFT);
		logoutTimeCol.setText("Logout Time");

		viewer.getTable().setHeaderVisible(true);

		viewer.setContentProvider(new UserSessionLogContentProvider());
		viewer.setLabelProvider(new UserSessionLogLabelProvider());

		viewer.setInput(new UserSessionLogInput(username));

		loginTimeCol.pack();
		logoutTimeCol.pack();

		return body;
	}

	class UserSessionLogLabelProvider implements ITableLabelProvider {
		
		@Override
		public Image getColumnImage(Object element, int columnIndex) {
			return null;
		}

		@Override
		public String getColumnText(Object element, int columnIndex) {
			UserSessionLog e = (UserSessionLog) element;
			SimpleDateFormat df = new SimpleDateFormat("dd MMM yyyy HH:mm z");
			switch (columnIndex) {
			case 0:
				return df.format(e.getLoginTime());
			case 1:
				return df.format(e.getLogoutTime());
			}
			return "";
		}

		@Override
		public void dispose() {
		}

		@Override
		public boolean isLabelProperty(Object element, String property) {
			return false;
		}

		@Override
		public void addListener(ILabelProviderListener arg0) {
			// TODO Auto-generated method stub

		}

		@Override
		public void removeListener(ILabelProviderListener arg0) {
			// TODO Auto-generated method stub

		}

	}
	
	class UserSessionLogContentProvider implements IStructuredContentProvider {
		  public Object[] getElements(Object inputElement) {
		    return ((UserSessionLogInput)inputElement).elements().toArray();
		  }
		  public void dispose() {
		  }

		  public void inputChanged(Viewer viewer, Object oldInput, Object newInput) {
		  }
		}

}
