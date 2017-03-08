package com.minlu.baselibrary.sqlite;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class MySQLiteOpenHelper extends SQLiteOpenHelper {

	public MySQLiteOpenHelper(Context context) {
		super(context, "save_password", null, 1);
	}

	@Override
	public void onCreate(SQLiteDatabase db) {
		String sql= "create table t_user (uid integer primary key,c_password varchar(20),c_pw integer)";
		db.execSQL(sql);
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
	}

}
