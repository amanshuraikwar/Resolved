package com.sonu.resolved.utils;

/**
 * Created by sonu on 5/3/17.
 */

public class Checker {
    private static final String EMPTY_MESSAGE = "this is required";
    public static String username(String value) {
        if(value == null) {
            return EMPTY_MESSAGE;
        }

        if(value.equals("")) {
            return EMPTY_MESSAGE;
        }

        return null;
    }

    public static String email(String value) {
        if(value == null) {
            return EMPTY_MESSAGE;
        }

        if(value.equals("")) {
            return EMPTY_MESSAGE;
        }

        return null;
    }

    public static String password(String value) {
        if(value == null) {
            return EMPTY_MESSAGE;
        }

        if(value.equals("")) {
            return EMPTY_MESSAGE;
        }

        return null;
    }

    public static String problemTitle(String value) {
        if(value == null) {
            return EMPTY_MESSAGE;
        }

        if(value.equals("")) {
            return EMPTY_MESSAGE;
        }

        return null;
    }

    public static String problemDescription(String value) {
        if(value == null) {
            return EMPTY_MESSAGE;
        }

        if(value.equals("")) {
            return EMPTY_MESSAGE;
        }

        return null;
    }
}

