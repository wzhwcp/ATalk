package com.myq.atalk.DAO;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteDatabase.CursorFactory;
import android.database.sqlite.SQLiteOpenHelper;

public class DBHelper extends SQLiteOpenHelper {

	public final static String DBNAME = "chatDB";
	public final static int DBVERSION = 1;
	public final static String CHAT_CREATE = "create table chat(_id integer primary key autoincrement, fromwho text not null,towho text not null,content text not null,time long not null);";
	public final static String MESSAGE_CREATE="create table message(_id integer primary key autoincrement, fromwho text not null,towho text not null,content text not null,time long not null);";
	public DBHelper(Context context) {
		super(context, DBNAME, null, DBVERSION);
		// TODO Auto-generated constructor stub
	}

	public DBHelper(Context context, String name, CursorFactory factory,
			int version) {
		super(context, name, factory, version);
		// TODO Auto-generated constructor stub
	}

	@Override
	public void onCreate(SQLiteDatabase db) {
		// TODO Auto-generated method stub
		db.execSQL(CHAT_CREATE);
		db.execSQL(MESSAGE_CREATE);
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		// TODO Auto-generated method stub
		db.execSQL("drop table if exists chat;");
		db.execSQL("drop table if exists message;");
		onCreate(db);
	}

}
