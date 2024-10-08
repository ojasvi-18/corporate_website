package com.zillious.corporate_website.logger;

public class Logger {
    org.apache.log4j.Logger m_logger = null;

    public static Logger getInstance(Class clazz) {
        Logger l = new Logger();
        l.m_logger = org.apache.log4j.Logger.getLogger(clazz);
        return l;
    }

    public boolean isDebugEnabled() {
        return isEnabled(LogType.DEBUG);
    }

    public Logger debug(Object s) {
        return log(LogType.DEBUG, s, null);
    }

    public Logger debug(Object s, Throwable t) {
        return log(LogType.DEBUG, s, t);
    }

    public boolean isInfoEnabled() {
        return isEnabled(LogType.INFO);
    }

    public Logger info(Object s) {
        return log(LogType.INFO, s, null);
    }

    public boolean isWarnEnabled() {
        return isEnabled(LogType.WARN);
    }

    public Logger warn(Object s) {
        return log(LogType.WARN, s, null);
    }

    public boolean isErrorEnabled() {
        return isEnabled(LogType.ERROR);
    }

    public Logger error(Object s) {
        return log(LogType.ERROR, s, null);
    }

    public Logger error(Object s, Throwable t) {
        return log(LogType.ERROR, s, t);
    }

    public boolean isFatalEnabled() {
        return isEnabled(LogType.FATAL);
    }

    public Logger fatal(Object s) {
        return log(LogType.FATAL, s, null);
    }

    private Logger log(LogType typ, Object obj, Throwable t) {
        String msg = (obj == null) ? null : obj.toString();
        if (msg == null) {
            msg = "null";
        }

        switch (typ) {
        case DEBUG:
            m_logger.debug(msg);
            break;

        case INFO:
            m_logger.info(msg);
            break;

        case WARN:
            m_logger.warn(msg);
            break;

        case ERROR:
            if (t != null) {
                m_logger.error(msg, t);
            } else {
                m_logger.error(msg);
            }
            break;

        case FATAL:
            m_logger.debug(msg);
            break;
        }

        return this;
    }

    private boolean isEnabled(LogType typ) {
        switch (typ) {
        case DEBUG:
            return m_logger.isDebugEnabled();

        case INFO:
            return m_logger.isInfoEnabled();

        case WARN:
            return true;

        case ERROR:
            return true;

        case FATAL:
            return true;
        }

        return false;
    }

    private static enum LogType {
        DEBUG, INFO, WARN, ERROR, FATAL
    }
}
