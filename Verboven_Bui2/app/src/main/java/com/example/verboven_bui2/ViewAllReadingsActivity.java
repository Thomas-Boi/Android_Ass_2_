package com.example.verboven_bui2;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

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

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.GregorianCalendar;

public class ViewAllReadingsActivity extends AppCompatActivity {
    private DatabaseReference readingsDB;
    private ListView allReadingsLV;
    private ArrayList<Reading> readingList;
    private AlertDialog editReadingDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_all_readings);
        allReadingsLV = findViewById(R.id.readingLV);
        readingsDB = FirebaseDatabase.getInstance().getReference("readings");
    }

    public void onStart() {
        super.onStart();
        // display readings we got from main activity
        readingList = getIntent().getParcelableArrayListExtra("readingList");
        ReadingListAdapter adapter = new ReadingListAdapter(ViewAllReadingsActivity.this,
                readingList);
        allReadingsLV.setAdapter(adapter);

        allReadingsLV.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                displayEditDialog(position);
                return true;
            }
        });

        // on data change, auto update readingList
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


    private void displayEditDialog(final int index) {
        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(
                ViewAllReadingsActivity.this);
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
        editReadingDialog = dialogBuilder.create();
        editReadingDialog.show();

        // set event handlers
        Button cancelBtn = dialogView.findViewById(R.id.cancelBtn);
        cancelBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                editReadingDialog.dismiss();
            }
        });

        // set event handlers
        Button updateBtn = dialogView.findViewById(R.id.updateBtn);
        updateBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                updateReading(index, editReadingDialog);
            }
        });
    }

    private void updateReading(int readingIndex, AlertDialog editDialog) {
        // get new values and create new reading
        EditText systolicET = editDialog.findViewById(R.id.systolicEditText);
        String systolicReading = systolicET.getText().toString().trim();

        EditText diastolicET = editDialog.findViewById(R.id.diastolicEditText);
        String diastolicReading = diastolicET.getText().toString().trim();

        EditText nameET = editDialog.findViewById(R.id.nameEditText);
        String name = nameET.getText().toString().trim();

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
            Toast.makeText(ViewAllReadingsActivity.this,
                    "Please enter a number here", Toast.LENGTH_LONG).show();
            return;
        }

        // get the current reading
        Reading oldReading = readingList.get(readingIndex);
        String id = oldReading.getKey();
        String oldName = oldReading.getName();

        // delete old reading
        readingsDB.child(oldName).child(id).removeValue();

        // create new reading
        GregorianCalendar curTime = new GregorianCalendar();
        Reading reading = new Reading(Reading.getCurTimeAsStr(curTime),
                Reading.getCurDateAsStr(curTime),
                systolicRate,
                diastolicRate,
                id, name);

        readingsDB.child(name).child(id).setValue(reading);
        Toast.makeText(ViewAllReadingsActivity.this,
                "Reading Updated", Toast.LENGTH_LONG).show();

        // close dialog when done
        editReadingDialog.dismiss();
    }
}
