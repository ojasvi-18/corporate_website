package com.zillious.corporate_website.utils;

import java.util.Date;
import java.util.Iterator;
import java.util.Map;

import org.owasp.esapi.filters.SecurityWrapperRequest;

public class JSPUtility {

    public static void repopulateFormParams(SecurityWrapperRequest request) {
        Map<String, String[]> params = (Map<String, String[]>) request.getParameterMap();
        Iterator<String> i = params.keySet().iterator();
        while (i.hasNext()) {
            String key = i.next();
            String value = ((String[]) params.get(key))[0];
            request.setAttribute(key, value);
        }
    }

    public static String writeToJspDefaultUnset(Object obj) {
        return writeToJsp(obj, true, true, true, "Unset");
    }

    public static String writeToJspDefaultEmpty(Object obj) {
        return writeToJsp(obj, true, true, true, "");
    }

    public static String writeToJspDefaultDDMMYYYY(Object obj) {
        if (obj instanceof Date) {
            return writeToJsp((obj == null) ? null : DateUtility.getDateInDDMMYYYY((Date) obj), true, true, true,
                    "dd/mm/yyyy");
        } else {
            return writeToJsp(obj, true, true, true, "dd/mm/yyyy");
        }
    }

    public static String writeToJspDefaultEmptyNoHtml(Object obj) {
        return writeToJsp(obj, true, true, false, "");
    }

    public static String writeToJsp(Object obj, boolean trim, boolean dblQuoteEscape, boolean htmlEscape,
            String defaultOnNullOrEmpty) {
        return writeToJsp(obj, trim, dblQuoteEscape, false, htmlEscape, defaultOnNullOrEmpty);
    }

    public static String writeToJsp(Object obj, boolean trim, boolean dblQuoteEscape, boolean ampEscape,
            boolean htmlEscape, String defaultOnNullOrEmpty) {
        if (obj == null) {
            return defaultOnNullOrEmpty;
        }
        String str = (obj instanceof String) ? ((String) obj) : obj.toString();
        if (str == null) {
            return defaultOnNullOrEmpty;
        }
        if (trim) {
            str = str.trim();
        }
        if (str.length() < 1) {
            return defaultOnNullOrEmpty;
        }

        // Html Escape
        if (dblQuoteEscape) {
            str = str.replaceAll("\"", "&quot;");
        }
        if (htmlEscape) {
            if (ampEscape) {
                str = str.replaceAll("&", "&amp;");
            }
            str = str.replaceAll("<", "&lt;");
            str = str.replaceAll(">", "&gt;");
            str = str.replaceAll("'", "&#39;");
        }
        return str;
    }
}
