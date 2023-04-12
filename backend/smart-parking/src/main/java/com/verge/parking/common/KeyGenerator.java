package com.verge.parking.common;

public class KeyGenerator {
    // generate 10 random string
    public static String generate() {
        String str = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789";
        StringBuilder sb = new StringBuilder(10);
        for (int i = 0; i < 10; i++) {
            char ch = str.charAt((int) (Math.random() * str.length()));
            sb.append(ch);
        }
        return sb.toString();
    }
}
