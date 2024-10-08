package com.zillious.corporate_website.ui.navigation;

/**
 * @author Nishant
 * 
 */
public class WebsiteException extends Exception {

    private static final long    serialVersionUID = 1692031980853416381L;
    private Object               m_support        = null;
    private WebsiteExceptionType m_type           = null;

    public WebsiteException(WebsiteExceptionType type, Object supportObject) {
        this(type);
        m_support = supportObject;
    }

    public WebsiteException(WebsiteExceptionType type) {
        super(type.getDesc());
        m_type = type;
    }

    public Object getSupport() {
        return m_support;
    }

    public void setSupport(Object support) {
        m_support = support;
    }

    public WebsiteExceptionType getType() {
        return m_type;
    }

    public void setType(WebsiteExceptionType type) {
        m_type = type;
    }

}
