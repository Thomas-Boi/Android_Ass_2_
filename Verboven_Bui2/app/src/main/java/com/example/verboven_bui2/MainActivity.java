package com.example.verboven_bui2;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    public void onAddReading(View v) {
        openAddPopUp();
    }

    protected void openAddPopUp() {

    }

    public void onCancel(View v) {
    }

    public void onSubmit(View v) {
        addReading();
    }

    protected void addReading() {

    }
}
