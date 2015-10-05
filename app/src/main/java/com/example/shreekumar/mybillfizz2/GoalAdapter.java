package com.example.shreekumar.mybillfizz2;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

/**
 * Created by Sharath on 18-08-2015.
 */
public class GoalAdapter  extends BaseAdapter {

    private Context mContext;
    private ArrayList<String> id;
    private ArrayList<String> goal;
    private ArrayList<String> date;
    private ArrayList<String> day;
    private ArrayList<String> month;
    private ArrayList<String> year;
    private ArrayList<String> amount;
    private ArrayList<String> breakdownDay;
    private ArrayList<String> breakdownWeek;
    private ArrayList<String> breakdownMonth;
    private ArrayList<String> daysLeftGoal;

    // receives values from GoalDisActivity.java
    public GoalAdapter(Context c, ArrayList<String> gId,ArrayList<String> gGoal, ArrayList<String> gDay, ArrayList<String> gMonth, ArrayList<String> gYear, ArrayList<String> gDate, ArrayList<String> gAmount, ArrayList<String> gBreakDay, ArrayList<String> gBreakWeek, ArrayList<String> gBreakMonth, ArrayList<String> gDaysLeft) {
        this.mContext = c;

        this.id = gId;
        this.goal = gGoal;
        this.day = gDay;
        this.month = gMonth;
        this.year = gYear;
        this.date = gDate;
        this.amount = gAmount;
        this.breakdownDay = gBreakDay;
        this.breakdownWeek = gBreakWeek;
        this.breakdownMonth = gBreakMonth;
        this.daysLeftGoal = gDaysLeft;
    }

    public customButtonListener customListener;

    public interface customButtonListener {
        public void onButtonClickListener(int position,String value);
    }

    public void setCustomButtonListener(customButtonListener listener) {
        this.customListener = listener;
    }

    public int getCount() {
        // TODO Auto-generated method stub
        return id.size();
    }

    public Object getItem(int position) {
        // TODO Auto-generated method stub
        return null;
    }

    public long getItemId(int position) {
        // TODO Auto-generated method stub
        return 0;
    }

    public View getView(int pos, View child, ViewGroup parent) {
        Holder mHolder;
        LayoutInflater layoutInflater;
        if (child == null) {
            layoutInflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            child = layoutInflater.inflate(R.layout.layout, null);
            mHolder = new Holder();
            //mHolder.g_Id = (TextView) child.findViewById(R.id.listid);
            mHolder.g_goal = (TextView) child.findViewById(R.id.goalView1);
            //mHolder.g_date = (TextView) child.findViewById(R.id.dateView2);
            mHolder.g_date = (TextView) child.findViewById(R.id.dateView2);
            mHolder.g_amount = (TextView) child.findViewById(R.id.amountView2);
            mHolder.g_daybreak = (TextView) child.findViewById(R.id.dailyView2);
            mHolder.g_weekbreak = (TextView) child.findViewById(R.id.weekView2);
            mHolder.g_monthbreak = (TextView) child.findViewById(R.id.monthView2);
            mHolder.g_daysLeft = (TextView) child.findViewById(R.id.daysLeft);
            mHolder.progressGoal = (ProgressBar) child.findViewById(R.id.progressBarGoal);
            child.setTag(mHolder);
        } else {
            mHolder = (Holder) child.getTag();
        }
        //mHolder.g_Id.setText(id.get(pos));
        mHolder.g_goal.setText(goal.get(pos));
        mHolder.g_date.setText(date.get(pos));
        //mHolder.g_day.setText(day.get(pos));
        mHolder.g_amount.setText(amount.get(pos));
        mHolder.g_daybreak.setText(breakdownDay.get(pos));
        mHolder.g_weekbreak.setText(breakdownWeek.get(pos));
        mHolder.g_monthbreak.setText(breakdownMonth.get(pos));
        mHolder.g_daysLeft.setText(daysLeftGoal.get(pos));
        //setting prgress value
        mHolder.progressGoal.setProgress(50);

        //For button
        /*LayoutInflater inflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View rowView = inflater.inflate(R.layout.layout, parent, false);
        final int position = pos;
        final String temp = String.valueOf(getItem(position));
        Button payBtn = (Button) rowView.findViewById(R.id.btnPayment);
        mHolder.paymentBtn = (Button) rowView.findViewById(R.id.btnPayment);
        //String temp = getItem(pos);
        //mHolder.text.setText(temp);
        mHolder.paymentBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(customListener!=null){
                    customListener.onButtonClickListener(position, temp);
                }
            }
        });
        */
        return child;
    }

    public class Holder {
        TextView g_Id;
        TextView g_date;
        TextView g_day;
        TextView g_month;
        TextView g_year;
        TextView g_goal;
        TextView g_amount;
        TextView g_daybreak;
        TextView g_weekbreak;
        TextView g_monthbreak;
        TextView g_daysLeft;
        Button paymentBtn;
        Button expenseBtn;
        ProgressBar progressGoal;
    }
}
