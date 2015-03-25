package com.myq.atalk.fragment;

import android.app.Fragment;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ExpandableListView;
import android.widget.ExpandableListView.OnChildClickListener;
import android.widget.Toast;

import com.myq.atalk.R;
import com.myq.atalk.activity.ChatActivity;
import com.myq.atalk.adapter.ContactsAdapter;
import com.myq.atalk.adapter.CopyOfContactsAdapter;
import com.myq.atalk.util.SmackUtil;

/**
 * 联系人fragment
 * @author quan
 *
 */
public class ContactsFragment extends Fragment {

	private ExpandableListView listView;
	private String[] fatherName;
	private String[][] childNames;
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		View view = inflater.inflate(R.layout.contacts, container, false);
		listView = (ExpandableListView) view.findViewById(R.id.contacts_list);
		fatherName = SmackUtil.getGroup();
		childNames = new String[fatherName.length][];
		for (int i = 0; i < fatherName.length; i++) {
			childNames[i] = SmackUtil.getFriendsName(fatherName[i]);
		}
		CopyOfContactsAdapter adapter = new CopyOfContactsAdapter(getActivity(),
				fatherName, childNames);
		listView.setAdapter(adapter);
		listView.setOnChildClickListener(new OnChildClickListener() {

			@Override
			public boolean onChildClick(ExpandableListView parent, View v,
					int groupPosition, int childPosition, long id) {
				// TODO Auto-generated method stub
				Toast.makeText(getActivity(),
						childNames[groupPosition][childPosition].toString(),
						Toast.LENGTH_SHORT).show();
				Intent intent=new Intent(getActivity(),ChatActivity.class);
				intent.putExtra("username", childNames[groupPosition][childPosition]);
				startActivity(intent);
				return false;
			}
		});
		return view;
	}
	
	@Override
	public void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
	}
}
