/*
 * Copyright 2013 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.example.android.networkconnect;

import android.content.BroadcastReceiver;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.wifi.ScanResult;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.content.LocalBroadcastManager;
import android.util.TypedValue;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;

import com.example.android.common.logger.Log;
import com.example.android.common.logger.LogFragment;
import com.example.android.common.logger.LogWrapper;
import com.example.android.common.logger.MessageOnlyLogFilter;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Sample application demonstrating how to connect to the network and fetch raw
 * HTML. It uses AsyncTask to do the fetch on a background thread. To establish
 * the network connection, it uses HttpURLConnection.
 *
 * This sample uses the logging framework to display log output in the log
 * fragment (LogFragment).
 */
public class MainActivity extends FragmentActivity {

    private String readFromFile(String filename) {

        String ret = "";

        try {
            InputStream inputStream = openFileInput(filename);

            if ( inputStream != null ) {
                InputStreamReader inputStreamReader = new InputStreamReader(inputStream);
                BufferedReader bufferedReader = new BufferedReader(inputStreamReader);
                String receiveString = "";
                StringBuilder stringBuilder = new StringBuilder();

                while ( (receiveString = bufferedReader.readLine()) != null ) {
                    stringBuilder.append(receiveString);
                }

                inputStream.close();
                ret = stringBuilder.toString();
            }
        }
        catch (FileNotFoundException e) {
            android.util.Log.e("login activity", "File not found: " + e.toString());
        } catch (IOException e) {
            android.util.Log.e("login activity", "Can not read file: " + e.toString());
        }

        return ret;
    }

    public static final String TAG = "Network Connect";

  //  WifiManager wifi;
    ListView lv;
    TextView textStatus;
    Button buttonScan;
    int size = 0;
    List<ScanResult> results;
    BroadcastReceiver receiver;
    String ITEM_KEY = "key";
    ArrayList<HashMap<String, String>> arraylist = new ArrayList<HashMap<String, String>>();
    SimpleAdapter adapter;

    // Reference to the fragment showing events, so we can clear it with a button
    // as necessary.
    private LogFragment mLogFragment;
    private  SimpleTextFragment introFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.sample_main);

        Intent i = new Intent(MainActivity.this, WifiService.class);
        MainActivity.this.startService(i);

        Intent btIntent = new Intent(MainActivity.this, BluetoothService.class);
        MainActivity.this.startService(btIntent);

        AddressUpdate addressUpdate = new AddressUpdate(this);

        //super.onCreate(savedInstanceState);

//        receiver = new BroadcastReceiver() {
//            @Override
//            public void onReceive(Context context, Intent intent) {
//                String s = intent.getStringExtra(WifiService.THE_MESSAGE);
//                // do something here.
//            }
//        };

       // wifi = (WifiManager) getSystemService(Context.WIFI_SERVICE);


        // Initialize text fragment that displays intro text.
        introFragment = (SimpleTextFragment)
                    getSupportFragmentManager().findFragmentById(R.id.intro_fragment);
        introFragment.setText(R.string.welcome_message);
        introFragment.getTextView().setTextSize(TypedValue.COMPLEX_UNIT_DIP, 16.0f);



        // Initialize the logging framework.
        initializeLogging();
//        if (wifi.isWifiEnabled() == false)
//        {
//            Toast.makeText(getApplicationContext(), "wifi is disabled..making it enabled", Toast.LENGTH_LONG).show();
//            mLogFragment.getLogView().setText("wifi is disabled..making it enabled");
//
//            wifi.setWifiEnabled(true);
//        }

    }

    @Override
    protected void onStart() {
        super.onStart();
        LocalBroadcastManager.getInstance(this).registerReceiver((receiver),
                new IntentFilter(WifiService.THE_MESSAGE)
        );
    }

    @Override
    protected void onStop() {
        LocalBroadcastManager.getInstance(this).unregisterReceiver(receiver);
        super.onStop();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            // When the user clicks FETCH, fetch the first 500 characters of
            // raw HTML from www.google.com.
            case R.id.fetch_action:
                onbody b=new onbody(this);
                String wifiStatus = readFromFile("wifiTable");
                String onBodyStatus = "" + b.onBody;
                String locationStatus = "N/A";
                String pickingUpStatus = "N/A";
                String blueToothStatus = "N/A";
                int xFactor = 55;


                String result = "WIFI: "+wifiStatus+"\n"
                        +"OnBody:"+"\t"+onBodyStatus+"\n"
                        +"Location:"+"\t"+locationStatus+"\n"
                        +"PickingUp:"+"\t"+pickingUpStatus+"\n"
                        +"BlueTooth:" +"\t"+blueToothStatus+"\n"
                        +"XFactor:"+"\t"+xFactor;
                introFragment.getTextView().setText(result);

                return true;
            // Clear the log view fragment.
            case R.id.clear_action:
                String wifilog = readFromFile("wifiLog");
                  mLogFragment.getLogView().setText(wifilog);


//                WifiService wifiService = new WifiService();
//                wifiService.startService();
              return true;

            case R.id.show_action:
                Intent i =new Intent(this,List_CAtegoriy.class);
                this.startActivity(i);
//                wifi.setWifiEnabled(true);
//
//                arraylist.clear();
//                wifi.startScan();
//                results = wifi.getScanResults();
//                size = results.size();
//
//                Toast.makeText(this, "Scanning...." + size, Toast.LENGTH_SHORT).show();
//                mLogFragment.getLogView().setText("Scanning...");
//                String currentNet = wifi.getConnectionInfo().getSSID();
//                StringBuilder sb = new StringBuilder();
//                sb.append("cur="+currentNet+" "+"\n");
//                try
//                {
//                    size = size - 1;
//                    while (size >= 0)
//                    {
//                        HashMap<String, String> item1 = new HashMap<String, String>();
//                        item1.put(ITEM_KEY, results.get(size).SSID + "  " + results.get(size).capabilities);
//
//                        sb.append(results.get(size).SSID + "  " + results.get(size).level+"\n");
//                        arraylist.add(item1);
//                        size--;
//                       // adapter.notifyDataSetChanged();
//                    }
//                }
//                catch (Exception e)
//                { }
//                mLogFragment.getLogView().setText(sb.toString());
                return true;
        }
        return false;
    }

    /**
     * Implementation of AsyncTask, to fetch the data in the background away from
     * the UI thread.
     */
    private class DownloadTask extends AsyncTask<String, Void, String> {

        @Override
        protected String doInBackground(String... urls) {
            try {
                return loadFromNetwork(urls[0]);
            } catch (IOException e) {
              return getString(R.string.connection_error);
            }
        }

        /**
         * Uses the logging framework to display the output of the fetch
         * operation in the log fragment.
         */
        @Override
        protected void onPostExecute(String result) {
          Log.i(TAG, result);
        }
    }

    /** Initiates the fetch operation. */
    private String loadFromNetwork(String urlString) throws IOException {
        InputStream stream = null;
        String str ="Hi";

        try {
            stream = downloadUrl(urlString);
            str = readIt(stream, 500);
       } finally {
           if (stream != null) {
               stream.close();
            }
        }
        return str;
    }

    /**
     * Given a string representation of a URL, sets up a connection and gets
     * an input stream.
     * @param urlString A string representation of a URL.
     * @return An InputStream retrieved from a successful HttpURLConnection.
     * @throws java.io.IOException
     */
    private InputStream downloadUrl(String urlString) throws IOException {
        // BEGIN_INCLUDE(get_inputstream)
        URL url = new URL(urlString);
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        conn.setReadTimeout(10000 /* milliseconds */);
        conn.setConnectTimeout(15000 /* milliseconds */);
        conn.setRequestMethod("GET");
        conn.setDoInput(true);
        // Start the query
        conn.connect();
        InputStream stream = conn.getInputStream();
        return stream;
        // END_INCLUDE(get_inputstream)
    }

    /** Reads an InputStream and converts it to a String.
     * @param stream InputStream containing HTML from targeted site.
     * @param len Length of string that this method returns.
     * @return String concatenated according to len parameter.
     * @throws java.io.IOException
     * @throws java.io.UnsupportedEncodingException
     */
    private String readIt(InputStream stream, int len) throws IOException, UnsupportedEncodingException {
        Reader reader = null;
        reader = new InputStreamReader(stream, "UTF-8");
        char[] buffer = new char[len];
        reader.read(buffer);
        return new String(buffer);
    }

    /** Create a chain of targets that will receive log data */
    public void initializeLogging() {

        // Using Log, front-end to the logging chain, emulates
        // android.util.log method signatures.

        // Wraps Android's native log framework
        LogWrapper logWrapper = new LogWrapper();
        Log.setLogNode(logWrapper);

        // A filter that strips out everything except the message text.
        MessageOnlyLogFilter msgFilter = new MessageOnlyLogFilter();
        logWrapper.setNext(msgFilter);

        // On screen logging via a fragment with a TextView.
        mLogFragment =
                (LogFragment) getSupportFragmentManager().findFragmentById(R.id.log_fragment);
        msgFilter.setNext(mLogFragment.getLogView());
    }
}
