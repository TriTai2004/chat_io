package com.chatio.socket.utils;

import java.util.Arrays;

import org.springframework.stereotype.Component;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;

@Component
public class CookieUtil {

    public String getAccessToken(HttpServletRequest request) {
        return getCookieValue(request, "accessToken");
    }

    public String getRefreshToken(HttpServletRequest request) {
        return getCookieValue(request, "refreshToken");
    }

    private String getCookieValue(
            HttpServletRequest request,
            String cookieName) {

        Cookie[] cookies = request.getCookies();

        if (cookies == null) {
            return null;
        }

        return Arrays.stream(cookies)
                .filter(cookie -> cookieName.equals(cookie.getName()))
                .map(Cookie::getValue)
                .findFirst()
                .orElse(null);
    }
}
