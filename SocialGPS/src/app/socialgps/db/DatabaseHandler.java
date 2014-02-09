package app.socialgps.db;

import java.util.ArrayList;
import java.util.List;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;
import app.socialgps.db.dao.*;

public class DatabaseHandler extends SQLiteOpenHelper {
	SQLiteDatabase db;
	private static final int DATABASE_VERSION = 1;

	private static final String DATABASE_NAME = "loc_db";


	public DatabaseHandler(Context context) {
		super(context, DATABASE_NAME, null, DATABASE_VERSION);
		
	}
	public void onOpen(SQLiteDatabase db) {
        super.onOpen(db);
     }
	// Creating Tables
	@Override
	public void onCreate(SQLiteDatabase db) {
		Log.d("creating: ", "tables..");
		db.execSQL("CREATE TABLE user_pass ( user_id  TEXT PRIMARY KEY, passwd  TEXT)");
		db.execSQL("CREATE TABLE user_detail ( user_id  TEXT PRIMARY KEY, user_name  TEXT, phone INTEGER, email_id TEXT, status TEXT)");
		db.execSQL("CREATE TABLE friend_detail (user_id TEXT, friend_id TEXT, status TEXT)");
		db.execSQL("CREATE TABLE location_detail ( user_id  TEXT PRIMARY KEY, location TEXT, time  TEXT)");
		Log.d("created: ", "tables..");
			
	}

	// Upgrading database
	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		// Drop older table if existed
		db.execSQL("DROP TABLE IF EXISTS user_pass");
		db.execSQL("DROP TABLE IF EXISTS user_detail");
		db.execSQL("DROP TABLE IF EXISTS friend_detail");
		db.execSQL("DROP TABLE IF EXISTS location_detail");
	
		// Create tables again
		onCreate(db);
	}

	/**
	 * All CRUD(Create, Read, Update, Delete) Operations
	 */

	// Adding new contact
	public int insert(user_pass_dao upd) {				// for User_pass table
		try
		{
		db = this.getWritableDatabase();

		ContentValues values = new ContentValues();
		values.put("user_id", upd.get_user_id()); 
		values.put("passwd", upd.get_passwd()); 

		// Inserting Row
		return (int)db.insert("user_pass", null, values);
		}
		catch(Exception e)
		{
			Log.e("[Exception in Sqlite insert_user_pass]", e.toString());
			return -1;
		}
		finally
		{
		//	db.close(); // Closing database connection
		}
	}
	
	public int insert(user_detail_dao upd) {			// for User_detail table
		try
		{
		db = this.getWritableDatabase();

		ContentValues values = new ContentValues();
		values.put("user_id", upd.get_user_id()); 
		values.put("user_name", upd.get_user_name()); 
		values.put("phone", upd.get_phone().intValue());  // alert may b error come
		values.put("email_id", upd.get_email_id()); 
		values.put("status", upd.get_status());
		
		// Inserting Row
		return (int)db.insert("user_detail", null, values);	}
		catch(Exception e)
		{
			Log.e("[Exception in Sqlite insert_user_detail]", e.toString());
			return -1;
		}
		finally
		{
		//	db.close(); // Closing database connection
		}
	}
	
	public int insert(friend_detail_dao upd) {			// for friend_detail table
		try {
		db = this.getWritableDatabase();

		ContentValues values = new ContentValues();
		values.put("user_id", upd.get_user_id()); 
		values.put("friend_id", upd.get_friend_id()); 
		values.put("status", upd.get_status());
		
		// Inserting Row
		return (int)db.insert("friend_detail", null, values);
		}
		catch(Exception e)
		{
			Log.e("[Exception in Sqlite insert_friend_detail]", e.toString());
			return -1;
		}
		finally
		{
		//	db.close(); // Closing database connection
		}}
	
	public int insert(location_detail_dao upd) {			// for User_detail table
		try {
		db = this.getWritableDatabase();

		ContentValues values = new ContentValues();
		values.put("user_id", upd.get_user_id()); 
		values.put("location", upd.get_location()); 
		values.put("time", upd.get_tyme());  
		
		// Inserting Row
		return (int)db.insert("location_detail", null, values);
		}
		catch(Exception e)
		{
			Log.e("[Exception in Sqlite insert_location_detail]", e.toString());
			return -1;
		}
		finally
		{
		//	db.close(); // Closing database connection
		}
	}
	
	
	
	// Getting Details (SELECT)
	public user_pass_dao check_record()
	{
		try
		{
			
		user_pass_dao upd_nu = new user_pass_dao();
	db = this.getReadableDatabase();

	Cursor cursor = db.rawQuery("select * from user_pass" ,null);
	
		if (!cursor.moveToFirst())
			return null;
		else	{
		cursor.moveToFirst();
		upd_nu.set_user_id(cursor.getString(0));
		upd_nu.set_passwd(cursor.getString(1));
		return upd_nu;
		}
		}
		catch(Exception e)
		{
			Log.e("[Exception in Sqlite select_user_pass]", e.toString());
			return null;
		}
		finally
		{
		//	db.close(); // Closing database connection
		}
	}
	
	public user_pass_dao select(user_pass_dao upd) {		//for user_pass table
		try
		{
			
		user_pass_dao upd_nu = new user_pass_dao();
	db = this.getReadableDatabase();
//	this.onOpen(db);

	Cursor cursor = db.query("user_pass", new String[] { "user_id", "passwd"}, "user_id" + "=?",
				new String[] { upd.get_user_id() }, null, null, null, null);
	
		if (!cursor.moveToFirst())
			return null;
		else	{
		cursor.moveToFirst();
		upd_nu.set_user_id(cursor.getString(0));
		upd_nu.set_passwd(cursor.getString(1));
		return upd_nu;
		}
		}
		catch(Exception e)
		{
			Log.e("[Exception in Sqlite select_user_pass]", e.toString());
			return null;
		}
		finally
		{
		//	db.close(); // Closing database connection
		}
	}
	
	public user_detail_dao select(user_detail_dao upd) {		// for user_detail table
		try
		{
		user_detail_dao upd_nu = new user_detail_dao();
		db = this.getReadableDatabase();
		Cursor cursor = db.rawQuery("select * from user_detail where user_id='"+upd.get_user_id()+"'" ,null);
		if (!cursor.moveToFirst())
			return null;
		else	{
		cursor.moveToFirst();
		upd_nu.set_user_id(cursor.getString(0));
		upd_nu.set_user_name(cursor.getString(1));
		upd_nu.set_phone(cursor.getLong(2));
		upd_nu.set_email_id(cursor.getString(3));
		upd_nu.set_status(cursor.getString(4));
		return upd_nu;
			}
		}
		catch(Exception e)
		{
			Log.e("[Exception in Sqlite selectiont_user_detail]", e.toString());
			return null;
		}
		finally
		{
		//	db.close(); // Closing database connection
		}
	}
	
	public List<friend_detail_dao> select(friend_detail_dao upd) {		// for user_detail table
		try
		{
		friend_detail_dao upd_nu = new friend_detail_dao();
		List<friend_detail_dao> contactList = new ArrayList<friend_detail_dao>();
		
		db = this.getReadableDatabase();
		Cursor cursor = db.rawQuery("select * from friend_detail where user_id='"+upd.get_user_id()+ "' AND friend_id='"+upd.get_friend_id()+"'", null);
		if (!cursor.moveToFirst())
					return null; 
	      	
		else	{
			if (cursor.moveToFirst()) {
				System.out.println("Empty "+cursor.getCount());
		   		do {
					upd_nu.set_user_id(cursor.getString(0));
					upd_nu.set_friend_id(cursor.getString(1));
					upd_nu.set_status(cursor.getString(2));	// Adding contact to list
					contactList.add(upd);
				} while (cursor.moveToNext());
			}
			else
				return null;
		
		return contactList;
		}
		}
		catch(Exception e)
		{
			Log.e("[Exception in Sqlite selectiont_friend_detail]", e.toString());
			return null;
		}
		finally
		{
		//	db.close(); // Closing database connection
		}
	}

	public location_detail_dao select(location_detail_dao upd) {		// for location_detail table
		try
		{
		location_detail_dao upd_nu = new location_detail_dao();
		
		db = this.getReadableDatabase();
		Cursor cursor = db.rawQuery("select * from location_detail where user_id='"+upd.get_user_id()+"'", null);
		if (!cursor.moveToFirst())
				return null;
		else	{
		cursor.moveToFirst();
		upd_nu.set_user_id(cursor.getString(0));
		upd_nu.set_location(cursor.getString(1));
		upd_nu.set_tyme(cursor.getString(2));
		return upd_nu;
		}		
		}
		catch(Exception e)
		{
			Log.e("[Exception in Sqlite selectiont_location_detail]", e.toString());
			return null;
		}
		finally
		{
			//db.close(); // Closing database connection
		}
	}
	
	
	// Updating single contact
	
	public int update(user_pass_dao upd) {
		try		{
		SQLiteDatabase db = this.getWritableDatabase();

		ContentValues values = new ContentValues();
		values.put("user_id", upd.get_user_id());
		values.put("passwd", upd.get_passwd());
		return db.update("user_pass", values, " user_id  = ?",
				new String[] {upd.get_user_id() });
		}
		catch(Exception e)		{
			Log.e("[Exception in Sqlite updation_user_pass]", e.toString());
			return 0;
		}
		finally		{
		//	db.close(); // Closing database connection
		}
	}

	public int update(user_detail_dao upd) {
		try		{
		SQLiteDatabase db = this.getWritableDatabase();

		ContentValues values = new ContentValues();
		values.put("user_id", upd.get_user_id());
		values.put("user_name", upd.get_user_name());
		values.put("phone", upd.get_phone());
		values.put("email_id", upd.get_email_id());
		values.put("status", upd.get_status());
		
		return db.update("user_detail", values, " user_id  = ?",
				new String[] {upd.get_user_id() });
		}
		catch(Exception e)		{
			Log.e("[Exception in Sqlite updation_user_detail]", e.toString());
			return 0;
		}
		finally		{
		//	db.close(); // Closing database connection
		}
	}
	
	public int update(friend_detail_dao upd) {
		try		{
		SQLiteDatabase db = this.getWritableDatabase();

		ContentValues values = new ContentValues();
		values.put("user_id", upd.get_user_id());
		values.put("friend_id", upd.get_friend_id());
		values.put("status", upd.get_status());
		return db.update("friend_detail", values, " user_id  = ? AND friend_id = ?",
		new String[] {upd.get_user_id(), upd.get_friend_id() });
		}
		catch(Exception e)		{
			Log.e("[Exception in Sqlite updation_friend_detail]", e.toString());
			return 0;
		}
		finally		{
			//db.close(); // Closing database connection
		}
	}
	
	public int update(location_detail_dao upd) {
		try		{
		SQLiteDatabase db = this.getWritableDatabase();
		ContentValues values = new ContentValues();
		values.put("user_id", upd.get_user_id());
		values.put("location", upd.get_location());
		values.put("time", upd.get_tyme());
		return db.update("friend_detail", values, " user_id  = ?",
		new String[] {upd.get_user_id() });
			}
		catch(Exception e)		{
			Log.e("[Exception in Sqlite updation_location_detail]", e.toString());
			return 0;
		}
		finally		{
			//db.close(); 
		}
	}
	
	// Deleting single contact
public int delete(user_pass_dao upd) {
	try		{			
			SQLiteDatabase db = this.getWritableDatabase();
			return db.delete("user_pass"," user_id = ?", new String[] {upd.get_user_id() });
			}
			catch(Exception e)		{
				Log.e("[Exception in Sqlite deletion_user_pass]", e.toString());
				return 0;
			}
			finally		{
			//	db.close(); 
			}		
	}
	

public int delete(user_detail_dao upd) {
	try		{			
			SQLiteDatabase db = this.getWritableDatabase();
			return db.delete("user_detail", "user_id  = ?", new String[] { upd.get_user_id() });
			}
			catch(Exception e)		{
				Log.e("[Exception in Sqlite deletion_user_detail]", e.toString());
				return 0;
			}
			finally		{
			//	db.close(); 
			}
	}

public int delete(friend_detail_dao upd) {
	try		{			
			SQLiteDatabase db = this.getWritableDatabase();
			return db.delete("friend_detail", "user_id  = ? AND friend_id=?", new String[] { upd.get_user_id(),upd.get_friend_id() });
			}
			catch(Exception e)		{
				Log.e("[Exception in Sqlite deletion_friend_detail]", e.toString());
				return 0;
			}
			finally		{
			//	db.close(); 
			}
	}

public int delete(location_detail_dao upd) {
	try		{			
			SQLiteDatabase db = this.getWritableDatabase();
			return db.delete("location_detail"," user_id  = ?", new String[] { upd.get_user_id() });
			}
			catch(Exception e)		{
				Log.e("[Exception in Sqlite deletion_location_detail]", e.toString());
				return 0;
			}
			finally		{
			//	db.close(); 
			}
	}


public void close_db()
{
	db.close();
}
}


	