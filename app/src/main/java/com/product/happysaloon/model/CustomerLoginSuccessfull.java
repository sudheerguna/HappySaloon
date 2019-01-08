package com.product.happysaloon.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class CustomerLoginSuccessfull {
    @SerializedName("UserName")
    @Expose
    private String userName;
    @SerializedName("cusid")
    @Expose
    private String cusid;
    @SerializedName("ShopId")
    @Expose
    private Object shopId;

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getCusid() {
        return cusid;
    }

    public void setCusid(String cusid) {
        this.cusid = cusid;
    }

    public Object getShopId() {
        return shopId;
    }

    public void setShopId(Object shopId) {
        this.shopId = shopId;
    }

    @SerializedName("current_field")
    @Expose
    private Object currentField;
    @SerializedName("field_count")
    @Expose
    private Object fieldCount;
    @SerializedName("lengths")
    @Expose
    private Object lengths;
    @SerializedName("num_rows")
    @Expose
    private Object numRows;
    @SerializedName("type")
    @Expose
    private Object type;

    public Object getCurrentField() {
        return currentField;
    }

    public void setCurrentField(Object currentField) {
        this.currentField = currentField;
    }

    public Object getFieldCount() {
        return fieldCount;
    }

    public void setFieldCount(Object fieldCount) {
        this.fieldCount = fieldCount;
    }

    public Object getLengths() {
        return lengths;
    }

    public void setLengths(Object lengths) {
        this.lengths = lengths;
    }

    public Object getNumRows() {
        return numRows;
    }

    public void setNumRows(Object numRows) {
        this.numRows = numRows;
    }

    public Object getType() {
        return type;
    }

    public void setType(Object type) {
        this.type = type;
    }

}
