package com.asu.score.hackslash.actions.im;

import java.io.IOException;
import java.util.List;

import org.eclipse.swt.widgets.Display;
import org.eclipse.ui.IViewPart;
import org.eclipse.ui.PlatformUI;
import org.jivesoftware.smack.SmackException;
import org.jivesoftware.smack.SmackException.NoResponseException;
import org.jivesoftware.smack.SmackException.NotConnectedException;
import org.jivesoftware.smack.SmackException.NotLoggedInException;
import org.jivesoftware.smack.XMPPException;
import org.jivesoftware.smack.XMPPException.XMPPErrorException;
import org.jivesoftware.smack.chat.Chat;
import org.jivesoftware.smack.chat.ChatManager;
import org.jivesoftware.smack.chat.ChatManagerListener;
import org.jivesoftware.smack.chat.ChatMessageListener;
import org.jivesoftware.smack.packet.Message;
import org.jivesoftware.smack.packet.Presence;
import org.jivesoftware.smack.packet.Presence.Type;
import org.jivesoftware.smack.roster.Roster;
import org.jivesoftware.smack.roster.Roster.SubscriptionMode;
import org.jivesoftware.smack.tcp.XMPPTCPConnection;

import com.asu.score.hackslash.chathelper.ActiveChats;
import com.asu.score.hackslash.chathelper.LocalChat;
import com.asu.score.hackslash.dao.TMemberDAO;
import com.asu.score.hackslash.engine.SessionManager;
import com.asu.score.hackslash.userhelper.User;
import com.asu.score.hackslash.views.ChatView;

/**
 * Controls the chat commands and messages exchanged between users.
 */
public class ChatController {

	private static ChatController controller;

	private XMPPTCPConnection connection;

	private ChatManager chatManager;
	private ChatMessageListener messageListener;
	private ChatManagerListener managerListener;

    private ChatController() {
    }
    
    /**
     * Returns the on and only chat controller instance of this class.
     * @return
     * @throws XMPPException
     * @throws SmackException
     * @throws IOException
     */
	public static synchronized ChatController getInstance()
			throws XMPPException, SmackException, IOException {
		// Synchronize to ensure that we don't end up creating two singletons
		if (null == controller) {
			controller = new ChatController();
		}
		return controller;
	}

	/**
	 * Initializes chat environment
	 * @throws XMPPException
	 * @throws SmackException
	 * @throws IOException
	 */
	public void init() throws XMPPException, SmackException, IOException {
		connection = SessionManager.getInstance().getConnection();
		chatManager = ChatManager.getInstanceFor(connection);
		managerListener = new MyManagerListener();
		chatManager.addChatListener(managerListener);
		Roster.setDefaultSubscriptionMode(SubscriptionMode.accept_all);
	}
	
	/**
	 * Sets status of the user visible to other users in the roster.
	 * @param available
	 * @param status
	 * @throws NotConnectedException
	 */
	public void setStatus(boolean available, String status)
			throws NotConnectedException {

		Presence.Type type = available ? Type.available : Type.unavailable;
		Presence presence = new Presence(type);

		presence.setStatus(status);
		connection.sendStanza(presence);

	}

	public void sendMessage(String message, String buddyJID)
			throws NotConnectedException {
		System.out.println(String.format("Sending mesage '%1$s' to user %2$s",
				message, buddyJID));
		Chat chat = chatManager.createChat(buddyJID, messageListener);
		chat.sendMessage(message);
	}
	
	public void sendMessage(String message)
			throws NotConnectedException {
		UsersService userSer = new UsersService();
		List<User> users;
		try {
			users = userSer.getAllUsers();
			for (User user : users) {
				System.out.println(String.format("Sending mesage '%1$s' to user %2$s",
						message, user.getName()));
				String me = user.getName().substring(0, user.getName().indexOf('@')).toLowerCase();
				if(SessionManager.getInstance().getUsername().toLowerCase().contains(me)){
					Display.getDefault().asyncExec(new Runnable() {
					    @Override
					    public void run() {
							ChatView chatView = (ChatView) PlatformUI.getWorkbench().getActiveWorkbenchWindow().getActivePage().findView("com.asu.score.hackslash.views.ChatView");
							if(chatView != null) {
								chatView.addItem(new com.asu.score.hackslash.chathelper.Chat(me.toUpperCase(), message));				
							}
					    }
					});
					continue;
				}
				Chat chat = chatManager.createChat(user.getName(), messageListener);
				chat.sendMessage(message);
			}
		} catch (SmackException | IOException | XMPPException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public void createEntry(String user)
			throws NotLoggedInException, NoResponseException,
			XMPPErrorException, NotConnectedException {
		System.out.println(String.format(
				"Creating entry for buddy '%1$s'", user));
		Roster roster = Roster.getInstanceFor(connection);
		roster.createEntry(user, null, null);
	}
	
	/**
	 * Updates rosters after getting all users from the database
	 */
	public void updateRoster(){
		TMemberDAO tmDao = new TMemberDAO();
		List<String> userList = tmDao.getUsers();
		String currentUser = SessionManager.getInstance().getUserJID();
		
		try {
				/*
				 * Roster entry subscription mode should be "both" to see each others last login
				 */
				//userList.removeAll(uList);
				for (String user : userList){
					if (!user.equals(currentUser)){
						createEntry(user);
					}
				}
			} catch (NotLoggedInException | NoResponseException | XMPPErrorException | NotConnectedException e) {
				System.out.println("Problem while updating Roster!!");
				e.printStackTrace();
			}
	}

	class MyMessageListener implements ChatMessageListener {

		@Override
		public void processMessage(Chat chat, Message message) {
			String from = message.getFrom();
			String body = message.getBody();
			System.out.println(String.format(
					"Received message '%1$s' from %2$s", body, from));

			Display.getDefault().asyncExec(new Runnable() {
			    @Override
			    public void run() {
					ChatView chatView = (ChatView) PlatformUI.getWorkbench().getActiveWorkbenchWindow().getActivePage().findView("com.asu.score.hackslash.views.ChatView");
					if(chatView != null) {
						chatView.addItem(new com.asu.score.hackslash.chathelper.Chat(from, body));				
					}
			    }
			});
			/*boolean flag = false;
			LocalChat lChat = ActiveChats.getChatIfAlreadyExist(from);
			if(lChat == null){
				lChat = new LocalChat(from, new Display());
			}
			lChat.receivedChat(body);*/
			
		}

	}

	class MyManagerListener implements ChatManagerListener {

		@Override
		public void chatCreated(final Chat chat, final boolean createdLocally) {
			messageListener = new MyMessageListener();
			chat.addMessageListener(messageListener);
		}

	}

}