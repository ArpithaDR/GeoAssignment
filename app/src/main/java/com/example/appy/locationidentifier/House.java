package com.example.appy.locationidentifier;

/**
 * Created by appy on 9/28/16.
 */

public class House {

    private String name;
    private double price;
    private int thumbnail;
    public int houseId;

    public House() {
    }

    public int getHouseId() {
        return houseId;
    }

    public void setHouseId(int houseId) {
        this.houseId = houseId;
    }

    public House(String name, double value, int thumbnail, int id) {
        this.name = name;
        this.price = value;
        this.thumbnail = thumbnail;
        this.houseId = id;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
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
