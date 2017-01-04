package com.esprit.findme.sqlite;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteDatabase.CursorFactory;
import android.database.sqlite.SQLiteOpenHelper;

public class DBHelper extends SQLiteOpenHelper{
	
	public static final String TABLE_CIRCLE = "CIRCLE";
	
	public static final String ID_CIRCLE = "id";
	public static final String TITLE_CIRCLE = "title";
	public static final String CODE_CIRCLE = "code";
	public static final String DESC_CIRCLE = "desc";
	public static final String CREATOR_CIRCLE = "creator";
	
	
	private static final String CREATE_CIRCLE = "CREATE TABLE " + TABLE_CIRCLE + " ("+
								ID_CIRCLE + " INTEGER, "+
								TITLE_CIRCLE + " TEXT, "+
								CODE_CIRCLE + " TEXT, "+
								DESC_CIRCLE + " TEXT, "+
								CREATOR_CIRCLE + " INTEGER);";

	public DBHelper(Context context, String name, CursorFactory factory, int version) {
		super(context, name, factory, version);
	}

	@Override
	public void onCreate(SQLiteDatabase db) {
		db.execSQL(CREATE_CIRCLE);
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int old, int newVersion) {
		db.execSQL("DROP TABLE IF EXISTS" + TABLE_CIRCLE + ";");
		onCreate(db);
	}

}
