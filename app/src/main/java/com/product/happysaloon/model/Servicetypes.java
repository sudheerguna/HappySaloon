package com.product.happysaloon.model;

public class Servicetypes {
    String id,serviceName,timeInMin;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
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

    public Servicetypes(String id, String serviceName, String timeInMin) {
        this.id = id;
        this.serviceName = serviceName;
        this.timeInMin = timeInMin;
    }
}
