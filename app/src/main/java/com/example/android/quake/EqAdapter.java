package com.example.android.quake;

import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Intent;
import android.graphics.drawable.GradientDrawable;
import android.net.Uri;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Switch;
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

    @TargetApi(Build.VERSION_CODES.M)
    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {

        // (Recycling) Check if the existing view is being reused, otherwise inflate the view
        View listItemView = convertView;
        if (listItemView==null){
            listItemView = LayoutInflater.from(getContext()).inflate(R.layout.list_item,parent,false);}

        // Get the {@link Word} object located at this position in the list
        final Earthquake pointer = getItem(position);

        //Declare and assign values to TextViews of Magnitude, Location and Date

        TextView magValue = (TextView) listItemView.findViewById(R.id.mag);

        //To format digits like this form: 9.8, 6.3, 8.0
        DecimalFormat formatter = new DecimalFormat("0.0");
        String output = formatter.format(pointer.getMagnitude());
        magValue.setText(output);

        // Set the proper background color on the magnitude circle.
        // Fetch the background from the TextView, which is a GradientDrawable.
        GradientDrawable magnitudeCircle = (GradientDrawable) magValue.getBackground();

        // Get the appropriate background color based on the current earthquake magnitude
        int magnitudeColor = getMagnitudeColor((int) pointer.getMagnitude());
        // Set the color on the magnitude circle
        magnitudeCircle.setColor(getContext().getColor(magnitudeColor));



        //Define the view to show Location
        TextView locValue = (TextView) listItemView.findViewById(R.id.loc);
        TextView offsetVal = (TextView) listItemView.findViewById(R.id.offset);

        //Split the string of the Location into location and location offset
        //and store them in two variables
        if (pointer.getLocation().contains(" of ")) {
            String[] split = pointer.getLocation().split(" of ");
            offsetVal.setText(split[0] + " of");
            locValue.setText(split[1]);
        }else {
            locValue.setText(pointer.getLocation());
            offsetVal.setText("Near the");
        }


        //Define a view to show date
        TextView dateValue = (TextView) listItemView.findViewById(R.id.date);

        //This is to fetch date& time from the Unix time provided
        Long timeInM =  pointer.getDate();
        Date dateObject = new Date(timeInM);

        //Format date in a format of your choice
        SimpleDateFormat dateFormatter = new SimpleDateFormat("MMM DD, yyyy", Locale.getDefault());
        String dateToDisplay = dateFormatter.format(dateObject);
        dateValue.setText(dateToDisplay);

        //Define a view to show time
        TextView timeValue = (TextView) listItemView.findViewById(R.id.time);
        //Format time in a format of your choice
        SimpleDateFormat timeFormatter = new SimpleDateFormat("hh:mm a", Locale.getDefault());
        String timeToDisplay = timeFormatter.format(dateObject);
        timeValue.setText(timeToDisplay);

        //Create Intent to open up the web page of the correspondent earthquake
        listItemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent openUrl = new Intent(Intent.ACTION_VIEW);
                openUrl.setData(Uri.parse(pointer.getUrl()));
                getContext().startActivity(openUrl);
            }

        });

        // Return the whole list item layout (containing 3 TextViews)
        // so that it can be shown in the ListView
        return listItemView;
    }

    //Get the color from the @res colors.xml based on the value of magnitude
    public int getMagnitudeColor (int d){
        int color = R.color.magnitude1;
        switch (d){
            case 1:
                color = R.color.magnitude1;
                break;

            case 2:
                color = R.color.magnitude2;
                break;

            case 3:
                color = R.color.magnitude3;
                break;

            case 4:
                color = R.color.magnitude4;
                break;

            case 5:
                color = R.color.magnitude5;
                break;

            case 6:
                color = R.color.magnitude6;
                break;

            case 7:
                color = R.color.magnitude7;
                break;

            case 8:
                color = R.color.magnitude8;
                break;

            case 9:
                color = R.color.magnitude9;
                break;

            case 10:
                color = R.color.magnitude10plus;
                break;

            default:
                color = R.color.magnitude1;
        }
        return color;
    }

}
