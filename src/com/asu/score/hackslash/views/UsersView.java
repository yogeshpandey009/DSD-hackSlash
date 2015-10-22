package com.asu.score.hackslash.views;

import java.io.IOException;
import java.util.Collection;

import org.eclipse.jface.action.Action;
import org.eclipse.jface.action.IMenuListener;
import org.eclipse.jface.action.IMenuManager;
import org.eclipse.jface.action.IToolBarManager;
import org.eclipse.jface.action.MenuManager;
import org.eclipse.jface.action.Separator;
import org.eclipse.jface.dialogs.IDialogConstants;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.viewers.DoubleClickEvent;
import org.eclipse.jface.viewers.IDoubleClickListener;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.ITableLabelProvider;
import org.eclipse.jface.viewers.LabelProvider;
import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.jface.viewers.ViewerSorter;
import org.eclipse.jface.window.Window;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Menu;
import org.eclipse.ui.IActionBars;
import org.eclipse.ui.ISharedImages;
import org.eclipse.ui.IWorkbenchActionConstants;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.part.ViewPart;
import org.jivesoftware.smack.SmackException;
import org.jivesoftware.smack.XMPPException;
import org.jivesoftware.smack.packet.Presence;
import org.jivesoftware.smack.roster.Roster;
import org.jivesoftware.smack.roster.RosterListener;
import org.jivesoftware.smack.tcp.XMPPTCPConnection;

import com.asu.score.hackslash.actions.im.ChatController;
import com.asu.score.hackslash.dialogs.AddContactDialog;
import com.asu.score.hackslash.dialogs.ChatDialog;
import com.asu.score.hackslash.dialogs.LoginDialog;
import com.asu.score.hackslash.engine.ConnectionManger;
import com.asu.score.hackslash.engine.SessionManager;
import com.asu.score.hackslash.helper.ImageProviderHelper;
import com.asu.score.hackslash.properties.Constants;
import com.asu.score.hackslash.userhelper.User;
import com.asu.score.hackslash.userhelper.UserContentProvider;
import com.asu.score.hackslash.userhelper.UserInput;

public class UsersView extends ViewPart {
	/**
	 * The ID of the view as specified by the extension.
	 */
	public static final String ID = "com.asu.score.hackslash.views.TaskView";

	private TableViewer viewer;
	private Action refreshAction, loginAction, addContactAction,
			doubleClickAction;
	private UserInput input;

	class ViewLabelProvider extends LabelProvider implements
			ITableLabelProvider {

		public String getColumnText(Object obj, int index) {
			SessionManager session = SessionManager.getInstance();
			String txt = "";
			if (session.isAuthenticated()) {
				if (obj instanceof User) {
					User u = (User) obj;
					txt = u.getName();
				}
			} else {
				txt = obj.toString();
			}
			return txt;
		}

		public Image getColumnImage(Object obj, int index) {
			return getImage(obj);
		}

		public Image getImage(Object obj) {
			SessionManager session = SessionManager.getInstance();
			if (session.isAuthenticated()) {
				if (obj instanceof User) {
					User u = (User) obj;
					if ("available".equals(u.getStatus())) {
						return ImageProviderHelper.getImageDescriptor(
								"Online.gif").createImage();
					}
				}
				// String type =
				// UsersService.getUserPresenceType(obj.toString());
			}
			return ImageProviderHelper.getImageDescriptor("Offline.gif")
					.createImage();
		}
	}

	class NameSorter extends ViewerSorter {
	}

	/**
	 * The constructor.
	 */
	public UsersView() {
		super();
		input = new UserInput();
	}

	/**
	 * This is a callback that will allow us to create the viewer and initialize
	 * it.
	 */
	public void createPartControl(Composite parent) {
		viewer = new TableViewer(parent, SWT.MULTI | SWT.H_SCROLL
				| SWT.V_SCROLL);
		viewer.setContentProvider(new UserContentProvider());
		viewer.setLabelProvider(new ViewLabelProvider());
		viewer.setSorter(new NameSorter());
		viewer.setInput(input);

		// Create the help context id for the viewer's control
		PlatformUI.getWorkbench().getHelpSystem()
				.setHelp(viewer.getControl(), "com.asu.score.hackslash.viewer");
		getSite().setSelectionProvider(viewer);
		makeActions();
		hookContextMenu();
		hookDoubleClickAction();
		contributeToActionBars();
	}

	private void hookContextMenu() {
		MenuManager menuMgr = new MenuManager("#PopupMenu");
		menuMgr.setRemoveAllWhenShown(true);
		menuMgr.addMenuListener(new IMenuListener() {
			public void menuAboutToShow(IMenuManager manager) {
				UsersView.this.fillContextMenu(manager);
			}
		});
		Menu menu = menuMgr.createContextMenu(viewer.getControl());
		viewer.getControl().setMenu(menu);
		getSite().registerContextMenu(menuMgr, viewer);
	}

	private void contributeToActionBars() {
		IActionBars bars = getViewSite().getActionBars();
		fillLocalPullDown(bars.getMenuManager());
		fillLocalToolBar(bars.getToolBarManager());
	}
	
	private void hookRosterListeners() {
		Roster roster = ConnectionManger.getRoster();
		
		roster.addRosterListener(new RosterListener() {
			@Override
			public void entriesAdded(Collection<String> addresses) {
				input.refresh();
			}
			@Override
			public void entriesUpdated(Collection<String> addresses) {
				input.refresh();
				
			}
			@Override
			public void entriesDeleted(Collection<String> addresses) {
				input.refresh();
			}
			@Override
			public void presenceChanged(Presence presence) {
				input.refresh();
			}
		});
	}

	private void fillLocalPullDown(IMenuManager manager) {
		manager.add(addContactAction);
	}

	private void fillContextMenu(IMenuManager manager) {
		manager.add(refreshAction);
		manager.add(loginAction);
		// Other plug-ins can contribute there actions here
		manager.add(new Separator(IWorkbenchActionConstants.MB_ADDITIONS));
	}

	private void fillLocalToolBar(IToolBarManager manager) {
		manager.add(refreshAction);
		manager.add(loginAction);
	}

	private void makeActions() {
		refreshAction = new Action() {
			public void run() {
				System.out.println("Refreshing User List....");
				input.refresh();
			}
		};
		refreshAction.setText("Refresh");
		refreshAction.setToolTipText("Refresh View");
		refreshAction.setImageDescriptor(ImageProviderHelper
				.getImageDescriptor("refresh.gif"));

		loginAction = new Action() {
			public void run() {
				performLogin();
			}
		};
		loginAction.setText("Login");
		loginAction.setToolTipText("Login");
		loginAction.setImageDescriptor(PlatformUI.getWorkbench()
				.getSharedImages()
				.getImageDescriptor(ISharedImages.IMG_ETOOL_HOME_NAV));

		addContactAction = new Action() {
			public void run() {
				addContact();
			}
		};
		addContactAction.setText("Add Contact");
		addContactAction.setToolTipText("Add Contact");
		addContactAction.setImageDescriptor(ImageProviderHelper
				.getImageDescriptor("add.gif"));

		doubleClickAction = new Action() {
			public void run() {
				onDoubleClick();
			}
		};
	}

	private void hookDoubleClickAction() {
		viewer.addDoubleClickListener(new IDoubleClickListener() {
			public void doubleClick(DoubleClickEvent event) {
				doubleClickAction.run();
			}
		});
	}

	private void showMessage(String message) {
		MessageDialog.openInformation(viewer.getControl().getShell(),
				"DSD User View", message);
	}

	private void performLogin() {
		LoginDialog dialog = new LoginDialog(getSite().getShell());
		String message = "";
		SessionManager session = SessionManager.getInstance();

		// get the new values from the dialog
		int result = dialog.open();
		if (result == IDialogConstants.OK_ID) {
			try {
				if (!session.isAuthenticated()) {
					XMPPTCPConnection conn = ConnectionManger.getConnection();
					String user = dialog.getUser();
					String pwrd = dialog.getPassword();

					try {
						ConnectionManger.login(user, pwrd);
						message = "Hello " + user
								+ ", Welcome to DSD work enviroment";
						session.setServerAddress(conn.getServiceName());
						session.initializeSession(conn, user, pwrd);
						session.setJID(conn.getUser());
						hookRosterListeners();
					} catch (XMPPException | SmackException | IOException e) {
						message = "UnAuthorized Username or Password!";
					}
					showMessage(message);
				}
			} catch (SmackException | IOException | XMPPException e) {
				message = "Failed to Login to Server";
			}
		} else if (result == IDialogConstants.CLOSE_ID) {
			try {
				ConnectionManger.disconnect();
				message = "User Logged Out Successfully.";
				//input.refresh();
			} catch (SmackException | IOException | XMPPException e) {
				message = "Unable to Log out User!";
				e.printStackTrace();
			}
			showMessage(message);
		}
	}

	private void addContact() {

		AddContactDialog dialog = new AddContactDialog(getSite().getShell());
		String message = "";
		// get the new values from the dialog
		int result = dialog.open();
		if (result == IDialogConstants.OK_ID) {
			message = input.add(dialog.getUser());
			showMessage(message);
		} else if (result == IDialogConstants.CLOSE_ID) {
			dialog.close();
		}

	}

	private void onDoubleClick() {

		ISelection selection = viewer.getSelection();
		Object obj = ((IStructuredSelection) selection).getFirstElement();
		SessionManager session = SessionManager.getInstance();
		if (!session.isAuthenticated()) {
			performLogin();
			return;
		}
		
		User user = (User)obj;
		ChatDialog dialog = new ChatDialog(getSite().getShell(), user.getName());
		if (dialog.open() == Window.OK)
			try {
				ChatController chatController = ChatController.getInstance();
				chatController.sendMessage(dialog.getMsg().trim(),
						obj.toString() + Constants.SERVER_NAME);
			} catch (XMPPException | SmackException | IOException e) {
				showMessage("Unable to send Chat");
				e.printStackTrace();
			}

	}

	/**
	 * Passing the focus request to the viewer's control.
	 */
	public void setFocus() {
		viewer.getControl().setFocus();
	}
}