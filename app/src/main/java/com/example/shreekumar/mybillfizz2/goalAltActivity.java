package com.example.shreekumar.mybillfizz2;

import android.database.sqlite.SQLiteDatabase;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

//For Goal Payment
public class GoalAltActivity extends AppCompatActivity {

    String currencyG;
    TextView GoalInfoTitle, Payment, Expense, Savings;
    public static String GoalId, amount;
    private DbHelperGoal iHelper;
    private SQLiteDatabase iDataBase;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_goal_alt);

        //Dropdown currency
        Spinner spinner = (Spinner) findViewById(R.id.spinnerPay1);
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this, R.array.currency, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                switch (position) {
                    case 0:
                        Toast.makeText(getApplication(), (String) parent.getSelectedItem(), Toast.LENGTH_SHORT).show();
                        currencyG = "₹";
                        break;
                    case 1:
                        Toast.makeText(getApplication(), (String) parent.getSelectedItem(), Toast.LENGTH_SHORT).show();
                        currencyG = "\u20ac";
                        break;
                    case 2:
                        Toast.makeText(getApplication(), (String) parent.getSelectedItem(), Toast.LENGTH_SHORT).show();
                        currencyG = "\u00a3";
                        break;
                    case 3:
                        Toast.makeText(getApplication(), (String) parent.getSelectedItem(), Toast.LENGTH_SHORT).show();
                        currencyG = "¥";
                        break;
                    case 4:
                        Toast.makeText(getApplication(), (String) parent.getSelectedItem(), Toast.LENGTH_SHORT).show();
                        currencyG = "$";
                        break;
                    default:
                        Toast.makeText(getApplication(), "No such choice", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                //
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_goal_alt, menu);
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
