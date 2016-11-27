package com.example.appy.locationidentifier;

/**
 * Created by appy on 11/27/16.
 */

public class PublicPlaces {
    public int id;
    public String name;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public double getDistance() {
        return distance;
    }

    public void setDistance(double distance) {
        this.distance = distance;
    }

    public String address;
    public String city;
    public double distance;

    public PublicPlaces(int id, String name, String address, String city, double distance) {
        this.id = id;
        this.name = name;
        this.address = address;
        this.city = city;
        this.distance = distance;
    }
}
