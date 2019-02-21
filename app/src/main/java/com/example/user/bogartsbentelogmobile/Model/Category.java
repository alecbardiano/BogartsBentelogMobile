package com.example.user.bogartsbentelogmobile.Model;

/**
 * Created by user on 1/31/2019.
 */

public class Category {

    private String Name, Image;
    private int Priority;

    public Category () {

    }


    public Category(String name, String image, int priority) {
        Name = name;
        Image = image;
        Priority = priority;
    }



    public String getName() {
        return Name;
    }

    public void setName(String name) {
        Name = name;
    }

    public String getImage() {
        return Image;
    }

    public void setImage(String image) {
        Image = image;
    }

    public int getPriority() {
        return Priority;
    }

    public void setPriority(int priority) {
        Priority = priority;
    }
}
