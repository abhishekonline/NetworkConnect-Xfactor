package com.example.android.networkconnect;

import android.app.Activity;
import android.app.AlertDialog;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Set;

/**
 * Created by April on 7/29/15.
 */
public class BluetoothActivity {
    private BluetoothAdapter bluetoothAdapter;
    private final static int REQUEST_ENABLE_BT = 1;
    private HashMap<String, Boolean> knownBtDevicesMap = new HashMap<String, Boolean>();
    public String pairedDeviceString;



    Context c;
    Activity a;
    public BluetoothActivity(Context c, Activity myActivity){
        this.c =c;
        this.a = myActivity;
        bluetoothAdapter = BluetoothAdapter.getDefaultAdapter();

        if (bluetoothAdapter == null) {
            Toast.makeText(c, "No Bluetooth",
                    Toast.LENGTH_LONG).show();
        }
        else if(!bluetoothAdapter.isEnabled()) {
            Intent enableBtIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
            //  c.startActivityForResult(enableBtIntent, REQUEST_ENABLE_BT);
            Toast.makeText(c, "Enable your BluetoothActivity.",Toast.LENGTH_LONG).show();
            findBluetooth();
        } else {
            Toast.makeText(c, "Bluetooth is already on.",
                    Toast.LENGTH_LONG).show();
            findBluetooth();
        }

    }

    private void findBluetooth (){
        // Find paired devices
        Set<BluetoothDevice> pairedDevices = bluetoothAdapter.getBondedDevices();
        // If there are paired devices
        if (pairedDevices.size() > 0) {
            // Loop through paired devices
            ArrayList<String> mArrayPairedAdapter = new ArrayList<String>();
            for (BluetoothDevice device : pairedDevices) {
                // Add the name and address to an array adapter to show in a ListView
                mArrayPairedAdapter.add(device.getName() + "\n" + device.getAddress());

                for (String s : mArrayPairedAdapter) {
                    pairedDeviceString += s + "\t";
                }

                if (!isKnownDevices(device.getAddress())) {
                    openAlert(device.getAddress(), device.getName());
                }
                pairedDeviceString = mArrayPairedAdapter.toString();

            }
        } else {
            pairedDeviceString = "No paired devices.";
        }
        Toast.makeText(c, "Your phone is currently paired with: "+pairedDeviceString,
                Toast.LENGTH_LONG).show();
    }

    public void openAlert(final String address, final String name) {

        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(a);
        // set title
        alertDialogBuilder.setTitle("Bluetooth");

        // set dialog message
        alertDialogBuilder
                .setMessage("Is "+name+" a trusted device?")
                .setCancelable(false)
                .setPositiveButton("Yes",new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog,int id) {
                        // if this button is clicked, close

                        knownBtDevicesMap.put(address, true);
                        Toast.makeText(c, name + "is a trusted Device.",
                                Toast.LENGTH_LONG).show();
                        // current activity
                        dialog.dismiss();
                    }
                })
                .setNegativeButton("No",new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog,int id) {
                        // if this button is clicked, just close
                        // the dialog box and do nothing
                        knownBtDevicesMap.put(address, true);
                        Toast.makeText(c, name + "is a untrusted Device.",
                                Toast.LENGTH_LONG).show();
                        dialog.dismiss();
                    }
                });

        // create alert dialog
        AlertDialog alertDialog = alertDialogBuilder.create();

        // show it
        alertDialog.show();
    }

    private boolean isKnownDevices(String address) {
        if (knownBtDevicesMap == null || knownBtDevicesMap.isEmpty()) {
            Toast.makeText(c,"Map is empty" + knownBtDevicesMap.size(), Toast.LENGTH_LONG).show();
            return false;
        }
        else {
            Toast.makeText(c,"Map is not empty" + knownBtDevicesMap.size(), Toast.LENGTH_LONG).show();
            return knownBtDevicesMap.containsKey(address);
        }
    }
}
