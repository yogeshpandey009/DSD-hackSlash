package com.asu.score.hackslash.views;

import java.io.IOException;

import javax.swing.text.html.FormView;

import org.eclipse.jface.viewers.ITableLabelProvider;
import org.eclipse.jface.viewers.LabelProvider;
import org.eclipse.jface.viewers.StructuredSelection;
import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.MouseAdapter;
import org.eclipse.swt.events.MouseEvent;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.Text;
import org.eclipse.ui.forms.widgets.Form;
import org.eclipse.ui.forms.widgets.FormToolkit;
import org.eclipse.ui.part.ViewPart;
import org.jivesoftware.smack.SmackException;
import org.jivesoftware.smack.XMPPException;

import com.asu.score.hackslash.actions.im.ChatController;
import com.asu.score.hackslash.chathelper.Chat;
import com.asu.score.hackslash.chathelper.ChatInput;
import com.asu.score.hackslash.taskhelper.TaskContentProvider;
import com.asu.score.hackslash.taskhelper.TaskInput;

public class ChatView extends ViewPart {
	public static final String ID = FormView.class.getPackage().getName()
			+ ".Form";

	private FormToolkit toolkit;
	private Form form;
	private Text text;
	private TableViewer viewer;
	private ChatInput input;
	
	/**
	 * Constructor
	 */
	public ChatView() {
		super();
		input = new ChatInput();
	}
	
	class ViewLabelProvider extends LabelProvider implements
			ITableLabelProvider {

		public String getColumnText(Object obj, int index) {
			String txt = "";
			return txt;
		}

		public Image getColumnImage(Object obj, int index) {
			if (index == 0) {
				return getImage(obj);
			}
			return null;
		}

		public Image getImage(Object obj) {
			return null;
		}
	}

	@Override
	public void createPartControl(Composite parent) {
		GridLayout layout = new GridLayout(2, false);
		parent.setLayout(layout);
		createViewer(parent);
		toolkit = new FormToolkit(parent.getDisplay());
		
		form = toolkit.createForm(parent);
		// form.setText("Pie Chucker");
		GridLayout layout2 = new GridLayout();
		form.getBody().setLayout(layout2);
		layout2.numColumns = 2;
		GridData gd = new GridData();
		gd.horizontalSpan = 2;
		Label label = new Label(form.getBody(), SWT.NULL);
		label.setText("Chat Box:");
		text = new Text(form.getBody(), SWT.BORDER);
		text.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
		Button button = new Button(form.getBody(), SWT.PUSH);
		button.setText("Send");
		gd = new GridData();
		gd.horizontalSpan = 2;
		button.setLayoutData(gd);
		button.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseDown(MouseEvent e) {
				try {
					ChatController.getInstance().sendMessage(text.getText());
				} catch (XMPPException | SmackException | IOException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
			}
		});

	}

	public void createViewer(Composite parent) {
		viewer = new TableViewer(parent, SWT.MULTI | SWT.H_SCROLL | SWT.V_SCROLL | SWT.FULL_SELECTION | SWT.BORDER);
		//createColumns(parent, viewer);
		final Table table = viewer.getTable();
		table.setHeaderVisible(true);
		table.setLinesVisible(true);
		
		// viewer.setContentProvider(new ArrayContentProvider());
		viewer.setContentProvider(new TaskContentProvider());
		// get the content for the viewer, setInput will call getElements in the
		// contentProvider
		// viewer.setInput(ModelProvider.INSTANCE.getPersons());
		viewer.setInput(input);
		// make the selection available to other views
		getSite().setSelectionProvider(viewer);
		// set the sorter for the table

		// define layout for the viewer
		GridData gridData = new GridData();
		gridData.verticalAlignment = GridData.FILL;
		gridData.horizontalSpan = 2;
		gridData.grabExcessHorizontalSpace = true;
		gridData.grabExcessVerticalSpace = true;
		gridData.horizontalAlignment = GridData.FILL;
		viewer.getControl().setLayoutData(gridData);
	}

	@Override
	public void setFocus() {
	}
	
	/**
	 * Add item to list.
	 */
	private void refresh() {
		input.refresh();
	}

	/**
	 * Add item to list.
	 */
	public void addItem(Chat chat) {
		if (chat != null) {
			chat = input.add(chat);
			viewer.setSelection(new StructuredSelection(chat));
		}
	}
}