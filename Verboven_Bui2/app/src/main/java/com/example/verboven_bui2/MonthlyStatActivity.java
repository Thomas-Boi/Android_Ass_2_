package com.example.verboven_bui2;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

public class MonthlyStatActivity extends AppCompatActivity {
    private ArrayList<Reading> readingsList;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_monthly_stat);
        readingsList = getIntent().getParcelableArrayListExtra("readingList");
    }

    protected void onStart() {

        super.onStart();
        TextView month1 = findViewById(R.id.month);
        String str = "";
        for (Reading reading : readingsList) {
            str += reading.getName() + " " + reading.getDiastolicReading() + "\n";

//        TextView month1 = findViewById(R.id.month);



        }
        month1.setText(str);
    }

}
