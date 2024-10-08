package com.zillious.corporate_website.ui.resources;

import java.util.LinkedHashMap;
import java.util.Map;

import com.zillious.corporate_website.ui.beans.I18NBean;
import com.zillious.corporate_website.ui.security.ZilliousSecurityWrapperRequest;
import com.zillious.corporate_website.ui.util.IntegerUtility;
import com.zillious.lang.ResourceManager;

/**
 * @author Nishant
 * 
 */
public enum QueryTypes {
    ATMQUERY(4, "ATM Inquiry"),

    SALES(1, "Sales"),

    BUSINESS(2, "Business Development Proposal"),

    CORPORATE(3, "Corporate Information Inquiry"),

    OTHERS(0, "Any Other Query");

    private String m_def;
    private int    m_queryId;

    QueryTypes(int id, String def) {
        m_queryId = id;
        m_def = def;
    }

    public static Map<String, String> getContactUsPageQueryTypes(ZilliousSecurityWrapperRequest request) {
        ResourceManager manager = new ResourceManager(I18NBean.getLocaleBundleFromRequest(request));
        Map<String, String> types = new LinkedHashMap<String, String>();
        for (QueryTypes type : QueryTypes.values()) {
            types.put(String.valueOf(type.getQueryId()),
                    manager.transformText("corp.contact.querytype", type.name(), type.getDef()));
        }
        return types;
    }

    public String getDef() {
        return m_def;
    }

    public void setDef(String def) {
        m_def = def;
    }

    public int getQueryId() {
        return m_queryId;
    }

    public void setQueryId(int queryId) {
        m_queryId = queryId;
    }

    public static QueryTypes getQueryType(String query) {
        int queryId = IntegerUtility.parseIntWithDefaultOnError(query, -1);
        if (queryId == -1) {
            return null;
        }
        for (QueryTypes type : QueryTypes.values()) {
            if (queryId == type.getQueryId()) {
                return type;
            }
        }
        return null;
    }

    public static void main(String[] args) {
        for (QueryTypes type : QueryTypes.values()) {
            System.out.println("corp.contact.querytype_" + type.name() + "=");
        }
    }
}
