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

import java.util.ArrayList;

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

        TextView magValue = (TextView) listItemView.findViewById(R.id.mag);
        magValue.setText(currentWord.getMagnitude());

        TextView locValue = (TextView) listItemView.findViewById(R.id.loc);
        locValue.setText(currentWord.getLocation());

        TextView dateValue = (TextView) listItemView.findViewById(R.id.date);
        dateValue.setText(currentWord.getDate());

        // Return the whole list item layout (containing 2 TextViews)
        // so that it can be shown in the ListView
        return listItemView;
    }
}
