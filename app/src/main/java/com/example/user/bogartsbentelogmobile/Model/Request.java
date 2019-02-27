package com.example.user.bogartsbentelogmobile.Model;

import com.example.user.bogartsbentelogmobile.RecyclerCartHolder;

import java.util.Date;

/**
 * Created by user on 2/27/2019.
 */

public class Request {
    private String Name, Address, status ,total;
    private Date dateOfOrder;

    public Request(String name, String address, String status, String total) {
        this.Name = name;
        this.Address = address;
        this.status = status;
        this.total = total;
        this.dateOfOrder = dateOfOrder;
    }

    public Request(){

    }

    public String getTotal() {
        return total;
    }

    public void setTotal(String total) {
        this.total = total;
    }

    public String getName() {
        return Name;
    }

    public void setName(String name) {
        this.Name = name;
    }

    public String getAddress() {
        return Address;
    }

    public void setAddress(String address) {
        this.Address = address;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public Date getDateOfOrder() {
        return dateOfOrder;
    }

    public void setDateOfOrder(Date dateOfOrder) {
        this.dateOfOrder = dateOfOrder;
    }
}
