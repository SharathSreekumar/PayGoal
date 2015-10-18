package com.example.shreekumar.mybillfizz2;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by Sharath on 18-08-2015.
 */
public class DbHelperGoal extends SQLiteOpenHelper {

    public static final String DATABASE_NAME="trialgoal2";
    public static final int DATABASE_VERSION = 1;
    public static final String TABLE_NAME="trigoal";
    public static final String GOAL_TITLE="gtitle";
    public static final String CURRENCY="currency";
    public static final String AMOUNT="gamount";//main goal amount
    public static final String ALT_PAYMENT="altpayment";//keeps record of the payment
    public static final String ALT_EXPENSE="altexpense";//keeps record of the expenses/withdrawl
    public static final String DAY="gday";
    public static final String MONTH="gmonth";
    public static final String YEAR="gyear";
    public static final String BREAKDOWN_DAY="breakday";
    public static final String BREAKDOWN_WEEK="breakweek";
    public static final String BREAKDOWN_MONTH="breakmonth";
    public static final String KEY_ID="_id";

    public DbHelperGoal(Context context) {super(context, DATABASE_NAME, null, DATABASE_VERSION);}

    @Override
    public void onCreate(SQLiteDatabase db) {
        String CREATE_TABLE="CREATE TABLE "+ TABLE_NAME +" ("+ KEY_ID +" INTEGER PRIMARY KEY, "+ DAY +" INTEGER, "+ MONTH +" INTEGER, "+ YEAR +" INTEGER, "+ GOAL_TITLE +" TEXT, "+ CURRENCY +" TEXT, "+ AMOUNT +" FLOAT, "+ ALT_PAYMENT +" FLOAT, "+ ALT_EXPENSE +" FLOAT, "+ BREAKDOWN_DAY +" TEXT, "+ BREAKDOWN_WEEK +" TEXT, "+ BREAKDOWN_MONTH +" TEXT)";
        db.execSQL(CREATE_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS "+TABLE_NAME);
        onCreate(db);
    }
}
