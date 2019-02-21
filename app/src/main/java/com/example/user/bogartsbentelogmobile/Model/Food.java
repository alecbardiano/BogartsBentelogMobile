package com.example.user.bogartsbentelogmobile.Model;

/**
 * Created by user on 2/8/2019.
 */

public class Food {
    private  String Name, Description, Price, Image;

    public Food(String name, String description, String price, String image) {
        Name = name;
        Description = description;
        Price = price;
        Image = image;
    }

    public Food(){


    }

    public String getName() {
        return Name;
    }

    public void setName(String name) {
        Name = name;
    }

    public String getDescription() {
        return Description;
    }

    public void setDescription(String description) {
        Description = description;
    }

    public String getPrice() {
        return Price;
    }

    public void setPrice(String price) {
        Price = price;
    }

    public String getImage() {
        return Image;
    }

    public void setImage(String image) {
        Image = image;
    }
}
