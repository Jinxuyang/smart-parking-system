package com.verge.parking.common;

public class UserContextHolder {
    private static final ThreadLocal<LoginInfo> USER_INFO = new ThreadLocal<>();

    public static void setUserInfo(LoginInfo info) {
        USER_INFO.set(info);
    }

    public static void clear() {
        USER_INFO.remove();
    }

    public static LoginInfo getLoginInfo() {
        return USER_INFO.get();
    }
}
