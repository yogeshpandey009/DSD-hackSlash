package com.asu.score.hackslash.chathelper;

import org.eclipse.jface.viewers.IContentProvider;
import org.eclipse.jface.viewers.IStructuredContentProvider;
import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.jface.viewers.Viewer;

public class ChatContentProvider 
	implements IStructuredContentProvider, ChatInput.Listener
{
	ChatInput input;
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
		if (newInput instanceof ChatInput) {
			input = (ChatInput)newInput;
			input.setListener(this);
		}
	}

	/**
	 * @see Listener#added()
	 */
	public void added(Chat e) {
		if (viewer != null)
			viewer.add(e);
	}
	

	@Override
	public void refresh() {
		if (viewer != null) {
			viewer.refresh();
		}
	}
}