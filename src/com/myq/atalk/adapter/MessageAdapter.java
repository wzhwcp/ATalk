package com.myq.atalk.adapter;

import java.text.SimpleDateFormat;
import java.util.List;

import android.content.Context;
import android.graphics.Bitmap;
import android.text.format.Time;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.myq.atalk.R;
import com.myq.atalk.bean.CircleImageView;
import com.myq.atalk.bean.DBInfo;
import com.myq.atalk.util.AvatarLoader;
import com.myq.atalk.util.MyUtil;
import com.myq.atalk.util.SmackUtil;

public class MessageAdapter extends BaseAdapter {

	private Context context;
	private List<DBInfo> list;
	private Bitmap item_avatar;

	public MessageAdapter() {

	}

	public MessageAdapter(Context context, List<DBInfo> list) {
		this.context = context;
		this.list = list;
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
			convertView = LayoutInflater.from(context).inflate(
					R.layout.message_lv_item, null);
			holder = new ViewHolder();
			holder.imageView = (CircleImageView) convertView
					.findViewById(R.id.message_item_avatar);
			holder.title = (TextView) convertView
					.findViewById(R.id.message_item_title);
			holder.content = (TextView) convertView
					.findViewById(R.id.message_item_content);
			holder.time = (TextView) convertView
					.findViewById(R.id.message_item_time);
			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}
		DBInfo info = list.get(position);
		String name = info.getFromwho();
		holder.title.setText(info.getFromwho());
		holder.content.setText(info.getContent());
		holder.time.setText("" + getDate(info.getTime()));
		holder.imageView.setTag(name);
		if (SmackUtil.getFriendsStatus(name)) {
			if (MyUtil.avatarCache.get(name) != null) { // 直接在内存缓存中取出
				holder.imageView.setImageBitmap(MyUtil.avatarCache.get(name));
			} else {
				AvatarLoader loader = new AvatarLoader(context,
						holder.imageView, true);
				loader.execute(name);
			}
		} else {
			if (MyUtil.avatarCache.get(name) != null) { // 直接在内存缓存中取出
				holder.imageView.setImageBitmap(MyUtil
						.toGrayscale(MyUtil.avatarCache.get(name)));
			} else {
				AvatarLoader loader = new AvatarLoader(context,
						holder.imageView, false);
				loader.execute(name);
			}
		}
		return convertView;
	}

	public class ViewHolder {
		CircleImageView imageView;
		TextView title;
		TextView content;
		TextView time;
	}

	/**
	 * 转化为时间常见格式
	 * 
	 * @param time
	 * @return
	 */
	public String getDate(long time) {
		String date = "";
		String format = "MM-dd HH:mm";
		SimpleDateFormat dateFormat = new SimpleDateFormat(format);
		date = dateFormat.format(time);
		return date;
	}

}
