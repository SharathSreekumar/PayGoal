package com.example.shreekumar.mybillfizz2;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.ContentValues;
import android.content.DialogInterface;
import android.database.sqlite.SQLiteDatabase;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import java.util.Calendar;

// This activity is used to retrive data from Goal page & store data to database
// this activity is linked to "DbHelperGoal.java" & "GoalAdapter.java"
public class GoalActivity extends AppCompatActivity {

    static final int DATE_DIALOG_ID = 0;
    private int currentYear, currentMonth, currentDay;

    private DbHelperGoal gHelper;
    private SQLiteDatabase dataBase;
    private boolean isUpdate;
    private String id;
    Button dateG, cal, save1;
    String dayG,monthG,yearG;
    EditText goalG,amountG;
    String currencyG;
    CheckBox dayBreakG,weekBreakG,monthBreakG;
    Boolean dateB,weekB,monthB;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_goal);

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
                        Toast.makeText(getApplication(),(String)parent.getSelectedItem(),Toast.LENGTH_SHORT).show();
                        currencyG = "₹";
                        break;
                    case 1:Toast.makeText(getApplication(),(String)parent.getSelectedItem(),Toast.LENGTH_SHORT).show();
                        currencyG = "\u20ac";
                        break;
                    case 2:Toast.makeText(getApplication(),(String)parent.getSelectedItem(),Toast.LENGTH_SHORT).show();
                        currencyG = "\u00a3";
                        break;
                    case 3:Toast.makeText(getApplication(),(String)parent.getSelectedItem(),Toast.LENGTH_SHORT).show();
                        currencyG = "¥";
                        break;
                    case 4:Toast.makeText(getApplication(),(String)parent.getSelectedItem(),Toast.LENGTH_SHORT).show();
                        currencyG = "$";
                        break;
                    default:Toast.makeText(getApplication(),"No such choice",Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                //
            }
        });
        //ActionBar actionBar = getSupportActionBar();
        //actionBar.setDisplayHomeAsUpEnabled(true);

        //button for calendar
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

        gHelper = new DbHelperGoal(this);
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
            if((year>curYear)||(year==curYear && month+1>curMonth)||(year==curYear && month+1==curMonth && day>curDay)) {
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
            case R.id.save_goal:
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
                if(!goalG.getText().toString().isEmpty() && !amountG.getText().toString().isEmpty() && !dateG.getText().toString().isEmpty())
                {
                    //Toast.makeText(getBaseContext(),dateB.toString(),Toast.LENGTH_LONG).show();
                    saveData();
                }else{// if slots found blank, pop an alert
                    AlertDialog.Builder alertBuilder=new AlertDialog.Builder(GoalActivity.this);
                    alertBuilder.setTitle("Invalid Data");
                    alertBuilder.setMessage("Please, Enter valid data");
                    alertBuilder.setPositiveButton("Ok", new DialogInterface.OnClickListener() {

                        public void onClick(DialogInterface dialog, int which) {
                            dialog.cancel();
                        }
                    });
                    alertBuilder.create().show();
                }

                return true;
            case R.id.action_settings:
                return true;
        }
        //noinspection SimplifiableIfStatement
        /*if (id == R.id.action_settings) {
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