/*
 * Copyright (C) 2016 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.example.android.quake;


import android.app.LoaderManager;
import android.content.Context;
import android.content.Loader;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.ArrayList;

public class EarthquakeActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<ArrayList<Earthquake>> {
    private static final String WEBSITE = "https://earthquake.usgs.gov/fdsnws/event/1/query?format=geojson&starttime=2010-01-01&endtime=2018-08-08&minmagnitude=5.1";
    private static final int EARTHQUAKE_LOADER_ID = 1;
    private ArrayList<Earthquake> mEarthquakes;
    TextView emptyState;
    ProgressBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.earthquake_activity);
        emptyState = (TextView) findViewById(R.id.empty_state);
        progressBar = (ProgressBar) findViewById(R.id.progress_bar);
        progressBar.setVisibility(View.VISIBLE);
        //Create Loader instance
        LoaderManager loaderManager = getLoaderManager();
        //initiate the loader when the activity starts up
        loaderManager.initLoader(EARTHQUAKE_LOADER_ID,null, this);
    }


    @Override
    public Loader<ArrayList<Earthquake>> onCreateLoader(int i, Bundle bundle) {
        EarthqauakeLoader earthqauakeLoader = new EarthqauakeLoader(this,WEBSITE);
        return earthqauakeLoader;
    }

    //Update the UI after finishing loading
    @Override
    public void onLoadFinished(Loader<ArrayList<Earthquake>> loader, ArrayList<Earthquake> earthquakes){
        mEarthquakes=earthquakes;
//        ConnectivityManager cm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
//        NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
//        Boolean isConnected = activeNetwork !=null&&activeNetwork.isConnectedOrConnecting();


        if(earthquakes==null) {
            return;
        }
        if (EarthqauakeLoader.getCheckInternet()) {
            emptyState.setText(R.string.no_earthquake);
        }else
            emptyState.setText(R.string.no_connection);

        progressBar.setVisibility(View.GONE);
        updateUi(mEarthquakes);
        Log.v("Verify Loader","******Loader action on load finished*****");
    }

    @Override
    public void onLoaderReset(Loader<ArrayList<Earthquake>> loader){
        mEarthquakes.clear();
        Log.v("Verify Loader","*****Loader action on reset******");
    }

    public void updateUi(ArrayList<Earthquake> getEarthquakes){
        // Fetches {@link ArrayList} earthquake.
        ArrayList<Earthquake> earthquakes = getEarthquakes;

        // Find a reference to the {@link ListView} in the layout
        ListView earthquakeListView = (ListView) findViewById(R.id.list);
        earthquakeListView.setEmptyView(emptyState);

        // Create a new {@link ArrayAdapter} of earthquakes
        EqAdapter<Earthquake> adapter = new EqAdapter<Earthquake>(this,  earthquakes);

        // Set the adapter on the {@link ListView}
        // so the list can be populated in the user interface
        if (earthquakeListView != null) {
            earthquakeListView.setAdapter(adapter);
        }
    }
}
