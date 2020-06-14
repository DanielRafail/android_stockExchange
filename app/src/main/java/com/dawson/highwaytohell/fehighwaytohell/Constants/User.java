package com.dawson.highwaytohell.fehighwaytohell.Constants;

public class User {

    private User(){};

    private static boolean isLogged = false;
    private static String token = null;

    public static boolean isLogged() {
        return isLogged;
    }

    public static void setLogged(boolean logged) {
        isLogged = logged;
    }

    public static String getToken() {
        return token;
    }

    public static void setToken(String token) {
        User.token = token;
    }
}
