package com.zillious.corporate_website.portal.ui;

/**
 * @author nishant.gupta
 *
 */
public enum EventType {
    EVENT("E"),

    HOLIDAY("H"), ;

    private String m_code;

    EventType(String code) {
        m_code = code;
    }

    /**
     * @return the code
     */
    public String getCode() {
        return m_code;
    }

    /**
     * @param code the code to set
     */
    public void setCode(String code) {
        m_code = code;
    }

    public static EventType getTypeByCode(String serializedCode) {
        if (serializedCode == null) {
            return null;
        }

        for (EventType type : EventType.values()) {
            if (serializedCode.equals(type.getCode())) {
                return type;
            }
        }

        return null;
    }

    public static EventType deserialize(String asString) {
        if (asString == null || (asString = asString.trim()).isEmpty()) {
            return null;
        }

        for (EventType type : EventType.values()) {
            if (type.getCode().equals(asString)) {
                return type;
            }
        }
        
        return null;
    }

}
