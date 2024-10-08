package com.zillious.corporate_website.portal.entity.utility;

import javax.persistence.AttributeConverter;

import com.zillious.corporate_website.portal.ui.EventType;

/**
 * @author nishant.gupta
 *
 */
public class EventTypeSerializer implements AttributeConverter<EventType, String> {

    @Override
    public String convertToDatabaseColumn(EventType event) {
        if (event == null) {
            return null;
        }
        return event.getCode();
    }

    @Override
    public EventType convertToEntityAttribute(String serializedCode) {
        return EventType.getTypeByCode(serializedCode);
    }

}
