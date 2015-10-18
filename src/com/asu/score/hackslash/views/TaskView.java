package com.asu.score.hackslash.views;

import java.util.ArrayList;
import java.util.Iterator;

import org.eclipse.jface.action.Action;
import org.eclipse.jface.action.GroupMarker;
import org.eclipse.jface.action.IMenuListener;
import org.eclipse.jface.action.IMenuManager;
import org.eclipse.jface.action.IToolBarManager;
import org.eclipse.jface.action.MenuManager;
import org.eclipse.jface.action.Separator;
import org.eclipse.jface.viewers.ColumnLabelProvider;
import org.eclipse.jface.viewers.ISelectionChangedListener;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.SelectionChangedEvent;
import org.eclipse.jface.viewers.StructuredSelection;
import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.jface.viewers.TableViewerColumn;
import org.eclipse.jface.window.Window;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.KeyAdapter;
import org.eclipse.swt.events.KeyEvent;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Menu;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableColumn;
import org.eclipse.ui.IActionBars;
import org.eclipse.ui.IMemento;
import org.eclipse.ui.IViewSite;
import org.eclipse.ui.IWorkbenchActionConstants;
import org.eclipse.ui.IWorkbenchPart;
import org.eclipse.ui.PartInitException;
import org.eclipse.ui.part.ViewPart;
import org.eclipse.ui.part.WorkbenchPart;

import com.asu.score.hackslash.dialogs.TaskDialog;
import com.asu.score.hackslash.helper.ImageProviderHelper;
import com.asu.score.hackslash.taskhelper.Task;
import com.asu.score.hackslash.taskhelper.TaskContentProvider;
import com.asu.score.hackslash.taskhelper.TaskFile;


/**
 * This sample class demonstrates how to plug-in a new workbench view. The view
 * shows data obtained from the model. The sample creates a dummy model on the
 * fly, but a real implementation would connect to the model available either in
 * this or another plug-in (e.g. the workspace). The view is connected to the
 * model using a content provider.
 * <p>
 * The view uses a label provider to define how model objects should be
 * presented in the view. Each view can present the same model objects using
 * different labels and icons, if needed. Alternatively, a single label provider
 * can be shared between views in order to ensure that objects of the same type
 * are presented in the same way everywhere.
 * <p>
 */

public class TaskView extends ViewPart {

	private TaskFile input;
	private Action addItemAction, deleteItemAction, selectAllAction, editItemAction;
	private IMemento memento;
	private TableViewer viewer;

	private void createViewer(Composite parent) {
		viewer = new TableViewer(parent, SWT.MULTI | SWT.H_SCROLL | SWT.V_SCROLL | SWT.FULL_SELECTION | SWT.BORDER);
		createColumns(parent, viewer);
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

	private void createColumns(final Composite parent, final TableViewer viewer) {

		String[] titles = { "Task Name", "Assigned To" };
		int[] bounds = { 100, 100 };

		// first column is for the Task Name
		TableViewerColumn col = createTableViewerColumn(titles[0], bounds[0], 0);
		col.setLabelProvider(new ColumnLabelProvider() {
			@Override
			public String getText(Object element) {
				Task p = (Task) element;
				return p.getName();
			}
		});

		// second column is for the Task Description
		col = createTableViewerColumn(titles[1], bounds[1], 1);
		col.setLabelProvider(new ColumnLabelProvider() {
			@Override
			public String getText(Object element) {
				Task p = (Task) element;
				return p.getAssignedTo();
			}
		});

	}

	private TableViewerColumn createTableViewerColumn(String title, int bound, final int colNumber) {
		final TableViewerColumn viewerColumn = new TableViewerColumn(viewer, SWT.NONE);
		final TableColumn column = viewerColumn.getColumn();
		column.setText(title);
		column.setWidth(bound);
		column.setResizable(true);
		column.setMoveable(true);
		return viewerColumn;
	}

	public TableViewer getViewer() {
		return viewer;
	}
		
	/**
	 * Constructor
	 */
	public TaskView() {
		super();
		input = new TaskFile();
	}

	/**
	 * @see IViewPart.init(IViewSite)
	 */
	public void init(IViewSite site) throws PartInitException {
		super.init(site);
	}

	/**
	 * Initializes this view with the given view site. A memento is passed to
	 * the view which contains a snapshot of the views state from a previous
	 * session.
	 */
	public void init(IViewSite site, IMemento memento) throws PartInitException {
		init(site);
		this.memento = memento;
	}

	/**
	 * @see IWorkbenchPart#createPartControl(Composite)
	 */

	public void createPartControl(Composite parent) {
		GridLayout layout = new GridLayout(2, false);
		parent.setLayout(layout);
		createViewer(parent);

		// Label searchLabel = new Label(parent, SWT.NONE);
		// searchLabel.setText("Search: ");
		// final Text searchText = new Text(parent, SWT.BORDER | SWT.SEARCH);
		// searchText.setLayoutData(new GridData(GridData.GRAB_HORIZONTAL |
		// GridData.HORIZONTAL_ALIGN_FILL));

		createActions();
		createMenu();
		createToolbar();
		createContextMenu();
		hookGlobalActions();

	}

	/*
	 * public void createPartControl(Composite parent) { // Create viewer.
	 * listViewer = new ListViewer(parent); listViewer.setContentProvider(new
	 * TaskContentProvider()); listViewer.setLabelProvider(new LabelProvider());
	 * listViewer.setInput(input); getSite().setSelectionProvider(listViewer);
	 * 
	 * // Create menu and toolbars. createActions(); createMenu();
	 * createToolbar(); createContextMenu(); hookGlobalActions();
	 * 
	 * // Restore state from the previous session. restoreState(); }
	 */

	/**
	 * @see WorkbenchPart#setFocus()
	 */
	public void setFocus() {
		// listViewer.getControl().setFocus();
		// viewer.getControl().setFocus();
	}

	/**
	 * Create the actions.
	 */
	public void createActions() {
		addItemAction = new Action("Add...") {
			public void run() {
				addItem();
			}
		};
		addItemAction.setImageDescriptor(ImageProviderHelper.getImageDescriptor("add.gif"));

		deleteItemAction = new Action("Delete") {
			public void run() {
				deleteItem();
			}
		};
		editItemAction = new Action("Edit...") {
			public void run() {
				editItem();
			}
		};
		editItemAction.setImageDescriptor(ImageProviderHelper.getImageDescriptor("edit.gif"));

		deleteItemAction.setImageDescriptor(ImageProviderHelper.getImageDescriptor("delete.gif"));
		selectAllAction = new Action("Select All") {
			public void run() {
				selectAll();
			}
		};

		// Add selection listener.
		viewer.addSelectionChangedListener(new ISelectionChangedListener() {
			public void selectionChanged(SelectionChangedEvent event) {
				updateActionEnablement();
			}
		});
	}

	/**
	 * Create menu.
	 */
	private void createMenu() {
		IMenuManager mgr = getViewSite().getActionBars().getMenuManager();
		mgr.add(selectAllAction);
	}

	/**
	 * Create toolbar.
	 */
	private void createToolbar() {
		IToolBarManager mgr = getViewSite().getActionBars().getToolBarManager();
		mgr.add(addItemAction);
		mgr.add(deleteItemAction);
		mgr.add(editItemAction);
	}

	/**
	 * Create context menu.
	 */
	private void createContextMenu() {
		// Create menu manager.
		MenuManager menuMgr = new MenuManager();
		menuMgr.setRemoveAllWhenShown(true);
		menuMgr.addMenuListener(new IMenuListener() {
			public void menuAboutToShow(IMenuManager mgr) {
				fillContextMenu(mgr);
			}
		});

		// Create menu.
		Menu menu = menuMgr.createContextMenu(viewer.getControl());
		viewer.getControl().setMenu(menu);

		// Register menu for extension.
		getSite().registerContextMenu(menuMgr, viewer);
	}

	/**
	 * Hook global actions
	 */
	private void hookGlobalActions() {
		IActionBars bars = getViewSite().getActionBars();
		bars.setGlobalActionHandler(IWorkbenchActionConstants.SELECT_ALL, selectAllAction);
		bars.setGlobalActionHandler(IWorkbenchActionConstants.DELETE, deleteItemAction);
		viewer.getControl().addKeyListener(new KeyAdapter() {
			public void keyPressed(KeyEvent event) {
				if (event.character == SWT.DEL && event.stateMask == 0 && deleteItemAction.isEnabled()) {
					deleteItemAction.run();
				}
			}
		});
	}

	private void fillContextMenu(IMenuManager mgr) {
		mgr.add(addItemAction);
		mgr.add(new GroupMarker(IWorkbenchActionConstants.MB_ADDITIONS));
		mgr.add(editItemAction);
		mgr.add(deleteItemAction);
		mgr.add(new Separator());
		mgr.add(selectAllAction);
	}

	private void updateActionEnablement() {
		IStructuredSelection sel = (IStructuredSelection) viewer.getSelection();
		deleteItemAction.setEnabled(sel.size() > 0);
	}

	/**
	 * Add item to list.
	 */
	private void addItem() {
		Task task = promptForValue(null);
		if (task != null) {
			input.add(task);
			viewer.setSelection(new StructuredSelection(task));
		}
	}

	/**
	 * Edit item to list.
	 */
	private void editItem() {
		Task task = promptForValue(null);
		if (task != null) {
			input.add(task);
			viewer.setSelection(new StructuredSelection(task));
		}
	}

	/**
	 * Remove item from list.
	 */
	private void deleteItem() {
		IStructuredSelection sel = (IStructuredSelection) viewer.getSelection();
		Iterator iter = sel.iterator();
		while (iter.hasNext()) {
			Task task = (Task) iter.next();
			input.remove(task);
		}
	}

	/**
	 * Select all items.
	 */
	private void selectAll() {
		viewer.getTable().selectAll();
		updateActionEnablement();
	}

	/**
	 * Ask user for value.
	 */
	private Task promptForValue(String oldValue) {
		// TODO: old value in case of edit
		// InputDialog dlg = new InputDialog(getSite().getShell(),
		// "List View", text, oldValue, null);
		TaskDialog dlg = new TaskDialog(getSite().getShell());
		if (dlg.open() == Window.OK)
			return new Task(dlg.getName(), dlg.getDesc(), dlg.getAssignedTo());
		return null;
	}

	/**
	 * Saves the object state within a memento.
	 */
	public void saveState(IMemento memento) {
		IStructuredSelection sel = (IStructuredSelection) viewer.getSelection();
		if (sel.isEmpty())
			return;
		memento = memento.createChild("selection");
		Iterator iter = sel.iterator();
		while (iter.hasNext()) {
			Task task = (Task) iter.next();
			memento.createChild("descriptor", task.toString());
		}
	}

	/**
	 * Restores the listViewer state from the memento.
	 */
	private void restoreState() {
		if (memento == null)
			return;
		memento = memento.getChild("selection");
		if (memento != null) {
			IMemento descriptors[] = memento.getChildren("descriptor");
			if (descriptors.length > 0) {
				ArrayList objList = new ArrayList(descriptors.length);
				for (int nX = 0; nX < descriptors.length; nX++) {
					String id = descriptors[nX].getID();
					Task task = input.find(id);
					if (task != null)
						objList.add(task);
				}
				viewer.setSelection(new StructuredSelection(objList));
			}
		}
		memento = null;
		updateActionEnablement();
	}
}
