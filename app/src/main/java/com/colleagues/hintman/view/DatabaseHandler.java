package com.colleagues.hintman.view;


import android.content.*;
import android.database.*;
import android.database.sqlite.*;
import android.util.*;
import java.io.*;
import java.sql.*;
import java.util.*;

import java.sql.SQLException;
import com.colleagues.hintman.objects.*;

public class DatabaseHandler extends SQLiteOpenHelper {

	Context context;
	SQLiteDatabase db;
    public static final String TAG = "DatabaseHandler.java";
    private static final int DATABASE_VERSION = 1;
    protected static final String DB_NAME = "hintman";
	private String DB_PATH = "/data/data/com.colleagues.hintman/databases/";
    
    public String tableName = "locations";
    public String fieldObjectId = "id";
    public String fieldObjectName = "name";
	public String filedObjectError = "error";

    public DatabaseHandler(Context context) {
        super(context, DB_NAME, null, DATABASE_VERSION);
		this.context = context;
		copyBase();
    }
	
	public void copyBase(){
		boolean dbexist = checkdatabase();
       /* if (dbexist) {
			  opendatabase(); 
        } else {
             createdatabase();
        }*/
	}

    @Override
    public void onCreate(SQLiteDatabase db) {

        String sql = "";

        sql += "CREATE TABLE " + tableName;
        sql += " ( ";
        sql += fieldObjectId + " INTEGER PRIMARY KEY AUTOINCREMENT, ";
        sql += fieldObjectName + " TEXT, ";
		sql += filedObjectError + " LONG ";
        sql += " ) ";

        db.execSQL(sql);

    }

    // When upgrading the database, it will drop the current table and recreate.
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

        String sql = "DROP TABLE IF EXISTS " + tableName;
        db.execSQL(sql);

        onCreate(db);
    }

    public boolean create(Group myObj) {

        boolean createSuccessful = false;

        if(!checkIfExists(myObj.title)){

            db = this.getWritableDatabase();

            ContentValues values = new ContentValues();
            values.put(fieldObjectName, myObj.title);
			values.put(filedObjectError, myObj.id);
            createSuccessful = db.insert(tableName, null, values) > 0;

            db.close();

            if(createSuccessful){ 
               // Log.e(TAG, myObj.objectName + " created.");
            }
        }

        return createSuccessful;
    }
	
	public void deleteTable(){
		db = this.getWritableDatabase();
        db.delete(tableName, null, null);
		db.close();
	}
	
    public boolean checkIfExists(String objectName){

        boolean recordExists = false;

        db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery("SELECT " + fieldObjectId + " FROM " + tableName + " WHERE " + fieldObjectName + " = '" + objectName + "'", null);

        if(cursor!=null) {

            if(cursor.getCount()>0) {
                recordExists = true;
            }
        }

        cursor.close();
        db.close();

        return recordExists;
    }
    public ArrayList<Group> read(String searchTerm) {

        ArrayList<Group> recordsList = new ArrayList<Group>();
        String sql = "";
        sql += "SELECT * FROM " + tableName;
        sql += " WHERE " + fieldObjectName + " LIKE '%" + searchTerm + "%'";
        sql += " ORDER BY " + fieldObjectId + " DESC";
        sql += " LIMIT 0,5";

        db = this.getWritableDatabase();

        Cursor cursor = db.rawQuery(sql, null);

        if (cursor.moveToFirst()) {
            do {

                // int productId = Integer.parseInt(cursor.getString(cursor.getColumnIndex(fieldProductId)));
                String objectName = cursor.getString(cursor.getColumnIndex(fieldObjectName));
				long objectError = cursor.getLong(cursor.getColumnIndex(filedObjectError));
                Group myObject = new Group(objectName, objectError);

                recordsList.add(myObject);

            } while (cursor.moveToNext());
        }

        cursor.close();
        db.close();
        return recordsList;
    }
	public void createdatabase(){
        boolean dbexist = checkdatabase();
        if(dbexist) {
          
        } else {
            this.getReadableDatabase();
            try {
                copydatabase();
            } catch(IOException e) {
                throw new Error("Error copying database");
            }
        }
    }   

    private boolean checkdatabase() {
        boolean checkdb = false;
        try {
            String myPath = DB_PATH + DB_NAME;
            File dbfile = new File(myPath);
            checkdb = dbfile.exists();
        } catch(SQLiteException e) {
            System.out.println("Database doesn't exist");
        }
        return checkdb;
    }

    private void copydatabase() throws IOException {
        InputStream myinput = context.getAssets().open(DB_NAME);
        String outfilename = DB_PATH + DB_NAME;
        OutputStream myoutput = new FileOutputStream(outfilename);
        byte[] buffer = new byte[1024];
        int length;
        while ((length = myinput.read(buffer))>0) {
            myoutput.write(buffer,0,length);
        }
        myoutput.flush();
        myoutput.close();
        myinput.close();
    }

    public void opendatabase() {
        String mypath = DB_PATH + DB_NAME;
        db = SQLiteDatabase.openDatabase(mypath, null, SQLiteDatabase.OPEN_READWRITE);
    }

    public synchronized void close() {
        if(db != null) {
            db.close();
        }
        super.close();
    }
	
}
