package com.example.verboven_bui2;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.time.LocalDateTime;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.Locale;
import java.util.zip.Inflater;

public class MainActivity extends AppCompatActivity {
    private DatabaseReference readingsDB;
    private AlertDialog addReadingDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    protected void onStart() {
        super.onStart();
        readingsDB = FirebaseDatabase.getInstance().getReference("readings");
    }

    public void onAddReading(View v) {
        openAddPopUp();
    }

    protected void openAddPopUp() {
        AlertDialog.Builder diaglogBuilder = new AlertDialog.Builder(this);
        LayoutInflater inflater = getLayoutInflater();

        View addReadingView = inflater.inflate(R.layout.activity_add_reading_dialog, null);
        diaglogBuilder.setView(addReadingView);

        addReadingDialog = diaglogBuilder.create();
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
        EditText systolicET = addReadingDialog.findViewById(R.id.systolicEditText);
        String systolicReading = systolicET.getText().toString().trim();

        EditText diastolicET = addReadingDialog.findViewById(R.id.diastolicEditText);
        String diastolicReading = diastolicET.getText().toString().trim();

        EditText nameET = addReadingDialog.findViewById(R.id.nameEditText);
        String name = nameET.getText().toString().trim();
        String key = readingsDB.push().getKey();

        if (TextUtils.isEmpty(systolicReading)) {
            Toast.makeText(this,
                    "You must enter a systolic value.",
                    Toast.LENGTH_LONG).show();
            return;
        }

        if (TextUtils.isEmpty(diastolicReading)) {
            Toast.makeText(this,
                    "You must enter a diastolic value.",
                    Toast.LENGTH_LONG).show();
            return;
        }

        if (TextUtils.isEmpty(name)) {
            Toast.makeText(this,
                    "You must enter a diastolic value.",
                    Toast.LENGTH_LONG).show();
            return;
        }

        GregorianCalendar curTime = new GregorianCalendar();
        Reading reading = new Reading(getCurTimeAsStr(curTime),
                getCurDateAsStr(curTime),
                Integer.parseInt(systolicReading),
                Integer.parseInt(diastolicReading),
                key, name);

        // go to this function to display the condition
        displayCondition(reading);


        Task task = readingsDB.child(name).child(key).setValue(reading);






        task.addOnSuccessListener(new OnSuccessListener() {
            @Override
            public void onSuccess(Object o) {
                Toast.makeText(MainActivity.this,
                        "Reading added.",
                        Toast.LENGTH_LONG).show();
            }
        });

        task.addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(MainActivity.this,
                        "Datebase error, please contact creator.",
                        Toast.LENGTH_LONG).show();

                e.printStackTrace(System.err);
            }
        });
    }

    // put code to display condition here
    public void displayCondition(Reading reading) {
        String condition = reading.getCondition();

    }

    private String getCurDateAsStr(GregorianCalendar curTime) {
        int year = curTime.get(Calendar.YEAR);
        int day = curTime.get(Calendar.DAY_OF_MONTH);

        return day + "th "
                + curTime.getDisplayName(Calendar.MONTH,
                Calendar.SHORT,
                Locale.CANADA) + " "
                + year;

    }

    private String getCurTimeAsStr(GregorianCalendar curTime) {
        int hour = curTime.get(Calendar.HOUR);
        int min = curTime.get(Calendar.MINUTE);
        String amPm = curTime.get(Calendar.AM_PM) == Calendar.AM ? "AM" : "PM";

        return hour + ":" + min + " " + amPm;
    }
}
