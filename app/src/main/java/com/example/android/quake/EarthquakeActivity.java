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

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.charset.Charset;
import java.util.ArrayList;

public class EarthquakeActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.earthquake_activity);

        //initiate the task and execute onCreate
        QuakeAsyncTask task = new QuakeAsyncTask();
        task.execute();
    }

    public void updateUi(ArrayList<Earthquake> getEarthquakes){
        // Fetches {@link ArrayList} earthquake.
        ArrayList<Earthquake> earthquakes = getEarthquakes;

        // Find a reference to the {@link ListView} in the layout
        ListView earthquakeListView = (ListView) findViewById(R.id.list);

        // Create a new {@link ArrayAdapter} of earthquakes
        EqAdapter<Earthquake> adapter = new EqAdapter<Earthquake>(this,  earthquakes);

        // Set the adapter on the {@link ListView}
        // so the list can be populated in the user interface
        if (earthquakeListView != null) {
            earthquakeListView.setAdapter(adapter);
        }
    }

    /* Inherit {@link QuakeAsyncTask} from AsyncTask class */
    public final class QuakeAsyncTask extends AsyncTask<URL,Void,ArrayList<Earthquake>> {
        public static final String WEBSITE = "https://earthquake.usgs.gov/fdsnws/event/1/query?format=geojson&starttime=2012-01-01&endtime=2017-12-01&minmagnitude=5";

        @Override
        protected ArrayList<Earthquake> doInBackground(URL... urls) {
            String jsonResponse = "";
            ArrayList<Earthquake> earthquakes = null;
            try {
                URL url = new URL(WEBSITE);
                //Fetch jsonRespone
                jsonResponse = makeHttpRequest(url);
            } catch (MalformedURLException e) {
                Log.e("Main Activity", "Error with the URL" + e);
            } catch (IOException ioe) {
                Log.e("Main Activity", "IOException occurs: " + ioe);
            }
            //earthquake data extracted from jsonResponse
            earthquakes = extractEarthquakes(jsonResponse);
            return earthquakes;
        }

        //Update the UI after finishing execution of the task
        @Override
        protected void onPostExecute(ArrayList<Earthquake> earthquakes){
            updateUi(earthquakes);
        }

        /*
        Configure and initiate the connection to the URL
        Get input stream from the URL
        Fetch JSON from the stream
        */
        private String makeHttpRequest(URL url) throws IOException {
            String jsonResponse = "";
            if (url == null)
                return jsonResponse;

            HttpURLConnection urlConnection = null;
            InputStream inputStream = null;
            try {
                urlConnection = (HttpURLConnection) url.openConnection();
                urlConnection.setRequestMethod("GET");
                urlConnection.setReadTimeout(10000 /* milliseconds */);
                urlConnection.setConnectTimeout(15000 /* milliseconds */);
                urlConnection.connect();
                inputStream = urlConnection.getInputStream();
                if (urlConnection.getResponseCode() == 200) /* If the connection is successful */
                    jsonResponse = readFromStream(inputStream);

            } catch (IOException e) {
                Log.e("Main Activity", "Error opening connection" + e);
            } catch (NullPointerException e) {
                e.printStackTrace();
            } finally {
                if (urlConnection != null) {
                    urlConnection.disconnect();
                }
                if (inputStream != null) {
                    inputStream.close();
                }
            }
            return jsonResponse;
        }

        private String readFromStream(InputStream inputStream) throws IOException {
            StringBuilder builder = new StringBuilder();
            InputStreamReader inputStreamReader = new InputStreamReader(inputStream, Charset.forName("UTF-8"));
            BufferedReader reader = new BufferedReader(inputStreamReader);
            String line = reader.readLine();
            while (line != null) {
                builder.append(line);
                line = reader.readLine();
            }
            return builder.toString();
        }
    }

    public ArrayList<Earthquake> extractEarthquakes(String jResponse) {

        // Create an empty ArrayList that we can start adding earthquakes to
        ArrayList<Earthquake> earthquakes = new ArrayList<Earthquake>();

        // Try to parse the SAMPLE_JSON_RESPONSE. If there's a problem with the way the JSON
        // is formatted, a JSONException exception object will be thrown.
        // Catch the exception so the app doesn't crash, and print the error message to the logs.
        try {

            // build up a list of Earthquake objects with the corresponding data.

            JSONObject root = new JSONObject(jResponse);

            JSONArray featuresArray = root.getJSONArray("features");

            for (int i=0; i<featuresArray.length(); i++) {
                JSONObject arrayElement = featuresArray.getJSONObject(i);
                JSONObject properties = arrayElement.getJSONObject("properties");
                double mag = properties.getDouble("mag");
                String place = properties.getString("place");
                Long time = properties.getLong("time");
                String url = properties.getString("url");
                earthquakes.add(new Earthquake(mag , place , time , url));
            }


        } catch (JSONException e) {
            // If an error is thrown when executing any of the above statements in the "try" block,
            // catch the exception here, so the app doesn't crash. Print a log message
            // with the message from the exception.
            Log.e("QueryUtils", "Problem parsing the earthquake JSON results", e);
        }

        // Return the list of earthquakes
        return earthquakes;
    }
}
