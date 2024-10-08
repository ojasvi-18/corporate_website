package com.zillious.corporate_website.data;

import java.util.ArrayList;
import java.util.List;

/**
 * @author nishant.gupta
 *
 */
public class PerDayAttendanceDTO {
    private List<AttendanceFromDeviceDTO> m_attendanceEntries;
    private AttendanceEnum                m_state;

    public PerDayAttendanceDTO() {
        m_attendanceEntries = new ArrayList<AttendanceFromDeviceDTO>();
        m_state = AttendanceEnum.ABSENT;
    }

    /**
     * @return the attendanceEntries
     */
    public List<AttendanceFromDeviceDTO> getAttendanceEntries() {
        return m_attendanceEntries;
    }

    /**
     * @return the state
     */
    public AttendanceEnum getState() {
        return m_state;
    }

    public void add(AttendanceFromDeviceDTO attendanceFromDeviceDTO) {
        m_attendanceEntries.add(attendanceFromDeviceDTO);
    }

    /**
     * @param state the state to set
     */
    public void setState(AttendanceEnum state) {
        m_state = state;
    }

}
