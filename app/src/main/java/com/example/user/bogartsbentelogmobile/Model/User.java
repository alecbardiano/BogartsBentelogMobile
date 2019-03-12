package com.example.user.bogartsbentelogmobile.Model;

import android.location.Address;

import java.util.Date;

/**
 * Created by user on 1/27/2019.
 */

public class User {
    private String email;
    private String lastName;
    private String firstName;
    private String middleInit;
    private String contactNumber;
    private String address;
    private String ID;
    private boolean isStaff;

    public User(){

    }

    public User(String email, String lastName, String firstName, String middleInit, String contactNumber, String address, String ID, boolean isStaff) {
        this.email = email;
        this.lastName = lastName;
        this.firstName = firstName;
        this.middleInit = middleInit;
        this.contactNumber = contactNumber;
        this.address = address;
        this.ID = ID;
        this.isStaff = isStaff;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getMiddleInit() {
        return middleInit;
    }

    public void setMiddleInit(String middleInit) {
        this.middleInit = middleInit;
    }

    public String getContactNumber() {
        return contactNumber;
    }

    public void setContactNumber(String contactNumber) {
        this.contactNumber = contactNumber;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getID() {
        return ID;
    }

    public void setID(String ID) {
        this.ID = ID;
    }

    public boolean isStaff() {
        return isStaff;
    }

    public void setStaff(boolean staff) {
        isStaff = staff;
    }
}
