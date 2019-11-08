package com.example.verboven_bui2;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;

import java.util.List;


public class ReadingListAdapter extends ArrayAdapter<Reading> {
    private Activity activityContext;
    private List<Reading> readingList;

    public ReadingListAdapter(Activity activityContext, List<Reading> readings) {
        super(activityContext, R.layout.reading_list_item, readings);
        this.activityContext = activityContext;
        this.readingList = readings;
    }

    public ReadingListAdapter(Context activityContext, int resource,
                              List<Reading> objects, Activity context1,
                              List<Reading> readings) {
        super(activityContext, resource, objects);
        this.activityContext = context1;
        this.readingList = readings;
    }

    @NonNull
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater = activityContext.getLayoutInflater();

        View listViewItem = inflater.inflate(R.layout.reading_list_item,
                null, true);

        TextView nameTV = listViewItem.findViewById(R.id.nameTV);
        TextView systolicTV = listViewItem.findViewById(R.id.systolicTV);
        TextView diastolicTV = listViewItem.findViewById(R.id.diastolicTV);
        TextView dateTV = listViewItem.findViewById(R.id.dateTV);
        TextView timeTV = listViewItem.findViewById(R.id.timeTV);

        Reading reading = readingList.get(position);
        nameTV.setText(reading.getName());
        systolicTV.setText(Integer.toString(reading.getSystolicReading()));
        diastolicTV.setText(Integer.toString(reading.getDiastolicReading()));
        dateTV.setText(reading.getCurDate());
        timeTV.setText(reading.getCurTime());

        return listViewItem;
    }
}
