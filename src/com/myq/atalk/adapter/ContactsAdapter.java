package com.myq.atalk.adapter;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.myq.atalk.R;
import com.myq.atalk.bean.CircleImageView;
import com.myq.atalk.util.MyUtil;
import com.myq.atalk.util.SmackUtil;

/**
 * 
 * @author quan ����ListView������
 */
public class ContactsAdapter extends BaseExpandableListAdapter {
	private Context context;
	private String[] fatherName;
	private String[][] childName;
	private Bitmap[][] childIcon;
	private boolean[][] childStatus;

	public ContactsAdapter(Context context, String[] fatherName,
			String[][] childName, Bitmap[][] childIcon, boolean[][] childStatus) {
		this.context = context;
		this.fatherName = fatherName;
		this.childIcon = childIcon;
		this.childName = childName;
		this.childStatus = childStatus;
	}

	@Override
	public Object getChild(int groupPosition, int childPosition) {
		// TODO Auto-generated method stub
		return childName[groupPosition][childPosition];
	}

	@Override
	public long getChildId(int groupPosition, int childPosition) {
		// TODO Auto-generated method stub
		return childPosition;
	}

	@Override
	public View getChildView(int groupPosition, int childPosition,
			boolean isLastChild, View convertView, ViewGroup parent) {
		// TODO Auto-generated method stub
		View view = LayoutInflater.from(context).inflate(
				R.layout.contacts_child_item, null);
		CircleImageView imageView = (CircleImageView) view
				.findViewById(R.id.item_icon);
		TextView name = (TextView) view.findViewById(R.id.item_name);
		TextView status = (TextView) view.findViewById(R.id.item_status);
		name.setText(childName[groupPosition][childPosition]);
		if (childIcon[groupPosition][childPosition] != null) {
			if (childStatus[groupPosition][childPosition]) {
				status.setText("[����]");
				imageView
						.setImageBitmap(childIcon[groupPosition][childPosition]);
			} else {
				status.setText("[����]");
				imageView.setImageBitmap(MyUtil
						.toGrayscale(childIcon[groupPosition][childPosition]));
			}
		}

		return view;
	}

	@Override
	public int getChildrenCount(int groupPosition) {
		// TODO Auto-generated method stub
		return childName[groupPosition].length;
	}

	@Override
	public Object getGroup(int groupPosition) {
		// TODO Auto-generated method stub
		return fatherName[groupPosition];
	}

	@Override
	public int getGroupCount() {
		// TODO Auto-generated method stub
		return fatherName.length;
	}

	@Override
	public long getGroupId(int groupPosition) {
		// TODO Auto-generated method stub
		return groupPosition;
	}

	@Override
	public View getGroupView(int groupPosition, boolean isExpanded,
			View convertView, ViewGroup parent) {
		// TODO Auto-generated method stub
		View view = LayoutInflater.from(context).inflate(
				R.layout.contacts_father_item, null);
		TextView groupName = (TextView) view
				.findViewById(R.id.father_item_name);
		groupName.setText(fatherName[groupPosition]);
		TextView groupMsg = (TextView) view.findViewById(R.id.father_item_num);
		groupMsg.setText(SmackUtil.getGroupStatus(fatherName[groupPosition]));
		return view;
	}

	@Override
	public boolean hasStableIds() {
		// TODO Auto-generated method stub
		return true;
	}

	@Override
	public boolean isChildSelectable(int groupPosition, int childPosition) {
		// TODO Auto-generated method stub
		return true;
	}

}
