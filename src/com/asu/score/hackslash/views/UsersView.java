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
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Menu;
import org.eclipse.swt.widgets.TableColumn;
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

import com.asu.score.hackslash.actions.im.ChatController;
import com.asu.score.hackslash.chathelper.ActiveChats;
import com.asu.score.hackslash.chathelper.LocalChat;
import com.asu.score.hackslash.dao.UsersDAO;
import com.asu.score.hackslash.dialogs.AddContactDialog;
import com.asu.score.hackslash.dialogs.ChatDialog;
import com.asu.score.hackslash.dialogs.LoginDialog;
import com.asu.score.hackslash.dialogs.UserSessionLogDialog;
import com.asu.score.hackslash.engine.ConnectionManager;
import com.asu.score.hackslash.engine.SessionManager;
import com.asu.score.hackslash.helper.ImageProviderHelper;
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

			doubleClickAction, userSessionLogAction;
	private UserInput input;

	private SessionManager session = SessionManager.getInstance();

	class ViewLabelProvider extends LabelProvider implements
			ITableLabelProvider {

		public String getColumnText(Object obj, int index) {
			String txt = "";
			if (session.isAuthenticated()) {
				if (obj instanceof User) {
					User u = (User) obj;
					if (index == 0) {
						txt = u.getName();
					} else if (index == 1) {
						if ("unavailable".equals(u.getStatus())) {
							txt = u.getLastSeen();
						}
					}
				}
			} else {
				if (index == 0) {
					txt = obj.toString();
				}
			}
			return txt;
		}

		public Image getColumnImage(Object obj, int index) {
			if (index == 0) {
				return getImage(obj);
			}
			return null;
		}

		public Image getImage(Object obj) {
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

		viewer = new TableViewer(parent, SWT.SINGLE | SWT.H_SCROLL
				| SWT.V_SCROLL);
		viewer.setContentProvider(new UserContentProvider());
		viewer.setLabelProvider(new ViewLabelProvider());
		viewer.setSorter(new NameSorter());
		viewer.setInput(input);
		TableColumn user = new TableColumn(viewer.getTable(), SWT.LEFT);
		user.setWidth(150);
		TableColumn lastLogin = new TableColumn(viewer.getTable(), SWT.RIGHT);
		lastLogin.setWidth(150);

		// Create the help context id for the viewer's control
		PlatformUI.getWorkbench().getHelpSystem()
				.setHelp(viewer.getControl(), "com.asu.score.hackslash.viewer");
		getSite().setSelectionProvider(viewer);
		makeActions();
		hookContextMenu();
		//hookDoubleClickAction();
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
		Roster roster = ConnectionManager.getRoster();

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

		manager.add(userSessionLogAction);
		// Other plug-ins can contribute there actions here
		manager.add(new Separator(IWorkbenchActionConstants.MB_ADDITIONS));
	}

	private void fillLocalToolBar(IToolBarManager manager) {
		manager.add(refreshAction);
		manager.add(loginAction);

		manager.add(userSessionLogAction);
	}

	private void makeActions() {
		refreshAction = new Action() {

			public void run() {
				System.out.println("Refreshing User List....");
				try {
					if (session.isAuthenticated()){
						ChatController.getInstance().updateRoster();
					}
				} catch (XMPPException | SmackException | IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
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

//		doubleClickAction = new Action() {
//			public void run() {
//				onDoubleClick();
//			}
//		};
		
		userSessionLogAction = new Action() {
			public void run() {
				ISelection sel = viewer.getSelection();
				Object obj = ((IStructuredSelection) sel).getFirstElement();
				if(obj != null) {
					createUserSessionLogDialog((User) obj);					
				}
			}	
		};
		userSessionLogAction.setText("User Login History");
		userSessionLogAction.setToolTipText("User Login History");
		userSessionLogAction.setImageDescriptor(ImageProviderHelper
				.getImageDescriptor("history.png"));

	}

	/*private void hookDoubleClickAction() {
		viewer.addDoubleClickListener(new IDoubleClickListener() {
			public void doubleClick(DoubleClickEvent event) {
				doubleClickAction.run();
			}
		});
	}*/

	private void showMessage(String message) {
		MessageDialog.openInformation(viewer.getControl().getShell(),
				"DSD User View", message);
	}

	private void performLogin() {
		LoginDialog dialog = new LoginDialog(getSite().getShell());
		String message = "";

		// get the new values from the dialog
		int result = dialog.open();
		if (result == IDialogConstants.OK_ID) {
			if (!session.isAuthenticated()) {
				String user = dialog.getUser();
				String pwrd = dialog.getPassword();
				try {
					ConnectionManager.login(user, pwrd);
					message = "Hello " + user
							+ ", Welcome to DSD work enviroment";
					hookRosterListeners();
					input.refresh();
				} catch (XMPPException | SmackException | IOException e) {
					message = "UnAuthorized Username or Password!";
				}
				showMessage(message);
			}
		} else if (result == IDialogConstants.CLOSE_ID) {
			UsersDAO usersDao = new UsersDAO();

			usersDao.addUserSessionTime(session.getUserJID(),
					session.getLoginTime());
			session.logout();
			message = "User Logged Out Successfully.";
			showMessage(message);
			input.refresh();
		}
	}
	
	public void refresh() {
		input.refresh();
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
		if (!session.isAuthenticated()) {
			performLogin();
			return;
		}

		User user = (User) obj;
		LocalChat lChat = ActiveChats.getChatIfAlreadyExist(user.getName());
		if (lChat == null) {
			lChat = new LocalChat(user.getName(), getSite().getShell()
					.getDisplay());
		}
		lChat.sendMessege();
		// createChatDialog(user, "");

	}
	private void createUserSessionLogDialog(User user) {
		UserSessionLogDialog dlg = new UserSessionLogDialog(getSite().getShell(),
				user.getName());
		dlg.open();
	}

	private void createChatDialog(User user, String msg) {
		ChatDialog dialog = new ChatDialog(getSite().getShell(),
				user.getName(), msg);
		if (dialog.open() == Window.OK)
			try {
				ChatController chatController = ChatController.getInstance();
				chatController.sendMessage(dialog.getMsg().trim(),
						user.getName());
				msg += "\n" + dialog.getMsg().trim();
				createChatDialog(user, msg);
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