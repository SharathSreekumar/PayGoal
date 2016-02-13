package com.example.shreekumar.mybillfizz2;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.ContentValues;
import android.content.DialogInterface;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Build;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.SimpleCursorAdapter;
import android.widget.Spinner;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Calendar;

// This activity is used to retrive data from Goal page & store data to database
// this activity is linked to "DbHelperGoal.java"
public class GoalActivity extends AppCompatActivity {

    static final int DATE_DIALOG_ID = 0;
    private int currentYear, currentMonth, currentDay;

    private DbHelperGoal gHelper;
    private DbHelperCategory cHelper;
    private SQLiteDatabase dataBase;
    private boolean isUpdate;
    private String id;
    Button dateG, cal, save1;
    String dayG,monthG,yearG;
    EditText goalG,amountG;
    String currencyG, categoryG;
    CheckBox dayBreakG,weekBreakG,monthBreakG;
    Boolean dateB,weekB,monthB;
    Spinner spinnerCat;
    private AlertDialog.Builder build;
    AlertDialog alert;
    EditText CatgyValue;
    int catgyFlag;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_goal);

        gHelper = new DbHelperGoal(this);
        cHelper = new DbHelperCategory(this);

        //Dropdown currency
        Spinner spinner = (Spinner) findViewById(R.id.spinner1);
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this, R.array.currency, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                switch (position){
                    case 0:
                        currencyG = "$";
                        break;
                    case 1:
                        currencyG = "\u20ac";
                        break;
                    case 2:
                        currencyG = "\u00a3";
                        break;
                    case 3:
                        currencyG = "₹";
                        break;
                    case 4:
                        currencyG = "¥";
                        break;
                    default://Toast.makeText(getApplication(),"No such choice",Toast.LENGTH_SHORT).show();
                }

                Spinner spinnerCat = (Spinner) findViewById(R.id.categoryDrop);

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                //
            }
        });
        //ActionBar actionBar = getSupportActionBar();
        //actionBar.setDisplayHomeAsUpEnabled(true);

        //button for calendar

        spinnerCat = (Spinner) findViewById(R.id.categoryDrop);
        categoryFunc();

        cal=(Button)findViewById(R.id.calendarButton);
        cal.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                showDialog(DATE_DIALOG_ID);
            }
        });

        final Calendar c = Calendar.getInstance();
        currentYear = c.get(Calendar.YEAR);
        currentMonth = c.get(Calendar.MONTH);
        currentDay = c.get(Calendar.DAY_OF_MONTH);
    }

    public void categoryFunc(){
        ArrayList<Integer> catId = new ArrayList<Integer>();
        ArrayList<String> catCont = new ArrayList<String>();
        dataBase = cHelper.getReadableDatabase();
        Cursor gCursor = dataBase.rawQuery("SELECT * FROM "+ DbHelperCategory.TABLE_NAME, null);

        if(gCursor.moveToFirst()){
            do{
                catId.add(gCursor.getInt(gCursor.getColumnIndex(DbHelperCategory.KEY_ID)));
                catCont.add(gCursor.getString(gCursor.getColumnIndex(DbHelperCategory.CAT_TYPE)));
            }while(gCursor.moveToNext());
        }
        gCursor.close();
        dataBase.close();//close database

        ArrayAdapter<String> adapterC = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, catCont);
        //SimpleCursorAdapter adapterC = new SimpleCursorAdapter(this, android.R.layout.simple_spinner_item, gCursor, catCont, catId);
        adapterC.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerCat.setAdapter(adapterC);
        spinnerCat.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                dataBase = cHelper.getWritableDatabase();
                Cursor gCursor = dataBase.rawQuery("SELECT * FROM " + DbHelperCategory.TABLE_NAME + " WHERE " + DbHelperCategory.KEY_ID + " = " + position + 1, null);
                categoryG = null;
                if (gCursor.moveToFirst()) {
                    do {
                        categoryG = gCursor.getString(gCursor.getColumnIndex(DbHelperCategory.CAT_TYPE));
                    } while (gCursor.moveToNext());
                }

                gCursor.close();
                dataBase.close();
            }

            //Spinner spinnerCat = (Spinner) findViewById(R.id.categoryDrop);

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                //
            }
        });
    }

    protected Dialog onCreateDialog(int id) {
        switch(id){
            case DATE_DIALOG_ID:
                return new DatePickerDialog(this, reservationDate, currentYear, currentMonth, currentDay);
        }
        return null;
    }

    private DatePickerDialog.OnDateSetListener reservationDate = new DatePickerDialog.OnDateSetListener() {
        @Override
        public void onDateSet(DatePicker view, int year, int month, int day){
            //Button cal=(Button)findViewById(R.id.calendarButton);
            final Calendar c = Calendar.getInstance();
            int curYear = c.get(Calendar.YEAR), curMonth = c.get(Calendar.MONTH)+1, curDay = c.get(Calendar.DAY_OF_MONTH);
            dateG=(Button)findViewById(R.id.calendarButton);
            //Picks the selected date, month & year & displays on button
            if((year > curYear)||(year == curYear && month+1 > curMonth)||(year == curYear && month+1 == curMonth && day > curDay)) {
                dayG = Integer.toString(day);
                monthG = Integer.toString(month + 1);
                yearG = Integer.toString(year);
                dateG.setText(Integer.toString(day) + "/" + Integer.toString(month + 1) + "/" + Integer.toString(year));
                Toast.makeText(getBaseContext(), "Your rental time is set from " + curDay + "-" + curMonth + "-" + curYear + " to " + day + "-" + (month + 1) + "-" + year + ".", Toast.LENGTH_SHORT).show();
            }else{
                Toast.makeText(getBaseContext(), "Please choose date after " + curDay + "-" + curMonth + "-" + curYear, Toast.LENGTH_SHORT).show();
            }
        }
    };

    //To save the data in db
    private void saveData(){

        //Toast.makeText(getBaseContext(),dateB.toString(),Toast.LENGTH_LONG).show();
        //gHelper=new DbHelperGoal(getBaseContex
        dataBase=gHelper.getWritableDatabase();
        ContentValues values=new ContentValues();

        // Values for database
        values.put(DbHelperGoal.GOAL_TITLE, goalG.getText().toString());
        //values.put(DbHelperGoal.DATE, dateG.getText().toString());
        values.put(DbHelperGoal.DAY, dayG.toString());
        values.put(DbHelperGoal.MONTH, monthG.toString());
        values.put(DbHelperGoal.YEAR, yearG.toString());
        values.put(DbHelperGoal.CURRENCY,currencyG.toString());
        values.put(DbHelperGoal.AMOUNT,amountG.getText().toString());
        values.put(DbHelperGoal.BREAKDOWN_DAY,dateB.toString());
        values.put(DbHelperGoal.BREAKDOWN_WEEK,weekB.toString());
        values.put(DbHelperGoal.BREAKDOWN_MONTH, monthB.toString());
        values.put(DbHelperGoal.ALT_PAYMENT,String.valueOf(0));
        values.put(DbHelperGoal.ALT_EXPENSE,String.valueOf(0));

        System.out.println("");
        if(isUpdate)
        {
            //update database with new data
            dataBase.update(DbHelperGoal.TABLE_NAME, values, DbHelperGoal.KEY_ID+"="+id, null);
        }
        else
        {
            //insert data into database
            dataBase.insert(DbHelperGoal.TABLE_NAME, null, values);
        }
        //close database
        dataBase.close();
        Toast.makeText(getBaseContext(), "Data saved successfully", Toast.LENGTH_LONG).show();
        finish();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_goal, menu);
        return true;
    }

    //For action menu, onClick
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        switch(id){
            case R.id.save_goal://save data
                goalG=(EditText)findViewById(R.id.goalEntry1);
                amountG=(EditText)findViewById(R.id.amountEntry2);
                dateG=(Button)findViewById(R.id.calendarButton);
                dayBreakG=(CheckBox)findViewById(R.id.checkBoxDaily);
                weekBreakG=(CheckBox)findViewById(R.id.checkBoxWeekly);
                monthBreakG=(CheckBox)findViewById(R.id.checkBoxMonthly);

                if (dayBreakG.isChecked()) {
                    dateB = true;
                }else{
                    dateB = false;
                }

                if (weekBreakG.isChecked()) {
                    weekB = true;
                }else{
                    weekB = false;
                }

                if (monthBreakG.isChecked()) {
                    monthB = true;
                }else{
                    monthB = false;
                }

                //Toast.makeText(getBaseContext(),monthB.toString(),Toast.LENGTH_LONG).show();

                // Checks if Text box slots are empty or not, if not, save data
                if(!goalG.getText().toString().isEmpty() && !amountG.getText().toString().isEmpty() && !dateG.getText().toString().isEmpty()) {
                    //Toast.makeText(getBaseContext(),dateB.toString(),Toast.LENGTH_LONG).show();
                    saveData();
                }else{// if slots found blank, pop an alert
                    AlertDialog.Builder alertBuilder=new AlertDialog.Builder(GoalActivity.this);
                    alertBuilder.setTitle("Incomplete Data");
                    alertBuilder.setMessage("Please complete the form.");
                    alertBuilder.setPositiveButton("Ok", new DialogInterface.OnClickListener() {

                        public void onClick(DialogInterface dialog, int which) {
                            dialog.cancel();
                        }
                    });
                    alertBuilder.create().show();
                }

                return true;
                case R.id.categoriesNewG://Category
                    LayoutInflater li = LayoutInflater.from(GoalActivity.this);
                    View promptsCategoryView = li.inflate(R.layout.category_layout, null);
                    build = new AlertDialog.Builder(GoalActivity.this);
                    build.setTitle("New Category");
                    build.setMessage("Please Enter new Category");
                    build.setView(promptsCategoryView);
                    CatgyValue = (EditText) promptsCategoryView.findViewById(R.id.CategoryEnter1);
                    //PayValue.isFocused();
                    CatgyValue.setFocusableInTouchMode(true);
                    CatgyValue.setFocusable(true);
                    CatgyValue.requestFocus();
                    //InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                    //imm.showSoftInput(PayValue, InputMethodManager.SHOW_IMPLICIT);
                    build.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            dataBase = cHelper.getWritableDatabase();
                            Cursor gCursor;
                            if(Build.VERSION.SDK_INT > 15) {
                                gCursor = dataBase.rawQuery("SELECT * FROM " + DbHelperCategory.TABLE_NAME + " WHERE " + DbHelperCategory.CAT_TYPE + "=?", new String[]{CatgyValue.getText().toString()}, null);
                            }else{
                                gCursor = dataBase.rawQuery("SELECT * FROM " + DbHelperCategory.TABLE_NAME, null);
                            }
                            String dbData = null;
                            catgyFlag = 0;
                            if(gCursor.getCount() > 0){
                                Toast.makeText(getApplicationContext(), "Data present", Toast.LENGTH_LONG).show();
                                catgyFlag = 1;
                            }
                            //gCursor.close();
                            //dataBase.close();
                            if (catgyFlag == 1) {
                                Toast.makeText(getApplicationContext(), "Sorry, this option is already present", Toast.LENGTH_LONG).show();
                                gCursor.close();
                                dataBase.close();
                            } else {
                                ContentValues values = new ContentValues();
                                values.put(DbHelperCategory.CAT_TYPE, CatgyValue.getText().toString());
                                dataBase.insert(DbHelperCategory.TABLE_NAME, null, values);
                                dataBase.close();
                                //setContentView(R.layout.activity_goal);
                                categoryFunc();
                                Toast.makeText(getApplication(), CatgyValue.getText().toString(), Toast.LENGTH_SHORT).show();
                                dialog.cancel();
                            }
                        }
                    });

                    build.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            Toast.makeText(getApplication(), "New Category Cancelled", Toast.LENGTH_SHORT).show();
                            dialog.cancel();
                        }
                    });

                    alert = build.create();
                    alert.show();
                    alert.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_VISIBLE);
                    return true;
            case R.id.action_settings:
                return true;
        }
        //noinspection SimplifiableIfStatement
        /** if (id == R.id.action_settings) {
            Toast.makeText(getBaseContext(), "Settings Click working successfully!!!", Toast.LENGTH_SHORT).show();
            return true;
        }
        else if(id == R.id.save_goal)
        {
            //Toast.makeText(getBaseContext(), "Settings Click working successfully!!!", Toast.LENGTH_SHORT).show();
            return true;
        }*/
        return super.onOptionsItemSelected(item);
    }
}