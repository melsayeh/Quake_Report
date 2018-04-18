package com.example.android.quake;

import android.app.Activity;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import java.sql.Time;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;

/**
 * Created by Mansour on 16-Apr-18.
 */

public class EqAdapter<E extends Object> extends ArrayAdapter<Earthquake>{


    public EqAdapter (@NonNull Activity context , ArrayList<Earthquake> resource){
        super(context,0, resource);
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {

        // (Recycling) Check if the existing view is being reused, otherwise inflate the view
        View listItemView = convertView;
        if (listItemView==null){
            listItemView = LayoutInflater.from(getContext()).inflate(R.layout.list_item,parent,false);}

        // Get the {@link Word} object located at this position in the list
        final Earthquake currentWord = getItem(position);

        //Declare and assign values to TextViews of Magnitude, Location and Date

        TextView magValue = (TextView) listItemView.findViewById(R.id.mag);
        DecimalFormat formatter = new DecimalFormat("0.0");
        String output = formatter.format(currentWord.getMagnitude());
        magValue.setText(output);

        TextView locValue = (TextView) listItemView.findViewById(R.id.loc);
        TextView offsetVal = (TextView) listItemView.findViewById(R.id.offset);

        if (currentWord.getLocation().contains(" of ")) {
            String[] split = currentWord.getLocation().split(" of ");
            locValue.setText(split[1]);
            offsetVal.setText(split[0] + " of");
        }else {
            locValue.setText(currentWord.getLocation());
            offsetVal.setText("Near the");
        }

        TextView dateValue = (TextView) listItemView.findViewById(R.id.date);

        Long timeInM =  currentWord.getDate();
        Date dateObject = new Date(timeInM);

        SimpleDateFormat dateFormatter = new SimpleDateFormat("MMM DD, yyyy", Locale.getDefault());
        String dateToDisplay = dateFormatter.format(dateObject);
        dateValue.setText(dateToDisplay);

        TextView timeValue = (TextView) listItemView.findViewById(R.id.time);
        SimpleDateFormat timeFormatter = new SimpleDateFormat("hh:mm a", Locale.getDefault());

        String timeToDisplay = timeFormatter.format(dateObject);
        timeValue.setText(timeToDisplay);

        // Return the whole list item layout (containing 3 TextViews)
        // so that it can be shown in the ListView
        return listItemView;
    }
}
