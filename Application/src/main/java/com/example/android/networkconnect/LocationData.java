package com.example.android.networkconnect;

/**
 * Created by abhishek on 8/1/2015.
 */
public class LocationData {
    int status;

    public void setStatus(int status) {
        this.status = status;
    }

    private static LocationData singleton= new LocationData();
    private LocationData(){

    }

    public static LocationData getInstance(){
        return singleton;
    }
}

