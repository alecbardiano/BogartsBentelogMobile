package com.example.user.bogartsbentelogmobile.Common;

import com.example.user.bogartsbentelogmobile.Model.User;


/**
 * Created by user on 1/30/2019.
 */

public class Common {
    public static User currUser;

    public static final int LOCATION_REQUEST = 1000;
    public static final int GPS_REQUEST = 1001;

    public static String convertCodeToStatus(String code){
        if (code.equals("0"))
            return "Placed";
        else if(code.equals("1"))
            return "On the way";
        else
            return "Shipped";
    }
}
