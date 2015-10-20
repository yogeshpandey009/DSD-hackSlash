package com.asu.score.hackslash.userhelper;

import org.eclipse.jface.viewers.IContentProvider;
import org.eclipse.jface.viewers.IStructuredContentProvider;
import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.jface.viewers.Viewer;
import org.eclipse.swt.widgets.Display;

/*
 * The content provider class is responsible for providing objects to the
 * view. It can wrap existing objects in adapters or simply return objects
 * as-is. These objects may be sensitive to the current input of the view,
 * or ignore it and always show the same content (like Users List, for
 * example).
 */
public class UserContentProvider implements IStructuredContentProvider,
		UserInput.Listener {

	UserInput input;
	TableViewer viewer;

	/**
	 * @see IContentProvider#inputChanged(Viewer, Object, Object)
	 */
	public void inputChanged(Viewer viewer, Object oldInput, Object newInput) {
		if (viewer instanceof TableViewer) {
			this.viewer = (TableViewer) viewer;
		}
		if (newInput instanceof UserInput) {
			input = (UserInput) newInput;
			input.setListener(this);
		}
	}

	/**
	 * @see IContentProvider#dispose()
	 */
	public void dispose() {
		if (input != null)
			input.setListener(null);
		input = null;
		viewer = null;
	}

	/**
	 * @see IStructuredContentProvider#getElements(Object)
	 */
	public Object[] getElements(Object parent) {
		if (parent == input)
			return input.elements().toArray();
		return new Object[0];
	}

	@Override
	public void refresh() {
		Display.getDefault().syncExec(new Runnable() {
			public void run() {
				if (viewer != null) {
					viewer.refresh();
				}
			}
		});
	}
}
