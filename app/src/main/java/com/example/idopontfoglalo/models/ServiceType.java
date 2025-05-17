package com.example.idopontfoglalo.models;

public class ServiceType {
    private int duration;
    private String name;
    private int price;

    public ServiceType() {}

    public ServiceType(int duration, String name, int price) {
        this.duration = duration;
        this.name = name;
        this.price = price;
    }

    public int getDuration() { return duration; }
    public void setDuration(int duration) { this.duration = duration; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public int getPrice() { return price; }
    public void setPrice(int price) { this.price = price; }
}