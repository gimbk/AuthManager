package com.microservice.authManager.Security.jwt;

import com.microservice.authManager.ServiceImpl.UserDetailsImpl;
import io.jsonwebtoken.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Date;

@Component
public class JwtUtils {
    private static final Logger logger = LoggerFactory.getLogger(JwtUtils.class);

    @Value("${ims.app.jwtSecret}")
    private String jwtSecret;

    @Value("${ims.app.jwtExpirationMs}")
    private int jwtExpirationMs;

    public String generateJwtToken(Authentication authentication) {

        UserDetailsImpl userPrincipal = (UserDetailsImpl) authentication.getPrincipal();

        return Jwts.builder()
                .setSubject((userPrincipal.getUsername()))
                .setIssuedAt(new Date())
                .setExpiration(new Date((new Date()).getTime() + jwtExpirationMs))
                .signWith(SignatureAlgorithm.HS512, jwtSecret)
                .compact();
    }

    public String getUserNameFromJwtToken(String token) {
        return Jwts.parser().setSigningKey(jwtSecret).parseClaimsJws(token).getBody().getSubject();
    }

    public String validateJwtToken(String authToken, HttpServletResponse response) throws IOException {
        String returnMessage;
        try {
            Jwts.parser().setSigningKey(jwtSecret).parseClaimsJws(authToken);
            return "";
        } catch (SignatureException e) {
            logger.error("Invalid JWT signature: {}", e.getMessage());
            //response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "Invalid JWT signature");
            returnMessage = "Invalid JWT signature";

        } catch (MalformedJwtException e) {
            logger.error("Invalid JWT token: {}", e.getMessage());
            returnMessage = "Invalid JWT token";
            //response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "Invalid JWT token");
        } catch (ExpiredJwtException e) {
            logger.error("JWT token is expired: {}", e.getMessage());
            //response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "JWT token is expired");
            returnMessage = "JWT token is expired";
        } catch (UnsupportedJwtException e) {
            logger.error("JWT token is unsupported: {}", e.getMessage());
            //response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "JWT token is unsupported");
            returnMessage = "JWT token is unsupported";
        } catch (IllegalArgumentException e) {
            logger.error("JWT claims string is empty: {}", e.getMessage());
            //response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "JWT claims string is empty");
            returnMessage = "JWT claims string is empty";
        }

        return returnMessage;
    }

}
