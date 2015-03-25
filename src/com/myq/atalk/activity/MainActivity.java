package com.myq.atalk.activity;

import org.jivesoftware.smack.Chat;
import org.jivesoftware.smack.ChatManagerListener;
import org.jivesoftware.smack.MessageListener;

import android.app.Activity;
import android.app.FragmentTransaction;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.widget.FrameLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RadioGroup.OnCheckedChangeListener;
import android.widget.TextView;

import com.myq.atalk.R;
import com.myq.atalk.DAO.ChatDAO;
import com.myq.atalk.DAO.MessageDAO;
import com.myq.atalk.bean.DBInfo;
import com.myq.atalk.fragment.ContactsFragment;
import com.myq.atalk.fragment.DiscoverFragment;
import com.myq.atalk.fragment.MessageFragment;
import com.myq.atalk.util.MyUtil;
import com.myq.atalk.util.SmackUtil;

/**
 * 主界面，包含三个子fragment
 * 
 * @author quan
 * 
 */
public class MainActivity extends Activity {
	private String activityName = "";
	private int msgnum = 0; // 消息数量
	private int notifiId = 0x23; // 通知栏id
	private RadioGroup radioGroup;
	private RadioButton radioMessage;
	private RadioButton radioContacts;
	private RadioButton radioDiscover;
	private TextView numTextView;
	private FrameLayout frameLayout;
	private MessageFragment messageFragment = null;
	private ContactsFragment contactsFragment = null;
	private DiscoverFragment discoverFragment = null;
	private FragmentTransaction transaction; // Fragment事务
	private NotificationManager nManager; // 通知栏manager
	private ChatDAO chatDAO;
	private MessageDAO messageDAO;
	private Handler handler = new Handler() {
		public void handleMessage(android.os.Message msg) {
			numTextView.setVisibility(View.VISIBLE);
			numTextView.setText(msgnum + "");
		};
	};
	private MyChatListener3 chatListener3 = new MyChatListener3();

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main);
		SmackUtil.connection.getChatManager().addChatListener(chatListener3);
		init();
	}

	/**
	 * 初始化view
	 */
	private void init() {
		activityName = this.getClass().getName();
		nManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
		numTextView = (TextView) findViewById(R.id.msgnum);
		radioGroup = (RadioGroup) findViewById(R.id.main_group);
		frameLayout = (FrameLayout) findViewById(R.id.main_framelayout);
		radioMessage = (RadioButton) findViewById(R.id.radio_message);
		radioContacts = (RadioButton) findViewById(R.id.radio_contacts);
		radioDiscover = (RadioButton) findViewById(R.id.radio_discover);
		transaction = getFragmentManager().beginTransaction();
		if (MyUtil.fragmentCache.get("contacts") == null) {
			contactsFragment = new ContactsFragment();
			MyUtil.fragmentCache.put("contacts", contactsFragment);
		} else {
			contactsFragment = (ContactsFragment) MyUtil.fragmentCache
					.get("contacts");
		}
		transaction.replace(R.id.main_framelayout, contactsFragment);
		transaction.commit();
		radioGroup.setOnCheckedChangeListener(new RadioCheckListener());
		chatDAO = new ChatDAO(MainActivity.this);
		messageDAO = new MessageDAO(MainActivity.this);

	}

	/**
	 * 底部按钮点击事件
	 * 
	 * @author Administrator
	 * 
	 */
	public class RadioCheckListener implements OnCheckedChangeListener {

		@Override
		public void onCheckedChanged(RadioGroup group, int checkedId) {
			// TODO Auto-generated method stub
			switch (checkedId) {
			case R.id.radio_message:
				nManager.cancel(notifiId);
				msgnum = 0;
				numTextView.setVisibility(View.GONE);
				transaction = getFragmentManager().beginTransaction();
				if (MyUtil.fragmentCache.get("message") == null) {
					messageFragment = new MessageFragment();
					MyUtil.fragmentCache.put("message", messageFragment);
				} else {
					messageFragment = (MessageFragment) MyUtil.fragmentCache
							.get("message");
				}
				transaction.replace(R.id.main_framelayout, messageFragment);
				transaction.commit();
				break;
			case R.id.radio_contacts:
				transaction = getFragmentManager().beginTransaction();
				if (MyUtil.fragmentCache.get("contacts") == null) {
					contactsFragment = new ContactsFragment();
					MyUtil.fragmentCache.put("contacts", contactsFragment);
				} else {
					contactsFragment = (ContactsFragment) MyUtil.fragmentCache
							.get("contacts");
				}
				transaction.replace(R.id.main_framelayout, contactsFragment);
				transaction.commit();
				break;
			case R.id.radio_discover:
				transaction = getFragmentManager().beginTransaction();
				if (MyUtil.fragmentCache.get("discover") == null) {
					discoverFragment = new DiscoverFragment();
					MyUtil.fragmentCache.put("discover", discoverFragment);
				} else {
					discoverFragment = (DiscoverFragment) MyUtil.fragmentCache
							.get("discover");
				}
				transaction.replace(R.id.main_framelayout, discoverFragment);
				transaction.commit();
				break;
			default:
				break;
			}
		}

	}

	/**
	 * 
	 * type1:消息到来时，当前activity为MainActivity,将消息放入数据库并对msgnum++，显示消息数量。
	 * 
	 * type2：消息到来时，当前activity为ChatActivity，不进行任何操作。
	 * type3:当消息到来时，当前activity为系统桌面或其他应用，将消息放入数据库并以通知栏的形式通知用户。
	 */
	private class MyChatListener3 implements ChatManagerListener {

		@Override
		public void chatCreated(Chat chat, boolean arg1) {
			// TODO Auto-generated method stub
			chat.addMessageListener(new MessageListener() {

				@Override
				public void processMessage(Chat arg0,
						org.jivesoftware.smack.packet.Message msg) {
					// type 1
					if (activityName.equals(MyUtil
							.getTopActivity(MainActivity.this))) {
						msgnum++;
						handler.sendEmptyMessage(0);
						String body = msg.getBody();
						String from[] = msg.getFrom().split("@");
						chatDAO.insert(new DBInfo(from[0], SmackUtil.username,
								body, System.currentTimeMillis()));
						messageDAO.insert(new DBInfo(from[0],
								SmackUtil.username, body, System
										.currentTimeMillis()));
					}
					// type 1
					else if ("com.myq.atalk.activity.ChatActivity"
							.equals(MyUtil.getTopActivity(MainActivity.this))) {
					}
					// type 3
					else {
						String body = msg.getBody();
						String from[] = msg.getFrom().split("@");
						chatDAO.insert(new DBInfo(from[0], SmackUtil.username,
								body, System.currentTimeMillis()));
						messageDAO.insert(new DBInfo(from[0],
								SmackUtil.username, body, System
										.currentTimeMillis()));
						Intent intent = new Intent(MainActivity.this,
								ChatActivity.class);
						intent.putExtra("username", from[0]);
						intent.putExtra("body", body);
						PendingIntent pendingIntent = PendingIntent
								.getActivity(MainActivity.this, 0, intent,
										PendingIntent.FLAG_UPDATE_CURRENT);
						Notification.Builder builder = new Notification.Builder(
								MainActivity.this);
						builder.setAutoCancel(true).setTicker("ATalk")
								.setSmallIcon(R.drawable.ic_launcher)
								.setContentTitle(from[0]).setContentText(body)
								.setWhen(System.currentTimeMillis())
								.setContentIntent(pendingIntent);
						Notification notification = builder.getNotification();
						nManager.notify(0x23, notification);
					}

				}
			});
		}

	}

	
	// 开启接收消息监听
	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		Log.e("MainActivity", "onResume()");

		super.onResume();
	}

	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		SmackUtil.connection.getChatManager().removeChatListener(chatListener3);
		SmackUtil.disConnect();
		super.onDestroy();
	}
}
