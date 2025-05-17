package com.example.idopontfoglalo.models;

public class AppointmentDisplay {
    public String date, status, serviceName, deviceName;
    public AppointmentDisplay(String date, String status, String serviceName, String deviceName) {
        this.date = date;
        this.status = status;
        this.serviceName = serviceName;
        this.deviceName = deviceName;
    }
}