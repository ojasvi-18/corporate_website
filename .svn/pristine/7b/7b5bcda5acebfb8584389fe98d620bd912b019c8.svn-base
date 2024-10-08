package com.zillious.corporate_website.portal.entity.utility;

import javax.persistence.AttributeConverter;

import com.zillious.corporate_website.portal.ui.Gender;

/**
 * @author satyam.mittal
 *
 */
public class GenderSerializer implements AttributeConverter<Gender, String> {

    @Override
    public String convertToDatabaseColumn(Gender gender) {
        if (gender == null) {
            return null;
        }

        return gender.getCode();
    }

    @Override
    public Gender convertToEntityAttribute(String serial) {
        return Gender.getByCode(serial);
    }

}
