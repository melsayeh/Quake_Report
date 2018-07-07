package com.example.android.quake;

import android.content.AsyncTaskLoader;
import android.content.Context;
import android.util.Log;

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
            jsonResponse = makeHttpRequest(new URL(mUrl));

        } catch (MalformedURLException e) {
            Log.e("Main Activity", "Error with the URL" + e);
        } catch (IOException ioe) {
            Log.e("Main Activity", "IOException occurs: " + ioe);
        }
        //earthquake data extracted from jsonResponse
        earthquakes = extractEarthquakes(jsonResponse);
        return earthquakes;
    }

    @Override
    protected void onStartLoading(){
        forceLoad();
    }

    /*
       Configure and initiate the connection to the URL
       Get input stream from the URL
       Fetch JSON from the stream
       */
    public String makeHttpRequest(URL url) throws IOException {
        String jsonResponse = "";
        if (url == null) {
            return null;
        }
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


    private ArrayList<Earthquake> extractEarthquakes(String jResponse) {

        // Create an empty ArrayList that we can start adding earthquakes to
        ArrayList<Earthquake> earthquakes = new ArrayList<>();

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
            Log.e("Earthquake Activity", "Problem parsing the earthquake JSON results", e);
        }

        // Return the list of earthquakes
        return earthquakes;
    }
}
