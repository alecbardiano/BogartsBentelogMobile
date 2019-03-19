package com.example.user.bogartsbentelogmobile.Model;

/**
 * Created by user on 2/27/2019.
 */

public class Store {

    private String nameOfBranch,storeID,address, contactNo;

    public Store (){

    }
    public Store(String nameOfBranch, String storeID, String address, String contactNo) {
        this.nameOfBranch = nameOfBranch;
        this.storeID = storeID;
        this.address = address;
        this.contactNo = contactNo;
    }

    public String getNameOfBranch() {
        return nameOfBranch;
    }

    public void setNameOfBranch(String nameOfBranch) {
        this.nameOfBranch = nameOfBranch;
    }

    public String getStoreID() {
        return storeID;
    }

    public void setStoreID(String storeID) {
        this.storeID = storeID;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getContactNo() {
        return contactNo;
    }

    public void setContactNo(String contactno) {
        this.contactNo = contactno;
    }
}
