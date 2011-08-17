package com.mscg.appstarter.util;

import java.util.Collection;

public class Util {

    public static boolean isEmpty(String string) {
        return string == null || string.length() == 0;
    }

    public static boolean isEmptyOrWhitespaceOnly(String string) {
        return string == null || isEmpty(string.trim());
    }

    public static boolean isNotEmpty(String string) {
        return !isEmpty(string);
    }

    public static boolean isNotEmptyOrWhitespaceOnly(String string) {
        return !isEmptyOrWhitespaceOnly(string);
    }

    public static boolean isEmpty(Collection collection) {
        return collection == null || collection.isEmpty();
    }

}
