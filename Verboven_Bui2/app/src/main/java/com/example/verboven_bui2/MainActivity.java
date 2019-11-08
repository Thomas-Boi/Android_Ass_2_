package com.example.verboven_bui2;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.Locale;
import java.util.zip.Inflater;

public class MainActivity extends AppCompatActivity {
    private DatabaseReference readingsDB;
    private AlertDialog addReadingDialog;
    private ArrayList<Reading> readingList;
    private ListView allReadingsLV;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        readingList = new ArrayList<>();
        allReadingsLV = findViewById(R.id.readingLV);
    }

    protected void onStart() {
        super.onStart();
        readingsDB = FirebaseDatabase.getInstance().getReference("readings");
        readingsDB.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                readingList.clear();
                for (DataSnapshot toDoTaskSnapshot : dataSnapshot.getChildren()) {
                    Reading toDoTask = toDoTaskSnapshot.getValue(Reading.class);
                    readingList.add(toDoTask);
                }

                ReadingListAdapter adapter = new ReadingListAdapter(MainActivity.this,
                        readingList);
                allReadingsLV.setAdapter(adapter);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) { }
        });

        allReadingsLV.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                displayEditDialog(position);
                return true;
            }
        });
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
                name);

        // go to this function to display the condition
        displayCondition(reading);

        String id = readingsDB.push().getKey();
        Task task = readingsDB.child(id).setValue(reading);

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

    private void displayEditDialog(int index) {
        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(MainActivity.this);
        LayoutInflater inflater = getLayoutInflater();

        View dialogView = inflater.inflate(R.layout.edit_reading_dialog, null);

        TextView nameET = dialogView.findViewById(R.id.nameEditText);
        TextView systolicET = dialogView.findViewById(R.id.systolicEditText);
        TextView diastolicET = dialogView.findViewById(R.id.diastolicEditText);

        Reading reading = readingList.get(index);
        nameET.setText(reading.getName());
        systolicET.setText(Integer.toString(reading.getSystolicReading()));
        diastolicET.setText(Integer.toString(reading.getDiastolicReading()));

        dialogBuilder.setView(dialogView);
        dialogBuilder.create().show();
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
        String formattedMin = min < 10 ? "0" + min : Integer.toString(min);

        String amPm = curTime.get(Calendar.AM_PM) == Calendar.AM ? "AM" : "PM";

        return hour + ":" + formattedMin + " " + amPm;
    }

}
