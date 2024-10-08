package com.zillious.commons.db;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Timestamp;
import java.util.Calendar;
import java.util.Date;

/**
 * This is an abstract class to wrap around all important DB functions for:
 * Creating connection; Closing connection; Setting prepared statements
 * variables; Setting statement variables with SQL injection check.
 * 
 */
public abstract class DBConnector {
    /**
     * Returns a DB connection as per the parameters passed
     * 
     * @param isAutoCommitOn
     * @param isReadOnly
     * @return
     * @throws SQLException
     */
    protected abstract Connection getConnection(boolean isAutoCommitOn, boolean isReadOnly) throws SQLException;

    /**
     * Returns a DB connection. Assumes connection is on auto-commit true, but
     * not read-only.
     * 
     * @return
     * @throws SQLException
     */
    public Connection getReadOnlyConnection() throws SQLException {
        return getConnection(true, true);
    }

    /**
     * Returns a DB connection. Assumes connection is on auto-commit true, but
     * not read-only.
     * 
     * @return
     * @throws SQLException
     */
    public Connection getConnection() throws SQLException {
        return getConnection(true, false);
    }

    /**
     * Returns a DB connection. Assumes connection is not for read-only.
     * 
     * @param isAutoCommitOn
     * @return
     * @throws SQLException
     */
    public Connection getConnection(boolean isAutoCommitOn) throws SQLException {
        return getConnection(isAutoCommitOn, false);
    }

    /**
     * Close the connection, with check on result set and stmt as well
     * 
     * @param conn
     * @param stmt
     * @param result
     * @param isCloseConnection
     * @return Whether the closing was done gracefully or not.
     */
    public boolean closeAll(Connection conn, Statement stmt, ResultSet result, boolean isCloseConnection) {
        boolean isGraceful = true;
        if (result != null) {
            try {
                result.close();
            } catch (Exception e) {
                isGraceful = false;
            }
        }
        if (stmt != null) {
            try {
                stmt.close();
            } catch (Exception e) {
                isGraceful = false;
            }
        }
        if (isCloseConnection && conn != null) {
            try {
                conn.close();
            } catch (Exception e) {
                isGraceful = false;
            }
        }
        return isGraceful;
    }

    /**
     * Wrapper around DB rollback which will eat any Exception thrown. For
     * example, this can be used in error handling section where any extra error
     * should not be thrown. No rollback is done in case he connection is not
     * supposed to be closed.
     * 
     * @return Whether rollback was done or not.
     */
    public boolean rollback(Connection conn, boolean isCloseConnection) {
        if (isCloseConnection && conn != null) {
            try {
                conn.rollback();
                return true;
            } catch (Exception e) {
            }
        }
        return false;
    }

    /**
     * Commit
     * 
     * @param conn
     * @param isCloseConnection
     * @throws SQLException
     */
    public void commit(Connection conn, boolean isCloseConnection) throws SQLException {
        if (isCloseConnection && conn != null) {
            conn.commit();
        }
    }

    /*
     * Prepared Statement Parameter Management
     */
    /**
     * Set String parameter in a prepared statement.
     * 
     * @param stmt
     * @param idx
     * @param val
     * @throws SQLException
     */
    public void setString(PreparedStatement stmt, int idx, String val) throws SQLException {
        if (val == null) {
            stmt.setNull(idx, java.sql.Types.VARCHAR);
        } else {
            stmt.setString(idx, val);
        }
    }

    /**
     * Set String parameter in a prepared statement with maxlength check.
     * 
     * @param stmt
     * @param idx
     * @param val
     * @param maxLength
     * @throws SQLException
     */
    public void setString(PreparedStatement stmt, int idx, String val, int maxLength) throws SQLException {
        setString(stmt, idx, stripMoreThanMaxLength(val, maxLength));
    }

    /**
     * Set Int parameter in a prepared statement.
     * 
     * @param stmt
     * @param idx
     * @param val
     * @throws SQLException
     */
    public void setIntWithDefault(PreparedStatement stmt, int idx, int val, int minVal, int defaultVal)
            throws SQLException {
        stmt.setInt(idx, (val <= minVal) ? defaultVal : val);
    }

    /**
     * Set Int parameter in a prepared statement.
     * 
     * @param stmt
     * @param idx
     * @param val
     * @throws SQLException
     */
    public void setIntDefaultNull(PreparedStatement stmt, int idx, int val, int minVal) throws SQLException {
        if (val <= minVal) {
            stmt.setNull(idx, java.sql.Types.INTEGER);
        } else {
            stmt.setInt(idx, val);
        }
    }

    /**
     * Set Long parameter in a prepared statement.
     * 
     * @param stmt
     * @param idx
     * @param val
     * @throws SQLException
     */
    public void setDoubleWithDefault(PreparedStatement stmt, int idx, double val, double minVal, double defaultVal)
            throws SQLException {
        stmt.setDouble(idx, (val <= minVal) ? defaultVal : val);
    }

    /**
     * Set Long parameter in a prepared statement.
     * 
     * @param stmt
     * @param idx
     * @param val
     * @throws SQLException
     */
    public void setDoubleDefaultNull(PreparedStatement stmt, int idx, double val, double minVal) throws SQLException {
        if (val <= minVal) {
            stmt.setNull(idx, java.sql.Types.DOUBLE);
        } else {
            stmt.setDouble(idx, val);
        }
    }

    /**
     * Set Long parameter in a prepared statement.
     * 
     * @param stmt
     * @param idx
     * @param val
     * @throws SQLException
     */
    public void setLongWithDefault(PreparedStatement stmt, int idx, long val, long minVal, long defaultVal)
            throws SQLException {
        stmt.setLong(idx, (val <= minVal) ? defaultVal : val);
    }

    /**
     * Set Long parameter in a prepared statement.
     * 
     * @param stmt
     * @param idx
     * @param val
     * @throws SQLException
     */
    public void setLongDefaultNull(PreparedStatement stmt, int idx, long val, long minVal) throws SQLException {
        if (val <= minVal) {
            stmt.setNull(idx, java.sql.Types.INTEGER);
        } else {
            stmt.setLong(idx, val);
        }
    }

    /**
     * Set Java Date into a SQL Data column.
     * 
     * @param stmt
     * @param idx
     * @param date
     * @throws SQLException
     */
    public void setDate(PreparedStatement stmt, int idx, Date date) throws SQLException {
        if (date == null) {
            stmt.setNull(idx, java.sql.Types.DATE);
        } else {
            stmt.setDate(idx, new java.sql.Date(date.getTime()));
        }
    }

    /**
     * Set Java Date into a SQL Timestamp object.
     * 
     * @param stmt
     * @param idx
     * @param date
     * @throws SQLException
     */
    public void setTimestamp(PreparedStatement stmt, int idx, Date date) throws SQLException {
        if (date == null) {
            stmt.setNull(idx, java.sql.Types.TIMESTAMP);
        } else {
            stmt.setTimestamp(idx, new Timestamp(date.getTime()));
        }
    }

    /**
     * Set Java Calendar into SQL Timestamp object.
     * 
     * @param stmt
     * @param idx
     * @param cal
     * @throws SQLException
     */
    public void setTimestamp(PreparedStatement stmt, int idx, Calendar cal) throws SQLException {
        if (cal == null) {
            stmt.setNull(idx, java.sql.Types.TIMESTAMP);
        } else {
            stmt.setTimestamp(idx, new Timestamp(cal.getTimeInMillis()));
        }
    }

    /**
     * Get SQL Timestamp as Java Date object.
     * 
     * @param rs
     * @param col
     * @return
     * @throws SQLException
     */
    public Date getTimeStampAsDate(ResultSet rs, String col) throws SQLException {
        Timestamp ts = rs.getTimestamp(col);
        if (ts == null) {
            return null;
        }
        return new Date(ts.getTime());
    }
    
    /**
     * Get SQL Timestamp as Java Date object.
     * 
     * @param rs
     * @param col
     * @return
     * @throws SQLException
     */
    public Date getTimeStampAsDate(ResultSet rs, int col) throws SQLException {
        Timestamp ts = rs.getTimestamp(col);
        if (ts == null) {
            return null;
        }
        return new Date(ts.getTime());
    }

    /**
     * Get SQL Date as Java Date object.
     * 
     * @param rs
     * @param col
     * @return
     * @throws SQLException
     */
    public Date getDateAsDate(ResultSet rs, String col) throws SQLException {
        java.sql.Date ts = rs.getDate(col);
        if (ts == null) {
            return null;
        }
        return new Date(ts.getTime());
    }

    /**
     * Get SQL Timestamp as Java Calendar object.
     * 
     * @param rs
     * @param col
     * @return
     * @throws SQLException
     */
    public Calendar getTimestampAsCalendar(ResultSet rs, String col) throws SQLException {
        Date ts = rs.getTimestamp(col);
        if (ts == null) {
            return null;
        }
        Calendar cal = Calendar.getInstance();
        cal.setTime(ts);
        return cal;
    }

    /*
     * Statement SQL Injection protection functions
     */
    /**
     * Remove dangerous code capable of SQL Injection for a String appended into
     * SQL Statement Query.
     * 
     * @param str
     * @param isStrict
     * @return
     */
    public String clearStringForSQLInjection(String str, boolean isStrict) {
        if (str == null) {
            return str;
        }
        if (isStrict) {
            return str.replaceAll("[';]+", "").replaceAll("[-]{2}", "");
        } else {
            return str.replaceAll("[']", "");
        }
    }

    /**
     * Clear String for SQL Injection with max length check.
     * 
     * @param str
     * @param maxLength
     * @return
     */
    public String clearStringForSQLInjection(String str, int maxLength, boolean isStrict) {
        return stripMoreThanMaxLength(clearStringForSQLInjection(str, isStrict), maxLength);
    }

    /**
     * Return only alphanumeric string, will remove all other characters.
     * 
     * @param str
     * @param maxLength
     * @return
     */
    public String clearAplhaNumericStringForSQLInjection(String str, int maxLength) {
        if (str == null) {
            return str;
        }
        str = str.replaceAll("[^0-9A-Za-z]+", "");
        return stripMoreThanMaxLength(str, maxLength);
    }

    /**
     * Remove more than max characters form string.
     * 
     * @param str
     * @param maxLength
     * @return
     */
    public String stripMoreThanMaxLength(String str, int maxLength) {
        if (str == null) {
            return str;
        }
        if (str.length() > maxLength) {
            str = str.substring(0, maxLength);
        }
        return str;
    }

    /*
     * Private Functions and Variables
     */
    protected DBConnector() {
        // No public instantiation
    }
}
