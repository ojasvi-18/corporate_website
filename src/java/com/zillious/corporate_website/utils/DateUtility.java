package com.zillious.corporate_website.utils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.TimeZone;
import java.util.concurrent.TimeUnit;

import org.apache.log4j.Logger;

public class DateUtility {
    public static long    MILIS_IN_DAY            = 24 * 60 * 60 * 1000;
    public static long    APPROX_MILIS_IN_MONTH   = MILIS_IN_DAY * 30;
    public static long    APPROX_MILIS_IN_YEAR    = MILIS_IN_DAY * 365;

    public static int     SECONDS_IN_DAY          = 24 * 60 * 60;
    public static int     APPROX_SECONDS_IN_MONTH = SECONDS_IN_DAY * 30;
    public static int     APPROX_SECONDS_IN_YEAR  = SECONDS_IN_DAY * 365;
    public static long    MILISECONDS_IN_HOUR     = 1 * 60 * 60 * 1000;
    public static long    MILISECONDS_IN_MINUTE   = 1 * 60 * 1000;

    private static Logger s_logger                = Logger.getLogger(DateUtility.class);

    public static String getDateInHtmlExpiresFormat(Date dt, boolean forceGMT) {
        SimpleDateFormat sdf = new SimpleDateFormat((forceGMT) ? "EEE, dd-MMM-yyyy HH:mm:ss 'GMT'"
                : "EEE, dd-MMM-yyyy HH:mm:ss z");
        return sdf.format(dt);
    }

    public static String getDateInDDMMYYYYWithUnsetOnNull(Calendar dt) {
        if (dt == null) {
            return "Unset";
        }
        return getDateInDDMMYYYYWithUnsetOnNull(dt.getTime());
    }

    public static String getDateInDDMMYYYYWithCurrentDateOnNull(Calendar dt) {

        return getDateInDDMMYYYYWithUnsetOnNull(dt == null ? getCurrentDate() : dt.getTime());
    }

    public static List<String> getDateInDDMMYYYYFormCal(List<Calendar> dt) {
        List<String> dates = null;
        if (dt == null) {
            return dates;
        }
        dates = new ArrayList<String>();
        for (Calendar cal : dt) {
            dates.add(DateUtility.getDateInDDMMYYYY(cal.getTime()));
        }
        return dates;
    }

    public static Date getDateWithZeroTimefields(Date dt) {
        Calendar cal = getCurrentDateCalendar();
        cal.setTime(dt);
        cal.set(Calendar.HOUR_OF_DAY, 0);
        cal.set(Calendar.MINUTE, 0);
        cal.set(Calendar.SECOND, 0);
        cal.set(Calendar.MILLISECOND, 0);
        return cal.getTime();
    }

    public static String getDateInDDMMYYYYWithUnsetOnNull(Date dt) {
        if (dt == null) {
            return "Unset";
        }
        return getDateInDDMMYYYY(dt);
    }

    public static String getDateInDDMMYYYYWithEmptyIfError(Date dt) {
        try {
            return getDateInDDMMYYYY(dt);
        } catch (Exception e) {
            return "";
        }
    }

    public static String getDateInDDMMYYWithUnsetOnNull(Calendar dt) {
        if (dt == null) {
            return "Unset";
        }
        return getDateInDDMMYYWithUnsetOnNull(dt.getTime());
    }

    public static String getDateInDDMMYYWithUnsetOnNull(Date dt) {
        if (dt == null) {
            return "Unset";
        }
        return getDateInDDMMYY(dt);
    }

    public static String getDateInMMDDYYYYWithUnsetOnNull(Date dt) {
        if (dt == null) {
            return "Unset";
        }
        return getDateInMMDDYYYY(dt);
    }

    public static String getDateInDDMMMWithUnsetOnNull(Date dt) {
        if (dt == null) {
            return "Unset";
        }
        return getDateInDDMMM(dt);
    }

    public static String getDateInDDMMYYYY(Date dt) {
        if (dt == null) {
            return null;
        }
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
        return sdf.format(dt);
    }

    public static String getDateInDDMMYY(Date dt) {
        SimpleDateFormat sdf = new SimpleDateFormat("ddMMyy");
        return sdf.format(dt);
    }

    public static String getDateInMMDDYYYY(Date dt) {
        SimpleDateFormat sdf = new SimpleDateFormat("MM/dd/yyyy");
        return sdf.format(dt);
    }

    public static String getDateInMMDDYYYYDash(Date dt) {
        if (dt == null)
            return null;
        try {
            SimpleDateFormat sdf = new SimpleDateFormat("MM-dd-yyyy");
            return sdf.format(dt);
        } catch (Exception e) {
            s_logger.error("Error while getting date in mm-dd-yyyy format", e);
            return null;
        }
    }

    public static String getDateInYYMMDD(Date dt) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyMMdd");
        return sdf.format(dt);
    }

    public static String getDateInDDMMYYYYDash(Date dt) {
        try {
            SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy");
            return sdf.format(dt);
        } catch (Exception e) {
            s_logger.error("Error while getting the date in required format", e);
            return null;
        }
    }

    public static String getDateInDDMMDash(Date dt) {
        SimpleDateFormat sdf = new SimpleDateFormat("dd-MM");
        return sdf.format(dt);
    }

    public static String getDateInYYYYMMDDDash(Date dt) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        return sdf.format(dt);
    }

    public static String getDateInDDMMMYYYYDash(Date dt) {
        SimpleDateFormat sdf = new SimpleDateFormat("dd-MMM-yyyy");
        return sdf.format(dt);
    }

    public static String getDateInDDMMMYY(Date dt) {
        SimpleDateFormat sdf = new SimpleDateFormat("ddMMMyy");
        return sdf.format(dt);
    }

    public static String getDateInDDMMMNoSeparator(Date dt) {
        SimpleDateFormat sdf = new SimpleDateFormat("ddMMM");
        return sdf.format(dt);
    }

    public static String getDateInDDMMMYYYY(Date dt) {
        SimpleDateFormat sdf = new SimpleDateFormat("ddMMMyyyy");
        return sdf.format(dt);
    }

    public static String getDateInDDMMMYYYYDashSeparator(Date dt) {
        SimpleDateFormat sdf = new SimpleDateFormat("dd-MMM-yyyy");
        return sdf.format(dt);
    }

    public static String getDateInDDMMMYYHHMMDashSeparator(Date dt) {
        SimpleDateFormat sdf = new SimpleDateFormat("dd-MMM-yy HH:mm");
        return sdf.format(dt);
    }

    public static String getDateInDDMMMYYDashSeparator(Date dt) {
        SimpleDateFormat sdf = new SimpleDateFormat("dd-MMM-yy");
        return sdf.format(dt);
    }

    public static String getDateInDDMMM(Date dt) {
        return getDateInDDMMM(dt, false);
    }

    public static String getDateInDDMMM(Date dt, boolean isLocale) {
        SimpleDateFormat sdf;
        // if (isLocale) {
        // sdf = new SimpleDateFormat("dd-MMM", I18NBean.getSelectedLocale());
        // } else {
        sdf = new SimpleDateFormat("dd-MMM");
        // }
        return sdf.format(dt);
    }

    public static String getDateInHHMMNoSeparator(Date dt) {
        SimpleDateFormat sdf = new SimpleDateFormat("HHmm");
        return sdf.format(dt);
    }

    public static String getDateInHHMM(Date dt) {
        return getDateInHHMM(dt, false);
    }

    public static String getDateInHHMM(Date dt, boolean isLocale) {
        SimpleDateFormat sdf;
        // if (isLocale) {
        // sdf = new SimpleDateFormat("HH:mm", I18NBean.getSelectedLocale());
        // } else {
        sdf = new SimpleDateFormat("HH:mm");
        // }
        return sdf.format(dt);
    }

    public static String getDateInHHMMAMPM(Date dt) {
        return getDateInHHMMAMPM(dt, false);
    }

    public static String getDateInHHMMAMPM(Date dt, boolean isLocale) {
        SimpleDateFormat sdf;
        // if (isLocale) {
        // sdf = new SimpleDateFormat("hh:mm a", I18NBean.getSelectedLocale());
        // } else {
        sdf = new SimpleDateFormat("hh:mm a");
        // }
        return sdf.format(dt);
    }

    public static String getDateInHHMMSS(Date dt) {
        SimpleDateFormat sdf = new SimpleDateFormat("HH:mm:ss");
        return sdf.format(dt);
    }

    public static String getDateInEEEDDMM(Date dt) {
        return getDateInEEEDDMM(dt, false);
    }

    public static String getDateInEEEDDMM(Date dt, boolean isLocale) {
        SimpleDateFormat sdf;
        // if (isLocale) {
        // sdf = new SimpleDateFormat("EEE, dd-MMM",
        // I18NBean.getSelectedLocale());
        // } else {
        sdf = new SimpleDateFormat("EEE, dd-MMM");
        // }
        return sdf.format(dt);
    }

    public static String getDateInEEEDDMMYYYY(Date dt) {
        SimpleDateFormat sdf = new SimpleDateFormat("EEE, dd-MMM yyyy");
        return sdf.format(dt);
    }

    public static String getDateInEEE(Date dt) {
        SimpleDateFormat sdf = new SimpleDateFormat("EEE");
        return sdf.format(dt);
    }

    public static String getDateInhhmmEEEDDMM(Date dt) {
        return getDateInhhmmEEEDDMM(dt, false);
    }

    public static String getDateInhhmmEEEDDMM(Date dt, boolean isLocale) {
        SimpleDateFormat sdf;
        // if (isLocale) {
        // sdf = new SimpleDateFormat("HH:mm, EEE dd-MMM",
        // I18NBean.getSelectedLocale());
        // } else {
        sdf = new SimpleDateFormat("HH:mm, EEE dd-MMM");
        // }
        return sdf.format(dt);
    }

    public static String getDateInhhmmEEEDDMMYYYY(Date dt) {
        SimpleDateFormat sdf = new SimpleDateFormat("HH:mm, EEE dd-MMM-yyyy");
        return sdf.format(dt);
    }

    /**
     * API to get the current system date
     * 
     * @return
     */
    public static Date getCurrentDate() {
        return new Date();
    }

    public static Date subtractFromDate(Date dt, int days) {
        return new Date(dt.getTime() - (days * MILIS_IN_DAY));
    }

    public static String getDateInDDMMYYTHHMM(Date dt) {
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy'T'HH:mm");
        return sdf.format(dt);
    }

    public static String getDateInDDMMYYHHMM(Date dt) {
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy HH:mm");
        return sdf.format(dt);
    }

    public static String getDateInhhmmEEEDDMMYYYYUnsetIfNull(Calendar cal) {
        if (cal == null) {
            return "Unset";
        }
        SimpleDateFormat sdf = new SimpleDateFormat("HH:mm, EEE dd-MMM-yyyy");
        return sdf.format(cal.getTime());
    }

    public static String getDateInHHMMDDMMM(Date dt) {
        SimpleDateFormat sdf = new SimpleDateFormat("HH:mm dd-MMM");
        return sdf.format(dt);
    }

    public static String getDateInMysqlDateFormat(Date dt) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        return sdf.format(dt);
    }

    public static String getDateInYYYYMMFormat(Date dt) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMM");
        return sdf.format(dt);
    }

    public static String getDateDDMMYYYYHHMM(Date dt) {
        SimpleDateFormat sdf = new SimpleDateFormat("dd-MMM-yyyy, HH:mm:ss");
        return sdf.format(dt);
    }

    public static String getDateDDMMYYYYCheckHHMMUnset(Calendar cal) {

        if (cal != null && cal.getTime() != null) {
            if ((cal.get(Calendar.MINUTE) > 0) || (cal.get(Calendar.HOUR_OF_DAY) > 0) || (cal.get(Calendar.HOUR) > 0)) {
                SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy, HH:mm:ss");
                return sdf.format(cal.getTime());
            }
            SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
            return sdf.format(cal.getTime());
        }
        return "";
    }

    public static String getDateDDMMYYYYDashCheckHHMMUnset(Calendar cal) {
        if (cal != null && cal.getTime() != null) {
            if ((cal.get(Calendar.MINUTE) > 0) || (cal.get(Calendar.HOUR_OF_DAY) > 0) || (cal.get(Calendar.HOUR) > 0)) {
                SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy, HH:mm:ss");
                return sdf.format(cal.getTime());
            }
            SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy");
            return sdf.format(cal.getTime());
        }
        return "";
    }

    public static String getDateMMDDHHMM(Date dt) {
        SimpleDateFormat sdf = new SimpleDateFormat("MM-dd'T'HH:mm");
        return sdf.format(dt);
    }

    public static String getDateDDMMYYYYbrHHMMIfNullUnset(Calendar dt) {
        if (dt == null) {
            return "Unset";
        }
        SimpleDateFormat sdf = new SimpleDateFormat("dd-MMM-yyyy'<br>'HH:mm:ss");
        return sdf.format(dt.getTime());
    }

    public static String getDateDDMYYYYHHMM(Date dt) {
        SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy, HH:mm:ss");
        return sdf.format(dt);
    }

    public static String getDateDD(Date dt) {
        SimpleDateFormat sdf = new SimpleDateFormat("dd");
        return sdf.format(dt);
    }

    public static String getDateMMDefaultEmpty(Date dt) {
        if (dt == null) {
            return "";
        }
        SimpleDateFormat sdf = new SimpleDateFormat("MM");
        return sdf.format(dt);
    }

    public static String getDateYY(Date dt) {
        SimpleDateFormat sdf = new SimpleDateFormat("yy");
        return sdf.format(dt);
    }

    public static String getDateYYYYDefaultEmpty(Date dt) {
        if (dt == null) {
            return "";
        }
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy");
        return sdf.format(dt);
    }

    public static String getDateInMysqlTSFormat(Date dt) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        return sdf.format(dt);
    }

    public static String getDateInHHMMSSDDMMYYT(Date dt) {
        SimpleDateFormat sdf = new SimpleDateFormat("HH:mm:ss, dd/MM/yyyy");
        return sdf.format(dt);
    }

    public static String getDateInYYYYMMDDHHMMSS(Date dt) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd'T'HHmmss");
        return sdf.format(dt);
    }

    public static String getDateInDashColonYYYYMMDDHHMMSS(Date dt) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");
        return sdf.format(dt);
    }

    public static String getDateInDashColonYYYYMMDDHHMMSSTimezone(Date dt) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.000'Z'");
        return sdf.format(dt);
    }

    public static Date parseDateInMysqlTSFormat(String dt) throws Exception {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        return sdf.parse(dt);

    }

    public static String getDateInDDMMYYYYHHMMSS(Date dt) {
        SimpleDateFormat sdf = new SimpleDateFormat("dd MMM yyyyh:mm:ss a");
        return sdf.format(dt);
    }

    public static String getDateInMMDDYYYYHHMMSS(Date dt) {
        SimpleDateFormat sdf = new SimpleDateFormat("MM/dd/yyyy hh:mm:ss a");
        return sdf.format(dt);
    }

    public static String getDateInYYYYMMDDHHMM(Date dt) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd HH:mm");
        return sdf.format(dt);
    }

    public static String getDateInDDMMYYYYTHHMMSS(Date dt) {
        return getDateInDDMMYYYYTHHMMSS(dt, false);
    }

    public static String getDateInDDMMYYYYTHHMMSS(Date dt, boolean isLocale) {
        SimpleDateFormat sdf;
        // if (isLocale) {
        // sdf = new SimpleDateFormat("dd MMM yyyy h:mm:ss a",
        // I18NBean.getSelectedLocale());
        // } else {
        sdf = new SimpleDateFormat("dd MMM yyyy h:mm:ss a");
        // }
        return sdf.format(dt);
    }

    public static String getDateInDDMMYYYYTHHMMSSWithEmptyOnNullOrError(Date dt) {
        return getDateInDDMMYYYYTHHMMSSWithEmptyOnNullOrError(dt, false);
    }

    public static String getDateInDDMMYYYYTHHMMSSWithEmptyOnNullOrError(Date dt, boolean isLocale) {
        if (dt == null) {
            return "";
        }
        try {
            return getDateInDDMMYYYYTHHMMSS(dt, isLocale);
        } catch (Exception e) {
            return "";
        }

    }

    public static String getDateInLogFormat(Date dt) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss' 'Z");
        return sdf.format(dt);
    }

    public static String getDateInGivenFormat(Date dt, String dateFormat) {
        SimpleDateFormat sdf = new SimpleDateFormat(dateFormat);
        return sdf.format(dt);
    }

    public static Date parseDateInGivenFormatNullIfError(String dateFormat, String str) {
        if (str == null) {
            return null;
        }
        try {
            SimpleDateFormat sdf = new SimpleDateFormat(dateFormat);
            return sdf.parse(str);
        } catch (Exception e) {
            return null;
        }
    }

    public static Date parseDateInYYYYMMDDHHMMSS(String dt) throws Exception {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd'T'HHmmss");
        return sdf.parse(dt);
    }

    public static Date parseDateInMMYY(String dt) throws Exception {
        SimpleDateFormat sdf = new SimpleDateFormat("MMyy");
        return sdf.parse(dt);
    }

    public static Date parseDateInYYYYMMDDTHHMMSSDashColonSeparated(String dt) throws Exception {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");
        return sdf.parse(dt);
    }

    public static Date parseDateInYYYYMMDDHHMM(String dt) throws Exception {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd'T'HHmm");
        return sdf.parse(dt);
    }

    public static Date parseDateInYYYYMMDDHHMMSSWithNullOnError(String dt) {
        try {
            SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd'T'HHmmss");
            return sdf.parse(dt);
        } catch (Throwable t) {
            return null;
        }
    }

    public static Date parseDateInHHMMaEMMMDDYYYY(String dt) throws Exception {

        try {
            SimpleDateFormat sdf = new SimpleDateFormat("hh:mma-EMMM-dd-yyyy");
            return sdf.parse(dt);
        } catch (Throwable t) {
            return null;
        }
    }

    public static Date parseDateInMMMDDYYYYHHMMSSa(String dt) throws Exception {

        try {
            SimpleDateFormat sdf = new SimpleDateFormat("MMM dd, yyyy hh:mm:ss a");
            return sdf.parse(dt);
        } catch (Throwable t) {
            return null;
        }
    }

    public static String getDateInYYYYMMDD(Calendar cal) {
        if (cal == null) {
            return "";
        }
        return getDateInYYYYMMDD(cal.getTime());
    }

    public static String getDateInYYYYMMDD(Date dt) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
        return sdf.format(dt);
    }

    public static String getDateInYYYYMMDDandNullIsEmpty(Date dt) {
        if (dt == null) {
            return "";
        }
        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
        return sdf.format(dt);
    }

    public static String getDateInMMDD(Date dt) {
        SimpleDateFormat sdf = new SimpleDateFormat("MMdd");
        return sdf.format(dt);
    }

    public static String getDateInMMYYYY(Date dt) {
        SimpleDateFormat sdf = new SimpleDateFormat("MM/yyyy");
        return sdf.format(dt);
    }

    public static String getDateInMMYYYY(Calendar cal) {
        return (getDateInMMYYYY(cal.getTime()));
    }

    public static String getDateInQQYYYY(Date dt) {
        Calendar c = getCurrentDateCalendar();
        c.setTime(dt);
        int m = c.get(Calendar.MONTH) + 1;

        SimpleDateFormat sdf = new SimpleDateFormat("yyyy");
        if (m < 4) {
            return "Q1-" + sdf.format(dt);
        } else if (m < 7) {
            return "Q2-" + sdf.format(dt);
        } else if (m < 10) {
            return "Q3-" + sdf.format(dt);
        } else if (m < 13) {
            return "Q4-" + sdf.format(dt);
        } else {
            return "Qx-" + sdf.format(dt);
        }
    }

    public static int getQuarterForMonth0To11(int m) {
        if (m < 3) {
            return 1;
        } else if (m < 6) {
            return 2;
        } else if (m < 9) {
            return 3;
        } else {
            return 4;
        }
    }

    public static String getDateInMMMYY(Date dt) {
        SimpleDateFormat sdf = new SimpleDateFormat("MMM-yy");
        return sdf.format(dt);
    }

    public static String getDateInMMMYYYY(Date dt) {
        SimpleDateFormat sdf = new SimpleDateFormat("MMM-yyyy");
        return sdf.format(dt);
    }

    public static String getDateInYYYYMMM(Date dt) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MMM");
        return sdf.format(dt);
    }

    public static String getDateInYYYYMM(Date dt) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM");
        return sdf.format(dt);
    }

    public static String getDateInYYYY(Date dt) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy");
        return sdf.format(dt);
    }

    public static String getDateInMM(Date dt) {
        SimpleDateFormat sdf = new SimpleDateFormat("MM");
        return sdf.format(dt);
    }

    public static String getDateInMMM(Date dt) {
        SimpleDateFormat sdf = new SimpleDateFormat("MMM");
        return sdf.format(dt);
    }

    public static String getDateInYY(Date dt) {
        SimpleDateFormat sdf = new SimpleDateFormat("yy");
        return sdf.format(dt);
    }

    public static String getDateInMMDDHHMM(Date dt) {
        SimpleDateFormat sdf = new SimpleDateFormat("MMddHHmm");
        return sdf.format(dt);
    }

    public static Date parseDateInDDMMMYY(String str) {
        if (str != null) {
            try {
                SimpleDateFormat sdf = new SimpleDateFormat("ddMMMyy");
                return sdf.parse(str);
            } catch (ParseException e) {
            }
        }
        return null;

    }

    public static Date parseDateInDDMMMYYHHmm(String str) {
        if (str != null) {
            try {
                SimpleDateFormat sdf = new SimpleDateFormat("ddMMMyy HHmm");
                return sdf.parse(str);
            } catch (ParseException e) {
            }
        }
        return null;

    }

    public static Date parseDateInDDMMYYYY(String str) throws Exception {
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
        return sdf.parse(str);
    }

    public static Date parseDateInDDMMMYYDashSeparator(String str) {
        if (str != null) {
            try {
                SimpleDateFormat sdf = new SimpleDateFormat("dd-MMM-yy");
                return sdf.parse(str);
            } catch (Exception e) {
            }
        }
        return null;
    }

    public static Date parseDateInGivenFormat(String str, String format) {
        if (str != null) {
            try {
                SimpleDateFormat sdf = new SimpleDateFormat(format);
                return sdf.parse(str);
            } catch (Exception e) {
            }
        }
        return null;
    }

    public static Date parseDateInMMDDYYYY(String str) throws Exception {
        SimpleDateFormat sdf = new SimpleDateFormat("MM/dd/yyyy");
        return sdf.parse(str);
    }

    public static Date parseDateInMMDDYYYYDash(String str) {
        try {
            SimpleDateFormat sdf = new SimpleDateFormat("MM-dd-yyyy");
            return sdf.parse(str);
        } catch (Exception e) {
            return null;
        }
    }

    public static Date parseDateInDDMMYYYYNullIfUnset(String str) throws Exception {
        if (str == null || "dd/mm/yyyy".equalsIgnoreCase(str)) {
            return null;
        }
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
        return sdf.parse(str);
    }

    public static Date parseDateInDDMMYYYYNullIfError(String str) {
        if (str == null || "dd/mm/yyyy".equalsIgnoreCase(str)) {
            return null;
        }
        try {
            SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
            return sdf.parse(str);
        } catch (Exception e) {
            return null;
        }
    }

    public static boolean isValidDate(String inDate) {
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
        dateFormat.setLenient(false);
        try {
            dateFormat.parse(inDate.trim());
        } catch (ParseException pe) {
            return false;
        }
        return true;
    }

    public static Date parseDateInMMYYYYNullIfError(String str) {
        if (str == null || "mm/yyyy".equalsIgnoreCase(str)) {
            return null;
        }
        try {
            SimpleDateFormat sdf = new SimpleDateFormat("MM/yyyy");
            return sdf.parse(str);
        } catch (Exception e) {
            return null;
        }
    }

    public static Date parseDateInYYYYMMDDNullForException(String str) {
        if (str != null) {
            try {
                SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
                return sdf.parse(str);
            } catch (Exception e) {
            }
        }
        return null;
    }

    public static Date parseDateInYYYYMMDDDashNullForException(String str) {
        if (str != null) {
            try {
                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
                return sdf.parse(str);
            } catch (Exception e) {
            }
        }
        return null;
    }

    public static Date parseDateInYYYYMMDDDashHHMMColonNullForException(String str) {
        if (str != null) {
            try {
                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm");
                return sdf.parse(str);
            } catch (Exception e) {
            }
        }
        return null;
    }

    public static Date parseDateInYYYYMMDDSlashNullForException(String str) {
        if (str != null) {
            try {
                SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd");
                return sdf.parse(str);
            } catch (Exception e) {
            }
        }
        return null;
    }

    public static Date parseDateInHHMMNullIfError(String str) {
        if (str == null || "hh:mm".equalsIgnoreCase(str)) {
            return null;
        }
        try {
            SimpleDateFormat sdf = new SimpleDateFormat("HH:mm");
            return sdf.parse(str);
        } catch (Exception e) {
            return null;
        }
    }

    public static Date parseDateInDDMMYYTHHMMNullIfError(String str) {
        if (str == null || "dd/mm/yyyythh:mm".equalsIgnoreCase(str)) {
            return null;
        }
        try {
            SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy'T'HH:mm");
            return sdf.parse(str);
        } catch (Exception e) {
            return null;
        }
    }

    public static Date parseDateInDDMMYYHHMMSSNullIfError(String str) {
        if (str == null || "dd/mm/yyyy hh:mm:ss".equalsIgnoreCase(str)) {
            return null;
        }
        try {
            SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
            return sdf.parse(str);
        } catch (Exception e) {
            return null;
        }
    }

    public static Date parseDateInDDMMYYYYHHMMSSNullIfError(String str) {
        if (str == null || "dd/mm/yyyy, hh:mm:ss".equalsIgnoreCase(str)) {
            return null;
        }
        try {
            SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy, HH:mm:ss");
            return sdf.parse(str);
        } catch (Exception e) {
            return null;
        }
    }

    public static Date parseDateInDDMMYYHHMMNullIfError(String str) {
        if (str == null || "dd/mm/yyyy hh:mm".equalsIgnoreCase(str)) {
            return null;
        }
        try {
            SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy HH:mm");
            return sdf.parse(str);
        } catch (Exception e) {
            return null;
        }
    }

    public static Date parseDateInDDMYYYYHHMMNullIfError(String str) {
        if (str == null) {
            return null;
        }
        try {
            SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy, HH:mm:ss");
            return sdf.parse(str);
        } catch (Exception e) {
            return null;
        }
    }

    public static Date parseDateInDDMYYYYHHMMSSNullIfError(String str) {
        if (str == null) {
            return null;
        }
        try {
            SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy, HH:mm:ss");
            return sdf.parse(str);
        } catch (Exception e) {
            return null;
        }
    }

    public static Date parseDateInDDMMYYYYDashNullIfError(String str) {
        if (str == null) {
            return null;
        }
        try {
            SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy");
            return sdf.parse(str);
        } catch (Exception e) {
            return null;
        }
    }

    public static Date parseDateInDDMMMYYYYDashNullIfError(String str) {

        if (str == null) {
            return null;
        }
        try {
            SimpleDateFormat sdf = new SimpleDateFormat("dd-MMM-yyyy");
            return sdf.parse(str);
        } catch (Exception e) {
            s_logger.error(e);
            return null;
        }
    }

    public static Date parseDateInYYYYMMDD(String str) throws Exception {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
        return sdf.parse(str);
    }

    public static Date parseDateInDDMMYYTHHMM(String str) throws Exception {
        if (str == null) {
            return null;
        }
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy'T'HH:mm");
        return sdf.parse(str);
    }

    public static Date parseDateInDDMMYYYYHHMMNullIfError(String str) throws Exception {
        if (str == null) {
            return null;
        }
        try {
            SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy HH:mm");
            return sdf.parse(str);
        } catch (Exception e) {
            return null;
        }
    }

    public static Date parseDateInHHMMAMPM(String str) throws Exception {
        if (str == null) {
            return null;
        }
        SimpleDateFormat sdf = new SimpleDateFormat("hh:mm a");
        return sdf.parse(str);
    }

    public static Date parseDateInYYYYMMDDTHHMM(String str) throws Exception {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd'T'HHmm");
        return sdf.parse(str);
    }

    public static String parseDateInDDMMYYYYSlashEmptyIfError(String str) {
        if (str == null || str == "") {
            return "";
        }
        try {
            SimpleDateFormat oldFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.0");
            SimpleDateFormat newFormat = new SimpleDateFormat("dd/MM/yyyy");
            Date date = oldFormat.parse(str);
            return newFormat.format(date);
        } catch (Exception e) {
            return "";
        }
    }

    public static String parseDateInDDMMYYYYHypenEmptyIfError(String str) {
        if (str == null || str == "") {
            return "";
        }
        try {
            SimpleDateFormat oldFormat = new SimpleDateFormat("dd-MM-yyyy");
            SimpleDateFormat newFormat = new SimpleDateFormat("dd/MM/yyyy");
            Date date = oldFormat.parse(str);
            return newFormat.format(date);
        } catch (Exception e) {
            return "";
        }
    }

    public static boolean isSameDate(Calendar date1, Calendar date2) {
        int year1 = date1.get(Calendar.YEAR);
        int year2 = date2.get(Calendar.YEAR);
        if (year1 != year2) {
            return false;
        }
        int dayOfYear1 = date1.get(Calendar.DAY_OF_YEAR);
        int dayOfYear2 = date2.get(Calendar.DAY_OF_YEAR);
        if (dayOfYear1 != dayOfYear2) {
            return false;
        }
        return true;
    }

    public static Date parseDateInMMDDYYYYHHMMAMPM(String str) throws Exception {
        if (str == null) {
            return null;
        }
        SimpleDateFormat sdf = new SimpleDateFormat("MM/dd/yyyy hh:mm:ss a", Locale.US);
        return sdf.parse(str);
    }

    public static boolean equalsWithNullCheck(Date dt1, Date dt2) {
        if (dt1 == null) {
            if (dt2 == null) {
                return true;
            } else {
                return false;
            }
        } else if (dt2 == null) {
            return false;
        } else {
            return dt1.equals(dt2);
        }
    }

    public static boolean compareToMinute(Calendar cal1, Calendar cal2) {
        if (cal1 == null && cal2 == null) {
            return true;
        } else if (cal1 == null || cal2 == null) {
            return false;
        }
        if (cal1.get(Calendar.YEAR) != cal2.get(Calendar.YEAR)) {
            return false;
        } else if (cal1.get(Calendar.MONTH) != cal2.get(Calendar.MONTH)) {
            return false;
        } else if (cal1.get(Calendar.DAY_OF_MONTH) != cal2.get(Calendar.DAY_OF_MONTH)) {
            return false;
        } else if (cal1.get(Calendar.HOUR_OF_DAY) != cal2.get(Calendar.HOUR_OF_DAY)) {
            return false;
        } else if (cal1.get(Calendar.MINUTE) != cal2.get(Calendar.MINUTE)) {
            return false;
        }
        return true;
    }

    public static boolean isDateInRageBothInclusive(Date startDate, Date endDate, Date dateToCheck) {
        if (startDate != null) {
            if (startDate.after(dateToCheck)) {
                return false;
            }
        }
        if (endDate != null) {
            Calendar endDateCal = getCurrentDateCalendar();
            endDateCal.setTime(endDate);
            endDateCal.add(Calendar.DAY_OF_YEAR, 1);
            endDate = endDateCal.getTime();
            if (endDate.before(dateToCheck)) {
                return false;
            }
        }
        return true;
    }

    public static Date getDateAfterNowAtHours() {
        Calendar cal = getCurrentDateCalendar();
        cal.setTime(DateUtility.getCurrentDate());
        cal.add(Calendar.MINUTE, -5);
        cal.add(Calendar.HOUR, 1);
        cal.set(Calendar.MINUTE, 0);
        cal.set(Calendar.SECOND, 0);
        cal.set(Calendar.MILLISECOND, 0);
        return cal.getTime();
    }

    public static Date getDateAfterNowAtEvenHours() {
        Calendar cal = getCurrentDateCalendar();
        cal.setTime(DateUtility.getCurrentDate());
        cal.add(Calendar.MINUTE, -5);
        if (cal.get(Calendar.HOUR_OF_DAY) % 2 == 0) {
            cal.add(Calendar.HOUR, 2);
        } else {
            cal.add(Calendar.HOUR, 1);
        }
        cal.set(Calendar.MINUTE, 0);
        cal.set(Calendar.SECOND, 0);
        cal.set(Calendar.MILLISECOND, 0);
        return cal.getTime();
    }

    public static long getDifferenceInDates(Date date1, Date date2, int differenceUnit) {
        Calendar cal1 = getCurrentDateCalendar();
        cal1.setTime(date1);
        Calendar cal2 = getCurrentDateCalendar();
        cal2.setTime(date2);
        return getDifferenceInDates(cal1, cal2, differenceUnit);
    }

    public static Calendar getCalendarWithTimeStampZeroes(Calendar cal) {
        cal.set(Calendar.HOUR_OF_DAY, 0);
        cal.set(Calendar.MINUTE, 0);
        cal.set(Calendar.SECOND, 0);
        cal.set(Calendar.MILLISECOND, 0);
        return cal;
    }

    public static Calendar getCalendarWithTimeStampZeroes(Date dt) {
        if (dt == null) {
            return null;
        }
        Calendar cal = getCurrentDateCalendar();
        cal.setTime(dt);
        cal.set(Calendar.HOUR_OF_DAY, 0);
        cal.set(Calendar.MINUTE, 0);
        cal.set(Calendar.SECOND, 0);
        cal.set(Calendar.MILLISECOND, 0);
        return cal;
    }

    public static Calendar getCalendarWithTimeStampZeroes(Calendar cal, int calTimeUnit) {
        switch (calTimeUnit) {
        case Calendar.HOUR_OF_DAY:
            cal.set(Calendar.HOUR_OF_DAY, 0);
        case Calendar.MINUTE:
            cal.set(Calendar.MINUTE, 0);
        case Calendar.SECOND:
            cal.set(Calendar.SECOND, 0);
        case Calendar.MILLISECOND:
            cal.set(Calendar.MILLISECOND, 0);
        }
        return cal;
    }

    public static Calendar getCalendarWithParsingDateInDDMMYYTHHMMNullIfError(String str) {
        Calendar cal = getCurrentDateCalendar();
        cal.setTime(DateUtility.parseDateInDDMMYYTHHMMNullIfError(str));
        return cal;
    }

    public static Calendar getCalendarWithParsingDateInDDMMYYTHHMM(String str) throws Exception {
        Calendar cal = getCurrentDateCalendar();
        cal.setTime(DateUtility.parseDateInDDMMYYTHHMM(str));
        return cal;
    }

    public static long getDifferenceInDates(Calendar cal1, Calendar cal2, int differenceUnit) {
        if (cal1.after(cal2)) { // swap dates so that d1 is start and d2 is end
            Calendar swap = cal1;
            cal1 = cal2;
            cal2 = swap;
        }
        return getDifferenceInDatesWithoutSwap(cal1, cal2, differenceUnit);
    }

    public static long getDifferenceInDatesWithoutSwap(Calendar cal1, Calendar cal2, int differenceUnit) {
        long diff = cal2.getTimeInMillis() - cal1.getTimeInMillis();
        switch (differenceUnit) {
        case Calendar.SECOND:
            return diff / 1000;
        case Calendar.MINUTE:
            return diff / (60 * 1000);
        case Calendar.HOUR:
            return diff / (60 * 60 * 1000);
        case Calendar.DATE:
            return diff / (24 * 60 * 60 * 1000);
        }
        throw new RuntimeException("Invalid Difference Unit Value: " + differenceUnit);
    }

    public static int[] getDifferenceInHMS(Date start, Date end) {
        int[] res = new int[4];
        long s = start.getTime();
        long e = end.getTime();
        long diff = Math.abs((e - s) / 1000);

        res[0] = (int) Math.floor(diff / (60 * 60 * 24));
        diff = (diff - (res[0] * 60 * 60 * 24));

        res[1] = (int) Math.floor(diff / (60 * 60));
        diff = (diff - (res[1] * 60 * 60));

        res[2] = (int) Math.floor(diff / 60);
        res[3] = (int) (diff - (res[2] * 60));
        return res;
    }

    public static Calendar getCalendarAfterNumberOfDays(Calendar calendar, int daysToAdd) {
        calendar.add(Calendar.DAY_OF_YEAR, daysToAdd);
        return calendar;
    }

    public static Date getDateBeforeOrAfterNumberOfDays(Date dt, int days) {
        Calendar cal = getCurrentDateCalendar();
        cal.setTime(dt);
        cal.add(Calendar.DAY_OF_YEAR, days);
        return cal.getTime();
    }

    public static Date getDateBeforeOrAfterNumberOfYears(Date dt, int years) {
        Calendar cal = getCurrentDateCalendar();
        cal.setTime(dt);
        cal.add(Calendar.YEAR, years);
        return cal.getTime();
    }

    public static String getDateinEEEEEMMMDD(Date dt) {
        SimpleDateFormat sdf = new SimpleDateFormat("EEEEE, MMM dd");
        return sdf.format(dt);
    }

    public static String getDateinEEEEEMMMDDWithCurrentDateOnNull(Calendar dt) {
        SimpleDateFormat sdf = new SimpleDateFormat("EEEEE, MMM dd");
        return sdf.format(dt == null ? getCurrentDate() : dt.getTime());
    }

    public static Date getDateAfterHours(Date dt, int hrs) {
        Calendar cal = getCurrentDateCalendar();
        cal.setTime(dt);
        cal.add(Calendar.HOUR, hrs);
        return cal.getTime();
    }

    public static Date getDateAfterMinutes(Date dt, int minutes) {
        Calendar cal = getCurrentDateCalendar();
        cal.setTime(dt);
        cal.add(Calendar.MINUTE, minutes);
        return cal.getTime();
    }

    public static Calendar getLocalCalendarFromGivenTimeZone(Calendar cal, String timeZone) {
        if (cal == null) {
            return null;
        }

        Calendar calInGivenTimeZone = Calendar.getInstance(TimeZone.getTimeZone(timeZone));
        Calendar localCal = getCurrentDateCalendar();
        int offset = cal.get(Calendar.DST_OFFSET) - calInGivenTimeZone.get(Calendar.DST_OFFSET)
                + cal.get(Calendar.ZONE_OFFSET) - calInGivenTimeZone.get(Calendar.ZONE_OFFSET);

        localCal.setTimeInMillis(cal.getTimeInMillis() + offset);

        return localCal;
    }

    private DateUtility() {
    }

    public static void main(String[] args) {
        try {
            // Date dt=parseDateInYYYYMMDDHHMM("20121213T0812");
            Date dt = getLastDateOfTheWeek();
            System.out.println(getDateInDDMMYYYY(dt));
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    public static String timeConverter(String timing) {
        if ("M".equals(timing)) {
            return "Morning";
        } else if ("A".equals(timing)) {
            return "Afternoon";
        } else if ("E".equals(timing)) {
            return "Evening";
        } else if ("N".equals(timing)) {
            return "Night";
        } else if (timing.length() == 1) {
            return "0" + timing + ":00";
        } else if (timing.length() == 2) {
            return timing + ":00";
        }
        return timing;
    }

    public static Date getDateAfterNowWithHourMinuteSecond(int hour, int minute, int second) {
        Calendar cal = getCurrentDateCalendar();
        cal.setTime(new Date());
        cal.set(Calendar.HOUR_OF_DAY, hour);
        cal.set(Calendar.MINUTE, minute);
        cal.set(Calendar.SECOND, second);
        cal.set(Calendar.MILLISECOND, 0);

        Calendar now = getCurrentDateCalendar();
        now.setTime(new Date());
        if (cal.before(now)) {
            cal.add(Calendar.DAY_OF_YEAR, 1);
        }

        return cal.getTime();
    }

    public static Date getPreviousDaysDate(int hour, int minute, int second) {
        Calendar cal = getCurrentDateCalendar();
        cal.setTime(new Date());
        cal.set(Calendar.HOUR_OF_DAY, hour);
        cal.set(Calendar.MINUTE, minute);
        cal.set(Calendar.SECOND, second);
        cal.set(Calendar.MILLISECOND, 0);
        cal.add(Calendar.DAY_OF_YEAR, -1);

        return cal.getTime();
    }

    public static Date getLastTimeForDay(Date date) {
        Calendar cal = getCurrentDateCalendar();
        cal.setTime(date);
        // Subtracting 1 second from the start time of the next day
        cal.add(Calendar.DATE, 1);
        cal = removeTime(false, cal);
        cal.add(Calendar.MILLISECOND, -1);
        return cal.getTime();
    }

    public static Date removeTime(Date date, boolean isToday) {
        Calendar cal = getCurrentDateCalendar();
        if (!isToday) {
            cal.setTime(date);
        }
        cal.set(Calendar.HOUR_OF_DAY, 0);
        cal.set(Calendar.MINUTE, 0);
        cal.set(Calendar.SECOND, 0);
        cal.set(Calendar.MILLISECOND, 0);
        return cal.getTime();
    }

    public static Calendar removeTime(boolean isToday, Calendar date) {
        Calendar cal = getCurrentDateCalendar();
        if (!isToday) {
            cal.setTime(date.getTime());
        }
        cal.set(Calendar.HOUR_OF_DAY, 0);
        cal.set(Calendar.MINUTE, 0);
        cal.set(Calendar.SECOND, 0);
        cal.set(Calendar.MILLISECOND, 0);
        return cal;
    }

    public static String convertMysqlTSFormatToYYYYMMDDT(String date) throws Exception {
        if (date == null) {
            return null;
        }

        Date parseDateInMysqlTSFormat = parseDateInMysqlTSFormat(date);

        return getDateInDashColonYYYYMMDDHHMMSS(parseDateInMysqlTSFormat);
    }

    public static String convertddMMMyyToYYMMDD(String date) {
        if (date == null) {
            return null;
        }

        Date parseDateFromDDMMMYY = parseDateInDDMMMYY(date);
        return getDateInYYYYMMDDDash(parseDateFromDDMMMYY);
    }

    public static Date getFirstDateOfYear() {
        return getFirstDateOfYear(null);
    }

    public static Date getFirstDateOfYear(String year) {
        Calendar cal = getCurrentDateCalendar();
        if (year != null) {
            Date parsedYear = parseDateInYYYY(year);
            if (parsedYear != null) {
                cal.setTime(parsedYear);
            }
        }
        cal.set(Calendar.MONTH, Calendar.JANUARY);
        cal.set(Calendar.DATE, 1);

        return cal.getTime();
    }

    private static Date parseDateInYYYY(String year) {
        try {
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy");
            return sdf.parse(year);
        } catch (ParseException e) {
            s_logger.error("could not parse date for year: " + year, e);
        }

        return null;
    }

    public static Date getLastDateOfYear() {
        return getLastDateOfYear(null);
    }

    public static Date getLastDateOfYear(String year) {
        Calendar cal = getCurrentDateCalendar();

        if (year != null) {
            Date parsedYear = parseDateInYYYY(year);
            if (parsedYear != null) {
                cal.setTime(parsedYear);
            }
        }

        cal.set(Calendar.MONTH, Calendar.DECEMBER);
        cal.set(Calendar.DATE, 31);

        return cal.getTime();
    }

    public static String getCurrentYear() {
        return getDateInYYYY(getCurrentDate());
    }

    public static int getNumberOfDays(Date endDate) {
        return getNumberOfDays(endDate, getCurrentDate());
    }

    private static int getNumberOfDays(Date endDate, Date startDate) {
        return (int) TimeUnit.DAYS.convert(endDate.getTime() - startDate.getTime(), TimeUnit.MILLISECONDS);
    }

    /**
     * checks the date and returns the month from it
     * 
     * @param date
     * @return
     */
    public static int getMonthOfDate(Date date) {
        Calendar cal = getCurrentDateCalendar();
        cal.setTime(date);
        int month = cal.get(Calendar.MONTH);
        return month+1;
    }

    /**
     * checks the date and returns which week of month it is
     * 
     * @param date
     * @return
     */
    public static int getWeekOfMonth(Date date) {
        Calendar cal = getCalendarWithDateSet(date);

        int week = cal.get(Calendar.WEEK_OF_MONTH);
        return week;
    }

    /**
     * sets the date supplied to the api to the new calendar instance.
     * 
     * @param dt
     * @return
     */
    private static Calendar getCalendarWithDateSet(Date dt) {
        Calendar cal = getCurrentDateCalendar();

        if (dt != null) {
            cal.setTime(dt);
        }

        return cal;
    }

    /**
     * This API returns the last date(Friday) of the current week
     * 
     * @param date
     * @return
     */
    public static Date getLastDateOfTheWeek() {
        Calendar cal = getCurrentDateCalendar();
        cal.set(Calendar.DAY_OF_WEEK, Calendar.FRIDAY);

        Date lastDateOfWeek = cal.getTime();
        return lastDateOfWeek;
    }

    /**
     * returns a new calendar instance
     * 
     * @return
     */
    private static Calendar getCurrentDateCalendar() {
        Calendar cal = Calendar.getInstance();
        return cal;
    }

    public static Date parseDateInDDMMM(String dt) {
        SimpleDateFormat sdf = new SimpleDateFormat("DD-MMM");
        if(dt!=null){
            try {
                return sdf.parse(dt);
            } catch (ParseException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }
        return null;
    }

    
    /**
     *
     * @param genTs
     * @return
     */
    public static Date getAndParseDateInDDMMYYYYDash(Date genTs) {
        String dateStr = DateUtility.getDateInDDMMYYYYDash(genTs);
        Date date = DateUtility.parseDateInDDMMYYYYDashNullIfError(dateStr);       
        return date;
    }

}
