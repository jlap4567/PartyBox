package com.partybox.adapters;

import android.content.Context;
import android.widget.ArrayAdapter;
import android.widget.TextView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.common.collect.Lists;
import com.partyboxAPI.PartySummary;
import com.partybox.R;

import java.util.ArrayList;
import java.util.Set;

/**
 * Adapter for desplaying summaries of parties in a list
 */
public class PartySummaryAdapter extends ArrayAdapter<PartySummary> {
    public PartySummaryAdapter(Context context, ArrayList<PartySummary> summaries) {
        super(context, 0, summaries);
    }

    public PartySummaryAdapter(Context context, int view, Set<PartySummary> summaries) {
        super(context, view, Lists.newArrayList(summaries));
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        // Get the data item for this position
        PartySummary summary = getItem(position);

        // Check if an existing view is being reused, otherwise inflate the view
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.party_list_entry, parent, false);
        }

        // Lookup view for data population
        TextView tvName = (TextView) convertView.findViewById(R.id.nameTextView);
        TextView tvDate = (TextView) convertView.findViewById(R.id.date);

        // Populate the data into the template view using the data object
        tvName.setText(summary.getName());
        tvDate.setText(summary.getDate().toString());

        // Return the completed view to render on screen
        return convertView;
    }
}
