package com.zillious.corporate_website.portal.entity.utility;

import javax.persistence.AttributeConverter;

import com.zillious.corporate_website.portal.ui.LeaveRequestStatus;

public class LeaveRequestStatusSerializer implements AttributeConverter<LeaveRequestStatus, String> {

    @Override
    public String convertToDatabaseColumn(LeaveRequestStatus statusCode) {
        if (statusCode == null) {
            return null;
        }
        return statusCode.getCode();
    }

    @Override
    public LeaveRequestStatus convertToEntityAttribute(String status) {
        return LeaveRequestStatus.getStatusByCode(status);
    }

}
