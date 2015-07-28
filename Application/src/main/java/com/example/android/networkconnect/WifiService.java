package com.example.android.networkconnect;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.net.wifi.ScanResult;
import android.net.wifi.WifiManager;
import android.os.Binder;
import android.os.Handler;
import android.os.IBinder;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;
import android.widget.Toast;

import com.example.android.common.logger.LogFragment;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;

/**
 * Created by ThomasZhao on 7/26/15.
 */
public class WifiService extends Service {
    static final public String THE_MESSAGE = "haha1";

    WifiManager wifi;
    int size = 0;
    List<ScanResult> results;
    String ITEM_KEY = "key";
    ArrayList<HashMap<String, String>> arraylist = new ArrayList<HashMap<String, String>>();
    LocalBroadcastManager broadcaster;
     LogFragment mLogFragment;

    private NotificationManager mNM;

    // Unique Identification Number for the Notification.
    // We use it on Notification start, and to cancel it.
    private int NOTIFICATION = R.string.local_service_started;
    private void updateList(){

    }
    private void update(){

        Toast.makeText(this, "updating....", Toast.LENGTH_SHORT).show();
        String currentNet = wifi.getConnectionInfo().getSSID();
        int currentIp = wifi.getConnectionInfo().getIpAddress();

        Calendar c = Calendar.getInstance();
        SimpleDateFormat sdf = new SimpleDateFormat("dd:MMMM:yyyy HH:mm:ss a");
        String strDate = sdf.format(c.getTime());

        //int seconds = c.get(Calendar.SECOND);
        String filename = "wifiLog";
        String string = currentNet+","+currentIp+","+strDate ;
        FileOutputStream outputStream;


        try {
            outputStream = openFileOutput(filename,  MODE_APPEND);
            outputStream.write(string.getBytes());
            outputStream.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        Toast.makeText(this, "updated...."+string, Toast.LENGTH_SHORT).show();

    }

    private Handler handler = new Handler( );

    private Runnable runnable = new Runnable( ) {
        public void run ( ) {
            update();

            handler.postDelayed(this,1000*5);
//postDelayed(this,1000)方法安排一个Runnable对象到主线程队列中
        }
    };



    /**
     * Class for clients to access.  Because we know this service always
     * runs in the same process as its clients, we don't need to deal with
     * IPC.
     */
    public class LocalBinder extends Binder {
        WifiService getService() {
            return WifiService.this;
        }
    }
    private void writeToFile(String filename, String content, int mode) {

        filename = filename;
        String string = content;
        FileOutputStream outputStream;

        try {
            outputStream = openFileOutput(filename, mode);
            outputStream.write(string.getBytes());
            outputStream.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

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
            Log.e("login activity", "File not found: " + e.toString());
        } catch (IOException e) {
            Log.e("login activity", "Can not read file: " + e.toString());
        }

        return ret;
    }
//    public void sendResult(String message) {
//        Intent intent = new Intent("haha");
//        if(message != null)
//            intent.putExtra("nothing", message);
//        broadcaster.sendBroadcast(intent);
//    }
    @Override
    public void onCreate() {

         writeToFile("wifiLog","", Context.MODE_PRIVATE);

        //broadcaster = LocalBroadcastManager.getInstance(this);
        handler.postDelayed(runnable, 1000); // 开始Timer
        //handler.removeCallbacks(runnable); //停止Timer
        mNM = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
        wifi = (WifiManager) getSystemService(Context.WIFI_SERVICE);

        onbody b=new onbody(this);

        if (wifi.isWifiEnabled() == false)
        {
            Toast.makeText(getApplicationContext(), "wifi is disabled..making it enabled", Toast.LENGTH_LONG).show();
           // mLogFragment.getLogView().setText("wifi is disabled..making it enabled");

            wifi.setWifiEnabled(true);
        }

        wifi.setWifiEnabled(true);

        arraylist.clear();
        wifi.startScan();
        results = wifi.getScanResults();
        size = results.size();

        Toast.makeText(this, "Scanning...." + size, Toast.LENGTH_SHORT).show();
     //   mLogFragment.getLogView().setText("Scanning...");
        String currentNet = wifi.getConnectionInfo().getSSID();
        int currentIp = wifi.getConnectionInfo().getIpAddress();

        StringBuilder sb = new StringBuilder();
        String result = "";

        //workWifi = currentNet;
        //write work to a file
       // writeToFile("wifiTable",currentNet+","+currentIp, Context.MODE_PRIVATE);

//        String filename = "wifiTable";
//        String string = currentNet+","+currentIp;
//        FileOutputStream outputStream;
//
//        try {
//            outputStream = openFileOutput(filename, Context.MODE_PRIVATE);
//            outputStream.write(string.getBytes());
//            outputStream.close();
//        } catch (Exception e) {
//            e.printStackTrace();
//        }


        String workWifi = readFromFile("wifiTable").split(",")[0];

        if ( currentNet.equals(workWifi)){
            result = "WORK";
        }
        else result = "UNKOWN";
        sb.append("cur="+currentNet+" "+"\n");
        try
        {
            size = size - 1;
            while (size >= 0)
            {
                HashMap<String, String> item1 = new HashMap<String, String>();
                item1.put(ITEM_KEY, results.get(size).SSID + "  " + results.get(size).capabilities);

                sb.append(results.get(size).SSID + "  " + results.get(size).level+"\n");
                arraylist.add(item1);
                size--;
                // adapter.notifyDataSetChanged();
            }

        }
        catch (Exception e)
        { }
//        this.sendResult(sb.toString());
        Toast.makeText(this, "WIFI: " + currentNet+" STATUS: "+result, Toast.LENGTH_SHORT).show();

        // mLogFragment.getLogView().setText(sb.toString());

        // Display a notification about us starting.  We put an icon in the status bar.
        //showNotification();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Log.i("LocalService", "Received start id " + startId + ": " + intent);
        // We want this service to continue running until it is explicitly
        // stopped, so return sticky.
        return START_STICKY;
    }

    @Override
    public void onDestroy() {
        // Cancel the persistent notification.
        mNM.cancel(NOTIFICATION);

        // Tell the user we stopped.
        Toast.makeText(this, "service stopped", Toast.LENGTH_SHORT).show();
    }

    @Override
    public IBinder onBind(Intent intent) {
        return mBinder;
    }

    // This is the object that receives interactions from clients.  See
    // RemoteService for a more complete example.
    private final IBinder mBinder = new LocalBinder();

    /**
     * Show a notification while this service is running.
     */
//    private void showNotification() {
//        // In this sample, we'll use the same text for the ticker and the expanded notification
//        CharSequence text = getText(R.string.local_service_started);
//
//        // Set the icon, scrolling text and timestamp
//        Notification notification = new Notification(R.drawable.stat_sample, text,
//                System.currentTimeMillis());
//
//        // The PendingIntent to launch our activity if the user selects this notification
//        PendingIntent contentIntent = PendingIntent.getActivity(this, 0,
//                new Intent(this, LocalServiceActivities.Controller.class), 0);
//
//        // Set the info for the views that show in the notification panel.
//        notification.setLatestEventInfo(this, getText(R.string.local_service_label),
//                text, contentIntent);
//
//        // Send the notification.
//        mNM.notify(NOTIFICATION, notification);
//    }
}
