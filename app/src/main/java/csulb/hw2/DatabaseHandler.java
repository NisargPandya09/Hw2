package csulb.hw2;

/**
 * Created by nisar on 05/03/2017.
 */

import java.util.ArrayList;
import java.util.List;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.os.Bundle;
import android.widget.Toast;

public class DatabaseHandler extends SQLiteOpenHelper {

    // Database Version
    private static final int DATABASE_VERSION = 1;
    // Database Name
    private static final String DATABASE_NAME = "NoteTakingApp";
    // Labels table name
    private static final String TABLE_NOTES = "hw2";
    // Labels Table Columns names
    private static final String KEY_CAPTION = "caption";
    private static final String KEY_PATH = "path";
    public DatabaseHandler(Context context) {

        super(context, DATABASE_NAME, null,
                DATABASE_VERSION);
    }
    // Creating Tables
    @Override
    public void onCreate(SQLiteDatabase db) {
// Category table create query
        //SQLiteDatabase db = this.getWritableDatabase();
        String CREATE_CATEGORIES_TABLE = "CREATE TABLE " +
                TABLE_NOTES + "("
                + KEY_CAPTION + " TEXT PRIMARY KEY," +
                KEY_PATH + " TEXT)";
        db.execSQL(CREATE_CATEGORIES_TABLE);
    }
    // Upgrading database
    @Override
    public void onUpgrade(SQLiteDatabase db, int
            oldVersion, int newVersion) {
// Drop older table if existed
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NOTES);
// Create tables again
        onCreate(db);
    }
    /**
     * Inserting new label into labels table
     * */
    public void insertNote(String caption, String path){
        SQLiteDatabase db = this.getWritableDatabase();
        try {

            ContentValues values = new ContentValues();
            values.put(KEY_CAPTION, caption);
            values.put(KEY_PATH, path);
// Inserting Row
            db.insert(TABLE_NOTES, null, values);
        }
        catch (Exception e) {
            e.printStackTrace();
            goToList();
                   }
        db.close(); // Closing database connection

    }

    public void goToList(){

        Context context = new ListActivity();
        Toast.makeText(context, "Duplicate", Toast.LENGTH_SHORT).show();


    }



    public void DeleteNote(){
        SQLiteDatabase db = this.getWritableDatabase();
        db.execSQL("DELETE FROM " + TABLE_NOTES);
        db.close(); // Closing database connection

    }

    /**
     * Getting all labels
     * returns list of labels
     * */
    public List<String> getAllNotes(){
        List<String> captions = new ArrayList<String>();
        List<String> imagepath = new ArrayList<String>();

        // Select All Query
        String selectQuery = "SELECT * FROM " +
                TABLE_NOTES;
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);
// looping through all rows and adding to list
        if (cursor.moveToFirst()) {
            do {
                captions.add(cursor.getString(0));
                //imagepath.add(cursor.getString(1));
            } while (cursor.moveToNext());
        }
// closing connection
        cursor.close();
        db.close();
// returning labels
        return captions;
    }

    public String getImagePath(String caption){

        String imagepath = null;

        // Select All Query
        String selectQuery = "SELECT * FROM " +
                TABLE_NOTES + " WHERE " + KEY_CAPTION + " = "+"'"+caption+"'" ;
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);
// looping through all rows and adding to list
        if (cursor.moveToFirst()) {
            do {
                imagepath = cursor.getString(1);

                //imagepath.add(cursor.getString(1));
            } while (cursor.moveToNext());
        }
// closing connection
        cursor.close();
        db.close();
// returning labels
        return imagepath;
    }
}
