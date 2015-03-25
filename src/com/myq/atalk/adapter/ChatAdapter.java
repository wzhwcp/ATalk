package com.myq.atalk.adapter;

import java.util.List;

import org.jivesoftware.smack.packet.Message;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.myq.atalk.R;
import com.myq.atalk.bean.DBInfo;
import com.myq.atalk.bean.CircleImageView;
import com.myq.atalk.util.AvatarLoader;
import com.myq.atalk.util.MyUtil;
import com.myq.atalk.util.SmackUtil;

/**
 * 聊天消息的适配器
 * @author quan
 *
 */
public class ChatAdapter extends BaseAdapter {
	private String friendName;
	private Context mContext;
	private List<DBInfo> list;
	private Bitmap myAvatar=null;
	private Bitmap otherAvatar=null;
	public ChatAdapter(Context context,String friendName, List<DBInfo> list) {
		this.friendName=friendName;
		this.mContext = context;
		this.list = list;
		this.myAvatar=SmackUtil.getUserImage(SmackUtil.username);
		this.otherAvatar=SmackUtil.getUserImage(friendName);

	}

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return list.size();
	}

	@Override
	public Object getItem(int position) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public long getItemId(int position) {
		// TODO Auto-generated method stub
		return position;
	}

	
	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		// TODO Auto-generated method stub
		ViewHolder holder;
		if (convertView == null) {
			convertView = LayoutInflater.from(mContext).inflate(
					R.layout.chat_lv_item,parent,false);
			holder = new ViewHolder();
			holder.myIv=(CircleImageView)convertView.findViewById(R.id.chat_item_myIcon);
			holder.otherIv=(CircleImageView)convertView.findViewById(R.id.chat_item_otherIcon);
			holder.myContent=(TextView)convertView.findViewById(R.id.chat_item_myContent);
			holder.otherContent=(TextView)convertView.findViewById(R.id.chat_item_otherContent);
			holder.myName=(TextView)convertView.findViewById(R.id.chat_item_myName);
			holder.otherName=(TextView)convertView.findViewById(R.id.chat_item_otherName);
			convertView.setTag(holder);
		}else{
			holder=(ViewHolder)convertView.getTag();
		}
		DBInfo chatInfo=list.get(position);
		if(chatInfo.getFromwho().equals(friendName)){
			holder.myContent.setVisibility(View.GONE);
			holder.myIv.setVisibility(View.GONE);
			holder.myName.setVisibility(View.GONE);
			holder.otherContent.setVisibility(View.VISIBLE);
			holder.otherIv.setVisibility(View.VISIBLE);
			holder.otherName.setVisibility(View.VISIBLE);
			holder.otherIv.setImageBitmap(otherAvatar);
			holder.otherContent.setText(chatInfo.getContent());
			holder.otherName.setText(chatInfo.getFromwho());
		}else{
			holder.myContent.setVisibility(View.VISIBLE);
			holder.myIv.setVisibility(View.VISIBLE);
			holder.myName.setVisibility(View.VISIBLE);
			holder.otherContent.setVisibility(View.GONE);
			holder.otherIv.setVisibility(View.GONE);
			holder.otherName.setVisibility(View.GONE);
			holder.myIv.setImageBitmap(myAvatar);
			holder.myContent.setText(chatInfo.getContent());
			holder.myName.setText(chatInfo.getFromwho());
		}
		return convertView;
	}

	public class ViewHolder {
		public CircleImageView myIv;
		public CircleImageView otherIv;
		public TextView myContent;
		public TextView otherContent;
		public TextView myName;
		public TextView otherName;
	}
	
	
}
