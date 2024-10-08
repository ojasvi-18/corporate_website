package com.zillious.corporate_website.data;

/**
 * @author nishant.gupta
 *
 */
public enum AttendanceEnum {

    PRESENT("#69bf56"), ABSENT("#e57650"), HALFDAY("#f2ed71"), APPROVED("#a7bf56");

    private String m_eventBackgroundColor;

    private AttendanceEnum(String eventBackgroundColorCode) {
        m_eventBackgroundColor = eventBackgroundColorCode;
    }

    public String getEventRenderingColor() {
        return m_eventBackgroundColor;
    }

}
