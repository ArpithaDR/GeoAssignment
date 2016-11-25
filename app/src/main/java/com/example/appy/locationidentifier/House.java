package com.example.appy.locationidentifier;

/**
 * Created by appy on 9/28/16.
 */

public class House {

    private String desc;
    private String subject;
    private String email;
    private String address;
    private String startDate;
    private String endDate;
    private String phone;
    private int spots;
    private double price;
    private int thumbnail;
    public int houseId;

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getEndDate() {
        return endDate;
    }

    public void setEndDate(String endDate) {
        this.endDate = endDate;
    }

    public String getSubject() {
        return subject;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getStartDate() {
        return startDate;
    }

    public void setStartDate(String startDate) {
        this.startDate = startDate;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public int getSpots() {
        return spots;
    }

    public void setSpots(int spots) {
        this.spots = spots;
    }

    public House() {
    }

    public int getHouseId() {
        return houseId;
    }

    public void setHouseId(int houseId) {
        this.houseId = houseId;
    }

    public House(String name, double value, int thumbnail, int id) {
        this.desc = name;
        this.price = value;
        this.thumbnail = thumbnail;
        this.houseId = id;
    }

    public House(String desc, String subject, String email, String address, String startDate, String endDate, String phone, int spots, double price, int houseId, int thumbnail) {
        this.desc = desc;
        this.subject = subject;
        this.email = email;
        this.address = address;
        this.startDate = startDate;
        this.endDate = endDate;
        this.phone = phone;
        this.spots = spots;
        this.price = price;
        this.houseId = houseId;
        this.thumbnail = thumbnail;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }



    public int getThumbnail() {
        return thumbnail;
    }

    public void setThumbnail(int thumbnail) {
        this.thumbnail = thumbnail;
    }
}
