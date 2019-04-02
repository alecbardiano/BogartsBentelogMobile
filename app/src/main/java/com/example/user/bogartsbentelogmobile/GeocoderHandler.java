package com.example.user.bogartsbentelogmobile;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;

/**
 * Created by user on 4/2/2019.
 */

public class GeocoderHandler extends Handler {
    @Override
    public void handleMessage(Message message) {
        String locationAddress;
        switch (message.what) {
            case 1:
                Bundle bundle = message.getData();
                locationAddress = bundle.getString("address");
                break;
            default:
                locationAddress = null;
        }
//        latLongTV.setText(locationAddress);
        Log.d("GEOCODER", locationAddress);
    }
}