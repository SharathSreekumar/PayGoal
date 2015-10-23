package com.example.shreekumar.mybillfizz2;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Handler;
import android.provider.CalendarContract;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.Toast;

import java.text.ParseException;
import java.text.ParsePosition;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.concurrent.TimeUnit;

//This java file is used to display a listview for Goal and also to update and delete
// This file calls "GoalAdapter.java"
public class GoalDisActivity extends AppCompatActivity {


    ImageButton tri;
    static final int PAY_DIALOG_ID = 0;
    private int currentYear, currentMonth, currentDay;
    PopupWindow pwindo;
    int buttonPosFlag = 0;
    AlertDialog alert;
    String currencyG, moneyValue;
    GoalAdapter goaladpt;
    private DbHelperGoal gHelper;
    private SQLiteDatabase dataBase;
    EditText PayValue, ExpValue;

    private ArrayList<String> keyId = new ArrayList<String>();
    private ArrayList<String> goalTitle = new ArrayList<String>();
    private ArrayList<String> date = new ArrayList<String>();
    private ArrayList<String> day = new ArrayList<String>();
    private ArrayList<String> month = new ArrayList<String>();
    private ArrayList<String> year = new ArrayList<String>();
    private ArrayList<String> amount = new ArrayList<String>();
    //private ArrayList<String> dailyBreak = new ArrayList<String>();
    //private ArrayList<String> weeklyBreak = new ArrayList<String>();
    //private ArrayList<String> monthlyBreak = new ArrayList<String>();
    private ArrayList<String> daysLeftGoal = new ArrayList<String>();
    private ArrayList<Integer> progressValue = new ArrayList<Integer>();

    public ListView goalList2;

    private AlertDialog.Builder build ;

    private ProgressBar mProgress;
    private int mProgressStatus = 0;
    private Handler mHandler = new Handler();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_goal_dis);

        //call the listview
        goalList2 = (ListView) findViewById(R.id.goalListView2);

        gHelper = new DbHelperGoal(this);//call the database

        //goaladpt.setCustomButtonListener(GoalDisActivity.this);

        tri = (ImageButton) findViewById(R.id.imageButtonAdd);
        tri.setOnClickListener(new AdapterView.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(getApplicationContext(), GoalActivity.class);
                i.putExtra("update", false);
                startActivity(i);
            }
        });

        goalList2.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> arg0, View arg1, final int arg2, long arg3) {
                Intent i = new Intent(getApplicationContext(), GoalInfoActivity.class);
                i.putExtra("Goal", goalTitle.get(arg2));
                i.putExtra("Date", date.get(arg2));
                i.putExtra("Amount", amount.get(arg2));
                //i.putExtra("DailyBreakDown", dailyBreak.get(arg2));
                //i.putExtra("WeeklyBreakDown", weeklyBreak.get(arg2));
                //i.putExtra("MonthlyBreakDown", monthlyBreak.get(arg2));
                i.putExtra("DaysLeft", daysLeftGoal.get(arg2));
                i.putExtra("ID", keyId.get(arg2));

                i.putExtra("update", true);
                startActivity(i);
            }
        });

        //On List data long Click
        goalList2.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            public boolean onItemLongClick(AdapterView<?> arg0, View arg1, final int arg2, long arg3) {
                final CharSequence[] listClick = {"Payment", "Expense", "Delete"};

                build = new AlertDialog.Builder(GoalDisActivity.this);
                build.setTitle("Make your selection");
                build.setItems(listClick, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int item) {
                        // Do something with the selection
                        switch (item) {
                            case 0:
                                LayoutInflater li = LayoutInflater.from(GoalDisActivity.this);
                                View promptsPaymentView = li.inflate(R.layout.payment_layout, null);
                                build = new AlertDialog.Builder(GoalDisActivity.this);
                                build.setTitle("Payment");
                                build.setMessage("Please Enter payment amount");
                                build.setView(promptsPaymentView);
                                PayValue = (EditText) promptsPaymentView.findViewById(R.id.PaymentEnter1);
                                //PayValue.isFocused();
                                PayValue.setFocusableInTouchMode(true);
                                PayValue.setFocusable(true);
                                PayValue.requestFocus();
                                //InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                                //imm.showSoftInput(PayValue, InputMethodManager.SHOW_IMPLICIT);
                                build.setPositiveButton("Ok",new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int which) {
                                        dataBase = gHelper.getWritableDatabase();
                                        Cursor mCursor = dataBase.rawQuery("SELECT * FROM "+ DbHelperGoal.TABLE_NAME+" WHERE "+DbHelperGoal.KEY_ID+"="+keyId.get(arg2), null);
                                        if (mCursor.moveToFirst()) {
                                            do {
                                                moneyValue = mCursor.getString(mCursor.getColumnIndex(DbHelperGoal.ALT_PAYMENT));
                                            } while (mCursor.moveToNext());
                                        }
                                        float val = Float.valueOf(PayValue.getText().toString())+ Float.valueOf(moneyValue);
                                        String strSQL = "UPDATE "+DbHelperGoal.TABLE_NAME+" SET "+DbHelperGoal.ALT_PAYMENT+"="+String.valueOf(val)+" WHERE "+DbHelperGoal.KEY_ID+"="+keyId.get(arg2);
                                        dataBase.execSQL(strSQL);
                                        Toast.makeText(getApplication(),PayValue.getText().toString(),Toast.LENGTH_SHORT).show();
                                        //PayValue.setText("");
                                        displayData();
                                        //InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                                        //imm.hideSoftInputFromWindow(PayValue.getWindowToken(), 0);
                                        dialog.cancel();
                                    }
                                });
                                build.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int which) {
                                        Toast.makeText(getApplication(), "Payment Cancelled", Toast.LENGTH_SHORT).show();
                                        //InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                                        //imm.hideSoftInputFromWindow(PayValue.getWindowToken(), 0);
                                        dialog.cancel();
                                    }
                                });
                                alert = build.create();
                                alert.show();
                                alert.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_VISIBLE);
                                break;
                            case 1://Expense
                                //Toast.makeText(getApplication(), "Expense", Toast.LENGTH_SHORT).show();
                                LayoutInflater le = LayoutInflater.from(GoalDisActivity.this);
                                View promptsExpenseView = le.inflate(R.layout.payment_layout, null);
                                build = new AlertDialog.Builder(GoalDisActivity.this);
                                build.setTitle("Expense");
                                build.setMessage("Please Enter withdrawl amount");
                                build.setView(promptsExpenseView);
                                ExpValue = (EditText) promptsExpenseView.findViewById(R.id.PaymentEnter1);
                                //PayValue.isFocused();
                                build.setPositiveButton("Ok",new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int which) {
                                        dataBase = gHelper.getWritableDatabase();
                                        Cursor mCursor = dataBase.rawQuery("SELECT * FROM "+ DbHelperGoal.TABLE_NAME+" WHERE "+DbHelperGoal.KEY_ID+"="+keyId.get(arg2), null);
                                        if (mCursor.moveToFirst()) {
                                            do {
                                                moneyValue = mCursor.getString(mCursor.getColumnIndex(DbHelperGoal.ALT_EXPENSE));
                                            } while(mCursor.moveToNext());
                                        }
                                        float val = Float.valueOf(ExpValue.getText().toString())+Float.valueOf(moneyValue);
                                        String strSQL = "UPDATE "+DbHelperGoal.TABLE_NAME+" SET "+DbHelperGoal.ALT_EXPENSE+"="+String.valueOf(val)+" WHERE "+DbHelperGoal.KEY_ID+"="+keyId.get(arg2);
                                        dataBase.execSQL(strSQL);
                                        ExpValue.setText("");
                                        displayData();
                                        dialog.cancel();
                                    }
                                });
                                build.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int which) {
                                        Toast.makeText(getApplication(), "Expense Cancelled", Toast.LENGTH_SHORT).show();
                                        dialog.cancel();
                                    }
                                });
                                alert = build.create();
                                alert.show();
                                alert.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_VISIBLE);
                                break;
                            case 2://Toast.makeText(getApplication(),"Delete",Toast.LENGTH_SHORT).show();
                                //Delete Data
                                build = new AlertDialog.Builder(GoalDisActivity.this);
                                build.setTitle("Delete " + goalTitle.get(arg2) + " " + date.get(arg2) + " " + amount.get(arg2));
                                build.setMessage("Do you want to delete ?");
                                build.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int which) {
                                        Toast.makeText(getApplicationContext(), goalTitle.get(arg2) + " " + date.get(arg2) + " " + amount.get(arg2) + " is deleted.", Toast.LENGTH_LONG).show();
                                        dataBase.delete(DbHelperGoal.TABLE_NAME, DbHelperGoal.KEY_ID + "=" + keyId.get(arg2), null);
                                        displayData();
                                        dialog.cancel();
                                    }
                                });
                                build.setNegativeButton("No", new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int which) {
                                        dialog.cancel();
                                    }
                                });
                                //AlertDialog alert = build.create();
                                alert = build.create();
                                alert.show();
                                break;
                            default:
                                Toast.makeText(getApplication(), "default", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
                AlertDialog alert = build.create();
                alert.show();
                return true;
            }
        });

        //hide button on slide down
        goalList2.setOnScrollListener(new AbsListView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(AbsListView view, int scrollState) {
                int zero = 0;
                int btn_initPosY = tri.getScrollY();
                if (scrollState == SCROLL_STATE_TOUCH_SCROLL) {
                    tri.animate().cancel();
                    tri.animate().translationYBy(150);
                }else if(scrollState == SCROLL_STATE_FLING){
                    tri.animate().cancel();
                    tri.animate().translationYBy(150);
                } else {
                    tri.animate().cancel();
                    tri.animate().translationYBy(-150);
                }
            }

            @Override
            public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {

            }
        });
    }

    @Override
    protected void onResume() {
        displayData();
        super.onResume();
    }

    //days_of_months(Jan,Feb,Mar,April,May,June,July,Aug,Sept,Oct,Nov,Dec)
    ArrayList<Integer> mon = new ArrayList<Integer>(Arrays.asList(31, 28, 31, 30, 31, 30, 31, 31, 30, 31, 30, 31));
    ArrayList<String> monStr = new ArrayList<String>(Arrays.asList("Jan","Feb","Mar","Apr","May","June","July","Aug","Sept","Oct","Nov","Dec"));

    // calculates days from months
    int calMonthDay(int m,int y){//calMonthDay(month,year)
        int x=0,c;
        for(c=1;c<m;c++)
        {
            if(c==1)
            {
                if(y%4==0)//checks if year is leap or not
                    x+=29;
                else
                    x+=28;
            }
            else
                x+=mon.get(c-1);
        }
        return(x);
    }

    //calculates no. of months from current month & year to goal month & year
    int calDateMonth(int mC,int yC,int mG,int yG){
        int x = 0,i,countM=0;
        if(yC<=yG){
            for(i = yC; i < yG; i++)
                countM+=12;
        }

        countM -= mC;
        countM += mG;
        return (countM);
    }

    //calculates no. of weeks from current month & year to goal month & year
    int calDateWeek(int mC,int yC,int mG,int yG){
        int x = 0,i,countW=0;
        if(yC<=yG){
            for(i = yC; i < yG; i++)
                countW+=52;
        }

        countW -= mC;
        countW += mG;
        countW *= 4;
        return (countW);
    }

    private void displayData() {
        dataBase = gHelper.getWritableDatabase();
        Cursor mCursor = dataBase.rawQuery("SELECT * FROM "+ DbHelperGoal.TABLE_NAME, null);

        keyId.clear();
        goalTitle.clear();
        date.clear();
        daysLeftGoal.clear();
        amount.clear();
        progressValue.clear();
        //dailyBreak.clear();
        //weeklyBreak.clear();
        //monthlyBreak.clear();
        if (mCursor.moveToFirst()) {
            do {
                keyId.add(mCursor.getString(mCursor.getColumnIndex(DbHelperGoal.KEY_ID)));
                goalTitle.add(mCursor.getString(mCursor.getColumnIndex(DbHelperGoal.GOAL_TITLE)));
                //date.add(mCursor.getString(mCursor.getColumnIndex(DbHelperGoal.DATE)));
                day.add(mCursor.getString(mCursor.getColumnIndex(DbHelperGoal.DAY)));
                month.add(mCursor.getString(mCursor.getColumnIndex(DbHelperGoal.MONTH)));
                year.add(mCursor.getString(mCursor.getColumnIndex(DbHelperGoal.YEAR)));

                //display date using day/month/year
                date.add(mCursor.getString(mCursor.getColumnIndex(DbHelperGoal.DAY))+"-"+monStr.get(mCursor.getInt(mCursor.getColumnIndex(DbHelperGoal.MONTH)) - 1)+"-"+mCursor.getString(mCursor.getColumnIndex(DbHelperGoal.YEAR)));

                //mProgress = (ProgressBar) findViewById(R.id.progressBarGoal);
                int addSavings = Integer.valueOf(mCursor.getString(mCursor.getColumnIndex(DbHelperGoal.ALT_PAYMENT))) - Integer.valueOf(mCursor.getString(mCursor.getColumnIndex(DbHelperGoal.ALT_EXPENSE)));
                float calSavings = Float.valueOf(addSavings) / Float.valueOf(mCursor.getString(mCursor.getColumnIndex(DbHelperGoal.AMOUNT)))*100;
                progressValue.add((int) calSavings);

                int amountLeftPay = Integer.valueOf(mCursor.getString(mCursor.getColumnIndex(DbHelperGoal.AMOUNT)))-addSavings;
                //Displays goal amount && currency
                amount.add(mCursor.getString(mCursor.getColumnIndex(DbHelperGoal.CURRENCY))+" "+String.valueOf(amountLeftPay)+" of "+mCursor.getString(mCursor.getColumnIndex(DbHelperGoal.AMOUNT)));

                //Calculate the days & amount per day/week/month
                final Calendar c = Calendar.getInstance();
                int curYear = c.get(Calendar.YEAR), curMonth = c.get(Calendar.MONTH)+1, curDay = c.get(Calendar.DAY_OF_MONTH);
                int goalYear = mCursor.getInt(mCursor.getColumnIndex(DbHelperGoal.YEAR)), goalMonth = mCursor.getInt(mCursor.getColumnIndex(DbHelperGoal.MONTH)), goalDay = mCursor.getInt(mCursor.getColumnIndex(DbHelperGoal.DAY));
                int calYear = 0,calMonth = 0,calDay = 0,calDayGoal = 0;
                float dailyAmount=0;

                //Get current date
                String curDate = String.valueOf(curDay)+"/"+String.valueOf(curMonth)+"/"+String.valueOf(curYear);
                //String goalDate=String.valueOf(mCursor.getString(mCursor.getColumnIndex(DbHelperGoal.DATE)));

                //Get goal date
                //String goalDate = String.valueOf(mCursor.getString(mCursor.getColumnIndex(DbHelperGoal.DAY)))+"/"+String.valueOf(mCursor.getString(mCursor.getColumnIndex(DbHelperGoal.MONTH)))+"/"+String.valueOf(mCursor.getString(mCursor.getColumnIndex(DbHelperGoal.YEAR)));
                String goalDate = String.valueOf(goalDay)+"/"+String.valueOf(goalMonth)+"/"+String.valueOf(goalYear);
                int count = 0;
                //Fetches the date and Time from system, hence not used
                if(curYear<=goalYear) {
                    count = 0;
                    int i;
                    for (i = curYear; i < goalYear; i++) {
                        if (i % 4 == 0) {
                            count += 366;//Leap year
                        } else {
                            count += 365;// Non leap year
                        }
                    }
                    //calculating the no of days left from current date to goal date
                    count -= calMonthDay(curMonth, curYear);
                    count -= curDay;
                    count += calMonthDay(goalMonth, goalYear);
                    count += goalDay;
                    if (count < 0) {
                        count *= -1;
                    }
                    // amount divided as per date
                    dailyAmount = mCursor.getFloat(mCursor.getColumnIndex(DbHelperGoal.AMOUNT)) / count;
                    daysLeftGoal.add(String.valueOf(count)+" days left");
                }
                else{// current year exceeds goal year
                    //dailyBreak.add("Timeup");
                    //weeklyBreak.add("Timeup");
                    //monthlyBreak.add("Timeup");
                    daysLeftGoal.add("0 days left");
                }
            } while (mCursor.moveToNext());
        }
        goaladpt = new GoalAdapter(GoalDisActivity.this,keyId, goalTitle, day, month, year, date, amount,daysLeftGoal,progressValue);// dailyBreak,weeklyBreak,monthlyBreak,daysLeftGoal);
        //goaladpt.setCustomButtonListener(GoalAltActivity.class);
        goalList2.setAdapter(goaladpt);
        //goalList2.invalidateViews();
        mCursor.close();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_goal_dis, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}