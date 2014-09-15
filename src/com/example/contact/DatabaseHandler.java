package com.example.contact;

import java.util.ArrayList;
import java.util.List;
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
	private static final String DATABASE_NAME = "contactsManager";

	// Contacts table name
	private static final String TABLE_CONTACTS = "contacts";

	// Contacts Table Columns names
	private static final String KEY_ID = "id";
	private static final String KEY_NAME = "name";
	private static final String KEY_PH_NO = "phone_number";

	public DatabaseHandler(Context context) {
		super(context, DATABASE_NAME, null, DATABASE_VERSION);
	}
	@Override
	public void onCreate(SQLiteDatabase db) {
		String CREATE_CONTACTS_TABLE = "CREATE TABLE " + TABLE_CONTACTS + "("
				+ KEY_ID + " INTEGER PRIMARY KEY," + KEY_NAME + " TEXT,"
				+ KEY_PH_NO + " TEXT" + ")";
		db.execSQL(CREATE_CONTACTS_TABLE);
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int arg1, int arg2) {
		db.execSQL("DROP TABLE IF EXISTS " + TABLE_CONTACTS);
		// Create tables again
		onCreate(db);
	}
	// Adding new contact
	public void addContact(Contact contact) {
		SQLiteDatabase db=this.getWritableDatabase();

		ContentValues values=new ContentValues();
		values.put(KEY_NAME, contact._name);
		values.put(KEY_PH_NO, contact._phone_number);

		db.insert(TABLE_CONTACTS, null, values);	
		db.close();
	}

	// Getting single contact
	public Contact getContact(int id) {
		SQLiteDatabase db=this.getReadableDatabase();
		Cursor cursor=db.query(TABLE_CONTACTS, new String[]{KEY_ID,KEY_NAME,KEY_PH_NO}, KEY_ID+"=?", new String[]{String.valueOf(id)}, null, null, null);

		if(cursor!=null)
			cursor.moveToFirst();

		Contact contact=new Contact(Integer.parseInt(cursor.getString(0)), cursor.getString(1),cursor.getString(2));

		return contact;
	}
	// Getting All Contacts
	public List<Contact> getAllContacts() {
		SQLiteDatabase db=this.getReadableDatabase();
		List<Contact> contactList=new ArrayList<Contact>();
		String selectQuery="Select * from "+ TABLE_CONTACTS;
		Cursor cursor=db.rawQuery(selectQuery, null);

		if(cursor.moveToFirst()){
			do{

				Contact contact=new Contact();
				contact.setID(Integer.parseInt(cursor.getString(0)));
				contact.setName(cursor.getString(1));
				contact.setPhoneNumber(cursor.getString(2));
				contactList.add(contact);

			}while(cursor.moveToNext());
		}

		return contactList;
	}

	// Getting contacts Count
	public int getContactsCount() {
		String query="SELECT * FROM" + TABLE_CONTACTS;
		SQLiteDatabase db=this.getReadableDatabase();
		Cursor cursor=db.rawQuery(query, null);
		cursor.close();
		return cursor.getCount();
	}
	// Updating single contact
	public int updateContact(Contact contact) {
		SQLiteDatabase db=this.getWritableDatabase();
		ContentValues values=new ContentValues();
		values.put(KEY_NAME, contact.getName());
		values.put(KEY_PH_NO, contact.getPhoneNumber());

		return db.update(TABLE_CONTACTS,values, KEY_ID +"=?",new String[]{ String.valueOf(contact.getID())});
	}
	// Deleting single contact
	public void deleteContact(Contact contact) {
		SQLiteDatabase db = this.getWritableDatabase();
		db.delete(TABLE_CONTACTS, KEY_ID + " = ?",new String[] { String.valueOf(contact.getID()) });
		db.close();
	}
	public Contact getContact(String keyword) {
		SQLiteDatabase db=this.getReadableDatabase();
		Cursor cursor=db.query(TABLE_CONTACTS, new String[]{KEY_ID,KEY_NAME,KEY_PH_NO}, KEY_NAME+"=?", new String[]{keyword}, null, null, null);

		if(cursor!=null)
			cursor.moveToFirst();

		Contact contact=new Contact(Integer.parseInt(cursor.getString(0)), cursor.getString(1),cursor.getString(2));

		return contact;
	}

	// Getting All Contacts by name
	public List<Contact> getAllContactsByName(String keyword) {
		SQLiteDatabase db=this.getReadableDatabase();
		List<Contact> contactList=new ArrayList<Contact>();
		//String selectQuery="Select * from "+ TABLE_CONTACTS + " Where "+ KEY_NAME+" = '" + keyword +"'";
		String selectQuery="Select * from "+ TABLE_CONTACTS + " Where "+ KEY_NAME+" LIKE '" + keyword +"%'";
		Cursor cursor=db.rawQuery(selectQuery, null);

		if(cursor.moveToFirst()){
			do{

				Contact contact=new Contact();
				contact.setID(Integer.parseInt(cursor.getString(0)));
				contact.setName(cursor.getString(1));
				contact.setPhoneNumber(cursor.getString(2));
				contactList.add(contact);

			}while(cursor.moveToNext());
		}

		return contactList;
	}
}
