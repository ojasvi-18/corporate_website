package com.zillious.corporate_website.portal.ui.dto;

import java.util.ArrayList;
import java.util.List;

import com.zillious.corporate_website.data.AttendanceEnum;
import com.zillious.corporate_website.portal.ui.model.AttendanceRecord;

/**
 * @author nishant.gupta
 *
 */
public class PerDayAttendanceDTO {
    private List<AttendanceRecord> m_attendanceEntries;
    private AttendanceEnum         m_state;
    private int                    m_hours   = 0;
    private int                    m_minutes = 0;

    public PerDayAttendanceDTO() {
        m_attendanceEntries = new ArrayList<AttendanceRecord>();
        m_state = AttendanceEnum.ABSENT;
    }

    public PerDayAttendanceDTO(AttendanceEnum state) {
        m_attendanceEntries = new ArrayList<AttendanceRecord>();
        m_state = state == null ? AttendanceEnum.ABSENT : state;
    }

    /**
     * @return the state
     */
    public AttendanceEnum getState() {
        return m_state;
    }

    public void add(AttendanceRecord attendanceFromDeviceDTO) {
        m_attendanceEntries.add(attendanceFromDeviceDTO);
    }

    /**
     * @param state the state to set
     */
    public void setState(AttendanceEnum state) {
        m_state = state;
    }

    /**
     * @return the attendanceEntries
     */
    public List<AttendanceRecord> getAttendanceEntries() {
        return m_attendanceEntries;
    }

    /**
     * @param attendanceEntries the attendanceEntries to set
     */
    public void setAttendanceEntries(List<AttendanceRecord> attendanceEntries) {
        m_attendanceEntries = attendanceEntries;
    }

    public int getHours() {
        return m_hours;
    }

    public void setHours(int hours) {
        m_hours = hours;
    }

    public int getMinutes() {
        return m_minutes;
    }

    public void setMinutes(int minutes) {
        m_minutes = minutes;
    }

}
