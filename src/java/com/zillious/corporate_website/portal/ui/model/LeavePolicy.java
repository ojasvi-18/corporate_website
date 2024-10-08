package com.zillious.corporate_website.portal.ui.model;

import javax.persistence.Column;
import javax.persistence.Convert;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import org.hibernate.annotations.Type;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.zillious.corporate_website.portal.entity.utility.BooleanToStringConvertor;
import com.zillious.corporate_website.portal.entity.utility.ListContactInfoConverter;

/**
 * @author satyam.mittal
 *
 */
@Entity
@Table(name = "leave_policy")
public class LeavePolicy implements DBObject {

    /**
     * 
     */
    private static final long serialVersionUID = 1906809412134206227L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private int               m_id;

    @Column(name = "name", nullable = true)
    private String            m_name;

    @Column(name = "days", nullable = true)
    private int               m_days;

    @Column(name = "additional_comments", nullable = true)
    private String               m_additionalComments;

    @Convert(converter=BooleanToStringConvertor.class)
    @Column(name = "isSandwich", nullable = false)
    private boolean              m_isSandwich;

    @Column(name = "is_default")
    @Type(type = "yes_no")
    private boolean           m_isDefault;

    public int getId() {
        return m_id;
    }

    public String getAdditionalComments() {
        return m_additionalComments;
    }

    public void setAdditionalComments(String additionalComments) {
        this.m_additionalComments = additionalComments;
    }

    public boolean getIsSandwich() {
        return m_isSandwich;
    }

    public void setIsSandwich(boolean isSandwich) {
        this.m_isSandwich = isSandwich;
    }

    public void setId(int holidayId) {
        m_id = holidayId;
    }

    public int getDays() {
        return m_days;
    }

    public void setDays(int i) {
        m_days = i;
    }

    public JsonElement convertToJson() {
        JsonObject leaveTypeJsonObject = new JsonObject();
        leaveTypeJsonObject.addProperty("id", getId());
        leaveTypeJsonObject.addProperty("name", getName());
        leaveTypeJsonObject.addProperty("days", getDays());
        leaveTypeJsonObject.addProperty("additionalComments", getAdditionalComments());
        leaveTypeJsonObject.addProperty("isSandwich", getIsSandwich());
        return leaveTypeJsonObject;
    }

    /**
     * @return the name
     */
    public String getName() {
        return m_name;
    }

    /**
     * @param name the name to set
     */
    public void setName(String name) {
        m_name = name;
    }

    public static LeavePolicy parseFromJson(JsonObject leaveTypeJson) {
        LeavePolicy leaveType = new LeavePolicy();
        leaveType.setDays(leaveTypeJson.get("days").getAsInt());
        leaveType.setName(leaveTypeJson.get("name").getAsString());
//        if(leaveTypeJson.get("additionalComments") != null){
//        leaveType.setAdditionalComments(leaveTypeJson.get("additionalComments").getAsString());
//        }
//        else{
//            leaveType.setAdditionalComments("");
//        }
        leaveType.setIsSandwich(leaveTypeJson.get("isSandwich").getAsBoolean());
        leaveType.setId(leaveTypeJson.get("id").getAsInt());
        return leaveType;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return true;
        }

        if (!(obj instanceof LeavePolicy)) {
            return false;
        }

        return getId() == ((LeavePolicy) obj).getId();
    }

    @Override
    public int hashCode() {
        return 31 * getId();
    }

    /**
     * @return the isDefault
     */
    public boolean isDefault() {
        return m_isDefault;
    }

    /**
     * @param isDefault the isDefault to set
     */
    public void setDefault(boolean isDefault) {
        m_isDefault = isDefault;
    }

}
