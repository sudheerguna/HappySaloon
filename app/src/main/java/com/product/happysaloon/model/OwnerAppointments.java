package com.product.happysaloon.model;

public class OwnerAppointments {
    String id, startTime, endTime, serviceName, customerName;

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

    public String getCustomerName() {
        return customerName;
    }

    public void setCustomerName(String customerName) {
        this.customerName = customerName;
    }

    public OwnerAppointments(String id, String startTime, String endTime, String serviceName, String customerName) {
        this.id = id;
        this.startTime = startTime;
        this.endTime = endTime;
        this.serviceName = serviceName;
        this.customerName = customerName;
    }
}
