package com.asu.score.hackslash.views;

import java.io.IOException;
import java.util.Set;

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
import org.eclipse.jface.viewers.IStructuredContentProvider;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.ITableLabelProvider;
import org.eclipse.jface.viewers.LabelProvider;
import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.jface.viewers.Viewer;
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
import org.jivesoftware.smack.SmackException.NoResponseException;
import org.jivesoftware.smack.SmackException.NotConnectedException;
import org.jivesoftware.smack.SmackException.NotLoggedInException;
import org.jivesoftware.smack.XMPPException;
import org.jivesoftware.smack.XMPPException.XMPPErrorException;
import org.jivesoftware.smack.tcp.XMPPTCPConnection;

import com.asu.score.hackslash.actions.im.ChatController;
import com.asu.score.hackslash.actions.im.Users;
import com.asu.score.hackslash.dialogs.AddContactDialog;
import com.asu.score.hackslash.dialogs.ChatDialog;
import com.asu.score.hackslash.dialogs.LoginDialog;
import com.asu.score.hackslash.engine.ConnectionManger;
import com.asu.score.hackslash.engine.SessionManager;
import com.asu.score.hackslash.helper.ImageProviderHelper;
import com.asu.score.hackslash.properties.Constants;

public class UsersView extends ViewPart {
	/**
	 * The ID of the view as specified by the extension.
	 */
	public static final String ID = "com.asu.score.hackslash.views.TaskView";

	private TableViewer viewer;
	private Action refreshAction;
	private Action loginAction;
	private Action addContactAction;
	private Action doubleClickAction;
	private ChatController chatController;

	/*
	 * The content provider class is responsible for providing objects to the
	 * view. It can wrap existing objects in adapters or simply return objects
	 * as-is. These objects may be sensitive to the current input of the view,
	 * or ignore it and always show the same content (like Task List, for
	 * example).
	 */

	class ViewContentProvider implements IStructuredContentProvider {
		public void inputChanged(Viewer v, Object oldInput, Object newInput) {
		}

		public void dispose() {
		}

		public Object[] getElements(Object parent) {
			if (SessionManager.getInstance().isAuthenticated()) {
				System.out.println("fetching all users");
				Set<String> users = Users.getAllUser();
				System.out.println(users.size() + " contacts founds");
				String[] user_list_array = users.toArray(new String[users
						.size()]);
				if (user_list_array != null) {
					return user_list_array;
				} else {
					return new Object[] {};
				}
			}
			return new String[] { "Log In To Start" };
		}
	}

	class ViewLabelProvider extends LabelProvider implements
			ITableLabelProvider {
		public String getColumnText(Object obj, int index) {
			return getText(obj);
		}

		public Image getColumnImage(Object obj, int index) {
			return getImage(obj);
		}

		public Image getImage(Object obj) {
			if (ConnectionManger.isUserLoggedIn()) {
				String type = Users.getUserPresenceType(obj.toString());
				if ("available".equals(type)) {
					return ImageProviderHelper.getImageDescriptor("Online.gif")
							.createImage();
				}
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
	}

	/**
	 * This is a callback that will allow us to create the viewer and initialize
	 * it.
	 */
	public void createPartControl(Composite parent) {
		viewer = new TableViewer(parent, SWT.MULTI | SWT.H_SCROLL
				| SWT.V_SCROLL);
		viewer.setContentProvider(new ViewContentProvider());
		viewer.setLabelProvider(new ViewLabelProvider());
		viewer.setSorter(new NameSorter());
		viewer.setInput(getViewSite());

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
				viewer.refresh();
			}
		};
		refreshAction.setText("Refresh");
		refreshAction.setToolTipText("Refresh View");
		refreshAction.setImageDescriptor(ImageProviderHelper
				.getImageDescriptor("refresh.gif"));

		loginAction = new Action() {
			public void run() {
				LoginDialog dialog = new LoginDialog(getSite().getShell());
				String message = "";
				SessionManager session = SessionManager.getInstance();

				// get the new values from the dialog
				int result = dialog.open();
				if (result == IDialogConstants.OK_ID) {
					try {
						if (!session.isAuthenticated()) {
							XMPPTCPConnection conn = ConnectionManger
									.getConnection();
							String user = dialog.getUser();
							String pwrd = dialog.getPassword();

							try {
								ConnectionManger.login(user, pwrd);
								message = "Hello " + user
										+ ", Welcome to DSD work enviroment";
								session.setServerAddress(conn.getServiceName());
								session.initializeSession(conn, user, pwrd);
								session.setJID(conn.getUser());
								viewer.refresh();

							} catch (XMPPException | SmackException
									| IOException e) {
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
						viewer.refresh();
					} catch (SmackException | IOException | XMPPException e) {
						message = "Unable to Log out User!";
						e.printStackTrace();
					}
					showMessage(message);
				}
			}
		};
		loginAction.setText("Login");
		loginAction.setToolTipText("Login");
		loginAction.setImageDescriptor(PlatformUI.getWorkbench()
				.getSharedImages()
				.getImageDescriptor(ISharedImages.IMG_ETOOL_HOME_NAV));

		addContactAction = new Action() {
			public void run() {
				AddContactDialog dialog = new AddContactDialog(getSite()
						.getShell());
				String message = "";
				SessionManager session = SessionManager.getInstance();

				// get the new values from the dialog
				int result = dialog.open();
				if (result == IDialogConstants.OK_ID) {
					if (session.isAuthenticated()) {
						String buddyJID = dialog.getUser();
						String buddyName = dialog.getPassword();
						try {
							ChatController chatCtrl = ChatController
									.getInstance();
							chatCtrl.createEntry(buddyJID, buddyName);
							message = "Buddy - " + buddyName
									+ " - added successfully";
							viewer.refresh();
						} catch (XMPPException | SmackException | IOException e) {
							message = "Unable to add Buddy. Chat Controller not Available.";
							e.printStackTrace();
						}
						showMessage(message);
					}
				} else if (result == IDialogConstants.CLOSE_ID) {
					dialog.close();
				}
			}
		};
		addContactAction.setText("Add Contact");
		addContactAction.setToolTipText("Add Contact");
		addContactAction.setImageDescriptor(ImageProviderHelper
				.getImageDescriptor("add.gif"));

		doubleClickAction = new Action() {
			public void run() {
				ISelection selection = viewer.getSelection();
				Object obj = ((IStructuredSelection) selection)
						.getFirstElement();
				ChatDialog dialog = new ChatDialog(getSite().getShell());
				try {
					chatController = ChatController.getInstance();
				} catch (SmackException | IOException | XMPPException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}

				if (dialog.open() == Window.OK)
					try {
						chatController.createEntry(obj.toString(),
								obj.toString());
						chatController.sendMessage(dialog.getMsg().trim(),
								obj.toString() + Constants.SERVER_NAME);
					} catch (NotConnectedException | NotLoggedInException
							| NoResponseException | XMPPErrorException e) {
						showMessage("Unable to send Chat");
						e.printStackTrace();
					}
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

	/**
	 * Passing the focus request to the viewer's control.
	 */
	public void setFocus() {
		viewer.getControl().setFocus();
	}
}