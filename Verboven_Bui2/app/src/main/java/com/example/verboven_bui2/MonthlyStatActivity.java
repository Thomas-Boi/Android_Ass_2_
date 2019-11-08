package com.example.verboven_bui2;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import java.util.ArrayList;

public class MonthlyStatActivity extends AppCompatActivity {
    private ArrayList<Reading> readingsList;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_monthly_stat);
        readingsList = getIntent().getParcelableArrayListExtra("readingList");
    }
}
