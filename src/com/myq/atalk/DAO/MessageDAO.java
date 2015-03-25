package com.myq.atalk.DAO;

import java.util.ArrayList;
import java.util.List;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.myq.atalk.bean.DBInfo;
import com.myq.atalk.util.SmackUtil;

public class MessageDAO {
	private Context context;
	private DBHelper helper;
	private SQLiteDatabase db;
	private final static String TABLENAME = "message";

	public MessageDAO(Context context) {
		this.context = context;
		helper = new DBHelper(context);
		db = helper.getWritableDatabase();
	}

	/**
	 * 显示数据库中所有数据
	 * 
	 * @return
	 */
	public List<DBInfo> queryAll() {
		List<DBInfo> list = new ArrayList<DBInfo>();
		Cursor cursor = db.query(TABLENAME, new String[] { "fromwho", "towho",
				"content", "time" }, "towho='" + SmackUtil.username + "'",
				null, null, null, "time desc");
		while (cursor.moveToNext()) {
			DBInfo info = new DBInfo();
			info.setFromwho(cursor.getString(0));
			info.setTowho(cursor.getString(1));
			info.setContent(cursor.getString(2));
			info.setTime(cursor.getLong(3));
			list.add(info);
		}
		cursor.close();
		return list;
	}

	/**
	 * 添加一条消息列表
	 * @param info
	 */
	public void insert(DBInfo info) {
		Cursor cursor = db.query(TABLENAME, null,
				"fromwho= '" + info.getFromwho() + "'", null, null, null, null);
		if (cursor.getCount() != 0) {
			update(info);
		} else {
			ContentValues values = new ContentValues();
			values.put("fromwho", info.getFromwho());
			values.put("towho", info.getTowho());
			values.put("content", info.getContent());
			values.put("time", info.getTime());
			db.insert(TABLENAME, null, values);
		}
		cursor.close();
	}

	/**
	 * 更新数据库中某条记录
	 * @param info
	 */
	public void update(DBInfo info) {
		ContentValues values = new ContentValues();
		values.put("fromwho", info.getFromwho());
		values.put("towho", info.getTowho());
		values.put("content", info.getContent());
		values.put("time", info.getTime());
		db.update(TABLENAME, values, "fromwho ='" + info.getFromwho() + "'",
				null);
	}
}
