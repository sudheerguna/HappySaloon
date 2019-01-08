package com.product.happysaloon.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class LoginResponse {

    @SerializedName("respond")
    @Expose
    private String respond;
    @SerializedName("Customer Login Successfull")
    @Expose
    private List<CustomerLoginSuccessfull> customerLoginSuccessfull = null;

    public List<CustomerLoginSuccessfull> getCustomerLoginSuccessfull() {
        return customerLoginSuccessfull;
    }

    public void setCustomerLoginSuccessfull(List<CustomerLoginSuccessfull> customerLoginSuccessfull) {
        this.customerLoginSuccessfull = customerLoginSuccessfull;
    }

    public List<OwnerLoginSuccessfull> getOwnerLoginSuccessfull() {
        return ownerLoginSuccessfull;
    }

    public void setOwnerLoginSuccessfull(List<OwnerLoginSuccessfull> ownerLoginSuccessfull) {
        this.ownerLoginSuccessfull = ownerLoginSuccessfull;
    }

    @SerializedName("Owner Login Successfull")

    @Expose
    private List<OwnerLoginSuccessfull> ownerLoginSuccessfull = null;
    @SerializedName("UserType")
    @Expose
    private String userType;

    public String getUserType() {
        return userType;
    }

    public void setUserType(String userType) {
        this.userType = userType;
    }


    @SerializedName("Login failed")
    @Expose
    private LoginFailed loginFailed;

    public String getRespond() {
        return respond;
    }

    public void setRespond(String respond) {
        this.respond = respond;
    }

    public LoginFailed getLoginFailed() {
        return loginFailed;
    }

    public void setLoginFailed(LoginFailed loginFailed) {
        this.loginFailed = loginFailed;
    }
}
