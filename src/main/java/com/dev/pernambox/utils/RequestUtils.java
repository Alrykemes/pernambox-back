package com.dev.pernambox.utils;

import jakarta.servlet.http.HttpServletRequest;

public class RequestUtils {

    public static String getRequestIp(HttpServletRequest request) {
        String ip = request.getHeader("X-Forwarded-For");
        if (ip != null && !ip.isEmpty() && !"unknown".equalsIgnoreCase(ip)) {
            // Se tiver múltiplos IPs no header, pega o primeiro
            return ip.split(",")[0];
        }

        ip = request.getHeader("Proxy-Client-IP");
        if (ip != null && !ip.isEmpty() && !"unknown".equalsIgnoreCase(ip)) {
            return ip;
        }

        ip = request.getHeader("WL-Proxy-Client-IP");
        if (ip != null && !ip.isEmpty() && !"unknown".equalsIgnoreCase(ip)) {
            return ip;
        }

        ip = request.getRemoteAddr();
        return ip;
    }

    public static String getRequestUserAgent(HttpServletRequest request) {
        return request.getHeader("User-Agent");
    }
}