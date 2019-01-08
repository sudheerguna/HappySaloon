package com.product.happysaloon.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class AvailableAppointments {
    @SerializedName("id")
    @Expose
    private String id;
    String startTime,endTime,serviceName,timeInMin;
    public AvailableAppointments(String id, String startTime, String endTime, String serviceName, String timeInMin) {
        this.id = id;
        this.startTime = startTime;
        this.endTime = endTime;
        this.serviceName = serviceName;
        this.timeInMin = timeInMin;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getStartTime() {
        return startTime;
    }

    public void setStartTime(String startTime) {
        this.startTime = startTime;
    }

    public String getEndTime() {
        return endTime;
    }

    public void setEndTime(String endTime) {
        this.endTime = endTime;
    }

    public String getServiceName() {
        return serviceName;
    }

    public void setServiceName(String serviceName) {
        this.serviceName = serviceName;
    }

    public String getTimeInMin() {
        return timeInMin;
    }

    public void setTimeInMin(String timeInMin) {
        this.timeInMin = timeInMin;
    }
}
