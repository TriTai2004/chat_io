package com.chatio.socket.utils;

import java.util.Arrays;

import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
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

    public String getAccessToken(StompHeaderAccessor accessor) {
        return getCookieValue(accessor, "accessToken");
    }

    public String getRefreshToken(StompHeaderAccessor accessor) {
        return getCookieValue(accessor, "refreshToken");
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

    private String getCookieValue(
            StompHeaderAccessor accessor,
            String cookieName) {

        String cookieHeader =
                accessor.getFirstNativeHeader("cookie");

        if (cookieHeader == null) {
            return null;
        }

        return Arrays.stream(cookieHeader.split(";"))
                .map(String::trim)
                .map(cookie -> cookie.split("=", 2))
                .filter(parts -> parts.length == 2)
                .filter(parts -> cookieName.equals(parts[0]))
                .map(parts -> parts[1])
                .findFirst()
                .orElse(null);
    }
}