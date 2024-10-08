package com.zillious.corporate_website.portal.entity.utility;

import javax.persistence.AttributeConverter;

import com.zillious.corporate_website.portal.ui.UserRoles;

public class RoleSerializer implements AttributeConverter<UserRoles, String> {

    @Override
    public String convertToDatabaseColumn(UserRoles uRole) {
        if (uRole == null) {
            return null;
        }
        return uRole.serialize();
    }

    @Override
    public UserRoles convertToEntityAttribute(String serial) {
        return UserRoles.deserialize(serial);
    }

}
