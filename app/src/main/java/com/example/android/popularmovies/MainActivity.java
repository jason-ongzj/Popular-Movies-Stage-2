package com.example.android.popularmovies;

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.TextView;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class MainActivity extends AppCompatActivity {

    private TextView textOutput;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        textOutput = (TextView) findViewById(R.id.textOutput);

        String apiKey = "***REMOVED***";

        new RetrieveFeedTask().execute(apiKey);
    }

    class RetrieveFeedTask extends AsyncTask<String, Void, String[]> {
        private static final String TAG = "AsyncTask";
        public static final String REQUEST_METHOD = "GET";
        public static final int READ_TIMEOUT = 15000;
        public static final int CONNECTION_TIMEOUT = 15000;
        private Exception exception;

        protected void onPreExecute() {
            textOutput.setText("");
        }

        protected String[] doInBackground(String... apiKey) {
            Log.d(TAG, "doInBackground: ");
            HttpURLConnection urlConnection = null;
            String urlString = "https://api.themoviedb.org/3/discover/movie?sort_by=popularity.desc?";

            try {
                URL url = new URL(urlString + "&api_key=" + apiKey[0]);
                urlConnection = (HttpURLConnection) url.openConnection();

                urlConnection.setRequestMethod(REQUEST_METHOD);
                urlConnection.setReadTimeout(READ_TIMEOUT);
                urlConnection.setConnectTimeout(CONNECTION_TIMEOUT);
                int response = urlConnection.getResponseCode();
                Log.d(TAG, "doInBackground: The response code was " + response);

                urlConnection.connect();

                InputStreamReader streamReader = new InputStreamReader(urlConnection.getInputStream());
                BufferedReader bufferedReader = new BufferedReader(streamReader);
                StringBuilder stringBuilder = new StringBuilder();
                String line;
                while ((line = bufferedReader.readLine()) != null) {
                    stringBuilder.append(line).append("\n");
                }
                bufferedReader.close();
                streamReader.close();

                String[] result = MovieDataBaseUtils.getImageFromJson(MainActivity.this, stringBuilder.toString());
                return result;

            } catch(IOException e) {
                e.printStackTrace();
            } catch(Exception e){
                e.printStackTrace();
            } finally {
                if(urlConnection != null){
                    urlConnection.disconnect();
                }
            }
            return null;
        }

        protected void onPostExecute(String[] response) {
            Log.d(TAG, "onPostExecute: " + response);
            if(response != null) {
                for (int i = 0; i < response.length; i++){
                    textOutput.append(response[i] + "\n");
                }
            }
        }
    }



}
