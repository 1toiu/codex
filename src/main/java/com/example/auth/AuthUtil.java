package com.example.auth;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

public final class AuthUtil {
    public static final String LOGIN_USER = "loginUser";
    public static final String ROLE = "role";
    public static final String ADMIN = "admin";
    public static final String USER = "user";

    private AuthUtil() {
    }

    public static boolean isLoggedIn(HttpServletRequest request) {
        HttpSession session = request.getSession(false);
        return session != null && session.getAttribute(LOGIN_USER) != null;
    }

    public static boolean isAdmin(HttpServletRequest request) {
        HttpSession session = request.getSession(false);
        return session != null && ADMIN.equals(session.getAttribute(ROLE));
    }
}
