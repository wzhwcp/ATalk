package com.myq.atalk.util;

import java.io.ByteArrayInputStream;
import java.sql.Connection;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.jivesoftware.smack.AccountManager;
import org.jivesoftware.smack.Chat;
import org.jivesoftware.smack.ChatManager;
import org.jivesoftware.smack.ChatManagerListener;
import org.jivesoftware.smack.ConnectionConfiguration;
import org.jivesoftware.smack.ConnectionConfiguration.SecurityMode;
import org.jivesoftware.smack.MessageListener;
import org.jivesoftware.smack.Roster;
import org.jivesoftware.smack.RosterEntry;
import org.jivesoftware.smack.RosterGroup;
import org.jivesoftware.smack.XMPPConnection;
import org.jivesoftware.smack.packet.Message;
import org.jivesoftware.smack.packet.Presence;
import org.jivesoftware.smackx.packet.VCard;

import com.myq.atalk.R;

import android.app.Notification;
import android.app.NotificationManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;
import android.util.Log;

/**
 * 
 * @author quan Smack������
 */
public class SmackUtil {

	public static XMPPConnection connection;
	/** ������ip��ַ */
	public static final String ipAddress = "112.74.110.102";
	/** ������ip�˿� */
	public static final int port = 5222;
	/** ��¼���û��� */
	public static String username = "";
	public static Map<String, Chat> chatManage = new HashMap<String, Chat>();
	public static NotificationManager nManager;

	/**
	 * ���ӷ�����
	 * 
	 * @return �ɹ�true ʧ��false
	 */
	public static boolean connectServer() {
		ConnectionConfiguration configuration = new ConnectionConfiguration(
				ipAddress,port);
		configuration.setSecurityMode(SecurityMode.disabled);
		configuration.setSASLAuthenticationEnabled(false);
		connection = new XMPPConnection(configuration);
		try {
			connection.connect();
			return true;
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
			return false;
		}

	}

	/**
	 * �Ͽ�����
	 */
	public static void disConnect() {
		connection.disconnect();
	}

	/**
	 * ��¼
	 * 
	 * @param username
	 *            �û���
	 * @param password
	 *            ����
	 * @return
	 */
	public static boolean login(String username, String password) {
		try {
			connection.login(username, password);
			SmackUtil.username = username;
			return true;
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
			return false;
		}
	}

	/**
	 * �½�һ���û�
	 * 
	 * @param conn
	 * @param username
	 * @param password
	 * @return
	 */
	public static boolean addAccount(XMPPConnection conn, String username,
			String password) {
		AccountManager manager = conn.getAccountManager();
		try {
			manager.createAccount(username, password);
			return true;
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return false;
	}

	/**
	 * ��Ӻ���
	 * 
	 * @param conn
	 */
	public static void addFriend(XMPPConnection conn) {
		Roster roster = conn.getRoster();
		try {
			roster.createEntry("NewUser@air-pc", null,
					new String[] { "friends" });
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}
	}

	/**
	 * ��ȡ�û�ͷ��
	 * 
	 * @param username
	 *            �û���
	 * @return
	 */
	public static Bitmap getUserImage(String username) {
		ByteArrayInputStream bis = null;
		VCard vCard = new VCard();
		try {
			vCard.load(connection, username + "@" + connection.getServiceName());
			if (vCard == null || vCard.getAvatar() == null) {
				return null;
			}
			bis = new ByteArrayInputStream(vCard.getAvatar());
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}
		return BitmapFactory.decodeStream(bis);
	}

	/**
	 * ��ȡ���з���
	 * 
	 * @return
	 */
	public static String[] getGroup() {
		List<String> list = new ArrayList<String>();
		Collection<RosterGroup> collection = connection.getRoster().getGroups();
		Iterator<RosterGroup> iterator = collection.iterator();
		while (iterator.hasNext()) {
			list.add(iterator.next().getName());
		}
		String[] groupNames = new String[list.size()];
		for (int i = 0; i < list.size(); i++) {
			groupNames[i] = list.get(i);
		}
		return groupNames;
	}

	/**
	 * ��ȡ��������б�
	 * 
	 * @param groupName
	 *            ������
	 * @return
	 */
	public static String[] getFriendsName(String groupName) {
		List<String> list = new ArrayList<String>();
		RosterGroup group = connection.getRoster().getGroup(groupName);
		Collection<RosterEntry> collection = group.getEntries();
		Iterator<RosterEntry> iterator = collection.iterator();
		while (iterator.hasNext()) {
			list.add(iterator.next().getName());
		}
		String[] friendsName = new String[list.size()];
		for (int i = 0; i < list.size(); i++) {
			friendsName[i] = list.get(i);
		}
		return friendsName;
	}

	/**
	 * ��ȡ����ͷ��
	 * 
	 * @param friendsName
	 * @return
	 */
	public static Bitmap[] getFriendsIcon(String[] friendsName) {
		Bitmap[] bitmaps = new Bitmap[friendsName.length];
		for (int i = 0; i < friendsName.length; i++) {
			bitmaps[i] = getUserImage(friendsName[i]);
		}
		return bitmaps;
	}

	/**
	 * ��ȡ����״̬
	 * 
	 * @param friendsName
	 *            ������
	 * @return
	 */
	public static boolean getFriendsStatus(String friendsName) {
		boolean status;
		Presence presence = connection.getRoster().getPresence(
				friendsName + "@" + connection.getServiceName());
		if (presence.isAvailable()) {// ����
			status = true;
		} else { // ����
			status = false;
		}
		return status;
	}

	/**
	 * ��ȡ���ѷ�������״̬
	 * 
	 * @param groupName������
	 * @return
	 */
	public static String getGroupStatus(String groupName) {
		int num0 = 0;// ��������
		int num1 = 0;// ������������
		RosterGroup group = connection.getRoster().getGroup(groupName);
		Collection<RosterEntry> collection = group.getEntries();
		Iterator<RosterEntry> iterator = collection.iterator();
		while (iterator.hasNext()) {
			num0++;
			Presence presence = connection.getRoster().getPresence(
					iterator.next().getUser());
			if (presence.isAvailable()) {
				num1++;
			}
		}
		return num1 + "/" + num0;

	}

	/**
	 * ��ȡ�򴴽����촰��
	 * 
	 * @param username
	 *            ������
	 * @param listener
	 *            ���������
	 * @return
	 */
	public static Chat getFriendChat(String username) {
		if (chatManage.containsKey(username)) {
			return chatManage.get(username);
		}
		Chat chat = connection.getChatManager().createChat(
				username + "@" + connection.getServiceName(),
				new MessageListener() {

					@Override
					public void processMessage(Chat arg0, Message msg) {
						// TODO Auto-generated method stub
						Log.e("chat", msg.getBody());
					}
				});
		chatManage.put(username, chat);
		return chat;
	}

	public static class MyChatManagerListener implements ChatManagerListener {

		@Override
		public void chatCreated(Chat chat, boolean arg1) {
			// TODO Auto-generated method stub
			chat.addMessageListener(new MessageListener() {
				@Override
				public void processMessage(Chat arg0, Message msg) {
					// TODO Auto-generated method stub
					String str = msg.getFrom() + "-->" + msg.getTo() + ":"
							+ msg.getBody();
					Log.e("message", str);
				}
			});

		}

	}
}
