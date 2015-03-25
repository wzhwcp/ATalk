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

public class ChatDAO {
	private Context context;
	private DBHelper helper;
	private SQLiteDatabase db;
	private final static String TABLENAME = "chat";

	public ChatDAO(Context context) {
		this.context = context;
		helper = new DBHelper(context);
		db = helper.getWritableDatabase();
	}

	/**
	 * 查询并返还发送方是name或接收方是name的聊天记录
	 * @param name
	 * @return
	 */
	public List<DBInfo> queryRecord(String name) {
		List<DBInfo> list = new ArrayList<DBInfo>();
		Cursor cursor = db.query(TABLENAME, new String[] { "fromwho", "towho",
				"content", "time" }, "fromwho='" + name + "'and towho='"
				+ SmackUtil.username + "' or fromwho='" + SmackUtil.username
				+ "' and towho='" + name + "'", null, null, null, "time");
		if (cursor != null) {
			while (cursor.moveToNext()) {
				DBInfo info = new DBInfo();
				info.setFromwho(cursor.getString(0));
				info.setTowho(cursor.getString(1));
				info.setContent(cursor.getString(2));
				info.setTime(cursor.getLong(3));
				list.add(info);
			}
		} else {
			return null;
		}
		cursor.close();
		return list;
	}

	/**
	 * 查询数据库中所有聊天记录
	 * @return 所有聊天记录
	 */
	public List<DBInfo> queryAll() {
		List<DBInfo> list = new ArrayList<DBInfo>();
		Cursor cursor = db.query(TABLENAME, new String[] { "fromwho", "towho",
				"content", "time" }, null, null, null, null, null);
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
	 * 添加一条聊天记录
	 * @param info
	 */
	public void insert(DBInfo info) {
		ContentValues values = new ContentValues();
		values.put("fromwho", info.getFromwho());
		values.put("towho", info.getTowho());
		values.put("content", info.getContent());
		values.put("time", info.getTime());
		db.insert(TABLENAME, null, values);
	}
}
