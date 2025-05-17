package com.example.idopontfoglalo.models;

public class Device {
    private String brand;
    private String id;
    private String model;
    private String type;

    public Device() {}

    public Device(String brand, String id, String model, String type) {
        this.brand = brand;
        this.id = id;
        this.model = model;
        this.type = type;
    }

    public String getBrand() { return brand; }
    public void setBrand(String brand) { this.brand = brand; }

    public String getId() { return id; }
    public void setId(String id) { this.id = id; }

    public String getModel() { return model; }
    public void setModel(String model) { this.model = model; }

    public String getType() { return type; }
    public void setType(String type) { this.type = type; }
}