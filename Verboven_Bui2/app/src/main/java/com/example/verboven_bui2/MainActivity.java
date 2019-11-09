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

import org.w3c.dom.Text;

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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        readingList = new ArrayList<>();
    }

    protected void onStart() {
        super.onStart();
        readingsDB = FirebaseDatabase.getInstance().getReference("readings");
        readingsDB.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                readingList.clear();
                for (DataSnapshot user : dataSnapshot.getChildren()) {
                    for (DataSnapshot readingSnapshot : user.getChildren()) {
                        Reading reading = readingSnapshot.getValue(Reading.class);
                        readingList.add(reading);
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) { }
        });
    }

    public void onAddReading(View v) {
        openAddPopUp();
    }

    public void onViewMonthlyReading(View v) {
        Intent i = new Intent(MainActivity.this, MonthlyStatActivity.class);
        i.putExtra("readingList", readingList);
        startActivity(i);
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

        // check for empty str
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
                    "You must enter a name.",
                    Toast.LENGTH_LONG).show();
            return;
        }

        // check that rates are int
        int systolicRate;
        int diastolicRate;
        try {
            systolicRate = Integer.parseInt(systolicReading);
            diastolicRate = Integer.parseInt(diastolicReading);
        }
        catch (NumberFormatException e) {
            Toast.makeText(MainActivity.this,
                    "Please enter a number here", Toast.LENGTH_LONG).show();
            return;
        }

        GregorianCalendar curTime = new GregorianCalendar();
        Reading reading = new Reading(Reading.getCurTimeAsStr(curTime),
                Reading.getCurDateAsStr(curTime),
                systolicRate,
                diastolicRate,
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
        addReadingDialog.dismiss();
    }

    public void onViewAllReadings(View v) {
        Intent i = new Intent(MainActivity.this, ViewAllReadingsActivity.class);
        i.putExtra("readingList", readingList);
        startActivity(i);
    }

    // put code to display condition here
    public void displayCondition(Reading reading) {
        String condition = reading.getCondition();

    }

}
