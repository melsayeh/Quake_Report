package com.example.android.quake;

import android.content.AsyncTaskLoader;
import android.content.Context;
import android.util.Log;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;


public class EarthqauakeLoader extends AsyncTaskLoader<ArrayList<Earthquake>>{
    private String mUrl;
    public EarthqauakeLoader(Context context, String url) {
        super(context);
        mUrl = url;
    }

    @Override
    public ArrayList<Earthquake> loadInBackground() {
        String jsonResponse = "";
        ArrayList<Earthquake> earthquakes = null;
        try {
            //Fetch jsonResponse
            jsonResponse = EarthquakeActivity.makeHttpRequest(new URL(mUrl));

        } catch (MalformedURLException e) {
            Log.e("Main Activity", "Error with the URL" + e);
        } catch (IOException ioe) {
            Log.e("Main Activity", "IOException occurs: " + ioe);
        }
        //earthquake data extracted from jsonResponse
        earthquakes = EarthquakeActivity.extractEarthquakes(jsonResponse);
        return earthquakes;
    }

    @Override
    protected void onStartLoading(){
        forceLoad();
    }
}
