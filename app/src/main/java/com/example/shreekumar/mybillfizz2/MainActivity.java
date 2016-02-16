package com.example.shreekumar.mybillfizz2;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.CalendarView;
import android.widget.DatePicker;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.RadioButton;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Calendar;

public class MainActivity extends AppCompatActivity {

    private DbHelperGoal tHelper;
    private DbHelperCategory cHelper;
    private SQLiteDatabase tDataBase, cDataBase;
    private AlertDialog.Builder build ;
    TextView tGoals, tSavings, tCategoryNo;
    private RelativeLayout totalG;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        tGoals = (TextView)findViewById(R.id.goalNo2);
        tSavings = (TextView)findViewById(R.id.totalSavings2);
        totalG = (RelativeLayout)findViewById(R.id.viewCashFlowSlot);
        tCategoryNo = (TextView)findViewById(R.id.category2);

        totalG.setOnClickListener(new AdapterView.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(getApplicationContext(), GoalDisActivity.class);
                i.putExtra("update", false);
                startActivity(i);
            }
        });
    }

    @Override
    protected void onResume() {
        displayData();
        super.onResume();
    }

    public void displayData() {
        ArrayList<String> categoryList = new ArrayList<String>();
        ArrayList<Integer> categoryCnt = new ArrayList<Integer>();
        cHelper = new DbHelperCategory(this.getBaseContext());
        cDataBase = cHelper.getWritableDatabase();
        Cursor cCursor = cDataBase.rawQuery("SELECT  * FROM " + DbHelperCategory.TABLE_NAME, null);
        if(cCursor.moveToFirst()){
            do{
                categoryList.add(cCursor.getString(cCursor.getColumnIndex(DbHelperCategory.CAT_TYPE)));
                categoryCnt.add(0);
                //Toast.makeText(getApplicationContext(),cCursor.getString(cCursor.getColumnIndex(DbHelperCategory.CAT_TYPE)),Toast.LENGTH_LONG).show();
            }while(cCursor.moveToNext());
            //Toast.makeText(getApplicationContext(),String.valueOf(categoryList.size()),Toast.LENGTH_LONG).show();
        }
        tHelper = new DbHelperGoal(this.getBaseContext());
        tDataBase = tHelper.getWritableDatabase();

        Cursor mCursor = tDataBase.rawQuery("SELECT * FROM " + DbHelperGoal.TABLE_NAME, null);
        float savings = 0;
        int goalCnt = 0;
        int categoryCntTemp = 0;
        String currencyType = "";
        int i = 0;
        if (mCursor.moveToFirst()) {
            do {
                i = 0;
                goalCnt += 1;
                currencyType = mCursor.getString(mCursor.getColumnIndex(DbHelperGoal.CURRENCY));
                savings += (mCursor.getFloat(mCursor.getColumnIndex(DbHelperGoal.ALT_PAYMENT)) - mCursor.getFloat(mCursor.getColumnIndex(DbHelperGoal.ALT_EXPENSE)));
                do{
                    if(mCursor.getString(mCursor.getColumnIndex(DbHelperGoal.CATEGORY)).equals(categoryList.get(i))){
                        categoryCntTemp = categoryCnt.get(i);
                        ++categoryCntTemp;
                        categoryCnt.set(i, categoryCntTemp);
                        break;
                    }
                    i++;
                }while(i < categoryList.size());
            } while (mCursor.moveToNext());
        }
        mCursor.close();
        tGoals.setText(String.valueOf(goalCnt));
        String catContent = "";
        for(i = 0;i<categoryList.size();i++){
            if(i == 0)
                catContent = categoryList.get(i) + ":" + String.valueOf(categoryCnt.get(i)) + "\n";
            else
                catContent += categoryList.get(i) + ":" + String.valueOf(categoryCnt.get(i)) + "\n";
        }
        tSavings.setText(currencyType + " " + String.format("%.0f",savings));
        tCategoryNo.setText(catContent);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        switch(id){
            case R.id.goal:
                Intent i = new Intent(getApplicationContext(), GoalDisActivity.class);
                i.putExtra("update", false);
                startActivity(i);
                return true;
            case R.id.currency_info:
                return true;
        }
        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}