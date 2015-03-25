package com.myq.atalk.activity;

import java.util.ArrayList;
import java.util.List;

import org.jivesoftware.smack.Chat;
import org.jivesoftware.smack.ChatManagerListener;
import org.jivesoftware.smack.MessageListener;
import org.jivesoftware.smack.XMPPException;
import org.jivesoftware.smack.packet.Message;

import android.R.anim;
import android.app.Activity;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Handler;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.myq.atalk.R;
import com.myq.atalk.DAO.ChatDAO;
import com.myq.atalk.DAO.MessageDAO;
import com.myq.atalk.adapter.ChatAdapter;
import com.myq.atalk.bean.DBInfo;
import com.myq.atalk.util.SmackUtil;

public class ChatActivity extends Activity {
	private ListView chat_listview;
	private TextView chat_title;
	private ImageView chat_back;
	private ImageView chat_member;
	private ImageView chat_sound;
	private EditText chat_editText;
	private ImageView chat_face;
	private ImageView chat_other;
	private Button chat_send;
	private LinearLayout chat_textLayout;
	private Chat chat;
	private ChatDAO chatDAO;
	private NotificationManager nManager; // 通知栏manager
	private MessageDAO messageDAO;
	/** 好友的用户名 */
	private String friendName;
	/** 传过来的消息*/
	private String body="";
	private ChatAdapter adapter;
	private MyChatListener listener = new MyChatListener();
	private List<DBInfo> list = new ArrayList<DBInfo>();
	/**每次接收到消息就在Handler中更改UI界面*/
	private Handler handler = new Handler() {
		public void handleMessage(android.os.Message msg) {
			Bundle bundle = msg.getData();
			String body = bundle.getString("body");
			addMsg(body, true);
		};
	};

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.chat);
		init();
		Intent intent = getIntent();
		friendName = intent.getStringExtra("username");
		body = intent.getStringExtra("body");
		list = chatDAO.queryRecord(friendName);	//从数据库中获取历史消息
		adapter = new ChatAdapter(ChatActivity.this, friendName, list);
		chat_listview.setAdapter(adapter);
		chat_title.setText(friendName);
		chat = SmackUtil.getFriendChat(friendName);
	}

	/**
	 * 初始化view
	 */
	public void init() {
		chatDAO = new ChatDAO(ChatActivity.this);
		messageDAO = new MessageDAO(ChatActivity.this);
		nManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
		chat_listview = (ListView) findViewById(R.id.chat_listview);
		chat_title = (TextView) findViewById(R.id.chat_title);
		chat_back = (ImageView) findViewById(R.id.chat_back);
		chat_member = (ImageView) findViewById(R.id.chat_member);
		chat_sound = (ImageView) findViewById(R.id.chat_sound);
		chat_editText = (EditText) findViewById(R.id.chat_editText);
		chat_face = (ImageView) findViewById(R.id.chat_face);
		chat_other = (ImageView) findViewById(R.id.chat_other);
		chat_send = (Button) findViewById(R.id.chat_send);
		chat_textLayout = (LinearLayout) findViewById(R.id.chat_textLayout);
		chat_editText.addTextChangedListener(new MyTextWatcher());
		chat_send.setOnClickListener(new MyViewOnClickListener());
		
	}

	class MyViewOnClickListener implements OnClickListener {

		@Override
		public void onClick(View v) {
			// TODO Auto-generated method stub
			switch (v.getId()) {
			case R.id.chat_send:		//发送消息的同时将消息保存在数据库中
				try {
					String msg = chat_editText.getText().toString();
					chat.sendMessage(msg);
					addMsg(msg, false);
					chat_editText.setText("");
				} catch (XMPPException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}

				break;
			case R.id.chat_back:
				finish();
				break;
			default:
				break;
			}
		}

	}

	/**
	 * EditText输入监听器
	 * 有输入时'发送'按钮显示，无输入正常显示
	 * @author Air
	 * 
	 */
	private class MyTextWatcher implements TextWatcher {

		@Override
		public void afterTextChanged(Editable arg0) {
			// TODO Auto-generated method stub
			if (chat_editText.getText().toString().isEmpty()) {
				chat_face.setVisibility(View.VISIBLE);
				chat_other.setVisibility(View.VISIBLE);
				chat_send.setVisibility(View.GONE);
			} else {
				chat_face.setVisibility(View.GONE);
				chat_other.setVisibility(View.GONE);
				chat_send.setVisibility(View.VISIBLE);
			}
		}

		@Override
		public void beforeTextChanged(CharSequence s, int start, int count,
				int after) {
			// TODO Auto-generated method stub

		}

		@Override
		public void onTextChanged(CharSequence s, int start, int before,
				int count) {
			// TODO Auto-generated method stub

		}

	}

	/**
	 * 刷新list并数据库把消息保存在数据库中
	 * 
	 * @param msg
	 * @param isOther
	 */
	private void addMsg(String msg, boolean isOther) {
		if (isOther) {
			list.add(new DBInfo(friendName, SmackUtil.username, msg, System
					.currentTimeMillis()));
			adapter.notifyDataSetChanged();
			chatDAO.insert(new DBInfo(friendName, SmackUtil.username, msg,
					System.currentTimeMillis()));
		} else {
			list.add(new DBInfo(SmackUtil.username, friendName, msg, System
					.currentTimeMillis()));
			adapter.notifyDataSetChanged();
			chatDAO.insert(new DBInfo(SmackUtil.username, friendName, msg,
					System.currentTimeMillis()));
		}
		messageDAO.insert(new DBInfo(friendName, SmackUtil.username, msg,
				System.currentTimeMillis()));
	}

	/** 接收消息的监听器*/
	private class MyChatListener implements ChatManagerListener {

		@Override
		public void chatCreated(Chat chat, boolean arg1) {
			// TODO Auto-generated method stub

			// TODO Auto-generated method stub
			chat.addMessageListener(new MessageListener() {

				@Override
				public void processMessage(Chat arg0, Message message) {
					// TODO Auto-generated method stub
					String content = message.getBody();
					String from[] = message.getFrom().split("@");
					if(from[0].equals(friendName)){
						android.os.Message msg = handler.obtainMessage();
						Bundle data = new Bundle();
						data.putString("body", content);
						msg.setData(data);
						handler.sendMessage(msg);
					}else{
						chatDAO.insert(new DBInfo(from[0], SmackUtil.username,
								content, System.currentTimeMillis()));
						messageDAO.insert(new DBInfo(from[0], SmackUtil.username,
								content, System.currentTimeMillis()));
						Intent intent = new Intent(ChatActivity.this,
								ChatActivity.class);
						intent.putExtra("username", from[0]);
						intent.putExtra("body", content);
						PendingIntent pendingIntent = PendingIntent.getActivity(
								ChatActivity.this, 0, intent,
								PendingIntent.FLAG_UPDATE_CURRENT);
						Notification.Builder builder = new Notification.Builder(
								ChatActivity.this);
						builder.setAutoCancel(true).setTicker("ATalk")
								.setSmallIcon(R.drawable.ic_launcher)
								.setContentTitle(from[0]).setContentText(content)
								.setWhen(System.currentTimeMillis())
								.setContentIntent(pendingIntent);
						Notification notification = builder.getNotification();
						nManager.notify(0x23, notification);
					}
					
				}
			});

		}
	}

	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		SmackUtil.connection.getChatManager().addChatListener(listener);//接收消息的监听器
	}
	@Override
	protected void onStop() {
		// TODO Auto-generated method stub
		super.onStop();
		SmackUtil.connection.getChatManager().removeChatListener(listener);
	}
}
