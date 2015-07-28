//<uses-feature android:name="android.hardware.sensor.accelerometer"/>
package com.example.android.networkconnect;


import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.util.Log;
import android.widget.Toast;

import static android.widget.Toast.LENGTH_LONG;


/**
 * Created by abhishek on 7/26/2015.
 */
public class onbody implements SensorEventListener {
    private float mLastX, mLastY, mLastZ;
    boolean mInitialized;
   public static boolean onBody;
    long t1 = 0,t2=0;
    SensorManager mSensorManager;
    Sensor mAccelerometer;
    Context con;
    //constructor
    onbody(Context c){
        this.con=c;
        mSensorManager = (SensorManager) con.getSystemService(con.SENSOR_SERVICE);
        mAccelerometer = mSensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
        mSensorManager.registerListener(this, mAccelerometer, SensorManager.SENSOR_DELAY_NORMAL);
        Toast.makeText(con,"CONSTRUCTOR", Toast.LENGTH_SHORT).show();
        Log.w("HELLLLLLLLO","IHUHFUHFUHFUHFU");

    }

    @Override
    public void onSensorChanged(SensorEvent event) {
        float x = event.values[0];
        float y = event.values[1];
        float z = event.values[2];
        if (!mInitialized) {
            mLastX = x;
            mLastY = y;
            mLastZ = z;
//            tvX.setText("0.0");
//            tvY.setText("0.0");
            mInitialized = true;
        } else {

            float deltaX = Math.abs(mLastX - x);
            float deltaY = Math.abs(mLastY - y);
            float deltaZ = Math.abs(mLastZ - z);
            mLastX = x;
            mLastY = y;
            mLastZ = z;

            String f="";

            if((System.currentTimeMillis()-t1>10000)) {
                if ((deltaX > 0.09 || deltaY > 0.09 || deltaZ > 0.09)) {
                    //Log.w("CHECK","IN LOOP");
                    //if(System.currentTimeMillis()-t1>10000)
                    onBody = true;
                    Toast.makeText(con,"ONBODY TRUE", Toast.LENGTH_SHORT).show();
                    t1 = System.currentTimeMillis();
                } else {
                    onBody = false;
                    Toast.makeText(con,"ONBODY FALSE", Toast.LENGTH_SHORT).show();
                }
            }






        }
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {

    }
}
