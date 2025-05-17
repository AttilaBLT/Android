package com.example.idopontfoglalo.models;

import com.google.firebase.Timestamp;

public class Appointment {
    private Timestamp date;
    private String deviceId;
    private String serviceId;
    private String status;
    private String userId;
    private String serviceName;

    public Appointment() {}

    public Appointment(Timestamp date, String deviceId, String serviceId, String status, String userId, String serviceName) {
        this.date = date;
        this.deviceId = deviceId;
        this.serviceId = serviceId;
        this.status = status;
        this.userId = userId;
        this.serviceName = serviceName;
    }

    public Timestamp getDate() { return date; }
    public void setDate(Timestamp date) { this.date = date; }

    public String getDeviceId() { return deviceId; }
    public void setDeviceId(String deviceId) { this.deviceId = deviceId; }

    public String getServiceId() { return serviceId; }
    public void setServiceId(String serviceId) { this.serviceId = serviceId; }

    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }

    public String getUserId() { return userId; }
    public void setUserId(String userId) { this.userId = userId; }

    public String getServiceName() { return serviceName; }
    public void setServiceName(String serviceName) { this.serviceName = serviceName; }
}