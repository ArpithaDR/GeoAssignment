package com.example.appy.locationidentifier;

/**
 * Created by appy on 9/28/16.
 */

public class House {

    private String name;
    private int price;
    private int thumbnail;

    public House() {
    }

    public House(String name, int value, int thumbnail) {
        this.name = name;
        this.price = value;
        this.thumbnail = thumbnail;
    }

    public int getPrice() {
        return price;
    }

    public void setPrice(int price) {
        this.price = price;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }



    public int getThumbnail() {
        return thumbnail;
    }

    public void setThumbnail(int thumbnail) {
        this.thumbnail = thumbnail;
    }
}
