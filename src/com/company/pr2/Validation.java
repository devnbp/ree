package com.company.pr2;

import java.util.Arrays;

public class Validation {
    /**
     * Checks if the actual value greater than given or equals to
     * @param actual value to be checked
     * @param value minimum value
     * @param include equal checking if is true
     * @return logical result of validation
     */
    public static boolean min(double actual, double value, boolean include) {
        if (actual > value) {
            return true;
        }

        if (include) {
            return actual == value;
        }

        return false;
    }

    /**
     * Checks if the actual value lesser than given or equals to
     * @param actual value to be checked
     * @param value maximum value
     * @param include equal checking if is true
     * @return logical result of validation
     */
    public static boolean max(double actual, double value, boolean include) {
        if (actual < value) {
            return true;
        }

        if (include) {
            return actual == value;
        }

        return false;
    }

    /**
     * Checks if the string length greater than given value or equals to
     * @param str string
     * @param value minimum length
     * @param include equal checking if is true
     * @return logical result of validation
     */
    public static boolean minLength(String str, int value, boolean include) {
        return min(str.length(), value, include);
    }

    /**
     * Checks if the string length lesser than given value or equals to
     * @param str string
     * @param value maximum length
     * @param include equal checking if is true
     * @return logical result of validation
     */
    public static boolean maxLength(String str, int value, boolean include) {
        return max(str.length(), value, include);
    }

    /**
     * Checks if the enum value lesser than given value or equals to
     * @param item enum value
     * @param values enum values
     * @param include -
     * @return logical result of validation
     */
    public static boolean inEnum(Enum item, Enum[] values, boolean include) {
        return Arrays.asList(values).contains(item);
    }
}
