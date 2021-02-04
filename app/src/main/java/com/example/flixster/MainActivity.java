package com.example.flixster;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;

import com.codepath.asynchttpclient.AsyncHttpClient;
import com.codepath.asynchttpclient.callback.JsonHttpResponseHandler;
import com.example.flixster.models.Movie;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;

import okhttp3.Headers;

public class MainActivity extends AppCompatActivity {

    /* create a constant of the endpoint for the IMDB API
    * TAG created to test via log */
    public static final String NOW_PLAYING_URL = "https://api.themoviedb.org/3/movie/now_playing?api_key=a07e22bc18f5cb106bfe4cc1f83ad8ed";
    public static final String TAG = "MainActivity";

    List<Movie> movies;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        /* create an instance of the asynchttpclient*/
        AsyncHttpClient client = new AsyncHttpClient();
        /* make a get request on that endpoint, and pass a callback
        * reason we are doing the json.. is because we know the api is returning
        * json. other options could of been for example text. */

        client.get(NOW_PLAYING_URL, new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int i, Headers headers, JSON json) {
                // logging out to check if successful or not.
                Log.d(TAG, "onSuccess");
                // returns the actual jsonObject
                JSONObject jsonObject = json.jsonObject;
                // Try => get corresponding jsonArray with key: 'results'
                // must use catch in case the key: 'results' doesn't exist. handles error
                // Log.e => log error, Log.i => log info
                try {
                    JSONArray results = jsonObject.getJSONArray("results");
                    Log.i(TAG, "Results: " + results.toString());
                    // Return list of Movie objects
                    movies = Movie.fromJsonArray(results);
                    Log.i(TAG, "Movies: " + movies.size());
                } catch (JSONException e) {
                    Log.e(TAG, "Hit json exception");
                }

            }

            @Override
            public void onFailure(int i, Headers headers, String s, Throwable throwable) {
                Log.d(TAG, "onFail");
            }
        });
    }
}