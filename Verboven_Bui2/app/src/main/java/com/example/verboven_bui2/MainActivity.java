package com.example.verboven_bui2;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;

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
        System.out.println("Running");
        AlertDialog.Builder diaglogBuilder = new AlertDialog.Builder(this);
        LayoutInflater inflater = getLayoutInflater();

        View addReadingView = inflater.inflate(R.layout.activity_add_reading_dialog, null);
        diaglogBuilder.setView(addReadingView);

        AlertDialog addReadingDialog = diaglogBuilder.create();
        addReadingDialog.show();
    }

    public void onCancel(View v) {
    }

    public void onSubmit(View v) {
        addReading();
    }

    protected void addReading() {

    }
}
