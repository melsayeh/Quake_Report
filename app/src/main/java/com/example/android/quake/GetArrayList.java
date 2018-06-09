package com.example.android.quake;

import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class GetArrayList{

    /**
     * Return a list of {@link Earthquake} objects that has been built up from
     * parsing a JSON response.
     */
    public ArrayList<Earthquake> extractEarthquakes() {

        // Create an empty ArrayList that we can start adding earthquakes to
        ArrayList<Earthquake> earthquakes = new ArrayList<Earthquake>();

        // Try to parse the SAMPLE_JSON_RESPONSE. If there's a problem with the way the JSON
        // is formatted, a JSONException exception object will be thrown.
        // Catch the exception so the app doesn't crash, and print the error message to the logs.
        try {

            // TODO: Parse the response given by the SAMPLE_JSON_RESPONSE string and
            // build up a list of Earthquake objects with the corresponding data.

            JSONObject root = new JSONObject();

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

    public GetArrayList(){
    }
}
