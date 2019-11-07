package com.example.verboven_bui2;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import java.time.LocalDateTime;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.zip.Inflater;

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
        AlertDialog.Builder diaglogBuilder = new AlertDialog.Builder(this);
        LayoutInflater inflater = getLayoutInflater();

        View addReadingView = inflater.inflate(R.layout.activity_add_reading_dialog, null);
        diaglogBuilder.setView(addReadingView);

        final AlertDialog addReadingDialog = diaglogBuilder.create();
        addReadingDialog.show();

        // set event handlers
        Button cancelBtn = addReadingView.findViewById(R.id.cancelBtn);
        cancelBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                addReadingDialog.dismiss();
            }
        });

        Button submitBtn = addReadingView.findViewById(R.id.submitBtn);
        submitBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                addReading();
            }
        });
    }

    protected void addReading() {
        EditText systolicET = findViewById(R.id.systolicEditText);
        int systolicReading = Integer.parseInt(systolicET.getText().toString());

        EditText diastolicET = findViewById(R.id.diastolicEditText);
        int diastolicReading = Integer.parseInt(diastolicET.getText().toString());

        GregorianCalendar curTime = new GregorianCalendar();
        Reading reading = new Reading(curTime, systolicReading, diastolicReading);
    }
}
