package com.zillious.corporate_website.portal.entity.utility;

import javax.persistence.AttributeConverter;

import com.zillious.corporate_website.portal.ui.UserStatus;

public class StatusSerializer implements AttributeConverter<UserStatus, String> {

    @Override
    public String convertToDatabaseColumn(UserStatus statusCode) {
        if (statusCode == null) {
            return null;
        }
        return statusCode.getCode();
    }

    @Override
    public UserStatus convertToEntityAttribute(String userStatus) {
        return UserStatus.getStatusByCode(userStatus);
    }

}
