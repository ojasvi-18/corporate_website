package com.zillious.corporate_website.utils;

import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.text.NumberFormat;

import org.owasp.esapi.ESAPI;

import com.zillious.corporate_website.logger.Logger;

public class HtmlUtility {
    private static final Logger s_logger       = Logger.getInstance(HtmlUtility.class);
    private static final String DECIMAL_FORMAT = "############.##";

    public static String encodeForHTMLStructuredCode(String html) {
        return html;
    }

    public static String encodeForHTML(BigDecimal value) {
        if (value == null) {
            return "0";
        }
        if (value.abs().compareTo(new BigDecimal(9999999)) > 0) {
            NumberFormat formatter = new DecimalFormat(DECIMAL_FORMAT);
            return encodeForHTML(formatter.format(value.doubleValue()));
        }
        return encodeForHTML(value.doubleValue());
    }

    public static String encodeForHTML(char value) {
        return encodeForHTML("" + value, "");
    }

    public static String encodeForHTML(int value) {
        return encodeForHTML("" + value, "");
    }

    public static String encodeForHTML(double value) {
        if (value > 9999999 || ((value * (-1)) > 9999999)) {
            NumberFormat formatter = new DecimalFormat(DECIMAL_FORMAT);
            return encodeForHTML("" + formatter.format(value), "");
        }
        return encodeForHTML("" + value, "");
    }

    public static String encodeForHTML(long value) {
        return encodeForHTML("" + value, "");
    }

    public static String encodeForHTML(boolean value) {
        return encodeForHTML("" + value, "");
    }

    public static String encodeForHTML(String value) {
        return encodeForHTML(value, "");
    }

    public static String encodeForHTML(String value, String nullValue) {
        if (value == null) {
            return nullValue;
        }
        value = ESAPI.encoder().encodeForHTML(value);
        return value;
    }

    public static String encodeForHTMLAttribute(BigDecimal value) {
        if (value == null) {
            return "0";
        }

        if (value.abs().compareTo(new BigDecimal(9999999)) > 0) {
            NumberFormat formatter = new DecimalFormat(DECIMAL_FORMAT);
            return encodeForHTMLAttribute(formatter.format(value.doubleValue()));
        }
        return encodeForHTMLAttribute(value.doubleValue());
    }

    public static String encodeForHTMLAttribute(char value) {
        return encodeForHTMLAttribute("" + value, "");
    }

    public static String encodeForHTMLAttribute(int value) {
        return encodeForHTMLAttribute("" + value, "");
    }

    public static String encodeForHTMLAttribute(long value) {
        return encodeForHTMLAttribute("" + value, "");
    }

    public static String encodeForHTMLAttribute(double value) {
        if (value > 9999999 || ((value * (-1)) > 9999999)) {
            NumberFormat formatter = new DecimalFormat(DECIMAL_FORMAT);
            return encodeForHTMLAttribute("" + formatter.format(value), "");
        }
        return encodeForHTMLAttribute("" + (value), "");
    }

    public static String encodeForHTMLAttribute(boolean value) {
        return encodeForHTMLAttribute("" + value, "");
    }

    public static String encodeForHTMLAttribute(String value) {
        return encodeForHTMLAttribute(value, "");
    }

    public static String encodeForHTMLAttribute(String value, String nullValue) {
        if (value == null) {
            return nullValue;
        }
        value = ESAPI.encoder().encodeForHTMLAttribute(value);
        return value;
    }

    public static String encodeForJavaScript(BigDecimal value) {
        if (value == null) {
            return "0";
        }
        if (value.abs().compareTo(new BigDecimal(9999999)) > 0) {
            NumberFormat formatter = new DecimalFormat(DECIMAL_FORMAT);
            return encodeForJavaScript(formatter.format(value.doubleValue()));
        }
        return encodeForJavaScript(value.doubleValue());
    }

    public static String encodeForJavaScript(char value) {
        return ("" + value);
    }

    public static String encodeForJavaScript(int value) {
        return ("" + value);
    }

    public static String encodeForJavaScript(double value) {
        if (value > 9999999 || ((value * (-1)) > 9999999)) {
            NumberFormat formatter = new DecimalFormat(DECIMAL_FORMAT);
            return ("" + formatter.format(value));
        }
        return ("" + value);
    }

    public static String encodeForJavaScript(boolean value) {
        return encodeForJavaScript("" + value, "");
    }

    public static String encodeForJavaScript(long value) {
        return ("" + value);
    }

    public static String encodeForJavaScript(String value) {
        return encodeForJavaScript(value, true);
    }

    public static String encodeForJavaScript(String value, boolean isEncodingRequired) {
        return encodeForJavaScript(value, "", isEncodingRequired);
    }

    public static String encodeForJavaScript(String value, String nullValue) {
        return encodeForJavaScript(value, nullValue, true);
    }

    public static String encodeForJavaScript(String value, String nullValue, boolean isEncodingRequired) {
        if (value == null) {
            return nullValue;
        }
        if (isEncodingRequired) {
            value = ESAPI.encoder().encodeForJavaScript(value);
        }
        return value;
    }

    public static String encodeForCSS(String value) {
        value = ESAPI.encoder().encodeForCSS(value);
        return value;
    }

    public static String encodeForURL(String value) {
        try {
            value = ESAPI.encoder().encodeForURL(value);
            return value;
        } catch (Exception e) {
            s_logger.error("Error in encodingForURL v-" + value, e);
        }
        return "";
    }

    // Old

    public static String convertToHTML(String text) {
        return convertToHTML(text, true, true);
    }

    public static String convertToHTML(Object obj) {
        if (obj == null) {
            return null;
        }
        if (obj instanceof String) {
            return convertToHTML((String) obj, true, true);
        } else {
            return convertToHTML(obj.toString(), true, true);
        }
    }

    public static String convertToHTML(String text, boolean escapeNewLines, boolean escapeSpecialChars) {
        if (text == null) {
            return null;
        }
        if (escapeNewLines) {
            text = text.replaceAll("\r\n", "<br>");
            text = text.replaceAll("\r", "<br>");
            text = text.replaceAll("\n", "<br>");
        }
        if (escapeSpecialChars) {
            text = text.replaceAll("&", "&amp;");
            text = text.replaceAll("<", "&lt;");
            text = text.replaceAll(">", "&gt;");
            text = text.replaceAll("'", "&#39;");
            text = text.replaceAll("\"", "&quot;");
        }
        return text;
    }

    public static String convertBracketsQuoteToHTML(String text) {
        if (text == null) {
            return null;
        }
        text = text.replaceAll("&", "&amp;");
        text = text.replaceAll("<", "&lt;");
        text = text.replaceAll(">", "&gt;");
        text = text.replaceAll("\"", "&quot;");
        return text;
    }

    public static String convertQuoteToHTML(Object obj) {
        return convertQuoteToHTML(obj, null);
    }

    public static String convertQuoteToHTML(Object obj, String nullValue) {
        if (obj == null) {
            return nullValue;
        }
        String str = (obj instanceof String) ? ((String) obj) : obj.toString();
        str = str.replaceAll("\"", "&quot;");
        return str;
    }

    public static String escapeJS(String text, String forNull, boolean escapeSingleQuote, boolean escapeDoubleQuote) {
        if (text == null) {
            return forNull;
        }
        text = text.replaceAll("[\\\\]", "\\\\\\\\");
        text = text.replaceAll("\r\n", "\\\\n");
        text = text.replaceAll("\n\r", "\\\\n");
        text = text.replaceAll("\r", "\\\\n");
        text = text.replaceAll("\n", "\\\\n");
        text = text.replaceAll("\t", "\\\\t");
        text = text.replaceAll("\b", "");
        text = text.replaceAll("\f", "");
        text = text.replaceAll("<", "&lt;");
        text = text.replaceAll(">", "&gt;");
        if (escapeSingleQuote) {
            text = text.replaceAll("[']", "\\\\'");
        }
        if (escapeDoubleQuote) {
            text = text.replaceAll("[\"]", "\\\\\"");
        }
        return text;
    }

    public static String quoteJson(String string) {
        if (string == null || string.length() == 0) {
            return "";
        }

        char b;
        char c = 0;
        String hhhh;
        int i;
        int len = string.length();
        StringBuffer sb = new StringBuffer(len + 4);

        for (i = 0; i < len; i += 1) {
            b = c;
            c = string.charAt(i);
            switch (c) {
            case '\\':
            case '"':
                sb.append('\\');
                sb.append(c);
                break;
            case '/':
                if (b == '<') {
                    sb.append('\\');
                }
                sb.append(c);
                break;
            case '\b':
                sb.append("\\b");
                break;
            case '\t':
                sb.append("\\t");
                break;
            case '\n':
                sb.append("\\n");
                break;
            case '\f':
                sb.append("\\f");
                break;
            case '\r':
                sb.append("\\r");
                break;
            default:
                if (c < ' ' || (c >= '\u0080' && c < '\u00a0') || (c >= '\u2000' && c < '\u2100')) {
                    hhhh = "000" + Integer.toHexString(c);
                    sb.append("\\u" + hhhh.substring(hhhh.length() - 4));
                } else {
                    sb.append(c);
                }
            }
        }
        return sb.toString();
    }

    private static String mark = "-_.!~*'()\"";

    public static String encodeURIComponent(String argString) {
        StringBuffer uri = new StringBuffer(); // Encoded URL

        char[] chars = argString.toCharArray();
        for (int i = 0; i < chars.length; i++) {
            char c = chars[i];
            if ((c >= '0' && c <= '9') || (c >= 'a' && c <= 'z') || (c >= 'A' && c <= 'Z') || mark.indexOf(c) != -1) {
                uri.append(c);
            } else {
                uri.append("%");
                uri.append(Integer.toHexString((int) c));
            }
        }
        return uri.toString();
    }

    private HtmlUtility() {
    }

    public static void main(String[] args) {
        String str = "t\\est\ne";
        System.out.println(str);
        System.out.println(escapeJS(str, "", true, true));
    }
}
