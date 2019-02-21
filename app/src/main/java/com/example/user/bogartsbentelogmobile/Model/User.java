package com.example.user.bogartsbentelogmobile.Model;

import android.location.Address;

import java.util.Date;

/**
 * Created by user on 1/27/2019.
 */

public class User {
    private String Email;
    private String lastName;
    private String firstName;
    private String middleInit;
    private String contactNumber;
    private String Address;
    private String ID;

    public User(){

    }

    public User(String email, String lastname, String firstname, String middleinit, String cNumber, String address) {
        Email = email;
        lastName = lastname;
        firstName = firstname;
        middleInit = middleinit;
        contactNumber = cNumber;
        Address = address;

    }

    public String getEmail() {
        return Email;
    }

    public void setEmail(String email) {
        Email = email;
    }

    public String getID() {
        return ID;
    }

    public void setID(String ID) {
        this.ID = ID;
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
        return Address;
    }

    public void setAddress(String address) {
        Address = address;
    }
}
