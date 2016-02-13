package com.example.shreekumar.mybillfizz2;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by Sharath on 18-08-2015.
 */
public class DbHelperCategory extends SQLiteOpenHelper {

    public static final String DATABASE_NAME="trialgoal3";
    public static final int DATABASE_VERSION = 1;
    public static final String TABLE_NAME="triCatgy";
    public static final String KEY_ID="_id";
    public static final String CAT_TYPE = "type";

    public DbHelperCategory(Context context) {super(context, DATABASE_NAME, null, DATABASE_VERSION);}

    @Override
    public void onCreate(SQLiteDatabase db) {
        String CREATE_TABLE="CREATE TABLE "+ TABLE_NAME +" ("+ KEY_ID +" INTEGER PRIMARY KEY, "+  CAT_TYPE +" TEXT)";
        db.execSQL(CREATE_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
        onCreate(db);
    }
}
