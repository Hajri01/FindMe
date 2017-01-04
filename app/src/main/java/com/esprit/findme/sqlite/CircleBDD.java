package com.esprit.findme.sqlite;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.esprit.findme.models.Circle;

import java.util.ArrayList;
import java.util.List;

public class CircleBDD {
	
	private static final int VERSION_BDD = 1;
	private static final String NAME_BDD = "circle.db";
	private SQLiteDatabase bdd;
	private DBHelper dbHelper;

	public SQLiteDatabase getBdd() {
		return bdd;
	}


	public CircleBDD(Context context) {
		super();
		dbHelper = new DBHelper(context, NAME_BDD, null, VERSION_BDD);
	}
	
	public void open(){
		bdd = dbHelper.getWritableDatabase();
	}
	
	public void close(){
		bdd.close();
	}
	
	public SQLiteDatabase getBDD(){
		return bdd;
	}
	
	public long insert(Circle circle){
		ContentValues values = new ContentValues();
		values.put(dbHelper.ID_CIRCLE,circle.getId());
		values.put(dbHelper.TITLE_CIRCLE,circle.getTitle());
		values.put(dbHelper.CODE_CIRCLE,circle.getCode());
		values.put(dbHelper.DESC_CIRCLE,circle.getDescription());
		values.put(dbHelper.CREATOR_CIRCLE,circle.getCreator());
		
		return bdd.insert(dbHelper.TABLE_CIRCLE,null,values);
	}
	
	public int removeAllCircles(){
		return bdd.delete(dbHelper.TABLE_CIRCLE,null,null);
	}
	public int removeCircle(int index){
		return bdd.delete(dbHelper.TABLE_CIRCLE,"`"+dbHelper.ID_CIRCLE+"`= ?", new String[] {String.valueOf(index)});
	}
	
	public ArrayList<Circle>  selectAll() {
		ArrayList<Circle>  list = new ArrayList<Circle>();
		Cursor cusor=bdd.query(dbHelper.TABLE_CIRCLE, new String[]{"*"},null,null,null,null,null);
		if (cusor.moveToFirst())
		{
			do {
				Circle circle=new Circle();
				circle.setId(cusor.getInt(0));
				circle.setTitle(cusor.getString(1));
				circle.setCode(cusor.getString(2));
				circle.setDescription(cusor.getString(3));
				circle.setCreator(cusor.getInt(4));
				list.add(circle);
			}while(cusor.moveToNext());
		}
		if (!cusor.isClosed() && cusor!=null)
		{
			cusor.close();
		}
	      return list;
	   }
}
