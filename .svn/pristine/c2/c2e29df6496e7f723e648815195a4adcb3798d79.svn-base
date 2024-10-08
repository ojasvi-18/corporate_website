package com.zillious.corporate_website.utils;

import java.util.Collection;
import java.util.List;

public class NumberUtility {

    public static char baseNChar(long value) {
        if (value < 10) {
            return (char) ('0' + value);
        }
        return ((char) ('A' + value - 10));
    }

    public static String baseNString(long Id, int base) {
        String baseNString = "";
        while (Id > 0) {
            long rem = Id % base;
            Id = Id / base;
            baseNString = baseNChar(rem) + baseNString;
        }
        return baseNString;
    }

    public static int base10Value(char c) {
        if (c >= '0' && c <= '9') {
            return c - '0';
        }
        return c - 'A' + 10;
    }

    public static long base10LongValue(String str, int base) {
        long ret = 0;
        for (int i = 0; i < str.length(); i++) {
            char c = str.charAt(i);
            ret = (ret * base) + base10Value(c);
        }
        return ret;
    }

    public static int base10Value(String str, int base) {
        int ret = 0;
        for (int i = 0; i < str.length(); i++) {
            char c = str.charAt(i);
            ret = (ret * base) + base10Value(c);
        }
        return ret;
    }

    public static int parsetIntWithDefaultOnErr(String val, int defaultVal) {
        try {
            return Integer.parseInt(val);
        } catch (Exception e) {
            return defaultVal;
        }
    }

    public static long parsetLongWithDefaultOnErr(String val, long defaultVal) {
        try {
            return Long.parseLong(val);
        } catch (Exception e) {
            return defaultVal;
        }
    }

    public static double parsetDoubleWithDefaultOnErr(String val, double defaultVal) {
        try {
            return Double.parseDouble(val);
        } catch (Exception e) {
            return defaultVal;
        }
    }

    public static double parsePositiveDoubleWithDefaultOnErr(String val, double defaultVal) {
        try {
            Double value = Double.parseDouble(val);
            return (value > 0 ? value : defaultVal);
        } catch (Exception e) {
            return defaultVal;
        }
    }

    public static int parsetDoubleWithRoundAndDefaultOnErr(String val, int defaultVal) {
        try {
            return (int) Math.round(Double.parseDouble(val));
        } catch (Exception e) {
            return defaultVal;
        }
    }

    public static int[] toArray(Collection<Integer> col, int nullValue) {
        if (col == null) {
            return null;
        }
        int[] res = new int[col.size()];
        int cnt = 0;
        for (Integer i : col) {
            res[cnt++] = (i == null) ? nullValue : i.intValue();
        }
        return res;
    }

    public static boolean equalsWithBothNullCheck(Double num1, Double num2) {
        if (num1 == null && num2 == null) {
            return true;
        }
        if (num1 == null) {
            return false;
        }
        if (num2 == null) {
            return false;
        }
        return num1.doubleValue() == num2.doubleValue();
    }

    public static boolean equalsWithBothNullCheck(Integer num1, Integer num2) {
        if (num1 == null && num2 == null) {
            return true;
        }
        if (num1 == null) {
            return false;
        }
        if (num2 == null) {
            return false;
        }
        return num1.intValue() == num2.intValue();
    }

    public static int getValueWithDefault(int trSearchQueryIdxFromReq, int def) {
        return trSearchQueryIdxFromReq > 0 ? trSearchQueryIdxFromReq : def;
    }

    public static int getRandomInt(List<Integer> numbers) {
        int random = (int) Math.ceil(Math.random() * (numbers.size() - 1));
        return numbers.get(random);
    }
}
