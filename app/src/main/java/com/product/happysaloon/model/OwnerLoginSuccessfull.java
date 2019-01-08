package com.product.happysaloon.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class OwnerLoginSuccessfull {

    @SerializedName("UserName")
    @Expose
    private String userName;
    @SerializedName("ShooId")
    @Expose
    private String shooId;

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getShooId() {
        return shooId;
    }

    public void setShooId(String shooId) {
        this.shooId = shooId;
    }

}

