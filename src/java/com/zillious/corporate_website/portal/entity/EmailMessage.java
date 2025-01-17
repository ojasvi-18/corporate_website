package com.zillious.corporate_website.portal.entity;

import java.util.HashSet;
import java.util.Set;

/**
 * @author nishant.gupta
 *
 */
public class EmailMessage {

    private Set<String> m_to;
    private Set<String> m_cc;
    private String      m_subject;
    private String      m_content;
    private String      m_from;

    private String      m_contentType;

    public EmailMessage() {
        m_contentType = "text/plain";
    }

    public EmailMessage(String contentType) {
        if (contentType != null) {
            m_contentType = contentType;
        }
    }

    /**
     * @return the content
     */
    public String getContent() {
        return m_content;
    }

    /**
     * @param content the content to set
     */
    public void setContent(String content) {
        m_content = content;
    }

    public void addTo(String email) {
        if (email == null) {
            return;
        }

        if (m_to == null) {
            m_to = new HashSet<String>();
        }

        m_to.add(email);

    }

    public void addCC(String email) {
        if (email == null) {
            return;
        }

        if (m_cc == null) {
            m_cc = new HashSet<String>();
        }

        m_cc.add(email);
    }

    /**
     * @return the subject
     */
    public String getSubject() {
        return m_subject;
    }

    /**
     * @param subject the subject to set
     */
    public void setSubject(String subject) {
        m_subject = subject;
    }

    public void setFrom(String fromEmail) {
        m_from = fromEmail;
    }

    /**
     * @return the from
     */
    public String getFrom() {
        return m_from;
    }

    /**
     * @return the to
     */
    public Set<String> getTo() {
        return m_to;
    }

    /**
     * @return the cc
     */
    public Set<String> getCc() {
        return m_cc;
    }

    public String getContentType() {
        return m_contentType;
    }

    public void setContentType(String contentType) {
        m_contentType = contentType;
    }

}
