package com.example.verboven_bui2;

import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ListView;

import java.util.ArrayList;

public class MonthlyStatActivity extends AppCompatActivity {
    private ArrayList<Reading> readingsList;
    private ListView monthlyStatLV;
    private ArrayList<MonthlyStat> stats;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_monthly_stat);
        readingsList = getIntent().getParcelableArrayListExtra("readingList");
        monthlyStatLV = findViewById(R.id.monthlyStatLV);
        stats = new ArrayList<>();
    }

    protected void onStart() {
        super.onStart();
        createMonthlyStats();
    }

    // create the monthly stats of each user.
    private void createMonthlyStats() {
        String curUser = readingsList.get(0).getName();
        int systolicSum = 0;
        int diastolicSum = 0;
        int counter = 0;

        // note: due to how we layout the firebase,
        // the names are sorted in alphabetical order
        // so we will get all the same names at the same place
        for (Reading reading : readingsList) {
            String name = reading.getName();

            // if we see a new name
            if ((!curUser.equals(name))) {
                // make a MonthlyStat obj and add it to stats
                stats.add(new MonthlyStat(curUser, systolicSum,
                        diastolicSum, counter));

                // reset values
                curUser = name;
                systolicSum = 0;
                diastolicSum = 0;
                counter = 0;
            }

            systolicSum += reading.getSystolicReading();
            diastolicSum += reading.getDiastolicReading();
            counter++;
        }

        // add the last user's stats after loop ended
        stats.add(new MonthlyStat(curUser, systolicSum,
                diastolicSum, counter));

        // display the monthly stat tables based on data given.
        MonthlyStatListAdapter listAdapter = new MonthlyStatListAdapter(
                MonthlyStatActivity.this, stats);
        monthlyStatLV.setAdapter(listAdapter);
    }

}
