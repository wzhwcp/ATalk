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
import com.myq.atalk.util.AvatarLoader;
import com.myq.atalk.util.MyUtil;
import com.myq.atalk.util.SmackUtil;

/**
 * 联系人适配器
 * @author quan
 */
public class CopyOfContactsAdapter extends BaseExpandableListAdapter {
	private Context context;
	private String[] fatherName;
	private String[][] childName;

	public CopyOfContactsAdapter(Context context, String[] fatherName,
			String[][] childName) {
		this.context = context;
		this.fatherName = fatherName;
		this.childName = childName;
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
		ChildViewHolder childViewHolder;
		if(convertView==null){
			convertView=LayoutInflater.from(context).inflate(R.layout.contacts_child_item, null);
			childViewHolder=new ChildViewHolder();
			childViewHolder.childAvatar=(CircleImageView)convertView.findViewById(R.id.item_icon);
			childViewHolder.childName=(TextView)convertView.findViewById(R.id.item_name);
			childViewHolder.childStatus=(TextView)convertView.findViewById(R.id.item_status);
			convertView.setTag(childViewHolder);
		}else{
			childViewHolder=(ChildViewHolder) convertView.getTag();
		}
		String strName=childName[groupPosition][childPosition];
		childViewHolder.childName.setText(childName[groupPosition][childPosition]);
		childViewHolder.childAvatar.setTag(strName);
		if(SmackUtil.getFriendsStatus(strName)){
			childViewHolder.childStatus.setText("[在线]");
			if(MyUtil.avatarCache.get(strName)!=null){			//直接在内存缓存中取出
				childViewHolder.childAvatar.setImageBitmap(MyUtil.avatarCache.get(strName));
			}else{
				AvatarLoader loader=new AvatarLoader(context, childViewHolder.childAvatar, true);
				loader.execute(childName[groupPosition][childPosition]);
			}
		}else{
			childViewHolder.childStatus.setText("[离线]");
			if(MyUtil.avatarCache.get(strName)!=null){			//直接在内存缓存中取出
				childViewHolder.childAvatar.setImageBitmap(MyUtil.toGrayscale(MyUtil.avatarCache.get(strName)));
			}else{
				AvatarLoader loader=new AvatarLoader(context, childViewHolder.childAvatar, false);
				loader.execute(childName[groupPosition][childPosition]);
			}
		}
		
		return convertView;
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
		GroupViewHolder groupViewHolder;
		if(convertView==null){
			convertView=LayoutInflater.from(context).inflate(R.layout.contacts_father_item, null);
			groupViewHolder=new GroupViewHolder();
			groupViewHolder.groupName=(TextView)convertView.findViewById(R.id.father_item_name);
			groupViewHolder.groupMsg=(TextView)convertView.findViewById(R.id.father_item_num);
			convertView.setTag(groupViewHolder);
		}else{
			groupViewHolder=(GroupViewHolder) convertView.getTag();
		}
		groupViewHolder.groupName.setText(fatherName[groupPosition]);
		groupViewHolder.groupMsg.setText(SmackUtil.getGroupStatus(fatherName[groupPosition]));
		return convertView;
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

	private class GroupViewHolder{
		TextView groupName;
		TextView groupMsg;
	}
	
	private class ChildViewHolder{
		CircleImageView childAvatar;
		TextView childName;
		TextView childStatus;
	}
}
