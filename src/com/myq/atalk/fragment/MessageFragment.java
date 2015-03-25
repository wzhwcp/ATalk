package com.myq.atalk.fragment;

import java.util.List;

import android.app.Activity;
import android.app.Fragment;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;

import com.myq.atalk.R;
import com.myq.atalk.DAO.MessageDAO;
import com.myq.atalk.activity.ChatActivity;
import com.myq.atalk.adapter.MessageAdapter;
import com.myq.atalk.bean.DBInfo;

/**
 * 消息fragment
 * @author quan
 *
 */
public class MessageFragment extends Fragment{

	private MessageDAO messageDAO;
	private ListView message_iv;
	private List<DBInfo> list;
	private MessageAdapter adapter;
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		View view = inflater.inflate(R.layout.message, container, false);
		message_iv=(ListView)view.findViewById(R.id.message_listview);
		messageDAO=new MessageDAO(getActivity());
//		list=messageDAO.queryAll();
//		MessageAdapter adapter=new MessageAdapter(getActivity(),list);
//		message_iv.setAdapter(adapter);
//		message_iv.setOnItemClickListener(new MyListOnclickListener());
		return view;
	}
	
	public  class MyListOnclickListener implements OnItemClickListener{

		@Override
		public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
				long arg3) {
			// TODO Auto-generated method stub
			DBInfo info=list.get(arg2);
			Intent intent=new Intent(getActivity(),ChatActivity.class);
			intent.putExtra("username", info.getFromwho());
			startActivity(intent);
		}
		
	}
	
	@Override
	public void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		RefreshList();
	}
	public void RefreshList(){
		list=messageDAO.queryAll();
		adapter=new MessageAdapter(getActivity(),list);
		message_iv.setAdapter(adapter);
		message_iv.setOnItemClickListener(new MyListOnclickListener());
	}
}
