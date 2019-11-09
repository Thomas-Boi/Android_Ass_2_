package com.example.verboven_bui2;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.GregorianCalendar;

public class ViewAllReadingsActivity extends AppCompatActivity {
    private DatabaseReference readingsDB;
    private ListView allReadingsLV;
    private ArrayList<Reading> readingList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_all_readings);
        allReadingsLV = findViewById(R.id.readingLV);
        readingsDB = FirebaseDatabase.getInstance().getReference("readings");
    }

    public void onStart() {
        super.onStart();
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
        final AlertDialog dialog = dialogBuilder.create();
        dialog.show();

        // set event handlers
        Button cancelBtn = dialogView.findViewById(R.id.cancelBtn);
        cancelBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
            }
        });

        // set event handlers
        Button updateBtn = dialogView.findViewById(R.id.updateBtn);
        updateBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                updateReading(index, dialog);
                dialog.dismiss();
            }
        });
    }

    private void updateReading(int readingIndex, AlertDialog editDialog) {
        Reading updatedReading = readingList.get(readingIndex);
        String id = updatedReading.getKey();
        String oldName = updatedReading.getName();

        // delete old reading
        readingsDB.child(oldName).child(id).removeValue();

        // get new values and create new reading
        EditText nameTV = editDialog.findViewById(R.id.nameEditText);
        EditText systolicTV = editDialog.findViewById(R.id.systolicEditText);
        EditText diastolicTV = editDialog.findViewById(R.id.diastolicEditText);

        String name = nameTV.getText().toString();
        int systolicReading = Integer.parseInt(systolicTV.getText().toString());
        int diastolicReading = Integer.parseInt(diastolicTV.getText().toString());

        GregorianCalendar curTime = new GregorianCalendar();
        Reading reading = new Reading(Reading.getCurTimeAsStr(curTime),
                Reading.getCurDateAsStr(curTime),
                systolicReading,
                diastolicReading,
                id, name);

        readingsDB.child(name).child(id).setValue(reading);
        Toast.makeText(ViewAllReadingsActivity.this,
                "Reading Updated", Toast.LENGTH_LONG).show();
    }
}
