package com.zillious.corporate_website.portal.ui.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "employee")
public class Employee implements DBObject {
    private static final long serialVersionUID = 3926488423913532891L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false, unique = true, length = 11)
    private int               id;
    @Column(name = "name", nullable = false)
    private String            name;
    @Column(name = "email", nullable = false)
    private String            email;
    @Column(name = "mobile", nullable = false)
    private String            mobile;
    @Column(name = "employeeCode", nullable = false)
    private String            employeeCode;
    @Column(name = "address", nullable = false)
    private String            address;
    @Column(name = "city", nullable = false)
    private String            city;
    @Column(name = "state", nullable = false)
    private String            state;
    @Column(name = "country", nullable = false)
    private String            country;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public String getEmployeeCode() {
        return employeeCode;
    }

    public void setEmployeeCode(String employeeCode) {
        this.employeeCode = employeeCode;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

}
