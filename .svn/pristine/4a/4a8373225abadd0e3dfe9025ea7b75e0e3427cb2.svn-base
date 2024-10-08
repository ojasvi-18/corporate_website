package com.zillious.corporate_website.utils;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.zip.GZIPInputStream;
import java.util.zip.GZIPOutputStream;

import org.apache.commons.codec.binary.Base64;
import org.apache.commons.lang.StringEscapeUtils;
import org.owasp.esapi.ESAPI;

public class StringUtility {
    public static final String   EMPTY_STRING = "";
    private static final String  SPL_CHARS    = "!@#$%&*";
    public static final String[] DELIM        = { "|", "$", "@", ":", "~", "`", "!" };
    public static final String[] DELIM_REGEX  = { "[|]", "[$]", "[@]", "[:]", "[~]", "[`]", "[!]" };
    public static final String   NEW_LINE     = "\r\n";

    private StringUtility() {
    }

    public static String clearStringForSensitiveInformation(String str) {
        if (str == null) {
            return null;
        }
        str = str.replaceAll("[0-9]{16}", "XXXXXXXXXXXXXXXX");
        str = str.replaceAll("[0-9]{15}", "XXXXXXXXXXXXXXX");
        return str;
    }

    public static int parseIntAndNullEmptyIsMinus1(String str) {
        if (str == null || str.length() < 1) {
            return -1;
        } else {
            return Integer.parseInt(str);
        }
    }

    public static String getDelim(int level) {
        return DELIM[level];
    }

    public static String getDelimRegex(int level) {
        return DELIM_REGEX[level];
    }

    public static int parseIntAndNullEmptyIsZero(String str) {
        if (str == null || str.length() < 1) {
            return 0;
        } else {
            return Integer.parseInt(str);
        }
    }

    public static String getNewLineString() {
        return System.getProperty("line.separator");
    }

    public static boolean isValidPassword(String str, boolean isSplCharReq) {
        boolean isValid = false;
        str = trimAndEmptyIsNull(str);
        if (str != null) {
            if (isSplCharReq) {
                isValid = str.matches("^.*(?=.{8,})(?=.*[a-z])(?=.*[A-Z])(?=.*[\\d])(?=.*[\\W\\_])(?=\\S+$).*$");

            } else {
                isValid = str.matches("^\\S{8,}$");
            }
        }
        return isValid;
    }

    public static boolean isValidEmail(String str) {
        return (str == null) ? false
                : str.matches(
                        "^[a-zA-Z0-9]+([a-zA-Z0-9_]+(\\.){0,1})*[a-zA-Z0-9]+@([a-zA-Z0-9]+(\\.){1})+[a-zA-Z0-9]+$");
    }

    public static boolean isValidName(String str) {
        return (str == null) ? false : str.matches("^[a-zA-Z\\s]{1,}$");
    }

    public static boolean isValidFullName(String str) {
        return (str == null) ? false : str.matches("^[\\w\\s]{1,}$");
    }

    public static boolean isNumeric(String str) {
        boolean isValid = false;
        str = trimAndEmptyIsNull(str);
        if (str == null) {
            return isValid;
        }
        String passRegex = "[0-9]{1,}";
        isValid = str.matches(passRegex);
        return isValid;
    }

    public static String trimCleanToAlphaNumbericNullisEmpty(String str) {
        if (str == null) {
            return EMPTY_STRING;
        }
        return str.replaceAll("[^0-9a-zA-Z]", EMPTY_STRING).trim();
    }

    public static String trimCleanToAlphaNullisEmpty(String str) {
        if (str == null) {
            return EMPTY_STRING;
        }
        return str.replaceAll("[^a-zA-Z ]", EMPTY_STRING).trim();
    }

    public static String pad(String str, int len) {
        if (str == null) {
            str = EMPTY_STRING;
        }
        return pad(str, "0", len, true);
    }

    public static String pad(String str, String padWith, int len, boolean padLeftNotRight) {
        while (str.length() < len) {
            if (padLeftNotRight) {
                str = padWith + str;
            } else {
                str = str + padWith;
            }
        }
        return str;
    }

    public static String substring(String text, String prefixRegex, String suffixRegex, String includes) {
        Pattern prefixPattern = Pattern.compile(prefixRegex);
        Pattern suffixPattern = Pattern.compile(suffixRegex);
        Matcher prefixMatcher = prefixPattern.matcher(text);
        while (prefixMatcher.find()) {
            Matcher suffixMatcher = suffixPattern.matcher(text);
            while (suffixMatcher.find()) {
                if (suffixMatcher.start() > prefixMatcher.end()) {
                    String temp = text.substring(prefixMatcher.end(), suffixMatcher.start());
                    if (includes != null) {
                        if (temp.contains(includes))
                            return temp;
                        else
                            return null;
                    } else {
                        return temp;
                    }
                }
            }
        }
        return null;
    }

    public static boolean isNonEmpty(String str) {
        if (str != null && str.length() > 0) {
            return true;
        }
        return false;
    }

    public static String trimAndEmptyIsNull(String str) {
        if (str == null) {
            return null;
        }
        str = str.trim();
        if (str.length() < 1) {
            return null;
        }
        return str;
    }

    public static String trimAndNullIsEmpty(String str) {
        if (str == null) {
            return EMPTY_STRING;
        }
        str = str.trim();
        return str;
    }

    public static String trimAndNullIsEmpty(StringBuilder str) {
        if (str == null) {
            return EMPTY_STRING;
        }
        return trimAndNullIsEmpty(new String(str));
    }

    public static String trimAndNullIsEmpty(Integer a) {
        if (a == null) {
            return EMPTY_STRING;
        }

        return a.toString();
    }

    public static String trimAndNullIsEmpty(Double a) {
        if (a == null) {
            return EMPTY_STRING;
        }

        return a.toString();
    }

    public static boolean equalsWithTrimAndBothNullCheck(String str1, String str2) {
        if (str1 == null && str2 == null) {
            return true;
        }
        if (str1 != null) {
            str1 = str1.trim();
        } else {
            return false;
        }
        if (str2 != null) {
            str2 = str2.trim();
        } else {
            return false;
        }
        return str1.equals(str2);
    }

    public static boolean equalsIgnoreCaseWithTrimAndBothNullCheck(String str1, String str2) {
        if (str1 == null && str2 == null) {
            return true;
        }
        if (str1 != null) {
            str1 = str1.trim();
        } else {
            return false;
        }
        if (str2 != null) {
            str2 = str2.trim();
        } else {
            return false;
        }
        return str1.equalsIgnoreCase(str2);
    }

    public static char getRandomAplanumeric() {
        // Generate random number 0-35 (both inclusive)
        int random = (int) Math.ceil(Math.random() * 35);
        if (random < 10) {
            return (char) ('0' + random);
        } else {
            int randomCase = (int) Math.ceil(Math.random() * 2);
            if (randomCase == 1) {
                return (char) ('a' + (random - 10));
            } else {
                return (char) ('A' + (random - 10));
            }
        }
    }

    public static String getRandomAplanumerics(int size) {
        StringBuilder result = new StringBuilder();
        for (int i = 0; i < size; i++) {
            int random = (int) Math.ceil(Math.random() * 35);
            if (random < 10) {
                result.append((char) ('0' + random));
            } else {
                int randomCase = (int) Math.ceil(Math.random() * 2);
                if (randomCase == 1) {
                    result.append((char) ('a' + (random - 10)));
                } else {
                    result.append((char) ('A' + (random - 10)));
                }
            }
        }
        return result.toString();
    }

    public static char getRandomSplChar() {
        int random = (int) Math.ceil(Math.random() * (SPL_CHARS.length() - 1));
        return SPL_CHARS.charAt(random);
    }

    public static int getRandomNumber() {
        return (int) Math.floor(Math.random() * 10);
    }

    public static String getRandomNumbers(int size) {
        StringBuilder result = new StringBuilder();
        for (int i = 0; i < size; i++) {
            result.append((char) ('0' + getRandomNumber()));
        }
        return result.toString();
    }

    public static char getRandomUpperCaseChar() {
        int random = (int) Math.ceil(Math.random() * 25);
        return (char) ('A' + random);
    }

    public static char getRandomLowerCaseChar() {
        int random = (int) Math.ceil(Math.random() * 25);
        return (char) ('a' + random);
    }

    public static char getRandomNumberChar() {
        int random = (int) Math.ceil(Math.random() * 9);
        return (char) ('0' + random);
    }

    public static String join(Map<String, Set<String>> map, String delim) {
        if (map == null || map.size() < 1) {
            return EMPTY_STRING;
        }
        StringBuilder b = new StringBuilder();
        for (Map.Entry<String, Set<String>> entry : map.entrySet()) {
            b.append(join(entry.getValue(), delim));
        }
        return b.toString();
    }

    public static String join(String[] str, String delim) {
        if (str == null || str.length < 1) {
            return EMPTY_STRING;
        }
        StringBuilder b = new StringBuilder();
        b.append(str[0]);
        for (int i = 1; i < str.length; i++) {
            b.append(delim).append(str[i]);
        }
        return b.toString();
    }

    public static String join(Object[] str, String delim) {
        if (str == null || str.length < 1) {
            return EMPTY_STRING;
        }
        StringBuilder b = new StringBuilder();
        b.append(str[0]);
        for (int i = 1; i < str.length; i++) {
            b.append(delim).append(str[i]);
        }
        return b.toString();
    }

    public static String join(int[] str, String delim) {
        if (str == null || str.length < 1) {
            return EMPTY_STRING;
        }
        StringBuilder b = new StringBuilder();
        b.append(str[0]);
        for (int i = 1; i < str.length; i++) {
            b.append(delim).append(str[i]);
        }
        return b.toString();
    }

    public static String join(List<Integer> list, String delim) {
        if (list == null || list.size() < 1) {
            return EMPTY_STRING;
        }
        StringBuilder b = new StringBuilder();
        b.append(list.get(0));
        for (int i = 1; i < list.size(); i++) {
            b.append(delim).append(list.get(i));
        }
        return b.toString();
    }

    public static String joinWithDelimBothEnd(List<Integer> list, String delim) {
        if (list == null || list.size() < 1) {
            return null;
        }
        StringBuilder b = new StringBuilder();
        b.append(delim);
        b.append(list.get(0));
        for (int i = 1; i < list.size(); i++) {
            b.append(delim).append(list.get(i));
        }
        b.append(delim);
        return b.toString();
    }

    public static String join(Collection< ? extends Object> list, String delim1, String delim2) {
        if (list == null || list.size() < 1) {
            return EMPTY_STRING;
        }
        StringBuilder b = new StringBuilder();
        Iterator< ? extends Object> itr = list.iterator();
        while (itr.hasNext()) {
            if (b.length() > 0) {
                b.append(delim1);
            }
            b.append(delim2).append(itr.next().toString()).append(delim2);
        }
        return b.toString();
    }

    public static String join(List<int[]> list, String delim1, String delim2) {
        if (list == null || list.size() < 1) {
            return EMPTY_STRING;
        }
        StringBuilder b = new StringBuilder();
        b.append(StringUtility.join(list.get(0), delim2));
        for (int i = 1; i < list.size(); i++) {
            b.append(delim1).append(StringUtility.join(list.get(i), delim2));
        }
        return b.toString();
    }

    public static String joinNullForEmpty(String[] str, String delim) {
        if (str == null || str.length < 1) {
            return null;
        }
        StringBuilder b = new StringBuilder();
        b.append(str[0]);
        for (int i = 1; i < str.length; i++) {
            b.append(delim).append(str[i]);
        }
        return b.toString();
    }

    public static String[] splitNullForEmpty(String str, String regex) {
        if (str == null || str.length() < 1) {
            return null;
        }
        return str.split(regex, -1);
    }

    public static String trimAndRemoveStartingZeros(String str) {
        if (str == null || str.length() < 1) {
            return null;
        }
        str = str.trim().replaceAll("^[0]+", EMPTY_STRING);
        if (str.length() < 1) {
            return "0";
        } else {
            return str;
        }
    }

    public static String joinWithHtmlEscape(Collection< ? extends Object> col, String delim) {
        if (col == null) {
            return EMPTY_STRING;
        }

        StringBuilder b = new StringBuilder();
        for (Object o : col) {
            if (b.length() > 0) {
                b.append(delim);
            }
            if (o != null) {
                b.append(JSPUtility.writeToJspDefaultEmpty(o));
            }
        }
        return b.toString();
    }

    public static String removeHtmltags(String string) {
        if (string == null) {
            return null;
        }
        string = string.replaceAll("\\<.*?\\>", EMPTY_STRING);
        return (string);
    }

    public static String join(Collection< ? extends Object> col, String delim) {

        if (col == null) {
            return EMPTY_STRING;
        }
        StringBuilder b = new StringBuilder();
        for (Object o : col) {
            if (b.length() > 0) {
                b.append(delim);
            }
            b.append(o != null ? o.toString() : EMPTY_STRING);
        }
        return b.toString();
    }

    public static String joinEmptyForNull(String[] col, String delim) {
        if (col == null || col.length < 1) {
            return EMPTY_STRING;
        }
        StringBuilder b = new StringBuilder();
        for (String s : col) {
            if (b.length() > 0) {
                b.append(delim);
            }
            b.append(s == null ? EMPTY_STRING : s);
        }
        return b.toString();
    }

    public static String joinEmptyForNull(Collection<String> col, String delim) {
        StringBuilder b = new StringBuilder();
        for (String s : col) {
            if (b.length() > 0) {
                b.append(delim);
            }
            b.append(s == null ? EMPTY_STRING : s);
        }
        return b.toString();
    }

    public static String toJSONArray(Collection<String> col) {
        StringBuilder b = new StringBuilder();
        b.append("[");
        for (Object o : col) {
            if (b.length() > 1) {
                b.append(",");
            }
            b.append("\"").append(HtmlUtility.encodeForJavaScript(o.toString())).append("\"");
        }
        b.append("]");
        return b.toString();
    }

    public static String toJSONArray(Object[] col) {
        StringBuilder b = new StringBuilder();
        b.append("[");
        if (col != null) {
            for (Object o : col) {
                if (b.length() > 1) {
                    b.append(",");
                }
                b.append("\"").append(HtmlUtility.encodeForJavaScript(o.toString())).append("\"");
            }
        }
        b.append("]");
        return b.toString();
    }

    public static int compare(String str1, String str2) {
        if (str1 == null || str2 == null) {
            if (str1 == null && str2 == null) {
                return 0;
            }
            if (str1 == null) {
                return 1;
            }
            if (str2 == null) {
                return -1;
            }
        }
        return str1.compareTo(str2);
    }

    public static class StringArrComparator implements Comparator<String[]> {
        private int m_idx = -1;

        public StringArrComparator(int srtIdx) {
            m_idx = srtIdx;
        }

        public int compare(String[] s1, String[] s2) {
            return s1[m_idx].compareTo(s2[m_idx]);
        }
    }

    /**
     * Gzip the input string into a byte[].
     * 
     * @param input
     * @return
     * @throws IOException
     */
    public static String zipStringToBytes(String input) throws IOException {
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        BufferedOutputStream bufos = new BufferedOutputStream(new GZIPOutputStream(bos));
        bufos.write(input.getBytes());
        bufos.close();
        byte[] retval = bos.toByteArray();
        bos.close();
        return new String(Base64.encodeBase64(retval));
    }

    /**
     * Unzip a string out of the given gzipped byte array.
     * 
     * @param bytes
     * @return
     * @throws IOException
     */
    public static String unzipStringFromBytes(byte[] bytes) throws IOException {
        ByteArrayInputStream bis = new ByteArrayInputStream(bytes);
        BufferedInputStream bufis = new BufferedInputStream(new GZIPInputStream(bis));
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        byte[] buf = new byte[1024];
        int len;
        while ((len = bufis.read(buf)) > 0) {
            bos.write(buf, 0, len);
        }
        String retval = bos.toString();
        bis.close();
        bufis.close();
        bos.close();
        return new String(Base64.decodeBase64(retval.getBytes()));
    }

    public static List<String> splitToStringList(String serialized, String delim) {
        if (serialized == null || serialized.length() < 1) {
            return new ArrayList<String>();
        }
        List<String> res = new ArrayList<String>();
        String[] keys = serialized.split(delim);
        for (String key : keys) {
            res.add(key);
        }
        return res;
    }

    public static Set<String> splitToStringSet(String serialized, String delim) {
        if (serialized == null || serialized.length() < 1) {
            return new HashSet<String>();
        }

        Set<String> res = new HashSet<String>();
        String[] keys = serialized.split(delim);
        for (String key : keys) {
            res.add(key);
        }
        return res;
    }

    public static List<Integer> convertSetIntoList(Set<Integer> setInt) {
        List<Integer> listInt = new ArrayList<Integer>();
        if (setInt != null) {
            for (Integer num : setInt) {
                listInt.add(num);
            }
        }
        return listInt;
    }

    public static List<Integer> splitToIntList(String serialized, String delim) {
        if (serialized == null || serialized.length() < 1) {
            return new ArrayList<Integer>();
        }
        List<Integer> res = new ArrayList<Integer>();
        String[] keys = serialized.split(delim);
        for (String key : keys) {
            res.add(Integer.valueOf(key));
        }
        return res;
    }

    public static String trimWithNullAndEmptyAsUnset(String str) {
        str = StringUtility.trimAndEmptyIsNull(str);
        if (str == null || str.length() < 0) {
            return "Unset";
        }
        return str;
    }

    public static String trimToSize(String str, int size) {
        return trimToSize(str, size, false);
    }

    public static String trimToSize(String str, int size, boolean isTrimFromLeft) {
        if (str == null || str.length() <= size) {
            return str;
        }
        if (!isTrimFromLeft) {
            return str.substring(0, size);
        }
        return str.substring(str.length() - size);
    }

    public static String trimToSizeAndNullIsEmpty(String str, int size) {
        if (str == null) {
            return EMPTY_STRING;
        } else if (size == -1 || str.length() <= size) {
            return str;
        }
        return str.substring(0, size);
    }

    public static String trimToSizeAndNullIsDefault(String str, int size, String def) {
        if (str == null) {
            return def;
        } else if (str.length() <= size) {
            return str;
        }
        return str.substring(0, size);
    }

    public static String trimNoSpecialSizeAndNullIsDefault(String str, int size, String def) {
        if (str == null) {
            return def;
        }
        str = str.replaceAll("[^\\w\\d\\s]", EMPTY_STRING).replaceAll("[\\s]+", " ").trim();
        if (str.length() < 1) {
            return def;
        }
        if (size == -1 || str.length() <= size) {
            return str;
        }
        return str.substring(0, size);
    }

    public static String convertToJavascriptArray(List<Integer> intList) {
        StringBuffer buffer = new StringBuffer();
        buffer.append("[ ");
        if (intList != null && intList.size() > 0) {
            buffer.append(intList.get(0));
            for (int i = 1; i < intList.size(); i++) {
                buffer.append(", ").append(intList.get(i));
            }
        }
        buffer.append(" ]");
        return buffer.toString();
    }

    public static String[] toEmailArr(String emails) {
        if (emails == null) {
            return null;
        }
        String[] toEmailArr = emails.split("[,;]");
        List<String> emailList = new ArrayList<String>(toEmailArr.length);
        for (int i = 0; i < toEmailArr.length; i++) {
            toEmailArr[i] = StringUtility.trimAndEmptyIsNull(toEmailArr[i]);
            if (toEmailArr[i] != null) {
                emailList.add(toEmailArr[i]);
            }
        }
        if (emailList.size() < 1) {
            return null;
        }
        return (String[]) emailList.toArray(new String[0]);
    }

    public static String cleanForDatabase(String str) {
        if (str == null) {
            return str;
        }
        return str.replaceAll("[\\n\\r\\t\"']+", EMPTY_STRING);
    }

    public static String cleanForJson(String str) {
        if (str == null) {
            return EMPTY_STRING;
        }
        return str.replaceAll("[\\n\\r\\t\"\\']+", EMPTY_STRING);
    }

    public static String removeLeadingZeroes(String str) {
        if (str == null) {
            return null;
        }
        String finalString = EMPTY_STRING;
        try {
            Integer asInt = Integer.parseInt(str);
            finalString = asInt.toString();
        } catch (Exception e) {

        }
        return finalString;
    }

    public static String removeLeadingCharacter(String str, char c) {
        if (str == null) {
            return null;
        }
        String finalString = EMPTY_STRING;
        try {

            int index = 0;
            for (index = 0; index < str.length(); index++) {
                if (str.charAt(index) != c) {
                    break;
                }
            }
            finalString = str.substring(index);

        } catch (Exception e) {

        }
        return finalString;
    }

    public static String removeAllMatchingFromLongString(String orig, String[] keywords) {
        if (orig == null) {
            return null;
        }

        int i = -1;
        StringBuilder b = new StringBuilder(orig);
        for (String keyword : keywords) {
            keyword = StringUtility.trimAndEmptyIsNull(keyword);
            if (keyword != null) {
                while ((i = b.indexOf(keyword)) != -1) {
                    b.delete(i, i + keyword.length());
                }
            }
        }
        return b.toString();
    }

    public static String replaceAllMatchingFromLongString(String orig, String[] keywords) {
        if (orig == null) {
            return null;
        }
        if (keywords == null || keywords.length < 1) {
            return orig;
        }

        int i = -1;
        StringBuilder b = new StringBuilder(orig);
        for (String keyword : keywords) {
            keyword = StringUtility.trimAndEmptyIsNull(keyword);
            if (keyword != null) {
                while ((i = b.indexOf(keyword)) != -1) {
                    b.replace(i, i + keyword.length(), "XXX");
                }
            }
        }
        return b.toString();
    }

    public static String[] removeAndEscapeHtmlTagExceptBreak(String HTMLString) {
        String[] replacedString = StringEscapeUtils.escapeHtml(HTMLString.replaceAll("\\r|\\n", " ")
                .replaceAll("\\<br.*?/>", "EndLine").replaceAll("\\<p.*?>", "EndLine")
                .replaceAll("\\<li.*?>", "EndLine").replaceAll("\\<.*?>", EMPTY_STRING)).replaceAll("&nbsp;", "&#160;")
                .split("EndLine");
        return replacedString;
    }

    public static String[] replaceBreakIntoArray(String HTMLString) {
        String[] replacedString = HTMLString.replaceAll("\\r|\\n", " ").replaceAll("\\<br.*?/>", "EndLine")
                .replaceAll("\\<p.*?>", "EndLine").replaceAll("\\<.*?>", EMPTY_STRING).replaceAll("&nbsp;", "&#160;")
                .split("EndLine");
        return replacedString;
    }

    public static String replaceHtmlToTextBreakEveryTagToNewLine(String HTMLString) {
        String text_new_line = "\r\n";
        String replacedString = new String(HTMLString);

        replacedString = replacedString.replaceAll("[\\s\\t]+", " ");
        replacedString = replacedString.replaceAll("\\<(/).*?>", text_new_line);
        replacedString = replacedString.replaceAll("\\<.*li.*>", "- ");
        replacedString = replacedString.replaceAll("\\<.*?>", EMPTY_STRING);
        return trimAndNullIsEmpty(replacedString);
    }

    public static String[] removeAndUnEscapeHtmlTagExceptBreak(String HTMLString) {
        String[] replacedString = StringEscapeUtils
                .unescapeHtml(HTMLString.replaceAll("\\r|\\n", " ").replaceAll("\\<br.*?/>", "EndLine")
                        .replaceAll("\\<.*?>", EMPTY_STRING))
                .replaceAll("&nbsp;", "&#160;").replaceAll("\\s&\\s", " &amp; ").split("EndLine");
        return replacedString;
    }

    public static String[] removeHtmlTagExceptBreak(String HTMLString) {
        HTMLString = StringEscapeUtils.escapeHtml(StringEscapeUtils.unescapeHtml(HTMLString));
        String[] replacedString = HTMLString.replaceAll("\\r|\\n", " ").replaceAll("\\<br.*?/>", "EndLine")
                .replaceAll("\\<.*?>", EMPTY_STRING).replaceAll("&nbsp;", "&#160;").split("EndLine");
        return replacedString;
    }

    public static List<String> sortStringsList(List<String> strings) {
        if (strings != null && strings.size() > 1) {
            for (int counter = 0; counter < strings.size() - 1; counter++) { // Loop
                for (int index = 0; index < strings.size() - 1 - counter; index++) { // Once
                    if (strings.get(index).compareTo(strings.get(index + 1)) > 1) { // Test
                        String temp = strings.get(index);
                        strings.set(index, (strings.get(index + 1)));
                        strings.set(index + 1, temp);
                    }
                }
            }
        }
        return strings;

    }

    public static String getEscapedDoubleQuoteString(final String originalString) {
        if (originalString == null) {
            return null;
        }
        StringBuilder escapedString = new StringBuilder(originalString);
        int i = escapedString.indexOf("\"");
        while (i != -1) {
            escapedString.insert(i, "\\");
            i = escapedString.indexOf("\"", i + 2);
        }
        return escapedString.toString();
    }

    public static String getMaskedString(String origStr) {
        if (origStr == null) {
            return "";
        }
        return getMaskedString(origStr, origStr.length(), 'X', true);
    }

    public static String getMaskedString(String origStr, int length, char c, boolean fromLeft) {
        if (origStr == null) {
            return "";
        }
        StringBuilder sb = new StringBuilder();
        if (origStr.length() >= length) {
            int i = 0;
            if (fromLeft) {
                for (; i < length; i++) {
                    sb.append(c);
                }
                for (; i < origStr.length(); i++) {
                    sb.append(origStr.charAt(i));
                }
            } else {
                for (; i < origStr.length() - length; i++) {
                    sb.append(origStr.charAt(i));
                }
                for (; i < origStr.length(); i++) {
                    sb.append(c);
                }
            }
        }
        return sb.toString();
    }

    public static String padCCExceptLastFour(String cardNumber, char c) {
        StringBuilder sb = new StringBuilder();
        if (cardNumber != null && cardNumber.length() > 4) {
            int len = cardNumber.length();
            for (int i = 0; i < (len - 4); ++i) {
                sb.append(c);
            }
            sb.append(cardNumber.substring(len - 4));
        }
        return sb.toString();
    }

    public static String breakWordIfLongerThanLimit(String origString, int maxLimit) {
        if (origString == null) {
            return origString;
        }

        String[] words = origString.split("\\s");
        for (int i = 0; i < words.length; i++) {
            String word = words[i];
            int insertLoc = maxLimit;
            int length = word.length();
            while (length > maxLimit) {
                word = new StringBuilder(word).insert(insertLoc, " ").toString();
                length -= (maxLimit);
                insertLoc += (maxLimit + 1);
                System.out.print("\nword = " + word + "length = " + length);
            }

            words[i] = word;
        }
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < words.length; i++) {
            if (i > 0 && words[i].charAt(words[i].length() - 1) != ' ') {
                sb.append(" ");
            }
            sb.append(words[i]);
        }
        return sb.toString();
    }

    public static boolean isNull(String str) {
        if (str == null || str.equals("null")) {
            return true;
        }
        return false;
    }

    public static void main(String[] args) throws UnsupportedEncodingException {

    }

    public static String trimNoSpecialSizeAndNullIsDefaultStripAmp(String input, int length, String defVal) {
        String val = trimNoSpecialSizeAndNullIsDefault(input, length, defVal);
        if (val != null) {
            val = val.replaceAll("&", "");
        }
        return val;
    }

    public static boolean isSetProperty(String keyDirectory) {
        if (StringUtility.trimAndEmptyIsNull(keyDirectory) == null) {
            return false;
        }
        if (keyDirectory.startsWith("@")) {
            return false;
        }
        return keyDirectory.length() > 0;
    }

    public static String convertBytesToString(byte[] bytesArray) throws Exception {
        if (bytesArray == null) {
            return null;
        }
        return ((bytesArray.length == 0) ? "" : (new String(bytesArray, "UTF-8")));
    }

    public static String getBytesDecimalValuesAsString(byte[] bytesArray) throws Exception {
        if (bytesArray == null) {
            return null;
        }
        String decimalValues = new String("");
        for (int index = 0; index < bytesArray.length; index++) {
            decimalValues += " " + Integer.toString(bytesArray[index]);
        }
        return decimalValues;
    }

    public static StringBuilder mapToString(Map<String, String> map) {
        if (map == null || map.entrySet().size() == 0) {
            return null;
        }
        StringBuilder stringBuilder = new StringBuilder();
        for (String key : map.keySet()) {
            if (stringBuilder.length() > 0) {
                stringBuilder.append("&");
            }
            String value = map.get(key);
            try {
                stringBuilder.append((key != null ? URLEncoder.encode(key, "UTF-8") : ""));
                stringBuilder.append("=");
                stringBuilder.append(value != null ? URLEncoder.encode(value, "UTF-8") : "");
            } catch (UnsupportedEncodingException e) {
                throw new RuntimeException("This method requires UTF-8 encoding support", e);
            }
        }
        return stringBuilder;
    }

    public static Map<String, String> stringToMap(String input) {
        if (input == null || input.length() == 0) {
            return null;
        }
        Map<String, String> map = new HashMap<String, String>();
        String[] nameValuePairs = input.split("[&;,]", -1);
        for (String nameValuePair : nameValuePairs) {
            String[] nameValue = nameValuePair.split("=", -1);
            try {
                map.put(URLDecoder.decode(nameValue[0], "UTF-8"),
                        nameValue.length > 1 ? URLDecoder.decode(nameValue[1], "UTF-8") : "");
            } catch (UnsupportedEncodingException e) {
                throw new RuntimeException("This method requires UTF-8 encoding support", e);
            }
        }
        return map;
    }

    public static String encodeForLDAP(String value) {
        if (value == null) {
            return null;
        }
        value = ESAPI.encoder().encodeForLDAP(value);
        return value;
    }

    public static boolean isValidMobileNo(String mobile) {
        // validate phone numbers of format "1234567890"
        if (mobile.matches("\\d{10}"))
            return true;
        // validating phone number with -, . or spaces
        else if (mobile.matches("\\d{3}[-\\.\\s]\\d{3}[-\\.\\s]\\d{4}"))
            return true;
        // validating phone number with extension length from 3 to 5
        else if (mobile.matches("\\d{3}-\\d{3}-\\d{4}\\s(x|(ext))\\d{3,5}"))
            return true;
        // validating phone number where area code is in braces ()
        else if (mobile.matches("\\(\\d{3}\\)-\\d{3}-\\d{4}"))
            return true;
        // return false if nothing matches the input
        else
            return false;
    }

    public static String toCamelCase(String value) {
        String result = "";
        String input = StringUtility.trimAndEmptyIsNull(value);
        if (input == null) {
            return null;
        }
        char firstChar = input.charAt(0);
        char firstCharToUpperCase = Character.toUpperCase(firstChar);
        result = result + firstCharToUpperCase;
        for (int i = 1; i < input.length(); i++) {
            char currentChar = input.charAt(i);
            char previousChar = input.charAt(i - 1);
            if (previousChar == ' ') {
                char currentCharToUpperCase = Character.toUpperCase(currentChar);
                result = result + currentCharToUpperCase;
            } else {
                char currentCharToLowerCase = Character.toLowerCase(currentChar);
                result = result + currentCharToLowerCase;
            }
        }
        return result;
    }

    public static String removeChar(String s, char c) {
        StringBuffer buf = new StringBuffer(s.length());
        buf.setLength(s.length() - 1);
        int current = 0;
        for (int i = 0; i < s.length(); i++) {
            char cur = s.charAt(i);
            if (cur != c)
                buf.setCharAt(current++, cur);
        }
        return buf.toString();
    }

    /**
     * This api converts the html coded spaces (%20) to the space character
     * recognised by Java.
     * 
     * @param string
     * @return
     */
    public static String convertHTMLSpaces(final String string) {
        String convertedString = string;
        if (convertedString == null) {
            return "";
        }
        return convertedString.replaceAll("%20", " ");
    }

    public static String trimAndNullIsDefault(String str, String def) {
        if (str == null) {
            return def;
        }

        try {
            str = str.trim();
            if (str.isEmpty()) {
                return def;
            }
            return str;
        } catch (Exception e) {
            return def;
        }
    }

    public static String getTabContent(int tabLevel) {
        StringBuilder tabContent = new StringBuilder();
        while (tabLevel > 0) {
            tabContent.append("\t");
            tabLevel--;
        }

        String tab = tabContent.toString();
        return tab;
    }
}
