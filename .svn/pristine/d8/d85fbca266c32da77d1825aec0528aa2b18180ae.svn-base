package com.zillious.taglibs;

import java.io.IOException;
import java.io.StringWriter;
import java.util.ResourceBundle;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.jsp.JspException;
import javax.servlet.jsp.JspWriter;
import javax.servlet.jsp.PageContext;
import javax.servlet.jsp.tagext.SimpleTagSupport;

import org.apache.log4j.Logger;

import com.zillious.corporate_website.ui.beans.I18NBean;
import com.zillious.corporate_website.ui.beans.SecurityBean;
import com.zillious.corporate_website.ui.security.ZilliousSecurityWrapperRequest;

public class ZilliousMessageTag extends SimpleTagSupport {
    private String              m_key;
    private static final Logger s_logger           = Logger.getLogger(ZilliousMessageTag.class);

    // Should not be static

    public void setKey(String msg) {
        this.m_key = msg;
    }

    StringWriter sw = new StringWriter();

    /*
     * The properties file will be looked in order :ButtonLabel_fr_CA_UNIX
     * ButtonLabel_fr_CA ButtonLabel_fr ButtonLabel_en_US ButtonLabel_en
     * ButtonLabel "
     */
    public void doTag() throws JspException, IOException {
        boolean foundMatch = false;
        JspWriter out = getJspContext().getOut();
        PageContext pageContext = (PageContext) getJspContext();
        HttpServletRequest request = (HttpServletRequest) pageContext.getRequest();
        ZilliousSecurityWrapperRequest zilliousRequest = SecurityBean.getRequest(request);
        ResourceBundle localeLangRB = I18NBean.getLocaleBundleFromRequest(zilliousRequest);
        if (m_key != null) {
            if (localeLangRB != null) {
                foundMatch = getPropertyValueFromResourceBundle(localeLangRB, out);
            } else {
                s_logger.info("Locale and Country property not found");
            }
            try {
                if (!foundMatch) {
                    // Message from the JSP body
                    getJspBody().invoke(sw);
                    getJspContext().getOut().print(sw.toString());
                }
            } catch (Exception e) {
                getJspContext().getOut().print(m_key.toString());
                s_logger.error("Exception while getting value for Key:" + this.m_key, e);
            }
        }

    }

    private boolean getPropertyValueFromResourceBundle(ResourceBundle labels, JspWriter out) throws IOException {
        boolean foundMatch = false;
        if (labels.containsKey(m_key)) {
            foundMatch = true;
            out.print(new String(labels.getString(m_key).getBytes("ISO-8859-1"), "UTF-8"));
        }
        return foundMatch;
    }

}
