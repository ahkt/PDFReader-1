package com.zhaapps.app.pdfview.data;

import java.util.ArrayList;

import com.zhaapps.app.pdfview.model.Bookmark;
import com.zhaapps.app.pdfview.model.ObjectPdfFile;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DatabaseHandler extends SQLiteOpenHelper {

	// All Static variables
	// Database Version
	private static final int DATABASE_VERSION = 1;

	// Database Name
	private static final String DATABASE_NAME = "pdf_db";

	// Contacts table name
	private static final String TABLE_BOOKMARK = "pdf_table_bookmark";
	// Contacts Table Columns
	private static final String KEY_B_NAME = "name";
	private static final String KEY_B_PATH = "path";	
	private static final String KEY_B_PAGE = "page";
	
	// Contacts table name
	private static final String TABLE_PDF_SCAN = "pdf_table_scan";
	// Contacts Table Columns
	private static final String KEY_S_NAME = "name";
	private static final String KEY_S_PATH = "path";	
	
	public DatabaseHandler(Context context) {
		super(context, DATABASE_NAME, null, DATABASE_VERSION);
	}

	// Creating Tables
	@Override
	public void onCreate(SQLiteDatabase db) {
		createTableBookmark(db);
		createTableScan(db);
	}
	
	private void createTableBookmark(SQLiteDatabase db){
		String CREATE_TABLE = "CREATE TABLE " + TABLE_BOOKMARK + "("
				+ KEY_B_NAME + " TEXT,"	
				+ KEY_B_PATH + " TEXT,"		
				+ KEY_B_PAGE + " INTEGER"
				+ ")";
		db.execSQL(CREATE_TABLE);
	}
	
	private void createTableScan(SQLiteDatabase db){
		String CREATE_TABLE = "CREATE TABLE " + TABLE_PDF_SCAN + "("
				+ KEY_S_NAME + " TEXT,"		
				+ KEY_S_PATH + " TEXT"
				+ ")";
		db.execSQL(CREATE_TABLE);
	}

	// Upgrading database
	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		// Drop older table if existed
		db.execSQL("DROP TABLE IF EXISTS " + TABLE_BOOKMARK);
		db.execSQL("DROP TABLE IF EXISTS " + TABLE_PDF_SCAN);
		// Create tables again
		onCreate(db);
	}
	
	public void truncateTableScan(){
		SQLiteDatabase db = this.getWritableDatabase();
		db.execSQL("DROP TABLE IF EXISTS " + TABLE_PDF_SCAN);
		createTableScan(db);
	}
	
	// Adding new pdf file
	public void addPdfFiles(ArrayList<ObjectPdfFile> obj) {
		SQLiteDatabase db = this.getWritableDatabase();
		for (int i = 0; i < obj.size(); i++) {
			ContentValues values = new ContentValues();
			values.put(KEY_S_NAME, obj.get(i).getName());
			values.put(KEY_S_PATH, obj.get(i).getPath());
			db.insert(TABLE_PDF_SCAN, null, values);
		}
		db.close(); // Closing database connection
	}
	
	// Getting All pdf file
	public ArrayList<ObjectPdfFile> getAllPdfFiles() {
		ArrayList<ObjectPdfFile> pdffiles = new ArrayList<ObjectPdfFile>();
		// Select All Query
		String selectQuery = "SELECT * FROM " + TABLE_PDF_SCAN;

		SQLiteDatabase db = this.getWritableDatabase();
		Cursor cursor = db.rawQuery(selectQuery, null);

		// looping through all rows and adding to list
		if (cursor.moveToFirst()) {
			do {
				ObjectPdfFile p = new ObjectPdfFile();
				p.setName(cursor.getString(0));
				p.setPath(cursor.getString(1));
				pdffiles.add(p);
			} while (cursor.moveToNext());
		}
		db.close();
		// return contact list
		return pdffiles;
	}
	


	// Adding new bookmark
	public void addBookmark(Bookmark book) {

		SQLiteDatabase db = this.getWritableDatabase();
		ContentValues values = new ContentValues();
		values.put(KEY_B_NAME, book.getName());
		values.put(KEY_B_PATH, book.getPath());
		values.put(KEY_B_PAGE, book.getPage());
		if(isBookmarExist(book.getName())){
			// updating row
			db.update(TABLE_BOOKMARK, values, KEY_B_NAME + " = ?", new String[] { book.getName() });
		}else{
			// Inserting Row
			db.insert(TABLE_BOOKMARK, null, values);
		}
		db.close(); // Closing database connection
		
	}

	
	// Getting All Contacts
	public ArrayList<Bookmark> getAllBookmark() {
		ArrayList<Bookmark> bookmarks = new ArrayList<Bookmark>();
		// Select All Query
		String selectQuery = "SELECT * FROM " + TABLE_BOOKMARK;

		SQLiteDatabase db = this.getWritableDatabase();
		Cursor cursor = db.rawQuery(selectQuery, null);

		// looping through all rows and adding to list
		if (cursor.moveToFirst()) {
			do {
				Bookmark b = new Bookmark();
				b.setName(cursor.getString(0));
				b.setPath(cursor.getString(1));
				b.setPage(cursor.getInt(2));
				bookmarks.add(b);
			} while (cursor.moveToNext());
		}
		db.close();
		// return contact list
		return bookmarks;
	}

	// Updating single contact
	public void updateBookmark(Bookmark book) {
		SQLiteDatabase db = this.getWritableDatabase();

		ContentValues values = new ContentValues();
		values.put(KEY_B_NAME, book.getName());
		values.put(KEY_B_PATH, book.getPath());
		values.put(KEY_B_PAGE, book.getPage());
		// updating row
		db.update(TABLE_BOOKMARK, values, KEY_B_NAME + " = ?", new String[] { book.getName() });
		db.close();
	}

	// Deleting single contact
	public void deleteBookmark(Bookmark book) {
		SQLiteDatabase db = this.getWritableDatabase();
		db.delete(TABLE_BOOKMARK, KEY_B_NAME + " = ?", new String[] { book.getName() });
		db.close();
	}


	// Getting contacts Count
	public int getBookmarkCount() {
		String countQuery = "SELECT  * FROM " + TABLE_BOOKMARK;
		SQLiteDatabase db = this.getReadableDatabase();
		Cursor cursor = db.rawQuery(countQuery, null);
		int count=cursor.getCount();
		cursor.close();
		// return count
		return count;
	}
	
	// Getting contacts Count
	public boolean isBookmarExist(String name) {
		String countQuery = "SELECT  * FROM " + TABLE_BOOKMARK +" WHERE "+KEY_B_NAME+"='"+name+"'";
		SQLiteDatabase db = this.getReadableDatabase();
		Cursor cursor = db.rawQuery(countQuery, null);
		int count = cursor.getCount();
		cursor.close();
		return count > 0;
	}

}
