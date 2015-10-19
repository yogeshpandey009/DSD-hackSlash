package com.asu.score.hackslash.taskhelper;

import org.eclipse.jface.viewers.*;

public class TaskContentProvider 
	implements IStructuredContentProvider, TaskFile.Listener
{
	TaskFile input;
	TableViewer viewer;

	/**
	 * @see IStructuredContentProvider#getElements(Object)
	 */
	public Object[] getElements(Object element) {
		if (element == input)
			return input.elements().toArray();
		return new Object[0];
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
	 * @see IContentProvider#inputChanged(Viewer, Object, Object)
	 */
	public void inputChanged(Viewer viewer, Object oldInput, Object newInput) {
		if (viewer instanceof TableViewer) {
			this.viewer = (TableViewer)viewer;
		}
		if (newInput instanceof TaskFile) {
			input = (TaskFile)newInput;
			input.setListener(this);
		}
	}

	/**
	 * @see Listener#added()
	 */
	public void added(Task e) {
		if (viewer != null)
			viewer.add(e);
	}
	
	/**
	 * @see Listener#removed()
	 */
	public void removed(Task e) {
		if (viewer != null) {
			viewer.setSelection(null);
			viewer.remove(e);
		}
	}

	@Override
	public void refresh() {
		if (viewer != null) {
			viewer.refresh();
		}
	}
}

