package com.product.happysaloon.model;

public class EmpNames {
    String id,empname;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getEmpname() {
        return empname;
    }

    public void setEmpname(String empname) {
        this.empname = empname;
    }

    public EmpNames(String id, String empName) {
        this.id = id;
        this.empname = empName;
    }
}
