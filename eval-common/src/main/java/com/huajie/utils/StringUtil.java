package com.huajie.utils;


public class StringUtil {

    public static boolean isNotEmpty(Object str) {
        str = String.valueOf(str);
        if (null != str && (!"".equals(str)) && (!"null".equals(str))) {
            return true;
        } else {
            return false;
        }
    }

    public static boolean isEmpty(Object str) {
        return !isNotEmpty(str);
    }

    public static String firstCharToUpperCase(String name) {
        return name.substring(0, 1).toUpperCase() + name.substring(1);
    }

    public static int getIndexByCharAndCnt(String str, String chr, int cnt) {
        int index = chr.length();
        for (int i = 0; i < cnt; i++) {
            int indexNew = str.indexOf(chr);
            if (indexNew < 0) {
                return -1;
            }
            index += indexNew;
            str = str.substring(indexNew + chr.length());
        }
        return index;
    }

}
