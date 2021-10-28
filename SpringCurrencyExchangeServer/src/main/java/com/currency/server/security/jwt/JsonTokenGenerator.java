package com.currency.server.security.jwt;

import java.util.Date;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.UnsupportedJwtException;

@Component
public class JsonTokenGenerator {
    private static final Logger logger = LoggerFactory.getLogger(JsonTokenGenerator.class);

    @Value("${settings.jsonTokenSecret}")
    private String tokenSecret;

    @Value("#{${settings.jsonTokenExpirationHours} * 1000 * 60 * 60 }")
    private int tokenExpiration;

    public String generateJsonToken(Authentication authentication) {
        UserDetails userPrincipal = (UserDetails) authentication.getPrincipal();
        return generateJsonToken(userPrincipal, new Date());
    }

    public String generateJsonToken(
            UserDetails userPrincipal,
            Date now) {

        return Jwts.builder()
                .setSubject((userPrincipal.getUsername()))
                .setIssuedAt(now)
                .setExpiration(new Date(now.getTime() + tokenExpiration))
                .signWith(SignatureAlgorithm.HS512, tokenSecret)
                .compact();
    }

    public String getUserNameFromJsonToken(String token) {
        return Jwts.parser().setSigningKey(tokenSecret).parseClaimsJws(token).getBody().getSubject();
    }

    public boolean validateJsonToken(String authToken) {
        try {
            Jwts.parser().setSigningKey(tokenSecret).parseClaimsJws(authToken);
            return true;
        } catch (MalformedJwtException e) {
            logger.error("Invalid json token: {} \n {}", authToken, e.getMessage());
        } catch (ExpiredJwtException e) {
            logger.error("Expired json token: {} \n {}", authToken, e.getMessage());
        } catch (UnsupportedJwtException e) {
            logger.error("Unsupported json token: {} \n {}", authToken, e.getMessage());
        } catch (IllegalArgumentException e) {
            logger.error("Json token empty claims string: {} \n {}", authToken, e.getMessage());
        }

        return false;
    }
}