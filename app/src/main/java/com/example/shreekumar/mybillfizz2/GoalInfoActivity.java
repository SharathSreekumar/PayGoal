package com.example.shreekumar.mybillfizz2;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.HashMap;

public class GoalInfoActivity extends AppCompatActivity {
    TextView GoalInfoTitle, Payment, Expense, Savings, DailyAmount, WeeklyAmount, MonthlyAmount, DaysToGoal;
    public static String GoalId, amount;
    private DbHelperGoal iHelper;
    private SQLiteDatabase iDataBase;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_goal_info);

        GoalId = getIntent().getExtras().getString("ID");
        //Toast.makeText(getBaseContext(), GoalId, Toast.LENGTH_SHORT).show();//Working, parser working successfully
        GoalInfoTitle = (TextView)findViewById(R.id.GoalInfoTitle1);
        Payment = (TextView)findViewById(R.id.PaymentsInfo2);
        Expense = (TextView)findViewById(R.id.ExpenseInfo2);
        Savings = (TextView)findViewById(R.id.SavingsInfo2);
        DailyAmount = (TextView)findViewById(R.id.dailyInfoView1);
        WeeklyAmount = (TextView)findViewById(R.id.weeklyInfoView1);
        MonthlyAmount = (TextView)findViewById(R.id.monthlyInfoView1);
        DaysToGoal = (TextView)findViewById(R.id.deadlineInfo1);
        displayData();
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
        for(c=1; c<m; c++)
        {
            if(c == 2)
            {
                if(y%4 == 0)//checks if year is leap or not
                    x += 29;
                else
                    x += 28;
            }
            else
                x += mon.get(c-1);
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
        int x = 0, i, countW = 0;
        if(yC <= yG){
            for(i = yC; i < yG; i++)
                countW += 12;//12 months // 52 weeks in a year 52 * 7 = 364
        }

        countW -= mC;// current year's month
        countW += mG;// goal year's month
        countW *= Math.round(4.3333);//52 weeks / 12 months
        return (countW);
    }

    public void displayData(){
        //Toast.makeText(getBaseContext(), GoalId, Toast.LENGTH_SHORT).show();//Working, parser working successfully
        //HashMap wordList = new HashMap();
        iHelper = new DbHelperGoal(this.getBaseContext());
        iDataBase = iHelper.getWritableDatabase();
        Cursor mCursor = iDataBase.rawQuery("SELECT * FROM "+ DbHelperGoal.TABLE_NAME +" WHERE "+ DbHelperGoal.KEY_ID +"="+ GoalId, null);
        if (mCursor.moveToFirst()) {
            do{
                GoalInfoTitle.setText(mCursor.getString(mCursor.getColumnIndex(DbHelperGoal.GOAL_TITLE)));
                Payment.setText(mCursor.getString(mCursor.getColumnIndex(DbHelperGoal.CURRENCY)) + " " + mCursor.getString(mCursor.getColumnIndex(DbHelperGoal.ALT_PAYMENT)));
                Expense.setText("-" + mCursor.getString(mCursor.getColumnIndex(DbHelperGoal.CURRENCY)) + " " + mCursor.getString(mCursor.getColumnIndex(DbHelperGoal.ALT_EXPENSE)));
                Savings.setText(mCursor.getString(mCursor.getColumnIndex(DbHelperGoal.CURRENCY)) + " " + String.valueOf(mCursor.getInt(mCursor.getColumnIndex(DbHelperGoal.ALT_PAYMENT)) - mCursor.getInt(mCursor.getColumnIndex(DbHelperGoal.ALT_EXPENSE))));
                //wordList.put(DbHelperGoal.ALT_EXPENSE,mCursor.getString(mCursor.getColumnIndex(DbHelperGoal.AMOUNT)));
                //Toast.makeText(getBaseContext(),monStr.get(mCursor.getColumnIndex(DbHelperGoal.MONTH)),Toast.LENGTH_SHORT).show();
                //Displays goal amount && currency
                amount = mCursor.getString(mCursor.getColumnIndex(DbHelperGoal.AMOUNT));

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
                String goalDate = String.valueOf(goalDay)+"-"+monStr.get(goalMonth-1)+"-"+String.valueOf(goalYear);
                int count = 0;
                //Fetches the date and Time from system, hence not used
                if(curYear <= goalYear) {
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
                    dailyAmount = (mCursor.getFloat(mCursor.getColumnIndex(DbHelperGoal.AMOUNT)) - mCursor.getFloat(mCursor.getColumnIndex(DbHelperGoal.ALT_PAYMENT)) + mCursor.getFloat(mCursor.getColumnIndex(DbHelperGoal.ALT_EXPENSE))) / count;
                    if(count != 1)
                        DaysToGoal.setText(String.valueOf(count) + " days until "+ goalDate);
                    else
                        DaysToGoal.setText(String.valueOf(count) + " day until "+ goalDate);
                }
                else{// current year exceeds goal year
                    DailyAmount.setText("Time's up");
                    WeeklyAmount.setText("Time's up");
                    MonthlyAmount.setText("Time's up");
                    DaysToGoal.setText("0 days left");
                }

                if(Boolean.valueOf(mCursor.getString(mCursor.getColumnIndex(DbHelperGoal.BREAKDOWN_DAY)))==true && count>0){
                    DailyAmount.setText(mCursor.getString(mCursor.getColumnIndex(DbHelperGoal.CURRENCY))+" "+String.valueOf(Math.ceil(dailyAmount))+"/Day");
                }else {
                    //DailyAmount.setVisibility(View.INVISIBLE);
                    DailyAmount.setText("-/Day");
                }

                if(Boolean.valueOf(mCursor.getString(mCursor.getColumnIndex(DbHelperGoal.BREAKDOWN_WEEK)))==true && count >= 7){
                    int countW = calDateWeek(curMonth,curYear,goalMonth,goalYear);
                    WeeklyAmount.setText(mCursor.getString(mCursor.getColumnIndex(DbHelperGoal.CURRENCY)) + " " + String.valueOf(Math.ceil(dailyAmount * count / countW)) + "/Week");
                }else{
                    //WeeklyAmount.setVisibility(View.INVISIBLE);
                    WeeklyAmount.setText("-/Week");
                }

                if(Boolean.valueOf(mCursor.getString(mCursor.getColumnIndex(DbHelperGoal.BREAKDOWN_MONTH)))==true && count >= 28){
                    int countM = calDateMonth(curMonth,curYear,goalMonth,goalYear);
                    MonthlyAmount.setText(mCursor.getString(mCursor.getColumnIndex(DbHelperGoal.CURRENCY))+" "+String.valueOf(Math.ceil(dailyAmount * count / countM))+"/Month");
                }else {
                    //MonthlyAmount.setVisibility(View.INVISIBLE);
                    MonthlyAmount.setText(String.valueOf("-/Month"));
                }
                //DaysToGoal.setText(mCursor.getString(mCursor.getColumnIndex(DbHelperGoal.DAY)) + "/" + mCursor.getString(mCursor.getColumnIndex(DbHelperGoal.MONTH)) + "/" + mCursor.getString(mCursor.getColumnIndex(DbHelperGoal.YEAR)));
                //break;
            } while (mCursor.moveToNext());
        }
        mCursor.close();
        //return wordList;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_goal_info, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        /*if (id == R.id.action_settings) {
            return true;
        }*/
        switch(id){
            case R.id.save_goal_Pay:
                break;
            case R.id.action_settings:
                Toast.makeText(getApplication(),"You clicked Settings",Toast.LENGTH_SHORT);
                break;
        }

        return super.onOptionsItemSelected(item);
    }
}
