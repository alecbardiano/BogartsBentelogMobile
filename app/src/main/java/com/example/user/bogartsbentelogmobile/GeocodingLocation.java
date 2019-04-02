package com.example.user.bogartsbentelogmobile;

/**
 * Created by user on 4/2/2019.
 */


import android.content.Context;
import android.location.Address;
import android.location.Geocoder;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;

import com.google.firebase.firestore.GeoPoint;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

public class GeocodingLocation {

    private double latitu;
    private double longitu;
    public double getLatitu() {
        return latitu;
    }

    public void setLatitu(double latitu) {
        this.latitu = latitu;
    }

    public double getLongitu() {
        return longitu;
    }

    public void setLongitu(double longitu) {
        this.longitu = longitu;
    }


    private static final String TAG = "GeocodingLocation";

    public  void getAddressFromLocation(final String locationAddress, final Context context, final Handler handler) {
       final Locale locale = new Locale("tl", "PH");
        Thread thread = new Thread() {
            @Override
            public void run() {
                Geocoder geocoder = new Geocoder(context, locale);
                String result = null;
                String resultlat = null;
                String resultlong = null;

                try {
                    List <Address> addressList = geocoder.getFromLocationName(locationAddress, 1);
                    if (addressList != null && addressList.size() > 0) {
                        Address address = addressList.get(0);
                        StringBuilder sb = new StringBuilder();
                        StringBuilder lat = new StringBuilder();
                        StringBuilder longit = new StringBuilder();
                        sb.append(address.getLatitude()).append("\n");
                        lat.append(address.getLatitude()).append("\n");
                        sb.append(address.getLongitude()).append("\n");
                        longit.append(address.getLongitude()).append("\n");
                        result = sb.toString();
                        resultlat = lat.toString();
                        resultlong = longit.toString();
                    }
                } catch (IOException e) {
                    Log.e(TAG, "Unable to connect to Geocoder", e);
                } finally {
                    Message message = Message.obtain();
                    message.setTarget(handler);
                    if (result != null) {
                        message.what = 1;
                        Bundle bundle = new Bundle();
                        result = "Address: " + locationAddress +
                                "\n\nLatitude and Longitude :\n" + result;
                        resultlat = resultlat;
                        resultlong = resultlong;
                        bundle.putString("address", result);
                        bundle.putString("latitude", resultlat);
                        bundle.putString("longitude", resultlong);
                        message.setData(bundle);
                    } else {
                        message.what = 1;
                        Bundle bundle = new Bundle();
                        result = "Address: " + locationAddress +
                                "\n Unable to get Latitude and Longitude for this address location.";
                        bundle.putString("address", result);
                        message.setData(bundle);
                    }
                    message.sendToTarget();
                }
            }
        };
        thread.start();
    }
}