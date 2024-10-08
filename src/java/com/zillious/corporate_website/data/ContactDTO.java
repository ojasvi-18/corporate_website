package com.zillious.corporate_website.data;

import com.zillious.corporate_website.ui.resources.QueryTypes;

public class ContactDTO {

    private String     m_email;
    private String     m_name;
    private String     m_phone;
    private String     m_message;
    private QueryTypes m_type;
    private String     m_clientIP;

    public ContactDTO(String emailID, String name, String phone, String message, QueryTypes type, String clientIP) {
        m_email = emailID;
        m_name = name;
        m_phone = phone;
        m_message = message;
        m_type = type;
        m_clientIP = clientIP;
    }

    public String getEmail() {
        return m_email;
    }

    public void setEmail(String email) {
        m_email = email;
    }

    public String getName() {
        return m_name;
    }

    public void setName(String name) {
        m_name = name;
    }

    public String getPhone() {
        return m_phone;
    }

    public void setPhone(String phone) {
        m_phone = phone;
    }

    public String getMessage() {
        return m_message;
    }

    public void setMessage(String message) {
        m_message = message;
    }

    public QueryTypes getType() {
        return m_type;
    }

    public void setType(QueryTypes type) {
        m_type = type;
    }

    public String getClientIP() {
        return m_clientIP;
    }

    public void setClientIP(String clientIP) {
        m_clientIP = clientIP;
    }

}
