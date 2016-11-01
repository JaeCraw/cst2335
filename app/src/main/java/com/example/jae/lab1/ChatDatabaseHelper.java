package com.example.jae.lab1;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

/**
 *
 */
public class ChatDatabaseHelper extends SQLiteOpenHelper{

    public static final String DATABASE_NAME = "Chats.db";
    public static final int VERSION_NUMBER = 1;
    public static final String TABLE_NAME = "ChatTable";
    public static final String KEY_ID = "ID";
    public static final String KEY_MESSAGE = "MESSAGES";


    public ChatDatabaseHelper(Context ctx){
        super(ctx, DATABASE_NAME, null, VERSION_NUMBER);
    }

    public void onCreate(SQLiteDatabase db){
        //create the query
        Log.i("ChatDatabaseHelper", "Calling onCreate");
        String CREATE_TABLE = "CREATE TABLE " +
                TABLE_NAME + " (" + KEY_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                + KEY_MESSAGE + " text)";

        db.execSQL(CREATE_TABLE);
    }

    public void onUpgrade(SQLiteDatabase db, int i, int j){
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
        this.onCreate(db);
    }

    public void onDowngrade(SQLiteDatabase db, int oldver, int newVer){
        Log.i("ChatDatabaseHelper", "Calling onUpgrade, oldVersion=" + oldver + " newVersion" + newVer);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
        this.onCreate(db);
    }
}
