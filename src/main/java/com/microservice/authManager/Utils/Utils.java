package com.microservice.authManager.Utils;

import javax.servlet.http.HttpServletRequest;

public class Utils {

    public static String extractTokenFromHeader(HttpServletRequest request) {
        String header = request.getHeader("Authorization");
        if (header != null && header.startsWith("Bearer ")) {
            return header.substring(7);
        }
        return null;
    }
}
