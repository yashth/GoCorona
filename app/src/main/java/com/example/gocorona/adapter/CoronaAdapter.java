package com.example.gocorona.adapter;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.gocorona.Corona;
import com.example.gocorona.R;

import java.util.ArrayList;

public class CoronaAdapter extends ArrayAdapter<Corona> {

    public CoronaAdapter(@NonNull Context context, ArrayList<Corona> corona) {
        super(context, 0,corona);
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {

        Log.d("CoronaAdapter","getView: "+ parent);

        View listItemView = convertView;

        if(listItemView == null) {
            listItemView = LayoutInflater.from(getContext()).inflate(R.layout.corona_list, parent, false);
        }

        Corona currentPosition = getItem(position);
        Log.d("CoronaAdapter","getView: currentPosition "+ currentPosition);

        TextView country = (TextView) listItemView.findViewById(R.id.country_name);
        String countryName = currentPosition.getCountryName();
        Log.d("CoronaAdapter","getView: countryName "+ countryName);
        // Display the magnitude of the current earthquake in that TextView
        country.setText(countryName);


        TextView total_confirmed = (TextView) listItemView.findViewById(R.id.total_Confirmed);
        String totalConfirmed = String.valueOf(currentPosition.getTotalConfirmed());
        Log.d("CoronaAdapter","getView: totalConfirmed "+ totalConfirmed);
        // Display the magnitude of the current earthquake in that TextView
        total_confirmed.setText(totalConfirmed);


        return listItemView;
    }
}
