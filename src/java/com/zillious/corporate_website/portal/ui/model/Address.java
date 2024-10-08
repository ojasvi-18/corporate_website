package com.zillious.corporate_website.portal.ui.model;

import javax.persistence.Column;
import javax.persistence.Embeddable;

@Embeddable
public class Address {

    @Column(name = "pin", nullable = true)
    private String m_pincode;

    @Column(name = "state", nullable = true)
    private String m_state;

    @Column(name = "country", nullable = true)
    private String m_country;

    @Column(name = "HNoStreet", nullable = true)
    private String m_houseno;

    public String getPincode() {
        return m_pincode;
    }

    public void setPincode(String pincode) {
        m_pincode = pincode;
    }

    public String getState() {
        return m_state;
    }

    public void setState(String state) {
        m_state = state;
    }

    public String getCountry() {
        return m_country;
    }

    public void setCountry(String country) {
        m_country = country;
    }

    public String getHouseno() {
        return m_houseno;
    }

    public void setHouseno(String houseno) {
        m_houseno = houseno;
    }

}
