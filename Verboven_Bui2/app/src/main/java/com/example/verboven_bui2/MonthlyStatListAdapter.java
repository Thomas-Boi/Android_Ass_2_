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

public class MonthlyStatListAdapter extends ArrayAdapter<MonthlyStat> {
    private Activity activityContext;
    private List<MonthlyStat> monthlyStats;

    public MonthlyStatListAdapter(Activity activityContext, List<MonthlyStat> stats) {
        super(activityContext, R.layout.monthly_stat_table_list_item, stats);
        this.activityContext = activityContext;
        this.monthlyStats = stats;
    }

    public MonthlyStatListAdapter(Context activityContext, int resource,
                              List<MonthlyStat> objects, Activity context1,
                              List<MonthlyStat> stats) {
        super(activityContext, resource, objects);
        this.activityContext = context1;
        this.monthlyStats = stats;
    }

    @NonNull
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater = activityContext.getLayoutInflater();

        View listViewItem = inflater.inflate(R.layout.monthly_stat_table_list_item,
                null, true);

        TextView nameTV = listViewItem.findViewById(R.id.nameTV);
        TextView systolicTV = listViewItem.findViewById(R.id.systolicTV);
        TextView diastolicTV = listViewItem.findViewById(R.id.diastolicTV);

        MonthlyStat stat = monthlyStats.get(position);

        nameTV.setText(stat.getUser());
        systolicTV.setText(Float.toString(stat.getSystolicAverage()));
        diastolicTV.setText(Float.toString(stat.getDiastolicAverage()));

        return listViewItem;
    }
}
